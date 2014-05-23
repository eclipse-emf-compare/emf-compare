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
package org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item.data.nodes.pseudoconflict.MergeViewerItemEcorePseudoConflictInputData;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
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
 * Tests for {@link MergeViewerItem} in case of a pseudo conflict.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class MergeViewerItemPseudoConflictTest {

	private static MergeViewerItemEcorePseudoConflictInputData inputData = new MergeViewerItemEcorePseudoConflictInputData();
	private final static ComposedAdapterFactory fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	private static Comparison comparison;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		final Resource leftResource = inputData.getLeft();
		final Resource rightResource = inputData.getRight();

		final IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, null);
		comparison = EMFCompare.builder().build().compare(scope);
	}
	
	@Test
	public void test3WayRefContainmentOnPseudoAddConflict() throws IOException {
		final List<Diff> differences = comparison.getDifferences();

		final Predicate<? super Diff> eClassA = EMFComparePredicates.addedToReference("P.SP", "eClassifiers", "P.SP.A");
		final Diff eClassADiff = Iterators.find(differences.iterator(), eClassA);
		final EObject eClassAValue = (EObject)MergeViewerUtil.getDiffValue(eClassADiff);
		final Match eClassAMatch = comparison.getMatch(eClassAValue);

		MergeViewerItem.Container eClassAMVI = new MergeViewerItem.Container(comparison, eClassADiff, eClassAMatch, MergeViewerSide.LEFT, fAdapterFactory);
		
		assertEquals(eClassAMVI.getLeft(), eClassAValue);
		assertNull(eClassAMVI.getRight());
		assertFalse(eClassAMVI.isInsertionPoint());
		
		assertNotNull(eClassAMVI.getParent());
	}
	
}
