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

import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEGet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.diff.data.pseudoconflict.SingleValuedAttributePseudoConflictInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class SingleValuedAttributePseudoConflictTest {
	private SingleValuedAttributePseudoConflictInputData input = new SingleValuedAttributePseudoConflictInputData();

	@Test
	public void testSingleValuedAttributePseudoConflict() throws IOException {
		Resource left = input.getSingleValueAttributePseudoConflictLeft();
		Resource right = input.getSingleValueAttributePseudoConflictRight();
		Resource origin = input.getSingleValueAttributePseudoConflictOrigin();

		IComparisonScope scope = new DefaultComparisonScope(right, left, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// There are only two differences
		List<Diff> differences = comparison.getDifferences();
		assertEquals(2, differences.size());
		Diff diff1 = differences.get(0);
		Diff diff2 = differences.get(1);

		// They constitute a single pseudo conflict
		assertNotNull(diff1.getConflict());
		assertEquals(diff1.getConflict(), diff2.getConflict());
		Conflict conflict = diff1.getConflict();
		assertEquals(ConflictKind.PSEUDO, conflict.getKind());

		// They are both a CHANGE AttributeChange on opposite sides
		assertTrue(diff1 instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, diff1.getKind());
		assertEquals(DifferenceSource.LEFT, diff1.getSource());
		assertTrue(diff2 instanceof AttributeChange);
		assertEquals(DifferenceKind.CHANGE, diff2.getKind());
		assertEquals(DifferenceSource.RIGHT, diff2.getSource());

		// And they have their own side as the changed value (not the origin side)
		Match container = diff1.getMatch();
		Object leftValue = safeEGet(container.getLeft(), ((AttributeChange)diff1).getAttribute());
		Object rightValue = safeEGet(container.getRight(), ((AttributeChange)diff2).getAttribute());
		assertNotNull(leftValue);
		assertEquals(leftValue, rightValue);
		assertEquals(leftValue, ((AttributeChange)diff1).getValue());
		assertEquals(rightValue, ((AttributeChange)diff2).getValue());
	}
}
