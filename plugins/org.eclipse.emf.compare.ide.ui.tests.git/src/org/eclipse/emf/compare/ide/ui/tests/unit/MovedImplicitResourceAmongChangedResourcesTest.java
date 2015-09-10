/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Florian Zoubek - initial API and implementation
 *     Philip Langer - refactoring and minor improvements
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.dependency.DependencyProviderDescriptor;
import org.eclipse.emf.compare.ide.ui.dependency.IDependencyProvider;
import org.eclipse.emf.compare.ide.ui.dependency.ModelDependencyProviderRegistry;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings({"nls", "restriction" })
public class MovedImplicitResourceAmongChangedResourcesTest extends CompareGitTestCase {

	private static final HashMap<Object, Object> EMPTY_MAP = new HashMap<Object, Object>();

	private static final String MASTER = Constants.R_HEADS + Constants.MASTER;

	private static final String MOVED_MODEL_BRANCH = Constants.R_HEADS + "movedModelBranch";

	private static final String CHANGED_MODEL_BRANCH = Constants.R_HEADS + "changedModelBranch";

	private static final String ROOT_FILE_PATH = "directory1/file1.ecore";

	private static final String IMPLICIT_FILE_PATH = "directory1/file2.ecore";

	private static final String IMPLICIT_FILE_MOVED_PATH = "directory2/file2.ecore";

	private static final String SUBMODEL_FILE_PATH = "directory1/file3.ecore";

	private static final String SUBMODEL_FILE_MOVED_PATH = "directory2/file3.ecore";

	private File rootFile;

	private File identicalFile;

	private File submodelFile;

	private IFile rootIFile;

	private IFile identicalIFile;

	private IFile submodelIFile;

	private ResourceSet resourceSet;

	private IProject iProject;

	private Resource rootResource;

	private Resource identicalResource;

	private Resource submodelResource;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		iProject = project.getProject();
		resourceSet = new ResourceSetImpl();

		rootFile = project.getOrCreateFile(iProject, ROOT_FILE_PATH);
		identicalFile = project.getOrCreateFile(iProject, IMPLICIT_FILE_PATH);
		submodelFile = project.getOrCreateFile(iProject, SUBMODEL_FILE_PATH);
		rootIFile = project.getIFile(iProject, rootFile);
		identicalIFile = project.getIFile(iProject, identicalFile);
		submodelIFile = project.getIFile(iProject, submodelFile);

