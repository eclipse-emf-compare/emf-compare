/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Tanja Mayerhofer - bug 501864
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups;

import static com.google.common.base.Predicates.and;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.TechnicalitiesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider.CompositeConflict;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider.ConflictsGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.ConflictNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchNode;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

@SuppressWarnings("restriction")
public class ConflictsGroupTest extends AbstractTestTreeNodeItemProviderAdapter {

	private static final CompareFactory FACTORY = CompareFactory.eINSTANCE;

	private ECrossReferenceAdapter crossReferenceAdapter;

	@Override
	public void before() throws IOException {
		super.before();
		crossReferenceAdapter = new TestECrossReferenceAdapter();
	}

	/**
	 * Tests that multiple conflicts containing overlapping diffs are merged in the SMV. Overlapping diffs can
	 * happen due to the replacement of refining diffs with their refined diffs in the SMV. This test is
	 * related to <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testMergingMultipleConflictsWithOverlappingDiffs() {
		// Create diffs
		final Diff diff1 = FACTORY.createDiff();
		final Diff diff1a = FACTORY.createDiff();
		final Diff diff1b = FACTORY.createDiff();
		diff1.getRefinedBy().add(diff1a);
		diff1.getRefinedBy().add(diff1b);

		final Diff diff2 = FACTORY.createDiff();

		final Diff diff3 = FACTORY.createDiff();
		final Diff diff3a = FACTORY.createDiff();
		final Diff diff3b = FACTORY.createDiff();
		diff3.getRefinedBy().add(diff3a);
		diff3.getRefinedBy().add(diff3b);

		final Diff diff4 = FACTORY.createDiff();

		// Create overlapping conflicts
		Conflict conflict1 = FACTORY.createConflict();
		conflict1.getDifferences().add(diff1a);
		conflict1.getDifferences().add(diff2);

		Conflict conflict2 = FACTORY.createConflict();
		conflict2.getDifferences().add(diff3a);
		conflict2.getDifferences().add(diff4);

		Conflict conflict3 = FACTORY.createConflict();
		conflict3.getDifferences().add(diff1b);
		conflict3.getDifferences().add(diff3b);

		// Create comparison
		final Comparison comparison = FACTORY.createComparison();
		comparison.getConflicts().add(conflict1);
		comparison.getConflicts().add(conflict2);
		comparison.getConflicts().add(conflict3);

		// Build conflict nodes
		final ConflictsGroupImpl conflictsGroup = buildConflictGroup(comparison);

		// One conflict node was created
		List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		// The conflict node groups the three created conflicts
		ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		CompositeConflict compositeConflict = (CompositeConflict)conflictNode.basicGetData();
		assertEquals(3, compositeConflict.getConflicts().size());

		// The composite conflict contains all refined diffs
		EList<Diff> differences = compositeConflict.getDifferences();
		assertEquals(4, differences.size());
		assertTrue(differences.contains(diff1));
		assertTrue(differences.contains(diff2));
		assertTrue(differences.contains(diff3));
		assertTrue(differences.contains(diff4));
	}

	/**
	 * Tests that a composite conflict has the conflict kind {@link ConflictKind#REAL} if it contains at least
	 * one conflict of conflict kind {@link ConflictKind#REAL}. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testDerivationOfConflictGroupKindReal() {
		final Conflict realConflict = FACTORY.createConflict();
		realConflict.setKind(ConflictKind.REAL);

		final Conflict pseudoConflict = FACTORY.createConflict();
		pseudoConflict.setKind(ConflictKind.PSEUDO);

		final CompositeConflict realCompositeConflict = new CompositeConflict(realConflict);
		assertEquals(ConflictKind.REAL, realCompositeConflict.getKind());

		final CompositeConflict pseudoCompositeConflict = new CompositeConflict(pseudoConflict);
		assertEquals(ConflictKind.PSEUDO, pseudoCompositeConflict.getKind());

		// The union of one real conflict and one pseudo conflict results in a composite conflict with
		// conflict kind real
		pseudoCompositeConflict.join(realCompositeConflict);
		final CompositeConflict mergedConflictGroup = pseudoCompositeConflict;
		assertEquals(2, mergedConflictGroup.getConflicts().size());
		assertTrue(mergedConflictGroup.getConflicts().contains(realConflict));
		assertTrue(mergedConflictGroup.getConflicts().contains(pseudoConflict));
		assertEquals(ConflictKind.REAL, mergedConflictGroup.getKind());
	}

	/**
	 * Tests that a composite conflict has the conflict kind {@link ConflictKind#PSEUDO} if it contains only
	 * conflicts of conflict kind {@link ConflictKind#PSEUDO}. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testDerivationOfConflictGroupKindPseudo() {
		final Conflict pseudoConflict1 = FACTORY.createConflict();
		pseudoConflict1.setKind(ConflictKind.PSEUDO);

		final Conflict pseudoConflict2 = FACTORY.createConflict();
		pseudoConflict2.setKind(ConflictKind.PSEUDO);

		final CompositeConflict pseudoCompositeConflict1 = new CompositeConflict(pseudoConflict1);
		assertEquals(ConflictKind.PSEUDO, pseudoCompositeConflict1.getKind());

		final CompositeConflict pseudoCompositeConflict2 = new CompositeConflict(pseudoConflict2);
		assertEquals(ConflictKind.PSEUDO, pseudoCompositeConflict2.getKind());

		// The union of pseudo conflicts results in a composite conflict with conflict kind pseudo
		pseudoCompositeConflict2.join(pseudoCompositeConflict1);
		final CompositeConflict mergedConflictGroup = pseudoCompositeConflict2;
		assertEquals(2, mergedConflictGroup.getConflicts().size());
		assertTrue(mergedConflictGroup.getConflicts().contains(pseudoConflict1));
		assertTrue(mergedConflictGroup.getConflicts().contains(pseudoConflict2));
		assertEquals(ConflictKind.PSEUDO, mergedConflictGroup.getKind());
	}

	/**
	 * Tests that a refined diff whose refinding diffs are involved in a real conflict
	 * {@link ConflictKind#REAL} is only shown in the conflicts group but not in the left or right diff group.
	 * This test is related to <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testRefinedDiffsWithRealConflictsAreOnlyInConflictGroup() {
		ConflictsGroupWithRefinedDiffTestScenario scenario = new ConflictsGroupWithRefinedDiffTestScenario();

		// Create real conflict between refining diffs
		final Conflict conflict = scenario.addConflict(scenario.diff1b, scenario.diff2b, ConflictKind.REAL);

		// Build conflict nodes
		final ConflictsGroupImpl conflictsGroup = buildConflictGroup(scenario.comparison);

		// One conflict node was created
		final List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		// The conflict node is associated with the created conflict
		final ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		final CompositeConflict conflictNodeConflict = (CompositeConflict)conflictNode.basicGetData();
		assertEquals(1, conflictNodeConflict.getConflicts().size());
		assertTrue(conflictNodeConflict.getConflicts().contains(conflict));

		// The conflict node contains diff nodes for the refined diffs
		assertEquals(1, conflictNode.getChildren().size());
		final MatchNode matchNode = (MatchNode)conflictNode.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNode, scenario.diff1, scenario.diff2));

		// The technicalities filter does not filter this conflict, because there is a real conflict between
		// the refining diffs
		final List<? extends TreeNode> conflictDiffNodesFiltered = applyTechnicalitiesFilter(
				matchNode.getChildren());
		assertEquals(2, conflictDiffNodesFiltered.size());

		// Build left side
		final BasicDifferenceGroupImpl leftSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.LEFT);

		// There are no diffs in the left side
		assertEquals(0, leftSide.getChildren().size());

		// Build right side
		final BasicDifferenceGroupImpl rightSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.RIGHT);

		// There are no diffs in the right side
		assertEquals(0, rightSide.getChildren().size());
	}

	/**
	 * Tests that a refined diff whose refining diffs are involved in a real conflict
	 * {@link ConflictKind#REAL} and in a pseudo conflict {@link ConflictKind#REAL} is only shown in the
	 * conflicts group but not in the left or right diff group. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testRefinedDiffsWithRealAndPseudoConflictsAreOnlyInConflictGroup() {
		ConflictsGroupWithRefinedDiffTestScenario scenario = new ConflictsGroupWithRefinedDiffTestScenario();

		// Create real conflict between refining diffs
		final Conflict conflictReal = scenario.addConflict(scenario.diff1b, scenario.diff2b,
				ConflictKind.REAL);

		// Create pseudo conflict between refining diffs
		final Conflict conflictPseudo = scenario.addConflict(scenario.diff1a, scenario.diff2a,
				ConflictKind.PSEUDO);

		// Build conflict nodes
		final ConflictsGroupImpl conflictsGroup = buildConflictGroup(scenario.comparison);

		// One conflict node was created
		final List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		// The conflict node is associated with the created conflicts
		final ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		final CompositeConflict conflictNodeConflict = (CompositeConflict)conflictNode.basicGetData();
		assertEquals(2, conflictNodeConflict.getConflicts().size());
		assertTrue(conflictNodeConflict.getConflicts().contains(conflictReal));
		assertTrue(conflictNodeConflict.getConflicts().contains(conflictPseudo));

		// The conflict node contains diff nodes for the refined diffs
		assertEquals(1, conflictNode.getChildren().size());
		final MatchNode matchNode = (MatchNode)conflictNode.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNode, scenario.diff1, scenario.diff2));

		// The technicalities filter does not filter this conflict, because there is a real conflict between
		// the refining diffs
		final List<? extends TreeNode> conflictDiffNodesFiltered = applyTechnicalitiesFilter(
				matchNode.getChildren());
		assertEquals(2, conflictDiffNodesFiltered.size());

		// Build left side
		final BasicDifferenceGroupImpl leftSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.LEFT);

		// There are no diffs in the left side
		assertEquals(0, leftSide.getChildren().size());

		// Build right side
		final BasicDifferenceGroupImpl rightSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.RIGHT);

		// There are no diffs in the right side
		assertEquals(0, rightSide.getChildren().size());
	}

	/**
	 * Tests that a refined diff that is refined by diffs who are all involved in pseudo conflicts
	 * {@link ConflictKind#REAL} is only shown in the conflicts group but not in the left or right diff group.
	 * This test is related to <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testRefinedDiffsWithOnlyPseudoConflictsAreOnlyInConflictGroup() {
		ConflictsGroupWithRefinedDiffTestScenario scenario = new ConflictsGroupWithRefinedDiffTestScenario();

		// Create pseudo conflicts between refining diffs
		final Conflict conflictPseudo1 = scenario.addConflict(scenario.diff1b, scenario.diff2b,
				ConflictKind.PSEUDO);
		final Conflict conflictPseudo2 = scenario.addConflict(scenario.diff1a, scenario.diff2a,
				ConflictKind.PSEUDO);

		// Build conflict nodes
		final ConflictsGroupImpl conflictsGroup = buildConflictGroup(scenario.comparison);

		// One conflict node was created
		final List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		// The conflict node is associated with the created conflicts
		final ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		final CompositeConflict conflictNodeConflict = (CompositeConflict)conflictNode.basicGetData();
		assertEquals(2, conflictNodeConflict.getConflicts().size());
		assertTrue(conflictNodeConflict.getConflicts().contains(conflictPseudo1));
		assertTrue(conflictNodeConflict.getConflicts().contains(conflictPseudo2));

		// The conflict node contains diff nodes for the refined diffs
		assertEquals(1, conflictNode.getChildren().size());
		final MatchNode matchNode = (MatchNode)conflictNode.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNode, scenario.diff1, scenario.diff2));

		// The technicalities filter filters this conflict, since the involved refined diffs are refined by
		// diffs that are all involved in pseudo conflicts
		final List<? extends TreeNode> conflictDiffNodesFiltered = applyTechnicalitiesFilter(
				matchNode.getChildren());
		assertEquals(0, conflictDiffNodesFiltered.size());

		// Build left side
		final BasicDifferenceGroupImpl leftSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.LEFT);

		// There are no diffs in the left side
		assertEquals(0, leftSide.getChildren().size());

		// Build right side
		final BasicDifferenceGroupImpl rightSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.RIGHT);

		// There are no diffs in the right side
		assertEquals(0, rightSide.getChildren().size());
	}

	/**
	 * Tests that a refined diff that is refined by one diff involved in a pseudo conflict
	 * {@link ConflictKind#PSEUDO} and one diff involved in no conflict is shown in the conflicts group and in
	 * the left or right diff group. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testRefinedDiffsWithOnlyOnePseudoConflictAreInDiffGroupOnly() {
		ConflictsGroupWithRefinedDiffTestScenario scenario = new ConflictsGroupWithRefinedDiffTestScenario();

		// Create pseudo conflict
		final Conflict conflictPseudo = scenario.addConflict(scenario.diff1b, scenario.diff2b,
				ConflictKind.PSEUDO);

		// Build conflict nodes
		final ConflictsGroupImpl conflictsGroup = buildConflictGroup(scenario.comparison);

		// One conflict node was created
		List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		// The conflict node is associated with the created conflict
		ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		CompositeConflict conflictNodeConflict = (CompositeConflict)conflictNode.basicGetData();
		assertEquals(1, conflictNodeConflict.getConflicts().size());
		assertTrue(conflictNodeConflict.getConflicts().contains(conflictPseudo));

		// The conflict node contains diff nodes for the refined diffs
		assertEquals(1, conflictNode.getChildren().size());
		MatchNode matchNode = (MatchNode)conflictNode.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNode, scenario.diff1, scenario.diff2));

		// The technicalities filter does not filter this conflict, because it involved a refined diff that is
		// refined by a diff that is not involved in a conflict
		final List<? extends TreeNode> conflictDiffNodesFiltered = applyTechnicalitiesFilter(
				matchNode.getChildren());
		assertEquals(2, conflictDiffNodesFiltered.size());

		// Build left side
		final BasicDifferenceGroupImpl leftSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.LEFT);

		// There is one refined diff in the left side, which is refined by one diff that is not in a conflict
		// and one diff that is in a PSEUDO conflict
		assertEquals(1, leftSide.getChildren().size());
		MatchNode matchNodeLeft = (MatchNode)leftSide.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNodeLeft, scenario.diff1));

		// The technicalities filter does not filter the changes on the left side, because the refined diff is
		// refined by a diff that is not involved in a conflict
		final List<? extends TreeNode> diffNodesLeftFiltered = applyTechnicalitiesFilter(
				matchNodeLeft.getChildren());
		assertEquals(1, diffNodesLeftFiltered.size());
		final List<? extends TreeNode> subDiffNodesLeftFiltered = applyTechnicalitiesFilter(
				diffNodesLeftFiltered.get(0).getChildren());
		assertEquals(2, subDiffNodesLeftFiltered.size());

		// Build right side
		final BasicDifferenceGroupImpl rightSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.RIGHT);

		// There is one refined diff in the right side, which is refined by one diff that is not in a conflict
		// and one diff that is in a PSEUDO conflict
		assertEquals(1, rightSide.getChildren().size());
		MatchNode matchNodeRight = (MatchNode)rightSide.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNodeRight, scenario.diff2));

		// The technicalities filter does not filter the changes on the right side, because the refined diff
		// is refined by a diff that is not involved in a conflict
		final List<? extends TreeNode> diffNodesRightFiltered = applyTechnicalitiesFilter(
				matchNodeRight.getChildren());
		assertEquals(1, diffNodesRightFiltered.size());
		final List<? extends TreeNode> subDiffNodesRightFiltered = applyTechnicalitiesFilter(
				diffNodesRightFiltered.get(0).getChildren());
		assertEquals(2, subDiffNodesRightFiltered.size());
	}

	/**
	 * Tests that a refined diff that is directly involved in a pseudo conflict {@link ConflictKind#PSEUDO}
	 * and that is refined by one diff involved in a real conflict {@link ConflictKind#REAL} is shown in the
	 * conflicts group. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testRefinedDiffsInPseudoConflictAndWithRefiningDiffsInRealConflictAreInConflictGroup() {
		ConflictsGroupWithRefinedDiffTestScenario scenario = new ConflictsGroupWithRefinedDiffTestScenario();

		// Create pseudo conflict
		final Conflict conflictPseudo = scenario.addConflict(scenario.diff1, scenario.diff2,
				ConflictKind.PSEUDO);

		// Create real conflict
		final Conflict conflictReal = scenario.addConflict(scenario.diff1b, scenario.diff2b,
				ConflictKind.REAL);

		// Build conflict nodes
		final ConflictsGroupImpl conflictsGroup = buildConflictGroup(scenario.comparison);

		// One conflict node was created
		List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		// The conflict node is associated with both created conflicts
		ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		CompositeConflict conflictNodeConflict = (CompositeConflict)conflictNode.basicGetData();
		assertEquals(2, conflictNodeConflict.getConflicts().size());
		assertTrue(conflictNodeConflict.getConflicts().contains(conflictPseudo));
		assertTrue(conflictNodeConflict.getConflicts().contains(conflictReal));

		// The conflict node contains diff nodes for the refined diffs
		assertEquals(1, conflictNode.getChildren().size());
		MatchNode matchNode = (MatchNode)conflictNode.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNode, scenario.diff1, scenario.diff2));

		// The technicalities filter does not filter this conflict, because there is a real conflict between
		// the refining diffs
		final List<? extends TreeNode> conflictDiffNodesFiltered = applyTechnicalitiesFilter(
				matchNode.getChildren());
		assertEquals(2, conflictDiffNodesFiltered.size());

		// Build left side
		final BasicDifferenceGroupImpl leftSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.LEFT);

		// There are no diffs in the left side
		assertEquals(0, leftSide.getChildren().size());

		// Build right side
		final BasicDifferenceGroupImpl rightSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.RIGHT);

		// There are no diffs in the right side
		assertEquals(0, rightSide.getChildren().size());
	}

	/**
	 * Tests that a refined diff that is neither directly nor indirect involved in any conflict is shown in
	 * the left or right diff group. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 */
	@Test
	public void testRefinedDiffsWithoutConfictsAreInDiffGroup() {
		ConflictsGroupWithRefinedDiffTestScenario scenario = new ConflictsGroupWithRefinedDiffTestScenario();

		// Build conflict nodes
		final ConflictsGroupImpl conflictsGroup = buildConflictGroup(scenario.comparison);

		// No conflict node was created
		assertEquals(0, conflictsGroup.getChildren().size());

		// Build left side
		final BasicDifferenceGroupImpl leftSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.LEFT);

