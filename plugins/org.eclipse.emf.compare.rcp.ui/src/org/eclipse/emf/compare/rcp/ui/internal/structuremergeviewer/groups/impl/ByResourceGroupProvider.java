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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * This implementation of a {@link IDifferenceGroupProvider} will be used to group the differences by their
 * Resource.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 3.0
 */
public class ByResourceGroupProvider extends AdapterImpl implements IDifferenceGroupProvider {

	/** A human-readable label for this group provider. This will be displayed in the EMF Compare UI. */
	private String label;

	/** The initial activation state of the group provider. */
	private boolean activeByDefault;

	/** The unique group provided by this provider. */
	private IDifferenceGroup group;

	/** The comparison object. */
	private Comparison comp;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(java.lang.Object)
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return type == IDifferenceGroupProvider.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Collection<? extends IDifferenceGroup> getGroups(Comparison comparison) {
		if (group == null || !comparison.equals(comp)) {
			this.comp = comparison;
			group = new ResourceGroup(comparison);
		}
		return ImmutableList.of(group);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#defaultSelected()
	 */
	public boolean defaultSelected() {
		return activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#setDefaultSelected(boolean)
	 */
	public void setDefaultSelected(boolean activeByDefault) {
		this.activeByDefault = activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#isEnabled(org
	 *      .eclipse.emf.compare.scope.IComparisonScope, org.eclipse.emf.compare.Comparison)
	 */
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		return true;
	}

	/**
	 * Specialized {@link BasicDifferenceGroupImpl} for Resources.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class ResourceGroup extends BasicDifferenceGroupImpl {

		/**
		 * {@inheritDoc}.
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl#BasicDifferenceGroupImpl(org.eclipse.emf.compare.Comparison)
		 */
		public ResourceGroup(Comparison comparison) {
			super(comparison, Predicates.<Diff> alwaysTrue());
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl#getGroupTree()
		 */
		@Override
		public List<? extends TreeNode> getGroupTree() {
			if (children == null) {
				children = newArrayList();
				for (MatchResource matchResource : comparison.getMatchedResources()) {
					children.add(buildSubTree(matchResource));
				}
			}
			return children;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl#buildSubTree(org.eclipse.emf.compare.Match,
		 *      org.eclipse.emf.compare.MatchResource)
		 */
		@Override
		protected TreeNode buildSubTree(MatchResource matchResource) {
			TreeNode ret = wrap(matchResource);

			for (Match match : comparison.getMatches()) {
				ret.getChildren().addAll(buildSubTree(matchResource, match));
			}

			return ret;
		}

		/**
		 * Build the sub tree of the given Match that is a root of the given MatchResource.
		 * 
		 * @param matchResource
		 *            the given MatchResource.
		 * @param match
		 *            the given Match.
		 * @return the sub tree of the given Match that is a root of the given MatchResource.
		 */
		protected List<TreeNode> buildSubTree(MatchResource matchResource, Match match) {
			List<TreeNode> ret = newArrayList();
			if (isRootOfResourceURI(match.getLeft(), matchResource.getLeftURI())
					|| isRootOfResourceURI(match.getRight(), matchResource.getRightURI())
					|| isRootOfResourceURI(match.getOrigin(), matchResource.getOriginURI())) {
				ret.addAll(buildSubTree((Match)null, match));
			} else {
				for (Match subMatch : match.getSubmatches()) {
					ret.addAll(buildSubTree(matchResource, subMatch));
				}
			}
			return ret;
		}

		/**
		 * Check if the resource of the given object as the same uri as the given uri.
		 * 
		 * @param eObject
		 *            the given object.
		 * @param uri
		 *            the given uri.
		 * @return true if the resource of the given object as the same uri as the given uri, false otherwise.
		 */
		protected boolean isRootOfResourceURI(EObject eObject, String uri) {
			return eObject != null && uri != null && eObject.eResource() != null
					&& uri.equals(eObject.eResource().getURI().toString());
		}

	}
}
