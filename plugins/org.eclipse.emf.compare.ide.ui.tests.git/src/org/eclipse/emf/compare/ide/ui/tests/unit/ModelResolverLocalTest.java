/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.CONTAINER;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.OUTGOING;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.PROJECT;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.SELF;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.WORKSPACE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * basic model setup will be one package containing two classes, from which we'll create cross-references through the 'suertypes' feature.
 * the model with error preventing its loading will be created by manually modifying a file containing a "basic model" as above to break a metaclass name.
 */
@SuppressWarnings("restriction")
public class ModelResolverLocalTest extends LogicalModelGraphTest {
	private static final String PROJECT2_NAME = "Project-2"; //$NON-NLS-1$

	private static final String FILE1_NAME = "file1.ecore"; //$NON-NLS-1$

	private static final String FILE2_NAME = "file2.ecore"; //$NON-NLS-1$

	private static final String FILE3_NAME = "file3.ecore"; //$NON-NLS-1$

	private static final String FILE4_NAME = "file4.ecore"; //$NON-NLS-1$

	private static final String FILE1_SUFFIX = "_file1"; //$NON-NLS-1$

	private static final String FILE2_SUFFIX = "_file2"; //$NON-NLS-1$

	private static final String FILE3_SUFFIX = "_file3"; //$NON-NLS-1$

	private static final String FILE4_SUFFIX = "_file4"; //$NON-NLS-1$

	private TestProject project2;

	private IFile iFile1;

	private IFile iFile2;

	private IFile iFile3;

	private IFile iFile4;

	private CrossReferenceResolutionScope originalResolutionScope;

	@Override
	@Before
	public void setUp() throws Exception {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		final String stringValue = store.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		originalResolutionScope = CrossReferenceResolutionScope.valueOf(stringValue);

		project2 = new TestProject(PROJECT2_NAME);

		super.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		final EMFModelProvider emfModelProvider = (EMFModelProvider)ModelProvider
				.getModelProviderDescriptor(EMFModelProvider.PROVIDER_ID).getModelProvider();
		emfModelProvider.clear();
		setResolutionScope(originalResolutionScope);
		iFile1 = null;
		iFile2 = null;
		project2.dispose();
		super.tearDown();
	}

