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

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.graphics.Image;

/**
 * This implementation of an {@link ITypedElement} will allow us to wrap an EMF {@link ResourceSet}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class ResourceSetTypedElement implements ITypedElement {
	/** This will be used as the type of our wrappers in order to determine the structure and content viewers. */
	public static final String EMF_TYPE = "EMF.TYPE"; //$NON-NLS-1$

	/** The wrapped ResourceSet. */
	private final ResourceSet resourceSet;

	/** Name of the wrapped ResourceSet. */
	private final String name;

	/**
	 * Wraps the given <em>resourceSet</em> within a new {@link ITypedElement}.
	 * 
	 * @param resourceSet
	 *            The ResourceSet to wrap.
	 */
	public ResourceSetTypedElement(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
		// Use the first resource as a basis for this type element's name and icon
		if (resourceSet.getResources().size() > 0) {
			Resource resource = resourceSet.getResources().get(0);
			name = resource.getURI().trimFragment().lastSegment();
		} else {
			// FIXME where is this name displayed? Externalize
			name = "EMF Compare Resource Set";
		}
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
		return null;
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
