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
package org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Before;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=560861">560861</a>
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
@SuppressWarnings("restriction")
public class TestBug560861 {

	private Comparison comparison;

	private CompareConfiguration cc;

	private EMFCompareConfiguration emfcc;

	private PhantomManager phantomManager;

	private boolean mirrored;

	/**
	 * Set up the two test models.
	 * <p>
	 * <ul>
	 * <li>left.nodes: In this model we have a tree nodes structured like this:
	 * 
	 * <pre>
	 * Root - DeletedNode
	 * </pre>
	 * 
	 * </li>
	 * <li>right.nodes: In this model "DeletedNode" has been deleted.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * A 2-way comparison between left.nodes (left input) and right.nodes (right input) gives the resulting
	 * comparison model:
	 * <li>A difference for the deletion of "DeletedNode".</li>
	 * </p>
	 * 
	 * @throws IOException
	 */
	@Before
	public void setUp() throws IOException {
		Bug560861InputData inputData = new Bug560861InputData();
		Resource left = inputData.getResource("left.nodes"); //$NON-NLS-1$
		Resource right = inputData.getResource("right.nodes"); //$NON-NLS-1$
		Resource origin = null;

		DefaultComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		comparison = EMFCompare.builder().build().compare(scope);
		cc = new CompareConfiguration();
		emfcc = new EMFCompareConfiguration(cc);
	}

	/**
	 * Tests that the side returned by the getTargetSide() method is correct before swapping.
	 */
	@Test
	public void testSideBeforeSwappingLeftAndRight() {
		mirrored = false;
		cc.setProperty(EMFCompareConfiguration.MIRRORED, mirrored);
		Match match = comparison.getMatches().get(0).getSubmatches().get(0);
		phantomManager = new PhantomManager(emfcc, null, null, null, null);
		MergeViewerSide phantomSide = phantomManager.getTargetSide(match, null);

		assertFalse(emfcc.isMirrored());
		assertEquals(phantomSide, MergeViewerSide.RIGHT);
	}

	/**
	 * Tests that the side returned by the getTargetSide() method is correct after swapping.
	 */
	@Test
	public void testSideAfterSwappingLeftAndRight() {
		mirrored = true;
		cc.setProperty(EMFCompareConfiguration.MIRRORED, mirrored);
		Match match = comparison.getMatches().get(0).getSubmatches().get(0);
		phantomManager = new PhantomManager(emfcc, null, null, null, null);
		MergeViewerSide phantomSide = phantomManager.getTargetSide(match, null);

		assertTrue(emfcc.isMirrored());
		assertEquals(phantomSide, MergeViewerSide.LEFT);
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
	 */
	public class Bug560861InputData extends AbstractInputData {

		private static final String PATH_PREFIX = "data/_560861/"; //$NON-NLS-1$

		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}

}
