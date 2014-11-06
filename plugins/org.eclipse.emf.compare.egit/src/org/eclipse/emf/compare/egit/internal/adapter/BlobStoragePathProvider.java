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

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.egit.core.storage.GitBlobStorage;
import org.eclipse.emf.compare.ide.utils.IStoragePathProvider;

/**
 * Fixes EGit's storage paths to something we can resolve in the workspace.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class BlobStoragePathProvider implements IStoragePathProvider {
	/** {@inheritDoc} */
	public IPath computeFixedPath(IStorage storage) {
		final GitBlobStorage gitStorage = (GitBlobStorage)storage;
		final IPath absolutePath = gitStorage.getAbsolutePath();
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// FIXME if the storage is located outside of the workspace, getFileForLocation will return null, and
		// this call will thus result in NPE. How could we work around that limitation?
		return root.getFileForLocation(absolutePath).getFullPath();
	}
}
