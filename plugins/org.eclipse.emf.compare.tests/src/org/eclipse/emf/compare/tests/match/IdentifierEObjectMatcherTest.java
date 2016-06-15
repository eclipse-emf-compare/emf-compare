/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

/**
 * Test class for {@link IdentifierEObjectMatcher}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("nls")
public class IdentifierEObjectMatcherTest {

	/**
	 * Test the matchPerId function, especially the containment between matches.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testMatchPerIdFunction() throws IOException {

		Node root = NodesFactory.eINSTANCE.createNode();
		root.setName("root");
		Node nodeLevel1 = NodesFactory.eINSTANCE.createNode();
		nodeLevel1.setName("nodeLevel1");
		Node nodeLevel2 = NodesFactory.eINSTANCE.createNode();
		nodeLevel2.setName("nodeLevel2");
		Node nodeLevel3 = NodesFactory.eINSTANCE.createNode();
		nodeLevel3.setName("nodeLevel3");
		root.getContainmentRef1().add(nodeLevel1);
		nodeLevel1.getContainmentRef1().add(nodeLevel2);
		nodeLevel2.getContainmentRef1().add(nodeLevel3);

		// Give the eObjects to matchPerId function in opposite containment order, to force the matchPerId
		// method to reorganize matches.
		Collection<Node> nodes = Lists.newArrayList(nodeLevel3, nodeLevel2, nodeLevel1, root);

		MockIdentifierEObjectMatcher matcher = new MockIdentifierEObjectMatcher();

		Iterator<? extends EObject> leftEObjects = nodes.iterator();
		Iterator<? extends EObject> rightEObjects = nodes.iterator();
		Iterator<? extends EObject> originEObjects = Iterators.emptyIterator();
		List<EObject> leftEObjectsNoID = Lists.newArrayList();
		List<EObject> rightEObjectsNoID = Lists.newArrayList();
		List<EObject> originEObjectsNoID = Lists.newArrayList();

		Set<Match> matches = matcher.matchPerId(leftEObjects, rightEObjects, originEObjects, leftEObjectsNoID,
				rightEObjectsNoID, originEObjectsNoID);

		assertEquals(1, matches.size());
		Match rootMatch = matches.iterator().next();
		assertEquals(root, rootMatch.getLeft());
		EList<Match> rootSubMatches = rootMatch.getSubmatches();
		assertEquals(1, rootSubMatches.size());
		Match nodeLevel1Match = rootSubMatches.iterator().next();
		assertEquals(nodeLevel1, nodeLevel1Match.getLeft());
		EList<Match> nodeLevel1SubMatches = nodeLevel1Match.getSubmatches();
		assertEquals(1, nodeLevel1SubMatches.size());
		Match nodeLevel2Match = nodeLevel1SubMatches.iterator().next();
		assertEquals(nodeLevel2, nodeLevel2Match.getLeft());
		EList<Match> nodeLevel2SubMatches = nodeLevel2Match.getSubmatches();
		assertEquals(1, nodeLevel2SubMatches.size());
		Match nodeLevel3Match = nodeLevel2SubMatches.iterator().next();
		assertEquals(nodeLevel3, nodeLevel3Match.getLeft());
		EList<Match> nodeLevel3SubMatches = nodeLevel3Match.getSubmatches();
		assertTrue(nodeLevel3SubMatches.isEmpty());

	}

	/**
	 * Mock {@link IdentifierEObjectMatcher} to test matchPerId method.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	private class MockIdentifierEObjectMatcher extends IdentifierEObjectMatcher {
		@Override
		public Set<Match> matchPerId(Iterator<? extends EObject> leftEObjects,
				Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects,
				List<EObject> leftEObjectsNoID, List<EObject> rightEObjectsNoID,
				List<EObject> originEObjectsNoID) {
			return super.matchPerId(leftEObjects, rightEObjects, originEObjects, leftEObjectsNoID,
					rightEObjectsNoID, originEObjectsNoID);
		}
	}
}
