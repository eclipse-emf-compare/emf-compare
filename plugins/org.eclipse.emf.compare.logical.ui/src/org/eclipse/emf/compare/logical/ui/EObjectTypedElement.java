/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * This implementation of an {@link ITypedElement} will allow us to wrap an EObject.
 * <p>
 * Implementing {@link IStreamContentAccessor} allows us to bypass bug 293926 so that our viewers are properly
 * instantiated from the synchronize view's navigator.
 * </p>
 * <p>
 * Implementing {@link IEditableContent} allows us to trick org.eclipse.compare into thinking that we are
 * editable, thus enabling the "copy" actions in the content viewer.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EObjectTypedElement implements ITypedElement, IStreamContentAccessor, IEditableContent {
	/** This will be used as the type of our wrappers in order to determine the structure and content viewers. */
	public static final String EMF_TYPE = "EMF.TYPE"; //$NON-NLS-1$

	/** Name of the wrapped EObject. */
	private final String name;

	/** Icon of the wrapped EObject. */
	private final Image image;

	/** <code>true</code> if the underlying object is in a local resource, <code>false</code> otherwise. */
	private final boolean isEditable;

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
		this.name = labelProvider.getText(eObject);
		if (Display.getCurrent() != null)
			this.image = labelProvider.getImage(eObject);
		else
			this.image = null;
		Resource resource = eObject.eResource();
		if (resource != null && resource.getURI() != null) {
			isEditable = !EMFResourceMapping.REMOTE_RESOURCE_SCHEME.equals(eObject.eResource().getURI()
					.scheme());
		} else {
			isEditable = false;
		}
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IEditableContent#isEditable()
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IEditableContent#replace(org.eclipse.compare.ITypedElement,
	 *      org.eclipse.compare.ITypedElement)
	 */
	public ITypedElement replace(ITypedElement dest, ITypedElement src) {
		return dest;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.IEditableContent#setContent(byte[])
	 */
	public void setContent(byte[] newContent) {
		// TODO can we use this instead of our own save?
	}
}
