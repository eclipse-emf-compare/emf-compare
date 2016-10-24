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
package org.eclipse.emf.compare.uml2.rcp.ui.tests.groups;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider.CompositeConflict;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider.ConflictsGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.ConflictNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchNode;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.data.uml.compositeconflict.UMLJoiningConflictsWithOverlappingDiffsInputData;
import org.eclipse.emf.compare.uml2.rcp.ui.tests.groups.data.uml.conflictrefiningdiffs.UMLConflictWithRefiningDiffInputData;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.Test;

@SuppressWarnings("restriction")
public class ConflictsGroupTest extends AbstractTestTreeNodeItemProviderAdapter {

	private AdapterFactoryItemDelegator itemDelegator;

	@Override
	public void before() throws IOException {
		super.before();

		final Collection<AdapterFactory> factories = Lists.newArrayList();
		factories.add(new CompareItemProviderAdapterFactorySpec());
		factories.add(treeItemProviderAdapterFactory);
		factories.add(new EcoreItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());

		final AdapterFactory composedAdapterFactory = new ComposedAdapterFactory(factories);
		itemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
	}

	/**
	 * Tests that for conflicts containing refining diffs, the refining diffs are replaced with their refined
	 * diffs when displayed in the SMV. This test is related to <a
	 * href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 * 
	 * @throws IOException
	 */
	@Test
	public void testReplacementOfRefiningDiffs() throws IOException {
		Comparison comparison = getComparison(new UMLConflictWithRefiningDiffInputData());
		ECrossReferenceAdapter crossReferenceAdapter = new ECrossReferenceAdapter() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
			 */
			@Override
			protected boolean isIncluded(EReference eReference) {
				return eReference == TreePackage.Literals.TREE_NODE__DATA;
			}
		};
		final ConflictsGroupImpl conflictsGroup = new ConflictsGroupImpl(comparison,
				hasConflict(ConflictKind.REAL, ConflictKind.PSEUDO),
				EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.conflicts.label"), //$NON-NLS-1$
				crossReferenceAdapter);
		conflictsGroup.buildSubTree();

		List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		List<? extends TreeNode> matchNodes = conflictNode.getChildren();
		assertEquals(1, matchNodes.size());

		// Match node for OpauqeAction
		MatchNode matchNode = (MatchNode)matchNodes.get(0);
		checkText(matchNode, "Opaque Action OpaqueAction"); //$NON-NLS-1$

		List<? extends TreeNode> diffNodes = matchNode.getChildren();
		assertEquals(2, diffNodes.size());

		// Diff node for deletion of OpaqueAction
		DiffNode diffNodeDeletion = (DiffNode)diffNodes.get(0);
		checkText(diffNodeDeletion, "Opaque Action OpaqueAction [ownedNode delete]"); //$NON-NLS-1$
		assertEquals(0, diffNodeDeletion.getChildren().size());

		// Diff node for opaque element change of OpaqueAction
		DiffNode diffNodeChange = (DiffNode)diffNodes.get(1);
		checkText(diffNodeChange, "Opaque Element Body Change Natural language"); //$NON-NLS-1$

		List<? extends TreeNode> diffNodeChildren = diffNodeChange.getChildren();
		assertEquals(2, diffNodeChildren.size());

		// Diff node for body add of OpaqueAction
		DiffNode diffNodeBodyAdd = (DiffNode)diffNodeChildren.get(0);
		checkText(diffNodeBodyAdd, "This is a natural language definition of Opaque... [body add]"); //$NON-NLS-1$
		assertEquals(0, diffNodeBodyAdd.getChildren().size());

