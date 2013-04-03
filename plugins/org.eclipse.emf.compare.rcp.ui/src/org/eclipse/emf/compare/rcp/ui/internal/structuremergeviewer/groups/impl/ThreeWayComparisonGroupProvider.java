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

import static com.google.common.base.Predicates.and;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * This implementation of a {@link IDifferenceGroupProvider} will be used to group the differences by their
 * {@link DifferenceSource side} : left, right and conflicts.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class ThreeWayComparisonGroupProvider implements IDifferenceGroupProvider {

	/** A human-readable label for this group provider. This will be displayed in the EMF Compare UI. */
	private String label;

	/** The initial activation state of the group provider. */
	private boolean activeByDefault;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Iterable<? extends IDifferenceGroup> getGroups(Comparison comparison) {
		final List<Diff> diffs = comparison.getDifferences();

		final IDifferenceGroup leftSide = new BasicDifferenceGroupImpl(comparison, diffs, Predicates.and(
				fromSide(DifferenceSource.LEFT), Predicates.not(hasConflict(ConflictKind.REAL,
						ConflictKind.PSEUDO))), "Left side");
		final IDifferenceGroup rightSide = new BasicDifferenceGroupImpl(comparison, diffs, Predicates.and(
				fromSide(DifferenceSource.RIGHT), Predicates.not(hasConflict(ConflictKind.REAL,
						ConflictKind.PSEUDO))), "Right side");
		final IDifferenceGroup conflicts = new ConflictsGroupImpl(comparison, diffs, hasConflict(
				ConflictKind.REAL, ConflictKind.PSEUDO), "Conflicts");

		return ImmutableList.of(leftSide, rightSide, conflicts);
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
	public void setDefaultSelected(boolean active) {
		this.activeByDefault = active;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#isEnabled(org
	 *      .eclipse.emf.compare.scope.IComparisonScope, org.eclipse.emf.compare.Comparison)
	 */
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
	class ConflictsGroupImpl extends BasicDifferenceGroupImpl {

		/**
		 * {@inheritDoc}.
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl#BasicDifferenceGroupImpl(org.eclipse.emf.compare.Comparison,
		 *      java.lang.Iterable, com.google.common.base.Predicate, java.lang.String)
		 */
		public ConflictsGroupImpl(Comparison comparison, Iterable<? extends Diff> unfiltered,
				Predicate<? super Diff> filter, String name) {
			super(comparison, unfiltered, filter, name);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl#getStyledName()
		 */
		@Override
		public IComposedStyledString getStyledName() {
			final IStyledString.IComposedStyledString ret = new ComposedStyledString(getName());
			int unresolvedRealDiffs = Iterables.size(Iterables.filter(getDifferences(), and(
					hasState(DifferenceState.UNRESOLVED), hasConflict(ConflictKind.REAL))));
			int unresolvedPseudoDiffs = Iterables.size(Iterables.filter(getDifferences(), and(
					hasState(DifferenceState.UNRESOLVED), hasConflict(ConflictKind.PSEUDO))));
			ret.append(" [" + unresolvedRealDiffs + " real and " + unresolvedPseudoDiffs
					+ " pseudo unresolved difference", Style.DECORATIONS_STYLER);
			if (unresolvedRealDiffs + unresolvedPseudoDiffs > 1) {
				ret.append("s", Style.DECORATIONS_STYLER);
			}
			ret.append("]", Style.DECORATIONS_STYLER);
			return ret;
		}
	}

}
