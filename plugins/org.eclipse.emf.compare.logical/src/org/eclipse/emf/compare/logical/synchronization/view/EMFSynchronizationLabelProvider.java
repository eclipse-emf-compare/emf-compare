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
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.logical.synchronization.EMFDelta;
import org.eclipse.emf.compare.logical.synchronization.EMFModelDelta;
import org.eclipse.emf.compare.logical.synchronization.EMFSaveableBuffer;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.ui.mapping.SynchronizationLabelProvider;

/**
 * This will be used to provide label information to the synchronize view's navigator when asked about EMF
 * synchronization state.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFSynchronizationLabelProvider extends SynchronizationLabelProvider {
	/** Our delegate label provider. */
	private ILabelProvider delegateLabelProvider;

	/**
	 * Instantiates our label provider.
	 */
	public EMFSynchronizationLabelProvider() {
		delegateLabelProvider = new DelegateLabelProvider(AdapterUtils.getAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.synchronize.AbstractSynchronizeLabelProvider#getDelegateLabelProvider()
	 */
	@Override
	protected ILabelProvider getDelegateLabelProvider() {
		return delegateLabelProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.synchronize.AbstractSynchronizeLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		delegateLabelProvider.dispose();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.ui.synchronize.AbstractSynchronizeLabelProvider#getDiff(java.lang.Object)
	 */
	@Override
	protected IDiff getDiff(Object element) {
		Object cachedDelta = getContext().getCache().get(EMFSaveableBuffer.SYNCHRONIZATION_CACHE_KEY);
		if (cachedDelta instanceof EMFModelDelta) {
			EMFDelta elementDelta = ((EMFModelDelta)cachedDelta).getChildDeltaFor(element);
			if (elementDelta != null) {
				return elementDelta.getDiff();
			}
		}
		return super.getDiff(element);
	}

	/**
	 * The objects displayed by this label provider's associated viewer may be wrapped by the logical
	 * resources framework. Thus, a plain {@link AdapterFactoryLabelProvider} could not find the proper label
	 * and icons. We'll use this particular implementation to unwrap the objects before providing them to the
	 * {@link AdapterFactoryLabelProvider}.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
	 */
	private class DelegateLabelProvider extends AdapterFactoryLabelProvider {
		/**
		 * Default constructor.
		 * 
		 * @param adapterFactory
		 *            The wrapped adapter factory.
		 */
		public DelegateLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object object) {
			if (object instanceof ModelProvider) {
				return ((ModelProvider)object).getDescriptor().getLabel();
			}
			return super.getText(unwrap(object));
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#getImage(java.lang.Object)
		 */
		@Override
		public Image getImage(Object object) {
			return super.getImage(unwrap(object));
		}

		/**
		 * Unwraps the given Object if it is part of the logical resources framework.
		 * 
		 * @param object
		 *            Object we are to unwrap.
		 * @return The unwrapped object if it is part of the logical resources framework, <em>object</em>
		 *         itself otherwise.
		 */
		private Object unwrap(Object object) {
			if (object instanceof ResourceMapping) {
				return ((ResourceMapping)object).getModelObject();
			}
			return object;
		}
	}
}
