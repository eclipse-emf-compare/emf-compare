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

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link UpdateUniqueReferenceValue} operation.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class UpdateUniqueReferenceValueMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final UpdateUniqueReferenceValue diff = (UpdateUniqueReferenceValue)this.diff;
		final EObject element = diff.getLeftElement();
		final EObject leftTarget = diff.getLeftTarget();
		try {
			EFactory.eSet(element, diff.getReference().getName(), leftTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
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
		final UpdateUniqueReferenceValue diff = (UpdateUniqueReferenceValue)this.diff;
		final EObject element = diff.getRightElement();
		final EObject rightTarget = diff.getRightTarget();
		try {
			EFactory.eSet(element, diff.getReference().getName(), rightTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		super.undoInTarget();
	}
}
