/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.diff;

import static com.google.common.base.Predicates.and;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.referenceValueMatch;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("all")
public class ComparisonUtilTest {
	@Test
	public void testSubDiffs() throws IOException {
		IdentifierMatchInputData inputData = new IdentifierMatchInputData();

		final Resource left = inputData.getExtlibraryLeft();
		final Resource origin = inputData.getExtlibraryOrigin();
		final Resource right = inputData.getExtlibraryRight();

		// 2-way
		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);
		List<Diff> differences = comparison.getDifferences();

		// Right to left on a deleted element
		final Predicate<? super Diff> leftPeriodical = and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE),
				referenceValueMatch("eClassifiers", "extlibrary.Periodical", true));
		final Diff leftPeriodicalDiff = Iterators.find(differences.iterator(), leftPeriodical);
		boolean leftToRight = false;
		Iterable<Diff> subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftPeriodicalDiff);

		assertEquals(7, Iterables.size(subDiffs));

		// Left to right on a deleted element
		leftToRight = true;
		subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftPeriodicalDiff);

		assertEquals(4, Iterables.size(subDiffs));

		// Right to left on an added element
		final Predicate<? super Diff> leftMagazine = and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), referenceValueMatch("eClassifiers", "extlibrary.Magazine", true));
		final Diff leftMagazineDiff = Iterators.find(differences.iterator(), leftMagazine);
		leftToRight = false;
		subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftMagazineDiff);

		assertEquals(5, Iterables.size(subDiffs));

		// Left to right on an added element
		leftToRight = true;
		subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftMagazineDiff);

		assertEquals(5, Iterables.size(subDiffs));

		// 3-way
		scope = new DefaultComparisonScope(left, right, origin);
		comparison = EMFCompare.builder().build().compare(scope);
		differences = comparison.getDifferences();

		// Right to left on a deleted element
		final Predicate<? super Diff> leftPeriodical3Way = and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE),
				referenceValueMatch("eClassifiers", "extlibrary.Periodical", true));
		final Diff leftPeriodicalDiff3Way = Iterators.find(differences.iterator(), leftPeriodical3Way);
		leftToRight = false;
		subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftPeriodicalDiff3Way);

		assertEquals(7, Iterables.size(subDiffs));

		// Left to right on a deleted element
		leftToRight = true;
		subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftPeriodicalDiff3Way);

		assertEquals(7, Iterables.size(subDiffs));

		// Right to left on a added element
		final Predicate<? super Diff> leftMagazine3Way = and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), referenceValueMatch("eClassifiers", "extlibrary.Magazine", true));
		final Diff leftMagazineDiff3Way = Iterators.find(differences.iterator(), leftMagazine3Way);
		leftToRight = false;
		subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftMagazineDiff3Way);

		assertEquals(5, Iterables.size(subDiffs));

		// Left to right on an added element
		leftToRight = true;
		subDiffs = ComparisonUtil.getSubDiffs(leftToRight).apply(leftMagazineDiff3Way);

		assertEquals(5, Iterables.size(subDiffs));
	}
}
