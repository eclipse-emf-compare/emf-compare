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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.events.ResourceDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IDiffTree;
import org.eclipse.team.core.diff.IThreeWayDiff;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.mapping.IResourceDiff;
import org.eclipse.team.core.mapping.ISynchronizationContext;

/**
 * This class will serve as a super class of all elements describing deltas between EMF models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFModelDelta {
	/** Children of this delta. */
	private final List<EMFModelDelta> children = new ArrayList<EMFModelDelta>();

	/** Parent of this delta. */
	private EMFModelDelta parent;

	/** Identifier of the model provider for which this delta has been created. */
	private String modelProviderId;

	/** Synchronization context of this delta. */
	private ISynchronizationContext context;

	/**
	 * Creates the root of our model delta.
	 * 
	 * @param context
	 *            Synchronization context of this delta.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this delta should be created.
	 */
	private EMFModelDelta(ISynchronizationContext context, String modelProviderId) {
		this.modelProviderId = modelProviderId;
		this.context = context;
	}

	/**
	 * Creates a new child delta under the given parent.
	 * 
	 * @param parent
	 *            Parent of the new delta. Can be <code>null</code> for root deltas.
	 */
	private EMFModelDelta(EMFModelDelta parent) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	/**
	 * Initializes a synchronization delta given the identifier of the model provider that required this delta
	 * and the current synchronization context.
	 * 
	 * @param context
	 *            Synchronization context of this delta.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this delta should be created.
	 * @return The created Synchronization delta.
	 */
	public static EMFModelDelta createDelta(ISynchronizationContext context, String modelProviderId) {
		EMFModelDelta delta = new EMFModelDelta(context, modelProviderId);

		delta.initialize();

		return delta;
	}

	/**
	 * This will be called internally to create or reset the comparison delta.
	 */
	private void initialize() {
		children.clear();

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
		if (emfResourcesInScope.size() != iResourcesInScope.size()) {
			// FIXME throw exception
		}

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
					handleThreeWayDiff((IThreeWayDiff)delta, emfResource);
				} else {
					// FIXME handleTwoWayDiff()
				}
			}
		}
	}

	/**
	 * Handles three-way deltas.
	 * 
	 * @param delta
	 *            The delta we are to build EMF deltas for.
	 */
	private void handleThreeWayDiff(IThreeWayDiff delta, Resource localVariant) {
		IResourceDiff remoteChange = (IResourceDiff)delta.getRemoteChange();
		if (remoteChange != null) {
			IFileRevision remoteVariant = remoteChange.getAfterState();
			Resource remoteResource = loadRemoteResource(localResource, remoteVariant.getStorage(monitor));

			IFileRevision baseVariant = remoteChange.getBeforeState();
			Resource baseResource = createRemoteResource(localResource, baseVariant.getStorage(monitor));

			ResourceDelta resourceDelta = new ResourceDelta(this, localResource, baseResource,
					remoteResource, delta);
			DELTA_TREE_BUILDER.buildDelta(resourceDelta, remoteResource, baseResource);
		} else {
			// FIXME No remote change. for now, assume that remote == base
			IResourceDiff localChange = (IResourceDiff)(delta).getLocalChange();

			IFileRevision baseVariant = localChange.getBeforeState();
			Resource remoteResource = createRemoteResource(localResource, baseVariant.getStorage(monitor));
			Resource baseResource = createRemoteResource(localResource, baseVariant.getStorage(monitor));

			ResourceDelta resourceDelta = new ResourceDelta(this, localResource, baseResource,
					remoteResource, delta);
			DELTA_TREE_BUILDER.buildDelta(resourceDelta, remoteResource, baseResource);
		}
	}

	/**
	 * This will try and load a resoruce corresponding to the given local variant from the given storage.
	 * 
	 * @param localVariant
	 *            Local variant of the resource we need read from <em>storage</em>.
	 * @param storage
	 *            The storage from which to read a remote resource.
	 * @return The loaded resource.
	 * @throws CoreException
	 *             Thrown if we did not manage to load the specified resource from the specified storage.
	 */
	private Resource loadRemoteResource(Resource localVariant, IStorage storage) throws CoreException {
		// "copy" the local resource set for the remote variant
		ResourceSet localResourceSet = localVariant.getResourceSet();

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.setPackageRegistry(localResourceSet.getPackageRegistry());
		resourceSet.setResourceFactoryRegistry(localResourceSet.getResourceFactoryRegistry());
		resourceSet.setURIConverter(localResourceSet.getURIConverter());

		URI localURI = localVariant.getURI();
		Resource remoteResource = resourceSet.createResource(localURI);

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
	 * Return all of the children of this delta.
	 * 
	 * @return All of the children of this delta.
	 */
	public List<EMFModelDelta> getChildren() {
		return new ArrayList<EMFModelDelta>(children);
	}

	/**
	 * Adds a new child to this delta, making sure that the new child's parent reference point to
	 * <code>this</code> aftewards.
	 * 
	 * @param child
	 *            The child that is to be added to this delta.
	 */
	public void addChild(EMFModelDelta child) {
		children.add(child);
		child.parent = this;
	}
}
