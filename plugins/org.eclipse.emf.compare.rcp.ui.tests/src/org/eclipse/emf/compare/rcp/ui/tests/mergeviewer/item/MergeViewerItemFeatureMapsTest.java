/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.mergeviewer.item;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.diff.data.featurefilter.featuremap.FeatureFilterFeatureMapsInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link MergeViewerItem}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class MergeViewerItemFeatureMapsTest {

	private static FeatureFilterFeatureMapsInputData inputData = new FeatureFilterFeatureMapsInputData();
	private final static ComposedAdapterFactory fAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	private static Comparison comparison;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		fAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		fAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		final Resource leftResource = inputData.getNodesLeft();
		final Resource rightResource = inputData.getNodesRight();

		final IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, null);
		final Builder comparisonBuilder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(comparisonBuilder);
		comparison = comparisonBuilder.build().compare(scope);
	}
	
	@Test
	public void test2WayFeatureMapContainment() throws IOException {
		// Test with models containing NodeFeatureMapContainment2 elements. A NodeFeatureMapContainment2 is an
		// element that contains a map with two types of entries: NodeMultipleContainment and
		// NodeSingleValueContainment. NodeMultipleContainment has containment references that don't exist in
		// NodeSingleValueContainment. The MergeViewer needs to properly handle the case.
		
		final Match mapNodeMatch = comparison.getMatches().get(0).getSubmatches().get(0);
		
		//Test Left Side
		MergeViewerItem.Container mapNode1MVI = new MergeViewerItem.Container(comparison, null, mapNodeMatch, MergeViewerSide.LEFT, fAdapterFactory);
		IMergeViewerItem[] children = mapNode1MVI.getChildren(null, null);
		assertTrue(children.length == 5);
		
		//Test Right Side
		mapNode1MVI = new MergeViewerItem.Container(comparison, null, mapNodeMatch, MergeViewerSide.RIGHT, fAdapterFactory);
		children = mapNode1MVI.getChildren(null, null);
		assertTrue(children.length == 5);
		
	}
	
}
