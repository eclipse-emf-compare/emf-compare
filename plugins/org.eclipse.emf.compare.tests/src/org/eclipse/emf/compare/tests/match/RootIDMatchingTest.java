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
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.match.resource.RootIDMatchingStrategy;
import org.eclipse.emf.compare.tests.match.data.MatchInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
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

	/**
	 * Tests a scenario in which no ids intersect, therefore no matches should be returned.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA1() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA1Left();
		List<Resource> right = input.getRootIDThreeWayA1Right();
		List<Resource> origin = input.getRootIDThreeWayA1Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}

	/**
	 * Tests a scenario in which a left resource intersects with an origin resource, a right resource
	 * intersects with another origin resource and no left and right resource intersects each other.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA2() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA2Left();
		List<Resource> right = input.getRootIDThreeWayA2Right();
		List<Resource> origin = input.getRootIDThreeWayA2Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(2, matchResources.size());
		assertContainsMatching(matchResources, "left.nodes", null, "origin2.nodes");
		assertContainsMatching(matchResources, null, "right2.nodes", "origin.nodes");
	}

	/**
	 * Tests a scenario in which a left resource intersects with an origin resource, a right resource
	 * intersects with the same origin resource and these resources are therefore matched together, although
	 * the left resource does not intersect the right resource.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA3() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA3Left();
		List<Resource> right = input.getRootIDThreeWayA3Right();
		List<Resource> origin = input.getRootIDThreeWayA3Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(1, matchResources.size());
		assertContainsMatching(matchResources, "left.nodes", "right.nodes", "origin.nodes");
	}

	/**
	 * Tests a scenario in which a left resource does not intersect with an origin resource, a right resource
	 * intersects with one origin resource while the left and right resource also intersect. These resources
	 * should therefore be matched together, although the left resource does not intersect the origin
	 * resource.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA4() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA4Left();
		List<Resource> right = input.getRootIDThreeWayA4Right();
		List<Resource> origin = input.getRootIDThreeWayA4Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(1, matchResources.size());
		assertContainsMatching(matchResources, "left.nodes", "right2.nodes", "origin.nodes");
	}

	/**
	 * Tests a scenario in which a left resource does intersect with an origin resource, a right resource does
	 * not intersect with an origin resource while the left and right resource also intersect. These resources
	 * should therefore be matched together, although the right resource does not intersect the origin
	 * resource.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA5() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA5Left();
		List<Resource> right = input.getRootIDThreeWayA5Right();
		List<Resource> origin = input.getRootIDThreeWayA5Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(1, matchResources.size());
		assertContainsMatching(matchResources, "left2.nodes", "right.nodes", "origin.nodes");
	}

	/**
	 * Tests a scenario in which resources intersect left, right and origin. Therefore they should be matched
	 * together.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA6() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA6Left();
		List<Resource> right = input.getRootIDThreeWayA6Right();
		List<Resource> origin = input.getRootIDThreeWayA6Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(2, matchResources.size());
		assertContainsMatching(matchResources, "left.nodes", "right.nodes", "origin.nodes");
		assertContainsMatching(matchResources, "left2.nodes", "right2.nodes", "origin2.nodes");
	}

	/**
	 * Tests a scenario in which three resources intersect left, right and origin but the origin intersects
	 * with multiple resources left. Therefore there should be no matches.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA7() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA7Left();
		List<Resource> right = input.getRootIDThreeWayA7Right();
		List<Resource> origin = input.getRootIDThreeWayA7Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}

	/**
	 * Tests a scenario in which three resources intersect left, right and origin but the origin intersects
	 * with multiple resources from right. Therefore there should be no matches.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA8() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA8Left();
		List<Resource> right = input.getRootIDThreeWayA8Right();
		List<Resource> origin = input.getRootIDThreeWayA8Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}

	/**
	 * Tests a scenario in which three resources intersect left, right and origin but the right intersects
	 * with multiple resources from left. Therefore there should be no matches.
	 * 
	 * @throws IOException
	 *             if {@link MatchInputData} fails to load the test models.
	 */
	@Test
	public void testRootIDThreeWayA9() throws IOException {
		List<Resource> left = input.getRootIDThreeWayA9Left();
		List<Resource> right = input.getRootIDThreeWayA9Right();
		List<Resource> origin = input.getRootIDThreeWayA9Origin();

		RootIDMatchingStrategy strategy = new RootIDMatchingStrategy();
		List<MatchResource> matchResources = strategy.matchResources(left, right, origin);

		assertEquals(0, matchResources.size());
	}

	private void assertContainsMatching(Collection<MatchResource> matchResources, String left, String right,
			String origin) {
		boolean foundMatching = false;
		for (MatchResource matchResource : matchResources) {
			if (nameEquals(matchResource.getLeftURI(), left)
					&& nameEquals(matchResource.getRightURI(), right)
					&& nameEquals(matchResource.getOriginURI(), origin)) {
				foundMatching = true;
				break;
			}
		}
		assertTrue(foundMatching);
	}

	private boolean nameEquals(String uri, String name) {
		if (uri == null && name == null) {
			return true;
		}
		if (uri == null || name == null) {
			return false;
		}
		return uri.endsWith(name);
	}
}
