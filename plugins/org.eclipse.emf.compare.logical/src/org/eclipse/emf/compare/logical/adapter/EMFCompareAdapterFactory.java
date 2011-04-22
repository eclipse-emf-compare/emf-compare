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
package org.eclipse.emf.compare.logical.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.compare.logical.synchronization.EMFCompareAdapter;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;

/**
 * This will be used to adapt EMF types to various parts of the logical model framework.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFCompareAdapterFactory implements IAdapterFactory {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		Object adapter = null;
		if (adaptableObject instanceof EMFModelProvider
				&& adapterType == ISynchronizationCompareAdapter.class) {
			adapter = new EMFCompareAdapter(EMFModelProvider.PROVIDER_ID);
		}
		return adapter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {ISynchronizationCompareAdapter.class,};
	}
}
