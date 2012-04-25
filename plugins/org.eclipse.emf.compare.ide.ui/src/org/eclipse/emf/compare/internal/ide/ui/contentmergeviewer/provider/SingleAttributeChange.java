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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class SingleAttributeChange implements ITypedElement, IStreamContentAccessor {

	private final EObject fEObject;

	private final EAttribute eAtribute;

	/**
	 * 
	 */
	public SingleAttributeChange(EObject eObject, EAttribute eAtribute) {
		this.fEObject = eObject;
		this.eAtribute = eAtribute;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return this.eAtribute.getName();
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return ExtendedImageRegistry.getInstance().getImage(
				EcoreEditPlugin.getPlugin().getImage("full/obj16/EAttribute"));
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return ITypedElement.TEXT_TYPE;
	}

	/**
	 * @{inheritDoc
	 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(((String)fEObject.eGet(eAtribute)).getBytes());
	}

}
