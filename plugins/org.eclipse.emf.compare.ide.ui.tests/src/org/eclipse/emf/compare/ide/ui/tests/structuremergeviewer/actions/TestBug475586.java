/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.IMergeOptionAware;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=475586">475586</a>
 * The test case contains a delete diff on left side (Node A) and and a rename of Node A to Node C on the
 * right side. The Node A references (non-containment) a Node B. When the user accept the merge of the Node C
 * (corresponding to merge from right to left), the diff on Node A must be rejected. If the cascading filter
 * is enabled, the diff on Node B must also be rejected because it is a sub-diff a Node A and it is hidden. If
 * the cascading filter is disabled, the diff on Node B must not be rejected because it is a sub-diff a Node A
 * but it is visible. When the user reject the merge of the Node C (corresponding to mark the diff as merged),
 * the diff on Node A and Node C must be unresolved, whether the cascading filter is enabled or disabled.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"nls", "restriction" })
public class TestBug475586 extends AbstractTestUITreeNodeItemProviderAdapter {

	private static TreeNodeItemProviderSpec itemProvider;

	private IMerger.Registry mergerRegistry;

	private TreeNode containmentRefDeleteA;

	private TreeNode nameSetC;

	private TreeNode singleValuedReferenceUnsetB;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory.createTreeNodeAdapter();
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		Bug475586InputData inputData = new Bug475586InputData();
		final Resource left = inputData.getResource("left.nodes"); //$NON-NLS-1$
		final Resource right = inputData.getResource("right.nodes"); //$NON-NLS-1$
		final Resource ancestor = inputData.getResource("ancestor.nodes"); //$NON-NLS-1$
		final DefaultComparisonScope scope = new DefaultComparisonScope(left, right, ancestor);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		editingDomain = EMFCompareEditingDomain.create(left, right, ancestor);

		TreeNode nodeRoot = getMatchNodeRoot(comparison);
		Collection<?> nodeRootChildren = nodeRoot.getChildren();
		Iterable<EObject> nodeRootChildrenData = transform(nodeRootChildren, TREE_NODE_DATA);

		// Get Node Single Value Reference A [containmentRef1 delete] difference
		ReferenceChange nodeA = getReferenceChangeWithFeatureValue(nodeRootChildrenData, "name", "A");
		containmentRefDeleteA = getTreeNode(nodeRoot, nodeA);

		// Get children of Match Node Single Value Reference A
		Collection<?> nodeAChildren = containmentRefDeleteA.getChildren();
		Iterable<EObject> nodeAChildrenData = transform(nodeAChildren, TREE_NODE_DATA);

		// Get C [name set] difference
		AttributeChange nodeC = getAttributeChangeWithFeatureValue(nodeAChildrenData, "name", "C");
		nameSetC = getTreeNode(containmentRefDeleteA, nodeC);

		// Get Node B [singleValuedReference unset] difference
		ReferenceChange nodeB = getReferenceChangeWithFeatureValue(nodeAChildrenData, "name", "B");
		singleValuedReferenceUnsetB = getTreeNode(containmentRefDeleteA, nodeB);
	}

	@Test
	public void testAcceptWithCascadingFilter() {
		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;
		final boolean cascadingFilter = true;
		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// Accept C [name set] difference
		Diff nodeC = (Diff)nameSetC.getData();
		assertFalse(accept.isLeftToRight(nodeC, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(nodeC, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(nameSetC));
		setCascadingDifferencesFilterEnabled(cascadingFilter);
		action.run();
		assertEquals(DifferenceState.MERGED, nodeC.getState());

		Diff nodeA = (Diff)containmentRefDeleteA.getData();
		assertEquals(DifferenceState.MERGED, nodeA.getState());

		Diff nodeB = (Diff)singleValuedReferenceUnsetB.getData();
		assertEquals(DifferenceState.MERGED, nodeB.getState());
	}

	@Test
	public void testRejectWithCascadingFilter() {
		final MergeMode reject = MergeMode.REJECT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;
		final boolean cascadingFilter = true;
		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, reject, null);

		// Reject C [name set] difference
		Diff nodeC = (Diff)nameSetC.getData();
		assertTrue(reject.isLeftToRight(nodeC, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, reject.getMergeAction(nodeC, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(nameSetC));
		setCascadingDifferencesFilterEnabled(cascadingFilter);
		action.run();
		assertEquals(DifferenceState.MERGED, nodeC.getState());

		Diff nodeA = (Diff)containmentRefDeleteA.getData();
		assertEquals(DifferenceState.UNRESOLVED, nodeA.getState());

		Diff nodeB = (Diff)singleValuedReferenceUnsetB.getData();
		assertEquals(DifferenceState.UNRESOLVED, nodeB.getState());
	}

	@Test
	public void testAcceptWithoutCascadingFilter() {
		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;
		final boolean cascadingFilter = false;
		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// Accept C [name set] difference
		Diff nodeC = (Diff)nameSetC.getData();
		assertFalse(accept.isLeftToRight(nodeC, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(nodeC, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(nameSetC));
		setCascadingDifferencesFilterEnabled(cascadingFilter);
		action.run();
		assertEquals(DifferenceState.MERGED, nodeC.getState());

		Diff nodeA = (Diff)containmentRefDeleteA.getData();
		assertEquals(DifferenceState.MERGED, nodeA.getState());

		Diff nodeB = (Diff)singleValuedReferenceUnsetB.getData();
		assertEquals(DifferenceState.UNRESOLVED, nodeB.getState());
	}

	@Test
	public void testRejectWithoutCascadingFilter() {
		final MergeMode reject = MergeMode.REJECT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;
		final boolean cascadingFilter = false;
		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, reject, null);

		// Reject C [name set] difference
		Diff nodeC = (Diff)nameSetC.getData();
		assertTrue(reject.isLeftToRight(nodeC, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, reject.getMergeAction(nodeC, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(nameSetC));
		setCascadingDifferencesFilterEnabled(cascadingFilter);
		action.run();
		assertEquals(DifferenceState.MERGED, nodeC.getState());

		Diff nodeA = (Diff)containmentRefDeleteA.getData();
		assertEquals(DifferenceState.UNRESOLVED, nodeA.getState());

		Diff nodeB = (Diff)singleValuedReferenceUnsetB.getData();
		assertEquals(DifferenceState.UNRESOLVED, nodeB.getState());
	}

	private void setCascadingDifferencesFilterEnabled(boolean enabled) {
		for (IMergeOptionAware merger : Iterables.filter(this.mergerRegistry.getMergers(null),
				IMergeOptionAware.class)) {
			Map<Object, Object> mergeOptions = merger.getMergeOptions();
			mergeOptions.put(AbstractMerger.SUB_DIFF_AWARE_OPTION, Boolean.valueOf(enabled));
		}
	}

	private static TreeNode getMatchNodeRoot(Comparison comparison) throws IOException {
		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());

		Collection<?> children = itemProvider.getChildren(treeNode);

		Iterable<?> matches = filter(children, matchTreeNode);
		return (TreeNode)matches.iterator().next();
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	public class Bug475586InputData extends AbstractInputData {

		private static final String PATH_PREFIX = "data/_475586/"; //$NON-NLS-1$

		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}
}
