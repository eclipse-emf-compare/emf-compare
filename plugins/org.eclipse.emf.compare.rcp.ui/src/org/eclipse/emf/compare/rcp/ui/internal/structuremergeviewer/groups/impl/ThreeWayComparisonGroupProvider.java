/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
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
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterators.concat;
import static com.google.common.collect.Iterators.filter;
import static com.google.common.collect.Iterators.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.SideLabelProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.AbstractDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * This implementation of a
 * {@link org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider} will be used to
 * group the differences by their {@link DifferenceSource side} : left, right and conflicts.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 4.0
 */
public class ThreeWayComparisonGroupProvider extends AbstractDifferenceGroupProvider {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#isEnabled(org
	 *      .eclipse.emf.compare.scope.IComparisonScope, org.eclipse.emf.compare.Comparison)
	 */
	@Override
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		if (comparison != null && comparison.isThreeWay()) {
			return true;
		}
		return false;
	}

	/**
	 * Specialized {@link BasicDifferenceGroupImpl} for Conflicts.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	public static class ConflictsGroupImpl extends BasicDifferenceGroupImpl {

		/**
		 * {@inheritDoc}.
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#BasicDifferenceGroupImpl(org.eclipse.emf.compare.Comparison,
		 *      java.lang.Iterable, com.google.common.base.Predicate, java.lang.String)
		 */
		public ConflictsGroupImpl(Comparison comparison, Predicate<? super Diff> filter, String name,
				ECrossReferenceAdapter crossReferenceAdapter) {
			super(comparison, filter, name, crossReferenceAdapter);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#getStyledName()
		 */
		@Override
		public IComposedStyledString getStyledName() {
			final IStyledString.IComposedStyledString ret = new ComposedStyledString();
			Iterator<EObject> eAllContents = concat(transform(getChildren().iterator(), E_ALL_CONTENTS));
			Iterator<EObject> eAllData = transform(eAllContents, TREE_NODE_DATA);
			UnmodifiableIterator<Diff> eAllDiffData = filter(eAllData, Diff.class);
			Collection<Diff> diffs = Sets.newHashSet(eAllDiffData);
			boolean unresolvedDiffs = any(diffs, and(hasState(DifferenceState.UNRESOLVED), hasConflict(
					ConflictKind.REAL, ConflictKind.PSEUDO)));
			if (unresolvedDiffs) {
				ret.append("> ", Style.DECORATIONS_STYLER); //$NON-NLS-1$
			}
			ret.append(getName());
			return ret;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl#buildSubTree()
		 */
		@Override
		public synchronized void buildSubTree() {
			children = newArrayList();
			extensionDiffProcessed = newLinkedHashSet();
			for (Conflict conflict : getComparison().getConflicts()) {
				TreeNode buildSubTree = buildSubTree(conflict);
				if (buildSubTree != null) {
					children.add(buildSubTree);
				}
			}
			registerCrossReferenceAdapter(children);
		}

		/**
		 * Build sub tree for the given Conflict.
		 * 
		 * @param conflict
		 *            the given Conflict.
		 * @return the sub tree for the given Conflict.
		 */
		protected TreeNode buildSubTree(Conflict conflict) {
			TreeNode ret = wrap(conflict);

			for (Match match : getComparison().getMatches()) {
				buildSubTree(ret, conflict, match);
			}

			return ret;

		}

		/**
		 * Build sub tree for the given Match.
		 * 
		 * @param parentNode
		 *            the parent node of the given match.
		 * @param conflict
		 *            the conflict of the tree.
		 * @param match
		 *            the given match.
		 */
		protected void buildSubTree(TreeNode parentNode, Conflict conflict, Match match) {

			// Use a LinkedHashSet for the first argument of Sets.intersection, in order to keep the order of
			// differences.
			SetView<Diff> setView = Sets.intersection(Sets.newLinkedHashSet(match.getDifferences()), Sets
					.newHashSet(conflict.getDifferences()));
			for (Diff diff : setView) {
				if (!isParentPseudoConflictFromOtherSide(diff, parentNode.getData())) {
					TreeNode wrap = wrap(diff);
					parentNode.getChildren().add(wrap);
					if (isContainment(diff)) {
						final Match diffMatch = ComparisonUtil.getComparison(diff).getMatch(
								((ReferenceChange)diff).getValue());
						buildSubTree(wrap, conflict, diffMatch);
					}
				}
			}
			for (Match subMatch : match.getSubmatches()) {
				if (!isMatchOfConflictContainmentDiff(conflict, subMatch)) {
					buildSubTree(parentNode, conflict, subMatch);
				}
			}

		}

		/**
		 * Checks if the parent of the given diff is a pseudo conflict with a different side than the given
		 * diff.
		 * 
		 * @param diff
		 *            the given diff.
		 * @param parent
		 *            the parent of the given duff.
		 * @return true if the parent of the given diff is a pseudo conflict with a different side than the
		 *         given diff, false otherwise.
		 */
		private boolean isParentPseudoConflictFromOtherSide(Diff diff, EObject parent) {
			boolean ret = false;
			if (parent instanceof Diff) {
				Conflict conflict = ((Diff)parent).getConflict();
				if (conflict != null) {
					if (ConflictKind.PSEUDO == conflict.getKind()) {
						ret = !fromSide(((Diff)parent).getSource()).apply(diff);
					}
				}
			}
			return ret;
		}

		/**
		 * Checks if the given match is a match of one of the containment diffs of the given Conflict.
		 * 
		 * @param conflict
		 *            the given conflict.
		 * @param subMatch
		 *            the given match.
		 * @return true, if the given match is a match of one of the containment diffs of the given Conflict,
		 *         false otherwise.
		 */
		protected boolean isMatchOfConflictContainmentDiff(Conflict conflict, Match subMatch) {
			if (subMatch != null) {
				for (Diff diff : conflict.getDifferences()) {
					if (isContainment(diff)) {
						final Match realMatch = diff.getMatch().getComparison().getMatch(
								((ReferenceChange)diff).getValue());
						if (subMatch.equals(realMatch)) {
							return true;
						}
					}
				}
			}
			return false;
		}

		/**
		 * Checks whether the given diff corresponds to a containment change. This holds true for differences
		 * on containment references' values.
		 * 
		 * @param diff
		 *            The diff to consider.
		 * @return <code>true</code> if the given {@code diff} is to be considered a containment change,
		 *         <code>false</code> otherwise.
		 */
		private boolean isContainment(Diff diff) {
			return diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.AbstractBuildingDifferenceGroupProvider#buildGroups(org.eclipse.emf.compare.Comparison)
	 */
	@Override
	protected Collection<? extends IDifferenceGroup> buildGroups(Comparison comparison2) {
		Adapter adapter = EcoreUtil.getAdapter(getComparison().eAdapters(), SideLabelProvider.class);

		final String leftLabel, rightLabel;
		if (adapter instanceof SideLabelProvider) {
			SideLabelProvider labelProvider = (SideLabelProvider)adapter;
			leftLabel = labelProvider.getLeftLabel();
			rightLabel = labelProvider.getRightLabel();
		} else {
			leftLabel = EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.left.label"); //$NON-NLS-1$
			rightLabel = EMFCompareRCPUIMessages.getString("ThreeWayComparisonGroupProvider.right.label"); //$NON-NLS-1$
		}

		final BasicDifferenceGroupImpl conflicts = new ConflictsGroupImpl(getComparison(), hasConflict(
				ConflictKind.REAL, ConflictKind.PSEUDO), EMFCompareRCPUIMessages
				.getString("ThreeWayComparisonGroupProvider.conflicts.label"), getCrossReferenceAdapter()); //$NON-NLS-1$
		conflicts.buildSubTree();

		final BasicDifferenceGroupImpl leftSide = new BasicDifferenceGroupImpl(getComparison(), Predicates
				.and(fromSide(DifferenceSource.LEFT), Predicates.not(hasConflict(ConflictKind.REAL,
						ConflictKind.PSEUDO))), leftLabel, getCrossReferenceAdapter());
		leftSide.buildSubTree();

		final BasicDifferenceGroupImpl rightSide = new BasicDifferenceGroupImpl(getComparison(), Predicates
				.and(fromSide(DifferenceSource.RIGHT), Predicates.not(hasConflict(ConflictKind.REAL,
						ConflictKind.PSEUDO))), rightLabel, getCrossReferenceAdapter());
		rightSide.buildSubTree();

		return ImmutableList.of(conflicts, leftSide, rightSide);
	}
}
