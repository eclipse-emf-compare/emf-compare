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

import com.google.common.base.Predicate;
import com.google.common.collect.UnmodifiableIterator;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fullcomparison.data.generics.GenericsMatchInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class GenericsTest {
	private GenericsMatchInputData inputData = new GenericsMatchInputData();

	@Test
	public void test1() throws IOException {
		final Resource left = inputData.getLeft();
		final Resource right = inputData.getRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		final List<Diff> differences = comparison.getDifferences();

		assertEquals(38, differences.size());

		final Predicate<? super Diff> addKind = ofKind(DifferenceKind.ADD);
		UnmodifiableIterator<Diff> addDiffs = filter(differences.iterator(), addKind);
		assertEquals(22, size(addDiffs));

		final Predicate<? super Diff> deleteKind = ofKind(DifferenceKind.DELETE);
		UnmodifiableIterator<Diff> deleteDiffs = filter(differences.iterator(), deleteKind);
		assertEquals(1, size(deleteDiffs));

		final Predicate<? super Diff> changeKind = ofKind(DifferenceKind.CHANGE);
		UnmodifiableIterator<Diff> changeDiffs = filter(differences.iterator(), changeKind);
		assertEquals(15, size(changeDiffs));

	}
}
