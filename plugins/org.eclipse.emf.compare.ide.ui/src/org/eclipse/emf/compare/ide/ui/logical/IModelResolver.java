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
 * <p>
 * Clients can subclass {@link AbstractModelResolver} instead.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
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
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor) throws InterruptedException;

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
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, IProgressMonitor monitor) throws InterruptedException;

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
	 * @throws InterruptedException
	 *             Thrown if the resolution is cancelled or interrupted one way or another.
	 */
	StorageTraversal resolveLocalModel(IResource resource, IProgressMonitor monitor)
			throws InterruptedException;

	/**
	 * This will be used in order to determine whether this resolver can be used for the given storage. For
	 * each given storage, the resolver with the highest ranking will be selected for resolution. This test
	 * should be fast.
	 * 
	 * @param sourceStorage
	 *            The resource we're trying to resolve the logical model of. This will always be the "source"
	 *            or "left" variant of the compared resource.
	 * @return <code>true</code> if this resolver is capable of handling the given storage, <code>false</code>
	 *         otherwise.
	 */
	boolean canResolve(IStorage sourceStorage);

	/**
	 * This will be called as soon as the class is created by the registry. Clients can override if they need
	 * to set up their resolver for use.
	 */
	void initialize();

	/**
	 * This will be called when the contributing plugin of this class is about to be stopped. Clients can
	 * override to get rid of any state they've maintained within their resolver.
	 */
	void dispose();
}
