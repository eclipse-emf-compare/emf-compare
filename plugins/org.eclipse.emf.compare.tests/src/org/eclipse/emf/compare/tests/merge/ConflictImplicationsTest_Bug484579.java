/**
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.merge;

import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Collections2;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.checkers.MergeDependenciesChecker;
import org.eclipse.emf.compare.tests.merge.data.IndividualDiffInputData;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * This test class verifies that MergeDependenciesUtil computes the correct requirements. It focuses on
 * conflicts and pseudo-conflicts with dependencies.
 * 
 * @see bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=484579">484579</a> for more details.
 */
public class ConflictImplicationsTest_Bug484579 {

	private IndividualDiffInputData input = new IndividualDiffInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	/**
	 * Assert that the rejection of the deletion of PackageY (right to left) will not lead to unwanted
	 * dependencies dues to the presence of a pseudo conflicts (with dependencies) on a child element of
	 * packageY.
	 * 
	 * <pre>
	 * The ancestor model:
	 * - PackageA
	 *   - ClassB
	 * - PackageX
	 *   - PackageY
	 *     - ClassZ -> ClassB
	 *     
	 * The left model: 
	 * - PackageX 
	 * 
	 * The right model: 
	 * - PackageA2
	 *   - ClassB2
	 * - PackageX
	 *   - PackageY
	 * </pre>
	 * 
	 * The comparison on those models leads to a pseudo-conflict (deletion of ClassZ -> ClassB) and two
	 * conflicts (renaming of PackageA and ClassB).
	 * 
	 * @throws IOException
	 */
	@Test
	public void testImplicationBetweenConflicts() throws IOException {
		Diff deletePackageY = setupImplicationBetweenConflicts();

		MergeDependenciesChecker checker = getChecker(deletePackageY);
		checker.rightToLeft().implies(1).rejects(0).check();
		checker.leftToRight().implies(5).rejects(0).check();
	}

	/**
	 * Test the dependencies between two conflicting diffs if on one side an element is deleted (which implies
	 * other diff merging) and on the other the element is moved (which may implies other dependencies).
	 * 
	 * @throws IOException
	 */
	@Test
	public void testImplicationsBetweenMoveAndDeleteConflicts() throws IOException {
		Collection<Conflict> realConflicts = setupImplicationBetweenMoveAndDelete();

		// Conflict should contain 2 diffs, one from left, the other from right
		Conflict next = realConflicts.iterator().next();
		Collection<Diff> conflict1FromLeft = Collections2.filter(next.getDifferences(), fromSide(LEFT));
		Collection<Diff> conflict1FromRight = Collections2.filter(next.getDifferences(), fromSide(RIGHT));
		assertEquals(1, conflict1FromLeft.size());
		assertEquals(1, conflict1FromRight.size());

		// Get each diff of the conflicts
		Diff deletePackageD = conflict1FromLeft.iterator().next();
		Diff movePackageD = conflict1FromRight.iterator().next();

		MergeDependenciesChecker checker = getChecker(deletePackageD);
		checker.rightToLeft().implies(1).rejects(0).check();
		checker.leftToRight().implies(2).rejects(1).check();

		checker = getChecker(movePackageD);
		checker.rightToLeft().implies(1).rejects(1).check();
		checker.leftToRight().implies(1).rejects(0).check();
	}

	/**
	 * Get a MergeDependenciesChecker for the diff.
	 * 
	 * @param diff
	 *            The diff to check
	 * @return an instance of MergeDependenciesChecker for the given diff
	 */
	private MergeDependenciesChecker getChecker(Diff diff) {
		return MergeDependenciesChecker.getDependenciesChecker(mergerRegistry, diff);
	}

