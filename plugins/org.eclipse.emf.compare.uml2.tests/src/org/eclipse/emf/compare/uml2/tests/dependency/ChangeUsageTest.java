package org.eclipse.emf.compare.uml2.tests.dependency;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
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

//TODO To extend from ChangeDependencyTest and change only descriptions.
@SuppressWarnings("nls")
public class ChangeUsageTest extends AbstractTest {

	private DependencyInputData input = new DependencyInputData();

	@Test
	public void testA40UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA41UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA40UseCase3way() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA41UseCase3way() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addSupplierInDependencyDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addSupplierInDependencyDescription = removedFromReference("model.Usage0", "supplier",
					"model.Class1");
		} else {
			addSupplierInDependencyDescription = addedToReference("model.Usage0", "supplier", "model.Class1");
		}

		final Diff addSupplierInDependency = Iterators.find(differences.iterator(),
				addSupplierInDependencyDescription);

		assertNotNull(addSupplierInDependency);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(DependencyChange.class)));
		Diff changeUMLDependency = Iterators.find(differences.iterator(), and(
				instanceOf(DependencyChange.class), ofKind(DifferenceKind.CHANGE)));
		assertNotNull(changeUMLDependency);
		assertSame(Integer.valueOf(1), Integer.valueOf(changeUMLDependency.getRefinedBy().size()));
		assertTrue(changeUMLDependency.getRefinedBy().contains(addSupplierInDependency));

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(0), Integer.valueOf(addSupplierInDependency.getRequires().size()));

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getEquivalences().size()));

	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}

}
