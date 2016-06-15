/*******************************************************************************
 * Copyright (C) 2016 Ericsson and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Simon Delisle - initial API and implementation
 *     Philip Langer - refactoring of test cases
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.move;

import static com.google.common.collect.Iterables.find;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diagram.internal.merge.CompareDiagramMerger;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.junit.Test;

import com.google.common.base.Predicate;

@SuppressWarnings("restriction")
public class ResourceAttachmentChangeMoveOrderTests extends AbstractResourceAttachmentChangeMoveTests {

	/** Path to the test data. */
	private static final String TEST_DATA_PATH = "src/org/eclipse/emf/compare/diagram/papyrus/tests/resourceattachmentchange/move/data/";

	/** Path of model files within the test data. */
	private static final String TEST_MODEL_PATH = "caseBug488089/";

	/** Path of ancestor model files within the test data. */
	private static final String ANCESTOR_PATH = TEST_MODEL_PATH + "ancestor/";

	/** Path of left model files within the test data. */
	private static final String LEFT_PATH = TEST_MODEL_PATH + "left/";

	/** Path of right model files within the test data. */
	private static final String RIGHT_PATH = TEST_MODEL_PATH + "right/";

	/** Name of component diagram 2. */
	private static final String COMPONENT_DIAGRAM_MODEL2 = "ComponentDiagramModel2";

	/** Name of component diagram 2. */
	private static final String COMPONENT_DIAGRAM_MODEL3 = "ComponentDiagramModel3";

	private ResourceSetImpl resourceSet;

	private IProject iProject;

	private TestProject testProject;

	private IFile modelDi;

	private IFile modelNotation;

	private IFile modelUml;

	private IFile model2Di;

	private IFile model2Notation;

	private IFile model2Uml;

	private IFile model3Di;

	private IFile model3Notation;

	private IFile model3Uml;

	/**
	 * Checkout BRANCH_2 and compare with BRANCH_1 (contain the move). 2 {@link ResourceAttachmentChange
	 * resource attachment changes} of kind MOVE. Merge ComponentDiagramModel3 diff then
	 * ComponentDiagramModel2 diff. In the resulting model, the order should be preserved
	 * (ComponentDiagramModel2 before ComponentDiagramModel3).
	 */
	@Test
	public void testOrderAfterManuallyApplyingDiffs() throws Exception {
		setUpRepositoryCaseBug488089();
		repository.checkoutBranch(BRANCH_2);
		Comparison comparison = compare(BRANCH_2, BRANCH_1, modelNotation);

		EList<Diff> diffs = comparison.getDifferences();
		Diff diffComponentDiagramModel2 = find(diffs, resourceAttachmentMoveDiff(COMPONENT_DIAGRAM_MODEL2));
		Diff diffComponentDiagramModel3 = find(diffs, resourceAttachmentMoveDiff(COMPONENT_DIAGRAM_MODEL3));

		IMerger.Registry mergerRegistry;
		mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		final IMerger umlMerger = new UMLMerger();
		umlMerger.setRanking(11);
		final IMerger diagramMerger = new CompareDiagramMerger();
		diagramMerger.setRanking(11);
		mergerRegistry.add(umlMerger);
		mergerRegistry.add(diagramMerger);

		mergerRegistry.getHighestRankingMerger(diffComponentDiagramModel3)
				.copyRightToLeft(diffComponentDiagramModel3, new BasicMonitor());
		mergerRegistry.getHighestRankingMerger(diffComponentDiagramModel2)
				.copyRightToLeft(diffComponentDiagramModel2, new BasicMonitor());

		checkOrderInLeftResource(comparison.getMatchedResources());
		testProject.dispose();
	}

	private void setUpRepositoryCaseBug488089() throws Exception {
		resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		testProject = new TestProject("Project", workingDirectory.getAbsolutePath());
		iProject = testProject.getProject();
		repository.connect(iProject);

		modelDi = addToProject(TEST_DATA_PATH, testProject, iProject, ANCESTOR_PATH + "model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject, iProject, ANCESTOR_PATH + "model.notation",
				"");
		modelUml = addToProject(TEST_DATA_PATH, testProject, iProject, ANCESTOR_PATH + "model.uml", "");

		model2Di = addToProject(TEST_DATA_PATH, testProject, iProject, ANCESTOR_PATH + "model2.di", "");
		model2Notation = addToProject(TEST_DATA_PATH, testProject, iProject,
				ANCESTOR_PATH + "model2.notation", "");
		model2Uml = addToProject(TEST_DATA_PATH, testProject, iProject, ANCESTOR_PATH + "model2.uml", "");

		model3Di = addToProject(TEST_DATA_PATH, testProject, iProject, ANCESTOR_PATH + "model3.di", "");
		model3Notation = addToProject(TEST_DATA_PATH, testProject, iProject,
				ANCESTOR_PATH + "model3.notation", "");
		model3Uml = addToProject(TEST_DATA_PATH, testProject, iProject, ANCESTOR_PATH + "model3.uml", "");

		repository.addAllAndCommit("1st-commit", true);

		repository.createBranch(MASTER, BRANCH_1);
		repository.createBranch(MASTER, BRANCH_2);

		repository.checkoutBranch(BRANCH_2);

		modelDi = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model.notation", "");
		modelUml = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model.uml", "");

		model2Di = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model2.di", "");
		model2Notation = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model2.notation",
				"");
		model2Uml = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model2.uml", "");

		model3Di = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model3.di", "");
		model3Notation = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model3.notation",
				"");
		model3Uml = addToProject(TEST_DATA_PATH, testProject, iProject, LEFT_PATH + "model3.uml", "");

		repository.addAllAndCommit("2nd-commit-branch2", true);

		repository.checkoutBranch(BRANCH_1);

		modelDi = addToProject(TEST_DATA_PATH, testProject, iProject, RIGHT_PATH + "model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject, iProject, RIGHT_PATH + "model.notation",
				"");
		modelUml = addToProject(TEST_DATA_PATH, testProject, iProject, RIGHT_PATH + "model.uml", "");

		removeFromProject(iProject, model2Di.getName());
		removeFromProject(iProject, model2Notation.getName());
		removeFromProject(iProject, model2Uml.getName());
		removeFromProject(iProject, model3Di.getName());
		removeFromProject(iProject, model3Notation.getName());
		removeFromProject(iProject, model3Uml.getName());

		repository.addAllAndCommit("2nd-commit-branch1", true);
	}

	private Predicate<Diff> resourceAttachmentMoveDiff(final String name) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input instanceof ResourceAttachmentChange
						&& DifferenceKind.MOVE.equals(input.getKind())) {
					final ResourceAttachmentChange change = (ResourceAttachmentChange)input;
					final EObject left = change.getMatch().getLeft();
					if (left instanceof Diagram) {
						return name.equals(((Diagram)left).getName());
					}
				}
				return false;
			}
		};
	}

	private void checkOrderInLeftResource(EList<MatchResource> comparisonResourceList) {
		Resource leftResource = null;
		for (MatchResource matchResource : comparisonResourceList) {
			if (matchResource.getLeftURI().contains("model.notation")) {
				leftResource = matchResource.getLeft();
				break;
			}
		}

		assertNotNull(leftResource);
		Diagram diagramModel2 = (Diagram)leftResource.getContents().get(1);
		Diagram diagramModel3 = (Diagram)leftResource.getContents().get(2);
		assertEquals(COMPONENT_DIAGRAM_MODEL2, diagramModel2.getName());
		assertEquals(COMPONENT_DIAGRAM_MODEL3, diagramModel3.getName());
	}

	/**
	 * Checkout BRANCH_2 and merge with BRANCH_1 (contain the move). In the resulting file, the order of the
	 * elements (diagrams) should be the same as BRANCH_1 (componentDiagramModel2 before
	 * componentDiagramModel3).
	 */
	@Test
	public void testOrderAfterLogicalMerge() throws Exception {
		setUpRepositoryCaseBug488089();
		repository.checkoutBranch(BRANCH_2);
		repository.mergeLogicalWithNewCommit(BRANCH_1);
		checkContentsCaseBug488089();
		testProject.dispose();
	}

	private void checkContentsCaseBug488089() throws Exception {
		assertTrue(repository.status().getConflicting().isEmpty());
		assertTrue(iProject.isAccessible());

		assertTrue(modelDi.exists());
		assertTrue(modelNotation.exists());
		assertTrue(modelUml.exists());

		URI modelNotationUri = URI.createPlatformResourceURI(modelNotation.getFullPath().toString(), true);
		Resource resourceModelNotation = resourceSet.getResource(modelNotationUri, true);

		assertEquals(3, resourceModelNotation.getContents().size());
		Diagram diagramModel2 = (Diagram)resourceModelNotation.getContents().get(1);
		Diagram diagramModel3 = (Diagram)resourceModelNotation.getContents().get(2);

		assertEquals(COMPONENT_DIAGRAM_MODEL2, diagramModel2.getName());
		assertEquals(COMPONENT_DIAGRAM_MODEL3, diagramModel3.getName());

		assertFalse(model2Di.exists());
		assertFalse(model2Notation.exists());
		assertFalse(model2Uml.exists());
		assertFalse(model3Di.exists());
		assertFalse(model3Notation.exists());
		assertFalse(model3Uml.exists());
	}
}
