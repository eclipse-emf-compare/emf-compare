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
package org.eclipse.emf.compare.diagram.sirius.internal.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.sirius.tests.AbstractSiriusTest;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=562321">562321.</a>
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
public class TestBug562321 extends AbstractSiriusTest {

	/**
	 * The main comparison.
	 */
	private Comparison comparison;

	/**
	 * The "singleAddedContainer.aird" data resource.
	 */
	private Resource singleAddedContainer;

	/**
	 * The "singleDeletedContainer.aird" data resource.
	 */
	private Resource singleDeletedContainer;

	/**
	 * Used to initialize the comparison scope.
	 */
	private Resource origin;

	/**
	 * The scope of the comparison.
	 */
	private DefaultComparisonScope scope;

	/**
	 * Set up the test models.
	 * <p>
	 * singleAddedContainer.nodes: In this model we have a tree nodes structured like this:
	 * <ul>
	 * <li>A Node "A" that holds a Node "B".</li>
	 * </ul>
	 * singleDeletedContainer.nodes: In this model "B" has been deleted.
	 * </p>
	 * <p>
	 * A 2-way comparison between their representations files singleAddedContainer.aird and
	 * singleDeletedContainer.aird gives us these differences in particular:
	 * <ul>
	 * <li>A ReferenceChange difference for the addition/deletion of "B" DNodeContainer.</li>
	 * <li>A ReferenceChange difference for the modification of "B.actualMapping" ContainerMapping.</li>
	 * </ul>
	 * </p>
	 * 
	 * @throws IOException
	 *             Thrown if we could not access either this class's resource, or the file towards which
	 *             <code>string</code> points.
	 */
	@Override
	public void setUp() throws IOException {
		super.setUp();
		Bug562321InputData inputData = new Bug562321InputData();
		singleAddedContainer = inputData.getResource("singleAddedContainer.aird"); //$NON-NLS-1$
		singleDeletedContainer = inputData.getResource("singleDeletedContainer.aird"); //$NON-NLS-1$
		origin = null;
	}

