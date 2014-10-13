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
		assertEquals(0, comparison.getConflicts().size());

		final List<Diff> rightDiff = getDiffsForSource(comparison.getDifferences(), DifferenceSource.LEFT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(rightDiff, new BasicMonitor());

		final String attributeValue = scenario.getLeftAttributeValue();
		assertEquals(nullIfEmpty(origin), nullIfEmpty(attributeValue));
	}

	private void assertRejectRight(String origin, String left, String right) throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		assertEquals(0, comparison.getConflicts().size());

		final List<Diff> rightDiff = getDiffsForSource(comparison.getDifferences(), DifferenceSource.RIGHT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(rightDiff, new BasicMonitor());

		final String attributeValue = scenario.getRightAttributeValue();
		assertEquals(nullIfEmpty(origin), nullIfEmpty(attributeValue));
	}

	private void assertMergedLeftToRight(String origin, String left, String right, String merged)
			throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		assertEquals(0, comparison.getConflicts().size());

		final List<Diff> rightDiff = getDiffsForSource(comparison.getDifferences(), DifferenceSource.LEFT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllLeftToRight(rightDiff, new BasicMonitor());

		final String attributeValue = scenario.getRightAttributeValue();
		assertEquals(nullIfEmpty(merged), nullIfEmpty(attributeValue));
	}

	private void assertMergedRightToLeft(String origin, String left, String right, String merged)
			throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		assertEquals(0, comparison.getConflicts().size());

		final List<Diff> rightDiff = getDiffsForSource(comparison.getDifferences(), DifferenceSource.RIGHT);
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		merger.copyAllRightToLeft(rightDiff, new BasicMonitor());

		final String attributeValue = scenario.getLeftAttributeValue();
		assertEquals(nullIfEmpty(merged), nullIfEmpty(attributeValue));
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
