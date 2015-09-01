/*******************************************************************************
 * Copyright (C) 2015 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.egit.core.op.MergeOperation;
import org.eclipse.emf.compare.egit.internal.merge.DirCacheResourceVariantTreeProvider;
import org.junit.Test;

@SuppressWarnings("restriction")
public class DirCacheResourceVariantTreeProviderTest extends VariantsTestCase {
	@Test
	public void testDirCacheAddToIndex() throws Exception {
		File file1 = repository.createFile(iProject, "file1"); //$NON-NLS-1$
		IFile iFile1 = repository.getIFile(iProject, file1);

		repository.appendFileContent(file1, INITIAL_CONTENT_1);

		// untracked file : not part of the index
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repo, true);
		assertTrue(treeProvider.getKnownResources().isEmpty());
		assertFalse(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertFalse(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertFalse(treeProvider.getRemoteTree().hasResourceVariant(iFile1));

		repository.addToIndex(iFile1);

		// We now have a stage 0, present in each tree
		treeProvider = new DirCacheResourceVariantTreeProvider(repo, true);
		assertEquals(1, treeProvider.getKnownResources().size());
		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
	}

	@Test
	public void testDirCacheTreesNoConflict() throws Exception {
		File file1 = repository.createFile(iProject, "file1"); //$NON-NLS-1$
		File file2 = repository.createFile(iProject, "file2"); //$NON-NLS-1$

		repository.appendContentAndCommit(iProject, file1, INITIAL_CONTENT_1, "first file - initial commit"); //$NON-NLS-1$
		repository.appendContentAndCommit(iProject, file2, INITIAL_CONTENT_2, "second file - initial commit"); //$NON-NLS-1$

		IFile iFile1 = repository.getIFile(iProject, file1);
		IFile iFile2 = repository.getIFile(iProject, file2);

		repository.createAndCheckoutBranch(MASTER, BRANCH);

		final String branchChanges = "branch changes\n"; //$NON-NLS-1$
		setContentsAndCommit(repository, iFile2, branchChanges + INITIAL_CONTENT_2, "branch commit"); //$NON-NLS-1$

		repository.checkoutBranch(MASTER);

		final String masterChanges = "\nsome changes"; //$NON-NLS-1$
		setContentsAndCommit(repository, iFile1, INITIAL_CONTENT_1 + masterChanges, "master commit"); //$NON-NLS-1$
		iProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		// end setup

		// try and merge the branch into master
		new MergeOperation(repo, BRANCH).execute(null);

		// no conflict on either file : present in the trees anyway
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repo, true);
		assertEquals(2, treeProvider.getKnownResources().size());

		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile2));
	}

	@Test
	public void testDirCacheTreesConflictOnOne() throws Exception {
		File file1 = repository.createFile(iProject, "file1"); //$NON-NLS-1$
		File file2 = repository.createFile(iProject, "file2"); //$NON-NLS-1$

		repository.appendContentAndCommit(iProject, file1, INITIAL_CONTENT_1, "first file - initial commit"); //$NON-NLS-1$
		repository.appendContentAndCommit(iProject, file2, INITIAL_CONTENT_2, "second file - initial commit"); //$NON-NLS-1$

		IFile iFile1 = repository.getIFile(iProject, file1);
		IFile iFile2 = repository.getIFile(iProject, file2);

		repository.createAndCheckoutBranch(MASTER, BRANCH);

		final String branchChanges = "branch changes\n"; //$NON-NLS-1$
		setContentsAndCommit(repository, iFile1, branchChanges + INITIAL_CONTENT_1, "branch commit"); //$NON-NLS-1$
		setContentsAndCommit(repository, iFile2, branchChanges + INITIAL_CONTENT_2, "branch commit"); //$NON-NLS-1$

		repository.checkoutBranch(MASTER);

		final String masterChanges = "\nsome changes"; //$NON-NLS-1$
		setContentsAndCommit(repository, iFile1, INITIAL_CONTENT_1 + masterChanges, "master commit"); //$NON-NLS-1$
		iProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		// end setup

		// try and merge the branch into master
		new MergeOperation(repo, BRANCH).execute(null);

		// conflict on file 1 : present in all three trees
		// no conflict on file 2 : present anyway
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repo, true);
		assertTrue(treeProvider.getKnownResources().contains(iFile1));
		assertTrue(treeProvider.getKnownResources().contains(iFile2));

		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile2));
	}

	@Test
	public void testDirCacheTreesConflict() throws Exception {
		File file1 = repository.createFile(iProject, "file1"); //$NON-NLS-1$
		File file2 = repository.createFile(iProject, "file2"); //$NON-NLS-1$

		repository.appendContentAndCommit(iProject, file1, INITIAL_CONTENT_1, "first file - initial commit"); //$NON-NLS-1$

		IFile iFile1 = repository.getIFile(iProject, file1);

		repository.createAndCheckoutBranch(MASTER, BRANCH);

		final String branchChanges = "branch changes\n"; //$NON-NLS-1$
		setContentsAndCommit(repository, iFile1, branchChanges + INITIAL_CONTENT_1, "branch commit"); //$NON-NLS-1$
		repository.appendContentAndCommit(iProject, file2, INITIAL_CONTENT_2 + "branch", //$NON-NLS-1$
				"second file - initial commit - branch"); //$NON-NLS-1$

		repository.checkoutBranch(MASTER);

		final String masterChanges = "some changes\n"; //$NON-NLS-1$
		setContentsAndCommit(repository, iFile1, INITIAL_CONTENT_1 + masterChanges, "master commit - file1"); //$NON-NLS-1$
		repository.appendContentAndCommit(iProject, file2, INITIAL_CONTENT_2 + "master", //$NON-NLS-1$
				"second file - initial commit - master"); //$NON-NLS-1$
		IFile iFile2 = repository.getIFile(iProject, file2);
		iProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		// end setup

		// try and merge the branch into master
		new MergeOperation(repo, BRANCH).execute(null);

		// conflict on file 1 : file 1 has three stages.
		// conflict on file 2, but was not in the base : only stage 2 and 3
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repo, true);
		assertTrue(treeProvider.getKnownResources().contains(iFile1));
		assertTrue(treeProvider.getKnownResources().contains(iFile2));

		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertFalse(treeProvider.getBaseTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile2));
	}
}
