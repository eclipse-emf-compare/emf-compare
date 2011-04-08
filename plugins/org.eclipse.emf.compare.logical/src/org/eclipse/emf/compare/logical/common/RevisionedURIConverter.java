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
package org.eclipse.emf.compare.logical.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.logical.Activator;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.history.IFileHistory;
import org.eclipse.team.core.history.IFileHistoryProvider;
import org.eclipse.team.core.history.IFileRevision;

/**
 * This {@link URIConverter} will be used in order to fetch remote contents instead of local contents when
 * loading resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class RevisionedURIConverter extends DelegatingURIConverter {
	/** The revision of the base resource. This revision's timestamp will be used to resolve proxies. */
	private final IFileRevision baseRevision;

	/**
	 * Instantiates our URI converter given its delegate.
	 * 
	 * @param delegate
	 *            Our delegate URI converter.
	 * @throws CoreException
	 *             This will be thrown if we couldn't adapt the given <em>storage</em> into a file revision.
	 */
	public RevisionedURIConverter(URIConverter delegate, IStorage storage) throws CoreException {
		super(delegate);

		Object revision = storage.getAdapter(IFileRevision.class);
		if (!(revision instanceof IFileRevision)) {
			revision = Platform.getAdapterManager().getAdapter(storage, IFileRevision.class);
		}

		if (revision instanceof IFileRevision) {
			baseRevision = (IFileRevision)revision;
		} else {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					EMFLogicalModelMessages.getString("storage.adapt.failure", storage.getFullPath()))); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.common.DelegatingURIConverter#createInputStream(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		InputStream stream = null;

		final URI normalizedUri = normalize(uri);
		// If this uri points to the plugins directory, load it locally
		if (normalizedUri.isPlatformPlugin() || normalizedUri.toString().matches("(\\.\\./)+?plugins/.*")) { //$NON-NLS-1$
			stream = super.createInputStream(normalizedUri, options);
		} else {
			// Otherwise, load it from the repository (resource might not yet (or no longer) exist locally)
			final IResource targetFile;
			if (normalizedUri.isPlatform()) {
				targetFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(normalizedUri.trimFragment().toPlatformString(true)));
			} else {
				/*
				 * FIXME Deresolve the URI against the workspace root, if it cannot be done, delegate to
				 * super.createInputStream()
				 */
				targetFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(normalizedUri.trimFragment().toString()));
			}

			if (targetFile != null) {
				stream = openRevisionStream(targetFile);
			} else {
				// FIXME The file URI couldn't be resolved in the workspace...
			}

			if (stream == null) {
				return super.createInputStream(uri, options);
			}
		}

		return stream;
	}

	/**
	 * Opens an input stream on the contents of the first revision of the file designed by <em>targetURI</em>
	 * which timestamp is inferior or equal to that of {@link #baseRevision}. If no such revision exist, we'll
	 * use the closest to the {@link #baseRevision}'s timestamp we can find, hoping that it does correspond to
	 * the sought revision.
	 * <p>
	 * Take good note that the <em>targetFile</em> may not exist locally. This handle will only serve in order
	 * to retrieve its repository provider.
	 * </p>
	 * 
	 * @param targetFile
	 *            The resource we seek a revision of.
	 * @return The opened input stream. May be <code>null</code> if we failed to open it.
	 */
	private InputStream openRevisionStream(IResource targetFile) {
		InputStream stream = null;
		final RepositoryProvider repositoryProvider = RepositoryProvider.getProvider(targetFile.getProject());

		if (repositoryProvider != null) {
			final IFileHistoryProvider historyProvider = repositoryProvider.getFileHistoryProvider();
			final IFileHistory history = historyProvider.getFileHistoryFor(targetFile,
					IFileHistoryProvider.NONE, new NullProgressMonitor());

			if (history != null) {
				/*
				 * This file exists on the repository. Try and find a revision that is older than the base
				 * revision, or the closest higher if no older revision can be found.
				 */
				IFileRevision soughtRevision = null;
				for (final IFileRevision revision : history.getFileRevisions()) {
					if (revision.getTimestamp() <= baseRevision.getTimestamp()) {
						soughtRevision = revision;
						break;
					} else if (soughtRevision == null
							|| soughtRevision.getTimestamp() >= revision.getTimestamp()) {
						soughtRevision = revision;
					}
				}

				if (soughtRevision != null) {
					try {
						stream = soughtRevision.getStorage(new NullProgressMonitor()).getContents();
					} catch (CoreException e) {
						// FIXME log this : failed to retrieve revision contents
					}
				}
			}
		}

		if (stream != null && targetFile.exists()) {
			// Either this file is not connected to a repository, or we failed to retrieve a revision.
			// Search through local history.
			try {
				IFileState soughtState = null;
				for (final IFileState state : ((IFile)targetFile).getHistory(new NullProgressMonitor())) {
					if (state.getModificationTime() <= baseRevision.getTimestamp()) {
						soughtState = state;
						break;
					}
				}

				if (soughtState != null) {
					stream = soughtState.getContents();
				} else {
					stream = ((IFile)targetFile).getContents();
				}
			} catch (CoreException e) {
				// FIXME log this : failed to retrieve local contents
			}
		}

		return stream;
	}
}
