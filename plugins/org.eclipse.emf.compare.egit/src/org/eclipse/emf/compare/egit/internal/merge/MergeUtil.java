/*******************************************************************************
 * Copyright (C) 2015, Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal.merge;

//CHECKSTYLE:OFF
import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.GitProvider;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.team.core.RepositoryProvider;

/**
 * Utility class to match workspace resources with git repo relative paths or URIs.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public class MergeUtil {

	/**
	 * Returns a resource handle for this path in the workspace. Note that neither the resource nor the result
	 * need exist in the workspace : this may return inexistent or otherwise non-accessible IResources.
	 *
	 * @param repository
	 *            The repository within which is tracked this file.
	 * @param repoRelativePath
	 *            Repository-relative path of the file we need an handle for.
	 * @param isFolder
	 *            <code>true</code> if the file being sought is a folder.
	 * @return The resource handle for the given path in the workspace.
	 */
	public static IResource getResourceHandleForLocation(Repository repository, String repoRelativePath,
			boolean isFolder) {
		final String workDir = repository.getWorkTree().getAbsolutePath();
		final IPath path = new Path(workDir + '/' + repoRelativePath);
		final File file = path.toFile();
		if (file.exists()) {
			if (isFolder) {
				return ResourceUtil.getContainerForLocation(path);
			}
			return ResourceUtil.getFileForLocation(path);
		}

		// This is a file that no longer exists locally, yet we still need to
		// determine an IResource for it.
		// Try and find a Project in the workspace which path is a prefix of the
		// file we seek and which is mapped to the current repository.
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			if (RepositoryProvider.getProvider(project, GitProvider.ID) != null) {
				final IPath projectLocation = project.getLocation();
				if (projectLocation != null && projectLocation.isPrefixOf(path)) {
					final IPath projectRelativePath = path.makeRelativeTo(projectLocation);
					if (isFolder) {
						return project.getFolder(projectRelativePath);
					} else {
						return project.getFile(projectRelativePath);
					}
				}
			}
		}
		return null;
	}
}
// CHECKSTYLE:ON
