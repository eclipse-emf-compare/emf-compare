/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * This implementation of a {@link ModelProvider} will be used to provide the logical model associated with
 * EMF models.
 * <p>
 * Concretely, an EMF model can span multiple physical resources (fragmented models); this model provider can
 * be used to find all of these associated physical resources.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFModelProvider extends ModelProvider {
	/** ID of this model provider. Must match the definition from the plugin.xml. */
	public static final String PROVIDER_ID = "org.eclipse.emf.compare.model.provider"; //$NON-NLS-1$

	/**
	 * The expiration timeout for our logical model cache values. This might mean that some of the
	 * {@link #getMappings(IResource, ResourceMappingContext, IProgressMonitor) returned mappings} might
	 * reflect a stale view of the files' logical models, but we expect said logical traversals not to change
	 * that often.
	 * <p>
	 * Basically, the mappings are used by the platform to determine whether a file can be moved, replaced or
	 * compared alone or if in the contrary the action's scope must be expanded, and thus we can cache the
	 * "local" computation (this model provider won't use remote context information). A more accurate mapping
	 * will be determined later on.
	 * </p>
	 */
	public static final long CACHE_EXPIRATION = 5L;

	/**
	 * Cache the logical model computed for a given file through this provider. Note that the sub-cache
	 * (resource to mapping) will be a short-lived cache (see also {@link #CACHE_EXPIRATION}).
	 */
	private final LoadingCache<ResourceMappingContext, Cache<IResource, SynchronizationModel>> contextToResourceMappingCache;

	/** Default constructor. */
	public EMFModelProvider() {
		contextToResourceMappingCache = CacheBuilder.newBuilder().initialCapacity(10).build(
				new CacheLoader<ResourceMappingContext, Cache<IResource, SynchronizationModel>>() {
					@Override
					public Cache<IResource, SynchronizationModel> load(ResourceMappingContext key)
							throws Exception {
						return CacheBuilder.newBuilder()
								.expireAfterAccess(CACHE_EXPIRATION, TimeUnit.SECONDS).initialCapacity(10)
								.build();
					}
				});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ModelProvider#getMappings(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.resources.mapping.ResourceMappingContext,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ResourceMapping[] getMappings(IResource resource, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException {
		if (resource instanceof IFile) {
			try {
				final SynchronizationModel syncModel = getOrComputeLogicalModel((IFile)resource, context,
						monitor);
				if (syncModel != null) {
					final ResourceMapping mapping = new EMFResourceMapping(resource, context, syncModel,
							PROVIDER_ID);
					return new ResourceMapping[] {mapping, };
				} else {
					// fall back to super
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// fall back to super
			}
		}
		return super.getMappings(resource, context, monitor);
	}

	/**
	 * Clears the caches of this provider.
	 */
	public void clear() {
		contextToResourceMappingCache.invalidateAll();
	}

	SynchronizationModel getOrComputeLogicalModel(IFile file, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException, InterruptedException {
		SynchronizationModel syncModel;
		synchronized(contextToResourceMappingCache) {
			final Cache<IResource, SynchronizationModel> resourceMappingCache = contextToResourceMappingCache
					.getUnchecked(context);
			syncModel = resourceMappingCache.getIfPresent(file);
			if (syncModel == null) {
				syncModel = computeLogicalModel(file, context, monitor);
				if (syncModel != null) {
					for (IResource res : syncModel.getResources()) {
						resourceMappingCache.put(res, syncModel);
					}
				}
			}
		}
		return syncModel;
	}

	/**
	 * Resolve the logical model of the given file. Do note that this will only reflect the local state of
	 * this file's logical model.
	 * 
	 * @param file
	 *            The file which logical model is to be resolved.
	 * @param context
	 *            The resource mapping context.
	 * @param monitor
	 *            Used to display progress information to the user.
	 * @return The resolved traversal.
	 */
	private SynchronizationModel computeLogicalModel(IFile file, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException, InterruptedException {
		IProgressMonitor actualMonitor = monitor;
		if (actualMonitor == null) {
			actualMonitor = new NullProgressMonitor();
		}

		// Computing the local traversal should be a fast (and cached!) operation. Start from there.
		final IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(file);
		final StorageTraversal localTraversal = resolver.resolveLocalModel(file, actualMonitor);

		final SynchronizationModel syncModel;
		if (context instanceof RemoteResourceMappingContext) {
			final IStorageProviderAccessor accessor = new RemoteMappingContextStorageAccessor(
					(RemoteResourceMappingContext)context);

			ITypedElement left = null;
			ITypedElement right = null;
			ITypedElement origin = null;
			if (((RemoteResourceMappingContext)context).isThreeWay()) {
				left = findTypedElement(localTraversal, accessor, monitor, DiffSide.SOURCE);
				right = findTypedElement(localTraversal, accessor, monitor, DiffSide.REMOTE);
				origin = findTypedElement(localTraversal, accessor, monitor, DiffSide.ORIGIN);
			} else {
				left = findTypedElement(localTraversal, accessor, monitor, DiffSide.SOURCE);
				right = findTypedElement(localTraversal, accessor, monitor, DiffSide.REMOTE);
			}

			IStorage leftStorage = null;
			if (left instanceof IAdaptable) {
				leftStorage = (IStorage)((IAdaptable)left).getAdapter(IStorage.class);
			}
			// we don't need to double-check for left != null here if leftStorage is not null.
			if (left == null || right == null) {
				return null;
			}

			final IModelResolver remoteResolver = EMFCompareIDEUIPlugin.getDefault()
					.getModelResolverRegistry().getBestResolverFor(leftStorage);
			final IModelMinimizer minimizer = new IdenticalResourceMinimizer();
			final ComparisonScopeBuilder builder = new ComparisonScopeBuilder(remoteResolver, minimizer,
					accessor);
			syncModel = builder.buildSynchronizationModel(left, right, origin, monitor);
		} else {
			syncModel = new SynchronizationModel(localTraversal, new StorageTraversal(Collections
					.<IStorage> emptySet()), new StorageTraversal(Collections.<IStorage> emptySet()));
		}

		return syncModel;
	}

	/**
	 * Browses the given traversal in order to create a typed element for the given side of the comparison.
	 * 
	 * @param traversal
	 *            The local traversal from which to find remote typed elements.
	 * @param storageAccessor
	 *            The storage provider accessor in which to retrieve remote information.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @param side
	 *            Side of the comparison for which we seek a typed element.
	 * @return The typed element for the given side if we could find one in the given mappings,
	 *         <code>null</code> otherwise.
	 * @throws CoreException
	 *             if we cannot retrieve the content of a resource for some reason.
	 */
	private ITypedElement findTypedElement(StorageTraversal traversal,
			IStorageProviderAccessor storageAccessor, IProgressMonitor monitor, DiffSide side)
			throws CoreException {
		if (traversal != null && !traversal.getStorages().isEmpty()) {
			for (IStorage storage : traversal.getStorages()) {
				if (storage instanceof IFile) {
					final IStorageProvider storageProvider = storageAccessor.getStorageProvider(
							(IFile)storage, side);
					if (storageProvider != null) {
						return new StorageTypedElement(storageProvider.getStorage(monitor), storage
								.getFullPath().toString());
					}
				}
			}
		}

		return null;
	}
}
