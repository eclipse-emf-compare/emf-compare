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
package org.eclipse.emf.compare.diagram.papyrus.tests.merge.sysml;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class MergeDiffInvolvingRefineDiffTest extends AbstractTest {

	private DiffInvolvingRefineDiffInputData input = new DiffInvolvingRefineDiffInputData();

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	@Test
	@Ignore("Mergers should not be aware of the cascading filter.")
	public void testMergeRightToLeftWithSubDiffsAwareOption() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differences = comparisonBefore.getDifferences();
		final IMerger.Registry registry = IMerger.RegistryImpl.createStandaloneInstance();
		final IMerger umlMerger = new UMLMerger();
		umlMerger.setRanking(11);
		registry.add(umlMerger);

		assertEquals(6, differences.size());

		Predicate<? super Diff> removedFromReference = removedFromReference("SysMLmodel.InternalBlock.Block1",
				"ownedConnector", "SysMLmodel.InternalBlock.Block1.Connector2");
		final Diff diff = Iterators.find(differences.iterator(), removedFromReference);

		BatchMerger bm = new BatchMerger(getMergerRegistry());
		bm.copyAllRightToLeft(Arrays.asList(diff), new BasicMonitor());
		// IMerger merger = registry.getHighestRankingMerger(diff);
		// if (merger instanceof IMergeOptionAware) {
		// ((IMergeOptionAware)merger).getMergeOptions().put(AbstractMerger.SUB_DIFF_AWARE_OPTION,
		// Boolean.TRUE);
		// }
		// merger.copyRightToLeft(diff, null);

		final Comparison comparisonAfter = getCompare().compare(scope);
		// The subdiffs are merged with the selected diff (and the dependencies of these subdiffs)
		assertTrue("Comparison#getDifferences() must be empty after copyRightToLeft",
				comparisonAfter.getDifferences().isEmpty());
	}

	@Test
	public void testMergeRightToLeftWithoutSubDiffsAwareOption() throws IOException {
		final Resource left = input.getLeft();
		final Resource right = input.getRight();
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		final Comparison comparisonBefore = getCompare().compare(scope);
		EList<Diff> differences = comparisonBefore.getDifferences();
		final IMerger.Registry registry = IMerger.RegistryImpl.createStandaloneInstance();
		final IMerger umlMerger = new UMLMerger();
		umlMerger.setRanking(11);
		registry.add(umlMerger);

		assertEquals(6, differences.size());

		Predicate<? super Diff> removedFromReference = removedFromReference("SysMLmodel.InternalBlock.Block1",
				"ownedConnector", "SysMLmodel.InternalBlock.Block1.Connector2");
		final Diff diff = Iterators.find(differences.iterator(), removedFromReference);

		BatchMerger bm = new BatchMerger(getMergerRegistry());
		bm.copyAllRightToLeft(Arrays.asList(diff), new BasicMonitor());
		// IMerger merger = registry.getHighestRankingMerger(diff);
		// if (merger instanceof IMergeOptionAware) {
		// ((IMergeOptionAware)merger).getMergeOptions().put(AbstractMerger.SUB_DIFF_AWARE_OPTION,
		// Boolean.FALSE);
		// }
		// merger.copyRightToLeft(diff, null);

		final Comparison comparisonAfter = getCompare().compare(scope);
		// The subdiffs are not merge, only the selected diff
		assertEquals(5, comparisonAfter.getDifferences().size());
	}
}
