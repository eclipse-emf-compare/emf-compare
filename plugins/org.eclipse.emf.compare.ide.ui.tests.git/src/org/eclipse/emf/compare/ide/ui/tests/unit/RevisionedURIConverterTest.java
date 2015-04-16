/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.RevisionedURIConverter;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.api.Status;
import org.junit.Test;

public class RevisionedURIConverterTest extends AbstractURITest {
	/**
	 * using the "straight" repo from {@link #setupStraightRepo()}, we expect
	 * that the base will be that initial commit, the remote side will be the
	 * branch, and the source side will be master.
	 * 
	 */
	@Test
	public void testStorageAccessorContents_straight() throws Exception {
		setupStraightRepo();

		IStorageProviderAccessor accessor = createAccessorForComparison(MASTER,
				BRANCH, iFile1);
		URI file1URI = ResourceUtil.createURIFor(iFile1);

		// origin is the "initial-commit" state
		assertStateInitial(file1URI, accessor, DiffSide.ORIGIN);

		// source is the "MASTER" state. C2 no longer exists
		assertStateMaster(file1URI, accessor, DiffSide.SOURCE);

		// remote is the "BRANCH" state. C2 is now a super-type of C1
		assertStateBranch(file1URI, accessor, DiffSide.REMOTE);
	}

	/**
	 * If we compare "master" with "branch2", we expect the common ancestor to
	 * be "commit-master-1".
	 */
	@Test
	public void testStorageAccessorContents_multipleCommits() throws Exception {
		String branch1Name = BRANCH + "1";
		String branch2Name = BRANCH + "2";
		setupMultipleCommitsRepo(branch1Name, branch2Name);

		IStorageProviderAccessor accessor = createAccessorForComparison(MASTER,
				branch2Name, iFile1);
		URI file1URI = ResourceUtil.createURIFor(iFile1);

		// origin is the "commit-master-1" state, which we initialized to be the
		// same as our usual "initial" state
		assertStateInitial(file1URI, accessor, DiffSide.ORIGIN);

		// source is the "MASTER" state. C2 no longer exists
		assertStateMaster(file1URI, accessor, DiffSide.SOURCE);

		// remote is the "BRANCH" state. C2 is now a super-type of C1
		assertStateBranch(file1URI, accessor, DiffSide.REMOTE);
	}

	/**
	 * This will create three files with the expected initial, master and branch
	 * content, then use a custom content accessor that will redirect
	 * {@link #file1}'s URI to these.
	 */
	@Test
	public void testRedirectingAccessor() throws Exception {
		/*
		 * setup has created a dummy file file1. we're gonna create the three
		 * sides in separate folders, and expect the storage accessor to do the
		 * redirection.
		 */
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		File originFile = project.getOrCreateFile(iProject,
				DiffSide.ORIGIN.toString() + "/file1.ecore");
		File sourceFile = project.getOrCreateFile(iProject,
				DiffSide.SOURCE.toString() + "/file1.ecore");
		File remoteFile = project.getOrCreateFile(iProject,
				DiffSide.REMOTE.toString() + "/file1.ecore");
		IFile originIFile = project.getIFile(iProject, originFile);
		IFile sourceIFile = project.getIFile(iProject, sourceFile);
		IFile remoteIFile = project.getIFile(iProject, remoteFile);

		Resource originResource = connectResource(originIFile, resourceSet);
		Resource sourceResource = connectResource(sourceIFile, resourceSet);
		Resource remoteResource = connectResource(remoteIFile, resourceSet);

		final EPackage originRoot = createPackage(null, "P1");
		createClass(originRoot, "C1");
		createClass(originRoot, "C2");
		originResource.getContents().add(originRoot);
		save(originResource);

		final EPackage sourceRoot = createPackage(null, "P1");
		createClass(sourceRoot, "C1_renamed");
		sourceResource.getContents().add(sourceRoot);
		save(sourceResource);

		final EPackage remoteRoot = createPackage(null, "P1");
		final EClass class1 = createClass(remoteRoot, "C1");
		final EClass class2 = createClass(remoteRoot, "C2");
		createClass(remoteRoot, "C3");
		class1.getESuperTypes().add(class2);
		remoteResource.getContents().add(remoteRoot);
		save(remoteResource);

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		IStorageProviderAccessor accessor = new PathRedirectingStorageAccessor();
		URI file1URI = ResourceUtil.createURIFor(iFile1);

		// origin is the "initial-commit" state
		assertStateInitial(file1URI, accessor, DiffSide.ORIGIN);

		// source is the "MASTER" state. C2 no longer exists
		assertStateMaster(file1URI, accessor, DiffSide.SOURCE);

		// remote is the "BRANCH" state. C2 is now a super-type of C1
		assertStateBranch(file1URI, accessor, DiffSide.REMOTE);
	}

