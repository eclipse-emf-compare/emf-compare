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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodesFactory;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Test;

/**
 * Verifies that the shared test builders select the intended identifier policy. A wrong policy could let
 * identifier matching choose all the partners while the surrounding test appears to exercise content
 * matching.
 * <p>
 * Each fixture contains nodes named A and B on both sides, with their XMI IDs exchanged on the right. The
 * expected partner is therefore different depending on whether names or identifiers take precedence.
 * {@link MatcherKind#builder()} must follow names for content hash and IDs for the default proximity policy;
 * {@link MatcherKind#builderWithIdentifiers()} must follow IDs for both matchers.
 * </p>
 * <p>
 * Each assertion checks the exact fixture object selected as a partner. Both builder contracts are exercised
 * for each matcher through {@link AbstractMatcherTest}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("nls")
public class MatcherKindTest extends AbstractMatcherTest {
	/**
	 * Selects the builder under test.
	 *
	 * @param matcher
	 *            the selected matcher
	 */
	public MatcherKindTest(MatcherKind matcher) {
		super(matcher);
	}

	/** Hash matches names despite exchanged IDs; the original proximity policy follows IDs. */
	@Test
	public void defaultBuilderUsesContentForHashAndIdentifiersForProximity() {
		XMIResourceImpl left = model("left.nodes", false);
		XMIResourceImpl right = model("right.nodes", true);
		Comparison result = matcher.builder().build().compare(new DefaultComparisonScope(left, right, null));
		// Right index 0 retains name A; index 1 carries A's exchanged identifier.
		int expectedIndex = 1;
		if (matcher == MatcherKind.CONTENT_HASH) {
			expectedIndex = 0;
		}
		assertSame(right.getContents().get(expectedIndex),
				result.getMatch(left.getContents().get(0)).getRight());
	}

	/** Both builders follow exchanged IDs when the test explicitly requests identifier matching. */
	@Test
	public void identifierBuilderFollowsIdentifiersEvenWhenNamesDisagree() {
		XMIResourceImpl left = model("left.nodes", false);
		XMIResourceImpl right = model("right.nodes", true);
		Comparison result = matcher.builderWithIdentifiers().build()
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
