/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.internal.impl;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.compare.util.EFactory;
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
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final UpdateUniqueReferenceValue theDiff = (UpdateUniqueReferenceValue)this.diff;
		final EObject element = theDiff.getLeftElement();
		final EObject leftTarget = theDiff.getLeftTarget();
		try {
			EFactory.eSet(element, theDiff.getReference().getName(), leftTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final UpdateUniqueReferenceValue theDiff = (UpdateUniqueReferenceValue)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject rightTarget = theDiff.getRightTarget();
		try {
			EFactory.eSet(element, theDiff.getReference().getName(), rightTarget);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		super.undoInTarget();
	}
}
