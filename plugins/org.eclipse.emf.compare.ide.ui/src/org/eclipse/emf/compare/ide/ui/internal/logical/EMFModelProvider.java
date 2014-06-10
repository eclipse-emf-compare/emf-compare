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

import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
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
	public static final long CACHE_EXPIRATION = 10L;

	/**
	 * Cache the logical model computed for a given file through this provider. Note that this will be a
	 * short-lived cache (see also {@link #CACHE_EXPIRATION}).
	 */
	private final Cache<IStorage, StorageTraversal> logicalModelCache;

	/** Default constructor. */
	public EMFModelProvider() {
		logicalModelCache = CacheBuilder.newBuilder().expireAfterAccess(CACHE_EXPIRATION, TimeUnit.SECONDS)
				.build();
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
				// Useless cast : not with Guava 11, which we support.
				@SuppressWarnings("cast")
				StorageTraversal traversal = logicalModelCache.getIfPresent((IStorage)resource);
				if (traversal == null) {
					traversal = resolveLogicalModel((IFile)resource, monitor);
					for (IStorage storage : traversal.getStorages()) {
						logicalModelCache.put(storage, traversal);
					}
				}
				final ResourceMapping mapping = new EMFResourceMapping(resource, traversal, PROVIDER_ID);
				return new ResourceMapping[] {mapping, };
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// fall back to super
			}
		}
		return super.getMappings(resource, context, monitor);
	}

	/**
	 * Resolve the logical model of the given file. Do note that this will only reflect the local state of
	 * this file's logical model.
	 * 
	 * @param file
	 *            The file which logical model is to be resolved.
	 * @param monitor
	 *            Used to display progress information to the user.
	 * @return The resolved traversal.
	 */
	private StorageTraversal resolveLogicalModel(IFile file, IProgressMonitor monitor)
			throws InterruptedException {
		IProgressMonitor actualMonitor = monitor;
		if (actualMonitor == null) {
			actualMonitor = new NullProgressMonitor();
		}
		final IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(file);
		return resolver.resolveLocalModel(file, actualMonitor);
	}
}
