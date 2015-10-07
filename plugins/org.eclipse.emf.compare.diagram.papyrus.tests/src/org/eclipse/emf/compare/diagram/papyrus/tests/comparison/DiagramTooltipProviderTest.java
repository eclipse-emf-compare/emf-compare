/*******************************************************************************
 * Copyright (C) 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.comparison;

import static com.google.common.collect.Iterables.filter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.internal.EMFCompareDiagramEditMessages;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.comparison.data.DiagramTooltipProviderInputData;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.MockMergeAction;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.provider.TooltipLabelAdapterFactory;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class DiagramTooltipProviderTest extends AbstractTest {

	private static AdapterFactory composedAdapterFactory;

	private DiagramTooltipProviderInputData input = new DiagramTooltipProviderInputData();

	private IMerger.Registry mergerRegistry;

	private ICompareEditingDomain editingDomain;

	private static TreeNodeItemProviderSpec itemProvider;

	private TreeNode moveEdge;

	private EventBus eventBus;

	private TreeItemProviderAdapterFactorySpec treeItemProviderAdapterFactorySpec;

	@SuppressWarnings("restriction")
	@Before
	public void setup() throws Exception {

		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		eventBus = new EventBus();
		treeItemProviderAdapterFactorySpec = new TreeItemProviderAdapterFactorySpec(
				new StructureMergeViewerFilter(eventBus));
		factories.add(treeItemProviderAdapterFactorySpec);
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		factories.add(new TooltipLabelAdapterFactory());
		composedAdapterFactory = new ComposedAdapterFactory(factories);

		itemProvider = (TreeNodeItemProviderSpec) treeItemProviderAdapterFactorySpec
				.createTreeNodeAdapter();
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		final Resource origin = input.getOrigin();
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		editingDomain = EMFCompareEditingDomain.create(left, right, origin);

		Comparison comparison = buildComparison(left, right, origin);
		EList<Diff> differences = comparison.getDifferences();
		Iterator<EdgeChange> iterator = Iterables.filter(differences,
				EdgeChange.class).iterator();
		assertTrue(iterator.hasNext());
		final EdgeChange diff = iterator.next();
		assertFalse(iterator.hasNext());

		// Get the compare editor tree nodes linked with the EdgeChange diff.
		Iterable<TreeNode> nodeRootMatchs = getNodeRootMatch(comparison);
		Predicate<EObject> predicate = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				if (input instanceof TreeNode) {
					TreeNode node = (TreeNode) input;
					if (node.getData() == diff) {
						return true;
					}
				}
				return false;
			}
		};
		for (TreeNode nodeRootMatch : nodeRootMatchs) {
			UnmodifiableIterator<EObject> treeNode = Iterators.filter(
					nodeRootMatch.eAllContents(), predicate);
			if (treeNode != null && treeNode.hasNext()) {
				moveEdge = (TreeNode) treeNode.next();
				break;
			}
		}
	}

	@Test
	public void testReject() {
		final MergeMode accept = MergeMode.REJECT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable,
				rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry,
				accept, null);

		// Get tooltip for a diff on a move of an edge.
		action.updateSelection(new StructuredSelection(moveEdge));
		String tooltipText = action.getToolTipText();
		String expectedtTooltip = EMFCompareDiagramEditMessages
				.getString("reject.change.tooltip");
		assertEquals(expectedtTooltip, tooltipText);
	}

	@Test
	public void testAccept() {
		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable,
				rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry,
				accept, null);

		// Get tooltip for a diff on a move of an edge.
		action.updateSelection(new StructuredSelection(moveEdge));
		String tooltipText = action.getToolTipText();
		String expectedtTooltip = EMFCompareDiagramEditMessages
				.getString("accept.change.tooltip");
		assertEquals(expectedtTooltip, tooltipText);
	}

	@Test
	public void testLeft2Right() {
		final MergeMode accept = MergeMode.LEFT_TO_RIGHT;
		final boolean leftEditable = true;
		final boolean rightEditable = true;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable,
				rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry,
				accept, null);

		// Get tooltip for a diff on a move of an edge.
		action.updateSelection(new StructuredSelection(moveEdge));
		String tooltipText = action.getToolTipText();
		String expectedtTooltip = EMFCompareDiagramEditMessages
				.getString("merged.to.right.tooltip");
		assertEquals(expectedtTooltip, tooltipText);
	}

	@Test
	public void testRight2Left() {
		final MergeMode accept = MergeMode.RIGHT_TO_LEFT;
		final boolean leftEditable = true;
		final boolean rightEditable = true;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable,
				rightEditable);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry,
				accept, null);

		// Get tooltip for a diff on a move of an edge.
		action.updateSelection(new StructuredSelection(moveEdge));
		String tooltipText = action.getToolTipText();
		String expectedtTooltip = EMFCompareDiagramEditMessages
				.getString("merged.to.left.tooltip");
		assertEquals(expectedtTooltip, tooltipText);
	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	private static Iterable<TreeNode> getNodeRootMatch(Comparison comparison)
			throws IOException {
		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());

		Collection<?> children = itemProvider.getChildren(treeNode);

		Iterable<TreeNode> matches = (Iterable<TreeNode>) filter(children,
				matchTreeNode);
		return matches;
	}

	public static Predicate<Object> matchTreeNode = new Predicate<Object>() {
		public boolean apply(Object object) {
			if (object instanceof TreeNode) {
				EObject data = ((TreeNode) object).getData();
				if (data instanceof Match) {
					return true;
				}
			}
			return false;
		}
	};

	private IEMFCompareConfiguration createConfiguration(boolean leftEditable,
			boolean rightEditable) {
		CompareConfiguration cc = new CompareConfiguration();
		cc.setLeftEditable(leftEditable);
		cc.setRightEditable(rightEditable);
		EMFCompareConfiguration emfCC = new EMFCompareConfiguration(cc);
		emfCC.setEditingDomain(editingDomain);
		emfCC.setAdapterFactory(composedAdapterFactory);

		return emfCC;
	}

}
