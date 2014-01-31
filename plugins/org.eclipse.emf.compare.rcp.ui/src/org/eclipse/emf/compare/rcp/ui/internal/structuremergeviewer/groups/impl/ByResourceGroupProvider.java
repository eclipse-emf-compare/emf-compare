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

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.AbstractDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.scope.IComparisonScope;
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
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Collection<? extends IDifferenceGroup> getGroups(Comparison comparison) {
		if (group == null || !comparison.equals(comp)) {
			dispose();
			this.comp = comparison;
			group = new ResourceGroup(comparison, getCrossReferenceAdapter());
		}
		return ImmutableList.of(group);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#defaultSelected()
	 */
	public boolean defaultSelected() {
		return activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#setDefaultSelected(boolean)
	 */
	public void setDefaultSelected(boolean active) {
		this.activeByDefault = active;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#isEnabled(org
	 *      .eclipse.emf.compare.scope.IComparisonScope, org.eclipse.emf.compare.Comparison)
	 */
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#dispose()
	 */
	public void dispose() {
		this.comp = null;
		if (this.group != null) {
			this.group.dispose();
			this.group = null;
		}
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
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#BasicDifferenceGroupImpl(org.eclipse.emf.compare.Comparison)
		 */
		public ResourceGroup(Comparison comparison, ECrossReferenceAdapter crossReferenceAdapter) {
			super(comparison, Predicates.<Diff> alwaysTrue(), crossReferenceAdapter);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#getChildren()
		 */
		@Override
		public List<? extends TreeNode> getChildren() {
			if (children == null) {
				children = newArrayList();
				for (MatchResource matchResource : getComparison().getMatchedResources()) {
					children.add(buildSubTree(matchResource));
				}
				registerCrossReferenceAdapter(children);
			}
			return children;
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
			boolean isContainment = false;

			if (parentMatch != null) {
				Collection<Diff> containmentChanges = filter(parentMatch.getDifferences(),
						containmentReferenceForMatch(match));
				if (!containmentChanges.isEmpty()) {
					isContainment = true;
					for (Diff diff : containmentChanges) {
						ret.add(wrap(diff));
					}
				}
			} else {
				Collection<Diff> resourceAttachmentChanges = filter(match.getDifferences(),
						resourceAttachmentChange());
				if (!resourceAttachmentChanges.isEmpty()) {
					for (Diff diff : resourceAttachmentChanges) {
						ret.add(wrap(diff));
					}
				}
			}
			if (ret.isEmpty() && !matchWithLeftAndRightInDifferentContainer(match)) {
				ret.add(wrap(match));
			}

			Collection<TreeNode> toRemove = Lists.newArrayList();
			for (TreeNode treeNode : ret) {
				boolean hasDiff = false;
				boolean hasNonEmptySubMatch = false;
				for (Diff diff : filter(match.getDifferences(), and(filter, not(or(
						CONTAINMENT_REFERENCE_CHANGE, resourceAttachmentChange()))))) {
					hasDiff = true;
					treeNode.getChildren().add(wrap(diff));
				}
				for (Match subMatch : match.getSubmatches()) {
					List<TreeNode> buildSubTree = buildSubTree(match, subMatch);
					if (!buildSubTree.isEmpty()) {
						hasNonEmptySubMatch = true;
						treeNode.getChildren().addAll(buildSubTree);
					}
				}
				for (Diff diff : filter(match.getDifferences(), and(filter, and(CONTAINMENT_REFERENCE_CHANGE,
						ofKind(DifferenceKind.MOVE))))) {
					if (!containsChildrenWithDataEqualsToDiff(treeNode, diff)) {
						TreeNode buildSubTree = buildSubTree(diff);
						if (buildSubTree != null) {
							hasDiff = true;
							treeNode.getChildren().add(buildSubTree);
							List<TreeNode> matchSubTree = buildSubTree((Match)null, getComparison().getMatch(
									((ReferenceChange)diff).getValue()));
							for (TreeNode matchSubTreeNode : matchSubTree) {
								buildSubTree.getChildren().addAll(matchSubTreeNode.getChildren());
							}
						}
					}
				}
				if (!(isContainment || hasDiff || hasNonEmptySubMatch || filter.equals(Predicates
						.alwaysTrue()))) {
					toRemove.add(treeNode);
				}
			}

			ret.removeAll(toRemove);

			return ret;
		}
	}
}
