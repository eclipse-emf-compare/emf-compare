/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.filters;

import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter.getComparison;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchResourceNode;
import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicates;

@SuppressWarnings("restriction")
public class CascadingDiffFilterTest {

	private Comparison comp;

	private CascadingDifferencesFilter filter;

	private TestECrossReferenceAdapter crossReferenceAdapter;

	/**
	 * Basic test to make sure the 1st addition is not filtered and its children are.
	 */
	@Test
	public void testWithoutRefiningDiffs() {
		BasicDifferenceGroupImpl group = new BasicDifferenceGroupImpl(comp, Predicates.alwaysTrue(),
				crossReferenceAdapter);
		group.buildSubTree();
		assertEquals(2, group.getChildren().size());
		MatchNode rootNode = (MatchNode)group.getChildren().get(0);
		assertTrue(group.getChildren().get(1) instanceof MatchResourceNode);

		assertEquals(1, rootNode.getChildren().size());
		MatchNode cNode = (MatchNode)rootNode.getChildren().get(0);
		assertEquals(2, cNode.getChildren().size());

		DiffNode addClassNode = (DiffNode)cNode.getChildren().get(0);
		assertFalse(filter.getPredicateWhenSelected().apply(addClassNode));
		assertFalse(filter.getPredicateWhenUnselected().apply(addClassNode));

		MatchNode attNode = (MatchNode)cNode.getChildren().get(1);
		assertEquals(1, attNode.getChildren().size());

		DiffNode addAttNode = (DiffNode)attNode.getChildren().get(0);
		assertTrue(filter.getPredicateWhenSelected().apply(addAttNode));
		assertFalse(filter.getPredicateWhenUnselected().apply(addAttNode));
	}

	/**
	 * A non-conflicting cascading refined diff is not filtered if it has a real conflict diff among its
	 * refining diffs.
	 */
	@Test
	public void testWithRealConflictRefiningDiffs() {
		Match rootMatch = comp.getMatches().get(0);
		Match cMatch = rootMatch.getSubmatches().get(0);
		Match attMatch = cMatch.getSubmatches().get(0);
		Diff attAddition = cMatch.getDifferences().get(0);

		// Create a refinedDiff refined by the att addition
		createRefinedDiff(attMatch, attAddition);

		// Create a real conflict on the refining diff
		createConflict(attAddition, REAL);

		BasicDifferenceGroupImpl group = new BasicDifferenceGroupImpl(comp, Predicates.alwaysTrue(),
				crossReferenceAdapter);
		group.buildSubTree();
		assertEquals(2, group.getChildren().size());
		MatchNode rootNode = (MatchNode)group.getChildren().get(0);
		assertTrue(group.getChildren().get(1) instanceof MatchResourceNode);

		assertEquals(1, rootNode.getChildren().size());
		MatchNode cNode = (MatchNode)rootNode.getChildren().get(0);
		assertEquals(2, cNode.getChildren().size());

		DiffNode addClassNode = (DiffNode)cNode.getChildren().get(0);
		assertFalse(filter.getPredicateWhenSelected().apply(addClassNode));
		assertFalse(filter.getPredicateWhenUnselected().apply(addClassNode));

		MatchNode attNode = (MatchNode)cNode.getChildren().get(1);
		assertEquals(1, attNode.getChildren().size());

		DiffNode refinedDiffNode = (DiffNode)attNode.getChildren().get(0);
		assertFalse(filter.getPredicateWhenSelected().apply(refinedDiffNode));
		assertFalse(filter.getPredicateWhenUnselected().apply(refinedDiffNode));
		assertEquals(1, refinedDiffNode.getChildren().size());

		// It's useless to test on the refining diff since filters are not applied on refining diffs
	}

	/**
	 * A non-conflicting cascading refined diff is filtered if it has some pseudo but no real conflict diff
	 * among its refining diffs.
	 */
	@Test
	public void testWithPseudoConflictRefiningDiffs() {
		Match rootMatch = comp.getMatches().get(0);
		Match cMatch = rootMatch.getSubmatches().get(0);
		Match attMatch = cMatch.getSubmatches().get(0);
		Diff attAddition = cMatch.getDifferences().get(0);

		createRefinedDiff(attMatch, attAddition);

		createConflict(attAddition, PSEUDO);

		BasicDifferenceGroupImpl group = new BasicDifferenceGroupImpl(comp, Predicates.alwaysTrue(),
				crossReferenceAdapter);
		group.buildSubTree();
		assertEquals(2, group.getChildren().size());
		MatchNode rootNode = (MatchNode)group.getChildren().get(0);
		assertTrue(group.getChildren().get(1) instanceof MatchResourceNode);

		assertEquals(1, rootNode.getChildren().size());
		MatchNode cNode = (MatchNode)rootNode.getChildren().get(0);
		assertEquals(2, cNode.getChildren().size());

		DiffNode addClassNode = (DiffNode)cNode.getChildren().get(0);
		assertFalse(filter.getPredicateWhenSelected().apply(addClassNode));
		assertFalse(filter.getPredicateWhenUnselected().apply(addClassNode));

		MatchNode attNode = (MatchNode)cNode.getChildren().get(1);
		assertEquals(1, attNode.getChildren().size());

		DiffNode refinedDiffNode = (DiffNode)attNode.getChildren().get(0);
		assertTrue(filter.getPredicateWhenSelected().apply(refinedDiffNode));
		assertFalse(filter.getPredicateWhenUnselected().apply(refinedDiffNode));
		assertEquals(1, refinedDiffNode.getChildren().size());

		// It's useless to test on the refining diff since filters are not applied on refining diffs
	}

	protected void createConflict(Diff attAddition, ConflictKind kind) {
		Conflict conflict = CompareFactory.eINSTANCE.createConflict();
		conflict.setKind(kind);
		conflict.getDifferences().add(attAddition);
		comp.getConflicts().add(conflict);
	}

	protected void createRefinedDiff(Match attMatch, Diff attAddition) {
		Diff refinedDiff = CompareFactory.eINSTANCE.createDiff();
		refinedDiff.getRefinedBy().add(attAddition);
		attMatch.getDifferences().add(refinedDiff);
	}

	@Before
	public void setUp() throws Exception {
		comp = getComparison(new CascadingScope());
		crossReferenceAdapter = new TestECrossReferenceAdapter();
		filter = new CascadingDifferencesFilter();
	}

	private class TestECrossReferenceAdapter extends ECrossReferenceAdapter {
		@Override
		protected boolean isIncluded(EReference eReference) {
			return eReference == TreePackage.Literals.TREE_NODE__DATA;
		}
	}

	public class CascadingScope extends AbstractInputData implements ResourceScopeProvider {

		public Resource getOrigin() throws IOException {
			return loadFromClassLoader("data/cascading/ancestor.ecore");//$NON-NLS-1$
		}

		public Resource getLeft() throws IOException {
			return loadFromClassLoader("data/cascading/left.ecore");//$NON-NLS-1$
		}

		public Resource getRight() throws IOException {
			return loadFromClassLoader("data/cascading/right.ecore");//$NON-NLS-1$
		}
	}
}
