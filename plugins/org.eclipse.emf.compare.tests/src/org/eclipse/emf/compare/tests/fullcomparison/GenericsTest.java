/**
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison;

import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.size;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.google.common.base.Predicate;
import com.google.common.collect.UnmodifiableIterator;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractMatcherTest;
import org.eclipse.emf.compare.tests.framework.MatchingAlgorithm;
import org.eclipse.emf.compare.tests.fullcomparison.data.generics.GenericsMatchInputData;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests matching and differences on generic Ecore types.
 */
public class GenericsTest extends AbstractMatcherTest {
	/** Current diff expansion includes the references inside unmatched generic subtrees. */
	private static final int EXPECTED_DIFFERENCES = 53;

	/** Containment and exception additions in this fixture. */
	private static final int EXPECTED_ADDITIONS = 27;

	/** Containment and exception deletions in this fixture. */
	private static final int EXPECTED_DELETIONS = 6;

	/** Type-reference changes in this fixture. */
	private static final int EXPECTED_CHANGES = 20;

	private GenericsMatchInputData inputData = new GenericsMatchInputData();

	/**
	 * Selects the matcher for this execution.
	 *
	 * @param matcher
	 *            the matcher under test
	 */
	public GenericsTest(MatchingAlgorithm matcher) {
		super(matcher);
	}

	@Test
	public void test1() throws IOException {
		final Resource left = inputData.getLeft();
		final Resource right = inputData.getRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = matcher.builder(UseIdentifiers.WHEN_AVAILABLE).build().compare(scope);
		final List<Diff> differences = comparison.getDifferences();

		assertEquals(EXPECTED_DIFFERENCES, differences.size());

		EPackage leftPackage = (EPackage)left.getContents().get(0);
		EPackage rightPackage = (EPackage)right.getContents().get(0);
		EClass leftClass = (EClass)leftPackage.getEClassifier("Class2"); //$NON-NLS-1$
		EClass rightClass = (EClass)rightPackage.getEClassifier("Class2"); //$NON-NLS-1$
		assertSame(rightClass, comparison.getMatch(leftClass).getRight());
		// get2 keeps its generic return type and argument while adding an upper bound.
		EOperation leftOperation = leftClass.getEOperations().get(1);
		EOperation rightOperation = rightClass.getEOperations().get(1);
		assertSame(rightOperation, comparison.getMatch(leftOperation).getRight());
		EGenericType leftReturnType = leftOperation.getEGenericType();
		EGenericType rightReturnType = rightOperation.getEGenericType();
		assertSame(rightReturnType, comparison.getMatch(leftReturnType).getRight());
		assertSame(rightReturnType.getETypeArguments().get(0),
				comparison.getMatch(leftReturnType.getETypeArguments().get(0)).getRight());

		final Predicate<? super Diff> addKind = ofKind(DifferenceKind.ADD);
		UnmodifiableIterator<Diff> addDiffs = filter(differences.iterator(), addKind);
		assertEquals(EXPECTED_ADDITIONS, size(addDiffs));

		final Predicate<? super Diff> deleteKind = ofKind(DifferenceKind.DELETE);
		UnmodifiableIterator<Diff> deleteDiffs = filter(differences.iterator(), deleteKind);
		assertEquals(EXPECTED_DELETIONS, size(deleteDiffs));

		final Predicate<? super Diff> changeKind = ofKind(DifferenceKind.CHANGE);
		UnmodifiableIterator<Diff> changeDiffs = filter(differences.iterator(), changeKind);
		assertEquals(EXPECTED_CHANGES, size(changeDiffs));

	}
}
