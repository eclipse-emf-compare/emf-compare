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

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static org.eclipse.emf.compare.internal.EMFCompareEditMessages.getString;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.moved;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions.data.tooltips.NodeTooltipsInputData;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.provider.TooltipLabelAdapterFactory;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"nls", "restriction" })
public class TooltipProviderTest extends AbstractTestUITreeNodeItemProviderAdapter {

	private static AdapterFactory composedAdapterFactory;

	private static TreeNodeItemProviderSpec itemProvider;

	private IMerger.Registry mergerRegistry;

	private TreeNode leftAttributeAdd;

	private TreeNode rightAttributeAdd;

	private TreeNode leftContainmentAdd;

	private TreeNode rightContainmentAdd;

	private TreeNode leftNonContainmentAdd;

	private TreeNode rightNonContainmentAdd;

	private TreeNode leftContainmentDelete;

	private TreeNode rightContainmentDelete;

	private TreeNode leftNonContainmentDelete;

	private TreeNode rightNonContainmentDelete;

	private TreeNode leftPositionMove;

	private TreeNode rightPositionMove;

	private TreeNode leftContainerMove;

	private TreeNode rightContainerMove;

	private TreeNode leftStringSet;

	private TreeNode rightStringSet;

	private TreeNode leftEmptyStringSet;

	private TreeNode rightEmptyStringSet;

	private TreeNode leftReferenceSet;

	private TreeNode rightReferenceSet;

	private TreeNode leftEmptyReferenceSet;

	private TreeNode rightEmptyReferenceSet;

	private TreeNode leftStringUnset;

	private TreeNode rightStringUnset;

	private TreeNode leftReferenceUnset;

	private TreeNode rightReferenceUnset;

	@Override
	@Before
	public void before() throws IOException {
		super.before();

		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
		factories.add(treeItemProviderAdapterFactory);
		factories.add(new TooltipLabelAdapterFactory());
		composedAdapterFactory = new ComposedAdapterFactory(factories);

		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory.createTreeNodeAdapter();
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		NodeTooltipsInputData scopeProvider = new NodeTooltipsInputData();
		final IComparisonScope scope = new DefaultComparisonScope(scopeProvider.getLeft(),
				scopeProvider.getRight(), scopeProvider.getOrigin());
		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);
		final Comparison comparison = comparisonBuilder.build().compare(scope);

		editingDomain = EMFCompareEditingDomain.create(scopeProvider.getLeft(), scopeProvider.getRight(),
				scopeProvider.getOrigin());

		TreeNode nodeRootMatch = getNodeRootMatch(comparison);
		EList<Diff> differences = comparison.getDifferences();

		// Get the compare editor tree nodes linked with each diff
		TreeNode nodeA = nodeRootMatch.getChildren().get(1).getChildren().get(0);
		Diff leftSet = find(differences,
				and(changedAttribute("Root.SetString.A", "singleValuedAttribute", "value1", "value1bis"),
						ofKind(DifferenceKind.CHANGE)));
		leftStringSet = getTreeNode(nodeA, leftSet);

		TreeNode nodeB = nodeRootMatch.getChildren().get(1).getChildren().get(1);
		Diff rightSet = find(differences,
				and(changedAttribute("Root.SetString.B", "singleValuedAttribute", "value2", "value2bis"),
						ofKind(DifferenceKind.CHANGE)));
		rightStringSet = getTreeNode(nodeB, rightSet);

		TreeNode nodeC = nodeRootMatch.getChildren().get(2).getChildren().get(0);
		Diff leftEmptySet = find(differences,
				and(changedAttribute("Root.SetFromEmptyString.C", "singleValuedAttribute", "", "newValue1"),
						ofKind(DifferenceKind.CHANGE)));
		leftEmptyStringSet = getTreeNode(nodeC, leftEmptySet);

		TreeNode nodeD = nodeRootMatch.getChildren().get(2).getChildren().get(1);
		Diff rightEmptySet = find(differences,
				and(changedAttribute("Root.SetFromEmptyString.D", "singleValuedAttribute", "", "newValue2"),
						ofKind(DifferenceKind.CHANGE)));
		rightEmptyStringSet = getTreeNode(nodeD, rightEmptySet);

