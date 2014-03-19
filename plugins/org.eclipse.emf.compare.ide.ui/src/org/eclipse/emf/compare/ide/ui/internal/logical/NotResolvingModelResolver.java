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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.compare.ide.ui.logical.AbstractModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * {@link IModelResolver} that does not resolve anything. It will only handle the current select resource.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class NotResolvingModelResolver extends AbstractModelResolver {

	/**
	 * {@inheritDoc}
	 */
	public SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 1);

		final StorageTraversal leftTraversal = resolveLocalModel(left, subMonitor.newChild(1));
		final StorageTraversal rightTraversal = resolveLocalModel(right, subMonitor.newChild(1));

		final StorageTraversal originTraversal;
		if (origin != null) {
			originTraversal = resolveLocalModel(origin, subMonitor.newChild(1));
		} else {
			originTraversal = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}
		subMonitor.setWorkRemaining(0);

		return new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);
	}

	/**
	 * {@inheritDoc}
	 */
	public SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, 1);
		SynchronizationModel syncModel;

		StorageTraversal leftTraversal = new StorageTraversal(Sets.newHashSet(left));
		StorageTraversal rightTraversal = new StorageTraversal(Sets.newHashSet(right));

		StorageTraversal originTraversal = null;
		if (origin != null) {
			originTraversal = new StorageTraversal(Sets.newHashSet(origin));
		}
		progress.setWorkRemaining(0);

		syncModel = new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);

		return syncModel;
	}

	/**
	 * {@inheritDoc}
	 */
	public StorageTraversal resolveLocalModel(IResource start, IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 1);
		final StorageTraversal result;
		/*
		 * Resolve the resource in order to avoid adding ForwardingResource object as storage. Team providers
		 * do not handle well ForwardingFile since their implementation is base on equal implementation.
		 */
		/*
		 * Example see EGit implementation.
		 * org.eclipse.egit.ui.internal.synchronize.GitModelSynchronize.synchronize(IResource[], Repository,
		 * String, String, boolean) implementation.
		 */
		IFile resolvedResource = ResourcesPlugin.getWorkspace().getRoot().getFile(start.getFullPath());
		if (resolvedResource != null && resolvedResource.exists()) {
			result = new StorageTraversal(Sets.newHashSet(resolvedResource));
		} else {
			result = new StorageTraversal(Sets.<IStorage> newLinkedHashSet());
		}
		subMonitor.setWorkRemaining(0);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canResolve(IStorage sourceStorage) {
		// It should be able to handle any case.
		return true;
	}

}
