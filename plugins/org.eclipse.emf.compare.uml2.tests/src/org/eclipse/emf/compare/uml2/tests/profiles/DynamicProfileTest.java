/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Christian W. Damus - bug 522064
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.profiles;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractDynamicProfileTest;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.profiles.data.dynamic.DynamicProfileInputData;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class DynamicProfileTest extends AbstractDynamicProfileTest {

	private DynamicProfileInputData input = new DynamicProfileInputData();

	@BeforeClass
	public static void setupClass() {
		initEPackageNsURIToProfileLocationMap();
	}

	@AfterClass
	public static void teardownClass() {
		resetEPackageNsURIToProfileLocationMap();
	}

	@Test
	public void testDynamicA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLDynamicA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRDynamicA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testDynamicA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLDynamicA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testMergeLtRDynamicA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testDynamicA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLDynamicA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRDynamicA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testDynamicA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLDynamicA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testMergeLtRDynamicA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Left();

		testMergeLeftToRight(left, right, left);
	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = compare(left, right);

		assertTrue(comparison.getDifferences().isEmpty());
	}

	/**
	 * Verify that a fatal error is reported when, in a three-way comparison, the origin has a different
	 * version of a profile applied than either other side.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();
		final Resource origin = input.getA3Origin();

		final Comparison comparison = compare(left, right, origin);

		assertThat("Comparison not cancelled", comparison.getDifferences(), empty());
		assertThat(comparison.getDiagnostic().getSeverity(), greaterThanOrEqualTo(Diagnostic.ERROR));
	}

	/**
	 * Verify that a sensible comparison is computed, in a three-way comparison, when the origin does not have
	 * the profile applied that the other two sides have applied at the same version.
	 */
	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();
		final Resource origin = input.getA4Origin();

		final Comparison comparison = compare(left, right, origin);

		assertThat("Comparison cancelled", comparison.getDifferences(), not(empty()));

		Iterable<Diff> leftDiffs = filter(comparison.getDifferences(), fromSide(LEFT));
		Iterable<Diff> rightDiffs = filter(comparison.getDifferences(), fromSide(RIGHT));

		Diff leftPA = getOnlyElement(filter(leftDiffs, instanceOf(ProfileApplicationChange.class)));
		assertThat(leftPA.getKind(), is(DifferenceKind.ADD));
		Diff rightPA = getOnlyElement(filter(rightDiffs, instanceOf(ProfileApplicationChange.class)));
		assertThat(rightPA.getKind(), is(DifferenceKind.ADD));
		Set<Conflict> conflicts = getConflicts(leftPA);
		assertThat("Left profile application not conflicting", conflicts, not(empty()));
		assertThat("Left and right profile applications not conflicting exactly", getConflicts(rightPA),
				is(conflicts));
		assertThat("Some profile application conflicts is real", conflicts, everyItem(conflictIs(PSEUDO)));

		Diff leftSA = getOnlyElement(filter(leftDiffs, instanceOf(StereotypeApplicationChange.class)));
		assertThat(leftSA.getKind(), is(DifferenceKind.ADD));
		Diff rightSA = getOnlyElement(filter(rightDiffs, instanceOf(StereotypeApplicationChange.class)));
		assertThat(rightSA.getKind(), is(DifferenceKind.ADD));
	}

	/**
	 * Verify that a fatal error is reported when, in a three-way comparison, the origin has no profile
	 * definition annotation, indicating a static profile definition (simulated for this test).
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();
		final Resource origin = input.getA5Origin();

		final Comparison comparison = compare(left, right, origin);

		assertThat("Comparison not cancelled", comparison.getDifferences(), empty());
		assertThat(comparison.getDiagnostic().getSeverity(), greaterThanOrEqualTo(Diagnostic.ERROR));
	}

	/**
	 * Gets all of the conflicts of a {@code diff} and its refinements.
	 * 
	 * @param diff
	 *            a diff
	 * @return all of its conflicts
	 */
	private Set<Conflict> getConflicts(Diff diff) {
		ImmutableSet.Builder<Conflict> result = ImmutableSet.builder();
		result.addAll(Optional.fromNullable(diff.getConflict()).asSet());

		for (Diff refining : diff.getRefinedBy()) {
			result.addAll(getConflicts(refining));
		}

		return result.build();
	}

	/**
	 * Hamcrest matcher for conflicts of some {@code kind}.
	 * 
	 * @param kind
	 *            the kind of conflict to match
	 * @return the conflict-kind matcher
	 */
	private Matcher<Conflict> conflictIs(final ConflictKind kind) {
		return new CustomTypeSafeMatcher<Conflict>(String.format("%s conflict", kind)) {
			@Override
			protected boolean matchesSafely(Conflict item) {
				return item.getKind() == kind;
			}
		};
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
			addUMLProfileApplication = Iterators.find(differences.iterator(),
					and(instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(),
					and(instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
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
