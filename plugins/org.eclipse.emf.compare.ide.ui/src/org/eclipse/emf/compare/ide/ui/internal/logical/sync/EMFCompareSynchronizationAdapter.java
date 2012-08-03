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
package org.eclipse.emf.compare.ide.ui.internal.logical.sync;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.ide.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.logical.sync.EMFSynchronizationModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.SynchronizationCompareAdapter;
import org.eclipse.ui.IMemento;

/**
 * This implementation of a {@link SynchronizationCompareAdapter} will be used to determine the difference
 * between two EMF logical models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareSynchronizationAdapter extends SynchronizationCompareAdapter {
	/** Identifier of the model this adapter will compare. */
	private final String modelProviderId;

	/**
	 * Instantiates our adapter given the identifier of the logical model to compare.
	 * 
	 * @param modelProviderId
	 *            Identifier of the model this adapter will compare.
	 */
	public EMFCompareSynchronizationAdapter(String modelProviderId) {
		this.modelProviderId = modelProviderId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationCompareAdapter#hasCompareInput(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object)
	 */
	@Override
	public boolean hasCompareInput(ISynchronizationContext context, Object object) {
		EMFSynchronizationModel model = EMFSynchronizationModel.getModel(context);
		return model != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationCompareAdapter#asCompareInput(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object)
	 */
	@Override
	public ICompareInput asCompareInput(ISynchronizationContext context, Object object) {
		EMFSynchronizationModel model = EMFSynchronizationModel.getModel(context);
		if (model == null) {
			/*
			 * We'll be here when using "Compare With" actions on a folder, then "open in compare editor" for
			 * one of the files that are part of an EMF model.
			 */
			try {
				final ResourceMapping[] mappings = context.getScope().getMappings();
				if (object instanceof IResource
						&& mappings.length == 1
						&& "org.eclipse.core.resources.modelProvider".equals(mappings[0].getModelProviderId())) { //$NON-NLS-1$
					ResourceMapping[] additionalMappings = ((EMFModelProvider)ModelProvider
							.getModelProviderDescriptor(modelProviderId).getModelProvider()).getMappings(
							(IResource)object, context.getScope().getContext(), new NullProgressMonitor());
					EMFModelProvider.cacheAdditionalMappings(context, additionalMappings);
				}
				initialize(context, new NullProgressMonitor());
				model = EMFSynchronizationModel.getModel(context);
			} catch (CoreException e) {
				// FIXME log
			}
		}

		ICompareInput input = null;
		if (model != null) {
			if (object instanceof EObject) {
				input = asCompareInput(context, model, (EObject)object);
			} else if (object instanceof Resource) {
				input = asCompareInput(context, model, (Resource)object);
			} else {
				input = asCompareInput(model);
			}
		}
		return input;
	}

	/**
	 * Creates a new compare input for the given delta.
	 * 
	 * @param delta
	 *            The delta for which we need a compare input.
	 * @return The created compare input.
	 */
	private ICompareInput asCompareInput(EMFSynchronizationModel delta) {
		// return new ResourceCompareInput
		return null;
	}

	/**
	 * Creates a new Compare input for the given EObject given the pre-computed delta.
	 * 
	 * @param context
	 *            Context from which to extract the saveable buffer.
	 * @param modelDelta
	 *            Comparison delta from which to focus on a given EObject.
	 * @param object
	 *            The object to focus on.
	 * @return The created compare input.
	 */
	private ICompareInput asCompareInput(ISynchronizationContext context, EMFSynchronizationModel modelDelta,
			EObject object) {
		// FIXME is there a way to set the initial selection of the compare editor? for now, ignore it.
		return asCompareInput(modelDelta);
	}

	/**
	 * Creates a new Compare input for the given EMF Resource given the pre-computed delta.
	 * 
	 * @param context
	 *            Context from which to extract the saveable buffer.
	 * @param modelDelta
	 *            Comparison delta from which to focus on a given EMF Resource.
	 * @param resource
	 *            The EMF Resource to focus on.
	 * @return The created compare input.
	 */
	private ICompareInput asCompareInput(ISynchronizationContext context, EMFSynchronizationModel modelDelta,
			Resource resource) {
		// FIXME is there a way to set the initial selection of the compare editor? for now, ignore it.
		return asCompareInput(modelDelta);
	}

	/**
	 * Initializes this compare adapter given the current synchronization context.
	 * 
	 * @param context
	 *            The context from which to retrieve comparison information.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 * @throws CoreException
	 *             Thrown if the comparison failed somehow.
	 */
	public void initialize(ISynchronizationContext context, IProgressMonitor monitor) throws CoreException {
		EMFSynchronizationModel.createModel(context, modelProviderId, monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter#restore(org.eclipse.ui.IMemento)
	 */
	public ResourceMapping[] restore(IMemento memento) {
		return new ResourceMapping[0];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter#save(org.eclipse.core.resources.mapping.ResourceMapping[],
	 *      org.eclipse.ui.IMemento)
	 */
	public void save(ResourceMapping[] mappings, IMemento memento) {
		// Don't save
	}
}
