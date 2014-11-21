/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * This will allow us to remember the storage path that allowed us to load a given resource.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class StoragePathAdapter extends AdapterImpl {
	/** The storage path this adapter has to remember. */
	private final String storagePath;

	/** Whether this storage was local. */
	private final boolean isLocal;

	/**
	 * Default constructor.
	 * 
	 * @param storagePath
	 *            The storage path to remember.
	 * @param isLocal
	 *            Whether this storage was local.
	 */
	public StoragePathAdapter(String storagePath, boolean isLocal) {
		this.storagePath = storagePath;
		this.isLocal = isLocal;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == StoragePathAdapter.class;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public boolean isLocal() {
		return isLocal;
	}
}
