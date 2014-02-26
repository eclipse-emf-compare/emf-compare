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
package org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractTableOrTreeMergeViewer.ElementComparer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

/**
 * Tests for {@link MergeViewerItem}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class MergeViewerItemTest {

	private static IdentifierMatchInputData inputData = new IdentifierMatchInputData();
	private final static ComposedAdapterFactory fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	private final ElementComparer ec = new ElementComparer();
	private static Comparison comparison;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		final Resource leftResource = inputData.getExtlibraryLeft();
		final Resource rightResource = inputData.getExtlibraryRight();

		final IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, null);
		comparison = EMFCompare.builder().build().compare(scope);
	}
	
	@Test
	public void test2WayRefContainmentMultiValued() throws IOException {
		final List<Diff> differences = comparison.getDifferences();

		final Predicate<? super Diff> titledItem = removedFromReference("extlibrary", "eClassifiers", "extlibrary.TitledItem");
		final Diff titledItemDiff = Iterators.find(differences.iterator(), titledItem);
		final EObject titledItemValue = (EObject)MergeViewerUtil.getDiffValue(titledItemDiff);
		final Match titledItemMatch = comparison.getMatch(titledItemValue);
		
		//Test Left Side
		MergeViewerItem.Container titledItemMVI = new MergeViewerItem.Container(comparison, titledItemDiff, titledItemMatch, MergeViewerSide.LEFT, fAdapterFactory);
		
		assertEquals(titledItemMVI.getLeft(), null);
		assertEquals(titledItemMVI.getRight(), titledItemValue);
		assertTrue(titledItemMVI.isInsertionPoint());
		
		IMergeViewerItem[] children = titledItemMVI.getChildren(null, null);
		
		assertTrue(children.length == 1);
		
		Predicate<? super Diff> title = removedFromReference("extlibrary.TitledItem", "eStructuralFeatures", "extlibrary.TitledItem.title");
		Diff titleDiff = Iterators.find(differences.iterator(), title);
		IMergeViewerItem titleMVI = children[0];
		
		assertEquals(titleDiff, titleMVI.getDiff());
		
		assertEquals(titleMVI.getLeft(), null);
		assertEquals(titleMVI.getRight(), MergeViewerUtil.getDiffValue(titleDiff));
		assertTrue(titleMVI.isInsertionPoint());
		assertEquals(titleMVI.getSide(), MergeViewerSide.LEFT);
		assertTrue(ec.equals(titleMVI.getParent(), titledItemMVI));
		
		//Test Right Side
		titledItemMVI = new MergeViewerItem.Container(comparison, titledItemDiff, titledItemMatch, MergeViewerSide.RIGHT, fAdapterFactory);
		
		assertEquals(titledItemMVI.getLeft(), null);
		assertEquals(titledItemMVI.getRight(), titledItemValue);
		assertFalse(titledItemMVI.isInsertionPoint());
		
		children = titledItemMVI.getChildren(null, null);
		
		assertTrue(children.length == 1);
		
		title = removedFromReference("extlibrary.TitledItem", "eStructuralFeatures", "extlibrary.TitledItem.title");
		titleDiff = Iterators.find(differences.iterator(), title);
		titleMVI = children[0];
		
		assertEquals(titleDiff, titleMVI.getDiff());
		
		assertEquals(titleMVI.getLeft(), null);
		assertEquals(titleMVI.getRight(), MergeViewerUtil.getDiffValue(titleDiff));
		assertFalse(titleMVI.isInsertionPoint());
		assertEquals(titleMVI.getSide(), MergeViewerSide.RIGHT);
		assertTrue(ec.equals(titleMVI.getParent(), titledItemMVI));
		
	}
	
	@Test
	public void test2WayRefNonContainmentMultiValued() throws IOException {
		final List<Diff> differences = comparison.getDifferences();

		final Predicate<? super Diff> periodcial = removedFromReference("extlibrary", "eClassifiers", "extlibrary.Periodical");
		final Diff periodicalDiff = Iterators.find(differences.iterator(), periodcial);
		final EObject periodicalValue = (EObject)MergeViewerUtil.getDiffValue(periodicalDiff);
		final Match periodicalMatch = comparison.getMatch(periodicalValue);
		
		final Predicate<? super Diff> item = removedFromReference("extlibrary.Periodical", "eSuperTypes", "extlibrary.Item");
		final Diff itemDiff = Iterators.find(differences.iterator(), item);
		
		//Test Left Side
		MergeViewerItem itemMVI = new MergeViewerItem(comparison, itemDiff, periodicalMatch, MergeViewerSide.LEFT, fAdapterFactory);
		
		assertEquals(itemMVI.getLeft(), null);
		assertEquals(itemMVI.getRight(), periodicalValue);
		assertTrue(itemMVI.isInsertionPoint());
		
		assertEquals(itemDiff, itemMVI.getDiff());
		
		//Test Right Side
		itemMVI = new MergeViewerItem(comparison, itemDiff, periodicalMatch, MergeViewerSide.RIGHT, fAdapterFactory);
		
		assertEquals(itemMVI.getLeft(), null);
		assertEquals(itemMVI.getRight(), periodicalValue);
		assertFalse(itemMVI.isInsertionPoint());
		
		assertEquals(itemDiff, itemMVI.getDiff());
	}
	
}
