/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.team.subversive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ui.team.AbstractTeamHandler;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;
import org.eclipse.team.svn.core.connector.ISVNConnector;
import org.eclipse.team.svn.core.connector.ISVNProgressMonitor;
import org.eclipse.team.svn.core.connector.SVNConnectorException;
import org.eclipse.team.svn.core.connector.SVNLogEntry;
import org.eclipse.team.svn.core.connector.SVNRevision;
import org.eclipse.team.svn.core.operation.SVNNullProgressMonitor;
import org.eclipse.team.svn.core.resource.ILocalResource;
import org.eclipse.team.svn.core.resource.IRepositoryLocation;
import org.eclipse.team.svn.core.resource.IRepositoryResource;
import org.eclipse.team.svn.core.utility.SVNUtility;
import org.eclipse.team.svn.ui.compare.ResourceCompareInput.ResourceElement;

/**
 * This class will handle the specific parts of loading resources for a comparison via subversive. We needed
 * this in order to use subversive-specific classes to retrieve the exact URIs of the resource and not load
 * them from streams (which wouldn't be mergeable).
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class SubversiveTeamHandler extends AbstractTeamHandler {
	/** Indicates that the left resource is remote. */
	private boolean leftIsRemote;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.team.AbstractTeamHandler#isLeftRemote()
	 */
	@Override
	public boolean isLeftRemote() {
		return leftIsRemote;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.team.AbstractTeamHandler#loadResources(org.eclipse.compare.structuremergeviewer.ICompareInput)
	 */
	@Override
	public boolean loadResources(ICompareInput input) throws IOException, CoreException {
		final ITypedElement left = input.getLeft();
		final ITypedElement right = input.getRight();
		final ITypedElement ancestor = input.getAncestor();

		if (left instanceof ResourceElement && right instanceof ResourceElement) {
			final ResourceSet leftResourceSet = new ResourceSetImpl();
			final ResourceSet rightResourceSet = new ResourceSetImpl();

			if (((ResourceElement)left).getRepositoryResource().getSelectedRevision() == SVNRevision.WORKING) {
				leftResource = EclipseModelUtils.load(
						((ResourceElement)left).getLocalResource().getResource().getFullPath(),
						leftResourceSet).eResource();
			} else {
				leftResource = ModelUtils.load(((ResourceElement)left).getContents(),
						((ResourceElement)left).getName(), leftResourceSet).eResource();
				leftIsRemote = true;
			}

			try {
				rightResource = ModelUtils.load(((ResourceElement)right).getContents(), right.getName(),
						rightResourceSet).eResource();
				final IRepositoryResource resource = ((ResourceElement)right).getRepositoryResource();
				final ILocalResource local = ((ResourceElement)ancestor).getLocalResource();
				rightResourceSet.setURIConverter(new RevisionedURIConverter(resource, local));
			} catch (final IOException e) {
				// We couldn't load the remote resource. Considers it has been added to the repository
				rightResource = ModelUtils.createResource(URI.createURI(right.getName()));
				// Set the left as remote to disable merge facilities
				leftIsRemote = true;
			}

			if (ancestor != null) {
				final ResourceSet ancestorResourceSet = new ResourceSetImpl();
				try {
					ancestorResource = ModelUtils.load(((IStreamContentAccessor)ancestor).getContents(),
							ancestor.getName(), ancestorResourceSet).eResource();
					final IRepositoryResource resource = ((ResourceElement)ancestor).getRepositoryResource();
					final ILocalResource local = ((ResourceElement)ancestor).getLocalResource();
					ancestorResourceSet.setURIConverter(new RevisionedURIConverter(resource, local));
				} catch (final IOException e) {
					// Couldn't load ancestor resource, create an empty one
					ancestorResource = ModelUtils.createResource(URI.createURI(ancestor.getName()));
				}
			}
			return true;
		}
		return false;
	}

	/* (non-javadoc) most of the behavior here has been copied from EMF 2.4 "URIHandlerImpl". */
	/**
	 * This implementation of an URIConverter allows us to properly resolve cross-model links towards the
	 * actual revision that should be loaded.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 * @since 1.0
	 */
	private class RevisionedURIConverter extends URIConverterImpl {
		/** The revision of the base model. This revision's timestamp will be used to resolve proxies. */
		private final IRepositoryResource baseRevision;

		/** The local resource currently compared. */
		private final ILocalResource localResource;

		/**
		 * This default constructor will add our own URI Handler to the top of the handlers list.
		 * 
		 * @param revision
		 *            Revision of the base model.
		 * @param local
		 *            Local resource currently being compared.
		 */
		public RevisionedURIConverter(IRepositoryResource revision, ILocalResource local) {
			super();
			baseRevision = revision;
			localResource = local;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.resource.URIConverter#createInputStream(org.eclipse.emf.common.util.URI)
		 */
		@Override
		public InputStream createInputStream(URI uri) {
			try {
				// We'll have to change the EMF URI to find the IFile it points to
				URI deresolvedURI = uri;
				if (uri.isRelative()) {
					deresolvedURI = uri.resolve(URI.createURI(baseRevision.getUrl()));
				}
				final IRepositoryLocation location = baseRevision.getRepositoryLocation();
				final ISVNConnector proxy = location.acquireSVNProxy();
				final IRepositoryResource target = location.asRepositoryFile(deresolvedURI.toString(), false);
				final long svnOptions = ISVNConnector.Options.NONE;
				final String[] revProps = ISVNConnector.DEFAULT_LOG_ENTRY_PROPS;
				final ISVNProgressMonitor monitor = new SVNNullProgressMonitor();

				final SVNLogEntry[] entries = SVNUtility.logEntries(proxy, SVNUtility
						.asEntryReference(deresolvedURI.toString()), SVNRevision.HEAD, SVNRevision
						.fromNumber(0), svnOptions, revProps, 0, monitor);

				StringOutputStream stream = null;
				if (baseRevision.getSelectedRevision() != SVNRevision.BASE) {
					final long baseTimestamp = baseRevision.getInfo().lastChangedDate;
					for (final SVNLogEntry entry : entries) {
						if (entry.date <= baseTimestamp) {
							target.setPegRevision(SVNRevision.fromNumber(entry.revision));
							target.setSelectedRevision(SVNRevision.fromNumber(entry.revision));

							stream = new StringOutputStream();
							final int bufferSize = 2048;
							proxy.streamFileContent(SVNUtility.getEntryRevisionReference(target), bufferSize,
									stream, monitor);
							break;
						}
					}
				} else {
					// FIXME find a way to determine revision number or timestamp of the BASE revision
					final long baseRevisionNumber = localResource.getBaseRevision();
					stream = new StringOutputStream();
					final int bufferSize = 2048;
					proxy.streamFileContent(SVNUtility.getEntryRevisionReference(target), bufferSize, stream,
							monitor);
				}
				if (stream != null)
					return new StringInputStream(stream.getWriter().getBuffer().toString());
			} catch (final SVNConnectorException e) {
				// FIXME log this
			}
			return null;
		}
	}

	/**
	 * This will allow us to open an InputStream over a String.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 * @since 1.0
	 */
	private class StringInputStream extends InputStream {
		/** StringReader towards which calls will be delegated. */
		private final Reader reader;

		/**
		 * Instantiates the InputStream.
		 * 
		 * @param content
		 *            String from which to read bytes.
		 */
		public StringInputStream(String content) {
			reader = new StringReader(content);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.io.InputStream#read()
		 */
		@Override
		public int read() throws IOException {
			return reader.read();
		}
	}

	/**
	 * This will allow us to stream a revision's content to a String.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 * @since 1.0
	 */
	private class StringOutputStream extends OutputStream {
		/** StringWriter towards which calls will be delegated. */
		private final StringWriter writer = new StringWriter();

		/**
		 * Instantiates the OutputStream.
		 */
		public StringOutputStream() {
			// increases visibility
		}

		/**
		 * Returns the underlying StringWriter.
		 * 
		 * @return The underlying StringWriter.
		 */
		public StringWriter getWriter() {
			return writer;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.io.OutputStream#write(int)
		 */
		@Override
		public void write(int b) throws IOException {
			writer.write(b);
		}
	}
}
