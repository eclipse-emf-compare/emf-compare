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
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
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

		assertEquals("extlibrary", itemDelegator.getText(node));

		List<? extends TreeNode> extlibraryChildren = node.getChildren();
		assertEquals(18, extlibraryChildren.size());

		node = extlibraryChildren.get(0);
		assertEquals("Book -> CirculatingItem", itemDelegator.getText(node));

		List<? extends TreeNode> bookChildren = node.getChildren();
		assertEquals(6, bookChildren.size());

		node = bookChildren.get(0);
		assertEquals("pages : EInt", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookChildren.get(1);
		assertEquals("category : BookCategory", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookChildren.get(2);
		assertEquals("author : Writer", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookChildren.get(3);
		assertEquals("TitledItem [eSuperTypes add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookChildren.get(4);
		assertEquals("subtitle : EString [eStructuralFeatures add]", itemDelegator.getText(node));

		List<? extends TreeNode> subtitleChildren = node.getChildren();
		assertEquals(1, subtitleChildren.size());
		node = subtitleChildren.get(0);
		assertEquals("EString [java.lang.String] [eType set]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookChildren.get(5);
		assertEquals("title : EString [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> titleChildren = node.getChildren();
		assertEquals(1, titleChildren.size());
		node = titleChildren.get(0);
		assertEquals("EString [java.lang.String] [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(1);
		assertEquals("Library -> Addressable", itemDelegator.getText(node));

		List<? extends TreeNode> libraryChildren = node.getChildren();
		assertEquals(9, libraryChildren.size());

		node = libraryChildren.get(0);
		assertEquals("name : EString", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = libraryChildren.get(1);
		assertEquals("writers : Writer", itemDelegator.getText(node));
		assertEquals(1, node.getChildren().size()); // ExtendedMetaData

		node = libraryChildren.get(2);
		assertEquals("employees : Employee", itemDelegator.getText(node));
		assertEquals(1, node.getChildren().size()); // ExtendedMetaData

		node = libraryChildren.get(3);
		assertEquals("borrowers : Borrower", itemDelegator.getText(node));
		assertEquals(1, node.getChildren().size()); // ExtendedMetaData

		node = libraryChildren.get(4);
		assertEquals("stock : Item", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = libraryChildren.get(5);
		assertEquals("books : Book", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = libraryChildren.get(6);
		assertEquals("branches : Library", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = libraryChildren.get(7);
		assertEquals("parentBranch : Library", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = libraryChildren.get(8);
		assertEquals("people : EFeatureMapEntry", itemDelegator.getText(node));
		assertEquals(1, node.getChildren().size()); // ExtendedMetaData

		node = extlibraryChildren.get(2);
		assertEquals("Writer -> Person", itemDelegator.getText(node));

		List<? extends TreeNode> writerChildren = node.getChildren();
		assertEquals(2, writerChildren.size());

		node = writerChildren.get(0);
		assertEquals("name : EString", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = writerChildren.get(1);
		assertEquals("books : Book", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(3);
		assertEquals("BookCategory", itemDelegator.getText(node));

		List<? extends TreeNode> bookCategoryChildren = node.getChildren();
		assertEquals(7, bookCategoryChildren.size());

		node = bookCategoryChildren.get(0);
		assertEquals("Mystery = 0", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookCategoryChildren.get(1);
		assertEquals("ScienceFiction = 1", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookCategoryChildren.get(2);
		assertEquals("Biography = 2", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookCategoryChildren.get(3);
		assertEquals("Encyclopedia = 3 [eLiterals add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookCategoryChildren.get(4);
		assertEquals("Dictionary = 4 [eLiterals add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookCategoryChildren.get(5);
		assertEquals("Manga = 3 [eLiterals add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookCategoryChildren.get(6);
		assertEquals("Manhwa = 5 [eLiterals add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(4);
		assertEquals("Item", itemDelegator.getText(node));

		List<? extends TreeNode> itemChildren = node.getChildren();
		assertEquals(1, itemChildren.size());

		node = itemChildren.get(0);
		assertEquals("publicationDate : EDate", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(5);
		assertEquals("Borrowable", itemDelegator.getText(node));

		List<? extends TreeNode> borrowableChildren = node.getChildren();
		assertEquals(3, borrowableChildren.size());

		node = borrowableChildren.get(0);
		assertEquals("copies : EInt", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = borrowableChildren.get(1);
		assertEquals("borrowers : Borrower", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = borrowableChildren.get(2);
		assertEquals("Borrowable [name changed]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(6);
		assertEquals("CirculatingItem -> Item, Borrowable", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(7);
		assertEquals("AudioVisualItem -> CirculatingItem", itemDelegator.getText(node));

		List<? extends TreeNode> audioVisualItemChildren = node.getChildren();
		assertEquals(4, audioVisualItemChildren.size());

		node = audioVisualItemChildren.get(0);
		assertEquals("length : EInt", itemDelegator.getText(node));

		List<? extends TreeNode> lengthChildren = node.getChildren();
		assertEquals(2, lengthChildren.size());

		node = lengthChildren.get(0);
		assertEquals("length [name changed]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = lengthChildren.get(1);
		assertEquals("minutes [name changed]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = audioVisualItemChildren.get(1);
		assertEquals("damaged : EBoolean", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = audioVisualItemChildren.get(2);
		assertEquals("TitledItem [eSuperTypes add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = audioVisualItemChildren.get(3);
		assertEquals("title : EString [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> audioTitleChildren = node.getChildren();
		assertEquals(1, audioTitleChildren.size());

		node = audioTitleChildren.get(0);
		assertEquals("EString [java.lang.String] [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(8);
		assertEquals("BookOnTape -> AudioVisualItem", itemDelegator.getText(node));

		List<? extends TreeNode> bookOnTapeChildren = node.getChildren();
		assertEquals(3, bookOnTapeChildren.size());

		node = bookOnTapeChildren.get(0);
		assertEquals("author : Writer", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookOnTapeChildren.get(1);
		assertEquals("reader : Person [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> reader1Children = node.getChildren();
		assertEquals(1, reader1Children.size());

		node = reader1Children.get(0);
		assertEquals("Person -> Addressable [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = bookOnTapeChildren.get(2);
		assertEquals("reader : Person [eStructuralFeatures delete]", itemDelegator.getText(node));
		assertEquals(1, node.getChildren().size());

		List<? extends TreeNode> reader2Children = node.getChildren();
		assertEquals(1, reader2Children.size());

		node = reader2Children.get(0);
		assertEquals("Person -> Addressable [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(9);
		assertEquals("VideoCassette -> AudioVisualItem", itemDelegator.getText(node));

		List<? extends TreeNode> videoCassetteChildren = node.getChildren();
		assertEquals(1, videoCassetteChildren.size());

		node = videoCassetteChildren.get(0);
		assertEquals("cast : Person", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(10);
		assertEquals("Borrower -> Person", itemDelegator.getText(node));

		List<? extends TreeNode> borrowerChildren = node.getChildren();
		assertEquals(1, borrowerChildren.size());

		node = borrowerChildren.get(0);
		assertEquals("borrowed : Borrowable", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(11);
		assertEquals("Person -> Addressable", itemDelegator.getText(node));

		List<? extends TreeNode> personChildren = node.getChildren();
		assertEquals(3, personChildren.size());

		node = personChildren.get(0);
		assertEquals("fullName : EString [eStructuralFeatures add]", itemDelegator.getText(node));

		List<? extends TreeNode> fullNameChildren = node.getChildren();
		assertEquals(1, fullNameChildren.size());

		node = fullNameChildren.get(0);
		assertEquals("EString [java.lang.String] [eType set]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = personChildren.get(1);
		assertEquals("firstName : EString [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> firstNameChildren = node.getChildren();
		assertEquals(1, firstNameChildren.size());

		node = firstNameChildren.get(0);
		assertEquals("EString [java.lang.String] [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = personChildren.get(2);
		assertEquals("lastName : EString [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> lastNameChildren = node.getChildren();
		assertEquals(2, lastNameChildren.size());

		node = lastNameChildren.get(0);
		assertEquals("EString [java.lang.String] [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = lastNameChildren.get(1);
		assertEquals("familyName [name set]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(12);
		assertEquals("Employee -> Person", itemDelegator.getText(node));

		List<? extends TreeNode> employeeChildren = node.getChildren();
		assertEquals(1, employeeChildren.size());

		node = employeeChildren.get(0);
		assertEquals("manager : Employee", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(13);
		assertEquals("Addressable", itemDelegator.getText(node));

		List<? extends TreeNode> addressableChildren = node.getChildren();
		assertEquals(1, addressableChildren.size());

		node = addressableChildren.get(0);
		assertEquals("address : EString", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(14);
		assertEquals("Magazine -> CirculatingItem [eClassifiers add]", itemDelegator.getText(node));

		List<? extends TreeNode> magazineCirculatingItemChildren = node.getChildren();
		assertEquals(3, magazineCirculatingItemChildren.size());

		node = magazineCirculatingItemChildren.get(0);
		assertEquals("CirculatingItem -> Item, Borrowable [eSuperTypes add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = magazineCirculatingItemChildren.get(1);
		assertEquals("title : EString [eStructuralFeatures add]", itemDelegator.getText(node));

		List<? extends TreeNode> circulatingItemTitleChildren = node.getChildren();
		assertEquals(1, circulatingItemTitleChildren.size());

		node = circulatingItemTitleChildren.get(0);
		assertEquals("EString [java.lang.String] [eType set]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = magazineCirculatingItemChildren.get(2);
		assertEquals("pages : EInt [eStructuralFeatures add]", itemDelegator.getText(node));

		List<? extends TreeNode> pagesChildren = node.getChildren();
		assertEquals(1, pagesChildren.size());

		node = pagesChildren.get(0);
		assertEquals("EInt [int] [eType set]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(15);
		assertEquals("Magazine -> Periodical [eClassifiers add]", itemDelegator.getText(node));

		List<? extends TreeNode> magazineChildren = node.getChildren();
		assertEquals(1, magazineChildren.size());

		node = magazineChildren.get(0);
		assertEquals("Periodical -> Item, TitledItem [eSuperTypes add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(16);
		assertEquals("TitledItem [eClassifiers add]", itemDelegator.getText(node));

		List<? extends TreeNode> titledItemChildren = node.getChildren();
		assertEquals(1, titledItemChildren.size());

		node = titledItemChildren.get(0);
		assertEquals("title : EString [eStructuralFeatures add]", itemDelegator.getText(node));

		List<? extends TreeNode> titledItemTitleChildren = node.getChildren();
		assertEquals(1, titledItemTitleChildren.size());

		node = titledItemTitleChildren.get(0);
		assertEquals("EString [java.lang.String] [eType set]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = extlibraryChildren.get(17);
		assertEquals("Periodical -> Item [eClassifiers delete]", itemDelegator.getText(node));

		List<? extends TreeNode> periodicalItemChildren = node.getChildren();
		assertEquals(5, periodicalItemChildren.size());

		node = periodicalItemChildren.get(0);
		assertEquals("TitledItem [eSuperTypes add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = periodicalItemChildren.get(1);
		assertEquals("Item [eSuperTypes delete]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = periodicalItemChildren.get(2);
		assertEquals("title : EString [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> title1Children = node.getChildren();
		assertEquals(1, title1Children.size());

		node = title1Children.get(0);
		assertEquals("EString [java.lang.String] [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = periodicalItemChildren.get(3);
		assertEquals("title : EString [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> title2Children = node.getChildren();
		assertEquals(1, title2Children.size());

		node = title2Children.get(0);
		assertEquals("EString [java.lang.String] [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = periodicalItemChildren.get(4);
		assertEquals("issuesPerYear : EInt [eStructuralFeatures delete]", itemDelegator.getText(node));

		List<? extends TreeNode> issuesPerYearChildren = node.getChildren();
		assertEquals(1, issuesPerYearChildren.size());

		node = issuesPerYearChildren.get(0);
		assertEquals("EInt [int] [eType unset]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = roots.get(1);
		assertEquals("extlibraryLeft.ecore <-> extlibraryRight.ecore (extlibraryOrigin.ecore)",
				itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());
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

		assertEquals("Node root", itemDelegator.getText(node));

		List<? extends TreeNode> nodeRootChildren = node.getChildren();
		assertEquals(3, nodeRootChildren.size());

		node = nodeRootChildren.get(0);
		assertEquals("Node A", itemDelegator.getText(node));

		List<? extends TreeNode> aChildren = node.getChildren();
		assertEquals(1, aChildren.size());

		node = aChildren.get(0);
		assertEquals("Node C [containmentRef1 add]", itemDelegator.getText(node));

		List<? extends TreeNode> c1Children = node.getChildren();
		assertEquals(2, c1Children.size());

		node = c1Children.get(0);
		assertEquals("Node D [containmentRef1 add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = c1Children.get(1);
		assertEquals("Node E [containmentRef1 add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = nodeRootChildren.get(1);
		assertEquals("Node B", itemDelegator.getText(node));

		List<? extends TreeNode> bChildren = node.getChildren();
		assertEquals(1, bChildren.size());

		node = bChildren.get(0);
		assertEquals("Node C [containmentRef1 add]", itemDelegator.getText(node));

		List<? extends TreeNode> c2Children = node.getChildren();
		assertEquals(2, c2Children.size());

		node = c2Children.get(0);
		assertEquals("Node D [containmentRef1 add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = c2Children.get(1);
		assertEquals("Node F [containmentRef1 add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = nodeRootChildren.get(2);
		assertEquals("Node G", itemDelegator.getText(node));

		List<? extends TreeNode> gChildren = node.getChildren();
		assertEquals(1, gChildren.size());

		node = gChildren.get(0);
		assertEquals("Node H [containmentRef1 add]", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());

		node = roots.get(1);
		assertEquals("left.nodes <-> right.nodes (ancestor.nodes)", itemDelegator.getText(node));
		assertEquals(0, node.getChildren().size());
	}
}
