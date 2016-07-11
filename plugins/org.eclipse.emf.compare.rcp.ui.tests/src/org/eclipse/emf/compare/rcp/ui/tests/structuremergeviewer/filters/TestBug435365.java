/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.filters;

import static com.google.common.base.Predicates.alwaysTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.DiffNode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.nodes.MatchNode;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider.AbstractTestTreeNodeItemProviderAdapter;
import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;
import org.junit.Test;

import com.google.common.base.Predicate;

/**
 * This test is related to <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=435365"</a>.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
@SuppressWarnings("restriction")
public class TestBug435365 extends AbstractTestTreeNodeItemProviderAdapter {

	/**
	 * Tests that the cascading filter does not hide cascading differences if the parent difference is a
	 * {@link DifferenceKind#MOVE}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCascadingFilterNotHidingDiffUnderMove() throws IOException {
		// Builds the input tree.
		Comparison comparison = getComparison(new Bug435365Scope());
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
		IDifferenceGroup group = new BasicDifferenceGroupImpl(comparison, alwaysTrue(),
				crossReferenceAdapter);
		List<? extends TreeNode> roots = group.getChildren();
		// Gets the node matching the move diff.
		TreeNode movingNode = roots.get(0).getChildren().get(0).getChildren().get(0);
		Predicate<? super EObject> cascadingFilter = new CascadingDifferencesFilter()
				.getPredicateWhenSelected();
		assertFalse(cascadingFilter.apply(movingNode));
		EList<TreeNode> children = movingNode.getChildren();
		assertEquals(2, children.size());
		DiffNode diffNode = (DiffNode)children.get(0);
		// Checks that its child is not hidden by the filter.
		assertFalse(cascadingFilter.apply(diffNode));
		MatchNode matchNode = (MatchNode)children.get(1);
		assertFalse(cascadingFilter.apply(matchNode));
		assertEquals(1, matchNode.getChildren().size());
		diffNode = (DiffNode)matchNode.getChildren().get(0);
		assertFalse(cascadingFilter.apply(diffNode));
	}

	/**
	 * Input data for this bug.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	public class Bug435365Scope extends AbstractInputData implements ResourceScopeProvider {

		public Resource getLeft() throws IOException {
			return loadFromClassLoader("data/_435365/afterMove.nodes");//$NON-NLS-1$
		}

		public Resource getRight() throws IOException {
			return loadFromClassLoader("data/_435365/beforeMove.nodes");//$NON-NLS-1$
		}

		public Resource getOrigin() throws IOException {
			return null;
		}
	}

}
