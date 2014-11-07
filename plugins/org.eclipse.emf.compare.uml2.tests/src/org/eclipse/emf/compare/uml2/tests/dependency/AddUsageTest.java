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

//TODO To extend from ChangeDependencyTest and change only descriptions.
@SuppressWarnings("nls")
public class AddUsageTest extends AbstractUMLTest {

	private DependencyInputData input = new DependencyInputData();

	@Test
	public void testA80UseCase() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA81UseCase() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA80UseCase3way() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA81UseCase3way() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	protected void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		// Was 5 with UML 4.0 but NamedElement::clientDependency has been made derived in UML 5.0
		assertEquals(4, differences.size());

		Predicate<? super Diff> addDependencyDescription = null;
		Predicate<? super Diff> addRefClass1InDependencyDescription = null;
		Predicate<? super Diff> addRefClass0InDependencyDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addDependencyDescription = removed("model.Usage0"); //$NON-NLS-1$
			addRefClass0InDependencyDescription = removedFromReference("model.Usage0", "client",
					"model.Class0");
			addRefClass1InDependencyDescription = removedFromReference("model.Usage0", "supplier",
					"model.Class1");
		} else {
			addDependencyDescription = added("model.Usage0"); //$NON-NLS-1$
			addRefClass0InDependencyDescription = addedToReference("model.Usage0", "client", "model.Class0");
			addRefClass1InDependencyDescription = addedToReference("model.Usage0", "supplier", "model.Class1");
		}

		final Diff addDependency = Iterators.find(differences.iterator(), addDependencyDescription);
		final Diff addRefClass0InDependency = Iterators.find(differences.iterator(),
				addRefClass0InDependencyDescription);
		final Diff addRefClass1InDependency = Iterators.find(differences.iterator(),
				addRefClass1InDependencyDescription);

		assertNotNull(addDependency);
		assertNotNull(addRefClass0InDependency);
		assertNotNull(addRefClass1InDependency);

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
		assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass0InDependency));
		assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass1InDependency));
		assertTrue(addUMLDependency.getRefinedBy().contains(addDependency));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addRefClass0InDependency.getRequires().size());
			assertTrue(addRefClass0InDependency.getRequires().contains(addDependency));

			assertEquals(1, addRefClass1InDependency.getRequires().size());
			assertTrue(addRefClass1InDependency.getRequires().contains(addDependency));

			assertEquals(0, addDependency.getRequires().size());
			assertEquals(0, addUMLDependency.getRequires().size());
		} else {
			assertEquals(0, addRefClass0InDependency.getRequires().size());

			assertEquals(0, addRefClass1InDependency.getRequires().size());

			assertEquals(2, addDependency.getRequires().size());
			assertTrue(addDependency.getRequires().contains(addRefClass0InDependency));
			assertTrue(addDependency.getRequires().contains(addRefClass1InDependency));

			assertEquals(0, addUMLDependency.getRequires().size());
		}

		// CHECK EQUIVALENCE
		assertEquals(0, comparison.getEquivalences().size());
		assertNull(addRefClass0InDependency.getEquivalence());

		testIntersections(comparison);
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
