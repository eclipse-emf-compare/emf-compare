package org.eclipse.emf.compare.diagram.papyrus.tests.egit.mergeresolution;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.mergeresolution.MergeResolutionListenerRegistry;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeNodeCompareInput;
import org.eclipse.emf.compare.ide.ui.mergeresolution.MergeResolutionManager;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("restriction")
public class MergeResolutionManagerTest {
	MergeResolutionManager sut;

	MergeResolutionListenerRegistry registry;

	@Before
	public void setUp() {
		registry = mock(MergeResolutionListenerRegistry.class);

		sut = new MergeResolutionManager(registry);
	}

	@Test
	public void testInvalidInput() {
		sut.handleFlush(null);
		sut.handleFlush("not a valid input");

		verifyNoMoreInteractions(registry);
	}

	@Test
	public void testNoConflicts() {
		EList<Conflict> conflicts = new BasicEList<Conflict>();

		Comparison comparison = mock(Comparison.class);
		when(comparison.getConflicts()).thenReturn(conflicts);

		sut.handleFlush(pack(comparison));
		verify(comparison).getConflicts();
		verifyNoMoreInteractions(registry);
	}

	@Test
	public void testOnlyUnresolvedConflicts() {
		EList<Conflict> conflicts = new BasicEList<Conflict>();

		Conflict c1 = mock(Conflict.class);
		EList<Diff> c1d = new BasicEList<Diff>();
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.UNRESOLVED).getMock());
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.UNRESOLVED).getMock());
		when(c1.getDifferences()).thenReturn(c1d);
		conflicts.add(c1);

		Conflict c2 = mock(Conflict.class);
		EList<Diff> c2d = new BasicEList<Diff>();
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.UNRESOLVED).getMock());
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.UNRESOLVED).getMock());
		when(c2.getDifferences()).thenReturn(c2d);
		conflicts.add(c2);

		Comparison comparison = mock(Comparison.class);
		when(comparison.getConflicts()).thenReturn(conflicts);

		sut.handleFlush(pack(comparison));
		verify(comparison, atLeastOnce()).getConflicts();
		verifyNoMoreInteractions(registry);
	}

	@Test
	public void testMixedConflicts() {
		EList<Conflict> conflicts = new BasicEList<Conflict>();

		Conflict c1 = mock(Conflict.class);
		EList<Diff> c1d = new BasicEList<Diff>();
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.UNRESOLVED).getMock());
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.UNRESOLVED).getMock());
		when(c1.getDifferences()).thenReturn(c1d);
		conflicts.add(c1);

		Conflict c2 = mock(Conflict.class);
		EList<Diff> c2d = new BasicEList<Diff>();
		c2d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.MERGED).getMock());
		c2d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.DISCARDED).getMock());
		when(c2.getDifferences()).thenReturn(c2d);
		conflicts.add(c2);

		Comparison comparison = mock(Comparison.class);
		when(comparison.getConflicts()).thenReturn(conflicts);

		sut.handleFlush(pack(comparison));
		verify(comparison, atLeastOnce()).getConflicts();
		verifyNoMoreInteractions(registry);
	}

	@Test
	public void testOnlyResolvedConflicts() {
		EList<Conflict> conflicts = new BasicEList<Conflict>();

		Conflict c1 = mock(Conflict.class);
		EList<Diff> c1d = new BasicEList<Diff>();
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.DISCARDED).getMock());
		c1d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.MERGED).getMock());
		when(c1.getDifferences()).thenReturn(c1d);
		conflicts.add(c1);

		Conflict c2 = mock(Conflict.class);
		EList<Diff> c2d = new BasicEList<Diff>();
		c2d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.MERGED).getMock());
		c2d.add((Diff) when(mock(Diff.class).getState()).thenReturn(DifferenceState.DISCARDED).getMock());
		when(c2.getDifferences()).thenReturn(c2d);
		conflicts.add(c2);

		Comparison comparison = mock(Comparison.class);
		when(comparison.getConflicts()).thenReturn(conflicts);

		sut.handleFlush(pack(comparison));
		verify(comparison, atLeastOnce()).getConflicts();
		verify(registry).mergeResolutionCompleted(comparison);
		verifyNoMoreInteractions(registry);
	}

	private Object pack(Comparison comparison) {
		Match match = mock(Match.class);
		when(match.getComparison()).thenReturn(comparison).getMock();

		TreeNodeCompareInput treeNodeCompareInput = mock(TreeNodeCompareInput.class);
		when(treeNodeCompareInput.getComparisonObject()).thenReturn(match);

		return treeNodeCompareInput;

	}
}
