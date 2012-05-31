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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ReferenceChangeNode implements ITypedElement {

	/**
	 * The EObject to get the value of the EReference from.
	 */
	private final EObject fEObject;

	/**
	 * The EReference to retrieve from the wrapped EObject.
	 */
	private final EReference fEReference;

	/**
	 * 
	 */
	public ReferenceChangeNode(EObject eObject, EReference eReference) {
		fEObject = eObject;
		fEReference = eReference;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return ReferenceChangeNode.class.getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return ContentMergeViewerConstants.REFERENCE_CHANGE_NODE_TYPE;
	}

}
