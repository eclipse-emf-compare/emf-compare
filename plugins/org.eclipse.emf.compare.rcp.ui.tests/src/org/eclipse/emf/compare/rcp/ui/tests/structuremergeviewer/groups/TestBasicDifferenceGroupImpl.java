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
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups;

import static com.google.common.base.Predicates.alwaysTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.data.nodes.addconflict.NodesAddConflictInputData;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;
import org.eclipse.emf.compare.tests.edit.data.ecore.a1.EcoreA1InputData;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.Test;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class TestBasicDifferenceGroupImpl extends AbstractTestTreeNodeItemProviderAdapter {

	private AdapterFactoryItemDelegator itemDelegator;

	@Override
	public void before() throws IOException {
		super.before();

		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		factories.add(treeItemProviderAdapterFactory);
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());

		final AdapterFactory composedAdapterFactory = new ComposedAdapterFactory(factories);
		itemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
	}

	@Test
	public void testEcoreA1() throws IOException {
		Comparison comparison = getComparison(new EcoreA1InputData());
		ECrossReferenceAdapter crossReferenceAdapter = new ECrossReferenceAdapter() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
			 */
			@Override
			protected boolean isIncluded(EReference eReference) {
				return eReference == TreePackage.Literals.TREE_NODE__DATA;
			}
		};
		IDifferenceGroup group = new BasicDifferenceGroupImpl(comparison, alwaysTrue(),
				crossReferenceAdapter);

		List<? extends TreeNode> roots = group.getChildren();
		assertEquals(2, roots.size());

		TreeNode node = roots.get(0);

		checkText(node, "extlibrary");

		List<? extends TreeNode> extlibraryChildren = node.getChildren();
		assertEquals(10, extlibraryChildren.size());

		// Book
		node = extlibraryChildren.get(0);
		checkText(node, "Book -> CirculatingItem");

		List<? extends TreeNode> bookChildren = node.getChildren();
		assertEquals(3, bookChildren.size());

		node = bookChildren.get(0);
		checkText(node, "TitledItem [eSuperTypes add]");
		assertEquals(0, node.getChildren().size());

		TreeNode titleNode = bookChildren.get(1);
		checkText(titleNode, "title : EString");

		List<TreeNode> bookTitleChildren = titleNode.getChildren();
		assertEquals(2, bookTitleChildren.size());
		node = bookTitleChildren.get(0);
		checkText(node, "title : EString [eStructuralFeatures delete]");
		assertEquals(0, node.getChildren().size());
		node = bookTitleChildren.get(1);
		checkText(node, "EString [java.lang.String] [eType unset]");
		assertEquals(0, node.getChildren().size());

		TreeNode subtitleNode = bookChildren.get(2);
		checkText(subtitleNode, "subtitle : EString");
		assertEquals(0, node.getChildren().size());

		List<TreeNode> bookSubtitleChildren = subtitleNode.getChildren();
		assertEquals(2, bookSubtitleChildren.size());
		node = bookSubtitleChildren.get(0);
		checkText(node, "subtitle : EString [eStructuralFeatures add]");
		assertEquals(0, node.getChildren().size());
		node = bookSubtitleChildren.get(1);
		checkText(node, "EString [java.lang.String] [eType set]");
		assertEquals(0, node.getChildren().size());

		// BookCategory
		node = extlibraryChildren.get(1);
		checkText(node, "BookCategory");
		assertEquals(4, node.getChildren().size());

		TreeNode childNode = node.getChildren().get(0);
		checkText(childNode, "Encyclopedia = 3");
		assertEquals(1, childNode.getChildren().size());
		checkText(childNode.getChildren().get(0), "Encyclopedia = 3 [eLiterals add]");

		childNode = node.getChildren().get(1);
		checkText(childNode, "Dictionary = 4");
		assertEquals(1, childNode.getChildren().size());
		checkText(childNode.getChildren().get(0), "Dictionary = 4 [eLiterals add]");

		childNode = node.getChildren().get(2);
		checkText(childNode, "Manga = 3");
		assertEquals(1, childNode.getChildren().size());
		checkText(childNode.getChildren().get(0), "Manga = 3 [eLiterals add]");

		childNode = node.getChildren().get(3);
		checkText(childNode, "Manhwa = 5");
		assertEquals(1, childNode.getChildren().size());
		checkText(childNode.getChildren().get(0), "Manhwa = 5 [eLiterals add]");

		// Borrowable
		node = extlibraryChildren.get(2);
		checkText(node, "Borrowable");
		assertEquals(1, node.getChildren().size());
		checkText(node.getChildren().get(0), "Borrowable [name changed]");

		// AudioVisualItem
		node = extlibraryChildren.get(3);
		checkText(node, "AudioVisualItem -> CirculatingItem");
		assertEquals(3, node.getChildren().size());

		childNode = node.getChildren().get(0);
		checkText(childNode, "TitledItem [eSuperTypes add]");
		assertEquals(0, childNode.getChildren().size());

		childNode = node.getChildren().get(1);
		checkText(childNode, "title : EString");
		assertEquals(2, childNode.getChildren().size());
		checkText(childNode.getChildren().get(0), "title : EString [eStructuralFeatures delete]");
		checkText(childNode.getChildren().get(1), "EString [java.lang.String] [eType unset]");

		childNode = node.getChildren().get(2);
		checkText(childNode, "length : EInt");
		assertEquals(2, childNode.getChildren().size());
		checkText(childNode.getChildren().get(0), "length [name changed]");
		checkText(childNode.getChildren().get(1), "minutes [name changed]");

		// BookOnTape
		node = extlibraryChildren.get(4);
		checkText(node, "BookOnTape -> AudioVisualItem");
		assertEquals(1, node.getChildren().size());

		node = node.getChildren().get(0);
		checkText(node, "reader : Person");
		assertEquals(4, node.getChildren().size());

		childNode = node.getChildren().get(0);
		checkText(childNode, "reader : Person [eStructuralFeatures delete]");
		assertEquals(0, childNode.getChildren().size());

		childNode = node.getChildren().get(1);
		checkText(childNode, "reader : Person [eStructuralFeatures delete]");
		assertEquals(0, childNode.getChildren().size());

		childNode = node.getChildren().get(2);
		checkText(childNode, "Person -> Addressable [eType unset]");
		assertEquals(0, childNode.getChildren().size());

		childNode = node.getChildren().get(3);
		checkText(childNode, "Person -> Addressable [eType unset]");
		assertEquals(0, childNode.getChildren().size());

		// Person
		node = extlibraryChildren.get(5);
		checkText(node, "Person -> Addressable");
		assertEquals(3, node.getChildren().size());
		// Person.fullName
		childNode = node.getChildren().get(0);
		checkText(childNode, "fullName : EString");
		assertEquals(2, childNode.getChildren().size());

		TreeNode childNode2 = childNode.getChildren().get(0);
		checkText(childNode2, "fullName : EString [eStructuralFeatures add]");
		assertEquals(0, childNode2.getChildren().size());

		childNode2 = childNode.getChildren().get(1);
		checkText(childNode2, "EString [java.lang.String] [eType set]");
		assertEquals(0, childNode2.getChildren().size());
		// Person.firstName
		childNode = node.getChildren().get(1);
		checkText(childNode, "firstName : EString");
		assertEquals(2, childNode.getChildren().size());

		childNode2 = childNode.getChildren().get(0);
		checkText(childNode2, "firstName : EString [eStructuralFeatures delete]");
		assertEquals(0, childNode2.getChildren().size());

		childNode2 = childNode.getChildren().get(1);
		checkText(childNode2, "EString [java.lang.String] [eType unset]");
		assertEquals(0, childNode2.getChildren().size());
		// Person.lastName
		childNode = node.getChildren().get(2);
		checkText(childNode, "lastName : EString");
		assertEquals(3, childNode.getChildren().size());

		childNode2 = childNode.getChildren().get(0);
		checkText(childNode2, "lastName : EString [eStructuralFeatures delete]");
		assertEquals(0, childNode2.getChildren().size());

		childNode2 = childNode.getChildren().get(1);
		checkText(childNode2, "EString [java.lang.String] [eType unset]");
		assertEquals(0, childNode2.getChildren().size());

		childNode2 = childNode.getChildren().get(2);
		checkText(childNode2, "familyName [name set]");
		assertEquals(0, childNode2.getChildren().size());

		// Magazine
		node = extlibraryChildren.get(6);
		checkText(node, "Magazine -> CirculatingItem");
		assertEquals(4, node.getChildren().size());
		// eClassifiers add
		childNode = node.getChildren().get(0);
		checkText(childNode, "Magazine -> CirculatingItem [eClassifiers add]");
		assertEquals(0, childNode.getChildren().size());
		// eSuperTypes add
		childNode = node.getChildren().get(1);
		checkText(childNode, "CirculatingItem -> Item, Borrowable [eSuperTypes add]");
		assertEquals(0, childNode.getChildren().size());
		// Magazine.title
		childNode = node.getChildren().get(2);
		checkText(childNode, "title : EString");
		assertEquals(2, childNode.getChildren().size());

		childNode2 = childNode.getChildren().get(0);
		checkText(childNode2, "title : EString [eStructuralFeatures add]");
		assertEquals(0, childNode2.getChildren().size());

		childNode2 = childNode.getChildren().get(1);
		checkText(childNode2, "EString [java.lang.String] [eType set]");
		assertEquals(0, childNode2.getChildren().size());
		// Magazine.pages
		childNode = node.getChildren().get(3);
		checkText(childNode, "pages : EInt");
		assertEquals(2, childNode.getChildren().size());

		childNode2 = childNode.getChildren().get(0);
		checkText(childNode2, "pages : EInt [eStructuralFeatures add]");
		assertEquals(0, childNode2.getChildren().size());

		childNode2 = childNode.getChildren().get(1);
		checkText(childNode2, "EInt [int] [eType set]");
		assertEquals(0, childNode2.getChildren().size());

		// Periodical
		node = extlibraryChildren.get(7);
		checkText(node, "Periodical -> Item");
		assertEquals(5, node.getChildren().size());
		// eClassifiers add
		childNode = node.getChildren().get(0);
		checkText(childNode, "Periodical -> Item [eClassifiers delete]");
		assertEquals(0, childNode.getChildren().size());
		// eSuperTypes add
		childNode = node.getChildren().get(1);
		checkText(childNode, "TitledItem [eSuperTypes add]");
		assertEquals(0, childNode.getChildren().size());
		// eSuperTypes delete
		childNode = node.getChildren().get(2);
		checkText(childNode, "Item [eSuperTypes delete]");
		assertEquals(0, childNode.getChildren().size());

		// Periodical.issuesPerYear
		childNode = node.getChildren().get(3);
		checkText(childNode, "issuesPerYear : EInt");
		assertEquals(2, childNode.getChildren().size());
		childNode2 = childNode.getChildren().get(0);
		checkText(childNode2, "issuesPerYear : EInt [eStructuralFeatures delete]");
		assertEquals(0, childNode2.getChildren().size());
		childNode2 = childNode.getChildren().get(1);
		checkText(childNode2, "EInt [int] [eType unset]");
		assertEquals(0, childNode2.getChildren().size());

		// Periodical.title
		childNode = node.getChildren().get(4);
		checkText(childNode, "title : EString");
		assertEquals(4, childNode.getChildren().size());
		checkText(childNode.getChildren().get(0), "title : EString [eStructuralFeatures delete]");
		checkText(childNode.getChildren().get(1), "title : EString [eStructuralFeatures delete]");
		checkText(childNode.getChildren().get(2), "EString [java.lang.String] [eType unset]");
		checkText(childNode.getChildren().get(3), "EString [java.lang.String] [eType unset]");

		// Magazine (from the right)
		node = extlibraryChildren.get(8);
		checkText(node, "Magazine -> Periodical");
		assertEquals(2, node.getChildren().size());
		childNode = node.getChildren().get(0);
		checkText(childNode, "Magazine -> Periodical [eClassifiers add]");
		assertEquals(0, childNode.getChildren().size());
		childNode = node.getChildren().get(1);
		checkText(childNode, "Periodical -> Item, TitledItem [eSuperTypes add]");
		assertEquals(0, childNode.getChildren().size());

		// TitledItem
		node = extlibraryChildren.get(9);
		checkText(node, "TitledItem");
		assertEquals(2, node.getChildren().size());
		childNode = node.getChildren().get(0);
		checkText(childNode, "TitledItem [eClassifiers add]");
		assertEquals(0, childNode.getChildren().size());
		childNode = node.getChildren().get(1);
		checkText(childNode, "title : EString");
		assertEquals(2, childNode.getChildren().size());
		childNode2 = childNode.getChildren().get(0);
		checkText(childNode2, "title : EString [eStructuralFeatures add]");
		assertEquals(0, childNode2.getChildren().size());
		childNode2 = childNode.getChildren().get(1);
		checkText(childNode2, "EString [java.lang.String] [eType set]");
		assertEquals(0, childNode2.getChildren().size());
	}

	@Test
	public void testNodesRealAddConflict() throws IOException {
		Comparison comparison = getComparison(new NodesAddConflictInputData());
		ECrossReferenceAdapter crossReferenceAdapter = new ECrossReferenceAdapter() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
			 */
			@Override
			protected boolean isIncluded(EReference eReference) {
				return eReference == TreePackage.Literals.TREE_NODE__DATA;
			}
		};
		IDifferenceGroup group = new BasicDifferenceGroupImpl(comparison, alwaysTrue(),
				crossReferenceAdapter);

		List<? extends TreeNode> roots = group.getChildren();
		assertEquals(2, roots.size());

		TreeNode node = roots.get(0);

		checkText(node, "Node root");

		List<? extends TreeNode> nodeRootChildren = node.getChildren();
		assertEquals(3, nodeRootChildren.size());

		node = nodeRootChildren.get(0);
		checkText(node, "Node A");

		List<? extends TreeNode> aChildren = node.getChildren();
		assertEquals(1, aChildren.size());

		TreeNode nodeC1 = aChildren.get(0);
		checkText(nodeC1, "Node C");
		assertEquals(4, nodeC1.getChildren().size());

		node = nodeC1.getChildren().get(0);
		checkText(node, "Node C [containmentRef1 add]");

		node = nodeC1.getChildren().get(1);
		checkText(node, "Node D");
		assertEquals(2, node.getChildren().size());
		TreeNode childNode = node.getChildren().get(0);
		checkText(childNode, "Node D [containmentRef1 add]");
		childNode = node.getChildren().get(1);
		checkText(childNode, "Node D [containmentRef1 add]");

		node = nodeC1.getChildren().get(2);
		checkText(node, "Node E");
		assertEquals(1, node.getChildren().size());
		childNode = node.getChildren().get(0);
		checkText(childNode, "Node E [containmentRef1 add]");

		node = nodeC1.getChildren().get(3);
		checkText(node, "Node F");
		assertEquals(1, node.getChildren().size());
		childNode = node.getChildren().get(0);
		checkText(childNode, "Node F [containmentRef1 add]");

		node = nodeRootChildren.get(1);
		checkText(node, "Node B");
		assertEquals(1, node.getChildren().size());
		node = node.getChildren().get(0);
		checkText(node, "Node C");
		assertEquals(1, node.getChildren().size());
		node = node.getChildren().get(0);
		checkText(node, "Node C [containmentRef1 add]");
		assertEquals(0, node.getChildren().size());

		node = nodeRootChildren.get(2);
		checkText(node, "Node G");
		assertEquals(1, node.getChildren().size());
		node = node.getChildren().get(0);
		checkText(node, "Node H");
		assertEquals(1, node.getChildren().size());
		node = node.getChildren().get(0);
		checkText(node, "Node H [containmentRef1 add]");
		assertEquals(0, node.getChildren().size());

		node = roots.get(1);
		checkText(node, "left.nodes <-> right.nodes (ancestor.nodes)");
		assertEquals(0, node.getChildren().size());
	}

	protected void checkText(TreeNode childNode, String expected) {
		assertEquals(expected, itemDelegator.getText(childNode));
	}
}
