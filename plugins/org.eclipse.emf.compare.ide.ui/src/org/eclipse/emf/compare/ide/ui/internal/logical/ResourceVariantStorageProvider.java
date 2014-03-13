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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.team.core.variants.IResourceVariant;

/**
 * Allows access to the underlying IResourceVariant's storage.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceVariantStorageProvider implements IStorageProvider {
	/** Path to the wrapped variant. */
	private final String path;

	/** Wrapped variant. */
	private final IResourceVariant variant;

	/**
	 * Wraps a resource variant as a storage provider.
	 * 
	 * @param path
	 *            Path to the wrapped variant.
	 * @param variant
	 *            The wrapped resource variant.
	 */
	public ResourceVariantStorageProvider(String path, IResourceVariant variant) {
		this.path = path;
		this.variant = variant;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IStorageProvider#getStorage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStorage getStorage(IProgressMonitor monitor) throws CoreException {
		return new StorageWrapper(path, variant.getStorage(monitor));
	}

	/**
	 * Wraps an IStorage, but override its path to something useable.
	 * <p>
	 * Egit uses its own kind of Paths, which cannot be resolved in an Eclipse context without knowledge of
	 * EGit's internals.
	 * </p>
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class StorageWrapper implements IStorage {
		/** Path to the wrapped storage. */
		private final String fullPath;

		/** Wrapped storage. */
		private final IStorage storage;

		/**
		 * Wraps an IStorage with the accurate path.
		 * 
		 * @param fullPath
		 *            Path of that storage.
		 * @param storage
		 *            The wrapped storage.
		 */
		public StorageWrapper(String fullPath, IStorage storage) {
			this.fullPath = fullPath;
			this.storage = storage;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
		 */
		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
			if (IResource.class.isAssignableFrom(adapter) && storage instanceof IFile) {
				return storage;
			}
			return storage.getAdapter(adapter);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.resources.IStorage#getContents()
		 */
		public InputStream getContents() throws CoreException {
			return storage.getContents();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.resources.IStorage#getFullPath()
		 */
		public IPath getFullPath() {
			return new Path(fullPath);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.resources.IStorage#getName()
		 */
		public String getName() {
			return storage.getName();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.core.resources.IStorage#isReadOnly()
		 */
		public boolean isReadOnly() {
			return storage.isReadOnly();
		}
	}
}
