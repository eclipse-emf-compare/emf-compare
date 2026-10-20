/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.framework;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Test;

/**
 * Checks identifier priority separately from content matching, including fallback beneath identified parents.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class MatchingAlgorithmTest extends AbstractMatcherTest {
	/** Resource URI for the left fixture. */
	private static final String LEFT_URI = "left.nodes";

	/** Resource URI for the right fixture. */
	private static final String RIGHT_URI = "right.nodes";

	/**
	 * Selects the builder under test.
	 *
	 * @param matcher
	 *            the selected matcher
	 */
	public MatchingAlgorithmTest(MatchingAlgorithm matcher) {
		super(matcher);
	}

	/** With IDs disabled, matching follows content even when IDs imply different partners. */
	@Test
	public void neverUsesContentEvenWhenIdentifiersDisagree() {
		XMIResourceImpl left = model(LEFT_URI, false);
		XMIResourceImpl right = model(RIGHT_URI, true);
		Comparison result = matcher.builder(UseIdentifiers.NEVER).build()
				.compare(new DefaultComparisonScope(left, right, null));
		assertSame(right.getContents().get(0), result.getMatch(left.getContents().get(0)).getRight());
	}

	/** Only a fallback can pair roots lacking both XMI and attribute IDs. */
	@Test
	public void unidentifiedRootsUseFallback() {
		XMIResourceImpl left = new XMIResourceImpl(URI.createURI(LEFT_URI));
		XMIResourceImpl right = new XMIResourceImpl(URI.createURI(RIGHT_URI));
		// Node.name is itself an ID attribute: leave it unset as well as the XMI ID.
		left.getContents().add(NodesFactory.eINSTANCE.createNode());
		right.getContents().add(NodesFactory.eINSTANCE.createNode());
		Comparison result = matcher.builder(UseIdentifiers.WHEN_AVAILABLE).build()
				.compare(new DefaultComparisonScope(left, right, null));
		assertSame(right.getContents().get(0), result.getMatch(left.getContents().get(0)).getRight());
		Comparison identifiersOnly = matcher.builder(UseIdentifiers.ONLY).build()
				.compare(new DefaultComparisonScope(left, right, null));
		Match anonymousRoot = identifiersOnly.getMatch(left.getContents().get(0));
		assertTrue(anonymousRoot == null || anonymousRoot.getRight() == null);
	}

	/** Identified parents must not prevent fallback matching of their anonymous children. */
	@Test
	public void unidentifiedChildrenUseFallbackUnderIdentifiedParents() {
		XMIResourceImpl left = model(LEFT_URI, false);
		XMIResourceImpl right = model(RIGHT_URI, true);
		Node leftChild = NodesFactory.eINSTANCE.createNode();
		((Node)left.getContents().get(0)).getContainmentRef1().add(leftChild);
		Node rightChild = NodesFactory.eINSTANCE.createNode();
		((Node)right.getContents().get(1)).getContainmentRef1().add(rightChild);
		Comparison result = matcher.builder(UseIdentifiers.WHEN_AVAILABLE).build()
				.compare(new DefaultComparisonScope(left, right, null));
		assertSame(right.getContents().get(1), result.getMatch(left.getContents().get(0)).getRight());
		assertSame(rightChild, result.getMatch(leftChild).getRight());

		Comparison identifiersOnly = matcher.builder(UseIdentifiers.ONLY).build()
				.compare(new DefaultComparisonScope(left, right, null));
		assertSame(right.getContents().get(1), identifiersOnly.getMatch(left.getContents().get(0)).getRight());
		Match anonymousChild = identifiersOnly.getMatch(leftChild);
		assertTrue(anonymousChild == null || anonymousChild.getRight() == null);
	}

	/** Identifier matching takes priority over either fallback algorithm. */
	@Test
	public void identifierBuilderFollowsIdentifiersEvenWhenNamesDisagree() {
		XMIResourceImpl left = model(LEFT_URI, false);
		XMIResourceImpl right = model(RIGHT_URI, true);
		Comparison result = matcher.builder(UseIdentifiers.WHEN_AVAILABLE).build()
				.compare(new DefaultComparisonScope(left, right, null));
		assertSame(right.getContents().get(1), result.getMatch(left.getContents().get(0)).getRight());
	}

	/**
	 * Creates two named nodes whose XMI identifiers can be exchanged without changing their content.
	 *
	 * @param uri
	 *            the resource URI
	 * @param swapIds
	 *            whether the nodes have exchanged IDs
	 * @return the new resource
	 */
	private static XMIResourceImpl model(String uri, boolean swapIds) {
		XMIResourceImpl resource = new XMIResourceImpl(URI.createURI(uri));
		Node first = NodesFactory.eINSTANCE.createNode();
		first.setName("A");
		Node second = NodesFactory.eINSTANCE.createNode();
		second.setName("B");
		resource.getContents().add(first);
		resource.getContents().add(second);
		if (swapIds) {
			resource.setID(first, "b");
			resource.setID(second, "a");
		} else {
			resource.setID(first, "a");
			resource.setID(second, "b");
		}
		return resource;
	}
}
