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

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeRunnableImpl;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.spec.ReferenceChangeSpec;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeOppositeRefOneToOne;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.EReference;
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
public class TestBug434827 {

	private Comparison comparison;

	private IMerger.Registry mergerRegistry;

	private Diff deletionDiff;

	private Diff subDiff;

	private Diff oppositeDiff;

	private Resource left;

	/**
	 * Set up the test models. In the test models there is:
	 * <p>
	 * Origin.nodes: In this model we have a Root node which holds:
	 * <ul>
	 * <li>A Node named "HoldingDeletedNode" thats holds another node named "ReferencedNode"</li>
	 * <li>A Node named "HoldingRefNode"</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Left.nodes: Same model as Origin except that we have set a reference destination from "HoldingRef" to
	 * "ReferencedNode".
	 * </p>
	 * <p>
	 * Right.nodes: Same mode as Origin but we have deleted "ReferencedNode".
	 * </p>
	 * 
	 * @throws IOException
	 */
	@Before
	public void setUp() throws IOException {
		Bug434827InputData inputData = new Bug434827InputData();
		left = inputData.getResource("left.nodes"); //$NON-NLS-1$
		Resource right = inputData.getResource("right.nodes"); //$NON-NLS-1$
		Resource origin = inputData.getResource("origin.nodes"); //$NON-NLS-1$
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		comparison = EMFCompare.builder().build().compare(scope);
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		// Keeps track of the 3 differences
		for (Diff diff : comparison.getDifferences()) {
			if (diff instanceof ReferenceChangeSpec) {
				EReference eReference = ((ReferenceChangeSpec)diff).getReference();
				if (NodesPackage.Literals.NODE_OPPOSITE_REF_ONE_TO_ONE__SOURCE.equals(eReference)) {
					subDiff = diff;
				} else if (NodesPackage.Literals.NODE__CONTAINMENT_REF1.equals((eReference))) {
					deletionDiff = diff;
				} else if (NodesPackage.Literals.NODE_OPPOSITE_REF_ONE_TO_ONE__DESTINATION
						.equals(eReference)) {
					oppositeDiff = diff;
				}
			}
		}
	}

	/**
	 * Tests that:
	 * <p>
	 * If you accept the deletion of the node "ReferencedNode" then two other differences are rejected. The
	 * merge data should be set accordingly.
	 * </p>
	 */
	@Test
	public void testMergeDataAfterAcceptingDeletion() {

		Assert.assertNotNull(subDiff);
		Assert.assertNotNull(deletionDiff);
		Assert.assertNotNull(oppositeDiff);

		// Mocks the UI behavior of UI if the deleting diff is selected with cascading diff filter active.
		// The subDiff is not added in this list because it is not considered as a cascading diff.
		ArrayList<Diff> uiDiff = Lists.newArrayList(deletionDiff);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, MergeMode.ACCEPT);
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		Node rootNode = (Node)left.getContents().get(0);
		// Checks that ReferencedNode has been deleted.
		Assert.assertEquals(0, rootNode.getContainmentRef1().get(0).getContainmentRef1().size());

		// Assert merge data
		Assert.assertEquals(DifferenceState.MERGED, deletionDiff.getState());
		Assert.assertEquals(DifferenceState.MERGED, subDiff.getState());
		Assert.assertEquals(DifferenceState.MERGED, oppositeDiff.getState());
		Assert.assertEquals(MergeMode.ACCEPT, getMergeData(deletionDiff).getMergeMode());
		Assert.assertEquals(MergeMode.REJECT, getMergeData(subDiff).getMergeMode());
		Assert.assertEquals(MergeMode.REJECT, getMergeData(oppositeDiff).getMergeMode());

	}

	/**
	 * Tests that:
	 * <p>
	 * If you reject the deletion of the node "ReferencedNode" then two other differences are accepted. The
	 * merge data should be set accordingly.
	 * </p>
	 */
	@Test
	public void testMergeDataAfterRejectingDeletion() {

		Assert.assertNotNull(subDiff);
		Assert.assertNotNull(deletionDiff);
		Assert.assertNotNull(oppositeDiff);

		// Mocks the UI behavior of UI if the deleting diff is selected with cascading diff filter active.
		// The subDiff is not added in this list because it is not considered as a cascading diff.
		ArrayList<Diff> uiDiff = Lists.newArrayList(deletionDiff);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, MergeMode.REJECT);
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		Node rootNode = (Node)left.getContents().get(0);
		// Checks that ReferenceNode node has not been deleted.
		Assert.assertEquals(1, rootNode.getContainmentRef1().get(0).getContainmentRef1().size());
		NodeOppositeRefOneToOne holdingRefNode = (NodeOppositeRefOneToOne)rootNode.getContainmentRef1()
				.get(1);
		NodeOppositeRefOneToOne referencedNode = (NodeOppositeRefOneToOne)rootNode.getContainmentRef1().get(0)
				.getContainmentRef1().get(0);
		// Checks that the opposite reference has been set.
		Assert.assertEquals(holdingRefNode, referencedNode.getSource());
		Assert.assertEquals(referencedNode, holdingRefNode.getDestination());

		// Assert merge data
		Assert.assertEquals(DifferenceState.MERGED, deletionDiff.getState());
		Assert.assertEquals(MergeMode.REJECT, getMergeData(deletionDiff).getMergeMode());
		// The two differences should be Unresolved.
		Assert.assertNull(getMergeData(subDiff));
		Assert.assertNull(getMergeData(oppositeDiff));
		Assert.assertEquals(DifferenceState.UNRESOLVED, subDiff.getState());
		Assert.assertEquals(DifferenceState.UNRESOLVED, oppositeDiff.getState());
	}

	private IMergeData getMergeData(Diff diff) {
		return (IMergeData)EcoreUtil.getExistingAdapter(diff, IMergeData.class);
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	public class Bug434827InputData extends AbstractInputData {

		private static final String PATH_PREFIX = "data/_434827/"; //$NON-NLS-1$

		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}

}
