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

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;

/**
 * This will be used as a default implementation of an {@link IModelResolver} by the comparison scope builder.
 * It is a "top-down" implementation : loading a given resource as a starting point, it will resolve all of
 * the containment tree to discover other resources composing the model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class LogicalModelResolver implements IModelResolver {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelResolver#resolveLocalModels(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.resources.IResource, org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);

		final StorageTraversal leftTraversal = resolveLocalModel(left, subMonitor.newChild(1));
		final StorageTraversal rightTraversal = resolveLocalModel(right, subMonitor.newChild(1));

		final StorageTraversal originTraversal;
		if (origin != null) {
			originTraversal = resolveLocalModel(origin, subMonitor.newChild(1));
		} else {
			subMonitor.setWorkRemaining(2);
			originTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}

		return new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelResolver#resolveLocalModel(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public StorageTraversal resolveLocalModel(IResource start, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);

		if (!(start instanceof IFile)) {
			subMonitor.setWorkRemaining(0);
			return new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}

		final SyncResourceSet resourceSet = new SyncResourceSet();
		final StorageURIConverter converter = new StorageURIConverter(resourceSet.getURIConverter());
		resourceSet.setURIConverter(converter);

		if (resourceSet.resolveAll((IFile)start, subMonitor.newChild(95))) {
			final Set<IStorage> storages = Sets.newLinkedHashSet(Sets.union(Collections
					.singleton((IFile)start), converter.getLoadedRevisions()));
			subMonitor.worked(5);
			return new StorageTraversal(storages);
		} else {
			subMonitor.setWorkRemaining(5);
			// We failed to load the starting point. simply return an empty traversal.
		}

		subMonitor.worked(5);

		return new StorageTraversal(Collections.singleton((IFile)start));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelResolver#resolveModels(org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor,
	 *      org.eclipse.core.resources.IStorage, org.eclipse.core.resources.IStorage,
	 *      org.eclipse.core.resources.IStorage, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, EMFCompareIDEUIMessages
				.getString("EMFSynchronizationModel.resolving"), 3); //$NON-NLS-1$
		SynchronizationModel syncModel;

		StorageTraversal leftTraversal = resolveTraversal(storageAccessor, left, DiffSide.SOURCE, progress
				.newChild(1));
		StorageTraversal rightTraversal = resolveTraversal(storageAccessor, right, DiffSide.REMOTE, progress
				.newChild(1));

		StorageTraversal originTraversal = null;
		if (origin != null) {
			originTraversal = resolveTraversal(storageAccessor, origin, DiffSide.ORIGIN, progress.newChild(1));
		}
		progress.setWorkRemaining(0);

		syncModel = new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);

		return syncModel;
	}

	/**
	 * Checks whether the given set already contains the given storage. We can't rely on contains() since the
	 * storages override neither hash code nor equals.
	 * 
	 * @param storages
	 *            The set of existing storages.
	 * @param storage
	 *            The candidate for addition in the storages set.
	 * @return <code>true</code> if the given set already contains the given storage.
	 */
	protected boolean contains(Set<IStorage> storages, IStorage storage) {
		final IPath candidateFullPath = storage.getFullPath();
		for (IStorage existing : storages) {
			if (candidateFullPath.equals(existing.getFullPath())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This will be called by EMF Compare in order to resolve the whole logical model containing the given
	 * storage. Since this may be either a local or remote model, all I/O should go through the given storage
	 * accessor.
	 * 
	 * @param storageAccessor
	 *            Provides access to the resources, local or remote, that can be found in this context.
	 * @param start
	 *            The storage that will be considered as the "starting point" of the traversal to resolve.
	 * @param side
	 *            Side of this particular model. Should be used in conjunction with the
	 *            {@code storageAccessor} to retrieve resource contents.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return A traversal corresponding to all resources composing the given storage's logical model.
	 */
	private StorageTraversal resolveTraversal(IStorageProviderAccessor storageAccessor, IStorage start,
			DiffSide side, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, 100);

		StorageTraversal traversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		if (start == null) {
			return traversal;
		}

		final SyncResourceSet resourceSet = new SyncResourceSet();
		final StorageURIConverter converter = new RevisionedURIConverter(resourceSet.getURIConverter(),
				storageAccessor, side);
		resourceSet.setURIConverter(converter);

		if (resourceSet.resolveAll(start, progress.newChild(95))) {
			final Set<IStorage> storages = Sets.newLinkedHashSet();
			for (IStorage loaded : converter.getLoadedRevisions()) {
				if (!contains(storages, loaded)) {
					storages.add(loaded);
				}
			}
			traversal = new StorageTraversal(storages);
		} else {
			progress.setWorkRemaining(5);
			// We failed to load the starting point. simply return an empty traversal.
		}

		progress.worked(5);

		return traversal;
	}
}
