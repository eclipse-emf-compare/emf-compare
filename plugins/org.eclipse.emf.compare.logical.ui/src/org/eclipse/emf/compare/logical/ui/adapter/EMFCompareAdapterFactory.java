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
package org.eclipse.emf.compare.logical.ui.adapter;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.compare.logical.model.EMFModelProvider;
import org.eclipse.emf.compare.logical.model.EMFResourceMapping;
import org.eclipse.emf.compare.logical.ui.synchronize.EMFCompareSynchronizationAdapter;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;

/**
 * This will be used to adapt EMF types to various parts of the logical model framework.
 * <p>
 * Note that the adapting of various objects to {@link ResourceMapping} will not be configured by default,
 * each project will have to adapt his own {@link Resource}s and {@link EObject}s to {@link ResourceMapping}s
 * if desired; adapting the generic superclasses that are "{@link Resource}" and "{@link EObject}" would be
 * too intrusive.
 * </p>
 * <p>
 * A third-party plugin can use this adapter factory for his own objects. For example
 * 
 * <pre>
 * &lt;extension point="org.eclipse.core.runtime.adapters">
 *    &lt;factory
 *          adaptableType="org.eclipse.emf.compare.example.library.writers.util.WritersResourceImpl"
 *          class="org.eclipse.emf.compare.logical.adapter.EMFCompareAdapterFactory">
 *       &lt;adapter
 *             type="org.eclipse.core.resources.mapping.ResourceMapping">
 *       &lt;/adapter>
 *    &lt;/factory>
 * &lt;extension>
 * </pre>
 * 
 * will allow "double-click" comparison from the synchronize view on "Writer" objects.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
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
				adapter = new EMFCompareSynchronizationAdapter(EMFModelProvider.PROVIDER_ID);
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
		IResource iResource = EclipseModelUtils.findIResource(eResource);
		if (iResource instanceof IFile) {
			// We'll use our own Resource Set to resolve the logical model
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource emfResource = resourceSet.getResource(eResource.getURI(), true);
			return new EMFResourceMapping((IFile)iResource, emfResource, EMFModelProvider.PROVIDER_ID);
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
			Resource eResource = null;
			try {
				eResource = EclipseModelUtils.getResource(iFile, new ResourceSetImpl());
			} catch (IOException e) {
				// Will return 'null'
			}
			if (eResource != null) {
				return new EMFResourceMapping(iFile, eResource, EMFModelProvider.PROVIDER_ID);
			}
		}
		return null;
	}
}
