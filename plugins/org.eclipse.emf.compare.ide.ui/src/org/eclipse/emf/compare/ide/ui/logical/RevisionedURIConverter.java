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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.history.IFileHistory;
import org.eclipse.team.core.history.IFileHistoryProvider;
import org.eclipse.team.core.history.IFileRevision;

/**
 * This {@link URIConverter} will be used in order to fetch remote contents instead of local contents when
 * loading resources.
 * <p>
 * Let's suppose we are trying to compare "ecore.genmodel". This file depends on "ecore.ecore". However,
 * "ecore.ecore" evolves more than its corresponding genmodel since all changes made in this file are not
 * "breaking" for the genmodel. For example, version <i>1.19</i> of the genmodel was commited at the same time
 * as version <i>1.17</i> of the ecore. If I compare my own copy of the genmodel with the "latest from HEAD"
 * (version <i>1.19</i>), then this URI Converter will be used to resolve the proxy of that "remote" file with
 * the corresponding "remote" version of the ecore file. In this case, it should be version <i>1.20</i>
 * (latest non-breaking) of the ecore file, not the <i>1.17</i> that was commited along.
 * </p>
 * <p>
 * To this end, when creating a revisioned URI converter, we give it a file revision that corresponds to what
 * the user asked to compare, in the above example, the "latest from HEAD" of ecore.genmodel. We will fetch
 * the revision that is "above" that one. In the case of the "latest", there is none. if we had been given
 * version <i>1.18</i>, we would consider version <i>1.19</i>... And consider this "younger" revision to be
 * the "upper bound" of the revisions we'll resolve for dependent fragments (in the example above,
 * ecore.ecore).
 * </p>
 * <p>
 * This resolution strategy means that we assume the users never broke their logical model by committing some
 * fragments without the others (i.e. they never committed "ecore.ecore" without committing the
 * "ecore.genmodel" if there were breaking changes made).
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class RevisionedURIConverter extends StorageURIConverter {
	/** The revision of the base resource. This revision's timestamp will be used to resolve proxies. */
	private IFileRevision baseRevision;

	/** The actual Ifile from which was extracted {@link #baseRevision}. */
	private IFile baseFile;

	/**
	 * We need a "maximum" timestamp : see javadoc of the class.
	 * <p>
	 * <ul>
	 * <li><code>-2</code> is the unitialized value,</li>
	 * <li><code>-1</code> means we have no upper bound (use the latest revisions of the files),</li>
	 * <li>Any other value will be used to find the file revisions of our dependencies.</li>
	 * </p>
	 */
	private long maxTimestamp = -2;

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
		this.baseFile = findFile(getFileURI(baseRevision).toString());

		try {
			getLoadedRevisions().add(baseRevision.getStorage(new NullProgressMonitor()));
		} catch (CoreException e) {
			// We cannot find the storage of our base revision?
		}

		final RepositoryProvider repositoryProvider = RepositoryProvider.getProvider(baseFile.getProject());
		if (repositoryProvider == null) {
			return;
		}

		final IFileHistoryProvider historyProvider = repositoryProvider.getFileHistoryProvider();
		final IFileHistory history = historyProvider.getFileHistoryFor(baseFile, IFileHistoryProvider.NONE,
				new NullProgressMonitor());
		if (history != null) {
			// We'll search for the revision "above" the given one in order to determine our upper bound
			final IFileRevision[] revisions = history.getFileRevisions();
			final String baseID = baseRevision.getContentIdentifier();

			for (int i = 0; i < revisions.length && this.maxTimestamp == -2; i++) {
				if (baseID.equals(revisions[i].getContentIdentifier())) {
					if (i == 0) {
						this.maxTimestamp = -1;
					} else {
						this.maxTimestamp = revisions[i - 1].getTimestamp();
					}
				}
			}
		}
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
				// This file exists on the repository.
				IFileRevision soughtRevision = null;
				final IFileRevision[] revisions = history.getFileRevisions();
				for (int i = 0; i < revisions.length && soughtRevision == null; i++) {
					final IFileRevision revision = revisions[i];
					if (maxTimestamp < 0 || revision.getTimestamp() < maxTimestamp) {
						soughtRevision = revision;
					}
				}

				if (soughtRevision != null) {
					try {
						IStorage storage = soughtRevision.getStorage(new NullProgressMonitor());
						getLoadedRevisions().add(storage);
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
				final IFileState[] revisions = ((IFile)actualFile).getHistory(new NullProgressMonitor());
				for (int i = 0; i < revisions.length && soughtState == null; i++) {
					final IFileState revision = revisions[i];
					if (maxTimestamp < 0 || revision.getModificationTime() < maxTimestamp) {
						soughtState = revision;
					}
				}

				if (soughtState != null) {
					getLoadedRevisions().add(soughtState);
					stream = soughtState.getContents();
				} else {
					getLoadedRevisions().add((IFile)actualFile);
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
		final java.net.URI baseURI = getFileURI(baseRevision);
		java.net.URI targetURI = convertToURI(path);
		java.net.URI relativizedURI = baseURI.relativize(targetURI);
		final IPath relativizedPath;
		if (relativizedURI.getPath().length() == 0) {
			relativizedPath = new Path(targetURI.getPath());
		} else {
			relativizedPath = new Path(relativizedURI.getPath());
		}
		final String[] pathSegments = relativizedPath.segments();

		/*
		 * 'Repository' URIs cannot be found directly in the workspace : they are relative to the repository
		 * location. Let's try to find the IProject in which this file is located ...
		 */
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject container = null;

		int projectIndex = 0;
		for (; projectIndex < pathSegments.length && (container == null || !container.exists()); projectIndex++) {
			container = root.getProject(pathSegments[projectIndex]);
		}

		IFile file = null;
		if (container != null && container.exists()) {
			final IPath filePath = relativizedPath.removeFirstSegments(projectIndex);
			file = container.getFile(filePath);
		}

		return file;
	}

	private java.net.URI getFileURI(IFileRevision revision) {
		final java.net.URI baseURI = revision.getURI();
		if (baseURI != null) {
			return baseURI;
		}

		java.net.URI result = null;
		try {
			final IStorage storage = revision.getStorage(new NullProgressMonitor());
			final String name = revision.getName();

			IPath path = storage.getFullPath();
			final String lastSegment = path.lastSegment();
			if (!lastSegment.equals(name)) {
				final int nameIndex = lastSegment.indexOf(name);
				if (nameIndex != -1) {
					path = path.removeLastSegments(1);
					path = path.append(lastSegment.substring(0, nameIndex + name.length()));
				}
			}

			result = new java.net.URI(path.toString());
		} catch (CoreException e) {
			// FIXME log
		} catch (URISyntaxException e) {
			// FIXME log
		}
		return result;
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
}
