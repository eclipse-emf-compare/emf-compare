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
package org.eclipse.emf.compare.tests.conflict;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.merge.ThreeWayAttributeMergeScenario;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests the conflict detection of concurrent changes of multi-line string attributes.
 * <p>
 * This test covers also the basic cases of single-line attributes to avoid regression.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
@SuppressWarnings("nls")
public class MultiLineAttributeConflictDetectionTest {

	private static final String NL = "\n";

	@Test
	public void multiLineAttributeChangeOnOneSide() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		final String left = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "" // removed
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac."; // added

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?";

		compareAndAssertNoConflictInBothDirections(origin, left, right);
	}

	@Test
	public void conflictingMultiLineAttributeChange_InsertInsertConflict() throws IOException {
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
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it??" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		compareAndAssertConflictInBothDirections(origin, left, right);
	}

	@Test
	public void conflictingMultiLineAttributeChange_MultipleLinesInsertInsert() throws IOException {
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

		compareAndAssertConflictInBothDirections(origin, left, right);
	}

	@Test
	public void conflictingMultiLineAttributeChange_DeleteInsertConflict() throws IOException {
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
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "" // removed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		compareAndAssertConflictInBothDirections(origin, left, right);
	}

	@Test
	public void mergableMultiLineAttributeChangeOnBothSides() throws IOException {
		final String origin = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String left = "" // removed
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What _do_ they call it?" + NL // changed
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "That's right." + NL //
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Mac.";

		final String right = "They don't call it a Quarter Pounder with Cheese?" + NL //
				+ "Nah, they got the metric system, they wouldn't know what a Quarter Pounder is." + NL //
				+ "What do they call it?" + NL //
				+ "They call it a Royale with Cheese." + NL //
				+ "Royale with Cheese." + NL //
				+ "" // removed
				+ "What do they call a Big Mac?" + NL //
				+ "A Big Mac's a Big Mac, but they call it Le Big Maec."; // changed

		compareAndAssertNoConflictInBothDirections(origin, left, right);
	}

	@Test
	public void conflictingSingleLineAttributeChange() throws IOException {
		final Comparison comparison = compare("A", "B", "C");
		assertOneConflict(comparison);
	}

	@Test
	public void conflictingSingleLineAttributeChangeAndDelete() throws IOException {
		final Comparison comparison = compare("A", "", "C");
		assertOneConflict(comparison);
	}

	@Test
	public void singleLineAttributeChangeRightSide() throws IOException {
		final Comparison comparison = compare("A", "A", "C");
		assertNoConflict(comparison);
	}

	@Test
	public void singleLineAttributeChangeLeftSide() throws IOException {
		final Comparison comparison = compare("A", "B", "A");
		assertNoConflict(comparison);
	}

	@Test
	public void singleLineAttributeSet() throws IOException {
		final Comparison comparison = compare(null, "B", null);
		assertNoConflict(comparison);
	}

	@Test
	public void singleLineAttributeUnset() throws IOException {
		final Comparison comparison = compare("A", "A", null);
		assertNoConflict(comparison);
	}

	@Test
	public void singleLineAttributeRemove() throws IOException {
		final Comparison comparison = compare("A", "A", "");
		assertNoConflict(comparison);
	}

	private void compareAndAssertNoConflictInBothDirections(String origin, String left, String right)
			throws IOException {
		final Comparison comparisonLeftRight = compare(origin, left, right);
		assertNoConflict(comparisonLeftRight);

		final Comparison comparisonRightLeft = compare(origin, right, left);
		assertNoConflict(comparisonRightLeft);
	}

	private void compareAndAssertConflictInBothDirections(final String origin, final String left,
			final String right) throws IOException {
		final Comparison comparisonLeftRight = compare(origin, left, right);
		assertOneConflict(comparisonLeftRight);

		final Comparison comparisonRightLeft = compare(origin, right, left);
		assertOneConflict(comparisonRightLeft);
	}

	private Comparison compare(String origin, String left, String right) throws IOException {
		final ThreeWayAttributeMergeScenario scenario = createMergeScenario(origin, left, right);
		final Comparison comparison = compare(scenario);
		return comparison;
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

	private void assertOneConflict(final Comparison comparison) {
		final List<Conflict> conflicts = comparison.getConflicts();
		assertEquals(1, conflicts.size());
	}

	private void assertNoConflict(final Comparison comparison) {
		final List<Conflict> conflicts = comparison.getConflicts();
		assertEquals(0, conflicts.size());
	}
}
