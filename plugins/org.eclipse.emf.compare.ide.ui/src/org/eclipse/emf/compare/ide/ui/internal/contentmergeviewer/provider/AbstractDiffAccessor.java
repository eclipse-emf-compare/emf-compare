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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractDiffAccessor implements IStructuralFeatureAccessor, ITypedElement {

	private final Diff fDiff;

	private final MergeViewerSide fSide;

	private final ImmutableList<Diff> fDiffFromThisSide;

	private final ImmutableList<Diff> fDiffFromTheOtherSide;

	private final ImmutableList<Diff> fDiffFromAncestor;

	public AbstractDiffAccessor(Diff diff, MergeViewerSide side) {
		fDiff = diff;
		fSide = side;

		List<Diff> siblingDifferences = diff.getMatch().getDifferences();
		ImmutableList.Builder<Diff> thisSide = ImmutableList.builder();
		ImmutableList.Builder<Diff> otherSide = ImmutableList.builder();
		ImmutableList.Builder<Diff> ancestorSide = ImmutableList.builder();
		fillSiblingDifferencesLists(siblingDifferences, thisSide, otherSide, ancestorSide);
		fDiffFromThisSide = thisSide.build();
		fDiffFromTheOtherSide = otherSide.build();
		fDiffFromAncestor = ancestorSide.build();
	}

	private EObject getEObject(final Diff diff) {
		final Match match = diff.getMatch();
		final EObject eObject;
		switch (fSide) {
			case ANCESTOR:
				eObject = match.getOrigin();
				break;
			case LEFT:
				eObject = match.getLeft();
				break;
			case RIGHT:
				eObject = match.getRight();
				break;
			default:
				throw new IllegalStateException();
		}
		return eObject;
	}

	/**
	 * @param siblingDifferences
	 * @param thisSide
	 * @param otherSide
	 */
	private void fillSiblingDifferencesLists(List<Diff> siblingDifferences,
			ImmutableList.Builder<Diff> thisSide, ImmutableList.Builder<Diff> otherSide,
			ImmutableList.Builder<Diff> ancestorSide) {
		for (Diff siblingDiff : siblingDifferences) {
			final EStructuralFeature feature = getEStructuralFeature(siblingDiff);
			if (feature == getEStructuralFeature()) {
				switch (siblingDiff.getKind()) {
					case ADD:
					case CHANGE:
					case MOVE:
						addToSideList(siblingDiff, thisSide, otherSide, ancestorSide);
						break;
					case DELETE:
						addToSideList(siblingDiff, otherSide, thisSide, ancestorSide);
						break;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getEStructuralFeature()
	 */
	public EStructuralFeature getEStructuralFeature() {
		return getEStructuralFeature(fDiff);
	}

	public Diff getDiff() {
		return fDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getValue()
	 */
	public Object getValue() {
		return getValue(fDiff);
	}

	private EStructuralFeature getEStructuralFeature(Diff siblingDiff) {
		final EStructuralFeature feature;
		if (siblingDiff instanceof ReferenceChange) {
			feature = ((ReferenceChange)siblingDiff).getReference();
		} else if (siblingDiff instanceof AttributeChange) {
			feature = ((AttributeChange)siblingDiff).getAttribute();
		} else {
			feature = null;
		}
		return feature;
	}

	/**
	 * Add the given <code>referenceChange</code> to the <code>boxSide</code> if it must be paint as a box or
	 * to <code>lineSide</code> if it must be paint as a line.
	 * 
	 * @param diff
	 * @param boxSide
	 * @param lineSide
	 */
	private void addToSideList(Diff diff, ImmutableList.Builder<Diff> boxSide,
			ImmutableList.Builder<Diff> lineSide, ImmutableList.Builder<Diff> ancestorSide) {
		final DifferenceSource source = diff.getSource();
		if (source == DifferenceSource.LEFT && fSide == MergeViewerSide.LEFT) {
			boxSide.add(diff);
		} else if (source == DifferenceSource.RIGHT && fSide == MergeViewerSide.RIGHT) {
			boxSide.add(diff);
		} else if (fSide != MergeViewerSide.ANCESTOR) {
			lineSide.add(diff);
		} else {
			ancestorSide.add(diff);
		}
	}

	/**
	 * @return the fSide
	 */
	protected final MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getValues()
	 */
	public List<?> getValues() {
		EObject eObject = getEObject(fDiff);
		if (eObject != null) {
			EStructuralFeature eStructuralFeature = getEStructuralFeature(fDiff);
			Object value = eObject.eGet(eStructuralFeature);
			if (eStructuralFeature.isMany()) {
				return (List<?>)value;
			} else {
				return ImmutableList.of(value);
			}
		} else {
			return ImmutableList.of();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getDiffFromThisSide()
	 */
	public ImmutableList<Diff> getDiffFromThisSide() {
		return fDiffFromThisSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getDiffFromTheOtherSide()
	 */
	public ImmutableList<Diff> getDiffFromTheOtherSide() {
		return fDiffFromTheOtherSide;
	}

	/**
	 * @return the fReferenceChangesFromAncestor
	 */
	public ImmutableList<Diff> getDiffFromAncestor() {
		return fDiffFromAncestor;
	}
}
