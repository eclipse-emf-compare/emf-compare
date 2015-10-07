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
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.PROJECT;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.WORKSPACE;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.OUTGOING;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.SELF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.graph.IGraphView;
import org.eclipse.emf.compare.graph.PruningIterator;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.internal.utils.ReadOnlyGraph;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/*
 * basic model setup will be one package containing two classes, from which we'll create cross-references through the 'suertypes' feature.
 * the model with error preventing its loading will be created by manually modifying a file containing a "basic model" as above to break a metaclass name.
 */
public class ModelResolverRemoteTest extends CompareGitTestCase {
	private static final String PROJECT2_NAME = "Project-2";

	private static final String FILE1_NAME = "file1.ecore";

	private static final String FILE2_NAME = "file2.ecore";

	private static final String FILE3_NAME = "file3.ecore";

	private static final String FILE4_NAME = "file4.ecore";

	private static final String FILE1_SUFFIX = "_file1";

	private static final String FILE2_SUFFIX = "_file2";

	private static final String FILE3_SUFFIX = "_file3";

	private static final String FILE4_SUFFIX = "_file4";

	private TestProject project2;

	private IFile iFile1;

	private IFile iFile2;

	private IFile iFile3;

	private IFile iFile4;

	private String initialCommit;

	private String targetCommit;

	private CrossReferenceResolutionScope originalResolutionScope;

	@Override
	@Before
	public void setUp() throws Exception {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault()
				.getPreferenceStore();
		final String stringValue = store
				.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		originalResolutionScope = CrossReferenceResolutionScope
				.valueOf(stringValue);

		project2 = new TestProject(PROJECT2_NAME);

		super.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		final EMFModelProvider emfModelProvider = (EMFModelProvider) ModelProvider
				.getModelProviderDescriptor(EMFModelProvider.PROVIDER_ID)
				.getModelProvider();
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
		initialCommit = repository.addAllAndCommit("initial-commit").getId()
				.getName();

		makeCrossReference(resource1, resource2);
		save(resourceSet);
		// force a little diff in file2 so that git thinks it's not in sync.
		addWhiteLine(iFile2);
		targetCommit = repository.addAllAndCommit("first-commit").getId()
				.getName();

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		repository.checkoutBranch(initialCommit);

		return Arrays.asList(iFile1, iFile2);
	}

