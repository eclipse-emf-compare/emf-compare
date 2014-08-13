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
import static org.junit.Assert.assertSame;
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
import org.junit.Test;

@SuppressWarnings("nls")
public class AddAssociation2Test extends AbstractUMLTest {

	private AssociationInputData input = new AssociationInputData();

	@Test
	public void testA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testMergeLtRA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testMergeLeftToRight(left, right, null);
	}

	@Test
	public void testA31UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLA31UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testMergeLtRA31UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testMergeLeftToRight(right, left, null);
	}

	@Test
	public void testA30UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testMergeRtLA30UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testMergeLtRA30UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();
		testMergeLeftToRight(left, right, right);
	}

	@Test
	public void testA31UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testMergeRtLA31UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testMergeRightToLeft(left, right, left);
	}

	@Test
	public void testMergeLtRA31UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testMergeLeftToRight(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		assertEquals(14, differences.size());

		Predicate<? super Diff> addAssociationDescription = null;
		Predicate<? super Diff> addPropertyClass1Description = null;
		Predicate<? super Diff> addPropertyClass0Description = null;
		Predicate<? super Diff> addPropertyClass0InAssociationDescription = null;
		Predicate<? super Diff> addPropertyClass1InAssociationDescription = null;
		Predicate<? super Diff> addRefAssociationInPropertyClass1Description = null;
		Predicate<? super Diff> addRefTypeInPropertyClass1Description = null;
		Predicate<? super Diff> addRefAssociationInPropertyClass0Description = null;
		Predicate<? super Diff> addRefTypeInPropertyClass0Description = null;
		Predicate<? super Diff> addLiteralIntegerInClass1Description = null;
		Predicate<? super Diff> addUnlimitedNaturalInClass1Description = null;
		Predicate<? super Diff> addLiteralIntegerInClass0Description = null;
		Predicate<? super Diff> addUnlimitedNaturalInClass0Description = null;

		if (kind.equals(TestKind.DELETE)) {
			addAssociationDescription = removed("model.class1_class0_0"); //$NON-NLS-1$
			addPropertyClass1Description = removedFromReference("model.class1_class0_0", "ownedEnd",
					"model.class1_class0_0.class1");
			addPropertyClass0Description = removedFromReference("model.Class1", "ownedAttribute",
					"model.Class1.class0");
			addPropertyClass0InAssociationDescription = removedFromReference("model.class1_class0_0",
					"memberEnd", "model.Class1.class0");
			addPropertyClass1InAssociationDescription = removedFromReference("model.class1_class0_0",
					"memberEnd", "model.class1_class0_0.class1");
			addRefAssociationInPropertyClass1Description = changedReference("model.class1_class0_0.class1",
					"association", "model.class1_class0_0", null);
			addRefTypeInPropertyClass1Description = changedReference("model.class1_class0_0.class1", "type",
					"model.Class1", null);
			addRefAssociationInPropertyClass0Description = changedReference("model.Class1.class0",
					"association", "model.class1_class0_0", null);
			addRefTypeInPropertyClass0Description = changedReference("model.Class1.class0", "type",
					"model.Class0", null);
			addLiteralIntegerInClass1Description = removedLowerValueIn("model.class1_class0_0.class1");
			addUnlimitedNaturalInClass1Description = removedUpperValueIn("model.class1_class0_0.class1");
			addLiteralIntegerInClass0Description = removedLowerValueIn("model.Class1.class0");
			addUnlimitedNaturalInClass0Description = removedUpperValueIn("model.Class1.class0");
		} else {
			addAssociationDescription = added("model.class1_class0_0"); //$NON-NLS-1$
			addPropertyClass1Description = addedToReference("model.class1_class0_0", "ownedEnd",
					"model.class1_class0_0.class1");
			addPropertyClass0Description = addedToReference("model.Class1", "ownedAttribute",
					"model.Class1.class0");
			addPropertyClass0InAssociationDescription = addedToReference("model.class1_class0_0",
					"memberEnd", "model.Class1.class0");
			addPropertyClass1InAssociationDescription = addedToReference("model.class1_class0_0",
					"memberEnd", "model.class1_class0_0.class1");
			addRefAssociationInPropertyClass1Description = changedReference("model.class1_class0_0.class1",
					"association", null, "model.class1_class0_0");
			addRefTypeInPropertyClass1Description = changedReference("model.class1_class0_0.class1", "type",
					null, "model.Class1");
			addRefAssociationInPropertyClass0Description = changedReference("model.Class1.class0",
					"association", null, "model.class1_class0_0");
			addRefTypeInPropertyClass0Description = changedReference("model.Class1.class0", "type", null,
					"model.Class0");
			addLiteralIntegerInClass1Description = addedLowerValueIn("model.class1_class0_0.class1");
			addUnlimitedNaturalInClass1Description = addedUpperValueIn("model.class1_class0_0.class1");
			addLiteralIntegerInClass0Description = addedLowerValueIn("model.Class1.class0");
			addUnlimitedNaturalInClass0Description = addedUpperValueIn("model.Class1.class0");
		}

		final Diff addAssociation = Iterators.find(differences.iterator(), addAssociationDescription);
		final Diff addPropertyClass1 = Iterators.find(differences.iterator(), addPropertyClass1Description);
		final Diff addPropertyClass0 = Iterators.find(differences.iterator(), addPropertyClass0Description);
		final Diff addPropertyClass0InAssociation = Iterators.find(differences.iterator(),
				addPropertyClass0InAssociationDescription);
		final Diff addPropertyClass1InAssociation = Iterators.find(differences.iterator(),
				addPropertyClass1InAssociationDescription);
		final Diff addRefAssociationInPropertyClass1 = Iterators.find(differences.iterator(),
				addRefAssociationInPropertyClass1Description);
		final Diff addRefTypeInPropertyClass1 = Iterators.find(differences.iterator(),
				addRefTypeInPropertyClass1Description);
		final Diff addRefAssociationInPropertyClass0 = Iterators.find(differences.iterator(),
				addRefAssociationInPropertyClass0Description);
		final Diff addRefTypeInPropertyClass0 = Iterators.find(differences.iterator(),
				addRefTypeInPropertyClass0Description);
		final Diff addLiteralIntegerInClass1 = Iterators.find(differences.iterator(),
				addLiteralIntegerInClass1Description);
		final Diff addUnlimitedNaturalInClass1 = Iterators.find(differences.iterator(),
				addUnlimitedNaturalInClass1Description);
		final Diff addLiteralIntegerInClass0 = Iterators.find(differences.iterator(),
				addLiteralIntegerInClass0Description);
		final Diff addUnlimitedNaturalInClass0 = Iterators.find(differences.iterator(),
				addUnlimitedNaturalInClass0Description);

		assertNotNull(addAssociation);
		assertNotNull(addPropertyClass1);
		assertNotNull(addPropertyClass0);
		assertNotNull(addPropertyClass0InAssociation);
		assertNotNull(addPropertyClass1InAssociation);
		assertNotNull(addRefAssociationInPropertyClass1);
		assertNotNull(addRefTypeInPropertyClass1);
		assertNotNull(addRefAssociationInPropertyClass0);
		assertNotNull(addRefTypeInPropertyClass0);
		assertNotNull(addLiteralIntegerInClass1);
		assertNotNull(addUnlimitedNaturalInClass1);
		assertNotNull(addLiteralIntegerInClass0);
		assertNotNull(addUnlimitedNaturalInClass0);

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
		assertEquals(13, addUMLAssociation.getRefinedBy().size());

		assertTrue(addUMLAssociation.getRefinedBy().contains(addAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addPropertyClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addPropertyClass0));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addPropertyClass0InAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addPropertyClass1InAssociation));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefAssociationInPropertyClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefTypeInPropertyClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefAssociationInPropertyClass0));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addRefTypeInPropertyClass0));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addLiteralIntegerInClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addUnlimitedNaturalInClass1));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addLiteralIntegerInClass0));
		assertTrue(addUMLAssociation.getRefinedBy().contains(addUnlimitedNaturalInClass0));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addPropertyClass1.getRequires().size());
			assertTrue(addPropertyClass1.getRequires().contains(addAssociation));

			assertEquals(0, addPropertyClass0.getRequires().size());

			assertSame(Integer.valueOf(2), Integer.valueOf(addPropertyClass0InAssociation.getRequires()
					.size()));
			assertTrue(addPropertyClass0InAssociation.getRequires().contains(addPropertyClass0));
			assertTrue(addPropertyClass0InAssociation.getRequires().contains(addAssociation));

			assertSame(Integer.valueOf(2), Integer.valueOf(addRefAssociationInPropertyClass1.getRequires()
					.size()));
			assertTrue(addRefAssociationInPropertyClass1.getRequires().contains(addPropertyClass1));
			assertTrue(addRefAssociationInPropertyClass1.getRequires().contains(addAssociation));

			assertEquals(1, addRefTypeInPropertyClass1.getRequires().size());
			assertTrue(addRefTypeInPropertyClass1.getRequires().contains(addPropertyClass1));

			assertSame(Integer.valueOf(2), Integer.valueOf(addRefAssociationInPropertyClass0.getRequires()
					.size()));
			assertTrue(addRefAssociationInPropertyClass0.getRequires().contains(addPropertyClass0));
			assertTrue(addRefAssociationInPropertyClass0.getRequires().contains(addAssociation));

			assertEquals(1, addRefTypeInPropertyClass0.getRequires().size());
			assertTrue(addRefTypeInPropertyClass0.getRequires().contains(addPropertyClass0));

			assertEquals(0, addAssociation.getRequires().size());
			assertEquals(0, addUMLAssociation.getRequires().size());

			assertEquals(1, addLiteralIntegerInClass1.getRequires().size());
			assertTrue(addLiteralIntegerInClass1.getRequires().contains(addPropertyClass1));

			assertEquals(1, addUnlimitedNaturalInClass1.getRequires().size());
			assertTrue(addUnlimitedNaturalInClass1.getRequires().contains(addPropertyClass1));

			assertEquals(1, addLiteralIntegerInClass0.getRequires().size());
			assertTrue(addLiteralIntegerInClass0.getRequires().contains(addPropertyClass0));

			assertEquals(1, addUnlimitedNaturalInClass0.getRequires().size());
			assertTrue(addUnlimitedNaturalInClass0.getRequires().contains(addPropertyClass0));
		} else {
			assertEquals(5, addPropertyClass1.getRequires().size());
			assertTrue(addPropertyClass1.getRequires().contains(addLiteralIntegerInClass1));
			assertTrue(addPropertyClass1.getRequires().contains(addUnlimitedNaturalInClass1));
			assertTrue(addPropertyClass1.getRequires().contains(addRefAssociationInPropertyClass1));
			assertTrue(addPropertyClass1.getRequires().contains(addRefTypeInPropertyClass1));
			assertTrue(addPropertyClass1.getRequires().contains(addPropertyClass1InAssociation));

			assertEquals(5, addPropertyClass0.getRequires().size());
			assertTrue(addPropertyClass0.getRequires().contains(addLiteralIntegerInClass0));
			assertTrue(addPropertyClass0.getRequires().contains(addUnlimitedNaturalInClass0));
			assertTrue(addPropertyClass0.getRequires().contains(addRefAssociationInPropertyClass0));
			assertTrue(addPropertyClass0.getRequires().contains(addRefTypeInPropertyClass0));
			assertTrue(addPropertyClass0.getRequires().contains(addPropertyClass0InAssociation));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefAssociationInPropertyClass1.getRequires()
					.size()));
			assertEquals(0, addRefTypeInPropertyClass1.getRequires().size());
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefAssociationInPropertyClass0.getRequires()
					.size()));
			assertEquals(0, addRefTypeInPropertyClass0.getRequires().size());

			assertEquals(5, addAssociation.getRequires().size());
			assertTrue(addAssociation.getRequires().contains(addPropertyClass1));
			assertTrue(addAssociation.getRequires().contains(addRefAssociationInPropertyClass1));
			assertTrue(addAssociation.getRequires().contains(addRefAssociationInPropertyClass0));
			assertTrue(addAssociation.getRequires().contains(addPropertyClass0InAssociation));
			assertTrue(addAssociation.getRequires().contains(addPropertyClass1InAssociation));

			assertEquals(0, addUMLAssociation.getRequires().size());

			assertEquals(0, addLiteralIntegerInClass1.getRequires().size());
			assertEquals(0, addUnlimitedNaturalInClass1.getRequires().size());
			assertEquals(0, addLiteralIntegerInClass0.getRequires().size());
			assertEquals(0, addUnlimitedNaturalInClass0.getRequires().size());
		}

		assertNotNull(addPropertyClass0InAssociation.getEquivalence());
		assertTrue(addPropertyClass0InAssociation.getEquivalence().getDifferences().contains(
				addRefAssociationInPropertyClass0));

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