		// Diff node for language add of OpaqueAction
		DiffNode diffNodeLanguageAdd = (DiffNode)diffNodeChildren.get(1);
		checkText(diffNodeLanguageAdd, "Natural language [language add]"); //$NON-NLS-1$
		assertEquals(0, diffNodeLanguageAdd.getChildren().size());
	}

	/**
	 * Tests that conflicts containing overlapping diffs are joined in the SMV. Overlapping diffs can happen
	 * due to the replacement of refining diffs with their refined diffs in the SMV. This test is related to
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=501864"</a>
	 * 
	 * @throws IOException
	 */
	@Test
	public void testJoiningConflictsWithOverlappingDiffs() throws IOException {
		Comparison comparison = getComparison(new UMLJoiningConflictsWithOverlappingDiffsInputData());
		ECrossReferenceAdapter crossReferenceAdapter = new ECrossReferenceAdapter() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
			 */
			@Override
			protected boolean isIncluded(EReference eReference) {
				return eReference == TreePackage.Literals.TREE_NODE__DATA;
			}
		};
		final ConflictsGroupImpl conflictsGroup = new ConflictsGroupImpl(comparison,
				hasConflict(ConflictKind.REAL, ConflictKind.PSEUDO),
				EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.conflicts.label"), //$NON-NLS-1$
				crossReferenceAdapter);
		conflictsGroup.buildSubTree();

		// There is one conflict node
		List<? extends TreeNode> conflictNodes = conflictsGroup.getChildren();
		assertEquals(1, conflictNodes.size());

		// The conflict node groups two conflicts
		ConflictNode conflictNode = (ConflictNode)conflictNodes.get(0);
		Conflict conflict = conflictNode.getConflict();
		assertTrue(conflict instanceof CompositeConflict);
		CompositeConflict compositeConflict = (CompositeConflict)conflict;
		assertEquals(2, compositeConflict.getConflicts().size());

		List<? extends TreeNode> matchNodes = conflictNode.getChildren();
		assertEquals(2, matchNodes.size());

		// Match node for association
		MatchNode matchNodeAssociation = (MatchNode)matchNodes.get(0);
		checkText(matchNodeAssociation, "Association"); //$NON-NLS-1$

		// Match node for association property owned by class1
		MatchNode matchNodePropertyClass1 = (MatchNode)matchNodes.get(1);
		checkText(matchNodePropertyClass1, "Property class2_"); //$NON-NLS-1$

		List<? extends TreeNode> childrenMatchNodeAssociation = matchNodeAssociation.getChildren();
		assertEquals(2, childrenMatchNodeAssociation.size());

		// Diff node for deletion of association
		DiffNode diffNodeDeletion = (DiffNode)childrenMatchNodeAssociation.get(0);
		checkText(diffNodeDeletion, "Association Change DELETE"); //$NON-NLS-1$
		assertTrue(diffNodeDeletion.getChildren().size() > 0);

		// Match node for association property owned by association
		MatchNode matchNodePropertyAssociation = (MatchNode)childrenMatchNodeAssociation.get(1);
		checkText(matchNodePropertyAssociation, "Property class1_"); //$NON-NLS-1$
		List<? extends TreeNode> childrenMatchNodePropertyAssociation = matchNodePropertyAssociation
				.getChildren();
		assertEquals(1, childrenMatchNodePropertyAssociation.size());

		// Diff node for name update of association property owned by association
		DiffNode diffNodeUpdatePropertyAssociation = (DiffNode)childrenMatchNodePropertyAssociation.get(0);
		checkText(diffNodeUpdatePropertyAssociation, "class1_ [name set]"); //$NON-NLS-1$
		assertEquals(0, diffNodeUpdatePropertyAssociation.getChildren().size());

		List<? extends TreeNode> childrenMatchNodePropertyClass1 = matchNodePropertyClass1.getChildren();
		assertEquals(1, childrenMatchNodePropertyClass1.size());

		// Diff node for name update of association property owned by class1
		DiffNode diffNodeUpdatePropertyClass1 = (DiffNode)childrenMatchNodePropertyClass1.get(0);
		checkText(diffNodeUpdatePropertyClass1, "class2_ [name set]"); //$NON-NLS-1$
		assertEquals(0, diffNodeUpdatePropertyClass1.getChildren().size());
	}

	protected void checkText(TreeNode childNode, String expected) {
		assertEquals(expected, itemDelegator.getText(childNode));
	}
}
