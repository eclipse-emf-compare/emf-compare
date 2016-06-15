/*******************************************************************************
 * Copyright (c) 2016 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Simon Delisle - bug 486923
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.data.ecore.bug486923.Bug486923InputData;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("restriction")
public class ThreeWayComparisonGroupProviderTest extends AbstractTestTreeNodeItemProviderAdapter {

	private TreeNodeItemProviderSpec itemProvider;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory.createTreeNodeAdapter();
	}

	@Test
	public void testBug486923() throws IOException {
		Comparison comparison = getComparison(new Bug486923InputData());

		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new ThreeWayComparisonGroupProvider());

		// we don't expect a stack overflow when obtaining the children
		itemProvider.getChildren(treeNode);
	}

}
