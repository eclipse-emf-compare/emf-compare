package org.eclipse.emf.compare.uml2.tests.dependency;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
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
import org.eclipse.emf.compare.uml2.DependencyChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.dependency.data.DependencyInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddDependencyTest extends AbstractTest {

	protected DependencyInputData input = new DependencyInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.DELETE, comparison);
	}

	protected void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addDependencyDescription = null;
		Predicate<? super Diff> addRefDependencyInClass0Description = null;
		Predicate<? super Diff> addRefClass1InDependencyDescription = null;
		Predicate<? super Diff> addRefClass0InDependencyDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addDependencyDescription = removed("model.Dependency0"); //$NON-NLS-1$
			addRefDependencyInClass0Description = removedFromReference("model.Class0", "clientDependency",
					"model.Dependency0");
			addRefClass0InDependencyDescription = removedFromReference("model.Dependency0", "client",
					"model.Class0");
			addRefClass1InDependencyDescription = removedFromReference("model.Dependency0", "supplier",
					"model.Class1");
		} else {
			addDependencyDescription = added("model.Dependency0"); //$NON-NLS-1$
			addRefDependencyInClass0Description = addedToReference("model.Class0", "clientDependency",
					"model.Dependency0");
			addRefClass0InDependencyDescription = addedToReference("model.Dependency0", "client",
					"model.Class0");
			addRefClass1InDependencyDescription = addedToReference("model.Dependency0", "supplier",
					"model.Class1");
		}

		final Diff addDependency = Iterators.find(differences.iterator(), addDependencyDescription);
		final Diff addRefDependencyInClass0 = Iterators.find(differences.iterator(),
				addRefDependencyInClass0Description);
		final Diff addRefClass0InDependency = Iterators.find(differences.iterator(),
				addRefClass0InDependencyDescription);
		final Diff addRefClass1InDependency = Iterators.find(differences.iterator(),
				addRefClass1InDependencyDescription);

		assertNotNull(addDependency);
		assertNotNull(addRefDependencyInClass0);
		assertNotNull(addRefClass0InDependency);
		assertNotNull(addRefClass1InDependency);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(DependencyChange.class)));
		Diff addUMLDependency = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLDependency = Iterators.find(differences.iterator(), and(instanceOf(DependencyChange.class),
					ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLDependency);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLDependency.getRefinedBy().size()));
			assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass0InDependency));
			assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass1InDependency));
		} else {
			addUMLDependency = Iterators.find(differences.iterator(), and(instanceOf(DependencyChange.class),
					ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLDependency);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLDependency.getRefinedBy().size()));
			assertTrue(addUMLDependency.getRefinedBy().contains(addDependency));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(addRefClass0InDependency.getRequires().size()));
			assertTrue(addRefClass0InDependency.getRequires().contains(addDependency));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefClass1InDependency.getRequires().size()));
			assertTrue(addRefClass1InDependency.getRequires().contains(addDependency));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefDependencyInClass0.getRequires().size()));
			assertTrue(addRefDependencyInClass0.getRequires().contains(addDependency));

			assertSame(Integer.valueOf(0), Integer.valueOf(addDependency.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLDependency.getRequires().size()));
		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefClass0InDependency.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefClass1InDependency.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefDependencyInClass0.getRequires().size()));

			assertSame(Integer.valueOf(3), Integer.valueOf(addDependency.getRequires().size()));
			assertTrue(addDependency.getRequires().contains(addRefClass0InDependency));
			assertTrue(addDependency.getRequires().contains(addRefClass1InDependency));
			assertTrue(addDependency.getRequires().contains(addRefDependencyInClass0));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLDependency.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addRefClass0InDependency.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addRefClass0InDependency.getEquivalence()
				.getDifferences().size()));
		assertTrue(addRefClass0InDependency.getEquivalence().getDifferences().contains(
				addRefDependencyInClass0));

	}

}
