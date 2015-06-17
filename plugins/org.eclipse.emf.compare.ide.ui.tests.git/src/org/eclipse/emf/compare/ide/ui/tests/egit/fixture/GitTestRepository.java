/*******************************************************************************
 * Copyright (C) 2013, 2015 Obeo and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.egit.fixture;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.op.BranchOperation;
import org.eclipse.egit.core.op.ConnectProviderOperation;
import org.eclipse.egit.core.op.IgnoreOperation;
import org.eclipse.egit.core.op.MergeOperation;
import org.eclipse.egit.core.op.ResetOperation;
import org.eclipse.egit.core.synchronize.GitResourceVariantTreeSubscriber;
import org.eclipse.egit.core.synchronize.GitSubscriberMergeContext;
import org.eclipse.egit.core.synchronize.GitSubscriberResourceMappingContext;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeData;
import org.eclipse.egit.core.synchronize.dto.GitSynchronizeDataSet;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberScopeManager;

/**
 * This class is largely inspired from
 * org.eclipse.egit.core.test.TestRepository. It has been copied here in order
 * to be usable from our build without dependencies towards egit.core.tests.
 */
@SuppressWarnings({ "nls", "restriction" })
public class GitTestRepository {
	private final List<Runnable> disposers;

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
		repository = Activator.getDefault().getRepositoryCache()
				.lookupRepository(gitDir);
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

