package org.eclipse.emf.compare.uml2.tests.profiles;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.added;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.addedToReference;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.changedReference;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.ofKind;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.removed;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.removedFromReference;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.FilterComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.profiles.data.ProfileInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Test;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;

@SuppressWarnings("nls")
public class AddProfileTest extends AbstractTest {

	private ProfileInputData input = new ProfileInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right
				.getResourceSet());
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(), left
				.getResourceSet());
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left.getResourceSet(), right
				.getResourceSet());
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(), left
				.getResourceSet());
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		FilterComparisonScope scope = new DefaultComparisonScope(left, right, null);
		scope.setResourceSetContentFilter(new Predicate<Resource>() {

			public boolean apply(Resource in) {
				return !in.getURI().toString().equals(
						UMLResource.PROFILES_PATHMAP + "/" + "Ecore.profile.uml");
			}
		});
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testA31UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right.getResourceSet(), left
				.getResourceSet());
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB3(TestKind.DELETE, comparison);
	}

	private static void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addProfileApplicationDescription = removed("aModel.Ecore Profile"); //$NON-NLS-1$

			addAppliedProfileInProfileApplicationDescription = changedReference("aModel.Ecore Profile",
					"appliedProfile", "Ecore", null);

			addUMLAnnotationDescription = removed("aModel.Ecore Profile.UML");
			addReferencesInUMLAnnotationDescription = removedFromReference("aModel.Ecore Profile.UML",
					"references", "Ecore.UML.Ecore");
		} else {
			addProfileApplicationDescription = added("aModel.Ecore Profile"); //$NON-NLS-1$

			addAppliedProfileInProfileApplicationDescription = changedReference("aModel.Ecore Profile",
					"appliedProfile", null, "Ecore");

			addUMLAnnotationDescription = added("aModel.Ecore Profile.UML");
			addReferencesInUMLAnnotationDescription = addedToReference("aModel.Ecore Profile.UML",
					"references", "Ecore.UML.Ecore");
		}

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
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));
			assertTrue(addUMLProfileApplication.getRefinedBy()
					.contains(addAppliedProfileInProfileApplication));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
		}

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

	}

	private static void testAB2(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(8), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;
		Predicate<? super Diff> addStereotypeApplicationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addProfileApplicationDescription = removed("aModel.UML2CompareTestProfile"); //$NON-NLS-1$

			addAppliedProfileInProfileApplicationDescription = changedReference(
					"aModel.UML2CompareTestProfile", "appliedProfile", "UML2CompareTestProfile", null);

			addUMLAnnotationDescription = removed("aModel.UML2CompareTestProfile.UML");
			addReferencesInUMLAnnotationDescription = removedFromReference(
					"aModel.UML2CompareTestProfile.UML", "references",
					"UML2CompareTestProfile.UML.uml2comparetestprofile");
		} else {
			addProfileApplicationDescription = added("aModel.UML2CompareTestProfile"); //$NON-NLS-1$

			addAppliedProfileInProfileApplicationDescription = changedReference(
					"aModel.UML2CompareTestProfile", "appliedProfile", null, "UML2CompareTestProfile");

			addUMLAnnotationDescription = added("aModel.UML2CompareTestProfile.UML");
			addReferencesInUMLAnnotationDescription = addedToReference("aModel.UML2CompareTestProfile.UML",
					"references", "UML2CompareTestProfile.UML.uml2comparetestprofile");
		}

		addStereotypeApplicationDescription = instanceOf(ResourceAttachmentChange.class);

		final Diff addProfileApplication = Iterators.find(differences.iterator(),
				addProfileApplicationDescription);
		final Diff addAppliedProfileInProfileApplication = Iterators.find(differences.iterator(),
				addAppliedProfileInProfileApplicationDescription);
		final Diff addUMLAnnotation = Iterators.find(differences.iterator(), addUMLAnnotationDescription);
		final Diff addReferencesInUMLAnnotation = Iterators.find(differences.iterator(),
				addReferencesInUMLAnnotationDescription);
		final Diff addStereotypeApplication = Iterators.find(differences.iterator(),
				addStereotypeApplicationDescription);

		assertNotNull(addProfileApplication);
		assertNotNull(addAppliedProfileInProfileApplication);
		assertNotNull(addUMLAnnotation);
		assertNotNull(addReferencesInUMLAnnotation);
		assertNotNull(addStereotypeApplication);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(ProfileApplicationChange.class)));
		assertSame(Integer.valueOf(1), count(differences, instanceOf(StereotypeApplicationChange.class)));
		Diff addUMLProfileApplication = null;
		Diff addUMLStereotypeApplication = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));
			assertTrue(addUMLProfileApplication.getRefinedBy()
					.contains(addAppliedProfileInProfileApplication));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLProfileApplication);
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
		}
		assertSame(Integer.valueOf(1), Integer.valueOf(addUMLStereotypeApplication.getRefinedBy().size()));
		assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addStereotypeApplication));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(0), Integer.valueOf(addProfileApplication.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLAnnotation.getRequires().size()));
			assertTrue(addUMLAnnotation.getRequires().contains(addProfileApplication));

			assertSame(Integer.valueOf(1), Integer.valueOf(addReferencesInUMLAnnotation.getRequires().size()));
			assertTrue(addReferencesInUMLAnnotation.getRequires().contains(addUMLAnnotation));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLProfileApplication.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLStereotypeApplication.getRequires().size()));
			assertTrue(addUMLStereotypeApplication.getRequires().contains(addUMLProfileApplication));

		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(addProfileApplication.getRequires().size()));
			assertTrue(addProfileApplication.getRequires().contains(addAppliedProfileInProfileApplication));
			assertTrue(addProfileApplication.getRequires().contains(addUMLAnnotation));

			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLAnnotation.getRequires().size()));
			assertTrue(addUMLAnnotation.getRequires().contains(addReferencesInUMLAnnotation));

			assertSame(Integer.valueOf(0), Integer.valueOf(addReferencesInUMLAnnotation.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLProfileApplication.getRequires().size()));
			assertTrue(addUMLProfileApplication.getRequires().contains(addUMLStereotypeApplication));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLStereotypeApplication.getRequires().size()));

		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getEquivalences().size()));

	}

	private void testAB3(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addProfileApplicationDescription = removed("aModel.UML2CompareTestProfile"); //$NON-NLS-1$

			addAppliedProfileInProfileApplicationDescription = changedReference(
					"aModel.UML2CompareTestProfile", "appliedProfile", "UML2CompareTestProfile", null);

			addUMLAnnotationDescription = removed("aModel.UML2CompareTestProfile.UML");
			addReferencesInUMLAnnotationDescription = removedFromReference(
					"aModel.UML2CompareTestProfile.UML", "references",
					"UML2CompareTestProfile.UML.uml2comparetestprofile");
		} else {
			addProfileApplicationDescription = added("aModel.UML2CompareTestProfile"); //$NON-NLS-1$

			addAppliedProfileInProfileApplicationDescription = changedReference(
					"aModel.UML2CompareTestProfile", "appliedProfile", null, "UML2CompareTestProfile");

			addUMLAnnotationDescription = added("aModel.UML2CompareTestProfile.UML");
			addReferencesInUMLAnnotationDescription = addedToReference("aModel.UML2CompareTestProfile.UML",
					"references", "UML2CompareTestProfile.UML.uml2comparetestprofile");
		}

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
		assertSame(Integer.valueOf(1), count(differences, instanceOf(StereotypeApplicationChange.class)));
		Diff addUMLProfileApplication = null;
		Diff addUMLStereotypeApplication = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));
			assertTrue(addUMLProfileApplication.getRefinedBy()
					.contains(addAppliedProfileInProfileApplication));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLProfileApplication);
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
		}
		assertSame(Integer.valueOf(1), Integer.valueOf(addUMLStereotypeApplication.getRefinedBy().size()));
		assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addReferencesInUMLAnnotation));

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

	}
}
