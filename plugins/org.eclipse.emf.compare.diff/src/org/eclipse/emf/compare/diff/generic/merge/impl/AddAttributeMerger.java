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
import org.eclipse.emf.compare.diff.metamodel.AddAttribute;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for an {@link AddAttribute} operation.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class AddAttributeMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void applyInOrigin() {
		final AddAttribute diff = (AddAttribute)this.diff;
		final EObject origin = diff.getLeftElement();
		final EObject element = diff.getRightTarget();
		final EObject newOne = EcoreUtil.copy(element);
		final EAttribute attr = diff.getAttribute();
		try {
			EFactory.eAdd(origin, attr.getName(), newOne);
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
		final AddAttribute diff = (AddAttribute)this.diff;
		final EObject target = diff.getRightElement();
		final EObject element = diff.getRightTarget();
		final EAttribute attr = diff.getAttribute();
		try {
			EFactory.eRemove(target, attr.getName(), element);
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		super.undoInTarget();
	}
}