		this.disposers = new ArrayList<Runnable>();
	}

	public RevCommit addAllAndCommit(String commitMessage) throws Exception {
		Git git = null;
		try {
			git = new Git(repository);
			git.add().addFilepattern(".").call();
			return commit(commitMessage);
		} finally {
			git.close();
		}
	}

	/**
	 * Adds all changes and amends the latest commit, also changing its message
	 * to the given message.
	 * 
	 * @param message
	 *            the amended commit message, must not be null
	 * @return The RevCommit of the amended commit.
	 * @throws Exception
	 *             if anything goes wrong.
	 */
	public RevCommit addAllAndAmend(String message) throws Exception {
		Git git = null;
		try {
			git = new Git(repository);
			git.add().addFilepattern(".").call();
			return git.commit().setAmend(true).setMessage(message).call();
		} finally {
			git.close();
		}
	}

	/**
	 * Track, add to index and finally commit the given files.
	 * 
	 * @param testProject
	 *            The project within which this file is located.
	 * @param commitMessage
	 *            Message with which to commit this file.
	 * @param files
	 *            The files to add and commit.
	 * @return The RevCommit corresponding to this operation.
	 */
	public RevCommit addAndCommit(TestProject testProject,
			String commitMessage, File... files) throws Exception {
		addToIndex(testProject, files);
		return commit(commitMessage);
	}

	public void ignore(File... files) throws Exception {
		final Set<IPath> paths = new LinkedHashSet<IPath>();
		for (File file : files) {
			paths.add(new Path(file.getPath()));
		}
		new IgnoreOperation(paths).execute(new NullProgressMonitor());
	}

	/**
	 * Adds the given files to the index.
	 * 
	 * @param testProject
	 *            Project that contains these files.
	 * @param files
	 *            Files to add to the index.
	 */
	public void addToIndex(TestProject testProject, File... files)
			throws Exception {
		for (File file : files) {
			addToIndex(testProject.getIFile(testProject.getProject(), file));
		}
	}

	/**
	 * Removes the given files from the index.
	 * 
	 * @param testProject
	 *            Project that contains these files.
	 * @param files
	 *            Files to remove from the index.
	 */
	public void removeFromIndex(TestProject testProject, File... files)
			throws Exception {
		for (File file : files) {
			removeFromIndex(testProject.getIFile(testProject.getProject(), file));
		}
	}

	/**
	 * Adds the given resources to the index
	 * 
	 * @param resources
	 *            Resources to add to the index.
	 */
	public void addToIndex(IResource... resources) throws CoreException,
			IOException, NoFilepatternException, GitAPIException {
		Git git = null;
		try {
			git = new Git(repository);
			for (IResource resource : resources) {
				String repoPath = getRepoRelativePath(resource.getLocation()
						.toString());
				git.add().addFilepattern(repoPath).call();
			}
		} finally {
			git.close();
		}
	}

	/**
	 * Adds the given resources to the index
	 * 
	 * @param resources
	 *            Resources to add to the index.
	 */
	public void removeFromIndex(IResource... resources) throws CoreException,
			IOException, NoFilepatternException, GitAPIException {
		Git git = null;
		try {
			git = new Git(repository);
			for (IResource resource : resources) {
				String repoPath = getRepoRelativePath(resource.getLocation()
						.toString());
				git.rm().addFilepattern(repoPath).call();
			}
		} finally {
			git.close();
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
		Git git = null;
		try {
			git = new Git(repository);
			CommitCommand commitCommand = git.commit();
			commitCommand.setAuthor("J. Git", "j.git@egit.org");
			commitCommand.setCommitter(commitCommand.getAuthor());
			commitCommand.setMessage(message);
			return commitCommand.call();
		} finally {
			git.close();
		}
	}

	/**
	 * Connect a project to this repository.
	 * 
	 * @param project
	 *            The project to connect
	 */
	public void connect(IProject project) throws CoreException {
		ConnectProviderOperation op = new ConnectProviderOperation(project,
				repository.getDirectory());
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
	public void createBranch(String refName, String newRefName)
			throws IOException {
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
		updateRef
				.setRefLogMessage("branch: Created from " + startBranch, false);
		updateRef.update();
	}

	/**
	 * Resets branch.
	 * 
	 * @param refName
	 *            Full name of the branch.
	 * @param type
	 *            Type of the reset.
	 */
	public void reset(String refName, ResetType type) throws CoreException {
		new ResetOperation(repository, refName, type).execute(null);
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
	 * Merge the given ref with the current HEAD, using the default (logical)
	 * strategy.
	 * 
	 * @param refName
	 *            Name of a commit to merge with the current HEAD.
	 */
	public void mergeLogical(String refName) throws CoreException {
		new MergeOperation(repository, refName).execute(null);
	}

	/**
	 * Merge the given ref with the current HEAD, using the textual "recursive"
	 * strategy.
	 * 
	 * @param refName
	 *            Name of a commit to merge with the current HEAD.
	 */
	public void mergeTextual(String refName) throws CoreException {
		new MergeOperation(repository, refName,
				MergeStrategy.RECURSIVE.getName()).execute(null);
	}

	/**
	 * Returns the status of this repository's files as would "git status".
	 * 
	 * @return
	 * @throws Exception
	 */
	public Status status() throws Exception {
		Git git = null;
		try {
			git = new Git(repository);
			return git.status().call();
		} finally {
			git.close();
		}
	}

	/**
	 * Return the commit with the given name if any.
	 * @param revstr see {@link Repository#resolve(String)}
	 * @return The commit with the given name if any.
	 * @see {@link Repository#resolve(String)}
	 */
	public RevCommit findCommit(String revstr) throws Exception {
		RevWalk walk = null;
		try {
			walk = new RevWalk(repository);
			return walk.parseCommit(repository.resolve(revstr));
		} finally {
			if (walk != null) {
				walk.close();
			}
		}
	}

	/**
	 * Dispose of this wrapper along with its underlying repository.
	 */
	public void dispose() {
		if (repository != null) {
			repository.close();
			repository = null;
		}
		for (Runnable disposer : disposers) {
			disposer.run();
		}
		disposers.clear();
	}

	/**
	 * Simulate a comparison between the two given references and returns back
	 * the subscriber that can provide all computed synchronization information.
	 * It will use the local comparison context for retrieving the resource mappings.
	 * 
	 * @param sourceRef
	 *            Source reference (i.e. "left" side of the comparison).
	 * @param targetRef
	 *            Target reference (i.e. "right" side of the comparison).
	 * @param comparedFile
	 *            The file we are comparing (that would be the file
	 *            right-clicked into the workspace).
	 * @return The created subscriber.
	 */
	public Subscriber createSubscriberForComparison(String sourceRef,
			String targetRef, IFile comparedFile) throws IOException {
		final GitSynchronizeData data = new GitSynchronizeData(repository,
				sourceRef, targetRef, false);
		final GitSynchronizeDataSet dataSet = new GitSynchronizeDataSet(data);
		final ResourceMapping[] mappings = getResourceMappings(comparedFile, ResourceMappingContext.LOCAL_CONTEXT);
		final GitResourceVariantTreeSubscriber subscriber = new GitResourceVariantTreeSubscriber(
				dataSet);
		subscriber.init(new NullProgressMonitor());

		final RemoteResourceMappingContext remoteContext = new GitSubscriberResourceMappingContext(
				subscriber, dataSet);
		final SubscriberScopeManager manager = new SubscriberScopeManager(
				subscriber.getName(), mappings, subscriber, remoteContext, true);
		final GitSubscriberMergeContext context = new GitSubscriberMergeContext(
				subscriber, manager, dataSet);
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
	 * Simulate a comparison between the two given references and returns back
	 * the subscriber that can provide all computed synchronization information.
	 * It will use a remote comparison context for retrieving the resource mappings.
	 * 
	 * @param sourceRef
	 *            Source reference (i.e. "left" side of the comparison).
	 * @param targetRef
	 *            Target reference (i.e. "right" side of the comparison).
	 * @param comparedFile
	 *            The file we are comparing (that would be the file
	 *            right-clicked into the workspace).
	 * @return The created subscriber.
	 */
	public Subscriber createSubscriberForComparisonWithRemoteMappings(String sourceRef,
			String targetRef, IFile comparedFile) throws IOException {
		final GitSynchronizeData data = new GitSynchronizeData(repository,
				sourceRef, targetRef, false);
		final GitSynchronizeDataSet dataSet = new GitSynchronizeDataSet(data);
		final GitResourceVariantTreeSubscriber subscriber = new GitResourceVariantTreeSubscriber(
				dataSet);
		subscriber.init(new NullProgressMonitor());

		final RemoteResourceMappingContext remoteContext = new GitSubscriberResourceMappingContext(
				subscriber, dataSet);
		final ResourceMapping[] mappings = getResourceMappings(comparedFile, remoteContext);
		final SubscriberScopeManager manager = new SubscriberScopeManager(
				subscriber.getName(), mappings, subscriber, remoteContext, true);
		final GitSubscriberMergeContext context = new GitSubscriberMergeContext(
				subscriber, manager, dataSet);
		disposers.add(new Runnable() {
			public void run() {
				manager.dispose();
				context.dispose();
				subscriber.dispose();
			}
		});
		return context.getSubscriber();
	}

	public String getRepoRelativePath(File file) {
		return getRepoRelativePath(new Path(file.getPath()).toString());
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

	/**
	 * This will query all model providers for those that are enabled on the
	 * given file and list all mappings available for that file.
	 * 
	 * @param file
	 *            The file for which we need the associated resource mappings.
	 * @param context 
	 *            The {@link ResourceMappingContext} that will be used for retrieving the mappings.
	 * @return All mappings available for that file.
	 */
	private static ResourceMapping[] getResourceMappings(IFile file, ResourceMappingContext context) {
		final IModelProviderDescriptor[] modelDescriptors = ModelProvider
				.getModelProviderDescriptors();

		final Set<ResourceMapping> mappings = new LinkedHashSet<ResourceMapping>();
		for (IModelProviderDescriptor candidate : modelDescriptors) {
			try {
				final IResource[] resources = candidate
						.getMatchingResources(new IResource[] { file, });
				if (resources.length > 0) {
					// get mappings from model provider if there are matching
					// resources
					final ModelProvider model = candidate.getModelProvider();
					final ResourceMapping[] modelMappings = model.getMappings(
							file, context, null);
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
