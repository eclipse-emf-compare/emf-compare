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

import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
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
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.implications.data.ImplicationsInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class ImplicationsTransitionTest extends AbstractUMLTest {

	private static final int NB_DIFFS = 6;

	private ImplicationsInputData input = new ImplicationsInputData();

	private DiffsOfInterest getDiffs(Comparison comparison, TestKind kind) {
		final List<Diff> differences = comparison.getDifferences();
		Predicate<? super Diff> addTransitionDescription = null;
		Predicate<? super Diff> addPreConditionDescription = null;
		Predicate<? super Diff> addOwnedRuleDescription = null;
		Predicate<? super Diff> addGuardDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addTransitionDescription = removed("model.StateMachine0.Region0.transition1"); //$NON-NLS-1$
			addPreConditionDescription = changedReference("model.StateMachine0.Region0.transition1",
					"preCondition", "model.StateMachine0.Region0.transition1.rule1", null);
			addOwnedRuleDescription = removedFromReference("model.StateMachine0.Region0.transition1",
					"ownedRule", "model.StateMachine0.Region0.transition1.rule1");
			addGuardDescription = changedReference("model.StateMachine0.Region0.transition1", "guard",
					"model.StateMachine0.Region0.transition1.rule1", null);
		} else {
			addTransitionDescription = added("model.StateMachine0.Region0.transition1"); //$NON-NLS-1$
			addPreConditionDescription = changedReference("model.StateMachine0.Region0.transition1",
					"preCondition", null, "model.StateMachine0.Region0.transition1.rule1");
			addOwnedRuleDescription = addedToReference("model.StateMachine0.Region0.transition1",
					"ownedRule", "model.StateMachine0.Region0.transition1.rule1");
			addGuardDescription = changedReference("model.StateMachine0.Region0.transition1", "guard", null,
					"model.StateMachine0.Region0.transition1.rule1");
		}

		DiffsOfInterest diffs = new DiffsOfInterest();
		diffs.addTransition = Iterators.find(differences.iterator(), addTransitionDescription, null);
		diffs.addPrecondition = Iterators.find(differences.iterator(), addPreConditionDescription, null);
		diffs.addOwnedRule = Iterators.find(differences.iterator(), addOwnedRuleDescription, null);
		diffs.addGuard = Iterators.find(differences.iterator(), addGuardDescription, null);

		return diffs;
	}

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	// local ADD
	public void testA10UseCase3way1() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	// remote ADD
	public void testA10UseCase3way2() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, left);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA10MergeLtR1UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyLeftToRight(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddPrecondition(comparison, diffs);
	}

	private void checkMergeAddPrecondition(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 4, comparison.getDifferences().size());
		assertNull(diffs.addPrecondition);
		assertNull(diffs.addGuard);
		assertNull(diffs.addOwnedRule);
		assertNull(diffs.addTransition);
	}

	@Test
	public void testA10MergeLtR1UseCase3way1() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyLeftToRight(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddPrecondition(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR1UseCase3way2() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyLeftToRight(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeletePrecondition(comparison, diffs);
	}

	private void checkMergeDeletePrecondition(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 3, comparison.getDifferences().size());
		assertNull(diffs.addPrecondition);
		assertNull(diffs.addGuard);
		assertNull(diffs.addOwnedRule);
	}

	@Test
	public void testA10MergeLtR2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyLeftToRight(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddGuard(comparison, diffs);
	}

	private void checkMergeAddGuard(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 4, comparison.getDifferences().size());
		assertNull(diffs.addGuard);
		assertNull(diffs.addOwnedRule);
		assertNull(diffs.addPrecondition);
		assertNull(diffs.addTransition);
	}

	@Test
	public void testA10MergeLtR2UseCase3way1() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyLeftToRight(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddGuard(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR2UseCase3way2() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyLeftToRight(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteGuard(comparison, diffs);
	}

	private void checkMergeDeleteGuard(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 3, comparison.getDifferences().size());
		assertNull(diffs.addGuard);
		assertNull(diffs.addPrecondition);
		assertNull(diffs.addOwnedRule);
	}

	@Test
	public void testA10MergeLtR3UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyLeftToRight(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddOwnedRule(comparison, diffs);
	}

	private void checkMergeAddOwnedRule(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 4, comparison.getDifferences().size());
		assertNull(diffs.addOwnedRule);
		assertNull(diffs.addGuard);
		assertNull(diffs.addTransition);
		assertNull(diffs.addPrecondition);
	}

	@Test
	public void testA10MergeLtR3UseCase3way1() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyLeftToRight(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddOwnedRule(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR3UseCase3way2() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyLeftToRight(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedRule(comparison, diffs);
	}

	private void checkMergeDeleteOwnedRule(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 3, comparison.getDifferences().size());
		assertNull(diffs.addOwnedRule);
		assertNull(diffs.addGuard);
		assertNull(diffs.addPrecondition);
	}

	@Test
	public void testA10MergeRtL1UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyRightToLeft(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeletePrecondition(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL1UseCase3way1() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyRightToLeft(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeletePrecondition(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL1UseCase3way2() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyRightToLeft(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddPrecondition(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL2UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyRightToLeft(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteGuard(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL2UseCase3way1() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyRightToLeft(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteGuard(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL2UseCase3way2() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyRightToLeft(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddGuard(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyRightToLeft(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedRule(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase3way1() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyRightToLeft(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedRule(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase3way2() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyRightToLeft(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddOwnedRule(comparison, diffs);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	// local DELETE
	public void testA11UseCase3way1() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	// remote DELETE
	public void testA11UseCase3way2() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA11MergeLtR1UseCase() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyLeftToRight(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeletePrecondition(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR1UseCase3way1() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyLeftToRight(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeletePrecondition(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR1UseCase3way2() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyLeftToRight(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddPrecondition(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR2UseCase() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyLeftToRight(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteGuard(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR2UseCase3way1() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyLeftToRight(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteGuard(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR2UseCase3way2() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyLeftToRight(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddGuard(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyLeftToRight(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedRule(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase3way1() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyLeftToRight(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedRule(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase3way2() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyLeftToRight(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddOwnedRule(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyRightToLeft(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddPrecondition(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase3way1() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyRightToLeft(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddPrecondition(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase3way2() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addPrecondition).copyRightToLeft(
				diffs.addPrecondition, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeletePrecondition(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL2UseCase() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyRightToLeft(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddGuard(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL2UseCase3way1() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyRightToLeft(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddGuard(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL2UseCase3way2() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addGuard).copyRightToLeft(diffs.addGuard,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteGuard(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyRightToLeft(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddOwnedRule(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase3way1() throws IOException {
		final Resource left = input.getA2Right();
		final Resource right = input.getA2Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyRightToLeft(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddOwnedRule(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase3way2() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedRule).copyRightToLeft(diffs.addOwnedRule,
				new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedRule(comparison, diffs);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		assertEquals(NB_DIFFS, differences.size());

		DiffsOfInterest diffs = getDiffs(comparison, kind);

		if (kind.equals(TestKind.DELETE)) {
			assertEquals(0, diffs.addPrecondition.getImplies().size());
			assertEquals(1, diffs.addPrecondition.getImpliedBy().size());
			assertTrue(diffs.addPrecondition.getImpliedBy().contains(diffs.addGuard));

			assertEquals(1, diffs.addGuard.getImplies().size());
			assertTrue(diffs.addGuard.getImplies().contains(diffs.addPrecondition));
			assertEquals(1, diffs.addGuard.getImpliedBy().size());
			assertTrue(diffs.addGuard.getImpliedBy().contains(diffs.addOwnedRule));

			assertEquals(1, diffs.addOwnedRule.getImplies().size());
			assertTrue(diffs.addOwnedRule.getImplies().contains(diffs.addGuard));
			assertEquals(0, diffs.addOwnedRule.getImpliedBy().size());
		} else {
			assertEquals(1, diffs.addPrecondition.getImplies().size());
			assertTrue(diffs.addPrecondition.getImplies().contains(diffs.addGuard));
			assertEquals(0, diffs.addPrecondition.getImpliedBy().size());

			assertEquals(1, diffs.addGuard.getImplies().size());
			assertTrue(diffs.addGuard.getImplies().contains(diffs.addOwnedRule));
			assertEquals(1, diffs.addGuard.getImpliedBy().size());
			assertTrue(diffs.addGuard.getImpliedBy().contains(diffs.addPrecondition));

			assertEquals(0, diffs.addOwnedRule.getImplies().size());
			assertEquals(1, diffs.addOwnedRule.getImpliedBy().size());
			assertTrue(diffs.addOwnedRule.getImpliedBy().contains(diffs.addGuard));
		}

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	private class DiffsOfInterest {
		public Diff addTransition;

		public Diff addPrecondition;

		public Diff addOwnedRule;

		public Diff addGuard;
	}
}
