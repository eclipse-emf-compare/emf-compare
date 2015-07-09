/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.mergeresolution;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.internal.mergeresolution.MergeResolutionListenerRegistry;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeNodeCompareInput;
import org.eclipse.emf.compare.ide.ui.mergeresolution.MergeResolutionManager;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("restriction")
public class MergeResolutionManagerTest {

	MergeResolutionListenerRegistry registry;

	MergeResolutionManager sut;

	@Before
	public void setUp() {
		registry = mock(MergeResolutionListenerRegistry.class);
		sut = new MergeResolutionManager(registry);
	}

	@Test
	public void testPositiveMatch() {
		Comparison comparison = prepareComparison(true);

		Match match = mock(Match.class);
		when(match.getComparison()).thenReturn(comparison);

		TreeNodeCompareInput input = mock(TreeNodeCompareInput.class);
		when(input.getComparisonObject()).thenReturn(match);

		sut.handleFlush(input);

		verify(registry).mergeResolutionCompleted(any(Comparison.class));
	}

	@Test
	public void testNegativeMatch() {
		Comparison comparison = prepareComparison(false);

		Match match = mock(Match.class);
		when(match.getComparison()).thenReturn(comparison);

		TreeNodeCompareInput input = mock(TreeNodeCompareInput.class);
		when(input.getComparisonObject()).thenReturn(match);

		sut.handleFlush(input);

		verify(registry, never()).mergeResolutionCompleted(any(Comparison.class));
	}

	@Test
	public void testPositiveDiff() {
		Comparison comparison = prepareComparison(true);

		Diff diff = mock(Diff.class);
		Match match = mock(Match.class);
		when(diff.getMatch()).thenReturn(match);
		when(match.getComparison()).thenReturn(comparison);

		TreeNodeCompareInput input = mock(TreeNodeCompareInput.class);
		when(input.getComparisonObject()).thenReturn(diff);

		sut.handleFlush(input);

		verify(registry).mergeResolutionCompleted(any(Comparison.class));
	}

	@Test
	public void testNegativeDiff() {
		Comparison comparison = prepareComparison(false);

		Diff diff = mock(Diff.class);
		Match match = mock(Match.class);
		when(diff.getMatch()).thenReturn(match);
		when(match.getComparison()).thenReturn(comparison);

		TreeNodeCompareInput input = mock(TreeNodeCompareInput.class);
		when(input.getComparisonObject()).thenReturn(diff);

		sut.handleFlush(input);

		verify(registry, never()).mergeResolutionCompleted(any(Comparison.class));
	}

	@Test
	public void testPositiveMatchResource() {
		Comparison comparison = prepareComparison(true);

		MatchResource matchResource = mock(MatchResource.class);
		when(matchResource.getComparison()).thenReturn(comparison);

		TreeNodeCompareInput input = mock(TreeNodeCompareInput.class);
		when(input.getComparisonObject()).thenReturn(matchResource);

		sut.handleFlush(input);

		verify(registry).mergeResolutionCompleted(any(Comparison.class));
	}

	@Test
	public void testNegativeMatchResource() {
		Comparison comparison = prepareComparison(false);

		MatchResource matchResource = mock(MatchResource.class);
		when(matchResource.getComparison()).thenReturn(comparison);

		TreeNodeCompareInput input = mock(TreeNodeCompareInput.class);
		when(input.getComparisonObject()).thenReturn(matchResource);

		sut.handleFlush(input);

		verify(registry, never()).mergeResolutionCompleted(any(Comparison.class));
	}

	private Comparison prepareComparison(boolean allResolved) {
		Comparison comparison = mock(Comparison.class);

		EList<Conflict> conflicts = new BasicEList<Conflict>();

		conflicts.add(createConflict(true));
		conflicts.add(createConflict(true));
		conflicts.add(createConflict(true));
		conflicts.add(createConflict(allResolved));

		when(comparison.getConflicts()).thenReturn(conflicts);
		return comparison;
	}

	private Conflict createConflict(boolean resolved) {
		Conflict conflict = mock(Conflict.class);
		EList<Diff> differences = new BasicEList<Diff>();
		differences.add(createResolvedDiff(DifferenceState.MERGED));
		differences.add(createResolvedDiff(DifferenceState.MERGED));
		differences.add(createResolvedDiff(DifferenceState.DISCARDED));
		if (!resolved) {
			differences.add(createResolvedDiff(DifferenceState.UNRESOLVED));
		}
		when(conflict.getDifferences()).thenReturn(differences);
		return conflict;
	}

	private Diff createResolvedDiff(DifferenceState state) {
		Diff diff = mock(Diff.class);
		when(diff.getState()).thenReturn(state);
		return diff;
	}
}
