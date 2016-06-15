/*******************************************************************************
 * Copyright (c) 2011, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * This {@link URIConverter} will be used in order to fetch remote content instead of local content when
 * loading resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class RevisionedURIConverter extends StorageURIConverter {
	/** The accessor that will provide us with resource content. */
	private final IStorageProviderAccessor storageAccessor;

	/** The side we are currently resolving. */
	private final DiffSide side;

	/**
	 * We can have <code>null</code> input streams from here (if the resource does not exist on the remote
	 * sides). However, this is not supported by the resource implementations (Resource#load(...)), which
	 * would fail in NPEs. Since we can only detect that late (when actually trying for the input stream), we
	 * have to "prefetch" this input stream from the resource set and check, from there, before creating the
	 * resource, that we will actually be able to load it. This field will be used to keep track of the
	 * prefetched stream so that we can avoid loading it twice.
	 */
	private final ConcurrentMap<URI, InputStream> prefetchedStreams = new ConcurrentHashMap<URI, InputStream>();

	/**
	 * Instantiates our URI converter given its delegate.
	 * 
	 * @param delegate
	 *            Our delegate URI converter.
	 * @param storageAccessor
	 *            The accessor that will provide synchronization information for the loaded files.
	 * @param side
	 *            The side we are currently resolving.
	 */
	public RevisionedURIConverter(URIConverter delegate, IStorageProviderAccessor storageAccessor,
			DiffSide side) {
		super(delegate);
		this.storageAccessor = storageAccessor;
		this.side = side;
	}

	/**
	 * Prefetches the input stream for the given URI if any. Note that the stream will be left opened and
	 * cached by this URIConverter, only to be closed when the associated "load resource" is called.
	 * <p>
	 * See comments on {@link #prefetchedStreams}. This is used to avoid loading a single URI more than once.
	 * </p>
	 * 
	 * @param uri
	 *            see {@link #createInputStream(URI, Map)}
	 * @param options
	 *            see {@link #createInputStream(URI, Map)}
	 * @return <code>true</code> if there is an input stream accessible for the given uri, <code>false</code>
	 *         otherwise.
	 * @throws IOException
	 *             if an IO problem occurs.
	 * @see #createInputStream(URI, Map)
	 * @see #prefetchedStreams
	 */
	// Suppressing the warning. It is the responsibility of the caller to then use #createInputStream(...)
	// somehow and close the stream then.
	@SuppressWarnings("resource")
	public boolean prefetchStream(URI uri, Map<?, ?> options) throws IOException {
		InputStream stream = createInputStream(uri, options);
		if (stream != null) {
			prefetchedStreams.put(uri, stream);
		}
		return stream != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.utils.StorageURIConverter#createInputStream(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		InputStream stream = prefetchedStreams.remove(uri);
		if (stream != null) {
			return stream;
		}

		final URI normalizedUri = normalize(uri);
		// If this uri points to the plugins directory, load it directly
		if (normalizedUri.isPlatformPlugin() || normalizedUri.toString().matches("(\\.\\./)+?plugins/.*")) { //$NON-NLS-1$
			// This uri will be used later on to determine whether the storage has already been loaded or not.
			// Use the unmodified URI instead of the normalized one here.
			stream = super.createInputStream(uri, options);
		} else {
			// Otherwise, load it from the repository (resource might not yet (or no longer) exist locally)
			final IResource targetFile = ResourceUtil.getResourceFromURI(normalizedUri);
			if (targetFile != null) {
				stream = openRevisionStream(targetFile);
			} else {
				stream = super.createInputStream(uri, options);
			}
		}

		return stream;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.utils.StorageURIConverter#exists(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		boolean exists = false;
		try {
			final URI normalizedUri = normalize(uri);
			IStorageProvider storageProvider = storageAccessor
					.getStorageProvider(ResourceUtil.getResourceFromURI(normalizedUri), side);
			if (storageProvider != null) {
				exists = storageProvider.getStorage(new NullProgressMonitor()) != null;
			} else {
				exists = super.exists(normalizedUri, options);
			}
		} catch (CoreException e) {
			EMFCompareIDEUIPlugin.getDefault().log(IStatus.ERROR, e.getMessage());
		}
		return exists;
	}

	/**
	 * Opens an input stream on the contents of the given file as provided by the registered
	 * {@link #subscriber}.
	 * <p>
	 * Take good note that the <em>targetFile</em> may not exist locally.
	 * </p>
	 * 
	 * @param targetFile
	 *            The resource we seek a revision of.
	 * @return The opened input stream. May be <code>null</code> if we failed to open it.
	 */
	private InputStream openRevisionStream(IResource targetFile) {
		if (storageAccessor == null) {
			// FIXME can this happen? does it matter? Fall back to local content for now.
		} else {
			try {
				final IStorageProvider provider = storageAccessor.getStorageProvider(targetFile, side);

				if (provider != null) {
					final IStorage storage = provider.getStorage(new NullProgressMonitor());
					if (storage != null
							&& (!(storage instanceof IResource) || ((IResource)storage).exists())) {
						getLoadedRevisions().add(storage);
						return storage.getContents();
					}
				}
			} catch (CoreException e) {
				logError(e);
			}
		}

		return null;
	}

	/**
	 * Logs the given exception as an error.
	 * 
	 * @param e
	 *            The exception we need to log.
	 */
	private static void logError(Exception e) {
		final IStatus status = new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID, e.getMessage(), e);
		EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
	}
}
