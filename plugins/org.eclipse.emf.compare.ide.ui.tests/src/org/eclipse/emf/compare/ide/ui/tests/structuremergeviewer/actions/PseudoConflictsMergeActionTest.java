/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.internal.merge.MergeOperation;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"restriction" })
public class PseudoConflictsMergeActionTest extends AbstractTestUITreeNodeItemProviderAdapter {

	private static TreeNodeItemProviderSpec itemProvider;

	private IMerger.Registry mergerRegistry;

	private TreeNode leftAdd;

	private TreeNode rightAdd;

	private TreeNode leftDelete;

	private TreeNode rightDelete;

	private TreeNode leftChange;

	private TreeNode rightChange;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory.createTreeNodeAdapter();
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		IndividualDiffInputData scopeProvider = new IndividualDiffInputData();
		final IComparisonScope scope = new DefaultComparisonScope(
				scopeProvider.getLeftPseudoConflictFullScope(),
				scopeProvider.getRightPseudoConflictFullScope(),
				scopeProvider.getOriginPseudoConflictFullScope());
		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);
		final Comparison comparison = comparisonBuilder.build().compare(scope);

		editingDomain = EMFCompareEditingDomain.create(scopeProvider.getLeftPseudoConflictFullScope(),
				scopeProvider.getRightPseudoConflictFullScope(),
				scopeProvider.getOriginPseudoConflictFullScope());

		TreeNode nodeRootMatch = getNodeRootMatch(comparison);
		EList<Diff> differences = comparison.getDifferences();

		// Get left add difference
		Diff leftAddDiff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD)));
		leftAdd = getTreeNode(nodeRootMatch.getChildren().get(0), leftAddDiff);

		// Get right add difference
		Diff rightAddDiff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD)));
		rightAdd = getTreeNode(nodeRootMatch.getChildren().get(0), rightAddDiff);

		// Get left delete difference
		Diff leftDeleteDiff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.DELETE)));
		leftDelete = getTreeNode(nodeRootMatch.getChildren().get(2), leftDeleteDiff);

		// Get right delete difference
		Diff rightDeleteDiff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.DELETE)));
		rightDelete = getTreeNode(nodeRootMatch.getChildren().get(2), rightDeleteDiff);

		TreeNode nodeD = nodeRootMatch.getChildren().get(1).getChildren().get(0);
		// Get left change difference
		Diff leftChangeDiff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.CHANGE)));
		leftChange = getTreeNode(nodeD, leftChangeDiff);

		// Get right change difference
		Diff rightChangeDiff = Iterators.find(differences.iterator(),
				and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.CHANGE)));
		rightChange = getTreeNode(nodeD, rightChangeDiff);
	}

	@Test
	public void testAcceptLocalLeftEditable() throws Exception {

		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		final TreeNode localAdd = leftAdd;
		final TreeNode remoteAdd = rightAdd;
		final TreeNode localDelete = leftDelete;
		final TreeNode remoteDelete = rightDelete;
		final TreeNode localChange = leftChange;
		final TreeNode remoteChange = rightChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// ACCEPT Local Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertTrue(accept.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				accept.getMergeAction(localAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// ACCEPT Local Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertTrue(accept.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				accept.getMergeAction(localDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// ACCEPT Local Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertTrue(accept.isLeftToRight(localChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				accept.getMergeAction(localChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	@Test
	public void testRejectLocalLeftEditable() throws Exception {

		final MergeMode reject = MergeMode.REJECT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		final TreeNode localAdd = leftAdd;
		final TreeNode remoteAdd = rightAdd;
		final TreeNode localDelete = leftDelete;
		final TreeNode remoteDelete = rightDelete;
		final TreeNode localChange = leftChange;
		final TreeNode remoteChange = rightChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, reject, null);

		// REJECT Local Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertFalse(reject.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, reject.getMergeAction(localAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// REJECT Local Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertFalse(reject.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				reject.getMergeAction(localDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// REJECT Local Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertFalse(reject.isLeftToRight(localChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				reject.getMergeAction(localChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	@Test
	public void testAcceptRemoteLeftEditable() throws Exception {

		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		final TreeNode localAdd = leftAdd;
		final TreeNode remoteAdd = rightAdd;
		final TreeNode localDelete = leftDelete;
		final TreeNode remoteDelete = rightDelete;
		final TreeNode localChange = leftChange;
		final TreeNode remoteChange = rightChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// ACCEPT Remote Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertFalse(accept.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(remoteAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// ACCEPT Remote Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertFalse(accept.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				accept.getMergeAction(remoteDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// ACCEPT Remote Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertFalse(accept.isLeftToRight(remoteChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				accept.getMergeAction(remoteChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	@Test
	public void testRejectRemoteLeftEditable() throws Exception {

		final MergeMode reject = MergeMode.REJECT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		final TreeNode localAdd = leftAdd;
		final TreeNode remoteAdd = rightAdd;
		final TreeNode localDelete = leftDelete;
		final TreeNode remoteDelete = rightDelete;
		final TreeNode localChange = leftChange;
		final TreeNode remoteChange = rightChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, reject, null);

		// REJECT Remote Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertTrue(reject.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				reject.getMergeAction(remoteAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// REJECT Remote Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertTrue(reject.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				reject.getMergeAction(remoteDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// REJECT Remote Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertTrue(reject.isLeftToRight(remoteChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				reject.getMergeAction(remoteChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	@Test
	public void testAcceptLocalRightEditable() throws Exception {

		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = false;
		final boolean rightEditable = true;

		final TreeNode localAdd = rightAdd;
		final TreeNode remoteAdd = leftAdd;
		final TreeNode localDelete = rightDelete;
		final TreeNode remoteDelete = leftDelete;
		final TreeNode localChange = rightChange;
		final TreeNode remoteChange = leftChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// ACCEPT Local Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertFalse(accept.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				accept.getMergeAction(localAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// ACCEPT Local Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertFalse(accept.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				accept.getMergeAction(localDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// ACCEPT Local Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertFalse(accept.isLeftToRight(localChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				accept.getMergeAction(localChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	@Test
	public void testRejectLocalRightEditable() throws Exception {

		final MergeMode reject = MergeMode.REJECT;
		final boolean leftEditable = false;
		final boolean rightEditable = true;

		final TreeNode localAdd = rightAdd;
		final TreeNode remoteAdd = leftAdd;
		final TreeNode localDelete = rightDelete;
		final TreeNode remoteDelete = leftDelete;
		final TreeNode localChange = rightChange;
		final TreeNode remoteChange = leftChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, reject, null);

		// REJECT Local Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertTrue(reject.isLeftToRight(localAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, reject.getMergeAction(localAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// REJECT Local Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertTrue(reject.isLeftToRight(localDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				reject.getMergeAction(localDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// REJECT Local Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertTrue(reject.isLeftToRight(localChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				reject.getMergeAction(localChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(localChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	@Test
	public void testAcceptRemoteRightEditable() throws Exception {

		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = false;
		final boolean rightEditable = true;

		final TreeNode localAdd = rightAdd;
		final TreeNode remoteAdd = leftAdd;
		final TreeNode localDelete = rightDelete;
		final TreeNode remoteDelete = leftDelete;
		final TreeNode localChange = rightChange;
		final TreeNode remoteChange = leftChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// ACCEPT Remote Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertTrue(accept.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE, accept.getMergeAction(remoteAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// ACCEPT Remote Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertTrue(accept.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				accept.getMergeAction(remoteDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// ACCEPT Remote Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertTrue(accept.isLeftToRight(remoteChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MERGE,
				accept.getMergeAction(remoteChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	@Test
	public void testRejectRemoteRightEditable() throws Exception {

		final MergeMode reject = MergeMode.REJECT;
		final boolean leftEditable = false;
		final boolean rightEditable = true;

		final TreeNode localAdd = rightAdd;
		final TreeNode remoteAdd = leftAdd;
		final TreeNode localDelete = rightDelete;
		final TreeNode remoteDelete = leftDelete;
		final TreeNode localChange = rightChange;
		final TreeNode remoteChange = leftChange;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, reject, null);

		// REJECT Remote Add difference
		Diff localAddDiff = (Diff)localAdd.getData();
		Diff remoteAddDiff = (Diff)remoteAdd.getData();
		assertFalse(reject.isLeftToRight(remoteAddDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				reject.getMergeAction(remoteAddDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteAdd));
		action.run();
		assertEquals(DifferenceState.MERGED, localAddDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteAddDiff.getState());

		// REJECT Remote Delete difference
		Diff localDeleteDiff = (Diff)localDelete.getData();
		Diff remoteDeleteDiff = (Diff)remoteDelete.getData();
		assertFalse(reject.isLeftToRight(remoteDeleteDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				reject.getMergeAction(remoteDeleteDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteDelete));
		action.run();
		assertEquals(DifferenceState.MERGED, localDeleteDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteDeleteDiff.getState());

		// REJECT Remote Change difference
		Diff localChangeDiff = (Diff)localChange.getData();
		Diff remoteChangeDiff = (Diff)remoteChange.getData();
		assertFalse(reject.isLeftToRight(remoteChangeDiff, leftEditable, rightEditable));
		assertEquals(MergeOperation.MARK_AS_MERGE,
				reject.getMergeAction(remoteChangeDiff, leftEditable, rightEditable));
		action.updateSelection(new StructuredSelection(remoteChange));
		action.run();
		assertEquals(DifferenceState.MERGED, localChangeDiff.getState());
		assertEquals(DifferenceState.MERGED, remoteChangeDiff.getState());

	}

	private static TreeNode getNodeRootMatch(Comparison comparison) throws IOException {
		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());

		Collection<?> children = itemProvider.getChildren(treeNode);

		Iterable<?> matches = filter(children, matchTreeNode);
		return (TreeNode)matches.iterator().next();
	}
}
