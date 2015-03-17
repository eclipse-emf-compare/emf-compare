/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.Set;

import org.eclipse.emf.compare.internal.utils.Graph;
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
}
