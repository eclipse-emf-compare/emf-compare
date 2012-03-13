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

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.history.provider.FileRevision;

/**
 * Mocks the behavior of a repository's {@link IFileRevision} by simply using the local content of the file.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MockFileRevision extends FileRevision implements IStorage {
	/** The local file for which this mocks a remote revision behavior. */
	private final IFile file;

	/**
	 * Instantiates a mocking remote revision for the given file.
	 * 
	 * @param file
	 *            File for which we are to mock a remote revision.
	 */
	public MockFileRevision(IFile file) {
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return file.getAdapter(adapter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.core.history.IFileRevision#getStorage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStorage getStorage(IProgressMonitor monitor) throws CoreException {
		return file;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.core.history.IFileRevision#isPropertyMissing()
	 */
	public boolean isPropertyMissing() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.core.history.IFileRevision#withAllProperties(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IFileRevision withAllProperties(IProgressMonitor monitor) throws CoreException {
		return this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getContents()
	 */
	public InputStream getContents() throws CoreException {
		return file.getContents();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#getFullPath()
	 */
	public IPath getFullPath() {
		return file.getFullPath();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.team.core.history.IFileRevision#getName()
	 */
	public String getName() {
		return file.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IStorage#isReadOnly()
	 */
	public boolean isReadOnly() {
		return file.isReadOnly();
	}
}
