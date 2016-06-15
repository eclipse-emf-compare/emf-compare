/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 470268, checkstyle fixes
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ide.ui.internal.logical.RenameDetector;

/**
 * This will be used by URI Converters in order to retrieve the storages for the files it seeks. The URI
 * Converter usually only knows about local resources, it will thus ask for its storage accessor for the
 * proper remote content.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
 */
@Beta
public interface IStorageProviderAccessor {
	/**
	 * This will be called by the URI Converter to get the content associated with the given local resource
	 * (which might not exist locally).
	 * 
	 * @param resource
	 *            The resource we need content for.
	 * @param side
	 *            Side of the content we seek.
	 * @return The content for the given side of the given resource.
	 * @throws CoreException
	 *             Thrown if the underlying provider cannot be retrieved.
	 */
	IStorageProvider getStorageProvider(IResource resource, DiffSide side) throws CoreException;

	/**
	 * Checks whether the given resource is considered "in sync".
	 * 
	 * @param resource
	 *            The resource to check.
	 * @return <code>true</code> if this resource is in sync with its remote variant, <code>false</code>
	 *         otherwise.
	 * @throws CoreException
	 *             Thrown if we cannot get the diff for this resource.
	 */
	boolean isInSync(IResource resource) throws CoreException;

	/**
	 * Given a source or remote file, this method optionally returns the corresponding {@link IFile} before it
	 * has been renamed on the respective {@code side}, if it has been renamed at all.
	 * <p>
	 * Implementers should delegate this to {@link RenameDetector}.
	 * </p>
	 * 
	 * @param sourceOrRemoteFile
	 *            The potentially renamed file.
	 * @param side
	 *            The {@link DiffSide} to look for the rename (only {@link DiffSide#SOURCE} or
	 *            {@link DiffSide#REMOTE} are valid).
	 * @return The file before the rename, if it has been renamed at all, <code>null</code> otherwise.
	 */
	IFile getFileBeforeRename(IFile sourceOrRemoteFile, DiffSide side);

	/**
	 * Given an origin file, this method optionally returns the corresponding {@link IFile} after it has been
	 * renamed on the respective {@code side}, if it has been renamed at all.
	 * <p>
	 * Implementers should delegate this to {@link RenameDetector}.
	 * </p>
	 * 
	 * @param originFile
	 *            The potentially renamed file.
	 * @param side
	 *            The {@link DiffSide} to look for the rename (only {@link DiffSide#SOURCE} or
	 *            {@link DiffSide#REMOTE} are valid).
	 * @return The file after the rename, if it has been renamed at all, <code>null</code> otherwise.
	 */
	IFile getFileAfterRename(IFile originFile, DiffSide side);

	/** Used by the resolution process to determine the side of the revision to fetch. */
	public static enum DiffSide {
		/** Source side. Usually denotes "left" or "local" content. */
		SOURCE,

		/** Remote side. Usually denotes the "right" or "reference" content for a comparison. */
		REMOTE,

		/** Origin side. Corresponds to the common ancestor of the local and remote sides. */
		ORIGIN;
	}
}
