/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.mergeviewer;

import static org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide.LEFT;
import static org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide.RIGHT;

import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.internal.spec.ConflictSpec;
import org.eclipse.emf.compare.internal.spec.DiffSpec;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl.AbstractTableOrTreeMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.jface.viewers.IElementComparer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the quality of merge viewer items in the table and tree content merge viewer. Specifically, this
 * class tests the following requirements:
 * <ul>
 * <li>For pseudo conflicts, we only compare the ancestor side value and the conflict.</li>
 * <li>If neither item has a left or right side value, we only compare their diffs.</li>
 * <li>If only one item has a diff, we only compare the left, right, and ancestor side values.</li>
 * <li>If there is no special handling, we compare everything (left, right, ancestor, diff).</li>
 * <li>For Non-MergeViewerItems use normal object equality.</li>
 * </ul>
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings({"restriction", "nls" })
public class TableOrTreeMergeViewerElementComparerTest {
	/** Stateless element comparer: Handles EMF Compare Viewer Items and defaults to Objects.equal(). */
	private static final IElementComparer ELEMENT_COMPARER = new AbstractTableOrTreeMergeViewer.ElementComparer();

	/**
	 * <p>
	 * Requirement: For pseudo conflicts, we only compare the ancestor side value and the conflict.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | aLeft      | bLeft      |
	 * ----------+------------+------------+
	 * Right     | aRight     | bRight     |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | bDiff      |
	 * ----------+------------+------------+
	 * Conflict  |       Equal PSEUDO      |
	 * ----------+------------+------------+
	 * 
	 * Expected: Equal (special treatment of pseudo conflicts)
	 * </pre>
	 */
	@Test
	public void testEqualPseudoConflictEqualAncestor() {
		Conflict pseudoConflict = createConflict(ConflictKind.PSEUDO);

		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "aLeft", "aRight", "Ancestor");

		Diff bDiff = createDiff(pseudoConflict);
		IMergeViewerItem rightItem = createItem(RIGHT, bDiff, "bLeft", "bRight", "Ancestor");

