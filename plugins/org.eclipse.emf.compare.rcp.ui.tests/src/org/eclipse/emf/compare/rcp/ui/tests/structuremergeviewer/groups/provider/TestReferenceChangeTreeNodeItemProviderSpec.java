/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
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
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.transform;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.data.ecore.a1.EcoreA1InputData;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class TestReferenceChangeTreeNodeItemProviderSpec extends AbstractTestTreeNodeItemProviderAdapter {

	private static TreeNodeItemProviderSpec itemProvider;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory
				.createTreeNodeAdapter();
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
	
	@Test
	public void testGetChildren_AudioVisualItem() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackageData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match audioVisualItem_Match = getMatchWithFeatureValue(ePackageData, "name",
				"AudioVisualItem");
		
		TreeNode audioVisualItem_Match_Node = getTreeNode(ePackageMatch, audioVisualItem_Match);
		
		Collection<?> audioVisualItem_MatchChildren = audioVisualItem_Match_Node.getChildren();

		Iterable<EObject> audioVisualItemChildrenData = transform(audioVisualItem_MatchChildren, TREE_NODE_DATA);
		
		ReferenceChange titleReferenceChange = getReferenceChangeWithFeatureValue(
				audioVisualItemChildrenData, "name", "title");

		TreeNode titleReferenceChange_Node = getTreeNode(audioVisualItem_Match_Node, titleReferenceChange);
		
		Collection<?> titleReferenceChange_Children = titleReferenceChange_Node.getChildren();

		assertEquals(1, titleReferenceChange_Children.size());
		Object child = get(titleReferenceChange_Children, 0);
		
		assertTrue(child instanceof TreeNode);
		assertTrue(((TreeNode)child).getData() instanceof ReferenceChange);
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, ((ReferenceChange)((TreeNode)child).getData()).getReference());

		ReferenceChange titledItemReferenceChange = getReferenceChangeWithFeatureValue(
				audioVisualItemChildrenData, "name", "TitledItem");
		
		TreeNode titledItemReferenceChange_Node = getTreeNode(audioVisualItem_Match_Node, titledItemReferenceChange);
		
		Collection<?> titledItemReferenceChange_Children = titledItemReferenceChange_Node.getChildren();
		assertEquals(0, titledItemReferenceChange_Children.size());
	}

	@Test
	public void testGetChildren_Book() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		
		Iterable<EObject> ePackageData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Match book_Match = getMatchWithFeatureValue(ePackageData, "name", "Book");
		
		TreeNode book_Match_Node = getTreeNode(ePackageMatch, book_Match);
		
		Collection<?> book_MatchChildren = book_Match_Node.getChildren();

		Iterable<EObject> book_MatchChildrenData = transform(book_MatchChildren, TREE_NODE_DATA);
		
		ReferenceChange subtitleReferenceChange = getReferenceChangeWithFeatureValue(book_MatchChildrenData,
				"name", "subtitle");

		TreeNode subtitleReferenceChange_Node = getTreeNode(book_Match_Node, subtitleReferenceChange);
		
		Collection<?> subtitleReferenceChange_Children = subtitleReferenceChange_Node.getChildren();

		assertEquals(1, subtitleReferenceChange_Children.size());
		Notifier child = (Notifier)get(subtitleReferenceChange_Children, 0);
		assertTrue(child instanceof TreeNode);
		assertTrue(((TreeNode)child).getData() instanceof ReferenceChange);
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, ((ReferenceChange)((TreeNode)child).getData()).getReference());
		assertTrue(((TreeNode)child).getChildren().isEmpty());

		ReferenceChange titleReferenceChange = getReferenceChangeWithFeatureValue(book_MatchChildrenData, "name",
				"title");
		
		TreeNode titleReferenceChange_Node = getTreeNode(book_Match_Node, titleReferenceChange);
		
		Collection<?> titleReferenceChange_Children = titleReferenceChange_Node.getChildren();
		assertEquals(1, titleReferenceChange_Children.size());
		child = (Notifier)get(titleReferenceChange_Children, 0);
		assertTrue(child instanceof TreeNode);
		assertTrue(((TreeNode)child).getData() instanceof ReferenceChange);
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, ((ReferenceChange)((TreeNode)child).getData()).getReference());
		assertTrue(((TreeNode)child).getChildren().isEmpty());

		ReferenceChange titledItemReferenceChange = getReferenceChangeWithFeatureValue(book_MatchChildrenData,
				"name", "TitledItem");
		
		TreeNode titledItemReferenceChange_Node = getTreeNode(book_Match_Node, titledItemReferenceChange);
		
		Collection<?> titledItemReferenceChange_Children = titledItemReferenceChange_Node.getChildren();
		assertEquals(0, titledItemReferenceChange_Children.size());
	}

	@Test
	public void testGetChildren_Borrowable() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		Iterable<EObject> ePackageData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		Match borrowableCategory_Match = getMatchWithFeatureValue(ePackageData, "name",
				"Borrowable");
		TreeNode borrowableCategory_Match_Node = getTreeNode(ePackageMatch, borrowableCategory_Match);
		Collection<?> borrowable_MatchChildren = borrowableCategory_Match_Node.getChildren();

		assertEquals(3, borrowable_MatchChildren.size());
	}

	@Test
	public void testGetChildren_BookCategory() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		Iterable<EObject> ePackageData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		Match bookCategory_Match = getMatchWithFeatureValue(ePackageData, "name", "BookCategory");
		TreeNode bookCategory_Match_Node = getTreeNode(ePackageMatch, bookCategory_Match);
		Collection<?> bookCategory_MatchChildren = bookCategory_Match_Node.getChildren();

		Iterable<EObject> bookCategory_MatchChildrenData = transform(bookCategory_MatchChildren, TREE_NODE_DATA);
		
		ReferenceChange dictionaryReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildrenData, "name", "Dictionary");
		ReferenceChange encyclopediaReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildrenData, "name", "Encyclopedia");
		ReferenceChange mangaReferenceChange = getReferenceChangeWithFeatureValue(bookCategory_MatchChildrenData,
				"name", "Manga");
		ReferenceChange manhwaReferenceChange = getReferenceChangeWithFeatureValue(
				bookCategory_MatchChildrenData, "name", "Manhwa");

		TreeNode dictionaryReferenceChange_Node = getTreeNode(bookCategory_Match_Node, dictionaryReferenceChange);
		assertTrue(dictionaryReferenceChange_Node.getChildren().isEmpty());
		
		TreeNode encyclopediaReferenceChange_Node = getTreeNode(bookCategory_Match_Node, encyclopediaReferenceChange);
		assertTrue(encyclopediaReferenceChange_Node.getChildren().isEmpty());
		
		TreeNode mangaReferenceChange_Node = getTreeNode(bookCategory_Match_Node, mangaReferenceChange);
		assertTrue(mangaReferenceChange_Node.getChildren().isEmpty());
		
		TreeNode manhwaReferenceChange_Node = getTreeNode(bookCategory_Match_Node, manhwaReferenceChange);
		assertTrue(manhwaReferenceChange_Node.getChildren().isEmpty());
	}

	@Test
	public void testGetChildren_Magazine1() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();

		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Collection<?> magazineChildren = null;
		TreeNode referenceChange_Node = null;
		for (ReferenceChange referenceChange : filter(ePackage_MatchChildrenData, ReferenceChange.class)) {
			EClass eClass = (EClass)referenceChange.getValue();
			if ("Magazine".equals(eClass.getName())
					&& "CirculatingItem".equals(eClass.getESuperTypes().get(0).getName())) {
				referenceChange_Node = getTreeNode(ePackageMatch, referenceChange);
				magazineChildren = referenceChange_Node.getChildren();
				assertEquals(3, magazineChildren.size());
				break;
			}
		}
		
		Iterable<EObject> magazineChildrenData = transform(magazineChildren, TREE_NODE_DATA);
		
		ReferenceChange magazineSuperTypeChange = getReferenceChangeWithFeatureValue(magazineChildrenData,
				"name", "CirculatingItem");
		TreeNode magazineSuperTypeChange_Node = getTreeNode(referenceChange_Node, magazineSuperTypeChange);
		assertTrue(magazineSuperTypeChange_Node.getChildren().isEmpty());

		ReferenceChange magazineSFChange1 = getReferenceChangeWithFeatureValue(magazineChildrenData, "name",
				"pages");
		TreeNode magazineSFChange1_Node = getTreeNode(referenceChange_Node, magazineSFChange1);
		assertEquals(1, magazineSFChange1_Node.getChildren().size());

		ReferenceChange magazineSFChange2 = getReferenceChangeWithFeatureValue(magazineChildrenData, "name",
				"title");
		TreeNode magazineSFChange2_Node = getTreeNode(referenceChange_Node, magazineSFChange2);
		assertEquals(1, magazineSFChange2_Node.getChildren().size());
	}

	@Test
	public void testGetChildren_Magazine2() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		Collection<?> magazineChildren = null;
		TreeNode referenceChange_Node = null;
		for (ReferenceChange referenceChange : filter(ePackage_MatchChildrenData, ReferenceChange.class)) {
			EClass eClass = (EClass)referenceChange.getValue();
			if ("Magazine".equals(eClass.getName())
					&& "Periodical".equals(eClass.getESuperTypes().get(0).getName())) {
				referenceChange_Node = getTreeNode(ePackageMatch, referenceChange);
				magazineChildren = referenceChange_Node.getChildren();
				assertEquals(1, magazineChildren.size());
				break;
			}
		}
		
		Iterable<EObject> magazineChildrenData = transform(magazineChildren, TREE_NODE_DATA);
		
		ReferenceChange magazineSuperTypeChange = getReferenceChangeWithFeatureValue(magazineChildrenData,
				"name", "Periodical");
		TreeNode magazineSuperTypeChange_Node = getTreeNode(referenceChange_Node, magazineSuperTypeChange);
		assertTrue(magazineSuperTypeChange_Node.getChildren().isEmpty());
	}

	@Test
	public void testGetChildren_Periodical() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		ReferenceChange periodical_ReferenceChange = getReferenceChangeWithFeatureValue(
				ePackage_MatchChildrenData, "name", "Periodical");
		TreeNode periodical_ReferenceChange_Node = getTreeNode(ePackageMatch, periodical_ReferenceChange);
		
		Collection<?> periodical_ReferenceChangeChildren = periodical_ReferenceChange_Node.getChildren();

		assertEquals(5, periodical_ReferenceChangeChildren.size());

		Iterable<EObject> periodical_ReferenceChangeChildrenData = transform(periodical_ReferenceChangeChildren, TREE_NODE_DATA);
		
		ReferenceChange issuesPerYearChange = getReferenceChangeWithFeatureValue(
				periodical_ReferenceChangeChildrenData, "name", "issuesPerYear");

		ReferenceChange itemChange = getReferenceChangeWithFeatureValue(periodical_ReferenceChangeChildrenData,
				"name", "Item");
		ReferenceChange titledItemChange = getReferenceChangeWithFeatureValue(
				periodical_ReferenceChangeChildrenData, "name", "TitledItem");

		TreeNode issuesPerYearChange_Node = getTreeNode(periodical_ReferenceChange_Node, issuesPerYearChange);
		Collection<?> issuesPerYearChildren = issuesPerYearChange_Node.getChildren();
		assertEquals(1, issuesPerYearChildren.size());
		
		TreeNode issuePerYearChildNode = (TreeNode)issuesPerYearChildren.iterator().next();
		ReferenceChange issuePerYearChild = (ReferenceChange)issuePerYearChildNode.getData();
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, issuePerYearChild.getReference());

		TreeNode itemChange_Node = getTreeNode(periodical_ReferenceChange_Node, itemChange);
		assertTrue(itemChange_Node.getChildren().isEmpty());
		
		TreeNode titledItemChange_Node = getTreeNode(periodical_ReferenceChange_Node, titledItemChange);
		assertTrue(titledItemChange_Node.getChildren().isEmpty());
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
		
		ReferenceChange firstNameChange = getReferenceChangeWithFeatureValue(person_MatchChildrenData,
				"name", "firstName");
		TreeNode firstNameChange_Node = getTreeNode(person_Match_Node, firstNameChange);
		Collection<?> firstNameChildren = firstNameChange_Node.getChildren();
		assertEquals(1, firstNameChildren.size());
		Iterable<EObject> firstNameChildrenData = transform(firstNameChildren, TREE_NODE_DATA);
		ReferenceChange firstNameChild = (ReferenceChange)firstNameChildrenData.iterator().next();
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, firstNameChild.getReference());

		ReferenceChange fullNameChange = getReferenceChangeWithFeatureValue(person_MatchChildrenData, "name",
				"fullName");
		TreeNode fullNameChange_Node = getTreeNode(person_Match_Node, fullNameChange);
		Collection<?> fullNameChildren = fullNameChange_Node.getChildren();
		assertEquals(1, fullNameChildren.size());
		Iterable<EObject> fullNameChildrenData = transform(fullNameChildren, TREE_NODE_DATA);
		ReferenceChange fullNameChild = (ReferenceChange)fullNameChildrenData.iterator().next();
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, fullNameChild.getReference());

		ReferenceChange lastNameChange = getReferenceChangeWithFeatureValue(person_MatchChildrenData, "name",
				"lastName");
		TreeNode lastNameChange_Node = getTreeNode(person_Match_Node, lastNameChange);
		Collection<?> lastNameChildren = lastNameChange_Node.getChildren();
		assertEquals(2, lastNameChildren.size());
		Iterable<EObject> lastNameChildrenData = transform(lastNameChildren, TREE_NODE_DATA);
		Iterator<?> lastNameiterator = lastNameChildrenData.iterator();
		ReferenceChange lastName1stChild = (ReferenceChange)lastNameiterator.next();
		AttributeChange lastName2ndChild = (AttributeChange)lastNameiterator.next();
		assertEquals(EcorePackage.Literals.ETYPED_ELEMENT__ETYPE, lastName1stChild.getReference());
		assertEquals(EcorePackage.Literals.ENAMED_ELEMENT__NAME, lastName2ndChild.getAttribute());
	}

	@Test
	public void testGetChildren_TitledItem() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();

		Collection<?> ePackage_MatchChildren = ePackageMatch.getChildren();
		Iterable<EObject> ePackage_MatchChildrenData = transform(ePackage_MatchChildren, TREE_NODE_DATA);
		
		ReferenceChange titledItem_ReferenceChange = getReferenceChangeWithFeatureValue(
				ePackage_MatchChildrenData, "name", "TitledItem");
		TreeNode titledItem_ReferenceChange_Node = getTreeNode(ePackageMatch, titledItem_ReferenceChange);
		Collection<?> titledItem_ReferenceChangeChildren = titledItem_ReferenceChange_Node.getChildren();
		assertEquals(1, titledItem_ReferenceChangeChildren.size());

		Iterable<EObject> titledItem_ReferenceChangeChildrenData = transform(titledItem_ReferenceChangeChildren, TREE_NODE_DATA);
		ReferenceChange title_Change = (ReferenceChange)titledItem_ReferenceChangeChildrenData.iterator().next();
		TreeNode title_Change_Node = getTreeNode(titledItem_ReferenceChange_Node, title_Change);
		Collection<?> title_ChangeChildren = title_Change_Node.getChildren();
		assertEquals(1, title_ChangeChildren.size());
		
		Iterable<EObject> title_ChangeChildrenData = transform(title_ChangeChildren, TREE_NODE_DATA);
		ReferenceChange eType_Change = (ReferenceChange)title_ChangeChildrenData.iterator().next();
		TreeNode eType_Change_Node = getTreeNode(title_Change_Node, eType_Change);
		assertTrue(eType_Change_Node.getChildren().isEmpty());
	}
}
