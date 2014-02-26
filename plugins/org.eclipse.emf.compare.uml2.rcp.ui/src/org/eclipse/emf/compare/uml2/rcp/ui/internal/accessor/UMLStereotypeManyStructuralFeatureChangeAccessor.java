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
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Case of stereotype many structural feature (attribute/reference) changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class UMLStereotypeManyStructuralFeatureChangeAccessor extends ManyStructuralFeatureAccessorImpl {

	/**
	 * Creates a specialized accessor for the stereotype application differences.
	 * 
	 * @param adapterFactory
	 *            The adapter factory used by the accessor.
	 * @param diff
	 *            The diff for which we need an accessor.
	 * @param side
	 *            The side on which this accessor will be used.
	 */
	public UMLStereotypeManyStructuralFeatureChangeAccessor(AdapterFactory adapterFactory, UMLDiff diff,
			MergeViewerSide side) {
		super(adapterFactory, diff, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor#getAffectedFeature(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected ImmutableList<Diff> computeDifferences() {
		Match match = getInitialDiff().getMatch();
		List<Diff> siblingDifferences = match.getDifferences();
		EStructuralFeature affectedFeature = getAffectedFeature(getInitialDiff());
		return ImmutableList.copyOf(filter(siblingDifferences, onFeature(getStructuralFeature().getName(),
				affectedFeature)));
	}

	/**
	 * This can be used to check that a given {@code affectedFeature} matches the given {@code featureName}.
	 * 
	 * @param featureName
	 *            Name of the feature on which we expect a change.
	 * @param affectedFeature
	 *            The affected feature.
	 * @return The created predicate.
	 */
	private static Predicate<? super Diff> onFeature(final String featureName,
			final EStructuralFeature affectedFeature) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return featureName.equals(affectedFeature.getName());
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor#getAffectedFeature(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected EStructuralFeature getAffectedFeature(Diff diff) {
		return (EStructuralFeature)((UMLDiff)diff).getDiscriminant();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor.UMLStereotypeManyStructuralFeatureChangeAccessor#getEObject(org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	public EObject getEObject(MergeViewerSide side) {
		Diff refined = getInitialDiff().getRefinedBy().get(0);
		final EObject eObject;
		switch (side) {
			case ANCESTOR:
				eObject = refined.getMatch().getOrigin();
				break;
			case LEFT:
				eObject = refined.getMatch().getLeft();
				break;
			case RIGHT:
				eObject = refined.getMatch().getRight();
				break;
			default:
				throw new IllegalStateException();
		}
		return eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl#getValueFromDiff(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide)
	 */
	@Override
	protected Object getValueFromDiff(Diff diff, MergeViewerSide side) {
		Diff refined = diff.getRefinedBy().get(0);
		return super.getValueFromDiff(refined, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.ManyStructuralFeatureAccessorImpl#findInsertionIndex(org.eclipse.emf.compare.Diff,
	 *      boolean)
	 */
	@Override
	protected int findInsertionIndex(Diff diff, boolean rightToLeft) {
		Diff refined = diff.getRefinedBy().get(0);
		return super.findInsertionIndex(refined, rightToLeft);
	}
}
