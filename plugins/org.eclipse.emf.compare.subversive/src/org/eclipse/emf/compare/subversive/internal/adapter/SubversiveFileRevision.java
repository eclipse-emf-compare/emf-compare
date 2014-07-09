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

import com.google.common.base.Preconditions;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.history.provider.FileRevision;
import org.eclipse.team.svn.core.PathForURL;
import org.eclipse.team.svn.core.resource.IRepositoryResource;
import org.eclipse.team.svn.ui.compare.ResourceCompareInput.ResourceElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class SubversiveFileRevision extends FileRevision {

	private IRepositoryResource repositoryResource;

	private ResourceElement resourceElement;

	public SubversiveFileRevision(IRepositoryResource repositoryResource, ResourceElement resourceElement) {
		this.repositoryResource = Preconditions.checkNotNull(repositoryResource);
		this.resourceElement = Preconditions.checkNotNull(resourceElement);
	}

	@Override
	public URI getURI() {
		URI uri;
		try {
			uri = new URI(repositoryResource.getUrl());
			return uri;
		} catch (URISyntaxException e) {
			Bundle bundle = FrameworkUtil.getBundle(SubversiveFileRevision.class);
			ILog log = Platform.getLog(bundle);
			IStatus status = new Status(IStatus.ERROR, bundle.getSymbolicName(), e.getMessage(), e);
			log.log(status);
		}
		return null;
	}

	@Override
	public IFileRevision withAllProperties(IProgressMonitor monitor) throws CoreException {
		return this;
	}

	@Override
	public boolean isPropertyMissing() {
		return false;
	}

	@Override
	public String getName() {
		return resourceElement.getName();
	}

	@Override
	public IStorage getStorage(IProgressMonitor monitor) throws CoreException {
		return new IStorage() {

			public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
				return null;
			}

			public boolean isReadOnly() {
				return !resourceElement.isEditable();
			}

			public String getName() {
				return resourceElement.getName();
			}

			public IPath getFullPath() {
				String url = repositoryResource.getUrl();
				IPath path = new PathForURL(url, true);
				return path;
			}

			public InputStream getContents() throws CoreException {
				return resourceElement.getContents();
			}
		};
	}

}
