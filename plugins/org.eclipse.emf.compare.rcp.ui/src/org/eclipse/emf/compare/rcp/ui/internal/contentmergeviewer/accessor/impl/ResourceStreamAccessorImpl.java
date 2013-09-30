/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.IStreamContentAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.impl.AbstractTypedElementAdapter;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ResourceStreamAccessorImpl extends AbstractTypedElementAdapter implements IStreamContentAccessor {

	private final Resource fResource;

	public ResourceStreamAccessorImpl(AdapterFactory adapterFactory, Resource resource) {
		super(adapterFactory);
		this.fResource = resource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getName()
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getImage()
	 */
	public Image getImage() {
		Object image = getItemDelegator().getImage(fResource);
		return ExtendedImageRegistry.getInstance().getImage(image);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getType()
	 */
	public String getType() {
		return TEXT_TYPE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(10240);
		try {
			fResource.save(os, null);
		} catch (IOException e) {
			EMFCompareRCPUIPlugin.getDefault().log(e);
		}
		return new ByteArrayInputStream(os.toByteArray());
	}
}
