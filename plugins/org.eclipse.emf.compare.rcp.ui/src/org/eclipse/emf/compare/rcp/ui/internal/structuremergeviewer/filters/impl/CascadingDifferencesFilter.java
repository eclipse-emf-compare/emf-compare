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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.or;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceSource.RIGHT;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.AbstractDifferenceFilter;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * A filter used by default that filtered out cascading differences (differences located under differences,
 * also known as sub differences). The MOVE differences are not hidden by this filter.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 4.0
 */
public class CascadingDifferencesFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> PREDICATE_WHEN_SELECTED = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			boolean ret = false;
			if (input instanceof TreeNode) {
				TreeNode treeNode = (TreeNode)input;
				EObject data = treeNode.getData();
				if (data instanceof Diff && !(data instanceof ResourceAttachmentChange)) {
					Diff diff = (Diff)data;
					if (diff.getKind() != MOVE && (diff.getConflict() == null)) {
						TreeNode parent = treeNode.getParent();
						if (parent != null && parent.getData() instanceof Match) {
							Match match = (Match)parent.getData();
							if (match.eContainer() instanceof Match) {
								Match parentMatch = (Match)match.eContainer();
								if (isAddOrDeleteMatch(parentMatch, diff.getSource())) {
									ret = true;
								} else if (isAddOrDeleteMatch(match, diff.getSource())) {
									ret = !and(or(CONTAINMENT_REFERENCE_CHANGE,
											REFINED_BY_CONTAINMENT_REF_CHANGE), ofKind(ADD, DELETE))
													.apply(diff);
								}
							} else if (isAddOrDeleteMatch(match, diff.getSource())) {
								ret = !and(
										or(CONTAINMENT_REFERENCE_CHANGE, REFINED_BY_CONTAINMENT_REF_CHANGE),
										ofKind(ADD, DELETE)).apply(diff);
							}
						}
					}
				}
			}
			return ret;
		}

		/**
		 * Indicate whether a Match is that of an object that was either added or deleted on the given side.
		 * 
		 * @param match
		 *            The match
		 * @param side
		 *            The side
		 * @return <code>true</code> if the matched object is present on side but not on origin or vice-versa.
		 */
		protected boolean isAddOrDeleteMatch(Match match, DifferenceSource side) {
			if (match.getComparison().isThreeWay()) {
				return (MatchUtil.getMatchedObject(match, side) == null) != (match.getOrigin() == null);
			}
			return (MatchUtil.getMatchedObject(match, side) == null) != (MatchUtil.getMatchedObject(match,
					opposite(side)) == null);
		}

		protected DifferenceSource opposite(DifferenceSource side) {
			switch (side) {
				case LEFT:
					return RIGHT;
				case RIGHT:
					return LEFT;
				default:
					throw new IllegalArgumentException("Source value not supported: " + side); //$NON-NLS-1$
			}
		}
	};

	private static final Predicate<Diff> REFINED_BY_CONTAINMENT_REF_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input.getPrimeRefining() != null
					&& CONTAINMENT_REFERENCE_CHANGE.apply(input.getPrimeRefining());
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenSelected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return PREDICATE_WHEN_SELECTED;
	}
}
