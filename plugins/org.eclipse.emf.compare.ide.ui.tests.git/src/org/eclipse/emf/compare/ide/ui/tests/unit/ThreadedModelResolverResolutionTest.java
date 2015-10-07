/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.StreamAccessorStorage;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Test class for the ThreadedModelResolver, used for resources that reside in a git repository.
 * 
 * @author Alexandra Buzila
 */
@SuppressWarnings("nls")
public class ThreadedModelResolverResolutionTest extends AbstractGitLogicalModelTest {

	private IProgressMonitor monitor = new NullProgressMonitor();

	private Resource resource3;

	private File file3;

	private IFile iFile3;

	private Resource resource4;

	private File file4;

	private IFile iFile4;

	@Test
	public void testRemoteResolutionWithIncomingLogicalModel() throws Exception {

		setupRepositoryWithRemoteIncomingLogicalModel();

		ThreadedModelResolver resolver = new ThreadedModelResolver();
		resolver.setGraph(new Graph<URI>());
		resolver.initialize();

		IStorageProviderAccessor storageAccessor = createRemoteAccessorForComparison(MASTER, BRANCH, iFile3);

		StreamAccessorStorage file3Storage = StreamAccessorStorage.fromTypedElement(new StorageTypedElement(
				iFile3, iFile3.getFullPath().toOSString()));

		SynchronizationModel synchronizationModel = resolver.resolveModels(storageAccessor, iFile3,
				file3Storage, null, monitor);

		StorageTraversal rightTraversal = synchronizationModel.getRightTraversal();
		StorageTraversal leftTraversal = synchronizationModel.getLeftTraversal();

		/*
		 * leftTraversal should be empty, since file3 and file4 do not exist locally
		 */
		assertTrue(leftTraversal.getStorages().isEmpty());

		/*
		 * rightTraversal should contain file3 and file4, since file3 has a reference to file4
		 */
		assertEquals(2, rightTraversal.getStorages().size());
		assertContainsFile(rightTraversal, iFile3);
		assertContainsFile(rightTraversal, iFile4);
	}

	/**
	 * Creates a repository in which branch BRANCH forks from MASTER and contains two new files forming a
	 * logical model (file3 and file4, with file3 containing a reference to file4). Checked out is MASTER and
	 * file3 and file4 do not exist locally.
	 * 
	 * @throws Exception
	 *             if something went wrong.
	 */
	private void setupRepositoryWithRemoteIncomingLogicalModel() throws Exception {
		resource1.getContents().add(createBasicModel("1"));
		resource2.getContents().add(createBasicModel("2"));
		makeCrossReference(resource1, resource2);
		save(resource1, resource2);

		repository.addAndCommit(project, "master-commit-1", file1, file2);

		resource3 = createAndConnectResource("file3.ecore");
		resource3.getContents().add(createBasicModel("3"));
		save(resource3);
		file3 = project.getOrCreateFile(iProject, "file3.ecore");
		iFile3 = project.getIFile(iProject, file3);

		resource4 = createAndConnectResource("file4.ecore");
		resource4.getContents().add(createBasicModel("4"));
		save(resource4);
		file4 = project.getOrCreateFile(iProject, "file4.ecore");
		iFile4 = project.getIFile(iProject, file4);

		makeCrossReference(resource3, resource4);
		save(resource3, resource4);

		repository.createBranch(MASTER, BRANCH);
		repository.checkoutBranch(BRANCH);
		reload(resource1, resource2, resource3, resource4);
		repository.addAndCommit(project, "branch-commit-1", file3, file4);

		repository.checkoutBranch(MASTER);
		reload(resource1, resource2);
	}

	/**
	 * Asserts that the given file is part of the storage traversal.
	 * 
	 * @param traversal
	 *            The traversal to be checked if it contains the file.
	 * @param iFile
	 *            The file to look for in the traversal.
	 */
	private void assertContainsFile(StorageTraversal traversal, final IFile iFile) {
		assertTrue(Iterables.any(traversal.getStorages(), containsFile(iFile)));
	}

	private static Predicate<IStorage> containsFile(final IFile iFile) {
		return new Predicate<IStorage>() {
			public boolean apply(IStorage input) {
				return iFile.getName().equals(input.getName());
			}
		};
	}

}
