/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.req;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInReference;
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

@SuppressWarnings("nls")
public class ReqComputingTest {

	enum TestKind {
		ADD, DELETE;
	}

	private ReqInputData input = new ReqInputData();

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB4(TestKind.DELETE, comparison);
	}

	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB5(TestKind.DELETE, comparison);
	}

	@Test
	public void testA6UseCase() throws IOException {
		final Resource left = input.getA6Left();
		final Resource right = input.getA6Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB6(TestKind.DELETE, comparison);

	}

	@Test
	public void testA7UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB7(TestKind.ADD, comparison);
	}

	@Test
	public void testA8UseCase() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB8(TestKind.ADD, comparison);
	}

	@Test
	public void testA9UseCase() throws IOException {
		final Resource left = input.getA9Left();
		final Resource right = input.getA9Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB9(TestKind.ADD, comparison);
	}

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA10Left();
		final Resource right = input.getA10Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB10(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA11Left();
		final Resource right = input.getA11Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testAB11(TestKind.ADD, comparison);
	}

	@Test
	public void testB1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testB2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testB3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testB4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB4(TestKind.ADD, comparison);
	}

	@Test
	public void testB5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB5(TestKind.ADD, comparison);
	}

	@Test
	public void testB6UseCase() throws IOException {
		final Resource left = input.getA6Left();
		final Resource right = input.getA6Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB6(TestKind.ADD, comparison);
	}

	@Test
	public void testB7UseCase() throws IOException {
		final Resource left = input.getA7Left();
		final Resource right = input.getA7Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB7(TestKind.DELETE, comparison);
	}

	@Test
	public void testB8UseCase() throws IOException {
		final Resource left = input.getA8Left();
		final Resource right = input.getA8Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB8(TestKind.DELETE, comparison);
	}

	@Test
	public void testB9UseCase() throws IOException {
		final Resource left = input.getA9Left();
		final Resource right = input.getA9Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB9(TestKind.DELETE, comparison);
	}

	@Test
	public void testB10UseCase() throws IOException {
		final Resource left = input.getA10Left();
		final Resource right = input.getA10Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB10(TestKind.DELETE, comparison);
	}

	@Test
	public void testB11UseCase() throws IOException {
		final Resource left = input.getA11Left();
		final Resource right = input.getA11Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testAB11(TestKind.DELETE, comparison);
	}

	@Test
	public void testC1UseCase() throws IOException {
		final Resource left = input.getC1Left();
		final Resource right = input.getC1Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testCD1(TestKind.DELETE, comparison);
	}

	@Test
	public void testC2UseCase() throws IOException {
		final Resource left = input.getC2Left();
		final Resource right = input.getC2Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testCD2(TestKind.DELETE, comparison);
	}

	@Test
	public void testC3UseCase() throws IOException {
		final Resource left = input.getC3Left();
		final Resource right = input.getC3Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testCD3(TestKind.DELETE, comparison);
	}

	@Test
	public void testC4UseCase() throws IOException {
		final Resource left = input.getC4Left();
		final Resource right = input.getC4Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testCD4(TestKind.DELETE, comparison);
	}

	@Test
	public void testC5UseCase() throws IOException {
		final Resource left = input.getC5Left();
		final Resource right = input.getC5Right();

		final Comparison comparison = EMFCompare.compare(left, right);
		testCD5(TestKind.DELETE, comparison);
	}

	@Test
	public void testD1UseCase() throws IOException {
		final Resource left = input.getC1Left();
		final Resource right = input.getC1Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testCD1(TestKind.ADD, comparison);
	}

	@Test
	public void testD2UseCase() throws IOException {
		final Resource left = input.getC2Left();
		final Resource right = input.getC2Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testCD2(TestKind.ADD, comparison);
	}

	@Test
	public void testD3UseCase() throws IOException {
		final Resource left = input.getC3Left();
		final Resource right = input.getC3Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testCD3(TestKind.ADD, comparison);
	}

	@Test
	public void testD4UseCase() throws IOException {
		final Resource left = input.getC4Left();
		final Resource right = input.getC4Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testCD4(TestKind.ADD, comparison);
	}

	@Test
	public void testD5UseCase() throws IOException {
		final Resource left = input.getC5Left();
		final Resource right = input.getC5Right();

		final Comparison comparison = EMFCompare.compare(right, left);
		testCD5(TestKind.ADD, comparison);
	}

	@Test
	public void testE1UseCase1() throws IOException {
		final Resource left = input.getE1Left();
		final Resource right = input.getE1Right();
		final Resource ancestor = input.getE1Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testE2UseCase() throws IOException {
		final Resource left = input.getE2Left();
		final Resource right = input.getE2Right();
		final Resource ancestor = input.getE2Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testE3UseCase() throws IOException {
		final Resource left = input.getE3Left();
		final Resource right = input.getE3Right();
		final Resource ancestor = input.getE3Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testE4UseCase() throws IOException {
		final Resource left = input.getE4Left();
		final Resource right = input.getE4Right();
		final Resource ancestor = input.getE4Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB4(TestKind.DELETE, comparison);
	}

	@Test
	public void testE5UseCase() throws IOException {
		final Resource left = input.getE5Left();
		final Resource right = input.getE5Right();
		final Resource ancestor = input.getE5Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB5(TestKind.DELETE, comparison);
	}

	@Test
	public void testE6UseCase() throws IOException {
		final Resource left = input.getE6Left();
		final Resource right = input.getE6Right();
		final Resource ancestor = input.getE6Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB6(TestKind.DELETE, comparison);
	}

	@Test
	public void testE7UseCase() throws IOException {
		final Resource left = input.getE7Left();
		final Resource right = input.getE7Right();
		final Resource ancestor = input.getE7Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD1(TestKind.DELETE, comparison);
	}

	@Test
	public void testE8UseCase() throws IOException {
		final Resource left = input.getE8Left();
		final Resource right = input.getE8Right();
		final Resource ancestor = input.getE8Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD2(TestKind.DELETE, comparison);
	}

	@Test
	public void testE9UseCase() throws IOException {
		final Resource left = input.getE9Left();
		final Resource right = input.getE9Right();
		final Resource ancestor = input.getE9Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD3(TestKind.DELETE, comparison);
	}

	@Test
	public void testE10UseCase() throws IOException {
		final Resource left = input.getE10Left();
		final Resource right = input.getE10Right();
		final Resource ancestor = input.getE10Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD4(TestKind.DELETE, comparison);
	}

	@Test
	public void testE11UseCase() throws IOException {
		final Resource left = input.getE11Left();
		final Resource right = input.getE11Right();
		final Resource ancestor = input.getE11Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD5(TestKind.DELETE, comparison);
	}

	@Test
	public void testF1UseCase1() throws IOException {
		final Resource left = input.getF1Left();
		final Resource right = input.getF1Right();
		final Resource ancestor = input.getF1Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testF2UseCase() throws IOException {
		final Resource left = input.getF2Left();
		final Resource right = input.getF2Right();
		final Resource ancestor = input.getF2Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testF3UseCase() throws IOException {
		final Resource left = input.getF3Left();
		final Resource right = input.getF3Right();
		final Resource ancestor = input.getF3Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testF4UseCase() throws IOException {
		final Resource left = input.getF4Left();
		final Resource right = input.getF4Right();
		final Resource ancestor = input.getF4Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB4(TestKind.ADD, comparison);
	}

	@Test
	public void testF5UseCase() throws IOException {
		final Resource left = input.getF5Left();
		final Resource right = input.getF5Right();
		final Resource ancestor = input.getF5Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB5(TestKind.ADD, comparison);
	}

	@Test
	public void testF6UseCase() throws IOException {
		final Resource left = input.getF6Left();
		final Resource right = input.getF6Right();
		final Resource ancestor = input.getF6Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testAB6(TestKind.ADD, comparison);
	}

	@Test
	public void testF7UseCase() throws IOException {
		final Resource left = input.getF7Left();
		final Resource right = input.getF7Right();
		final Resource ancestor = input.getF7Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD1(TestKind.ADD, comparison);
	}

	@Test
	public void testF8UseCase() throws IOException {
		final Resource left = input.getF8Left();
		final Resource right = input.getF8Right();
		final Resource ancestor = input.getF8Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD2(TestKind.ADD, comparison);
	}

	@Test
	public void testF9UseCase() throws IOException {
		final Resource left = input.getF9Left();
		final Resource right = input.getF9Right();
		final Resource ancestor = input.getF9Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD3(TestKind.ADD, comparison);
	}

	@Test
	public void testF10UseCase() throws IOException {
		final Resource left = input.getF10Left();
		final Resource right = input.getF10Right();
		final Resource ancestor = input.getF10Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD4(TestKind.ADD, comparison);
	}

	@Test
	public void testF11UseCase() throws IOException {
		final Resource left = input.getF11Left();
		final Resource right = input.getF11Right();
		final Resource ancestor = input.getF11Ancestor();

		final Comparison comparison = EMFCompare.compare(left, right, ancestor);
		testCD5(TestKind.ADD, comparison);
	}

	private static void testAB1(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteSourceDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));

			assertSame(Integer.valueOf(2), Integer.valueOf(changedSingleValuedRefDiff.getRequires().size()));
			assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteDestinationDiff));
			assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteSourceDiff));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(deleteSourceDiff.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteDestinationDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(changedSingleValuedRefDiff.getRequires().size()));
		}
	}

	private static void testAB2(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteSourceDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));

			assertSame(Integer.valueOf(2), Integer.valueOf(deleteMultiValuedRefDiff.getRequires().size()));
			assertTrue(deleteMultiValuedRefDiff.getRequires().contains(deleteDestinationDiff));
			assertTrue(deleteMultiValuedRefDiff.getRequires().contains(deleteSourceDiff));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(deleteSourceDiff.getRequires().size()));
			assertTrue(deleteSourceDiff.getRequires().contains(deleteMultiValuedRefDiff));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteDestinationDiff.getRequires().size()));
			assertTrue(deleteDestinationDiff.getRequires().contains(deleteMultiValuedRefDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteMultiValuedRefDiff.getRequires().size()));
		}
	}

	private static void testAB3(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(changedSingleValuedRefDiff.getRequires().size()));
			assertTrue(changedSingleValuedRefDiff.getRequires().contains(deleteDestinationDiff));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(deleteDestinationDiff.getRequires().size()));
			assertTrue(deleteDestinationDiff.getRequires().contains(changedSingleValuedRefDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(changedSingleValuedRefDiff.getRequires().size()));
		}
	}

	private static void testAB4(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteDestinationDiff.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(deletedMultiValuedRefDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addedMultiValuedRefDiff.getRequires().size()));
			assertTrue(deletedMultiValuedRefDiff.getRequires().contains(deleteDestinationDiff));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(deleteDestinationDiff.getRequires().size()));
			assertTrue(deleteDestinationDiff.getRequires().contains(deletedMultiValuedRefDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(deletedMultiValuedRefDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addedMultiValuedRefDiff.getRequires().size()));
		}

	}

	private static void testAB5(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(addDest4.getRequires().size()));
			assertTrue(addDest4.getRequires().contains(addRefDest4));

			assertSame(Integer.valueOf(0), Integer.valueOf(delDest2.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(delDest3.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefDest1.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefDest4.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(delRefDest2.getRequires().size()));
			assertTrue(delRefDest2.getRequires().contains(delDest2));

			assertSame(Integer.valueOf(1), Integer.valueOf(delRefDest3.getRequires().size()));
			assertTrue(delRefDest3.getRequires().contains(delDest3));

			assertSame(Integer.valueOf(0), Integer.valueOf(delRefDest5.getRequires().size()));

		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(addDest4.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(delDest2.getRequires().size()));
			assertTrue(delDest2.getRequires().contains(delRefDest2));

			assertSame(Integer.valueOf(1), Integer.valueOf(delDest3.getRequires().size()));
			assertTrue(delDest3.getRequires().contains(delRefDest3));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefDest1.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefDest4.getRequires().size()));
			assertTrue(addRefDest4.getRequires().contains(addDest4));

			assertSame(Integer.valueOf(0), Integer.valueOf(delRefDest2.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(delRefDest3.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(delRefDest5.getRequires().size()));
		}

	}

	private static void testAB6(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(delDest.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(delContainer.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(delSubContainer.getRequires().size()));
			assertTrue(delSubContainer.getRequires().contains(delContainer));

			assertSame(Integer.valueOf(1), Integer.valueOf(delSource1.getRequires().size()));
			assertTrue(delSource1.getRequires().contains(delSubContainer));

			assertSame(Integer.valueOf(1), Integer.valueOf(delSource2.getRequires().size()));
			assertTrue(delSource2.getRequires().contains(delSubContainer));

			assertSame(Integer.valueOf(2), Integer.valueOf(delRefSource1.getRequires().size()));
			assertTrue(delRefSource1.getRequires().contains(delSource1));
			assertTrue(delRefSource2.getRequires().contains(delSource2));

			assertSame(Integer.valueOf(2), Integer.valueOf(delRefSource2.getRequires().size()));
			assertTrue(delRefSource1.getRequires().contains(delDest));
			assertTrue(delRefSource2.getRequires().contains(delDest));
		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(delDest.getRequires().size()));
			assertTrue(delDest.getRequires().contains(delRefSource1));
			assertTrue(delDest.getRequires().contains(delRefSource2));

			assertSame(Integer.valueOf(1), Integer.valueOf(delContainer.getRequires().size()));
			assertTrue(delContainer.getRequires().contains(delSubContainer));

			assertSame(Integer.valueOf(2), Integer.valueOf(delSubContainer.getRequires().size()));
			assertTrue(delSubContainer.getRequires().contains(delSource1));
			assertTrue(delSubContainer.getRequires().contains(delSource2));

			assertSame(Integer.valueOf(1), Integer.valueOf(delSource1.getRequires().size()));
			assertTrue(delSource1.getRequires().contains(delRefSource1));

			assertSame(Integer.valueOf(1), Integer.valueOf(delSource2.getRequires().size()));
			assertTrue(delSource2.getRequires().contains(delRefSource2));

			assertSame(Integer.valueOf(0), Integer.valueOf(delRefSource1.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(delRefSource2.getRequires().size()));
		}

	}

	private static void testAB7(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addBDescription = null;
		Predicate<? super Diff> delCDescription = null;
		Predicate<? super Diff> addEDescription = null;
		Predicate<? super Diff> delFDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addBDescription = added("Requirements.A.B"); //$NON-NLS-1$
			delCDescription = removed("Requirements.A.C"); //$NON-NLS-1$
			addEDescription = added("Requirements.D.E"); //$NON-NLS-1$
			delFDescription = removed("Requirements.D.F"); //$NON-NLS-1$
		} else {
			addBDescription = removed("Requirements.A.B"); //$NON-NLS-1$
			delCDescription = added("Requirements.A.C"); //$NON-NLS-1$
			addEDescription = removed("Requirements.D.E"); //$NON-NLS-1$
			delFDescription = added("Requirements.D.F"); //$NON-NLS-1$
		}

		final Diff addB = Iterators.find(differences.iterator(), addBDescription);
		final Diff delC = Iterators.find(differences.iterator(), delCDescription);
		final Diff addE = Iterators.find(differences.iterator(), addEDescription);
		final Diff delF = Iterators.find(differences.iterator(), delFDescription);

		assertNotNull(addB);
		assertNotNull(delC);
		assertNotNull(addE);
		assertNotNull(delF);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(addB.getRequires().size()));
			assertTrue(addB.getRequires().contains(delC));

			assertSame(Integer.valueOf(0), Integer.valueOf(delC.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addE.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(delF.getRequires().size()));
		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(addB.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(delC.getRequires().size()));
			assertTrue(delC.getRequires().contains(addB));

			assertSame(Integer.valueOf(0), Integer.valueOf(addE.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(delF.getRequires().size()));
		}

	}

	private static void testAB8(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		assertSame(Integer.valueOf(7), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addBDescription = null;
		Predicate<? super Diff> delCDescription = null;
		Predicate<? super Diff> changeRefBDescription = null;
		Predicate<? super Diff> addEDescription = null;
		Predicate<? super Diff> delFDescription = null;
		Predicate<? super Diff> addRefEDescription = null;
		Predicate<? super Diff> delRefFDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addBDescription = added("Requirements.B"); //$NON-NLS-1$
			delCDescription = removed("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference(
					"Requirements.A", "singleValuedReference", "Requirements.C", "Requirements.B"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			addEDescription = added("Requirements.E"); //$NON-NLS-1$
			delFDescription = removed("Requirements.F"); //$NON-NLS-1$
			addRefEDescription = addedToReference("Requirements.D", "multiValuedReference", "Requirements.E"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
			delRefFDescription = removedFromReference(
					"Requirements.D", "multiValuedReference", "Requirements.F"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
		} else {
			addBDescription = removed("Requirements.B"); //$NON-NLS-1$
			delCDescription = added("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference(
					"Requirements.A", "singleValuedReference", "Requirements.B", "Requirements.C"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			addEDescription = removed("Requirements.E"); //$NON-NLS-1$
			delFDescription = added("Requirements.F"); //$NON-NLS-1$
			addRefEDescription = removedFromReference(
					"Requirements.D", "multiValuedReference", "Requirements.E"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
			delRefFDescription = addedToReference("Requirements.D", "multiValuedReference", "Requirements.F"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
		}

		final Diff addB = Iterators.find(differences.iterator(), addBDescription);
		final Diff delC = Iterators.find(differences.iterator(), delCDescription);
		final Diff changeRefB = Iterators.find(differences.iterator(), changeRefBDescription);
		final Diff addE = Iterators.find(differences.iterator(), addEDescription);
		final Diff delF = Iterators.find(differences.iterator(), delFDescription);
		final Diff addRefE = Iterators.find(differences.iterator(), addRefEDescription);
		final Diff delRefF = Iterators.find(differences.iterator(), delRefFDescription);

		assertNotNull(addB);
		assertNotNull(delC);
		assertNotNull(changeRefB);
		assertNotNull(addE);
		assertNotNull(delF);
		assertNotNull(addRefE);
		assertNotNull(delRefF);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(addB.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(delC.getRequires().size()));
			assertTrue(delC.getRequires().contains(changeRefB));

			assertSame(Integer.valueOf(1), Integer.valueOf(changeRefB.getRequires().size()));
			assertTrue(changeRefB.getRequires().contains(addB));

			assertSame(Integer.valueOf(0), Integer.valueOf(addE.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(delF.getRequires().size()));
			assertTrue(delF.getRequires().contains(delRefF));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefE.getRequires().size()));
			assertTrue(addRefE.getRequires().contains(addE));

			assertSame(Integer.valueOf(0), Integer.valueOf(delRefF.getRequires().size()));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(addB.getRequires().size()));
			assertTrue(addB.getRequires().contains(changeRefB));

			assertSame(Integer.valueOf(0), Integer.valueOf(delC.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(changeRefB.getRequires().size()));
			assertTrue(changeRefB.getRequires().contains(delC));

			assertSame(Integer.valueOf(1), Integer.valueOf(addE.getRequires().size()));
			assertTrue(addE.getRequires().contains(addRefE));

			assertSame(Integer.valueOf(0), Integer.valueOf(delF.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefE.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(delRefF.getRequires().size()));
			assertTrue(delRefF.getRequires().contains(delF));
		}

	}

	private static void testAB9(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
		assertSame(Integer.valueOf(2), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addBDescription = null;
		Predicate<? super Diff> addCDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addBDescription = added("Requirements.A.B"); //$NON-NLS-1$
			addCDescription = added("Requirements.A.B.C"); //$NON-NLS-1$
		} else {
			addBDescription = removed("Requirements.A.B"); //$NON-NLS-1$
			addCDescription = removed("Requirements.A.B.C"); //$NON-NLS-1$
		}

		final Diff addB = Iterators.find(differences.iterator(), addBDescription);
		final Diff addC = Iterators.find(differences.iterator(), addCDescription);

		assertNotNull(addB);
		assertNotNull(addC);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(addB.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addC.getRequires().size()));
			assertTrue(addC.getRequires().contains(addB));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(addB.getRequires().size()));
			assertTrue(addB.getRequires().contains(addC));

			assertSame(Integer.valueOf(0), Integer.valueOf(addC.getRequires().size()));
		}

	}

	private static void testAB10(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addADescription = null;
		Predicate<? super Diff> addCDescription = null;
		Predicate<? super Diff> changeRefBDescription = null;
		Predicate<? super Diff> addRefBDescription = null;

		if (kind.equals(TestKind.ADD)) {
			addADescription = added("Requirements.A"); //$NON-NLS-1$
			addCDescription = added("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference("Requirements.A", "singleValuedReference", null,
					"Requirements.B");
			addRefBDescription = addedToReference("Requirements.C", "multiValuedReference", "Requirements.B");
		} else {
			addADescription = removed("Requirements.A"); //$NON-NLS-1$
			addCDescription = removed("Requirements.C"); //$NON-NLS-1$
			changeRefBDescription = changedReference("Requirements.A", "singleValuedReference",
					"Requirements.B", null);
			addRefBDescription = removedFromReference("Requirements.C", "multiValuedReference",
					"Requirements.B");
		}

		final Diff addA = Iterators.find(differences.iterator(), addADescription);
		final Diff addC = Iterators.find(differences.iterator(), addCDescription);
		final Diff changeRefB = Iterators.find(differences.iterator(), changeRefBDescription);
		final Diff addRefB = Iterators.find(differences.iterator(), addRefBDescription);

		assertNotNull(addA);
		assertNotNull(addC);
		assertNotNull(changeRefB);
		assertNotNull(addRefB);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(addA.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addC.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(changeRefB.getRequires().size()));
			assertTrue(changeRefB.getRequires().contains(addA));

			assertSame(Integer.valueOf(1), Integer.valueOf(addRefB.getRequires().size()));
			assertTrue(addRefB.getRequires().contains(addC));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(addA.getRequires().size()));
			assertTrue(addA.getRequires().contains(changeRefB));

			assertSame(Integer.valueOf(1), Integer.valueOf(addC.getRequires().size()));
			assertTrue(addC.getRequires().contains(addRefB));

			assertSame(Integer.valueOf(0), Integer.valueOf(changeRefB.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addRefB.getRequires().size()));
		}

	}

	private static void testAB11(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> delBDescription = null;
		Predicate<? super Diff> moveCDescription = null;
		Predicate<? super Diff> moveDDescription = null;
		Predicate<? super Diff> moveEDescription = null;

		if (kind.equals(TestKind.ADD)) {
			delBDescription = removed("Requirements.A.B"); //$NON-NLS-1$
			moveCDescription = movedInReference("Requirements.A", "containmentRef1", "Requirements.A.C"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveDDescription = movedInReference("Requirements.A", "containmentRef1", "Requirements.A.D"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveEDescription = movedInReference("Requirements.A.D", "containmentRef1", "Requirements.A.D.E"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			delBDescription = added("Requirements.A.B"); //$NON-NLS-1$
			moveCDescription = movedInReference("Requirements.A.B", "containmentRef1", "Requirements.A.B.C"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveDDescription = movedInReference("Requirements.A.E", "containmentRef1", "Requirements.A.E.D"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			moveEDescription = movedInReference("Requirements.A", "containmentRef1", "Requirements.A.E"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		final Diff delB = Iterators.find(differences.iterator(), delBDescription);
		final Diff moveC = Iterators.find(differences.iterator(), moveCDescription);
		final Diff moveD = Iterators.find(differences.iterator(), moveDDescription);
		final Diff moveE = Iterators.find(differences.iterator(), moveEDescription);

		assertNotNull(delB);
		assertNotNull(moveC);
		assertNotNull(moveD);
		assertNotNull(moveE);

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(1), Integer.valueOf(delB.getRequires().size()));
			assertTrue(delB.getRequires().contains(moveC));

			assertSame(Integer.valueOf(0), Integer.valueOf(moveC.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(moveD.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(moveE.getRequires().size()));
			assertTrue(moveE.getRequires().contains(moveD));
		} else {
			assertSame(Integer.valueOf(0), Integer.valueOf(delB.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(moveC.getRequires().size()));
			assertTrue(moveC.getRequires().contains(delB));

			assertSame(Integer.valueOf(1), Integer.valueOf(moveD.getRequires().size()));
			assertTrue(moveD.getRequires().contains(moveE));

			assertSame(Integer.valueOf(0), Integer.valueOf(moveE.getRequires().size()));
		}

	}

	private static void testCD1(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteADiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteBDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

			assertSame(Integer.valueOf(2), Integer.valueOf(deleteRefBDiff.getRequires().size()));
			assertTrue(deleteRefBDiff.getRequires().contains(deleteBDiff));
			assertTrue(deleteRefBDiff.getRequires().contains(deleteADiff));

			assertSame(Integer.valueOf(2), Integer.valueOf(deleteRefCDiff.getRequires().size()));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteADiff));
		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(deleteADiff.getRequires().size()));
			assertTrue(deleteADiff.getRequires().contains(deleteRefBDiff));
			assertTrue(deleteADiff.getRequires().contains(deleteRefCDiff));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteBDiff.getRequires().size()));
			assertTrue(deleteBDiff.getRequires().contains(deleteRefBDiff));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteCDiff.getRequires().size()));
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefBDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		}

	}

	private static void testCD2(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteADiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefBDiff.getRequires().size()));
			assertTrue(deleteRefBDiff.getRequires().contains(deleteADiff));

			assertSame(Integer.valueOf(2), Integer.valueOf(deleteRefCDiff.getRequires().size()));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteADiff));
		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(deleteADiff.getRequires().size()));
			assertTrue(deleteADiff.getRequires().contains(deleteRefBDiff));
			assertTrue(deleteADiff.getRequires().contains(deleteRefCDiff));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteCDiff.getRequires().size()));
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefBDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		}

	}

	private static void testCD3(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteBDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefBDiff.getRequires().size()));
			assertTrue(deleteRefBDiff.getRequires().contains(deleteBDiff));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefCDiff.getRequires().size()));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(deleteBDiff.getRequires().size()));
			assertTrue(deleteBDiff.getRequires().contains(deleteRefBDiff));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteCDiff.getRequires().size()));
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefBDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		}

	}

	private static void testCD4(TestKind kind, final Comparison comparison) {
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefBDiff.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefCDiff.getRequires().size()));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(deleteCDiff.getRequires().size()));
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefBDiff.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		}

	}

	private static void testCD5(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 2 differences
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
		if (kind.equals(TestKind.ADD)) {
			assertSame(Integer.valueOf(0), Integer.valueOf(deleteCDiff.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(deleteRefCDiff.getRequires().size()));
			assertTrue(deleteRefCDiff.getRequires().contains(deleteCDiff));
		} else {
			assertSame(Integer.valueOf(1), Integer.valueOf(deleteCDiff.getRequires().size()));
			assertTrue(deleteCDiff.getRequires().contains(deleteRefCDiff));

			assertSame(Integer.valueOf(0), Integer.valueOf(deleteRefCDiff.getRequires().size()));
		}

	}
}