	/**
	 * We'll be using a storage provider accessor that returns <code>null</code>
	 * IStorageProviders. We expect the uri converter not to throw exceptions,
	 * and not to fall back to the existing file pointed by the URI.
	 */
	@Test
	public void testNullAccessor() throws Exception {
		NullStorageAccessor accessor = new NullStorageAccessor();
		URIConverter delegate = new ExtensibleURIConverterImpl();
		URI fileURI = ResourceUtil.createURIFor(iFile1);

		RevisionedURIConverter converter = new RevisionedURIConverter(delegate,
				accessor, DiffSide.ORIGIN);
		InputStream stream = converter.createInputStream(fileURI,
				Collections.emptyMap());
		assertNull(stream);

		converter = new RevisionedURIConverter(delegate, accessor,
				DiffSide.REMOTE);
		stream = converter.createInputStream(fileURI, Collections.emptyMap());
		assertNull(stream);

		converter = new RevisionedURIConverter(delegate, accessor,
				DiffSide.SOURCE);
		stream = converter.createInputStream(fileURI, Collections.emptyMap());
		assertNull(stream);
	}

	/**
	 * We'll be using a storage provide accessor that throws exception. We
	 * expect that exception to slip through and fail the test.
	 */
	@Test
	public void testExceptionAccessor() throws Exception {
		ExceptionStorageAccessor accessor = new ExceptionStorageAccessor();
		URIConverter delegate = new ExtensibleURIConverterImpl();
		URI fileURI = ResourceUtil.createURIFor(iFile1);

		RevisionedURIConverter converter = new RevisionedURIConverter(delegate,
				accessor, DiffSide.ORIGIN);
		try {
			converter.createInputStream(fileURI, Collections.emptyMap());
			fail();
		} catch (UnsupportedOperationException e) {
			// expected this one to slip through
		}
		try {
			converter = new RevisionedURIConverter(delegate, accessor,
					DiffSide.REMOTE);
			converter.createInputStream(fileURI, Collections.emptyMap());
			fail();
		} catch (UnsupportedOperationException e) {
			// expected this one to slip through
		}

		try {
			converter = new RevisionedURIConverter(delegate, accessor,
					DiffSide.SOURCE);
			converter.createInputStream(fileURI, Collections.emptyMap());
			fail();
		} catch (UnsupportedOperationException e) {
			// expected this one to slip through
		}
	}

	private static class PathRedirectingStorageAccessor implements
			IStorageProviderAccessor {
		public IStorageProvider getStorageProvider(IResource resource,
				DiffSide side) throws CoreException {
			assertTrue(resource instanceof IFile && resource.exists());
			IPath originalPath = resource.getFullPath();
			String fileName = originalPath.lastSegment();
			final IPath redirectedPath = originalPath.removeLastSegments(1)
					.append(side.toString()).append(fileName);
			return new IStorageProvider() {
				public IStorage getStorage(IProgressMonitor monitor)
						throws CoreException {
					return ResourcesPlugin.getWorkspace().getRoot()
							.getFile(redirectedPath);
				}
			};
		}

		public boolean isInSync(IResource resource) throws CoreException {
			return false;
		}
	}

	private static class NullStorageAccessor implements
			IStorageProviderAccessor {
		public IStorageProvider getStorageProvider(IResource resource,
				DiffSide side) throws CoreException {
			return null;
		}

		public boolean isInSync(IResource resource) throws CoreException {
			return false;
		}
	}

	private static class ExceptionStorageAccessor implements
			IStorageProviderAccessor {
		public IStorageProvider getStorageProvider(IResource resource,
				DiffSide side) throws CoreException {
			throw new UnsupportedOperationException();
		}

		public boolean isInSync(IResource resource) throws CoreException {
			return false;
		}
	}
}
