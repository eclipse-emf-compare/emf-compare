/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.command;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.command.impl.MergeAllNonConflictingCommand;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAllNonConflictingRunnable;
import org.eclipse.emf.compare.ide.ui.tests.command.data.MergeAllCommandInputData;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings({"restriction", "unchecked" })
public class MergeAllCommandTests {

	private MergeAllCommandInputData input = new MergeAllCommandInputData();

	private final IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();

	@Test
	public void testMergeAllNonConflictingFromLeftToRight() throws IOException {
		MergeMode mergeMode = MergeMode.LEFT_TO_RIGHT;
		boolean leftToRight = true;
		boolean isLeftEditable = true;
		boolean isRightEditable = true;

		Resource left = input.getLeftScope();
		Resource right = input.getRightScope();
		Resource origin = input.getOriginScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		EMFCompareEditingDomain editingDomain = (EMFCompareEditingDomain)EMFCompareEditingDomain.create(left,
				right, origin);
		ChangeRecorder changeRecorder = editingDomain.getChangeRecorder();

		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		ImmutableSet<Notifier> notifiers = notifiersBuilder.add(comparison).addAll(
				ImmutableList.of(left, right, origin)).build();

		MergeAllNonConflictingRunnable runnable = new MergeAllNonConflictingRunnable(isLeftEditable,
				isRightEditable, mergeMode);

		MergeAllNonConflictingCommand command = new MergeAllNonConflictingCommand(changeRecorder, notifiers,
				comparison, leftToRight, mergerRegistry, runnable);

		EList<Diff> differencesBefore = comparison.getDifferences();
		// Test state of differences before command
		// 3 Left Delete differences
		Iterable<Diff> leftDelete = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftDelete));
		// 3 Right Delete differences
		Iterable<Diff> rightDelete = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightDelete));
		// 3 Left Add differences
		Iterable<Diff> leftAdd = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftAdd));
		// 3 Right Add differences
		Iterable<Diff> rightAdd = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightAdd));

		// Execute command
		command.execute();

		EList<Diff> differencesAfter = comparison.getDifferences();
		// Test state of differences before command
		// 3 Left Delete differences merged
		leftDelete = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(3, size(leftDelete));
		// 0 Right Delete differences merged
		rightDelete = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(0, size(rightDelete));
		// 3 Left Add differences merged
		leftAdd = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(3, size(leftAdd));
		// 0 Right Add differences merged
		rightAdd = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(0, size(rightAdd));

		command.dispose();
		editingDomain.dispose();
	}

	@Test
	public void testMergeAllNonConflictingFromRightToLeft() throws IOException {
		MergeMode mergeMode = MergeMode.RIGHT_TO_LEFT;
		boolean leftToRight = false;
		boolean isLeftEditable = true;
		boolean isRightEditable = true;

		Resource left = input.getLeftScope();
		Resource right = input.getRightScope();
		Resource origin = input.getOriginScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		EMFCompareEditingDomain editingDomain = (EMFCompareEditingDomain)EMFCompareEditingDomain.create(left,
				right, origin);
		ChangeRecorder changeRecorder = editingDomain.getChangeRecorder();

		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		ImmutableSet<Notifier> notifiers = notifiersBuilder.add(comparison).addAll(
				ImmutableList.of(left, right, origin)).build();

		MergeAllNonConflictingRunnable runnable = new MergeAllNonConflictingRunnable(isLeftEditable,
				isRightEditable, mergeMode);

		MergeAllNonConflictingCommand command = new MergeAllNonConflictingCommand(changeRecorder, notifiers,
				comparison, leftToRight, mergerRegistry, runnable);

		EList<Diff> differencesBefore = comparison.getDifferences();
		// Test state of differences before command
		// 3 Left Delete differences
		Iterable<Diff> leftDelete = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftDelete));
		// 3 Right Delete differences
		Iterable<Diff> rightDelete = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightDelete));
		// 3 Left Add differences
		Iterable<Diff> leftAdd = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftAdd));
		// 3 Right Add differences
		Iterable<Diff> rightAdd = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightAdd));

		// Execute command
		command.execute();

		EList<Diff> differencesAfter = comparison.getDifferences();
		// Test state of differences before command
		// 0 Left Delete differences merged
		leftDelete = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(0, size(leftDelete));
		// 3 Right Delete differences merged
		rightDelete = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(3, size(rightDelete));
		// 0 Left Add differences merged
		leftAdd = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(0, size(leftAdd));
		// 3 Right Add differences merged
		rightAdd = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(3, size(rightAdd));

		command.dispose();
		editingDomain.dispose();
	}

	@Test
	public void testAcceptAllNonConflictingChanges() throws IOException {
		MergeMode mergeMode = MergeMode.ACCEPT;
		boolean leftToRight = false;
		boolean isLeftEditable = true;
		boolean isRightEditable = false;

		Resource left = input.getLeftScope();
		Resource right = input.getRightScope();
		Resource origin = input.getOriginScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		EMFCompareEditingDomain editingDomain = (EMFCompareEditingDomain)EMFCompareEditingDomain.create(left,
				right, origin);
		ChangeRecorder changeRecorder = editingDomain.getChangeRecorder();

		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		ImmutableSet<Notifier> notifiers = notifiersBuilder.add(comparison).addAll(
				ImmutableList.of(left, right, origin)).build();

		MergeAllNonConflictingRunnable runnable = new MergeAllNonConflictingRunnable(isLeftEditable,
				isRightEditable, mergeMode);

		MergeAllNonConflictingCommand command = new MergeAllNonConflictingCommand(changeRecorder, notifiers,
				comparison, leftToRight, mergerRegistry, runnable);

		EList<Diff> differencesBefore = comparison.getDifferences();
		// Test state of differences before command
		// 3 Left Delete differences
		Iterable<Diff> leftDelete = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftDelete));
		// 3 Right Delete differences
		Iterable<Diff> rightDelete = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightDelete));
		// 3 Left Add differences
		Iterable<Diff> leftAdd = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftAdd));
		// 3 Right Add differences
		Iterable<Diff> rightAdd = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightAdd));

		// Execute command
		command.execute();

		EList<Diff> differencesAfter = comparison.getDifferences();
		// Test state of differences before command
		// 3 Left Delete differences merged
		leftDelete = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(3, size(leftDelete));
		// 3 Right Delete differences merged
		rightDelete = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(3, size(rightDelete));
		// 3 Left Add differences merged
		leftAdd = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(3, size(leftAdd));
		// 3 Right Add differences merged
		rightAdd = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(3, size(rightAdd));

		command.dispose();
		editingDomain.dispose();
	}

	@Test
	public void testRejectAllNonConflictingChanges() throws IOException {
		MergeMode mergeMode = MergeMode.REJECT;
		boolean leftToRight = false;
		boolean isLeftEditable = true;
		boolean isRightEditable = false;

		Resource left = input.getLeftScope();
		Resource right = input.getRightScope();
		Resource origin = input.getOriginScope();

		IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		EMFCompareEditingDomain editingDomain = (EMFCompareEditingDomain)EMFCompareEditingDomain.create(left,
				right, origin);
		ChangeRecorder changeRecorder = editingDomain.getChangeRecorder();

		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		ImmutableSet<Notifier> notifiers = notifiersBuilder.add(comparison).addAll(
				ImmutableList.of(left, right, origin)).build();

		MergeAllNonConflictingRunnable runnable = new MergeAllNonConflictingRunnable(isLeftEditable,
				isRightEditable, mergeMode);

		MergeAllNonConflictingCommand command = new MergeAllNonConflictingCommand(changeRecorder, notifiers,
				comparison, leftToRight, mergerRegistry, runnable);

		EList<Diff> differencesBefore = comparison.getDifferences();
		// Test state of differences before command
		// 3 Left Delete differences
		Iterable<Diff> leftDelete = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftDelete));
		// 3 Right Delete differences
		Iterable<Diff> rightDelete = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightDelete));
		// 3 Left Add differences
		Iterable<Diff> leftAdd = filter(differencesBefore, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(leftAdd));
		// 3 Right Add differences
		Iterable<Diff> rightAdd = filter(differencesBefore, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.ADD), hasState(DifferenceState.UNRESOLVED)));
		assertEquals(3, size(rightAdd));

		// Execute command
		command.execute();

		EList<Diff> differencesAfter = comparison.getDifferences();
		// Test state of differences before command
		// 3 Left Delete differences merged
		leftDelete = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(3, size(leftDelete));
		// 3 Right Delete differences merged
		rightDelete = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT),
				ofKind(DifferenceKind.DELETE), hasState(DifferenceState.MERGED)));
		assertEquals(3, size(rightDelete));
		// 3 Left Add differences merged
		leftAdd = filter(differencesAfter, and(fromSide(DifferenceSource.LEFT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(3, size(leftAdd));
		// 3 Right Add differences merged
		rightAdd = filter(differencesAfter, and(fromSide(DifferenceSource.RIGHT), ofKind(DifferenceKind.ADD),
				hasState(DifferenceState.MERGED)));
		assertEquals(3, size(rightAdd));

		command.dispose();
		editingDomain.dispose();
	}
}
