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
package org.eclipse.emf.compare.ide.ui.logical;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.SynchronizationCompareAdapter;
import org.eclipse.ui.IMemento;

/**
 * This implementation of a {@link SynchronizationCompareAdapter} will be used to determine the difference
 * between two EMF logical models.
 * <p>
 * This is mainly used by comparisons through the Synchronize perspective.
 * </p>
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
	 * @see org.eclipse.team.ui.mapping.SynchronizationCompareAdapter#asCompareInput(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object)
	 */
	@Override
	public ICompareInput asCompareInput(ISynchronizationContext context, Object o) {
		return super.asCompareInput(context, o);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter#save(org.eclipse.core.resources.mapping.ResourceMapping[],
	 *      org.eclipse.ui.IMemento)
	 */
	public void save(ResourceMapping[] mappings, IMemento memento) {
		// Does not support save ... yet?
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter#restore(org.eclipse.ui.IMemento)
	 */
	public ResourceMapping[] restore(IMemento memento) {
		// Does not support save ... yet?
		return new ResourceMapping[0];
	}
}