		TreeNode nodeE = nodeRootMatch.getChildren().get(3).getChildren().get(0);
		Diff leftRefSet = find(differences,
				and(changedReference("Root.SetReference.E", "singleValuedReference", "Root.temp.temp1",
						"Root.temp.temp5"), ofKind(DifferenceKind.CHANGE)));
		leftReferenceSet = getTreeNode(nodeE, leftRefSet);

		TreeNode nodeF = nodeRootMatch.getChildren().get(3).getChildren().get(1);
		Diff rightRefSet = find(differences,
				and(changedReference("Root.SetReference.F", "singleValuedReference", "Root.temp.temp2",
						"Root.temp.temp6"), ofKind(DifferenceKind.CHANGE)));
		rightReferenceSet = getTreeNode(nodeF, rightRefSet);

		TreeNode nodeG = nodeRootMatch.getChildren().get(4).getChildren().get(0);
		Diff leftRefSetEmpty = find(differences, and(changedReference("Root.SetFromEmptyReference.G",
				"singleValuedReference", null, "Root.temp.temp5"), ofKind(DifferenceKind.CHANGE)));
		leftEmptyReferenceSet = getTreeNode(nodeG, leftRefSetEmpty);

		TreeNode nodeH = nodeRootMatch.getChildren().get(4).getChildren().get(1);
		Diff rightRefSetEmpty = find(differences, and(changedReference("Root.SetFromEmptyReference.H",
				"singleValuedReference", null, "Root.temp.temp6"), ofKind(DifferenceKind.CHANGE)));
		rightEmptyReferenceSet = getTreeNode(nodeH, rightRefSetEmpty);

		TreeNode nodeI = nodeRootMatch.getChildren().get(5).getChildren().get(0);
		Diff leftUnset = find(differences,
				and(changedAttribute("Root.UnsetString.I", "singleValuedAttribute", "value3", ""),
						ofKind(DifferenceKind.CHANGE)));
		leftStringUnset = getTreeNode(nodeI, leftUnset);

		TreeNode nodeJ = nodeRootMatch.getChildren().get(5).getChildren().get(1);
		Diff rightUnset = find(differences,
				and(changedAttribute("Root.UnsetString.J", "singleValuedAttribute", "value4", ""),
						ofKind(DifferenceKind.CHANGE)));
		rightStringUnset = getTreeNode(nodeJ, rightUnset);

		TreeNode nodeK = nodeRootMatch.getChildren().get(6).getChildren().get(0);
		Diff leftRefUnset = find(differences, and(
				changedReference("Root.UnsetReference.K", "singleValuedReference", "Root.temp.temp3", null),
				ofKind(DifferenceKind.CHANGE)));
		leftReferenceUnset = getTreeNode(nodeK, leftRefUnset);

		TreeNode nodeL = nodeRootMatch.getChildren().get(6).getChildren().get(1);
		Diff rightRefUnset = find(differences, and(
				changedReference("Root.UnsetReference.L", "singleValuedReference", "Root.temp.temp4", null),
				ofKind(DifferenceKind.CHANGE)));
		rightReferenceUnset = getTreeNode(nodeL, rightRefUnset);

		TreeNode nodeAddAtt1 = nodeRootMatch.getChildren().get(7).getChildren().get(0);
		Diff leftAddAtribute = find(differences,
				and(addedToAttribute("Root.AddAttribute.attribute1", "multiValuedAttribute", "value 1"),
						ofKind(DifferenceKind.ADD)));
		leftAttributeAdd = getTreeNode(nodeAddAtt1, leftAddAtribute);

		TreeNode nodeAddAtt2 = nodeRootMatch.getChildren().get(7).getChildren().get(1);
		Diff rightAddAtribute = find(differences,
				and(addedToAttribute("Root.AddAttribute.attribute2", "multiValuedAttribute", "value 2"),
						ofKind(DifferenceKind.ADD)));
		rightAttributeAdd = getTreeNode(nodeAddAtt2, rightAddAtribute);

		TreeNode nodeAddRef = nodeRootMatch.getChildren().get(8);
		Diff leftAddRef = find(differences,
				and(added("Root.AddReference.newNode1"), ofKind(DifferenceKind.ADD)));
		leftContainmentAdd = getTreeNode(nodeAddRef, leftAddRef);