	/**
	 * Setup the test checking the implication between conflicts. This setup will assert that the dependencies
	 * of all the diffs are correct, then return the exact diff to test in order to validate the point.
	 * 
	 * @return the diff we want to check
	 * @throws IOException
	 */
	private Diff setupImplicationBetweenConflicts() throws IOException {
		final Resource origin = input.getConflictAndPseudoConflictImplicationsAncestor();
		final Resource left = input.getConflictAndPseudoConflictImplicationsLeft();
		final Resource right = input.getConflictAndPseudoConflictImplicationsRight();

		MergeDependenciesChecker checker = null;

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		EList<Conflict> conflicts = comparison.getConflicts();
		EList<Diff> differences = comparison.getDifferences();

		assertEquals(9, comparison.getDifferences().size());
		assertEquals(3, conflicts.size());

		Collection<Conflict> pseudoConflicts = Collections2.filter(conflicts, EMFComparePredicates
				.containsConflictOfTypes(PSEUDO));
		Collection<Conflict> realConflicts = Collections2.filter(conflicts, EMFComparePredicates
				.containsConflictOfTypes(REAL));

		assertEquals(1, pseudoConflicts.size());
		assertEquals(2, realConflicts.size());

		EList<Diff> pseudoConflictDifferences = pseudoConflicts.iterator().next().getDifferences();
		Collection<Diff> pseudoConflictsFromLeft = Collections2.filter(pseudoConflictDifferences,
				fromSide(LEFT));
		Collection<Diff> pseudoConflictFromRight = Collections2.filter(pseudoConflictDifferences,
				fromSide(RIGHT));

		Conflict real1 = null;
		Conflict real2 = null;
		for (Conflict conflict : realConflicts) {
			if (conflict.getLeftDifferences().get(0).getMatch().getDifferences().size() == 1) {
				real2 = conflict;
			} else if (conflict.getLeftDifferences().get(0).getMatch().getDifferences().size() == 2) {
				real1 = conflict;
			}
		}

		assertNotNull(real1);
		assertNotNull(real2);

		// Conflict should contain 2 diffs, one from left, the other from right
		Collection<Diff> conflict1FromLeft = Collections2.filter(real1.getDifferences(), fromSide(LEFT));
		Collection<Diff> conflict1FromRight = Collections2.filter(real1.getDifferences(), fromSide(RIGHT));
		assertEquals(1, conflict1FromLeft.size());
		assertEquals(1, conflict1FromRight.size());

		// Conflict should contain 2 diffs, one from left, the other from right
		Collection<Diff> conflict2FromLeft = Collections2.filter(real2.getDifferences(), fromSide(LEFT));
		Collection<Diff> conflict2FromRight = Collections2.filter(real2.getDifferences(), fromSide(RIGHT));
		assertEquals(1, conflict2FromLeft.size());
		assertEquals(1, conflict2FromRight.size());

		// Get each diff of the conflicts
		Diff deleteClassB = conflict1FromLeft.iterator().next();
		differences.remove(deleteClassB);
		Diff renameClassB = conflict1FromRight.iterator().next();
		differences.remove(renameClassB);
		Diff deletePackageA = conflict2FromLeft.iterator().next();
		differences.remove(deletePackageA);
		Diff renamePackageA = conflict2FromRight.iterator().next();
		differences.remove(renamePackageA);

		// Each pseudo-conflicting diff from left should implies 6 other diff when merging from right to left,
		// and 3 other when merging from left to right.
		for (Diff diff : pseudoConflictsFromLeft) {
			differences.remove(diff);
			checker = getChecker(diff);
			checker.rightToLeft().implies(7).rejects(0).check();
			checker.leftToRight().implies(4).rejects(0).check();
		}

		// Each pseudo-conflicting diff from right should implies 3 other diff when merging from right to
		// left, and 6 other when merging from left to right.
		for (Diff diff : pseudoConflictFromRight) {
			differences.remove(diff);
			checker = getChecker(diff);
			checker.rightToLeft().implies(4).rejects(0).check();
			checker.leftToRight().implies(7).rejects(0).check();
		}

		checker = getChecker(deleteClassB);
		checker.rightToLeft().implies(2).rejects(0).check();
		checker.leftToRight().implies(5).rejects(1).check();

		checker = getChecker(renameClassB);
		checker.rightToLeft().implies(1).rejects(2).check();
		checker.leftToRight().implies(1).rejects(0).check();

		checker = getChecker(deletePackageA);
		checker.rightToLeft().implies(1).rejects(0).check();
		checker.leftToRight().implies(6).rejects(2).check();

		checker = getChecker(renamePackageA);
		checker.rightToLeft().implies(1).rejects(1).check();
		checker.leftToRight().implies(1).rejects(0).check();

		assertEquals(1, differences.size());
		return differences.get(0);
	}

	/**
	 * Setup the test checking implication between move and delete.
	 * 
	 * @return the list of pseudoConflicts to test
	 * @throws IOException
	 */
	private Collection<Conflict> setupImplicationBetweenMoveAndDelete() throws IOException {
		final Resource origin = input.getMoveConflictAndPseudoConflictImplicationsAncestor();
		final Resource left = input.getMoveConflictAndPseudoConflictImplicationsLeft();
		final Resource right = input.getMoveConflictAndPseudoConflictImplicationsRight();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		EList<Conflict> conflicts = comparison.getConflicts();

		assertEquals(17, comparison.getDifferences().size());
		assertEquals(3, conflicts.size());

		Collection<Conflict> pseudoConflicts = Collections2.filter(conflicts, EMFComparePredicates
				.containsConflictOfTypes(PSEUDO));
		Collection<Conflict> realConflicts = Collections2.filter(conflicts, EMFComparePredicates
				.containsConflictOfTypes(REAL));

		assertEquals(2, pseudoConflicts.size());
		assertEquals(1, realConflicts.size());

		return realConflicts;
	}
}
