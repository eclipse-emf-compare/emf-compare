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
package org.eclipse.emf.compare.logical.model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This will be used to map EMF {@link Resource}s to their physical {@link IResource}s.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFResourceMapping extends ResourceMapping {
	/** The physical resource of this mapping. */
	private final IResource resource;

	/** The EMF Resource of this mapping. */
	private final Resource emfResource;

	/** The Model provider for which this mapping has been created. */
	private final String providerId;

	/**
	 * Instantiates our mapping given both the physical {@link IResource} and the logical {@link Resource}.
	 * 
	 * @param resource
	 *            The physical resource of this mapping.
	 * @param emfResource
	 *            The EMF Resource of this mapping.
	 * @param providerId
	 *            The Model provider for which this mapping should be created.
	 */
	public EMFResourceMapping(IResource resource, Resource emfResource, String providerId) {
		this.resource = resource;
		this.emfResource = emfResource;
		this.providerId = providerId;
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
	 * Returns this mapping's physical resource.
	 * 
	 * @return This mapping's physical resource.
	 */
	public IResource getIResource() {
		return resource;
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
		Set<IResource> physicalResources = resolvePhysicalResources();

		Set<IProject> projects = new LinkedHashSet<IProject>(physicalResources.size());
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
		Set<IResource> physicalResources = resolvePhysicalResources();

		ResourceTraversal traversal = new ResourceTraversal(
				physicalResources.toArray(new IResource[physicalResources.size()]), IResource.DEPTH_ONE,
				IResource.NONE);

		return new ResourceTraversal[] {traversal,};
	}

	/**
	 * This will resolve all logical (EMF) {@link Resource}s that constitute this model, then resolve the
	 * corresponding physical {@link IResource}s and return them.
	 * 
	 * @return The list of all physical resources that constitute this model.
	 */
	private Set<IResource> resolvePhysicalResources() {
		Set<Resource> logicalResources = resolveLogicalResources();

		Set<IResource> physicalResources = new LinkedHashSet<IResource>(logicalResources.size());
		for (Resource eResource : logicalResources) {
			if (eResource == emfResource) {
				physicalResources.add(resource);
			} else {
				URI uri = eResource.getURI();
				if (uri != null) {
					IPath path = new Path(uri.path());
					IResource iResource = ResourcesPlugin.getWorkspace().getRoot()
							.findMember(path.removeFirstSegments(1));
					if (iResource != null && iResource.exists() && iResource.isAccessible()) {
						physicalResources.add(iResource);
					}
				}
			}
		}

		return physicalResources;
	}

	/**
	 * This will try and resolve all logical resources that constitute this model.
	 * 
	 * @return The list of all logical resources that constitute this model.
	 */
	private Set<Resource> resolveLogicalResources() {
		EcoreUtil.resolveAll(emfResource.getResourceSet());

		Set<Resource> result = new LinkedHashSet<Resource>();
		result.addAll(emfResource.getResourceSet().getResources());

		return result;
	}
}
