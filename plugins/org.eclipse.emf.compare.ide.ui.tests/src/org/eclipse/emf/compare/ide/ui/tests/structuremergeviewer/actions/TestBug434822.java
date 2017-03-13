/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.DifferenceState.UNRESOLVED;
import static org.eclipse.emf.compare.internal.merge.MergeMode.ACCEPT;
import static org.eclipse.emf.compare.internal.merge.MergeMode.REJECT;
import static org.eclipse.emf.compare.internal.merge.MergeMode.getMergeMode;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeRunnableImpl;
import org.eclipse.emf.compare.internal.merge.MergeDataImpl;
import org.eclipse.emf.compare.merge.DiffRelationshipComputer;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=434822">434822</a>
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@SuppressWarnings("restriction")
public class TestBug434822 {

	private IMerger.Registry mergerRegistry;

	private Diff rightDelete;

	private Diff leftMove;

	/**
	 * Set up the three test models.
	 * <p>
	 * <ul>
	 * <li>Origin.nodes: In this model we have a three nodes structured like this:
	 * 
	 * <pre>
	 * 	Root - DeletedNode
	 * 	  \  - MovingNode
	 * </pre>
	 * 
	 * </li>
	 * <li>Left.node: In this model "MovingNode" has been moved under "DeletedNode"</li>
	 * <li>Right.nodes: In this model "DeletedNode" has been deleted.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * A 3-way comparison between left.nodes (left input), right.node (right input) and origin.nodes (common
	 * ancestor) gives the resulting comparison model (two conflicting differences):
	 * <ul>
	 * <li>A difference for the deletion of "DeletedNode".</li>
	 * <li>A difference for moving "MovingNode"</li>
	 * <ul>
	 * </p>
	 * 
	 * @throws IOException
	 */
	@Before
	public void setUp() throws IOException {
		Bug434822InputData inputData = new Bug434822InputData();
		Resource left = inputData.getResource("left.nodes"); //$NON-NLS-1$
		Resource right = inputData.getResource("right.nodes"); //$NON-NLS-1$
		Resource origin = inputData.getResource("origin.nodes"); //$NON-NLS-1$
		DefaultComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		Comparison comparison = EMFCompare.builder().build().compare(scope);

		// Add a IMergeData to handle status decorations on Diffs
		comparison.eAdapters().add(new MergeDataImpl(true, false));

		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		// Keeps track of the 2 differences
		for (Diff diff : comparison.getDifferences()) {
			switch (diff.getKind()) {
				case MOVE:
					leftMove = diff;
					break;
				case DELETE:
					rightDelete = diff;
					break;
				default:
			}
		}
	}

	/**
	 * Checks that accepting the deletion works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterAcceptingDeletion() {
		ArrayList<Diff> uiDiff = Lists.newArrayList(rightDelete);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, ACCEPT,
				new DiffRelationshipComputer(mergerRegistry));
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data and diff states
		assertEquals(MERGED, rightDelete.getState());
		assertEquals(ACCEPT, getMergeMode(rightDelete, true, false));
		assertEquals(DISCARDED, leftMove.getState());
		assertEquals(REJECT, getMergeMode(leftMove, true, false));
	}

	/**
	 * Checks that accepting the deletion works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterAcceptingDeletionSeveral() {
		// Mocks the UI behavior of UI if the deleting diff is selected for merging (with cascading diff
		// filter activated).
		ArrayList<Diff> uiDiff = Lists.newArrayList(rightDelete, leftMove);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, ACCEPT,
				new DiffRelationshipComputer(mergerRegistry));
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data and diff states
		assertEquals(MERGED, rightDelete.getState());
		assertEquals(ACCEPT, getMergeMode(rightDelete, true, false));
		assertEquals(DISCARDED, leftMove.getState());
		assertEquals(REJECT, getMergeMode(leftMove, true, false));
	}

	/**
	 * Checks that rejecting the deletion works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterRejectingDeletion() {
		/*
		 * Mocks the UI behavior of UI if the deleting diff is selected for merging (with cascading diff
		 * filter activated).
		 */
		ArrayList<Diff> uiDiff = Lists.newArrayList(rightDelete);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, REJECT,
				new DiffRelationshipComputer(mergerRegistry));
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data and diff states
		assertEquals(DISCARDED, rightDelete.getState());
		assertEquals(REJECT, getMergeMode(rightDelete, true, false));
		assertEquals(UNRESOLVED, leftMove.getState());
	}

	/**
	 * Checks that accepting the movement works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterAcceptingMovement() {
		// Mocks the UI behavior of UI if the movement diff is selected for merging.
		ArrayList<Diff> uiDiff = Lists.newArrayList(leftMove);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, ACCEPT,
				new DiffRelationshipComputer(mergerRegistry));
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data
		assertEquals(MERGED, leftMove.getState());
		assertEquals(ACCEPT, getMergeMode(leftMove, true, false));
		assertEquals(DISCARDED, rightDelete.getState());
		assertEquals(REJECT, getMergeMode(rightDelete, true, false));
	}

	/**
	 * Checks that rejecting the movement works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterRejectingMovement() {
		// Mocks the UI behavior of UI if the movement diff is selected for merging.
		ArrayList<Diff> uiDiff = Lists.newArrayList(leftMove);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, REJECT,
				new DiffRelationshipComputer(mergerRegistry));
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data
		assertEquals(DISCARDED, leftMove.getState());
		assertEquals(REJECT, getMergeMode(leftMove, true, false));
		assertEquals(UNRESOLVED, rightDelete.getState());
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	public class Bug434822InputData extends AbstractInputData {

		private static final String PATH_PREFIX = "data/_434822/"; //$NON-NLS-1$

		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}

}
