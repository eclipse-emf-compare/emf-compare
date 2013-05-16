package org.eclipse.emf.compare.uml2.tests.association;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.association.data.AssociationInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddAssociationTest extends AbstractTest {

	private AssociationInputData input = new AssociationInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	@Ignore
	// See UMLPostProcessor#removeDuplicateDiffs(Comparison)
	public void testMergeLtRA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	@Ignore
	// See UMLPostProcessor#removeDuplicateDiffs(Comparison)
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

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	@Ignore
	// See UMLPostProcessor#removeDuplicateDiffs(Comparison)
	public void testMergeLtRA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeRightToLeft(left, right, left);
	}

	@Test
	@Ignore
	// See UMLPostProcessor#removeDuplicateDiffs(Comparison)
	public void testMergeLtRA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testMergeLeftToRight(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 16 differences
		assertSame(Integer.valueOf(12), Integer.valueOf(differences.size()));

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
		// duplicate diffs: Add differences on (opposite and subset features):
		// - Association.memberEnd to class1s
		// - Association.memberEnd to class2s
		// - Association.ownedEnd to class1s
		// - Association.ownedEnd to class2s

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

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(AssociationChange.class)));
		Diff addUMLAssociation = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLAssociation = Iterators.find(differences.iterator(), and(
					instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLAssociation = Iterators.find(differences.iterator(), and(
					instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLAssociation);
		assertSame(Integer.valueOf(11), Integer.valueOf(addUMLAssociation.getRefinedBy().size()));
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

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(2), Integer.valueOf(addRefAssociationInPropertyClass1.getRequires()
					.size()));
			assertTrue(addRefAssociationInPropertyClass1.getRequires().contains(
					addNavigableOwnedEndClass1InAssociation));
			// duplicate diff: requires Association.ownedEnd to class1s
			assertTrue(addRefAssociationInPropertyClass1.getRequires().contains(addAssociation));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefTypeInPropertyClass1.getRequires().size()));
			assertTrue(addRefTypeInPropertyClass1.getRequires().contains(
					addNavigableOwnedEndClass1InAssociation));
			// duplicate diff: requires Association.ownedEnd to class1s

			assertSame(Integer.valueOf(2), Integer.valueOf(addRefAssociationInPropertyClass2.getRequires()
					.size()));
			assertTrue(addRefAssociationInPropertyClass2.getRequires().contains(
					addNavigableOwnedEndClass2InAssociation));
			// duplicate diff: requires Association.ownedEnd to class2s
			assertTrue(addRefAssociationInPropertyClass2.getRequires().contains(addAssociation));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefTypeInPropertyClass2.getRequires().size()));
			assertTrue(addRefTypeInPropertyClass2.getRequires().contains(
					addNavigableOwnedEndClass2InAssociation));
			// duplicate diff: requires Association.ownedEnd to class2s

			assertSame(Integer.valueOf(0), Integer.valueOf(addAssociation.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLAssociation.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addNavigableOwnedEndClass1InAssociation
					.getRequires().size()));
			assertTrue(addNavigableOwnedEndClass1InAssociation.getRequires().contains(addAssociation));
			// duplicate diff: requires Association.ownedEnd to class1s

			assertSame(Integer.valueOf(1), Integer.valueOf(addNavigableOwnedEndClass2InAssociation
					.getRequires().size()));
			assertTrue(addNavigableOwnedEndClass2InAssociation.getRequires().contains(addAssociation));
			// duplicate diff: requires Association.ownedEnd to class2s

			assertSame(Integer.valueOf(1), Integer.valueOf(addLiteralIntegerInClass1.getRequires().size()));
			assertTrue(addLiteralIntegerInClass1.getRequires().contains(
					addNavigableOwnedEndClass1InAssociation));
			// duplicate diff: requires Association.ownedEnd to class1s

			assertSame(Integer.valueOf(1), Integer.valueOf(addUnlimitedNaturalInClass1.getRequires().size()));
			assertTrue(addUnlimitedNaturalInClass1.getRequires().contains(
					addNavigableOwnedEndClass1InAssociation));
			// duplicate diff: requires Association.ownedEnd to class1s

			assertSame(Integer.valueOf(1), Integer.valueOf(addLiteralIntegerInClass2.getRequires().size()));
			assertTrue(addLiteralIntegerInClass2.getRequires().contains(
					addNavigableOwnedEndClass2InAssociation));
			// duplicate diff: requires Association.ownedEnd to class2s

			assertSame(Integer.valueOf(1), Integer.valueOf(addUnlimitedNaturalInClass2.getRequires().size()));
			assertTrue(addUnlimitedNaturalInClass2.getRequires().contains(
					addNavigableOwnedEndClass2InAssociation));
			// duplicate diff: requires Association.ownedEnd to class2s
		} else {
			assertSame(Integer.valueOf(4), Integer.valueOf(addNavigableOwnedEndClass1InAssociation
					.getRequires().size()));
			// duplicate diff
			assertTrue(addNavigableOwnedEndClass1InAssociation.getRequires().contains(
					addLiteralIntegerInClass1));
			assertTrue(addNavigableOwnedEndClass1InAssociation.getRequires().contains(
					addUnlimitedNaturalInClass1));
			assertTrue(addNavigableOwnedEndClass1InAssociation.getRequires().contains(
					addRefAssociationInPropertyClass1));
			assertTrue(addNavigableOwnedEndClass1InAssociation.getRequires().contains(
					addRefTypeInPropertyClass1));

			assertSame(Integer.valueOf(4), Integer.valueOf(addNavigableOwnedEndClass2InAssociation
					.getRequires().size()));
			// duplicate diff
			assertTrue(addNavigableOwnedEndClass2InAssociation.getRequires().contains(
					addLiteralIntegerInClass2));
			assertTrue(addNavigableOwnedEndClass2InAssociation.getRequires().contains(
					addUnlimitedNaturalInClass2));
			assertTrue(addNavigableOwnedEndClass2InAssociation.getRequires().contains(
					addRefAssociationInPropertyClass2));
			assertTrue(addNavigableOwnedEndClass2InAssociation.getRequires().contains(
					addRefTypeInPropertyClass2));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefAssociationInPropertyClass1.getRequires()
					.size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefTypeInPropertyClass1.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefAssociationInPropertyClass2.getRequires()
					.size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefTypeInPropertyClass2.getRequires().size()));

			assertSame(Integer.valueOf(4), Integer.valueOf(addAssociation.getRequires().size()));
			assertTrue(addAssociation.getRequires().contains(addNavigableOwnedEndClass1InAssociation));
			assertTrue(addAssociation.getRequires().contains(addNavigableOwnedEndClass2InAssociation));
			assertTrue(addAssociation.getRequires().contains(addRefAssociationInPropertyClass1));
			assertTrue(addAssociation.getRequires().contains(addRefAssociationInPropertyClass2));
			// duplicate diff: requires:
			// - Association.memberEnd to class1s
			// - Association.memberEnd to class2s
			// - Association.ownedEnd to class1s
			// - Association.ownedEnd to class2s

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLAssociation.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addLiteralIntegerInClass1.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUnlimitedNaturalInClass1.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addLiteralIntegerInClass2.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUnlimitedNaturalInClass2.getRequires().size()));

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
	protected AbstractInputData getInput() {
		return input;
	}
}
