/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Tobias Ortmayr - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.size;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAllNonConflictingAction;
import org.eclipse.emf.compare.ide.ui.tests.command.data.MergeAllCommandInputData;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the the {@link MergeAction#setMirrored(boolean)} method.
 * <p>
 * The goal of this test is to verify that merge actions behave like expected when their state is set to
 * mirrored. In particular this means that mirrored CopyLeftToRight actions should behave like CopyRightToLeft
 * actions and vice versa.
 * 
 * @author Tobias Ortmayr <tortmayr.ext@eclipsesource.com>
 */
@SuppressWarnings({"restriction" })
public class MirroredMergeActionTest {

	private Resource leftResource;

	private Resource rightResource;

	private Resource originResource;

	private IMerger.Registry mergerRegistry;

	private EMFCompareEditingDomain editingDomain;

	@Before
	public void getInputData() throws IOException {
		final MergeAllCommandInputData input = new MergeAllCommandInputData();
		leftResource = input.getLeftScope();
		rightResource = input.getRightScope();
		originResource = input.getOriginScope();
		mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		editingDomain = (EMFCompareEditingDomain)EMFCompareEditingDomain.create(leftResource, rightResource,
				originResource);
	}

	@After
	public void tearDown() {
		editingDomain.dispose();
	}

	private void testMirrorMergeAction(MergeMode actionMergeMode, DifferenceState expectedLeftDiffState,
			DifferenceState expectedRightDiffState) {
		IEMFCompareConfiguration emfCC = createConfiguration(true, true);
		IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, originResource);
		Comparison comparision = EMFCompare.builder().build().compare(scope);

		// create merge action and set the mirror state to true
		final MockMergeAction mergeAction = new MockMergeAction(emfCC, mergerRegistry, actionMergeMode, null);
		mergeAction.setMirrored(true);

		// get the first left difference from the comparison, ensure that its unresolved and invoke the
		// action.
		Diff leftDiff = get(filter(comparision.getDifferences(), fromSide(DifferenceSource.LEFT)), 0);
		assertEquals(DifferenceState.UNRESOLVED, leftDiff.getState());
		mergeAction.updateSelection(new StructuredSelection(leftDiff));
		mergeAction.run();

		// check if difference state is matching the expected one
		leftDiff = get(filter(comparision.getDifferences(), fromSide(DifferenceSource.LEFT)), 0);
		assertEquals(expectedLeftDiffState, leftDiff.getState());

		// get the first right difference from the comparison, ensure that its unresolved and invoke the
		// action.
		Diff rightDiff = get(filter(comparision.getDifferences(), fromSide(DifferenceSource.RIGHT)), 0);
		assertEquals(DifferenceState.UNRESOLVED, rightDiff.getState());
		mergeAction.updateSelection(new StructuredSelection(rightDiff));
		mergeAction.run();

		// check if difference state is matching the expected one
		rightDiff = get(filter(comparision.getDifferences(), fromSide(DifferenceSource.RIGHT)), 0);
		assertEquals(expectedRightDiffState, rightDiff.getState());
	}

	private void testMirrorMergeAction(MergeMode actionMergeMode, DifferenceState expectedDiffState) {
		IEMFCompareConfiguration emfCC = createConfiguration(true, false);
		IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, originResource);
		Comparison comparision = EMFCompare.builder().build().compare(scope);

		// create merge action and set the mirror state to true
		final MockMergeAction mergeAction = new MockMergeAction(emfCC, mergerRegistry, actionMergeMode, null);
		mergeAction.setMirrored(true);

		// get the first left difference from the comparison, ensure that its unresolved and invoke the
		// action.
		Diff leftDiff = get(filter(comparision.getDifferences(), fromSide(DifferenceSource.LEFT)), 0);
		assertEquals(DifferenceState.UNRESOLVED, leftDiff.getState());
		mergeAction.updateSelection(new StructuredSelection(leftDiff));
		mergeAction.run();

		// check if difference state is matching the expected one
		leftDiff = get(filter(comparision.getDifferences(), fromSide(DifferenceSource.LEFT)), 0);
		assertEquals(expectedDiffState, leftDiff.getState());
	}

	private void testMirrorAllNonConflictingAction(MergeMode actionMergeMode,
			DifferenceState expectedLeftDiffsState, DifferenceState expectedRightDiffsState) {
		IEMFCompareConfiguration emfCC = createConfiguration(true, true);
		IComparisonScope scope = new DefaultComparisonScope(leftResource, originResource, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final MergeAllNonConflictingAction mergeAcion = new MergeAllNonConflictingAction(emfCC, comparison,
				mergerRegistry, actionMergeMode);
		mergeAcion.setMirrored(true);

		// get amount of unresolved left & right diffs
		int leftDiffAmount = size(filter(comparison.getDifferences(),
				and(fromSide(DifferenceSource.LEFT), hasState(DifferenceState.UNRESOLVED))));

		int rightDiffAmount = size(filter(comparison.getDifferences(),
				and(fromSide(DifferenceSource.RIGHT), hasState(DifferenceState.UNRESOLVED))));

		// Execute action
		mergeAcion.run();

		// test if all diffs are in the expected state
		assertEquals(leftDiffAmount, size(filter(comparison.getDifferences(),
				and(fromSide(DifferenceSource.LEFT), hasState(expectedLeftDiffsState)))));
		assertEquals(rightDiffAmount, size(filter(comparison.getDifferences(),
				and(fromSide(DifferenceSource.RIGHT), hasState(expectedRightDiffsState)))));
	}

	private void testMirrorAllNonConflictingAction(MergeMode actionMergeMode,
			DifferenceState expectedDiffState) {
		IEMFCompareConfiguration emfCC = createConfiguration(true, false);
		IComparisonScope scope = new DefaultComparisonScope(leftResource, originResource, null);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		final MergeAllNonConflictingAction mergeAcion = new MergeAllNonConflictingAction(emfCC, comparison,
				mergerRegistry, actionMergeMode);
		mergeAcion.setMirrored(true);

		// get amount of unresolved left & right diffs
		int leftDiffAmount = size(filter(comparison.getDifferences(),
				and(fromSide(DifferenceSource.LEFT), hasState(DifferenceState.UNRESOLVED))));

		// Execute action
		mergeAcion.run();

		// test if all left diffs are in the expected state
		assertEquals(leftDiffAmount, size(filter(comparison.getDifferences(),
				and(fromSide(DifferenceSource.LEFT), hasState(expectedDiffState)))));
	}

	@Test
	public void mirrorCopyLeftToRightAction() {
		// mirrored -> discard left diffs, merge right diffs
		testMirrorMergeAction(MergeMode.LEFT_TO_RIGHT, DifferenceState.DISCARDED, DifferenceState.MERGED);
	}

	@Test
	public void testMirrorCopyRightToLeftAction() {
		// mirrored -> merge left diffs, merge discard diffs
		testMirrorMergeAction(MergeMode.RIGHT_TO_LEFT, DifferenceState.MERGED, DifferenceState.DISCARDED);

	}

	@Test
	public void testMirrorCopyAllNonConflictingFromLeftToRightAction() {
		// mirrored -> merge left diffs, merge right diffs
		testMirrorAllNonConflictingAction(MergeMode.LEFT_TO_RIGHT, DifferenceState.DISCARDED,
				DifferenceState.MERGED);
	}

	@Test
	public void testMirrorCopyAllNonConflictingFromRightToLeftAction() {
		// mirrored -> merge left diffs, merge discard diffs{
		testMirrorAllNonConflictingAction(MergeMode.RIGHT_TO_LEFT, DifferenceState.MERGED,
				DifferenceState.DISCARDED);
	}

	@Test
	public void testMirrorAcceptChangeAction() {
		// mirrored-> same behavior as not mirrored action
		testMirrorMergeAction(MergeMode.ACCEPT, DifferenceState.MERGED);
	}

	@Test
	public void testMirrorRejectChangeAction() {
		// mirrored-> same behavior as not mirrored action
		testMirrorMergeAction(MergeMode.REJECT, DifferenceState.DISCARDED);
	}

	@Test
	public void testMirrorAcceptAllNonConflictingAction() {
		// mirrored-> same behavior as not mirrored action
		testMirrorAllNonConflictingAction(MergeMode.ACCEPT, DifferenceState.MERGED);
	}

	@Test
	public void testMirrorRejectAllNonConflictingAction() {
		// mirrored-> same behavior as not mirrored action
		testMirrorAllNonConflictingAction(MergeMode.REJECT, DifferenceState.DISCARDED);
	}

	private IEMFCompareConfiguration createConfiguration(boolean leftEditable, boolean rightEditable) {
		CompareConfiguration cc = new CompareConfiguration();
		cc.setLeftEditable(leftEditable);
		cc.setRightEditable(rightEditable);
		EMFCompareConfiguration emfCC = new EMFCompareConfiguration(cc);
		emfCC.setEditingDomain(editingDomain);

		return emfCC;
	}

	class MockMergeAction extends MergeAction {

		public MockMergeAction(IEMFCompareConfiguration compareConfiguration, Registry mergerRegistry,
				MergeMode mode, INavigatable navigatable) {
			super(compareConfiguration, mergerRegistry, mode, navigatable);
		}

		@Override
		public boolean updateSelection(IStructuredSelection selection) {
			return super.updateSelection(selection);
		}

		@Override
		protected void clearCache() {
			super.clearCache();
		}

		@Override
		protected Iterable<Diff> getSelectedDifferences(IStructuredSelection selection) {
			List<?> selectedObjects = selection.toList();
			return filter(selectedObjects, Diff.class);
		}
	}

}
