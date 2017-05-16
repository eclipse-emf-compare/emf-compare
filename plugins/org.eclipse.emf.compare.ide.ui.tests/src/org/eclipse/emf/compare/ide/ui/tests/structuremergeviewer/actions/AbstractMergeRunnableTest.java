/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.AbstractMergeRunnable;
import org.eclipse.emf.compare.merge.IMergeCriterion;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry2;
import org.eclipse.emf.compare.merge.IMerger2;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Abstract base class for testing {@link AbstractMergeRunnable merge runnables}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings({"restriction" })
public class AbstractMergeRunnableTest {

	protected Comparison comparison;

	protected Registry2 mergerRegistry;

	protected IMerger2 merger;

	public AbstractMergeRunnableTest() {
		super();
	}

	@Before
	public void setupMocks() {
		comparison = mock(Comparison.class);
		mergerRegistry = mock(Registry2.class);
		merger = mock(IMerger2.class);
		when(mergerRegistry.getHighestRankingMerger(any(Diff.class))).thenReturn(merger);
		when(mergerRegistry.getMergersByRankDescending(any(Diff.class), any(IMergeCriterion.class)))
				.thenAnswer(new Answer<Iterator<IMerger>>() {
					public Iterator<IMerger> answer(InvocationOnMock invocation) throws Throwable {
						return Iterators.<IMerger> singletonIterator(merger);
					}
				});
	}

	protected void addConflictsToMockComparison(Conflict... conflicts) {
		when(comparison.getConflicts()).thenReturn(newEList(conflicts));
	}

	protected void addDifferencesToMockComparison(Diff... diffs) {
		when(comparison.getDifferences()).thenReturn(newEList(diffs));
		for (Diff diff : diffs) {
			final Match match = mock(Match.class);
			when(match.getComparison()).thenReturn(comparison);
			when(diff.getMatch()).thenReturn(match);
		}
	}

	protected ReferenceChange mockReferenceChange(DifferenceSource side, DifferenceKind kind, String name) {
		final ReferenceChange diff = mock(ReferenceChange.class, name);
		when(diff.getSource()).thenReturn(side);
		when(diff.getKind()).thenReturn(kind);
		when(diff.getRefinedBy()).thenReturn(new BasicEList<Diff>());
		when(diff.getRefines()).thenReturn(new BasicEList<Diff>());
		when(diff.getState()).thenReturn(UNRESOLVED);
		when(diff.eAdapters()).thenReturn(new BasicEList<Adapter>());
		return diff;
	}

	protected Conflict newConflict(Diff... diffs) {
		Conflict conflict = mock(Conflict.class);
		when(conflict.getKind()).thenReturn(ConflictKind.REAL);
		when(conflict.getDifferences()).thenReturn(newEList(diffs));
		for (Diff diff : diffs) {
			when(diff.getConflict()).thenReturn(conflict);
		}
		return conflict;
	}

	protected EList<Conflict> newEList(Conflict... diffs) {
		final EList<Conflict> list = new BasicEList<Conflict>();
		for (Conflict diff : diffs) {
			list.add(diff);
		}
		return list;
	}

	protected EList<Diff> newEList(Diff... diffs) {
		final EList<Diff> list = new BasicEList<Diff>();
		for (Diff diff : diffs) {
			list.add(diff);
		}
		return list;
	}

	protected void setNoConflictsInMockComparison() {
		addConflictsToMockComparison(new Conflict[0]);
	}

	protected void setThreeWayComparison() {
		when(Boolean.valueOf(comparison.isThreeWay())).thenReturn(Boolean.TRUE);
	}

	protected void setTwoWayComparison() {
		when(Boolean.valueOf(comparison.isThreeWay())).thenReturn(Boolean.FALSE);
	}

	protected void verifyHasBeenMarkedAsMerged(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasBeenMarkedAsMerged(diff);
		}
	}

	protected void verifyHasBeenMarkedAsDiscarded(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasBeenMarkedAsDiscarded(diff);
		}
	}

	protected void verifyHasBeenMarkedAsMerged(Diff diff) {
		verify(diff).setState(eq(MERGED));
	}

	protected void verifyHasBeenMarkedAsDiscarded(Diff diff) {
		verify(diff).setState(eq(DISCARDED));
	}

	protected void verifyHasBeenMergedLeftToRightOnly(Diff diff) {
		verify(merger, atLeastOnce()).copyLeftToRight(same(diff), any(Monitor.class));
		verify(merger, never()).copyRightToLeft(same(diff), any(Monitor.class));
	}

	protected void verifyHasBeenMergedLeftToRightOnly(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasBeenMergedLeftToRightOnly(diff);
		}
	}

	protected void verifyHasBeenMergedRightToLeftOnly(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasBeenMergedRightToLeftOnly(diff);
		}
	}

	protected void verifyHasBeenMergedRightToLeftOnly(Diff diff) {
		verify(merger, atLeastOnce()).copyRightToLeft(same(diff), any(Monitor.class));
		verify(merger, never()).copyLeftToRight(same(diff), any(Monitor.class));
	}

	protected void verifyHasNotBeenMerged(Diff diff) {
		verify(merger, never()).copyLeftToRight(same(diff), any(Monitor.class));
		verify(merger, never()).copyRightToLeft(same(diff), any(Monitor.class));
	}

	protected void verifyHasNotBeenMerged(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyHasNotBeenMerged(diff);
		}
	}

	protected void verifyStateIsUnchanged(Diff diff) {
		verify(diff, never()).setState(any(DifferenceState.class));
	}

	protected void verifyStateIsUnchanged(Diff... diffs) {
		for (Diff diff : diffs) {
			verifyStateIsUnchanged(diff);
		}
	}

}