	private List<IFile> setUpCase1() throws Exception {
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		save(resourceSet);

		breakModel(iFile2);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case1_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase1();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1), uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedGraph, expectedFile1Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedGraph, expectedFile2Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	@Test
	public void test_case1_outgoing_self() throws Exception {
		List<IFile> files = setUpCase1();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(OUTGOING, SELF)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	private List<IFile> setUpCase2() throws Exception {
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		breakModel(iFile2);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case2_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase2();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedResult, expectedResult);
		}
	}

	@Test
	public void test_case2_outgoing() throws Exception {
		List<IFile> files = setUpCase2();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	@Test
	public void test_case2_self() throws Exception {
		List<IFile> files = setUpCase2();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	private List<IFile> setUpCase3() throws Exception {
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case3_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase3();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1), uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedGraph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedGraph, expectedFile2Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	@Test
	public void test_case3_outgoing_self() throws Exception {
		List<IFile> files = setUpCase3();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(OUTGOING, SELF)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	private List<IFile> setUpCase4() throws Exception {
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case4_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase4();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal, Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedResult, expectedResult);
		}
	}

	@Test
	public void test_case4_outgoing() throws Exception {
		List<IFile> files = setUpCase4();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	@Test
	public void test_case4_self() throws Exception {
		List<IFile> files = setUpCase4();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	private List<IFile> setUpCase5() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject2, FILE2_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project2.getIFile(iProject2, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		save(resourceSet);

		breakModel(iFile2);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case5_workspace() throws Exception {
		List<IFile> files = setUpCase5();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1), uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedGraph, expectedFile1Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedGraph, expectedFile2Traversal,
				Diagnostic.ERROR);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	@Test
	public void test_case5_project_container_outgoing_self() throws Exception {
		List<IFile> files = setUpCase5();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER, OUTGOING, SELF)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	private List<IFile> setUpCase6() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject2, FILE2_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project2.getIFile(iProject2, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		breakModel(iFile2);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case6_workspace() throws Exception {
		List<IFile> files = setUpCase6();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal,
				Diagnostic.ERROR);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedResult, expectedResult);
	}

	@Test
	public void test_case6_project_container_self() throws Exception {
		List<IFile> files = setUpCase6();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER, SELF)) {
			setResolutionScope(scope);
			// FIXME fails for now. The reference is resolved in modes PROJECT and CONTAINER
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	@Test
	public void test_case6_outgoing() throws Exception {
		List<IFile> files = setUpCase6();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	private List<IFile> setUpCase7() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject2, FILE2_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project2.getIFile(iProject2, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case7_workspace() throws Exception {
		List<IFile> files = setUpCase7();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1), uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedGraph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedGraph, expectedFile2Traversal,
				Diagnostic.OK);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	@Test
	public void test_case7_project_container_outgoing_self() throws Exception {
		List<IFile> files = setUpCase7();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER, OUTGOING, SELF)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	private List<IFile> setUpCase8() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject2, FILE2_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project2.getIFile(iProject2, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case8_workspace() throws Exception {
		List<IFile> files = setUpCase8();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal, Diagnostic.OK);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedResult, expectedResult);
	}

	@Test
	public void test_case8_project_container_self() throws Exception {
		List<IFile> files = setUpCase8();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER, SELF)) {
			setResolutionScope(scope);
			// FIXME fails : the reference is resolved anyway in modes PROJECT and CONTAINER.
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
		}
	}

	@Test
	public void test_case8_outgoing() throws Exception {
		List<IFile> files = setUpCase8();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result);
	}

	private List<IFile> setUpCase9() throws Exception {
		final IProject iProject1 = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject1, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		breakModel(iFile3);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case9_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase9();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2), uriSet(iFile3));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedGraph, expectedFile3Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Result);
		}
	}

	@Test
	public void test_case9_outgoing() throws Exception {
		List<IFile> files = setUpCase9();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case9_self() throws Exception {
		List<IFile> files = setUpCase9();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	private List<IFile> setUpCase10() throws Exception {
		final IProject iProject1 = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject1, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource2);
		makeCrossReference(resource2, resource3);

		save(resourceSet);

		breakModel(iFile3);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case10_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase10();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2, iFile3));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2, iFile3);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedResult, expectedResult, expectedResult);
		}
	}

	@Test
	public void test_case10_outgoing() throws Exception {
		List<IFile> files = setUpCase10();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2, iFile3);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2, iFile3);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case10_self() throws Exception {
		List<IFile> files = setUpCase10();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	private List<IFile> setUpCase11() throws Exception {
		final IProject iProject1 = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject1, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case11_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase11();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2), uriSet(iFile3));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedGraph, expectedFile3Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Result);
		}
	}

	@Test
	public void test_case11_outgoing() throws Exception {
		List<IFile> files = setUpCase11();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case11_self() throws Exception {
		List<IFile> files = setUpCase11();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	private List<IFile> setUpCase12() throws Exception {
		final IProject iProject1 = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		final File file4 = project.getOrCreateFile(iProject1, FILE4_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject1, file3);
		iFile4 = project.getIFile(iProject1, file4);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);
		final Resource resource4 = connectResource(iFile4, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));
		resource4.getContents().add(createBasicModel(FILE4_SUFFIX));

		makeCrossReference(resource1, resource2);
		makeCrossReference(resource3, resource4);

		save(resourceSet);

		breakModel(iFile4);

		return Arrays.asList(iFile1, iFile2, iFile3, iFile4);
	}

	@Test
	public void test_case12_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase12();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2),
				uriSet(iFile3, iFile4));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Or4Traversal = storageSet(iFile3, iFile4);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile3Or4Result = new ExpectedResult(expectedGraph, expectedFile3Or4Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result,
					expectedFile3Or4Result, expectedFile3Or4Result);
		}
	}

	@Test
	public void test_case12_outgoing() throws Exception {
		List<IFile> files = setUpCase12();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3, iFile4));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3, iFile4);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	@Test
	public void test_case12_self() throws Exception {
		List<IFile> files = setUpCase12();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.ERROR);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	private List<IFile> setUpCase13() throws Exception {
		final IProject iProject1 = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		final File file4 = project.getOrCreateFile(iProject1, FILE4_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject1, file3);
		iFile4 = project.getIFile(iProject1, file4);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);
		final Resource resource4 = connectResource(iFile4, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));
		resource4.getContents().add(createBasicModel(FILE4_SUFFIX));

		makeCrossReference(resource1, resource2);
		makeCrossReference(resource3, resource4);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2, iFile3, iFile4);
	}

	@Test
	public void test_case13_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase13();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2),
				uriSet(iFile3, iFile4));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Or4Traversal = storageSet(iFile3, iFile4);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Or4Result = new ExpectedResult(expectedGraph, expectedFile3Or4Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result,
					expectedFile3Or4Result, expectedFile3Or4Result);
		}
	}

	@Test
	public void test_case13_outgoing() throws Exception {
		List<IFile> files = setUpCase13();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3, iFile4));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3, iFile4);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	@Test
	public void test_case13_self() throws Exception {
		List<IFile> files = setUpCase13();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.OK);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	private List<IFile> setUpCase14() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject2, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject2, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		breakModel(iFile3);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case14_workspace() throws Exception {
		List<IFile> files = setUpCase14();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2), uriSet(iFile3));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedGraph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Result);
	}

	@Test
	public void test_case14_project_container() throws Exception {
		List<IFile> files = setUpCase14();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Or2Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedFile1Or2Graph,
				expectedFile1Or2Traversal, Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Result);
		}
	}

	@Test
	public void test_case14_outgoing() throws Exception {
		List<IFile> files = setUpCase14();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case14_self() throws Exception {
		List<IFile> files = setUpCase14();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	private List<IFile> setUpCase15() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject2, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject2, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource2);
		makeCrossReference(resource2, resource3);

		save(resourceSet);

		breakModel(iFile3);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case15_workspace() throws Exception {
		List<IFile> files = setUpCase15();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2, iFile3));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2, iFile3);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal,
				Diagnostic.ERROR);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedResult, expectedResult, expectedResult);
	}

	@Test
	public void test_case15_project_container() throws Exception {
		List<IFile> files = setUpCase15();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Or2Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedFile1Or2Graph,
				expectedFile1Or2Traversal, Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			// FIXME fails cause cross-project references are resolved regardless of scope
			resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Result);
		}
	}

	@Test
	public void test_case15_outgoing() throws Exception {
		List<IFile> files = setUpCase15();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2, iFile3);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2, iFile3);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case15_self() throws Exception {
		List<IFile> files = setUpCase15();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	private List<IFile> setUpCase16() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject2, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject2, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource2);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case16_workspace() throws Exception {
		List<IFile> files = setUpCase16();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2), uriSet(iFile3));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedGraph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Result);
	}

	@Test
	public void test_case16_project_container() throws Exception {
		List<IFile> files = setUpCase16();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Or2Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedFile1Or2Graph,
				expectedFile1Or2Traversal, Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Result);
		}
	}

	@Test
	public void test_case16_outgoing() throws Exception {
		List<IFile> files = setUpCase16();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case16_self() throws Exception {
		List<IFile> files = setUpCase16();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	private List<IFile> setUpCase17() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		// Note that file2 is in project2 and file3 in project1 here
		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject2, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		final File file4 = project.getOrCreateFile(iProject2, FILE4_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject2, file2);
		iFile3 = project.getIFile(iProject1, file3);
		iFile4 = project.getIFile(iProject2, file4);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);
		final Resource resource4 = connectResource(iFile4, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));
		resource4.getContents().add(createBasicModel(FILE4_SUFFIX));

		makeCrossReference(resource1, resource2);
		makeCrossReference(resource3, resource4);

		save(resourceSet);

		breakModel(iFile4);

		return Arrays.asList(iFile1, iFile2, iFile3, iFile4);
	}

	@Test
	public void test_case17_workspace() throws Exception {
		List<IFile> files = setUpCase17();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2),
				uriSet(iFile3, iFile4));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Or4Traversal = storageSet(iFile3, iFile4);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile3Or4Result = new ExpectedResult(expectedGraph, expectedFile3Or4Traversal,
				Diagnostic.ERROR);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Or4Result,
				expectedFile3Or4Result);
	}

	@Test
	public void test_case17_project_container() throws Exception {
		List<IFile> files = setUpCase17();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Or2Graph = ImmutableSet.of(uriSet(iFile1, iFile3));
		Set<? extends Set<URI>> expectedFile3Or4Graph = ImmutableSet.of(uriSet(iFile2, iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Or2Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile1Or2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Or4Graph, expectedFile3Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile3Or4Graph, expectedFile4Traversal,
				Diagnostic.ERROR);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			// FIXME fails even on file1. the scope makes it so that "file3" is resolved too, and the
			// reference to file4 is resolved regardless of the scope.
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
					expectedFile4Result);
		}
	}

	@Test
	public void test_case17_outgoing() throws Exception {
		List<IFile> files = setUpCase17();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3, iFile4));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3, iFile4);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.ERROR);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.ERROR);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	@Test
	public void test_case17_self() throws Exception {
		List<IFile> files = setUpCase17();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.ERROR);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	private List<IFile> setUpCase18() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		// Note that file2 is in project2 and file3 in project1 here
		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject2, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		final File file4 = project.getOrCreateFile(iProject2, FILE4_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject2, file2);
		iFile3 = project.getIFile(iProject1, file3);
		iFile4 = project.getIFile(iProject2, file4);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);
		final Resource resource4 = connectResource(iFile4, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));
		resource4.getContents().add(createBasicModel(FILE4_SUFFIX));

		makeCrossReference(resource1, resource2);
		makeCrossReference(resource3, resource4);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2, iFile3, iFile4);
	}

	@Test
	public void test_case18_workspace() throws Exception {
		List<IFile> files = setUpCase18();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2),
				uriSet(iFile3, iFile4));
		Set<IStorage> expectedFile1Or2Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile3Or4Traversal = storageSet(iFile3, iFile4);
		ExpectedResult expectedFile1Or2Result = new ExpectedResult(expectedGraph, expectedFile1Or2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Or4Result = new ExpectedResult(expectedGraph, expectedFile3Or4Traversal,
				Diagnostic.OK);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedFile1Or2Result, expectedFile1Or2Result, expectedFile3Or4Result,
				expectedFile3Or4Result);
	}

	@Test
	public void test_case18_project_container() throws Exception {
		List<IFile> files = setUpCase18();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Or2Graph = ImmutableSet.of(uriSet(iFile1, iFile3));
		Set<? extends Set<URI>> expectedFile3Or4Graph = ImmutableSet.of(uriSet(iFile2, iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Or2Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile1Or2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Or4Graph, expectedFile3Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile3Or4Graph, expectedFile4Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			// FIXME fails even on file1. the scope makes it so that "file3" is resolved too, and the
			// reference to file4 is resolved regardless of the scope.
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
					expectedFile4Result);
		}
	}

	@Test
	public void test_case18_outgoing() throws Exception {
		List<IFile> files = setUpCase18();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3, iFile4));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3, iFile4);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	@Test
	public void test_case18_self() throws Exception {
		List<IFile> files = setUpCase18();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(uriSet(iFile4));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		Set<IStorage> expectedFile4Traversal = storageSet(iFile4);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile4Result = new ExpectedResult(expectedFile4Graph, expectedFile4Traversal,
				Diagnostic.OK);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	private List<IFile> setUpCase19() throws Exception {
		final IProject iProject1 = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject1, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject1, file2);
		iFile3 = project.getIFile(iProject1, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource3);
		makeCrossReference(resource2, resource3);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case19_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase19();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2, iFile3));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2, iFile3);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal, Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE, PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(files, expectedResult, expectedResult, expectedResult);
		}
	}

	@Test
	public void test_case19_outgoing() throws Exception {
		List<IFile> files = setUpCase19();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile3));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile3);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2, iFile3);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case19_self() throws Exception {
		List<IFile> files = setUpCase19();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(SELF);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	private List<IFile> setUpCase20() throws Exception {
		final IProject iProject1 = project.getProject();
		final IProject iProject2 = project2.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject1, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject2, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject1, FILE3_NAME);
		iFile1 = project.getIFile(iProject1, file1);
		iFile2 = project.getIFile(iProject2, file2);
		iFile3 = project.getIFile(iProject1, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));

		makeCrossReference(resource1, resource2);
		makeCrossReference(resource2, resource3);

		save(resourceSet);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}

	@Test
	public void test_case20_outgoing() throws Exception {
		List<IFile> files = setUpCase20();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1, iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1, iFile2, iFile3);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2, iFile3);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
	}

	@Test
	public void test_case20_project_container_self() throws Exception {
		List<IFile> files = setUpCase20();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(uriSet(iFile3));
		Set<IStorage> expectedFile1Traversal = storageSet(iFile1);
		Set<IStorage> expectedFile2Traversal = storageSet(iFile2);
		Set<IStorage> expectedFile3Traversal = storageSet(iFile3);
		ExpectedResult expectedFile1Result = new ExpectedResult(expectedFile1Graph, expectedFile1Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile2Result = new ExpectedResult(expectedFile2Graph, expectedFile2Traversal,
				Diagnostic.OK);
		ExpectedResult expectedFile3Result = new ExpectedResult(expectedFile3Graph, expectedFile3Traversal,
				Diagnostic.OK);

		for (CrossReferenceResolutionScope scope : Arrays.asList(PROJECT, CONTAINER, SELF)) {
			setResolutionScope(scope);
			// FIXME fails because scope doesn't restrict resolution of references
			resolveAndCheckResult(files, expectedFile1Result, expectedFile2Result, expectedFile3Result);
		}
	}

	@Test
	public void test_case20_workspace() throws Exception {
		List<IFile> files = setUpCase20();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(uriSet(iFile1, iFile2, iFile3));
		Set<IStorage> expectedTraversal = storageSet(iFile1, iFile2, iFile3);
		ExpectedResult expectedResult = new ExpectedResult(expectedGraph, expectedTraversal, Diagnostic.OK);

		setResolutionScope(WORKSPACE);
		resolveAndCheckResult(files, expectedResult, expectedResult, expectedResult);
	}

	private void resolveAndCheckResult(List<IFile> files, ExpectedResult... expected) throws Exception {
		for (int i = 0; i < files.size(); i++) {
			ResolvingResult resolutionResult = resolveTraversalOf(files.get(i));
			assertResultMatches(expected[i], resolutionResult);
		}
	}

	private void assertResultMatches(ExpectedResult expected, ResolvingResult actual) {
		assertEquals(expected.getDiagnosticSeverity(), actual.getTraversal().getDiagnostic().getSeverity());
		Set<? extends IStorage> actualStorages = actual.getTraversal().getStorages();
		Set<? extends IStorage> expectedStorages = expected.getStoragesInModel();
		assertEquals(expectedStorages.size(), actualStorages.size());
		assertTrue(actualStorages.containsAll(expectedStorages));

		Set<Set<URI>> actualGraph = actual.getSubGraphs();
		Set<? extends Set<URI>> expectedGraph = expected.getSubGraphs();
		assertEquals(expectedGraph.size(), actualGraph.size());
		assertTrue(actualGraph.containsAll(expectedGraph));
	}

	@SuppressWarnings("resource")
	private void breakModel(IFile file) throws Exception {
		Scanner scanner = null;
		InputStream outputSource = null;
		try {
			scanner = new Scanner(file.getContents()).useDelimiter("\\A"); //$NON-NLS-1$

			String fileContent = ""; //$NON-NLS-1$
			if (scanner.hasNext()) {
				fileContent = scanner.next();
			}

			String brokenModelContent = fileContent.replaceFirst("EClass", "BrokenEClass"); //$NON-NLS-1$ //$NON-NLS-2$

			outputSource = new ByteArrayInputStream(brokenModelContent.getBytes());
			file.setContents(outputSource, IResource.KEEP_HISTORY, new NullProgressMonitor());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			if (outputSource != null) {
				outputSource.close();
			}
		}
	}

	private static class ExpectedResult {
		private final Set<? extends Set<URI>> subGraphs;

		private final int diagnosticSeverity;

		private final Set<? extends IStorage> storagesInModel;

		public ExpectedResult(Set<? extends Set<URI>> subGraphs, Set<? extends IStorage> storagesInModel,
				int diagnosticSeverity) {
			this.subGraphs = subGraphs;
			this.storagesInModel = storagesInModel;
			this.diagnosticSeverity = diagnosticSeverity;
		}

		public Set<? extends Set<URI>> getSubGraphs() {
			return subGraphs;
		}

		public int getDiagnosticSeverity() {
			return diagnosticSeverity;
		}

		public Set<? extends IStorage> getStoragesInModel() {
			return storagesInModel;
		}
	}
}
