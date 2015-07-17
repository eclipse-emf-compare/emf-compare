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
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
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
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.association.data.AssociationInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddAssociationTest extends AbstractUMLTest {

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
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLA11UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRA11UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testMergeLtRA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeLeftToRight(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 16 differences
		assertEquals(16, differences.size());

		Predicate<? super Diff> addAssociationDescription = null;
		Predicate<? super Diff> addNavigableOwnedEndClass1InAssociationDescription = null;
		Predicate<? super Diff> addNavigableOwnedEndClass2InAssociationDescription = null;
		Predicate<? super Diff> addRefAssociationInPropertyClass1Description = null;
		Predicate<? super Diff> addRefTypeInPropertyClass1Description = null;
		Predicate<? super Diff> addRefAssociationInPropertyClass2Description = null;
		Predicate<? super Diff> addRefTypeInPropertyClass2Description = null;
		Predicate<? super Diff> addLiteralIntegerInClass1Description = null;
		Predicate<? super Diff> addUnlimitedNaturalInClass1Description = null;
		Predicate<? super Diff> addLiteralIntegerInClass2Description = null;
		Predicate<? super Diff> addUnlimitedNaturalInClass2Description = null;
		Predicate<? super Diff> addMemberEndClass1InAssociationDescription = null;
		Predicate<? super Diff> addMemberEndClass2InAssociationDescription = null;
		Predicate<? super Diff> addOwnedEndClass1Description = null;
		Predicate<? super Diff> addOwnedEndClass2Description = null;

		if (kind.equals(TestKind.DELETE)) {
			addAssociationDescription = removed("myModel.class1sToClass2s"); //$NON-NLS-1$
			addNavigableOwnedEndClass1InAssociationDescription = removedFromReference(
					"myModel.class1sToClass2s", "navigableOwnedEnd", "myModel.class1sToClass2s.class1s");
			addNavigableOwnedEndClass2InAssociationDescription = removedFromReference(
					"myModel.class1sToClass2s", "navigableOwnedEnd", "myModel.class1sToClass2s.class2s");
			addRefAssociationInPropertyClass1Description = changedReference(
					"myModel.class1sToClass2s.class1s", "association", "myModel.class1sToClass2s", null);
			addRefTypeInPropertyClass1Description = changedReference("myModel.class1sToClass2s.class1s",
					"type", "myModel.Class1", null);
			addRefAssociationInPropertyClass2Description = changedReference(
					"myModel.class1sToClass2s.class2s", "association", "myModel.class1sToClass2s", null);
			addRefTypeInPropertyClass2Description = changedReference("myModel.class1sToClass2s.class2s",
					"type", "myModel.Class2", null);
			addLiteralIntegerInClass1Description = removedLowerValueIn("myModel.class1sToClass2s.class1s");
			addUnlimitedNaturalInClass1Description = removedUpperValueIn("myModel.class1sToClass2s.class1s");
			addLiteralIntegerInClass2Description = removedLowerValueIn("myModel.class1sToClass2s.class2s");
			addUnlimitedNaturalInClass2Description = removedUpperValueIn("myModel.class1sToClass2s.class2s");
			addMemberEndClass1InAssociationDescription = removedFromReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class1s");
			addMemberEndClass2InAssociationDescription = removedFromReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class2s");
			addOwnedEndClass1Description = removedFromReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class1s");
			addOwnedEndClass2Description = removedFromReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class2s");
		} else {
			addAssociationDescription = added("myModel.class1sToClass2s"); //$NON-NLS-1$
			addNavigableOwnedEndClass1InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"navigableOwnedEnd", "myModel.class1sToClass2s.class1s");
			addNavigableOwnedEndClass2InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"navigableOwnedEnd", "myModel.class1sToClass2s.class2s");
			addRefAssociationInPropertyClass1Description = changedReference(
					"myModel.class1sToClass2s.class1s", "association", null, "myModel.class1sToClass2s");
			addRefTypeInPropertyClass1Description = changedReference("myModel.class1sToClass2s.class1s",
					"type", null, "myModel.Class1");
			addRefAssociationInPropertyClass2Description = changedReference(
					"myModel.class1sToClass2s.class2s", "association", null, "myModel.class1sToClass2s");
			addRefTypeInPropertyClass2Description = changedReference("myModel.class1sToClass2s.class2s",
					"type", null, "myModel.Class2");
			addLiteralIntegerInClass1Description = addedLowerValueIn("myModel.class1sToClass2s.class1s");
			addUnlimitedNaturalInClass1Description = addedUpperValueIn("myModel.class1sToClass2s.class1s");
			addLiteralIntegerInClass2Description = addedLowerValueIn("myModel.class1sToClass2s.class2s");
			addUnlimitedNaturalInClass2Description = addedUpperValueIn("myModel.class1sToClass2s.class2s");
			addMemberEndClass1InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class1s");
			addMemberEndClass2InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class2s");
			addOwnedEndClass1Description = addedToReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class1s");
			addOwnedEndClass2Description = addedToReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class2s");
		}

		final Diff addAssociation = Iterators.find(differences.iterator(), addAssociationDescription);
		final Diff addNavigableOwnedEndClass1InAssociation = Iterators.find(differences.iterator(),
				addNavigableOwnedEndClass1InAssociationDescription);
		final Diff addNavigableOwnedEndClass2InAssociation = Iterators.find(differences.iterator(),
				addNavigableOwnedEndClass2InAssociationDescription);
		final Diff addRefAssociationInPropertyClass1 = Iterators.find(differences.iterator(),
				addRefAssociationInPropertyClass1Description);
		final Diff addRefTypeInPropertyClass1 = Iterators.find(differences.iterator(),
				addRefTypeInPropertyClass1Description);
		final Diff addRefAssociationInPropertyClass2 = Iterators.find(differences.iterator(),
				addRefAssociationInPropertyClass2Description);
		final Diff addRefTypeInPropertyClass2 = Iterators.find(differences.iterator(),
				addRefTypeInPropertyClass2Description);
		final Diff addLiteralIntegerInClass1 = Iterators.find(differences.iterator(),
				addLiteralIntegerInClass1Description);
		final Diff addUnlimitedNaturalInClass1 = Iterators.find(differences.iterator(),
				addUnlimitedNaturalInClass1Description);
		final Diff addLiteralIntegerInClass2 = Iterators.find(differences.iterator(),
				addLiteralIntegerInClass2Description);
		final Diff addUnlimitedNaturalInClass2 = Iterators.find(differences.iterator(),
				addUnlimitedNaturalInClass2Description);
		final Diff addMemberEndClass1InAssociation = Iterators.find(differences.iterator(),
				addMemberEndClass1InAssociationDescription);
		final Diff addMemberEndClass2InAssociation = Iterators.find(differences.iterator(),
				addMemberEndClass2InAssociationDescription);
		final Diff addOwnedEndClass1InAssociation = Iterators.find(differences.iterator(),
				addOwnedEndClass1Description);
		final Diff addOwnedEndClass2InAssociation = Iterators.find(differences.iterator(),
				addOwnedEndClass2Description);

		assertNotNull(addNavigableOwnedEndClass1InAssociation);
		assertNotNull(addNavigableOwnedEndClass2InAssociation);
		assertNotNull(addAssociation);
		assertNotNull(addRefAssociationInPropertyClass1);
		assertNotNull(addRefTypeInPropertyClass1);
		assertNotNull(addRefAssociationInPropertyClass2);
		assertNotNull(addRefTypeInPropertyClass2);
		assertNotNull(addLiteralIntegerInClass1);
		assertNotNull(addUnlimitedNaturalInClass1);
		assertNotNull(addLiteralIntegerInClass2);
		assertNotNull(addUnlimitedNaturalInClass2);
		assertNotNull(addMemberEndClass1InAssociation);
		assertNotNull(addMemberEndClass2InAssociation);
		assertNotNull(addOwnedEndClass1InAssociation);
		assertNotNull(addOwnedEndClass2InAssociation);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(AssociationChange.class)));
		Diff addUMLAssociation = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLAssociation = Iterators.find(differences.iterator(), and(
					instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLAssociation = Iterators.find(differences.iterator(), and(
					instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLAssociation);
		assertEquals(15, addUMLAssociation.getRefinedBy().size());
		assertTrue(addUMLAssociation.getRefinedBy().contains(addAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addNavigableOwnedEndClass1InAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addNavigableOwnedEndClass2InAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefAssociationInPropertyClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefTypeInPropertyClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefAssociationInPropertyClass2));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefTypeInPropertyClass2));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addLiteralIntegerInClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addUnlimitedNaturalInClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addLiteralIntegerInClass2));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addUnlimitedNaturalInClass2));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addMemberEndClass1InAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addMemberEndClass2InAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addOwnedEndClass1InAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addOwnedEndClass2InAssociation));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(2, addRefAssociationInPropertyClass1.getRequires().size());
			assertTrue(addRefAssociationInPropertyClass1.getRequires().contains(
					addOwnedEndClass1InAssociation));
			assertTrue(addRefAssociationInPropertyClass1.getRequires().contains(addAssociation));

			assertEquals(1, addRefTypeInPropertyClass1.getRequires().size());
			assertTrue(addRefTypeInPropertyClass1.getRequires().contains(addOwnedEndClass1InAssociation));

			assertEquals(2, addRefAssociationInPropertyClass2.getRequires().size());
			assertTrue(addRefAssociationInPropertyClass2.getRequires().contains(
					addOwnedEndClass2InAssociation));
			assertTrue(addRefAssociationInPropertyClass2.getRequires().contains(addAssociation));

			assertEquals(1, addRefTypeInPropertyClass2.getRequires().size());
			assertTrue(addRefTypeInPropertyClass2.getRequires().contains(addOwnedEndClass2InAssociation));

			assertEquals(0, addAssociation.getRequires().size());
			assertEquals(0, addUMLAssociation.getRequires().size());

			assertEquals(2, addNavigableOwnedEndClass1InAssociation.getRequires().size());
			assertTrue(addNavigableOwnedEndClass1InAssociation.getRequires().contains(addAssociation));
			assertTrue(addNavigableOwnedEndClass1InAssociation.getRequires().contains(
					addOwnedEndClass1InAssociation));

			assertEquals(2, addNavigableOwnedEndClass2InAssociation.getRequires().size());
			assertTrue(addNavigableOwnedEndClass2InAssociation.getRequires().contains(addAssociation));
			assertTrue(addNavigableOwnedEndClass2InAssociation.getRequires().contains(
					addOwnedEndClass2InAssociation));

			assertEquals(1, addLiteralIntegerInClass1.getRequires().size());
			assertTrue(addLiteralIntegerInClass1.getRequires().contains(addOwnedEndClass1InAssociation));

			assertEquals(1, addUnlimitedNaturalInClass1.getRequires().size());
			assertTrue(addUnlimitedNaturalInClass1.getRequires().contains(addOwnedEndClass1InAssociation));

			assertEquals(1, addLiteralIntegerInClass2.getRequires().size());
			assertTrue(addLiteralIntegerInClass2.getRequires().contains(addOwnedEndClass2InAssociation));

			assertEquals(1, addUnlimitedNaturalInClass2.getRequires().size());
			assertTrue(addUnlimitedNaturalInClass2.getRequires().contains(addOwnedEndClass2InAssociation));
		} else {
			assertEquals(0, addNavigableOwnedEndClass1InAssociation.getRequires().size());

			assertEquals(0, addNavigableOwnedEndClass2InAssociation.getRequires().size());

			assertEquals(0, addRefAssociationInPropertyClass1.getRequires().size());
			assertEquals(0, addRefTypeInPropertyClass1.getRequires().size());
			assertEquals(0, addRefAssociationInPropertyClass2.getRequires().size());
			assertEquals(0, addRefTypeInPropertyClass2.getRequires().size());

			assertEquals(8, addAssociation.getRequires().size());
			assertTrue(addAssociation.getRequires().contains(addNavigableOwnedEndClass1InAssociation));
			assertTrue(addAssociation.getRequires().contains(addNavigableOwnedEndClass2InAssociation));
			assertTrue(addAssociation.getRequires().contains(addRefAssociationInPropertyClass1));
			assertTrue(addAssociation.getRequires().contains(addRefAssociationInPropertyClass2));
			assertTrue(addAssociation.getRequires().contains(addOwnedEndClass1InAssociation));
			assertTrue(addAssociation.getRequires().contains(addOwnedEndClass2InAssociation));
			assertTrue(addAssociation.getRequires().contains(addMemberEndClass1InAssociation));
			assertTrue(addAssociation.getRequires().contains(addMemberEndClass2InAssociation));

			assertEquals(0, addUMLAssociation.getRequires().size());

			assertEquals(0, addLiteralIntegerInClass1.getRequires().size());
			assertEquals(0, addUnlimitedNaturalInClass1.getRequires().size());
			assertEquals(0, addLiteralIntegerInClass2.getRequires().size());
			assertEquals(0, addUnlimitedNaturalInClass2.getRequires().size());

			testIntersections(comparison);
		}
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
