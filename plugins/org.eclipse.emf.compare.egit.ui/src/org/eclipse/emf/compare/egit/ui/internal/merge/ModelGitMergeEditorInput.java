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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.RepositoryUtil;
import org.eclipse.egit.core.internal.CompareCoreUtils;
import org.eclipse.egit.core.internal.efs.EgitFileSystem;
import org.eclipse.egit.core.internal.efs.HiddenResources;
import org.eclipse.egit.core.internal.indexdiff.IndexDiffCache;
import org.eclipse.egit.core.internal.indexdiff.IndexDiffCacheEntry;
import org.eclipse.egit.core.internal.indexdiff.IndexDiffData;
import org.eclipse.egit.core.internal.job.RuleUtil;
import org.eclipse.egit.core.internal.storage.GitFileRevision;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.egit.core.util.RevCommitUtils;
import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.internal.CompareUtils;
import org.eclipse.egit.ui.internal.UIText;
import org.eclipse.egit.ui.internal.merge.MergeInputMode;
import org.eclipse.egit.ui.internal.revision.EditableRevision;
import org.eclipse.egit.ui.internal.revision.FileRevisionTypedElement;
import org.eclipse.egit.ui.internal.revision.GitCompareFileRevisionEditorInput.EmptyTypedElement;
import org.eclipse.egit.ui.internal.revision.ResourceEditableRevision;
import org.eclipse.egit.ui.internal.synchronize.compare.LocalNonWorkspaceTypedElement;
import org.eclipse.emf.compare.egit.internal.merge.DirCacheResourceVariantTreeProvider;
import org.eclipse.emf.compare.egit.internal.merge.GitResourceVariantTreeProvider;
import org.eclipse.emf.compare.egit.internal.merge.GitResourceVariantTreeSubscriber;
import org.eclipse.emf.compare.egit.internal.merge.LogicalModels;
import org.eclipse.emf.compare.egit.ui.internal.EMFCompareEGitUIMessages;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jgit.api.MergeCommand.ConflictStyle;
import org.eclipse.jgit.attributes.Attribute;
import org.eclipse.jgit.attributes.Attributes;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEditor;
import org.eclipse.jgit.dircache.DirCacheEditor.PathEdit;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.dircache.DirCacheIterator;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.util.IO;
import org.eclipse.jgit.util.RawParseUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.core.mapping.ISynchronizationScopeManager;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberMergeContext;
import org.eclipse.team.core.subscribers.SubscriberResourceMappingContext;
import org.eclipse.team.core.subscribers.SubscriberScopeManager;
import org.eclipse.team.internal.ui.synchronize.EditableSharedDocumentAdapter.ISharedDocumentAdapterListener;
import org.eclipse.team.internal.ui.synchronize.LocalResourceTypedElement;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;

/**
 * A Git-specific {@link CompareEditorInput}. This class is a copy from
 * org.eclipse.egit.ui.internal.merge.GitMergeEditorInput.
 * 
 * @author <a href="mailto:mathias.kinzler@sap.com">Mathias Kinzler</a>
 */
@SuppressWarnings("restriction")
public class ModelGitMergeEditorInput extends CompareEditorInput {
	private static final String LABELPATTERN = "{0} - {1}"; //$NON-NLS-1$

	private static final Image FOLDER_IMAGE = PlatformUI.getWorkbench().getSharedImages()
			.getImage(ISharedImages.IMG_OBJ_FOLDER);

	private static final Image PROJECT_IMAGE = PlatformUI.getWorkbench().getSharedImages()
			.getImage(SharedImages.IMG_OBJ_PROJECT);

	private final MergeInputMode mode;

	private final boolean useWorkspace;

	private final boolean useOurs;

	private final IPath[] locations;

	private List<IFile> toDelete;

	/**
	 * @param mode
	 *            defining what to use as input for the logical left side.
	 * @param locations
	 *            as selected by the user
	 */
	public ModelGitMergeEditorInput(MergeInputMode mode, IPath... locations) {
		super(new CompareConfiguration());
		this.useWorkspace = !MergeInputMode.STAGE_2.equals(mode);
		this.useOurs = MergeInputMode.MERGED_OURS.equals(mode);
		this.mode = mode;
		this.locations = locations;
		CompareConfiguration config = getCompareConfiguration();
		config.setLeftEditable(true);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if ((adapter == IFile.class || adapter == IResource.class) && isUIThread()) {
			Object selectedEdition = getSelectedEdition();
			if (selectedEdition instanceof DiffNode) {
				DiffNode diffNode = (DiffNode)selectedEdition;
				ITypedElement element = diffNode.getLeft();
				IResource resource = null;
				if (element instanceof HiddenResourceTypedElement) {
					resource = ((HiddenResourceTypedElement)element).getRealFile();
				}
				if (element instanceof IResourceProvider) {
					resource = ((IResourceProvider)element).getResource();
				}
				if (resource != null && adapter.isInstance(resource)) {
					return resource;
				}
			}
		}
		return super.getAdapter(adapter);
	}

