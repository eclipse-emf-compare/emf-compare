package org.eclipse.emf.compare.uml2.tests.dependency;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
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
import org.eclipse.emf.compare.uml2.SubstitutionChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.dependency.data.DependencyInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

//TODO To extend from AddInterfaceRealizationTest and change only descriptions.

@SuppressWarnings("nls")
public class AddSubstitutionTest extends AbstractTest {

	private DependencyInputData input = new DependencyInputData();

	@Test
	public void testA70UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA71UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA70UseCase3way() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA71UseCase3way() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addInterfaceRealizationDescription = null;
		Predicate<? super Diff> addClientInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addSupplierInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addContractInInterfaceRealizationDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			//addInterfaceRealizationDescription = removed("model.Class0.Substitution0"); //$NON-NLS-1$
			addInterfaceRealizationDescription = removedFromReference("model.Class0", "substitution",
					"model.Class0.Substitution0");
			addClientInInterfaceRealizationDescription = removedFromReference("model.Class0.Substitution0",
					"client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = removedFromReference("model.Class0.Substitution0",
					"supplier", "model.Class1");
			addContractInInterfaceRealizationDescription = changedReference("model.Class0.Substitution0",
					"contract", "model.Class1", null);
		} else {
			//addInterfaceRealizationDescription = added("model.Class0.Substitution0"); //$NON-NLS-1$
			addInterfaceRealizationDescription = addedToReference(
					"model.Class0", "substitution", "model.Class0.Substitution0"); //$NON-NLS-1$
			addClientInInterfaceRealizationDescription = addedToReference("model.Class0.Substitution0",
					"client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = addedToReference("model.Class0.Substitution0",
					"supplier", "model.Class1");
			addContractInInterfaceRealizationDescription = changedReference("model.Class0.Substitution0",
					"contract", null, "model.Class1");
		}

		final Diff addInterfaceRealization = Iterators.find(differences.iterator(),
				addInterfaceRealizationDescription);
		final Diff addClientInInterfaceRealization = Iterators.find(differences.iterator(),
				addClientInInterfaceRealizationDescription);
		final Diff addSupplierInInterfaceRealization = Iterators.find(differences.iterator(),
				addSupplierInInterfaceRealizationDescription);
		final Diff addContractInInterfaceRealization = Iterators.find(differences.iterator(),
				addContractInInterfaceRealizationDescription);

		assertNotNull(addInterfaceRealization);
		assertNotNull(addClientInInterfaceRealization);
		assertNotNull(addSupplierInInterfaceRealization);
		assertNotNull(addContractInInterfaceRealization);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(SubstitutionChange.class)));
		Diff addUMLDependency = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(SubstitutionChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLDependency);
			assertSame(Integer.valueOf(3), Integer.valueOf(addUMLDependency.getRefinedBy().size()));
			assertTrue(addUMLDependency.getRefinedBy().contains(addClientInInterfaceRealization));
			assertTrue(addUMLDependency.getRefinedBy().contains(addSupplierInInterfaceRealization));
			assertTrue(addUMLDependency.getRefinedBy().contains(addContractInInterfaceRealization));
		} else {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(SubstitutionChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLDependency);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLDependency.getRefinedBy().size()));
			assertTrue(addUMLDependency.getRefinedBy().contains(addInterfaceRealization));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(addClientInInterfaceRealization.getRequires()
					.size()));
			assertTrue(addClientInInterfaceRealization.getRequires().contains(addInterfaceRealization));
			assertSame(Integer.valueOf(1), Integer.valueOf(addSupplierInInterfaceRealization.getRequires()
					.size()));
			assertTrue(addSupplierInInterfaceRealization.getRequires().contains(addInterfaceRealization));
			assertSame(Integer.valueOf(1), Integer.valueOf(addContractInInterfaceRealization.getRequires()
					.size()));
			assertTrue(addContractInInterfaceRealization.getRequires().contains(addInterfaceRealization));

			assertSame(Integer.valueOf(0), Integer.valueOf(addInterfaceRealization.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLDependency.getRequires().size()));
		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(addClientInInterfaceRealization.getRequires()
					.size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addSupplierInInterfaceRealization.getRequires()
					.size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addContractInInterfaceRealization.getRequires()
					.size()));

			assertSame(Integer.valueOf(3), Integer.valueOf(addInterfaceRealization.getRequires().size()));
			assertTrue(addInterfaceRealization.getRequires().contains(addClientInInterfaceRealization));
			assertTrue(addInterfaceRealization.getRequires().contains(addSupplierInInterfaceRealization));
			assertTrue(addInterfaceRealization.getRequires().contains(addContractInInterfaceRealization));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLDependency.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addClientInInterfaceRealization.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addClientInInterfaceRealization.getEquivalence()
				.getDifferences().size()));
		assertTrue(addClientInInterfaceRealization.getEquivalence().getDifferences().contains(
				addInterfaceRealization));
	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}
}
