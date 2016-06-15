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
package org.eclipse.emf.compare.uml2.tests.include;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.include.data.IncludeInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddIncludeTest extends AbstractUMLTest {

	private IncludeInputData input = new IncludeInputData();

	@BeforeClass
	public static void setupClass() {
		fillRegistries();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistries();
	}

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertEquals(3, differences.size());

		Predicate<? super Diff> addExtendDescription = null;
		Predicate<? super Diff> changeRefExtendedCaseInExtendDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addExtendDescription = removed("model.UseCase1.include"); //$NON-NLS-1$
			changeRefExtendedCaseInExtendDescription = changedReference("model.UseCase1.include", "addition",
					"model.UseCase", null);
		} else {
			addExtendDescription = added("model.UseCase1.include"); //$NON-NLS-1$
			changeRefExtendedCaseInExtendDescription = changedReference("model.UseCase1.include", "addition",
					null, "model.UseCase");
		}

		final Diff addExtend = Iterators.find(differences.iterator(), addExtendDescription);
		final Diff addRefExtendedCaseInExtend = Iterators.find(differences.iterator(),
				changeRefExtendedCaseInExtendDescription);

		assertNotNull(addExtend);
		assertNotNull(addRefExtendedCaseInExtend);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(DirectedRelationshipChange.class)));
		Diff addUMLExtend = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLExtend = Iterators.find(differences.iterator(),
					and(instanceOf(DirectedRelationshipChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLExtend = Iterators.find(differences.iterator(),
					and(instanceOf(DirectedRelationshipChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLExtend);
		assertEquals(2, addUMLExtend.getRefinedBy().size());
		assertTrue(addUMLExtend.getRefinedBy().contains(addRefExtendedCaseInExtend));
		assertTrue(addUMLExtend.getRefinedBy().contains(addExtend));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addRefExtendedCaseInExtend.getRequires().size());
			assertTrue(addRefExtendedCaseInExtend.getRequires().contains(addExtend));

			assertEquals(0, addExtend.getRequires().size());
			assertEquals(0, addUMLExtend.getRequires().size());
		} else {
			assertEquals(0, addRefExtendedCaseInExtend.getRequires().size());

			assertEquals(1, addExtend.getRequires().size());
			assertTrue(addExtend.getRequires().contains(addRefExtendedCaseInExtend));

			assertEquals(0, addUMLExtend.getRequires().size());
		}

		// CHECK EQUIVALENCE
		assertEquals(0, comparison.getEquivalences().size());

		testIntersections(comparison);

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
