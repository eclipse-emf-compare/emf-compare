/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal.adapter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.egit.core.storage.GitBlobStorage;
import org.eclipse.emf.compare.egit.internal.storage.WorkspaceGitBlobStorage;
import org.eclipse.emf.compare.ide.utils.IStoragePathProvider2;

/**
 * Fixes EGit's storage paths to something we can resolve in the workspace.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class BlobStoragePathProvider implements IStoragePathProvider2 {
	/** {@inheritDoc} */
	public IPath computeFixedPath(IStorage storage) {
		final IPath fixedPath;
		if (storage instanceof WorkspaceGitBlobStorage) {
			fixedPath = ((WorkspaceGitBlobStorage)storage).getWorkspacePath();
		} else if (storage instanceof GitBlobStorage) {
			final IPath absolutePath = ((GitBlobStorage)storage).getAbsolutePath();
			final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IFile fileForLocation = root.getFileForLocation(absolutePath);
			if (fileForLocation != null) {
				fixedPath = fileForLocation.getFullPath();
			} else {
				fixedPath = null;
			}
		} else {
			fixedPath = null;
		}
		return fixedPath;
	}

	/** {@inheritDoc} */
	public IPath computeAbsolutePath(IStorage storage) {
		if (storage instanceof GitBlobStorage) {
			final GitBlobStorage gitStorage = (GitBlobStorage)storage;
			final IPath absolutePath = gitStorage.getAbsolutePath();
			return absolutePath;
		}
		return null;
	}
}
