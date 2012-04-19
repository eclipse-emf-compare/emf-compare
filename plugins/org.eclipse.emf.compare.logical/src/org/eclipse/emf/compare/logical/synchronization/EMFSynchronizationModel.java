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
package org.eclipse.emf.compare.logical.synchronization;

import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.team.core.mapping.ISynchronizationContext;

/**
 * This class will have the responsibility of feeding an editor input with access to the full logical model of
 * all three sides of a comparison. It will mainly be used by the synchronization process.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class EMFSynchronizationModel {
	/** Identifier of the model provider for which this model has been created. */
	private String modelProviderId;

	/** Synchronization context of this model. */
	private ISynchronizationContext context;

	/** Keeps track of the left resource set for this context. */
	private ResourceSet leftResourceSet;

	/** Keeps track of the right resource set for this context. */
	private ResourceSet rightResourceSet;

	/** Keeps track of the origin resource set for this context. */
	private ResourceSet originResourceSet;

	/**
	 * Creates our synchronization model. Should only be called through
	 * {@link #createModel(ISynchronizationContext, String, IProgressMonitor)}.
	 * 
	 * @param context
	 *            Synchronization context of this model.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this model should be created.
	 */
	private EMFSynchronizationModel(ISynchronizationContext context, String modelProviderId) {
		this.modelProviderId = modelProviderId;
		this.context = context;
	}

	/**
	 * Initializes a synchronization model given the identifier of the model provider that required it and the
	 * current synchronization context.
	 * 
	 * @param context
	 *            Synchronization context of this model.
	 * @param modelProviderId
	 *            Identifier of the model provider for which this model should be created.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @return The created Synchronization model.
	 * @throws CoreException
	 *             Thrown when the comparison failed somehow.
	 */
	public static EMFSynchronizationModel createModel(ISynchronizationContext context,
			String modelProviderId, IProgressMonitor monitor) throws CoreException {
		final EMFSynchronizationModel model = new EMFSynchronizationModel(context, modelProviderId);

		model.initialize(monitor);
		context.getCache().put(EMFModelProvider.SYNCHRONIZATION_CACHE_KEY, model);

		return model;
	}

	/**
	 * This will return the cached model within the given context.
	 * 
	 * @param context
	 *            Context from which to retrieve the cached model.
	 * @return The cached model for this context.
	 */
	public static EMFSynchronizationModel getModel(ISynchronizationContext context) {
		return (EMFSynchronizationModel)context.getCache().get(EMFModelProvider.SYNCHRONIZATION_CACHE_KEY);
	}

	/**
	 * Disposes of this model.
	 */
	public void dispose() {
		/*
		 * FIXME we're nulling out the references, but the resource mapping and resource sets are not disposed
		 * ... where could we unload the resources?
		 */
		leftResourceSet = null;
		rightResourceSet = null;
		originResourceSet = null;
	}

	/**
	 * Returns the resource set containing the left logical model.
	 * 
	 * @return The resource set containing the left logical model.
	 */
	public ResourceSet getLeftResourceSet() {
		return leftResourceSet;
	}

	/**
	 * Returns the resource set containing the right logical model.
	 * 
	 * @return The resource set containing the right logical model.
	 */
	public ResourceSet getRightResourceSet() {
		return rightResourceSet;
	}

	/**
	 * Returns the resource set containing the origin logical model.
	 * 
	 * @return The resource set containing the origin logical model.
	 */
	public ResourceSet getOriginResourceSet() {
		return originResourceSet;
	}

	/**
	 * This will be called internally to create or reset the logical model for a given synchronization
	 * context.
	 * 
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @throws CoreException
	 *             Thrown if we did not manage to load the resources.
	 */
	private void initialize(IProgressMonitor monitor) throws CoreException {
		dispose();
		// FIXME monitor.subTask("comparing models")

		// Retrieve all three resource sets from the scope
		ResourceMapping[] mappings = EMFModelProvider.getAdditionalMappings(context);
		if (mappings == null) {
			mappings = context.getScope().getMappings();
		}

		for (ResourceMapping mapping : mappings) {
			if (modelProviderId.equals(mapping.getModelProviderId()) && mapping instanceof EMFResourceMapping) {
				EMFResourceMapping emfMapping = (EMFResourceMapping)mapping;

				ResourceSet left = emfMapping.getLeftResourceSet();
				ResourceSet right = emfMapping.getRightResourceSet();
				ResourceSet origin = emfMapping.getOriginResourceSet();

				// If any one of these is null, continue on to the next mapping
				if (left == null || right == null || origin == null) {
					// Continue
				} else if (leftResourceSet == null
						|| leftResourceSet.getResources().size() < left.getResources().size()) {
					leftResourceSet = left;
					rightResourceSet = right;
					originResourceSet = origin;
				}
			}
		}

		/*
		 * If all three resource sets were null on all mappings, try to force their resolution (called when
		 * using right-click > compare with... actions from an EMF model editor).
		 */
		if (leftResourceSet == null) {
			// Seek for an EMFResourceMapping
			EMFResourceMapping emfMapping = null;
			for (int i = 0; i < mappings.length && emfMapping == null; i++) {
				ResourceMapping mapping = mappings[i];
				if (modelProviderId.equals(mapping.getModelProviderId())
						&& mapping instanceof EMFResourceMapping) {
					emfMapping = (EMFResourceMapping)mapping;
				}
			}

			if (emfMapping != null) {
				emfMapping.forceResolving(context.getScope().getContext(), monitor);

				leftResourceSet = emfMapping.getLeftResourceSet();
				rightResourceSet = emfMapping.getRightResourceSet();
				originResourceSet = emfMapping.getOriginResourceSet();
			}
		}
	}
}
