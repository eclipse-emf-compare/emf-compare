/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import java.io.InputStream;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

/**
 * Mostly copied from org.eclipse.team.internal.ui.StorageTypedElement.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class StorageTypedElement implements ITypedElement, IEncodedStreamContentAccessor, IAdaptable {
	/** The underlying storage. */
	private final IStorage storage;

	/** Full path associated with this storage, relative to the workspace root. */
	private final String fullPath;

	/**
	 * Default constructor.
	 * 
	 * @param storage
	 *            The storage for which we need a typed element.
	 * @param fullPath
	 *            Full path to this storage, relative to the workspace root.
	 */
	public StorageTypedElement(IStorage storage, String fullPath) {
		this.storage = storage;
		this.fullPath = fullPath;
	}

	/** {@inheritDoc} */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == IStorage.class) {
			return storage;
		}
		return storage.getAdapter(adapter);
	}

	/** {@inheritDoc} */
	public String getCharset() throws CoreException {
		if (storage instanceof IEncodedStreamContentAccessor) {
			return ((IEncodedStreamContentAccessor)storage).getCharset();
		}
		return null;
	}

	/** {@inheritDoc} */
	public InputStream getContents() throws CoreException {
		return storage.getContents();
	}

	/** {@inheritDoc} */
	public Image getImage() {
		return CompareUI.getImage(getType());
	}

	/** {@inheritDoc} */
	public String getName() {
		return fullPath;
	}

	/** {@inheritDoc} */
	public String getType() {
		String name = getName();
		if (name != null) {
			int index = name.lastIndexOf('.');
			if (index == -1) {
				return ""; //$NON-NLS-1$
			}
			if (index == (name.length() - 1)) {
				return ""; //$NON-NLS-1$
			}
			return name.substring(index + 1);
		}
		return ITypedElement.FOLDER_TYPE;
	}
}
