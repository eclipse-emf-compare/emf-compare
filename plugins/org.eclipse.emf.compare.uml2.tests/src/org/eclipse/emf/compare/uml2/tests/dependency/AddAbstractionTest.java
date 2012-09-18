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
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.DependencyChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.dependency.data.DependencyInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

// TODO To extend from AddDependencyTest and change only descriptions.
@SuppressWarnings("nls")
public class AddAbstractionTest extends AbstractTest {

	private DependencyInputData input = new DependencyInputData();

	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.DELETE, comparison);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addAbstractionDescription = null;
		Predicate<? super Diff> addRefAbstractionInClass1Description = null;
		Predicate<? super Diff> addRefClass1InAbstractionDescription = null;
		Predicate<? super Diff> addRefClass0InAbstractionDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addAbstractionDescription = removed("model.Abstraction0"); //$NON-NLS-1$
			addRefAbstractionInClass1Description = removedFromReference("model.Class1", "clientDependency",
					"model.Abstraction0");
			addRefClass1InAbstractionDescription = removedFromReference("model.Abstraction0", "client",
					"model.Class1");
			addRefClass0InAbstractionDescription = removedFromReference("model.Abstraction0", "supplier",
					"model.Class0");
		} else {
			addAbstractionDescription = added("model.Abstraction0"); //$NON-NLS-1$
			addRefAbstractionInClass1Description = addedToReference("model.Class1", "clientDependency",
					"model.Abstraction0");
			addRefClass1InAbstractionDescription = addedToReference("model.Abstraction0", "client",
					"model.Class1");
			addRefClass0InAbstractionDescription = addedToReference("model.Abstraction0", "supplier",
					"model.Class0");
		}

		final Diff addAbstraction = Iterators.find(differences.iterator(), addAbstractionDescription);
		final Diff addRefAbstractionInClass1 = Iterators.find(differences.iterator(),
				addRefAbstractionInClass1Description);
		final Diff addRefClass1InAbstraction = Iterators.find(differences.iterator(),
				addRefClass1InAbstractionDescription);
		final Diff addRefClass0InAbstraction = Iterators.find(differences.iterator(),
				addRefClass0InAbstractionDescription);

		assertNotNull(addAbstraction);
		assertNotNull(addRefAbstractionInClass1);
		assertNotNull(addRefClass1InAbstraction);
		assertNotNull(addRefClass0InAbstraction);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(DependencyChange.class)));
		Diff addUMLDependency = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLDependency = Iterators.find(differences.iterator(), and(instanceOf(DependencyChange.class),
					ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLDependency);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLDependency.getRefinedBy().size()));
			assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass1InAbstraction));
			assertTrue(addUMLDependency.getRefinedBy().contains(addRefClass0InAbstraction));
		} else {
			addUMLDependency = Iterators.find(differences.iterator(), and(instanceOf(DependencyChange.class),
					ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLDependency);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLDependency.getRefinedBy().size()));
			assertTrue(addUMLDependency.getRefinedBy().contains(addAbstraction));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(addRefClass1InAbstraction.getRequires().size()));
			assertTrue(addRefClass1InAbstraction.getRequires().contains(addAbstraction));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefClass0InAbstraction.getRequires().size()));
			assertTrue(addRefClass0InAbstraction.getRequires().contains(addAbstraction));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefAbstractionInClass1.getRequires().size()));
			assertTrue(addRefAbstractionInClass1.getRequires().contains(addAbstraction));

			assertSame(Integer.valueOf(0), Integer.valueOf(addAbstraction.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLDependency.getRequires().size()));
		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefClass1InAbstraction.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefClass0InAbstraction.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefAbstractionInClass1.getRequires().size()));

			assertSame(Integer.valueOf(3), Integer.valueOf(addAbstraction.getRequires().size()));
			assertTrue(addAbstraction.getRequires().contains(addRefClass1InAbstraction));
			assertTrue(addAbstraction.getRequires().contains(addRefClass0InAbstraction));
			assertTrue(addAbstraction.getRequires().contains(addRefAbstractionInClass1));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLDependency.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addRefClass1InAbstraction.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addRefClass1InAbstraction.getEquivalence()
				.getDifferences().size()));
		assertTrue(addRefClass1InAbstraction.getEquivalence().getDifferences().contains(
				addRefAbstractionInClass1));

	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}

}
