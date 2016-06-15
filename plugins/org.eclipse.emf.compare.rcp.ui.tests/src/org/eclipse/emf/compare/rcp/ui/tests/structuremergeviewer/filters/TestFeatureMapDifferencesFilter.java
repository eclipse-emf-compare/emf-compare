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

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.FeatureMapDifferencesFilter;
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
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class TestFeatureMapDifferencesFilter extends AbstractTestTreeNodeItemProviderAdapter {

	/**
	 * Tests that the feature map differences filter does not hide feature map differences that have no
	 * equivalences.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testFeatureMapDifferencesWithNoEquivalences() throws IOException {
		// Builds the input tree.
		Comparison comparison = getComparison(new TestFeatureMapFilterScope());
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
		Predicate<? super EObject> featureMapDifferencesFilter = new FeatureMapDifferencesFilter()
				.getPredicateWhenSelected();

		for (TreeNode root : roots) {
			TreeIterator<EObject> nodeIte = root.eAllContents();
			while (nodeIte.hasNext()) {
				EObject n = nodeIte.next();
				if (n instanceof TreeNode) {
					TreeNode treeNode = (TreeNode)n;
					EObject data = treeNode.getData();
					if (data instanceof FeatureMapChange
							&& ((FeatureMapChange)data).getEquivalence() != null) {
						Assert.assertTrue(featureMapDifferencesFilter.apply(treeNode));
					} else {
						Assert.assertFalse(featureMapDifferencesFilter.apply(treeNode));
					}
				}
			}
		}
	}

	/**
	 * Input data for this test.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	public class TestFeatureMapFilterScope extends AbstractInputData implements ResourceScopeProvider {

		public Resource getLeft() throws IOException {
			return loadFromClassLoader("data/featuremapfilter/left.nodes");//$NON-NLS-1$
		}

		public Resource getRight() throws IOException {
			return loadFromClassLoader("data/featuremapfilter/right.nodes");//$NON-NLS-1$
		}

		public Resource getOrigin() throws IOException {
			return null;
		}
	}

}
