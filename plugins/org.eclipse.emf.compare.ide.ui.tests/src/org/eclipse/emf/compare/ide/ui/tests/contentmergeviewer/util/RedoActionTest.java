/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.contentmergeviewer.util;

import static org.eclipse.emf.compare.tests.utils.EMFCompareTestsUtils.assertEqualContents;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.RedoAction;
import org.eclipse.emf.compare.ide.ui.tests.contentmergeviewer.util.data.UtilInputData;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * Tests the {@link RedoAction}.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public class RedoActionTest extends AbstractReverseActionTest {

	/**
	 * The main comparison.
	 */
	private Comparison comparison;

	/**
	 * The scope of the comparison.
	 */
	private DefaultComparisonScope scope;

	/**
	 * The input data.
	 */
	private UtilInputData utilInput = new UtilInputData();

	/**
	 * Tests that the redo action restores the state of the model after merging from left to right, and undo,
	 * on the "nodes1" model.
	 * 
	 * @throws IOException
	 *             Thrown if we could not access either the resource.
	 */
	@Test
	public void testRedoMergeL2RNodes1() throws IOException {
		Resource leftResource = utilInput.getNodes1Left();
		Resource rightResource = utilInput.getNodes1Right();

		scope = new DefaultComparisonScope(leftResource, rightResource, null);
		comparison = EMFCompare.builder().build().compare(getScope());

		mergeDiffsLeftToRight(comparison.getDifferences());
		assertEqualContents(comparison, getNodes(leftResource), getNodes(rightResource));

		undo();
		redo();
		assertEqualContents(comparison, getNodes(leftResource), getNodes(rightResource));
	}

	/**
	 * Tests that the redo action restores the state of the model after merging from right to left, and undo,
	 * on the "nodes1" model.
	 * 
	 * @throws IOException
	 *             Thrown if we could not access either the resource.
	 */
	@Test
	public void testRedoMergeR2LNodes1() throws IOException {
		Resource leftResource = utilInput.getNodes1Left();
		Resource rightResource = utilInput.getNodes1Right();

		scope = new DefaultComparisonScope(leftResource, rightResource, null);
		comparison = EMFCompare.builder().build().compare(getScope());

		mergeDiffsRightToLeft(comparison.getDifferences());
		assertEqualContents(comparison, getNodes(leftResource), getNodes(rightResource));

		undo();
		redo();
		assertEqualContents(comparison, getNodes(leftResource), getNodes(rightResource));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DefaultComparisonScope getScope() {
		return scope;
	}

}
