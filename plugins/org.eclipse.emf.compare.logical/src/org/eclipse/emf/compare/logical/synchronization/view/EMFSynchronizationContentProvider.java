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
package org.eclipse.emf.compare.logical.synchronization.view;

import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.compare.logical.common.EMFLogicalModelMessages;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.compare.logical.synchronization.EMFCompareAdapter;
import org.eclipse.emf.compare.logical.synchronization.EMFSaveableBuffer;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Display;
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
		super.dispose();
		delegateContentProvider.dispose();
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
		return context.getCache().get(EMFSaveableBuffer.SYNCHRONIZATION_CACHE_KEY) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#requestInitialization(org.eclipse.team.core.mapping.ISynchronizationContext)
	 */
	@Override
	protected void requestInitialization(final ISynchronizationContext context) {
		Job emfSynchronizationJob = new Job(EMFLogicalModelMessages.getString("synchronize.job.label")) { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				EMFCompareAdapter emfCompareAdapter = null;
				ModelProvider modelProvider = getModelProvider();
				if (modelProvider instanceof EMFModelProvider) {
					Object adapter = modelProvider.getAdapter(ISynchronizationCompareAdapter.class);
					if (adapter instanceof EMFCompareAdapter) {
						emfCompareAdapter = (EMFCompareAdapter)adapter;
					}
				}

				if (emfCompareAdapter != null) {
					emfCompareAdapter.initialize(context, monitor);
				}

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						refresh();
					}
				});

				return Status.OK_STATUS;
			}
		};
		emfSynchronizationJob.schedule();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.mapping.SynchronizationContentProvider#getChildrenInContext(org.eclipse.team.core.mapping.ISynchronizationContext,
	 *      java.lang.Object, java.lang.Object[])
	 */
	@Override
	protected Object[] getChildrenInContext(ISynchronizationContext context, Object parent, Object[] children) {
		// FIXME Override this by browsing the EMF Delta
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
		// Try and find the current resource set
		ResourceMapping[] mappings = getScope().getMappings();
		for (ResourceMapping mapping : mappings) {
			if (EMFModelProvider.PROVIDER_ID.equals(mapping.getModelProviderId())
					&& mapping instanceof EMFResourceMapping) {
				Object modelObject = ((EMFResourceMapping)mapping).getModelObject();
				if (modelObject instanceof Resource) {
					return ((Resource)modelObject).getResourceSet();
				}
			}
		}
		return null;
	}
}
