/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.compare.diagram.internal.merge.CompareDiagramMerger;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;

@SuppressWarnings("nls")
public class IndividualMergeTest extends AbstractTest {
	private IndividualDiffInputData input = new IndividualDiffInputData();

	@Test
	// ADD Edge
	public void testA1_ADDEdgeLeftToRight() throws IOException {
		final Resource left = input.getA1EdgeChangeLeft();
		final Resource right = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Diff edgeChange = testA1_EdgeChange(comparison, DifferenceKind.ADD);

		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		comparison = buildComparison(left, right);		
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}
	
	@Test
	// ADD Edge
	public void testA1_ADDEdgeRightToLeft() throws IOException {
		final Resource left = input.getA1EdgeChangeLeft();
		final Resource right = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Diff edgeChange = testA1_EdgeChange(comparison, DifferenceKind.ADD);

		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		comparison = buildComparison(left, right);
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(13), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Collections2.filter(differences, instanceOf(UMLDiff.class)).size());
		assertSame(Integer.valueOf(0), Collections2.filter(differences, instanceOf(DiagramDiff.class)).size());
	}
	
	@Test
	// ADD Edge
	public void testA1_DELETEEdgeLeftToRight() throws IOException {
		final Resource right = input.getA1EdgeChangeLeft();
		final Resource left = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Diff edgeChange = testA1_EdgeChange(comparison, DifferenceKind.DELETE);

		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		testA1_OnlyDiagramDifferencesMerged(left, right);
	}
	
	@Test
	// ADD Edge
	public void testA1_DELETEEdgeRightToLeft() throws IOException {
		final Resource right = input.getA1EdgeChangeLeft();
		final Resource left = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Diff edgeChange = testA1_EdgeChange(comparison, DifferenceKind.DELETE);

		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		testA1_AllDifferencesMerged(left, right);
	}

	private void testA1_AllDifferencesMerged(Resource left, Resource right) {
		Comparison comparison = buildComparison(left, right);		
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getDifferences().size()));
	}
	
	private void testA1_OnlyDiagramDifferencesMerged(Resource left, Resource right) {
		Comparison comparison = buildComparison(left, right);
		List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(13), Integer.valueOf(differences.size()));
		assertSame(Integer.valueOf(1), Collections2.filter(differences, instanceOf(UMLDiff.class)).size());
		assertSame(Integer.valueOf(0), Collections2.filter(differences, instanceOf(DiagramDiff.class)).size());
	}
	
	private Diff testA1_EdgeChange(Comparison comparison, DifferenceKind kind) throws IOException {
		final List<Diff> differences = comparison.getDifferences();
		assertSame(Integer.valueOf(32), Integer.valueOf(differences.size()));

		assertSame(Integer.valueOf(1), Collections2.filter(differences, instanceOf(UMLDiff.class)).size());
		assertSame(Integer.valueOf(1), Collections2.filter(differences, instanceOf(DiagramDiff.class)).size());
		
		final Diff associationChange = Iterators.find(differences.iterator(),
				and(instanceOf(AssociationChange.class), ofKind(kind)), null);
		final Diff edgeChange = Iterators.find(differences.iterator(),
				and(instanceOf(EdgeChange.class), ofKind(kind)), null);
		
		assertNotNull(associationChange);
		assertNotNull(edgeChange);
		return edgeChange;
	}
	
	@Override
	protected DiagramInputData getInput() {
		return input;
	}
}
