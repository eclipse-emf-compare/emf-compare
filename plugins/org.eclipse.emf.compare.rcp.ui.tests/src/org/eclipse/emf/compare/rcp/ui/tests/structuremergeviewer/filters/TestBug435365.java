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

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.CascadingDifferencesFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
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
import org.junit.Assert;
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
	public void testCascadingFilterNotHiddingDiffUnderMove() throws IOException {
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
		TreeNode moveNode = getMoveNode(roots);
		Predicate<? super EObject> whenSelectedCascadingFilterPredicate = new CascadingDifferencesFilter()
				.getPredicateWhenSelected();
		Assert.assertEquals(false, whenSelectedCascadingFilterPredicate.apply(moveNode));
		EList<TreeNode> children = moveNode.getChildren();
		Assert.assertEquals(1, children.size());
		TreeNode newAdditionNode = children.get(0);
		// Checks that its child is not hidden by the filter.
		Assert.assertEquals(false, whenSelectedCascadingFilterPredicate.apply(newAdditionNode));
	}

	/**
	 * Retrieves the node matching the Move difference.
	 * 
	 * @param roots
	 * @return
	 */
	private TreeNode getMoveNode(List<? extends TreeNode> roots) {
		for (TreeNode root : roots) {
			TreeIterator<EObject> nodeIte = root.eAllContents();
			while (nodeIte.hasNext()) {
				EObject n = nodeIte.next();
				if (n instanceof TreeNode) {
					TreeNode treeNode = (TreeNode)n;
					EObject data = treeNode.getData();
					if (data instanceof ReferenceChange
							&& ((ReferenceChange)data).getKind() == DifferenceKind.MOVE) {
						return treeNode;
					}
				}
			}
		}
		return null;
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
