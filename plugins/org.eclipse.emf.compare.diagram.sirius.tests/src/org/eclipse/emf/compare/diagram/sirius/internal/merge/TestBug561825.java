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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.sirius.tests.AbstractSiriusTest;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.viewpoint.DMappingBased;
import org.eclipse.sirius.viewpoint.description.RepresentationElementMapping;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=561825">561825.</a>
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
public class TestBug561825 extends AbstractSiriusTest {

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
	 *             Thrown if we could not access either this class' resource, or the file towards which
	 *             <code>string</code> points.
	 */
	@Override
	public void setUp() throws IOException {
		super.setUp();
		Bug561825 inputData = new Bug561825();
		singleAddedContainer = inputData.getResource("singleAddedContainer.aird"); //$NON-NLS-1$
		singleDeletedContainer = inputData.getResource("singleDeletedContainer.aird"); //$NON-NLS-1$
		origin = null;
	}

	/**
	 * Tests that the merge from right to left of a deleted container on the left correctly sets the left
	 * value of the match corresponding to the mapping, and also the mapping of this value, without failing.
	 */
	@Test
	public void testSingleDeletedContainerMappingSet() {
		scope = new DefaultComparisonScope(singleDeletedContainer, singleAddedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		ReferenceChange mappingDiff = getRepresentationElementMappingDiffs(comparison.getDifferences())
				.get(0);
		ReferenceChange nodeDiff = getDMappingBasedDiffs(comparison.getDifferences()).get(0);

		mergeDiffsRightToLeft(comparison.getDifferences());

		assertEquals(comparison.getMatch(nodeDiff.getValue()), mappingDiff.getMatch());
		assertNotNull(mappingDiff.getMatch().getLeft());
		assertTrue(mappingDiff.getMatch().getLeft() instanceof DMappingBased);
		assertNotNull(((DMappingBased)mappingDiff.getMatch().getLeft()).getMapping());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * Tests that the merge from left to right of a deleted container on the left correctly unsets the right
	 * value of the match corresponding to the mapping, without failing.
	 */
	@Test
	public void testSingleDeletedContainerMappingUnset() {
		scope = new DefaultComparisonScope(singleDeletedContainer, singleAddedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		ReferenceChange mappingDiff = getRepresentationElementMappingDiffs(comparison.getDifferences())
				.get(0);
		ReferenceChange nodeDiff = getDMappingBasedDiffs(comparison.getDifferences()).get(0);

		Match valueMatch = comparison.getMatch(nodeDiff.getValue());
		mergeDiffsLeftToRight(comparison.getDifferences());

		assertEquals(valueMatch, mappingDiff.getMatch());
		assertNull(comparison.getMatch(nodeDiff.getValue()));
		assertNull(mappingDiff.getMatch().getRight());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * Tests that the merge from left to right of an added container on the left correctly sets the right
	 * value of the match corresponding to the mapping, and also the mapping of this value, without failing.
	 */
	@Test
	public void testSingleAddedContainerMappingSet() {
		scope = new DefaultComparisonScope(singleAddedContainer, singleDeletedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		ReferenceChange mappingDiff = getRepresentationElementMappingDiffs(comparison.getDifferences())
				.get(0);
		ReferenceChange nodeDiff = getDMappingBasedDiffs(comparison.getDifferences()).get(0);

		mergeDiffsLeftToRight(comparison.getDifferences());

		assertEquals(comparison.getMatch(nodeDiff.getValue()), mappingDiff.getMatch());
		assertNotNull(mappingDiff.getMatch().getRight());
		assertTrue(mappingDiff.getMatch().getRight() instanceof DMappingBased);
		assertNotNull(((DMappingBased)mappingDiff.getMatch().getRight()).getMapping());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * Tests that the merge from right to left of an added container on the left correctly unsets the left
	 * value of the match corresponding to the mapping, without failing.
	 */
	@Test
	public void testSingleAddedContainerMappingUnset() {
		scope = new DefaultComparisonScope(singleAddedContainer, singleDeletedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		ReferenceChange mappingDiff = getRepresentationElementMappingDiffs(comparison.getDifferences())
				.get(0);
		ReferenceChange nodeDiff = getDMappingBasedDiffs(comparison.getDifferences()).get(0);

		Match valueMatch = comparison.getMatch(nodeDiff.getValue());
		mergeDiffsRightToLeft(comparison.getDifferences());

		assertEquals(valueMatch, mappingDiff.getMatch());
		assertNull(comparison.getMatch(nodeDiff.getValue()));
		assertNull(mappingDiff.getMatch().getLeft());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * Filters a list of differences by keeping only the differences concerned by a mapping in their "implied"
	 * relation.
	 * 
	 * @param differences
	 *            the list of differences to filter.
	 * @return the differences concerned by a mapping in their "implied" relation.
	 */
	private Stream<ReferenceChange> filterMappingDifferences(List<Diff> differences) {
		Stream<ReferenceChange> refChanges = differences.stream()
				.filter(diff -> diff instanceof ReferenceChange).map(ReferenceChange.class::cast);
		Stream<ReferenceChange> mappingDifferences = refChanges
				.filter(diff -> (!diff.getImpliedBy().isEmpty() || !diff.getImplies().isEmpty())
						&& (diff.getValue() instanceof DMappingBased
								|| diff.getValue() instanceof RepresentationElementMapping));
		return mappingDifferences;

	}

	/**
	 * Used to filter a list by keeping only the differences with RepresentationElementMapping type values.
	 * 
	 * @see org.eclipse.sirius.viewpoint.description.RepresentationElementMapping
	 * @param differences
	 *            the list of differences to filter.
	 * @return the list of mappings differences.
	 */
	private List<ReferenceChange> getRepresentationElementMappingDiffs(List<Diff> differences) {
		return filterMappingDifferences(differences)
				.filter(diff -> diff.getValue() instanceof RepresentationElementMapping)
				.collect(Collectors.toList());
	}

	/**
	 * Used to filter a list by keeping only the differences with DMappingBased type values, such as DNode,
	 * DNodeContainer and DEdge.
	 * 
	 * @see org.eclipse.sirius.viewpoint.DMappingBased
	 * @param differences
	 *            the list of differences to filter.
	 * @return the list of DMappingBased differences.
	 */
	private List<ReferenceChange> getDMappingBasedDiffs(List<Diff> differences) {
		return filterMappingDifferences(differences).filter(diff -> diff.getValue() instanceof DMappingBased)
				.collect(Collectors.toList());
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
	public class Bug561825 extends AbstractInputData {

		/**
		 * The data path.
		 */
		private static final String PATH_PREFIX = "data/_561825/"; //$NON-NLS-1$

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
