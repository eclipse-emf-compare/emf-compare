/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.generic.merge.impl;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link RemoveReferenceValue}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class RemoveReferenceValueMerger extends DefaultMerger {
	/**
	 * Constructs a merger for an {@link RemoveReferenceValue} operation.
	 * 
	 * @param element
	 *            The element for which we create the merger.
	 */
	public RemoveReferenceValueMerger(DiffElement element) {
		super(element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final RemoveReferenceValue diff = (RemoveReferenceValue)this.diff;
		final EObject element = diff.getLeftElement();
		// Iterator oldTarget = getReferencesOrigins().iterator();
		final Iterator oldTarget = diff.getLeftRemovedTarget().iterator();
		while (oldTarget.hasNext()) {
			try {
				EFactory.eRemove(element, (diff.getReference()).getName(), oldTarget.next());
			} catch (FactoryException e) {
				EMFComparePlugin.getDefault().log(e, true);
			}
		}
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final RemoveReferenceValue diff = (RemoveReferenceValue)this.diff;
		final EObject element = diff.getRightElement();
		if (canUndoInTarget()) {
			final Iterator newTarget = diff.getRightRemovedTarget().iterator();
			while (newTarget.hasNext()) {
				try {
					EFactory.eAdd(element, (diff.getReference()).getName(), newTarget.next());
				} catch (FactoryException e) {
					EMFComparePlugin.getDefault().log(e, true);
				}
			}
			super.undoInTarget();
		}
	}
}
