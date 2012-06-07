/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This defines the semantics of an EObject copy in the case of EMF Compare.
 * <p>
 * Namely, references now have their own Diffs, they will be merged separately from the EObject copy. However,
 * attributes must be copied from the start.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareCopier extends EcoreUtil.Copier {
	/** Generated SUID. */
	private static final long serialVersionUID = 4729702753308688732L;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation mostly copied from Copier#copy(EObject), we only remove the reference copying from here.
	 * </p>
	 * 
	 * @see org.eclipse.emf.ecore.util.EcoreUtil.Copier#copy(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public EObject copy(EObject eObject) {
		if (eObject == null) {
			return null;
		}

		final EObject copyEObject = createCopy(eObject);
		put(eObject, copyEObject);

		final EClass eClass = eObject.eClass();
		final int size = eClass.getFeatureCount();
		for (int i = 0; i < size; ++i) {
			final EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(i);
			if (eStructuralFeature instanceof EAttribute && eStructuralFeature.isChangeable()
					&& !eStructuralFeature.isDerived()) {
				copyAttribute((EAttribute)eStructuralFeature, eObject, copyEObject);
			}
		}

		copyProxyURI(eObject, copyEObject);

		return copyEObject;
	}
}
