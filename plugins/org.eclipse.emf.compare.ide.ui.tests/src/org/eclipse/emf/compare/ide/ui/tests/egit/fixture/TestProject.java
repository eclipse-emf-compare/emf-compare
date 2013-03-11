/*******************************************************************************
 * Copyright (C) 2013 Obeo and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.egit.fixture;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

@SuppressWarnings("nls")
public class TestProject {
	/** The underlying eclipse project. */
	private IProject project;

	/**
	 * Creates a new project in the workspace with a default name. An existing test project with that name
	 * will be deleted and re-created.
	 */
	public TestProject() throws CoreException {
		this("Project-1");
	}

	/**
	 * This will create a new project with the given name inside the workspace. If this project already
	 * existed, it will be deleted and re-created.
	 * 
	 * @param name
	 *            Name of our project.
	 */
	public TestProject(String name) throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(name);
		project = root.getProject(description.getName());
		if (project.exists()) {
			project.delete(true, true, new NullProgressMonitor());
		}
		project.create(description, new NullProgressMonitor());
		project.open(new NullProgressMonitor());
	}

	public IProject getProject() {
		return project;
	}

	public IFile createFile(String name, byte[] content) throws Exception {
		IFile file = project.getFile(name);
		InputStream inputStream = new ByteArrayInputStream(content);
		file.create(inputStream, true, new NullProgressMonitor());

		return file;
	}

	public IFolder createFolder(String name) throws Exception {
		IFolder folder = project.getFolder(name);
		folder.create(true, true, new NullProgressMonitor());

		return folder;
	}

	public void dispose() throws CoreException, IOException {
		if (project.exists()) {
			project.delete(true, true, new NullProgressMonitor());
		}
	}
}
