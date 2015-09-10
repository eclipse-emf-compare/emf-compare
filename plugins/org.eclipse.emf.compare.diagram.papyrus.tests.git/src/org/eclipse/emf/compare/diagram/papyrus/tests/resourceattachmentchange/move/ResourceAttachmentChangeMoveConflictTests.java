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
public class ResourceAttachmentChangeMoveConflictTests extends AbstractResourceAttachmentChangeMoveTests {

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
	 * BRANCH_3 checkouted (the deletion). Comparison with BRANCH_2 (the move).
	 * Comparison: Conflicts, with 1 concerning a {@link ResourceAttachmentChange} DELETE with 1 {@link ResourceAttachmentChange} MOVE.
	 */
	@Test
	public void testComparisonMoveRemoteDeleteLocal() throws Exception {
		setUpRepositoryCase002();

		// Check comparison model : conflicts and 1 RAC Move
		repository.checkoutBranch(BRANCH_3);
		Comparison comparison = compare(BRANCH_3, BRANCH_2, modelNotation);
		assertEquals(2, comparison.getConflicts().size());
		assertEquals(1, size(filter(comparison.getDifferences(), and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		// There is a conflict between the move in 2nd commit & the deletion in 3rd commit
		Conflict conflict = comparison.getConflicts().get(1);
		EList<Diff> differences = conflict.getDifferences();
		assertEquals(3, differences.size());
		assertEquals(1, size(filter(differences, and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.DELETE)))));
		assertEquals(1, size(filter(differences, and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		assertEquals(1, size(filter(differences, and(instanceOf(DiagramChange.class), ofKind(DifferenceKind.DELETE)))));
		
		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the move). Comparison with BRANCH_3 (the deletion).
	 * Comparison: Conflicts, with 1 concerning a {@link ResourceAttachmentChange} DELETE with 1 {@link ResourceAttachmentChange} MOVE.
	 */
	@Test
	public void testComparisonMoveLocalDeleteRemote() throws Exception {
		setUpRepositoryCase002();

		// Check comparison model : conflicts and 1 RAC Move
		repository.checkoutBranch(BRANCH_2);
		Comparison comparison = compare(BRANCH_2, BRANCH_3, modelNotation);
		assertEquals(2, comparison.getConflicts().size());
		assertEquals(1, size(filter(comparison.getDifferences(), and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		// There is a conflict between the move in 2nd commit & the deletion in 3rd commit
		Conflict conflict = comparison.getConflicts().get(1);
		EList<Diff> differences = conflict.getDifferences();
		assertEquals(3, differences.size());
		assertEquals(1, size(filter(differences, and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.DELETE)))));
		assertEquals(1, size(filter(differences, and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		assertEquals(1, size(filter(differences, and(instanceOf(DiagramChange.class), ofKind(DifferenceKind.DELETE)))));
		
		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted (the deletion). Comparison with BRANCH_2 (the move).
	 * Merge: Conflicts.
	 */
	@Test
	public void testMergeMoveRemoteDeleteLocal() throws Exception {
		setUpRepositoryCase002();
		
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the move). Comparison with BRANCH_3 (the deletion).
	 * Merge: Conflicts.
	 */
	@Test
	public void testMergeMoveLocalDeleteRemote() throws Exception {
		setUpRepositoryCase002();
		
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		repository.mergeLogicalWithNewCommit(BRANCH_3);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted (the deletion). Comparison with BRANCH_2 (the move).
	 * Rebase: Conflicts.
	 */
	@Test
	public void testRebaseMoveRemoteDeleteLocal() throws Exception {
		setUpRepositoryCase002();
		
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		repository.rebaseLogical(BRANCH_2);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the move). Comparison with BRANCH_3 (the deletion).
	 * Rebase: Conflicts.
	 */
	@Test
	public void testRebaseMoveLocalDeleteRemote() throws Exception {
		setUpRepositoryCase002();
		
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		repository.rebaseLogical(BRANCH_3);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted (the deletion). Comparison with BRANCH_2 (the move).
	 * Merge: Conflicts.
	 */
	@Test
	public void testCherryPickMoveRemoteDeleteLocal() throws Exception {
		setUpRepositoryCase002();
		
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		repository.cherryPickLogical(BRANCH_2);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the move). Comparison with BRANCH_3 (the deletion).
	 * Merge: Conflicts.
	 */
	@Test
	public void testCherryPickMoveLocalDeleteRemote() throws Exception {
		setUpRepositoryCase002();
		
		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		repository.cherryPickLogical(BRANCH_3);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	
	/**
	 * BRANCH_3 checkouted (the second move). Comparison with BRANCH_2 (the first move).
	 * Comparison: Conflicts, with 2 {@link ResourceAttachmentChange} MOVE.
	 */
	@Test
	public void testComparisonMoveRemoteMoveLocal() throws Exception {
		setUpRepositoryCase003();

		// Check comparison model : conflicts and 2 RAC Move
		repository.checkoutBranch(BRANCH_3);
		Comparison comparison = compare(BRANCH_3, BRANCH_2, modelNotation);
		assertEquals(2, comparison.getConflicts().size());
		assertEquals(2, size(filter(comparison.getDifferences(), and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		// There is a conflict between the move in 2nd commit & the move in 3rd commit
		Conflict conflict = comparison.getConflicts().get(1);
		EList<Diff> differences = conflict.getDifferences();
		assertEquals(2, differences.size());
		assertEquals(2, size(filter(differences, and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
				
		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the first move). Comparison with BRANCH_3 (the second move).
	 * Comparison: Conflicts, with 2 {@link ResourceAttachmentChange} MOVE.
	 */
	@Test
	public void testComparisonMoveLocalMoveRemote() throws Exception {
		setUpRepositoryCase003();

		// Check comparison model : conflicts and 2 RAC Move
		repository.checkoutBranch(BRANCH_2);
		Comparison comparison = compare(BRANCH_2, BRANCH_3, modelNotation);
		assertEquals(2, comparison.getConflicts().size());
		assertEquals(2, size(filter(comparison.getDifferences(), and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
		// There is a conflict between the move in 2nd commit & the move in 3rd commit
		Conflict conflict = comparison.getConflicts().get(1);
		EList<Diff> differences = conflict.getDifferences();
		assertEquals(2, differences.size());
		assertEquals(2, size(filter(differences, and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
				
		testProject1.dispose();
	}

	/**
	 * BRANCH_3 checkouted (the second move). Comparison with BRANCH_2 (the first move).
	 * Merge: Conflicts.
	 */
	@Test
	public void testMergeMoveRemoteMoveLocal() throws Exception {
		setUpRepositoryCase003();

		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		repository.mergeLogicalWithNewCommit(BRANCH_2);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the first move). Comparison with BRANCH_3 (the second move).
	 * Merge: Conflicts.
	 */
	@Test
	public void testMergeMoveLocalMoveRemote() throws Exception {
		setUpRepositoryCase003();

		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		repository.mergeLogicalWithNewCommit(BRANCH_3);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted (the second move). Comparison with BRANCH_2 (the first move).
	 * Rebase: Conflicts.
	 */
	@Test
	public void testRebaseMoveRemoteMoveLocal() throws Exception {
		setUpRepositoryCase003();

		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		repository.rebaseLogical(BRANCH_2);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the first move). Comparison with BRANCH_3 (the second move).
	 * Rebase: Conflicts.
	 */
	@Test
	public void testRebaseMoveLocalMoveRemote() throws Exception {
		setUpRepositoryCase003();

		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		repository.rebaseLogical(BRANCH_3);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_3 checkouted (the second move). Comparison with BRANCH_2 (the first move).
	 * CherryPick: Conflicts.
	 */
	@Test
	public void testCherryPickMoveRemoteMoveLocal() throws Exception {
		setUpRepositoryCase003();

		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_3, ResetType.HARD);
		repository.cherryPickLogical(BRANCH_2);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * BRANCH_2 checkouted (the first move). Comparison with BRANCH_3 (the second move).
	 * CherryPick: Conflicts.
	 */
	@Test
	public void testCherryPickMoveLocalMoveRemote() throws Exception {
		setUpRepositoryCase003();

		repository.checkoutBranch(MASTER);
		repository.reset(BRANCH_2, ResetType.HARD);
		repository.cherryPickLogical(BRANCH_3);

		// Conflicts
		assertFalse(repository.status().getConflicting().isEmpty());

		testProject1.dispose();
	}
	
	/**
	 * Case 002.
	 * 3 commits. 
	 * 1st commit: a model with 2 packages. 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
	 * 2nd commit: the 2nd package is fragmented. The class diagram associated is moved in a new resource (notation model).
	 * 3rd commit: reset to the 1st commit. Remove the 2nd package. Also remove it from the diagram.
	 */
	private void setUpRepositoryCase002() throws Exception {
		resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		iProject = testProject1.getProject();
		repository.connect(iProject);
		
		// 1st commit: a model with 2 packages. 
		// 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
		modelDi = addToProject(testProject1, iProject, "case002/commit1/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case002/commit1/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case002/commit1/model.uml", "");

		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		// 2nd commit: the 2nd package is fragmented. 
		// The class diagram associated is moved in a new resource (notation model).
		modelDi = addToProject(testProject1, iProject, "case002/commit2/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case002/commit2/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case002/commit2/model.uml", "");
		fragmentDi = addToProject(testProject1, iProject, "case002/commit2/fragment.di", "");
		fragmentNotation = addToProject(testProject1, iProject, "case002/commit2/fragment.notation", "");
		fragmentUml = addToProject(testProject1, iProject, "case002/commit2/fragment.uml", "");
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit: reset to the 1st commit. 
		// Remove the 2nd package. Also remove it from the diagram.
		modelDi = addToProject(testProject1, iProject, "case002/commit3/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case002/commit3/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case002/commit3/model.uml", "");
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);
		
		repository.checkoutBranch(MASTER);
	}
	
	/**
	 * Case 003.
	 * 3 commits. 
	 * 1st commit: a model with 2 packages. 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
	 * 2nd commit: the 2nd package is fragmented. The class diagram associated is moved in a new resource X (notation model).
	 * 3rd commit: reset to the 1st commit. The 2nd package is fragmented. The class diagram associated is moved in a new resource Y (notation model).
	 */
	private void setUpRepositoryCase003() throws Exception {
		resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		iProject = testProject1.getProject();
		repository.connect(iProject);
		
		// 1st commit: a model with 2 packages. 
		// 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
		modelDi = addToProject(testProject1, iProject, "case003/commit1/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case003/commit1/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case003/commit1/model.uml", "");

		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);

		// 2nd commit: the 2nd package is fragmented. 
		// The class diagram associated is moved in a new resource X (notation model).
		modelDi = addToProject(testProject1, iProject, "case003/commit2/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case003/commit2/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case003/commit2/model.uml", "");
		fragmentDi = addToProject(testProject1, iProject, "case003/commit2/fragment.di", "");
		fragmentNotation = addToProject(testProject1, iProject, "case003/commit2/fragment.notation", "");
		fragmentUml = addToProject(testProject1, iProject, "case003/commit2/fragment.uml", "");
		repository.addAllAndCommit("2nd-commit");
		repository.createBranch(MASTER, BRANCH_2);

		// Back to 1st commit
		repository.reset(BRANCH_1, ResetType.HARD);

		// 3rd commit: the 2nd package is fragmented. 
		// The class diagram associated is moved in a new resource Y (notation model).
		modelDi = addToProject(testProject1, iProject, "case003/commit3/model.di", "");
		modelNotation = addToProject(testProject1, iProject, "case003/commit3/model.notation", "");
		modelUml = addToProject(testProject1, iProject, "case003/commit3/model.uml", "");
		IFile controlDi = addToProject(testProject1, iProject, "case003/commit3/control.di", "");
		IFile controlNotation = addToProject(testProject1, iProject, "case003/commit3/control.notation", "");
		IFile controlUml = addToProject(testProject1, iProject, "case003/commit3/control.uml", "");
		repository.addAllAndCommit("3rd-commit");
		repository.createBranch(MASTER, BRANCH_3);
		
		repository.checkoutBranch(MASTER);
	}
	
}
