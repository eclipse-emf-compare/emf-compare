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
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link UpdateAttribute}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class UpdateAttributeMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		/*
		 * FIXME [bug #209521] if we're merging an attribute pointing to an UnmatchedElement (namely, an added
		 * or remotely deleted datatype), we should merge its corresponding AddModelElement (or
		 * RemoteDeleteModelElement) beforehand. In the current state, we're doing a hard-link between the two
		 * models.
		 */
		final UpdateAttribute theDiff = (UpdateAttribute)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject origin = theDiff.getLeftElement();
		final EAttribute attr = theDiff.getAttribute();
		try {
			EFactory.eSet(origin, attr.getName(), EFactory.eGet(element, attr.getName()));
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
		/*
		 * FIXME [bug #209521] if we're merging an attribute pointing to an UnmatchedElement (namely, a
		 * deleted or remotely added datatype), we should merge its corresponding DeleteModelElement (or
		 * RemoteAddModelElement) beforehand. In the current state, we're doing a hard-link between the two
		 * models.
		 */
		final UpdateAttribute theDiff = (UpdateAttribute)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject origin = theDiff.getLeftElement();
		final EAttribute attr = theDiff.getAttribute();
		try {
			EFactory.eSet(element, attr.getName(), EFactory.eGet(origin, attr.getName()));
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		super.undoInTarget();
	}

}
