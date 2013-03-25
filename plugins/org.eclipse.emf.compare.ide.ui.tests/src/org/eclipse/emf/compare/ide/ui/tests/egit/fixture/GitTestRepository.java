/*******************************************************************************
 * Copyright (C) 2013 Obeo and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.egit.fixture;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.op.BranchOperation;
import org.eclipse.egit.core.op.ConnectProviderOperation;
import org.eclipse.egit.core.synchronize.GitResourceVariantTreeSubscriber;
import org.eclipse.egit.core.synchronize.GitSubscriberMergeContext;
import org.eclipse.egit.core.synchronize.GitSubscriberResourceMappingContext;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeData;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeDataSet;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberScopeManager;

/**
 * This class is largely inspired from org.eclipse.egit.core.test.TestRepository. It has been copied here in
 * order to be usable from our build without dependencies towards egit.core.tests.
 */
@SuppressWarnings("nls")
public class GitTestRepository {
	Repository repository;

	String workdirPrefix;

	/**
	 * Creates a new test repository.
	 * 
	 * @param gitDir
	 *            The ".git" file we'll use to create a repository.
	 * @throws IOException
	 *             Thrown if we cannot write at the given location.
	 */
	public GitTestRepository(File gitDir) throws IOException {
		repository = Activator.getDefault().getRepositoryCache().lookupRepository(gitDir);
		repository.create();

		try {
			workdirPrefix = repository.getWorkTree().getCanonicalPath();
		} catch (IOException err) {
			workdirPrefix = repository.getWorkTree().getAbsolutePath();
		}
		workdirPrefix = workdirPrefix.replace('\\', '/');
		if (!workdirPrefix.endsWith("/")) {
			workdirPrefix += "/";
		}
	}

	/**
	 * Track, add to index and finally commit the given files.
	 * 
	 * @param project
	 *            The project within which this file is located.
	 * @param commitMessage
	 *            Message with which to commit this file.
	 * @param files
	 *            The files to add and commit.
	 * @return The RevCommit corresponding to this operation.
	 */
	public RevCommit addAndCommit(IProject project, String commitMessage, File... files) throws Exception {
		track(files);
		addToIndex(project, files);
		return commit(commitMessage);
	}

	/**
	 * Adds The given files to version control.
	 * 
	 * @param files
	 *            The files to add.
	 */
	public void track(File... files) throws IOException, NoFilepatternException, GitAPIException {
		for (File file : files) {
			String repoPath = getRepoRelativePath(new Path(file.getPath()).toString());
			new Git(repository).add().addFilepattern(repoPath).call();
		}
	}

	/**
	 * Adds the given files to the index.
	 * 
	 * @param project
	 *            Project that contains these files.
	 * @param files
	 *            Files to add to the index.
	 */
	public void addToIndex(IProject project, File... files) throws Exception {
		for (File file : files) {
			addToIndex(getIFile(project, file));
		}
	}

	/**
	 * Adds the given resources to the index
	 * 
	 * @param resources
	 *            Resources to add to the index.
	 */
	public void addToIndex(IResource... resources) throws CoreException, IOException, NoFilepatternException,
			GitAPIException {
		for (IResource resource : resources) {
			String repoPath = getRepoRelativePath(resource.getLocation().toOSString());
			new Git(repository).add().addFilepattern(repoPath).call();
		}
	}

	/**
	 * Commits the current index.
	 * 
	 * @param message
	 *            commit message
	 * @return commit object
	 */
	public RevCommit commit(String message) throws Exception {
		Git git = new Git(repository);
		CommitCommand commitCommand = git.commit();
		commitCommand.setAuthor("J. Git", "j.git@egit.org");
		commitCommand.setCommitter(commitCommand.getAuthor());
		commitCommand.setMessage(message);
		return commitCommand.call();
	}

	/**
	 * Connect a project to this repository.
	 * 
	 * @param project
	 *            The project to connect
	 */
	public void connect(IProject project) throws CoreException {
		ConnectProviderOperation op = new ConnectProviderOperation(project, repository.getDirectory());
		op.execute(null);
	}

	/**
	 * Creates a new branch.
	 * 
	 * @param refName
	 *            Starting point for the new branch.
	 * @param newRefName
	 *            Name of the new branch.
	 */
	public void createBranch(String refName, String newRefName) throws IOException {
		RefUpdate updateRef;
		updateRef = repository.updateRef(newRefName);
		Ref startRef = repository.getRef(refName);
		ObjectId startAt = repository.resolve(refName);
		String startBranch;
		if (startRef != null) {
			startBranch = refName;
		} else {
			startBranch = startAt.name();
		}
		startBranch = Repository.shortenRefName(startBranch);
		updateRef.setNewObjectId(startAt);
		updateRef.setRefLogMessage("branch: Created from " + startBranch, false);
		updateRef.update();
	}

