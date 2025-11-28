/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *	 Laurent Goubet <laurent.goubet@obeo.fr> - initial API and implementation
 *	 Axel Richard <axel.richard@obeo.fr> - Update #registerHandledFiles()
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal.merge;

//CHECKSTYLE:OFF
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.Activator;
import org.eclipse.egit.core.internal.job.RuleUtil;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.emf.compare.egit.internal.storage.AbstractGitResourceVariant;
import org.eclipse.emf.compare.egit.internal.storage.TreeParserResourceVariant;
import org.eclipse.emf.compare.egit.internal.wrapper.JGitProgressMonitorWrapper;
import org.eclipse.emf.compare.rcp.EMFCompareLogger;
import org.eclipse.jgit.attributes.Attributes;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.dircache.DirCacheBuildIterator;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.dircache.DirCacheIterator;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.merge.MergeResult;
import org.eclipse.jgit.merge.RecursiveMerger;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.WorkingTreeIterator;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.mapping.IMergeContext;
import org.eclipse.team.core.mapping.IResourceMappingMerger;
import org.eclipse.team.core.mapping.ISynchronizationScopeManager;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberMergeContext;
import org.eclipse.team.core.subscribers.SubscriberResourceMappingContext;
import org.eclipse.team.core.subscribers.SubscriberScopeManager;
import org.eclipse.team.core.variants.IResourceVariant;

