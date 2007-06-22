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
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link AddReferenceValue}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class AddReferenceValueMerger extends DefaultMerger {
	/**
	 * Constructs a merger for an {@link AddReferenceValue} operation.
	 * 
	 * @param element
	 *            The element for which we create the merger.
	 */
	public AddReferenceValueMerger(DiffElement element) {
		super(element);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final AddReferenceValue diff = (AddReferenceValue)this.diff;
		final EObject element = diff.getLeftElement();
		if (canApplyInOrigin()) {
			final Iterator oldTarget = diff.getLeftAddedTarget().iterator();
			while (oldTarget.hasNext()) {
				try {
					EFactory.eAdd(element, (diff.getReference()).getName(), oldTarget.next());
				} catch (FactoryException e) {
					EMFComparePlugin.getDefault().log(e, true);
				}
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
		final AddReferenceValue diff = (AddReferenceValue)this.diff;
		final EObject element = diff.getRightElement();
		// Iterator newTarget = getReferencesTargets().iterator();
		final Iterator newTarget = diff.getRightAddedTarget().iterator();
		while (newTarget.hasNext()) {
			try {
				EFactory.eRemove(element, (diff.getReference()).getName(), newTarget.next());
			} catch (FactoryException e) {
				EMFComparePlugin.getDefault().log(e, true);
			}
		}
		super.undoInTarget();
	}
}
