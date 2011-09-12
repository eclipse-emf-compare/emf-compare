/*******************************************************************************
 * Copyright (c) 2009, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal.merge.impl;

import java.util.List;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for a {@link ReferenceOrderChange} operation.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ReferenceOrderChangeMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#doApplyInOrigin()
	 */
	@Override
	public void doApplyInOrigin() {
		final ReferenceOrderChange theDiff = (ReferenceOrderChange)this.diff;
		final EObject element = theDiff.getLeftElement();
		final List<EObject> leftTarget = theDiff.getLeftTarget();
		try {
			EFactory.eSet(element, theDiff.getReference().getName(), leftTarget);
		} catch (final FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#doUndoInTarget()
	 */
	@Override
	public void doUndoInTarget() {
		final ReferenceOrderChange theDiff = (ReferenceOrderChange)this.diff;
		final EObject element = theDiff.getRightElement();
		final List<EObject> rightTarget = theDiff.getRightTarget();
		try {
			EFactory.eSet(element, theDiff.getReference().getName(), rightTarget);
		} catch (final FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
	}
}
