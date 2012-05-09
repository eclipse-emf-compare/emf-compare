package org.eclipse.emf.compare.tests.req;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.tests.req.data.ReqInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class ReqComputingTest {

	enum TestKind {
		ADD, DELETE;
	}

	private ReqInputData input = new ReqInputData();

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testAB1(left, right, TestKind.DELETE);
	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		testAB2(left, right, TestKind.DELETE);

	}

	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testAB3(left, right, TestKind.DELETE);
	}

	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		testAB4(left, right, TestKind.DELETE);

	}

	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		testAB5(left, right, TestKind.DELETE);

	}

	@Test
	public void testA6UseCase() throws IOException {
		final Resource left = input.getA6Left();
		final Resource right = input.getA6Right();

		testAB6(left, right, TestKind.DELETE);

	}

	@Test
	public void testB1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		testAB1(right, left, TestKind.ADD);
	}

	@Test
	public void testB2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		testAB2(right, left, TestKind.ADD);
	}

	@Test
	public void testB3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		testAB3(right, left, TestKind.ADD);
	}

	@Test
	public void testB4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		testAB4(right, left, TestKind.ADD);
	}

	@Test
	public void testB5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		testAB5(right, left, TestKind.ADD);
	}

	@Test
	public void testB6UseCase() throws IOException {
		final Resource left = input.getA6Left();
		final Resource right = input.getA6Right();

		testAB6(right, left, TestKind.ADD);
	}

	@Test
	public void testC1UseCase() throws IOException {
		final Resource left = input.getC1Left();
		final Resource right = input.getC1Right();

		testCD1(left, right, TestKind.DELETE);
	}

	@Test
	public void testC2UseCase() throws IOException {
		final Resource left = input.getC2Left();
		final Resource right = input.getC2Right();

		testCD2(left, right, TestKind.DELETE);
	}

	@Test
	public void testC3UseCase() throws IOException {
		final Resource left = input.getC3Left();
		final Resource right = input.getC3Right();

		testCD3(left, right, TestKind.DELETE);
	}

	@Test
	public void testC4UseCase() throws IOException {
		final Resource left = input.getC4Left();
		final Resource right = input.getC4Right();

		testCD4(left, right, TestKind.DELETE);
	}

	@Test
	public void testC5UseCase() throws IOException {
		final Resource left = input.getC5Left();
		final Resource right = input.getC5Right();

		testCD5(left, right, TestKind.DELETE);
	}

	@Test
	public void testD1UseCase() throws IOException {
		final Resource left = input.getC1Left();
		final Resource right = input.getC1Right();

		testCD1(right, left, TestKind.ADD);
	}

	@Test
	public void testD2UseCase() throws IOException {
		final Resource left = input.getC2Left();
		final Resource right = input.getC2Right();

		testCD2(right, left, TestKind.ADD);
	}

	@Test
	public void testD3UseCase() throws IOException {
		final Resource left = input.getC3Left();
		final Resource right = input.getC3Right();

		testCD3(right, left, TestKind.ADD);
	}

	@Test
	public void testD4UseCase() throws IOException {
		final Resource left = input.getC4Left();
		final Resource right = input.getC4Right();

		testCD4(right, left, TestKind.ADD);
	}

	@Test
	public void testD5UseCase() throws IOException {
		final Resource left = input.getC5Left();
		final Resource right = input.getC5Right();

		testCD5(right, left, TestKind.ADD);
	}

	private static void testAB1(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteSourceDiffDescription = null;
		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> changedSingleValuedRefDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteSourceDiffDescription = removed("Requirements.containerSource.source"); //$NON-NLS-1$

			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination"); //$NON-NLS-1$

			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", "Requirements.containerDestination.destination", null); //$NON-NLS-1$//$NON-NLS-2$
		} else {
			deleteSourceDiffDescription = added("Requirements.containerSource.source"); //$NON-NLS-1$

			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination"); //$NON-NLS-1$

			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", null, "Requirements.containerDestination.destination"); //$NON-NLS-1$//$NON-NLS-2$
		}

		final Diff deleteSourceDiff = Iterators.find(differences.iterator(), deleteSourceDiffDescription);

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff changedSingleValuedRefDiff = Iterators.find(differences.iterator(),
				changedSingleValuedRefDiffDescription);

		assertNotNull(deleteSourceDiff);
		assertNotNull(deleteDestinationDiff);
		assertNotNull(changedSingleValuedRefDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(1), Integer.valueOf(deleteSourceDiff.getRequires().size()));
		assertTrue(deleteSourceDiff.getRequires().contains(deleteDestinationDiff));

		assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));

		assertSame(Integer.valueOf(2), Integer.valueOf(changedSingleValuedRefDiff.getRequires().size()));
		assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteDestinationDiff));
		assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteSourceDiff));
	}

	private static void testAB2(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteSourceDiffDescription = null;
		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> deleteMultiValuedRefDiffDescription = null;
		if (kind.equals(TestKind.DELETE)) {
			deleteSourceDiffDescription = removed("Requirements.containerSource.source"); //$NON-NLS-1$
			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination"); //$NON-NLS-1$
			deleteMultiValuedRefDiffDescription = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination"); //$NON-NLS-1$//$NON-NLS-2$
		} else {
			deleteSourceDiffDescription = added("Requirements.containerSource.source"); //$NON-NLS-1$
			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination"); //$NON-NLS-1$
			deleteMultiValuedRefDiffDescription = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final Diff deleteSourceDiff = Iterators.find(differences.iterator(), deleteSourceDiffDescription);

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff deleteMultiValuedRefDiff = Iterators.find(differences.iterator(),
				deleteMultiValuedRefDiffDescription);

		assertNotNull(deleteSourceDiff);
		assertNotNull(deleteDestinationDiff);
		assertNotNull(deleteMultiValuedRefDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(1), Integer.valueOf(deleteSourceDiff.getRequires().size()));
		assertTrue(deleteSourceDiff.getRequires().contains(deleteDestinationDiff));

		assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));

		assertSame(Integer.valueOf(2), Integer.valueOf(deleteMultiValuedRefDiff.getRequires().size()));
		assertTrue(deleteMultiValuedRefDiff.getRequires().contains(deleteDestinationDiff));
		assertTrue(deleteMultiValuedRefDiff.getRequires().contains(deleteSourceDiff));
	}

	private static void testAB3(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> changedSingleValuedRefDiffDescription = null;
		if (kind.equals(TestKind.DELETE)) {
			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", "Requirements.containerDestination.destination2", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.destination1"); //$NON-NLS-1$
		} else {
			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			changedSingleValuedRefDiffDescription = changedReference("Requirements.containerSource.source", //$NON-NLS-1$
					"singleValuedReference", "Requirements.destination1", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.containerDestination.destination2"); //$NON-NLS-1$
		}

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff changedSingleValuedRefDiff = Iterators.find(differences.iterator(),
				changedSingleValuedRefDiffDescription);

		assertNotNull(deleteDestinationDiff);
		assertNotNull(changedSingleValuedRefDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(1), Integer.valueOf(changedSingleValuedRefDiff.getRequires().size()));
		assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteDestinationDiff));

		assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));
	}

	private static void testAB4(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteDestinationDiffDescription = null;
		Predicate<? super Diff> deletedMultiValuedRefDiffDescription = null;
		Predicate<? super Diff> addedMultiValuedRefDiffDescription = null;
		if (kind.equals(TestKind.DELETE)) {
			deleteDestinationDiffDescription = removed("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			deletedMultiValuedRefDiffDescription = removedFromReference(
					"Requirements.containerSource.source", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.containerDestination.destination2"); //$NON-NLS-1$
			addedMultiValuedRefDiffDescription = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$//$NON-NLS-2$
		} else {
			deleteDestinationDiffDescription = added("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			deletedMultiValuedRefDiffDescription = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination2"); //$NON-NLS-1$ //$NON-NLS-2$
			addedMultiValuedRefDiffDescription = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final Diff deleteDestinationDiff = Iterators.find(differences.iterator(),
				deleteDestinationDiffDescription);

		final Diff deletedMultiValuedRefDiff = Iterators.find(differences.iterator(),
				deletedMultiValuedRefDiffDescription);

		final Diff addedMultiValuedRefDiff = Iterators.find(differences.iterator(),
				addedMultiValuedRefDiffDescription);

		assertNotNull(deleteDestinationDiff);
		assertNotNull(deletedMultiValuedRefDiff);
		assertNotNull(addedMultiValuedRefDiffDescription);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(1), Integer.valueOf(deletedMultiValuedRefDiff.getRequires().size()));
		assertSame(Integer.valueOf(0), Integer.valueOf(addedMultiValuedRefDiff.getRequires().size()));
		assertTrue(deletedMultiValuedRefDiff.getRequires().contains(deleteDestinationDiff));

		assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));
	}

	private static void testAB5(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 8 differences
		assertSame(Integer.valueOf(8), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addDest4Description = null;
		Predicate<? super Diff> delDest2Description = null;
		Predicate<? super Diff> delDest3Description = null;
		Predicate<? super Diff> addRefDest1Description = null;
		Predicate<? super Diff> addRefDest4Description = null;
		Predicate<? super Diff> delRefDest2Description = null;
		Predicate<? super Diff> delRefDest3Description = null;
		Predicate<? super Diff> delRefDest5Description = null;

		if (kind.equals(TestKind.DELETE)) {
			addDest4Description = added("Requirements.destination4"); //$NON-NLS-1$
			delDest2Description = removed("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			delDest3Description = removed("Requirements.containerDestination.destination3"); //$NON-NLS-1$

			addRefDest1Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$ //$NON-NLS-2$
			addRefDest4Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination4"); //$NON-NLS-1$//$NON-NLS-2$
			delRefDest2Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination2"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefDest3Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination3"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefDest5Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination5"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			addDest4Description = removed("Requirements.destination4"); //$NON-NLS-1$
			delDest2Description = added("Requirements.containerDestination.destination2"); //$NON-NLS-1$
			delDest3Description = added("Requirements.containerDestination.destination3"); //$NON-NLS-1$

			addRefDest1Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination1"); //$NON-NLS-1$//$NON-NLS-2$
			addRefDest4Description = removedFromReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination4"); //$NON-NLS-1$//$NON-NLS-2$
			delRefDest2Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination2"); //$NON-NLS-1$//$NON-NLS-2$
			delRefDest3Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.containerDestination.destination3"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefDest5Description = addedToReference("Requirements.containerSource.source", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination5"); //$NON-NLS-1$//$NON-NLS-2$
		}

		final Diff addDest4 = Iterators.find(differences.iterator(), addDest4Description);
		final Diff delDest2 = Iterators.find(differences.iterator(), delDest2Description);
		final Diff delDest3 = Iterators.find(differences.iterator(), delDest3Description);

		final Diff addRefDest1 = Iterators.find(differences.iterator(), addRefDest1Description);
		final Diff addRefDest4 = Iterators.find(differences.iterator(), addRefDest4Description);
		final Diff delRefDest2 = Iterators.find(differences.iterator(), delRefDest2Description);
		final Diff delRefDest3 = Iterators.find(differences.iterator(), delRefDest3Description);
		final Diff delRefDest5 = Iterators.find(differences.iterator(), delRefDest5Description);

		assertNotNull(addDest4);
		assertNotNull(delDest2);
		assertNotNull(delDest3);
		assertNotNull(addRefDest1);
		assertNotNull(addRefDest4);
		assertNotNull(delRefDest2);
		assertNotNull(delRefDest3);
		assertNotNull(delRefDest5);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(0), Integer.valueOf(addRefDest1.getRequires().size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(addRefDest4.getRequires().size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(delRefDest2.getRequires().size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(delRefDest3.getRequires().size()));
		assertSame(Integer.valueOf(0), Integer.valueOf(delRefDest5.getRequires().size()));

		assertTrue(addRefDest4.getRequires().contains(addDest4));
		assertTrue(delRefDest2.getRequires().contains(delDest2));
		assertTrue(delRefDest3.getRequires().contains(delDest3));

		assertSame(Integer.valueOf(0), Integer.valueOf(addDest4.getRequires().size()));
		assertSame(Integer.valueOf(0), Integer.valueOf(delDest2.getRequires().size()));
		assertSame(Integer.valueOf(0), Integer.valueOf(delDest3.getRequires().size()));
	}

	private static void testAB6(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		assertSame(Integer.valueOf(7), Integer.valueOf(differences.size()));

		Predicate<? super Diff> delDestDescription = null;
		Predicate<? super Diff> delContainerDescription = null;
		Predicate<? super Diff> delSubContainerDescription = null;
		Predicate<? super Diff> delSource1Description = null;
		Predicate<? super Diff> delSource2Description = null;
		Predicate<? super Diff> delRefSource1Description = null;
		Predicate<? super Diff> delRefSource2Description = null;

		if (kind.equals(TestKind.DELETE)) {
			delDestDescription = removed("Requirements.destination"); //$NON-NLS-1$
			delContainerDescription = removed("Requirements.container"); //$NON-NLS-1$
			delSubContainerDescription = removed("Requirements.container.subContainer"); //$NON-NLS-1$
			delSource1Description = removed("Requirements.container.subContainer.source1"); //$NON-NLS-1$
			delSource2Description = removed("Requirements.container.subContainer.source2"); //$NON-NLS-1$

			delRefSource1Description = changedReference("Requirements.container.subContainer.source1", //$NON-NLS-1$
					"singleValuedReference", "Requirements.destination", null); //$NON-NLS-1$//$NON-NLS-2$
			delRefSource2Description = removedFromReference("Requirements.container.subContainer.source2", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			delDestDescription = added("Requirements.destination"); //$NON-NLS-1$
			delContainerDescription = added("Requirements.container"); //$NON-NLS-1$
			delSubContainerDescription = added("Requirements.container.subContainer"); //$NON-NLS-1$
			delSource1Description = added("Requirements.container.subContainer.source1"); //$NON-NLS-1$
			delSource2Description = added("Requirements.container.subContainer.source2"); //$NON-NLS-1$

			delRefSource1Description = changedReference("Requirements.container.subContainer.source1", //$NON-NLS-1$
					"singleValuedReference", null, "Requirements.destination"); //$NON-NLS-1$ //$NON-NLS-2$
			delRefSource2Description = addedToReference("Requirements.container.subContainer.source2", //$NON-NLS-1$
					"multiValuedReference", "Requirements.destination"); //$NON-NLS-1$//$NON-NLS-2$
		}

		final Diff delDest = Iterators.find(differences.iterator(), delDestDescription);
		final Diff delContainer = Iterators.find(differences.iterator(), delContainerDescription);
		final Diff delSubContainer = Iterators.find(differences.iterator(), delSubContainerDescription);
		final Diff delSource1 = Iterators.find(differences.iterator(), delSource1Description);
		final Diff delSource2 = Iterators.find(differences.iterator(), delSource2Description);
		final Diff delRefSource1 = Iterators.find(differences.iterator(), delRefSource1Description);
		final Diff delRefSource2 = Iterators.find(differences.iterator(), delRefSource2Description);

		assertNotNull(delDest);
		assertNotNull(delContainer);
		assertNotNull(delSubContainer);
		assertNotNull(delSource1);
		assertNotNull(delSource2);
		assertNotNull(delRefSource1);
		assertNotNull(delRefSource2);

		// CHECK REQUIREMENT

		assertSame(Integer.valueOf(0), Integer.valueOf(delDest.getRequires().size()));
		assertSame(Integer.valueOf(0), Integer.valueOf(delContainer.getRequires().size()));
		assertSame(Integer.valueOf(1), Integer.valueOf(delSubContainer.getRequires().size()));
		assertSame(Integer.valueOf(2), Integer.valueOf(delSource1.getRequires().size()));
		assertSame(Integer.valueOf(2), Integer.valueOf(delSource2.getRequires().size()));
		assertSame(Integer.valueOf(2), Integer.valueOf(delRefSource1.getRequires().size()));
		assertSame(Integer.valueOf(2), Integer.valueOf(delRefSource2.getRequires().size()));

		assertTrue(delSubContainer.getRequires().contains(delContainer));
		assertTrue(delSource1.getRequires().contains(delSubContainer));
		assertTrue(delSource2.getRequires().contains(delSubContainer));
		assertTrue(delRefSource1.getRequires().contains(delSource1));
		assertTrue(delRefSource2.getRequires().contains(delSource2));

		assertTrue(delSource1.getRequires().contains(delDest));
		assertTrue(delSource2.getRequires().contains(delDest));
		assertTrue(delRefSource1.getRequires().contains(delDest));
		assertTrue(delRefSource2.getRequires().contains(delDest));
	}

	private static void testCD1(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteADiffDescription = null;
		Predicate<? super Diff> deleteBDiffDescription = null;
		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteADiffDescription = removed("Requirements.A"); //$NON-NLS-1$
			deleteBDiffDescription = removed("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteADiffDescription = added("Requirements.A"); //$NON-NLS-1$
			deleteBDiffDescription = added("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteADiff = Iterators.find(differences.iterator(), deleteADiffDescription);
		final Diff deleteBDiff = Iterators.find(differences.iterator(), deleteBDiffDescription);
		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteADiff);
		assertNotNull(deleteBDiff);
		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(2), Integer.valueOf(deleteADiff.getRequires().size()));
		assertTrue(deleteADiff.getRequires().contains(deleteBDiff));
		assertTrue(deleteADiff.getRequires().contains(deleteCDiff));

		assertSame(Integer.valueOf(2), Integer.valueOf(deleteRefBDiff.getRequires().size()));
		assertTrue(deleteRefBDiff.getRequires().contains(deleteBDiff));
		assertTrue(deleteRefBDiff.getRequires().contains(deleteADiff));

		assertSame(Integer.valueOf(2), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		assertTrue(deleteRefCDiff.getRequires().contains(deleteADiff));

	}

	private static void testCD2(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteADiffDescription = null;
		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteADiffDescription = removed("Requirements.A"); //$NON-NLS-1$
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteADiffDescription = added("Requirements.A"); //$NON-NLS-1$
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteADiff = Iterators.find(differences.iterator(), deleteADiffDescription);
		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteADiff);
		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(1), Integer.valueOf(deleteADiff.getRequires().size()));
		assertTrue(deleteADiff.getRequires().contains(deleteCDiff));

		assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefBDiff.getRequires().size()));
		assertTrue(deleteRefBDiff.getRequires().contains(deleteADiff));

		assertSame(Integer.valueOf(2), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		assertTrue(deleteRefCDiff.getRequires().contains(deleteADiff));

	}

	private static void testCD3(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteBDiffDescription = null;
		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteBDiffDescription = removed("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteBDiffDescription = added("Requirements.B"); //$NON-NLS-1$
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteBDiff = Iterators.find(differences.iterator(), deleteBDiffDescription);
		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteBDiff);
		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(0), Integer.valueOf(deleteBDiff.getRequires().size()));
		assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

		assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefBDiff.getRequires().size()));
		assertTrue(deleteRefBDiff.getRequires().contains(deleteBDiff));

		assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));

	}

	private static void testCD4(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefBDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$ //$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefBDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.B"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefBDiff = Iterators.find(differences.iterator(), deleteRefBDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefBDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

		assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefBDiff.getRequires().size()));

		assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));

	}

	private static void testCD5(final Resource left, final Resource right, TestKind kind) {
		final Comparison comparison = EMFCompare.compare(left, right);

		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> deleteCDiffDescription = null;
		Predicate<? super Diff> deleteRefCDiffDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			deleteCDiffDescription = removed("Requirements.C"); //$NON-NLS-1$
			deleteRefCDiffDescription = removedFromReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		} else {
			deleteCDiffDescription = added("Requirements.C"); //$NON-NLS-1$
			deleteRefCDiffDescription = addedToReference("Requirements.A", "multiValuedReference", //$NON-NLS-1$//$NON-NLS-2$
					"Requirements.C"); //$NON-NLS-1$
		}

		final Diff deleteCDiff = Iterators.find(differences.iterator(), deleteCDiffDescription);
		final Diff deleteRefCDiff = Iterators.find(differences.iterator(), deleteRefCDiffDescription);

		assertNotNull(deleteCDiff);
		assertNotNull(deleteRefCDiff);

		// CHECK REQUIREMENT
		assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

		assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));

	}
}
