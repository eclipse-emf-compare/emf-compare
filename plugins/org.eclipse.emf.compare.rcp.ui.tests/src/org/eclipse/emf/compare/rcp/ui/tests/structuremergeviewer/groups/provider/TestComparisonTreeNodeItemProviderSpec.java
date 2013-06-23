/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Iterables.transform;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.data.ecore.a1.EcoreA1InputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class TestComparisonTreeNodeItemProviderSpec extends AbstractTestTreeNodeItemProviderAdapter {

	private TreeNodeItemProviderSpec itemProvider;
	
	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory
				.createTreeNodeAdapter();
	}

	@Test
	public void testGetChildren_EcoreA1() throws IOException {
		Comparison comparison = getComparison(new EcoreA1InputData());

		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());
		
		Collection<?> children = itemProvider.getChildren(treeNode);

		assertEquals(2, children.size());
		
		Iterable<EObject> eAllData = transform(children, TREE_NODE_DATA);
		assertEquals(1, size(filter(eAllData, Match.class)));
		assertEquals(1, size(filter(eAllData, MatchResource.class)));
	}
}
