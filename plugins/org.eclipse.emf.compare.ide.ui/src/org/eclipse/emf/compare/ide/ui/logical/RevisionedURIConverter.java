/*******************************************************************************
 * Copyright (c) 2011, 2013 Obeo.
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
import java.util.Map;

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
import org.eclipse.emf.compare.ide.utils.StorageURIConverter;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IThreeWayDiff;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.mapping.provider.ResourceDiff;
import org.eclipse.team.core.subscribers.Subscriber;

/**
 * This {@link URIConverter} will be used in order to fetch remote contents instead of local contents when
 * loading resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class RevisionedURIConverter extends StorageURIConverter {
	/** The Subscriber from which we'll retrieve remote content for referenced resources. */
	private Subscriber subscriber;

	/** The side we are currently resolving. */
	private DiffSide side;

	/**
	 * Instantiates our URI converter given its delegate.
	 * 
	 * @param delegate
	 *            Our delegate URI converter.
	 * @param subscriber
	 *            The Subscriber that will provide synchronization information for these files.
	 * @param side
	 *            The side we are currently resolving.
	 */
	public RevisionedURIConverter(URIConverter delegate, Subscriber subscriber, DiffSide side) {
		super(delegate);
		this.subscriber = subscriber;
		this.side = side;
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
				IPath platformString = new Path(normalizedUri.trimFragment().toPlatformString(true));
				IResource temp = ResourcesPlugin.getWorkspace().getRoot().getFile(platformString);
				if (!temp.exists() && normalizedUri.isPlatformResource() && platformString.segmentCount() > 1) {
					// We tend to get here with unresolvable URIs with git; as it tends to give URIs of the
					// form platform:/resource/<repository name>/<workspace relative path> instead of the
					// resolvable platform:/resource/<workspace relative path> . We'll try for this case.
					targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
							platformString.removeFirstSegments(1));
				} else {
					targetFile = temp;
				}
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
		if (subscriber != null) {
			IDiff diff = null;
			try {
				diff = subscriber.getDiff(targetFile);
			} catch (CoreException e) {
				logError(e);
			}

			IFileRevision revision = null;
			switch (side) {
				case LEFT:
					if (diff instanceof IThreeWayDiff) {
						final IDiff change = ((IThreeWayDiff)diff).getLocalChange();
						if (change instanceof ResourceDiff) {
							revision = ((ResourceDiff)change).getAfterState();
						}
					} else if (diff instanceof ResourceDiff) {
						revision = ((ResourceDiff)diff).getAfterState();
					}
					break;
				case RIGHT:
					if (diff instanceof IThreeWayDiff) {
						final IDiff change = ((IThreeWayDiff)diff).getRemoteChange();
						if (change instanceof ResourceDiff) {
							revision = ((ResourceDiff)change).getAfterState();
						}
					} else if (diff instanceof ResourceDiff) {
						revision = ((ResourceDiff)diff).getBeforeState();
					}
					break;
				case ORIGIN:
					if (diff instanceof IThreeWayDiff) {
						final IDiff change = ((IThreeWayDiff)diff).getLocalChange();
						if (change instanceof ResourceDiff) {
							revision = ((ResourceDiff)change).getBeforeState();
						}
					}
					break;
				default:
					break;
			}

			if (revision != null) {
				try {
					final IStorage storage = revision.getStorage(new NullProgressMonitor());
					getLoadedRevisions().add(storage);
					return storage.getContents();
				} catch (CoreException e) {
					logError(e);
				}
			}
		} else {
			// FIXME can this happen? does it matter?
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
