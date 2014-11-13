/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.opaque;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Predicate;

import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.opaque.data.OpaqueInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.OpaqueAction;
import org.junit.Test;

/**
 * Tests the detection of {@link OpaqueElementBodyChange opaque element body changes}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings("nls")
public class OpaqueElementBodyChangeDiffTest extends AbstractUMLTest {

	private static final Predicate<Diff> IS_OPAQUE_ELEMENT_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return diff instanceof OpaqueElementBodyChange;
		}
	};

	private static final String OPAQUE_ACTION1_ID = "_opaqueAction1";

	private OpaqueInputData input = new OpaqueInputData();

	@Test
	public void testA1UseCaseLeft() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();

		Comparison comparison = compare(left, origin, null);
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> opaqueChanges = filter(diffs, IS_OPAQUE_ELEMENT_CHANGE);
		assertEquals(1, size(opaqueChanges));

		Diff firstDiff = opaqueChanges.iterator().next();
		OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)firstDiff;

		assertEquals(DifferenceKind.CHANGE, bodyChange.getKind());
		assertEquals(DifferenceSource.LEFT, bodyChange.getSource());
		assertEquals(2, bodyChange.getRefinedBy().size());
		assertEquals(1, size(filter(bodyChange.getRefinedBy(), ofKind(ADD))));
		assertEquals(1, size(filter(bodyChange.getRefinedBy(), ofKind(DELETE))));
		assertEquals(2, size(filter(bodyChange.getRefinedBy(), fromSide(LEFT))));
	}

	@Test
	public void testA1UseCase() throws IOException {
		Resource origin = input.getA1Origin();
		Resource left = input.getA1Left();
		Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, origin);
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftOpaqueChanges = filter(diffs, and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(LEFT)));
		assertEquals(1, size(leftOpaqueChanges));

		Diff firstLeftDiff = leftOpaqueChanges.iterator().next();
		OpaqueElementBodyChange leftBodyChange = (OpaqueElementBodyChange)firstLeftDiff;

		assertEquals(DifferenceKind.CHANGE, leftBodyChange.getKind());
		assertEquals(DifferenceSource.LEFT, leftBodyChange.getSource());
		assertEquals(2, leftBodyChange.getRefinedBy().size());
		assertEquals(0, size(filter(leftBodyChange.getRefinedBy(), fromSide(RIGHT))));
		assertEquals(2, size(filter(leftBodyChange.getRefinedBy(), fromSide(LEFT))));
		assertEquals(1, size(filter(leftBodyChange.getRefinedBy(), ofKind(ADD))));
		assertEquals(1, size(filter(leftBodyChange.getRefinedBy(), ofKind(DELETE))));

		Iterable<Diff> rightOpaqueChanges = filter(diffs, and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(RIGHT)));
		assertEquals(1, size(rightOpaqueChanges));

		Diff firstRightDiff = rightOpaqueChanges.iterator().next();
		OpaqueElementBodyChange rightBodyChange = (OpaqueElementBodyChange)firstRightDiff;

		assertEquals(DifferenceKind.CHANGE, rightBodyChange.getKind());
		assertEquals(DifferenceSource.RIGHT, rightBodyChange.getSource());
		assertEquals(2, rightBodyChange.getRefinedBy().size());
		assertEquals(2, size(filter(rightBodyChange.getRefinedBy(), fromSide(RIGHT))));
		assertEquals(0, size(filter(rightBodyChange.getRefinedBy(), fromSide(LEFT))));
		assertEquals(1, size(filter(rightBodyChange.getRefinedBy(), ofKind(ADD))));
		assertEquals(1, size(filter(rightBodyChange.getRefinedBy(), ofKind(DELETE))));
	}

	@Test
	public void testA5UseCaseLeft() throws IOException {
		Resource origin = input.getA5Origin();
		Resource left = input.getA5Left();

		Comparison comparison = compare(left, origin, null);
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> opaqueChanges = filter(diffs, IS_OPAQUE_ELEMENT_CHANGE);
		assertEquals(1, size(opaqueChanges));

		Diff firstDiff = opaqueChanges.iterator().next();
		OpaqueElementBodyChange bodyChange = (OpaqueElementBodyChange)firstDiff;

		assertEquals(DifferenceKind.ADD, bodyChange.getKind());
		assertEquals(DifferenceSource.LEFT, bodyChange.getSource());
		assertEquals(2, bodyChange.getRefinedBy().size());
		assertEquals(2, size(filter(bodyChange.getRefinedBy(), ofKind(ADD))));
		assertEquals(2, size(filter(bodyChange.getRefinedBy(), fromSide(LEFT))));
	}

	@Test
	public void testA5UseCase() throws IOException {
		Resource origin = input.getA5Origin();
		Resource left = input.getA5Left();
		Resource right = input.getA5Right();

		Comparison comparison = compare(left, right, origin);
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> leftOpaqueChanges = filter(diffs, and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(LEFT)));
		assertEquals(1, size(leftOpaqueChanges));

		Diff firstLeftDiff = leftOpaqueChanges.iterator().next();
		OpaqueElementBodyChange leftBodyChange = (OpaqueElementBodyChange)firstLeftDiff;

		assertEquals(DifferenceKind.ADD, leftBodyChange.getKind());
		assertEquals(DifferenceSource.LEFT, leftBodyChange.getSource());
		assertEquals(2, leftBodyChange.getRefinedBy().size());
		assertEquals(0, size(filter(leftBodyChange.getRefinedBy(), fromSide(RIGHT))));
		assertEquals(2, size(filter(leftBodyChange.getRefinedBy(), fromSide(LEFT))));
		assertEquals(2, size(filter(leftBodyChange.getRefinedBy(), ofKind(ADD))));

		Iterable<Diff> rightOpaqueChanges = filter(diffs, and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(RIGHT)));
		assertEquals(1, size(rightOpaqueChanges));

		Diff firstRightDiff = rightOpaqueChanges.iterator().next();
		OpaqueElementBodyChange rightBodyChange = (OpaqueElementBodyChange)firstRightDiff;

		assertEquals(DifferenceKind.ADD, rightBodyChange.getKind());
		assertEquals(DifferenceSource.RIGHT, rightBodyChange.getSource());
		assertEquals(2, rightBodyChange.getRefinedBy().size());
		assertEquals(2, size(filter(rightBodyChange.getRefinedBy(), fromSide(RIGHT))));
		assertEquals(0, size(filter(rightBodyChange.getRefinedBy(), fromSide(LEFT))));
		assertEquals(2, size(filter(rightBodyChange.getRefinedBy(), ofKind(ADD))));
	}

	@Test
	public void testA7UseCaseRight_MoveAndChange() throws IOException {
		Resource origin = input.getA7Origin();
		Resource right = input.getA7Right();

		Comparison comparison = compare(right, origin, null);
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> opaqueChanges = filter(diffs, and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(LEFT)));
		assertEquals(2, size(opaqueChanges));
		assertEquals(1, size(filter(opaqueChanges, ofKind(MOVE))));
		assertEquals(1, size(filter(opaqueChanges, ofKind(CHANGE))));
	}

	@Test
	public void testA7UseCaseRight_Move() throws IOException {
		Resource origin = input.getA7Origin();
		Resource right = input.getA7Right();

		// Make sure JAVA body is unchanged
		OpaqueAction rightAction = (OpaqueAction)right.getEObject(OPAQUE_ACTION1_ID);
		OpaqueAction originAction = (OpaqueAction)origin.getEObject(OPAQUE_ACTION1_ID);
		String bodyJava = originAction.getBodies().get(0);
		rightAction.getBodies().set(1, bodyJava);

		Comparison comparison = compare(right, origin, null);
		EList<Diff> diffs = comparison.getDifferences();
		Iterable<Diff> opaqueChanges = filter(diffs, and(IS_OPAQUE_ELEMENT_CHANGE, fromSide(LEFT)));
		assertEquals(1, size(opaqueChanges));
		assertEquals(1, size(filter(opaqueChanges, ofKind(MOVE))));

		Diff opaqueChangeDiff = opaqueChanges.iterator().next();
		OpaqueElementBodyChange opaqueChange = (OpaqueElementBodyChange)opaqueChangeDiff;
		assertEquals(2, opaqueChange.getRefinedBy().size());
		assertEquals(2, size(filter(opaqueChange.getRefinedBy(), ofKind(MOVE))));
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

}
