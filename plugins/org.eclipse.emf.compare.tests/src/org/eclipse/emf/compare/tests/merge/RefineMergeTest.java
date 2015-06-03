/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.internal.spec.DiffSpec;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.junit.Test;

public class RefineMergeTest {

	private final AbstractMerger merger = new AbstractMerger() {
		public boolean isMergerFor(Diff target) {
			return true;
		}
	};

	/**
	 * If a diff refines another, we have to check if the "macro" diff has to be merged with it.
	 */
	@Test
	public void testRefineOnGetDirectResultingMerges() {

		// Create diffs
		Diff diff1 = new DiffSpec();
		Diff diff2 = new DiffSpec();

		Diff diff3 = new DiffSpec();
		diff3.getRefinedBy().add(diff1);
		diff3.getRefinedBy().add(diff2);

		// First call : diff3 must not to be in the merge dependencies, cause diff2 is not merged.
		Set<Diff> dependencies = merger.getDirectResultingMerges(diff1, true);
		assertTrue(dependencies.isEmpty());
		// Merge diff2
		diff2.setState(DifferenceState.MERGED);
		// Second call : diff3 must be in the merge dependencies, cause diff2 has been merged.
		dependencies = merger.getDirectResultingMerges(diff1, true);
		assertEquals(1, dependencies.size());
		assertTrue(dependencies.contains(diff3));

	}

	@Test
	public void testRefineOnGetDirectResultingMerges_2() {

		// Create diffs
		Diff diff1 = new DiffSpec();
		Diff diff2 = new DiffSpec();
		Diff diff3 = new DiffSpec();

		Diff diff4 = new DiffSpec();
		diff4.getRefinedBy().add(diff1);
		diff4.getRefinedBy().add(diff2);
		diff4.getRefinedBy().add(diff3);

		// First call : diff4 must not to be in the merge dependencies, cause diff2 & diff3 are not merged.
		Set<Diff> dependencies = merger.getDirectResultingMerges(diff1, true);
		assertTrue(dependencies.isEmpty());
		// Merge diff2
		diff2.setState(DifferenceState.MERGED);
		// Second call : diff4 must not to be in the merge dependencies, cause diff3 is not merged.
		dependencies = merger.getDirectResultingMerges(diff1, true);
		assertTrue(dependencies.isEmpty());
		// Merge diff3
		diff3.setState(DifferenceState.MERGED);
		// Third call : diff4 must be in the merge dependencies, cause diff2 & diff3 have been merged.
		dependencies = merger.getDirectResultingMerges(diff1, true);
		assertEquals(1, dependencies.size());
		assertTrue(dependencies.contains(diff4));

	}
}
