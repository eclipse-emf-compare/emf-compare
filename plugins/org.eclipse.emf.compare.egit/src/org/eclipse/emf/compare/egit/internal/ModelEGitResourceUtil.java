/*******************************************************************************
 * Copyright (C) 2015, Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal;

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
 * Resource utilities.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("restriction")
public class ModelEGitResourceUtil extends ResourceUtil {

	public static IResource getResourceHandleForLocation(Repository repository, String repoRelativePath,
			boolean isFolder) {
		final String workDir = repository.getWorkTree().getAbsolutePath();
		final IPath path = new Path(workDir + '/' + repoRelativePath);
		final File file = path.toFile();
		if (file.exists()) {
			if (isFolder) {
				return ResourceUtil.getContainerForLocation(path, false);
			}
			return ResourceUtil.getFileForLocation(path, false);
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
