/*******************************************************************************
 * Copyright (c) 2014, 2015 EclipseSource Muenchen GmbH and others.
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
				+ "I asked, what do they call it?" + NL // changed
				+ "Do you hear me?" + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Alright." + NL // added
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String merged = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "I asked, what do they call it?" + NL // changed right
				+ "Do you hear me?" + NL // added right
				+ "They call it a \"Royale with Cheese\"." + NL // changed left
				+ "Alright." + NL // added right
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
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
	public void addDifferentLineAtTheBeginningInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "Whose chopper is this?" + NL // added
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String right = "Who's Zed?" + NL // added
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addDifferentLineAtTheEndOfMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Whose chopper is this?" + NL; // added

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Who's Zed?" + NL; // added

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addDifferentMultipleLinesAtTheBeginningInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "Whose chopper is this?" + NL // added same as right
				+ "It's Zed's!" + NL // added same as right
				+ "Who is Zed?" + NL // added different from right
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String right = "Whose chopper is this?" + NL // added same as left
				+ "It's Zed's!" + NL // added same as left
				+ "Who's Zed?" + NL // added different from left
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addSameMultipleLinesAtTheBeginningOfFirstLineInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "Whose chopper is this?" + NL // added same as right
				+ "It's Zed's!" + NL // added same as right
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String right = "Whose chopper is this?" + NL // added same as left
				+ "It's Zed's!" + NL // added same as left
				+ "Who's Zed?" + NL // added additionally to left
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		// The merge result should be the right side, since right inserts an additional line and the rest are
		// equal insertions
		final String merged = right;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void addSameMultipleLinesAtTheBeginningAndChangeOfFirstLineInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "Whose chopper is this?" + NL // added same as right
				+ "It's Zed's!" + NL // added same as right
				+ "They do not call it a Quarter Pounder with Cheese?" + NL // changed
				+ "They call it a Royale with Cheese.";

		final String right = "Whose chopper is this?" + NL // added same as left
				+ "It's Zed's!" + NL // added same as left
				+ "Who's Zed?" + NL // added additionally to left
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String merged = "Whose chopper is this?" + NL // added
				+ "It's Zed's!" + NL // added
				+ "Who's Zed?" + NL // added additionally to left
				+ "They do not call it a Quarter Pounder with Cheese?" + NL // changed left
				+ "They call it a Royale with Cheese.";

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void addSameMultipleLinesAtTheBeginningAndChangeOfFirstLine2InMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "Whose chopper is this?" + NL // added same as right
				+ "It's Zed's!" + NL // added same as right
				+ "They don't call it a Quarter Pounder with Cheese?" + NL
				+ "They call it a Royale with Cheese.";

		final String right = "Whose chopper is this?" + NL // added same as left
				+ "It's Zed's!" + NL // added same as left
				+ "Who's Zed?" + NL // added additionally to left
				+ "They do not call it a Quarter Pounder with Cheese?" + NL // changed
				+ "They call it a Royale with Cheese.";

		final String merged = "Whose chopper is this?" + NL // added
				+ "It's Zed's!" + NL // added
				+ "Who's Zed?" + NL // added additionally to left
				+ "They do not call it a Quarter Pounder with Cheese?" + NL // changed left
				+ "They call it a Royale with Cheese.";

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void addDifferentLinesAtTheBeginningInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "Whose chopper is this?" + NL // added
				+ "Whose chopper is this?" + NL // added
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String right = "Who's Zed?" + NL // added
				+ "It's Zed's." + NL // added
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		assertConflictingBidirectional(origin, left, right);
	}

	@Test
	public void addSameLineAtTheBeginningInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String left = "Who's Zed?" + NL // added
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String right = "Who's Zed?" + NL // added
				+ "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese.";

		final String merged = right;

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void changeAndAddDifferentLinesOnBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system.1" + NL // added
				+ "Nah, they got the metric system.2" + NL // added
				+ "Nah, they got the metric system.3" + NL // added
				+ "They call it a \"Royale\" with Cheese." + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "What do they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL//
				+ "Royale with Cheese." + NL //
				+ "That's right!"; // changed

		final String merged = "What do they call it?" + NL // changed line 1 from right
				+ "Nah, they got the metric system.1" + NL // added left
				+ "Nah, they got the metric system.2" + NL // added left
				+ "Nah, they got the metric system.3" + NL // added left
				+ "They call it a \"Royale\" with Cheese." + NL// changed line 3 from left
				+ "Royale with Cheese." + NL // unchanged on both sides
				+ "That's right!"; // changed right

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
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
	public void addSameLineAtSameLineOnBothSidesInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL // added
				+ "Royale with Cheese." + NL //
				+ "That's right!!!"; // changed

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL // added
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String merged = left; // right didn't contribute a unique change

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void changeSameLineToSameContentOnBothSidesInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese!!!" + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right!!!" + NL // changed
				+ "They call it a Royale with Cheese!!!"; // added

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese!!!" + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String merged = left; // right didn't contribute a unique change

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void removeAndChangeVersusSameChangeInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right!"; // changed

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right!"; // changed

		final String merged = left; // right didn't contribute a unique change

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void removeSameLineOnBothSidesInMultiLineText() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "" // removed
				+ "Royale with Cheese." + NL //
				+ "That's right!!!"; // changed

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "" // removed
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String merged = left; // right didn't contribute a unique change

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

		final String merged = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL // added
																										// left
				+ "" // removed right
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?"; // added right

		assertNonConflictingBidirectional(origin, left, right);
		assertMergedBidirectional(origin, left, right, merged);
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
