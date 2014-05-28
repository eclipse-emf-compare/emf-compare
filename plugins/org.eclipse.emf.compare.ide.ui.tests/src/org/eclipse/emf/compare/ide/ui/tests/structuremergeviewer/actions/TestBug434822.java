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

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeRunnableImpl;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
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

	private Diff deletionDiff;

	private Diff movingDiff;

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
		mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();

		// Keeps track of the 2 differences
		for (Diff diff : comparison.getDifferences()) {
			switch (diff.getKind()) {
				case MOVE:
					movingDiff = diff;
					break;
				case DELETE:
					deletionDiff = diff;
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

		Assert.assertNotNull(movingDiff);
		Assert.assertNotNull(deletionDiff);

		/*
		 * Mocks the UI behavior of UI if the deleting diff is selected for merging (with cascading diff
		 * filter activated).
		 */
		ArrayList<Diff> uiDiff = Lists.newArrayList(deletionDiff, movingDiff);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, MergeMode.ACCEPT);
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data and diff states
		Assert.assertEquals(DifferenceState.MERGED, deletionDiff.getState());
		Assert.assertEquals(MergeMode.ACCEPT, getMergeData(deletionDiff).getMergeMode());
		Assert.assertEquals(DifferenceState.MERGED, movingDiff.getState());
		IMergeData mergeData = getMergeData(movingDiff);
		Assert.assertNotNull(mergeData);
		Assert.assertEquals(MergeMode.REJECT, mergeData.getMergeMode());

	}

	/**
	 * Checks that rejecting the deletion works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterRejectingDeletion() {

		Assert.assertNotNull(movingDiff);
		Assert.assertNotNull(deletionDiff);

		/*
		 * Mocks the UI behavior of UI if the deleting diff is selected for merging (with cascading diff
		 * filter activated).
		 */
		ArrayList<Diff> uiDiff = Lists.newArrayList(deletionDiff);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, MergeMode.REJECT);
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data and diff states
		Assert.assertEquals(DifferenceState.MERGED, deletionDiff.getState());
		Assert.assertEquals(MergeMode.REJECT, getMergeData(deletionDiff).getMergeMode());
		Assert.assertEquals(DifferenceState.UNRESOLVED, movingDiff.getState());
		IMergeData mergeData = getMergeData(movingDiff);
		Assert.assertNull(mergeData);

	}

	/**
	 * Checks that accepting the movement works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterAcceptingMovement() {

		Assert.assertNotNull(movingDiff);
		Assert.assertNotNull(deletionDiff);

		// Mocks the UI behavior of UI if the movement diff is selected for merging.
		ArrayList<Diff> uiDiff = Lists.newArrayList(movingDiff);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, MergeMode.ACCEPT);
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data
		Assert.assertEquals(DifferenceState.MERGED, movingDiff.getState());
		Assert.assertEquals(MergeMode.ACCEPT, getMergeData(movingDiff).getMergeMode());
		Assert.assertEquals(DifferenceState.UNRESOLVED, deletionDiff.getState());
		IMergeData mergeData = getMergeData(deletionDiff);
		Assert.assertNull(mergeData);

	}

	/**
	 * Checks that rejecting the movement works properly (no crash and correct merge data).
	 */
	@Test
	public void testMergeDataAfterRejectingMovement() {

		Assert.assertNotNull(movingDiff);
		Assert.assertNotNull(deletionDiff);

		// Mocks the UI behavior of UI if the movement diff is selected for merging.
		ArrayList<Diff> uiDiff = Lists.newArrayList(movingDiff);

		MergeRunnableImpl mergeRunnable = new MergeRunnableImpl(true, false, MergeMode.REJECT);
		mergeRunnable.merge(uiDiff, false, mergerRegistry);

		// Assert merge data
		Assert.assertEquals(DifferenceState.MERGED, movingDiff.getState());
		Assert.assertEquals(MergeMode.REJECT, getMergeData(movingDiff).getMergeMode());
		Assert.assertEquals(DifferenceState.UNRESOLVED, deletionDiff.getState());
		IMergeData mergeData = getMergeData(deletionDiff);
		Assert.assertNull(mergeData);

	}

	private IMergeData getMergeData(Diff diff) {
		return (IMergeData)EcoreUtil.getExistingAdapter(diff, IMergeData.class);
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
