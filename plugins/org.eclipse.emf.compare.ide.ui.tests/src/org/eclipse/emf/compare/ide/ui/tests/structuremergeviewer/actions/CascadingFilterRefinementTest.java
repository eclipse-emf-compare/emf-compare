/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.any;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;
import static org.eclipse.emf.compare.merge.AbstractMerger.SUB_DIFF_AWARE_OPTION;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.containsConflictOfTypes;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.tests.framework.RuntimeTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.eclipse.emf.compare.ide.ui.tests.framework.internal.CompareTestSupport;
import org.eclipse.emf.compare.merge.DiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IDiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMergeOptionAware;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.junit.runner.RunWith;

/**
 * This class tests the influence of the cascading differences filter on the merging of refinement
 * differences. The expectation is that refining and refined differences can only be merged together,
 * independent of the filter setting.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings({"nls" })
@RunWith(RuntimeTestRunner.class)
public class CascadingFilterRefinementTest {

	private static final boolean MERGE_RIGHT_TO_LEFT = true;

	private static final Map<IMergeOptionAware, Object> CACHED_SUBDIFF_OPTIONS = Maps.newHashMap();

	private static final IMerger.Registry MERGER_REGISTRY = EMFCompareRCPPlugin.getDefault()
			.getMergerRegistry();

	public void enableCascadingFilter() {
		setCascadingFilter(true);
	}

	public void disableCascadingFilter() {
		setCascadingFilter(false);
	}

	public void setCascadingFilter(boolean enabled) {
		for (IMergeOptionAware merger : Iterables.filter(MERGER_REGISTRY.getMergers(null),
				IMergeOptionAware.class)) {
			Map<Object, Object> mergeOptions = merger.getMergeOptions();
			Object previousValue = mergeOptions.get(SUB_DIFF_AWARE_OPTION);
			CACHED_SUBDIFF_OPTIONS.put(merger, previousValue);
			mergeOptions.put(SUB_DIFF_AWARE_OPTION, Boolean.valueOf(enabled));
		}
	}

	public void restoreCascadingFilter() {
		// restore previous values
		for (Entry<IMergeOptionAware, Object> entry : CACHED_SUBDIFF_OPTIONS.entrySet()) {
			IMergeOptionAware merger = entry.getKey();
			merger.getMergeOptions().put(SUB_DIFF_AWARE_OPTION, entry.getValue());
		}
	}

	/**
	 * <p>
	 * Tests whether refining/refined diffs are only merged together when the cascading filter is enabled.
	 * Input model:
	 * </p>
	 * <ul>
	 * <li>Origin: Package 'Package1' containing class 'Class1'</li>
	 * <li>Left: Package 'Package1' (class 'Class1' removed)</li>
	 * <li>Right: Package 'Package1' containing class 'Class1' and class 'Class2' subclassing class 'Class1'
	 * (class 'Class2' and generalization added)
	 * </ul>
	 * <p>
	 * In the comparison, we detect a conflict between adding the generalization and the deletion of class
	 * 'Class1'. The generalization (DirectRelationshipChange) is a refinement of adding the generalization to
	 * 'Class2' and setting the generalizations 'general' property to the super class 'Class1'.
	 * </p>
	 * 
	 * @see #testRefinement_NoCascadingFilter(Comparison, CompareTestSupport)
	 */
	@Compare(left = "data/filter/cascading/left.uml", right = "data/filter/cascading/right.uml", ancestor = "data/filter/cascading/ancestor.uml")
	public void testRefinement_CascadingFilterEnabled(final Comparison comparison) {
		try {
			final List<Diff> differences = comparison.getDifferences();
			final List<Conflict> conflicts = comparison.getConflicts();

			// has real conflict
			assertTrue(any(conflicts, containsConflictOfTypes(REAL)));

			// all differences are unresolved
			assertTrue(all(differences, hasState(UNRESOLVED)));

			// enable filter and check refinement
			enableCascadingFilter();
			verifyRefinement(differences, MERGE_RIGHT_TO_LEFT);
		} finally {
			restoreCascadingFilter();
		}
	}

	/**
	 * <p>
	 * Tests whether refining/refined diffs are only merged together when the cascading filter is disabled.
	 * Input model:
	 * </p>
	 * <ul>
	 * <li>Origin: Package 'Package1' containing class 'Class1'</li>
	 * <li>Left: Package 'Package1' (class 'Class1' removed)</li>
	 * <li>Right: Package 'Package1' containing class 'Class1' and class 'Class2' subclassing class 'Class1'
	 * (class 'Class2' and generalization added)
	 * </ul>
	 * <p>
	 * In the comparison, we detect a conflict between adding the generalization and the deletion of class
	 * 'Class1'. The generalization (DirectRelationshipChange) is a refinement of adding the generalization to
	 * 'Class2' and setting the generalizations 'general' property to the super class 'Class1'.
	 * </p>
	 * 
	 * @see #testRefinement_NoCascadingFilter(Comparison, CompareTestSupport)
	 */
	@Compare(left = "data/filter/cascading/left.uml", right = "data/filter/cascading/right.uml", ancestor = "data/filter/cascading/ancestor.uml")
	public void testRefinement_CascadingFilterDisabled(final Comparison comparison) {
		try {
			final List<Diff> differences = comparison.getDifferences();
			final List<Conflict> conflicts = comparison.getConflicts();

			// has real conflict
			assertTrue(any(conflicts, containsConflictOfTypes(REAL)));

			// all differences are unresolved
			assertTrue(all(differences, hasState(UNRESOLVED)));

			// enable filter and check refinement
			disableCascadingFilter();
			verifyRefinement(differences, MERGE_RIGHT_TO_LEFT);
		} finally {
			restoreCascadingFilter();
		}
	}

	/**
	 * Verifies that the calculated resulting merges for each difference contain all refined and refining
	 * differences, i.e., they may only be merged as a group or not at all.
	 * 
	 * @param differences
	 *            differences to check
	 * @param mergeRightToLeft
	 *            merge direction
	 */
	public void verifyRefinement(List<Diff> differences, boolean mergeRightToLeft) {
		for (Diff diff : differences) {
			IDiffRelationshipComputer computer = new DiffRelationshipComputer(MERGER_REGISTRY);
			Set<Diff> resultingMerges = computer.getAllResultingMerges(diff, mergeRightToLeft);
			assertTrue("Not all refined diffs are in resulting merges.",
					resultingMerges.containsAll(diff.getRefines()));
			assertTrue("Not all refining diffs are in resulting merges.",
					resultingMerges.containsAll(diff.getRefinedBy()));
		}
	}
}
