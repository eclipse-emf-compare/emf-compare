/*******************************************************************************
 * Copyright (C) 2013 Obeo and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.workspace;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.regex.Pattern;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

@SuppressWarnings("nls")
public class TestProject {

	private File workspaceSupplement;

	private String location;

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
	 * @param path
	 *            Name of our project.
	 */
	public TestProject(String path) throws CoreException {
		this(path, true, null);
	}

	/**
	 * This will create a new project with the given name inside the workspace. If this project already
	 * existed, it will be deleted and re-created.
	 * 
	 * @param path
	 *            Name of our project.
	 * @param workspaceSupplement
	 *            File to host the workspace
	 */
	public TestProject(String path, boolean delete, File workspaceSupplement) throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		this.workspaceSupplement = workspaceSupplement;
		IProjectDescription description = createDescription(path, true, root, workspaceSupplement);
		project = root.getProject(description.getName());
		if (delete) {
			project.delete(true, true, null);
		}
		IPath locationBefore = description.getLocation();
		if (locationBefore == null) {
			locationBefore = root.getRawLocation().append(path);
		}
		location = locationBefore.toOSString();
		project.create(description, null);
		project.open(null);
	}

	private IProjectDescription createDescription(String path, boolean insidews, IWorkspaceRoot root,
			File workspaceSupplement) {
		Path ppath = new Path(path);
		String projectName = ppath.lastSegment();
		URI locationURI;
		URI top;
		if (insidews) {
			top = root.getRawLocationURI();
		} else {
			top = URIUtil.toURI(workspaceSupplement.getAbsolutePath());
		}
		if (!insidews || !ppath.lastSegment().equals(path)) {
			locationURI = URIUtil.toURI(URIUtil.toPath(top).append(path));
		} else {
			locationURI = null;
		}
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);

		description.setName(projectName);
		description.setLocationURI(locationURI);
		return description;
	}

	/**
	 * This will create a new project with the given name inside the workspace. If this project already
	 * existed, it will be deleted and re-created.
	 * 
	 * @param name
	 *            Name of our project.
	 * @param location
	 *            Location of our project.
	 */
	public TestProject(String name, String location) throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(name);
		description.setLocation(new Path(location + File.separator + name));
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

	/**
	 * Create or get a file for the given path.
	 * 
	 * @param project
	 *            Instance of project inside which a file will be created.
	 * @param path
	 *            Project-relative path for the new file.
	 * @return Newly created file
	 */
	public File getOrCreateFile(IProject aProject, String path) throws IOException {
		String fullPath = aProject.getLocation().append(path).toOSString();
		int lastSeparator = fullPath.lastIndexOf(File.separator);
		mkdirs(new File(fullPath.substring(0, lastSeparator)), true);

		File file = new File(fullPath);
		if (!file.exists()) {
			createNewFile(file);
		}

		return file;
	}

	/**
	 * Find an IFile corresponding to the given java.io.File within the given project.
	 * 
	 * @param aProject
	 *            The project within which we're searching for a file.
	 * @param file
	 *            java.io.File for which we're searching a corresponding eclipse IFile.
	 * @return The IFile we found for the given file.
	 */
	public IFile getIFile(IProject aProject, File file) throws CoreException {
		IPath filePath = new Path(file.getAbsolutePath());
		String relativePath = filePath.makeRelativeTo(aProject.getLocation()).toString();

		String quotedProjectName = Pattern.quote(aProject.getName());
		relativePath = relativePath.replaceFirst(quotedProjectName, "");

		IFile iFile = aProject.getFile(relativePath);
		iFile.refreshLocal(0, new NullProgressMonitor());

		return iFile;
	}

	/**
	 * Creates the directory named by this abstract pathname, including any necessary but nonexistent parent
	 * directories. Note that if this operation fails it may have succeeded in creating some of the necessary
	 * parent directories.
	 * 
	 * @param d
	 *            directory to be created
	 * @param skipExisting
	 *            if {@code true} skip creation of the given directory if it already exists in the file system
	 * @throws IOException
	 *             if creation of {@code d} fails. This may occur if {@code d} did exist when the method was
	 *             called. This can therefore cause IOExceptions during race conditions when multiple
	 *             concurrent threads all try to create the same directory.
	 */
	private static void mkdirs(final File d, boolean skipExisting) throws IOException {
		if (!d.mkdirs()) {
			if (skipExisting && d.isDirectory()) {
				return;
			} else {
				throw new IOException();
			}
		}
	}

	private static void createNewFile(File f) throws IOException {
		f.createNewFile();
	}

	public IFile createFile(String name, InputStream inputStream) throws Exception {
		IFile file = project.getFile(name);
		if (!file.getParent().exists()) {
			((IFolder)file.getParent()).create(IResource.FORCE, true, new NullProgressMonitor());
		}
		file.create(inputStream, true, new NullProgressMonitor());

		return file;
	}

	public IFile createFile(String name, byte[] content) throws Exception {
		return createFile(name, new ByteArrayInputStream(content));
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
