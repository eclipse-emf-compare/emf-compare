/*******************************************************************************

 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.mergeviewer.accessor;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.onFeature;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewerItem;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class BasicStructuralFeatureAccessorImpl implements IStructuralFeatureAccessor {

	private final Diff fDiff;

	private final MergeViewerSide fSide;

	private final Match fOwnerMatch;

	private final EStructuralFeature fStructuralFeature;

	private final ImmutableList<Diff> fDifferences;

	public BasicStructuralFeatureAccessorImpl(Diff diff, MergeViewerSide side) {
		fDiff = diff;
		fSide = side;
		fOwnerMatch = diff.getMatch();
		fStructuralFeature = getAffectedFeature(diff);
		fDifferences = computeDifferences();
	}

	public Comparison getComparison() {
		return fOwnerMatch.getComparison();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getInitialItem()
	 */
	public IMergeViewerItem getInitialItem() {
		IMergeViewerItem ret = null;
		ImmutableList<? extends IMergeViewerItem> items = getItems();
		for (IMergeViewerItem item : items) {
			Diff diff = item.getDiff();
			if (diff == fDiff) {
				ret = item;
			}
		}
		return ret;
	}

	public EObject getEObject(MergeViewerSide side) {
		final EObject eObject;
		switch (side) {
			case ANCESTOR:
				eObject = fOwnerMatch.getOrigin();
				break;
			case LEFT:
				eObject = fOwnerMatch.getLeft();
				break;
			case RIGHT:
				eObject = fOwnerMatch.getRight();
				break;
			default:
				throw new IllegalStateException();
		}
		return eObject;
	}

	/**
	 * @return the fStructuralFeature
	 */
	public EStructuralFeature getStructuralFeature() {
		return fStructuralFeature;
	}

	/**
	 * @return the fSide
	 */
	protected final MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * @return the fDifferences
	 */
	protected final ImmutableList<Diff> getDifferences() {
		return fDifferences;
	}

	protected ImmutableList<Diff> computeDifferences() {
		List<Diff> siblingDifferences = fOwnerMatch.getDifferences();
		// We'll display all diffs on the same reference, excluding the pseudo conflicts.
		Predicate<? super Diff> diffFilter = and(onFeature(fStructuralFeature.getName()),
				not(hasConflict(ConflictKind.PSEUDO)));
		return ImmutableList.copyOf(filter(siblingDifferences, diffFilter));
	}

	/**
	 * Returns the structural feature affected by the given diff, if any.
	 * 
	 * @param diff
	 *            The diff from which we need to retrieve a feature.
	 * @return The feature affected by this {@code diff}, if any. <code>null</code> if none.
	 */
	protected EStructuralFeature getAffectedFeature(Diff diff) {
		final EStructuralFeature feature;
		if (diff instanceof ReferenceChange) {
			feature = ((ReferenceChange)diff).getReference();
		} else if (diff instanceof AttributeChange) {
			feature = ((AttributeChange)diff).getAttribute();
		} else {
			feature = null;
		}
		return feature;
	}

	/**
	 * @return the fDiff
	 */
	protected final Diff getInitialDiff() {
		return fDiff;
	}
}
