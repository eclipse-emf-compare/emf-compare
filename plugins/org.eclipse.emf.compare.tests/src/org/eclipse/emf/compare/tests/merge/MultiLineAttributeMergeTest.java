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
package org.eclipse.emf.compare.tests.merge;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests merging of concurrent changes of multi-line string attributes.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings("nls")
public class MultiLineAttributeMergeTest {

	private static final String NL = "\n";

	private IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void changeSingleLineTextOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = "They do not call it a Quarter Pounder with Cheese?"; // changed
		final String merged = right;

		assertRejectedAndMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void addSingleLineTextOnOneSide() throws IOException {
		final String origin = "";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = "";
		final String merged = left;

		assertRejectedAndMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void unsetSingleLineTextOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = null; // unset
		final String merged = right;

		assertRejectedAndMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void removeSingleLineTextOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?";
		final String left = "They don't call it a Quarter Pounder with Cheese?";
		final String right = ""; // removed
		final String merged = null; // empty String is interpreted as unset by EMF Compare

		assertRejectedAndMergedBidirectional(origin, left, right, merged);
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

		assertRejectedAndMergedBidirectional(origin, left, right, merged);
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

		assertRejectedAndMergedBidirectional(origin, left, right, merged);
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

		assertRejectedAndMergedBidirectional(origin, left, right, merged);
	}

	@Test
	public void acceptingLeftInsertOnRightSideAndRejectingRightInsertOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system." + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "What do they call a Big Mac?" + NL // added
				+ "That's right.";

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftInsertOnRightSideAndRejectingRightDeleteOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system." + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "" // deleted
				+ "That's right.";

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftInsertOnRightSideAndRejectingRightChangeOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system." + NL // added
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right!!!!"; // changed

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftChangeOnRightSideAndRejectingRightInsertOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese!!!" + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "What do they call a Big Mac?" + NL// added
				+ "That's right."; //

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftChangeOnRightSideAndRejectingRightDeleteOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese!!!" + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "" // deleted
				+ "What do they call a Big Mac?";

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftChangeOnRightSideAndRejectingRightChangeOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese!!!" + NL // changed
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?????"; // changed

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftDeleteOnRightSideAndRejectingRightInsertOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "" // deleted
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "That's freaking right." + NL // added
				+ "What do they call a Big Mac?";

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftDeleteOnRightSideAndRejectingRightChangeOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "" // deleted
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?????"; // changed

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	@Test
	public void acceptingLeftDeleteOnRightSideAndRejectingRightDeleteOnRightSideAfterwards()
			throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "" // deleted
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "" // deleted
				+ "What do they call a Big Mac?"; // changed

		assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(origin, left, right);
		assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(origin, right, left);
	}

	private void assertAcceptingLeftOnRightSideAndRejectingRightOnRightSideIsLeftValue(final String origin,
			final String left, final String right) throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		acceptLeft(comparison);
		rejectRight(comparison);

		final String rightValue = scenario.getRightAttributeValue();
		assertEquals(nullIfEmpty(left), nullIfEmpty(rightValue));
	}

	private void assertAcceptingRightOnLeftSideAndRejectingLeftOnLeftSideIsRightValue(final String origin,
			final String left, final String right) throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		acceptRight(comparison);
		rejectLeft(comparison);

		final String leftValue = scenario.getLeftAttributeValue();
		assertEquals(nullIfEmpty(right), nullIfEmpty(leftValue));
	}

	private void assertRejectedAndMergedBidirectional(String origin, String left, String right, String merged)
			throws IOException {
		assertRejectLeft(origin, left, right);
		assertRejectRight(origin, left, right);
		assertMergedLeftToRight(origin, left, right, merged);
		assertMergedRightToLeft(origin, left, right, merged);
	}

	private void assertRejectLeft(String origin, String left, String right) throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		rejectLeft(comparison);
		final String attributeValue = scenario.getLeftAttributeValue();
		assertEquals(nullIfEmpty(origin), nullIfEmpty(attributeValue));
	}

	private void assertRejectRight(String origin, String left, String right) throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		rejectRight(comparison);
		final String attributeValue = scenario.getRightAttributeValue();
		assertEquals(nullIfEmpty(origin), nullIfEmpty(attributeValue));
	}

	private void assertMergedLeftToRight(String origin, String left, String right, String merged)
			throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		acceptLeft(comparison);
		final String attributeValue = scenario.getRightAttributeValue();
		assertEquals(nullIfEmpty(merged), nullIfEmpty(attributeValue));
	}

	private void assertMergedRightToLeft(String origin, String left, String right, String merged)
			throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		acceptRight(comparison);
		final String attributeValue = scenario.getLeftAttributeValue();
		assertEquals(nullIfEmpty(merged), nullIfEmpty(attributeValue));
	}

	private void rejectLeft(Comparison comparison) {
		final List<Diff> leftDiffs = getDiffsForSource(comparison.getDifferences(), DifferenceSource.LEFT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(leftDiffs, new BasicMonitor());
	}

	private void rejectRight(Comparison comparison) {
		final List<Diff> rightDiffs = getDiffsForSource(comparison.getDifferences(), DifferenceSource.RIGHT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(rightDiffs, new BasicMonitor());
	}

	private void acceptRight(Comparison comparison) {
		final List<Diff> rightDiffs = getDiffsForSource(comparison.getDifferences(), DifferenceSource.RIGHT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(rightDiffs, new BasicMonitor());
	}

	private void acceptLeft(Comparison comparison) {
		final List<Diff> leftDiffs = getDiffsForSource(comparison.getDifferences(), DifferenceSource.LEFT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(leftDiffs, new BasicMonitor());
	}

	private List<Diff> getDiffsForSource(List<Diff> differences, DifferenceSource source) {
		List<Diff> diffsForSource = new ArrayList<Diff>();
		for (Diff diff : differences) {
			if (source.equals(diff.getSource())) {
				diffsForSource.add(diff);
			}
		}
		return diffsForSource;
	}

	private ThreeWayAttributeMergeScenario createMergeScenario(String origin, String left, String right)
			throws IOException {
		return new ThreeWayAttributeMergeScenario(origin, left, right);
	}

	private Comparison compare(final ThreeWayAttributeMergeScenario scenario) {
		final Resource origin = scenario.getOriginResource();
		final Resource left = scenario.getLeftResource();
		final Resource right = scenario.getRightResource();

		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);
		return comparison;
	}

	private String nullIfEmpty(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		} else {
			return string;
		}
	}
}
