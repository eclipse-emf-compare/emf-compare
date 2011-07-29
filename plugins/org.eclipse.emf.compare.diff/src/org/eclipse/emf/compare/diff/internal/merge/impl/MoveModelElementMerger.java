/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for a {@link MoveModelElement} operation.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
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
		final MoveModelElement theDiff = (MoveModelElement)this.diff;
		final EObject leftTarget = theDiff.getLeftTarget();
		final EObject leftElement = theDiff.getLeftElement();
		final EReference ref = theDiff.getRightElement().eContainmentFeature();
		if (ref != null) {
			// ordering handling:
			int index = -1;
			final EObject rightElementParent = theDiff.getRightElement().eContainer();
			final Object rightRefValue = rightElementParent.eGet(ref);
			if (rightRefValue instanceof List) {
				final List<Object> refRightValueList = (List<Object>)rightRefValue;
				index = refRightValueList.indexOf(theDiff.getRightElement());
			}

			try {
				// We'll store the element's ID because moving an element deletes its XMI ID
				final String elementID = getXMIID(leftElement);
				EcoreUtil.remove(leftElement);
				EFactory.eAdd(leftTarget, ref.getName(), leftElement, index);
				// Sets anew the element's ID
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
	@SuppressWarnings("unchecked")
	@Override
	public void undoInTarget() {
		final MoveModelElement theDiff = (MoveModelElement)this.diff;
		final EObject rightTarget = theDiff.getRightTarget();
		final EObject rightElement = theDiff.getRightElement();
		final EReference ref = theDiff.getLeftElement().eContainmentFeature();
		if (ref != null) {
			// ordering handling:
			int index = -1;
			final EObject leftElementParent = theDiff.getLeftElement().eContainer();
			final Object leftRefValue = leftElementParent.eGet(ref);
			if (leftRefValue instanceof List) {
				final List<Object> refLeftValueList = (List<Object>)leftRefValue;
				index = refLeftValueList.indexOf(theDiff.getLeftElement());
			}

			try {
				final String elementID = getXMIID(rightElement);
				EcoreUtil.remove(rightElement);
				EFactory.eAdd(rightTarget, ref.getName(), rightElement, index);
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
