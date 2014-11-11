/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.diff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.diff.data.FeatureMapMoveDiffInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests the DiffEngine for its handling of FeatureMap Changes.
 *
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
@SuppressWarnings("nls")
public class FeatureMapMoveDiffTest {

	private FeatureMapMoveDiffInputData input = new FeatureMapMoveDiffInputData();

	/**
	 * Tests a scenario in which an element is moved from one FeatureMap to another whereby the Nodes
	 * containing the FeatureMaps are also added / removed. For this case the DiffEngine must calculate
	 * additional FeatureMapChanges to keep the order of the elements during merging.
	 *
	 * @throws IOException
	 *             if {@link FeatureMapMoveDiffInputData} fails to load the test models.
	 */
	@Test
	public void testFeatureMapMoveDiffs() throws IOException {
		final Resource left = input.getFeatureMapMoveLeft();
		final Resource right = input.getFeatureMapMoveRight();

		final IComparisonScope scope = new DefaultComparisonScope(right, left, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// should detect 6 differences when diffengine does not cut off featuremap changes
		final List<Diff> differences = comparison.getDifferences();
		assertEquals(6, differences.size());

		// 2 of them should be move reference changes
		final List<Diff> moveDifferences = new ArrayList<Diff>(2);
		for (Diff diff : differences) {
			if (diff instanceof ReferenceChange && diff.getKind() == DifferenceKind.MOVE) {
				moveDifferences.add(diff);
			}
		}
		assertEquals(2, moveDifferences.size());

		// check if move reference changes are accompanied with featuremap changes
		for (Diff mdiff : moveDifferences) {
			// moveDiff itself + featuremap change
			assertEquals(2, mdiff.getEquivalence().getDifferences().size());

			boolean foundFeatureMapChange = false;
			for (Diff equivalent : mdiff.getEquivalence().getDifferences()) {
				if (equivalent != mdiff) {
					assertTrue(equivalent instanceof FeatureMapChange);
					foundFeatureMapChange = true;
				}
			}
			if (!foundFeatureMapChange) {
				fail("ReferenceChange should have FeatureMapChange as equivalent");
			}
		}
	}

}
