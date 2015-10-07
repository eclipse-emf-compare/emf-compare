/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.data.ecore.a1.EcoreA1InputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"nls", "restriction" })
public class MergeActionTest extends AbstractTestUITreeNodeItemProviderAdapter {

	private static TreeNodeItemProviderSpec itemProvider;

	private IMerger.Registry mergerRegistry;

	private TreeNode leftAdd;

	private TreeNode leftDelete;

	private TreeNode rightAdd;

	private TreeNode rightDelete;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory.createTreeNodeAdapter();
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		EcoreA1InputData scopeProvider = new EcoreA1InputData();
		Comparison comparison = getComparison(scopeProvider);

		editingDomain = EMFCompareEditingDomain.create(scopeProvider.getLeft(), scopeProvider.getRight(),
				scopeProvider.getOrigin());

		TreeNode ePackageMatch = getEcoreA1_EPackageMatch(comparison);
		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);

		// Get children of Match Person
		Match person_Match = getMatchWithFeatureValue(ePackage_MatchChildrenData, "name", "Person");
		TreeNode person_Match_Node = getTreeNode(ePackageMatch, person_Match);
		Collection<?> person_MatchChildren = person_Match_Node.getChildren();
		Iterable<EObject> person_MatchChildrenData = transform(person_MatchChildren, TREE_NODE_DATA);

		// Get left add difference
		ReferenceChange fullNameChange = getReferenceChangeWithFeatureValue(person_MatchChildrenData, "name",
				"fullName");
		leftAdd = getTreeNode(person_Match_Node, fullNameChange);

		// Get left delete difference
		ReferenceChange firstNameChange = getReferenceChangeWithFeatureValue(person_MatchChildrenData,
				"name", "firstName");
		leftDelete = getTreeNode(person_Match_Node, firstNameChange);

		// Get children of Match Book
		Match book_Match = getMatchWithFeatureValue(ePackage_MatchChildrenData, "name", "Book");
		TreeNode book_Match_Node = getTreeNode(ePackageMatch, book_Match);
		Collection<?> book_MatchChildren = book_Match_Node.getChildren();
		Iterable<EObject> book_MatchChildrenData = transform(book_MatchChildren, TREE_NODE_DATA);

		// Get right add difference
		ReferenceChange subtitleNameChange = getReferenceChangeWithFeatureValue(book_MatchChildrenData,
				"name", "subtitle");
		rightAdd = getTreeNode(book_Match_Node, subtitleNameChange);

		// Get right delete difference
		ReferenceChange titleNameChange = getReferenceChangeWithFeatureValue(book_MatchChildrenData, "name",
				"title");
		rightDelete = getTreeNode(book_Match_Node, titleNameChange);
	}

	@Test
	public void testAcceptLeftEditable() throws Exception {

		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		final TreeNode localAdd = leftAdd;
		final TreeNode localDelete = leftDelete;
		final TreeNode remoteAdd = rightAdd;
		final TreeNode remoteDelete = rightDelete;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// ACCEPT Local add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		assertTrue(accept.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(localAddDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());

		// ACCEPT Local delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		assertTrue(accept.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(localDeleteDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());

		// ACCEPT Remote add difference
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertFalse(accept.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(remoteAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// ACCEPT Remote delete difference
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertFalse(accept.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(remoteDeleteDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

	}

	@Test
	public void testRejectLeftEditable() throws Exception {

		final MergeMode accept = MergeMode.REJECT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		final TreeNode localAdd = leftAdd;
		final TreeNode localDelete = leftDelete;
		final TreeNode remoteAdd = rightAdd;
		final TreeNode remoteDelete = rightDelete;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// REJECT Local add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		assertFalse(accept.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(localAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());

		// REJECT Local delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		assertFalse(accept.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept
				.getMergeAction(localDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());

		// REJECT Remote add difference
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertTrue(accept.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(remoteAddDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// REJECT Remote delete difference
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertTrue(accept.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(remoteDeleteDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

	}

	@Test
	public void testAcceptRightEditable() throws Exception {

		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = false;
		final boolean rightEditable = true;

		final TreeNode localAdd = rightAdd;
		final TreeNode localDelete = rightDelete;
		final TreeNode remoteAdd = leftAdd;
		final TreeNode remoteDelete = leftDelete;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// ACCEPT Local add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		assertFalse(accept.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(localAddDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());

		// ACCEPT Local delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		assertFalse(accept.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(localDeleteDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());

		// ACCEPT Remote add difference
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertTrue(accept.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(remoteAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// ACCEPT Remote delete difference
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertTrue(accept.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(remoteDeleteDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

	}

	@Test
	public void testRejectRightEditable() throws Exception {

		final MergeMode accept = MergeMode.REJECT;
		final boolean leftEditable = false;
		final boolean rightEditable = true;

		final TreeNode localAdd = rightAdd;
		final TreeNode localDelete = rightDelete;
		final TreeNode remoteAdd = leftAdd;
		final TreeNode remoteDelete = leftDelete;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// REJECT Local add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		assertTrue(accept.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(localAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());

		// REJECT Local delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		assertTrue(accept.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept
				.getMergeAction(localDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());

		// REJECT Remote add difference
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertFalse(accept.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(remoteAddDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// REJECT Remote delete difference
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertFalse(accept.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE, accept.getMergeAction(remoteDeleteDiff, leftEditable,
				rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

	}

	private static TreeNode getEcoreA1_EPackageMatch(Comparison comparison) throws IOException {
		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());

		Collection<?> children = itemProvider.getChildren(treeNode);

		Iterable<?> matches = filter(children, matchTreeNode);
		return (TreeNode)matches.iterator().next();
	}

}
