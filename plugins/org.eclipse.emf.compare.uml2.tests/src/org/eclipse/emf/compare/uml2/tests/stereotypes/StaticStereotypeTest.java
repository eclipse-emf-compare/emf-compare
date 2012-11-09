package org.eclipse.emf.compare.uml2.tests.stereotypes;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueIs;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueNameMatches;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.stereotypes.data.static_.StaticStereotypeInputData;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.After;
import org.junit.Test;

@SuppressWarnings("nls")
public class StaticStereotypeTest extends AbstractTest {

	private StaticStereotypeInputData input = new StaticStereotypeInputData();

	@After
	public void cleanup() {
		for (ResourceSet set : input.getSets()) {
			for (Resource res : set.getResources()) {
				res.unload();
			}
			set.getResources().clear();
		}
	}

	@Test
	public void testB10UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testB11UseCase() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testB10UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testB11UseCase3way() throws IOException {
		final Resource left = input.getB1Left();
		final Resource right = input.getB1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testB20UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testB21UseCase() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testB20UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testB21UseCase3way() throws IOException {
		final Resource left = input.getB2Left();
		final Resource right = input.getB2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testB30UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testB31UseCase() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testB30UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testB31UseCase3way() throws IOException {
		final Resource left = input.getB3Left();
		final Resource right = input.getB3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testB40UseCase() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB4(TestKind.ADD, comparison);
	}

	@Test
	public void testB41UseCase() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB4(TestKind.DELETE, comparison);
	}

	@Test
	public void testB40UseCase3way() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB4(TestKind.ADD, comparison);
	}

	@Test
	public void testB41UseCase3way() throws IOException {
		final Resource left = input.getB4Left();
		final Resource right = input.getB4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB4(TestKind.DELETE, comparison);
	}

