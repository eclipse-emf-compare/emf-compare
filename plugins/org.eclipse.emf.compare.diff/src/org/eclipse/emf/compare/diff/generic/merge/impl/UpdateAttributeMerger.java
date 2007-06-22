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
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * Merger for an {@link UpdateAttribute}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class UpdateAttributeMerger extends DefaultMerger {
	/**
	 * Constructs a merger for an {@link UpdateAttribute} operation.
	 * 
	 * @param element
	 *            The element for which we create the merger.
	 */
	public UpdateAttributeMerger(DiffElement element) {
		super(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final UpdateAttribute diff = (UpdateAttribute)this.diff;
		final EObject element = diff.getRightElement();
		final EObject origin = diff.getLeftElement();
		final EAttribute attr = diff.getAttribute();
		try {
			EFactory.eSet(origin, attr.getName(), EFactory.eGet(element, attr.getName()));
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
		final UpdateAttribute diff = (UpdateAttribute)this.diff;
		final EObject element = diff.getRightElement();
		final EObject origin = diff.getLeftElement();
		final EAttribute attr = diff.getAttribute();
		try {
			EFactory.eSet(element, attr.getName(), EFactory.eGet(origin, attr.getName()));
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, true);
		}
		super.undoInTarget();
	}

}