	@Test
	public void test_case1_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase1();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(
				uriSet(iFile1), uriSet(iFile2));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1, iFile2));
		StorageTraversal expectedFile2Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedGraph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedGraph, expectedFile2Traversal,
				expectedFile2Traversal, expectedFile2Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE,
				PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(accessor, files, expectedFile1Result,
					expectedFile2Result);
		}
	}
	
	@Test
	public void test_case1_outgoing() throws Exception {
		List<IFile> files = setUpCase1();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(
				uriSet(iFile1), uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(
				uriSet(iFile2));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1, iFile2));
		StorageTraversal expectedFile2Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedFile1Graph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedFile2Graph, expectedFile2Traversal,
				expectedFile2Traversal, expectedFile2Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(accessor, files, expectedFile1Result,
				expectedFile2Result);
	}
	
	@Test
	public void test_case1_self() throws Exception {
		List<IFile> files = setUpCase1();
		assertEquals(2, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(
				uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(
				uriSet(iFile2));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1));
		StorageTraversal expectedFile2Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedFile1Graph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedFile2Graph, expectedFile2Traversal,
				expectedFile2Traversal, expectedFile2Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		setResolutionScope(SELF);
		resolveAndCheckResult(accessor, files, expectedFile1Result,
				expectedFile2Result);
	}
	
	private List<IFile> setUpCase2() throws Exception {
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		iFile2 = project.getIFile(iProject, file2);

		final Resource resource2 = connectResource(iFile2, resourceSet);

		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));

		save(resourceSet);
		initialCommit = repository.addAllAndCommit("initial-commit").getId()
				.getName();
		
		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		iFile1 = project.getIFile(iProject, file1);
		
		final Resource resource1 = connectResource(iFile1, resourceSet);
		
		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));

		makeCrossReference(resource1, resource2);
		save(resourceSet);
		// force a little diff in file2 so that git thinks it's not in sync.
		addWhiteLine(iFile2);
		targetCommit = repository.addAllAndCommit("first-commit").getId()
				.getName();

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		repository.checkoutBranch(initialCommit);

		return Arrays.asList(iFile1, iFile2);
	}
	
	@Test
	public void test_case2_workspace_project_container_outgoing_self() throws Exception {
		List<IFile> files = setUpCase2();
		assertEquals(2, files.size());
		
		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(
				uriSet(iFile2));

		StorageTraversal expectedTraversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedGraph, expectedTraversal,
				expectedTraversal, expectedTraversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedGraph, expectedTraversal,
				expectedTraversal, expectedTraversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE,
				PROJECT, CONTAINER, OUTGOING, SELF)) {
			setResolutionScope(scope);
			// File1 is not present locally, so we can't start the resolution from there
			resolveAndCheckResult(accessor, Arrays.asList(iFile2), expectedFile1Result,
					expectedFile2Result);
		}
	}
	
	private List<IFile> setUpCase3() throws Exception {
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject, FILE3_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);
		iFile3 = project.getIFile(iProject, file3);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));
		
		makeCrossReference(resource2, resource3);

		save(resourceSet);
		initialCommit = repository.addAllAndCommit("initial-commit").getId()
				.getName();
		
		makeCrossReference(resource1, resource2);
		save(resourceSet);
		// force a little diff in file2 and file3 so that git thinks they're not in sync.
		addWhiteLine(iFile2);
		addWhiteLine(iFile3);
		targetCommit = repository.addAllAndCommit("first-commit").getId()
				.getName();

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		repository.checkoutBranch(initialCommit);

		return Arrays.asList(iFile1, iFile2, iFile3);
	}
	
	@Test
	public void test_case3_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase3();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(
				uriSet(iFile1), uriSet(iFile2, iFile3));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1, iFile2, iFile3));
		StorageTraversal expectedFile2Or3Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2, iFile3));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedGraph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Or3Result = new ExpectedResult(
				expectedGraph, expectedFile2Or3Traversal,
				expectedFile2Or3Traversal, expectedFile2Or3Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE,
				PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(accessor, files, expectedFile1Result,
					expectedFile2Or3Result, expectedFile2Or3Result);
		}
	}
	
	@Test
	public void test_case3_outgoing() throws Exception {
		List<IFile> files = setUpCase3();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(
				uriSet(iFile1), uriSet(iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(
				uriSet(iFile2, iFile3));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(
				uriSet(iFile3));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1, iFile2, iFile3));
		StorageTraversal expectedFile2Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2, iFile3));
		StorageTraversal expectedFile3Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile3));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedFile1Graph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedFile2Graph, expectedFile2Traversal,
				expectedFile2Traversal, expectedFile2Traversal);
		ExpectedResult expectedFile3Result = new ExpectedResult(
				expectedFile3Graph, expectedFile3Traversal,
				expectedFile3Traversal, expectedFile3Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(accessor, files, expectedFile1Result,
				expectedFile2Result, expectedFile3Result);
	}
	
	@Test
	public void test_case3_self() throws Exception {
		List<IFile> files = setUpCase3();
		assertEquals(3, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(
				uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(
				uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(
				uriSet(iFile3));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1));
		StorageTraversal expectedFile2Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2));
		StorageTraversal expectedFile3Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile3));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedFile1Graph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedFile2Graph, expectedFile2Traversal,
				expectedFile2Traversal, expectedFile2Traversal);
		ExpectedResult expectedFile3Result = new ExpectedResult(
				expectedFile3Graph, expectedFile3Traversal,
				expectedFile3Traversal, expectedFile3Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		setResolutionScope(SELF);
		resolveAndCheckResult(accessor, files, expectedFile1Result,
				expectedFile2Result, expectedFile3Result);
	}
	
	private List<IFile> setUpCase4() throws Exception {
		final IProject iProject = project.getProject();
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		final File file3 = project.getOrCreateFile(iProject, FILE3_NAME);
		final File file4 = project.getOrCreateFile(iProject, FILE4_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);
		iFile3 = project.getIFile(iProject, file3);
		iFile4 = project.getIFile(iProject, file4);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);
		final Resource resource3 = connectResource(iFile3, resourceSet);
		final Resource resource4 = connectResource(iFile4, resourceSet);

		resource1.getContents().add(createBasicModel(FILE1_SUFFIX));
		resource2.getContents().add(createBasicModel(FILE2_SUFFIX));
		resource3.getContents().add(createBasicModel(FILE3_SUFFIX));
		resource4.getContents().add(createBasicModel(FILE4_SUFFIX));
		
		makeCrossReference(resource2, resource3);

		save(resourceSet);
		initialCommit = repository.addAllAndCommit("initial-commit").getId()
				.getName();
		
		makeCrossReference(resource1, resource2);
		makeCrossReference(resource3, resource4);
		save(resourceSet);
		// force a little diff in file2 and file4 so that git thinks they're not in sync.
		addWhiteLine(iFile2);
		addWhiteLine(iFile4);
		targetCommit = repository.addAllAndCommit("first-commit").getId()
				.getName();

		final Status status = repository.status();
		assertFalse(status.hasUncommittedChanges());

		repository.checkoutBranch(initialCommit);

		return Arrays.asList(iFile1, iFile2, iFile3, iFile4);
	}
	
	@Test
	public void test_case4_workspace_project_container() throws Exception {
		List<IFile> files = setUpCase4();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedGraph = ImmutableSet.of(
				uriSet(iFile1), uriSet(iFile2, iFile3), uriSet(iFile4));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1, iFile2, iFile3, iFile4));
		StorageTraversal expectedFile2Or3Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2, iFile3, iFile4));
		StorageTraversal expectedFile4Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile4));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedGraph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Or3Result = new ExpectedResult(
				expectedGraph, expectedFile2Or3Traversal,
				expectedFile2Or3Traversal, expectedFile2Or3Traversal);
		ExpectedResult expectedFile4Result = new ExpectedResult(
				expectedGraph, expectedFile4Traversal,
				expectedFile4Traversal, expectedFile4Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		for (CrossReferenceResolutionScope scope : Arrays.asList(WORKSPACE,
				PROJECT, CONTAINER)) {
			setResolutionScope(scope);
			resolveAndCheckResult(accessor, files, expectedFile1Result,
					expectedFile2Or3Result, expectedFile2Or3Result,
					expectedFile4Result);
		}
	}
	
	@Test
	public void test_case4_outgoing() throws Exception {
		List<IFile> files = setUpCase4();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(
				uriSet(iFile1), uriSet(iFile2, iFile3), uriSet(iFile4));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(
				uriSet(iFile2, iFile3), uriSet(iFile4));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(
				uriSet(iFile3), uriSet(iFile4));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(
				uriSet(iFile4));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1, iFile2, iFile3, iFile4));
		StorageTraversal expectedFile2Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2, iFile3, iFile4));
		StorageTraversal expectedFile3Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile3, iFile4));
		StorageTraversal expectedFile4Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile4));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedFile1Graph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedFile2Graph, expectedFile2Traversal,
				expectedFile2Traversal, expectedFile2Traversal);
		ExpectedResult expectedFile3Result = new ExpectedResult(
				expectedFile3Graph, expectedFile3Traversal,
				expectedFile3Traversal, expectedFile3Traversal);
		ExpectedResult expectedFile4Result = new ExpectedResult(
				expectedFile4Graph, expectedFile4Traversal,
				expectedFile4Traversal, expectedFile4Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		setResolutionScope(OUTGOING);
		resolveAndCheckResult(accessor, files, expectedFile1Result,
				expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}
	
	@Test
	public void test_case4_self() throws Exception {
		List<IFile> files = setUpCase4();
		assertEquals(4, files.size());

		Set<? extends Set<URI>> expectedFile1Graph = ImmutableSet.of(
				uriSet(iFile1));
		Set<? extends Set<URI>> expectedFile2Graph = ImmutableSet.of(
				uriSet(iFile2));
		Set<? extends Set<URI>> expectedFile3Graph = ImmutableSet.of(
				uriSet(iFile3));
		Set<? extends Set<URI>> expectedFile4Graph = ImmutableSet.of(
				uriSet(iFile4));

		StorageTraversal expectedFile1Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile1));
		StorageTraversal expectedFile2Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile2));
		StorageTraversal expectedFile3Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile3));
		StorageTraversal expectedFile4Traversal = expectedTraversal(
				Diagnostic.OK, storageSet(iFile4));

		ExpectedResult expectedFile1Result = new ExpectedResult(
				expectedFile1Graph, expectedFile1Traversal,
				expectedFile1Traversal, expectedFile1Traversal);
		ExpectedResult expectedFile2Result = new ExpectedResult(
				expectedFile2Graph, expectedFile2Traversal,
				expectedFile2Traversal, expectedFile2Traversal);
		ExpectedResult expectedFile3Result = new ExpectedResult(
				expectedFile3Graph, expectedFile3Traversal,
				expectedFile3Traversal, expectedFile3Traversal);
		ExpectedResult expectedFile4Result = new ExpectedResult(
				expectedFile4Graph, expectedFile4Traversal,
				expectedFile4Traversal, expectedFile4Traversal);

		IStorageProviderAccessor accessor = createAccessorForComparison(
				initialCommit, targetCommit, true);

		setResolutionScope(SELF);
		resolveAndCheckResult(accessor, files, expectedFile1Result,
				expectedFile2Result, expectedFile3Result,
				expectedFile4Result);
	}

	private void setResolutionScope(CrossReferenceResolutionScope scope) {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault()
				.getPreferenceStore();
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE,
				scope.name());
	}

	private void resolveAndCheckResult(IStorageProviderAccessor accessor,
			List<IFile> files, ExpectedResult... expected) throws Exception {
		for (int i = 0; i < files.size(); i++) {
			ResolvingResult resolutionResult = resolveTraversalOf(accessor,
					files.get(i));
			assertResultMatches(expected[i], resolutionResult);
		}
	}

	private void assertResultMatches(ExpectedResult expected,
			ResolvingResult actual) {
		assertTraversalsMatch(expected.getLeftTraversal(), actual
				.getSyncModel().getLeftTraversal());
		assertTraversalsMatch(expected.getRightTraversal(), actual
				.getSyncModel().getRightTraversal());
		assertTraversalsMatch(expected.getOriginTraversal(), actual
				.getSyncModel().getOriginTraversal());

		Set<Set<URI>> actualGraph = actual.getSubGraphs();
		Set<? extends Set<URI>> expectedGraph = expected.getSubGraphs();
		assertEquals(expectedGraph.size(), actualGraph.size());
		assertTrue(actualGraph.containsAll(expectedGraph));
	}

	private void assertTraversalsMatch(StorageTraversal expected,
			StorageTraversal actual) {
		assertEquals(expected.getDiagnostic().getSeverity(), actual
				.getDiagnostic().getSeverity());
		Set<? extends IStorage> actualStorages = actual.getStorages();
		Set<? extends IStorage> expectedStorages = expected.getStorages();
		assertEquals(expectedStorages.size(), actualStorages.size());
		Iterator<? extends IStorage> expectedIt = expectedStorages.iterator();
		while (expectedIt.hasNext()) {
			assertTrue(contains(actualStorages, expectedIt.next()));
		}
	}

	private boolean contains(Set<? extends IStorage> set, IStorage element) {
		IPath elementPath = ResourceUtil.getFixedPath(element);
		for (IStorage next : set) {
			IPath nextPath = ResourceUtil.getFixedPath(next);
			if (nextPath.isPrefixOf(elementPath)) {
				return true;
			}
		}
		return false;
	}

	private Set<URI> uriSet(IStorage... storages) {
		return ImmutableSet.copyOf(Iterables.transform(Arrays.asList(storages),
				ResourceUtil.asURI()));
	}

	private Set<IStorage> storageSet(IStorage... storages) {
		return Sets.<IStorage> newLinkedHashSet(Arrays.asList(storages));
	}

	private StorageTraversal expectedTraversal(int severity,
			Set<IStorage> storages) {
		return new StorageTraversal(storages, new BasicDiagnostic(severity, "",
				0, "", null));
	}

	private ResolvingResult resolveTraversalOf(
			IStorageProviderAccessor accessor, IFile file) throws Exception {
		ThreadedModelResolver resolver = new ThreadedModelResolver();
		resolver.initialize();
		IProgressMonitor nullMonitor = new NullProgressMonitor();
		IStorage leftStorage = accessor.getStorageProvider(file,
				DiffSide.SOURCE).getStorage(nullMonitor);
		IStorage rightStorage = accessor.getStorageProvider(file,
				DiffSide.REMOTE).getStorage(nullMonitor);
		IStorage originStorage = accessor.getStorageProvider(file,
				DiffSide.ORIGIN).getStorage(nullMonitor);
		SynchronizationModel syncModel = resolver.resolveModels(accessor,
				leftStorage, rightStorage, originStorage, nullMonitor);
		Set<Set<URI>> subGraphs = getSubGraphs(resolver.getGraphView());
		return new ResolvingResult(subGraphs, syncModel);
	}

	private Set<Set<URI>> getSubGraphs(IGraphView<URI> graph) {
		PruningIterator<URI> iterator = graph.breadthFirstIterator();
		Set<URI> roots = new LinkedHashSet<URI>();
		while (iterator.hasNext()) {
			roots.add(iterator.next());
			iterator.prune();
		}

		Set<Set<URI>> subgraphs = new LinkedHashSet<Set<URI>>();
		Set<URI> knownURIs = new HashSet<URI>();
		for (URI root : roots) {
			if (!knownURIs.contains(root)) {
				Set<URI> subgraph = graph.getSubgraphContaining(root);
				knownURIs.addAll(subgraph);
				subgraphs.add(subgraph);
			}
		}
		return subgraphs;
	}

	@SuppressWarnings("resource")
	private void addWhiteLine(IFile file) throws Exception {
		Scanner scanner = null;
		InputStream outputSource = null;
		try {
			scanner = new Scanner(file.getContents()).useDelimiter("\\A");

			String fileContent = "";
			if (scanner.hasNext()) {
				fileContent = scanner.next();
			}
			fileContent += '\n';

			outputSource = new ByteArrayInputStream(fileContent.getBytes());
			file.setContents(outputSource, IResource.KEEP_HISTORY,
					new NullProgressMonitor());
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

		private final StorageTraversal leftTraversal;

		private final StorageTraversal rightTraversal;

		private final StorageTraversal originTraversal;

		public ExpectedResult(Set<? extends Set<URI>> subGraphs,
				StorageTraversal leftTraversal,
				StorageTraversal rightTraversal,
				StorageTraversal originTraversal) {
			this.subGraphs = subGraphs;
			this.leftTraversal = leftTraversal;
			this.rightTraversal = rightTraversal;
			this.originTraversal = originTraversal;
		}

		public Set<? extends Set<URI>> getSubGraphs() {
			return subGraphs;
		}

		public StorageTraversal getLeftTraversal() {
			return leftTraversal;
		}

		public StorageTraversal getRightTraversal() {
			return rightTraversal;
		}

		public StorageTraversal getOriginTraversal() {
			return originTraversal;
		}
	}

	private static class ResolvingResult {
		private final Set<Set<URI>> subGraphs;

		private final SynchronizationModel syncModel;

		public ResolvingResult(Set<Set<URI>> subGraphs,
				SynchronizationModel syncModel) {
			this.subGraphs = subGraphs;
			this.syncModel = syncModel;
		}

		public Set<Set<URI>> getSubGraphs() {
			return subGraphs;
		}

		public SynchronizationModel getSyncModel() {
			return syncModel;
		}
	}
}
