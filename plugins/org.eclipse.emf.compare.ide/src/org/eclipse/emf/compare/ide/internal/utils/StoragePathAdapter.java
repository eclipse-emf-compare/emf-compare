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

	/** The identifier of the commit. */
	private String commitId;

	/** The username of the owner of the commit. */
	private String username;

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

	/**
	 * Constructor.
	 * 
	 * @param storagePath
	 *            The storage path to remember.
	 * @param isLocal
	 *            Whether this storage was local.
	 * @param commitId
	 *            The identifier of the commit.
	 * @param username
	 *            The username of the owner of the commit.
	 */
	public StoragePathAdapter(String storagePath, boolean isLocal, String commitId, String username) {
		this.storagePath = storagePath;
		this.isLocal = isLocal;
		this.commitId = commitId;
		this.username = username;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == StoragePathAdapter.class;
	}

	/**
	 * Getter for the commit id.
	 * 
	 * @return the commit it
	 */
	public String getCommitId() {
		return commitId;
	}

	/**
	 * Getter for the commit owner username.
	 * 
	 * @return the username of the owner
	 */
	public String getUsername() {
		return username;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public boolean isLocal() {
		return isLocal;
	}
}
