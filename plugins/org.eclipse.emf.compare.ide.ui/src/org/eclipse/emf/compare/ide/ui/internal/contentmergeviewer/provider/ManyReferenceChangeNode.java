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

import static com.google.common.collect.Iterables.filter;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ManyReferenceChangeNode implements ITypedElement, IManyStructuralFeatureAccessor<EObject> {

	private final MergeViewerSide fSide;

	private final EReference fEReference;

	private final ImmutableList<ReferenceChange> fReferenceChangesFromThisSide;

	private final ImmutableList<ReferenceChange> fReferenceChangesFromTheOtherSide;

	private final ImmutableList<ReferenceChange> fReferenceChangesFromAncestor;

	private EObject fEObject;

	private EObject fValue;

	/**
	 * 
	 */
	public ManyReferenceChangeNode(ReferenceChange referenceChange, MergeViewerSide side) {
		fSide = side;
		fEReference = referenceChange.getReference();
		fEObject = getEObject(referenceChange);
		fValue = getValue(referenceChange);

		EList<Diff> siblingDifferences = referenceChange.getMatch().getDifferences();
		ImmutableList.Builder<ReferenceChange> thisSide = ImmutableList.builder();
		ImmutableList.Builder<ReferenceChange> otherSide = ImmutableList.builder();
		ImmutableList.Builder<ReferenceChange> ancestorSide = ImmutableList.builder();
		fillSiblingDifferencesLists(siblingDifferences, thisSide, otherSide, ancestorSide);
		fReferenceChangesFromThisSide = thisSide.build();
		fReferenceChangesFromTheOtherSide = otherSide.build();
		fReferenceChangesFromAncestor = ancestorSide.build();
	}

	/**
	 * @param siblingDifferences
	 * @param thisSide
	 * @param otherSide
	 */
	private void fillSiblingDifferencesLists(EList<Diff> siblingDifferences,
			ImmutableList.Builder<ReferenceChange> thisSide,
			ImmutableList.Builder<ReferenceChange> otherSide,
			ImmutableList.Builder<ReferenceChange> ancestorSide) {
		for (ReferenceChange referenceChange : filter(siblingDifferences, ReferenceChange.class)) {
			if (referenceChange.getReference() == fEReference) {
				switch (referenceChange.getKind()) {
					case ADD:
					case CHANGE:
					case MOVE:
						addToSideList(referenceChange, thisSide, otherSide, ancestorSide);
						break;
					case DELETE:
						addToSideList(referenceChange, otherSide, thisSide, ancestorSide);
						break;
				}
			}
		}
	}

	/**
	 * Add the given <code>referenceChange</code> to the <code>boxSide</code> if it must be paint as a box or
	 * to <code>lineSide</code> if it must be paint as a line.
	 * 
	 * @param referenceChange
	 * @param boxSide
	 * @param lineSide
	 */
	private void addToSideList(ReferenceChange referenceChange,
			ImmutableList.Builder<ReferenceChange> boxSide, ImmutableList.Builder<ReferenceChange> lineSide,
			ImmutableList.Builder<ReferenceChange> ancestorSide) {
		final DifferenceSource source = referenceChange.getSource();
		if (source == DifferenceSource.LEFT && fSide == MergeViewerSide.LEFT) {
			boxSide.add(referenceChange);
		} else if (source == DifferenceSource.RIGHT && fSide == MergeViewerSide.RIGHT) {
			boxSide.add(referenceChange);
		} else if (fSide != MergeViewerSide.ANCESTOR) {
			lineSide.add(referenceChange);
		} else {
			ancestorSide.add(referenceChange);
		}
	}

	public EObject getValue(final ReferenceChange referenceChange) {
		final Match match = referenceChange.getMatch();
		final Match matchOfValue = match.getComparison().getMatch(referenceChange.getValue());
		final EObject value;
		if (matchOfValue != null) {
			switch (fSide) {
				case ANCESTOR:
					value = matchOfValue.getOrigin();
					break;
				case LEFT:
					value = matchOfValue.getLeft();
					break;
				case RIGHT:
					value = matchOfValue.getRight();
					break;
				default:
					value = null;
					throw new IllegalStateException("Can not have other side than ancestor, left or right");
			}
		} else {
			value = null;
		}
		return value;
	}

	private EObject getEObject(final ReferenceChange referenceChange) {
		final Match match = referenceChange.getMatch();
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
				eObject = null;
				throw new IllegalStateException("Can not have other side than ancestor, left or right");
		}
		return eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return ManyReferenceChangeNode.class.getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return ContentMergeViewerConstants.REFERENCE_CHANGE_NODE_TYPE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor#getValues()
	 */
	@SuppressWarnings("unchecked")
	public List<EObject> getValues() {
		if (fEObject != null) {
			Object value = fEObject.eGet(fEReference);
			if (fEReference.isMany()) {
				return (List<EObject>)value;
			} else {
				return ImmutableList.of((EObject)value);
			}
		} else {
			return ImmutableList.of();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor#getValue()
	 */
	public EObject getValue() {
		return fValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor#getDiffFromThisSide()
	 */
	public ImmutableList<ReferenceChange> getDiffFromThisSide() {
		return fReferenceChangesFromThisSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IManyStructuralFeatureAccessor#getDiffFromTheOtherSide()
	 */
	public ImmutableList<ReferenceChange> getDiffFromTheOtherSide() {
		return fReferenceChangesFromTheOtherSide;
	}

	/**
	 * @return the fReferenceChangesFromAncestor
	 */
	public ImmutableList<ReferenceChange> getDiffFromAncestor() {
		return fReferenceChangesFromAncestor;
	}
}
