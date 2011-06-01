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
package org.eclipse.emf.compare.logical.ui.synchronize;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.compare.logical.synchronization.EMFModelDelta;
import org.eclipse.emf.compare.logical.ui.LogicalModelCompareInput;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.SynchronizationCompareAdapter;
import org.eclipse.ui.IMemento;

/**
 * This implementation of a {@link SynchronizationCompareAdapter} will be used to determine the difference
 * between two EMF logical models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
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
		EMFModelDelta delta = EMFModelDelta.getDelta(context);
		return delta != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationCompareAdapter#asCompareInput(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object)
	 */
	@Override
	public ICompareInput asCompareInput(ISynchronizationContext context, Object object) {
		EMFModelDelta delta = EMFModelDelta.getDelta(context);
		if (delta == null) {
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
				delta = EMFModelDelta.getDelta(context);
			} catch (CoreException e) {
				// FIXME log
			}
		}

		ICompareInput input = null;
		if (delta != null) {
			if (object instanceof EObject) {
				input = asCompareInput(context, delta, (EObject)object);
			} else if (object instanceof Resource) {
				input = asCompareInput(context, delta, (Resource)object);
			} else {
				input = asCompareInput(delta);
			}
		}
		return input;
	}

	/**
	 * Creates a new compare input for the given delta.
	 * 
	 * @return The created compare input.
	 */
	private ICompareInput asCompareInput(EMFModelDelta delta) {
		ComparisonSnapshot snapshot = delta.getComparisonSnapshot();
		if (snapshot instanceof ComparisonResourceSetSnapshot) {
			return new LogicalModelCompareInput((ComparisonResourceSetSnapshot)snapshot);
		}
		return new LogicalModelCompareInput((ComparisonResourceSnapshot)snapshot);
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
	private ICompareInput asCompareInput(ISynchronizationContext context, EMFModelDelta modelDelta,
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
	private ICompareInput asCompareInput(ISynchronizationContext context, EMFModelDelta modelDelta,
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
		EMFModelDelta.createDelta(context, modelProviderId, monitor);
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
