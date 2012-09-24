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
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.utils.DelegatingURIConverter;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.history.IFileHistory;
import org.eclipse.team.core.history.IFileHistoryProvider;
import org.eclipse.team.core.history.IFileRevision;

/**
 * This {@link URIConverter} will be used in order to fetch remote contents instead of local contents when
 * loading resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class RevisionedURIConverter extends DelegatingURIConverter {
	/** The revision of the base resource. This revision's timestamp will be used to resolve proxies. */
	private IFileRevision baseRevision;

	/** Keeps references towards the revisions that we've loaded through this URI converter. */
	private Set<IStorage> loadedRevisions;

	/**
	 * Instantiates our URI converter given its delegate.
	 * 
	 * @param delegate
	 *            Our delegate URI converter.
	 * @param baseRevision
	 *            The base revision against which this URI converter should resolve URIs.
	 */
	public RevisionedURIConverter(URIConverter delegate, IFileRevision baseRevision) {
		super(delegate);
		this.baseRevision = baseRevision;
		this.loadedRevisions = Sets.newLinkedHashSet();
		try {
			this.loadedRevisions.add(baseRevision.getStorage(new NullProgressMonitor()));
		} catch (CoreException e) {
			// We cannot find the storage of our base revision?
		}
	}

	/**
	 * Allows clients of this API to retrieve the set of revisions that were loaded while resolving the
	 * resource set on which this converter is installed.
	 * 
	 * @return The set of revisions loaded through this converter.
	 */
	public Set<IStorage> getLoadedRevisions() {
		return loadedRevisions;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.DelegatingURIConverter#createInputStream(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@SuppressWarnings("resource")
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		InputStream stream = null;

		final URI normalizedUri = normalize(uri);
		// If this uri points to the plugins directory, load it directly
		if (normalizedUri.isPlatformPlugin() || normalizedUri.toString().matches("(\\.\\./)+?plugins/.*")) { //$NON-NLS-1$
			stream = super.createInputStream(normalizedUri, options);
		} else {
			// Otherwise, load it from the repository (resource might not yet (or no longer) exist locally)
			final IResource targetFile;
			if (normalizedUri.isPlatform()) {
				targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
						new Path(normalizedUri.trimFragment().toPlatformString(true)));
			} else {
				/*
				 * FIXME Deresolve the URI against the workspace root, if it cannot be done, delegate to
				 * super.createInputStream()
				 */
				targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
						new Path(normalizedUri.trimFragment().toString()));
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

	public InputStream createDefaultInpuStream(URI uri, Map<?, ?> options) throws IOException {
		final URI normalizedURI = normalize(uri);
		final URIHandler handler = getURIHandler(normalizedURI);
		loadedRevisions.add(new URIStorage(uri, handler));
		final Map<Object, Object> actualOptions = Maps.newLinkedHashMap();
		actualOptions.put(URIConverter.OPTION_URI_CONVERTER, RevisionedURIConverter.this);
		actualOptions.putAll(options);
		return handler.createInputStream(normalizedURI, actualOptions);
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
	@SuppressWarnings("resource")
	private InputStream openRevisionStream(IResource targetFile) {
		IResource actualFile = targetFile;
		if (!actualFile.exists()) {
			// Can we relativize its path according to the baseRevision?
			actualFile = findFile(actualFile.getFullPath().toString());
			if (actualFile == null) {
				actualFile = targetFile;
			}
		}

		InputStream stream = null;
		final RepositoryProvider repositoryProvider = RepositoryProvider.getProvider(actualFile.getProject());

		if (repositoryProvider != null) {
			final IFileHistoryProvider historyProvider = repositoryProvider.getFileHistoryProvider();
			final IFileHistory history = historyProvider.getFileHistoryFor(actualFile,
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
						IStorage storage = soughtRevision.getStorage(new NullProgressMonitor());
						loadedRevisions.add(storage);
						stream = storage.getContents();
					} catch (CoreException e) {
						// FIXME log this : failed to retrieve revision contents
					}
				}
			}
		}

		if (stream == null) {
			// Either this file is not connected to a repository, or we failed to retrieve a revision.
			// Search through local history.
			try {
				IFileState soughtState = null;
				for (final IFileState state : ((IFile)actualFile).getHistory(new NullProgressMonitor())) {
					if (state.getModificationTime() <= baseRevision.getTimestamp()) {
						soughtState = state;
						break;
					}
				}

				if (soughtState != null) {
					loadedRevisions.add(soughtState);
					stream = soughtState.getContents();
				} else {
					loadedRevisions.add((IFile)actualFile);
					stream = ((IFile)actualFile).getContents();
				}
			} catch (CoreException e) {
				// FIXME log this : failed to retrieve local contents
			}
		}

		return stream;
	}

	/**
	 * Tries and find an IFile corresponding to the given path.
	 * 
	 * @param path
	 *            The path for which we need an IFile.
	 * @return The IFile for the given path if we could find it, <code>null</code> otherwise.
	 */
	private IFile findFile(String path) {
		final java.net.URI baseURI = baseRevision.getURI();
		java.net.URI targetURI = convertToURI(path);
		java.net.URI relativizedURI = baseURI.relativize(targetURI);
		IFile file = null;
		if (!relativizedURI.equals(targetURI)) {
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(relativizedURI.toString()));
		} else {
			// git has a tendency to add the name of the repository before the name of the project, even for
			// platform:/resource
			final int indexOfSeparator = path.indexOf('/', 1);
			if (indexOfSeparator > 0) {
				targetURI = convertToURI(path.substring(indexOfSeparator));
				relativizedURI = baseURI.relativize(targetURI);
				file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(relativizedURI.toString()));
			}
		}
		if (file != null) {
			return file;
		}
		return null;
	}

	/**
	 * Silently converts the given {@code path} into an {@link java.net.URI}.
	 * 
	 * @param path
	 *            The path for which we need a java URI.
	 * @return The converted URI if the path could be parsed as a valid URI, <code>null</code> otherwise.
	 */
	private java.net.URI convertToURI(String path) {
		try {
			return new java.net.URI(path);
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private class URIStorage implements IStorage {
		private final URI uri;

		private final URIHandler handler;

		public URIStorage(URI uri, URIHandler handler) {
			this.uri = uri;
			this.handler = handler;
		}

		public Object getAdapter(Class adapter) {
			return null;
		}

		public InputStream getContents() throws CoreException {
			final Map<?, ?> options = Collections.singletonMap(URIConverter.OPTION_URI_CONVERTER,
					RevisionedURIConverter.this);
			try {
				return handler.createInputStream(uri, options);
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID, e
						.getMessage(), e));
			}
		}

		public IPath getFullPath() {
			return new Path(uri.toString());
		}

		public String getName() {
			return uri.lastSegment();
		}

		public boolean isReadOnly() {
			final Map<?, ?> options = Collections.singletonMap(URIConverter.OPTION_REQUESTED_ATTRIBUTES,
					Collections.singleton(URIConverter.ATTRIBUTE_READ_ONLY));
			final Map<String, ?> attributes = handler.getAttributes(uri, options);
			return Boolean.TRUE.equals(attributes.get(URIConverter.ATTRIBUTE_READ_ONLY));
		}

	}
}
