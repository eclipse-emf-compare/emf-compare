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
package org.eclipse.emf.compare.uml2.tests.dependency;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.eclipse.emf.compare.uml2.tests.dependency.data.DependencyInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

// TODO To extend from AddDependencyTest and change only descriptions.
@SuppressWarnings("nls")
public class AddAbstractionTest extends AbstractUMLTest {

	private DependencyInputData input = new DependencyInputData();

	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		// Was 5 with UML 4.0 but NamedElement::clientDependency has been made derived in UML 5.0
		assertEquals(4, differences.size());

		Predicate<? super Diff> addAbstractionDescription = null;
		Predicate<? super Diff> addRefClass1InAbstractionDescription = null;
		Predicate<? super Diff> addRefClass0InAbstractionDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addAbstractionDescription = removed("model.Abstraction0"); //$NON-NLS-1$
			addRefClass1InAbstractionDescription = removedFromReference("model.Abstraction0", "client",
					"model.Class1");
			addRefClass0InAbstractionDescription = removedFromReference("model.Abstraction0", "supplier",
					"model.Class0");
		} else {
			addAbstractionDescription = added("model.Abstraction0"); //$NON-NLS-1$
			addRefClass1InAbstractionDescription = addedToReference("model.Abstraction0", "client",
					"model.Class1");
			addRefClass0InAbstractionDescription = addedToReference("model.Abstraction0", "supplier",
					"model.Class0");
		}

		final Diff addAbstraction = Iterators.find(differences.iterator(), addAbstractionDescription);
		final Diff addRefClass1InAbstraction = Iterators.find(differences.iterator(),
				addRefClass1InAbstractionDescription);
		final Diff addRefClass0InAbstraction = Iterators.find(differences.iterator(),
				addRefClass0InAbstractionDescription);

		assertNotNull(addAbstraction);
		assertNotNull(addRefClass1InAbstraction);
		assertNotNull(addRefClass0InAbstraction);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(DirectedRelationshipChange.class)));
		Diff addUMLDependency = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(DirectedRelationshipChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(DirectedRelationshipChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLDependency);
		assertEquals(3, addUMLDependency.getRefinedBy().size());
		assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass1InAbstraction));
		assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass0InAbstraction));
		assertTrue(addUMLDependency.getRefinedBy().contains(addAbstraction));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addRefClass1InAbstraction.getRequires().size());
			assertTrue(addRefClass1InAbstraction.getRequires().contains(addAbstraction));

			assertEquals(1, addRefClass0InAbstraction.getRequires().size());
			assertTrue(addRefClass0InAbstraction.getRequires().contains(addAbstraction));

			assertEquals(0, addAbstraction.getRequires().size());
			assertEquals(0, addUMLDependency.getRequires().size());
		} else {
			assertEquals(0, addRefClass1InAbstraction.getRequires().size());

			assertEquals(0, addRefClass0InAbstraction.getRequires().size());

			assertEquals(2, addAbstraction.getRequires().size());
			assertTrue(addAbstraction.getRequires().contains(addRefClass1InAbstraction));
			assertTrue(addAbstraction.getRequires().contains(addRefClass0InAbstraction));

			assertEquals(0, addUMLDependency.getRequires().size());
		}

		// CHECK EQUIVALENCE
		assertEquals(0, comparison.getEquivalences().size());
		assertNull(addRefClass1InAbstraction.getEquivalence());

		testIntersections(comparison);

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
