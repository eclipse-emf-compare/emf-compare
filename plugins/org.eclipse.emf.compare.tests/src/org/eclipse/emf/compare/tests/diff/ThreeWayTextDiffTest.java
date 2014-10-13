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
package org.eclipse.emf.compare.tests.diff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.compare.internal.ThreeWayTextDiff;
import org.junit.Test;

/**
 * Tests the {@link ThreeWayTextDiff three-way text differencing utility}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings({"nls", "restriction" })
public class ThreeWayTextDiffTest {

	private static final String NL = "\n";

	@Test
	public void changeSingleLineTextOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = "They do not call it a Quarter Pounder with Cheese?"; // changed
		final String merged = right;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void setSingleLineTextOnOneSide() throws IOException {
		final String origin = null;
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = null;
		final String merged = left;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void addSingleLineTextOnOneSide() throws IOException {
		final String origin = "";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = "";
		final String merged = left;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void changeSingleLineTextOnBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "They don't call it a Quarter Pounder?"; // changed
		final String right = "They do not call it a Quarter Pounder with Cheese?"; // changed

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void unsetSingleLineTextOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = null; // removed
		final String merged = right;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void removeSingleLineTextOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = ""; // removed
		final String merged = right;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void changeAndRemoveSingleLine() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "";
		final String right = "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addDifferentSingleLineTextOnBothSide() throws IOException {
		final String origin = "";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void changeAndRemoveLinesInMultiLineTextOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "" // removed
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String merged = right;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void changeAndRemoveDifferentLinesInMultiLineOnTextBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a \"Big Mac\"?" // changed
				+ ""; // removed

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "" // removed
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String merged = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed right
				+ "They call it a Royale with Cheese." + NL //
				+ "" // removed right
				+ "That's right." + NL //
				+ "What do they call a \"Big Mac\"?" // changed left
				+ ""; // removed left

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void changeSameLineInMultiLineTextOnBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a \"Royale with Cheese\"." + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They'd call it a Royale with Cheese." + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void changeLineAndRemoveSameLineInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a \"Royale with Cheese\"." + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "" // removed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void changeLineAndRemoveSameAreaInMultiLineTextOnBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a \"Royale with Cheese\"." + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "" // deleted
				+ "" // deleted
				+ "" // deleted
				+ "" // deleted
				+ "What _do_ they call a Big Mac?" + NL // changed
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void changeLineAndAddMultipleLinesInSameAreaInMultiLineTextOnBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a \"Royale with Cheese\"." + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "I asked, what do they call it?" + NL //
				+ "Do you hear me?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Alright." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addDifferentLineAtSameLineInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "What do they call it?" + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addDifferentLinesIndirectlyAtSameLineInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL // added
				+ "They call it a \"Royale\" with Cheese." + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "" // removed
				+ "What do they call it?" + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addDifferentLineAtDifferentLineInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?"; // added

		final String merged = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL // added
																										// left
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?"; // added right;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void removeLineAndAddLineAtSameLineInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "" // removed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?"; // added

		assertConflictingBidirectional(origin, left, right);
	}

	private void assertNonConflictingBidirectional(String origin, String left, String right) {
		assertNonConflicting(origin, left, right);
		assertNonConflicting(origin, right, left);
	}

	private void assertNonConflicting(String origin, String left, String right) {
		final ThreeWayTextDiff diff = new ThreeWayTextDiff(origin, left, right);
		assertFalse(diff.isConflicting());
	}

	private void assertConflictingBidirectional(String origin, String left, String right) {
		assertConflicting(origin, left, right);
		assertConflicting(origin, right, left);
	}

	private void assertConflicting(String origin, String left, String right) {
		final ThreeWayTextDiff diff = new ThreeWayTextDiff(origin, left, right);
		assertTrue(diff.isConflicting());
	}

	private void assertMergedBidirectional(String origin, String left, String right, String merged) {
		assertMerged(origin, left, right, merged);
		assertMerged(origin, right, left, merged);
	}

	private void assertMerged(String origin, String left, String right, String merged) {
		final ThreeWayTextDiff diff = new ThreeWayTextDiff(origin, left, right);
		assertEquals(merged, diff.getMerged());
	}

}
