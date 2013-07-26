package org.eclipse.emf.compare.uml2.tests.profiles;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractDynamicProfileTest;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.profiles.data.dynamic.DynamicProfileInputData;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class DynamicProfileTest extends AbstractDynamicProfileTest {

	private DynamicProfileInputData input = new DynamicProfileInputData();

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

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

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
		assertSame(Integer.valueOf(1), count(differences, instanceOf(ProfileApplicationChange.class)));
		Diff addUMLProfileApplication = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLProfileApplication);
		assertSame(Integer.valueOf(4), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addAppliedProfileInProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
		assertTrue(addUMLProfileApplication.getRefinedBy().contains(addUMLAnnotation));

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(0), Integer.valueOf(addUMLProfileApplication.getRequires().size()));
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(0), Integer.valueOf(addProfileApplication.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLAnnotation.getRequires().size()));
			assertTrue(addUMLAnnotation.getRequires().contains(addProfileApplication));

			assertSame(Integer.valueOf(1), Integer.valueOf(addReferencesInUMLAnnotation.getRequires().size()));
			assertTrue(addReferencesInUMLAnnotation.getRequires().contains(addUMLAnnotation));

		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(addProfileApplication.getRequires().size()));
			assertTrue(addProfileApplication.getRequires().contains(addAppliedProfileInProfileApplication));
			assertTrue(addProfileApplication.getRequires().contains(addUMLAnnotation));

			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLAnnotation.getRequires().size()));
			assertTrue(addUMLAnnotation.getRequires().contains(addReferencesInUMLAnnotation));

			assertSame(Integer.valueOf(0), Integer.valueOf(addReferencesInUMLAnnotation.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getEquivalences().size()));

		testIntersections(comparison);

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