		rootResource = connectResource(rootIFile, resourceSet);
		identicalResource = connectResource(identicalIFile, resourceSet);
		submodelResource = connectResource(submodelIFile, resourceSet);
	}

	@Ignore("Due to Bug 464379, this test no longer works. It needs management of resource renaming via ResourceAttachmentChange.MOVE")
	@Test
	public void testRebaseNoConflictMovedSubmodel() throws Exception {
		setUpMovedIdenticalResourceAndChangedResourceBranches();

		installMockModelDependencyProvider(ImmutableMap.of(submodelFile.getName(), ImmutableSet
				.of(identicalFile.getName())));

		repository.checkoutBranch(MOVED_MODEL_BRANCH);
		iProject.refreshLocal(IResource.DEPTH_INFINITE, null);

		repository.rebaseLogical(CHANGED_MODEL_BRANCH);
		iProject.refreshLocal(IResource.DEPTH_INFINITE, null);

		assertTrue(repository.status().getConflicting().isEmpty());

		assertFalse(iProject.getFile(IMPLICIT_FILE_PATH).exists());
		assertFalse(iProject.getFile(SUBMODEL_FILE_PATH).exists());

		final IFile movedSubmodelFile = iProject.getFile(SUBMODEL_FILE_MOVED_PATH);
		final IFile movedIdenticalFile = iProject.getFile(IMPLICIT_FILE_MOVED_PATH);
		assertTrue(iProject.getFile(ROOT_FILE_PATH).exists());
		assertTrue(movedSubmodelFile.exists());
		assertTrue(movedIdenticalFile.exists());

		// Check the contents
		unload(rootResource, identicalResource, submodelResource);
		resourceSet.getResources().clear();
		rootResource = connectResource(iProject.getFile(ROOT_FILE_PATH), resourceSet);
		Resource movedIdenticalResource = connectResource(movedIdenticalFile, resourceSet);
		Resource movedSubmodelResource = connectResource(movedSubmodelFile, resourceSet);
		rootResource.load(EMPTY_MAP);
		movedIdenticalResource.load(EMPTY_MAP);
		movedSubmodelResource.load(EMPTY_MAP);

		EPackage testRootPack = (EPackage)rootResource.getContents().get(0);
		assertEquals("parent1", testRootPack.getName());

		EPackage testImplicitRootPack = (EPackage)movedIdenticalResource.getContents().get(0);
		assertEquals("parent2", testImplicitRootPack.getName());

		EPackage testSubmodelPack = testRootPack.getESubpackages().get(0);
		assertEquals("child", testSubmodelPack.getName());
		assertSame(movedSubmodelResource, ((InternalEObject)testSubmodelPack).eDirectResource());

		EClass testC1 = (EClass)testSubmodelPack.getEClassifiers().get(0);
		assertEquals("C1", testC1.getName());

		EClass nonConflictingClass = (EClass)testSubmodelPack.getEClassifiers().get(1);
		assertEquals("NonConflicting", nonConflictingClass.getName());
	}

	/**
	 * Sets up a git repository with a model and a submodel with an implicit dependency in the master branch,
	 * and two branches that base on the master branch: A branch {@link #MOVED_MODEL_BRANCH} with the submodel
	 * and implicit dependency moved into another directory, as well as a branch {@link #CHANGED_MODEL_BRANCH}
	 * with the submodel changed.
	 */
	public void setUpMovedIdenticalResourceAndChangedResourceBranches() throws Exception {
		EPackage root = createPackage(null, "parent1");
		EPackage implicitRoot = createPackage(null, "parent2");
		EPackage submodelChild = createPackage(root, "child");
		createClass(submodelChild, "C1");
		saveTestResource(submodelResource, submodelChild);
		saveTestResource(identicalResource, implicitRoot);
		saveTestResource(rootResource, root);
		repository.addAllAndCommit("initial-commit");

		// create and checkout branch for the moved submodel
		repository.createBranch(MASTER, MOVED_MODEL_BRANCH);
		repository.checkoutBranch(MOVED_MODEL_BRANCH);
		iProject.refreshLocal(IResource.DEPTH_INFINITE, null);

		reload(rootResource, submodelResource, identicalResource);

		// create new files with "moved" path
		IFile movedSubmodelFile = project.getIFile(iProject, project.getOrCreateFile(iProject,
				SUBMODEL_FILE_MOVED_PATH));
		IFile movedIdenticalFile = project.getIFile(iProject, project.getOrCreateFile(iProject,
				IMPLICIT_FILE_MOVED_PATH));
		Resource movedSubRes = connectResource(movedSubmodelFile, resourceSet);
		Resource movedIdenticalRes = connectResource(movedIdenticalFile, resourceSet);

		// save the existing objects from the old files into the new "moved" files
		saveTestResource(movedSubRes, (EPackage)findObject(submodelResource, "child"));
		saveTestResource(movedIdenticalRes, (EPackage)findObject(identicalResource, "parent2"));

		// save other resources as well
		save(rootResource, submodelResource, identicalResource);

		// delete old files
		iProject.getFile(IMPLICIT_FILE_PATH).delete(true, new NullProgressMonitor());
		iProject.getFile(SUBMODEL_FILE_PATH).delete(true, new NullProgressMonitor());

		// remove former files from the index
		repository.removeFromIndex(submodelIFile);
		repository.removeFromIndex(identicalIFile);
		repository.addAllAndCommit("Moved submodel file and implicit dependency to other directory");

		// create and checkout branch for the model changes
		repository.createBranch(MASTER, CHANGED_MODEL_BRANCH);
		repository.checkoutBranch(CHANGED_MODEL_BRANCH);
		repository.reset(CHANGED_MODEL_BRANCH, ResetType.HARD);
		iProject.refreshLocal(IResource.DEPTH_INFINITE, null);

		// reload resources
		unload(rootResource, identicalResource, submodelResource, movedIdenticalRes, movedSubRes);
		resourceSet.getResources().clear();
		reload(rootResource, identicalResource, submodelResource);

		// add a class and save all resources
		createClass((EPackage)submodelResource.getContents().get(0), "NonConflicting");
		save(submodelResource);
		save(identicalResource);
		save(rootResource);
		repository.addAllAndCommit("Added class to child.");

		// reset current branch to master
		repository.checkoutBranch(MASTER);
		repository.reset(MASTER, ResetType.HARD);
		iProject.refreshLocal(IResource.DEPTH_INFINITE, null);

		// unload resources
		unload(rootResource, identicalResource, submodelResource, movedIdenticalRes, movedSubRes);
		resourceSet.getResources().clear();
	}

	protected void saveTestResource(Resource resource, EPackage pkg) throws IOException, CoreException {
		resource.getContents().clear();
		resource.getContents().add(pkg);
		save(resource);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		getModelDependencyProviderRegistry().clear();
		unload(rootResource, identicalResource, submodelResource);
		super.tearDown();
	}

	private ModelDependencyProviderRegistry getModelDependencyProviderRegistry() {
		return EMFCompareIDEUIPlugin.getDefault().getModelDependencyProviderRegistry();
	}

	private void installMockModelDependencyProvider(
			final ImmutableMap<String, ImmutableSet<String>> dependencies) {
		getModelDependencyProviderRegistry().addProvider("mock",
				new DependencyProviderDescriptor(null, null) {
					private final IDependencyProvider mock = new MockDependencyProvider(dependencies);

					@Override
					public IDependencyProvider getDependencyProvider() {
						return mock;
					}
				});
	}

	private class MockDependencyProvider implements IDependencyProvider {

		private ImmutableMap<String, ImmutableSet<String>> dependencies;

		public MockDependencyProvider(ImmutableMap<String, ImmutableSet<String>> dependencies) {
			this.dependencies = dependencies;
		}

		public boolean apply(URI uri) {
			return dependencies.containsKey(uri.lastSegment());
		}

		public Set<URI> getDependencies(URI uri, URIConverter uriConverter) {
			if (dependencies.containsKey(uri.lastSegment())) {
				final Set<URI> uris = new HashSet<URI>();
				for (String fileName : dependencies.get(uri.lastSegment())) {
					final URI dependentUri = uri.trimSegments(1).appendSegment(fileName);
					if (uriConverter.exists(dependentUri, Collections.emptyMap())) {
						uris.add(dependentUri);
					}
				}
				return uris;
			} else {
				return Collections.emptySet();
			}
		}

	}
}
