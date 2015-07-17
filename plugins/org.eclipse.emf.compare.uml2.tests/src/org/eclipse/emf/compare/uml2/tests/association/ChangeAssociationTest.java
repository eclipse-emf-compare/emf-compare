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
package org.eclipse.emf.compare.uml2.tests.association;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
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
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.association.data.AssociationInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class ChangeAssociationTest extends AbstractUMLTest {

	private AssociationInputData input = new AssociationInputData();

	@BeforeClass
	public static void setupClass() {
		fillRegistries();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistries();
	}

	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testMergeLtRA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testMergeLtRA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Left();

		testMergeLeftToRight(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertEquals(6, differences.size());

		Predicate<? super Diff> addPropertyClass2Description = null;
		Predicate<? super Diff> addRefAssociationInPropertyClass2Description = null;
		Predicate<? super Diff> addRefTypeInPropertyClass2Description = null;
		Predicate<? super Diff> addLiteralIntegerInClass2Description = null;
		Predicate<? super Diff> addUnlimitedNaturalInClass2Description = null;
		Predicate<? super Diff> addPropertyClass2InAssociationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addPropertyClass2Description = removedFromReference("model.class1_class0_0", "ownedEnd",
					"model.class1_class0_0.Class2");
			addRefAssociationInPropertyClass2Description = changedReference("model.class1_class0_0.Class2",
					"association", "model.class1_class0_0", null);
			addRefTypeInPropertyClass2Description = changedReference("model.class1_class0_0.Class2", "type",
					"model.Class2", null);
			addLiteralIntegerInClass2Description = removedLowerValueIn("model.class1_class0_0.Class2");
			addUnlimitedNaturalInClass2Description = removedUpperValueIn("model.class1_class0_0.Class2");
			addPropertyClass2InAssociationDescription = removedFromReference("model.class1_class0_0",
					"memberEnd", "model.class1_class0_0.Class2");
		} else {
			addPropertyClass2Description = addedToReference("model.class1_class0_0", "ownedEnd",
					"model.class1_class0_0.Class2");
			addRefAssociationInPropertyClass2Description = changedReference("model.class1_class0_0.Class2",
					"association", null, "model.class1_class0_0");
			addRefTypeInPropertyClass2Description = changedReference("model.class1_class0_0.Class2", "type",
					null, "model.Class2");
			addLiteralIntegerInClass2Description = addedLowerValueIn("model.class1_class0_0.Class2");
			addUnlimitedNaturalInClass2Description = addedUpperValueIn("model.class1_class0_0.Class2");
			addPropertyClass2InAssociationDescription = addedToReference("model.class1_class0_0",
					"memberEnd", "model.class1_class0_0.Class2");
		}

		final Diff addPropertyClass2 = Iterators.find(differences.iterator(), addPropertyClass2Description);
		final Diff addRefAssociationInPropertyClass2 = Iterators.find(differences.iterator(),
				addRefAssociationInPropertyClass2Description);
		final Diff addRefTypeInPropertyClass2 = Iterators.find(differences.iterator(),
				addRefTypeInPropertyClass2Description);
		final Diff addLiteralIntegerInClass2 = Iterators.find(differences.iterator(),
				addLiteralIntegerInClass2Description);
		final Diff addUnlimitedNaturalInClass2 = Iterators.find(differences.iterator(),
				addUnlimitedNaturalInClass2Description);
		final Diff addPropertyClass2InAssociation = Iterators.find(differences.iterator(),
				addPropertyClass2InAssociationDescription);

		assertNotNull(addPropertyClass2);
		assertNotNull(addRefAssociationInPropertyClass2);
		assertNotNull(addRefTypeInPropertyClass2);
		assertNotNull(addLiteralIntegerInClass2);
		assertNotNull(addUnlimitedNaturalInClass2);
		assertNotNull(addPropertyClass2InAssociation);

		// CHECK EXTENSION
		// No extension anymore
		assertEquals(0, count(differences, instanceOf(AssociationChange.class)));
		Diff changeUMLAssociation = Iterators.find(differences.iterator(), and(
				instanceOf(AssociationChange.class), ofKind(DifferenceKind.CHANGE)), null);
		assertNull(changeUMLAssociation);
		// assertNotNull(changeUMLAssociation);
		// assertEquals(5, changeUMLAssociation.getRefinedBy().size());
		// assertTrue(changeUMLAssociation.getRefinedBy().contains(addRefTypeInPropertyClass2));
		// assertTrue(changeUMLAssociation.getRefinedBy().contains(addLiteralIntegerInClass2));
		// assertTrue(changeUMLAssociation.getRefinedBy().contains(addUnlimitedNaturalInClass2));
		// assertTrue(changeUMLAssociation.getRefinedBy().contains(addRefAssociationInPropertyClass2));
		// assertTrue(changeUMLAssociation.getRefinedBy().contains(addPropertyClass2));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(0, addPropertyClass2.getRequires().size());

			assertEquals(1, addRefAssociationInPropertyClass2.getRequires().size());
			assertTrue(addRefAssociationInPropertyClass2.getRequires().contains(addPropertyClass2));

			assertEquals(1, addRefTypeInPropertyClass2.getRequires().size());
			assertTrue(addRefTypeInPropertyClass2.getRequires().contains(addPropertyClass2));

			// assertEquals(0, changeUMLAssociation.getRequires().size());

			assertEquals(1, addLiteralIntegerInClass2.getRequires().size());
			assertTrue(addLiteralIntegerInClass2.getRequires().contains(addPropertyClass2));

			assertEquals(1, addUnlimitedNaturalInClass2.getRequires().size());
			assertTrue(addUnlimitedNaturalInClass2.getRequires().contains(addPropertyClass2));
		} else {
			assertEquals(5, addPropertyClass2.getRequires().size());
			assertTrue(addPropertyClass2.getRequires().contains(addLiteralIntegerInClass2));
			assertTrue(addPropertyClass2.getRequires().contains(addUnlimitedNaturalInClass2));
			assertTrue(addPropertyClass2.getRequires().contains(addRefAssociationInPropertyClass2));
			assertTrue(addPropertyClass2.getRequires().contains(addRefTypeInPropertyClass2));
			assertTrue(addPropertyClass2.getRequires().contains(addPropertyClass2InAssociation));

			assertEquals(0, addRefAssociationInPropertyClass2.getRequires().size());
			assertEquals(0, addRefTypeInPropertyClass2.getRequires().size());
			// assertEquals(0, changeUMLAssociation.getRequires().size());
			assertEquals(0, addLiteralIntegerInClass2.getRequires().size());
			assertEquals(0, addUnlimitedNaturalInClass2.getRequires().size());
		}

		testIntersections(comparison);
	}

	private static Predicate<? super Diff> addedLowerValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName), onFeature("lowerValue"));
	}

	private static Predicate<? super Diff> addedUpperValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName), onFeature("upperValue"));
	}

	private static Predicate<? super Diff> removedLowerValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.DELETE), onEObject(qualifiedName), onFeature("lowerValue"));
	}

	private static Predicate<? super Diff> removedUpperValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.DELETE), onEObject(qualifiedName), onFeature("upperValue"));
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
