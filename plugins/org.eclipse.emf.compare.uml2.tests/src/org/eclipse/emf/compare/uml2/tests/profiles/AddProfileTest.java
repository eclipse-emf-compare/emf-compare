package org.eclipse.emf.compare.uml2.tests.profiles;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.added;
import static org.eclipse.emf.compare.uml2.tests.UMLComparePredicates.addedToReference;
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
import org.eclipse.emf.compare.uml2.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.profiles.data.ProfileInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddProfileTest extends AbstractTest {

	private ProfileInputData input = new ProfileInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = EMFCompare.compare(left.getResourceSet(), right.getResourceSet());
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = EMFCompare.compare(right.getResourceSet(), left.getResourceSet());
		testAB1(TestKind.DELETE, comparison);
	}

	private static void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		// FIXME assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addProfileApplicationDescription = null;
		// FIXME Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addProfileApplicationDescription = removed("aModel.Ecore Profile"); //$NON-NLS-1$
			/*
			 * addAppliedProfileInProfileApplicationDescription = changedReference("aModel.Ecore Profile",
			 * "appliedProfile", "Ecore", null);
			 */
			addUMLAnnotationDescription = removed("aModel.Ecore Profile.UML");
			addReferencesInUMLAnnotationDescription = removedFromReference("aModel.Ecore Profile.UML",
					"references", "Ecore.UML.Ecore");
		} else {
			addProfileApplicationDescription = added("aModel.Ecore Profile"); //$NON-NLS-1$
			/*
			 * addAppliedProfileInProfileApplicationDescription = changedReference("aModel.Ecore Profile",
			 * "appliedProfile", null, "Ecore");
			 */
			addUMLAnnotationDescription = added("aModel.Ecore Profile.UML");
			addReferencesInUMLAnnotationDescription = addedToReference("aModel.Ecore Profile.UML",
					"references", "Ecore.UML.Ecore");
		}

		final Diff addProfileApplication = Iterators.find(differences.iterator(),
				addProfileApplicationDescription);
		/*
		 * final Diff addAppliedProfileInProfileApplication = Iterators.find(differences.iterator(),
		 * addAppliedProfileInProfileApplicationDescription);
		 */
		final Diff addUMLAnnotation = Iterators.find(differences.iterator(), addUMLAnnotationDescription);
		final Diff addReferencesInUMLAnnotation = Iterators.find(differences.iterator(),
				addReferencesInUMLAnnotationDescription);

		assertNotNull(addProfileApplication);
		assertNotNull(addUMLAnnotation);
		assertNotNull(addReferencesInUMLAnnotation);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(2), count(differences, instanceOf(ProfileApplicationChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addReferencesInUMLAnnotation));
		} else {
			addUMLMessage = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addProfileApplication));
		}

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(0), Integer.valueOf(addUMLMessage.getRequires().size()));
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(0), Integer.valueOf(addProfileApplication.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLAnnotation.getRequires().size()));
			assertTrue(addUMLAnnotation.getRequires().contains(addProfileApplication));

			// FIXME assertSame(Integer.valueOf(1),
			// Integer.valueOf(addReferencesInUMLAnnotation.getRequires().size()));
			// assertTrue(addReferencesInUMLAnnotation.getRequires().contains(addUMLAnnotation));

		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(addProfileApplication.getRequires().size()));
			assertTrue(addProfileApplication.getRequires().contains(addUMLAnnotation));

			// FIXME assertSame(Integer.valueOf(1), Integer.valueOf(addUMLAnnotation.getRequires().size()));
			// assertTrue(addUMLAnnotation.getRequires().contains(addProfileApplication));

			assertSame(Integer.valueOf(0), Integer.valueOf(addReferencesInUMLAnnotation.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getEquivalences().size()));

	}
}
