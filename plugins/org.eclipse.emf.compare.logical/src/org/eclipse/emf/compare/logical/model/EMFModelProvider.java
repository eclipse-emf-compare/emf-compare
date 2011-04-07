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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.logical.common.EMFResourceUtil;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This implementation of a {@link ModelProvider} will be used to provide the logical model associated with
 * EMF models.
 * <p>
 * Concretely, an EMF model can span multiple physical resources (fragmented models); this model provider can
 * be used to find all of these associated physical resources.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFModelProvider extends ModelProvider {
	/** ID of this model provider. Must match the definition from the plugin.xml. */
	public static final String PROVIDER_ID = "org.eclipse.emf.compare.model.provider"; //$NON-NLS-1$

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
		/*
		 * TODO check that this also works when synchronizing a folder containing a fragmented model which
		 * fragment(s) are located in another folder.
		 */
		// TODO what additional information can the context provide us?
		if (resource instanceof IFile && resource.exists() && resource.isAccessible()) {
			return getMappings((IFile)resource, monitor);
		}
		return super.getMappings(resource, context, monitor);
	}

	/**
	 * Return the {@link ResourceMapping}s that cover the given {@link IFile}.
	 * 
	 * @param file
	 *            The file for which to determine mappings.
	 * @param monitor
	 *            A progress monitor, or <code>null</code> if progress reporting is not desired.
	 * @return The {@link ResourceMapping}s that cover the given {@link IFile}.
	 */
	private ResourceMapping[] getMappings(IFile file, IProgressMonitor monitor) {
		List<ResourceMapping> mappings = new ArrayList<ResourceMapping>();
		// FIXME find a way to dispose of this resource set
		Resource resource = EMFResourceUtil.getResource(file, createLogicalModelResourceSet());
		if (resource != null) {
			mappings.add(new EMFResourceMapping(file, resource, PROVIDER_ID));
		}
		return mappings.toArray(new ResourceMapping[mappings.size()]);
	}

	/**
	 * Creates the resource set that should be used to load this model's resources.
	 * 
	 * @return The resource set that should be used to load this model's resources.
	 */
	private ResourceSet createLogicalModelResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		return resourceSet;
	}
}
