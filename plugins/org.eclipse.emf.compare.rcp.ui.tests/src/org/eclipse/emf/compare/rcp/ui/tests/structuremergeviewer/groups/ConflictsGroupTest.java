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

import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider.ConflictsGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider.ConflictsGroupImpl.CompositeConflict;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.ConflictNode;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.Test;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class ConflictsGroupTest extends AbstractTestTreeNodeItemProviderAdapter {

	private static final CompareFactory FACTORY = CompareFactory.eINSTANCE;

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

	protected void checkText(TreeNode childNode, String expected) {
		assertEquals(expected, itemDelegator.getText(childNode));
	}
}
