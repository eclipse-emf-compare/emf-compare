package org.eclipse.emf.compare.tests.conflict;

import static java.util.Arrays.asList;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.conflict.AbstractConflictSearch;
import org.eclipse.emf.compare.internal.conflict.ComparisonIndex;
import org.junit.Before;
import org.junit.Test;

public class ConflictSearchTest {

	private Diff diff;

	private Match match;

	private Comparison comparison;

	private ComparisonIndex index;

	private Diff other;

	private Diff equivToDiff;

	private Diff equivToOther;

	private Equivalence equivDiff;

	private Equivalence equivOther;

	/**
	 * @see <a href="http://eclip.se/510704">Bug 510704</a>
	 */
	@Test
	public void testEquivalentDiffsAreInSameConflict() {
		// Given
		TestConflictSearch search = new TestConflictSearch(diff, index);
		// When
		search.conflict(other, REAL);
		// then
		assertEquals(1, comparison.getConflicts().size());
		Conflict conflict = comparison.getConflicts().get(0);
		assertEquals(4, conflict.getDifferences().size());
		assertTrue(conflict.getDifferences().containsAll(asList(diff, equivToDiff, other, equivToOther)));
	}

	@Before
	public void setUp() throws Exception {
		comparison = CompareFactory.eINSTANCE.createComparison();
		match = CompareFactory.eINSTANCE.createMatch();
		comparison.getMatches().add(match);
		diff = CompareFactory.eINSTANCE.createDiff();
		diff.setMatch(match);
		other = CompareFactory.eINSTANCE.createDiff();
		other.setMatch(match);
		equivToDiff = CompareFactory.eINSTANCE.createDiff();
		equivToDiff.setMatch(match);
		equivToOther = CompareFactory.eINSTANCE.createDiff();
		equivToOther.setMatch(match);
		equivDiff = CompareFactory.eINSTANCE.createEquivalence();
		equivOther = CompareFactory.eINSTANCE.createEquivalence();
		equivDiff.getDifferences().addAll(asList(diff, equivToDiff));
		equivOther.getDifferences().addAll(asList(other, equivToOther));
		index = ComparisonIndex.index(comparison, new BasicMonitor());
	}

	public static final class TestConflictSearch extends AbstractConflictSearch<Diff> {

		public TestConflictSearch(Diff diff, ComparisonIndex index) {
			super(diff, index, new BasicMonitor());
		}

		@Override
		public void detectConflicts() {
			// Nothing to do
		}

		@Override
		public void conflict(Diff other, ConflictKind kind) {
			super.conflict(other, kind);
		}

	}
}