	/**
	 * Checkouts branch.
	 * 
	 * @param refName
	 *            Full name of the branch.
	 */
	public void checkoutBranch(String refName) throws CoreException {
		new BranchOperation(repository, refName).execute(null);
	}

	/**
	 * Dispose of this wrapper along with its underlying repository.
	 */
	public void dispose() {
		if (repository != null) {
			repository.close();
			repository = null;
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
	public Subscriber createSubscriberForComparison(String sourceRef, String targetRef, IFile comparedFile)
			throws IOException {
		final GitSynchronizeData data = new GitSynchronizeData(repository, sourceRef, targetRef, false);
		final GitSynchronizeDataSet dataSet = new GitSynchronizeDataSet(data);
		final ResourceMapping[] mappings = getResourceMappings(comparedFile);
		GitResourceVariantTreeSubscriber subscriber = new GitResourceVariantTreeSubscriber(dataSet);
		subscriber.init(new NullProgressMonitor());

		final RemoteResourceMappingContext remoteContext = new GitSubscriberResourceMappingContext(
				subscriber, dataSet);
		final SubscriberScopeManager manager = new SubscriberScopeManager(subscriber.getName(), mappings,
				subscriber, remoteContext, true);
		final GitSubscriberMergeContext context = new GitSubscriberMergeContext(subscriber, manager, dataSet);
		return context.getSubscriber();
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
	public File getOrCreateFile(IProject project, String path) throws IOException {
		String fullPath = project.getLocation().append(path).toOSString();
		int lastSeparator = fullPath.lastIndexOf(File.separator);
		FileUtils.mkdirs(new File(fullPath.substring(0, lastSeparator)), true);

		File file = new File(fullPath);
		if (!file.exists()) {
			FileUtils.createNewFile(file);
		}

		return file;
	}

	/**
	 * Find an IFile corresponding to the given java.io.File within the given project.
	 * 
	 * @param project
	 *            The project within which we're searching for a file.
	 * @param file
	 *            java.io.File for which we're searching a corresponding eclipse IFile.
	 * @return The IFile we found for the given file.
	 */
	public IFile getIFile(IProject project, File file) throws CoreException {
		String relativePath = getRepoRelativePath(file.getAbsolutePath());

		String quotedProjectName = Pattern.quote(project.getName());
		relativePath = relativePath.replaceFirst(quotedProjectName, "");

		IFile iFile = project.getFile(relativePath);
		iFile.refreshLocal(0, new NullProgressMonitor());

		return iFile;
	}

	/**
	 * Connects an EMF resource to the given File within the given project. The resource will be created with
	 * a workspace-relative "{@code platform:/resource}" URI.
	 * 
	 * @param project
	 *            The project within which the given file is located.
	 * @param file
	 *            The file we're attaching an EMF Resource on.
	 * @param resourceSet
	 *            The resource set in which the new Resource will be created.
	 * @return The created EMF Resource.
	 */
	public Resource connectResource(IProject project, File file, ResourceSet resourceSet)
			throws CoreException {
		String relativePath = getRepoRelativePath(file.getAbsolutePath());

		String quotedProjectName = Pattern.quote(project.getName());
		relativePath = relativePath.replaceFirst(".*(" + quotedProjectName + ")", "$1");

		URI uri = URI.createPlatformResourceURI(relativePath, true);

		return createResource(uri, resourceSet);
	}

	private String getRepoRelativePath(String path) {
		final int pfxLen = workdirPrefix.length();
		final int pLen = path.length();
		if (pLen > pfxLen) {
			return path.substring(pfxLen);
		} else if (path.length() == pfxLen - 1) {
			return "";
		}
		return null;
	}

	private static Resource createResource(URI modelURI, ResourceSet resourceSet) {
		final Resource resource = new XMIResourceImpl(modelURI) {
			@Override
			protected boolean useUUIDs() {
				return true;
			}
		};
		resourceSet.getResources().add(resource);
		return resource;
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
				Activator.logError(e.getMessage(), e);
			}
		}
		return mappings.toArray(new ResourceMapping[mappings.size()]);
	}
}
