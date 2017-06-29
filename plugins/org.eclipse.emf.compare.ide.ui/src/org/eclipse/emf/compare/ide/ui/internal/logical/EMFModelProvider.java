/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 466607
 *     Philip Langer - bug 508855
 *     Martin Fleck - bug 512562
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
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
		contextToResourceMappingCache = CacheBuilder.newBuilder().initialCapacity(10)
				.build(new CacheLoader<ResourceMappingContext, Cache<IResource, SynchronizationModel>>() {
					@Override
					public Cache<IResource, SynchronizationModel> load(ResourceMappingContext key)
							throws Exception {
						return CacheBuilder.newBuilder().expireAfterAccess(CACHE_EXPIRATION, TimeUnit.SECONDS)
								.initialCapacity(10).build();
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
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("getMappings() - START for " + resource); //$NON-NLS-1$
		}

		if (resource instanceof IContainer) {

			final List<IResource> modelResources = new ArrayList<IResource>();
			resource.accept(new IResourceVisitor() {
				public boolean visit(IResource visitedResource) throws CoreException {
					if (visitedResource instanceof IFile) {
						final IFile visitedFile = (IFile)visitedResource;
						if (ResourceUtil.hasModelType(visitedFile)) {
							modelResources.add(visitedFile);
						}
						return false;
					}
					return true;
				}
			});
			if (!modelResources.isEmpty()) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getMappings() - " + resource + " is a container with models: " //$NON-NLS-1$ //$NON-NLS-2$
							+ modelResources.toString());
				}
				return this.getMappings(modelResources.toArray(new IResource[] {}), context, monitor);
			}

		} else if (resource instanceof IFile) {

			try {
				final SynchronizationModel syncModel = getOrComputeLogicalModel((IFile)resource, context,
						monitor);
				if (syncModel != null) {
					final ResourceMapping mapping = new EMFResourceMapping(resource, context, syncModel,
							PROVIDER_ID);
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("getMappings() - FINISH NORMALLY"); //$NON-NLS-1$
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
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("getMappings() - fallback to super."); //$NON-NLS-1$
		}
		return super.getMappings(resource, context, monitor);
	}

	@Override
	public ResourceMapping[] getMappings(IResource[] resources, ResourceMappingContext context,
			IProgressMonitor monitor) throws CoreException {
		if (LOGGER.isInfoEnabled()) {
			final Joiner joiner = Joiner.on(",").skipNulls(); //$NON-NLS-1$
			final String resourceList = joiner.join(resources);
			LOGGER.info("getMappings() - START for " + resourceList); //$NON-NLS-1$
		}

		final List<ResourceMapping> mappings = new ArrayList<ResourceMapping>();

		// collect all IFiles
		final List<IFile> files = new ArrayList<IFile>();
		final List<IResource> remainingResources = new ArrayList<IResource>();

		for (IResource resource : resources) {
			if (resource instanceof IFile) {
				files.add((IFile)resource);
			} else {
				remainingResources.add(resource);
			}
		}

		try {
			final Map<SynchronizationModel, IFile> syncModels = computeLogicalModels(files, context, monitor);
			for (Map.Entry<SynchronizationModel, IFile> entry : syncModels.entrySet()) {
				final ResourceMapping mapping = new EMFResourceMapping(entry.getValue(), context,
						entry.getKey(), PROVIDER_ID);
				mappings.add(mapping);
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("getMappings() - interrupt exception: fallback to super."); //$NON-NLS-1$
			}
			return super.getMappings(resources, context, monitor);
		}

		if (!remainingResources.isEmpty()) {
			if (LOGGER.isInfoEnabled()) {
				final Joiner joiner = Joiner.on(",").skipNulls(); //$NON-NLS-1$
				final String resourceList = joiner.join(remainingResources);
				LOGGER.info("getMappings() - not all resources were handled. fallback to super for: " //$NON-NLS-1$
						+ resourceList);
			}
			mappings.addAll(Arrays.asList(super.getMappings(
					remainingResources.toArray(new IResource[remainingResources.size()]), context, monitor)));
		} else {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("getMappings() - FINISH NORMALLY"); //$NON-NLS-1$
			}
		}

		return mappings.toArray(new ResourceMapping[mappings.size()]);
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
			removeEmptyCacheEntries();

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
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("EMFModelProvider - Minimizing model"); //$NON-NLS-1$
					}
					EMFCompareIDEUIPlugin.getDefault().getModelMinimizerRegistry().getCompoundMinimizer()
							.minimize(file, syncModel, monitor);
				}
			} else if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Cache FOUND entry for " + file); //$NON-NLS-1$
			}
		}
		return syncModel;
	}

	/**
	 * Remove any cache entries that have become stale, i.e., any entry whose value is empty, because those
	 * entries expired.
	 */
	private void removeEmptyCacheEntries() {
		Iterator<Map.Entry<ResourceMappingContext, Cache<IResource, SynchronizationModel>>> entries = contextToResourceMappingCache
				.asMap().entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<ResourceMappingContext, Cache<IResource, SynchronizationModel>> entry = entries.next();
			if (entry.getValue().size() == 0) {
				entries.remove();
			}
		}
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
	 * <p>
	 * <b>Note</b> that this will return an unminimized synchronization model.
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
			final ComparisonScopeBuilder builder = new ComparisonScopeBuilder(remoteResolver,
					new NullModelMinimizer(), accessor);
			syncModel = builder.buildSynchronizationModel(left, right, origin, actualMonitor);
		} else {
			// TODO wouldn't it be better to use Collections.singleton(file) for the right and origin?
			syncModel = new SynchronizationModel(localTraversal,
					new StorageTraversal(Collections.<IStorage> emptySet()),
					new StorageTraversal(Collections.<IStorage> emptySet()));
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("computeLogicalModel() - FINISH"); //$NON-NLS-1$
		}
		return syncModel;
	}

	/**
	 * Resolve the logical model(s) of the given files.
	 * <p>
	 * When model resolution setting is not set to "workspace" the
	 * {@link #computeLogicalModel(IFile, ResourceMappingContext, IProgressMonitor)} method can not guarantee
	 * to compute the whole logical model for the given file. But there are actually use cases in which the
	 * client already knows which files constitute the logical model, see for example
	 * {@link #getMappings(IResource[], ResourceMappingContext, IProgressMonitor)}.
	 * </p>
	 * <p>
	 * This method tries to combine the logical models of the given {@code files} if applicable. Depending of
	 * the given {@code files} the whole logical model may be resolved independent of the workspace resolution
	 * settings.
	 * </p>
	 *
	 * @param files
	 *            The {@link IFile}s for which the logical model(s) is to be computed.
	 * @param context
	 *            The resource mapping context.
	 * @param monitor
	 *            Used to display progress information to the user.
	 * @return The computed logical models each mapped to the first file from which they were resolved. The
	 *         logical models are disjoint.
	 * @throws CoreException
	 *             If we cannot retrieve the content of a resource for some reason.
	 * @throws InterruptedException
	 *             If the user interrupts the resolving.
	 */
	Map<SynchronizationModel, IFile> computeLogicalModels(Collection<IFile> files,
			ResourceMappingContext context, IProgressMonitor monitor)
			throws CoreException, InterruptedException {
		if (LOGGER.isDebugEnabled()) {
			final Joiner joiner = Joiner.on(",").skipNulls(); //$NON-NLS-1$
			final String fileList = joiner.join(files);
			LOGGER.debug("computeLogicalModels() - START with " + fileList); //$NON-NLS-1$
		}
		final Map<SynchronizationModel, IFile> syncModels = new LinkedHashMap<SynchronizationModel, IFile>();

		for (IFile file : files) {
			final SynchronizationModel currentSyncModel = getOrComputeLogicalModel(file, context, monitor);

			if (currentSyncModel == null) {
				// skip file
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("computeLogicalModels() - Could not determine logical model for \"" + file //$NON-NLS-1$
							+ "\". SKIP file."); //$NON-NLS-1$
				}
				continue;
			}

			// subsets can be shortcutted
			boolean combineModels = false;

			final List<SynchronizationModel> toCombine = new LinkedList<SynchronizationModel>();

			for (SynchronizationModel syncModel : syncModels.keySet()) {

				if (!Collections.disjoint(syncModel.getResources(), currentSyncModel.getResources())) {
					toCombine.add(syncModel);

					if (!syncModel.getResources().containsAll(currentSyncModel.getResources())) {
						// the model is not a subset -> deactivate shortcut
						combineModels = true;
					}
				}
			}

			// if there are no models to combine, add this model to the result set
			if (toCombine.isEmpty()) {
				syncModels.put(currentSyncModel, file);
			}

			// when a model can be added to multiple other models, all these models must be combined
			if (combineModels) {
				final Iterator<SynchronizationModel> it = toCombine.iterator();
				final SynchronizationModel firstToCombine = it.next();
				final IFile value = syncModels.get(firstToCombine);

				SynchronizationModel combinedModel = combineModels(currentSyncModel, firstToCombine);
				syncModels.remove(firstToCombine);

				while (it.hasNext()) {
					final SynchronizationModel currentModel = it.next();
					combinedModel = combineModels(combinedModel, currentModel);
					syncModels.remove(currentModel);
				}

				syncModels.put(combinedModel, value);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("computeLogicalModels() - FINISH with " + syncModels.size() + " models"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return syncModels;
	}

	/**
	 * Combines the given {@link SynchronizationModel}s to a new model.
	 *
	 * @param modelA
	 *            The model to combine.
	 * @param modelB
	 *            The model to combine.
	 * @return A new {@link SynchronizationModel} which contains the combination of {@link StorageTraversal}s
	 *         of the given models.
	 */
	private SynchronizationModel combineModels(SynchronizationModel modelA, SynchronizationModel modelB) {
		Set<IStorage> left = new HashSet<IStorage>();
		Set<IStorage> right = new HashSet<IStorage>();
		Set<IStorage> origin = new HashSet<IStorage>();

		StorageTraversal leftTraversal = null;
		StorageTraversal rightTraversal = null;
		StorageTraversal originTraversal = null;

		if (modelA.getLeftTraversal() != null) {
			left.addAll(modelA.getLeftTraversal().getStorages());
			if (modelB != null && modelB.getLeftTraversal() != null) {
				left.addAll(modelB.getLeftTraversal().getStorages());
			}
			leftTraversal = new StorageTraversal(left);
		}
		if (modelA.getRightTraversal() != null) {
			right.addAll(modelA.getRightTraversal().getStorages());
			if (modelB != null && modelB.getRightTraversal() != null) {
				right.addAll(modelB.getRightTraversal().getStorages());
			}
			rightTraversal = new StorageTraversal(right);
		}
		if (modelA.getOriginTraversal() != null) {
			origin.addAll(modelA.getOriginTraversal().getStorages());
			if (modelB != null && modelB.getOriginTraversal() != null) {
				right.addAll(modelB.getOriginTraversal().getStorages());
			}
			originTraversal = new StorageTraversal(origin);
		}

		return new SynchronizationModel(leftTraversal, rightTraversal, originTraversal);
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
					final IStorageProvider storageProvider = storageAccessor
							.getStorageProvider((IFile)storage, side);
					if (storageProvider != null) {
						return new StorageTypedElement(storageProvider.getStorage(monitor),
								ResourceUtil.getFixedPath(storage).toString());
					}
				}
			}
		}

		return null;
	}
}
