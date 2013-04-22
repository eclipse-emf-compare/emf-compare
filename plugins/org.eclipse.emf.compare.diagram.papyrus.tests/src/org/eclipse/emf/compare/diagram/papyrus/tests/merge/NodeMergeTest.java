/*******************************************************************************
 * Copyright (c) 2013 Obeo.
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
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.or;
import static com.google.common.base.Predicates.not;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.movedInReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromAttribute;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.valueNameMatches;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.diagram.internal.extensions.CoordinatesChange;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.compare.diagram.internal.merge.CompareDiagramMerger;
import org.eclipse.emf.compare.diagram.papyrus.tests.AbstractTest;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.merge.data.EdgeMergeInputData;
import org.eclipse.emf.compare.diagram.papyrus.tests.merge.data.NodeMergeInputData;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.internal.AssociationChange;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.compare.uml2.internal.merge.UMLMerger;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class NodeMergeTest extends AbstractTest {
	private NodeMergeInputData input = new NodeMergeInputData();

	private static final int A1_PKGNODECHANGE1_NB = 14;

	private static final int A1_PKGNODECHANGES_NB = 2 * A1_PKGNODECHANGE1_NB;

	private static final int A1_CLASSNODECHANGE1_NB = 26;

	private static final int A1_CLASSNODECHANGES_NB = 3 * A1_CLASSNODECHANGE1_NB;

	private static final int A1_LISTNODECHANGE1_NB = 4;

	private static final int A1_LISTNODECHANGES_NB = 3 * A1_LISTNODECHANGE1_NB;

	private static final int A1_EDGECHANGE1_NB = 19;

	private static final int A1_EDGECHANGES_NB = 3 * A1_EDGECHANGE1_NB;

	private static final int A1_ELTCHANGE1_NB = 1;

	private static final int A1_ELTCHANGES_NB = 8 * A1_ELTCHANGE1_NB;

	private static final int A1_ASSOCHANGE1_NB = 13;

	private static final int A1_ASSOCHANGES_NB = 3 * A1_ASSOCHANGE1_NB;

	private static final int A1_DIFFS_NB = A1_PKGNODECHANGES_NB + A1_CLASSNODECHANGES_NB
			+ A1_LISTNODECHANGES_NB + A1_EDGECHANGES_NB + A1_ELTCHANGES_NB + A1_ASSOCHANGES_NB;

	private static final int A2_ADD_PKGNODE1_NB = 14;

	private static final int A2_MOVE_NODEA_NB = 4;

	private static final int A2_ADD_PKG1_NB = 1;

	private static final int A2_MOVE_A_NB = 1;

	private static final int A2_DIFFS_NB = A2_ADD_PKGNODE1_NB + A2_MOVE_NODEA_NB + A2_ADD_PKG1_NB
			+ A2_MOVE_A_NB;

	private static final int A4_PROPNODE_CHANGE_NB = 4;

	private static final int A4_DIFFS_NB = A4_PROPNODE_CHANGE_NB * 5 + 5;

	@Test
	// Merge left to right <ADD NodeA> -> merge <ADD NodePkgB>, merge <ADD NodePkgA> (and merge <ADD ClassA>,
	// merge <ADD PkgB>, merge <ADD PkgA>)
	public void testA1a() throws IOException {
		final Resource left = input.getA1NodeChangeLeft();
		final Resource right = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.ADD;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(
				comparison,
				A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - A1_PKGNODECHANGES_NB - A1_ELTCHANGE1_NB * 3,
				new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1),
				new ExpectedStat(assoToClassC, 1),
				new ExpectedStat(classA, 0), // ** -1
				new ExpectedStat(classB, 1),
				new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 0), // ** -1
				new ExpectedStat(pkgB, 0), // ** -1
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 0), // ** -1
				new ExpectedStat(nodeB, 1), new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1),
				new ExpectedStat(nodePkgA, 0), // ** -1
				new ExpectedStat(nodePkgB, 0), // ** -1
				new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge right to left <ADD NodeA> -> merge <ADD NodeProp1>, merge <ADD NodeOp1>, merge <ADD NodeSubA>,
	// merge <ADD Edge1>, merge <ADD Edge2>
	public void testA1b() throws IOException {
		final Resource left = input.getA1NodeChangeLeft();
		final Resource right = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.ADD;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - A1_LISTNODECHANGES_NB
				- A1_EDGECHANGE1_NB * 2, new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(
						classA, 1), new ExpectedStat(classB, 1), new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1),
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 0), // **
																										// -1
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 0), // ** -1
				new ExpectedStat(nodeA, 0), // ** -1
				new ExpectedStat(nodeB, 1), new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 0), // **
																										// -1
				new ExpectedStat(nodePkgA, 1), new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 0), // **
																												// -1
				new ExpectedStat(nodeOp1, 0)); // ** -1
	}

	@Test
	// Merge right to left <DELETE NodeA> -> merge <DELETE NodePkgB>, merge <DELETE NodePkgA> (and merge
	// <DELETE ClassA>, merge <DELETE PkgB>, merge <DELETE PkgA>)
	public void testA1c() throws IOException {
		final Resource right = input.getA1NodeChangeLeft();
		final Resource left = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.DELETE;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(
				comparison,
				A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - A1_PKGNODECHANGES_NB - A1_ELTCHANGE1_NB * 3,
				new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1),
				new ExpectedStat(assoToClassC, 1),
				new ExpectedStat(classA, 0), // ** -1
				new ExpectedStat(classB, 1),
				new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 0), // ** -1
				new ExpectedStat(pkgB, 0), // ** -1
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 0), // ** -1
				new ExpectedStat(nodeB, 1), new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1),
				new ExpectedStat(nodePkgA, 0), // ** -1
				new ExpectedStat(nodePkgB, 0), // ** -1
				new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge left to right <DELETE NodeA> -> merge <DELETE NodeProp1>, merge <DELETE NodeOp1>, merge <DELETE
	// NodeSubA>, merge <DELETE Edge1>, merge <DELETE Edge2>
	public void testA1d() throws IOException {
		final Resource right = input.getA1NodeChangeLeft();
		final Resource left = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.DELETE;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - A1_LISTNODECHANGES_NB
				- A1_EDGECHANGE1_NB * 2, new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(
						classA, 1), new ExpectedStat(classB, 1), new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1),
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 0), // **
																										// -1
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 0), // ** -1
				new ExpectedStat(nodeA, 0), // ** -1
				new ExpectedStat(nodeB, 1), new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 0), // **
																										// -1
				new ExpectedStat(nodePkgA, 1), new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 0), // **
																												// -1
				new ExpectedStat(nodeOp1, 0)); // ** -1
	}

	@Test
	// Merge left to right <ADD NodeB> -> (merge <ADD ClassB>)
	public void testA1e() throws IOException {
		final Resource left = input.getA1NodeChangeLeft();
		final Resource right = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.ADD;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeB);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - A1_ELTCHANGE1_NB, new ExpectedStat(
				assoToClassA, 1), new ExpectedStat(assoToClassB, 1), new ExpectedStat(assoToClassC, 1),
				new ExpectedStat(classA, 1), new ExpectedStat(classB, 0), new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1),
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 0), new ExpectedStat(nodeC, 1),
				new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1), new ExpectedStat(nodePkgB, 1),
				new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge right to left <ADD NodeB> -> merge <ADD Edge1>, merge <ADD Edge2>
	public void testA1f() throws IOException {
		final Resource left = input.getA1NodeChangeLeft();
		final Resource right = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.ADD;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeB);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - A1_EDGECHANGE1_NB * 2,
				new ExpectedStat(assoToClassA, 1), new ExpectedStat(assoToClassB, 1), new ExpectedStat(
						assoToClassC, 1), new ExpectedStat(classA, 1), new ExpectedStat(classB, 1),
				new ExpectedStat(classC, 1), new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1),
				new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1),
				new ExpectedStat(edgeToNodeA, 0), // ** -1
				new ExpectedStat(edgeToNodeB, 0), // ** -1
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1),
				new ExpectedStat(nodeB, 0), // ** -1
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge right to left <DELETE NodeB> -> (merge <DELETE ClassB>)
	public void testA1g() throws IOException {
		final Resource right = input.getA1NodeChangeLeft();
		final Resource left = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.DELETE;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeB);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - A1_ELTCHANGE1_NB, new ExpectedStat(
				assoToClassA, 1), new ExpectedStat(assoToClassB, 1), new ExpectedStat(assoToClassC, 1),
				new ExpectedStat(classA, 1), new ExpectedStat(classB, 0), new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1),
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 0), new ExpectedStat(nodeC, 1),
				new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1), new ExpectedStat(nodePkgB, 1),
				new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge left to right <DELETE NodeB> -> merge <DELETE Edge1>, merge <DELETE Edge2>
	public void testA1h() throws IOException {
		final Resource right = input.getA1NodeChangeLeft();
		final Resource left = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.DELETE;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeB);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_CLASSNODECHANGE1_NB - 2 * A1_EDGECHANGE1_NB,
				new ExpectedStat(assoToClassA, 1), new ExpectedStat(assoToClassB, 1), new ExpectedStat(
						assoToClassC, 1), new ExpectedStat(classA, 1), new ExpectedStat(classB, 1),
				new ExpectedStat(classC, 1), new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1),
				new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1),
				new ExpectedStat(edgeToNodeA, 0), // ** -1
				new ExpectedStat(edgeToNodeB, 0), // ** -1
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1),
				new ExpectedStat(nodeB, 0), // ** -1
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge left to right <ADD NodeSubA> -> merge <ADD NodeClassA>, merge <ADD NodePkgB>, merge <ADD
	// NodePkgA> (and merge <ADD SubA>, merge <ADD ClassA>, merge <ADD PkgB>, merge <ADD PkgA>)
	public void testA1i() throws IOException {
		final Resource left = input.getA1NodeChangeLeft();
		final Resource right = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.ADD;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeSubA);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_LISTNODECHANGE1_NB - A1_CLASSNODECHANGE1_NB
				- A1_PKGNODECHANGES_NB - A1_ELTCHANGE1_NB * 4,
				new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1),
				new ExpectedStat(assoToClassC, 1),
				new ExpectedStat(classA, 0), // ** -1
				new ExpectedStat(classB, 1),
				new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 0), // ** -1
				new ExpectedStat(pkgA, 0), // ** -1
				new ExpectedStat(pkgB, 0), // ** -1
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 0), // ** -1
				new ExpectedStat(nodeB, 1), new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 0), // **
																										// -1
				new ExpectedStat(nodePkgA, 0), // ** -1
				new ExpectedStat(nodePkgB, 0), // ** -1
				new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge right to left <ADD NodeSubA>
	public void testA1j() throws IOException {
		final Resource left = input.getA1NodeChangeLeft();
		final Resource right = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.ADD;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeSubA);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_LISTNODECHANGE1_NB, new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(
						classA, 1), new ExpectedStat(classB, 1), new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1),
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1),
				new ExpectedStat(nodeSubA, 0), // ** -1
				new ExpectedStat(nodePkgA, 1), new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1),
				new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge right to left <DELETE NodeSubA> -> merge <DELETE NodeClassA>, merge <DELETE NodePkgB>, merge
	// <DELETE NodePkgA> (and merge <DELETE SubA>, merge <DELETE ClassA>, merge <DELETE PkgB>, merge <DELETE
	// PkgA>)
	public void testA1k() throws IOException {
		final Resource right = input.getA1NodeChangeLeft();
		final Resource left = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.DELETE;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeSubA);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_LISTNODECHANGE1_NB - A1_CLASSNODECHANGE1_NB
				- A1_PKGNODECHANGES_NB - A1_ELTCHANGE1_NB * 4,
				new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1),
				new ExpectedStat(assoToClassC, 1),
				new ExpectedStat(classA, 0), // ** -1
				new ExpectedStat(classB, 1),
				new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 0), // ** -1
				new ExpectedStat(pkgA, 0), // ** -1
				new ExpectedStat(pkgB, 0), // ** -1
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 0), // ** -1
				new ExpectedStat(nodeB, 1), new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 0), // **
																										// -1
				new ExpectedStat(nodePkgA, 0), // ** -1
				new ExpectedStat(nodePkgB, 0), // ** -1
				new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge left to right <DELETE NodeSubA>
	public void testA1l() throws IOException {
		final Resource right = input.getA1NodeChangeLeft();
		final Resource left = input.getA1NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		DifferenceKind kind = DifferenceKind.DELETE;

		// ** DIFF CHECKING **
		Predicate<Diff> assoToClassA = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classB_classA_1"));
		Predicate<Diff> assoToClassB = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classC_classB_1"));
		Predicate<Diff> assoToClassC = and(instanceOf(AssociationChange.class), ofKind(kind),
				nameIs("classA_classC_1"));
		Predicate<Diff> classA = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassA"));
		Predicate<Diff> classB = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassB"));
		Predicate<Diff> classC = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("ClassC"));
		Predicate<Diff> subA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("SubA"));
		Predicate<Diff> pkgA = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgA"));
		Predicate<Diff> pkgB = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("PkgB"));
		Predicate<Diff> prop1 = and(instanceOf(ReferenceChange.class), ofKind(kind),
				valueNameMatches("prop1"));
		Predicate<Diff> op1 = and(instanceOf(ReferenceChange.class), ofKind(kind), valueNameMatches("op1"));
		Predicate<Diff> edgeToNodeA = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classB_classA_1"));
		Predicate<Diff> edgeToNodeB = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classC_classB_1"));
		Predicate<Diff> edgeToNodeC = and(instanceOf(EdgeChange.class), ofKind(kind),
				elementNameIs("classA_classC_1"));
		Predicate<Diff> nodeA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassA"));
		Predicate<Diff> nodeB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassB"));
		Predicate<Diff> nodeC = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("ClassC"));
		Predicate<Diff> nodeSubA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("SubA"));
		Predicate<Diff> nodePkgA = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgA"));
		Predicate<Diff> nodePkgB = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("PkgB"));
		Predicate<Diff> nodeProp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("prop1"));
		Predicate<Diff> nodeOp1 = and(instanceOf(NodeChange.class), ofKind(kind), elementNameIs("op1"));

		diffsChecking(comparison, A1_DIFFS_NB, new ExpectedStat(assoToClassA, 1), new ExpectedStat(
				assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(classA, 1),
				new ExpectedStat(classB, 1), new ExpectedStat(classC, 1), new ExpectedStat(subA, 1),
				new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1), new ExpectedStat(prop1, 1),
				new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1), new ExpectedStat(edgeToNodeB, 1),
				new ExpectedStat(edgeToNodeC, 1), new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1), new ExpectedStat(nodeSubA, 1), new ExpectedStat(nodePkgA, 1),
				new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1), new ExpectedStat(nodeOp1, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), nodeSubA);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A1_DIFFS_NB - A1_LISTNODECHANGE1_NB, new ExpectedStat(assoToClassA, 1),
				new ExpectedStat(assoToClassB, 1), new ExpectedStat(assoToClassC, 1), new ExpectedStat(
						classA, 1), new ExpectedStat(classB, 1), new ExpectedStat(classC, 1),
				new ExpectedStat(subA, 1), new ExpectedStat(pkgA, 1), new ExpectedStat(pkgB, 1),
				new ExpectedStat(prop1, 1), new ExpectedStat(op1, 1), new ExpectedStat(edgeToNodeA, 1),
				new ExpectedStat(edgeToNodeB, 1), new ExpectedStat(edgeToNodeC, 1),
				new ExpectedStat(nodeA, 1), new ExpectedStat(nodeB, 1),
				new ExpectedStat(nodeC, 1),
				new ExpectedStat(nodeSubA, 0), // ** -1
				new ExpectedStat(nodePkgA, 1), new ExpectedStat(nodePkgB, 1), new ExpectedStat(nodeProp1, 1),
				new ExpectedStat(nodeOp1, 1));
	}

	@Test
	// Merge left to right <MOVE NodeA> -> merge <ADD NodePackage> (and merge <MOVE A>, merge <ADD Package>)
	public void testA2a() throws IOException {
		final Resource left = input.getA2NodeChangeLeft();
		final Resource right = input.getA2NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Predicate<Diff> addNodePackage = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> moveNodeA = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitMoveNodeA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitCoordinatesA = and(instanceOf(AttributeChange.class),
				ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> addPackage = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD),
				valueIs(UMLPackage.Literals.PACKAGE));
		Predicate<Diff> moveA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(UMLPackage.Literals.CLASS));

		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(addNodePackage, 1), new ExpectedStat(
				moveNodeA, 1), new ExpectedStat(unitMoveNodeA, 1), new ExpectedStat(unitCoordinatesA, 2),
				new ExpectedStat(addPackage, 1), new ExpectedStat(moveA, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), moveNodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_MOVE_NODEA_NB - A2_ADD_PKGNODE1_NB - A2_MOVE_A_NB
				- A2_ADD_PKG1_NB, new ExpectedStat(addNodePackage, 0), new ExpectedStat(moveNodeA, 0),
				new ExpectedStat(unitMoveNodeA, 0), new ExpectedStat(unitCoordinatesA, 0), new ExpectedStat(
						addPackage, 0), new ExpectedStat(moveA, 0));

	}

	@Test
	// Merge right to left <MOVE NodeA> -> (and merge <MOVE A>)
	public void testA2b() throws IOException {
		final Resource left = input.getA2NodeChangeLeft();
		final Resource right = input.getA2NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Predicate<Diff> addNodePackage = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> moveNodeA = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitMoveNodeA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitCoordinatesA = and(instanceOf(AttributeChange.class),
				ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> addPackage = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.ADD),
				valueIs(UMLPackage.Literals.PACKAGE));
		Predicate<Diff> moveA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(UMLPackage.Literals.CLASS));

		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(addNodePackage, 1), new ExpectedStat(
				moveNodeA, 1), new ExpectedStat(unitMoveNodeA, 1), new ExpectedStat(unitCoordinatesA, 2),
				new ExpectedStat(addPackage, 1), new ExpectedStat(moveA, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), moveNodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_MOVE_NODEA_NB - A2_MOVE_A_NB, new ExpectedStat(
				addNodePackage, 1), new ExpectedStat(moveNodeA, 0), new ExpectedStat(unitMoveNodeA, 0),
				new ExpectedStat(unitCoordinatesA, 0), new ExpectedStat(addPackage, 1), new ExpectedStat(
						moveA, 0));

	}

	@Test
	// Merge left to right <MOVE NodeA> -> (and merge <MOVE A>)
	public void testA2c() throws IOException {
		final Resource right = input.getA2NodeChangeLeft();
		final Resource left = input.getA2NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Predicate<Diff> addNodePackage = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> moveNodeA = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitMoveNodeA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitCoordinatesA = and(instanceOf(AttributeChange.class),
				ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> addPackage = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(UMLPackage.Literals.PACKAGE));
		Predicate<Diff> moveA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(UMLPackage.Literals.CLASS));

		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(addNodePackage, 1), new ExpectedStat(
				moveNodeA, 1), new ExpectedStat(unitMoveNodeA, 1), new ExpectedStat(unitCoordinatesA, 2),
				new ExpectedStat(addPackage, 1), new ExpectedStat(moveA, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), moveNodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_MOVE_NODEA_NB - A2_MOVE_A_NB, new ExpectedStat(
				addNodePackage, 1), new ExpectedStat(moveNodeA, 0), new ExpectedStat(unitMoveNodeA, 0),
				new ExpectedStat(unitCoordinatesA, 0), new ExpectedStat(addPackage, 1), new ExpectedStat(
						moveA, 0));

	}

	@Test
	// Merge right to left <MOVE NodeA> -> merge <ADD NodePackage> (and merge <MOVE A>, merge <ADD Package>)
	public void testA2d() throws IOException {
		final Resource right = input.getA2NodeChangeLeft();
		final Resource left = input.getA2NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Predicate<Diff> addNodePackage = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> moveNodeA = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitMoveNodeA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(NotationPackage.Literals.SHAPE));
		Predicate<Diff> unitCoordinatesA = and(instanceOf(AttributeChange.class),
				ofKind(DifferenceKind.CHANGE));
		Predicate<Diff> addPackage = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(UMLPackage.Literals.PACKAGE));
		Predicate<Diff> moveA = and(instanceOf(ReferenceChange.class), ofKind(DifferenceKind.MOVE),
				valueIs(UMLPackage.Literals.CLASS));

		diffsChecking(comparison, A2_DIFFS_NB, new ExpectedStat(addNodePackage, 1), new ExpectedStat(
				moveNodeA, 1), new ExpectedStat(unitMoveNodeA, 1), new ExpectedStat(unitCoordinatesA, 2),
				new ExpectedStat(addPackage, 1), new ExpectedStat(moveA, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), moveNodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyRightToLeft(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, A2_DIFFS_NB - A2_MOVE_NODEA_NB - A2_ADD_PKGNODE1_NB - A2_MOVE_A_NB
				- A2_ADD_PKG1_NB, new ExpectedStat(addNodePackage, 0), new ExpectedStat(moveNodeA, 0),
				new ExpectedStat(unitMoveNodeA, 0), new ExpectedStat(unitCoordinatesA, 0), new ExpectedStat(
						addPackage, 0), new ExpectedStat(moveA, 0));

	}

	@Test
	// Merge left to right <Coordinates CHANGE NodeA>
	public void testA3a() throws IOException {
		final Resource left = input.getA3NodeChangeLeft();
		final Resource right = input.getA3NodeChangeRight();

		Comparison comparison = buildComparison(left, right);

		Predicate<Diff> coordinatesChangeNodeA = and(instanceOf(CoordinatesChange.class),
				ofKind(DifferenceKind.CHANGE), valueIs(NotationPackage.Literals.SHAPE));

		diffsChecking(comparison, 3, new ExpectedStat(coordinatesChangeNodeA, 1));

		// ** MERGE **
		Diff node = Iterables.find(comparison.getDifferences(), coordinatesChangeNodeA);
		getMergerRegistry().getHighestRankingMerger(node).copyLeftToRight(node, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right);
		diffsChecking(comparison, 0, new ExpectedStat(coordinatesChangeNodeA, 0));

	}

	@Test
	// Successive merges of NodeLists (mix left to right and right to left)
	public void testA4a() throws IOException {
		final Resource left = input.getA4NodeChangeLeft();
		final Resource right = input.getA4NodeChangeRight();
		final Resource ancestor = input.getA4NodeChangeOrigin();

		Comparison comparison = buildComparison(left, right, ancestor);
		int expectedResult = A4_DIFFS_NB;

		Predicate<Diff> feature4 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature4"));
		Predicate<Diff> feature2 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature2"));
		Predicate<Diff> feature5 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature5"));
		Predicate<Diff> feature6 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.LEFT),
				elementNameIs("feature6"));
		Predicate<Diff> feature7 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.LEFT),
				elementNameIs("feature7"));

		diffsChecking(comparison, A4_DIFFS_NB);

		
		// ** MERGE feature4 <- **
		Diff feature = Iterables.find(comparison.getDifferences(), feature4, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature4, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature2 -> **
		feature = Iterables.find(comparison.getDifferences(), feature2, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyLeftToRight(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature2, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature5 <- **
		feature = Iterables.find(comparison.getDifferences(), feature5, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature5, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature6 -> **
		feature = Iterables.find(comparison.getDifferences(), feature6, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyLeftToRight(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature6, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature7 <- **
		feature = Iterables.find(comparison.getDifferences(), feature7, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature7, null);
		assertTrue(isMergedFor3way(comparison, feature));

	}
	
	@Test
	// Successive merges right to left of NodeLists
	public void testA4b() throws IOException {
		final Resource left = input.getA4NodeChangeLeft();
		final Resource right = input.getA4NodeChangeRight();
		final Resource ancestor = input.getA4NodeChangeOrigin();

		Comparison comparison = buildComparison(left, right, ancestor);
		int expectedResult = A4_DIFFS_NB;

		Predicate<Diff> feature4 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature4"));
		Predicate<Diff> feature2 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature2"));
		Predicate<Diff> feature5 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature5"));
		Predicate<Diff> feature6 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.LEFT),
				elementNameIs("feature6"));
		Predicate<Diff> feature7 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.LEFT),
				elementNameIs("feature7"));

		diffsChecking(comparison, A4_DIFFS_NB);

		
		// ** MERGE feature4 <- **
		Diff feature = Iterables.find(comparison.getDifferences(), feature4, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature4, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature2 -> **
		feature = Iterables.find(comparison.getDifferences(), feature2, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature2, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature5 <- **
		feature = Iterables.find(comparison.getDifferences(), feature5, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature5, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature6 -> **
		feature = Iterables.find(comparison.getDifferences(), feature6, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature6, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature7 <- **
		feature = Iterables.find(comparison.getDifferences(), feature7, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyRightToLeft(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature7, null);
		assertTrue(isMergedFor3way(comparison, feature));

	}
	
	@Test
	// Successive merges left to right of NodeLists
	public void testA4c() throws IOException {
		final Resource left = input.getA4NodeChangeLeft();
		final Resource right = input.getA4NodeChangeRight();
		final Resource ancestor = input.getA4NodeChangeOrigin();

		Comparison comparison = buildComparison(left, right, ancestor);
		int expectedResult = A4_DIFFS_NB;

		Predicate<Diff> feature4 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature4"));
		Predicate<Diff> feature2 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature2"));
		Predicate<Diff> feature5 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.RIGHT),
				elementNameIs("feature5"));
		Predicate<Diff> feature6 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.ADD),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.LEFT),
				elementNameIs("feature6"));
		Predicate<Diff> feature7 = and(instanceOf(NodeChange.class), ofKind(DifferenceKind.DELETE),
				valueIs(NotationPackage.Literals.SHAPE), fromSide(DifferenceSource.LEFT),
				elementNameIs("feature7"));

		diffsChecking(comparison, A4_DIFFS_NB);

		
		// ** MERGE feature4 <- **
		Diff feature = Iterables.find(comparison.getDifferences(), feature4, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyLeftToRight(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature4, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature2 -> **
		feature = Iterables.find(comparison.getDifferences(), feature2, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyLeftToRight(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature2, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature5 <- **
		feature = Iterables.find(comparison.getDifferences(), feature5, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyLeftToRight(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature5, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature6 -> **
		feature = Iterables.find(comparison.getDifferences(), feature6, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyLeftToRight(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature6, null);
		assertTrue(isMergedFor3way(comparison, feature));

		
		// ** MERGE feature7 <- **
		feature = Iterables.find(comparison.getDifferences(), feature7, null);
		assertFalse(isMergedFor3way(comparison, feature));
		getMergerRegistry().getHighestRankingMerger(feature).copyLeftToRight(feature, new BasicMonitor());

		// ** MERGE CHECKING **
		comparison = buildComparison(left, right, ancestor);
		feature = Iterables.find(comparison.getDifferences(), feature7, null);
		assertTrue(isMergedFor3way(comparison, feature));

	}

	@Override
	protected DiagramInputData getInput() {
		return input;
	}

	private static boolean isMergedFor3way(Comparison comparison, final Diff diff) {
		return Iterables.any(comparison.getDifferences(), isMergedFor3way(diff)) || diff == null;
	}
	
	private static Predicate<Diff> isMergedFor3way(final Diff diff) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input.getConflict() != null && input.getConflict().getKind() == ConflictKind.PSEUDO
						&& input.getConflict().getDifferences().contains(diff);
			}
		};
	}

}
