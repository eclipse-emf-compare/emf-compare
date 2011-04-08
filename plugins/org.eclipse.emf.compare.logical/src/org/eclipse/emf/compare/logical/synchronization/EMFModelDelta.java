/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.synchronization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IDiffTree;
import org.eclipse.team.core.diff.IThreeWayDiff;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.mapping.IResourceDiff;
import org.eclipse.team.core.mapping.ISynchronizationContext;

/**
 * This class will serve as the root of our logical model deltas.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFModelDelta extends EMFDelta {
	/** Identifier of the model provider for which this delta has been created. */
	private String modelProviderId;

	/** Synchronization context of this delta. */
	private ISynchronizationContext context;

	/** Keeps track of the local resource set for which this instance holds deltas. */
	private ResourceSet localResourceSet;

	/** Keeps track of the remote resource set for which this instance holds deltas. */
	private ResourceSet remoteResourceSet;

	/** Keeps track of the ancestor resource set for which this instance holds deltas. */
	private ResourceSet ancestorResourceSet;

	/**
	 * Creates the root of our model delta.
	 * 
	 * @param context
	 *            Synchronization context of this delta.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this delta should be created.
	 */
	private EMFModelDelta(ISynchronizationContext context, String modelProviderId) {
		super(null);
		this.modelProviderId = modelProviderId;
		this.context = context;
	}

	/**
	 * Initializes a synchronization delta given the identifier of the model provider that required this delta
	 * and the current synchronization context.
	 * 
	 * @param context
	 *            Synchronization context of this delta.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this delta should be created.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @return The created Synchronization delta.
	 * @throws CoreException
	 *             Thrown when the comparison failed somehow.
	 */
	public static EMFModelDelta createDelta(ISynchronizationContext context, String modelProviderId,
			IProgressMonitor monitor) throws CoreException {
		EMFModelDelta delta = new EMFModelDelta(context, modelProviderId);

		delta.initialize(monitor);

		return delta;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		// FIXME is this really where the unloading should be done?
		if (localResourceSet != null) {
			for (Resource resource : localResourceSet.getResources()) {
				resource.unload();
			}
			localResourceSet.getResources().clear();
			localResourceSet = null;
		}
		if (remoteResourceSet != null) {
			for (Resource resource : remoteResourceSet.getResources()) {
				resource.unload();
			}
			remoteResourceSet.getResources().clear();
			remoteResourceSet = null;
		}
		if (ancestorResourceSet != null) {
			for (Resource resource : ancestorResourceSet.getResources()) {
				resource.unload();
			}
			ancestorResourceSet.getResources().clear();
			ancestorResourceSet = null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getAncestor()
	 */
	@Override
	public Object getAncestor() {
		return ancestorResourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getLocal()
	 */
	@Override
	public Object getLocal() {
		return localResourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getRemote()
	 */
	@Override
	public Object getRemote() {
		return remoteResourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getDiff()
	 */
	@Override
	public IDiff getDiff() {
		return null;
	}

	/**
	 * This will be called internally to create or reset the comparison delta.
	 * 
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @throws CoreException
	 *             Thrown if we did not manage to load the remote resources.
	 */
	private void initialize(IProgressMonitor monitor) throws CoreException {
		clear();

		// Extract the emf and physcial resources from the scope
		ResourceMapping[] mappings = context.getScope().getMappings();
		Set<Resource> emfResourcesInScope = new LinkedHashSet<Resource>(mappings.length);
		Set<IResource> iResourcesInScope = new LinkedHashSet<IResource>(mappings.length);
		for (ResourceMapping mapping : mappings) {
			if (modelProviderId.equals(mapping.getModelProviderId()) && mapping instanceof EMFResourceMapping) {
				Object modelObject = ((EMFResourceMapping)mapping).getModelObject();
				if (modelObject instanceof Resource) {
					emfResourcesInScope.add((Resource)modelObject);
					iResourcesInScope.add(((EMFResourceMapping)mapping).getIResource());
				}

			}
		}
		if (emfResourcesInScope.size() <= 0 || emfResourcesInScope.size() != iResourcesInScope.size()) {
			// FIXME throw exception
		}

		initializeResourceSets(emfResourcesInScope.iterator().next().getResourceSet());

		// Compute the delta for each resource
		IDiffTree diffTree = context.getDiffTree();
		Iterator<Resource> emfResourcesIterator = emfResourcesInScope.iterator();
		Iterator<IResource> iResourcesIterator = iResourcesInScope.iterator();
		while (emfResourcesIterator.hasNext() && iResourcesIterator.hasNext()) {
			Resource emfResource = emfResourcesIterator.next();
			IResource iResource = iResourcesIterator.next();

			IDiff delta = diffTree.getDiff(iResource.getFullPath());

			if (delta != null && delta.getKind() != IDiff.NO_CHANGE) {
				if (delta instanceof IThreeWayDiff) {
					handleThreeWayDiff((IThreeWayDiff)delta, emfResource, monitor);
				} else {
					// FIXME handleTwoWayDiff()
				}
			}
		}
	}

	/**
	 * This will be called once from {@link #initialize(IProgressMonitor)} in order to set the local, remote
	 * and ancestor to their respective values.
	 * 
	 * @param local
	 *            The local resource set.
	 */
	private void initializeResourceSets(ResourceSet local) {
		localResourceSet = local;
		remoteResourceSet = createResourceSet(local);
		ancestorResourceSet = createResourceSet(local);
	}

	/**
	 * Handles three-way deltas.
	 * 
	 * @param delta
	 *            The delta we are to build EMF deltas for.
	 * @param localVariant
	 *            Local variant of the resource being compared.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @throws CoreException
	 *             Thrown if we did not manage to load the remote variants of the resource.
	 */
	private void handleThreeWayDiff(IThreeWayDiff delta, Resource localVariant, IProgressMonitor monitor)
			throws CoreException {
		IResourceDiff remoteChange = (IResourceDiff)delta.getRemoteChange();
		URI resourceURI = localVariant.getURI();
		if (remoteChange != null) {
			IFileRevision remoteVariant = remoteChange.getAfterState();
			Resource remoteResource = loadRemoteResource(remoteResourceSet, resourceURI,
					remoteVariant.getStorage(monitor));

			IFileRevision baseVariant = remoteChange.getBeforeState();
			Resource ancestorResource = loadRemoteResource(ancestorResourceSet, resourceURI,
					baseVariant.getStorage(monitor));

			EMFResourceDelta resourceDelta = new EMFResourceDelta(this, delta, localVariant, remoteResource,
					ancestorResource);
			// FIXME
			// DELTA_TREE_BUILDER.buildDelta(resourceDelta, remoteResource, ancestorResource);
		} else {
			// FIXME No remote change; thus no remote resource. For now, assume that remote == base.
			IResourceDiff localChange = (IResourceDiff)(delta).getLocalChange();

			IFileRevision baseVariant = localChange.getBeforeState();
			Resource remoteResource = loadRemoteResource(remoteResourceSet, resourceURI,
					baseVariant.getStorage(monitor));
			Resource ancestorResource = loadRemoteResource(ancestorResourceSet, resourceURI,
					baseVariant.getStorage(monitor));

			EMFResourceDelta resourceDelta = new EMFResourceDelta(this, delta, localVariant, remoteResource,
					ancestorResource);
			// FIXME
			// DELTA_TREE_BUILDER.buildDelta(resourceDelta, remoteResource, ancestorResource);
		}
	}

	/**
	 * This will try and load a resoruce corresponding to the given local variant from the given storage.
	 * 
	 * @param resourceSet
	 *            The resource set in which to load the model.
	 * @param resourceURI
	 *            Local variant of the resource we need read from <em>storage</em>.
	 * @param storage
	 *            The storage from which to read a remote resource.
	 * @return The loaded resource.
	 * @throws CoreException
	 *             Thrown if we did not manage to load the specified resource from the specified storage.
	 */
	private Resource loadRemoteResource(ResourceSet resourceSet, URI resourceURI, IStorage storage)
			throws CoreException {
		Resource remoteResource = resourceSet.createResource(resourceURI);

		InputStream remoteStream = null;
		try {
			remoteStream = storage.getContents();
			remoteResource.load(remoteStream, Collections.emptyMap());
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

		return remoteResource;
	}

	/**
	 * Creates a new {@link ResourceSet} with the given <em>base</em>. This new resource set will share its
	 * {@link EPackage.Registry package registry}, {@link Resource.Factory.Registry resource factory registry}
	 * and {@link URIConverter URI converter} with the given <em>base</em>.
	 * 
	 * @param base
	 *            The base resource set from which to copy registries.
	 * @return The newly created resource set.
	 */
	private ResourceSet createResourceSet(ResourceSet base) {
		ResourceSet resourceSet = new ResourceSetImpl();

		resourceSet.setPackageRegistry(base.getPackageRegistry());
		resourceSet.setResourceFactoryRegistry(base.getResourceFactoryRegistry());
		resourceSet.setURIConverter(base.getURIConverter());

		return resourceSet;
	}
}
