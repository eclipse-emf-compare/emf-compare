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
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
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

//TODO To extend from AddInterfaceRealizationTest and change only descriptions.

@SuppressWarnings("nls")
public class AddSubstitutionTest extends AbstractUMLTest {

	private DependencyInputData input = new DependencyInputData();

	@Test
	public void testA70UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA71UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA70UseCase3way() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA71UseCase3way() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		// Was 6 with UML 4.0 but NamedElement::clientDependency has been made derived in UML 5.0
		assertEquals(5, differences.size());

		Predicate<? super Diff> addInterfaceRealizationDescription = null;
		Predicate<? super Diff> addClientInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addSupplierInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addContractInInterfaceRealizationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addInterfaceRealizationDescription = removedFromReference("model.Class0", "substitution",
					"model.Class0.Substitution0");
			addClientInInterfaceRealizationDescription = removedFromReference("model.Class0.Substitution0",
					"client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = removedFromReference("model.Class0.Substitution0",
					"supplier", "model.Class1");
			addContractInInterfaceRealizationDescription = changedReference("model.Class0.Substitution0",
					"contract", "model.Class1", null);
		} else {
			addInterfaceRealizationDescription = addedToReference(
					"model.Class0", "substitution", "model.Class0.Substitution0"); //$NON-NLS-1$
			addClientInInterfaceRealizationDescription = addedToReference("model.Class0.Substitution0",
					"client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = addedToReference("model.Class0.Substitution0",
					"supplier", "model.Class1");
			addContractInInterfaceRealizationDescription = changedReference("model.Class0.Substitution0",
					"contract", null, "model.Class1");
		}

		final Diff addInterfaceRealization = Iterators.find(differences.iterator(),
				addInterfaceRealizationDescription);
		final Diff addClientInInterfaceRealization = Iterators.find(differences.iterator(),
				addClientInInterfaceRealizationDescription);
		final Diff addSupplierInInterfaceRealization = Iterators.find(differences.iterator(),
				addSupplierInInterfaceRealizationDescription);
		final Diff addContractInInterfaceRealization = Iterators.find(differences.iterator(),
				addContractInInterfaceRealizationDescription);

		assertNotNull(addInterfaceRealization);
		assertNotNull(addClientInInterfaceRealization);
		assertNotNull(addSupplierInInterfaceRealization);
		assertNotNull(addContractInInterfaceRealization);

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
		assertEquals(4, addUMLDependency.getRefinedBy().size());
		assertTrue(addUMLDependency.getRefinedBy().contains(addClientInInterfaceRealization));
		assertTrue(addUMLDependency.getRefinedBy().contains(addSupplierInInterfaceRealization));
		assertTrue(addUMLDependency.getRefinedBy().contains(addContractInInterfaceRealization));
		assertTrue(addUMLDependency.getRefinedBy().contains(addInterfaceRealization));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addClientInInterfaceRealization.getRequires().size());
			assertTrue(addClientInInterfaceRealization.getRequires().contains(addInterfaceRealization));
			assertEquals(1, addSupplierInInterfaceRealization.getRequires().size());
			assertTrue(addSupplierInInterfaceRealization.getRequires().contains(addInterfaceRealization));
			assertEquals(1, addContractInInterfaceRealization.getRequires().size());
			assertTrue(addContractInInterfaceRealization.getRequires().contains(addInterfaceRealization));

			assertEquals(0, addInterfaceRealization.getRequires().size());
			assertEquals(0, addUMLDependency.getRequires().size());
		} else {
			assertEquals(0, addClientInInterfaceRealization.getRequires().size());
			assertEquals(0, addSupplierInInterfaceRealization.getRequires().size());
			assertEquals(0, addContractInInterfaceRealization.getRequires().size());

			assertEquals(3, addInterfaceRealization.getRequires().size());
			assertTrue(addInterfaceRealization.getRequires().contains(addClientInInterfaceRealization));
			assertTrue(addInterfaceRealization.getRequires().contains(addSupplierInInterfaceRealization));
			assertTrue(addInterfaceRealization.getRequires().contains(addContractInInterfaceRealization));

			assertEquals(0, addUMLDependency.getRequires().size());
		}

		// CHECK EQUIVALENCE
		assertEquals(0, comparison.getEquivalences().size());
		assertNull(addClientInInterfaceRealization.getEquivalence());

		testIntersections(comparison);
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}
}
