package org.eclipse.emf.compare.uml2.tests.dependency;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.dependency.data.DependencyInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddDependencyTest extends AbstractUMLTest {

	protected DependencyInputData input = new DependencyInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	protected void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		// Was 5 with UML 4.0 but NamedElement::clientDependency has been made derived in UML 5.0
		assertEquals(4, differences.size());

		Predicate<? super Diff> addDependencyDescription = null;
		Predicate<? super Diff> addRefClass1InDependencyDescription = null;
		Predicate<? super Diff> addRefClass0InDependencyDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addDependencyDescription = removed("model.Dependency0"); //$NON-NLS-1$
			addRefClass0InDependencyDescription = removedFromReference("model.Dependency0", "client",
					"model.Class0");
			addRefClass1InDependencyDescription = removedFromReference("model.Dependency0", "supplier",
					"model.Class1");
		} else {
			addDependencyDescription = added("model.Dependency0"); //$NON-NLS-1$
			addRefClass0InDependencyDescription = addedToReference("model.Dependency0", "client",
					"model.Class0");
			addRefClass1InDependencyDescription = addedToReference("model.Dependency0", "supplier",
					"model.Class1");
		}

		final Diff addDependency = Iterators.find(differences.iterator(), addDependencyDescription);
		final Diff addRefClass0InDependency = Iterators.find(differences.iterator(),
				addRefClass0InDependencyDescription);
		final Diff addRefClass1InDependency = Iterators.find(differences.iterator(),
				addRefClass1InDependencyDescription);

		assertNotNull(addDependency);
		assertNotNull(addRefClass0InDependency);
		assertNotNull(addRefClass1InDependency);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(DirectedRelationshipChange.class)));
		Diff addUMLDependency = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(DirectedRelationshipChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(DirectedRelationshipChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLDependency);
		assertEquals(3, addUMLDependency.getRefinedBy().size());
		assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass0InDependency));
		assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass1InDependency));
		assertTrue(addUMLDependency.getRefinedBy().contains(addDependency));

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertEquals(1, addRefClass0InDependency.getRequires().size());
			assertTrue(addRefClass0InDependency.getRequires().contains(addDependency));

			assertEquals(1, addRefClass1InDependency.getRequires().size());
			assertTrue(addRefClass1InDependency.getRequires().contains(addDependency));

			assertEquals(0, addDependency.getRequires().size());
			assertEquals(0, addUMLDependency.getRequires().size());
		} else {
			assertEquals(0, addRefClass0InDependency.getRequires().size());

			assertEquals(0, addRefClass1InDependency.getRequires().size());

			// Was 3 with UML 4.0 but NamedElement::clientDependency has been made derived in UML 5.0
			assertEquals(2, addDependency.getRequires().size());
			assertTrue(addDependency.getRequires().contains(addRefClass0InDependency));
			assertTrue(addDependency.getRequires().contains(addRefClass1InDependency));

			assertEquals(0, addUMLDependency.getRequires().size());
		}

		// CHECK EQUIVALENCE
		// Was 1 with UML 4.0 but NamedElement::clientDependency has been made derived in UML 5.0
		assertEquals(0, comparison.getEquivalences().size());

		// Was not null with UML 4.0 but NamedElement::clientDependency has been made derived in UML 5.0
		assertNull(addRefClass0InDependency.getEquivalence());
		// assertSame(Integer.valueOf(2), Integer.valueOf(addRefClass0InDependency.getEquivalence()
		// .getDifferences().size()));

		testIntersections(comparison);

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