		Diff rightAddRef = find(differences,
				and(added("Root.AddReference.newNode2"), ofKind(DifferenceKind.ADD)));
		rightContainmentAdd = getTreeNode(nodeAddRef, rightAddRef);

		TreeNode nodeAddRefNonCont1 = nodeRootMatch.getChildren().get(9).getChildren().get(0);
		Diff leftAddNonCont = find(differences,
				and(addedToReference("Root.AddReferenceNonContainment.reference1", "multiValuedReference",
						"Root.temp.temp1"), ofKind(DifferenceKind.ADD)));
		leftNonContainmentAdd = getTreeNode(nodeAddRefNonCont1, leftAddNonCont);

		TreeNode nodeAddRefNonCont2 = nodeRootMatch.getChildren().get(9).getChildren().get(1);
		Diff rightAddNonCont = find(differences,
				and(addedToReference("Root.AddReferenceNonContainment.reference2", "multiValuedReference",
						"Root.temp.temp2"), ofKind(DifferenceKind.ADD)));
		rightNonContainmentAdd = getTreeNode(nodeAddRefNonCont2, rightAddNonCont);

		TreeNode nodeDel = nodeRootMatch.getChildren().get(10);
		Diff leftDelete = find(differences,
				and(removed("Root.DelContainment.M"), ofKind(DifferenceKind.DELETE)));
		leftContainmentDelete = getTreeNode(nodeDel, leftDelete);

		Diff rightDelete = find(differences,
				and(removed("Root.DelContainment.N"), ofKind(DifferenceKind.DELETE)));
		rightContainmentDelete = getTreeNode(nodeDel, rightDelete);

		TreeNode nodeDelNonCont1 = nodeRootMatch.getChildren().get(11).getChildren().get(0);
		Diff leftDeleteNonCont = find(differences,
				and(removedFromReference("Root.DelNonContainment.reference3", "multiValuedReference",
						"Root.temp.temp7"), ofKind(DifferenceKind.DELETE)));
		leftNonContainmentDelete = getTreeNode(nodeDelNonCont1, leftDeleteNonCont);

		TreeNode nodeDelNonCont2 = nodeRootMatch.getChildren().get(11).getChildren().get(1);
		Diff rightDeleteNonCont = find(differences,
				and(removedFromReference("Root.DelNonContainment.reference4", "multiValuedReference",
						"Root.temp.temp8"), ofKind(DifferenceKind.DELETE)));
		rightNonContainmentDelete = getTreeNode(nodeDelNonCont2, rightDeleteNonCont);

		TreeNode nodeMoveCont = nodeRootMatch.getChildren().get(13);
		Diff leftMoveCont = find(differences, and(moved("Root.MoveContainerDestination.O", "containmentRef1"),
				ofKind(DifferenceKind.MOVE)));
		leftContainerMove = getTreeNode(nodeMoveCont, leftMoveCont);

		Diff rightMoveCont = find(differences, and(
				moved("Root.MoveContainerDestination.P", "containmentRef1"), ofKind(DifferenceKind.MOVE)));
		rightContainerMove = getTreeNode(nodeMoveCont, rightMoveCont);

		TreeNode nodeMovePos = nodeRootMatch.getChildren().get(14);
		Diff leftMovePos = find(differences,
				and(moved("Root.MovePosition.Q", "containmentRef1"), ofKind(DifferenceKind.MOVE)));
		leftPositionMove = getTreeNode(nodeMovePos, leftMovePos);

