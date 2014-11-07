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
package org.eclipse.emf.compare.uml2.tests.profiles;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
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
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractStaticProfileTest;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.profiles.data.static_.StaticProfileInputData;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class StaticProfileTest extends AbstractStaticProfileTest {

	private StaticProfileInputData input = new StaticProfileInputData();

	@Test
	public void testStaticA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLStaticA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRStaticA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testStaticA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLStaticA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testMergeLtRStaticA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testStaticA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLStaticA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRStaticA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testStaticA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLStaticA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRStaticA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(left, right, right);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertEquals(5, differences.size());

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(UMLPackage.Literals.PACKAGE__PROFILE_APPLICATION));

			// addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
			// ofKind(DifferenceKind.CHANGE),
			// onRealFeature(UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE),
			// not(isChangeAdd()));

			addUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS));

			addReferencesInUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE),
					onRealFeature(EcorePackage.Literals.EANNOTATION__REFERENCES));
		} else {
			addProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.ADD),
					onRealFeature(UMLPackage.Literals.PACKAGE__PROFILE_APPLICATION));

			// addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
			// ofKind(DifferenceKind.CHANGE),
			// onRealFeature(UMLPackage.Literals.PROFILE_APPLICATION__APPLIED_PROFILE), isChangeAdd());

			addUMLAnnotationDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD),
					onRealFeature(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS));

			addReferencesInUMLAnnotationDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.ADD), onRealFeature(EcorePackage.Literals.EANNOTATION__REFERENCES));
		}

		addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
				ofKind(DifferenceKind.CHANGE), onFeature("appliedProfile"));

		final Diff addProfileApplication = Iterators.find(differences.iterator(),
				addProfileApplicationDescription);
		final Diff addAppliedProfileInProfileApplication = Iterators.find(differences.iterator(),
				addAppliedProfileInProfileApplicationDescription);
		final Diff addUMLAnnotation = Iterators.find(differences.iterator(), addUMLAnnotationDescription);
		final Diff addReferencesInUMLAnnotation = Iterators.find(differences.iterator(),
				addReferencesInUMLAnnotationDescription);

		assertNotNull(addProfileApplication);
		assertNotNull(addAppliedProfileInProfileApplication);
		assertNotNull(addUMLAnnotation);
		assertNotNull(addReferencesInUMLAnnotation);

		// CHECK EXTENSION
		assertEquals(1, count(differences, instanceOf(ProfileApplicationChange.class)));
		Diff addUMLProfileApplication = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLProfileApplication);
		assertEquals(4, addUMLProfileApplication.getRefinedBy().size());
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addAppliedProfileInProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addUMLAnnotation));

		// CHECK REQUIREMENT
		assertEquals(0, addUMLProfileApplication.getRequires().size());
		if (kind.equals(TestKind.ADD)) {

			assertEquals(0, addProfileApplication.getRequires().size());

			assertEquals(1, addUMLAnnotation.getRequires().size());
			assertTrue(addUMLAnnotation.getRequires().contains(addProfileApplication));

			assertEquals(1, addReferencesInUMLAnnotation.getRequires().size());
			assertTrue(addReferencesInUMLAnnotation.getRequires().contains(addUMLAnnotation));

		} else {
			assertEquals(2, addProfileApplication.getRequires().size());
			assertTrue(addProfileApplication.getRequires().contains(addAppliedProfileInProfileApplication));
			assertTrue(addProfileApplication.getRequires().contains(addUMLAnnotation));

			assertEquals(1, addUMLAnnotation.getRequires().size());
			assertTrue(addUMLAnnotation.getRequires().contains(addReferencesInUMLAnnotation));

			assertEquals(0, addReferencesInUMLAnnotation.getRequires().size());
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
