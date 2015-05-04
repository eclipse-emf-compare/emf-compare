/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
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

import org.apache.log4j.Logger;
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
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * This implementation of a {@link ModelProvider} will be used to provide the logical model associated with
 * EMF models.
 * <p>
 * Concretely, an EMF model can span multiple physical resources (fragmented models); this model provider can
 * be used to find all of these associated physical resources. <b>Note</b> that a model can span remote
 * resources that do not exist locally; these will be accounted for when using this model with a
 * {@link RemoteResourceMappingContext}.
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
	 * compared alone or if in the contrary the action's scope must be expanded, and will do so very often in
	 * short intervals. We will cache the result in order to avoid multiple identical computation to take
	 * place and hasten the whole process.
	 * </p>
	 */
	public static final long CACHE_EXPIRATION = 120L;

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(EMFModelProvider.class);

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
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getMappings() - START"); //$NON-NLS-1$
		}
		if (resource instanceof IFile) {
			try {
				final SynchronizationModel syncModel = getOrComputeLogicalModel((IFile)resource, context,
						monitor);
				if (syncModel != null) {
					final ResourceMapping mapping = new EMFResourceMapping(resource, context, syncModel,
							PROVIDER_ID);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("getMappings() - FINISH NORMALLY"); //$NON-NLS-1$
					}
					return new ResourceMapping[] {mapping, };
				} else {
					// fall back to super
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// fall back to super
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getMappings() - FINISH ABNORMALLY"); //$NON-NLS-1$
		}
		return super.getMappings(resource, context, monitor);
	}

	/**
	 * Clears the caches of this provider.
	 */
	public void clear() {
		contextToResourceMappingCache.invalidateAll();
	}

	/**
	 * Retrieve the logical model associated with the given IFile from our
	 * {@link #contextToResourceMappingCache cache}, compute and store it if we do not have it yet (or no
	 * longer).
	 * <p>
	 * The returned traversal will only reflect the local state of the logical model if this is passed a
	 * {@link ResourceMappingContext#LOCAL_CONTEXT local mapping context}. Since computing the whole
	 * traversal, including remote resources and links, can be a costly operation which involves I/O calls
	 * over remote repositories, using a local context is advisable when such an accurate traversal is not
	 * needed.
	 * </p>
	 * 
	 * @param file
	 *            The IFile for which we are currently seeking a logical model. Does not require to exist
	 *            locally, but in this case we may only retrieve its model from the cache and will be unable
	 *            to compute it.
	 * @param context
	 *            The context we'll use to compute this file's model.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return The resolved synchronization model for this file.
	 * @throws CoreException
	 *             if we cannot retrieve the content of a resource for some reason.
	 * @throws InterruptedException
	 *             If the user interrupts the resolving.
	 */
	SynchronizationModel getOrComputeLogicalModel(IFile file, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException, InterruptedException {
		SynchronizationModel syncModel;
		synchronized(contextToResourceMappingCache) {
			final Cache<IResource, SynchronizationModel> resourceMappingCache = contextToResourceMappingCache
					.getUnchecked(context);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Retrieved cache with ~ " + resourceMappingCache.size() //$NON-NLS-1$
						+ " entries  for context " + context); //$NON-NLS-1$ 
			}
			syncModel = resourceMappingCache.getIfPresent(file);
			if (syncModel == null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Cache MISSED for " + file); //$NON-NLS-1$
				}
				syncModel = computeLogicalModel(file, context, monitor);
				if (syncModel != null) {
					for (IResource res : syncModel.getResources()) {
						resourceMappingCache.put(res, syncModel);
					}
				}
			} else if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Cache FOUND entry for " + file); //$NON-NLS-1$
			}
		}
		return syncModel;
	}

	/**
	 * Resolve the logical model of the given file.
	 * <p>
	 * If the given {@link ResourceMappingContext context} is a {@link RemoteResourceMappingContext remote}
	 * one, the returned synchronization model will reflect the state of all three sides of this file's model.
	 * For example, two files <code>file1</code> and <code>file2</code> may not be linked locally, but still
	 * have cross-references to each other on one of the remote sides. In such a case, considering we're
	 * trying to compute the model of <code>file1</code>, the returned model will only contained
	 * <code>file1</code> if called with a {@link ResourceMappingContext#LOCAL_CONTEXT local context}, but it
	 * will contain both files if called with a {@link RemoteResourceMappingContext remote context}.
	 * </p>
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
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("computeLogicalModel() - START"); //$NON-NLS-1$
		}
		IProgressMonitor actualMonitor = monitor;
		if (actualMonitor == null) {
			actualMonitor = new NullProgressMonitor();
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("computeLogicalModel() - resolving local model"); //$NON-NLS-1$
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
				left = findTypedElement(localTraversal, accessor, actualMonitor, DiffSide.SOURCE);
				right = findTypedElement(localTraversal, accessor, actualMonitor, DiffSide.REMOTE);
				origin = findTypedElement(localTraversal, accessor, actualMonitor, DiffSide.ORIGIN);
			} else {
				left = findTypedElement(localTraversal, accessor, actualMonitor, DiffSide.SOURCE);
				right = findTypedElement(localTraversal, accessor, actualMonitor, DiffSide.REMOTE);
			}

			IStorage leftStorage = null;
			if (left instanceof IAdaptable) {
				leftStorage = (IStorage)((IAdaptable)left).getAdapter(IStorage.class);
			}
			if (left == null || right == null) {
				return null;
			}

			final IModelResolver remoteResolver = EMFCompareIDEUIPlugin.getDefault()
					.getModelResolverRegistry().getBestResolverFor(leftStorage);
			final IModelMinimizer minimizer = new IdenticalResourceMinimizer();
			final ComparisonScopeBuilder builder = new ComparisonScopeBuilder(remoteResolver, minimizer,
					accessor);
			syncModel = builder.buildSynchronizationModel(left, right, origin, actualMonitor);
		} else {
			// TODO wouldn't it be better to use Collections.singleton(file) for the right and origin?
			syncModel = new SynchronizationModel(localTraversal, new StorageTraversal(Collections
					.<IStorage> emptySet()), new StorageTraversal(Collections.<IStorage> emptySet()));
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("computeLogicalModel() - FINISH"); //$NON-NLS-1$
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
						return new StorageTypedElement(storageProvider.getStorage(monitor), ResourceUtil
								.getFixedPath(storage).toString());
					}
				}
			}
		}

		return null;
	}
}
