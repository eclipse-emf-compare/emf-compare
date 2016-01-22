/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Abstract test class for merge tests. The class create a comparison for the given resources, merge given
 * diffs to merge in the correct side then assert that the result is equal to a given resource. The
 * responsibility of finding the diffs to merge is given to concrete classes since this is specific to each
 * test.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public abstract class AbstractMergerTest {

	protected Resource origin;

	protected Resource left;

	protected Resource right;

	protected Resource expected;

	protected boolean rightToLeft;

	protected Registry mergerRegistry;

	protected Comparison comparison;

	public AbstractMergerTest(final Resource origin, final Resource left, final Resource right,
			final boolean rightToLeft, final Resource expected, IMerger.Registry mergerRegistry) {
		this.origin = checkNotNull(origin);
		this.left = checkNotNull(left);
		this.right = checkNotNull(right);
		this.expected = checkNotNull(expected);
		this.rightToLeft = rightToLeft;
		this.mergerRegistry = checkNotNull(mergerRegistry);
	}

	/**
	 * This class is implemented by client to get the diffs to merge for each specific test.
	 * 
	 * @return the list of diffs to merge
	 */
	protected abstract List<Diff> getDiffsToMerge();

	@Test
	public void runMergeTest() {
		createComparison();
		List<Diff> diffsToMerge = getDiffsToMerge();
		mergeAndCompare(diffsToMerge);
	}

	/**
	 * Create the initial comparison.
	 */
	protected void createComparison() {
		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		comparison = EMFCompare.builder().build().compare(scope);
	}

	/**
	 * Merge the given diffs and assert that the result is identical with the expected result resource.
	 * 
	 * @param diffsToMerge
	 *            The list of diffs to merge
	 */
	protected void mergeAndCompare(List<Diff> diffsToMerge) {
		for (Diff diff : diffsToMerge) {
			if (rightToLeft) {
				mergerRegistry.getHighestRankingMerger(diff).copyRightToLeft(diff, new BasicMonitor());
			} else {
				mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor());
			}
		}

		IComparisonScope scope;
		if (rightToLeft) {
			scope = new DefaultComparisonScope(left, expected, null);
		} else {
			scope = new DefaultComparisonScope(right, expected, null);
		}

		comparison = EMFCompare.builder().build().compare(scope);
		assertEquals(0, comparison.getDifferences().size());
	}

}
