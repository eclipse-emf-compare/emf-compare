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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.tests.framework.RuntimeTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.TechnicalitiesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.ThreeWayComparisonGroupProvider.ConflictsGroupImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.runner.RunWith;

import com.google.common.base.Predicate;

@SuppressWarnings("restriction")
@RunWith(RuntimeTestRunner.class)
public class TechnicalitiesFilterTests {

	/**
	 * This test checks that the following type of pseudo conflicts is filtered by the Technicalities filter.
	 * 
	 * <pre>
	 * Ancestor: - root
	 * 				- class1
	 * 					- attribute1
	 * 
	 * Left: - root
	 * 
	 * Right: - root
	 * 			- class1
	 * </pre>
	 * 
	 * @throws IOException
	 */
	@Compare(left = "data/predicates/technicalities/directPseudoConflict/left.ecore", right = "data/predicates/technicalities/directPseudoConflict/right.ecore", ancestor = "data/predicates/technicalities/directPseudoConflict/ancestor.ecore")
	public void testDirectPseudoConflictingDiffFilter(Comparison comparison) throws IOException {
		ConflictsGroupImpl group = new ThreeWayComparisonGroupProvider.ConflictsGroupImpl(comparison,
				"Conflict", new TestECrossReferenceAdapter());
		group.buildSubTree();
		assertEquals(1, group.getChildren().size());

		TreeNode conflictNode = group.getChildren().get(0);
		assertEquals(1, conflictNode.getChildren().size());

		TreeNode matchNode = conflictNode.getChildren().get(0);
		assertEquals(2, matchNode.getChildren().size());

		TechnicalitiesFilter filter = new TechnicalitiesFilter();
		Predicate<? super EObject> selected = filter.getPredicateWhenSelected();
		Predicate<? super EObject> unselected = filter.getPredicateWhenUnselected();

		TreeNode diffNode1 = matchNode.getChildren().get(0);
		assertTrue(selected.apply(diffNode1));
		assertFalse(unselected.apply(diffNode1));

		TreeNode diffNode2 = matchNode.getChildren().get(1);
		assertTrue(selected.apply(diffNode2));
		assertFalse(unselected.apply(diffNode2));
	}

	private class TestECrossReferenceAdapter extends ECrossReferenceAdapter {
		@Override
		protected boolean isIncluded(EReference eReference) {
			return eReference == TreePackage.Literals.TREE_NODE__DATA;
		}
	}
}
