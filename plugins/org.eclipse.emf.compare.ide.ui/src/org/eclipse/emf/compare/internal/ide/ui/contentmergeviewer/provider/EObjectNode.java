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
package org.eclipse.emf.compare.internal.ide.ui.contentmergeviewer.provider;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EObjectNode implements ITypedElement, IEObjectAccessor {

	private final EObject fEObject;

	/**
	 * 
	 */
	public EObjectNode(EObject eObject) {
		fEObject = eObject;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return "";
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return "eobject";
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.emf.compare.internal.ide.ui.contentmergeviewer.provider.IEObjectAccessor#getEObject()
	 */
	public EObject getEObject() {
		return fEObject;
	}
}
