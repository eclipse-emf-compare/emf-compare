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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

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

	/** The set of files composing this mapping's logical model. */
	private final StorageTraversal traversal;

	/** The Model provider for which this mapping has been created. */
	private String providerId;

	/** We'll cache the "eclipse team" traversals in order to avoid multiple conversions. */
	private ResourceTraversal[] cachedTraversals;

	/**
	 * Instantiates our mapping given its underlying physical {@link IResource}.
	 * 
	 * @param resource
	 *            The physical resource of this mapping.
	 * @param traversal
	 *            The pre-computed local traversal composing this resource's logical model.
	 * @param providerId
	 *            The Model provider for which this mapping should be created.
	 */
	public EMFResourceMapping(IResource resource, StorageTraversal traversal, String providerId) {
		this.resource = checkNotNull(resource);
		this.traversal = checkNotNull(traversal);
		this.providerId = providerId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getModelObject()
	 */
	@Override
	public Object getModelObject() {
		return traversal;
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
		/*
		 * FIXME in time, the ResourceMappingContext will have to be used to invalidate the previously cached
		 * traversal if it changes in any way (assume a Cache<ResourceMappingContext, StorageTraversal>). EMF
		 * Compare itself will re-compute an accurate traversal, taking remote versions into account, later on
		 * in the comparison process. Other consumers of this mapping might not (for example, "replace with"
		 * or "move" actions might only use the info available from our cached "local" mapping, potentially
		 * causing issues. Needs to be investigated further.
		 */
		if (cachedTraversals == null) {
			if (traversal.getDiagnostic().getSeverity() >= Diagnostic.ERROR) {
				EMFCompareIDEUIPlugin.getDefault().getLog().log(
						BasicDiagnostic.toIStatus(traversal.getDiagnostic()));
				return createSingletonTraversal(resource);
			}

			cachedTraversals = convertCompareTraversal(traversal);
		}

		final ResourceTraversal[] traversals = new ResourceTraversal[cachedTraversals.length];
		System.arraycopy(cachedTraversals, 0, traversals, 0, traversals.length);
		return traversals;
	}

	private static ResourceTraversal[] createSingletonTraversal(IResource aResource) {
		final ResourceTraversal singletonTraversal = new ResourceTraversal(new IResource[] {aResource, },
				IResource.DEPTH_ONE, IResource.NONE);
		return new ResourceTraversal[] {singletonTraversal, };
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object other) {
		if (other instanceof EMFResourceMapping) {
			return traversal.getStorages().equals(((EMFResourceMapping)other).traversal.getStorages());
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return traversal.getStorages().hashCode();
	}

	/**
	 * Convert EMF Compare's traversals to Team ones.
	 * 
	 * @param traversal
	 *            The traversal to convert to Team.
	 * @return The converted traversals.
	 */
	private static ResourceTraversal[] convertCompareTraversal(StorageTraversal aTraversal) {
		final ResourceTraversal converted = (ResourceTraversal)aTraversal.getAdapter(ResourceTraversal.class);
		return new ResourceTraversal[] {converted, };
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getProjects()
	 */
	@Override
	public IProject[] getProjects() {
		final Set<IProject> projects = Sets.newLinkedHashSet();
		if (traversal.getDiagnostic().getSeverity() >= Diagnostic.ERROR) {
			return new IProject[] {resource.getProject(), };
		}

		for (IStorage storage : traversal.getStorages()) {
			if (storage instanceof IResource) {
				projects.add(((IResource)storage).getProject());
			}
		}
		final IProject[] projectArray = projects.toArray(new IProject[projects.size()]);
		return projectArray;
	}
}
