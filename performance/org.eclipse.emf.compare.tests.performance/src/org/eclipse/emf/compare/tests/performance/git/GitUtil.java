/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance.git;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.op.ConnectProviderOperation;
import org.eclipse.egit.core.synchronize.GitResourceVariantTreeSubscriber;
import org.eclipse.egit.core.synchronize.GitSubscriberMergeContext;
import org.eclipse.egit.core.synchronize.GitSubscriberResourceMappingContext;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeData;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeDataSet;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberScopeManager;

import com.google.common.base.Throwables;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public final class GitUtil {

	/**
	 * This will unzip the repository contained in the given zip location in the given destination.
	 * 
	 * @param zipLocation
	 *            the zip (that contains the repository) location.
	 * @param destination
	 *            the destination
	 * @param monitor
	 *            {@link IProgressMonitor} that will be used to monitor the operation.
	 */
	public static void unzipRepo(URL zipLocation, String destination, IProgressMonitor monitor) {

		try {
			final ZipInputStream zipFileStream = new ZipInputStream(zipLocation.openStream());
			ZipEntry zipEntry = zipFileStream.getNextEntry();

			while (zipEntry != null) {
				// We will construct the new file but we will strip off the project
				// directory from the beginning of the path because we have already
				// created the destination project for this zip.
				final File file = new File(destination, zipEntry.getName());

				if (!zipEntry.isDirectory()) {

					/*
					 * Copy files (and make sure parent directory exist)
					 */
					final File parentFile = file.getParentFile();
					if (null != parentFile && !parentFile.exists()) {
						parentFile.mkdirs();
					}

					OutputStream os = null;

					try {
						os = new FileOutputStream(file);

						final int bufferSize = 102400;
						final byte[] buffer = new byte[bufferSize];
						while (true) {
							final int len = zipFileStream.read(buffer);
							if (zipFileStream.available() == 0) {
								break;
							}
							os.write(buffer, 0, len);
						}
					} finally {
						if (null != os) {
							os.close();
						}
					}
				}

				zipFileStream.closeEntry();
				zipEntry = zipFileStream.getNextEntry();
			}
		} catch (final IOException e) {
			Throwables.propagate(e);
		}
	}

	/**
	 * Delete the repository (and all files contained in).
	 * 
	 * @param repository
	 *            the File representing the repository.
	 */
	public static void deleteRepo(File repository) {
		if (repository.exists()) {
			File[] files = repository.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteRepo(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
			repository.delete();
		}
	}

	/**
	 * Import the project from the given repository.
	 * 
	 * @param repository
	 *            the given repository.
	 * @param projectFilePath
	 *            the path containing the project in the repository.
	 * @return the imported project, or null if import step fails.
	 */
	public static IProject importProjectFromRepo(File repository, String projectFilePath) {
		try {
			IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(
					new Path(repository.getPath() + File.separator + projectFilePath));
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
			project.create(description, new NullProgressMonitor());
			project.open(new NullProgressMonitor());

			return project;
		} catch (CoreException e) {
			Throwables.propagate(e);
		}
		return null;
	}

	/**
	 * Import all projects contain in the given repository.
	 * 
	 * @param repository
	 *            the given repository.
	 */
	public static Collection<IProject> importProjectsFromRepo(File repository) {
		Collection<IProject> projects = new ArrayList<IProject>();
		try {
			Collection<File> projectsFromRepo = getProjectsFromRepo(repository);
			for (File file : projectsFromRepo) {
				IProjectDescription description = ResourcesPlugin.getWorkspace()
						.loadProjectDescription(new Path(file.getPath()));
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
				project.create(description, new NullProgressMonitor());
				project.open(new NullProgressMonitor());
				projects.add(project);
			}
		} catch (CoreException e) {
			Throwables.propagate(e);
		}
		return projects;
	}

	/**
	 * Get the ".projects" files from the given repository.
	 * 
	 * @param repository
	 *            the given repository.
	 * @return the ".projects" files from the given repository.
	 */
	public static Collection<File> getProjectsFromRepo(File repository) {
		List<File> projects = new ArrayList<File>();
		if (repository.exists()) {
			File[] files = repository.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						projects.addAll(getProjectsFromRepo(files[i]));
					} else if (files[i].getName().endsWith(IProjectDescription.DESCRIPTION_FILE_NAME)) {
						projects.add(files[i]);
					}
				}
			}
		}
		return projects;
	}

	/**
	 * Connect given projects to the given repository.
	 * 
	 * @param repository
	 *            the given repository.
	 * @param projects
	 *            the given projects.
	 */
	public static void connectProjectsToRepo(Repository repository, Collection<IProject> projects) {
		for (IProject project : projects) {
			try {
				ConnectProviderOperation op = new ConnectProviderOperation(project,
						repository.getDirectory());
				op.execute(new NullProgressMonitor());
			} catch (CoreException e) {
				Throwables.propagate(e);
			}
		}
	}

	/**
	 * Simulate a comparison between the two given references and returns back the subscriber that can provide
	 * all computed synchronization information.
	 * 
	 * @param sourceRef
	 *            Source reference (i.e. "left" side of the comparison).
	 * @param targetRef
	 *            Target reference (i.e. "right" side of the comparison).
	 * @param comparedFile
	 *            The file we are comparing (that would be the file right-clicked into the workspace).
	 * @return The created subscriber.
	 */
	public static Subscriber createSubscriberForComparison(Repository repository, String sourceRef,
			String targetRef, IFile comparedFile, List<Runnable> disposers) throws IOException {
		final GitSynchronizeData data = new GitSynchronizeData(repository, sourceRef, targetRef, false);
		final GitSynchronizeDataSet dataSet = new GitSynchronizeDataSet(data);
		final ResourceMapping[] mappings = getResourceMappings(comparedFile);
		final GitResourceVariantTreeSubscriber subscriber = new GitResourceVariantTreeSubscriber(dataSet);
		subscriber.init(new NullProgressMonitor());

		final RemoteResourceMappingContext remoteContext = new GitSubscriberResourceMappingContext(subscriber,
				dataSet);
		final SubscriberScopeManager manager = new SubscriberScopeManager(subscriber.getName(), mappings,
				subscriber, remoteContext, true);
		final GitSubscriberMergeContext context = new GitSubscriberMergeContext(subscriber, manager, dataSet);
		disposers.add(new Runnable() {
			public void run() {
				manager.dispose();
				context.dispose();
				subscriber.dispose();
			}
		});
		return context.getSubscriber();
	}

	/**
	 * This will query all model providers for those that are enabled on the given file and list all mappings
	 * available for that file.
	 * 
	 * @param file
	 *            The file for which we need the associated resource mappings.
	 * @return All mappings available for that file.
	 */
	private static ResourceMapping[] getResourceMappings(IFile file) {
		final IModelProviderDescriptor[] modelDescriptors = ModelProvider.getModelProviderDescriptors();

		final Set<ResourceMapping> mappings = new LinkedHashSet<ResourceMapping>();
		for (IModelProviderDescriptor candidate : modelDescriptors) {
			try {
				final IResource[] resources = candidate.getMatchingResources(new IResource[] {file, });
				if (resources.length > 0) {
					// get mappings from model provider if there are matching resources
					final ModelProvider model = candidate.getModelProvider();
					final ResourceMapping[] modelMappings = model.getMappings(file,
							ResourceMappingContext.LOCAL_CONTEXT, null);
					for (ResourceMapping mapping : modelMappings) {
						mappings.add(mapping);
					}
				}
			} catch (CoreException e) {
				Throwables.propagate(e);
			}
		}
		return mappings.toArray(new ResourceMapping[mappings.size()]);
	}
}
