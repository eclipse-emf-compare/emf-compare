/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.tests.fullcomparison.data.dynamic.DynamicInstancesInputData;
import org.eclipse.emf.compare.tests.suite.AllTests;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;
import org.junit.Test;

/**
 * A very basic test of comparing and merging dynamic instances.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DynamicInstanceComparisonTest {

	DynamicInstancesInputData data = new DynamicInstancesInputData();

	Resource left;

	Resource right;

	Resource origin;

	@Before
	public void setUp() throws Exception {
		AllTests.fillEMFRegistries();
		left = data.getCompareLeft();
		right = data.getCompareRight();
		origin = data.getCompareRight();
	}

	@Test
	public void compare2Ways() throws IOException {
		Comparison result = EMFCompare.newComparator(EMFCompare.createDefaultScope(left, right)).compare();
		assertEquals("We are supposed to have one difference (ADD/REMOVE of an instance)", 1, result
				.getDifferences().size());
	}

	@Test
	public void compare3Ways() throws IOException {
		Comparison result = EMFCompare.newComparator(EMFCompare.createDefaultScope(left, right, origin))
				.compare();
		assertEquals("We are supposed to have one difference (ADD/REMOVE of an instance)", 1, result
				.getDifferences().size());
	}

	@Test
	public void copyLeftToRight() throws IOException {
		EMFCompare comparator = EMFCompare.newComparator(EMFCompare.createDefaultScope(left, right));
		Comparison result = comparator.compare();
		assertEquals("We are supposed to have one difference (ADD/REMOVE of an instance)", 1, result
				.getDifferences().size());
		for (Diff diff : result.getDifferences()) {
			diff.copyLeftToRight();
		}
		assertEquals("We are supposed to have no difference as we merged everything", 0, comparator.compare()
				.getDifferences().size());
	}

	@Test
	public void copyRightToLeft() throws IOException {
		EMFCompare comparator = EMFCompare.newComparator(EMFCompare.createDefaultScope(left, right));
		Comparison result = comparator.compare();
		assertEquals("We are supposed to have one difference (ADD/REMOVE of an instance)", 1, result
				.getDifferences().size());
		for (Diff diff : result.getDifferences()) {
			diff.copyRightToLeft();
		}
		assertEquals("We are supposed to have no difference as we merged everything", 0, comparator.compare()
				.getDifferences().size());
	}

}
