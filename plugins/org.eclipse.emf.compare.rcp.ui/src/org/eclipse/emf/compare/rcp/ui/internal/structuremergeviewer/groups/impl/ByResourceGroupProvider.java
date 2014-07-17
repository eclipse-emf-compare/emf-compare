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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.AbstractDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * This implementation of a
 * {@link org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider} will be used to
 * group the differences by their Resource.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
public class ByResourceGroupProvider extends AbstractDifferenceGroupProvider {

	/**
	 * Specialized {@link BasicDifferenceGroupImpl} for Resources.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class ResourceGroup extends BasicDifferenceGroupImpl {

		/**
		 * {@inheritDoc}.
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#BasicDifferenceGroupImpl(org.eclipse.emf.compare.Comparison)
		 */
		public ResourceGroup(Comparison comparison, ECrossReferenceAdapter crossReferenceAdapter) {
			super(comparison, Predicates.<Diff> alwaysTrue(), crossReferenceAdapter);
		}

		@Override
		public synchronized void buildSubTree() {
			children = newArrayList();
			extensionDiffProcessed = newLinkedHashSet();
			for (MatchResource matchResource : getComparison().getMatchedResources()) {
				children.add(buildSubTree(matchResource));
			}
			registerCrossReferenceAdapter(children);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#buildSubTree(org.eclipse.emf.compare.MatchResource)
		 */
		@Override
		protected TreeNode buildSubTree(MatchResource matchResource) {
			TreeNode ret = wrap(matchResource);

			for (Match match : getComparison().getMatches()) {
				ret.getChildren().addAll(buildSubTree(matchResource, match));
			}

			return ret;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#buildSubTree(org.eclipse.emf.compare.MatchResource,
		 *      org.eclipse.emf.compare.Match)
		 */
		@Override
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
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#buildSubTree(org.eclipse.emf.compare.Match,
		 *      org.eclipse.emf.compare.Match)
		 */
		@Override
		public List<TreeNode> buildSubTree(Match parentMatch, Match match) {
			final List<TreeNode> ret = Lists.newArrayList();
			Collection<Diff> resourceAttachmentChanges = filter(match.getDifferences(),
					resourceAttachmentChange());
			if (!resourceAttachmentChanges.isEmpty()) {
				for (Diff diff : resourceAttachmentChanges) {
					ret.add(wrap(diff));
				}
			}

			if (ret.isEmpty()) {
				ret.add(wrap(match));
			}

			for (TreeNode treeNode : ret) {
				treeNode.getChildren().addAll(buildSubTree(match, false, ChildrenSide.BOTH));
			}

			return ret;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.AbstractBuildingDifferenceGroupProvider#buildGroups(org.eclipse.emf.compare.Comparison)
	 */
	@Override
	protected Collection<? extends IDifferenceGroup> buildGroups(Comparison comparison2) {
		ResourceGroup group = new ResourceGroup(comparison2, getCrossReferenceAdapter());
		((BasicDifferenceGroupImpl)group).buildSubTree();
		return ImmutableList.of(group);
	}
}
