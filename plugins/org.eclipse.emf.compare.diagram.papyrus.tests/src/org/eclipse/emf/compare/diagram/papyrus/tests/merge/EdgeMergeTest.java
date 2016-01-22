/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.merge;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.merge.data.EdgeMergeInputData;
import org.eclipse.emf.compare.tests.postprocess.data.TestPostProcessor;
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.internal.DirectedRelationshipChange;
import org.eclipse.emf.compare.uml2.internal.postprocessor.MultiplicityElementChangePostProcessor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class EdgeMergeTest extends AbstractTest {
	private EdgeMergeInputData input = new EdgeMergeInputData();
	
	private static final int A1_EDGECHANGE_NB = 19;
	private static final int A1_ASSOCHANGE_NB = 18;
	private static final int A1_DIFFS_NB = A1_EDGECHANGE_NB + A1_ASSOCHANGE_NB;
	
	private static final int A2_EDGECHANGE_NB = 19;
	private static final int A2_NODECHANGE_NB = 26;
	private static final int A2_ASSOCHANGE_NB = 18;
	private static final int A2_CLASSCHANGE_NB = 1;
	private static final int A2_DIFFS_NB = A2_EDGECHANGE_NB + A2_NODECHANGE_NB + A2_ASSOCHANGE_NB + A2_CLASSCHANGE_NB;
	
	private static final int A3_EDGECHANGE_NB = A2_EDGECHANGE_NB;
	private static final int A3_NODECHANGE_NB = 2 * A2_NODECHANGE_NB;
	private static final int A3_ASSOCHANGE_NB = A2_ASSOCHANGE_NB;
	private static final int A3_CLASSCHANGE_NB = 2 * A2_CLASSCHANGE_NB;
	private static final int A3_DIFFS_NB = A3_EDGECHANGE_NB + A3_NODECHANGE_NB + A3_ASSOCHANGE_NB + A3_CLASSCHANGE_NB;
	
	private static final int A4_EDGECHANGE1_NB = A3_EDGECHANGE_NB;
	private static final int A4_EDGECHANGE_NB = 2 * A4_EDGECHANGE1_NB;
	private static final int A4_NODECHANGE1_NB = A2_NODECHANGE_NB;
	private static final int A4_NODECHANGE_NB = 2 * A4_NODECHANGE1_NB;
	private static final int A4_SUBNODECHANGE_NB = 4;
	private static final int A4_PROPNODECHANGE_NB = 4;
	private static final int A4_PKGNODECHANGE_NB = 14;
	
	private static final int A4_ASSOCHANGE1_NB = A2_ASSOCHANGE_NB;
	private static final int A4_ASSOCHANGE_NB = 2 * A4_ASSOCHANGE1_NB;
	private static final int A4_CLASSCHANGE1_NB = A2_CLASSCHANGE_NB;
	private static final int A4_CLASSCHANGE_NB = 3 * A4_CLASSCHANGE1_NB;
	private static final int A4_PKGCHANGE_NB = 1;
	private static final int A4_PROPCHANGE_NB = 1;
	private static final int A4_DIFFS_NB = A4_EDGECHANGE_NB + A4_NODECHANGE_NB + A4_SUBNODECHANGE_NB + A4_PROPNODECHANGE_NB + A4_PKGNODECHANGE_NB + A4_ASSOCHANGE_NB + A4_CLASSCHANGE_NB + A4_PKGCHANGE_NB + A4_PROPCHANGE_NB;
	
	private static final int A5_NOTMANAGED_CONTAINMENT_LINK_NB = 15;
	private static final int A5_NOTMANAGED_BRANCH1_NB = 13;
	private static final int A5_NOTMANAGED_BRANCHES_NB = 3 * A5_NOTMANAGED_BRANCH1_NB;
	private static final int A5_NOTMANAGED_BRANCH_COMMON_NB = 6;
	private static final int A5_NOTMANAGED_MULTI_EDGES_NB = A5_NOTMANAGED_BRANCH_COMMON_NB + A5_NOTMANAGED_BRANCHES_NB;
	private static final int A5_NODECHANGE1_NB = A2_NODECHANGE_NB;
	private static final int A5_NODECHANGES_NB = 3 * A5_NODECHANGE1_NB;
	private static final int A5_EDGECHANGE1_NB = 13; 
	private static final int A5_EDGECHANGES_NB = 7 * A5_EDGECHANGE1_NB; 
	private static final int A5_EDGECHANGE_ASSO1_NB = 21;
	private static final int A5_EDGECHANGE_ASSOS_NB = 2 * A5_EDGECHANGE_ASSO1_NB;
	private static final int A5_EDGECHANGE_GENE1_NB = 11;
	private static final int A5_EDGECHANGE_IREAL_NB = 12;
	private static final int A5_SEMANTIC_DIFFS_NB = 90;
	private static final int A5_DIFFS_NB = A5_NOTMANAGED_CONTAINMENT_LINK_NB + A5_NOTMANAGED_MULTI_EDGES_NB + A5_NODECHANGES_NB + A5_EDGECHANGES_NB + A5_EDGECHANGE_ASSOS_NB + A5_EDGECHANGE_GENE1_NB + A5_EDGECHANGE_IREAL_NB + A5_SEMANTIC_DIFFS_NB;
	
	private static final int A6_NODECHANGE1_NB = 26;
	private static final int A6_NODECHANGES_NB = 2 * A6_NODECHANGE1_NB;
	private static final int A6_DEPENDENCY_EDGE_CHANGE1_NB = 12;
	private static final int A6_DEPENDENCY_EDGE_CHANGES_NB = 11 * A6_DEPENDENCY_EDGE_CHANGE1_NB;
	private static final int A6_ASSO_EDGE_CHANGE1_NB = 21;
	private static final int A6_ASSO_EDGE_CHANGES_NB = 2 * A6_ASSO_EDGE_CHANGE1_NB;
	private static final int A6_IMPORT_EDGE_CHANGE1_NB = 13;
	private static final int A6_IMPORT_EDGE_CHANGES_NB = 2 * A6_IMPORT_EDGE_CHANGE1_NB;
	private static final int A6_FLOW_EDGE_CHANGE1_NB = 13;
	private static final int A6_FLOW_EDGE_CHANGES_NB = 2 * A6_FLOW_EDGE_CHANGE1_NB;
	private static final int A6_GENERAL_EDGE_CHANGE1_NB = 11;
	private static final int A6_GENERAL_EDGE_CHANGES_NB = 2 * A6_GENERAL_EDGE_CHANGE1_NB;
	private static final int A6_CLASSCHANGE1_NB = 1;
	private static final int A6_CLASSCHANGES_NB = 2 * A6_CLASSCHANGE1_NB;
	private static final int A6_DEPENDENCY_CHANGE1_NB = 5;
	private static final int A6_SUBSTITUTION_CHANGE1_NB = 6;
	private static final int A6_IREAL_CHANGE1_NB = 6;
	private static final int A6_DEPENDENCY_CHANGES_NB = 8 * A6_DEPENDENCY_CHANGE1_NB + 2 * A6_SUBSTITUTION_CHANGE1_NB + A6_IREAL_CHANGE1_NB;
	private static final int A6_ASSO_CHANGE1_NB = 18;
	private static final int A6_ASSO_CHANGES_NB = 2 * A6_ASSO_CHANGE1_NB;
	private static final int A6_IMPORT_CHANGE1_NB = 3;
	private static final int A6_IMPORT_CHANGES_NB = 2 * A6_IMPORT_CHANGE1_NB;
	private static final int A6_FLOW_CHANGE1_NB = 4;
	private static final int A6_FLOW_CHANGES_NB = 2 * A6_FLOW_CHANGE1_NB;
	private static final int A6_GENERAL_CHANGE1_NB = 3;
	private static final int A6_GENERAL_CHANGES_NB = 2 * A6_GENERAL_CHANGE1_NB;
	private static final int A6_DIFFS_NB = A6_NODECHANGES_NB + A6_DEPENDENCY_EDGE_CHANGES_NB + A6_ASSO_EDGE_CHANGES_NB + A6_IMPORT_EDGE_CHANGES_NB + A6_FLOW_EDGE_CHANGES_NB + A6_GENERAL_EDGE_CHANGES_NB + A6_CLASSCHANGES_NB + A6_DEPENDENCY_CHANGES_NB + A6_ASSO_CHANGES_NB + A6_IMPORT_CHANGES_NB + A6_FLOW_CHANGES_NB + A6_GENERAL_CHANGES_NB;
	
	private static final int A7_EDGECHANGE1_NB = 2;
	private static final int A7_EDGECHANGES_NB = 3 * A7_EDGECHANGE1_NB;
	private static final int A7_EDGE_TARGET_CHANGE_NB = 1;
	private static final int A7_ASSO_TARGET_CHANGE_NB = 1;
	private static final int A7_DIFFS_NB = A7_EDGECHANGES_NB + A7_EDGE_TARGET_CHANGE_NB + A7_ASSO_TARGET_CHANGE_NB;
	
	@Test
	// Merge left to right <ADD Edge>
	public void testA1a() throws IOException {
		final Resource left = input.getA1EdgeChangeLeft();
		final Resource right = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);	
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge right to left <ADD Edge>
	public void testA1b() throws IOException {
		final Resource left = input.getA1EdgeChangeLeft();
		final Resource right = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_EDGECHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 0)); // only graphical diffs merged
	}
	
	@Test
	// Merge left to right <DELETE Edge>
	public void testA1c() throws IOException {
		final Resource right = input.getA1EdgeChangeLeft();
		final Resource left = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_EDGECHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 0)); // only graphical diffs merged
	}
	
	@Test
	// Merge right to left <DELETE Edge>
	public void testA1d() throws IOException {
		final Resource right = input.getA1EdgeChangeLeft();
		final Resource left = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge left to right <ADD Association>
	public void testA1e() throws IOException {
		final Resource left = input.getA1EdgeChangeLeft();
		final Resource right = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A1_DIFFS_NB - A1_ASSOCHANGE_NB); // only 13 semantic diffs merged
	}
	
	@Test
	// Merge right to left <ADD Association>
	public void testA1f() throws IOException {
		final Resource left = input.getA1EdgeChangeLeft();
		final Resource right = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge left to right <DELETE Association>
	public void testA1g() throws IOException {
		final Resource right = input.getA1EdgeChangeLeft();
		final Resource left = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge right to left <DELETE Association>
	public void testA1h() throws IOException {
		final Resource right = input.getA1EdgeChangeLeft();
		final Resource left = input.getA1EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A1_DIFFS_NB - A1_ASSOCHANGE_NB); // only 13 semantic diffs merged
	}
	
	@Test
	// Merge left to right <ADD Edge> -> merge <ADD Node> (and merge <ADD Association>, merge <ADD Class>)
	public void testA2a() throws IOException {
		final Resource left = input.getA2EdgeChangeLeft();
		final Resource right = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge right to left <ADD Edge>
	public void testA2b() throws IOException {
		final Resource left = input.getA2EdgeChangeLeft();
		final Resource right = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_EDGECHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 0), new ExpectedStat(node, 1));
	}
	
	@Test
	// Merge left to right <DELETE Edge>
	public void testA2c() throws IOException {
		final Resource right = input.getA2EdgeChangeLeft();
		final Resource left = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_EDGECHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 0), new ExpectedStat(node, 1));
	}
	
	@Test
	// Merge right to left <DELETE Edge> -> merge <ADD Node> (and merge <ADD Association>, merge <ADD Class>)
	public void testA2d() throws IOException {
		final Resource right = input.getA2EdgeChangeLeft();
		final Resource left = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge left to right <ADD Association> -> merge <ADD Class>
	public void testA2e() throws IOException {
		final Resource left = input.getA2EdgeChangeLeft();
		final Resource right = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A2_DIFFS_NB - A2_ASSOCHANGE_NB - A2_CLASSCHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));
	}
	
	@Test
	// Merge right to left <ADD Association> -> (merge <ADD Edge>)
	public void testA2f() throws IOException {
		final Resource left = input.getA2EdgeChangeLeft();
		final Resource right = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_ASSOCHANGE_NB - A2_EDGECHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 0), new ExpectedStat(node, 1));
	}
	
	@Test
	// Merge left to right <DELETE Association> -> (merge <DELETE Edge>)
	public void testA2g() throws IOException {
		final Resource right = input.getA2EdgeChangeLeft();
		final Resource left = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_ASSOCHANGE_NB - A2_EDGECHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 0), new ExpectedStat(node, 1));
	}
	
	@Test
	// Merge right to left <DELETE Association> -> merge <DELETE Class>
	public void testA2h() throws IOException {
		final Resource right = input.getA2EdgeChangeLeft();
		final Resource left = input.getA2EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A2_DIFFS_NB - A2_ASSOCHANGE_NB - A2_CLASSCHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 1), new ExpectedStat(node, 1));
	}
	
	@Test
	// Merge left to right <ADD Edge> -> merge <ADD Nodes> (and merge <ADD Association>, merge <ADD Classes>)
	public void testA3a() throws IOException {
		final Resource left = input.getA3EdgeChangeLeft();
		final Resource right = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge right to left <ADD Edge>
	public void testA3b() throws IOException {
		final Resource left = input.getA3EdgeChangeLeft();
		final Resource right = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A3_DIFFS_NB - A3_EDGECHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 0), new ExpectedStat(node, 2));
	}
	
	@Test
	// Merge left to right <DELETE Edge>
	public void testA3c() throws IOException {
		final Resource right = input.getA3EdgeChangeLeft();
		final Resource left = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A3_DIFFS_NB - A3_EDGECHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 0), new ExpectedStat(node, 2));
	}
	
	@Test
	// Merge right to left <DELETE Edge> -> merge <ADD Nodes> (and merge <ADD Association>, merge <ADD Classes>)
	public void testA3d() throws IOException {
		final Resource right = input.getA3EdgeChangeLeft();
		final Resource left = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, 0); // all diffs merged
	}
	
	@Test
	// Merge left to right <ADD Association> -> merge <ADD Classes>
	public void testA3e() throws IOException {
		final Resource left = input.getA3EdgeChangeLeft();
		final Resource right = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A3_DIFFS_NB - A3_ASSOCHANGE_NB - A3_CLASSCHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));
	}
	
	@Test
	// Merge right to left <ADD Association> -> (merge <ADD Edge>)
	public void testA3f() throws IOException {
		final Resource left = input.getA3EdgeChangeLeft();
		final Resource right = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A3_DIFFS_NB - A3_ASSOCHANGE_NB - A3_EDGECHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 0), new ExpectedStat(node, 2));
	}
	
	@Test
	// Merge left to right <DELETE Association> -> (merge <DELETE Edge>)
	public void testA3g() throws IOException {
		final Resource right = input.getA3EdgeChangeLeft();
		final Resource left = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A3_DIFFS_NB - A3_ASSOCHANGE_NB - A3_EDGECHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 0), new ExpectedStat(node, 2));
	}
	
	@Test
	// Merge right to left <DELETE Association> -> merge <DELETE Classes>
	public void testA3h() throws IOException {
		final Resource right = input.getA3EdgeChangeLeft();
		final Resource left = input.getA3EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A3_DIFFS_NB, new ExpectedStat(association, 1), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), association);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A3_DIFFS_NB - A3_ASSOCHANGE_NB - A3_CLASSCHANGE_NB, new ExpectedStat(association, 0), new ExpectedStat(edge, 1), new ExpectedStat(node, 2));
	}
	
	@Test
	// Merge left to right <ADD Edge> -> merge <ADD Node>, merge <ADD PkgNode> (and merge <ADD Association>, merge <ADD Class>, merge <ADD Package>)
	public void testA4a() throws IOException {
		final Resource left = input.getA4EdgeChangeLeft();
		final Resource right = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(association, 2), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge1);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A4_DIFFS_NB - A4_EDGECHANGE1_NB - A4_NODECHANGE1_NB - A4_PKGNODECHANGE_NB - A4_ASSOCHANGE1_NB - A4_CLASSCHANGE1_NB - A4_PKGCHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge1, 0), new ExpectedStat(edge2, 1), new ExpectedStat(node, 3));
	}
	
	@Test
	// Merge right to left <ADD Edge>
	public void testA4b() throws IOException {
		final Resource left = input.getA4EdgeChangeLeft();
		final Resource right = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(association, 2), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge1);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A4_DIFFS_NB - A4_EDGECHANGE1_NB, new ExpectedStat(association, 2), new ExpectedStat(edge1, 0), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));
	}
	
	@Test
	// Merge left to right <DELETE Edge>
	public void testA4c() throws IOException {
		final Resource right = input.getA4EdgeChangeLeft();
		final Resource left = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(association, 2), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge1);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyLeftToRight(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A4_DIFFS_NB - A4_EDGECHANGE1_NB, new ExpectedStat(association, 2), new ExpectedStat(edge1, 0), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));
	}
	
	@Test
	// Merge right to left <DELETE Edge> -> merge <ADD Node>, merge <ADD PkgNode> (and merge <ADD Association>, merge <ADD Class>, merge <ADD Package>)
	public void testA4d() throws IOException {
		final Resource right = input.getA4EdgeChangeLeft();
		final Resource left = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> association = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(association, 2), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff edgeChange = Iterables.find(comparison.getDifferences(), edge1);
		getMergerRegistry().getHighestRankingMerger(edgeChange).copyRightToLeft(edgeChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A4_DIFFS_NB - A4_EDGECHANGE1_NB - A4_NODECHANGE1_NB - A4_PKGNODECHANGE_NB - A4_ASSOCHANGE1_NB - A4_CLASSCHANGE1_NB - A4_PKGCHANGE_NB, new ExpectedStat(association, 1), new ExpectedStat(edge1, 0), new ExpectedStat(edge2, 1), new ExpectedStat(node, 3));
	}
	
	@Test
	// Merge left to right <ADD Association> -> merge <ADD Class>, merge <ADD Package>
	public void testA4e() throws IOException {
		final Resource left = input.getA4EdgeChangeLeft();
		final Resource right = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> asso1 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("a_b_1"));
		Predicate<Diff> asso2 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("c_b_1"));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), asso1);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A4_DIFFS_NB - A4_ASSOCHANGE1_NB - A4_CLASSCHANGE1_NB - A4_PKGCHANGE_NB, new ExpectedStat(asso1, 0), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));
	}
	
	@Test
	// Merge right to left <ADD Association> -> (merge <ADD Edge>)
	public void testA4f() throws IOException {
		final Resource left = input.getA4EdgeChangeLeft();
		final Resource right = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> asso1 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("a_b_1"));
		Predicate<Diff> asso2 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("c_b_1"));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), asso1);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A4_DIFFS_NB - A4_ASSOCHANGE1_NB - A4_EDGECHANGE1_NB, new ExpectedStat(asso1, 0), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 0), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));
	}
	
	@Test
	// Merge left to right <DELETE Association> -> (merge <DELETE Edge>)
	public void testA4g() throws IOException {
		final Resource right = input.getA4EdgeChangeLeft();
		final Resource left = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> asso1 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE), nameIs("a_b_1"));
		Predicate<Diff> asso2 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE), nameIs("c_b_1"));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), asso1);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyLeftToRight(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A4_DIFFS_NB - A4_ASSOCHANGE1_NB - A4_EDGECHANGE1_NB, new ExpectedStat(asso1, 0), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 0), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));
	}
	
	@Test
	// Merge right to left <DELETE Association> -> merge <DELETE Class>, merge <DELETE Package>
	public void testA4h() throws IOException {
		final Resource right = input.getA4EdgeChangeLeft();
		final Resource left = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> asso1 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE), nameIs("a_b_1"));
		Predicate<Diff> asso2 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.DELETE), nameIs("c_b_1"));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.DELETE), elementNameIs("c_b_1"));
		Predicate<Diff> node = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));

		// ** MERGE **
		Diff associationChange = Iterables.find(comparison.getDifferences(), asso1);
		getMergerRegistry().getHighestRankingMerger(associationChange).copyRightToLeft(associationChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A4_DIFFS_NB - A4_ASSOCHANGE1_NB - A4_CLASSCHANGE1_NB - A4_PKGCHANGE_NB, new ExpectedStat(asso1, 0), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(node, 5));
	}
	
	@Test
	// Merge left to right <ADD NodeB> -> merge <ADD PkgNodeB> (and merge <ADD ClassB>, merge <ADD PackageB>)
	public void testA4i() throws IOException {
		final Resource left = input.getA4EdgeChangeLeft();
		final Resource right = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> asso1 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("a_b_1"));
		Predicate<Diff> asso2 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("c_b_1"));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("c_b_1"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("B"));
		Predicate<Diff> nodeSubB = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("SubB"));
		Predicate<Diff> nodePropB = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("propB"));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(nodeB, 1), new ExpectedStat(nodeSubB, 1), new ExpectedStat(nodePropB, 1));

		// ** MERGE **
		Diff nodeBChange = Iterables.find(comparison.getDifferences(), nodeB);
		getMergerRegistry().getHighestRankingMerger(nodeBChange).copyLeftToRight(nodeBChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A4_DIFFS_NB - A4_NODECHANGE1_NB - A4_PKGNODECHANGE_NB - A4_CLASSCHANGE1_NB - A4_PKGCHANGE_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(nodeB, 0), new ExpectedStat(nodeSubB, 1), new ExpectedStat(nodePropB, 1));
	}
	
	@Test
	// Merge right to left <ADD NodeB> -> merge <ADD edge1>, merge <ADD edge2>, merge <ADD nodePropB>, merge <ADD nodeSubB>
	public void testA4j() throws IOException {
		final Resource left = input.getA4EdgeChangeLeft();
		final Resource right = input.getA4EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> asso1 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("a_b_1"));
		Predicate<Diff> asso2 = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD), nameIs("c_b_1"));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("c_b_1"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("B"));
		Predicate<Diff> nodeSubB = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("SubB"));
		Predicate<Diff> nodePropB = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD), elementNameIs("propB"));
		diffsChecking(comparison, A4_DIFFS_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 1), new ExpectedStat(edge2, 1), new ExpectedStat(nodeB, 1), new ExpectedStat(nodeSubB, 1), new ExpectedStat(nodePropB, 1));

		// ** MERGE **
		Diff nodeBChange = Iterables.find(comparison.getDifferences(), nodeB);
		getMergerRegistry().getHighestRankingMerger(nodeBChange).copyRightToLeft(nodeBChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A4_DIFFS_NB - A4_NODECHANGE1_NB - 2 * A4_EDGECHANGE1_NB - A4_PROPNODECHANGE_NB - A4_SUBNODECHANGE_NB, new ExpectedStat(asso1, 1), new ExpectedStat(asso2, 1), new ExpectedStat(edge1, 0), new ExpectedStat(edge2, 0), new ExpectedStat(nodeB, 0), new ExpectedStat(nodeSubB, 0), new ExpectedStat(nodePropB, 0));
	}
	
	@Test
	// Merge Left to Right <ADD Abstraction Edge>
	public void testA6a() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages. Details below:
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edgeAbstractionChange = Iterables.find(comparison.getDifferences(), edgeAbstraction);
		getMergerRegistry().getHighestRankingMerger(edgeAbstractionChange).copyLeftToRight(edgeAbstractionChange, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A6_DIFFS_NB - A6_DEPENDENCY_EDGE_CHANGE1_NB - A6_DEPENDENCY_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2),
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 7), // ** -1
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 1), // ** -1
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD Association Edge>
	public void testA6b() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows. Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeAssociation);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A6_DIFFS_NB - A6_ASSO_EDGE_CHANGE1_NB - A6_ASSO_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 1), // ** -1
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8),
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 1), // ** -1
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD Dependency Edge>
	public void testA6c() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeDependency);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A6_DIFFS_NB - A6_DEPENDENCY_EDGE_CHANGE1_NB - A6_DEPENDENCY_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 7), // ** -1
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 1), // ** -1
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD Import Edge>
	public void testA6d() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows. Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeImport);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		//FIXME: ElementImport.importedElement is not merged => should create a UMLDiff for this DirectedRelationship (A6_IMPORT_CHANGE1_NB += 1)
		// See Bug 406405
		diffsChecking(comparison, A6_DIFFS_NB - A6_IMPORT_EDGE_CHANGE1_NB - A6_IMPORT_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8),
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 1), // ** -1
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD Generalization Edge>
	public void testA6e() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeGeneralization);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		//FIXME: Generalization.general is not merged => should create a UMLDiff for this DirectedRelationship (A6_GENERAL_CHANGE1_NB += 1)
		// See Bug 406405
		diffsChecking(comparison, A6_DIFFS_NB - A6_GENERAL_EDGE_CHANGE1_NB - A6_GENERAL_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8),
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2), 
				new ExpectedStat(edgeGeneralization, 1), // ** -1
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD Flow Edge>
	public void testA6f() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeFlow);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		//FIXME: InformationFlow.informationSource and InformationFlow.informationTarget are not merged => should create a UMLDiff for this DirectedRelationship (A6_FLOW_CHANGE1_NB += 2)
		// See Bug 406405
		diffsChecking(comparison, A6_DIFFS_NB - A6_FLOW_EDGE_CHANGE1_NB - A6_FLOW_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8),
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2), 
				new ExpectedStat(edgeGeneralization, 2), 
				new ExpectedStat(edgeFlow, 1), // ** -1
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD InterfaceRealization Edge>
	public void testA6g() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeInterfaceRealization);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A6_DIFFS_NB - A6_DEPENDENCY_EDGE_CHANGE1_NB - A6_IREAL_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 0), // ** -1
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8),
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2), 
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 0),// ** -1
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD Realization Edge>
	public void testA6h() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeRealization);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A6_DIFFS_NB - A6_DEPENDENCY_EDGE_CHANGE1_NB - A6_DEPENDENCY_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1),
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 7), // ** -1
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2), 
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 1), // ** -1
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));
	}

	@Test
	// Merge Left to Right <ADD Substitution Edge>
	public void testA6i() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeSubstitution);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);	
		diffsChecking(comparison, A6_DIFFS_NB - A6_DEPENDENCY_EDGE_CHANGE1_NB - A6_SUBSTITUTION_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 1), // ** -1
				new ExpectedStat(interfaceRealizations, 1),
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), 
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2), 
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2), 
				new ExpectedStat(edgeSubstitution, 1), // ** -1
				new ExpectedStat(edgeUsage, 2));
	}
	
	@Test
	// Merge Left to Right <ADD Usage Edge>
	public void testA6j() throws IOException {
		final Resource left = input.getA6EdgeChangeLeft();
		final Resource right = input.getA6EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> substitutions = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.SUBSTITUTION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> interfaceRealizations = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.INTERFACE_REALIZATION), ofKind(DifferenceKind.ADD));
		Predicate<Diff> associations = and(instanceOf(AssociationChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> dependencies = and(instanceOf(DirectedRelationshipChange.class), discriminantInstanceOf(UMLPackage.Literals.DEPENDENCY), ofKind(DifferenceKind.ADD));
		Predicate<Diff> nodes = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD));
		Predicate<Diff> edgeAbstraction = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ABSTRACTION));
		Predicate<Diff> edgeAssociation = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ASSOCIATION));
		Predicate<Diff> edgeDependency = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.DEPENDENCY));
		Predicate<Diff> edgeImport = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.ELEMENT_IMPORT));
		Predicate<Diff> edgeGeneralization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.GENERALIZATION));
		Predicate<Diff> edgeFlow = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INFORMATION_FLOW));
		Predicate<Diff> edgeInterfaceRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.INTERFACE_REALIZATION));
		Predicate<Diff> edgeRealization = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.REALIZATION));
		Predicate<Diff> edgeSubstitution = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.SUBSTITUTION));
		Predicate<Diff> edgeUsage = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.ADD), elementClassIs(UMLPackage.Literals.USAGE));
		
		diffsChecking(comparison, A6_DIFFS_NB, 
				new ExpectedStat(substitutions, 2), 
				new ExpectedStat(interfaceRealizations, 1), 
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 8), // 2 abstractions + 2 dependencies + 2 realizations + 2 usages.
				new ExpectedStat(nodes, 2),
				new ExpectedStat(edges, 19), // 13 + 2 imports + 2 generalizations + 2 information flows (no UMLDiff for them). Details below:
				new ExpectedStat(edgeAbstraction, 2),
				new ExpectedStat(edgeAssociation, 2),
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2),
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2),
				new ExpectedStat(edgeSubstitution, 2),
				new ExpectedStat(edgeUsage, 2));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeUsage);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);	
		diffsChecking(comparison, A6_DIFFS_NB - A6_DEPENDENCY_EDGE_CHANGE1_NB - A6_DEPENDENCY_CHANGE1_NB - A6_NODECHANGE1_NB - A6_CLASSCHANGE1_NB, 
				new ExpectedStat(substitutions, 2),
				new ExpectedStat(interfaceRealizations, 1),
				new ExpectedStat(associations, 2), 
				new ExpectedStat(and(dependencies, not(substitutions), not(interfaceRealizations)), 7), // ** -1
				new ExpectedStat(nodes, 1), // ** -1
				new ExpectedStat(edgeAbstraction, 2), 
				new ExpectedStat(edgeAssociation, 2), 
				new ExpectedStat(edgeDependency, 2),
				new ExpectedStat(edgeImport, 2), 
				new ExpectedStat(edgeGeneralization, 2),
				new ExpectedStat(edgeFlow, 2),
				new ExpectedStat(edgeInterfaceRealization, 1),
				new ExpectedStat(edgeRealization, 2), 
				new ExpectedStat(edgeSubstitution, 2), 
				new ExpectedStat(edgeUsage, 1)); // ** -1
	}
	
	@Test
	// Merge Left to Right <CHANGE Edge1>
	public void testA7a() throws IOException {
		final Resource left = input.getA7EdgeChangeLeft();
		final Resource right = input.getA7EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_2"));
		Predicate<Diff> edge3 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_3"));
		Predicate<Diff> edgeTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> assoTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(UMLPackage.Literals.CLASS));
		
		diffsChecking(comparison, A7_DIFFS_NB, 
				new ExpectedStat(edges, 3), 
				new ExpectedStat(edgeTarget, 1), 
				new ExpectedStat(assoTarget, 1));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edge1);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A7_DIFFS_NB - A7_EDGECHANGE1_NB, 
				new ExpectedStat(edges, 2), // ** -1
				new ExpectedStat(edgeTarget, 1), 
				new ExpectedStat(assoTarget, 1));
	}
	
	@Test
	// Merge Left to Right <CHANGE Edge2>
	public void testA7b() throws IOException {
		final Resource left = input.getA7EdgeChangeLeft();
		final Resource right = input.getA7EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_2"));
		Predicate<Diff> edge3 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_3"));
		Predicate<Diff> edgeTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> assoTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(UMLPackage.Literals.CLASS));
		
		diffsChecking(comparison, A7_DIFFS_NB, 
				new ExpectedStat(edges, 3), 
				new ExpectedStat(edgeTarget, 1), 
				new ExpectedStat(assoTarget, 1));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edge2);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A7_DIFFS_NB - A7_EDGECHANGE1_NB, 
				new ExpectedStat(edges, 2), // ** -1
				new ExpectedStat(edgeTarget, 1), 
				new ExpectedStat(assoTarget, 1));
	}
	
	@Test
	// Merge Left to Right <CHANGE Edge3>
	public void testA7c() throws IOException {
		final Resource left = input.getA7EdgeChangeLeft();
		final Resource right = input.getA7EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_2"));
		Predicate<Diff> edge3 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_3"));
		Predicate<Diff> edgeTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> assoTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(UMLPackage.Literals.CLASS));
		
		diffsChecking(comparison, A7_DIFFS_NB, 
				new ExpectedStat(edges, 3), 
				new ExpectedStat(edgeTarget, 1), 
				new ExpectedStat(assoTarget, 1));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edge3);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A7_DIFFS_NB - A7_EDGECHANGE1_NB, 
				new ExpectedStat(edges, 2), // ** -1
				new ExpectedStat(edgeTarget, 1), 
				new ExpectedStat(assoTarget, 1));
	}
	
	@Test
	// Merge Left to Right <CHANGE EdgeTarget>
	public void testA7d() throws IOException {
		final Resource left = input.getA7EdgeChangeLeft();
		final Resource right = input.getA7EdgeChangeRight();

		Comparison comparison = buildComparison(left, right);

		// ** DIFF CHECKING **
		Predicate<Diff> edges = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> edge1 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_1"));
		Predicate<Diff> edge2 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_2"));
		Predicate<Diff> edge3 = and(instanceOf(EdgeChange.class), ofKind(DifferenceKind.CHANGE), elementNameIs("a_b_3"));
		Predicate<Diff> edgeTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> assoTarget = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.CHANGE), valueIs(UMLPackage.Literals.CLASS));
		
		diffsChecking(comparison, A7_DIFFS_NB, 
				new ExpectedStat(edges, 3), 
				new ExpectedStat(edgeTarget, 1), 
				new ExpectedStat(assoTarget, 1));

		// ** MERGE **
		Diff edge = Iterables.find(comparison.getDifferences(), edgeTarget);
		getMergerRegistry().getHighestRankingMerger(edge).copyLeftToRight(edge, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);		
		diffsChecking(comparison, A7_DIFFS_NB - A7_EDGE_TARGET_CHANGE_NB, 
				new ExpectedStat(edges, 3), 
				new ExpectedStat(edgeTarget, 0), // ** -1
				new ExpectedStat(assoTarget, 1));
	}
	
	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	@Override
	protected void registerPostProcessors() {
		super.registerPostProcessors();
		getPostProcessorRegistry()
				.put(MultiplicityElementChangePostProcessor.class.getName(),
						new TestPostProcessor.TestPostProcessorDescriptor(
								Pattern.compile("http://www.eclipse.org/uml2/\\d\\.0\\.0/UML"),
								null,
								new MultiplicityElementChangePostProcessor(),
								25));
	}

}
