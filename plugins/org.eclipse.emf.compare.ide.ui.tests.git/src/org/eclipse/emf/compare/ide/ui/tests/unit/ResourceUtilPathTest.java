/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

public class ResourceUtilPathTest extends AbstractURITest {
	@Test
	public void testFixedPath_git_straightRepo() throws Exception {
		setupStraightRepo();

		RevCommit masterTip = repository.findCommit(MASTER);
		RevCommit branchTip = repository.findCommit(BRANCH);
		RevCommit origin = masterTip.getParent(0);

		IStorageProviderAccessor accessor = createAccessorForComparison(MASTER, BRANCH, false);
		final String workspaceRelativePath = iFile1.getFullPath().toOSString();

		IStorageProvider provider = accessor.getStorageProvider(iFile1, DiffSide.ORIGIN);
		IStorage storage = provider.getStorage(new NullProgressMonitor());
		String storagePath = storage.getFullPath().toOSString();
		// The path contains our iFile's workspace-relative path
		assertTrue(storagePath.contains(workspaceRelativePath));
		// But it doesn't start with it (repository name is the first segment)
		assertFalse(storagePath.startsWith(iFile1.getFullPath().toOSString()));
		// And it contains the commit's id at the end
		assertTrue(storagePath.endsWith(origin.getId().abbreviate(6).name()));

		// while we want an actually useable path
		String fixedPath = ResourceUtil.getFixedPath(storage).toOSString();
		assertEquals(workspaceRelativePath, fixedPath);

		// Likewise for the remote side
		provider = accessor.getStorageProvider(iFile1, DiffSide.REMOTE);
		storage = provider.getStorage(new NullProgressMonitor());
		storagePath = storage.getFullPath().toOSString();
		assertTrue(storagePath.contains(workspaceRelativePath));
		assertFalse(storagePath.startsWith(iFile1.getFullPath().toOSString()));
		assertTrue(storagePath.endsWith(branchTip.getId().abbreviate(6).name()));

		fixedPath = ResourceUtil.getFixedPath(storage).toOSString();
		assertEquals(workspaceRelativePath, fixedPath);

		// and the source side
		provider = accessor.getStorageProvider(iFile1, DiffSide.SOURCE);
		storage = provider.getStorage(new NullProgressMonitor());
		storagePath = storage.getFullPath().toOSString();
		assertTrue(storagePath.contains(workspaceRelativePath));
		assertFalse(storagePath.startsWith(iFile1.getFullPath().toOSString()));
		assertTrue(storagePath.endsWith(masterTip.getId().abbreviate(6).name()));

		fixedPath = ResourceUtil.getFixedPath(storage).toOSString();
		assertEquals(workspaceRelativePath, fixedPath);
	}

	@Test
	public void testFixedPath_git_multipleCommitsRepo() throws Exception {
		String branch1Name = BRANCH + "1";
		String branch2Name = BRANCH + "2";
		setupMultipleCommitsRepo(branch1Name, branch2Name);

		RevCommit masterTip = repository.findCommit(MASTER);
		RevCommit branchTip = repository.findCommit(branch2Name);
		// #464792 : can't use "getParent(0).getParent(0)" without NPE
		RevCommit masterCommit2 = masterTip.getParent(0);
		RevCommit origin = repository.findCommit(masterCommit2.getId().getName()).getParent(0);

		IStorageProviderAccessor accessor = createAccessorForComparison(MASTER, branch2Name, false);
		final String workspaceRelativePath = iFile1.getFullPath().toOSString();

		IStorageProvider provider = accessor.getStorageProvider(iFile1, DiffSide.ORIGIN);
		IStorage storage = provider.getStorage(new NullProgressMonitor());
		String storagePath = storage.getFullPath().toOSString();
		// The path contains our iFile's workspace-relative path
		assertTrue(storagePath.contains(workspaceRelativePath));
		// But it doesn't start with it (repository name is the first segment)
		assertFalse(storagePath.startsWith(iFile1.getFullPath().toOSString()));
		// And it contains the commit's id at the end
		assertTrue(storagePath.endsWith(origin.getId().abbreviate(6).name()));

		// while we want an actually useable path
		String fixedPath = ResourceUtil.getFixedPath(storage).toOSString();
		assertEquals(workspaceRelativePath, fixedPath);

		// Likewise for the remote side
		provider = accessor.getStorageProvider(iFile1, DiffSide.REMOTE);
		storage = provider.getStorage(new NullProgressMonitor());
		storagePath = storage.getFullPath().toOSString();
		assertTrue(storagePath.contains(workspaceRelativePath));
		assertFalse(storagePath.startsWith(iFile1.getFullPath().toOSString()));
		assertTrue(storagePath.endsWith(branchTip.getId().abbreviate(6).name()));

		fixedPath = ResourceUtil.getFixedPath(storage).toOSString();
		assertEquals(workspaceRelativePath, fixedPath);

		// and the source side
		provider = accessor.getStorageProvider(iFile1, DiffSide.SOURCE);
		storage = provider.getStorage(new NullProgressMonitor());
		storagePath = storage.getFullPath().toOSString();
		assertTrue(storagePath.contains(workspaceRelativePath));
		assertFalse(storagePath.startsWith(iFile1.getFullPath().toOSString()));
		assertTrue(storagePath.endsWith(masterTip.getId().abbreviate(6).name()));

		fixedPath = ResourceUtil.getFixedPath(storage).toOSString();
		assertEquals(workspaceRelativePath, fixedPath);
	}
}
