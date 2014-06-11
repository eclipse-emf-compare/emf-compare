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
import org.eclipse.core.runtime.NullProgressMonitor;
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
	/** The physical resource underlying this mapping. */
	private ForwardingFile file;

	/** The Model provider for which this mapping has been created. */
	private String providerId;

	/** We'll cache the "eclipse team" traversals in order to avoid multiple conversions. */
	private ResourceTraversal[] cachedTraversals;

	/**
	 * Instantiates our mapping given its underlying physical {@link IResource}.
	 * 
	 * @param file
	 *            The physical resource of this mapping.
	 * @param providerId
	 *            The Model provider for which this mapping should be created.
	 */
	public EMFResourceMapping(IFile file, String providerId) {
		checkNotNull(file);
		this.file = new ForwardingFile(file);
		this.providerId = providerId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.ResourceMapping#getModelObject()
	 */
	@Override
	public Object getModelObject() {
		return file;
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
		final IProgressMonitor progressMonitor;
		if (monitor == null) {
			progressMonitor = new NullProgressMonitor();
		} else {
			progressMonitor = monitor;
		}

		if (cachedTraversals == null) {
			try {
				StorageTraversal emfTraversal = resolveEMFTraversal(context, progressMonitor);
				if (emfTraversal.getDiagnostic().getSeverity() >= Diagnostic.ERROR) {
					EMFCompareIDEUIPlugin.getDefault().getLog().log(
							BasicDiagnostic.toIStatus(emfTraversal.getDiagnostic()));
					return createSingletonTraversal(file.getDelegate());
				}

				cachedTraversals = convertCompareTraversal(emfTraversal);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return createSingletonTraversal(file.getDelegate());
			}
		}

		final ResourceTraversal[] traversals = new ResourceTraversal[cachedTraversals.length];
		System.arraycopy(cachedTraversals, 0, traversals, 0, traversals.length);
		return traversals;
	}

	private ResourceTraversal[] createSingletonTraversal(IFile resource) {
		final ResourceTraversal singletonTraversal = new ResourceTraversal(new IResource[] {resource, },
				IResource.DEPTH_ONE, IResource.NONE);
		return new ResourceTraversal[] {singletonTraversal, };
	}

	// FIXME delete once we have a true model object to return from #getModelObject()
	@Override
	public boolean equals(Object other) {
		if (other instanceof EMFResourceMapping) {
			return ((EMFResourceMapping)other).file.getFullPath().equals(file.getFullPath());
		}
		return false;
	}

	// FIXME delete once we have a true model object to return from #getModelObject()
	@Override
	public int hashCode() {
		return file.getFullPath().hashCode();
	}

	/**
	 * Resolve the traversal underlying this mapping. This will iterate over all resources needed by the
	 * logical model of {@link #file} and return their list. Do note that the returned traversal might contain
	 * references to files that do not exist in the workspace (in case of remote file revisions that have been
	 * locally deleted).
	 * 
	 * @param context
	 *            The context that may be used to resolve remote file revisions.
	 * @param monitor
	 *            Used to display progress information to the user.
	 * @return The resolved traversal.
	 */
	private StorageTraversal resolveEMFTraversal(ResourceMappingContext context, IProgressMonitor monitor)
			throws InterruptedException {
		/*
		 * Using the context (if it is an instance of RemoteResourceMappingContext) would give better
		 * results... but would also be far longer. This is mainly used prior to synchronization, and
		 * detecting a traversal for a file that's been removed locally but is present on the repository is
		 * not necessary at this point.
		 */
		return EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry().getBestResolverFor(file)
				.resolveLocalModel(file, monitor);
	}

	/**
	 * Convert EMF Compare's traversals to Team ones. Extracted here to avoid usage of
	 * org.eclipse.emf.compare.ide.utils.ResourceTraversal anywhere else.
	 * 
	 * @param traversal
	 *            The traversal to convert to Team.
	 * @return The converted traversals.
	 */
	private ResourceTraversal[] convertCompareTraversal(StorageTraversal traversal) {
		final ResourceTraversal converted = (ResourceTraversal)traversal.getAdapter(ResourceTraversal.class);
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
		if (cachedTraversals == null) {
			// We need to resolve our traversals with no real info, assume local
			try {
				final ResourceTraversal[] traversals = getTraversals(ResourceMappingContext.LOCAL_CONTEXT,
						new NullProgressMonitor());
				for (int i = 0; i < traversals.length; i++) {
					final IResource[] resources = traversals[i].getResources();
					for (int j = 0; j < resources.length; j++) {
						projects.add(resources[j].getProject());
					}
				}
			} catch (CoreException e) {
				// FIXME log.
				// only use the underlying resource's project
				projects.add(file.getProject());
			}
		} else {
			// use cached information
			final ResourceTraversal[] traversals = this.cachedTraversals;
			for (int i = 0; i < traversals.length; i++) {
				final IResource[] resources = traversals[i].getResources();
				for (int j = 0; j < resources.length; j++) {
					projects.add(resources[j].getProject());
				}
			}
		}
		final IProject[] projectArray = projects.toArray(new IProject[projects.size()]);
		return projectArray;
	}
}
