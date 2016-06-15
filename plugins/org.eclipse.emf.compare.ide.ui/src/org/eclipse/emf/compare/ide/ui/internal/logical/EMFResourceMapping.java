/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;

/**
 * This will be used to represent an EMF resource's mapping. It will allow us to properly resolve the whole
 * logical model of that EMF resource and return the proper traversal so that 'model-aware' tools can work on
 * the whole logical model instead of considering only single files.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFResourceMapping extends ResourceMapping {
	/** The resource from which this mapping has been initialized. */
	private final IResource resource;

	/** Context with which this mapping was initially created. */
	private final ResourceMappingContext initialContext;

	/** The actual logical model underlying this mapping. */
	private final SynchronizationModel synchronizationModel;

	/** The Model provider for which this mapping has been created. */
	private final String providerId;

	/** Keeps track of the latest model computed from this mapping. */
	private SynchronizationModel latestModel;

	/**
	 * Instantiates our mapping given its underlying physical {@link IResource}.
	 * 
	 * @param resource
	 *            The physical resource of this mapping.
	 * @param initialContext
	 *            The context with which this mapping was initially created.
	 * @param traversal
	 *            The pre-computed local traversal composing this resource's logical model.
	 * @param providerId
	 *            The Model provider for which this mapping should be created.
	 */
	public EMFResourceMapping(IResource resource, ResourceMappingContext initialContext,
			SynchronizationModel synchronizationModel, String providerId) {
		this.resource = checkNotNull(resource);
		this.initialContext = checkNotNull(initialContext);
		this.synchronizationModel = checkNotNull(synchronizationModel);
		this.providerId = checkNotNull(providerId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getModelObject()
	 */
	@Override
	public Object getModelObject() {
		return synchronizationModel;
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
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getTraversals(org.eclipse.core.resources.mapping.ResourceMappingContext,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ResourceTraversal[] getTraversals(ResourceMappingContext context, IProgressMonitor monitor)
			throws CoreException {
		final SynchronizationModel syncModel;
		if (context == initialContext || !(resource instanceof IFile)) {
			syncModel = synchronizationModel;
		} else {
			SynchronizationModel temp = null;
			try {
				temp = ((EMFModelProvider)getModelProvider()).getOrComputeLogicalModel((IFile)resource,
						context, monitor);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				temp = synchronizationModel;
			}
			syncModel = temp;
		}

		latestModel = syncModel;

		return convertToTraversal(syncModel);
	}

	private ResourceTraversal[] convertToTraversal(SynchronizationModel syncModel) {
		final Set<IResource> resources = syncModel.getResources();
		final IResource[] resourcesArray = resources.toArray(new IResource[resources.size()]);
		return new ResourceTraversal[] {
				new ResourceTraversal(resourcesArray, IResource.DEPTH_ZERO, IResource.NONE), };
	}

	private static ResourceTraversal[] createSingletonTraversal(IResource aResource) {
		final ResourceTraversal singletonTraversal = new ResourceTraversal(new IResource[] {aResource, },
				IResource.DEPTH_ONE, IResource.NONE);
		return new ResourceTraversal[] {singletonTraversal, };
	}

	/**
	 * Returns the latest synchronization model built from a call to
	 * {@link #getTraversals(ResourceMappingContext, IProgressMonitor)}.
	 * <p>
	 * Note that this will return <code>null</code> until
	 * {@link #getTraversals(ResourceMappingContext, IProgressMonitor)} has been called at least once.
	 * </p>
	 * <p>
	 * This internal API is not meant to be used outside of EMF Compare.
	 * </p>
	 * 
	 * @return The latest synchronization model built from a call to
	 *         {@link #getTraversals(ResourceMappingContext, IProgressMonitor)}.
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final SynchronizationModel getLatestModel() {
		return latestModel;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object other) {
		if (other instanceof EMFResourceMapping) {
			return synchronizationModel.equals(((EMFResourceMapping)other).synchronizationModel);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return synchronizationModel.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getProjects()
	 */
	@Override
	public IProject[] getProjects() {
		final Set<IProject> projects = Sets.newLinkedHashSet();
		if (synchronizationModel.getDiagnostic().getSeverity() >= Diagnostic.ERROR) {
			return new IProject[] {resource.getProject(), };
		}

		for (IResource res : synchronizationModel.getResources()) {
			projects.add(res.getProject());
		}
		final IProject[] projectArray = projects.toArray(new IProject[projects.size()]);
		return projectArray;
	}
}
