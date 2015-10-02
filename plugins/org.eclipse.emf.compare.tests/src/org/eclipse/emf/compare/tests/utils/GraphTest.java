/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - bug 478620
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

import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.internal.utils.PruningIterator;
import org.junit.Test;

/**
 * We will use this to test the utility methods exposed by the {@link Graph}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("nls")
public class GraphTest {

	@Test
	public void testBuildSubGraph() {
		Graph<String> graph = new Graph<String>();
		//@formatter:off
		/*
		 * Add the following graph:
		 *          e      f
		 *          |      |
		 *    b     c      d
		 *    \     |     /
		 *     \    |    /
		 *      ---------
		 *          |
		 *          a
		 */
		//@formatter:on
		graph.addChildren("a", ImmutableSet.of("b", "c", "d"));
		graph.addChildren("c", ImmutableSet.of("e"));
		graph.addChildren("d", ImmutableSet.of("f"));

		Set<String> subgraph = graph.getSubgraphContaining("d", ImmutableSet.of("c"));
		assertEquals(4, subgraph.size());
		assertTrue(subgraph.containsAll(Lists.newArrayList("a", "b", "d", "f")));
	}

	/*
	 * Just to avoid infinite loop in prune.
	 */
	@Test
	public void testPrune() {
		Graph<String> graph = new Graph<String>();
		//@formatter:off
		/*
		 * Add the following graph:
		 *          c-\
		 *          |  |
		 *          b-/
		 *          |
		 *          a
		 */
		//@formatter:on
		graph.addChildren("a", ImmutableSet.of("b"));
		graph.addChildren("b", ImmutableSet.of("c"));
		graph.addChildren("c", ImmutableSet.of("b"));
		PruningIterator<String> iterator = graph.breadthFirstIterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.prune();
		}
	}

	@Test
	public void testBreadthFirstIteration() {
		Graph<String> graph = new Graph<String>();
		//@formatter:off
		/*
		 * With the following Graph:
		 *
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
		 */
		//@formatter:on
		graph.addChildren("A", ImmutableSet.of("B", "C"));
		graph.addChildren("B", ImmutableSet.of("D"));
		graph.addChildren("C", ImmutableSet.of("E", "F"));
		graph.addChildren("I", ImmutableSet.of("G"));
		graph.addChildren("G", ImmutableSet.of("F", "H"));
		graph.addChildren("J", ImmutableSet.of("K", "L"));
		graph.addChildren("L", ImmutableSet.of("M", "N"));

		Set<String> firstThree = Sets.newHashSet("A", "I", "J");
		Set<String> nextFive = Sets.newHashSet("B", "C", "G", "K", "L");
		Set<String> finalSix = Sets.newHashSet("D", "E", "F", "H", "M", "N");

		PruningIterator<String> iterator = graph.breadthFirstIterator();
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

	@Test
	public void testTreeIteration_1() {
		Graph<String> graph = getAcyclicGraph();

		Iterator<String> iteratorOnA = graph.depthFirstIterator("A");
		assertEquals("A", iteratorOnA.next());
		assertEquals("B", iteratorOnA.next());
		assertEquals("D", iteratorOnA.next());
		assertEquals("C", iteratorOnA.next());
		assertEquals("E", iteratorOnA.next());
		assertEquals("F", iteratorOnA.next());
		assertFalse(iteratorOnA.hasNext());
	}

	@Test
	public void testTreeIteration_2() {
		Graph<String> graph = getAcyclicGraph();

		Iterator<String> iteratorOnC = graph.depthFirstIterator("C");
		assertEquals("C", iteratorOnC.next());
		assertEquals("E", iteratorOnC.next());
		assertEquals("F", iteratorOnC.next());
		assertFalse(iteratorOnC.hasNext());
	}

	@Test
	public void testTreeIteration_3() {
		Graph<String> graph = getAcyclicGraph();

		Iterator<String> iteratorOnI = graph.depthFirstIterator("I");
		assertEquals("I", iteratorOnI.next());
		assertEquals("G", iteratorOnI.next());
		assertEquals("F", iteratorOnI.next());
		assertEquals("H", iteratorOnI.next());
		assertFalse(iteratorOnI.hasNext());
	}

	@Test
	public void testTreeIteration_4() {
		Graph<String> graph = getAcyclicGraph();

		Iterator<String> iteratorOnJ = graph.depthFirstIterator("J");
		assertEquals("J", iteratorOnJ.next());
		assertEquals("K", iteratorOnJ.next());
		assertEquals("L", iteratorOnJ.next());
		assertEquals("M", iteratorOnJ.next());
		assertEquals("N", iteratorOnJ.next());
		assertFalse(iteratorOnJ.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_1() {
		Graph<String> graph = getGraphWithCycles();

		Iterator<String> iteratorOnA = graph.depthFirstIterator("A");
		assertEquals("A", iteratorOnA.next());
		assertEquals("B", iteratorOnA.next());
		assertEquals("D", iteratorOnA.next());
		assertEquals("E", iteratorOnA.next());
		assertEquals("C", iteratorOnA.next());
		assertEquals("F", iteratorOnA.next());
		assertEquals("H", iteratorOnA.next());

		assertFalse(iteratorOnA.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_2() {
		Graph<String> graph = getGraphWithCycles();

		Iterator<String> iteratorOnC = graph.depthFirstIterator("C");
		assertEquals("C", iteratorOnC.next());
		assertEquals("A", iteratorOnC.next());
		assertEquals("B", iteratorOnC.next());
		assertEquals("D", iteratorOnC.next());
		assertEquals("E", iteratorOnC.next());
		assertEquals("F", iteratorOnC.next());
		assertEquals("H", iteratorOnC.next());

		assertFalse(iteratorOnC.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_3() {
		Graph<String> graph = getGraphWithCycles();

		Iterator<String> iteratorOnI = graph.depthFirstIterator("I");
		assertEquals("I", iteratorOnI.next());
		assertEquals("G", iteratorOnI.next());
		assertEquals("F", iteratorOnI.next());
		assertEquals("H", iteratorOnI.next());

		assertFalse(iteratorOnI.hasNext());
	}

	@Test
	public void testDepthIterationWithCycles_4() {
		Graph<String> graph = getGraphWithCycles();

		Iterator<String> iteratorOnJ = graph.depthFirstIterator("J");
		assertEquals("J", iteratorOnJ.next());
		assertEquals("K", iteratorOnJ.next());
		assertEquals("M", iteratorOnJ.next());
		assertEquals("L", iteratorOnJ.next());
		assertEquals("N", iteratorOnJ.next());

		assertFalse(iteratorOnJ.hasNext());
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
	 * </pre>
	 */
	private Graph<String> getAcyclicGraph() {
		Graph<String> graph = new Graph<String>();

		graph.addChildren("A", ImmutableSet.of("B", "C"));
		graph.addChildren("B", ImmutableSet.of("D"));
		graph.addChildren("C", ImmutableSet.of("E", "F"));
		graph.addChildren("I", ImmutableSet.of("G"));
		graph.addChildren("G", ImmutableSet.of("F", "H"));
		graph.addChildren("J", ImmutableSet.of("K", "L"));
		graph.addChildren("L", ImmutableSet.of("M", "N"));

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
	 * </pre>
	 */
	private Graph<String> getGraphWithCycles() {
		Graph<String> graph = new Graph<String>();

		graph.addChildren("A", ImmutableSet.of("B", "C"));
		graph.addChildren("B", ImmutableSet.of("D"));
		graph.addChildren("D", ImmutableSet.of("E"));
		graph.addChildren("C", ImmutableSet.of("A", "E", "F"));
		graph.addChildren("I", ImmutableSet.of("G"));
		graph.addChildren("G", ImmutableSet.of("F", "H"));
		graph.addChildren("J", ImmutableSet.of("K", "L"));
		graph.addChildren("K", ImmutableSet.of("M"));
		graph.addChildren("M", ImmutableSet.of("L"));
		graph.addChildren("L", ImmutableSet.of("M", "N"));
		graph.addChildren("F", ImmutableSet.of("H"));
		graph.addChildren("H", ImmutableSet.of("F"));

		return graph;
	}

}
