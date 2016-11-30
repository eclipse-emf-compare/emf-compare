/*******************************************************************************
 * Copyright (c) 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Iterables.transform;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ByResourceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.KindGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchResourceNode;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.data.nodes.resourceattachmentchange.ResourceAttachmentChangeInGroupsInputData;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;

@SuppressWarnings("restriction")
public class ResourceAttachmentChangeInGroupsTest extends AbstractTestTreeNodeItemProviderAdapter {

	private Comparison comp;

	@Before
	public void setUp() throws Exception {
		comp = getComparison(new ResourceAttachmentChangeInGroupsInputData());
	}

	/**
	 * Test that a {@link ResourceAttachmentChange} is only display once in
	 * {@link ThreeWayComparisonGroupProvider}. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=508294"</a>
	 */
	@Test
	public void testResourceAttachmentChangeInThreeWayComparisonGroupProvider() {
		ThreeWayComparisonGroupProvider threeWayComparisonGroup = new ThreeWayComparisonGroupProvider();
		Collection<? extends IDifferenceGroup> groups = threeWayComparisonGroup.getGroups(comp);
		assertNotNull(groups);
		assertEquals(3, groups.size());
		IDifferenceGroup[] groupsArray = groups.toArray(new IDifferenceGroup[3]);

		// Conflicts group
		IDifferenceGroup conflictsGroup = groupsArray[0];
		assertEquals(EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.conflicts.label"), //$NON-NLS-1$
				conflictsGroup.getName());
		List<? extends TreeNode> children = conflictsGroup.getChildren();
		assertEquals(0, children.size());

		// Left group
		IDifferenceGroup leftGroup = groupsArray[1];
		assertEquals(EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.left.label"), //$NON-NLS-1$
				leftGroup.getName());
		children = leftGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		TreeNode fragmentMatchResourceNode = children.get(0);
		EList<TreeNode> fragmentMatchResourceNodeChildren = fragmentMatchResourceNode.getChildren();
		assertEquals(1, fragmentMatchResourceNodeChildren.size());
		assertEquals(1, size(filter(fragmentMatchResourceNodeChildren, DiffNode.class)));
		Iterable<EObject> fragmentMatchResourceNodeData = transform(fragmentMatchResourceNodeChildren,
				TREE_NODE_DATA);
		assertEquals(1, Iterables.size(fragmentMatchResourceNodeData));
		assertEquals(1, size(filter(fragmentMatchResourceNodeData, ResourceAttachmentChange.class)));
		TreeNode rootMatchResourceNode = children.get(1);
		assertEquals(0, rootMatchResourceNode.getChildren().size());

		// Right group
		IDifferenceGroup rightGroup = groupsArray[2];
		assertEquals(EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.right.label"), //$NON-NLS-1$
				rightGroup.getName());
		children = rightGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		for (TreeNode treeNode : children) {
			children = treeNode.getChildren();
			assertEquals(0, children.size());
		}
	}

	/**
	 * Test that a {@link ResourceAttachmentChange} is only display once in {@link DefaultGroupProvider}. This
	 * test is related to <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=508294"</a>
	 */
	@Test
	public void testResourceAttachmentChangeInDefaultComparisonGroupProvider() {
		DefaultGroupProvider defaultComparisonGroup = new DefaultGroupProvider();
		Collection<? extends IDifferenceGroup> groups = defaultComparisonGroup.getGroups(comp);
		assertNotNull(groups);
		assertEquals(1, groups.size());
		IDifferenceGroup defaultGroup = groups.iterator().next();
		List<? extends TreeNode> children = defaultGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		TreeNode fragmentMatchResourceNode = children.get(0);
		EList<TreeNode> fragmentMatchResourceNodeChildren = fragmentMatchResourceNode.getChildren();
		assertEquals(1, fragmentMatchResourceNodeChildren.size());
		assertEquals(1, size(filter(fragmentMatchResourceNodeChildren, DiffNode.class)));
		Iterable<EObject> fragmentMatchResourceNodeData = transform(fragmentMatchResourceNodeChildren,
				TREE_NODE_DATA);
		assertEquals(1, Iterables.size(fragmentMatchResourceNodeData));
		assertEquals(1, size(filter(fragmentMatchResourceNodeData, ResourceAttachmentChange.class)));
		TreeNode rootMatchResourceNode = children.get(1);
		assertEquals(0, rootMatchResourceNode.getChildren().size());
	}

	/**
	 * Test that a {@link ResourceAttachmentChange} is only display once in {@link KindGroupProvider}. This
	 * test is related to <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=508294"</a>
	 */
	@Test
	public void testResourceAttachmentChangeInByKindComparisonGroupProvider() {
		KindGroupProvider kindComparisonGroup = new KindGroupProvider();
		Collection<? extends IDifferenceGroup> groups = kindComparisonGroup.getGroups(comp);
		assertNotNull(groups);
		assertEquals(4, groups.size());
		IDifferenceGroup[] groupsArray = groups.toArray(new IDifferenceGroup[4]);

		// Addition group
		IDifferenceGroup additionGroup = groupsArray[0];
		assertEquals(EMFCompareRCPUIMessages.getString("KindGroupProvider.addition.label"), //$NON-NLS-1$
				additionGroup.getName());
		List<? extends TreeNode> children = additionGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		for (TreeNode treeNode : children) {
			children = treeNode.getChildren();
			assertEquals(0, children.size());
		}

		// Deletion group
		IDifferenceGroup deletionGroup = groupsArray[1];
		assertEquals(EMFCompareRCPUIMessages.getString("KindGroupProvider.deletion.label"), //$NON-NLS-1$
				deletionGroup.getName());
		children = deletionGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		TreeNode fragmentMatchResourceNode = children.get(0);
		EList<TreeNode> fragmentMatchResourceNodeChildren = fragmentMatchResourceNode.getChildren();
		assertEquals(1, fragmentMatchResourceNodeChildren.size());
		assertEquals(1, size(filter(fragmentMatchResourceNodeChildren, DiffNode.class)));
		Iterable<EObject> fragmentMatchResourceNodeData = transform(fragmentMatchResourceNodeChildren,
				TREE_NODE_DATA);
		assertEquals(1, Iterables.size(fragmentMatchResourceNodeData));
		assertEquals(1, size(filter(fragmentMatchResourceNodeData, ResourceAttachmentChange.class)));
		TreeNode rootMatchResourceNode = children.get(1);
		assertEquals(0, rootMatchResourceNode.getChildren().size());

		// Change group
		IDifferenceGroup changeGroup = groupsArray[2];
		assertEquals(EMFCompareRCPUIMessages.getString("KindGroupProvider.change.label"), //$NON-NLS-1$
				changeGroup.getName());
		children = changeGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		for (TreeNode treeNode : children) {
			children = treeNode.getChildren();
			assertEquals(0, children.size());
		}

		// Move group
		IDifferenceGroup moveGroup = groupsArray[3];
		assertEquals(EMFCompareRCPUIMessages.getString("KindGroupProvider.move.label"), //$NON-NLS-1$
				moveGroup.getName());
		children = moveGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		for (TreeNode treeNode : children) {
			children = treeNode.getChildren();
			assertEquals(0, children.size());
		}
	}

	/**
	 * Test that a {@link ResourceAttachmentChange} is only display once in {@link ByResourceGroupProvider}.
	 * This test is related to <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=508294"</a>
	 */
	@Test
	public void testResourceAttachmentChangeInByResourceComparisonGroupProvider() {
		ByResourceGroupProvider resourceComparisonGroup = new ByResourceGroupProvider();
		Collection<? extends IDifferenceGroup> groups = resourceComparisonGroup.getGroups(comp);
		assertNotNull(groups);
		assertEquals(1, groups.size());
		IDifferenceGroup byResourceGroup = groups.iterator().next();
		List<? extends TreeNode> children = byResourceGroup.getChildren();
		assertEquals(2, children.size());
		assertEquals(2, size(filter(children, MatchResourceNode.class)));
		TreeNode fragmentMatchResourceNode = children.get(0);
		EList<TreeNode> fragmentMatchResourceNodeChildren = fragmentMatchResourceNode.getChildren();
		assertEquals(1, fragmentMatchResourceNodeChildren.size());
		assertEquals(1, size(filter(fragmentMatchResourceNodeChildren, DiffNode.class)));
		Iterable<EObject> fragmentMatchResourceNodeData = transform(fragmentMatchResourceNodeChildren,
				TREE_NODE_DATA);
		assertEquals(1, Iterables.size(fragmentMatchResourceNodeData));
		assertEquals(1, size(filter(fragmentMatchResourceNodeData, ResourceAttachmentChange.class)));
		TreeNode rootMatchResourceNode = children.get(1);
		assertEquals(0, rootMatchResourceNode.getChildren().size());
	}

}
