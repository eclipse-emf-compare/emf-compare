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
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for an {@link AttributeChangeRightTarget} operation.<br/>
 * <p>
 * Are considered for this merger :
 * <ul>
 * <li>{@link AddAttribute}</li>
 * <li>{@link RemoteRemoveAttribute}</li>
 * </ul>
 * </p>
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class AttributeChangeRightTargetMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void applyInOrigin() {
		final AttributeChangeRightTarget theDiff = (AttributeChangeRightTarget)this.diff;
		final EObject origin = theDiff.getLeftElement();
		final EObject element = theDiff.getRightTarget();
		final EObject newOne = EcoreUtil.copy(element);
		final EAttribute attr = theDiff.getAttribute();
		try {
			EFactory.eAdd(origin, attr.getName(), newOne);
			copyXMIID(element, newOne);
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
		final AttributeChangeRightTarget theDiff = (AttributeChangeRightTarget)this.diff;
		final EObject target = theDiff.getRightElement();
		final EObject element = theDiff.getRightTarget();
		final EAttribute attr = theDiff.getAttribute();
		try {
			EFactory.eRemove(target, attr.getName(), element);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		super.undoInTarget();
	}
}
