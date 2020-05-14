/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor;
import org.eclipse.emf.compare.diagram.ide.ui.sirius.data.IDEUISiriusInputData;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.sirius.tests.AbstractSiriusTest;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=564680">564680.</a>
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public class TestBug564680 extends AbstractSiriusTest {

	/**
	 * The input data.
	 */
	private IDEUISiriusInputData inputData = new IDEUISiriusInputData();

	/**
	 * The main comparison.
	 */
	private Comparison comparison;

	/**
	 * The scope of the comparison.
	 */
	private DefaultComparisonScope scope;

	@Override
	public DefaultComparisonScope getScope() {
		return scope;
	}

	/**
	 * Tests that diagrams owned by left and right accessors are not null after a merge from right to left of
	 * elements added on the left side, and also after undo and redo actions.
	 * 
	 * @throws IOException
	 *             Thrown if we could not access either this class' resource, or the file towards which
	 *             <code>string</code> points.
	 */
	@Test
	public void testAccessorsNotNullR2LElementsAdded() throws IOException {
		Resource leftResource = inputData.getNodes1LeftRepresentation();
		Resource rightResource = inputData.getNodes1RightRepresentation();
		scope = new DefaultComparisonScope(leftResource, rightResource, null);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<DiagramDiff> diagralDiffs = getDiagramDiffs(comparison.getDifferences());
		List<DiagramDiff> dDiffs = new ArrayList<DiagramDiff>();
		DiagramDiff firstDiff = diagralDiffs.get(0);
		dDiffs.add(firstDiff);
		IDiagramNodeAccessor accessorLeft = new SiriusDiffAccessor(firstDiff, MergeViewerSide.LEFT);
		IDiagramNodeAccessor accessorRight = new SiriusDiffAccessor(firstDiff, MergeViewerSide.RIGHT);

		mergeDiffsRightToLeft(dDiffs);
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		undo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		redo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);
	}

	/**
	 * Tests that diagrams owned by left and right accessors are not null after a merge from left to right of
	 * elements added on the left side, and also after undo and redo actions.
	 * 
	 * @throws IOException
	 *             Thrown if we could not access either this class' resource, or the file towards which
	 *             <code>string</code> points.
	 */
	@Test
	public void testAccessorsNotNullL2RElementsAdded() throws IOException {
		Resource leftResource = inputData.getNodes1LeftRepresentation();
		Resource rightResource = inputData.getNodes1RightRepresentation();
		scope = new DefaultComparisonScope(leftResource, rightResource, null);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<DiagramDiff> diagralDiffs = getDiagramDiffs(comparison.getDifferences());
		List<DiagramDiff> dDiffs = new ArrayList<DiagramDiff>();
		DiagramDiff firstDiff = diagralDiffs.get(0);
		dDiffs.add(firstDiff);
		IDiagramNodeAccessor accessorLeft = new SiriusDiffAccessor(firstDiff, MergeViewerSide.LEFT);
		IDiagramNodeAccessor accessorRight = new SiriusDiffAccessor(firstDiff, MergeViewerSide.RIGHT);

		mergeDiffsLeftToRight(dDiffs);
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		undo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		redo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);
	}

	/**
	 * Tests that diagrams owned by left and right accessors are not null after a merge from right to left of
	 * elements deleted on the left side, and also after undo and redo actions.
	 * 
	 * @throws IOException
	 *             Thrown if we could not access either this class' resource, or the file towards which
	 *             <code>string</code> points.
	 */
	@Test
	public void testAccessorsNotNullR2LElementsDeleted() throws IOException {
		Resource leftResource = inputData.getNodes1LeftRepresentation();
		Resource rightResource = inputData.getNodes1RightRepresentation();
		scope = new DefaultComparisonScope(rightResource, leftResource, null);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<DiagramDiff> diagralDiffs = getDiagramDiffs(comparison.getDifferences());
		List<DiagramDiff> dDiffs = new ArrayList<DiagramDiff>();
		DiagramDiff firstDiff = diagralDiffs.get(0);
		dDiffs.add(firstDiff);
		IDiagramNodeAccessor accessorLeft = new SiriusDiffAccessor(firstDiff, MergeViewerSide.LEFT);
		IDiagramNodeAccessor accessorRight = new SiriusDiffAccessor(firstDiff, MergeViewerSide.RIGHT);

		mergeDiffsRightToLeft(dDiffs);
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		undo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		redo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);
	}

	/**
	 * Tests that diagrams owned by left and right accessors are not null after a merge from left to right of
	 * elements deleted on the left side, and also after undo and redo actions.
	 * 
	 * @throws IOException
	 *             Thrown if we could not access either this class' resource, or the file towards which
	 *             <code>string</code> points.
	 */
	@Test
	public void testAccessorsNotNullL2RElementsDeleted() throws IOException {
		Resource leftResource = inputData.getNodes1LeftRepresentation();
		Resource rightResource = inputData.getNodes1RightRepresentation();
		scope = new DefaultComparisonScope(rightResource, leftResource, null);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<DiagramDiff> diagralDiffs = getDiagramDiffs(comparison.getDifferences());
		List<DiagramDiff> dDiffs = new ArrayList<DiagramDiff>();
		DiagramDiff firstDiff = diagralDiffs.get(0);
		dDiffs.add(firstDiff);
		IDiagramNodeAccessor accessorLeft = new SiriusDiffAccessor(firstDiff, MergeViewerSide.LEFT);
		IDiagramNodeAccessor accessorRight = new SiriusDiffAccessor(firstDiff, MergeViewerSide.RIGHT);

		mergeDiffsLeftToRight(dDiffs);
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		undo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);

		redo();
		assertAccessorsDiagramNotNull(accessorLeft, accessorRight);
	}

	/**
	 * Ensures that diagrams owned by given accessors are not null.
	 * 
	 * @param accessorLeft
	 *            the first accessor.
	 * @param accessorRight
	 *            the second accessor.
	 */
	private static void assertAccessorsDiagramNotNull(IDiagramNodeAccessor accessorLeft,
			IDiagramNodeAccessor accessorRight) {
		assertNotNull(accessorLeft.getOwnedDiagram());
		assertNotNull(accessorRight.getOwnedDiagram());
	}

	/**
	 * Provides access to DiagramDiff that exist in the difference list.
	 * 
	 * @param differences
	 *            the list of differences
	 * @return the list of DiagramDiff.
	 */
	private List<DiagramDiff> getDiagramDiffs(List<Diff> differences) {
		Stream<DiagramDiff> siriusDiffs = differences.stream().filter(diff -> diff instanceof DiagramDiff)
				.map(DiagramDiff.class::cast);
		return siriusDiffs.collect(Collectors.toList());
	}

}
