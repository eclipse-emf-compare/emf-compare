/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.resourceattachmentchange.implication;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractGitTestCase;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Iterables;

@SuppressWarnings({"nls", "unused" })
public class AttachmentChangeImplicationTest extends AbstractGitTestCase {

	/**
	 * Path to the test data.
	 */
	private static String TEST_DATA_PATH = "src/org/eclipse/emf/compare/diagram/papyrus/tests/resourceattachmentchange/implication/data/";

	/** UML files extension. */
	private static final String UML_EXTENSION = '.' + UmlModel.UML_FILE_EXTENSION;

	/** UML files extension. */
	private static final String NOTATION_EXTENSION = '.' + NotationModel.NOTATION_FILE_EXTENSION;

	private ResourceSetImpl resourceSet;

	private IProject iProject;

	private TestProject testProject1;

	private IFile modelDi;

	private IFile modelNotation;

	private IFile modelUml;

	private IFile fragmentDi;

	private IFile fragmentNotation;

	private IFile fragmentUml;

	private void setupTestControlledPackagedWithDiagram() throws Exception {
		resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		iProject = testProject1.getProject();
		repository.connect(iProject);

		// 1st commit: a model with 2 packages.
		// 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
		modelDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/model.notation",
				"");
		modelUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit1/model.uml", "");

		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);
		repository.checkoutBranch(BRANCH_1);

		// 2nd commit: the 2nd package is fragmented.
		// The class diagram associated is moved in a new resource (notation model).
		modelDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/model.notation",
				"");
		modelUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/model.uml", "");
		fragmentDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/fragment.di", "");
		fragmentNotation = addToProject(TEST_DATA_PATH, testProject1, iProject,
				"case001/commit2/fragment.notation", "");
		fragmentUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case001/commit2/fragment.uml",
				"");
		repository.addAllAndCommit("2nd-commit");

		repository.checkoutBranch(MASTER);
	}

	private void setupTestControlledPackagedWithoutDiagram() throws Exception {
		resourceSet = new ResourceSetImpl();
		File workingDirectory = repository.getRepository().getWorkTree();
		testProject1 = new TestProject("Project1", workingDirectory.getAbsolutePath());
		iProject = testProject1.getProject();
		repository.connect(iProject);

		// 1st commit: a model with 2 packages.
		// 1 class diagram associated to the model, and 1 class diagram associated to the 2nd package.
		modelDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit1/model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit1/model.notation",
				"");
		modelUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit1/model.uml", "");

		repository.addAllAndCommit("1st-commit");
		repository.createBranch(MASTER, BRANCH_1);
		repository.checkoutBranch(BRANCH_1);

		// 2nd commit: the 2nd package is fragmented.
		// The class diagram associated is moved in a new resource (notation model).
		modelDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit2/model.di", "");
		modelNotation = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit2/model.notation",
				"");
		modelUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit2/model.uml", "");
		fragmentDi = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit2/fragment.di", "");
		fragmentNotation = addToProject(TEST_DATA_PATH, testProject1, iProject,
				"case002/commit2/fragment.notation", "");
		fragmentUml = addToProject(TEST_DATA_PATH, testProject1, iProject, "case002/commit2/fragment.uml",
				"");
		repository.addAllAndCommit("2nd-commit");

		repository.checkoutBranch(MASTER);
	}

	@Test
	@Ignore("This test is now wrong, no such requirement should exist")
	public void testControlledPackagedWithDiagram() throws Exception {
		setupTestControlledPackagedWithDiagram();

		Comparison comparison = compare(MASTER, BRANCH_1, modelNotation);
		EList<Diff> differences = comparison.getDifferences();

		ResourceAttachmentChange umlChange = null;
		ResourceAttachmentChange notationChange = null;
		ResourceAttachmentChange diChange = null;

		for (Diff diff : Iterables.filter(differences, ResourceAttachmentChange.class)) {
			ResourceAttachmentChange change = (ResourceAttachmentChange)diff;
			if (change.getResourceURI().endsWith(UML_EXTENSION)) {
				umlChange = change;
			} else if (change.getResourceURI().endsWith(NOTATION_EXTENSION)) {
				notationChange = change;
			} else {
				diChange = change;
			}
		}

		assertNotNull(umlChange);
		assertNotNull(notationChange);
		assertNotNull(diChange);

		assertEquals(1, notationChange.getRequires().size());
		assertEquals(1, notationChange.getRequiredBy().size());
		assertEquals(2, umlChange.getRequires().size());
		assertEquals(2, umlChange.getRequiredBy().size());
		assertEquals(1, diChange.getRequires().size());
		assertEquals(2, diChange.getRequiredBy().size());

		assertTrue(umlChange.getRequires().contains(notationChange));
		assertTrue(umlChange.getRequiredBy().contains(notationChange));
		assertTrue(notationChange.getRequires().contains(umlChange));
		assertTrue(diChange.getRequires().contains(umlChange));
		assertTrue(notationChange.getRequiredBy().contains(umlChange));
		assertTrue(diChange.getRequiredBy().contains(umlChange));
		assertTrue(umlChange.getRequires().contains(diChange));
		assertTrue(umlChange.getRequiredBy().contains(diChange));

	}

	@Test
	@Ignore("This test is now wrong, no such requirement should exist")
	public void testControlledPackagedWithoutDiagram() throws Exception {
		setupTestControlledPackagedWithoutDiagram();

		Comparison comparison = compare(MASTER, BRANCH_1, modelNotation);
		EList<Diff> differences = comparison.getDifferences();

		ResourceAttachmentChange umlChange = null;
		ResourceAttachmentChange notationChange = null;
		ResourceAttachmentChange diChange = null;

		for (Diff diff : Iterables.filter(differences, ResourceAttachmentChange.class)) {
			ResourceAttachmentChange change = (ResourceAttachmentChange)diff;
			if (change.getResourceURI().endsWith(UML_EXTENSION)) {
				umlChange = change;
			} else if (change.getResourceURI().endsWith(NOTATION_EXTENSION)) {
				notationChange = change;
			} else {
				diChange = change;
			}
		}

		assertNotNull(umlChange);
		assertNotNull(notationChange);
		assertNotNull(diChange);

		assertEquals(2, umlChange.getRequires().size());
		assertEquals(2, umlChange.getRequiredBy().size());
		assertEquals(1, notationChange.getRequires().size());
		Iterable<Diff> requiredForSecondChange = filter(notationChange.getRequiredBy(),
				instanceOf(ResourceAttachmentChange.class));
		assertEquals(1, Iterables.size(requiredForSecondChange));
		assertEquals(1, diChange.getRequires().size());
		assertEquals(2, diChange.getRequiredBy().size());

		Iterator<Diff> requiredForSecondChangeIterator = requiredForSecondChange.iterator();
		ResourceAttachmentChange firstRequirement = (ResourceAttachmentChange)requiredForSecondChangeIterator
				.next();

		assertTrue(notationChange.getRequires().contains(umlChange));
		assertTrue(diChange.getRequires().contains(umlChange));
		assertEquals(umlChange, firstRequirement);
		assertTrue(diChange.getRequiredBy().contains(umlChange));
		assertTrue(umlChange.getRequires().contains(notationChange));
		assertTrue(umlChange.getRequiredBy().contains(notationChange));
		assertTrue(umlChange.getRequires().contains(diChange));
		assertTrue(umlChange.getRequiredBy().contains(diChange));
	}

}
