/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.spec.DiffSpec;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger2;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the implication relationship between differences. In EMF Compare, implication means that we always
 * merge the antecedent (A) instead of the consequent (B) in a relationship A implies B. B is considered
 * merged if A is merged and therefore requires no merging of its own.
 *
 * @author <a href="mailto:mfleck@eclipsesource.com">Martin Fleck</a>
 */
public class ImplicationsMergeTest {
	private static final boolean MERGE_RIGHT_TO_LEFT = false;

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	private final BatchMerger batchMerger = new BatchMerger(mergerRegistry);

	@Before
	public void setUp() {
		mergerRegistry.add(new AbstractMerger() {
			public boolean isMergerFor(Diff target) {
				return true;
			}
		});
	}

	protected IMerger2 getMerger(Diff diff) {
		return (IMerger2)mergerRegistry.getHighestRankingMerger(diff);
	}

	protected void mergeLeftToRight(Diff diff) {
		batchMerger.copyAllLeftToRight(Arrays.asList(diff), new BasicMonitor());
	}

	protected Set<Diff> getDirectResultingMerges(Diff diff) {
		return getMerger(diff).getDirectResultingMerges(diff, MERGE_RIGHT_TO_LEFT);
	}

	/**
	 * Tests: A => B <br/>
	 * Merge: A <br/>
	 * Resulting Merges: A, B <br/>
	 * A implies B, therefore if we merge A, B is set to MERGED automatically.
	 */
	@Test
	public void testsAImpliesBMergeA() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(1, resultingMerges.size());
		assertSame(diffB, resultingMerges.iterator().next());

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(1, resultingMerges.size());
		assertSame(diffA, resultingMerges.iterator().next());

		mergeLeftToRight(diffA);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
	}

	/**
	 * Tests: A => B <br/>
	 * Merge: B <br/>
	 * Resulting Merges: A, B <br/>
	 * In EMF Compare, implication means that we always merge the antecedent (A) instead of the consequent
	 * (B), i.e., B requires no merging. Therefore A is merged and B is set to MERGED automatically.
	 */
	@Test
	public void testsAImpliesBMergeB() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(1, resultingMerges.size());
		assertSame(diffB, resultingMerges.iterator().next());

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(1, resultingMerges.size());
		assertSame(diffA, resultingMerges.iterator().next());

		mergeLeftToRight(diffB);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
	}

	/**
	 * Tests: A => B, B => C <br/>
	 * Merge: A <br/>
	 * Resulting Merges: A, B, C <br/>
	 * A implies B, therefore if we merge A, B is set to MERGED automatically (requires no merging). Since B
	 * also implies C, C is also set to MERGED automatically.
	 */
	@Test
	public void testsAImpliesBImpliesCMergeA() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		Diff diffC = createDiff("DiffC"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);
		diffB.getImplies().add(diffC);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(1, resultingMerges.size());
		assertSame(diffB, resultingMerges.iterator().next());

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(2, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));
		assertTrue(resultingMerges.contains(diffC));

		resultingMerges = getDirectResultingMerges(diffC);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffB));

		mergeLeftToRight(diffA);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
		assertEquals(MERGED, diffC.getState());
	}

	/**
	 * Tests: A => B, B => C <br/>
	 * Merge: B <br/>
	 * Resulting Merges: A, B, C <br/>
	 * In EMF Compare, implication means that we always merge A instead of B (B requires no merging),
	 * therefore A is merged and B is set to MERGED automatically. B also implies C, so C is also set to
	 * MERGED automatically.
	 */
	@Test
	public void testsAImpliesBImpliesCMergeB() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		Diff diffC = createDiff("DiffC"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);
		diffB.getImplies().add(diffC);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(1, resultingMerges.size());
		assertSame(diffB, resultingMerges.iterator().next());

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(2, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));
		assertTrue(resultingMerges.contains(diffC));

		resultingMerges = getDirectResultingMerges(diffC);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffB));

		mergeLeftToRight(diffB);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
		assertEquals(MERGED, diffC.getState());
	}

	/**
	 * Tests: A => B, B => C <br/>
	 * Merge: C <br/>
	 * Resulting Merges: A, B, C <br/>
	 * In EMF Compare, implication means that we always merge A instead of B (B requires no merging), and B
	 * instead of C. Therefore A is merged and B and C are set to MERGED automatically.
	 */
	@Test
	public void testsAImpliesBImpliesCMergeC() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		Diff diffC = createDiff("DiffC"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);
		diffB.getImplies().add(diffC);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(1, resultingMerges.size());
		assertSame(diffB, resultingMerges.iterator().next());

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(2, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));
		assertTrue(resultingMerges.contains(diffC));

		resultingMerges = getDirectResultingMerges(diffC);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffB));

		mergeLeftToRight(diffC);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
		assertEquals(MERGED, diffC.getState());
	}

	/**
	 * Tests: A => B, C <br/>
	 * Merge: A <br/>
	 * Resulting Merges: A, B, C <br/>
	 * A implies B, and C so we merge A and set B and C to MERGED automatically.
	 */
	@Test
	public void testsAImpliesBCMergeA() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		Diff diffC = createDiff("DiffC"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);
		diffA.getImplies().add(diffC);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(2, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffB));
		assertTrue(resultingMerges.contains(diffC));

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));

		resultingMerges = getDirectResultingMerges(diffC);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));

		mergeLeftToRight(diffA);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
		assertEquals(MERGED, diffC.getState());
	}

	/**
	 * Tests: A => B, C <br/>
	 * Merge: B <br/>
	 * Resulting Merges: A, B, C <br/>
	 * In EMF Compare, implication means that we always merge A instead of B (B and C require no merging).
	 * Therefore we merge A and set B and C to MERGED automatically.
	 */
	@Test
	public void testsAImpliesBCMergeB() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		Diff diffC = createDiff("DiffC"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);
		diffA.getImplies().add(diffC);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(2, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffB));
		assertTrue(resultingMerges.contains(diffC));

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));

		resultingMerges = getDirectResultingMerges(diffC);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));

		mergeLeftToRight(diffB);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
		assertEquals(MERGED, diffC.getState());
	}

	/**
	 * Tests: A => B, C <br/>
	 * Merge: B <br/>
	 * Resulting Merges: A, B, C <br/>
	 * In EMF Compare, implication means that we always merge A instead of C (B and C require no merging).
	 * Therefore we merge A and set B and C to MERGED automatically.
	 */
	@Test
	public void testsAImpliesBCMergeC() {
		Diff diffA = createDiff("DiffA"); //$NON-NLS-1$
		Diff diffB = createDiff("DiffB"); //$NON-NLS-1$
		Diff diffC = createDiff("DiffC"); //$NON-NLS-1$
		diffA.getImplies().add(diffB);
		diffA.getImplies().add(diffC);

		Set<Diff> resultingMerges = getDirectResultingMerges(diffA);
		assertEquals(2, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffB));
		assertTrue(resultingMerges.contains(diffC));

		resultingMerges = getDirectResultingMerges(diffB);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));

		resultingMerges = getDirectResultingMerges(diffC);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diffA));

		mergeLeftToRight(diffC);
		assertEquals(MERGED, diffA.getState());
		assertEquals(MERGED, diffB.getState());
		assertEquals(MERGED, diffC.getState());
	}

	protected Diff createDiff(final String name) {
		return new DiffSpec() {
			@Override
			public String toString() {
				return name;
			}
		};
	}
}