		// There is one refined diff in the left side, which is neither directly nor indirectly involved in a
		// conflict
		assertEquals(1, leftSide.getChildren().size());
		MatchNode matchNodeLeft = (MatchNode)leftSide.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNodeLeft, scenario.diff1));

		// The technicalities filter does not filter the changes on the left side, because they are not
		// involved in conflicts
		final List<? extends TreeNode> diffNodesLeftFiltered = applyTechnicalitiesFilter(
				matchNodeLeft.getChildren());
		assertEquals(1, diffNodesLeftFiltered.size());
		final List<? extends TreeNode> subDiffNodesLeftFiltered = applyTechnicalitiesFilter(
				diffNodesLeftFiltered.get(0).getChildren());
		assertEquals(2, subDiffNodesLeftFiltered.size());

		// Build right side
		final BasicDifferenceGroupImpl rightSide = buildDifferenceGroup(scenario.comparison,
				DifferenceSource.RIGHT);

		// There is one refined diff in the right side, which is neither directly nor indirectly involved in a
		// conflict
		assertEquals(1, rightSide.getChildren().size());
		MatchNode matchNodeRight = (MatchNode)rightSide.getChildren().get(0);
		assertTrue(matchNodeContainsDiffNodesForDiffs(matchNodeRight, scenario.diff2));

		// The technicalities filter does not filter the changes on the right side, because they are not
		// involved in conflicts
		final List<? extends TreeNode> diffNodesRightFiltered = applyTechnicalitiesFilter(
				matchNodeRight.getChildren());
		assertEquals(1, diffNodesRightFiltered.size());
		final List<? extends TreeNode> subDiffNodesRightFiltered = applyTechnicalitiesFilter(
				diffNodesRightFiltered.get(0).getChildren());
		assertEquals(2, subDiffNodesRightFiltered.size());

	}

	private class TestECrossReferenceAdapter extends ECrossReferenceAdapter {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
		 */
		@Override
		protected boolean isIncluded(EReference eReference) {
			return eReference == TreePackage.Literals.TREE_NODE__DATA;
		}
	}

	private ConflictsGroupImpl buildConflictGroup(Comparison comparison) {
		final ConflictsGroupImpl conflictsGroup = new ConflictsGroupImpl(comparison,
				EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.conflicts.label"), //$NON-NLS-1$
				crossReferenceAdapter);
		conflictsGroup.buildSubTree();
		return conflictsGroup;
	}

	private BasicDifferenceGroupImpl buildDifferenceGroup(Comparison comparison, DifferenceSource source) {
		final BasicDifferenceGroupImpl differenceGroup = new BasicDifferenceGroupImpl(comparison,
				and(fromSide(source), ThreeWayComparisonGroupProvider.DEFAULT_DIFF_GROUP_FILTER_PREDICATE),
				"", crossReferenceAdapter);
		differenceGroup.buildSubTree();
		return differenceGroup;
	}

	private boolean matchNodeContainsDiffNodesForDiffs(MatchNode matchNode, Diff... diffs) {
		final boolean matchNodeContainsRightAmountOfChildren = matchNode.getChildren().size() == diffs.length;
		boolean matchNodeContainsDiffNodesForDiffs = true;
		for (Diff diff : diffs) {
			DiffNode diffNode = getDiffNode(matchNode.getChildren(), diff);
			if (!isDiffNodeForDiff(diffNode, diff)) {
				matchNodeContainsDiffNodesForDiffs = false;
			}
		}
		return matchNodeContainsRightAmountOfChildren && matchNodeContainsDiffNodesForDiffs;
	}

	private DiffNode getDiffNode(EList<TreeNode> nodes, Diff diff) {
		for (TreeNode node : nodes) {
			if (node instanceof DiffNode) {
				DiffNode diffNode = (DiffNode)node;
				if (diffNode.basicGetData() == diff) {
					return diffNode;
				}
			}
		}
		return null;
	}

	private boolean isDiffNodeForDiff(DiffNode diffNode, Diff diff) {
		if (diffNode == null || diff == null) {
			return false;
		}
		final boolean diffNodeAssociatedWithDiff = diffNode.basicGetData() == diff;
		final boolean diffNodeContainsRightAmountOfChildren = diffNode.getChildren().size() == diff
				.getRefinedBy().size();
		boolean diffNodeContainsNodesForRefiningDiffs = true;
		for (Diff refiningDiff : diff.getRefinedBy()) {
			final DiffNode diffNodeForRefiningDiff = getDiffNode(diffNode.getChildren(), refiningDiff);
			if (diffNodeForRefiningDiff == null) {
				diffNodeContainsNodesForRefiningDiffs = false;
			}
		}
		return diffNodeAssociatedWithDiff && diffNodeContainsRightAmountOfChildren
				&& diffNodeContainsNodesForRefiningDiffs;
	}

	private List<? extends TreeNode> applyTechnicalitiesFilter(List<? extends TreeNode> actualTrees) {
		final StructureMergeViewerFilter filter = new StructureMergeViewerFilter(new EventBus());
		final TechnicalitiesFilter technicalitiesFilter = new TechnicalitiesFilter();
		filter.addFilter(technicalitiesFilter);
		Predicate<EObject> viewerFilterPredicate = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				AdapterImpl adapter = new AdapterImpl();
				adapter.setTarget(input);
				return filter.select(null, null, adapter);
			}
		};
		List<? extends TreeNode> filteredTrees = Lists
				.newArrayList(Collections2.filter(actualTrees, viewerFilterPredicate));
		return filteredTrees;
	}
}
