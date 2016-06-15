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

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.egit.internal.merge.DirCacheResourceVariantTreeProvider;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitTest;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Repository;
import org.junit.runner.RunWith;

@RunWith(GitTestRunner.class)
@SuppressWarnings("unused")
public class DirCacheResourceVariantTreeProviderTest {

	@GitTest
	@GitInput("data/dirCacheResourceVariantTreeProvider/dirCacheAddToIndex.zip")
	public void testDirCacheAddToIndex(Status status, Repository repository, List<IProject> projects)
			throws Exception {
		IProject project = projects.get(0);
		IFile iFile1 = project.getFile("file1"); //$NON-NLS-1$

		// untracked file : not part of the index
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repository,
				true);
		assertTrue(treeProvider.getKnownResources().isEmpty());
		assertFalse(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertFalse(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertFalse(treeProvider.getRemoteTree().hasResourceVariant(iFile1));

		iFile1.toString();
		addToIndex(repository, iFile1);

		// We now have a stage 0, present in each tree
		treeProvider = new DirCacheResourceVariantTreeProvider(repository, true);
		assertEquals(1, treeProvider.getKnownResources().size());
		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
	}

	@GitMerge(localBranch = "master", remoteBranch = "branch")
	@GitInput("data/dirCacheResourceVariantTreeProvider/dirCacheTreesNoConflict.zip")
	public void testDirCacheTreesNoConflict(Status status, Repository repository, List<IProject> projects)
			throws Exception {
		IProject iProject = projects.get(0);
		IFile iFile1 = iProject.getFile("file1"); //$NON-NLS-1$
		IFile iFile2 = iProject.getFile("file2"); //$NON-NLS-1$

		// no conflict on either file : present in the trees anyway
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repository,
				true);
		assertEquals(3, treeProvider.getKnownResources().size());

		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile2));
	}

	@GitMerge(localBranch = "master", remoteBranch = "branch")
	@GitInput("data/dirCacheResourceVariantTreeProvider/dirCacheTreesConflictOnOne.zip")
	public void testDirCacheTreesConflictOnOne(Status status, Repository repository, List<IProject> projects)
			throws Exception {
		IProject iProject = projects.get(0);
		IFile iFile1 = iProject.getFile("file1"); //$NON-NLS-1$
		IFile iFile2 = iProject.getFile("file2"); //$NON-NLS-1$

		// conflict on file 1 : present in all three trees
		// no conflict on file 2 : present anyway
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repository,
				true);
		assertTrue(treeProvider.getKnownResources().contains(iFile1));
		assertTrue(treeProvider.getKnownResources().contains(iFile2));

		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile2));
	}

	@GitMerge(localBranch = "master", remoteBranch = "branch")
	@GitInput("data/dirCacheResourceVariantTreeProvider/dirCacheTreesConflict.zip")
	public void testDirCacheTreesConflict(Status status, Repository repository, List<IProject> projects)
			throws Exception {
		IProject iProject = projects.get(0);
		IFile iFile1 = iProject.getFile("file1"); //$NON-NLS-1$
		IFile iFile2 = iProject.getFile("file2"); //$NON-NLS-1$

		// conflict on file 1 : file 1 has three stages.
		// conflict on file 2, but was not in the base : only stage 2 and 3
		DirCacheResourceVariantTreeProvider treeProvider = new DirCacheResourceVariantTreeProvider(repository,
				true);
		assertTrue(treeProvider.getKnownResources().contains(iFile1));
		assertTrue(treeProvider.getKnownResources().contains(iFile2));

		assertTrue(treeProvider.getBaseTree().hasResourceVariant(iFile1));
		assertFalse(treeProvider.getBaseTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getSourceTree().hasResourceVariant(iFile2));

		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile1));
		assertTrue(treeProvider.getRemoteTree().hasResourceVariant(iFile2));
	}

	private void addToIndex(Repository repository, IFile file)
			throws CoreException, IOException, NoFilepatternException, GitAPIException {
		String filePath = file.getProject().getName() + "/" + file.getProjectRelativePath(); //$NON-NLS-1$
		Git git = new Git(repository);
		try {
			git.add().addFilepattern(filePath).call();
		} finally {
			git.close();
		}
	}

	private String getRepoRelativePath(String repoPath, String path) {
		final int pfxLen = repoPath.length();
		final int pLen = path.length();
		if (pLen > pfxLen) {
			return path.substring(pfxLen);
		} else if (path.length() == pfxLen - 1) {
			return ""; //$NON-NLS-1$
		}
		return null;
	}

}
