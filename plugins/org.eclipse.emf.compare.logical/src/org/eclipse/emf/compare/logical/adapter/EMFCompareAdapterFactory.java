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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.compare.logical.common.EMFResourceUtil;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.compare.logical.synchronization.EMFCompareAdapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
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
		if (adapterType == ISynchronizationCompareAdapter.class) {
			if (adaptableObject instanceof EMFModelProvider || adaptableObject instanceof EObject
					|| adaptableObject instanceof Resource) {
				adapter = new EMFCompareAdapter(EMFModelProvider.PROVIDER_ID);
			}
		} else if (adapterType == ResourceMapping.class) {
			if (adaptableObject instanceof EObject) {
				adapter = createResourceMapping((EObject)adaptableObject);
			} else if (adaptableObject instanceof Resource) {
				adapter = createResourceMapping((Resource)adaptableObject);
			} else if (adaptableObject instanceof IFile) {
				adapter = createResourceMapping((IFile)adaptableObject);
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
		return new Class[] {ISynchronizationCompareAdapter.class,};
	}

	/**
	 * This will try and create a resource mapping for the given EObject.
	 * 
	 * @param eObject
	 *            The EObject for which we need a resource mapping.
	 * @return The resource mapping if it could be created, <code>null</code> otherwise.
	 */
	private ResourceMapping createResourceMapping(EObject eObject) {
		Resource eResource = eObject.eResource();
		if (eResource != null) {
			return createResourceMapping(eResource);
		}
		return null;
	}

	/**
	 * This will try and create a resource mapping for the given Resource.
	 * 
	 * @param eResource
	 *            The EMF Resource for which we need a resource mapping.
	 * @return The resource mapping if it could be created, <code>null</code> otherwise.
	 */
	private ResourceMapping createResourceMapping(Resource eResource) {
		IResource iResource = EMFResourceUtil.findIResource(eResource);
		if (iResource instanceof IFile) {
			return new EMFResourceMapping((IFile)iResource, eResource, EMFModelProvider.PROVIDER_ID);
		}
		return null;
	}

	/**
	 * This will try and create a resource mapping for the given IFile.
	 * 
	 * @param iFile
	 *            The IFile for which we need a resource mapping.
	 * @return The resource mapping if it could be created, <code>null</code> otherwise.
	 */
	private ResourceMapping createResourceMapping(IFile iFile) {
		if (iFile.exists() && iFile.isAccessible()) {
			Resource eResource = EMFResourceUtil.getResource(iFile, new ResourceSetImpl());
			if (eResource != null) {
				return new EMFResourceMapping(iFile, eResource, EMFModelProvider.PROVIDER_ID);
			}
		}
		return null;
	}
}
