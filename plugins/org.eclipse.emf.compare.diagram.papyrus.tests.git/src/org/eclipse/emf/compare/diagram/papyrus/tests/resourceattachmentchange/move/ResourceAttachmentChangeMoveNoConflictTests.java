/*******************************************************************************
 * Copyright (C) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.GitCorePreferences;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramChange;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.fixture.GitTestRepository;
import org.eclipse.emf.compare.diagram.papyrus.tests.egit.fixture.MockSystemReader;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.IdenticalResourceMinimizer;
import org.eclipse.emf.compare.ide.ui.internal.logical.StorageTypedElement;
import org.eclipse.emf.compare.ide.ui.internal.logical.SubscriberStorageAccessor;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.tests.CompareTestCase;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.SystemReader;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * Tests for ResourceAttachmentChange with MOVE kind.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"restriction", "nls", "unused" })
public class ResourceAttachmentChangeMoveNoConflictTests extends AbstractResourceAttachmentChangeMoveTests {

	private ResourceSetImpl resourceSet;
	private IProject iProject;
	private TestProject testProject1;
	private IFile modelDi;
	private IFile modelNotation;
	private IFile modelUml;
	private IFile fragmentDi;
	private IFile fragmentNotation;
	private IFile fragmentUml;
	
	/**
	 * BRANCH_3 checkouted. Comparison with BRANCH_2 (the move).
	 * Comparison: No conflicts. Some differences, with just 1 {@link ResourceAttachmentChange} of kind MOVE.
	 */
	@Test
	public void testComparisonMoveRemote() throws Exception {
		setUpRepositoryCase001();
		// Check comparison model : no conflicts and 1 RAC Move
		repository.checkoutBranch(BRANCH_3);
		Comparison comparison = compare(BRANCH_3, BRANCH_2, modelNotation);
		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(1, size(filter(comparison.getDifferences(), and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the move). Comparison with BRANCH_3.
	 * Comparison: No conflicts. Some differences, with just 1 {@link ResourceAttachmentChange} of kind MOVE.
	 */
	@Test
	public void testComparisonMoveLocal() throws Exception {
		setUpRepositoryCase001();
		// Check comparison model : no conflicts and 1 RAC Move
		repository.checkoutBranch(BRANCH_2);
		Comparison comparison = compare(BRANCH_2, BRANCH_3, modelNotation);
		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(1, size(filter(comparison.getDifferences(), and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted. Merge with BRANCH_2 (the move).
	 * Merge: No conflicts. The fragmented model and new diagram from 2nd commit have to be in the repository.
	 * The changes from the 3rd commit have to be there.
	 */
	@Test
	public void testMergeMoveRemote() throws Exception {
		setUpRepositoryCase001();
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		checkContentsCase001();
		
		testProject1.dispose();
	}

	/**
	 * BRANCH_2 checkouted (the move). Merge with BRANCH_3.
	 * Merge: No conflicts. The fragmented model and new diagram from 2nd commit have to be in the repository.
	 * The changes from the 3rd commit have to be there.
	 */
	@Test
	public void testMergeMoveLocal() throws Exception {
		setUpRepositoryCase001();
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		
		repository.mergeLogicalWithNewCommit(BRANCH_3);

		checkContentsCase001();
		
		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted. Merge with BRANCH_2 (the move).
	 * Rebase: No conflicts. The fragmented model and new diagram from 2nd commit have to be in the repository.
	 * The changes from the 3rd commit have to be there.
	 */
	@Test
	public void testRebaseMoveRemote() throws Exception {
		setUpRepositoryCase001();
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		
		repository.rebaseLogical(BRANCH_2);

		checkContentsCase001();
		
		testProject1.dispose();
	}

	/**
	 * BRANCH_2 checkouted (the move). Merge with BRANCH_3.
	 * Rebase: No conflicts. The fragmented model and new diagram from 2nd commit have to be in the repository.
	 * The changes from the 3rd commit have to be there.
	 */
	@Test
	public void testRebaseMoveLocal() throws Exception {
		setUpRepositoryCase001();
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		
		repository.rebaseLogical(BRANCH_3);

		checkContentsCase001();
		
		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted. Merge with BRANCH_2 (the move).
	 * CherryPick: No conflicts. The fragmented model and new diagram from 2nd commit have to be in the repository.
	 * The changes from the 3rd commit have to be there.
	 */
	@Test
	public void testCherryPickMoveRemote() throws Exception {
		setUpRepositoryCase001();
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		
		repository.cherryPickLogical(BRANCH_2);

		checkContentsCase001();
		
		testProject1.dispose();
	}

	/**
	 * BRANCH_2 checkouted (the move). Merge with BRANCH_3.
	 * CherryPick: No conflicts. The fragmented model and new diagram from 2nd commit have to be in the repository.
	 * The changes from the 3rd commit have to be there.
	 */
	@Test
	public void testCherryPickMoveLocal() throws Exception {
		setUpRepositoryCase001();
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		
		repository.cherryPickLogical(BRANCH_3);

		checkContentsCase001();
		
		testProject1.dispose();
	}
	
	/**
	 * Case 001.
	 * 3 commits. 
	 * 1st commit: a model with 2 packages. 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
	 * 2nd commit: the 2nd package is fragmented. The class diagram associated is moved in a new resource (notation model).
	 * 3rd commit: reset to the 1st commit. Add a class under the 2nd package. Also add in the diagram.
	 */
	private void setUpRepositoryCase001() throws Exception {
		resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		iProject = testProject1.getProject();
		repository.connect(iProject);
		
		// 1st commit: a model with 2 packages. 
		// 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
		modelDi = addToProject(testProject1, iProject, "case001/commit1/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case001/commit1/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case001/commit1/model.uml", "");

		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		// 2nd commit: the 2nd package is fragmented. 
		// The class diagram associated is moved in a new resource (notation model).
		modelDi = addToProject(testProject1, iProject, "case001/commit2/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case001/commit2/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case001/commit2/model.uml", "");
		fragmentDi = addToProject(testProject1, iProject, "case001/commit2/fragment.di", "");
		fragmentNotation = addToProject(testProject1, iProject, "case001/commit2/fragment.notation", "");
		fragmentUml = addToProject(testProject1, iProject, "case001/commit2/fragment.uml", "");
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit: reset to the 1st commit. 
		// Add a class under the 2nd package. Also add in the diagram.
		modelDi = addToProject(testProject1, iProject, "case001/commit3/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case001/commit3/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case001/commit3/model.uml", "");
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);
		
		repository.checkoutBranch(MASTER);
	}
	
	private void checkContentsCase001() throws Exception {
		// No conflicts
		assertTrue(repository.status().getConflicting().isEmpty());

		// The project1 is accessible.
		assertTrue(iProject.isAccessible());
		// After the merge, the repository should contain the new files from 2nd commit.
		assertTrue(modelDi.exists());
		assertTrue(modelNotation.exists());
		assertTrue(modelUml.exists());
		assertTrue(fragmentDi.exists());
		assertTrue(fragmentNotation.exists());
		assertTrue(fragmentUml.exists());

		// Check resources contents
		Resource resourceFragmentUml = resourceSet.getResource(URI.createPlatformResourceURI(fragmentUml.getFullPath().toString(),
				true), true);
		Resource resourceFragmentNotation = resourceSet.getResource(URI.createPlatformResourceURI(fragmentNotation.getFullPath().toString(),
				true), true);
		Resource resourceFragmentDi = resourceSet.getResource(URI.createPlatformResourceURI(fragmentDi.getFullPath().toString(),
				true), true);
		Resource resourceModelUml = resourceSet.getResource(URI.createPlatformResourceURI(modelUml.getFullPath().toString(),
				true), true);
		Resource resourceModelNotation = resourceSet.getResource(URI.createPlatformResourceURI(modelNotation.getFullPath().toString(),
				true), true);
		Resource resourceModelDi = resourceSet.getResource(URI.createPlatformResourceURI(modelDi.getFullPath().toString(),
				true), true);
		
		// ClassDiagramP2 should be in fragment.notation
		assertEquals(1, resourceFragmentNotation.getContents().size());
		Diagram diagramP2 = (Diagram)resourceFragmentNotation.getContents().get(0);
		assertEquals("ClassDiagramP2", diagramP2.getName());
		
		// It should contains the new graphical representation of semantic object Class1
		EList<?> children = diagramP2.getChildren();
		EObject element = ((Shape)children.get(0)).getElement();
		assertTrue(element instanceof org.eclipse.uml2.uml.Class);
		assertEquals("Class1", ((org.eclipse.uml2.uml.Class)element).getName());
		
		// P2 & Class1 should be in fragment.uml
		assertEquals(1, resourceFragmentUml.getContents().size());
		Package p2 = (Package)resourceFragmentUml.getContents().get(0);
		assertEquals("Package2", p2.getName());
		EList<PackageableElement> packagedElements = p2.getPackagedElements();
		assertEquals(1, packagedElements.size());
		org.eclipse.uml2.uml.Class class1 = (org.eclipse.uml2.uml.Class)packagedElements.get(0);
		assertEquals("Class1", class1.getName());
		
		// ClassDiagram should be in model.notation
		assertEquals(1, resourceModelNotation.getContents().size());
		Diagram diagram = (Diagram)resourceModelNotation.getContents().get(0);
		assertEquals("Class Diagram", diagram.getName());
	}
	
}