		Diff rightMovePos = find(differences,
				and(moved("Root.MovePosition.S", "containmentRef1"), ofKind(DifferenceKind.MOVE)));
		rightPositionMove = getTreeNode(nodeMovePos, rightMovePos);

	}

	@Test
	public void testReject() {
		final String rejectChange = getString("ContextualTooltip.rejectChange");
		final String leftUnchanged = getString("ContextualTooltip.readonly.leftUnchanged");
		final String leftChanged = getString("ContextualTooltip.readonly.leftChanged");

		final MergeMode accept = MergeMode.REJECT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		emfCC.setAdapterFactory(composedAdapterFactory);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// Get tooltip for the REJECT of a String Set on the Left side
		action.updateSelection(new StructuredSelection(leftStringSet));
		String toolTipText = action.getToolTipText();
		String tooltipBody = getString("ContextualTooltip.set.left.reject", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value1", "value1bis");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a String Set on the Right side
		action.updateSelection(new StructuredSelection(rightStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.reject", "value2", "singleValuedAttribute",
				"NodeSingleValueAttribute");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of an empty String Set on the Left side
		action.updateSelection(new StructuredSelection(leftEmptyStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.reject.empty", "singleValuedAttribute",
				"NodeSingleValueAttribute", "newValue1");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of an empty String Set on the Right side
		action.updateSelection(new StructuredSelection(rightEmptyStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.reject.empty", "singleValuedAttribute",
				"NodeSingleValueAttribute");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.reject", "singleValuedReference",
				"NodeSingleValueReference", "Node temp1", "Node temp5");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.reject", "Node temp2", "singleValuedReference",
				"NodeSingleValueReference");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of an empty Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.reject.empty", "singleValuedReference",
				"NodeSingleValueReference", "Node temp5");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of an empty Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.reject.empty", "singleValuedReference",
				"NodeSingleValueReference");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a String Unset on the Left side
		action.updateSelection(new StructuredSelection(leftStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.reject", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value3");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a String Unset on the Right side
		action.updateSelection(new StructuredSelection(rightStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.reject", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value4");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Reference Unset on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.reject", "singleValuedReference",
				"NodeSingleValueReference", "Node temp3");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a reference Unset on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.reject", "singleValuedReference",
				"NodeSingleValueReference", "Node temp4");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of an Attribute Addition on the Left side
		action.updateSelection(new StructuredSelection(leftAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.left.reject", "value 1",
				"Node Multi Valued Attribute attribute1");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of an Attribute Addition on the Right side
		action.updateSelection(new StructuredSelection(rightAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.right.reject", "value 2",
				"Node Multi Valued Attribute attribute2");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.left.reject", "Node newNode1",
				"Node AddReference");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.right.reject", "Node newNode2",
				"Node AddReference");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Non Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.left.reject", "Node temp1");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Non Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.right.reject", "Node temp2");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Containment reference Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.left.reject",
				"Node Single Value Attribute M", "Node DelContainment");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Containment reference Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.right.reject",
				"Node Single Value Attribute N", "Node DelContainment");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Non Containment Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.left.reject", "Node temp7");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of a Non Containment Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.right.reject", "Node temp8");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of the move of an element to another Container on the Left side
		action.updateSelection(new StructuredSelection(leftContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.left.reject",
				"Node Single Value Attribute O", "Node MoveContainerOrigin", "Node MoveContainerDestination");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of the move of an element to another Container on the Right side
		action.updateSelection(new StructuredSelection(rightContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.right.reject",
				"Node Single Value Attribute P", "Node MoveContainerOrigin");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of the move of an element's position on the Left side
		action.updateSelection(new StructuredSelection(leftPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.left.reject", "Node Q");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the REJECT of the move of an element's position on the Right side
		action.updateSelection(new StructuredSelection(rightPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.right.container.reject", "Node S",
				"Node MovePosition");
		assertEquals(computeTooltip(rejectChange, tooltipBody, leftUnchanged), toolTipText);

		// Get tooltip for the REJECT of multiple changes
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = EMFCompareIDEUIMessages.getString("reject.multiple.changes.tooltip");
		assertEquals(tooltipBody, toolTipText);
		action.clearCache();
	}

	@Test
	public void testAccept() {
		final String acceptChange = getString("ContextualTooltip.acceptChange");
		final String leftUnchanged = getString("ContextualTooltip.readonly.leftUnchanged");
		final String leftChanged = getString("ContextualTooltip.readonly.leftChanged");

		final MergeMode accept = MergeMode.ACCEPT;
		final boolean leftEditable = true;
		final boolean rightEditable = false;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		emfCC.setAdapterFactory(composedAdapterFactory);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// Get tooltip for the ACCEPT of a String Set on the Left side
		action.updateSelection(new StructuredSelection(leftStringSet));
		String toolTipText = action.getToolTipText();
		String tooltipBody = getString("ContextualTooltip.set.left.accept", "value1bis",
				"singleValuedAttribute", "NodeSingleValueAttribute");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a String Set on the Right side
		action.updateSelection(new StructuredSelection(rightStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.accept", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value2bis", "value2");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.accept", "Node temp5", "singleValuedReference",
				"NodeSingleValueReference");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.accept", "singleValuedReference",
				"NodeSingleValueReference", "Node temp6", "Node temp2");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of an empty Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.accept", "Node temp5", "singleValuedReference",
				"NodeSingleValueReference");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of an empty Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.accept.empty", "singleValuedReference",
				"NodeSingleValueReference", "Node temp6");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a String Unset on the Left side
		action.updateSelection(new StructuredSelection(leftStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.accept", "singleValuedAttribute",
				"NodeSingleValueAttribute");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a String Unset on the Right side
		action.updateSelection(new StructuredSelection(rightStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.accept", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value4");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Reference Unset on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.accept", "singleValuedReference",
				"NodeSingleValueReference");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a reference Unset on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.accept", "singleValuedReference",
				"NodeSingleValueReference", "Node temp4");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of an Attribute Addition on the Left side
		action.updateSelection(new StructuredSelection(leftAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.left.accept", "value 1",
				"Node Multi Valued Attribute attribute1");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of an Attribute Addition on the Right side
		action.updateSelection(new StructuredSelection(rightAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.right.accept", "value 2",
				"Node Multi Valued Attribute attribute2");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.left.accept", "Node newNode1",
				"Node AddReference");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.right.accept", "Node newNode2",
				"Node AddReference");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Non Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.left.accept", "Node temp1");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Non Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.right.accept", "Node temp2");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Containment reference Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.left.accept",
				"Node Single Value Attribute M", "Node DelContainment");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Containment reference Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.right.accept",
				"Node Single Value Attribute N", "Node DelContainment");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Non Containment Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.left.accept", "Node temp7");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of a Non Containment Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.right.accept", "Node temp8");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of the move of an element to another Container on the Left side
		action.updateSelection(new StructuredSelection(leftContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.left.accept",
				"Node Single Value Attribute O", "Node MoveContainerDestination");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of the move of an element to another Container on the Right side
		action.updateSelection(new StructuredSelection(rightContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.right.accept",
				"Node Single Value Attribute P", "Node MoveContainerDestination", "Node MoveContainerOrigin");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of the move of an element's position on the Left side
		action.updateSelection(new StructuredSelection(leftPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.left.container.accept", "Node Q",
				"Node MovePosition");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the ACCEPT of the move of an element's position on the Right side
		action.updateSelection(new StructuredSelection(rightPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.right.container.accept", "Node S",
				"Node MovePosition");
		assertEquals(computeTooltip(acceptChange, tooltipBody, leftChanged), toolTipText);

		// Get tooltip for the ACCEPT of multiple changes
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = EMFCompareIDEUIMessages.getString("accept.multiple.changes.tooltip");
		assertEquals(tooltipBody, toolTipText);
		action.clearCache();
	}

	@Test
	public void testLeftToRight() {
		final String rightChanged = getString("ContextualTooltip.editable.rightChanged");

		final MergeMode accept = MergeMode.LEFT_TO_RIGHT;
		final boolean leftEditable = true;
		final boolean rightEditable = true;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		emfCC.setAdapterFactory(composedAdapterFactory);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// Get tooltip for the LEFT_TO_RIGHT copy of a String Set on the Left side
		action.updateSelection(new StructuredSelection(leftStringSet));
		String toolTipText = action.getToolTipText();
		String tooltipBody = getString("ContextualTooltip.set.left.leftToRight", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value1bis", "value1");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a String Set on the Right side
		action.updateSelection(new StructuredSelection(rightStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.leftToRight", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value2", "value2bis");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of an empty String Set on the Right side
		action.updateSelection(new StructuredSelection(rightEmptyStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.leftToRight.empty", "singleValuedAttribute",
				"NodeSingleValueAttribute", "newValue2");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.leftToRight", "singleValuedReference",
				"NodeSingleValueReference", "Node temp5", "Node temp1");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.leftToRight", "singleValuedReference",
				"NodeSingleValueReference", "Node temp2", "Node temp6");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of an empty Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.leftToRight.empty", "singleValuedReference",
				"NodeSingleValueReference", "Node temp5");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of an empty Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.leftToRight.empty", "singleValuedReference",
				"NodeSingleValueReference", "Node temp6");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a String Unset on the Left side
		action.updateSelection(new StructuredSelection(leftStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.leftToRight", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value3");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a String Unset on the Right side
		action.updateSelection(new StructuredSelection(rightStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.leftToRight", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value4");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Reference Unset on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.leftToRight", "singleValuedReference",
				"NodeSingleValueReference", "Node temp3");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a reference Unset on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.leftToRight", "singleValuedReference",
				"NodeSingleValueReference", "Node temp4");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of an Attribute Addition on the Left side
		action.updateSelection(new StructuredSelection(leftAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.left.leftToRight", "value 1",
				"Node Multi Valued Attribute attribute1");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of an Attribute Addition on the Right side
		action.updateSelection(new StructuredSelection(rightAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.right.leftToRight", "value 2",
				"Node Multi Valued Attribute attribute2");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.left.leftToRight", "Node newNode1",
				"Node AddReference");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.right.leftToRight", "Node newNode2",
				"Node AddReference");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Non Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.left.leftToRight", "Node temp1");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Non Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.right.leftToRight", "Node temp2");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Containment reference Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.left.leftToRight",
				"Node Single Value Attribute M", "Node DelContainment");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Containment reference Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.right.leftToRight",
				"Node Single Value Attribute N", "Node DelContainment");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Non Containment Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.left.leftToRight", "Node temp7");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of a Non Containment Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.right.leftToRight", "Node temp8");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of the move of an element to another Container on the Left
		// side
		action.updateSelection(new StructuredSelection(leftContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.left.leftToRight",
				"Node Single Value Attribute O", "Node MoveContainerDestination", "Node MoveContainerOrigin");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of the move of an element to another Container on the Right
		// side
		action.updateSelection(new StructuredSelection(rightContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.right.leftToRight",
				"Node Single Value Attribute P", "Node MoveContainerOrigin", "Node MoveContainerDestination");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of the move of an element's position on the Left side
		action.updateSelection(new StructuredSelection(leftPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.left.leftToRight", "Node Q");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);
		action.clearCache();

		// Get tooltip for the LEFT_TO_RIGHT copy of the move of an element's position on the Right side
		action.updateSelection(new StructuredSelection(rightPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.right.leftToRight", "Node S");
		assertEquals(computeTooltip("", tooltipBody, rightChanged), toolTipText);

		// Get tooltip for the LEFT_TO_RIGHT copy of multiple changes
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = EMFCompareIDEUIMessages.getString("merged.multiple.to.right.tooltip");
		assertEquals(tooltipBody, toolTipText);
		action.clearCache();
	}

	@Test
	public void testRightToLeft() {
		final String rightUnchanged = getString("ContextualTooltip.editable.rightUnchanged");

		final MergeMode accept = MergeMode.RIGHT_TO_LEFT;
		final boolean leftEditable = true;
		final boolean rightEditable = true;

		IEMFCompareConfiguration emfCC = createConfiguration(leftEditable, rightEditable);
		emfCC.setAdapterFactory(composedAdapterFactory);
		MockMergeAction action = new MockMergeAction(emfCC, mergerRegistry, accept, null);

		// Get tooltip for the RIGHT_TO_LEFT copy of a String Set on the Left side
		action.updateSelection(new StructuredSelection(leftStringSet));
		String toolTipText = action.getToolTipText();
		String tooltipBody = getString("ContextualTooltip.set.left.rightToLeft", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value1", "value1bis");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a String Set on the Right side
		action.updateSelection(new StructuredSelection(rightStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.rightToLeft", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value2bis", "value2");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of an empty String Set on the Left side
		action.updateSelection(new StructuredSelection(leftEmptyStringSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.rightToLeft.empty", "singleValuedAttribute",
				"NodeSingleValueAttribute", "newValue1");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.rightToLeft", "singleValuedReference",
				"NodeSingleValueReference", "Node temp1", "Node temp5");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.rightToLeft", "singleValuedReference",
				"NodeSingleValueReference", "Node temp6", "Node temp2");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Reference Set on the Left side
		action.updateSelection(new StructuredSelection(leftEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.left.rightToLeft.empty", "singleValuedReference",
				"NodeSingleValueReference", "Node temp5");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Reference Set on the Right side
		action.updateSelection(new StructuredSelection(rightEmptyReferenceSet));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.set.right.rightToLeft.empty", "singleValuedReference",
				"NodeSingleValueReference", "Node temp6");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a String Unset on the Left side
		action.updateSelection(new StructuredSelection(leftStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.rightToLeft", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value3");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a String Unset on the Right side
		action.updateSelection(new StructuredSelection(rightStringUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.rightToLeft", "singleValuedAttribute",
				"NodeSingleValueAttribute", "value4");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Reference Unset on the Left side
		action.updateSelection(new StructuredSelection(leftReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.left.rightToLeft", "singleValuedReference",
				"NodeSingleValueReference", "Node temp3");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a reference Unset on the Right side
		action.updateSelection(new StructuredSelection(rightReferenceUnset));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.unset.right.rightToLeft", "singleValuedReference",
				"NodeSingleValueReference", "Node temp4");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of an Attribute Addition on the Left side
		action.updateSelection(new StructuredSelection(leftAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.left.rightToLeft", "value 1",
				"Node Multi Valued Attribute attribute1");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of an Attribute Addition on the Right side
		action.updateSelection(new StructuredSelection(rightAttributeAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.attribute.right.rightToLeft", "value 2",
				"Node Multi Valued Attribute attribute2");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.left.rightToLeft", "Node newNode1",
				"Node AddReference");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.containment.right.rightToLeft", "Node newNode2",
				"Node AddReference");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Non Containment reference Addition on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.left.rightToLeft", "Node temp1");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Non Containment reference Addition on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.add.nonContainment.right.rightToLeft", "Node temp2");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Containment reference Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.left.rightToLeft",
				"Node Single Value Attribute M", "Node DelContainment");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Containment reference Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.containment.right.rightToLeft",
				"Node Single Value Attribute N", "Node DelContainment");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Non Containment Deletion on the Left side
		action.updateSelection(new StructuredSelection(leftNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.left.rightToLeft", "Node temp7");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of a Non Containment Deletion on the Right side
		action.updateSelection(new StructuredSelection(rightNonContainmentDelete));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.delete.nonContainment.right.rightToLeft", "Node temp8");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of the move of an element to another Container on the Left
		// side
		action.updateSelection(new StructuredSelection(leftContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.left.rightToLeft",
				"Node Single Value Attribute O", "Node MoveContainerOrigin", "Node MoveContainerDestination");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of the move of an element to another Container on the Right
		// side
		action.updateSelection(new StructuredSelection(rightContainerMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.container.right.rightToLeft",
				"Node Single Value Attribute P", "Node MoveContainerDestination", "Node MoveContainerOrigin");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of the move of an element's position on the Left side
		action.updateSelection(new StructuredSelection(leftPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.left.rightToLeft", "Node Q");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);
		action.clearCache();

		// Get tooltip for the RIGHT_TO_LEFT copy of the move of an element's position on the Right side
		action.updateSelection(new StructuredSelection(rightPositionMove));
		toolTipText = action.getToolTipText();
		tooltipBody = getString("ContextualTooltip.move.position.right.rightToLeft", "Node S");
		assertEquals(computeTooltip("", tooltipBody, rightUnchanged), toolTipText);

		// Get tooltip for the RIGHT_TO_LEFT copy of multiple changes
		action.updateSelection(new StructuredSelection(leftContainmentAdd));
		toolTipText = action.getToolTipText();
		tooltipBody = EMFCompareIDEUIMessages.getString("merged.multiple.to.left.tooltip");
		assertEquals(tooltipBody, toolTipText);
		action.clearCache();
	}

	private static TreeNode getNodeRootMatch(Comparison comparison) throws IOException {
		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());

		Collection<?> children = itemProvider.getChildren(treeNode);

		Iterable<?> matches = filter(children, matchTreeNode);
		return (TreeNode)matches.iterator().next();
	}

	private String computeTooltip(String header, String body, String footer) {
		String separator = "\n";

		StringBuilder builder = new StringBuilder();
		if (!"".equals(header)) {
			builder.append(header);
			builder.append(separator);
		}

		builder.append(body);
		builder.append(separator);
		builder.append(footer);
		return builder.toString();
	}

}
