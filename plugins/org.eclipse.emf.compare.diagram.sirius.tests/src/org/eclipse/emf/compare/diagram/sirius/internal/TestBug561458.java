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
package org.eclipse.emf.compare.diagram.sirius.internal;

import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.sirius.tests.AbstractSiriusTest;
import org.eclipse.emf.compare.merge.AbstractMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.viewpoint.DMappingBased;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.junit.Before;
import org.junit.Test;

/**
 * This test is related to the bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=561458">561458.</a>
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
public class TestBug561458 extends AbstractSiriusTest {

	/**
	 * The main comparison.
	 */
	private Comparison comparison;

	/**
	 * The "addedContainers.aird" data resource.
	 */
	private Resource addedContainers;

	/**
	 * The "deletedContainers.aird" data resource.
	 */
	private Resource deletedContainers;

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
	 * Set up the test models.
	 * <p>
	 * addedContainers.nodes: In this model we have a tree nodes structured like this:
	 * <ul>
	 * <li>A Node "A" that holds a Node "B" that holds a Node "C" that holds a Node "D".</li>
	 * </ul>
	 * deletedContainers.nodes: In this model "C" and "D" have been deleted.
	 * </p>
	 * <p>
	 * A 2-way comparison between their representations files addedContainers.aird and deletedContainers.aird
	 * gives us these differences in particular:
	 * <ul>
	 * <li>A ReferenceChange difference for the addition/deletion of "C" DNodeContainer.</li>
	 * <li>A ReferenceChange difference for the addition/deletion of "D" DNodeContainer.</li>
	 * <li>A ReferenceChange difference for the modification of "C.actualMapping" ContainerMapping.</li>
	 * <li>A ReferenceChange difference for the modification of "D.actualMapping" ContainerMapping.</li>
	 * </ul>
	 * </p>
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
	@Before
	public void setUp() throws IOException {
		Bug561458 inputData = new Bug561458();
		fillRegistries();
		addedContainers = inputData.getResource("addedContainers.aird"); //$NON-NLS-1$
		deletedContainers = inputData.getResource("deletedContainers.aird"); //$NON-NLS-1$
		singleAddedContainer = inputData.getResource("singleAddedContainer.aird"); //$NON-NLS-1$
		singleDeletedContainer = inputData.getResource("singleDeletedContainer.aird"); //$NON-NLS-1$
		origin = null;
	}

	/**
	 * Tests that the added node difference implies its mapping difference.
	 */
	@Test
	public void testSingleAddedContainerImpliesMapping() {
		DefaultComparisonScope scope = new DefaultComparisonScope(singleAddedContainer,
				singleDeletedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<ReferenceChange> decorators = getAddedOrRemovedDecorators();

		ReferenceChange addedDiff = decorators.get(0);
		DSemanticDecorator value = (DSemanticDecorator)addedDiff.getValue();
		Diff mapping = comparison.getDifferences(((DMappingBased)value).getMapping()).get(0);

		assertTrue(addedDiff.getKind() == ADD);
		assertEquals(mapping, addedDiff.getImplies().get(0));
	}

	/**
	 * Tests that the deleted node difference is implied by its mapping difference.
	 */
	@Test
	public void testSingleDeletedContainerImpliedByMapping() {
		DefaultComparisonScope scope = new DefaultComparisonScope(singleDeletedContainer,
				singleAddedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<ReferenceChange> decorators = getAddedOrRemovedDecorators();

		ReferenceChange deletedDiff = decorators.get(0);
		DSemanticDecorator value = (DSemanticDecorator)deletedDiff.getValue();
		Diff mapping = comparison.getDifferences(((DMappingBased)value).getMapping()).get(0);

		assertTrue(deletedDiff.getKind() == DELETE);
		assertEquals(mapping, deletedDiff.getImpliedBy().get(0));
	}

	/**
	 * Tests that the added nodes differences implies their mapping difference.
	 */
	@Test
	public void testAddedContainersImpliesMapping() {
		DefaultComparisonScope scope = new DefaultComparisonScope(addedContainers, deletedContainers, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<ReferenceChange> decorators = getAddedOrRemovedDecorators();

		ReferenceChange firstAddedDiff = decorators.get(0);
		DSemanticDecorator firstValue = (DSemanticDecorator)firstAddedDiff.getValue();
		Diff firstMapping = comparison.getDifferences(((DMappingBased)firstValue).getMapping()).get(0);

		ReferenceChange secondAddedDiff = decorators.get(1);
		DSemanticDecorator secondValue = (DSemanticDecorator)secondAddedDiff.getValue();
		Diff secondMapping = comparison.getDifferences(((DMappingBased)secondValue).getMapping()).get(0);

		assertTrue(firstAddedDiff.getKind() == ADD);
		assertTrue(secondAddedDiff.getKind() == ADD);
		assertEquals(firstMapping, firstAddedDiff.getImplies().get(0));
		assertEquals(secondMapping, secondAddedDiff.getImplies().get(0));
	}

	/**
	 * Tests that the deleted nodes differences are implied by their mapping difference.
	 */
	@Test
	public void testDeletedContainersImpliedByMapping() {
		DefaultComparisonScope scope = new DefaultComparisonScope(deletedContainers, addedContainers, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);
		List<ReferenceChange> decorators = getAddedOrRemovedDecorators();

		ReferenceChange firstDeletedDiff = decorators.get(0);
		DSemanticDecorator firstValue = (DSemanticDecorator)firstDeletedDiff.getValue();
		Diff firstMapping = comparison.getDifferences(((DMappingBased)firstValue).getMapping()).get(0);

		ReferenceChange secondDeletedDiff = decorators.get(1);
		DSemanticDecorator secondValue = (DSemanticDecorator)secondDeletedDiff.getValue();
		Diff secondMapping = comparison.getDifferences(((DMappingBased)secondValue).getMapping()).get(0);

		assertTrue(firstDeletedDiff.getKind() == DELETE);
		assertTrue(secondDeletedDiff.getKind() == DELETE);
		assertEquals(firstMapping, firstDeletedDiff.getImpliedBy().get(0));
		assertEquals(secondMapping, secondDeletedDiff.getImpliedBy().get(0));
	}

	/**
	 * Tests that the merge from right to left of a deleted container does not fail.
	 */
	@Test
	public void testMergeRightToLeftDeletedContainer() {
		DefaultComparisonScope scope = new DefaultComparisonScope(singleDeletedContainer,
				singleAddedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);

		mergeDiffsRightToLeft(comparison.getDifferences());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * Tests that the merge from left to right of a deleted container does not fail.
	 */
	@Test
	public void testMergeLeftToRightDeletedContainer() {
		DefaultComparisonScope scope = new DefaultComparisonScope(singleDeletedContainer,
				singleAddedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);

		mergeDiffsLeftToRight(comparison.getDifferences());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * Tests that the merge from right to left of an added container does not fail.
	 */
	@Test
	public void testMergeRightToLeftAddedContainer() {
		DefaultComparisonScope scope = new DefaultComparisonScope(singleAddedContainer,
				singleDeletedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);

		mergeDiffsRightToLeft(comparison.getDifferences());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * Tests that the merge from left to right of an added container does not fail.
	 */
	@Test
	public void testMergeLeftToRightAddedContainer() {
		DefaultComparisonScope scope = new DefaultComparisonScope(singleAddedContainer,
				singleDeletedContainer, origin);
		comparison = EMFCompare.builder().setPostProcessorRegistry(getPostProcessorRegistry()).build()
				.compare(scope);

		mergeDiffsLeftToRight(comparison.getDifferences());
		comparison.getDifferences().forEach(diff -> assertTrue(AbstractMerger.isInTerminalState(diff)));
	}

	/**
	 * It creates a collection of differences with DSemanticDecorator type values that have been added or
	 * deleted.
	 * 
	 * @see org.eclipse.sirius.viewpoint.DSemanticDecorator
	 * @return the list of added or removed decorators
	 */
	protected List<ReferenceChange> getAddedOrRemovedDecorators() {
		Stream<ReferenceChange> refChanges = comparison.getDifferences().stream()
				.filter(diff -> diff instanceof ReferenceChange
						&& ((ReferenceChange)diff).getReference().isContainment())
				.map(ReferenceChange.class::cast);
		Stream<ReferenceChange> addedOrRemovedSemanticDecorators = refChanges
				.filter(diff -> diff.getValue() instanceof DSemanticDecorator);
		return addedOrRemovedSemanticDecorators.collect(Collectors.toList());
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
	 */
	public class Bug561458 extends AbstractInputData {

		/**
		 * The data path.
		 */
		private static final String PATH_PREFIX = "data/_561458/"; //$NON-NLS-1$

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
