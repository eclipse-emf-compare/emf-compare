/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.match.resource.RootIDMatchingStrategy;
import org.eclipse.emf.compare.tests.match.data.MatchInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class RootIDMatchingTest {

	private MatchInputData input = new MatchInputData();

	/**
	 * Tests a scenario in which all root ids are different and no match should be returned.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDTwoWayA1() throws IOException {
		List<Resource> left = input.getRootIDTwoWayA1Left();
		List<Resource> right = input.getRootIDTwoWayA1Right();
		List<Resource> origin = Lists.newLinkedList();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}

	/**
	 * Tests a scenario in which one pair of root ids intersect and one match should be returned.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDTwoWayA2() throws IOException {
		List<Resource> left = input.getRootIDTwoWayA2Left();
		List<Resource> right = input.getRootIDTwoWayA2Right();
		List<Resource> origin = Lists.newLinkedList();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(1, matchResources.size());
	}

	/**
	 * Tests a scenario in which each file has at least one root id which intersects and therefore two matches
	 * should be returned.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDTwoWayA3() throws IOException {
		List<Resource> left = input.getRootIDTwoWayA3Left();
		List<Resource> right = input.getRootIDTwoWayA3Right();
		List<Resource> origin = Lists.newLinkedList();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(2, matchResources.size());
	}

	/**
	 * Tests a scenario in which one resource from the left intersects with all files from the right,
	 * therefore no match should be returned.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDTwoWayA4() throws IOException {
		List<Resource> left = input.getRootIDTwoWayA4Left();
		List<Resource> right = input.getRootIDTwoWayA4Right();
		List<Resource> origin = Lists.newLinkedList();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}

	/**
	 * Tests a scenario in which one resource from the left intersects with both files from the right, while
	 * the other left file intersects with only one. Still no match should be returned.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDTwoWayA5() throws IOException {
		List<Resource> left = input.getRootIDTwoWayA5Left();
		List<Resource> right = input.getRootIDTwoWayA5Right();
		List<Resource> origin = Lists.newLinkedList();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}

	/**
	 * Tests a scenario in which one resource from the right intersects with both files from the left, while
	 * the other right file intersects with only one. Still no match should be returned.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDTwoWayA6() throws IOException {
		List<Resource> left = input.getRootIDTwoWayA6Left();
		List<Resource> right = input.getRootIDTwoWayA6Right();
		List<Resource> origin = Lists.newLinkedList();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}
}
