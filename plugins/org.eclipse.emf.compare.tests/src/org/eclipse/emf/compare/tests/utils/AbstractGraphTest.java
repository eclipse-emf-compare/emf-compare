/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - bug 478620
 *     Martin Fleck - bug 507177
 *******************************************************************************/
package org.eclipse.emf.compare.tests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.compare.graph.IGraph;
import org.eclipse.emf.compare.graph.PruningIterator;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.junit.Test;

/**
 * We will use this to test the utility methods exposed by the {@link Graph}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings({"nls", "unchecked" })
public abstract class AbstractGraphTest<E> {
	protected abstract E toType(final String name);

	protected abstract IGraph<E> createGraph();

	/**
	 * Test correct subgraph.
	 * 
	 * <pre>
	 *       _A_
	 *      / | \
	 *     B  C  D
	 *        |  |
	 *        E  F
	 * </pre>
	 */
	@Test
	public void testBuildSubGraph() {
		IGraph<E> graph = createGraph();
		graph.addChildren(toType("A"), ImmutableSet.of(toType("B"), toType("C"), toType("D")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("E")));
		graph.addChildren(toType("D"), ImmutableSet.of(toType("F")));

		Set<E> subgraph = graph.getSubgraphContaining(toType("D"), ImmutableSet.of(toType("C")));
		assertEquals(4, subgraph.size());
		assertTrue(
				subgraph.containsAll(Lists.newArrayList(toType("A"), toType("B"), toType("D"), toType("F"))));
	}

	/**
	 * Test avoidance of infinite loops in cyclic graph.
	 * 
	 * <pre>
	 *          C-+
	 *          | |
	 *          B-+
	 *          |
	 *          A
	 * </pre>
	 */
	@Test
	public void testPrune() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("B")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("C")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("B")));
		PruningIterator<E> iterator = graph.breadthFirstIterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.prune();
		}
	}

	/**
	 * Tests breadth first iteration with the following Graph:
	 * 
	 * <pre>
	 *     A       I    J
	 *    / \     /    / \
	 *   B   C   G    K   L
	 *  /   / \ / \      / \
	 * D   E   F   H    M   N
	 * 
	 * We expect our iteration to go in the following order:
	 * three first items, in unspecified order : A, I, J
	 * next five, in unspecified order :         B, C, G, K, L
	 * finally, still in unspecified order :     D, E, F, H, M, N
	 * </pre>
	 */
	@Test
	public void testBreadthFirstIteration() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("B"), toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("D")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("E"), toType("F")));
		graph.addChildren(toType("I"), ImmutableSet.of(toType("G")));
		graph.addChildren(toType("G"), ImmutableSet.of(toType("F"), toType("H")));
		graph.addChildren(toType("J"), ImmutableSet.of(toType("K"), toType("L")));
		graph.addChildren(toType("L"), ImmutableSet.of(toType("M"), toType("N")));

		Set<E> firstThree = Sets.newHashSet(toType("A"), toType("I"), toType("J"));
		Set<E> nextFive = Sets.newHashSet(toType("B"), toType("C"), toType("G"), toType("K"), toType("L"));
		Set<E> finalSix = Sets.newHashSet(toType("D"), toType("E"), toType("F"), toType("H"), toType("M"),
				toType("N"));

		PruningIterator<E> iterator = graph.breadthFirstIterator();
		assertTrue(firstThree.remove(iterator.next()));
		assertTrue(firstThree.remove(iterator.next()));
		assertTrue(firstThree.remove(iterator.next()));
		assertTrue(firstThree.isEmpty());

		assertTrue(nextFive.remove(iterator.next()));
		assertTrue(nextFive.remove(iterator.next()));
		assertTrue(nextFive.remove(iterator.next()));
		assertTrue(nextFive.remove(iterator.next()));
		assertTrue(nextFive.remove(iterator.next()));
		assertTrue(nextFive.isEmpty());

		assertTrue(finalSix.remove(iterator.next()));
		assertTrue(finalSix.remove(iterator.next()));
		assertTrue(finalSix.remove(iterator.next()));
		assertTrue(finalSix.remove(iterator.next()));
		assertTrue(finalSix.remove(iterator.next()));
		assertTrue(finalSix.remove(iterator.next()));
		assertTrue(finalSix.isEmpty());
		assertFalse(iterator.hasNext());
	}

	/**
	 * This test case ensures that pruned elements are not returned in a scenario where the next iterator in
	 * the graph has already been prepared. This test uses the following graph:
	 * 
	 * <pre>
	 * A   B
	 *  \ /
	 *   C
	 * </pre>
	 */
	@Test
	public void testBreadthFirstIteration_MultipleParents() {
		IGraph<E> graph = createGraph();
		graph.addChildren(toType("A"), ImmutableSet.of(toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("C")));

		PruningIterator<E> breadthFirstIterator = graph.breadthFirstIterator();

		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("A"), breadthFirstIterator.next());

		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("B"), breadthFirstIterator.next());

		breadthFirstIterator.prune(); // prune all children of B: [C]

		assertFalse(breadthFirstIterator.hasNext());
	}

	/**
	 * This test case ensures that pruned elements are not returned in a scenario where the next iterator in
	 * the graph has already been prepared:
	 * 
	 * <pre>
	 *   _A_   B
	 *  / | \ /
	 * D  E  C
	 * </pre>
	 */
	@Test
	public void testBreadthFirstIteration_MultipleParentsMultipleChildren() {
		IGraph<E> graph = createGraph();
		graph.addChildren(toType("A"), ImmutableSet.of(toType("C"), toType("D"), toType("E")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("C"), toType("D")));

		PruningIterator<E> breadthFirstIterator = graph.breadthFirstIterator();

		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("A"), breadthFirstIterator.next());

		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("B"), breadthFirstIterator.next());

		breadthFirstIterator.prune(); // prune all children of B: [C, D]

		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("E"), breadthFirstIterator.next());

		assertFalse(breadthFirstIterator.hasNext());
	}

	/**
	 * This test case ensures that elements over multiple lvels are pruned correctly and are not returned
	 * through another path in the graph:
	 * 
	 * <pre>
	 *   A   B
	 *  / \ /
	 * D   C
	 *  \ /
	 *   E
	 * </pre>
	 */
	@Test
	public void testBreadthFirstIteration_PruneMultipleLevels() {
		IGraph<E> graph = new Graph<E>();
		// first level
		graph.addChildren(toType("A"), ImmutableSet.of(toType("D"), toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("C")));

		// second level
		graph.addChildren(toType("D"), ImmutableSet.of(toType("E")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("E")));

		PruningIterator<E> breadthFirstIterator = graph.breadthFirstIterator();

		// iterate over first level
		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("A"), breadthFirstIterator.next());

		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("B"), breadthFirstIterator.next());

		// prune all children of B: [C and also E from the level below]
		breadthFirstIterator.prune();

		assertTrue(breadthFirstIterator.hasNext());
		assertEquals(toType("D"), breadthFirstIterator.next());

		// no more children as C and E have been pruned
		assertFalse(breadthFirstIterator.hasNext());
	}

	@Test
	public void testTreeIteration_1() {
		IGraph<E> graph = getAcyclicGraph();

		Iterator<E> iteratorOnA = graph.depthFirstIterator(toType("A"));
		assertEquals(toType("A"), iteratorOnA.next());
		assertEquals(toType("B"), iteratorOnA.next());
		assertEquals(toType("D"), iteratorOnA.next());
		assertEquals(toType("C"), iteratorOnA.next());
		assertEquals(toType("E"), iteratorOnA.next());
		assertEquals(toType("F"), iteratorOnA.next());
		assertFalse(iteratorOnA.hasNext());
	}

	@Test
	public void testTreeIteration_2() {
		IGraph<E> graph = getAcyclicGraph();

		Iterator<E> iteratorOnC = graph.depthFirstIterator(toType("C"));
		assertEquals(toType("C"), iteratorOnC.next());
		assertEquals(toType("E"), iteratorOnC.next());
		assertEquals(toType("F"), iteratorOnC.next());
		assertFalse(iteratorOnC.hasNext());
	}

	@Test
	public void testTreeIteration_3() {
		IGraph<E> graph = getAcyclicGraph();

		Iterator<E> iteratorOnI = graph.depthFirstIterator(toType("I"));
		assertEquals(toType("I"), iteratorOnI.next());
		assertEquals(toType("G"), iteratorOnI.next());
		assertEquals(toType("F"), iteratorOnI.next());
		assertEquals(toType("H"), iteratorOnI.next());
		assertFalse(iteratorOnI.hasNext());
	}

	@Test
	public void testTreeIteration_4() {
		IGraph<E> graph = getAcyclicGraph();

		Iterator<E> iteratorOnJ = graph.depthFirstIterator(toType("J"));
		assertEquals(toType("J"), iteratorOnJ.next());
		assertEquals(toType("K"), iteratorOnJ.next());
		assertEquals(toType("L"), iteratorOnJ.next());
		assertEquals(toType("M"), iteratorOnJ.next());
		assertEquals(toType("N"), iteratorOnJ.next());
		assertFalse(iteratorOnJ.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_1() {
		IGraph<E> graph = getGraphWithCycles();

		Iterator<E> iteratorOnA = graph.depthFirstIterator(toType("A"));
		assertEquals(toType("A"), iteratorOnA.next());
		assertEquals(toType("B"), iteratorOnA.next());
		assertEquals(toType("D"), iteratorOnA.next());
		assertEquals(toType("E"), iteratorOnA.next());
		assertEquals(toType("C"), iteratorOnA.next());
		assertEquals(toType("F"), iteratorOnA.next());
		assertEquals(toType("H"), iteratorOnA.next());

		assertFalse(iteratorOnA.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_2() {
		IGraph<E> graph = getGraphWithCycles();

		Iterator<E> iteratorOnC = graph.depthFirstIterator(toType("C"));
		assertEquals(toType("C"), iteratorOnC.next());
		assertEquals(toType("A"), iteratorOnC.next());
		assertEquals(toType("B"), iteratorOnC.next());
		assertEquals(toType("D"), iteratorOnC.next());
		assertEquals(toType("E"), iteratorOnC.next());
		assertEquals(toType("F"), iteratorOnC.next());
		assertEquals(toType("H"), iteratorOnC.next());

		assertFalse(iteratorOnC.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_3() {
		IGraph<E> graph = getGraphWithCycles();

		Iterator<E> iteratorOnI = graph.depthFirstIterator(toType("I"));
		assertEquals(toType("I"), iteratorOnI.next());
		assertEquals(toType("G"), iteratorOnI.next());
		assertEquals(toType("F"), iteratorOnI.next());
		assertEquals(toType("H"), iteratorOnI.next());

		assertFalse(iteratorOnI.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_4() {
		IGraph<E> graph = getGraphWithCycles();

		Iterator<E> iteratorOnJ = graph.depthFirstIterator(toType("J"));
		assertEquals(toType("J"), iteratorOnJ.next());
		assertEquals(toType("K"), iteratorOnJ.next());
		assertEquals(toType("M"), iteratorOnJ.next());
		assertEquals(toType("L"), iteratorOnJ.next());
		assertEquals(toType("N"), iteratorOnJ.next());

		assertFalse(iteratorOnJ.hasNext());
	}

	/**
	 * Test the BreadthFirstIterator with the following cyclic graph:
	 * 
	 * <pre>
	 *          A
	 *         / \
	 *        B = C
	 * </pre>
	 */
	@Test
	public void testBug503035_1() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("B"), toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("C")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("B")));

		Iterator<E> it = graph.breadthFirstIterator();
		assertTrue(it.hasNext());
		assertEquals(toType("A"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("B"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("C"), it.next());
		assertFalse(it.hasNext());
	}

	/**
	 * Test the BreadthFirstIterator with the following cyclic graph:
	 * 
	 * <pre>
	 *          A
	 *         / \
	 *        B = C
	 *       /
	 *      D
	 * </pre>
	 */
	@Test
	public void testBug503035_2() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("B"), toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("D"), toType("C")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("B")));

		Iterator<E> it = graph.breadthFirstIterator();
		assertTrue(it.hasNext());
		assertEquals(toType("A"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("B"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("C"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("D"), it.next());
		assertFalse(it.hasNext());
	}

	/**
	 * Test the BreadthFirstIterator with the following cyclic graph:
	 * 
	 * <pre>
	 *        A   B 
	 *        |   |
	 *        C = D
	 * </pre>
	 */
	@Test
	public void testBug503035_3() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("D")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("D")));
		graph.addChildren(toType("D"), ImmutableSet.of(toType("C")));

		Iterator<E> it = graph.breadthFirstIterator();
		assertTrue(it.hasNext());
		assertEquals(toType("A"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("B"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("C"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("D"), it.next());
		assertFalse(it.hasNext());
	}

	/**
	 * Test the BreadthFirstIterator with the following cyclic graph:
	 * 
	 * <pre>
	 *          A
	 *         /|\
	 *        / | \
	 *       B  C  D
	 *       \\===//
	 * </pre>
	 */
	@Test
	public void testBug503035_4() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("B"), toType("C"), toType("D")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("D")));
		graph.addChildren(toType("D"), ImmutableSet.of(toType("B")));

		Iterator<E> it = graph.breadthFirstIterator();
		assertTrue(it.hasNext());
		assertEquals(toType("A"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("B"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("C"), it.next());
		assertTrue(it.hasNext());
		assertEquals(toType("D"), it.next());
		assertFalse(it.hasNext());
	}

	/**
	 * @return The following acyclic graph:
	 * 
	 *         <pre>
	 *     A       I    J
	 *    / \     /    / \
	 *   B   C   G    K   L
	 *  /   / \ / \      / \
	 * D   E   F   H    M   N
	 *         </pre>
	 */
	protected IGraph<E> getAcyclicGraph() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("B"), toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("D")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("E"), toType("F")));
		graph.addChildren(toType("I"), ImmutableSet.of(toType("G")));
		graph.addChildren(toType("G"), ImmutableSet.of(toType("F"), toType("H")));
		graph.addChildren(toType("J"), ImmutableSet.of(toType("K"), toType("L")));
		graph.addChildren(toType("L"), ImmutableSet.of(toType("M"), toType("N")));

		return graph;
	}

	/**
	 * @return The following cyclic graph:
	 * 
	 *         <pre>
	 *     A       I    J
	 *    / \\    /    /  \
	 *   B   C   G    K    L
	 *  /   / \ / \    \ // \
	 * D - E   F = H    M    N
	 *         </pre>
	 */
	protected IGraph<E> getGraphWithCycles() {
		IGraph<E> graph = createGraph();

		graph.addChildren(toType("A"), ImmutableSet.of(toType("B"), toType("C")));
		graph.addChildren(toType("B"), ImmutableSet.of(toType("D")));
		graph.addChildren(toType("D"), ImmutableSet.of(toType("E")));
		graph.addChildren(toType("C"), ImmutableSet.of(toType("A"), toType("E"), toType("F")));
		graph.addChildren(toType("I"), ImmutableSet.of(toType("G")));
		graph.addChildren(toType("G"), ImmutableSet.of(toType("F"), toType("H")));
		graph.addChildren(toType("J"), ImmutableSet.of(toType("K"), toType("L")));
		graph.addChildren(toType("K"), ImmutableSet.of(toType("M")));
		graph.addChildren(toType("M"), ImmutableSet.of(toType("L")));
		graph.addChildren(toType("L"), ImmutableSet.of(toType("M"), toType("N")));
		graph.addChildren(toType("F"), ImmutableSet.of(toType("H")));
		graph.addChildren(toType("H"), ImmutableSet.of(toType("F")));

		return graph;
	}
}
