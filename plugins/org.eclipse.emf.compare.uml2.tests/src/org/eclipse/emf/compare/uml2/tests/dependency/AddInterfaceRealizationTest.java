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
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.internal.InterfaceRealizationChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.dependency.data.DependencyInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddInterfaceRealizationTest extends AbstractTest {

	private DependencyInputData input = new DependencyInputData();

	@Test
	public void testA50UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA51UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final Comparison comparison = compare(right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA50UseCase3way() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA51UseCase3way() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addInterfaceRealizationDescription = null;
		Predicate<? super Diff> addClientInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addSupplierInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addContractInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addClientDependencyInClass0Description = null;

		if (kind.equals(TestKind.DELETE)) {
			//addInterfaceRealizationDescription = removed("model.Class0.InterfaceRealization0"); //$NON-NLS-1$
			addInterfaceRealizationDescription = removedFromReference("model.Class0", "interfaceRealization",
					"model.Class0.InterfaceRealization0");
			addClientInInterfaceRealizationDescription = removedFromReference(
					"model.Class0.InterfaceRealization0", "client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = removedFromReference(
					"model.Class0.InterfaceRealization0", "supplier", "model.Interface0");
			addContractInInterfaceRealizationDescription = changedReference(
					"model.Class0.InterfaceRealization0", "contract", "model.Interface0", null);
			addClientDependencyInClass0Description = removedFromReference("model.Class0", "clientDependency",
					"model.Class0.InterfaceRealization0");
		} else {
			//addInterfaceRealizationDescription = added("model.Class0.InterfaceRealization0"); //$NON-NLS-1$
			addInterfaceRealizationDescription = addedToReference(
					"model.Class0", "interfaceRealization", "model.Class0.InterfaceRealization0"); //$NON-NLS-1$
			addClientInInterfaceRealizationDescription = addedToReference(
					"model.Class0.InterfaceRealization0", "client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = addedToReference(
					"model.Class0.InterfaceRealization0", "supplier", "model.Interface0");
			addContractInInterfaceRealizationDescription = changedReference(
					"model.Class0.InterfaceRealization0", "contract", null, "model.Interface0");
			addClientDependencyInClass0Description = addedToReference("model.Class0", "clientDependency",
					"model.Class0.InterfaceRealization0");
		}

		final Diff addInterfaceRealization = Iterators.find(differences.iterator(),
				addInterfaceRealizationDescription);
		final Diff addClientInInterfaceRealization = Iterators.find(differences.iterator(),
				addClientInInterfaceRealizationDescription);
		final Diff addSupplierInInterfaceRealization = Iterators.find(differences.iterator(),
				addSupplierInInterfaceRealizationDescription);
		final Diff addContractInInterfaceRealization = Iterators.find(differences.iterator(),
				addContractInInterfaceRealizationDescription);
		final Diff addClientDependencyInClass0 = Iterators.find(differences.iterator(),
				addClientDependencyInClass0Description);

		assertNotNull(addInterfaceRealization);
		assertNotNull(addClientInInterfaceRealization);
		assertNotNull(addSupplierInInterfaceRealization);
		assertNotNull(addContractInInterfaceRealization);
		assertNotNull(addClientDependencyInClass0);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(InterfaceRealizationChange.class)));
		Diff addUMLDependency = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(InterfaceRealizationChange.class), ofKind(DifferenceKind.ADD)));
		} else {
			addUMLDependency = Iterators.find(differences.iterator(), and(
					instanceOf(InterfaceRealizationChange.class), ofKind(DifferenceKind.DELETE)));
		}
		assertNotNull(addUMLDependency);
		assertSame(Integer.valueOf(5), Integer.valueOf(addUMLDependency.getRefinedBy().size()));
		assertTrue(addUMLDependency.getRefinedBy().contains(addInterfaceRealization));
		assertTrue(addUMLDependency.getRefinedBy().contains(addClientInInterfaceRealization));
		assertTrue(addUMLDependency.getRefinedBy().contains(addSupplierInInterfaceRealization));
		assertTrue(addUMLDependency.getRefinedBy().contains(addContractInInterfaceRealization));
		assertTrue(addUMLDependency.getRefinedBy().contains(addClientDependencyInClass0));

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

			assertSame(Integer.valueOf(4), Integer.valueOf(addInterfaceRealization.getRequires().size()));
			assertTrue(addInterfaceRealization.getRequires().contains(addClientInInterfaceRealization));
			assertTrue(addInterfaceRealization.getRequires().contains(addSupplierInInterfaceRealization));
			assertTrue(addInterfaceRealization.getRequires().contains(addContractInInterfaceRealization));
			assertTrue(addInterfaceRealization.getRequires().contains(addClientDependencyInClass0));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLDependency.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addClientInInterfaceRealization.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addClientInInterfaceRealization.getEquivalence()
				.getDifferences().size()));
		assertTrue(addClientInInterfaceRealization.getEquivalence().getDifferences().contains(
				addClientInInterfaceRealization));
		assertTrue(addClientInInterfaceRealization.getEquivalence().getDifferences().contains(
				addClientDependencyInClass0));

		testIntersections(comparison);
	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}
}