		assertEquals(leftItem, rightItem, true);
	}

	/**
	 * <p>
	 * Requirement: For pseudo conflicts, we only compare the ancestor side value and the conflict.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | aLeft      | bLeft      |
	 * ----------+------------+------------+
	 * Right     | aRight     | bRight     |
	 * ----------+------------+------------+
	 * Ancestor  | aAncestor  | bAncestor  |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | bDiff      |
	 * ----------+------------+------------+
	 * Conflict  |       Equal PSEUDO      |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (unequal ancestors, so no special treatment of pseudo conflict)
	 * </pre>
	 * 
	 * @see #testEqualPseudoConflictEqualAncestor()
	 */
	@Test
	public void testEqualPseudoConflictUnequalAncestor() {
		Conflict pseudoConflict = createConflict(ConflictKind.PSEUDO);

		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "aLeft", "aRight", "aAncestor");

		Diff bDiff = createDiff(pseudoConflict);
		IMergeViewerItem rightItem = createItem(RIGHT, bDiff, "bLeft", "bRight", "bAncestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: For pseudo conflicts, we only compare the ancestor side value and the conflict.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | aLeft      | bLeft      |
	 * ----------+------------+------------+
	 * Right     | aRight     | bRight     |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | bDiff      |
	 * ----------+------------+------------+
	 * Conflict  | aPseudo    | bPseudo    |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (unequal conflicts, so no special treatment of pseudo conflict)
	 * </pre>
	 * 
	 * @see #testEqualPseudoConflictEqualAncestor()
	 */
	@Test
	public void testUnequalPseudoConflictEqualAncestor() {
		Conflict aPseudo = createConflict(ConflictKind.PSEUDO);
		Diff aDiff = createDiff(aPseudo);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "aLeft", "aRight", "Ancestor");

		Conflict bPseudo = createConflict(ConflictKind.PSEUDO);
		Diff bDiff = createDiff(bPseudo);
		IMergeViewerItem rightItem = createItem(RIGHT, bDiff, "bLeft", "bRight", "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: For pseudo conflicts, we only compare the ancestor side value and the conflict.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | aLeft      | bLeft      |
	 * ----------+------------+------------+
	 * Right     | aRight     | bRight     |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | bDiff      |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (no special treatment for real conflicts)
	 * </pre>
	 * 
	 * @see #testEqualPseudoConflictEqualAncestor()
	 */
	@Test
	public void testEqualRealConflictEqualAncestor() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);

		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "aLeft", "aRight", "Ancestor");

		Diff bDiff = createDiff(pseudoConflict);
		IMergeViewerItem rightItem = createItem(RIGHT, bDiff, "bLeft", "bRight", "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If neither item has a left or right side value, we only compare their diffs.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | null       | null       |
	 * ----------+------------+------------+
	 * Right     | null       | null       |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      |          Equal          |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Equal (left and right item use equal diffs)
	 * </pre>
	 */
	@Test
	public void testNullSidesEqualDiff() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff diff = createDiff(pseudoConflict);

		IMergeViewerItem leftItem = createItem(LEFT, diff, null, null, "Ancestor");
		IMergeViewerItem rightItem = createItem(RIGHT, diff, null, null, "Ancestor");

		assertEquals(leftItem, rightItem, true);
	}

	/**
	 * <p>
	 * Requirement: If neither item has a left or right side value, we only compare their diffs.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | null       | null       |
	 * ----------+------------+------------+
	 * Right     | null       | null       |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | bDiff      |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (left and right item use unequal diffs)
	 * </pre>
	 */
	@Test
	public void testNullSidesUnequalDiff() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);

		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, null, null, "Ancestor");

		Diff bDiff = createDiff(pseudoConflict);
		IMergeViewerItem rightItem = createItem(RIGHT, bDiff, null, null, "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If only one item has a diff, we only compare the left, right, and ancestor side values.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |          Equal          |
	 * ----------+------------+------------+
	 * Right     |          Equal          |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | null       |
	 * ----------+------------+------------+
	 * Conflict  |          REAL           |
	 * ----------+------------+------------+
	 * 
	 * Expected: Equal (left, right and ancestor side values are equal)
	 * </pre>
	 */
	@Test
	public void testOnlyOneSideDiffEqualLeftRightAncestor() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "Left", "Right", "Ancestor");

		IMergeViewerItem rightItem = createItem(RIGHT, null, "Left", "Right", "Ancestor");

		assertEquals(leftItem, rightItem, true);
	}

	/**
	 * <p>
	 * Requirement: If only one item has a diff, we only compare the left, right, and ancestor side values.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | aLeft      | bLeft      |
	 * ----------+------------+------------+
	 * Right     |          Equal          |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | null       |
	 * ----------+------------+------------+
	 * Conflict  |          REAL           |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (left side values not equal)
	 * </pre>
	 */
	@Test
	public void testOnlyOneSideDiffEqualRightAncestorUnequalLeft() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "aLeft", "Right", "Ancestor");

		IMergeViewerItem rightItem = createItem(RIGHT, null, "bLeft", "Right", "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If only one item has a diff, we only compare the left, right, and ancestor side values.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |          Equal          |
	 * ----------+------------+------------+
	 * Right     | aRight     | bRight     |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | null       |
	 * ----------+------------+------------+
	 * Conflict  |          REAL           |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (right side values not equal)
	 * </pre>
	 */
	@Test
	public void testOnlyOneSideDiffEqualLeftAncestorUnequalRight() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "Left", "aRight", "Ancestor");

		IMergeViewerItem rightItem = createItem(RIGHT, null, "Left", "bRight", "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If only one item has a diff, we only compare the left, right, and ancestor side values.
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |          Equal          |
	 * ----------+------------+------------+
	 * Right     |          Equal          |
	 * ----------+------------+------------+
	 * Ancestor  | aAncestor  | bAncestor  |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | null       |
	 * ----------+------------+------------+
	 * Conflict  |          REAL           |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (ancestor side values not equal)
	 * </pre>
	 */
	@Test
	public void testOnlyOneSideDiffEqualLeftRightUnequalAncestor() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "Left", "Right", "aAncestor");

		IMergeViewerItem rightItem = createItem(RIGHT, null, "Left", "Right", "bAncestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If there is no special handling, we compare everything (left, right, ancestor, diff).
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |          Equal          |
	 * ----------+------------+------------+
	 * Right     |          Equal          |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      |          Equal          |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Equal (left, right, ancestor and diff are equal)
	 * </pre>
	 */
	@Test
	public void testAllEqual() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "Left", "Right", "Ancestor");
		IMergeViewerItem rightItem = createItem(RIGHT, aDiff, "Left", "Right", "Ancestor");

		assertEquals(leftItem, rightItem, true);
	}

	/**
	 * <p>
	 * Requirement: If there is no special handling, we compare everything (left, right, ancestor, diff).
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |          Equal          |
	 * ----------+------------+------------+
	 * Right     |          Equal          |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      | aDiff      | bDiff      |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (diffs not equal)
	 * </pre>
	 */
	@Test
	public void testAllEqualExceptDiff() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);

		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "Left", "Right", "Ancestor");

		Diff bDiff = createDiff(pseudoConflict);
		IMergeViewerItem rightItem = createItem(RIGHT, bDiff, "Left", "Right", "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If there is no special handling, we compare everything (left, right, ancestor, diff).
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      | aLeft      | bLeft      |
	 * ----------+------------+------------+
	 * Right     |          Equal          |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      |          Equal          |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (left values not equal)
	 * </pre>
	 */
	@Test
	public void testAllEqualExceptLeft() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "aLeft", "Right", "Ancestor");
		IMergeViewerItem rightItem = createItem(RIGHT, aDiff, "bLeft", "Right", "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If there is no special handling, we compare everything (left, right, ancestor, diff).
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |          Equal          |
	 * ----------+------------+------------+
	 * Right     | aRight     | bRight     |
	 * ----------+------------+------------+
	 * Ancestor  |          Equal          |
	 * ----------+------------+------------+
	 * Diff      |          Equal          |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (right values not equal)
	 * </pre>
	 */
	@Test
	public void testAllEqualExceptRight() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "Left", "aRight", "Ancestor");
		IMergeViewerItem rightItem = createItem(RIGHT, aDiff, "Left", "bRight", "Ancestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If there is no special handling, we compare everything (left, right, ancestor, diff).
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |          Equal          |
	 * ----------+------------+------------+
	 * Right     |          Equal          |
	 * ----------+------------+------------+
	 * Ancestor  | aAncestor  | bAncestor  |
	 * ----------+------------+------------+
	 * Diff      |          Equal          |
	 * ----------+------------+------------+
	 * Conflict  |        Equal REAL       |
	 * ----------+------------+------------+
	 * 
	 * Expected: Not Equal (ancestor values not equal)
	 * </pre>
	 */
	@Test
	public void testAllEqualExceptAncestor() {
		Conflict pseudoConflict = createConflict(ConflictKind.REAL);
		Diff aDiff = createDiff(pseudoConflict);
		IMergeViewerItem leftItem = createItem(LEFT, aDiff, "Left", "Right", "aAncestor");
		IMergeViewerItem rightItem = createItem(RIGHT, aDiff, "Left", "Right", "bAncestor");

		assertEquals(leftItem, rightItem, false);
	}

	/**
	 * <p>
	 * Requirement: If there is no special handling, we compare everything (left, right, ancestor, diff).
	 * </p>
	 * Tests the equality of two items using the following setup:
	 * 
	 * <pre>
	 *           |   Item A   |  Item B    |
	 * ----------+------------+------------+
	 * Left      |       Equal Null        |
	 * ----------+------------+------------+
	 * Right     |       Equal Null        |
	 * ----------+------------+------------+
	 * Ancestor  |       Equal Null        |
	 * ----------+------------+------------+
	 * Diff      |       Equal Null        |
	 * ----------+------------+------------+
	 * Conflict  |            -            |
	 * ----------+------------+------------+
	 * 
	 * Expected: Equal (left, right, ancestor and diff are equal)
	 * </pre>
	 */
	@Test
	public void testAllEqualNull() {
		IMergeViewerItem leftItem = createItem(LEFT, null, null, null, null);
		IMergeViewerItem rightItem = createItem(RIGHT, null, null, null, null);

		assertEquals(leftItem, rightItem, true);
	}

	/**
	 * <p>
	 * Requirement: For Non-MergeViewerItems, use normal object equality.
	 * </p>
	 */
	@Test
	public void testFallbackSameObject() {
		String left = "a";
		assertEquals(left, left, true);
	}

	/**
	 * <p>
	 * Requirement: For Non-MergeViewerItems, use normal object equality.
	 * </p>
	 */
	@Test
	public void testFallbackEqualObject() {
		String left = "a";
		String right = "a";
		assertEquals(left, right, true);
	}

	/**
	 * <p>
	 * Requirement: For Non-MergeViewerItems, use normal object equality.
	 * </p>
	 */
	@Test
	public void testFallbackUnequalObjects() {
		String left = "a";
		String right = "b";
		assertEquals(left, right, false);
	}

	/**
	 * Creates a new conflict with the given conflict kind.
	 * 
	 * @param kind
	 *            conflict kind
	 * @return a new conflict instance
	 */
	protected Conflict createConflict(ConflictKind kind) {
		Conflict conflict = new ConflictSpec();
		conflict.setKind(kind);
		return conflict;
	}

	/**
	 * Creates a new diff with the given conflict set.
	 * 
	 * @param conflict
	 *            conflict
	 * @return a new diff instance
	 */
	protected Diff createDiff(Conflict conflict) {
		Diff diff = new DiffSpec();
		diff.setConflict(conflict);
		return diff;
	}

	/**
	 * Creates a new merge viewer item.
	 * 
	 * @param side
	 *            merge viewer side
	 * @param diff
	 *            diff
	 * @param left
	 *            value of the left side
	 * @param right
	 *            value of the right side
	 * @param ancestor
	 *            value of the ancestor side
	 * @return a new merge viewer item instance
	 */
	protected IMergeViewerItem createItem(MergeViewerSide side, Diff diff, final Object left,
			final Object right, final Object ancestor) {
		IMergeViewerItem item = new MergeViewerItem(null, diff, left, right, ancestor, side, null) {
			@Override
			public String toString() {
				return left + " - " + right + " - " + ancestor;
			}
		};
		return item;
	}

	/**
	 * Asserts that the given objects have the expected equality relation in both directions.
	 * 
	 * <pre>
	 * expected == (a equals b) == (b equals a)
	 * </pre>
	 * 
	 * @param a
	 *            element
	 * @param b
	 *            element
	 * @param expected
	 *            expected equality
	 */
	@SuppressWarnings({"boxing" })
	protected void assertEquals(Object a, Object b, boolean expected) {
		Assert.assertEquals("{" + a + "} equals {" + b + "}.", expected, ELEMENT_COMPARER.equals(a, b));
		Assert.assertEquals("{" + b + "} equals {" + a + "}.", expected, ELEMENT_COMPARER.equals(b, a));
	}
}
