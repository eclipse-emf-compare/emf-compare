/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 507177
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.spec.DiffSpec;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.junit.Test;

public class RefineMergeTest {

	private static final boolean MERGE_RIGHT_TO_LEFT = true;

	private final AbstractMerger merger = new AbstractMerger() {
		public boolean isMergerFor(Diff target) {
			return true;
		}
	};

	/**
	 * If a diff refines another, we have to check if the "macro" diff has to be merged with it. Refinement
	 * diffs can only be merged together as a whole. Therefore, refined diffs should have the refining diffs
	 * as dependency and refining diffs the refined diff as resulting diff.
	 */
	@Test
	public void testRefineOnGetDirectResultingMerges() {
		// Create diffs
		Diff diff1 = new DiffSpec();
		Diff diff2 = new DiffSpec();

		Diff diff3 = new DiffSpec();
		diff3.getRefinedBy().add(diff1);
		diff3.getRefinedBy().add(diff2);

		// First call: diff3 must be merged, because refinements are only merged together as a whole.
		Set<Diff> directMergeDependencies = merger.getDirectMergeDependencies(diff1, MERGE_RIGHT_TO_LEFT);
		Set<Diff> resultingMerges = merger.getDirectResultingMerges(diff1, MERGE_RIGHT_TO_LEFT);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diff3));
		assertTrue(directMergeDependencies.isEmpty());

		directMergeDependencies = merger.getDirectMergeDependencies(diff2, MERGE_RIGHT_TO_LEFT);
		resultingMerges = merger.getDirectResultingMerges(diff2, MERGE_RIGHT_TO_LEFT);
		assertTrue(directMergeDependencies.isEmpty());
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diff3));

		directMergeDependencies = merger.getDirectMergeDependencies(diff3, MERGE_RIGHT_TO_LEFT);
		resultingMerges = merger.getDirectResultingMerges(diff3, MERGE_RIGHT_TO_LEFT);
		assertEquals(2, directMergeDependencies.size());
		assertTrue(directMergeDependencies.contains(diff1));
		assertTrue(directMergeDependencies.contains(diff2));
		assertTrue(resultingMerges.isEmpty());

		// Merge diff2
		diff2.setState(MERGED);

		// Second call: there should not be any changes
		directMergeDependencies = merger.getDirectMergeDependencies(diff1, MERGE_RIGHT_TO_LEFT);
		resultingMerges = merger.getDirectResultingMerges(diff1, MERGE_RIGHT_TO_LEFT);
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diff3));
		assertTrue(directMergeDependencies.isEmpty());

		directMergeDependencies = merger.getDirectMergeDependencies(diff2, MERGE_RIGHT_TO_LEFT);
		resultingMerges = merger.getDirectResultingMerges(diff2, MERGE_RIGHT_TO_LEFT);
		assertTrue(directMergeDependencies.isEmpty());
		assertEquals(1, resultingMerges.size());
		assertTrue(resultingMerges.contains(diff3));

		directMergeDependencies = merger.getDirectMergeDependencies(diff3, MERGE_RIGHT_TO_LEFT);
		resultingMerges = merger.getDirectResultingMerges(diff3, MERGE_RIGHT_TO_LEFT);
		assertEquals(2, directMergeDependencies.size());
		assertTrue(directMergeDependencies.contains(diff1));
		assertTrue(directMergeDependencies.contains(diff2));
		assertTrue(resultingMerges.isEmpty());
	}

}
