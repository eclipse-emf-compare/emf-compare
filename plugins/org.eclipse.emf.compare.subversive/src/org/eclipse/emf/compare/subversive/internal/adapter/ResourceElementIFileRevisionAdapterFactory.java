/*******************************************************************************
 * Copyright (c) 2014 Jan Reimann and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jan Reimann - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.subversive.internal.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.svn.core.resource.IRepositoryResource;
import org.eclipse.team.svn.ui.compare.ResourceCompareInput.ResourceElement;

/**
 * The adapter factory for {@link IFileRevision}s.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceElementIFileRevisionAdapterFactory implements IAdapterFactory {

	/**
	 * Return the adapter for the given Object.
	 * 
	 * @param adaptableObject
	 *            The object to adapt
	 * @param adapterType
	 *            The type to adapt the given object in
	 * @return The adapted object
	 */
	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (IFileRevision.class.equals(adapterType) && adaptableObject instanceof ResourceElement) {
			ResourceElement resourceElement = (ResourceElement)adaptableObject;
			IRepositoryResource repositoryResource = resourceElement.getRepositoryResource();
			if (repositoryResource != null) {
				IFileRevision fileRevision = new SubversiveFileRevision(repositoryResource, resourceElement);
				return fileRevision;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList() {
		return new Class[] {IFileRevision.class };
	}

}
