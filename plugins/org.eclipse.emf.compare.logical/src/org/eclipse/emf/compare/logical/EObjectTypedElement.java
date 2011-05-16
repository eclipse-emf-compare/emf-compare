/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * This implementation of an {@link ITypedElement} will allow us to wrap an EObject.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EObjectTypedElement implements ITypedElement, IStreamContentAccessor {
	/** This will be used as the type of our wrappers in order to determine the structure and content viewers. */
	public static final String EMF_TYPE = "EMF.TYPE"; //$NON-NLS-1$

	/** The wrapped EObject. */
	private final EObject eObject;

	/** Name of the wrapped EObject. */
	private final String name;

	/** Icon of the wrapped EObject. */
	private final Image image;

	/**
	 * Wraps the given <em>eObject</em> within a new {@link ITypedElement}, using the given
	 * {@link ILabelProvider} in order to determine this EObject's text and icon.
	 * 
	 * @param eObject
	 *            The EObject to wrap.
	 * @param labelProvider
	 *            Label provider for the <em>eObject</em>'s text and icon.
	 */
	public EObjectTypedElement(EObject eObject, ILabelProvider labelProvider) {
		this.eObject = eObject;
		this.name = labelProvider.getText(eObject);
		this.image = labelProvider.getImage(eObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		/*
		 * FIXME this method and its corresponding 'implements' have only been implemented here in order to
		 * work around bug 293926. Remove this as soon as this bug is fixed.
		 */
		return new ByteArrayInputStream(new byte[] {' '});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return EMF_TYPE;
	}
}
