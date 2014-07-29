/*******************************************************************************
 * Copyright (c) EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.data.TwoWayMergeInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeMultipleContainment;
import org.eclipse.emf.compare.tests.nodes.NodesPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests two-way comparison of {@link NodesPackage nodes models} with XMI IDs and subsequent merging using the
 * {@link BatchMerger}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class TwoWayBatchMergingTest {

	private TwoWayMergeInputData input = new TwoWayMergeInputData();

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	/**
	 * Tests a scenario in which an element is moved from one container to another, whereas the containment
	 * reference in the original container (left) is not available in the target container (right). This lead
	 * to a NPE in {@link DiffUtil#findInsertionIndex(Comparison, org.eclipse.emf.compare.Diff, boolean)} (cf.
	 * Bug #440679). In particular, we have two differences: (1) Deletion of {@link NodeMultipleContainment}
	 * "A" and (2) Move of {@link Node} "B" from {@link NodeMultipleContainment} "A" (reference
	 * "containmentRef2") to {@link Node} "Root" into reference "containmentRef1". As a result, we move node
	 * "B" originally contained through "containmentRef2" into a {@link Node}, which does not have the feature
	 * "containmentRef2".
	 * 
	 * @throws IOException
	 *             if {@link TwoWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingMoveToDifferentContainmentFeatureR2L() throws IOException {
		final Resource left = input.getMoveToDifferentContainmentFeatureRTLLeft();
		final Resource right = input.getMoveToDifferentContainmentFeatureRTLRight();

		// perform comparison
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// batch merging of all detected differences:
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(comparison.getDifferences(), new BasicMonitor());

		// check that models are equal after batch merging
		Comparison assertionComparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, assertionComparison.getDifferences().size());
	}

	/**
	 * This test is the reverse of the test ({@link #mergingMoveToDifferentContainmentFeatureR2L() above} to
	 * make sure, this issue is not appearing in the other direction as well.
	 * 
	 * @throws IOException
	 *             if {@link TwoWayMergeInputData} fails to load the test models.
	 */
	@Test
	public void mergingMoveToDifferentContainmentFeatureL2R() throws IOException {
		final Resource left = input.getMoveToDifferentContainmentFeatureL2RLeft();
		final Resource right = input.getMoveToDifferentContainmentFeatureL2RRight();

		// perform comparison
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// batch merging of all detected differences:
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(comparison.getDifferences(), new BasicMonitor());

		// check that models are equal after batch merging
		Comparison assertionComparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, assertionComparison.getDifferences().size());
	}

}