	private static boolean isUIThread() {
		return Display.getCurrent() != null;
	}

	@Override
	protected Object prepareInput(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {
		monitor.beginTask(UIText.GitMergeEditorInput_CheckingResourcesTaskName, IProgressMonitor.UNKNOWN);

		// Make sure all resources belong to the same repository
		final Map<Repository, Collection<String>> pathsByRepository = ResourceUtil
				.splitPathsByRepository(Arrays.asList(locations));
		if (pathsByRepository.size() != 1) {
			throw new InvocationTargetException(
					new IllegalStateException(UIText.RepositoryAction_multiRepoSelection));
		}

		checkCanceled(monitor);

		// The merge drivers have done their job of putting the necessary
		// information in the index
		// Read that info and provide it to the file-specific comparators
		final Repository repository = pathsByRepository.keySet().iterator().next();
		try (RevWalk rw = new RevWalk(repository)) {
			final List<String> filterPaths = new ArrayList<String>(pathsByRepository.get(repository));
			// get the "right" side (MERGE_HEAD for merge, ORIG_HEAD for rebase)
			RevCommit rightCommit;
			try {
				rightCommit = RevCommitUtils.getTheirs(repository, rw);
			} catch (IOException e) {
				throw new InvocationTargetException(e);
			}

			// we need the HEAD, also to determine the common ancestor
			final RevCommit headCommit = getLeftCommit(rw, repository);

			// try to obtain the common ancestor
			RevCommit ancestorCommit = null;
			boolean unknownAncestor = false;
			switch (repository.getRepositoryState()) {
				case CHERRY_PICKING:
				case REBASING_INTERACTIVE:
				case REBASING_MERGE:
					if (rightCommit.getParentCount() == 1) {
						try {
							ancestorCommit = rw.parseCommit(rightCommit.getParent(0));
						} catch (IOException e) {
							unknownAncestor = true;
						}
					} else {
						// Cherry-pick of a merge commit -- git doesn't record the
						// mainline index anywhere, so we don't know which parent
						// was taken.
						unknownAncestor = true;
					}
					if (!MergeInputMode.WORKTREE.equals(mode)) {
						// Do not suppress any changes on the left if the input is
						// the possibly pre-merged working tree version. Conflict
						// markers exist only on the left; they would not be shown
						// as differences, and are then too easy to miss.
						getCompareConfiguration().setChangeIgnored(
								getCompareConfiguration().isMirrored() ? RangeDifference.RIGHT
										: RangeDifference.LEFT,
								true);
						getCompareConfiguration().setChangeIgnored(RangeDifference.ANCESTOR, true);
					}
					break;
				default:
					List<RevCommit> startPoints = new ArrayList<>();
					rw.setRevFilter(RevFilter.MERGE_BASE);
					startPoints.add(rightCommit);
					startPoints.add(headCommit);
					try {
						rw.markStart(startPoints);
						ancestorCommit = rw.next();
					} catch (Exception e) {
						// Ignore; ancestor remains null
					}
					break;
			}

			checkCanceled(monitor);

			// set the labels
			setLabels(repository, rightCommit, headCommit, ancestorCommit, unknownAncestor);

			final ICompareInput input = prepareCompareInput(repository, filterPaths, monitor);
			if (input != null) {
				return input;
			}

			checkCanceled(monitor);
			return buildDiffContainer(repository, headCommit, ancestorCommit, filterPaths, rw, monitor);
		} catch (IOException e) {
			// potential resource leak : repository is not our responsibility to close.
			throw new InvocationTargetException(e);
		} finally {
			monitor.done();
		}
	}

	private void checkCanceled(IProgressMonitor monitor) throws InterruptedException {
		if (monitor.isCanceled()) {
			throw new InterruptedException();
		}
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
	private ICompareInput prepareCompareInput(Repository repository, List<String> filterPaths,
			IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			final GitResourceVariantTreeProvider variantTreeProvider = new DirCacheResourceVariantTreeProvider(
					repository, useWorkspace);
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
			for (IPath path : locations) {
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

	private Set<IResource> getConflictingFilesFrom(IContainer container) throws IOException {
		final Set<IResource> conflictingResources = new LinkedHashSet<IResource>();
		final RepositoryMapping mapping = RepositoryMapping.getMapping(container);
		if (mapping == null) {
			return conflictingResources;
		}
		final IndexDiffCacheEntry indexDiffCacheEntry = IndexDiffCache.getInstance()
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

	private RevCommit getLeftCommit(RevWalk revWalk, Repository repository) throws InvocationTargetException {
		try {
			ObjectId head = repository.resolve(Constants.HEAD);
			if (head == null) {
				throw new IOException(NLS.bind(
						EMFCompareEGitUIMessages.getString("ValidationUtils_CanNotResolveRefMessage"), //$NON-NLS-1$
						Constants.HEAD));
			}
			return revWalk.parseCommit(head);
		} catch (IOException e) {
			throw new InvocationTargetException(e);
		}
	}

	private void setLabels(Repository repository, RevCommit rightCommit, RevCommit leftCommit,
			RevCommit ancestorCommit, boolean unknownAncestor) throws InvocationTargetException {
		CompareConfiguration config = getCompareConfiguration();
		config.setRightLabel(NLS.bind(LABELPATTERN, rightCommit.getShortMessage(),
				CompareUtils.truncatedRevision(rightCommit.name())));

		if (!useWorkspace) {
			config.setLeftLabel(NLS.bind(LABELPATTERN, leftCommit.getShortMessage(),
					CompareUtils.truncatedRevision(leftCommit.name())));
		} else if (useOurs) {
			config.setLeftLabel(UIText.GitMergeEditorInput_WorkspaceOursHeader);
		} else {
			config.setLeftLabel(UIText.GitMergeEditorInput_WorkspaceHeader);
		}

		if (ancestorCommit != null) {
			config.setAncestorLabel(NLS.bind(LABELPATTERN, ancestorCommit.getShortMessage(),
					CompareUtils.truncatedRevision(ancestorCommit.name())));
		} else if (unknownAncestor) {
			config.setAncestorLabel(NLS.bind(UIText.GitMergeEditorInput_AncestorUnknownHeader,
					CompareUtils.truncatedRevision(rightCommit.name())));
		}

		// set title and icon
		final String fullBranch;
		try {
			fullBranch = repository.getFullBranch();
		} catch (IOException e) {
			throw new InvocationTargetException(e);
		}
		setTitle(NLS.bind(UIText.GitMergeEditorInput_MergeEditorTitle,
				new Object[] {RepositoryUtil.getInstance().getRepositoryName(repository),
						rightCommit.getShortMessage(), fullBranch }));
	}

	@Override
	protected void contentsCreated() {
		super.contentsCreated();
		// select the first conflict
		getNavigator().selectChange(true);
	}

	@Override
	protected void handleDispose() {
		super.handleDispose();
		// we do NOT dispose the images, as these are shared
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				cleanUp();
			}
		});
	}

	private void cleanUp() {
		if (toDelete == null || toDelete.isEmpty()) {
			return;
		}
		final List<IFile> toClean = toDelete;
		toDelete = null;
		// Don't clean up if the workbench is shutting down; we would exit with
		// unsaved workspace changes. Instead, EGit core cleans the project on
		// start.
		Job job = new Job(UIText.GitMergeEditorInput_ResourceCleanupJobName) {

			@Override
			public boolean shouldSchedule() {
				return super.shouldSchedule() && !PlatformUI.getWorkbench().isClosing();
			}

			@Override
			public boolean shouldRun() {
				return super.shouldRun() && !PlatformUI.getWorkbench().isClosing();
			}

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
					public void run(IProgressMonitor m) throws CoreException {
						SubMonitor progress = SubMonitor.convert(m, toClean.size());
						for (IFile tmp : toClean) {
							if (PlatformUI.getWorkbench().isClosing()) {
								return;
							}
							try {
								tmp.delete(true, progress.newChild(1));
							} catch (CoreException e) {
								// Ignore
							}
						}
					}
				};
				try {
					ResourcesPlugin.getWorkspace().run(runnable, null, IWorkspace.AVOID_UPDATE, monitor);
				} catch (CoreException e) {
					return e.getStatus();
				}
				return Status.OK_STATUS;
			}
		};
		job.setSystem(true);
		job.setUser(false);
		job.schedule();
	}

