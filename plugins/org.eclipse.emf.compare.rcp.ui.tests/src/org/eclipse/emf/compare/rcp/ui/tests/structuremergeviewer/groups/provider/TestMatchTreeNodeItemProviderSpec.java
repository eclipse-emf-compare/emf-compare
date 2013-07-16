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
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.tests.edit.data.ecore.a1.EcoreA1InputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class TestMatchTreeNodeItemProviderSpec extends AbstractTestTreeNodeItemProviderAdapter {

	private static TreeNodeItemProviderSpec itemProvider;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory
				.createTreeNodeAdapter();
	}

	@Test
	public void testGetChildren_EcoreA1() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		
		Collection<?> ePackageMatchChildren = ePackageMatch.getChildren();
		assertEquals(18, ePackageMatchChildren.size());
		Iterable<EObject> ePackageData = transform(ePackageMatchChildren, TREE_NODE_DATA);
		assertEquals(4, size(filter(ePackageData, Diff.class)));
		assertEquals(14, size(filter(ePackageData, Match.class)));
	}

	static TreeNode getEcoreA1_EPackageMatch() throws IOException {
		Comparison comparison = getComparison(new EcoreA1InputData());

		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());
		
		Collection<?> children = itemProvider.getChildren(treeNode);
		
		Iterable<?> matches = filter(children, matchTreeNode);
		return (TreeNode)matches.iterator().next();
	}

	static Predicate<Object> matchTreeNode = new Predicate<Object>() {
		public boolean apply(Object object) {
			if (object instanceof TreeNode) {
				EObject data = ((TreeNode)object).getData();
				if (data instanceof Match) {
					return true;
				}
			}
			return false;
		}
	};
	
	@Test
	public void testGetChildren_AudioVisualItem() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		
		Collection<TreeNode> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackageData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match audioVisualItem_Match = getMatchWithFeatureValue(ePackageData, "name",
				"AudioVisualItem");
		
		TreeNode audioVisualItem_Match_Node = getTreeNode(ePackageMatch, audioVisualItem_Match);
		
		Collection<?> audioVisualItem_MatchChildren = audioVisualItem_Match_Node.getChildren();

		assertEquals(4, audioVisualItem_MatchChildren.size());
		
		Iterable<EObject> audioData = transform(audioVisualItem_MatchChildren, TREE_NODE_DATA);
		
		assertEquals(2, size(filter(audioData, Diff.class)));
		assertEquals(2, size(filter(audioData, Match.class)));
	}

	@Test
	public void testGetChildren_AudioVisualItem_length() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		
		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackageData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match audioVisualItem_Match = getMatchWithFeatureValue(ePackageData, "name",
				"AudioVisualItem");
		
		TreeNode audioVisualItem_Match_Node = getTreeNode(ePackageMatch, audioVisualItem_Match);
		
		Collection<?> audioVisualItem_MatchChildren = audioVisualItem_Match_Node.getChildren();
		
		Iterable<EObject> audioData = transform(audioVisualItem_MatchChildren, TREE_NODE_DATA);
		
		Match audioVisualItem_length_Match = getMatchWithFeatureValue(audioData, "name",
				"length");
		
		TreeNode audioLengthTreeNode = getTreeNode(audioVisualItem_Match_Node, audioVisualItem_length_Match);
		
		Collection<?> audioVisualItem_length_MatchChildren = itemProvider
				.getChildren(audioLengthTreeNode);

		assertEquals(2, audioVisualItem_length_MatchChildren.size());
		
		Iterable<EObject> audioLengthData = transform(audioVisualItem_length_MatchChildren, TREE_NODE_DATA);
		
		assertEquals(2, size(filter(audioLengthData, Diff.class)));
		assertEquals(0, size(filter(audioLengthData, Match.class)));
	}

	@Test
	public void testGetChildren_Book() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match book_Match = getMatchWithFeatureValue(ePackage_MatchChildrenData, "name", "Book");
		
		TreeNode book_Match_Node = getTreeNode(ePackageMatch, book_Match);
		
		Collection<?> book_MatchChildren = book_Match_Node.getChildren();

		assertEquals(6, book_MatchChildren.size());
		
		Iterable<EObject> book_MatchChildrenData = transform(book_MatchChildren, TREE_NODE_DATA);
		
		assertEquals(3, size(filter(book_MatchChildrenData, Diff.class)));
		assertEquals(3, size(filter(book_MatchChildrenData, Match.class)));
	}

	@Test
	public void testGetChildren_BookCategory() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match bookCategory_Match = getMatchWithFeatureValue(ePackage_MatchChildrenData, "name", "BookCategory");
		
		TreeNode bookCategory_Match_Node = getTreeNode(ePackageMatch, bookCategory_Match);
		
		Collection<?> bookCategory_MatchChildren = bookCategory_Match_Node.getChildren();

		assertEquals(7, bookCategory_MatchChildren.size());
		
		Iterable<EObject> bookCategory_MatchChildrenData = transform(bookCategory_MatchChildren, TREE_NODE_DATA);
		
		assertEquals(4, size(filter(bookCategory_MatchChildrenData, Diff.class)));
		assertEquals(3, size(filter(bookCategory_MatchChildrenData, Match.class)));
	}

	@Test
	public void testGetChildren_Borrowable() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match borrowable_Match = getMatchWithFeatureValue(ePackage_MatchChildrenData, "name", "Borrowable");
		
		TreeNode borrowable_Match_Node = getTreeNode(ePackageMatch, borrowable_Match);
		
		Collection<?> borrowable_MatchChildren = borrowable_Match_Node.getChildren();

		assertEquals(3, borrowable_MatchChildren.size());
		
		Iterable<EObject> borrowable_MatchChildrenData = transform(borrowable_MatchChildren, TREE_NODE_DATA);
		
		assertEquals(1, size(filter(borrowable_MatchChildrenData, Diff.class)));
		assertEquals(2, size(filter(borrowable_MatchChildrenData, Match.class)));
	}

	@Test
	public void testGetChildren_Person() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match person_Match = getMatchWithFeatureValue(ePackage_MatchChildrenData, "name", "Person");
		
		TreeNode person_Match_Node = getTreeNode(ePackageMatch, person_Match);
		
		Collection<?> person_MatchChildren = person_Match_Node.getChildren();

		assertEquals(3, person_MatchChildren.size());
		
		Iterable<EObject> person_MatchChildrenData = transform(person_MatchChildren, TREE_NODE_DATA);
		
		assertEquals(3, size(filter(person_MatchChildrenData, Diff.class)));
		assertEquals(0, size(filter(person_MatchChildrenData, Match.class)));
	}
}
