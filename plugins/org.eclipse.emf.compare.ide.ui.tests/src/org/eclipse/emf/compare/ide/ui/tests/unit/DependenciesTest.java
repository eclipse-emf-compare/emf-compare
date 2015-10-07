/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class DependenciesTest extends CompareTestCase {
	private static final String FILE1_SUFFIX = "_file1";

	private static final String FILE2_SUFFIX = "_file2";

	private static final String FILE3_SUFFIX = "_file3";

	private IFile iFile1;

	private IFile iFile2;

	private IFile iFile3;

	private ResourceSet resourceSet;

	private Resource resource1;

	private Resource resource2;

	private Resource resource3;

	private IModelResolver resolver;

	private IProgressMonitor monitor;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		resolver = new ThreadedModelResolver();
		((ThreadedModelResolver)resolver).setGraph(new Graph<URI>());
		resolver.initialize();
		monitor = new NullProgressMonitor();

		final IProject iProject = project.getProject();
		resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, "file1.ecore");
		final File file2 = project.getOrCreateFile(iProject, "file2.ecore");
		final File file3 = project.getOrCreateFile(iProject, "file3.ecore");

		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);
		iFile3 = project.getIFile(iProject, file3);

		resource1 = connectResource(iFile1, resourceSet);
		resource2 = connectResource(iFile2, resourceSet);
		resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		save(resourceSet);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		resolver.dispose();
		super.tearDown();
	}

	@Test
	public void testScopeNoDependencies() throws Exception {
		StorageTraversal traversal = resolver.resolveLocalModel(iFile1, monitor);
		assertEquals(1, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));

		traversal = resolver.resolveLocalModel(iFile2, monitor);
		assertEquals(1, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile2));

		traversal = resolver.resolveLocalModel(iFile3, monitor);
		assertEquals(1, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile3));
	}

	@Test
	public void testScopeAddedDependency() throws Exception {
		makeCrossReference(resource2, resource1);
		save(resourceSet);

		StorageTraversal traversal = resolver.resolveLocalModel(iFile1, monitor);
		assertEquals(2, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile2));

		traversal = resolver.resolveLocalModel(iFile2, monitor);
		assertEquals(2, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile2));
	}

	@Test
	public void testScopeRemovedDependency() throws Exception {
		makeCrossReference(resource2, resource1);
		save(resourceSet);

		breakCrossReferences(resource2, resource1);
		save(resourceSet);

		StorageTraversal traversal = resolver.resolveLocalModel(iFile1, monitor);
		assertEquals(1, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));

		traversal = resolver.resolveLocalModel(iFile2, monitor);
		assertEquals(1, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile2));
	}

	@Test
	public void testScopeDepth() throws Exception {
		makeCrossReference(resource2, resource1);
		makeCrossReference(resource3, resource2);
		save(resourceSet);

		StorageTraversal traversal = resolver.resolveLocalModel(iFile1, monitor);
		assertEquals(3, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile2));
		assertTrue(traversal.getStorages().contains(iFile3));

		traversal = resolver.resolveLocalModel(iFile2, monitor);
		assertEquals(3, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile2));
		assertTrue(traversal.getStorages().contains(iFile3));

		traversal = resolver.resolveLocalModel(iFile3, monitor);
		assertEquals(3, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile2));
		assertTrue(traversal.getStorages().contains(iFile3));
	}

	@Test
	public void testScopeUpdate() throws Exception {
		makeCrossReference(resource2, resource1);
		makeCrossReference(resource3, resource2);
		save(resourceSet);

		StorageTraversal traversal = resolver.resolveLocalModel(iFile1, monitor);
		assertEquals(3, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile2));
		assertTrue(traversal.getStorages().contains(iFile3));

		breakCrossReferences(resource2, resource1);
		save(resourceSet);

		traversal = resolver.resolveLocalModel(iFile1, monitor);
		assertEquals(1, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));

		traversal = resolver.resolveLocalModel(iFile2, monitor);
		assertEquals(2, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile2));
		assertTrue(traversal.getStorages().contains(iFile3));

		breakCrossReferences(resource3, resource2);
		makeCrossReference(resource3, resource1);
		save(resourceSet);

		traversal = resolver.resolveLocalModel(iFile1, monitor);
		assertEquals(2, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile3));

		traversal = resolver.resolveLocalModel(iFile2, monitor);
		assertEquals(1, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile2));

		traversal = resolver.resolveLocalModel(iFile3, monitor);
		assertEquals(2, traversal.getStorages().size());
		assertTrue(traversal.getStorages().contains(iFile1));
		assertTrue(traversal.getStorages().contains(iFile3));
	}
}