	private IDiffContainer buildDiffContainer(final Repository repository, RevCommit headCommit,
			RevCommit ancestorCommit, List<String> filterPaths, RevWalk rw, IProgressMonitor monitor)
			throws IOException, InterruptedException {

		monitor.setTaskName(UIText.GitMergeEditorInput_CalculatingDiffTaskName);
		IDiffContainer result = new DiffNode(Differencer.CONFLICTING);

		ConflictStyle style = null;
		try (TreeWalk tw = new TreeWalk(repository)) {
			int dirCacheIndex = tw.addTree(new DirCacheIterator(repository.readDirCache()));
			FileTreeIterator fIter = new FileTreeIterator(repository);
			int fileTreeIndex = tw.addTree(fIter);
			fIter.setDirCacheIterator(tw, dirCacheIndex);
			int repositoryTreeIndex = tw.addTree(rw.parseTree(repository.resolve(Constants.HEAD)));

			// filter by selected resources
			if (!filterPaths.isEmpty()) {
				if (filterPaths.size() > 1) {
					tw.setFilter(PathFilterGroup.createFromStrings(filterPaths));
				} else {
					String path = filterPaths.get(0);
					if (!path.isEmpty()) {
						tw.setFilter(PathFilterGroup.createFromStrings(path));
					}
				}
			}
			tw.setRecursive(true);

			while (tw.next()) {
				if (monitor.isCanceled()) {
					throw new InterruptedException();
				}
				final String gitPath = tw.getPathString();
				monitor.setTaskName(gitPath);

				FileTreeIterator fit = tw.getTree(fileTreeIndex, FileTreeIterator.class);
				if (fit == null) {
					continue;
				}

				DirCacheIterator dit = tw.getTree(dirCacheIndex, DirCacheIterator.class);

				final DirCacheEntry dirCacheEntry = dit == null ? null : dit.getDirCacheEntry();

				boolean conflicting = dirCacheEntry != null && dirCacheEntry.getStage() > 0;

				AbstractTreeIterator rt = tw.getTree(repositoryTreeIndex, AbstractTreeIterator.class);

				// compare local file against HEAD to see if it was modified
				boolean modified = rt != null && !fit.getEntryObjectId().equals(rt.getEntryObjectId());

				// if this is neither conflicting nor changed, we skip it
				if (!conflicting && !modified) {
					continue;
				}

				ITypedElement right;
				String encoding = null;
				if (conflicting) {
					GitFileRevision revision = GitFileRevision.inIndex(repository, gitPath,
							DirCacheEntry.STAGE_3);
					encoding = CompareCoreUtils.getResourceEncoding(repository, gitPath);
					right = new FileRevisionTypedElement(revision, encoding);
				} else {
					right = CompareUtils.getFileRevisionTypedElement(gitPath, headCommit, repository);
				}

				// can this really happen?
				if (right instanceof EmptyTypedElement) {
					continue;
				}

				ITypedElement left;
				IFileRevision rev;
				// if the file is not conflicting (as it was auto-merged)
				// we will show the auto-merged (local) version

				Path repositoryPath = new Path(repository.getWorkTree().getAbsolutePath());
				IPath location = repositoryPath.append(gitPath);
				assert location != null;

				final IFile file = ResourceUtil.getFileForLocation(location, false);
				boolean useWorkingTree = !conflicting || useWorkspace;
				if (!useWorkingTree && conflicting && dirCacheEntry != null) {
					// Normal conflict stages have a zero timestamp. If it's not
					// zero, we marked it below when the content was saved to
					// the working tree file in an earlier merge editor.
					useWorkingTree = !Instant.EPOCH.equals(dirCacheEntry.getLastModifiedInstant());
				}
				if (useWorkingTree) {
					boolean useOursFilter = conflicting && useOurs;
					int conflictMarkerSize = 7; // Git default
					if (useOursFilter) {
						Attributes attributes = tw.getAttributes();
						useOursFilter = attributes.canBeContentMerged();
						if (useOursFilter) {
							Attribute markerSize = attributes.get("conflict-marker-size"); //$NON-NLS-1$
							if (markerSize != null && Attribute.State.CUSTOM.equals(markerSize.getState())) {
								try {
									conflictMarkerSize = Integer.parseUnsignedInt(markerSize.getValue());
								} catch (NumberFormatException e) {
									// Ignore
								}
							}
						}
					}
					LocalResourceTypedElement item;
					if (useOursFilter) {
						if (style == null) {
							style = repository.getConfig().getEnum(ConfigConstants.CONFIG_MERGE_SECTION, null,
									ConfigConstants.CONFIG_KEY_CONFLICTSTYLE, ConflictStyle.MERGE);
						}
						boolean useDiff3Style = ConflictStyle.DIFF3.equals(style);
						String filter = (useDiff3Style ? 'O' : 'o') + Integer.toString(conflictMarkerSize);
						URI uri = EgitFileSystem.createURI(repository, gitPath, "WORKTREE:" + filter); //$NON-NLS-1$
						Charset rscEncoding = null;
						if (file != null) {
							if (encoding == null) {
								encoding = CompareCoreUtils.getResourceEncoding(file);
							}
							try {
								rscEncoding = Charset.forName(encoding);
							} catch (IllegalArgumentException e) {
								// Ignore here; use default.
							}
						}
						item = createWithHiddenResource(uri, tw.getNameString(), file, rscEncoding);
						if (file != null) {
							item.setSharedDocumentListener(new LocalResourceSaver(item) {

								@Override
								protected void save() throws CoreException {
									super.save();
									file.refreshLocal(IResource.DEPTH_ZERO, null);
								}
							});
						} else {
							item.setSharedDocumentListener(new LocalResourceSaver(item));
						}
					} else {
						if (file != null) {
							item = new LocalResourceTypedElement(file);
						} else {
							item = new LocalNonWorkspaceTypedElement(repository, location);
						}
						item.setSharedDocumentListener(new LocalResourceSaver(item));
					}
					left = item;
				} else {
					IFile rsc = file != null ? file
							: createHiddenResource(location.toFile().toURI(), tw.getNameString(), null);
					assert rsc != null;
					// Stage 2 from index with backing IResource
					rev = GitFileRevision.inIndex(repository, gitPath, DirCacheEntry.STAGE_2);
					IRunnableContext runnableContext = getContainer();
					if (runnableContext == null) {
						runnableContext = PlatformUI.getWorkbench().getProgressService();
						assert runnableContext != null;
					}
					left = new ResourceEditableRevision(rev, rsc, runnableContext);
					// 'left' saves to the working tree. Update the index entry
					// with the current time. Normal conflict stages have a
					// timestamp of zero, so this is a non-invasive fully
					// compatible way to mark this conflict stage so that the
					// next time we do take the file contents.
					((EditableRevision)left).addContentChangeListener(new IContentChangeListener() {
						public void contentChanged(IContentChangeNotifier source) {
							updateIndexTimestamp(repository, gitPath);
						}
					});
					// make sure we don't need a round trip later
					try {
						((EditableRevision)left).cacheContents(monitor);
					} catch (CoreException e) {
						throw new IOException(e.getMessage(), e);
					}
				}

				int kind = Differencer.NO_CHANGE;
				if (conflicting) {
					kind = Differencer.CONFLICTING;
				} else if (modified) {
					kind = Differencer.PSEUDO_CONFLICT;
				}

				IDiffContainer fileParent = getFileParent(result, repositoryPath, file, location);

				ITypedElement ancestor = null;
				if (ancestorCommit != null) {
					ancestor = CompareUtils.getFileRevisionTypedElement(gitPath, ancestorCommit, repository);
				}
				// we get an ugly black icon if we have an EmptyTypedElement
				// instead of null
				if (ancestor instanceof EmptyTypedElement) {
					ancestor = null;
				}
				// create the node as child
				new DiffNode(fileParent, kind, ancestor, left, right);
			}
			return result;
		} catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	private LocalResourceTypedElement createWithHiddenResource(URI uri, String name, IFile file,
			Charset encoding) throws IOException {
		IFile tmp = createHiddenResource(uri, name, encoding);
		return new HiddenResourceTypedElement(tmp, file);
	}

	private IFile createHiddenResource(URI uri, String name, Charset encoding) throws IOException {
		try {
			IFile tmp = HiddenResources.INSTANCE.createFile(uri, name, encoding, null);
			if (toDelete == null) {
				toDelete = new ArrayList<>();
			}
			toDelete.add(tmp);
			return tmp;
		} catch (CoreException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	private void updateIndexTimestamp(Repository repository, String gitPath) {
		DirCache cache = null;
		try {
			cache = repository.lockDirCache();
			DirCacheEditor editor = cache.editor();
			editor.add(new PathEdit(gitPath) {

				private boolean done;

				@Override
				public void apply(DirCacheEntry ent) {
					if (!done && ent.getStage() > 0) {
						ent.setLastModified(Instant.now());
						done = true;
					}
				}
			});
			editor.commit();
		} catch (IOException e) {
			Activator.logError(MessageFormat.format(UIText.GitMergeEditorInput_ErrorUpdatingIndex, gitPath),
					e);
		} finally {
			if (cache != null) {
				cache.unlock();
			}
		}
	}

	private IDiffContainer getFileParent(IDiffContainer root, IPath repositoryPath, IFile file,
			IPath location) {
		int projectSegment = -1;
		String projectName = null;
		if (file != null) {
			IProject project = file.getProject();
			IPath projectLocation = project.getLocation();
			if (projectLocation != null) {
				IPath projectPath = project.getLocation().makeRelativeTo(repositoryPath);
				projectSegment = projectPath.segmentCount() - 1;
				projectName = project.getName();
			}
		}

		IPath path = location.makeRelativeTo(repositoryPath);
		IDiffContainer child = root;
		for (int i = 0; i < path.segmentCount() - 1; i++) {
			if (i == projectSegment) {
				child = getOrCreateChild(child, projectName, true);
			} else {
				child = getOrCreateChild(child, path.segment(i), false);
			}
		}
		return child;
	}

	private DiffNode getOrCreateChild(IDiffContainer parent, final String name, final boolean projectMode) {
		for (IDiffElement child : parent.getChildren()) {
			if (child.getName().equals(name)) {
				return ((DiffNode)child);
			}
		}
		DiffNode child = new DiffNode(parent, Differencer.NO_CHANGE) {

			@Override
			public String getName() {
				return name;
			}

			@Override
			public Image getImage() {
				if (projectMode) {
					return PROJECT_IMAGE;
				} else {
					return FOLDER_IMAGE;
				}
			}
		};
		return child;
	}

	private String readFile(File directory, String fileName) throws IOException {
		byte[] content = IO.readFully(new File(directory, fileName));
		// strip off the last LF
		int end = content.length;
		while (0 < end && content[end - 1] == '\n') {
			end--;
		}
		return RawParseUtils.decode(content, 0, end);
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

	private static class LocalResourceSaver implements ISharedDocumentAdapterListener {

		LocalResourceTypedElement element;

		public LocalResourceSaver(LocalResourceTypedElement element) {
			this.element = element;
		}

		protected void save() throws CoreException {
			element.saveDocument(true, null);
			refreshIndexDiff();
		}

		private void refreshIndexDiff() {
			IResource resource = element.getResource();
			if (resource != null && HiddenResources.INSTANCE.isHiddenProject(resource.getProject())) {
				String gitPath = null;
				Repository repository = null;
				URI uri = resource.getLocationURI();
				if (EFS.SCHEME_FILE.equals(uri.getScheme())) {
					IPath location = new Path(uri.getSchemeSpecificPart());
					repository = ResourceUtil.getRepository(location);
					if (repository != null) {
						location = ResourceUtil.getRepositoryRelativePath(location, repository);
						if (location != null) {
							gitPath = location.toPortableString();
						}
					}
				} else {
					repository = HiddenResources.INSTANCE.getRepository(uri);
					if (repository != null) {
						gitPath = HiddenResources.INSTANCE.getGitPath(uri);
					}
				}
				if (gitPath != null && repository != null) {
					IndexDiffCacheEntry indexDiffCacheForRepository = IndexDiffCache.getInstance()
							.getIndexDiffCacheEntry(repository);
					if (indexDiffCacheForRepository != null) {
						indexDiffCacheForRepository.refreshFiles(Collections.singletonList(gitPath));
					}
				}
			}
		}

		@Override
		public void handleDocumentConnected() {
			// Nothing
		}

		@Override
		public void handleDocumentDisconnected() {
			// Nothing
		}

		@Override
		public void handleDocumentFlushed() {
			try {
				save();
			} catch (CoreException e) {
				Activator.handleStatus(e.getStatus(), true);
			}
		}

		@Override
		public void handleDocumentDeleted() {
			// Nothing
		}

		@Override
		public void handleDocumentSaved() {
			// Nothing
		}
	}

	private static class HiddenResourceTypedElement extends LocalResourceTypedElement {

		private final IFile realFile;

		public HiddenResourceTypedElement(IFile file, IFile realFile) {
			super(file);
			this.realFile = realFile;
		}

		public IFile getRealFile() {
			return realFile;
		}

		@Override
		public boolean equals(Object obj) {
			// realFile not considered
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			// realFile not considered
			return super.hashCode();
		}
	}
}
// CHECKSTYLE:ON