/**
 * This extends the recursive merger in order to take into account specific mergers provided by the Team
 * {@link org.eclipse.core.resources.mapping.ModelProvider model providers}.
 * <p>
 * The Recursive Merger handles files one-by-one, calling file-specific merge drivers for each. On the
 * opposite, this strategy can handle bigger sets of files at once, delegating the merge to the files' model.
 * As such, file-specific merge drivers may not be called from this strategy if that file is part of a larger
 * model.
 * </p>
 * <p>
 * Any file that is <b>not</b> part of a model, which model cannot be determined, or which model does not
 * specify a custom merger, will be handled as it would by the RecursiveMerger.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public class RecursiveModelMerger extends RecursiveMerger {

	private static final EMFCompareLogger LOGGER = new EMFCompareLogger(RecursiveModelMerger.class);

	/**
	 * This will be populated during the course of the RecursiveMappingMergers' executions. These files have
	 * been cleanly merged and we should thus make sure the DirCacheBuilder takes their latest working
	 * directory version before committing.
	 */
	private final Set<String> makeInSync = new LinkedHashSet<>();

	/**
	 * keeps track of the files we've already merged. Since we iterate one file at a time but may merge
	 * multiple files at once when they are part of the same model, this will help us avoid merging the same
	 * file or model twice.
	 */
	private final Set<String> handledPaths = new HashSet<>();

	private AbstractTreeIterator aBaseTree;

	private RevTree aHeadTree;

	private RevTree aMergeTree;

	/**
	 * Finding a resource merger for a given set of resources can be a time consuming operation for large
	 * models. If we've already computed a merger for a given set, do not re-compute.
	 */
	private Map<Set<IResource>, IResourceMappingMerger> cachedMergers = new LinkedHashMap<>();

	/**
	 * Default recursive model merger.
	 *
	 * @param db
	 * @param inCore
	 */
	public RecursiveModelMerger(Repository db, boolean inCore) {
		super(db, inCore);
	}

	@Override
	protected boolean mergeTrees(AbstractTreeIterator baseTree, RevTree headTree, RevTree mergeTree,
			boolean ignoreConflicts) throws IOException {
		// We need to keep track on all version to be merged to be able to build a new TreeWalk in
		// #mergeTreeWalk
		this.aBaseTree = baseTree;
		this.aHeadTree = headTree;
		this.aMergeTree = mergeTree;
		return super.mergeTrees(baseTree, headTree, mergeTree, ignoreConflicts);
	}

	@Override
	protected boolean mergeTreeWalk(TreeWalk treeWalk, boolean ignoreConflicts) throws IOException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("STARTING Recursive model merge."); //$NON-NLS-1$
		}
		final TreeWalkResourceVariantTreeProvider variantTreeProvider = new TreeWalkResourceVariantTreeProvider.Builder()//
				.setRepository(getRepository())//
				.setaBaseTree(aBaseTree)//
				.setHeadTree(aHeadTree)//
				.setMergeTree(aMergeTree)//
				.setDircache(dircache) //
				.setReader(reader) //
				.build();

		final GitResourceVariantTreeSubscriber subscriber = new GitResourceVariantTreeSubscriber(
				variantTreeProvider);
		final RemoteResourceMappingContext remoteMappingContext = new SubscriberResourceMappingContext(
				subscriber, true);

		try {
			refreshRoots(subscriber.roots());
		} catch (CoreException e) {
			// We cannot be sure that Team and/or the merger implementations
			// will properly handle unrefreshed files. Fall back to merging
			// without workspace awareness.
			Activator.logError(MergeText.RecursiveModelMerger_RefreshError, e);
			return super.mergeTreeWalk(treeWalk, ignoreConflicts);
		}

		monitor.beginTask(MergeText.RecursiveModelMerger_BuildLogicalModels, ProgressMonitor.UNKNOWN);
		// Eager lookup for the logical models to avoid issues in case we
		// iterate over a file that does not exist locally before the rest of
		// its logical model.
		final LogicalModels logicalModels = new LogicalModels();
		logicalModels.build(variantTreeProvider.getKnownResources(), remoteMappingContext);
		monitor.endTask();

		if (monitor.isCancelled()) {
			throw new OperationCanceledException();
		}

		// The treewalk might decide to handle files with no differences by itself instead of leaving that
		// decision to us (id treewalk.getFilter() is "AnyDiff", then it'll skip over the "in-sync" files
		// while adding them to the dircache at the same time). In such a case, we need to prevent ModelMerge
		// from adding the "unchanged" files (which require no merging) in the DirCacheBuilder, otherwise
		// we'll end up with "duplicate stage not allowed" exceptions at the end of merge operations
		// (dirCache.finish()).
		// see also bug #535200
		boolean handleUnchangedFiles = true;
		if (treeWalk.getFilter() == TreeFilter.ANY_DIFF) {
			handleUnchangedFiles = false;
		}

		// We are done with the setup. We can now iterate over the tree walk and
		// either delegate to the logical model's merger if any or fall back to
		// standard git merging. Basically, any file that is not a part of a
		// logical model that defines its own specific merger will be handled as
		// it would by the RecursiveMerger.
		while (treeWalk.next()) {
			final int modeBase = treeWalk.getRawMode(T_BASE);
			final int modeOurs = treeWalk.getRawMode(T_OURS);
			final int modeTheirs = treeWalk.getRawMode(T_THEIRS);
			if (modeBase == 0 && modeOurs == 0 && modeTheirs == 0) {
				// untracked
				continue;
			}
			final String path = treeWalk.getPathString();
			if (handledPaths.contains(path)) {
				// This one has been handled as a result of a previous model
				// merge. Simply make sure we use its latest content if it is
				// not in conflict.
				if (treeWalk.isSubtree() && enterSubtree) {
					treeWalk.enterSubtree();
				}
				if (!unmergedPaths.contains(path)) {
					registerMergedPath(path);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Ignoring previously handled file: " + path); //$NON-NLS-1$
					}
				}
				continue;
			}

			final int nonZeroMode = modeBase != 0 ? modeBase : modeOurs != 0 ? modeOurs : modeTheirs;
			final IResource resource = variantTreeProvider.getResourceHandleForLocation(getRepository(), path,
					FileMode.fromBits(nonZeroMode) == FileMode.TREE);
			Set<IResource> logicalModel = logicalModels.getModel(resource);

			IResourceMappingMerger modelMerger = null;
			if (logicalModel != null) {
				try {
					modelMerger = getResourceMappingMerger(logicalModel);
				} catch (CoreException e) {
					Activator.logError(MergeText.RecursiveModelMerger_AdaptError, e);
					// ignore this model and fall back to default
					if (!fallBackToDefaultMerge(treeWalk, ignoreConflicts)) {
						workTreeUpdater.revertModifiedFiles();
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info(
									"FAILED - Recursive model merge, could not find appropriate merger and default merge failed."); //$NON-NLS-1$
						}
						return false;
					}
				}
			}
			if (modelMerger != null) {
				enterSubtree = true;

				boolean success = new ModelMerge(this, subscriber, remoteMappingContext, path, logicalModel,
						modelMerger, handleUnchangedFiles).run(new JGitProgressMonitorWrapper(monitor));
				if (success) {
					for (IResource res : logicalModel) {
						try {
							res.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
						} catch (CoreException e) {
							// Carry on with the merge, the workspace won't be in sync with the filesystem
						}
					}
				}
				if (!success) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("FAILED - Recursive model merge."); //$NON-NLS-1$
					}
					return false;
				} else if (!unmergedPaths.contains(path)) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Merged model file: " + path); //$NON-NLS-1$
					}
					registerMergedPath(path);
				}
				if (treeWalk.isSubtree()) {
					enterSubtree = true;
				}
			} else if (!fallBackToDefaultMerge(treeWalk, ignoreConflicts)) {
				workTreeUpdater.revertModifiedFiles();
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("FAILED - Recursive model merge, default merge failed."); //$NON-NLS-1$
				}
				return false;
			} else {
				// fallBackToDefaultMerge returned true
				if (resource != null && resource.exists()) {
					try {
						resource.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
					} catch (CoreException e) {
						// Carry on with the merge, the workspace won't be in sync with the filesystem
					}
				}
			}
			if (treeWalk.isSubtree() && enterSubtree) {
				treeWalk.enterSubtree();
			}
		}
		if (!makeInSync.isEmpty()) {
			indexModelMergedFiles();
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("SUCCESS - Recursive model merge."); //$NON-NLS-1$
		}
		cachedMergers.clear();
		return true;
	}

	protected IResourceMappingMerger getResourceMappingMerger(Set<IResource> logicalModel)
			throws CoreException {
		IResourceMappingMerger cached = cachedMergers.get(logicalModel);
		if (cached == null) {
			cached = LogicalModels.findAdapter(logicalModel, IResourceMappingMerger.class);
			cachedMergers.put(logicalModel, cached);
		}
		return cached;
	}

	private boolean fallBackToDefaultMerge(TreeWalk treeWalk, boolean ignoreConflicts)
			throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		boolean hasWorkingTreeIterator = tw.getTreeCount() > T_FILE;
		boolean hasAttributeNodeProvider = treeWalk.getAttributesNodeProvider() != null;
		Attributes[] attributes = {new Attributes(), new Attributes(), new Attributes() };
		if (hasAttributeNodeProvider) {
			attributes[T_BASE] = treeWalk.getAttributes(T_BASE);
			attributes[T_OURS] = treeWalk.getAttributes(T_OURS);
			attributes[T_THEIRS] = treeWalk.getAttributes(T_THEIRS);
		}
		return processEntry(treeWalk.getTree(T_BASE, CanonicalTreeParser.class),
				treeWalk.getTree(T_OURS, CanonicalTreeParser.class),
				treeWalk.getTree(T_THEIRS, CanonicalTreeParser.class),
				treeWalk.getTree(T_INDEX, DirCacheBuildIterator.class),
				hasWorkingTreeIterator ? treeWalk.getTree(T_FILE, WorkingTreeIterator.class) : null,
				ignoreConflicts, attributes);
	}

	/**
	 * Add files modified by model mergers to the index.
	 *
	 * @throws CorruptObjectException
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws IOException
	 */
	@SuppressWarnings("resource") // We're not in charge of closing the ObjectInserter
	private void indexModelMergedFiles()
			throws CorruptObjectException, MissingObjectException, IncorrectObjectTypeException, IOException {
		try (TreeWalk syncingTreeWalk = new TreeWalk(getRepository())) {
			int dirCacheIndex = syncingTreeWalk.addTree(new DirCacheIterator(dircache));

			// Setting the DirCacheIterator is required to be able to handle properly Git attributes
			FileTreeIterator aWorkingTreeIterator = new FileTreeIterator(getRepository());
			syncingTreeWalk.addTree(aWorkingTreeIterator);
			aWorkingTreeIterator.setDirCacheIterator(syncingTreeWalk, dirCacheIndex);

			syncingTreeWalk.setRecursive(true);
			syncingTreeWalk.setFilter(PathFilterGroup.createFromStrings(makeInSync));
			String lastAdded = null;
			while (syncingTreeWalk.next()) {
				String path = syncingTreeWalk.getPathString();
				if (path.equals(lastAdded)) {
					continue;
				}

				WorkingTreeIterator workingTree = syncingTreeWalk.getTree(1, WorkingTreeIterator.class);
				DirCacheIterator dirCache = syncingTreeWalk.getTree(0, DirCacheIterator.class);
				if (dirCache == null && workingTree != null && workingTree.isEntryIgnored()) {
					// nothing to do on this one
				} else if (workingTree != null) {
					if (dirCache == null || dirCache.getDirCacheEntry() == null
							|| !dirCache.getDirCacheEntry().isAssumeValid()) {
						final DirCacheEntry dce = new DirCacheEntry(path);
						final FileMode mode = workingTree.getIndexFileMode(dirCache);
						dce.setFileMode(mode);

						final ObjectId id;
						Instant lastModified = Instant.ofEpochSecond(0);
						int length = 0;
						if (FileMode.GITLINK != mode) {
							// Note that the size was internally cast to int by DirCacheEntry.setLength(long)
							// as well
							length = (int)workingTree.getEntryLength();
							lastModified = workingTree.getEntryLastModifiedInstant();
							try (InputStream is = workingTree.openEntryStream()) {
								id = getObjectInserter().insert(Constants.OBJ_BLOB,
										workingTree.getEntryContentLength(), is);
							}
						} else {
							id = workingTree.getEntryObjectId();
						}
						workTreeUpdater.addExistingToIndex(id, syncingTreeWalk.getRawPath(), mode,
								DirCacheEntry.STAGE_0, lastModified, length);
						lastAdded = path;
					} else {
						addExistingToIndex(dirCache.getDirCacheEntry());
					}
				} else if (dirCache != null && FileMode.GITLINK == dirCache.getEntryFileMode()) {
					addExistingToIndex(dirCache.getDirCacheEntry());
				}
			}
		}
	}

	private static String getRepoRelativePath(IResource file) {
		final RepositoryMapping mapping = RepositoryMapping.getMapping(file);
		if (mapping != null) {
			return mapping.getRepoRelativePath(file);
		}
		return null;
	}

	/**
	 * On many aspects, team relies on the refreshed state of the workspace files, notably to determine if a
	 * file is in sync or not. Since we could have been called for a rebase, rebase that checked out a new
	 * commit without refreshing the workspace afterwards, team could see "in-sync" files even though they no
	 * longer exist in the workspace. This should be called before any merging takes place to make sure all
	 * files concerned by this merge operation are refreshed beforehand.
	 *
	 * @param resources
	 *            The set of resource roots to refresh.
	 * @throws CoreException
	 *             Thrown whenever we fail at refreshing one of the resources or its children.
	 */
	private void refreshRoots(IResource[] resources) throws CoreException {
		for (IResource root : resources) {
			if (root.isAccessible()) {
				root.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			}
		}
	}

	private void markConflict(String filePath, WorkTreeUpdater updater, TreeParserResourceVariant baseVariant,
			TreeParserResourceVariant ourVariant, TreeParserResourceVariant theirVariant) {
		add(filePath, updater, baseVariant, DirCacheEntry.STAGE_1);
		add(filePath, updater, ourVariant, DirCacheEntry.STAGE_2);
		add(filePath, updater, theirVariant, DirCacheEntry.STAGE_3);
	}

	private void add(String path, WorkTreeUpdater updater, TreeParserResourceVariant variant, int stage) {
		if (variant != null && !FileMode.TREE.equals(variant.getRawMode())) {
			updater.addExistingToIndex(variant.getObjectId(), Constants.encode(path),
					FileMode.fromBits(variant.getRawMode()), stage, Instant.ofEpochSecond(0), 0);
		}
	}

	private void addExistingToIndex(DirCacheEntry dce) {
		workTreeUpdater.addExistingToIndex(dce.getObjectId(), dce.getRawPath(), dce.getFileMode(),
				dce.getStage(), dce.getLastModifiedInstant(), dce.getLength());
	}

	private static class ModelMerge {
		private final RecursiveModelMerger merger;

		private final GitResourceVariantTreeSubscriber subscriber;

		private final RemoteResourceMappingContext remoteMappingContext;

		private final String path;

		private final Set<IResource> logicalModel;

		private final IResourceMappingMerger modelMerger;

		private final boolean handleUnchangedFiles;

		public ModelMerge(RecursiveModelMerger merger, GitResourceVariantTreeSubscriber subscriber,
				RemoteResourceMappingContext remoteMappingContext, String path, Set<IResource> logicalModel,
				IResourceMappingMerger modelMerger, boolean handleUnchangedFiles) {
			this.merger = merger;
			this.subscriber = subscriber;
			this.remoteMappingContext = remoteMappingContext;
			this.path = path;
			this.logicalModel = logicalModel;
			this.modelMerger = modelMerger;
			this.handleUnchangedFiles = handleUnchangedFiles;
		}

		private boolean run(IProgressMonitor monitor) throws CorruptObjectException, IOException {
			SubMonitor progress = SubMonitor.convert(monitor, 1);
			IMergeContext mergeContext = null;
			try {
				mergeContext = prepareMergeContext();
				final IStatus status = modelMerger.merge(mergeContext, progress.newChild(1));
				registerHandledFiles(mergeContext, status);
			} catch (CoreException e) {
				Activator.logError(e.getMessage(), e);
				merger.workTreeUpdater.revertModifiedFiles();
				return false;
			} catch (OperationCanceledException e) {
				final String message = NLS.bind(MergeText.RecursiveModelMerger_ScopeInitializationInterrupted,
						path);
				Activator.logError(message, e);
				merger.workTreeUpdater.revertModifiedFiles();
				return false;
			} finally {
				if (mergeContext != null) {
					mergeContext.dispose();
				}
			}
			return true;
		}

		private void registerHandledFiles(final IMergeContext mergeContext, final IStatus status)
				throws TeamException, CoreException {
			for (IResource handledFile : logicalModel) {
				String filePath = getRepoRelativePath(handledFile);
				if (filePath == null) {
					IResourceVariant sourceVariant = subscriber.getSourceTree()
							.getResourceVariant(handledFile);
					IResourceVariant remoteVariant = subscriber.getRemoteTree()
							.getResourceVariant(handledFile);
					IResourceVariant baseVariant = subscriber.getBaseTree().getResourceVariant(handledFile);
					if (sourceVariant instanceof AbstractGitResourceVariant) {
						// In the case of a conflict, the file may not exist
						// So don't check for file existence
						filePath = ((AbstractGitResourceVariant)sourceVariant).getPath();
					}
					if (filePath == null && remoteVariant instanceof AbstractGitResourceVariant) {
						// In the case of a conflict, the file may not exist
						// So don't check for file existence
						filePath = ((AbstractGitResourceVariant)remoteVariant).getPath();
					}
					if (filePath == null && baseVariant instanceof AbstractGitResourceVariant) {
						// In the case of a conflict, the file may not exist
						// So don't check for file existence
						filePath = ((AbstractGitResourceVariant)baseVariant).getPath();
					}

				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Registering handled file " + filePath); //$NON-NLS-1$
				}

				if (merger.handledPaths.contains(filePath)) {
					continue;
				}

				if (filePath != null) {
					merger.modifiedFiles.add(filePath);
					merger.handledPaths.add(filePath);
				} else if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Impossible to compute a repo-relative filePath for file " //$NON-NLS-1$
							+ handledFile);
				}

				// The merge failed. If some parts of the model were
				// auto-mergeable, the model merger told us so through
				// GitMergeContext#markAsMerged() (stored within #makeInSync).
				// All other components of the logical model should be marked as
				// conflicts.
				if (mergeContext.getDiffTree().getDiff(handledFile) == null) {
					// If no diff, the model merger does... nothing
					// Make sure this file will be added to the index.
					if (handleUnchangedFiles) {
						merger.registerMergedPath(filePath);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Merged non-modified file: " + filePath); //$NON-NLS-1$
						}
					}
				} else if (filePath != null && status.getSeverity() != IStatus.OK) {
					if (merger.makeInSync.contains(filePath)) {
						// The conflict has priority over the merge. This only can happen when
						// the comparison could not finish successful.
						merger.makeInSync.remove(filePath);
					}
					merger.unmergedPaths.add(filePath);
					merger.mergeResults.put(filePath, new MergeResult<>(Collections.<RawText> emptyList()));
					final TreeParserResourceVariant baseVariant = (TreeParserResourceVariant)subscriber
							.getBaseTree().getResourceVariant(handledFile);
					final TreeParserResourceVariant ourVariant = (TreeParserResourceVariant)subscriber
							.getSourceTree().getResourceVariant(handledFile);
					final TreeParserResourceVariant theirVariant = (TreeParserResourceVariant)subscriber
							.getRemoteTree().getResourceVariant(handledFile);
					merger.markConflict(filePath, merger.workTreeUpdater, baseVariant, ourVariant,
							theirVariant);

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Marking conflict on " + filePath); //$NON-NLS-1$
					}
				}
			}
		}

		/**
		 * Create and initialize the merge context for the given model.
		 *
		 * @return An initialized merge context for the given model.
		 * @throws CoreException
		 *             Thrown if we cannot initialize the scope for this merge context.
		 * @throws OperationCanceledException
		 *             Thrown if the user cancelled the initialization.
		 */
		private IMergeContext prepareMergeContext() throws CoreException, OperationCanceledException {
			final Set<ResourceMapping> allMappings = LogicalModels.getResourceMappings(logicalModel,
					remoteMappingContext);
			final ResourceMapping[] mappings = allMappings.toArray(new ResourceMapping[allMappings.size()]);

			final ISynchronizationScopeManager manager = new SubscriberScopeManager(subscriber.getName(),
					mappings, subscriber, remoteMappingContext, true) {
				@SuppressWarnings("resource") // We are not in charge of closing the repository
				@Override
				public ISchedulingRule getSchedulingRule() {
					return RuleUtil.getRule(merger.getRepository());
				}
			};
			manager.initialize(new NullProgressMonitor());

			final IMergeContext context = new GitMergeContext(merger, subscriber, manager);
			// Wait for the asynchronous scope expanding to end (started from
			// the initialization of our merge context)
			waitForScope(context);

			return context;
		}

		private void waitForScope(IMergeContext context) {
			// The UILockListener might prevent us from properly joining.
			boolean joined = false;
			while (!joined) {
				try {
					Job.getJobManager().join(context, new NullProgressMonitor());
					joined = true;
				} catch (InterruptedException e) {
					// Some other UI threads were trying to run. Let the
					// syncExecs do their jobs and re-try to join on ours.
				}
			}
		}
	}

	private static class GitMergeContext extends SubscriberMergeContext {

		private final RecursiveModelMerger merger;

		/**
		 * Create and initialize a merge context for the given subscriber.
		 *
		 * @param merger
		 *            the merger
		 * @param subscriber
		 *            the subscriber.
		 * @param scopeManager
		 *            the scope manager.
		 */
		public GitMergeContext(RecursiveModelMerger merger, Subscriber subscriber,
				ISynchronizationScopeManager scopeManager) {
			super(subscriber, scopeManager);
			this.merger = merger;
			initialize();
		}

		public void markAsMerged(IDiff node, boolean inSyncHint, IProgressMonitor monitor)
				throws CoreException {
			final IResource resource = getDiffTree().getResource(node);
			merger.addSyncPath(resource);
		}

		public void reject(IDiff diff, IProgressMonitor monitor) throws CoreException {
			// Empty implementation
		}

		@Override
		protected void makeInSync(IDiff diff, IProgressMonitor monitor) throws CoreException {
			final IResource resource = getDiffTree().getResource(diff);
			merger.addSyncPath(resource);
		}
	}

	private void addSyncPath(IResource resource) {
		String repoRelativePath = getRepoRelativePath(resource);
		registerMergedPath(repoRelativePath);
	}

	private boolean registerMergedPath(String path) {
		if (path != null && !unmergedPaths.contains(path)) {
			return makeInSync.add(path);
		}
		return false;
	}
}
// CHECKSTYLE:ON
