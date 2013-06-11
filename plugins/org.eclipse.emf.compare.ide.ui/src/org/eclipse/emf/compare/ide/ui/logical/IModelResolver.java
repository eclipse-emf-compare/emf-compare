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
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * This can be used in order to tell EMF Compare how the logical model of a given file can be resolved in its
 * entirety.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public interface IModelResolver {
	/**
	 * Called by EMF Compare in order to resolve the logical models corresponding to the given IResources.
	 * Only local data is available.
	 * 
	 * @param left
	 *            The file that will be considered as the "starting point" of the traversal to resolve as the
	 *            left logical model.
	 * @param right
	 *            "starting point" of the traversal to resolve as the right logical model.
	 * @param origin
	 *            "starting point" of the traversal to resolve as the origin logical model (common ancestor of
	 *            left and right). Can be <code>null</code>.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return A traversal corresponding to all resources composing the given file's logical model.
	 */
	SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor);

	/**
	 * This will be called by EMF Compare in order to resolve the whole logical models containing the given
	 * storages. Since this may be either a local or remote model, all I/O should go through the given storage
	 * accessor.
	 * 
	 * @param storageAccessor
	 *            The accessor that can be used to retrieve synchronization information between our resources.
	 * @param left
	 *            The storage that will be considered as the "starting point" of the traversal to resolve as
	 *            the left logical model.
	 * @param right
	 *            "starting point" of the traversal to resolve as the right logical model.
	 * @param origin
	 *            "starting point" of the traversal to resolve as the origin logical model (common ancestor of
	 *            left and right). Can be <code>null</code>.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return A traversal corresponding to all resources composing the given file's logical model.
	 */
	SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, IProgressMonitor monitor);

	/**
	 * This will be called by Team in order to determine whether a given file can be compared alone, or if it
	 * needs to be compared along with others (and, thus, compared from the synchronize view). Note that only
	 * local data is available here.
	 * 
	 * @param resource
	 *            The workspace resource for which we need a traversal.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return A traversal corresponding to all resources composing the given file's logical model.
	 */
	StorageTraversal resolveLocalModel(IResource resource, IProgressMonitor monitor);
}
