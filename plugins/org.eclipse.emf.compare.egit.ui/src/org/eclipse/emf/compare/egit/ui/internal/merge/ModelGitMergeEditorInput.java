/*******************************************************************************
 * Copyright (C) 2010, 2014 Mathias Kinzler <mathias.kinzler@sap.com> and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.ui.internal.merge;

//CHECKSTYLE:OFF
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.internal.indexdiff.IndexDiffCache;
import org.eclipse.egit.core.internal.indexdiff.IndexDiffCacheEntry;
import org.eclipse.egit.core.internal.indexdiff.IndexDiffData;
import org.eclipse.egit.core.internal.job.RuleUtil;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.egit.ui.internal.merge.MergeInputMode;
import org.eclipse.emf.compare.egit.internal.merge.DirCacheResourceVariantTreeProvider;
import org.eclipse.emf.compare.egit.internal.merge.GitResourceVariantTreeProvider;
import org.eclipse.emf.compare.egit.internal.merge.GitResourceVariantTreeSubscriber;
import org.eclipse.emf.compare.egit.internal.merge.LogicalModels;
import org.eclipse.emf.compare.egit.ui.internal.EMFCompareEGitUIMessages;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.core.mapping.ISynchronizationScopeManager;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberMergeContext;
import org.eclipse.team.core.subscribers.SubscriberResourceMappingContext;
import org.eclipse.team.core.subscribers.SubscriberScopeManager;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;

/**
 * A Git-specific {@link CompareEditorInput}. This class is a copy from
 * org.eclipse.egit.ui.internal.merge.GitMergeEditorInput.
 * 
 * @author <a href="mailto:mathias.kinzler@sap.com">Mathias Kinzler</a>
 */
@SuppressWarnings("restriction")
public class ModelGitMergeEditorInput extends GitMergeEditorInput {

	private final boolean useWS;

	private final IPath[] paths;

	public ModelGitMergeEditorInput(MergeInputMode mode, IPath... locations) {
		super(mode, locations);
		this.useWS = !MergeInputMode.STAGE_2.equals(mode);
		this.paths = locations;
	}

