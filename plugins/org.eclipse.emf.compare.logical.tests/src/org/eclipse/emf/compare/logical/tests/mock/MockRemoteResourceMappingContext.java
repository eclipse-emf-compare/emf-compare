/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.tests.mock;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Mocks the behavior of a "remote" resource mapping context that simply returns the local file contents when
 * asked for remote or ancestor content.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MockRemoteResourceMappingContext extends RemoteResourceMappingContext {
	/** Allows us to mock both three way and two way contexts. */
	private boolean isThreeWay;

	/**
	 * Default constructor for a two way resource mapping context.
	 */
	public MockRemoteResourceMappingContext() {
		// Default constructor
	}

	/**
	 * Constructs a remote resource context that can be either two or three way.
	 * 
	 * @param threeWay
	 *            Tells us whether or not to provide ancestor content.
	 */
	public MockRemoteResourceMappingContext(boolean threeWay) {
		this();
		this.isThreeWay = threeWay;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#fetchBaseContents(org.eclipse.core.resources.IFile,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStorage fetchBaseContents(IFile file, IProgressMonitor monitor) throws CoreException {
		if (isThreeWay()) {
			return new MockFileRevision(file);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#fetchMembers(org.eclipse.core.resources.IContainer,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IResource[] fetchMembers(IContainer container, IProgressMonitor monitor) throws CoreException {
		Set<IResource> result = new HashSet<IResource>();
		IResource[] children = container.members();
		for (int i = 0; i < children.length; i++) {
			IResource resource = children[i];
			result.add(resource);
		}
		return result.toArray(new IResource[result.size()]);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#fetchRemoteContents(org.eclipse.core.resources.IFile,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStorage fetchRemoteContents(IFile file, IProgressMonitor monitor) throws CoreException {
		return new MockFileRevision(file);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#getProjects()
	 */
	@Override
	public IProject[] getProjects() {
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#hasLocalChange(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean hasLocalChange(IResource resource, IProgressMonitor monitor) throws CoreException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#hasRemoteChange(org.eclipse.core.resources.IResource,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean hasRemoteChange(IResource resource, IProgressMonitor monitor) throws CoreException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#isThreeWay()
	 */
	@Override
	public boolean isThreeWay() {
		return isThreeWay;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.mapping.RemoteResourceMappingContext#refresh(org.eclipse.core.resources.mapping.ResourceTraversal[],
	 *      int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void refresh(ResourceTraversal[] traversals, int flags, IProgressMonitor monitor)
			throws CoreException {
		// Does nothing
	}

}
