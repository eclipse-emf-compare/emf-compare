/*******************************************************************************
 * Copyright (c) 2019 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent Goubet - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.diff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.diff.data.nonuniquemultivaluedattribute.NonUniqueMultiValuedAttributeInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/*
 * non-unique multi-valued attributes may contain duplicate values and thus present particularities for
 * both the differencing process and the conflict detection process. During detection, we need to consider
 * that a if one side has two identical values and the other has three, then there is an addition (and not
 * a move has was previously detected). Furthermore, we will consider that adding the same value on both
 * sides of a three-way comparison is a pseudo-conflict (previously, there was no conflict at all since
 * non-unique values can have the same value more than once and thus the merge of such differences could
 * be done without issues). The comparison path for 3-way and 2-way being different, we need to make sure
 * both situations are tested.
 */
@SuppressWarnings("nls")
public class NonUniqueMultiValuedAttributeTest {
	private NonUniqueMultiValuedAttributeInputData input = new NonUniqueMultiValuedAttributeInputData();

	@Test
	public void testNonUniqueMultiValuedAttributeCaseA3WayDiff() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseALeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseARight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseAOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());
		Diff diff1 = differences.get(0); // ADD 3.03030303E-4
		Diff diff2 = differences.get(1); // ADD 0.0
		Diff diff3 = differences.get(2); // DELETE 1.69714
		Diff diff4 = differences.get(3); // DELETE 5.985E-4

		assertTrue(diff1 instanceof AttributeChange);
		assertTrue(diff2 instanceof AttributeChange);
		assertTrue(diff3 instanceof AttributeChange);
		assertTrue(diff4 instanceof AttributeChange);
		assertEquals("3.03030303E-4", ((AttributeChange)diff1).getValue());
		assertEquals("0.0", ((AttributeChange)diff2).getValue());
		assertEquals("1.69714", ((AttributeChange)diff3).getValue());
		assertEquals("5.985E-4", ((AttributeChange)diff4).getValue());
		assertEquals(DifferenceKind.ADD, diff1.getKind());
		assertEquals(DifferenceKind.ADD, diff2.getKind());
		assertEquals(DifferenceKind.DELETE, diff3.getKind());
		assertEquals(DifferenceKind.DELETE, diff4.getKind());
		assertEquals(DifferenceSource.RIGHT, diff1.getSource());
		assertEquals(DifferenceSource.RIGHT, diff2.getSource());
		assertEquals(DifferenceSource.RIGHT, diff3.getSource());
		assertEquals(DifferenceSource.RIGHT, diff4.getSource());

		// No conflicts
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseA2WayDiff() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseALeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseARight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());
		Diff diff1 = differences.get(0); // ADD 1.69714
		Diff diff2 = differences.get(1); // ADD 5.985E-4
		Diff diff3 = differences.get(2); // DELETE 3.03030303E-4
		Diff diff4 = differences.get(3); // DELETE 0.0

		assertTrue(diff1 instanceof AttributeChange);
		assertTrue(diff2 instanceof AttributeChange);
		assertTrue(diff3 instanceof AttributeChange);
		assertTrue(diff4 instanceof AttributeChange);
		assertEquals("1.69714", ((AttributeChange)diff1).getValue());
		assertEquals("5.985E-4", ((AttributeChange)diff2).getValue());
		assertEquals("3.03030303E-4", ((AttributeChange)diff3).getValue());
		assertEquals("0.0", ((AttributeChange)diff4).getValue());
		assertEquals(DifferenceKind.ADD, diff1.getKind());
		assertEquals(DifferenceKind.ADD, diff2.getKind());
		assertEquals(DifferenceKind.DELETE, diff3.getKind());
		assertEquals(DifferenceKind.DELETE, diff4.getKind());
		assertEquals(DifferenceSource.LEFT, diff1.getSource());
		assertEquals(DifferenceSource.LEFT, diff2.getSource());
		assertEquals(DifferenceSource.LEFT, diff3.getSource());
		assertEquals(DifferenceSource.LEFT, diff4.getSource());

		// No conflicts
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseA3WayMergeRtL() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseALeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseARight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseAOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseA3WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have 8 differences, paired in 4 pseudo-conflicts
		assertEquals(8, comparison.getDifferences().size());
		assertEquals(4, comparison.getConflicts().size());

		for (Conflict c : comparison.getConflicts()) {
			assertEquals(2, c.getDifferences().size());
			assertEquals(ConflictKind.PSEUDO, c.getKind());
		}
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseA2WayMergeRtL() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseALeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseARight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseA2WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have no differences left
		assertEquals(0, comparison.getDifferences().size());
		assertEquals(0, comparison.getConflicts().size());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseA3WayMergeLtR() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseALeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseARight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseAOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseA3WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have 0 differences
		// (all diffs were on the right side so we cancelled them by merging)
		assertTrue(comparison.getDifferences().isEmpty());
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseA2WayMergeLtR() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseALeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseARight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseA2WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have no differences left
		assertEquals(0, comparison.getDifferences().size());
		assertEquals(0, comparison.getConflicts().size());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseB3WayDiff() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseBLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseBRight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseBOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 3 differences
		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());
		Diff diff1 = differences.get(0); // MOVE a
		Diff diff2 = differences.get(1); // ADD a
		Diff diff3 = differences.get(2); // DELETE b

		assertTrue(diff1 instanceof AttributeChange);
		assertTrue(diff2 instanceof AttributeChange);
		assertTrue(diff3 instanceof AttributeChange);
		assertEquals("a", ((AttributeChange)diff1).getValue());
		assertEquals("a", ((AttributeChange)diff2).getValue());
		assertEquals("b", ((AttributeChange)diff3).getValue());
		assertEquals(DifferenceKind.MOVE, diff1.getKind());
		assertEquals(DifferenceKind.ADD, diff2.getKind());
		assertEquals(DifferenceKind.DELETE, diff3.getKind());
		assertEquals(DifferenceSource.RIGHT, diff1.getSource());
		assertEquals(DifferenceSource.RIGHT, diff2.getSource());
		assertEquals(DifferenceSource.RIGHT, diff3.getSource());

		// No conflicts
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseB2WayDiff() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseBLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseBRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 3 differences
		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());
		Diff diff1 = differences.get(0); // MOVE b
		Diff diff2 = differences.get(1); // MOVE d
		Diff diff3 = differences.get(2); // DELETE a

		assertTrue(diff1 instanceof AttributeChange);
		assertTrue(diff2 instanceof AttributeChange);
		assertTrue(diff3 instanceof AttributeChange);
		assertEquals("b", ((AttributeChange)diff1).getValue());
		assertEquals("d", ((AttributeChange)diff2).getValue());
		assertEquals("a", ((AttributeChange)diff3).getValue());
		assertEquals(DifferenceKind.ADD, diff1.getKind());
		assertEquals(DifferenceKind.MOVE, diff2.getKind());
		assertEquals(DifferenceKind.DELETE, diff3.getKind());
		assertEquals(DifferenceSource.LEFT, diff1.getSource());
		assertEquals(DifferenceSource.LEFT, diff2.getSource());
		assertEquals(DifferenceSource.LEFT, diff3.getSource());

		// No conflicts
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseB3WayMergeRtL() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseBLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseBRight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseBOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 3 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseB3WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have 6 differences, paired in 3 pseudo-conflicts
		assertEquals(6, comparison.getDifferences().size());
		assertEquals(3, comparison.getConflicts().size());

		for (Conflict c : comparison.getConflicts()) {
			assertEquals(2, c.getDifferences().size());
			assertEquals(ConflictKind.PSEUDO, c.getKind());
		}
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseB2WayMergeRtL() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseBLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseBRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 3 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseB2WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have no differences left
		assertEquals(0, comparison.getDifferences().size());
		assertEquals(0, comparison.getConflicts().size());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseB3WayMergeLtR() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseBLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseBRight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseBOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 3 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseB3WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have 0 differences
		// (all diffs were on the right side so we cancelled them by merging)
		assertTrue(comparison.getDifferences().isEmpty());
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseB2WayMergeLtR() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseBLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseBRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 3 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseB2WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(3, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have no differences left
		assertEquals(0, comparison.getDifferences().size());
		assertEquals(0, comparison.getConflicts().size());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseC3WayDiff() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseCLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseCRight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseCOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());
		Diff diff1 = differences.get(0); // ADD c
		Diff diff2 = differences.get(1); // ADD b
		Diff diff3 = differences.get(2); // ADD b
		Diff diff4 = differences.get(3); // MOVE d

		assertTrue(diff1 instanceof AttributeChange);
		assertTrue(diff2 instanceof AttributeChange);
		assertTrue(diff3 instanceof AttributeChange);
		assertTrue(diff4 instanceof AttributeChange);
		assertEquals("c", ((AttributeChange)diff1).getValue());
		assertEquals("b", ((AttributeChange)diff2).getValue());
		assertEquals("b", ((AttributeChange)diff3).getValue());
		assertEquals("d", ((AttributeChange)diff4).getValue());
		assertEquals(DifferenceKind.ADD, diff1.getKind());
		assertEquals(DifferenceKind.ADD, diff2.getKind());
		assertEquals(DifferenceKind.ADD, diff3.getKind());
		assertEquals(DifferenceKind.MOVE, diff4.getKind());
		assertEquals(DifferenceSource.LEFT, diff1.getSource());
		assertEquals(DifferenceSource.LEFT, diff2.getSource());
		assertEquals(DifferenceSource.LEFT, diff3.getSource());
		assertEquals(DifferenceSource.LEFT, diff4.getSource());

		// No conflicts
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseC2WayDiff() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseCLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseCRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());
		Diff diff1 = differences.get(0); // ADD c
		Diff diff2 = differences.get(1); // ADD b
		Diff diff3 = differences.get(2); // ADD b
		Diff diff4 = differences.get(3); // MOVE d

		assertTrue(diff1 instanceof AttributeChange);
		assertTrue(diff2 instanceof AttributeChange);
		assertTrue(diff3 instanceof AttributeChange);
		assertTrue(diff4 instanceof AttributeChange);
		assertEquals("c", ((AttributeChange)diff1).getValue());
		assertEquals("b", ((AttributeChange)diff2).getValue());
		assertEquals("b", ((AttributeChange)diff3).getValue());
		assertEquals("d", ((AttributeChange)diff4).getValue());
		assertEquals(DifferenceKind.ADD, diff1.getKind());
		assertEquals(DifferenceKind.ADD, diff2.getKind());
		assertEquals(DifferenceKind.ADD, diff3.getKind());
		assertEquals(DifferenceKind.MOVE, diff4.getKind());
		assertEquals(DifferenceSource.LEFT, diff1.getSource());
		assertEquals(DifferenceSource.LEFT, diff2.getSource());
		assertEquals(DifferenceSource.LEFT, diff3.getSource());
		assertEquals(DifferenceSource.LEFT, diff4.getSource());

		// No conflicts
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseC3WayMergeRtL() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseCLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseCRight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseCOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseC3WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have 0 differences
		// (all diffs were on the left side so we cancelled them by merging)
		assertTrue(comparison.getDifferences().isEmpty());
		assertTrue(comparison.getConflicts().isEmpty());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseC2WayMergeRtL() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseCLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseCRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseC2WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have no differences left
		assertEquals(0, comparison.getDifferences().size());
		assertEquals(0, comparison.getConflicts().size());
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseC3WayMergeLtR() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseCLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseCRight();
		Resource origin = input.getNonUniqueMultiValuedAttributeCaseCOrigin();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseC3WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have 8 differences, paired in 4 pseudo-conflicts
		assertEquals(8, comparison.getDifferences().size());
		assertEquals(4, comparison.getConflicts().size());

		for (Conflict c : comparison.getConflicts()) {
			assertEquals(2, c.getDifferences().size());
			assertEquals(ConflictKind.PSEUDO, c.getKind());
		}
	}

	@Test
	public void testNonUniqueMultiValuedAttributeCaseC2WayMergeLtR() throws IOException {
		Resource left = input.getNonUniqueMultiValuedAttributeCaseCLeft();
		Resource right = input.getNonUniqueMultiValuedAttributeCaseCRight();

		IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// We expect 4 differences and no conflict.
		// see testNonUniqueMultiValuedAttributeCaseC2WayDiff if this fails
		assertTrue(comparison.getConflicts().isEmpty());
		List<Diff> differences = comparison.getDifferences();
		assertEquals(4, differences.size());

		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		for (Diff diff : differences) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff,
					BasicMonitor.toMonitor(new NullProgressMonitor()));
		}

		comparison = EMFCompare.builder().build().compare(scope);
		// we should now have no differences left
		assertEquals(0, comparison.getDifferences().size());
		assertEquals(0, comparison.getConflicts().size());
	}
}
