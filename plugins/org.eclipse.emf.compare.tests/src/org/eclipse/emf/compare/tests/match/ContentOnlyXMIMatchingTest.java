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
package org.eclipse.emf.compare.tests.match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractMatcherTest;
import org.eclipse.emf.compare.tests.framework.MatchingAlgorithm;
import org.eclipse.emf.compare.tests.match.data.MatchInputData;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.junit.Test;

/**
 * Verifies content-only matching on XMI models whose identifiers disagree with their content. Each case
 * runs with both proximity and content-hash matching, with identifier matching disabled.
 * <p>
 * The three scenarios make the intended policy observable:
 * </p>
 * <ul>
 * <li>a shared ID does not force the changed node in the fixture to match, while its unchanged sibling keeps
 * its counterpart;</li>
 * <li>exchanging IDs between two nodes does not override the partners selected by their stored names;</li>
 * <li>different generated IDs do not split identical content into additions and deletions.</li>
 * </ul>
 * <p>
 * These cases guard against accidentally enabling identifier-first matching in
 * {@link MatchingAlgorithm#builder(UseIdentifiers)} with {@link UseIdentifiers#NEVER}. Assertions check
 * exact fixture partners, missing counterparts and
 * expected difference counts. IDs used in assertions only locate fixture objects; the expected partners
 * come from their names and content, independently for each matcher.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ContentOnlyXMIMatchingTest extends AbstractMatcherTest {
	/** XMI ID retained by the node that gains a name in the fixture. */
	private static final String FIRST_ID = "_a";

	/** XMI ID of the fixture's unchanged node. */
	private static final String SECOND_ID = "_b";

	/** Fresh resource loader for this individual test instance. */
	private final MatchInputData input = new MatchInputData();

	/**
	 * Selects the content matcher.
	 *
	 * @param matcher
	 *            the matcher under test
	 */
	public ContentOnlyXMIMatchingTest(MatchingAlgorithm matcher) {
		super(matcher);
	}

	/**
	 * Records the current content-only result when the node with ID _a gains the previously absent name
	 * "a": it remains unmatched on both sides, while its unchanged sibling keeps its counterpart. The five
	 * differences are a regression total for that result, not a claim that replacing the node is preferable
	 * to recognizing the name change. The shared ID must not itself force a pairing.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded
	 */
	@Test
	public void sharedXMIIdentifierDoesNotForceChangedContentToMatch() throws IOException {
		Resource left = input.getSetIDAttributeLeft();
		Resource right = input.getSetIDAttributeRight();
		Comparison result = matcher.builder(UseIdentifiers.NEVER).build()
				.compare(new DefaultComparisonScope(left, right, null));
		EObject original = left.getEObject(FIRST_ID);
		EObject changed = right.getEObject(FIRST_ID);
		// The shared ID is only a fixture lookup key; it must not force these two objects into one match.
		assertNull(result.getMatch(original).getRight());
		assertNull(result.getMatch(changed).getLeft());
		assertSame(right.getEObject(SECOND_ID), result.getMatch(left.getEObject(SECOND_ID)).getRight());
		assertEquals(5, result.getDifferences().size());
	}

	/**
	 * Exchanging XMI IDs must not change the partners chosen by their stored names. The cross-ID assertions
	 * distinguish content matching from identifier-first matching; the difference count alone would not.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded
	 */
	@Test
	public void namesDeterminePartnersWhenXMIIdentifiersAreExchanged() throws IOException {
		Resource left = input.getXMIIDPriorityA1Left();
		Resource right = input.getXMIIDPriorityA1Right();
		Comparison result = matcher.builder(UseIdentifiers.NEVER).build()
				.compare(new DefaultComparisonScope(left, right, null));
		assertSame(right.getEObject(SECOND_ID), result.getMatch(left.getEObject(FIRST_ID)).getRight());
		assertSame(right.getEObject(FIRST_ID), result.getMatch(left.getEObject(SECOND_ID)).getRight());
		assertEquals(1, result.getDifferences().size());
	}

	/**
	 * Freshly generated IDs must not turn identical nodes into additions and deletions.
	 *
	 * @throws IOException
	 *             if the fixture cannot be loaded
	 */
	@Test
	public void generatedXMIIdentifiersDoNotSplitIdenticalContent() throws IOException {
		Resource left = input.getXMIIDPriorityA2Left();
		Resource right = input.getXMIIDPriorityA2Right();
		Node leftRoot = (Node)left.getContents().get(0);
		Node rightRoot = (Node)right.getContents().get(0);
		Node first = leftRoot.getContainmentRef1().get(0);
		Node counterpart = rightRoot.getContainmentRef1().get(0);
		// Check the fixture premise before comparing: equal IDs would make this case inconclusive.
		assertNotEquals(((XMLResource)left).getID(first), ((XMLResource)right).getID(counterpart));
		Comparison result = matcher.builder(UseIdentifiers.NEVER).build()
				.compare(new DefaultComparisonScope(left, right, null));
		assertSame(counterpart, result.getMatch(first).getRight());
		assertSame(rightRoot.getContainmentRef1().get(1),
				result.getMatch(leftRoot.getContainmentRef1().get(1)).getRight());
		assertEquals(0, result.getDifferences().size());
	}
}
