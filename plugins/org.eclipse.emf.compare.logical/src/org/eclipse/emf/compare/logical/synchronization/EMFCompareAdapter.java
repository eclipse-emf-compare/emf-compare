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

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.logical.EMFCompareInput;
import org.eclipse.emf.compare.logical.EObjectTypedElement;
import org.eclipse.emf.compare.logical.LogicalModelCompareInput;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
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
		EMFModelDelta delta = EMFModelDelta.getDelta(context);

		ICompareInput input = null;
		if (delta != null) {
			if (o instanceof EObject) {
				input = asCompareInput(context, delta, (EObject)o);
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
		EMFDelta delta = modelDelta.getChildDeltaFor(object);
		if (delta == null) {
			return asCompareInput(context, modelDelta);
		}

		EMFSaveableBuffer buffer = EMFSaveableBuffer.getBuffer(context);
		ILabelProvider labelProvider = new AdapterFactoryLabelProvider(AdapterUtils.getAdapterFactory());

		Object localObject = delta.getLocal();
		Object remoteObject = delta.getRemote();
		Object ancestorObject = delta.getAncestor();

		ITypedElement local = null;
		ITypedElement remote = null;
		ITypedElement ancestor = null;

		if (localObject instanceof EObject) {
			local = new EObjectTypedElement((EObject)local, labelProvider);
		}
		if (remoteObject instanceof EObject) {
			remote = new EObjectTypedElement((EObject)remote, labelProvider);
		}
		if (ancestorObject instanceof EObject) {
			ancestor = new EObjectTypedElement((EObject)ancestor, labelProvider);
		}

		return new EMFCompareInput(buffer, delta, ancestor, local, remote);
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
		EMFSaveableBuffer.createBuffer(context);
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
