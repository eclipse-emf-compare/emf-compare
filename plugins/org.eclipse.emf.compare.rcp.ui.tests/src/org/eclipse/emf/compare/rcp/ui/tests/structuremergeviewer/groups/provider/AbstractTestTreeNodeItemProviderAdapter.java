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
package org.eclipse.emf.compare.rcp.ui.tests.structuremergeviewer.groups.provider;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider.TreeItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.tree.TreeNode;
import org.junit.Before;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class AbstractTestTreeNodeItemProviderAdapter {

	protected static TreeItemProviderAdapterFactorySpec treeItemProviderAdapterFactory;

	@Before
	public void before() throws IOException {
		treeItemProviderAdapterFactory = new TreeItemProviderAdapterFactorySpec();
	}

	/**
	 * @return the comparison
	 * @throws IOException
	 */
	protected static Comparison getComparison(ResourceScopeProvider scopeProvider) throws IOException {
		final IComparisonScope scope = new DefaultComparisonScope(scopeProvider.getLeft(), scopeProvider
				.getRight(), scopeProvider.getOrigin());
		return EMFCompare.builder().build().compare(scope);
	}

	/**
	 * Function that retrieve the data of the given TreeNode.
	 */
	public static final Function<Object, EObject> TREE_NODE_DATA = new Function<Object, EObject>() {
		public EObject apply(Object node) {
			if (node instanceof TreeNode) {
				return ((TreeNode)node).getData();
			} else if (node instanceof EObject) {
				return (EObject)node;
			}
			return null;
		}
	};
	
	/**
	 * Function that returns all contents of the given EObject.
	 */
	public static final Function<Object, Iterator<EObject>> E_ALL_CONTENTS = new Function<Object, Iterator<EObject>>() {
		public Iterator<EObject> apply(Object object) {
			if (object instanceof EObject) {				
				return ((EObject)object).eAllContents();
			}
			return null;
		}
	};
	
	
	public static TreeNode getTreeNode(TreeNode parent, EObject eObject) {
		for (TreeNode child : parent.getChildren()) {
			EObject data = child.getData();
			if (eObject.equals(data)) {
				return child;
			}
		}
		return null;
	}
	
	protected Match getMatchWithFeatureValue(Iterable<?> c, final String featureName, final Object value) {
		Iterable<Match> matches = filter(c, Match.class);
		Predicate<Match> predicate = hasFeatureValue(featureName, value);
		return find(matches, predicate);
	}
	
	protected ReferenceChange getReferenceChangeWithFeatureValue(Iterable<?> c, final String featureName,
			final Object value) {
		Iterable<ReferenceChange> matches = filter(c, ReferenceChange.class);
		Predicate<ReferenceChange> predicate = new Predicate<ReferenceChange>() {
			public boolean apply(ReferenceChange referenceChange) {
				EObject referenceChangeValue = referenceChange.getValue();
				if (referenceChangeValue != null) {
					return Objects.equal(eGet(referenceChangeValue, featureName), value);
				}
				return false;
			}
		};
		return find(matches, predicate);
	}
	
	protected Predicate<Match> hasFeatureValue(final String featureName, final Object value) {
		Predicate<Match> predicate = new Predicate<Match>() {
			public boolean apply(Match match) {
				final boolean ret;
				final EObject left = match.getLeft();
				final EObject right = match.getRight();
				final EObject origin = match.getOrigin();
				if (left != null) {
					ret = Objects.equal(value, eGet(left, featureName));
				} else if (right != null) {
					ret = Objects.equal(value, eGet(right, featureName));
				} else if (origin != null) {
					ret = Objects.equal(value, eGet(origin, featureName));
				} else {
					ret = false;
				}
				return ret;
			}
		};
		return predicate;
	}
	
	protected Object eGet(EObject eObject, String featureName) {
		EStructuralFeature eStructuralFeature = eObject.eClass().getEStructuralFeature(featureName);
		return eObject.eGet(eStructuralFeature);
	}
	
	protected ITreeItemContentProvider adaptAsITreeItemContentProvider(Notifier notifier) {
		ITreeItemContentProvider contentProvider = (ITreeItemContentProvider)treeItemProviderAdapterFactory
				.adapt(notifier, ITreeItemContentProvider.class);
		return contentProvider;
	}
}
