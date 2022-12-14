/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.git.framework.internal.statements;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.RepositoryCache;
import org.eclipse.egit.core.op.ConnectProviderOperation;
import org.eclipse.egit.core.op.DisconnectProviderOperation;
import org.eclipse.egit.core.project.RepositoryFinder;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

/**
 * This class contains utility methods to perform git tests. Those methods are internal and are not intended
 * to be used by clients.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@SuppressWarnings({"restriction" })
public class InternalGitTestSupport {

	/** The prefix of Git branches. */
	private static final String GIT_BRANCH_PREFIX = "refs/heads/"; //$NON-NLS-1$

	/** Name of the Eclipse metadata folder. */
	private static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$

	/** Name of the git metadata folder. */
	private static final String GIT_FOLDER = ".git"; //$NON-NLS-1$

	/** Size of the buffer to read/write data. */
	private static final int BUFFER_SIZE = 4096;

	/** The JGit repository. */
	protected Repository repository;

	/** The list of projects in the repository. */
	protected IProject[] projects;

	/** The list of JGit disposers. */
	protected ArrayList<Runnable> disposers;

	/** Query used to specify if the projects import must override existing projects. */
	private IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
		public String queryOverwrite(String file) {
			return ALL;
		}
	};

	/**
	 * Adapt the given <code>branch</code> name to be in a correct format for Git or return it directly if it
	 * is already the case.
	 * 
	 * @param branch
	 *            The branch name we want to normalize
	 * @return the branch in Git format
	 */
	protected static String normalizeBranch(String branch) {
		if (branch.startsWith(GIT_BRANCH_PREFIX)) {
			return branch;
		} else {
			return GIT_BRANCH_PREFIX + branch;
		}
	}

	/**
	 * Perform all loading actions (unzip the archive, connect the git repository to JGit, import the
	 * contained projects in the workspace and connect them through JGit).
	 * 
	 * @param clazz
	 *            The test class
	 * @param path
	 *            The path of the archive containing the Git repository
	 * @throws Exception
	 *             If something goes wrong during unzip or import steps
	 */
	protected void createRepositoryFromPath(Class<?> clazz, String path) throws Exception {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// Delete all projects that can remain in the workspace : prevent errors dues to wrong cleanup of
		// other tests
		root.delete(true, new NullProgressMonitor());
		IPath location = root.getLocation();
		List<String> extractedPaths = extractArchive(clazz, path, root);
		for (String extractedPath : extractedPaths) {
			File extractedFile = new File(extractedPath);
			if (!extractedFile.getPath().contains(File.separatorChar + METADATA_FOLDER + File.separatorChar)
					&& !extractedFile.getPath()
							.contains(File.separatorChar + GIT_FOLDER + File.separatorChar)) {
				if (".project".equals(extractedFile.getName())) { //$NON-NLS-1$
					importProject(extractedFile);
				}
			}
		}
		connectRepository(new File(location.toString()));
		List<IProject> existingProjects = Arrays.asList(root.getProjects());
		List<IProject> connectedProjects = new ArrayList<>();
		for (IProject project : existingProjects) {
			RepositoryFinder finder = new RepositoryFinder(project);
			finder.setFindInChildren(false);
			Collection<RepositoryMapping> repos = finder.find(new NullProgressMonitor());
			if (!repos.isEmpty()) {
				connect(project);
				connectedProjects.add(project);
			}
		}
		projects = connectedProjects.toArray(new IProject[connectedProjects.size()]);
	}

	/**
	 * Connect a project to the Git repository.
	 * 
	 * @param project
	 *            The project to connect
	 * @throws CoreException
	 *             Thrown if the project cannot be connected to the workspace
	 * @throws InterruptedException
	 *             Thrown if the operation is interrupted
	 */
	private void connect(IProject project) throws CoreException, InterruptedException {
		ConnectProviderOperation op = new ConnectProviderOperation(project, repository.getDirectory());
		op.execute(null);
	}

	/**
	 * Connect the Git repository to JGit.
	 * 
	 * @param file
	 *            The path to the root of the repository
	 * @throws IOException
	 *             If their is a problem during the connection to JGit
	 */
	private void connectRepository(File file) throws IOException {
		File gitDir = findGitDir(file);
		this.repository = RepositoryCache.INSTANCE.lookupRepository(gitDir);
		this.disposers = new ArrayList<Runnable>();
	}

	/**
	 * Find the ".git" folder in the repository.
	 * 
	 * @param file
	 *            The path of the root of the unziped files
	 * @return The path to the .git folder
	 */
	private File findGitDir(File file) {
		File[] listFiles = file.listFiles();
		if (listFiles != null) {
			for (File child : listFiles) {
				if (child.isDirectory() && child.getName().equals(".git")) { //$NON-NLS-1$
					return child;
				} else if (child.isDirectory()) {
					File findGitDir = findGitDir(child);
					if (findGitDir != null) {
						return findGitDir;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Import the project located in the given path into the test workspace.
	 * 
	 * @param file
	 *            The path to the .project file
	 * @throws InvocationTargetException
	 *             Thrown if an error happen during the import of the project
	 * @throws InterruptedException
	 *             Thrown if the import operation is interrupted
	 * @throws CoreException
	 *             Thrown if the project cannot be created in the workspace
	 */
	private void importProject(File file)
			throws InvocationTargetException, InterruptedException, CoreException {
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.loadProjectDescription(new Path(file.getAbsolutePath()));
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		project.create(description, new NullProgressMonitor());
		project.open(new NullProgressMonitor());

		ImportOperation importOperation = new ImportOperation(project.getFullPath(), file.getParentFile(),
				FileSystemStructureProvider.INSTANCE, overwriteQuery);
		importOperation.setCreateContainerStructure(false);
		importOperation.run(new NullProgressMonitor());
	}

	/**
	 * Extract the zip file into the given workspace.
	 * 
	 * @param clazz
	 *            The test class
	 * @param path
	 *            The path to the archive (relative to the test class)
	 * @param root
	 *            The root of the test workspace
	 * @return The list of files extracted from this archive. Does not include folders.
	 * @throws IOException
	 *             Thrown if the zip extraction goes wrong
	 */
	private List<String> extractArchive(Class<?> clazz, String path, IWorkspaceRoot root) throws IOException {
		List<String> extractedPaths = new ArrayList<>();
		try (InputStream resourceAsStream = clazz.getResourceAsStream(path);
				ZipInputStream zipIn = new ZipInputStream(resourceAsStream);) {
			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
				String filePath = root.getLocation() + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					extractFile(zipIn, filePath);
					extractedPaths.add(filePath);
				} else {
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
			}
		}
		return extractedPaths;
	}

	/**
	 * Extract the given input stream to the given location.
	 * 
	 * @param zipIn
	 *            The zip input stream
	 * @param filePath
	 *            The destination path for the extraction
	 * @throws IOException
	 *             Thrown if something happen during the extraction
	 */
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));) {
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		}
	}

	/**
	 * Clean all possibly remaining elements to start the test in a clean state.
	 * 
	 * @throws CoreException
	 *             Thrown if a project cannot be deleted
	 * @throws IOException
	 *             Thrown if a file cannot be deleted
	 */
	protected void setup() throws CoreException, IOException {
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] unknownProjects = workspaceRoot.getProjects();
		if (unknownProjects != null && unknownProjects.length > 0) {
			for (IProject iProject : unknownProjects) {
				iProject.delete(true, new NullProgressMonitor());
			}
		}
		RepositoryCache.INSTANCE.clear();

		File file = new File(workspaceRoot.getLocation().toOSString());
		File[] listFiles = file.listFiles();
		if (listFiles != null) {
			for (File child : listFiles) {
				if (!child.getName().equals(METADATA_FOLDER)) {
					try (Stream<java.nio.file.Path> walk = Files.walk(child.toPath())) {
						walk.sorted(Comparator.reverseOrder()).map(java.nio.file.Path::toFile)
								.forEach(File::delete);
					}
				}
			}
		}
	}

	/**
	 * Clear workspace and repository for next tests.
	 * 
	 * @throws CoreException
	 *             Thrown if a project cannot be deleted
	 * @throws IOException
	 *             Thrown if a file cannot be deleted
	 */
	protected void tearDown() throws CoreException, IOException {
		if (disposers != null) {
			for (Runnable disposer : disposers) {
				disposer.run();
			}
			disposers.clear();
		}

		RepositoryCache.INSTANCE.clear();

		if (projects != null) {
			List<IProject> disconnectMe = new ArrayList<>();
			for (IProject iProject : projects) {
				disconnectMe.add(iProject);
			}
			new DisconnectProviderOperation(disconnectMe).execute(null);

			// Delete sub-projects first
			List<IProject> allProjects = new ArrayList<>(
					Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects()));
			allProjects.sort((p1, p2) -> p2.getLocation().toString().compareTo(p1.getLocation().toString()));

			for (IProject iProject : allProjects) {
				if (iProject.isAccessible()) {
					iProject.delete(true, new NullProgressMonitor());
				}
			}
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE,
					new NullProgressMonitor());
		}

		File file = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
		File[] listFiles = file.listFiles();
		if (listFiles != null) {
			for (File child : listFiles) {
				if (!child.getName().equals(METADATA_FOLDER)) {
					try (Stream<java.nio.file.Path> walk = Files.walk(child.toPath())) {
						walk.sorted(Comparator.reverseOrder()).map(java.nio.file.Path::toFile)
								.forEach(File::delete);
					}
				}
			}
		}
	}

}
