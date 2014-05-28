/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeRunnableImpl;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=434827">434827</a>
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@SuppressWarnings("restriction")
public class TestBug434828 {

	private IMerger.Registry mergerRegistry;

	private DefaultComparisonScope scope;

	private Diff deletedNodeDeletionDiff;

	private Diff holdingRefDeletionDiff;

	private Diff refChangeDiff;

	/**
	 * Set up the model comparison with the following use case:
	 * <p>
	 * The 3 models loaded are:
	 * <ul>
	 * <li>Origin which contains 3 nodes structured like this:
	 * 
	 * <pre>
	 *  		Root - DeletedNode - ReferencedNode
	 *  		  \  - HoldingReferenceNode
	 * </pre>
	 * 
	 * </li>
	 * <li>Left that has an extra eopposite reference between "HoldingReference" and "ReferencedNode"</li>
	 * <li>Right in which "DeletedNode" has been deleted"</li>
	 * </ul>
	 * </p>
	 * 
	 * @throws IOException
	 */
	@Before
	public void setUp() throws IOException {
		Bug434828InputData inputData = new Bug434828InputData();
		final Resource left = inputData.getResource("left.nodes"); //$NON-NLS-1$
		final Resource right = inputData.getResource("right.nodes"); //$NON-NLS-1$
		final Resource origin = inputData.getResource("origin.nodes"); //$NON-NLS-1$
		scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		// Keeps tracks of the differences to test.
		EList<Conflict> conflicts = comparison.getConflicts();
		Assert.assertEquals(1, conflicts.size());
		Conflict conflict = conflicts.get(0);

		// Get the right diff of the conflict: change of eopposite reference
		EList<Diff> leftConflicts = conflict.getLeftDifferences();
		Assert.assertEquals(2, leftConflicts.size());
		refChangeDiff = leftConflicts.get(0);// Both difference are equivalent.

		// Get the left diff of the conflict; deletion of "ReferencedNode"
		EList<Diff> rightConflicts = conflict.getRightDifferences();
		Assert.assertEquals(1, rightConflicts.size());
		holdingRefDeletionDiff = rightConflicts.get(0);
		// Get the required by diff of the right conflict
		EList<Diff> rightRequiredBy = holdingRefDeletionDiff.getRequiredBy();
		Assert.assertEquals(1, rightRequiredBy.size());
		deletedNodeDeletionDiff = rightRequiredBy.get(0);

	}

	/**
	 * In the resulting comparison model we have:
	 * <ul>
	 * <li>Two equivalent differences which are the reference changes between "HoldingReference" and
	 * "ReferencedNode"</li>
	 * <li>One deletion difference (deletion of "HoldingDeletedNode") that requires a another deletion
	 * difference (deletion of "RefencedNode")</li>
	 * <ul>
	 * This test aims to check that accepting one of the two equivalent differences should reject the deletion
	 * of "ReferencedNode", and doesn't modify the state of "HoldingDeletedNode".
	 */
	@Test
	public void testAcceptConflictDiffWithConflictingDiffWithRequiredBy() {
		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, MergeMode.ACCEPT);
		mergeRunnable.merge(Collections.singletonList(refChangeDiff), false, mergerRegistry);

		Assert.assertEquals(DifferenceState.MERGED, refChangeDiff.getState());
		Assert.assertEquals(MergeMode.ACCEPT, getMergeData(refChangeDiff).getMergeMode());
		Assert.assertEquals(DifferenceState.MERGED, holdingRefDeletionDiff.getState());
		Assert.assertEquals(MergeMode.REJECT, getMergeData(holdingRefDeletionDiff).getMergeMode());
		Assert.assertEquals(DifferenceState.UNRESOLVED, deletedNodeDeletionDiff.getState());

		// Checks that the content of the left resource is correct.
		Resource leftResource = (Resource)scope.getLeft();

		EList<EObject> content = leftResource.getContents();
		Assert.assertEquals(1, content.size());
		Node root = (Node)content.get(0);
		EList<Node> children = root.getContainmentRef1();
		// Checks that "HoldingDeletedNode" is in the model.
		Assert.assertEquals(2, children.size());
		Node firstChildren = children.get(0);
		// Checks that "ReferencedNode" is in the model.
		Assert.assertEquals(1, firstChildren.getContainmentRef1().size());
	}

	/**
	 * Same test described above but this time the merge is done programmatically.
	 */
	@Test
	public void testAcceptConflictDiffWithConflictingDiffWithRequiredByProg() {
		mergerRegistry.getHighestRankingMerger(refChangeDiff).copyLeftToRight(refChangeDiff,
				new BasicMonitor());

		Assert.assertEquals(DifferenceState.MERGED, refChangeDiff.getState());
		Assert.assertEquals(DifferenceState.MERGED, holdingRefDeletionDiff.getState());
		Assert.assertEquals(DifferenceState.MERGED, deletedNodeDeletionDiff.getState());

		// Checks that the content of the right resource is correct.
		Resource rightResource = (Resource)scope.getRight();

		EList<EObject> content = rightResource.getContents();
		Assert.assertEquals(1, content.size());
		Node root = (Node)content.get(0);
		EList<Node> children = root.getContainmentRef1();
		// Checks that "DeletedNode" is in the model.
		Assert.assertEquals(2, children.size());
		Node firstChildren = children.get(0);
		// Checks that "ReferencedNode" is in the model.
		Assert.assertEquals(1, firstChildren.getContainmentRef1().size());
	}

	private IMergeData getMergeData(Diff diff) {
		return (IMergeData)EcoreUtil.getExistingAdapter(diff, IMergeData.class);
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	public class Bug434828InputData extends AbstractInputData {

		private static final String PATH_PREFIX = "data/_434828/"; //$NON-NLS-1$

		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}

}
