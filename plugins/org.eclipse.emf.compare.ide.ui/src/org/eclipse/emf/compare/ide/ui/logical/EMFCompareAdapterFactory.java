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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;

/**
 * This will be used to adapt EMF types to various parts of the logical model framework.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class EMFCompareAdapterFactory implements IAdapterFactory {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		Object adapter = null;
		if (adapterType == ISynchronizationCompareAdapter.class) {
			if (adaptableObject instanceof EMFModelProvider || adaptableObject instanceof EObject
					|| adaptableObject instanceof Resource) {
				adapter = new EMFCompareSynchronizationAdapter(EMFModelProvider.PROVIDER_ID);
			}
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
		return new Class[] {ISynchronizationCompareAdapter.class, };
	}
}
