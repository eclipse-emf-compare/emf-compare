/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.model;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.internal.ModelIdentifier;
import org.eclipse.emf.compare.logical.RevisionedURIConverter;
import org.eclipse.emf.compare.logical.extension.EMFCompareExtensionRegistry;
import org.eclipse.emf.compare.logical.extension.ModelResolverDescriptor;
import org.eclipse.emf.compare.logical.internal.utils.ResourceUtil;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

// FIXME find a way to properly dispose of the three resource sets
/**
 * This will be used to map EMF {@link Resource}s to their physical {@link IResource}s.
 * <p>
 * Take note that this will keep references to all three resource sets (left, right and origin) in order to
 * avoid doing more than one {@link EcoreUtil#resolveAll(org.eclipse.emf.ecore.resource.ResourceSet)} for
 * these. We need to do one on each here in order to determine whether either one of them references a
 * resource that no longer exist in the others.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFResourceMapping extends ResourceMapping {
	// FIXME can we use "resolveable" URIs? git:/repo/commit/revSHA#file ?
	/** We'll use this as the scheme of our remotely loaded resources. */
	public static final String REMOTE_RESOURCE_SCHEME = "remote"; //$NON-NLS-1$

	/** The physical resource for which this mapping has been created. */
	private final IFile file;

	/** The EMF Resource corresponding to {@link #file}. */
	private final Resource emfResource;

	/** Keep reference to the left resource set. */
	private ResourceSet localResourceSet;

	/**
	 * Keep reference to the right resource set. This will only be available if we are fed a
	 * RemoteResourceContext when resolving the logical model through
	 * {@link #getTraversals(ResourceMappingContext, IProgressMonitor)}.
	 */
	private ResourceSet remoteResourceSet;

	/**
	 * Keep reference to the origin resource set. This will only be available if we are fed a
	 * RemoteResourceContext when resolving the logical model through
	 * {@link #getTraversals(ResourceMappingContext, IProgressMonitor)}
	 */
	private ResourceSet originResourceSet;

	/** The model provider which created this mapping. */
	private final String providerId;

	/**
	 * This will keep track of the {@link IResource}s this logical model spans (will contain paths
	 * corresponding to left, right and origin resources regardless of their "existing" state for the three).
	 */
	private Set<IResource> iResourcesInScope;

	/** We will use this boolean to prevent us from resolving the full logical model more than once. */
	private boolean isResolved;

	/**
	 * Instantiates our mapping given both the physical {@link IResource} and its corresponding logical
	 * {@link Resource}.
	 * <p>
	 * Do note that we consider the given file to be the "local" resource of this mapping if we are given a
	 * RemoteResourceContext for resolution.
	 * </p>
	 * 
	 * @param file
	 *            The physical resource of this mapping. Considered to be the "local" resource.
	 * @param emfResource
	 *            The corresponding EMF Resource for this mapping.
	 * @param providerId
	 *            The Model provider which created this mapping.
	 */
	public EMFResourceMapping(IFile file, Resource emfResource, String providerId) {
		this.file = file;
		this.emfResource = emfResource;
		this.providerId = providerId;
		this.localResourceSet = emfResource.getResourceSet();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getModelObject()
	 */
	@Override
	public Object getModelObject() {
		return emfResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getModelProviderId()
	 */
	@Override
	public String getModelProviderId() {
		return providerId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getProjects()
	 */
	@Override
	public IProject[] getProjects() {
		final Set<IResource> physicalResources = resolvePhysicalResources();

		final Set<IProject> projects = Sets.newLinkedHashSet();
		for (IResource iResource : physicalResources) {
			projects.add(iResource.getProject());
		}

		return projects.toArray(new IProject[projects.size()]);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getTraversals(org.eclipse.core.resources.mapping.ResourceMappingContext,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ResourceTraversal[] getTraversals(ResourceMappingContext context, IProgressMonitor monitor)
			throws CoreException {
		if (!isResolved) {
			isResolved = true;

			resolveResourceSets(context, monitor);
		}

		/*
		 * All of our resource sets are now fully resolved (if we had a remote context, otherwise only the
		 * left one is). Browse them to find all IResources than constitute this logical model, whether they
		 * exist locally or not.
		 */

		ResourceTraversal traversal = new ResourceTraversal(iResourcesInScope
				.toArray(new IResource[iResourcesInScope.size()]), IResource.DEPTH_ONE, IResource.NONE);

		return new ResourceTraversal[] {traversal, };
	}

	/**
	 * Forces the re-resolution of this resource mapping in the given context.
	 * 
	 * @param context
	 *            Context in which to replay the model resolving.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @throws CoreException
	 *             Thrown if we cannot resolve the logical model in this context.
	 */
	public void forceResolving(ResourceMappingContext context, IProgressMonitor monitor) throws CoreException {
		isResolved = false;
		remoteResourceSet = null;
		originResourceSet = null;
		iResourcesInScope = null;

		getTraversals(context, monitor);
	}

	/**
	 * Returns the resource set in which the local variant of the logical model has been loaded.
	 * 
	 * @return The resource set in which the local variant of the logical model has been loaded.
	 */
	public ResourceSet getLocalResourceSet() {
		return localResourceSet;
	}

	/**
	 * Returns the resource set in which the remote variant of the logical model has been loaded.
	 * 
	 * @return The resource set in which the remote variant of the logical model has been loaded.
	 */
	public ResourceSet getRemoteResourceSet() {
		return remoteResourceSet;
	}

	/**
	 * Returns the resource set in which the ancestor variant of the logical model has been loaded.
	 * 
	 * @return The resource set in which the ancestor variant of the logical model has been loaded.
	 */
	public ResourceSet getOriginResourceSet() {
		return originResourceSet;
	}

	/**
	 * Try and resolve all three resource sets. Should only be called once.
	 * 
	 * @param context
	 *            Context from which to retrieve resource content.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @throws CoreException
	 *             Thrown if we cannot fetch resource content through the {@code context}.
	 */
	private void resolveResourceSets(ResourceMappingContext context, IProgressMonitor monitor)
			throws CoreException {
		resolveLocalResourceSet();

		if (context instanceof RemoteResourceMappingContext) {
			final RemoteResourceMappingContext remoteContext = (RemoteResourceMappingContext)context;

			for (Resource eResource : localResourceSet.getResources()) {
				final IFile localFile;
				if (eResource == emfResource) {
					localFile = file;
				} else {
					localFile = (IFile)ResourceUtil.findIResource(eResource);
				}

				IStorage remoteContents = remoteContext.fetchRemoteContents(localFile, monitor);
				IStorage originContents = remoteContext.fetchBaseContents(localFile, monitor);

				if (remoteContents != null) {
					if (remoteResourceSet == null) {
						remoteResourceSet = createRemoteResourceSet(localFile, remoteContents);
					} else {
						final RevisionedURIConverter converter = (RevisionedURIConverter)remoteResourceSet
								.getURIConverter();
						converter.setStorage(remoteContents);
					}
					loadRemoteResource(eResource.getURI(), remoteContents, remoteResourceSet);
				}
				if (originContents != null) {
					if (originResourceSet == null) {
						originResourceSet = createRemoteResourceSet(localFile, originContents);
					} else {
						final RevisionedURIConverter converter = (RevisionedURIConverter)originResourceSet
								.getURIConverter();
						converter.setStorage(originContents);
					}
					loadRemoteResource(eResource.getURI(), originContents, originResourceSet);
				}
			}

			// make sure that these two are fully resolved
			if (remoteResourceSet != null) {
				EcoreUtil.resolveAll(remoteResourceSet);
			}
			if (originResourceSet != null) {
				EcoreUtil.resolveAll(originResourceSet);
			}
		}

		resolvePhysicalResources();
	}

	/**
	 * Browse through all three resource sets and resolve the physical {@link IResource}s than constitute this
	 * logical model.
	 * 
	 * @return The list of all physical resources that constitute this logical model.
	 */
	private Set<IResource> resolvePhysicalResources() {
		if (localResourceSet == null) {
			// FIXME throw exception of some kind
		}
		if (iResourcesInScope != null) {
			return iResourcesInScope;
		}

		iResourcesInScope = Sets.newLinkedHashSet();

		for (Resource eResource : localResourceSet.getResources()) {
			if (eResource == emfResource) {
				iResourcesInScope.add(file);
			} else {
				IResource iResource = ResourceUtil.findIResource(eResource);
				if (iResource != null) {
					iResourcesInScope.add(iResource);
				}
			}
		}

		if (remoteResourceSet != null) {
			for (Resource eResource : remoteResourceSet.getResources()) {
				IResource iResource = ResourceUtil.findIResource(eResource);
				if (iResource != null) {
					iResourcesInScope.add(iResource);
				}
			}
		}

		if (originResourceSet != null) {
			for (Resource eResource : originResourceSet.getResources()) {
				IResource iResource = ResourceUtil.findIResource(eResource);
				if (iResource != null) {
					iResourcesInScope.add(iResource);
				}
			}
		}

		return iResourcesInScope;
	}

	/**
	 * This will try and resolve all logical resources that constitute this model in the local resource set.
	 */
	private void resolveLocalResourceSet() {
		Iterator<ModelResolverDescriptor> modelResolverIterator = EMFCompareExtensionRegistry
				.getRegisteredModelResolvers().iterator();
		boolean resolved = false;
		while (!resolved && modelResolverIterator.hasNext()) {
			ModelResolverDescriptor descriptor = modelResolverIterator.next();
			if (descriptor.canResolve(new ModelIdentifier(emfResource))) {
				descriptor.getModelResolver().resolve(file, emfResource);
				resolved = true;
			}
		}
		if (!resolved) {
			EcoreUtil.resolveAll(localResourceSet);
		}
	}

	/**
	 * Loads the given <em>storage</em> as an EMF {@link Resource} in the given resource set..
	 * 
	 * @param resourceURI
	 *            URI of the resource contained in <em>storage</em>.
	 * @param storage
	 *            The {@link IStorage} from which to fetch the parent EMF Resource contents.
	 * @param resourceSet
	 *            Resource set in which to load the remote logical model.
	 * @throws CoreException
	 *             Thrown if we cannot load this storage as an EMF {@link Resource}.
	 */
	private static void loadRemoteResource(URI resourceURI, IStorage storage, ResourceSet resourceSet)
			throws CoreException {
		String resourcePath = resourceURI.path();
		if (resourceURI.isPlatform()) {
			resourcePath = resourcePath.substring(resourcePath.indexOf('/') + 1);
		}
		URI actualURI = URI.createURI(REMOTE_RESOURCE_SCHEME + ':' + '/' + resourcePath);
		Resource resource = resourceSet.createResource(actualURI);

		InputStream remoteStream = null;
		try {
			remoteStream = storage.getContents();
			resource.load(remoteStream, Collections.emptyMap());
		} catch (IOException e) {
			// FIXME log
		} finally {
			if (remoteStream != null) {
				try {
					remoteStream.close();
				} catch (IOException e) {
					// FIXME log
				}
			}
		}
	}

	/**
	 * This will be used internally in order to create a resource set that can resolve remote URIs against
	 * their actual revision.
	 * 
	 * @param baseResource
	 *            The first resource that will be loaded from this resource set.
	 * @param storage
	 *            The {@link IStorage} that contains the "base" resource (the first we'll load) and against
	 *            which timestamp we'll have to resolve URIs.
	 * @return The created resource set.
	 * @throws CoreException
	 *             This will be thrown if we cannot retrieve a file revision for the given storage.
	 */
	private static ResourceSet createRemoteResourceSet(IResource baseResource, IStorage storage)
			throws CoreException {
		ResourceSet resourceSet = new ResourceSetImpl();

		resourceSet.setURIConverter(new RevisionedURIConverter(resourceSet.getURIConverter(), storage));

		return resourceSet;
	}
}
