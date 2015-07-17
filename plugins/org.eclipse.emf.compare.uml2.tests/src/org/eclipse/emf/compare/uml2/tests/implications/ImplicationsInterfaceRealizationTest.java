/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.implications;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.implications.data.ImplicationsInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class ImplicationsInterfaceRealizationTest extends AbstractUMLTest {

	private static final int NB_DIFFS = 9;

	private ImplicationsInputData input = new ImplicationsInputData();

	@BeforeClass
	public static void setupClass() {
		fillRegistries();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistries();
	}

	private DiffsOfInterest getDiffs(Comparison comparison, TestKind kind) {
		final List<Diff> differences = comparison.getDifferences();

		Predicate<? super Diff> addInterfaceRealizationDescription = null;
		Predicate<? super Diff> addClientInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addSupplierInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addContractInInterfaceRealizationDescription = null;
		Predicate<? super Diff> addSubstitutionDescription = null;
		Predicate<? super Diff> addClientInSubstitutionDescription = null;
		Predicate<? super Diff> addSupplierInSubstitutionDescription = null;
		Predicate<? super Diff> addUMLInterfaceRealizationDescription = null;
		Predicate<? super Diff> addUMLSubstitutionDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addInterfaceRealizationDescription = removedFromReference("model.Class0", "interfaceRealization",
					"model.Class0.InterfaceRealization0");
			addClientInInterfaceRealizationDescription = removedFromReference(
					"model.Class0.InterfaceRealization0", "client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = removedFromReference(
					"model.Class0.InterfaceRealization0", "supplier", "model.Interface0");
			addContractInInterfaceRealizationDescription = changedReference(
					"model.Class0.InterfaceRealization0", "contract", "model.Interface0", null);
			addSubstitutionDescription = removedFromReference("model.Class0", "substitution",
					"model.Class0.substitution1");
			addClientInSubstitutionDescription = removedFromReference("model.Class0.substitution1", "client",
					"model.Class0");
			addSupplierInSubstitutionDescription = removedFromReference("model.Class0.substitution1",
					"supplier", "model.Interface0");
		} else {
			addInterfaceRealizationDescription = addedToReference(
					"model.Class0", "interfaceRealization", "model.Class0.InterfaceRealization0"); //$NON-NLS-1$
			addClientInInterfaceRealizationDescription = addedToReference(
					"model.Class0.InterfaceRealization0", "client", "model.Class0");
			addSupplierInInterfaceRealizationDescription = addedToReference(
					"model.Class0.InterfaceRealization0", "supplier", "model.Interface0");
			addContractInInterfaceRealizationDescription = changedReference(
					"model.Class0.InterfaceRealization0", "contract", null, "model.Interface0");
			addSubstitutionDescription = addedToReference("model.Class0", "substitution",
					"model.Class0.substitution1");
			addClientInSubstitutionDescription = addedToReference("model.Class0.substitution1", "client",
					"model.Class0");
			addSupplierInSubstitutionDescription = addedToReference("model.Class0.substitution1", "supplier",
					"model.Interface0");
		}

		DiffsOfInterest diffs = new DiffsOfInterest();
		diffs.addInterfaceRealization = Iterators.find(differences.iterator(),
				addInterfaceRealizationDescription, null);
		diffs.addClientInInterfaceRealization = Iterators.find(differences.iterator(),
				addClientInInterfaceRealizationDescription, null);
		diffs.addSupplierInInterfaceRealization = Iterators.find(differences.iterator(),
				addSupplierInInterfaceRealizationDescription, null);
		diffs.addContractInInterfaceRealization = Iterators.find(differences.iterator(),
				addContractInInterfaceRealizationDescription, null);
		diffs.addSubstitution = Iterators.find(differences.iterator(), addSubstitutionDescription, null);
		diffs.addClientInSubstitution = Iterators.find(differences.iterator(),
				addClientInSubstitutionDescription, null);
		diffs.addSupplierInSubstitution = Iterators.find(differences.iterator(),
				addSupplierInSubstitutionDescription, null);
		diffs.addUMLInterfaceRealization = Iterators.find(differences.iterator(), and(
				instanceOf(DirectedRelationshipChange.class),
				discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION)), null);
		diffs.addUMLSubstitution = Iterators.find(differences.iterator(), and(
				instanceOf(DirectedRelationshipChange.class),
				discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION)), null);
		return diffs;
	}

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	// local ADD
	public void testA10UseCase3way1() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	// remote ADD
	public void testA10UseCase3way2() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, left);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA10MergeLtR1UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyLeftToRight(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddClientInInterfaceRealization(comparison, diffs);
	}

	private void checkMergeAddClientInInterfaceRealization(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 3, comparison.getDifferences().size());
		assertNull(diffs.addClientInInterfaceRealization);
		assertNull(diffs.addInterfaceRealization);
		assertNull(diffs.addUMLInterfaceRealization);
	}

	@Test
	public void testA10MergeLtR1UseCase3way1() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyLeftToRight(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR1UseCase3way2() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyLeftToRight(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteClientInInterfaceRealization(comparison, diffs);
	}

	private void checkMergeDeleteClientInInterfaceRealization(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 5, comparison.getDifferences().size());
		assertNull(diffs.addClientInInterfaceRealization);
		assertNull(diffs.addInterfaceRealization);
		assertNull(diffs.addSupplierInInterfaceRealization);
		assertNull(diffs.addContractInInterfaceRealization);
		assertNull(diffs.addUMLInterfaceRealization);
	}

	@Test
	public void testA10MergeLtR3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyLeftToRight(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddInterfaceRealization(comparison, diffs);
	}

	private void checkMergeAddInterfaceRealization(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 3, comparison.getDifferences().size());
		assertNull(diffs.addInterfaceRealization);
		assertNull(diffs.addClientInInterfaceRealization);
		assertNull(diffs.addUMLInterfaceRealization);
	}

	@Test
	public void testA10MergeLtR3UseCase3way1() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyLeftToRight(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR3UseCase3way2() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyLeftToRight(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteInterfaceRealization(comparison, diffs);
	}

	private void checkMergeDeleteInterfaceRealization(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 5, comparison.getDifferences().size());
		assertNull(diffs.addInterfaceRealization);
		assertNull(diffs.addClientInInterfaceRealization);
		assertNull(diffs.addSupplierInInterfaceRealization);
		assertNull(diffs.addContractInInterfaceRealization);
		assertNull(diffs.addUMLInterfaceRealization);
	}

	@Test
	public void testA10MergeRtL1UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyRightToLeft(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL1UseCase3way1() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyRightToLeft(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL1UseCase3way2() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyRightToLeft(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyRightToLeft(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase3way1() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyRightToLeft(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase3way2() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyRightToLeft(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	// local DELETE
	public void testA11UseCase3way1() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	// remote DELETE
	public void testA11UseCase3way2() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA11MergeLtR1UseCase() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyLeftToRight(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR1UseCase3way1() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyLeftToRight(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR1UseCase3way2() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyLeftToRight(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyLeftToRight(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase3way1() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyLeftToRight(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase3way2() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyLeftToRight(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyRightToLeft(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase3way1() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyRightToLeft(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase3way2() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addClientInInterfaceRealization).copyRightToLeft(
				diffs.addClientInInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteClientInInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyRightToLeft(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase3way1() throws IOException {
		final Resource left = input.getA3Right();
		final Resource right = input.getA3Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyRightToLeft(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddInterfaceRealization(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase3way2() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addInterfaceRealization).copyRightToLeft(
				diffs.addInterfaceRealization, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteInterfaceRealization(comparison, diffs);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		assertEquals(NB_DIFFS, differences.size());

		DiffsOfInterest diffs = getDiffs(comparison, kind);

		if (kind.equals(TestKind.DELETE)) {
			assertEquals(1, diffs.addInterfaceRealization.getImpliedBy().size());
			assertTrue(diffs.addInterfaceRealization.getImpliedBy().contains(
					diffs.addClientInInterfaceRealization));
			assertEquals(1, diffs.addSubstitution.getImpliedBy().size());
			assertTrue(diffs.addSubstitution.getImpliedBy().contains(diffs.addClientInSubstitution));
		} else {
			assertEquals(1, diffs.addInterfaceRealization.getImplies().size());
			assertTrue(diffs.addInterfaceRealization.getImplies().contains(
					diffs.addClientInInterfaceRealization));
			assertEquals(1, diffs.addSubstitution.getImplies().size());
			assertTrue(diffs.addSubstitution.getImplies().contains(diffs.addClientInSubstitution));
		}
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	private class DiffsOfInterest {
		public Diff addInterfaceRealization;

		public Diff addClientInInterfaceRealization;

		public Diff addSupplierInInterfaceRealization;

		public Diff addContractInInterfaceRealization;

		public Diff addSubstitution;

		public Diff addClientInSubstitution;

		public Diff addSupplierInSubstitution;

		public Diff addUMLInterfaceRealization;

		public Diff addUMLSubstitution;
	}
}
