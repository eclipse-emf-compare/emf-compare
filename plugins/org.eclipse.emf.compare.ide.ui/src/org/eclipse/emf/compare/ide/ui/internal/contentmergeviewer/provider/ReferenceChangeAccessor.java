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
		final Match match = diff.getMatch();
		final Match matchOfValue = match.getComparison().getMatch(((ReferenceChange)diff).getValue());
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
