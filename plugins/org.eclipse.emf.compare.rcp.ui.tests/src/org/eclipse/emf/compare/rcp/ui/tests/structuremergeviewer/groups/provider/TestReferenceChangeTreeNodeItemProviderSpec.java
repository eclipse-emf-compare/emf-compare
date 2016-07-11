/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider;

import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DefaultGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeNodeItemProviderSpec;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchNode;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.data.ecore.a1.EcoreA1InputData;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class TestReferenceChangeTreeNodeItemProviderSpec extends AbstractTestTreeNodeItemProviderAdapter {

	private static TreeNodeItemProviderSpec itemProvider;

	@Override
	@Before
	public void before() throws IOException {
		super.before();
		itemProvider = (TreeNodeItemProviderSpec)treeItemProviderAdapterFactory.createTreeNodeAdapter();
	}

	static TreeNode getEcoreA1_EPackageMatch() throws IOException {
		Comparison comparison = getComparison(new EcoreA1InputData());

		TreeNode treeNode = TreeFactory.eINSTANCE.createTreeNode();
		treeNode.setData(comparison);
		treeNode.eAdapters().add(new DefaultGroupProvider());

		Collection<?> children = itemProvider.getChildren(treeNode);

		Iterable<?> matches = filter(children, matchTreeNode);
		return (TreeNode)matches.iterator().next();
	}

	@Test
	public void testGetChildren_AudioVisualItem() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode audioVisualItemNode = (MatchNode)ePackageMatch.getChildren().get(3);
		checkMatch(audioVisualItemNode, RIGHT, "AudioVisualItem");

		List<TreeNode> audioVisualChildren = audioVisualItemNode.getChildren();
		assertEquals(3, audioVisualChildren.size());
		TreeNode node = audioVisualChildren.get(0);
		checkRefChange(node, RIGHT, ADD, "eSuperTypes");
		checkNoChild(node);

		node = audioVisualChildren.get(1);
		assertTrue(node instanceof MatchNode);
		assertEquals(2, node.getChildren().size());
		TreeNode childNode = node.getChildren().get(0);
		checkRefChange(childNode, RIGHT, DELETE, "eStructuralFeatures");
		checkNoChild(childNode);
		childNode = node.getChildren().get(1);
		checkRefChange(childNode, RIGHT, CHANGE, "eType");
		checkNoChild(childNode);

		node = audioVisualChildren.get(2);
		assertTrue(node instanceof MatchNode);
		assertEquals(2, node.getChildren().size());
		childNode = node.getChildren().get(0);
		checkAttChange(childNode, LEFT, CHANGE, "name", "length");
		checkNoChild(childNode);
		childNode = node.getChildren().get(1);
		checkAttChange(childNode, RIGHT, CHANGE, "name", "minutes");
		checkNoChild(childNode);
	}

	@Test
	public void testGetChildren_Book() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode bookNode = (MatchNode)ePackageMatch.getChildren().get(0);
		checkMatch(bookNode, RIGHT, "Book");

		List<TreeNode> bookChildren = bookNode.getChildren();
		assertEquals(3, bookChildren.size());
		TreeNode diffNode = bookChildren.get(0);
		checkRefChange(diffNode, RIGHT, ADD, "eSuperTypes");
		checkNoChild(diffNode);

		MatchNode titleMatchNode = (MatchNode)bookChildren.get(1);
		assertEquals(2, titleMatchNode.getChildren().size());
		diffNode = titleMatchNode.getChildren().get(0);
		checkRefChange(diffNode, RIGHT, DELETE, "eStructuralFeatures");
		checkNoChild(diffNode);

		diffNode = titleMatchNode.getChildren().get(1);
		checkRefChange(diffNode, RIGHT, CHANGE, "eType");
		checkNoChild(diffNode);

		MatchNode subtitleMatchNode = (MatchNode)bookChildren.get(2);
		assertEquals(2, subtitleMatchNode.getChildren().size());
		diffNode = subtitleMatchNode.getChildren().get(0);
		checkRefChange(diffNode, RIGHT, ADD, "eStructuralFeatures");
		checkNoChild(diffNode);

		diffNode = subtitleMatchNode.getChildren().get(1);
		checkRefChange(diffNode, RIGHT, CHANGE, "eType");
		checkNoChild(diffNode);
	}

	@Test
	public void testGetChildren_Borrowable() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode borrowableNode = (MatchNode)ePackageMatch.getChildren().get(2);
		checkMatch(borrowableNode, LEFT, "Borrowable");
		checkMatch(borrowableNode, RIGHT, "Lendable");

		assertEquals(1, borrowableNode.getChildren().size());
		TreeNode node = borrowableNode.getChildren().get(0);
		checkAttChange(node, LEFT, CHANGE, "name", "Borrowable");
	}

	@Test
	public void testGetChildren_BookCategory() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode bookCategoryNode = (MatchNode)ePackageMatch.getChildren().get(1);
		checkMatch(bookCategoryNode, RIGHT, "BookCategory");
		List<TreeNode> bookCategoryChildren = bookCategoryNode.getChildren();
		assertEquals(4, bookCategoryChildren.size());

		MatchNode matchNode = (MatchNode)bookCategoryChildren.get(0);
		assertEquals(1, matchNode.getChildren().size());
		Match match = matchNode.getMatch();
		assertEquals("Encyclopedia", safeEGet(match.getLeft(), "name"));
		DiffNode diffNode = (DiffNode)matchNode.getChildren().get(0);
		assertTrue(diffNode.getChildren().isEmpty());
		ReferenceChange diff = (ReferenceChange)diffNode.getDiff();
		assertEquals(DifferenceKind.ADD, diff.getKind());
		assertEquals(DifferenceSource.LEFT, diff.getSource());

		matchNode = (MatchNode)bookCategoryChildren.get(1);
		assertEquals(1, matchNode.getChildren().size());
		match = matchNode.getMatch();
		assertEquals("Dictionary", safeEGet(match.getLeft(), "name"));
		diffNode = (DiffNode)matchNode.getChildren().get(0);
		assertTrue(diffNode.getChildren().isEmpty());
		diff = (ReferenceChange)diffNode.getDiff();
		assertEquals(DifferenceKind.ADD, diff.getKind());
		assertEquals(DifferenceSource.LEFT, diff.getSource());

		matchNode = (MatchNode)bookCategoryChildren.get(2);
		assertEquals(1, matchNode.getChildren().size());
		match = matchNode.getMatch();
		assertEquals("Manga", safeEGet(match.getRight(), "name"));
		diffNode = (DiffNode)matchNode.getChildren().get(0);
		assertTrue(diffNode.getChildren().isEmpty());
		diff = (ReferenceChange)diffNode.getDiff();
		assertEquals(DifferenceKind.ADD, diff.getKind());
		assertEquals(DifferenceSource.RIGHT, diff.getSource());

		matchNode = (MatchNode)bookCategoryChildren.get(3);
		assertEquals(1, matchNode.getChildren().size());
		match = matchNode.getMatch();
		assertEquals("Manhwa", safeEGet(match.getRight(), "name"));
		diffNode = (DiffNode)matchNode.getChildren().get(0);
		assertTrue(diffNode.getChildren().isEmpty());
		diff = (ReferenceChange)diffNode.getDiff();
		assertEquals(DifferenceKind.ADD, diff.getKind());
		assertEquals(DifferenceSource.RIGHT, diff.getSource());
	}

	@Test
	public void testGetChildren_Magazine1() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode magazine1Node = (MatchNode)ePackageMatch.getChildren().get(6);
		checkMatch(magazine1Node, LEFT, "Magazine");
		assertEquals(4, magazine1Node.getChildren().size());

		DiffNode node = (DiffNode)magazine1Node.getChildren().get(0);
		checkRefChange(node, LEFT, ADD, "eClassifiers");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());

		node = (DiffNode)magazine1Node.getChildren().get(1);
		checkRefChange(node, LEFT, ADD, "eSuperTypes");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());

		MatchNode matchNode = (MatchNode)magazine1Node.getChildren().get(2);
		assertEquals(2, matchNode.getChildren().size());
		node = (DiffNode)matchNode.getChildren().get(0);
		checkRefChange(node, LEFT, ADD, "eStructuralFeatures");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());
		node = (DiffNode)matchNode.getChildren().get(1);
		checkRefChange(node, LEFT, CHANGE, "eType");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());

		matchNode = (MatchNode)magazine1Node.getChildren().get(3);
		assertEquals(2, matchNode.getChildren().size());
		node = (DiffNode)matchNode.getChildren().get(0);
		checkRefChange(node, LEFT, ADD, "eStructuralFeatures");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());
		node = (DiffNode)matchNode.getChildren().get(1);
		checkRefChange(node, LEFT, CHANGE, "eType");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());
	}

	@Test
	public void testGetChildren_Magazine2() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode magazine2Node = (MatchNode)ePackageMatch.getChildren().get(8);
		checkMatch(magazine2Node, RIGHT, "Magazine");
		assertEquals(2, magazine2Node.getChildren().size());
		DiffNode node = (DiffNode)magazine2Node.getChildren().get(0);
		checkRefChange(node, RIGHT, ADD, "eClassifiers");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());

		node = (DiffNode)magazine2Node.getChildren().get(1);
		checkRefChange(node, RIGHT, ADD, "eSuperTypes");
		checkNoChild(node);
		assertNotNull(node.getDiff().getConflict());
	}

	@Test
	public void testGetChildren_Periodical() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode periodicalNode = (MatchNode)ePackageMatch.getChildren().get(7);
		checkMatch(periodicalNode, RIGHT, "Periodical");
		List<TreeNode> periodicalChildren = periodicalNode.getChildren();
		assertEquals(5, periodicalChildren.size());

		TreeNode node0 = periodicalChildren.get(0);
		checkRefChange(node0, LEFT, DELETE, "eClassifiers");
		checkNoChild(node0);
		assertNotNull(((Diff)node0.getData()).getConflict());
		TreeNode node1 = periodicalChildren.get(1);
		checkRefChange(node1, RIGHT, ADD, "eSuperTypes");
		checkNoChild(node1);
		assertNotNull(((Diff)node1.getData()).getConflict());
		assertSame(((Diff)node0.getData()).getConflict(), ((Diff)node1.getData()).getConflict());
		TreeNode node2 = periodicalChildren.get(2);
		checkRefChange(node2, LEFT, DELETE, "eSuperTypes");
		checkNoChild(node2);

		TreeNode node3 = periodicalChildren.get(3);
		assertTrue(node3 instanceof MatchNode);
		assertEquals(2, node3.getChildren().size());
		DiffNode childNode = (DiffNode)node3.getChildren().get(0);
		checkRefChange(childNode, LEFT, DELETE, "eStructuralFeatures");
		checkNoChild(childNode);
		assertNull(childNode.getDiff().getConflict());

		childNode = (DiffNode)node3.getChildren().get(1);
		checkRefChange(childNode, LEFT, CHANGE, "eType");
		checkNoChild(childNode);
		assertNull(childNode.getDiff().getConflict());

		TreeNode node4 = periodicalChildren.get(4);
		assertTrue(node4 instanceof MatchNode);
		assertEquals(4, node4.getChildren().size());
		childNode = (DiffNode)node4.getChildren().get(0);
		checkRefChange(childNode, LEFT, DELETE, "eStructuralFeatures");
		checkNoChild(childNode);
		Conflict conflict = childNode.getDiff().getConflict();
		assertNotNull(conflict);
		assertEquals(PSEUDO, conflict.getKind());

		childNode = (DiffNode)node4.getChildren().get(1);
		checkRefChange(childNode, RIGHT, DELETE, "eStructuralFeatures");
		checkNoChild(childNode);
		assertNotNull(childNode.getDiff().getConflict());
		assertSame(conflict, childNode.getDiff().getConflict());

		childNode = (DiffNode)node4.getChildren().get(2);
		checkRefChange(childNode, LEFT, CHANGE, "eType");
		checkNoChild(childNode);
		Conflict conflict2 = childNode.getDiff().getConflict();
		assertNotNull(conflict2);
		assertEquals(PSEUDO, conflict2.getKind());
		assertNotSame(conflict, conflict2);

		childNode = (DiffNode)node4.getChildren().get(3);
		checkRefChange(childNode, RIGHT, CHANGE, "eType");
		checkNoChild(childNode);
		assertNotNull(childNode.getDiff().getConflict());
		assertSame(conflict2, childNode.getDiff().getConflict());
	}

	@Test
	public void testGetChildren_Person() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode personNode = (MatchNode)ePackageMatch.getChildren().get(5);
		checkMatch(personNode, LEFT, "Person");
		List<TreeNode> personChildren = personNode.getChildren();
		assertEquals(3, personChildren.size());

		TreeNode fullNameNode = personChildren.get(0);
		assertEquals(2, fullNameNode.getChildren().size());
		DiffNode diffNode = (DiffNode)fullNameNode.getChildren().get(0);
		checkRefChange(diffNode, LEFT, ADD, "eStructuralFeatures");
		checkNoChild(diffNode);
		assertNull(diffNode.getDiff().getConflict());

		diffNode = (DiffNode)fullNameNode.getChildren().get(1);
		checkRefChange(diffNode, LEFT, CHANGE, "eType");
		checkNoChild(diffNode);
		assertNull(diffNode.getDiff().getConflict());

		TreeNode firstNameNode = personChildren.get(1);
		assertEquals(2, firstNameNode.getChildren().size());
		diffNode = (DiffNode)firstNameNode.getChildren().get(0);
		checkRefChange(diffNode, LEFT, DELETE, "eStructuralFeatures");
		checkNoChild(diffNode);
		assertNull(diffNode.getDiff().getConflict());

		diffNode = (DiffNode)firstNameNode.getChildren().get(1);
		checkRefChange(diffNode, LEFT, CHANGE, "eType");
		checkNoChild(diffNode);
		assertNull(diffNode.getDiff().getConflict());

		TreeNode lastNameNode = personChildren.get(2);
		assertEquals(3, lastNameNode.getChildren().size());
		diffNode = (DiffNode)lastNameNode.getChildren().get(0);
		checkRefChange(diffNode, LEFT, DELETE, "eStructuralFeatures");
		checkNoChild(diffNode);
		Conflict conflict = diffNode.getDiff().getConflict();
		assertNotNull(conflict);

		diffNode = (DiffNode)lastNameNode.getChildren().get(1);
		checkRefChange(diffNode, LEFT, CHANGE, "eType");
		checkNoChild(diffNode);
		assertNull(diffNode.getDiff().getConflict());

		diffNode = (DiffNode)lastNameNode.getChildren().get(2);
		checkAttChange(diffNode, RIGHT, CHANGE, "name", "familyName");
		checkNoChild(diffNode);
		assertSame(conflict, diffNode.getDiff().getConflict());
	}

	@Test
	public void testGetChildren_TitledItem() throws IOException {
		TreeNode ePackageMatch = getEcoreA1_EPackageMatch();
		MatchNode titledItemNode = (MatchNode)ePackageMatch.getChildren().get(9);
		checkMatch(titledItemNode, RIGHT, "TitledItem");
		assertEquals(2, titledItemNode.getChildren().size());
		DiffNode node = (DiffNode)titledItemNode.getChildren().get(0);
		checkRefChange(node, RIGHT, ADD, "eClassifiers");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());

		MatchNode titleMatchNode = (MatchNode)titledItemNode.getChildren().get(1);
		assertEquals(2, titleMatchNode.getChildren().size());
		node = (DiffNode)titleMatchNode.getChildren().get(0);
		checkRefChange(node, RIGHT, ADD, "eStructuralFeatures");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());

		node = (DiffNode)titleMatchNode.getChildren().get(1);
		checkRefChange(node, RIGHT, CHANGE, "eType");
		checkNoChild(node);
		assertNull(node.getDiff().getConflict());
	}

	private Object safeEGet(EObject o, String name) {
		return o.eGet(o.eClass().getEStructuralFeature(name));
	}

	protected void checkMatch(MatchNode node, DifferenceSource side, String value) {
		assertEquals(value, safeEGet(MatchUtil.getMatchedObject(node.getMatch(), side), "name"));
	}

	protected void checkDiffNode(TreeNode node, Class<?> clazz, DifferenceSource src, DifferenceKind kind) {
		assertTrue(node instanceof DiffNode);
		Diff diff = ((DiffNode)node).getDiff();
		assertTrue(clazz.isAssignableFrom(diff.getClass()));
		assertSame(src, diff.getSource());
		assertSame(kind, diff.getKind());
	}

	protected void checkRefChange(TreeNode node, DifferenceSource src, DifferenceKind kind, String refName) {
		checkDiffNode(node, ReferenceChange.class, src, kind);
		ReferenceChange diff = (ReferenceChange)((DiffNode)node).getDiff();
		assertEquals(refName, diff.getReference().getName());
	}

	protected void checkAttChange(TreeNode node, DifferenceSource src, DifferenceKind kind, String refName,
			Object value) {
		checkDiffNode(node, AttributeChange.class, src, kind);
		AttributeChange diff = (AttributeChange)((DiffNode)node).getDiff();
		assertEquals(refName, diff.getAttribute().getName());
		assertEquals(value, diff.getValue());
	}

	protected void checkNoChild(TreeNode node) {
		assertTrue(node.getChildren().isEmpty());
	}
}
