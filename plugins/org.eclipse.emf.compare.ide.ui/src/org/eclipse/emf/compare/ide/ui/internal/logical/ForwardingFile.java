/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.content.IContentDescription;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ForwardingFile extends ForwardingResource implements IFile {

	private final IFile delegate;

	public ForwardingFile(IFile iFile) {
		delegate = iFile;
	}

	public void appendContents(InputStream source, boolean force, boolean keepHistory,
			IProgressMonitor monitor) throws CoreException {
		delegate().appendContents(source, force, keepHistory, monitor);
	}

	public void appendContents(InputStream source, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		delegate().appendContents(source, updateFlags, monitor);
	}

	public void create(InputStream source, boolean force, IProgressMonitor monitor) throws CoreException {
		delegate().create(source, force, monitor);
	}

	public void create(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException {
		delegate().create(source, updateFlags, monitor);
	}

	public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		delegate().createLink(localLocation, updateFlags, monitor);
	}

	public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
		delegate().createLink(location, updateFlags, monitor);
	}

	public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
		delegate().delete(force, keepHistory, monitor);
	}

	public String getCharset() throws CoreException {
		return delegate().getCharset();
	}

	public String getCharset(boolean checkImplicit) throws CoreException {
		return delegate().getCharset(checkImplicit);
	}

	public String getCharsetFor(Reader reader) throws CoreException {
		return delegate().getCharsetFor(reader);
	}

	public IContentDescription getContentDescription() throws CoreException {
		return delegate().getContentDescription();
	}

	public InputStream getContents() throws CoreException {
		return delegate().getContents();
	}

	public InputStream getContents(boolean force) throws CoreException {
		return delegate().getContents(force);
	}

	@Deprecated
	public int getEncoding() throws CoreException {
		return delegate().getEncoding();
	}

	public IFileState[] getHistory(IProgressMonitor monitor) throws CoreException {
		return delegate().getHistory(monitor);
	}

	public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
		delegate().move(destination, force, keepHistory, monitor);
	}

	@Deprecated
	public void setCharset(String newCharset) throws CoreException {
		delegate().setCharset(newCharset);
	}

	public void setCharset(String newCharset, IProgressMonitor monitor) throws CoreException {
		delegate().setCharset(newCharset, monitor);
	}

	public void setContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
		delegate().setContents(source, force, keepHistory, monitor);
	}

	public void setContents(IFileState source, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
		delegate().setContents(source, force, keepHistory, monitor);
	}

	@Override
	public IResourceProxy createProxy() {
		return delegate().createProxy();
	}

	public void setContents(InputStream source, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		delegate().setContents(source, updateFlags, monitor);
	}

	public void setContents(IFileState source, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		delegate().setContents(source, updateFlags, monitor);
	}

	public IFile getDelegate() {
		return delegate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.ForwardingResource#delegate()
	 */
	@Override
	protected IFile delegate() {
		return delegate;
	}
}
