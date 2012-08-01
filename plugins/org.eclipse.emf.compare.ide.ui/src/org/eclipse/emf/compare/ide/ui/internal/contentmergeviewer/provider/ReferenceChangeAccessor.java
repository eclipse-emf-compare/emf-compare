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

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ReferenceChangeAccessor extends AbstractDiffAccessor {

	/**
	 * 
	 */
	public ReferenceChangeAccessor(ReferenceChange referenceChange, MergeViewerSide side) {
		super(referenceChange, side);
	}

	public EObject getValue(final Diff diff) {
		final Match matchOfValue = getMatch(diff);
		final EObject value;
		if (matchOfValue != null) {
			switch (getSide()) {
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
					throw new IllegalStateException();
			}
		} else {
			value = null;
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getMatch()
	 */
	public Match getMatch() {
		return getMatch(getDiff());
	}

	private Match getMatch(final Diff diff) {
		final Match match = diff.getMatch();
		final Match matchOfValue = match.getComparison().getMatch(((ReferenceChange)diff).getValue());
		return matchOfValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getDiff(java.lang.Object)
	 */
	public Diff getDiff(Object value, MergeViewerSide side) {
		Diff ret = null;
		Iterable<ReferenceChange> referencesChanges = filter(getDiffFromThisSide(), ReferenceChange.class);
		for (ReferenceChange referenceChange : referencesChanges) {
			final EObject referenceChangeValue = referenceChange.getValue();
			final Match match = referenceChange.getMatch().getComparison().getMatch(referenceChangeValue);
			final EObject matchValue;
			switch (side) {
				case LEFT:
					matchValue = match.getLeft();
					break;
				case RIGHT:
					matchValue = match.getRight();
					break;
				default:
					matchValue = null;
					break;
			}
			if (matchValue == value) {
				ret = referenceChange;
				break;
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return ReferenceChangeAccessor.class.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return ExtendedImageRegistry.getInstance().getImage(
				EcoreEditPlugin.getPlugin().getImage("full/obj16/EReference")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return ContentMergeViewerConstants.REFERENCE_CHANGE_NODE_TYPE;
	}

}
