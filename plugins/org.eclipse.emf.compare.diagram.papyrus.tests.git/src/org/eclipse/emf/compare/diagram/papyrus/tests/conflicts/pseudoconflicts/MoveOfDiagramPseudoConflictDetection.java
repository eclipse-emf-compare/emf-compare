/*******************************************************************************
 * Copyright (C) 2016 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.conflicts.pseudoconflicts;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move.AbstractResourceAttachmentChangeMoveTests;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.junit.Test;

@SuppressWarnings({"nls", "unused" })
public class MoveOfDiagramPseudoConflictDetection extends AbstractResourceAttachmentChangeMoveTests {

	/**
	 * Path to the test data.
	 */
	private static String TEST_DATA_PATH = "src/org/eclipse/emf/compare/diagram/papyrus/tests/conflicts/pseudoconflicts/data/";

	private ResourceSetImpl resourceSet;
	private IProject iProject;
	private TestProject testProject1;
	private IFile modelDi;
	private IFile modelNotation;
	private IFile modelUml;
	private IFile employeeDi;
	private IFile employeeNotation;
	private IFile employeeUml;
	private IFile timeTrackingDi;
	private IFile timeTrackingNotation;
	private IFile timeTrackingUml;
	
	@Test
	public void pseudoConflictsOnResourceRootTest() throws Exception {
		setUpRepositoryMoveOfDiagrams();
		repository.checkoutBranch(BRANCH_1);
		Comparison comparison = compare(BRANCH_1, BRANCH_2, modelUml);
		assertEquals(16, comparison.getDifferences().size());
		assertEquals(4, comparison.getConflicts().size());
		
		for (Conflict conflict : comparison.getConflicts()) {
			assertEquals(ConflictKind.PSEUDO, conflict.getKind());
		}
	}
	
	private void setUpRepositoryMoveOfDiagrams() throws Exception {
		resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		iProject = testProject1.getProject();
		repository.connect(iProject);
		
		// 1st commit: a model with two controlled packages. 
		// The model and each packages contains a diagram
		modelDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/model.notation", "");
		modelUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/model.uml", "");
		employeeDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/Employee.di", "");
		employeeNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/Employee.notation", "");
		employeeUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/Employee.uml", "");
		timeTrackingDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/TimeTracking.di", "");
		timeTrackingNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/TimeTracking.notation", "");
		timeTrackingUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/TimeTracking.uml", "");
		repository.addAllAndCommit("1st-commit");
		
		// Create branchs.
		repository.createBranch(MASTER, BRANCH_1);
		repository.createBranch(MASTER, BRANCH_2);
		
		// 2nd commit: Uncontrol the packages. 
		repository.checkoutBranch(BRANCH_1);
		modelDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/model.notation", "");
		modelUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/model.uml", "");
		removeFromProject(iProject, employeeDi.getName());
		removeFromProject(iProject, employeeNotation.getName());
		removeFromProject(iProject, employeeUml.getName());
		removeFromProject(iProject, timeTrackingDi.getName());
		removeFromProject(iProject, timeTrackingNotation.getName());
		removeFromProject(iProject, timeTrackingUml.getName());
		repository.addAllAndCommit("2nd-commit");
		
		// 3rd commit: small changes on the model from master. 
		// The model have just a small change to make it different from master
		repository.checkoutBranch(BRANCH_2);
		modelDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit3/model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit3/model.notation", "");
		modelUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit3/model.uml", "");
		repository.addAllAndCommit("3rd-commit");
		
		repository.checkoutBranch(MASTER);
	}
	
}
