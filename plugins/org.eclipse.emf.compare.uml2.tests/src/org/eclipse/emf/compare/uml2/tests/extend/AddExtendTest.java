package org.eclipse.emf.compare.uml2.tests.extend;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
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
import org.eclipse.emf.compare.uml2.ExtendChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.extend.data.ExtendInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddExtendTest extends AbstractTest {

	private ExtendInputData input = new ExtendInputData();

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

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testAB1(TestKind.DELETE, comparison);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addExtendDescription = null;
		Predicate<? super Diff> changeRefExtendedCaseInExtendDescription = null;
		Predicate<? super Diff> addRefExtensionLocationInExtendDescription = null;
		Predicate<? super Diff> addExtensionPointDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addExtendDescription = removed("model.UseCase2.useCase2Extension"); //$NON-NLS-1$
			changeRefExtendedCaseInExtendDescription = changedReference("model.UseCase2.useCase2Extension",
					"extendedCase", "model.UseCase1", null);
			addRefExtensionLocationInExtendDescription = removedFromReference(
					"model.UseCase2.useCase2Extension", "extensionLocation", "model.UseCase1.point");
			addExtensionPointDescription = removed("model.UseCase1.point");
		} else {
			addExtendDescription = added("model.UseCase2.useCase2Extension"); //$NON-NLS-1$
			changeRefExtendedCaseInExtendDescription = changedReference("model.UseCase2.useCase2Extension",
					"extendedCase", null, "model.UseCase1");
			addRefExtensionLocationInExtendDescription = addedToReference("model.UseCase2.useCase2Extension",
					"extensionLocation", "model.UseCase1.point");
			addExtensionPointDescription = added("model.UseCase1.point");
		}

		final Diff addExtend = Iterators.find(differences.iterator(), addExtendDescription);
		final Diff addRefExtendedCaseInExtend = Iterators.find(differences.iterator(),
				changeRefExtendedCaseInExtendDescription);
		final Diff addRefExtensionLocationInExtend = Iterators.find(differences.iterator(),
				addRefExtensionLocationInExtendDescription);
		final Diff addExtensionPoint = Iterators.find(differences.iterator(), addExtensionPointDescription);

		assertNotNull(addExtend);
		assertNotNull(addRefExtendedCaseInExtend);
		assertNotNull(addRefExtensionLocationInExtend);
		assertNotNull(addExtensionPoint);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(ExtendChange.class)));
		Diff addUMLExtend = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLExtend = Iterators.find(differences.iterator(), and(instanceOf(ExtendChange.class),
					ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLExtend);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLExtend.getRefinedBy().size()));
			assertTrue(addUMLExtend.getRefinedBy().contains(addRefExtendedCaseInExtend));
			assertTrue(addUMLExtend.getRefinedBy().contains(addRefExtensionLocationInExtend));
		} else {
			addUMLExtend = Iterators.find(differences.iterator(), and(instanceOf(ExtendChange.class),
					ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLExtend);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLExtend.getRefinedBy().size()));
			assertTrue(addUMLExtend.getRefinedBy().contains(addExtend));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(addRefExtendedCaseInExtend.getRequires().size()));
			assertTrue(addRefExtendedCaseInExtend.getRequires().contains(addExtend));

			assertSame(Integer.valueOf(2), Integer.valueOf(addRefExtensionLocationInExtend.getRequires()
					.size()));
			assertTrue(addRefExtensionLocationInExtend.getRequires().contains(addExtend));
			assertTrue(addRefExtensionLocationInExtend.getRequires().contains(addExtensionPoint));

			assertSame(Integer.valueOf(0), Integer.valueOf(addExtend.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLExtend.getRequires().size()));
		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(addRefExtendedCaseInExtend.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefExtensionLocationInExtend.getRequires()
					.size()));

			assertSame(Integer.valueOf(2), Integer.valueOf(addExtend.getRequires().size()));
			assertTrue(addExtend.getRequires().contains(addRefExtendedCaseInExtend));
			assertTrue(addExtend.getRequires().contains(addRefExtensionLocationInExtend));

			assertSame(Integer.valueOf(0), Integer.valueOf(addUMLExtend.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getEquivalences().size()));

	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}

}
