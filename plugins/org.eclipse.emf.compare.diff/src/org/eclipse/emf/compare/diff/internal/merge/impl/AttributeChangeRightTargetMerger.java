/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

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
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class AttributeChangeRightTargetMerger extends DefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final AttributeChangeRightTarget theDiff = (AttributeChangeRightTarget)this.diff;
		final EObject origin = theDiff.getLeftElement();
		final Object value = theDiff.getRightTarget();
		final EAttribute attr = theDiff.getAttribute();
		try {
			int valueIndex = -1;
			if (attr.isMany()) {
				final EObject rightElement = theDiff.getRightElement();
				final Object rightValues = rightElement.eGet(attr);
				if (rightValues instanceof List) {
					final List rightValuesList = (List)rightValues;
					valueIndex = rightValuesList.indexOf(value);
				}
			}
			EFactory.eAdd(origin, attr.getName(), value, valueIndex);
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
		final Object value = theDiff.getRightTarget();
		final EAttribute attr = theDiff.getAttribute();
		try {
			EFactory.eRemove(target, attr.getName(), value);
		} catch (FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		super.undoInTarget();
	}
}
