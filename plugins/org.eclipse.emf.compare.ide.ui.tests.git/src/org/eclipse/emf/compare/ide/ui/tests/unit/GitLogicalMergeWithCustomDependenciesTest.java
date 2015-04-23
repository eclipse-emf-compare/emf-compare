/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.dependency.DependencyProviderDescriptor;
import org.eclipse.emf.compare.ide.ui.dependency.IDependencyProvider;
import org.eclipse.emf.compare.ide.ui.dependency.ModelDependencyProviderRegistry;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@SuppressWarnings({"nls", "restriction" })
public class GitLogicalMergeWithCustomDependenciesTest extends AbstractGitLogicalModelTest {

	private static final String DEPENDENT_FILE_NAME = "dependentFile.ecore";

	/**
	 * We have a file1 and file2. The file1 contains package P1, which in turn contains the classes C1 and C2.
	 * The file2 contains the package P2 containing the classes C3 and C4. The class C1 (in file1) is a
	 * super-class of C3 (in file2). Thus, there is a cross-reference from a model element in file1 to file2.
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();

		// set up the initial models in file1 and file2
		final EPackage packageP1 = createPackage(null, "P1");
		final EClass classC1 = createClass(packageP1, "C1");
		createClass(packageP1, "C2");
		resource1.getContents().add(packageP1);

		final EPackage packageP2 = createPackage(null, "P2");
		final EClass classC3 = createClass(packageP2, "C3");
		createClass(packageP2, "C4");
		resource2.getContents().add(packageP2);

		classC3.getESuperTypes().add(classC1);

		save(resource1, resource2);
	}

	@Override
	public void tearDown() throws Exception {
		// clear all dependency providers
		getModelDependencyProviderRegistry().clear();
		super.tearDown();
	}

	private ModelDependencyProviderRegistry getModelDependencyProviderRegistry() {
		return EMFCompareIDEUIPlugin.getDefault().getModelDependencyProviderRegistry();
	}

	/**
	 * In this test, a commit in a branch <em>adds</em> a <em>non-empty</em> file that is specified as a
	 * custom dependency but is not linked using cross-references; also it adds a class to file1. A commit in
	 * the master branch also adds a new class to file1 and file2. Adding the new classes on both sides, is
	 * only done to make sure the logical merge kicked in. If the logical merge wouldn't kick in, we'd get a
	 * textual conflict.
	 * <p>
	 * Since the logical merge has to deal with two existing and one added file, the EMFResourceMappingMerger
	 * is also responsible for merging the addition of the file in the branch although it is not directly
	 * linked to any other model.
	 * </p>
	 */
	@Test
	public void testRemoteBranchAddsUnlinkedNonEmptyDependentFile() throws Exception {
		// commit initial state as common ancestor commit
		repository.addAllAndCommit("initial-commit");
		
		// create branch but stay on master
		repository.createBranch(MASTER, BRANCH);
		
		// add class C4 to file1, add class C5 to file2, and commit to master
		createClass((EPackage)findObject(resource1, "P1"), "C4");
		createClass((EPackage)findObject(resource2, "P2"), "C5");
		save(resource1, resource2);
		repository.addAndCommit(project, "master-commit", file1, file2);
		
		// checkout branch and add a file that will be declared as dependency
		repository.checkoutBranch(BRANCH);
		final Collection<EObject> contentsOfNewResource = new HashSet<EObject>();
		contentsOfNewResource.add(createPackage(null, "P3"));
		save(createResourceWithContents(DEPENDENT_FILE_NAME, contentsOfNewResource));
		final File dependentFile = getFile(DEPENDENT_FILE_NAME);
		assertTrue(dependentFile.exists());
		
		// also add a new class to produce a textual conflict
		createClass((EPackage)findObject(resource1, "P1"), "C6");
		save(resource1);
		repository.addAndCommit(project, "branch-commit", dependentFile, file1);
		
		// install mock dependency provider
		installMockModelDependencyProvider(ImmutableMap.of(file1.getName(), ImmutableSet
				.of(DEPENDENT_FILE_NAME)));
		
		// checkout master and merge branch
		repository.checkoutBranch(MASTER);
		// dependent file does not exit before the merge
		assertFalse(iProject.exists(new Path(DEPENDENT_FILE_NAME)));
		
		repository.mergeLogical(BRANCH);
		// there shouldn't be a conflict, because the model-based merge handled this merge
		assertTrue(repository.status().getConflicting().isEmpty());
		// dependent file does exit after the merge
		assertTrue(iProject.exists(new Path(DEPENDENT_FILE_NAME)));
	}
	
	private Resource createResourceWithContents(String fileName, Collection<EObject> contents)
			throws Exception {
		final Resource newResource = createAndConnectResource(fileName);
		newResource.getContents().addAll(contents);
		return newResource;
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
