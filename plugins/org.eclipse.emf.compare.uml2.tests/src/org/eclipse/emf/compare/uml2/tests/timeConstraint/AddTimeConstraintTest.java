package org.eclipse.emf.compare.uml2.tests.timeConstraint;

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
import org.eclipse.emf.compare.uml2.internal.IntervalConstraintChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.timeConstraint.data.TimeConstraintInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
//import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;

@SuppressWarnings("nls")
public class AddTimeConstraintTest extends AbstractTest {

	private TimeConstraintInputData input = new TimeConstraintInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, null);
		testMergeRightToLeft(left, right, null);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(right, left, null);
		testMergeRightToLeft(right, left, null);
	}

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);

		testMergeLeftToRight(left, right, right);
		testMergeRightToLeft(left, right, right);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);

		testMergeLeftToRight(left, right, left);
		testMergeRightToLeft(left, right, left);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 10 differences
		assertSame(Integer.valueOf(10), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addTimeConstraintDescription = null;
		Predicate<? super Diff> addTimeIntervalDescription = null;
		Predicate<? super Diff> addTimeExpressionMinDescription = null;
		Predicate<? super Diff> addTimeExpressionMaxDescription = null;
		Predicate<? super Diff> addMinValueDescription = null;
		Predicate<? super Diff> addMaxValueDescription = null;
		Predicate<? super Diff> addConstrainedElementInTimeConstraintDescription = null;
		Predicate<? super Diff> addMinInTimeIntervalDescription = null;
		Predicate<? super Diff> addMaxInTimeIntervalDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addTimeConstraintDescription = removed("model.interaction0.TimeConstraint0"); //$NON-NLS-1$
			addTimeIntervalDescription = removed("model.interaction0.TimeConstraint0.TimeInterval"); //$NON-NLS-1$
			addTimeExpressionMinDescription = removed("model.TimeIntervalMin0"); //$NON-NLS-1$
			addTimeExpressionMaxDescription = removed("model.TimeIntervalMax0");
			addMinValueDescription = removed("model.TimeIntervalMin0.min"); //$NON-NLS-1$
			addMaxValueDescription = removed("model.TimeIntervalMax0.max"); //$NON-NLS-1$
			addConstrainedElementInTimeConstraintDescription = removedFromReference(
					"model.interaction0.TimeConstraint0", "constrainedElement",
					"model.interaction0.Message0Send0");
			addMinInTimeIntervalDescription = changedReference(
					"model.interaction0.TimeConstraint0.TimeInterval", "min", "model.TimeIntervalMin0", null);
			addMaxInTimeIntervalDescription = changedReference(
					"model.interaction0.TimeConstraint0.TimeInterval", "max", "model.TimeIntervalMax0", null);
		} else {
			addTimeConstraintDescription = added("model.interaction0.TimeConstraint0"); //$NON-NLS-1$
			addTimeIntervalDescription = added("model.interaction0.TimeConstraint0.TimeInterval"); //$NON-NLS-1$
			addTimeExpressionMinDescription = added("model.TimeIntervalMin0"); //$NON-NLS-1$
			addTimeExpressionMaxDescription = added("model.TimeIntervalMax0");
			addMinValueDescription = added("model.TimeIntervalMin0.min"); //$NON-NLS-1$
			addMaxValueDescription = added("model.TimeIntervalMax0.max"); //$NON-NLS-1$
			addConstrainedElementInTimeConstraintDescription = addedToReference(
					"model.interaction0.TimeConstraint0", "constrainedElement",
					"model.interaction0.Message0Send0");
			addMinInTimeIntervalDescription = changedReference(
					"model.interaction0.TimeConstraint0.TimeInterval", "min", null, "model.TimeIntervalMin0");
			addMaxInTimeIntervalDescription = changedReference(
					"model.interaction0.TimeConstraint0.TimeInterval", "max", null, "model.TimeIntervalMax0");
		}

		final Diff addTimeConstraint = Iterators.find(differences.iterator(), addTimeConstraintDescription);
		final Diff addTimeInterval = Iterators.find(differences.iterator(), addTimeIntervalDescription);
		final Diff addTimeExpressionMin = Iterators.find(differences.iterator(),
				addTimeExpressionMinDescription);
		final Diff addTimeExpressionMax = Iterators.find(differences.iterator(),
				addTimeExpressionMaxDescription);
		final Diff addMinValue = Iterators.find(differences.iterator(), addMinValueDescription);
		final Diff addMaxValue = Iterators.find(differences.iterator(), addMaxValueDescription);
		final Diff addConstrainedElementInTimeConstraint = Iterators.find(differences.iterator(),
				addConstrainedElementInTimeConstraintDescription);
		final Diff addMinInTimeInterval = Iterators.find(differences.iterator(),
				addMinInTimeIntervalDescription);
		final Diff addMaxInTimeInterval = Iterators.find(differences.iterator(),
				addMaxInTimeIntervalDescription);

		assertNotNull(addTimeConstraint);
		assertNotNull(addTimeInterval);
		assertNotNull(addTimeExpressionMin);
		assertNotNull(addTimeExpressionMax);
		assertNotNull(addMinValue);
		assertNotNull(addMaxValue);
		assertNotNull(addConstrainedElementInTimeConstraint);
		assertNotNull(addMinInTimeInterval);
		assertNotNull(addMaxInTimeInterval);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(IntervalConstraintChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(differences.iterator(), and(
					instanceOf(IntervalConstraintChange.class), ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(8), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addConstrainedElementInTimeConstraint));
			assertTrue(addUMLMessage.getRefinedBy().contains(addTimeInterval));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMinInTimeInterval));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMaxInTimeInterval));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMinValue));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMaxValue));
			assertTrue(addUMLMessage.getRefinedBy().contains(addTimeExpressionMin));
			assertTrue(addUMLMessage.getRefinedBy().contains(addTimeExpressionMax));

		} else {
			addUMLMessage = Iterators.find(differences.iterator(), and(
					instanceOf(IntervalConstraintChange.class), ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addTimeConstraint));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(0), Integer.valueOf(addTimeConstraint.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addTimeInterval.getRequires().size()));
			assertTrue(addTimeInterval.getRequires().contains(addTimeConstraint));

			assertSame(Integer.valueOf(0), Integer.valueOf(addTimeExpressionMin.getRequires().size()));
			assertSame(Integer.valueOf(0), Integer.valueOf(addTimeExpressionMax.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addMinValue.getRequires().size()));
			assertTrue(addMinValue.getRequires().contains(addTimeExpressionMin));

			assertSame(Integer.valueOf(1), Integer.valueOf(addMaxValue.getRequires().size()));
			assertTrue(addMaxValue.getRequires().contains(addTimeExpressionMax));

			assertSame(Integer.valueOf(1), Integer.valueOf(addConstrainedElementInTimeConstraint
					.getRequires().size()));
			assertTrue(addTimeInterval.getRequires().contains(addTimeConstraint));

			assertSame(Integer.valueOf(2), Integer.valueOf(addMinInTimeInterval.getRequires().size()));
			assertTrue(addMinInTimeInterval.getRequires().contains(addTimeInterval));
			assertTrue(addMinInTimeInterval.getRequires().contains(addTimeExpressionMin));

			assertSame(Integer.valueOf(2), Integer.valueOf(addMaxInTimeInterval.getRequires().size()));
			assertTrue(addMaxInTimeInterval.getRequires().contains(addTimeInterval));
			assertTrue(addMaxInTimeInterval.getRequires().contains(addTimeExpressionMax));

		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(addTimeConstraint.getRequires().size()));
			assertTrue(addTimeConstraint.getRequires().contains(addConstrainedElementInTimeConstraint));
			assertTrue(addTimeConstraint.getRequires().contains(addTimeInterval));

			assertSame(Integer.valueOf(2), Integer.valueOf(addTimeInterval.getRequires().size()));
			assertTrue(addTimeInterval.getRequires().contains(addMinInTimeInterval));
			assertTrue(addTimeInterval.getRequires().contains(addMaxInTimeInterval));

			assertSame(Integer.valueOf(2), Integer.valueOf(addTimeExpressionMin.getRequires().size()));
			assertTrue(addTimeExpressionMin.getRequires().contains(addMinValue));
			assertTrue(addTimeExpressionMin.getRequires().contains(addMinInTimeInterval));

			assertSame(Integer.valueOf(2), Integer.valueOf(addTimeExpressionMax.getRequires().size()));
			assertTrue(addTimeExpressionMax.getRequires().contains(addMaxValue));
			assertTrue(addTimeExpressionMax.getRequires().contains(addMaxInTimeInterval));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMinValue.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMaxValue.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addConstrainedElementInTimeConstraint
					.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMinInTimeInterval.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMaxInTimeInterval.getRequires().size()));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(0), Integer.valueOf(comparison.getEquivalences().size()));

	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}

}
