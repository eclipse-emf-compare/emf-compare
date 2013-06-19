/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests;

import java.io.InputStream;

import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

/** Mostly copied from org.eclipse.team.internal.ui.StorageTypedElement. */
public class StorageTypedElement implements ITypedElement, IEncodedStreamContentAccessor, IAdaptable {
	private final IStorage storage;

	private final String fullPath;

	public StorageTypedElement(IStorage storage, String fullPath) {
		this.storage = storage;
		this.fullPath = fullPath;
	}

	public StorageTypedElement(IFile file) {
		this(file, file.getFullPath().toString());
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == IStorage.class) {
			return storage;
		}
		return storage.getAdapter(adapter);
	}

	public String getCharset() throws CoreException {
		if (storage instanceof IEncodedStreamContentAccessor) {
			return ((IEncodedStreamContentAccessor)storage).getCharset();
		}
		return null;
	}

	public InputStream getContents() throws CoreException {
		return storage.getContents();
	}

	public Image getImage() {
		return CompareUI.getImage(getType());
	}

	public String getName() {
		return fullPath;
	}

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