	/**
	 * Tests that the undo and redo actions after a merge from right to left of a deleted container on the
	 * left correctly restores the model to its previous state.
	 */
	@Test
	public void testUndoRedoR2LSingleDeletedContainer() {
		scope = new DefaultComparisonScope(singleDeletedContainer, singleAddedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(getScope());

		DRepresentationElement node = getSiriusNodes(comparison.getDifferences()).get(0);
		Match valueMatch = comparison.getMatch(node);

		assertEquals(node, valueMatch.getRight());
		assertNull(valueMatch.getLeft());

		mergeDiffsRightToLeft(comparison.getDifferences());
		assertNodeIsSet((DRepresentationElement)valueMatch.getLeft());

		undo();
		assertNull(valueMatch.getLeft());

		redo();
		assertNodeIsSet((DRepresentationElement)valueMatch.getLeft());
	}

	/**
	 * Tests that the undo and redo actions after a merge from left to right of a deleted container on the
	 * left correctly restores the model to its previous state.
	 */
	@Test
	public void testUndoRedoL2RSingleDeletedContainer() {
		scope = new DefaultComparisonScope(singleDeletedContainer, singleAddedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(getScope());

		DRepresentationElement node = getSiriusNodes(comparison.getDifferences()).get(0);
		Match valueMatch = comparison.getMatch(node);

		assertEquals(node, valueMatch.getRight());
		assertNodeIsSet(node);
		assertNull(valueMatch.getLeft());

		mergeDiffsLeftToRight(comparison.getDifferences());
		assertNodeIsUnset(node);
		assertNull(valueMatch.getRight());

		undo();
		assertNodeIsSet(node);
		assertEquals(node, valueMatch.getRight());

		redo();
		assertNodeIsUnset(node);
		assertNull(valueMatch.getRight());
	}

	/**
	 * Tests that the undo and redo actions after a merge from right to left of an added container on the left
	 * correctly restores the model to its previous state.
	 */
	@Test
	public void testUndoRedoR2LSingleAddedContainer() {
		scope = new DefaultComparisonScope(singleAddedContainer, singleDeletedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(getScope());

		DRepresentationElement node = getSiriusNodes(comparison.getDifferences()).get(0);
		Match valueMatch = comparison.getMatch(node);

		assertEquals(node, valueMatch.getLeft());
		assertNodeIsSet(node);
		assertNull(valueMatch.getRight());

		mergeDiffsRightToLeft(comparison.getDifferences());
		assertNodeIsUnset(node);
		assertNull(valueMatch.getLeft());

		undo();
		assertNodeIsSet(node);
		assertEquals(node, valueMatch.getLeft());

		redo();
		assertNodeIsUnset(node);
		assertNull(valueMatch.getLeft());
	}

	/**
	 * Tests that the undo and redo actions after a merge from left to right of an added container on the left
	 * correctly restores the model to its previous state.
	 */
	@Test
	public void testUndoRedoL2RSingleAddedContainer() {
		scope = new DefaultComparisonScope(singleAddedContainer, singleDeletedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(getScope());

		DRepresentationElement node = getSiriusNodes(comparison.getDifferences()).get(0);
		Match valueMatch = comparison.getMatch(node);

		assertEquals(node, valueMatch.getLeft());
		assertNull(valueMatch.getRight());

		mergeDiffsLeftToRight(comparison.getDifferences());
		assertNodeIsSet((DRepresentationElement)valueMatch.getRight());

		undo();
		assertNull(valueMatch.getRight());

		redo();
		assertNodeIsSet((DRepresentationElement)valueMatch.getRight());
	}

	/**
	 * Ensures that the given node is correctly unset.
	 * 
	 * @param node
	 *            the Sirius Node to check.
	 */
	private static void assertNodeIsUnset(DRepresentationElement node) {
		assertNull(node.getMapping());
		assertNull(node.getTarget());
		assertTrue(node.getSemanticElements().isEmpty());
	}

	/**
	 * Ensures that the given node is correctly set.
	 * 
	 * @param node
	 *            the Sirius Node to check.
	 */
	private static void assertNodeIsSet(DRepresentationElement node) {
		assertNotNull(node.getMapping());
		assertNotNull(node.getTarget());
		assertFalse(node.getSemanticElements().isEmpty());
	}

	/**
	 * Provides access to Sirius Nodes that exist in the difference list.
	 * 
	 * @param differences
	 *            the list of differences.
	 * @return the list of Sirius Nodes.
	 */
	protected List<DRepresentationElement> getSiriusNodes(List<Diff> differences) {
		Set<ReferenceChange> siriusDiff = differences.stream().filter(diff -> diff instanceof ReferenceChange)
				.map(ReferenceChange.class::cast)
				.filter(diff -> diff.getValue() instanceof DRepresentationElement)
				.collect(Collectors.toSet());

		Set<DRepresentationElement> siriusNodes = new HashSet<DRepresentationElement>();
		for (ReferenceChange diff : siriusDiff) {
			siriusNodes.add((DRepresentationElement)diff.getValue());
		}
		return new ArrayList<DRepresentationElement>(siriusNodes);
	}

	/**
	 * Getter for the scope.
	 * 
	 * @return scope.
	 */
	@Override
	public DefaultComparisonScope getScope() {
		return scope;
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
	 */
	public class Bug562321InputData extends AbstractInputData {

		/**
		 * The data path.
		 */
		private static final String PATH_PREFIX = "data/_562321/"; //$NON-NLS-1$

		/**
		 * Used to load a resource.
		 * 
		 * @param resourceName
		 *            the resource to load.
		 * @return the resource loaded.
		 * @throws IOException
		 *             Thrown if we could not access either this class' resource, or the file towards which
		 *             <code>string</code> points.
		 */
		public Resource getResource(String resourceName) throws IOException {
			StringBuilder resourceURL = new StringBuilder(PATH_PREFIX);
			resourceURL.append(resourceName);
			return loadFromClassLoader(resourceURL.toString());
		}
	}
}