	/**
	 * Even if there is a single file involved in this operation, it may have a custom comparator or merger
	 * defined. This will be found through its specific ISynchronizationCompareAdapter.
	 * <p>
	 * If there are multiple files involved, we need them all to be part of the same logical model. Otherwise,
	 * we can't be sure that multiple ISynchronizationCompareAdapter aren't interested in the different files,
	 * and thus cannot show a valid 'aggregate' compare editor.
	 * </p>
	 * <p>
	 * Then again, even if the multiple files involved are all part of single-file models (i.e. none of them
	 * is a part of a larger logical model, and we have as many models involved as there are files), we cannot
	 * show them all within the same compare editor input. Comparing the files and determining conflicts is
	 * the job of the ISynchronizationCompareAdapter(s), <u>not</u> ours. If we cannot reliably find the
	 * appropriate compare adapter, we should not try and compare the files ourselves. The user will have to
	 * manually open the merge tool on each individual logical model.
	 * </p>
	 * <p>
	 * Since we cannot determine the logical model of a file that is not in the workspace, this will fall back
	 * to the 'old' merge tool (we compute the diffs ourselves without consideration for the file type) iff
	 * there are <u>only</u> such files in the locations set.
	 * </p>
	 *
	 * @param repository
	 *            Repository within which the compared files are located.
	 * @param filterPaths
	 *            repository-relative paths of the resources we are comparing.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @return The useable compare input.
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	@Override
	protected ICompareInput prepareCompareInput(Repository repository, Collection<String> filterPaths,
			IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			final GitResourceVariantTreeProvider variantTreeProvider = new DirCacheResourceVariantTreeProvider(
					repository, useWS);
			final Subscriber subscriber = new GitResourceVariantTreeSubscriber(variantTreeProvider);
			checkCanceled(monitor);

			final Set<IProject> projects = new LinkedHashSet<IProject>();
			for (IResource root : subscriber.roots()) {
				projects.add(root.getProject());
			}

			// Compute the set of IResources involved in this operation.
			// This will be cut short if we find that at least one resource is
			// in the workspace and at least one resource is not.
			final Set<IResource> resourcesInOperation = new LinkedHashSet<IResource>();
			boolean outOfWS = false;
			for (IPath path : paths) {
				boolean foundMatchInWS = false;
				final Iterator<IProject> projectIterator = projects.iterator();
				while (!foundMatchInWS && projectIterator.hasNext()) {
					final IProject project = projectIterator.next();
					final IPath projectLocation = project.getLocation();
					if (projectLocation.equals(path)) {
						resourcesInOperation.addAll(getConflictingFilesFrom(project));
						foundMatchInWS = true;
					} else if (project.getLocation().isPrefixOf(path)) {
						final IResource resource = ResourceUtil.getResourceForLocation(path, false);
						if (resource instanceof IContainer) {
							resourcesInOperation.addAll(getConflictingFilesFrom((IContainer)resource));
						} else {
							resourcesInOperation.add(resource);
						}
						foundMatchInWS = true;
					}
				}
				if (!foundMatchInWS) {
					if (!resourcesInOperation.isEmpty()) {
						// no need to go any further : we have both files in the
						// workspace and files outside of it
						break;
					} else {
						// for now, all paths are out of the ws
						outOfWS = true;
					}
				} else if (outOfWS) {
					// There was a match in the workspace for this one
					// yet at least one path before that was out of the ws
					break;
				}
			}

			checkCanceled(monitor);

			if (!resourcesInOperation.isEmpty() && outOfWS) {
				// At least one resource is in the workspace while at least one
				// is out of it.
				// We cannot reliably tell whether they are related enough to be
				// in the same compare editor.
				throw new InvocationTargetException(new IllegalStateException(
						EMFCompareEGitUIMessages.getString("GitMergeEditorInput_OutOfWSResources"))); //$NON-NLS-1$
			} else if (resourcesInOperation.isEmpty()) {
				// All resources are out of the workspace.
				// Fall back to the workspace-unaware "prepareDiffInput"
			} else {
				final RemoteResourceMappingContext remoteMappingContext = new SubscriberResourceMappingContext(
						subscriber, true);
				// Make sure that all of the compared resources are either
				// - all part of the same model, or
				// - not part of any model.
				Set<IResource> model = null;
				for (IResource comparedResource : resourcesInOperation) {
					model = LogicalModels.discoverModel(comparedResource, remoteMappingContext);
					if (model.isEmpty()) {
						// not part of any model... carry on
					} else {
						if (!model.containsAll(resourcesInOperation)) {
							// These resources belong to multiple different
							// models.
							// The merge tool needs to be launched manually on
							// each distinct logical model.
							throw new RuntimeException(
									EMFCompareEGitUIMessages.getString("GitMergeEditorInput_MultipleModels")); //$NON-NLS-1$
						} else {
							// No use going further : we know these resource all
							// belong to the same model.
							break;
						}
					}
				}

				final ISynchronizationCompareAdapter compareAdapter = LogicalModels.findAdapter(model,
						ISynchronizationCompareAdapter.class);
				if (compareAdapter != null) {
					final Set<ResourceMapping> allMappings = LogicalModels.getResourceMappings(model,
							remoteMappingContext);

					checkCanceled(monitor);

					final ISynchronizationContext synchronizationContext = prepareSynchronizationContext(
							repository, subscriber, allMappings, remoteMappingContext);
					final Object modelObject = allMappings.iterator().next().getModelObject();
					if (compareAdapter.hasCompareInput(synchronizationContext, modelObject)) {
						return compareAdapter.asCompareInput(synchronizationContext, modelObject);
					} else {
						// This compare adapter does not know about our model
						// object
					}
				} else {
					// There isn't a specific compare adapter for this logical
					// model. Fall back to default.
				}
			}
		} catch (IOException | CoreException e) {
			throw new InvocationTargetException(e);
		}
		return null;
	}

	private void checkCanceled(IProgressMonitor monitor) throws InterruptedException {
		if (monitor.isCanceled()) {
			throw new InterruptedException();
		}
	}

	private Set<IResource> getConflictingFilesFrom(IContainer container) throws IOException {
		final Set<IResource> conflictingResources = new LinkedHashSet<IResource>();
		final RepositoryMapping mapping = RepositoryMapping.getMapping(container);
		if (mapping == null) {
			return conflictingResources;
		}
		final IndexDiffCacheEntry indexDiffCacheEntry = IndexDiffCache.INSTANCE
				.getIndexDiffCacheEntry(mapping.getRepository());
		if (indexDiffCacheEntry == null) {
			return conflictingResources;
		}
		final IndexDiffData indexDiffData = indexDiffCacheEntry.getIndexDiff();
		if (indexDiffData != null) {
			final IPath containerPath = container.getLocation();
			final File workTree = mapping.getWorkTree();
			if (workTree != null) {
				final IPath workDirPrefix = new Path(workTree.getCanonicalPath());
				for (String conflicting : indexDiffData.getConflicting()) {
					final IPath resourcePath = workDirPrefix.append(conflicting);
					if (containerPath.isPrefixOf(resourcePath)) {
						final IPath containerRelativePath = resourcePath
								.removeFirstSegments(containerPath.segmentCount());
						conflictingResources.add(container.getFile(containerRelativePath));
					}
				}
			}
		}
		return conflictingResources;
	}

	private ISynchronizationContext prepareSynchronizationContext(final Repository repository,
			Subscriber subscriber, Set<ResourceMapping> allModelMappings,
			RemoteResourceMappingContext mappingContext)
			throws CoreException, OperationCanceledException, InterruptedException {
		final ResourceMapping[] mappings = allModelMappings
				.toArray(new ResourceMapping[allModelMappings.size()]);

		final ISynchronizationScopeManager manager = new InternalSubscriberScopeManager(subscriber.getName(),
				mappings, subscriber, mappingContext, true, repository);
		manager.initialize(new NullProgressMonitor());

		final ISynchronizationContext context = new GitSynchronizationContext(subscriber, manager);
		// Wait for the asynchronous scope expanding to end (started from the
		// initialization of our synchronization context)
		Job.getJobManager().join(context, new NullProgressMonitor());

		return context;
	}

	private static class GitSynchronizationContext extends SubscriberMergeContext {
		public GitSynchronizationContext(Subscriber subscriber, ISynchronizationScopeManager scopeManager) {
			super(subscriber, scopeManager);
			initialize();
		}

		public void markAsMerged(IDiff node, boolean inSyncHint, IProgressMonitor monitor)
				throws CoreException {
			// Won't be used as a merging context
		}

		public void reject(IDiff diff, IProgressMonitor monitor) throws CoreException {
			// Won't be used as a merging context
		}

		@Override
		protected void makeInSync(IDiff diff, IProgressMonitor monitor) throws CoreException {
			// Won't be used as a merging context
		}

		@Override
		public void dispose() {
			super.dispose();
		}
	}

	private static final class InternalSubscriberScopeManager extends SubscriberScopeManager {
		private final Repository repository;

		public InternalSubscriberScopeManager(String name, ResourceMapping[] inputMappings,
				Subscriber subscriber, RemoteResourceMappingContext context, boolean consultModels,
				Repository repository) {
			super(name, inputMappings, subscriber, context, consultModels);
			this.repository = repository;
		}

		@Override
		public ISchedulingRule getSchedulingRule() {
			return RuleUtil.getRule(repository);
		}
	}

}
// CHECKSTYLE:ON
