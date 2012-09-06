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
package org.eclipse.emf.compare.tests.equi;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.equi.data.EquiInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings({"nls", "boxing" })
public class EquiComputingTest {

	enum TestKind {
		LEFT, RIGHT;
	}

	private EquiInputData input = new EquiInputData();

	@Test
	public void testA1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();
		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();
		testA1(TestKind.LEFT, comparison);
	}

	private static void testA1(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 6 differences
		assertSame(Integer.valueOf(6), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefA2BDiffDescription = null;
		Predicate<? super Diff> changeRefB2ADiffDescription = null;
		Predicate<? super Diff> changeRefC2DDiffDescription = null;
		Predicate<? super Diff> changeRefD2CDiffDescription = null;
		Predicate<? super Diff> changeRefE2FDiffDescription = null;
		Predicate<? super Diff> changeRefF2EDiffDescription = null;
		if (kind.equals(TestKind.LEFT)) {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", null,
					"Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", null, "Requirements.A");
			changeRefC2DDiffDescription = addedToReference("Requirements.C", "destination", "Requirements.D");
			changeRefD2CDiffDescription = changedReference("Requirements.D", "source", null, "Requirements.C");
			changeRefE2FDiffDescription = addedToReference("Requirements.E", "destination", "Requirements.F");
			changeRefF2EDiffDescription = addedToReference("Requirements.F", "source", "Requirements.E");
		} else {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", "Requirements.B",
					null);
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", "Requirements.A", null);
			changeRefC2DDiffDescription = removedFromReference("Requirements.C", "destination",
					"Requirements.D");
			changeRefD2CDiffDescription = changedReference("Requirements.D", "source", "Requirements.C", null);
			changeRefE2FDiffDescription = removedFromReference("Requirements.E", "destination",
					"Requirements.F");
			changeRefF2EDiffDescription = removedFromReference("Requirements.F", "source", "Requirements.E");
		}

		final Diff changeRefA2BDiff = Iterators.find(differences.iterator(), changeRefA2BDiffDescription);
		final Diff changeRefB2ADiff = Iterators.find(differences.iterator(), changeRefB2ADiffDescription);
		final Diff changeRefC2DDiff = Iterators.find(differences.iterator(), changeRefC2DDiffDescription);
		final Diff changeRefD2CDiff = Iterators.find(differences.iterator(), changeRefD2CDiffDescription);
		final Diff changeRefE2FDiff = Iterators.find(differences.iterator(), changeRefE2FDiffDescription);
		final Diff changeRefF2EDiff = Iterators.find(differences.iterator(), changeRefF2EDiffDescription);

		assertNotNull(changeRefA2BDiff);
		assertNotNull(changeRefB2ADiff);
		assertNotNull(changeRefC2DDiff);
		assertNotNull(changeRefD2CDiff);
		assertNotNull(changeRefE2FDiff);
		assertNotNull(changeRefF2EDiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(3), comparison.getEquivalences().size());

		assertNotNull(changeRefA2BDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefA2BDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefA2BDiff));
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefB2ADiff));

		assertNotNull(changeRefC2DDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefC2DDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefC2DDiff.getEquivalence().getDifferences().contains(changeRefC2DDiff));
		assertTrue(changeRefC2DDiff.getEquivalence().getDifferences().contains(changeRefD2CDiff));

		assertNotNull(changeRefE2FDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefE2FDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefE2FDiff.getEquivalence().getDifferences().contains(changeRefE2FDiff));
		assertTrue(changeRefE2FDiff.getEquivalence().getDifferences().contains(changeRefF2EDiff));
	}

	@Test
	public void testA2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA2(TestKind.LEFT, comparison);

	}

	private static void testA2(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 8 differences
		assertSame(Integer.valueOf(8), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefC2D1DiffDescription = null;
		Predicate<? super Diff> changeRefC2D2DiffDescription = null;
		Predicate<? super Diff> changeRefD12CDiffDescription = null;
		Predicate<? super Diff> changeRefD22CDiffDescription = null;
		Predicate<? super Diff> changeRefE2F1DiffDescription = null;
		Predicate<? super Diff> changeRefE2F2DiffDescription = null;
		Predicate<? super Diff> changeRefF12EDiffDescription = null;
		Predicate<? super Diff> changeRefF22EDiffDescription = null;

		if (kind.equals(TestKind.LEFT)) {
			changeRefC2D1DiffDescription = addedToReference("Requirements.C", "destination",
					"Requirements.D1");
			changeRefC2D2DiffDescription = addedToReference("Requirements.C", "destination",
					"Requirements.D2");
			changeRefD12CDiffDescription = changedReference("Requirements.D1", "source", null,
					"Requirements.C");
			changeRefD22CDiffDescription = changedReference("Requirements.D2", "source", null,
					"Requirements.C");
			changeRefE2F1DiffDescription = addedToReference("Requirements.E", "destination",
					"Requirements.F1");
			changeRefE2F2DiffDescription = addedToReference("Requirements.E", "destination",
					"Requirements.F2");
			changeRefF12EDiffDescription = addedToReference("Requirements.F1", "source", "Requirements.E");
			changeRefF22EDiffDescription = addedToReference("Requirements.F2", "source", "Requirements.E");
		} else {
			changeRefC2D1DiffDescription = removedFromReference("Requirements.C", "destination",
					"Requirements.D1");
			changeRefC2D2DiffDescription = removedFromReference("Requirements.C", "destination",
					"Requirements.D2");
			changeRefD12CDiffDescription = changedReference("Requirements.D1", "source", "Requirements.C",
					null);
			changeRefD22CDiffDescription = changedReference("Requirements.D2", "source", "Requirements.C",
					null);
			changeRefE2F1DiffDescription = removedFromReference("Requirements.E", "destination",
					"Requirements.F1");
			changeRefE2F2DiffDescription = removedFromReference("Requirements.E", "destination",
					"Requirements.F2");
			changeRefF12EDiffDescription = removedFromReference("Requirements.F1", "source", "Requirements.E");
			changeRefF22EDiffDescription = removedFromReference("Requirements.F2", "source", "Requirements.E");
		}

		final Diff changeRefC2D1Diff = Iterators.find(differences.iterator(), changeRefC2D1DiffDescription);
		final Diff changeRefC2D2Diff = Iterators.find(differences.iterator(), changeRefC2D2DiffDescription);
		final Diff changeRefD12CDiff = Iterators.find(differences.iterator(), changeRefD12CDiffDescription);
		final Diff changeRefD22CDiff = Iterators.find(differences.iterator(), changeRefD22CDiffDescription);
		final Diff changeRefE2F1Diff = Iterators.find(differences.iterator(), changeRefE2F1DiffDescription);
		final Diff changeRefE2F2Diff = Iterators.find(differences.iterator(), changeRefE2F2DiffDescription);
		final Diff changeRefF12EDiff = Iterators.find(differences.iterator(), changeRefF12EDiffDescription);
		final Diff changeRefF22EDiff = Iterators.find(differences.iterator(), changeRefF22EDiffDescription);

		assertNotNull(changeRefC2D1Diff);
		assertNotNull(changeRefC2D2Diff);
		assertNotNull(changeRefD12CDiff);
		assertNotNull(changeRefD22CDiff);
		assertNotNull(changeRefE2F1Diff);
		assertNotNull(changeRefE2F2Diff);
		assertNotNull(changeRefF12EDiff);
		assertNotNull(changeRefF22EDiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(4), comparison.getEquivalences().size());

		assertNotNull(changeRefC2D1Diff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefC2D1Diff.getEquivalence().getDifferences().size());
		assertTrue(changeRefC2D1Diff.getEquivalence().getDifferences().contains(changeRefC2D1Diff));
		assertTrue(changeRefC2D1Diff.getEquivalence().getDifferences().contains(changeRefD12CDiff));

		assertNotNull(changeRefC2D2Diff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefC2D2Diff.getEquivalence().getDifferences().size());
		assertTrue(changeRefC2D2Diff.getEquivalence().getDifferences().contains(changeRefC2D2Diff));
		assertTrue(changeRefC2D2Diff.getEquivalence().getDifferences().contains(changeRefD22CDiff));

		assertNotNull(changeRefE2F1Diff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefE2F1Diff.getEquivalence().getDifferences().size());
		assertTrue(changeRefE2F1Diff.getEquivalence().getDifferences().contains(changeRefE2F1Diff));
		assertTrue(changeRefE2F1Diff.getEquivalence().getDifferences().contains(changeRefF12EDiff));

		assertNotNull(changeRefE2F2Diff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefE2F2Diff.getEquivalence().getDifferences().size());
		assertTrue(changeRefE2F2Diff.getEquivalence().getDifferences().contains(changeRefE2F2Diff));
		assertTrue(changeRefE2F2Diff.getEquivalence().getDifferences().contains(changeRefF22EDiff));
	}

	@Test
	public void testA3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA3(TestKind.LEFT, comparison);

	}

	private static void testA3(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 8 differences
		assertSame(Integer.valueOf(8), Integer.valueOf(differences.size()));

		Predicate<? super Diff> C1toD1DiffDescription = null;
		Predicate<? super Diff> C1toD2DiffDescription = null;
		Predicate<? super Diff> C2toD1DiffDescription = null;
		Predicate<? super Diff> C2toD2DiffDescription = null;
		Predicate<? super Diff> D1toC1DiffDescription = null;
		Predicate<? super Diff> D1toC2DiffDescription = null;
		Predicate<? super Diff> D2toC2DiffDescription = null;
		Predicate<? super Diff> D2toC1DiffDescription = null;

		if (kind.equals(TestKind.LEFT)) {
			C1toD1DiffDescription = addedToReference("Requirements.C1", "destination", "Requirements.D1");
			C1toD2DiffDescription = addedToReference("Requirements.C1", "destination", "Requirements.D2");
			C2toD1DiffDescription = addedToReference("Requirements.C2", "destination", "Requirements.D1");
			C2toD2DiffDescription = addedToReference("Requirements.C2", "source", "Requirements.D2");
			D1toC1DiffDescription = addedToReference("Requirements.D1", "source", "Requirements.C1");
			D1toC2DiffDescription = addedToReference("Requirements.D1", "source", "Requirements.C2");
			D2toC2DiffDescription = addedToReference("Requirements.D2", "destination", "Requirements.C2");
			D2toC1DiffDescription = addedToReference("Requirements.D2", "source", "Requirements.C1");
		} else {
			C1toD1DiffDescription = removedFromReference("Requirements.C1", "destination", "Requirements.D1");
			C1toD2DiffDescription = removedFromReference("Requirements.C1", "destination", "Requirements.D2");
			C2toD1DiffDescription = removedFromReference("Requirements.C2", "destination", "Requirements.D1");
			C2toD2DiffDescription = removedFromReference("Requirements.C2", "source", "Requirements.D2");
			D1toC1DiffDescription = removedFromReference("Requirements.D1", "source", "Requirements.C1");
			D1toC2DiffDescription = removedFromReference("Requirements.D1", "source", "Requirements.C2");
			D2toC2DiffDescription = removedFromReference("Requirements.D2", "destination", "Requirements.C2");
			D2toC1DiffDescription = removedFromReference("Requirements.D2", "source", "Requirements.C1");
		}

		final Diff C1toD1Diff = Iterators.find(differences.iterator(), C1toD1DiffDescription);
		final Diff C1toD2Diff = Iterators.find(differences.iterator(), C1toD2DiffDescription);
		final Diff C2toD1Diff = Iterators.find(differences.iterator(), C2toD1DiffDescription);
		final Diff C2toD2Diff = Iterators.find(differences.iterator(), C2toD2DiffDescription);
		final Diff D1toC1Diff = Iterators.find(differences.iterator(), D1toC1DiffDescription);
		final Diff D1toC2Diff = Iterators.find(differences.iterator(), D1toC2DiffDescription);
		final Diff D2toC2Diff = Iterators.find(differences.iterator(), D2toC2DiffDescription);
		final Diff D2toC1Diff = Iterators.find(differences.iterator(), D2toC1DiffDescription);

		assertNotNull(C1toD1Diff);
		assertNotNull(C1toD2Diff);
		assertNotNull(C2toD1Diff);
		assertNotNull(C2toD2Diff);
		assertNotNull(D1toC1Diff);
		assertNotNull(D1toC2Diff);
		assertNotNull(D2toC2Diff);
		assertNotNull(D2toC1Diff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(4), comparison.getEquivalences().size());

		assertNotNull(C1toD1Diff.getEquivalence());
		assertSame(Integer.valueOf(2), C1toD1Diff.getEquivalence().getDifferences().size());
		assertTrue(C1toD1Diff.getEquivalence().getDifferences().contains(C1toD1Diff));
		assertTrue(C1toD1Diff.getEquivalence().getDifferences().contains(D1toC1Diff));

		assertNotNull(C1toD2Diff.getEquivalence());
		assertSame(Integer.valueOf(2), C1toD2Diff.getEquivalence().getDifferences().size());
		assertTrue(C1toD2Diff.getEquivalence().getDifferences().contains(C1toD2Diff));
		assertTrue(C1toD2Diff.getEquivalence().getDifferences().contains(D2toC1Diff));

		assertNotNull(C2toD1Diff.getEquivalence());
		assertSame(Integer.valueOf(2), C2toD1Diff.getEquivalence().getDifferences().size());
		assertTrue(C2toD1Diff.getEquivalence().getDifferences().contains(C2toD1Diff));
		assertTrue(C2toD1Diff.getEquivalence().getDifferences().contains(D1toC2Diff));

		assertNotNull(C2toD2Diff.getEquivalence());
		assertSame(Integer.valueOf(2), C2toD2Diff.getEquivalence().getDifferences().size());
		assertTrue(C2toD2Diff.getEquivalence().getDifferences().contains(C2toD2Diff));
		assertTrue(C2toD2Diff.getEquivalence().getDifferences().contains(D2toC2Diff));
	}

	@Test
	public void testA4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA4(TestKind.LEFT, comparison);
	}

	private static void testA4(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 8 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> AtoBdestDiffDescription = null;
		Predicate<? super Diff> AtoBsourceDiffDescription = null;
		Predicate<? super Diff> BtoAdestDiffDescription = null;
		Predicate<? super Diff> BtoAsourceDiffDescription = null;

		if (kind.equals(TestKind.LEFT)) {
			AtoBdestDiffDescription = addedToReference("Requirements.A", "destination", "Requirements.B");
			AtoBsourceDiffDescription = addedToReference("Requirements.A", "source", "Requirements.B");
			BtoAdestDiffDescription = addedToReference("Requirements.B", "destination", "Requirements.A");
			BtoAsourceDiffDescription = addedToReference("Requirements.B", "source", "Requirements.A");
		} else {
			AtoBdestDiffDescription = removedFromReference("Requirements.A", "destination", "Requirements.B");
			AtoBsourceDiffDescription = removedFromReference("Requirements.A", "source", "Requirements.B");
			BtoAdestDiffDescription = removedFromReference("Requirements.B", "destination", "Requirements.A");
			BtoAsourceDiffDescription = removedFromReference("Requirements.B", "source", "Requirements.A");
		}

		final Diff AtoBdestDiff = Iterators.find(differences.iterator(), AtoBdestDiffDescription);
		final Diff AtoBsourceDiff = Iterators.find(differences.iterator(), AtoBsourceDiffDescription);
		final Diff BtoAdestDiff = Iterators.find(differences.iterator(), BtoAdestDiffDescription);
		final Diff BtoAsourceDiff = Iterators.find(differences.iterator(), BtoAsourceDiffDescription);

		assertNotNull(AtoBdestDiff);
		assertNotNull(AtoBsourceDiff);
		assertNotNull(BtoAdestDiff);
		assertNotNull(BtoAsourceDiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(2), comparison.getEquivalences().size());

		assertNotNull(AtoBdestDiff.getEquivalence());
		assertSame(Integer.valueOf(2), AtoBdestDiff.getEquivalence().getDifferences().size());
		assertTrue(AtoBdestDiff.getEquivalence().getDifferences().contains(AtoBdestDiff));
		assertTrue(AtoBdestDiff.getEquivalence().getDifferences().contains(BtoAsourceDiff));

		assertNotNull(AtoBsourceDiff.getEquivalence());
		assertSame(Integer.valueOf(2), AtoBsourceDiff.getEquivalence().getDifferences().size());
		assertTrue(AtoBsourceDiff.getEquivalence().getDifferences().contains(AtoBsourceDiff));
		assertTrue(AtoBsourceDiff.getEquivalence().getDifferences().contains(BtoAdestDiff));
	}

	@Test
	public void testA5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA5(TestKind.LEFT, comparison);
	}

	private static void testA5(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 12 differences
		assertSame(Integer.valueOf(12), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefA2BDiffDescription = null;
		Predicate<? super Diff> changeRefB2ADiffDescription = null;
		Predicate<? super Diff> changeRefC2DDiffDescription = null;
		Predicate<? super Diff> changeRefD2CDiffDescription = null;
		Predicate<? super Diff> changeRefE2FDiffDescription = null;
		Predicate<? super Diff> changeRefF2EDiffDescription = null;
		Predicate<? super Diff> addADiffDescription = null;
		Predicate<? super Diff> addBDiffDescription = null;
		Predicate<? super Diff> addCDiffDescription = null;
		Predicate<? super Diff> addDDiffDescription = null;
		Predicate<? super Diff> addEDiffDescription = null;
		Predicate<? super Diff> addFDiffDescription = null;

		if (kind.equals(TestKind.LEFT)) {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", null,
					"Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", null, "Requirements.A");
			changeRefC2DDiffDescription = addedToReference("Requirements.C", "destination", "Requirements.D");
			changeRefD2CDiffDescription = changedReference("Requirements.D", "source", null, "Requirements.C");
			changeRefE2FDiffDescription = addedToReference("Requirements.E", "destination", "Requirements.F");
			changeRefF2EDiffDescription = addedToReference("Requirements.F", "source", "Requirements.E");

			addADiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.A");
			addBDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.B");
			addCDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.C");
			addDDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.D");
			addEDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.E");
			addFDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.F");
		} else {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", "Requirements.B",
					null);
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", "Requirements.A", null);
			changeRefC2DDiffDescription = removedFromReference("Requirements.C", "destination",
					"Requirements.D");
			changeRefD2CDiffDescription = changedReference("Requirements.D", "source", "Requirements.C", null);
			changeRefE2FDiffDescription = removedFromReference("Requirements.E", "destination",
					"Requirements.F");
			changeRefF2EDiffDescription = removedFromReference("Requirements.F", "source", "Requirements.E");

			addADiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.A");
			addBDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.B");
			addCDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.C");
			addDDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.D");
			addEDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.E");
			addFDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.F");
		}

		final Diff changeRefA2BDiff = Iterators.find(differences.iterator(), changeRefA2BDiffDescription);
		final Diff changeRefB2ADiff = Iterators.find(differences.iterator(), changeRefB2ADiffDescription);
		final Diff changeRefC2DDiff = Iterators.find(differences.iterator(), changeRefC2DDiffDescription);
		final Diff changeRefD2CDiff = Iterators.find(differences.iterator(), changeRefD2CDiffDescription);
		final Diff changeRefE2FDiff = Iterators.find(differences.iterator(), changeRefE2FDiffDescription);
		final Diff changeRefF2EDiff = Iterators.find(differences.iterator(), changeRefF2EDiffDescription);

		final Diff addADiff = Iterators.find(differences.iterator(), addADiffDescription);
		final Diff addBDiff = Iterators.find(differences.iterator(), addBDiffDescription);
		final Diff addCDiff = Iterators.find(differences.iterator(), addCDiffDescription);
		final Diff addDDiff = Iterators.find(differences.iterator(), addDDiffDescription);
		final Diff addEDiff = Iterators.find(differences.iterator(), addEDiffDescription);
		final Diff addFDiff = Iterators.find(differences.iterator(), addFDiffDescription);

		assertNotNull(changeRefA2BDiff);
		assertNotNull(changeRefB2ADiff);
		assertNotNull(changeRefC2DDiff);
		assertNotNull(changeRefD2CDiff);
		assertNotNull(changeRefE2FDiff);
		assertNotNull(changeRefF2EDiff);

		assertNotNull(addADiff);
		assertNotNull(addBDiff);
		assertNotNull(addCDiff);
		assertNotNull(addDDiff);
		assertNotNull(addEDiff);
		assertNotNull(addFDiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(3), comparison.getEquivalences().size());

		assertNotNull(changeRefA2BDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefA2BDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefA2BDiff));
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefB2ADiff));

		assertNotNull(changeRefC2DDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefC2DDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefC2DDiff.getEquivalence().getDifferences().contains(changeRefC2DDiff));
		assertTrue(changeRefC2DDiff.getEquivalence().getDifferences().contains(changeRefD2CDiff));

		assertNotNull(changeRefE2FDiff.getEquivalence());
		assertSame(Integer.valueOf(2), changeRefE2FDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefE2FDiff.getEquivalence().getDifferences().contains(changeRefE2FDiff));
		assertTrue(changeRefE2FDiff.getEquivalence().getDifferences().contains(changeRefF2EDiff));
	}

	@Test
	public void testB1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA1(TestKind.RIGHT, comparison);
	}

	@Test
	public void testB2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA2(TestKind.RIGHT, comparison);
	}

	@Test
	public void testB3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA3(TestKind.RIGHT, comparison);
	}

	@Test
	public void testB4UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA4(TestKind.RIGHT, comparison);
	}

	@Test
	public void testB5UseCase() throws IOException {
		final Resource left = input.getA5Left();
		final Resource right = input.getA5Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA5(TestKind.RIGHT, comparison);
	}

	@Test
	public void testC1UseCase() throws IOException {
		final Resource left = input.getC1Left();
		final Resource right = input.getC1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC1(TestKind.LEFT, comparison);
	}

	private static void testC1(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 3 differences
		assertSame(Integer.valueOf(3), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefA2BDiffDescription = null;
		Predicate<? super Diff> changeRefB2ADiffDescription = null;
		Predicate<? super Diff> changeRefC2ADiffDescription = null;
		if (kind.equals(TestKind.LEFT)) {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", "Requirements.B",
					"Requirements.C");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", "Requirements.A", null);
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", null, "Requirements.A");
		} else {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", "Requirements.C",
					"Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", null, "Requirements.A");
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", "Requirements.A", null);
		}

		final Diff changeRefA2BDiff = Iterators.find(differences.iterator(), changeRefA2BDiffDescription);
		final Diff changeRefB2ADiff = Iterators.find(differences.iterator(), changeRefB2ADiffDescription);
		final Diff changeRefC2ADiff = Iterators.find(differences.iterator(), changeRefC2ADiffDescription);

		assertNotNull(changeRefA2BDiff);
		assertNotNull(changeRefB2ADiff);
		assertNotNull(changeRefC2ADiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), comparison.getEquivalences().size());

		assertNotNull(changeRefA2BDiff.getEquivalence());
		assertSame(Integer.valueOf(3), changeRefA2BDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefA2BDiff));
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefB2ADiff));
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefC2ADiff));

	}

	@Test
	public void testC2UseCase() throws IOException {
		final Resource left = input.getC2Left();
		final Resource right = input.getC2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC2(TestKind.LEFT, comparison);
	}

	private static void testC2(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefA2BDiffDescription = null;
		Predicate<? super Diff> changeRefB2ADiffDescription = null;
		Predicate<? super Diff> changeRefC2ADiffDescription = null;
		Predicate<? super Diff> addCDiffDescription = null;
		if (kind.equals(TestKind.LEFT)) {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", "Requirements.B",
					"Requirements.C");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", "Requirements.A", null);
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", null, "Requirements.A");
			addCDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.C");
		} else {
			changeRefA2BDiffDescription = changedReference("Requirements.A", "destination", "Requirements.C",
					"Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", null, "Requirements.A");
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", "Requirements.A", null);
			addCDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.C");
		}

		final Diff changeRefA2BDiff = Iterators.find(differences.iterator(), changeRefA2BDiffDescription);
		final Diff changeRefB2ADiff = Iterators.find(differences.iterator(), changeRefB2ADiffDescription);
		final Diff changeRefC2ADiff = Iterators.find(differences.iterator(), changeRefC2ADiffDescription);
		final Diff addCDiff = Iterators.find(differences.iterator(), addCDiffDescription);

		assertNotNull(changeRefA2BDiff);
		assertNotNull(changeRefB2ADiff);
		assertNotNull(changeRefC2ADiff);
		assertNotNull(addCDiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), comparison.getEquivalences().size());

		assertNotNull(changeRefA2BDiff.getEquivalence());
		assertSame(Integer.valueOf(3), changeRefA2BDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefA2BDiff));
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefB2ADiff));
		assertTrue(changeRefA2BDiff.getEquivalence().getDifferences().contains(changeRefC2ADiff));

	}

	@Test
	public void testC3UseCase() throws IOException {
		final Resource left = input.getC3Left();
		final Resource right = input.getC3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC3(TestKind.LEFT, comparison);
	}

	private static void testC3(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 4 differences
		assertSame(Integer.valueOf(4), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefA2CDiffDescription = null;
		Predicate<? super Diff> deleteBDiffDescription = null;
		Predicate<? super Diff> changeRefC2ADiffDescription = null;
		Predicate<? super Diff> changeRefB2ADiffDescription = null;
		if (kind.equals(TestKind.LEFT)) {
			changeRefA2CDiffDescription = changedReference("Requirements.A", "destination", "Requirements.B",
					"Requirements.C");
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", null, "Requirements.A");
			deleteBDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", "Requirements.A", null);
		} else {
			changeRefA2CDiffDescription = changedReference("Requirements.A", "destination", "Requirements.C",
					"Requirements.B");
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", "Requirements.A", null);
			deleteBDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", null, "Requirements.A");
		}

		final Diff changeRefA2CDiff = Iterators.find(differences.iterator(), changeRefA2CDiffDescription);
		final Diff changeRefC2ADiff = Iterators.find(differences.iterator(), changeRefC2ADiffDescription);
		final Diff deleteBDiff = Iterators.find(differences.iterator(), deleteBDiffDescription);
		final Diff changeRefB2ADiff = Iterators.find(differences.iterator(), changeRefB2ADiffDescription);

		assertNotNull(changeRefA2CDiff);
		assertNotNull(changeRefC2ADiff);
		assertNotNull(deleteBDiff);
		assertNotNull(changeRefB2ADiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), comparison.getEquivalences().size());

		assertNotNull(changeRefA2CDiff.getEquivalence());
		assertSame(Integer.valueOf(3), changeRefA2CDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefA2CDiff.getEquivalence().getDifferences().contains(changeRefA2CDiff));
		assertTrue(changeRefA2CDiff.getEquivalence().getDifferences().contains(changeRefC2ADiff));
		assertTrue(changeRefA2CDiff.getEquivalence().getDifferences().contains(changeRefB2ADiff));

	}

	@Test
	public void testC4UseCase() throws IOException {
		final Resource left = input.getC4Left();
		final Resource right = input.getC4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC4(TestKind.LEFT, comparison);
	}

	private static void testC4(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 5 differences
		assertSame(Integer.valueOf(5), Integer.valueOf(differences.size()));

		Predicate<? super Diff> changeRefA2CDiffDescription = null;
		Predicate<? super Diff> deleteBDiffDescription = null;
		Predicate<? super Diff> changeRefC2ADiffDescription = null;
		Predicate<? super Diff> changeRefB2ADiffDescription = null;
		Predicate<? super Diff> addCDiffDescription = null;
		if (kind.equals(TestKind.LEFT)) {
			changeRefA2CDiffDescription = changedReference("Requirements.A", "destination", "Requirements.B",
					"Requirements.C");
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", null, "Requirements.A");
			deleteBDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", "Requirements.A", null);
			addCDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.C");
		} else {
			changeRefA2CDiffDescription = changedReference("Requirements.A", "destination", "Requirements.C",
					"Requirements.B");
			changeRefC2ADiffDescription = changedReference("Requirements.C", "source", "Requirements.A", null);
			deleteBDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.B");
			changeRefB2ADiffDescription = changedReference("Requirements.B", "source", null, "Requirements.A");
			addCDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.C");
		}

		final Diff changeRefA2CDiff = Iterators.find(differences.iterator(), changeRefA2CDiffDescription);
		final Diff changeRefC2ADiff = Iterators.find(differences.iterator(), changeRefC2ADiffDescription);
		final Diff deleteBDiff = Iterators.find(differences.iterator(), deleteBDiffDescription);
		final Diff changeRefB2ADiff = Iterators.find(differences.iterator(), changeRefB2ADiffDescription);
		final Diff addCDiff = Iterators.find(differences.iterator(), addCDiffDescription);

		assertNotNull(changeRefA2CDiff);
		assertNotNull(changeRefC2ADiff);
		assertNotNull(deleteBDiff);
		assertNotNull(changeRefB2ADiff);
		assertNotNull(addCDiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), comparison.getEquivalences().size());

		assertNotNull(changeRefA2CDiff.getEquivalence());
		assertSame(Integer.valueOf(3), changeRefA2CDiff.getEquivalence().getDifferences().size());
		assertTrue(changeRefA2CDiff.getEquivalence().getDifferences().contains(changeRefA2CDiff));
		assertTrue(changeRefA2CDiff.getEquivalence().getDifferences().contains(changeRefC2ADiff));
		assertTrue(changeRefA2CDiff.getEquivalence().getDifferences().contains(changeRefB2ADiff));

	}

	@Test
	public void testD1UseCase() throws IOException {
		final Resource left = input.getD1Left();
		final Resource right = input.getD1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = EMFCompare.newComparator(scope).compare();

		testD1(TestKind.LEFT, comparison);
	}

	private static void testD1(final TestKind kind, final Comparison comparison) {
		List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 16 differences
		assertSame(Integer.valueOf(16), Integer.valueOf(differences.size()));

		Predicate<? super Diff> delBonADiffDescription = null;
		Predicate<? super Diff> delAonBDiffDescription = null;
		Predicate<? super Diff> addDonADiffDescription = null;
		Predicate<? super Diff> addEonADiffDescription = null;
		Predicate<? super Diff> delFonBDiffDescription = null;
		Predicate<? super Diff> addFonCDiffDescription = null;
		Predicate<? super Diff> addAonDDiffDescription = null;
		Predicate<? super Diff> delFonDDiffDescription = null;
		Predicate<? super Diff> addAonEDiffDescription = null;
		Predicate<? super Diff> addFonEDiffDescription = null;
		Predicate<? super Diff> delDonFDiffDescription = null;
		Predicate<? super Diff> delBonFDiffDescription = null;
		Predicate<? super Diff> addEonFDiffDescription = null;
		Predicate<? super Diff> addConFDiffDescription = null;
		Predicate<? super Diff> delBDiffDescription = null;
		Predicate<? super Diff> addEDiffDescription = null;
		if (kind.equals(TestKind.LEFT)) {
			delBonADiffDescription = removedFromReference("Requirements.A", "destination", "Requirements.B");
			delAonBDiffDescription = removedFromReference("Requirements.B", "source", "Requirements.A");
			addDonADiffDescription = addedToReference("Requirements.A", "destination", "Requirements.D");
			addEonADiffDescription = addedToReference("Requirements.A", "destination", "Requirements.E");
			delFonBDiffDescription = removedFromReference("Requirements.B", "source", "Requirements.F");
			addFonCDiffDescription = addedToReference("Requirements.C", "source", "Requirements.F");
			addAonDDiffDescription = addedToReference("Requirements.D", "source", "Requirements.A");
			delFonDDiffDescription = removedFromReference("Requirements.D", "source", "Requirements.F");
			addAonEDiffDescription = addedToReference("Requirements.E", "source", "Requirements.A");
			addFonEDiffDescription = addedToReference("Requirements.E", "source", "Requirements.F");
			delDonFDiffDescription = removedFromReference("Requirements.F", "destination", "Requirements.D");
			delBonFDiffDescription = removedFromReference("Requirements.F", "destination", "Requirements.B");
			addEonFDiffDescription = addedToReference("Requirements.F", "destination", "Requirements.E");
			addConFDiffDescription = addedToReference("Requirements.F", "destination", "Requirements.C");
			delBDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.B");
			addEDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.E");
		} else {
			delBonADiffDescription = addedToReference("Requirements.A", "destination", "Requirements.B");
			delAonBDiffDescription = addedToReference("Requirements.B", "source", "Requirements.A");
			addDonADiffDescription = removedFromReference("Requirements.A", "destination", "Requirements.D");
			addEonADiffDescription = removedFromReference("Requirements.A", "destination", "Requirements.E");
			delFonBDiffDescription = addedToReference("Requirements.B", "source", "Requirements.F");
			addFonCDiffDescription = removedFromReference("Requirements.C", "source", "Requirements.F");
			addAonDDiffDescription = removedFromReference("Requirements.D", "source", "Requirements.A");
			delFonDDiffDescription = addedToReference("Requirements.D", "source", "Requirements.F");
			addAonEDiffDescription = removedFromReference("Requirements.E", "source", "Requirements.A");
			addFonEDiffDescription = removedFromReference("Requirements.E", "source", "Requirements.F");
			delDonFDiffDescription = addedToReference("Requirements.F", "destination", "Requirements.D");
			delBonFDiffDescription = addedToReference("Requirements.F", "destination", "Requirements.B");
			addEonFDiffDescription = removedFromReference("Requirements.F", "destination", "Requirements.E");
			addConFDiffDescription = removedFromReference("Requirements.F", "destination", "Requirements.C");
			delBDiffDescription = addedToReference("Requirements", "containmentRef1", "Requirements.B");
			addEDiffDescription = removedFromReference("Requirements", "containmentRef1", "Requirements.E");
		}

		final Diff delBonADiff = Iterators.find(differences.iterator(), delBonADiffDescription);
		final Diff delAonBDiff = Iterators.find(differences.iterator(), delAonBDiffDescription);
		final Diff addDonADiff = Iterators.find(differences.iterator(), addDonADiffDescription);
		final Diff addEonADiff = Iterators.find(differences.iterator(), addEonADiffDescription);
		final Diff delFonBDiff = Iterators.find(differences.iterator(), delFonBDiffDescription);
		final Diff addFonCDiff = Iterators.find(differences.iterator(), addFonCDiffDescription);
		final Diff addAonDDiff = Iterators.find(differences.iterator(), addAonDDiffDescription);
		final Diff delFonDDiff = Iterators.find(differences.iterator(), delFonDDiffDescription);
		final Diff addAonEDiff = Iterators.find(differences.iterator(), addAonEDiffDescription);
		final Diff addFonEDiff = Iterators.find(differences.iterator(), addFonEDiffDescription);
		final Diff delDonFDiff = Iterators.find(differences.iterator(), delDonFDiffDescription);
		final Diff delBonFDiff = Iterators.find(differences.iterator(), delBonFDiffDescription);
		final Diff addEonFDiff = Iterators.find(differences.iterator(), addEonFDiffDescription);
		final Diff addConFDiff = Iterators.find(differences.iterator(), addConFDiffDescription);
		final Diff delBDiff = Iterators.find(differences.iterator(), delBDiffDescription);
		final Diff addEDiff = Iterators.find(differences.iterator(), addEDiffDescription);

		assertNotNull(delBonADiff);
		assertNotNull(delAonBDiff);
		assertNotNull(addDonADiff);
		assertNotNull(addEonADiff);
		assertNotNull(delFonBDiff);
		assertNotNull(addFonCDiff);
		assertNotNull(addAonDDiff);
		assertNotNull(delFonDDiff);
		assertNotNull(addAonEDiff);
		assertNotNull(addFonEDiff);
		assertNotNull(delDonFDiff);
		assertNotNull(delBonFDiff);
		assertNotNull(addEonFDiff);
		assertNotNull(addConFDiff);
		assertNotNull(delBDiff);
		assertNotNull(addEDiff);

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(7), comparison.getEquivalences().size());

		assertNotNull(delBonADiff.getEquivalence());
		assertSame(Integer.valueOf(2), delBonADiff.getEquivalence().getDifferences().size());
		assertTrue(delBonADiff.getEquivalence().getDifferences().contains(delBonADiff));
		assertTrue(delBonADiff.getEquivalence().getDifferences().contains(delAonBDiff));

		assertNotNull(addDonADiff.getEquivalence());
		assertSame(Integer.valueOf(2), addDonADiff.getEquivalence().getDifferences().size());
		assertTrue(addDonADiff.getEquivalence().getDifferences().contains(addDonADiff));
		assertTrue(addDonADiff.getEquivalence().getDifferences().contains(addAonDDiff));

		assertNotNull(addEonADiff.getEquivalence());
		assertSame(Integer.valueOf(2), addEonADiff.getEquivalence().getDifferences().size());
		assertTrue(addEonADiff.getEquivalence().getDifferences().contains(addEonADiff));
		assertTrue(addEonADiff.getEquivalence().getDifferences().contains(addAonEDiff));

		assertNotNull(delFonBDiff.getEquivalence());
		assertSame(Integer.valueOf(2), delFonBDiff.getEquivalence().getDifferences().size());
		assertTrue(delFonBDiff.getEquivalence().getDifferences().contains(delFonBDiff));
		assertTrue(delFonBDiff.getEquivalence().getDifferences().contains(delBonFDiff));

		assertNotNull(addFonCDiff.getEquivalence());
		assertSame(Integer.valueOf(2), addFonCDiff.getEquivalence().getDifferences().size());
		assertTrue(addFonCDiff.getEquivalence().getDifferences().contains(addFonCDiff));
		assertTrue(addFonCDiff.getEquivalence().getDifferences().contains(addConFDiff));

		assertNotNull(delFonDDiff.getEquivalence());
		assertSame(Integer.valueOf(2), delFonDDiff.getEquivalence().getDifferences().size());
		assertTrue(delFonDDiff.getEquivalence().getDifferences().contains(delFonDDiff));
		assertTrue(delFonDDiff.getEquivalence().getDifferences().contains(delDonFDiff));

		assertNotNull(addFonEDiff.getEquivalence());
		assertSame(Integer.valueOf(2), addFonEDiff.getEquivalence().getDifferences().size());
		assertTrue(addFonEDiff.getEquivalence().getDifferences().contains(addFonEDiff));
		assertTrue(addFonEDiff.getEquivalence().getDifferences().contains(addEonFDiff));

	}

	@Test
	public void testE1UseCase() throws IOException {
		final Resource left = input.getE1Left();
		final Resource right = input.getE1Right();
		final Resource ancestor = input.getE1Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA1(TestKind.LEFT, comparison);
	}

	@Test
	public void testE2UseCase() throws IOException {
		final Resource left = input.getE2Left();
		final Resource right = input.getE2Right();
		final Resource ancestor = input.getE2Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA2(TestKind.LEFT, comparison);
	}

	@Test
	public void testE3UseCase() throws IOException {
		final Resource left = input.getE3Left();
		final Resource right = input.getE3Right();
		final Resource ancestor = input.getE3Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA3(TestKind.LEFT, comparison);
	}

	@Test
	public void testE4UseCase() throws IOException {
		final Resource left = input.getE4Left();
		final Resource right = input.getE4Right();
		final Resource ancestor = input.getE4Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA4(TestKind.LEFT, comparison);
	}

	@Test
	public void testE5UseCase() throws IOException {
		final Resource left = input.getE5Left();
		final Resource right = input.getE5Right();
		final Resource ancestor = input.getE5Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA5(TestKind.LEFT, comparison);
	}

	@Test
	public void testE6UseCase() throws IOException {
		final Resource left = input.getE6Left();
		final Resource right = input.getE6Right();
		final Resource ancestor = input.getE6Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC1(TestKind.LEFT, comparison);
	}

	@Test
	public void testE7UseCase() throws IOException {
		final Resource left = input.getE7Left();
		final Resource right = input.getE7Right();
		final Resource ancestor = input.getE7Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC2(TestKind.LEFT, comparison);
	}

	@Test
	public void testE8UseCase() throws IOException {
		final Resource left = input.getE8Left();
		final Resource right = input.getE8Right();
		final Resource ancestor = input.getE8Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC3(TestKind.LEFT, comparison);
	}

	@Test
	public void testE9UseCase() throws IOException {
		final Resource left = input.getE9Left();
		final Resource right = input.getE9Right();
		final Resource ancestor = input.getE9Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC4(TestKind.LEFT, comparison);
	}

	@Test
	public void testE10UseCase() throws IOException {
		final Resource left = input.getE10Left();
		final Resource right = input.getE10Right();
		final Resource ancestor = input.getE10Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testD1(TestKind.LEFT, comparison);
	}

	@Test
	public void testF1UseCase() throws IOException {
		final Resource left = input.getF1Left();
		final Resource right = input.getF1Right();
		final Resource ancestor = input.getF1Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA1(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF2UseCase() throws IOException {
		final Resource left = input.getF2Left();
		final Resource right = input.getF2Right();
		final Resource ancestor = input.getF2Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA2(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF3UseCase() throws IOException {
		final Resource left = input.getF3Left();
		final Resource right = input.getF3Right();
		final Resource ancestor = input.getF3Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA3(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF4UseCase() throws IOException {
		final Resource left = input.getF4Left();
		final Resource right = input.getF4Right();
		final Resource ancestor = input.getF4Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA4(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF5UseCase() throws IOException {
		final Resource left = input.getF5Left();
		final Resource right = input.getF5Right();
		final Resource ancestor = input.getF5Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testA5(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF6UseCase() throws IOException {
		final Resource left = input.getF6Left();
		final Resource right = input.getF6Right();
		final Resource ancestor = input.getF6Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC1(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF7UseCase() throws IOException {
		final Resource left = input.getF7Left();
		final Resource right = input.getF7Right();
		final Resource ancestor = input.getF7Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC2(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF8UseCase() throws IOException {
		final Resource left = input.getF8Left();
		final Resource right = input.getF8Right();
		final Resource ancestor = input.getF8Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC3(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF9UseCase() throws IOException {
		final Resource left = input.getF9Left();
		final Resource right = input.getF9Right();
		final Resource ancestor = input.getF9Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testC4(TestKind.RIGHT, comparison);
	}

	@Test
	public void testF10UseCase() throws IOException {
		final Resource left = input.getF10Left();
		final Resource right = input.getF10Right();
		final Resource ancestor = input.getF10Ancestor();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, ancestor);
		Comparison comparison = EMFCompare.newComparator(scope).compare();

		testD1(TestKind.RIGHT, comparison);
	}

}
