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
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for a {@link MoveModelElement} operation.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class MoveModelElementMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void applyInOrigin() {
		final MoveModelElement aDiff = (MoveModelElement)this.diff;
		final EObject leftTarget = aDiff.getLeftTarget();
		final EObject leftElement = aDiff.getLeftElement();
		final EReference ref = aDiff.getRightElement().eContainmentFeature();
		if (ref != null) {
			try {
				// We'll store the element's ID because moving an element deletes its XMI ID
				final String elementID = getXMIID(leftElement);
				EcoreUtil.remove(leftElement);
				EFactory.eAdd(leftTarget, ref.getName(), leftElement);
				setXMIID(leftElement, elementID);
			} catch (FactoryException e) {
				EMFComparePlugin.log(e, true);
			}
		} else {
			// shouldn't be here
			assert false;
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
		final MoveModelElement aDiff = (MoveModelElement)this.diff;
		final EObject rightTarget = aDiff.getRightTarget();
		final EObject rightElement = aDiff.getRightElement();
		final EReference ref = aDiff.getLeftElement().eContainmentFeature();
		if (ref != null) {
			try {
				final String elementID = getXMIID(rightElement);
				EcoreUtil.remove(rightElement);
				EFactory.eAdd(rightTarget, ref.getName(), rightElement);
				setXMIID(rightElement, elementID);
			} catch (FactoryException e) {
				EMFComparePlugin.log(e, true);
			}
		} else {
			// shouldn't be here
			assert false;
		}
		super.applyInOrigin();
	}
}
