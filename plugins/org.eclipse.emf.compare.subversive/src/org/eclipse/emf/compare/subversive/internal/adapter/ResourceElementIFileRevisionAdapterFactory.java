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

public class ResourceElementIFileRevisionAdapterFactory implements IAdapterFactory {

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
