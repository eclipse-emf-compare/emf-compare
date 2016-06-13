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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.ide.ui.tests.framework.ResolutionStrategyID;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.ResolutionStrategies;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.GitTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCherryPick;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitCompare;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitInput;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitMerge;
import org.eclipse.emf.compare.ide.ui.tests.git.framework.annotations.GitRebase;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.jgit.api.Status;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.junit.runner.RunWith;

/**
 * Tests for ResourceAttachmentChange with MOVE kind.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"nls", })
@RunWith(GitTestRunner.class)
@ResolutionStrategies(ResolutionStrategyID.WORKSPACE)
public class ResourceAttachmentChangeMoveNoConflictTests {

	/**
	 * BRANCH_3 checkouted. Comparison with BRANCH_2 (the move). Comparison: No conflicts. Some differences,
	 * with just 1 {@link ResourceAttachmentChange} of kind MOVE.
	 */
	@GitCompare(local = "branch3", remote = "branch2", file = "model.notation")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testComparisonMoveRemote(Comparison comparison) throws Exception {
		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(1, size(filter(comparison.getDifferences(),
				and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
	}

	/**
	 * BRANCH_2 checkouted (the move). Comparison with BRANCH_3. Comparison: No conflicts. Some differences,
	 * with just 1 {@link ResourceAttachmentChange} of kind MOVE.
	 */
	@GitCompare(local = "branch2", remote = "branch3", file = "model.notation")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testComparisonMoveLocal(Comparison comparison) throws Exception {
		assertTrue(comparison.getConflicts().isEmpty());
		assertEquals(1, size(filter(comparison.getDifferences(),
				and(instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.MOVE)))));
	}

	/**
	 * BRANCH_3 checkouted. Merge with BRANCH_2 (the move). Merge: No conflicts. The fragmented model and new
	 * diagram from 2nd commit have to be in the repository. The changes from the 3rd commit have to be there.
	 */
	@GitMerge(local = "branch3", remote = "branch2")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testMergeMoveRemote(Status status, List<IProject> projects) throws Exception {
		checkContentsCase001(status, projects);
	}

	/**
	 * BRANCH_2 checkouted (the move). Merge with BRANCH_3. Merge: No conflicts. The fragmented model and new
	 * diagram from 2nd commit have to be in the repository. The changes from the 3rd commit have to be there.
	 */
	@GitMerge(local = "branch2", remote = "branch3")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testMergeMoveLocal(Status status, List<IProject> projects) throws Exception {
		checkContentsCase001(status, projects);
	}

	/**
	 * BRANCH_3 checkouted. Merge with BRANCH_2 (the move). Rebase: No conflicts. The fragmented model and new
	 * diagram from 2nd commit have to be in the repository. The changes from the 3rd commit have to be there.
	 */
	@GitRebase(local = "branch3", remote = "branch2")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testRebaseMoveRemote(Status status, List<IProject> projects) throws Exception {
		checkContentsCase001(status, projects);
	}

	/**
	 * BRANCH_2 checkouted (the move). Merge with BRANCH_3. Rebase: No conflicts. The fragmented model and new
	 * diagram from 2nd commit have to be in the repository. The changes from the 3rd commit have to be there.
	 */
	@GitRebase(local = "branch2", remote = "branch3")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testRebaseMoveLocal(Status status, List<IProject> projects) throws Exception {
		checkContentsCase001(status, projects);
	}

	/**
	 * BRANCH_3 checkouted. Merge with BRANCH_2 (the move). CherryPick: No conflicts. The fragmented model and
	 * new diagram from 2nd commit have to be in the repository. The changes from the 3rd commit have to be
	 * there.
	 */
	@GitCherryPick(local = "branch3", remote = "branch2")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testCherryPickMoveRemote(Status status, List<IProject> projects) throws Exception {
		checkContentsCase001(status, projects);
	}

	/**
	 * BRANCH_2 checkouted (the move). Merge with BRANCH_3. CherryPick: No conflicts. The fragmented model and
	 * new diagram from 2nd commit have to be in the repository. The changes from the 3rd commit have to be
	 * there.
	 */
	@GitCherryPick(local = "branch2", remote = "branch3")
	@GitInput("data/resourceAttachmentChangeMoveNoConflict.zip")
	public void testCherryPickMoveLocal(Status status, List<IProject> projects) throws Exception {
		checkContentsCase001(status, projects);
	}

	private void checkContentsCase001(Status status, List<IProject> projects) throws Exception {
		// No conflicts
		assertTrue(status.getConflicting().isEmpty());

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resourceModelDi = null;
		Resource resourceModelUml = null;
		Resource resourceModelNotation = null;
		Resource resourceFragmentDi = null;
		Resource resourceFragmentUml = null;
		Resource resourceFragmentNotation = null;

		// The project1 is accessible.
		IProject iProject = projects.get(0);
		assertTrue(iProject.isAccessible());
		// After the merge, the repository should contain the new files from 2nd commit.
		for (IResource res : iProject.members()) {
			if (res.getName().equals("model.di")) {
				resourceModelDi = resourceSet
						.getResource(URI.createPlatformResourceURI(res.getFullPath().toString(), true), true);
			} else if (res.getName().equals("model.uml")) {
				resourceModelUml = resourceSet
						.getResource(URI.createPlatformResourceURI(res.getFullPath().toString(), true), true);
			} else if (res.getName().equals("model.notation")) {
				resourceModelNotation = resourceSet
						.getResource(URI.createPlatformResourceURI(res.getFullPath().toString(), true), true);
			} else if (res.getName().equals("fragment.di")) {
				resourceFragmentDi = resourceSet
						.getResource(URI.createPlatformResourceURI(res.getFullPath().toString(), true), true);
			} else if (res.getName().equals("fragment.uml")) {
				resourceFragmentUml = resourceSet
						.getResource(URI.createPlatformResourceURI(res.getFullPath().toString(), true), true);
			} else if (res.getName().equals("fragment.notation")) {
				resourceFragmentNotation = resourceSet
						.getResource(URI.createPlatformResourceURI(res.getFullPath().toString(), true), true);
			}
		}
		assertNotNull(resourceModelDi);
		assertNotNull(resourceModelUml);
		assertNotNull(resourceModelNotation);
		assertNotNull(resourceFragmentDi);
		assertNotNull(resourceFragmentUml);
		assertNotNull(resourceFragmentNotation);

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