	@Test
	public void testB50UseCase() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB5(TestKind.ADD, comparison);
	}

	@Test
	public void testB51UseCase() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB5(TestKind.DELETE, comparison);
	}

	@Test
	public void testB50UseCase3way() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB5(TestKind.ADD, comparison);
	}

	@Test
	public void testB51UseCase3way() throws IOException {
		final Resource left = input.getB5Left();
		final Resource right = input.getB5Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB5(TestKind.DELETE, comparison);
	}

	@Test
	public void testB60UseCase() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB6(TestKind.ADD, comparison);
	}

	@Test
	public void testB61UseCase() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB6(TestKind.DELETE, comparison);
	}

	@Test
	public void testB60UseCase3way() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB6(TestKind.ADD, comparison);
	}

	@Test
	public void testB61UseCase3way() throws IOException {
		final Resource left = input.getB6Left();
		final Resource right = input.getB6Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB6(TestKind.DELETE, comparison);
	}

	@Test
	public void testB70UseCase() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB7(TestKind.ADD, comparison);
	}

	@Test
	public void testB71UseCase() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB7(TestKind.DELETE, comparison);
	}

	@Test
	public void testB70UseCase3way() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB7(TestKind.ADD, comparison);
	}

	@Test
	public void testB71UseCase3way() throws IOException {
		final Resource left = input.getB7Left();
		final Resource right = input.getB7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB7(TestKind.DELETE, comparison);
	}

	@Test
	public void testB80UseCase() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB8(TestKind.ADD, comparison);
	}

	@Test
	public void testB81UseCase() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB8(TestKind.DELETE, comparison);
	}

	@Test
	public void testB80UseCase3way() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB8(TestKind.ADD, comparison);
	}

	@Test
	public void testB81UseCase3way() throws IOException {
		final Resource left = input.getB8Left();
		final Resource right = input.getB8Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB8(TestKind.DELETE, comparison);
	}

	@Test
	public void testB90UseCase() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB9(TestKind.ADD, comparison);
	}

	@Test
	public void testB91UseCase() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB9(TestKind.DELETE, comparison);
	}

	@Test
	public void testB90UseCase3way() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB9(TestKind.ADD, comparison);
	}

	@Test
	public void testB91UseCase3way() throws IOException {
		final Resource left = input.getB9Left();
		final Resource right = input.getB9Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB9(TestKind.DELETE, comparison);
	}

	@Test
	public void testB100UseCase() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB10(TestKind.ADD, comparison);
	}

	@Test
	public void testB101UseCase() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB10(TestKind.DELETE, comparison);
	}

	@Test
	public void testB100UseCase3way() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB10(TestKind.ADD, comparison);
	}

	@Test
	public void testB101UseCase3way() throws IOException {
		final Resource left = input.getB10Left();
		final Resource right = input.getB10Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB10(TestKind.DELETE, comparison);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(8), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;
		Predicate<? super Diff> addStereotypeApplicationDescription = null;
		Predicate<? super Diff> addRefBaseClassDescription = null;

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

		addStereotypeApplicationDescription = instanceOf(ResourceAttachmentChange.class);
		addRefBaseClassDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("base_Class"));

		final Diff addProfileApplication = Iterators.find(differences.iterator(),
				addProfileApplicationDescription);
		final Diff addAppliedProfileInProfileApplication = Iterators.find(differences.iterator(),
				addAppliedProfileInProfileApplicationDescription);
		final Diff addUMLAnnotation = Iterators.find(differences.iterator(), addUMLAnnotationDescription);
		final Diff addReferencesInUMLAnnotation = Iterators.find(differences.iterator(),
				addReferencesInUMLAnnotationDescription);
		final Diff addStereotypeApplication = Iterators.find(differences.iterator(),
				addStereotypeApplicationDescription);
		final Diff addRefBaseClass = Iterators.find(differences.iterator(), addRefBaseClassDescription);

		assertNotNull(addProfileApplication);
		assertNotNull(addAppliedProfileInProfileApplication);
		assertNotNull(addUMLAnnotation);
		assertNotNull(addReferencesInUMLAnnotation);
		assertNotNull(addStereotypeApplication);
		assertNotNull(addRefBaseClass);

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
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLStereotypeApplication.getRefinedBy().size()));
			assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addRefBaseClass));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLStereotypeApplication.getRefinedBy().size()));
			assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addStereotypeApplication));
			assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addRefBaseClass));
		}

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

	private void testAB2(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addAttributeDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addAttributeDescription = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.DELETE),
					onFeature("manyValuedAttribute"));
		} else {
			addAttributeDescription = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.ADD),
					onFeature("manyValuedAttribute"));
		}

		final Diff addAttribute = Iterators.find(differences.iterator(), addAttributeDescription);
		assertNotNull(addAttribute);

	}

	private void testAB3(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		assertSame(Integer.valueOf(10), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addProfileApplicationDescription = null;
		Predicate<? super Diff> addAppliedProfileInProfileApplicationDescription = null;
		Predicate<? super Diff> addUMLAnnotationDescription = null;
		Predicate<? super Diff> addReferencesInUMLAnnotationDescription = null;
		Predicate<? super Diff> addStereotypeApplicationDescription = null;
		Predicate<? super Diff> addModelDescription = null;
		Predicate<? super Diff> addClassDescription = null;
		Predicate<? super Diff> addRefBaseClassDescription = null;

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
			addModelDescription = removed("model.MyNiceModel");
			addClassDescription = removed("model.MyNiceModel.Class1");

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
			addModelDescription = added("model.MyNiceModel");
			addClassDescription = added("model.MyNiceModel.Class1");
		}

		addAppliedProfileInProfileApplicationDescription = and(instanceOf(ReferenceChange.class),
				ofKind(DifferenceKind.CHANGE), onFeature("appliedProfile"));

		addStereotypeApplicationDescription = instanceOf(ResourceAttachmentChange.class);
		addRefBaseClassDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("base_Class"));

		final Diff addProfileApplication = Iterators.find(differences.iterator(),
				addProfileApplicationDescription);
		final Diff addAppliedProfileInProfileApplication = Iterators.find(differences.iterator(),
				addAppliedProfileInProfileApplicationDescription);
		final Diff addUMLAnnotation = Iterators.find(differences.iterator(), addUMLAnnotationDescription);
		final Diff addReferencesInUMLAnnotation = Iterators.find(differences.iterator(),
				addReferencesInUMLAnnotationDescription);
		final Diff addStereotypeApplication = Iterators.find(differences.iterator(),
				addStereotypeApplicationDescription);
		final Diff addModel = Iterators.find(differences.iterator(), addModelDescription);
		final Diff addClass = Iterators.find(differences.iterator(), addClassDescription);
		final Diff addRefBaseClass = Iterators.find(differences.iterator(), addRefBaseClassDescription);

		assertNotNull(addProfileApplication);
		assertNotNull(addAppliedProfileInProfileApplication);
		assertNotNull(addUMLAnnotation);
		assertNotNull(addReferencesInUMLAnnotation);
		assertNotNull(addStereotypeApplication);
		assertNotNull(addModel);
		assertNotNull(addClass);
		assertNotNull(addRefBaseClass);

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
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLStereotypeApplication.getRefinedBy().size()));
			assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addRefBaseClass));
		} else {
			addUMLProfileApplication = Iterators.find(differences.iterator(), and(
					instanceOf(ProfileApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLProfileApplication.getRefinedBy().size()));
			assertTrue(addUMLProfileApplication.getRefinedBy().contains(addProfileApplication));
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLProfileApplication);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLStereotypeApplication.getRefinedBy().size()));
			assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addStereotypeApplication));
			assertTrue(addUMLStereotypeApplication.getRefinedBy().contains(addRefBaseClass));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(1), Integer.valueOf(addProfileApplication.getRequires().size()));
			assertTrue(addProfileApplication.getRequires().contains(addModel));

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

	private void testAB4(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addStereotypeApplicationDescription = null;
		Predicate<? super Diff> addClassDescription = null;

		Diff addUMLStereotypeApplication = null;
		if (kind.equals(TestKind.DELETE)) {
			addClassDescription = removed("model.Class0");
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.DELETE)));
		} else {
			addClassDescription = added("model.Class0");
			addUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
					instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.ADD)));
		}
		addStereotypeApplicationDescription = instanceOf(ResourceAttachmentChange.class);

		final Diff addClass = Iterators.find(differences.iterator(), addClassDescription);
		final Diff addStereotypeApplication = Iterators.find(differences.iterator(),
				addStereotypeApplicationDescription);
		assertNotNull(addClass);
		assertNotNull(addStereotypeApplication);
		assertNotNull(addUMLStereotypeApplication);

	}

	private void testAB5(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeAttributeDescription1 = null;
		Predicate<? super Diff> changeAttributeDescription2 = null;

		changeAttributeDescription1 = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("xmlName"), valueIs("clazz"));
		changeAttributeDescription2 = and(instanceOf(AttributeChange.class), ofKind(DifferenceKind.CHANGE),
				onFeature("className"), valueIs("theEClassName"));

		final Diff changeAttribute1 = Iterators.find(differences.iterator(), changeAttributeDescription1);
		final Diff changeAttribute2 = Iterators.find(differences.iterator(), changeAttributeDescription2);
		final Diff changeUMLStereotypeApplication = Iterators.find(differences.iterator(), and(
				instanceOf(StereotypeApplicationChange.class), ofKind(DifferenceKind.CHANGE)));
		assertNotNull(changeAttribute1);
		assertNotNull(changeAttribute2);
		assertNotNull(changeUMLStereotypeApplication);

		assertSame(Integer.valueOf(2), Integer.valueOf(changeUMLStereotypeApplication.getRefinedBy().size()));
		assertTrue(changeUMLStereotypeApplication.getRefinedBy().contains(changeAttribute1));
		assertTrue(changeUMLStereotypeApplication.getRefinedBy().contains(changeAttribute2));

	}

	private void testAB6(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addStereotypeRefDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD),
					onFeature("manyValuedReference"));
		} else {
			addStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.DELETE), onFeature("manyValuedReference"));
		}
		changeUMLStereotypeDescription = and(instanceOf(StereotypeApplicationChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff addStereotypeRef = Iterators.find(differences.iterator(), addStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(addStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertSame(Integer.valueOf(1), Integer.valueOf(changeUMLStereotype.getRefinedBy().size()));
		assertTrue(changeUMLStereotype.getRefinedBy().contains(addStereotypeRef));

	}

	private void testAB7(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> moveStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		moveStereotypeRefDescription = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				onFeature("manyValuedReference"));
		changeUMLStereotypeDescription = and(instanceOf(StereotypeApplicationChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff moveStereotypeRef = Iterators.find(differences.iterator(), moveStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(moveStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertSame(Integer.valueOf(1), Integer.valueOf(changeUMLStereotype.getRefinedBy().size()));
		assertTrue(changeUMLStereotype.getRefinedBy().contains(moveStereotypeRef));

	}

	private void testAB8(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		if (kind.equals(TestKind.ADD)) {
			changeStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedReference"),
					valueNameMatches("class0"));
		} else {
			changeStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedReference"),
					valueNameMatches("class1"));
		}
		changeUMLStereotypeDescription = and(instanceOf(StereotypeApplicationChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff changeStereotypeRef = Iterators.find(differences.iterator(),
				changeStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(changeStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertSame(Integer.valueOf(1), Integer.valueOf(changeUMLStereotype.getRefinedBy().size()));
		assertTrue(changeUMLStereotype.getRefinedBy().contains(changeStereotypeRef));

	}

	private void testAB9(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		changeStereotypeRefDescription = and(instanceOf(ReferenceChange.class),
				ofKind(DifferenceKind.CHANGE), onFeature("singleValuedReference"), valueNameMatches("class0"));

		changeUMLStereotypeDescription = and(instanceOf(StereotypeApplicationChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff changeStereotypeRef = Iterators.find(differences.iterator(),
				changeStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(changeStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertSame(Integer.valueOf(1), Integer.valueOf(changeUMLStereotype.getRefinedBy().size()));
		assertTrue(changeUMLStereotype.getRefinedBy().contains(changeStereotypeRef));

	}

	private void testAB10(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeStereotypeRefDescription = null;
		Predicate<? super Diff> changeUMLStereotypeDescription = null;

		if (kind.equals(TestKind.ADD)) {
			changeStereotypeRefDescription = and(instanceOf(AttributeChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedAttribute"), valueIs("myValue"));
		} else {
			changeStereotypeRefDescription = and(instanceOf(AttributeChange.class),
					ofKind(DifferenceKind.CHANGE), onFeature("singleValuedAttribute"), valueIs("oldValue"));
		}

		changeUMLStereotypeDescription = and(instanceOf(StereotypeApplicationChange.class),
				ofKind(DifferenceKind.CHANGE));

		final Diff changeStereotypeRef = Iterators.find(differences.iterator(),
				changeStereotypeRefDescription);
		final Diff changeUMLStereotype = Iterators.find(differences.iterator(),
				changeUMLStereotypeDescription);

		assertNotNull(changeStereotypeRef);
		assertNotNull(changeUMLStereotype);

		assertSame(Integer.valueOf(1), Integer.valueOf(changeUMLStereotype.getRefinedBy().size()));
		assertTrue(changeUMLStereotype.getRefinedBy().contains(changeStereotypeRef));

	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}

}
