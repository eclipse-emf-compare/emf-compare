/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.tests.implications;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onEObject;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.implications.data.ImplicationsInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class ImplicationsAssociationTest extends AbstractUMLTest {

	// 16 diffs of interest and 4 MultiplicityElementChanges
	private static final int NB_DIFFS = 20;

	private ImplicationsInputData input = new ImplicationsInputData();

	@BeforeClass
	public static void setupClass() {
		fillRegistries();
	}

	@AfterClass
	public static void teardownClass() {
		resetRegistries();
	}

	private DiffsOfInterest getDiffs(Comparison comparison, TestKind kind) {
		final List<Diff> differences = comparison.getDifferences();
		Predicate<? super Diff> addAssociationDescription = null;
		Predicate<? super Diff> addNavigableOwnedEndClass1InAssociationDescription = null;
		Predicate<? super Diff> addNavigableOwnedEndClass2InAssociationDescription = null;
		Predicate<? super Diff> addRefAssociationInPropertyClass1Description = null;
		Predicate<? super Diff> addRefTypeInPropertyClass1Description = null;
		Predicate<? super Diff> addRefAssociationInPropertyClass2Description = null;
		Predicate<? super Diff> addRefTypeInPropertyClass2Description = null;
		Predicate<? super Diff> addLiteralIntegerInClass1Description = null;
		Predicate<? super Diff> addUnlimitedNaturalInClass1Description = null;
		Predicate<? super Diff> addLiteralIntegerInClass2Description = null;
		Predicate<? super Diff> addUnlimitedNaturalInClass2Description = null;
		Predicate<? super Diff> addMemberEndClass1InAssociationDescription = null;
		Predicate<? super Diff> addMemberEndClass2InAssociationDescription = null;
		Predicate<? super Diff> addOwnedEndClass1Description = null;
		Predicate<? super Diff> addOwnedEndClass2Description = null;

		if (kind.equals(TestKind.DELETE)) {
			addAssociationDescription = removed("myModel.class1sToClass2s"); //$NON-NLS-1$
			addNavigableOwnedEndClass1InAssociationDescription = removedFromReference(
					"myModel.class1sToClass2s", "navigableOwnedEnd", "myModel.class1sToClass2s.class1s");
			addNavigableOwnedEndClass2InAssociationDescription = removedFromReference(
					"myModel.class1sToClass2s", "navigableOwnedEnd", "myModel.class1sToClass2s.class2s");
			addRefAssociationInPropertyClass1Description = changedReference(
					"myModel.class1sToClass2s.class1s", "association", "myModel.class1sToClass2s", null);
			addRefTypeInPropertyClass1Description = changedReference("myModel.class1sToClass2s.class1s",
					"type", "myModel.Class1", null);
			addRefAssociationInPropertyClass2Description = changedReference(
					"myModel.class1sToClass2s.class2s", "association", "myModel.class1sToClass2s", null);
			addRefTypeInPropertyClass2Description = changedReference("myModel.class1sToClass2s.class2s",
					"type", "myModel.Class2", null);
			addLiteralIntegerInClass1Description = removedLowerValueIn("myModel.class1sToClass2s.class1s");
			addUnlimitedNaturalInClass1Description = removedUpperValueIn("myModel.class1sToClass2s.class1s");
			addLiteralIntegerInClass2Description = removedLowerValueIn("myModel.class1sToClass2s.class2s");
			addUnlimitedNaturalInClass2Description = removedUpperValueIn("myModel.class1sToClass2s.class2s");
			addMemberEndClass1InAssociationDescription = removedFromReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class1s");
			addMemberEndClass2InAssociationDescription = removedFromReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class2s");
			addOwnedEndClass1Description = removedFromReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class1s");
			addOwnedEndClass2Description = removedFromReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class2s");
		} else {
			addAssociationDescription = added("myModel.class1sToClass2s"); //$NON-NLS-1$
			addNavigableOwnedEndClass1InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"navigableOwnedEnd", "myModel.class1sToClass2s.class1s");
			addNavigableOwnedEndClass2InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"navigableOwnedEnd", "myModel.class1sToClass2s.class2s");
			addRefAssociationInPropertyClass1Description = changedReference(
					"myModel.class1sToClass2s.class1s", "association", null, "myModel.class1sToClass2s");
			addRefTypeInPropertyClass1Description = changedReference("myModel.class1sToClass2s.class1s",
					"type", null, "myModel.Class1");
			addRefAssociationInPropertyClass2Description = changedReference(
					"myModel.class1sToClass2s.class2s", "association", null, "myModel.class1sToClass2s");
			addRefTypeInPropertyClass2Description = changedReference("myModel.class1sToClass2s.class2s",
					"type", null, "myModel.Class2");
			addLiteralIntegerInClass1Description = addedLowerValueIn("myModel.class1sToClass2s.class1s");
			addUnlimitedNaturalInClass1Description = addedUpperValueIn("myModel.class1sToClass2s.class1s");
			addLiteralIntegerInClass2Description = addedLowerValueIn("myModel.class1sToClass2s.class2s");
			addUnlimitedNaturalInClass2Description = addedUpperValueIn("myModel.class1sToClass2s.class2s");
			addMemberEndClass1InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class1s");
			addMemberEndClass2InAssociationDescription = addedToReference("myModel.class1sToClass2s",
					"memberEnd", "myModel.class1sToClass2s.class2s");
			addOwnedEndClass1Description = addedToReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class1s");
			addOwnedEndClass2Description = addedToReference("myModel.class1sToClass2s", "ownedEnd",
					"myModel.class1sToClass2s.class2s");
		}

		DiffsOfInterest diffs = new DiffsOfInterest();
		diffs.addAssociation = Iterators.find(differences.iterator(), addAssociationDescription, null);
		diffs.addNavigableOwnedEndClass1InAssociation = Iterators.find(differences.iterator(),
				addNavigableOwnedEndClass1InAssociationDescription, null);
		diffs.addNavigableOwnedEndClass2InAssociation = Iterators.find(differences.iterator(),
				addNavigableOwnedEndClass2InAssociationDescription, null);
		diffs.addMemberEndClass1InAssociation = Iterators.find(differences.iterator(),
				addMemberEndClass1InAssociationDescription, null);
		diffs.addMemberEndClass2InAssociation = Iterators.find(differences.iterator(),
				addMemberEndClass2InAssociationDescription, null);
		diffs.addOwnedEndClass1InAssociation = Iterators.find(differences.iterator(),
				addOwnedEndClass1Description, null);
		diffs.addOwnedEndClass2InAssociation = Iterators.find(differences.iterator(),
				addOwnedEndClass2Description, null);
		diffs.addUMLAssociation = Iterators.find(differences.iterator(), instanceOf(AssociationChange.class),
				null);
		diffs.addRefAssociationInPropertyClass1 = Iterators.find(differences.iterator(),
				addRefAssociationInPropertyClass1Description, null);
		diffs.addRefTypeInPropertyClass1 = Iterators.find(differences.iterator(),
				addRefTypeInPropertyClass1Description, null);
		diffs.addRefAssociationInPropertyClass2 = Iterators.find(differences.iterator(),
				addRefAssociationInPropertyClass2Description, null);
		diffs.addRefTypeInPropertyClass2 = Iterators.find(differences.iterator(),
				addRefTypeInPropertyClass2Description, null);
		diffs.addLiteralIntegerInClass1 = Iterators.find(differences.iterator(),
				addLiteralIntegerInClass1Description, null);
		diffs.addUnlimitedNaturalInClass1 = Iterators.find(differences.iterator(),
				addUnlimitedNaturalInClass1Description, null);
		diffs.addLiteralIntegerInClass2 = Iterators.find(differences.iterator(),
				addLiteralIntegerInClass2Description, null);
		diffs.addUnlimitedNaturalInClass2 = Iterators.find(differences.iterator(),
				addUnlimitedNaturalInClass2Description, null);

		return diffs;
	}

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	// local ADD
	public void testA10UseCase3way1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, right);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	// remote ADD
	public void testA10UseCase3way2() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, left);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA10MergeLtR1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyLeftToRight(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddNavigableOwnedEnd(comparison, diffs);
	}

	private void checkMergeAddNavigableOwnedEnd(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 6, comparison.getDifferences().size());
		assertNull(diffs.addNavigableOwnedEndClass1InAssociation);
		assertNull(diffs.addOwnedEndClass1InAssociation);
		assertNull(diffs.addMemberEndClass1InAssociation);
		assertNull(diffs.addRefAssociationInPropertyClass1);
		assertNull(diffs.addAssociation);
		assertNull(diffs.addUMLAssociation);
	}

	@Test
	public void testA10MergeLtR1UseCase3way1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyLeftToRight(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR1UseCase3way2() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyLeftToRight(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteNavigableOwnedEnd(comparison, diffs);
	}

	private void checkMergeDeleteNavigableOwnedEnd(Comparison comparison, DiffsOfInterest diffs) {
		// 7 diffs of interest + 2 MultiplicityElementChanges
		assertEquals(NB_DIFFS - 9, comparison.getDifferences().size());
		assertNull(diffs.addNavigableOwnedEndClass1InAssociation);
		assertNull(diffs.addOwnedEndClass1InAssociation);
		assertNull(diffs.addMemberEndClass1InAssociation);
		assertNull(diffs.addRefAssociationInPropertyClass1);
		assertNull(diffs.addLiteralIntegerInClass1);
		assertNull(diffs.addUnlimitedNaturalInClass1);
		assertNull(diffs.addRefTypeInPropertyClass1);
	}

	@Test
	public void testA10MergeLtR2UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyLeftToRight(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddOwnedEnd(comparison, diffs);
	}

	private void checkMergeAddOwnedEnd(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 6, comparison.getDifferences().size());
		assertNull(diffs.addOwnedEndClass1InAssociation);
		assertNull(diffs.addMemberEndClass1InAssociation);
		assertNull(diffs.addNavigableOwnedEndClass1InAssociation);
		assertNull(diffs.addRefAssociationInPropertyClass1);
		assertNull(diffs.addAssociation);
		assertNull(diffs.addUMLAssociation);
	}

	@Test
	public void testA10MergeLtR2UseCase3way1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyLeftToRight(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR2UseCase3way2() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyLeftToRight(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	private void checkMergeDeleteOwnedEnd(Comparison comparison, DiffsOfInterest diffs) {
		// 7 diffs of interest+ 2 MultiplicityElementChanges
		assertEquals(NB_DIFFS - 9, comparison.getDifferences().size());
		assertNull(diffs.addOwnedEndClass1InAssociation);
		assertNull(diffs.addMemberEndClass1InAssociation);
		assertNull(diffs.addRefAssociationInPropertyClass1);
		assertNull(diffs.addNavigableOwnedEndClass1InAssociation);
		assertNull(diffs.addLiteralIntegerInClass1);
		assertNull(diffs.addUnlimitedNaturalInClass1);
		assertNull(diffs.addRefTypeInPropertyClass1);
	}

	@Test
	public void testA10MergeLtR3UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyLeftToRight(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddMemberEnd(comparison, diffs);
	}

	private void checkMergeAddMemberEnd(Comparison comparison, DiffsOfInterest diffs) {
		assertEquals(NB_DIFFS - 6, comparison.getDifferences().size());
		assertNull(diffs.addMemberEndClass1InAssociation);
		assertNull(diffs.addRefAssociationInPropertyClass1);
		assertNull(diffs.addOwnedEndClass1InAssociation);
		assertNull(diffs.addNavigableOwnedEndClass1InAssociation);
		assertNull(diffs.addAssociation);
		assertNull(diffs.addUMLAssociation);
	}

	@Test
	public void testA10MergeLtR3UseCase3way1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyLeftToRight(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddMemberEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeLtR3UseCase3way2() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyLeftToRight(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL1UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyRightToLeft(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL1UseCase3way1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyRightToLeft(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL1UseCase3way2() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyRightToLeft(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL2UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyRightToLeft(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL2UseCase3way1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyRightToLeft(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL2UseCase3way2() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyRightToLeft(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyRightToLeft(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase3way1() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyRightToLeft(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA10MergeRtL3UseCase3way2() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.ADD);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyRightToLeft(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.ADD);

		checkMergeAddMemberEnd(comparison, diffs);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	// local DELETE
	public void testA11UseCase3way1() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, right);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	// remote DELETE
	public void testA11UseCase3way2() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, left);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA11MergeLtR1UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyLeftToRight(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR1UseCase3way1() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyLeftToRight(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR1UseCase3way2() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyLeftToRight(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR2UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyLeftToRight(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR2UseCase3way1() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyLeftToRight(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR2UseCase3way2() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyLeftToRight(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyLeftToRight(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase3way1() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyLeftToRight(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeLtR3UseCase3way2() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyLeftToRight(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddMemberEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyRightToLeft(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase3way1() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyRightToLeft(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL1UseCase3way2() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addNavigableOwnedEndClass1InAssociation)
				.copyRightToLeft(diffs.addNavigableOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteNavigableOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL2UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyRightToLeft(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL2UseCase3way1() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyRightToLeft(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL2UseCase3way2() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addOwnedEndClass1InAssociation).copyRightToLeft(
				diffs.addOwnedEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyRightToLeft(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddMemberEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase3way1() throws IOException {
		final Resource left = input.getA1Right();
		final Resource right = input.getA1Left();

		Comparison comparison = compare(left, right, right);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyRightToLeft(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeAddMemberEnd(comparison, diffs);
	}

	@Test
	public void testA11MergeRtL3UseCase3way2() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		Comparison comparison = compare(left, right, left);

		DiffsOfInterest diffs = getDiffs(comparison, TestKind.DELETE);

		// ** MERGE **
		getMergerRegistry().getHighestRankingMerger(diffs.addMemberEndClass1InAssociation).copyRightToLeft(
				diffs.addMemberEndClass1InAssociation, new BasicMonitor());

		comparison = compare(left, right);
		diffs = getDiffs(comparison, TestKind.DELETE);

		checkMergeDeleteOwnedEnd(comparison, diffs);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 16 differences
		assertEquals(NB_DIFFS, differences.size());

		DiffsOfInterest diffs = getDiffs(comparison, kind);

		if (kind.equals(TestKind.DELETE)) {
			assertEquals(0, diffs.addNavigableOwnedEndClass1InAssociation.getImplies().size());
			assertEquals(1, diffs.addNavigableOwnedEndClass1InAssociation.getImpliedBy().size());
			assertTrue(diffs.addNavigableOwnedEndClass1InAssociation.getImpliedBy().contains(
					diffs.addOwnedEndClass1InAssociation));

			assertEquals(1, diffs.addOwnedEndClass1InAssociation.getImplies().size());
			assertTrue(diffs.addOwnedEndClass1InAssociation.getImplies().contains(
					diffs.addNavigableOwnedEndClass1InAssociation));
			assertEquals(2, diffs.addOwnedEndClass1InAssociation.getImpliedBy().size());
			assertTrue(diffs.addOwnedEndClass1InAssociation.getImpliedBy().contains(
					diffs.addMemberEndClass1InAssociation));
			assertTrue(diffs.addOwnedEndClass1InAssociation.getImpliedBy().contains(
					diffs.addRefAssociationInPropertyClass1));

			assertEquals(1, diffs.addMemberEndClass1InAssociation.getImplies().size());
			assertTrue(diffs.addMemberEndClass1InAssociation.getImplies().contains(
					diffs.addOwnedEndClass1InAssociation));
			assertEquals(0, diffs.addMemberEndClass1InAssociation.getImpliedBy().size());
		} else {
			assertEquals(1, diffs.addNavigableOwnedEndClass1InAssociation.getImplies().size());
			assertTrue(diffs.addNavigableOwnedEndClass1InAssociation.getImplies().contains(
					diffs.addOwnedEndClass1InAssociation));
			assertEquals(0, diffs.addNavigableOwnedEndClass1InAssociation.getImpliedBy().size());

			assertEquals(2, diffs.addOwnedEndClass1InAssociation.getImplies().size());
			assertTrue(diffs.addOwnedEndClass1InAssociation.getImplies().contains(
					diffs.addMemberEndClass1InAssociation));
			assertTrue(diffs.addOwnedEndClass1InAssociation.getImplies().contains(
					diffs.addRefAssociationInPropertyClass1));
			assertEquals(1, diffs.addOwnedEndClass1InAssociation.getImpliedBy().size());
			assertTrue(diffs.addOwnedEndClass1InAssociation.getImpliedBy().contains(
					diffs.addNavigableOwnedEndClass1InAssociation));

			assertEquals(0, diffs.addMemberEndClass1InAssociation.getImplies().size());
			assertEquals(1, diffs.addMemberEndClass1InAssociation.getImpliedBy().size());
			assertTrue(diffs.addMemberEndClass1InAssociation.getImpliedBy().contains(
					diffs.addOwnedEndClass1InAssociation));
		}

	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}

	private static Predicate<? super Diff> addedLowerValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName), onFeature("lowerValue"),
				refinesMultiplicityElementChange());
	}

	private static Predicate<? super Diff> addedUpperValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.ADD), onEObject(qualifiedName), onFeature("upperValue"),
				refinesMultiplicityElementChange());
	}

	private static Predicate<? super Diff> removedLowerValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.DELETE), onEObject(qualifiedName), onFeature("lowerValue"),
				refinesMultiplicityElementChange());
	}

	private static Predicate<? super Diff> removedUpperValueIn(final String qualifiedName) {
		return and(ofKind(DifferenceKind.DELETE), onEObject(qualifiedName), onFeature("upperValue"),
				refinesMultiplicityElementChange());
	}

	private static Predicate<? super Diff> refinesMultiplicityElementChange() {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return Iterators.any(input.getRefines().iterator(), instanceOf(
						MultiplicityElementChange.class));
			}
		};
	}

	private class DiffsOfInterest {
		public Diff addAssociation;

		public Diff addNavigableOwnedEndClass1InAssociation;

		public Diff addNavigableOwnedEndClass2InAssociation;

		public Diff addMemberEndClass1InAssociation;

		public Diff addMemberEndClass2InAssociation;

		public Diff addOwnedEndClass1InAssociation;

		public Diff addOwnedEndClass2InAssociation;

		public Diff addRefAssociationInPropertyClass1;

		public Diff addRefTypeInPropertyClass1;

		public Diff addRefAssociationInPropertyClass2;

		public Diff addRefTypeInPropertyClass2;

		public Diff addLiteralIntegerInClass1;

		public Diff addUnlimitedNaturalInClass1;

		public Diff addLiteralIntegerInClass2;

		public Diff addUnlimitedNaturalInClass2;

		public Diff addUMLAssociation;
	}
}
