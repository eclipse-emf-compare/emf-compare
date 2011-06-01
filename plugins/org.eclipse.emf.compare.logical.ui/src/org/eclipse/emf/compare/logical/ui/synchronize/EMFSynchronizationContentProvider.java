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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.compare.logical.EMFLogicalModelMessages;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.compare.logical.synchronization.EMFDelta;
import org.eclipse.emf.compare.logical.synchronization.EMFModelDelta;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;
import org.eclipse.team.ui.mapping.SynchronizationContentProvider;

/**
 * This will be used to provide content information to the synchronize view's navigator when asked about EMF
 * synchronization state.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFSynchronizationContentProvider extends SynchronizationContentProvider {
	/** This will be used to determine icons and labels of the EObjects. */
	private final AdapterFactoryContentProvider delegateContentProvider;

	/** Initialization is a long running process; and we do not want to initialize this more than once. */
	private boolean isInitializing;

	/**
	 * Instantiates our content provider.
	 */
	public EMFSynchronizationContentProvider() {
		this.delegateContentProvider = new AdapterFactoryContentProvider(AdapterUtils.getAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		delegateContentProvider.dispose();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#getDelegateContentProvider()
	 */
	@Override
	protected ITreeContentProvider getDelegateContentProvider() {
		return delegateContentProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#isInitialized(org.eclipse.team.core.mapping.ISynchronizationContext)
	 */
	@Override
	protected boolean isInitialized(ISynchronizationContext context) {
		return !isInitializing && context.getCache().get(EMFModelProvider.SYNCHRONIZATION_CACHE_KEY) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#requestInitialization(org.eclipse.team.core.mapping.ISynchronizationContext)
	 */
	@Override
	protected void requestInitialization(final ISynchronizationContext context) {
		if (!isInitializing) {
			isInitializing = true;
			Job emfSynchronizationJob = new Job(EMFLogicalModelMessages.getString("synchronize.job.label")) { //$NON-NLS-1$
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					EMFCompareSynchronizationAdapter emfCompareAdapter = null;
					ModelProvider modelProvider = getModelProvider();
					if (modelProvider instanceof EMFModelProvider) {
						Object adapter = modelProvider.getAdapter(ISynchronizationCompareAdapter.class);
						if (adapter instanceof EMFCompareSynchronizationAdapter) {
							emfCompareAdapter = (EMFCompareSynchronizationAdapter)adapter;
						}
					}

					if (emfCompareAdapter != null) {
						try {
							emfCompareAdapter.initialize(context, monitor);
						} catch (CoreException e) {
							// FIXME we couldn't carry on with the comparison. Log and let Eclipse do its job
						}
					}

					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							refresh();
						}
					});

					isInitializing = false;
					return Status.OK_STATUS;
				}
			};
			emfSynchronizationJob.schedule();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#getChildrenInContext(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object, java.lang.Object[])
	 */
	@Override
	protected Object[] getChildrenInContext(ISynchronizationContext context, Object parent, Object[] children) {
		Object cachedDelta = context.getCache().get(EMFModelProvider.SYNCHRONIZATION_CACHE_KEY);
		if (cachedDelta instanceof EMFModelDelta) {
			EMFModelDelta delta = (EMFModelDelta)cachedDelta;
			List<Object> childrenInScope = new ArrayList<Object>();
			for (Object child : children) {
				EMFDelta childDelta = delta.getChildDeltaFor(child);
				if (childDelta != null
						&& (childDelta.getKind() == IDiff.NO_CHANGE || includeDirection(childDelta
								.getDirection()))) {
					childrenInScope.add(child);
				}
			}
			return childrenInScope.toArray(new Object[childrenInScope.size()]);
		}
		return super.getChildrenInContext(context, parent, children);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#getTraversals(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object)
	 */
	@Override
	protected ResourceTraversal[] getTraversals(ISynchronizationContext context, Object object) {
		// We're taking care of the traversal in getChildrenInContext()
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#getModelProviderId()
	 */
	@Override
	protected String getModelProviderId() {
		return EMFModelProvider.PROVIDER_ID;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#getModelRoot()
	 */
	@Override
	protected Object getModelRoot() {
		ResourceSet modelRoot = null;
		// Try and find the current resource set
		ResourceMapping[] mappings = getScope().getMappings();
		for (ResourceMapping mapping : mappings) {
			if (EMFModelProvider.PROVIDER_ID.equals(mapping.getModelProviderId())
					&& mapping instanceof EMFResourceMapping) {
				EMFResourceMapping emfMapping = (EMFResourceMapping)mapping;
				ResourceSet local = emfMapping.getLocalResourceSet();

				if (modelRoot == null || modelRoot.getResources().size() < local.getResources().size()) {
					modelRoot = local;
				}
			}
		}
		return modelRoot;
	}
}
