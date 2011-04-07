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

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.SynchronizationCompareAdapter;
import org.eclipse.ui.IMemento;

/**
 * This implementation of a {@link SynchronizationCompareAdapter} will be used to determine the difference
 * between two EMF logical models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFCompareAdapter extends SynchronizationCompareAdapter {
	/** Cache key for our saveable comparison buffer. */
	private static final String SAVEABLE_BUFFER = "org.eclipse.emf.compare.logical.saveable.buffer"; //$NON-NLS-1$

	/** Identifier of the model this adapter will compare. */
	private final String modelProviderId;

	/**
	 * Instantiates our adapter given the identifier of the logical model to compare.
	 * 
	 * @param modelProviderId
	 *            Identifier of the model this adapter will compare.
	 */
	public EMFCompareAdapter(String modelProviderId) {
		this.modelProviderId = modelProviderId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationCompareAdapter#asCompareInput(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object)
	 */
	@Override
	public ICompareInput asCompareInput(ISynchronizationContext context, Object o) {
		// FIXME
		return super.asCompareInput(context, o);
	}

	/**
	 * Initializes this compare adapter given the current synchronization context.
	 * 
	 * @param context
	 *            The context from which to retrieve comparison information.
	 * @param monitor
	 *            Monitor on which to display progress information.
	 */
	public void initialize(ISynchronizationContext context, IProgressMonitor monitor) {
		EMFModelDelta delta = EMFModelDelta.createDelta(context, modelProviderId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter#restore(org.eclipse.ui.IMemento)
	 */
	public ResourceMapping[] restore(IMemento memento) {
		// Don't restore
		return null;
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
