/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;

/**
 * A Resource Traversal is no more than a set of resources used by the synchronization model to determine
 * which resources to load as part of a given logical model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public class StorageTraversal implements IAdaptable {
	/** The set of storages that are part of this traversal. */
	private Set<? extends IStorage> storages;

	/**
	 * Creates our traversal given its set of resources.
	 * 
	 * @param storages
	 *            The set of resources that are part of this traversal.
	 */
	public StorageTraversal(Set<? extends IStorage> storages) {
		this.storages = storages;
	}

	/**
	 * Returns the set of resources that are part of this traversal.
	 * <p>
	 * Note that this is the original set, and that any modification on the returned {@link Set} will affect
	 * this traversal.
	 * </p>
	 * 
	 * @return The set of resources that are part of this traversal.
	 */
	public Set<? extends IStorage> getStorages() {
		return new LinkedHashSet<IStorage>(storages);
	}

	/**
	 * Removes the given storage from this traversal.
	 * 
	 * @param storage
	 *            The storage to be removed.
	 * @since 3.1
	 */
	public void removeStorage(IStorage storage) {
		storages.remove(storage);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == org.eclipse.core.resources.mapping.ResourceTraversal.class) {
			// Team's resource traversal only knows about IResources.
			final List<IResource> resources = Lists.newArrayListWithCapacity(storages.size());
			for (IStorage storage : storages) {
				if (storage instanceof IFile) {
					resources.add((IFile)storage);
				} else {
					/*
					 * Use a file handle. Since files can be both local and remote, they might not even exist
					 * in the current workspace. It will be the responsibility of the user to either get the
					 * remote or local content. The traversal itself only tells "all" potential resources
					 * linked to the current.
					 */
					resources.add(ResourcesPlugin.getWorkspace().getRoot().getFile(storage.getFullPath()));
				}
			}
			final IResource[] resourceArray = resources.toArray(new IResource[resources.size()]);
			return new org.eclipse.core.resources.mapping.ResourceTraversal(resourceArray,
					IResource.DEPTH_ONE, IResource.NONE);
		}
		return null;
	}
}
