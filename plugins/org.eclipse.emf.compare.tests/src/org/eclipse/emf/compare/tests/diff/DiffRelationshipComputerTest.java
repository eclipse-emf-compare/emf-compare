/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.diff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.spec.DiffSpec;
import org.eclipse.emf.compare.merge.CachingDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.ComputeDiffsToMerge;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry2;
import org.eclipse.emf.compare.merge.IMerger2;
import org.junit.Before;
import org.junit.Test;

/**
 * This class holds test cases that are related to the {@link IDiffRelationshipComputer} and specifically, the
 * {@link CachingDiffRelationshipComputer}.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings({"boxing" })
public class DiffRelationshipComputerTest {

	/** Merge direction. */
	protected static boolean MERGE_RIGHT_TO_LEFT = true;

	/** Merger registry with the mock merger. */
	protected Registry2 mergerRegistry;

	/** Mock merger with a high ranking applying to the testing diff. */
	protected IMerger2 merger;

	/** Comparison. */
	protected Comparison comparison;

	/** Match. */
	protected Match match;

	/** Testing diff. */
	protected Diff diff;

	/** Dependency of the testing diff. */
	protected Diff mergeDependency;

	/** Resulting diff of the testing diff. */
	protected Diff resultingMerge;

	/** Resulting rejection of the testing diff. */
	protected Diff resultingRejection;

	/**
	 * Creates a new merger registry, merger and relevant diffs.
	 */
	@Before
	public void setupClass() {
		mergerRegistry = createMergerRegistry();
		comparison = createComparison();
		comparison.getMatches().add(match = createMatch());
		comparison.getDifferences().add(diff = createDiff(match));
		comparison.getDifferences().add(mergeDependency = createDiff(match));
		comparison.getDifferences().add(resultingMerge = createDiff(match));
		comparison.getDifferences().add(resultingRejection = createDiff(match));
		merger = mockMerger(diff, mergeDependency, resultingMerge, resultingRejection, 1000, mergerRegistry);
		mergerRegistry.add(merger);
	}

	protected CachingDiffRelationshipComputer getDiffRelationshipComputer() {
		return new CachingDiffRelationshipComputer(mergerRegistry);
	}

	/**
	 * Tests that the computer is working correctly for direct merge dependencies.
	 */
	@Test
	public void testDirectMergeDependencies() {
		IDiffRelationshipComputer computer = getDiffRelationshipComputer();

		// call calculation method a few times
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods was called exactly once
		verifyDirectMergeDependenciesCalledExactly(1);
	}

	/**
	 * Tests that the computer is working correctly for direct resulting merges.
	 */
	@Test
	public void testDirectResultingMerges() {
		IDiffRelationshipComputer computer = getDiffRelationshipComputer();

		// call calculation method a few times
		assertDirectResultingMerges(computer.getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectResultingMerges(computer.getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectResultingMerges(computer.getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectResultingMerges(computer.getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods was called exactly once
		verifyDirectResultingMergesCalledExactly(1);
	}

	/**
	 * Tests that the computer is working correctly for direct resulting rejections.
	 */
	@Test
	public void testDirectResultingRejections() {
		IDiffRelationshipComputer computer = getDiffRelationshipComputer();

		// call calculation method a few times
		assertDirectResultingRejections(computer.getDirectResultingRejections(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectResultingRejections(computer.getDirectResultingRejections(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectResultingRejections(computer.getDirectResultingRejections(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectResultingRejections(computer.getDirectResultingRejections(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods was called exactly once
		verifyDirectResultingRejectionsCalledExactly(1);
	}

	/**
	 * Tests that the computer is working correctly for all resulting merges.
	 */
	@Test
	public void testAllResultingMerges() {
		IDiffRelationshipComputer computer = getDiffRelationshipComputer();

		// call calculation method a few times
		assertAllResultingMerges(computer.getAllResultingMerges(diff, MERGE_RIGHT_TO_LEFT));
		assertAllResultingMerges(computer.getAllResultingMerges(diff, MERGE_RIGHT_TO_LEFT));
		assertAllResultingMerges(computer.getAllResultingMerges(diff, MERGE_RIGHT_TO_LEFT));
		assertAllResultingMerges(computer.getAllResultingMerges(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods of the diffs are called at most once
		verifyMergerCalculationsCalledAtMostOnce();
	}

	/**
	 * Tests that the computer is working correctly for all resulting rejections.
	 */
	@Test
	public void testAllResultingRejections() {
		IDiffRelationshipComputer computer = getDiffRelationshipComputer();

		// call resulting rejections a few more times
		assertAllResultingRejections(computer.getAllResultingRejections(diff, MERGE_RIGHT_TO_LEFT));
		assertAllResultingRejections(computer.getAllResultingRejections(diff, MERGE_RIGHT_TO_LEFT));
		assertAllResultingRejections(computer.getAllResultingRejections(diff, MERGE_RIGHT_TO_LEFT));
		assertAllResultingRejections(computer.getAllResultingRejections(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods of the diffs are called at most once
		verifyMergerCalculationsCalledAtMostOnce();
	}

	/**
	 * Tests that a diff state change invalidates the cached relationships and leads to a re-calculation of
	 * the relationships.
	 */
	@Test
	public void testInvalidateCache() {
		CachingDiffRelationshipComputer computer = getDiffRelationshipComputer();

		// trigger first time caching
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation method was called exactly once
		verifyDirectMergeDependenciesCalledExactly(1);

		// invalidate cache
		computer.invalidate();

		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods was called twice now, because we needed to recalculate the relationships
		verifyDirectMergeDependenciesCalledExactly(2);

		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods was called twice now, same as before
		verifyDirectMergeDependenciesCalledExactly(2);

		// invalidate cache
		computer.invalidate();

		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));
		assertDirectMergeDependencies(computer.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT));

		// the calculation methods was called three times now, because of recalculation
		verifyDirectMergeDependenciesCalledExactly(3);
	}

	/**
	 * Tests that the is correctly used in {@link ComputeDiffsToMerge}.
	 */
	@Test
	public void testComputeDiffsToMergeIntegration() {
		ComputeDiffsToMerge computer = new ComputeDiffsToMerge(MERGE_RIGHT_TO_LEFT,
				new CachingDiffRelationshipComputer(mergerRegistry));

		// call resulting merges a few more times
		assertAllResultingMerges(computer.getAllDiffsToMerge(diff));
		assertAllResultingMerges(computer.getAllDiffsToMerge(diff));
		assertAllResultingMerges(computer.getAllDiffsToMerge(diff));
		assertAllResultingMerges(computer.getAllDiffsToMerge(diff));

		// the calculation methods of the diffs are called at most once
		verifyMergerCalculationsCalledAtMostOnce();
	}

	/**
	 * Verifies that any of the relationship calculating methods of the merger were called at most once.
	 */
	protected void verifyMergerCalculationsCalledAtMostOnce() {
		verify(merger, atMost(1)).getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT);
		verify(merger, atMost(1)).getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT);
		verify(merger, atMost(1)).getDirectResultingRejections(diff, MERGE_RIGHT_TO_LEFT);
	}

	/**
	 * Verifies that the calculation method for the direct merge dependencies was called exactly the given
	 * number of times.
	 * 
	 * @param times
	 *            number of times the method should have been called.
	 */
	protected void verifyDirectMergeDependenciesCalledExactly(int times) {
		verify(merger, times(times)).getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT);
	}

	/**
	 * Verifies that the calculation method for the direct resulting merges was called exactly the given
	 * number of times.
	 * 
	 * @param times
	 *            number of times the method should have been called.
	 */
	protected void verifyDirectResultingMergesCalledExactly(int times) {
		verify(merger, times(times)).getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT);
	}

	/**
	 * Verifies that the calculation method for the direct resulting rejections was called exactly the given
	 * number of times.
	 * 
	 * @param times
	 *            number of times the method should have been called.
	 */
	protected void verifyDirectResultingRejectionsCalledExactly(int times) {
		verify(merger, times(times)).getDirectResultingRejections(diff, MERGE_RIGHT_TO_LEFT);
	}

	/**
	 * Asserts that the known resulting merges (dependency, diff itself, direct resulting merge) are the only
	 * diffs available in the given set.
	 * 
	 * @param resultingMerges
	 *            calculated resulting merges
	 */
	protected void assertDirectMergeDependencies(Set<Diff> mergeDependencies) {
		assertEquals(1, mergeDependencies.size());
		assertTrue(mergeDependencies.contains(mergeDependency));
	}

	/**
	 * Asserts that the known resulting merges (dependency, diff itself, direct resulting merge) are the only
	 * diffs available in the given set.
	 * 
	 * @param resultingMerges
	 *            calculated resulting merges
	 */
	protected void assertDirectResultingRejections(Set<Diff> resultingRejections) {
		assertEquals(1, resultingRejections.size());
		assertTrue(resultingRejections.contains(resultingRejection));
	}

	/**
	 * Asserts that the known resulting merges (dependency, diff itself, direct resulting merge) are the only
	 * diffs available in the given set.
	 * 
	 * @param resultingMerges
	 *            calculated resulting merges
	 */
	protected void assertDirectResultingMerges(Set<Diff> resultingMerges) {
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(resultingMerge));
	}

	/**
	 * Asserts that the known resulting merges (dependency, diff itself, direct resulting merge) are the only
	 * diffs available in the given set.
	 * 
	 * @param resultingMerges
	 *            calculated resulting merges
	 */
	protected void assertAllResultingMerges(Set<Diff> resultingMerges) {
		assertEquals(3, resultingMerges.size());
		assertTrue(resultingMerges.contains(mergeDependency));
		assertTrue(resultingMerges.contains(diff));
		assertTrue(resultingMerges.contains(resultingMerge));
	}

	/**
	 * Asserts that the known resulting rejection is the only diff available in the given set.
	 * 
	 * @param resultingRejections
	 *            calculated resulting rejections
	 */
	protected void assertAllResultingRejections(Set<Diff> resultingRejections) {
		assertEquals(1, resultingRejections.size());
		assertTrue(resultingRejections.contains(resultingRejection));
	}

	/**
	 * Creates a new comparison.
	 * 
	 * @return new comparison instance
	 */
	private static Comparison createComparison() {
		return CompareFactory.eINSTANCE.createComparison();
	}

	/**
	 * Creates a new match.
	 * 
	 * @return new match instance
	 */
	private static Match createMatch() {
		return CompareFactory.eINSTANCE.createMatch();
	}

	/**
	 * Creates a new diff instance.
	 * 
	 * @param match
	 * @return new diff instance
	 */
	private static Diff createDiff(Match match) {
		Diff diff = new DiffSpec();
		diff.setMatch(match);
		return diff;
	}

	/**
	 * Creates a new standalone instance of a merger registry.
	 * 
	 * @return new merger registry instance
	 */
	private static IMerger.Registry2 createMergerRegistry() {
		return (Registry2)IMerger.RegistryImpl.createStandaloneInstance();
	}

	/**
	 * Creates a mock merger that returns the given diffs as relationships for the testing diff.
	 * 
	 * @param diff
	 *            testing diff
	 * @param mergeDependency
	 *            dependency of the testing diff
	 * @param resultingMerge
	 *            resulting diff for the testing diff
	 * @param resultingRejection
	 *            resulting rejection for the testing diff
	 * @param ranking
	 *            ranking of the mock merger
	 * @param registry
	 *            registry of the mock merger
	 * @return a mock merger
	 */
	private static IMerger2 mockMerger(Diff diff, Diff mergeDependency, Diff resultingMerge,
			Diff resultingRejection, int ranking, IMerger.Registry registry) {
		IMerger2 mockMerger = mock(IMerger2.class);
		when(mockMerger.getDirectMergeDependencies(diff, MERGE_RIGHT_TO_LEFT))
				.thenReturn(Sets.newHashSet(mergeDependency));
		when(mockMerger.getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT))
				.thenReturn(Sets.newHashSet(resultingMerge));
		when(mockMerger.getDirectResultingRejections(diff, MERGE_RIGHT_TO_LEFT))
				.thenReturn(Sets.newHashSet(resultingRejection));
		when(mockMerger.isMergerFor(any(Diff.class))).thenReturn(Boolean.TRUE);
		when(mockMerger.getRanking()).thenReturn(ranking);
		when(mockMerger.getRegistry()).thenReturn(registry);
		return mockMerger;
	}
}
