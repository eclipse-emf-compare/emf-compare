/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - log messages (bug 461713), bug 465331
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.internal.merge.MergeDependenciesUtil;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.internal.utils.PruningIterator;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.mapping.IMergeContext;
import org.eclipse.team.core.mapping.IResourceMappingMerger;
import org.eclipse.team.core.mapping.provider.MergeStatus;

/**
 * A customized merger for the {@link EMFResourceMapping}s. This will use EMF Compare to recompute the logical
 * model of the mappings it needs to merge, then merge everything to the left model if there are no conflicts,
 * stopping dead if there is any conflict.
 * <p>
 * Mapping mergers are usually retrieved through an adapter registered on the ModelProvider. In this case,
 * {@code org.eclipse.core.runtime.Platform.getAdapterManager().getAdapter(emfModelProvider, IResourceMappingMerger.class)}
 * .
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see EMFLogicalModelAdapterFactory
 */
/*
 * Illegally implements IResourceMappingMerger, but we need none of the behavior from the abstract
 * ResourceMappingMerger. Filtered in the API filters.
 */
public class EMFResourceMappingMerger implements IResourceMappingMerger {
	/** {@inheritDoc} */
	public IStatus merge(IMergeContext mergeContext, IProgressMonitor monitor) throws CoreException {
		final ResourceMapping[] emfMappings = getEMFMappings(mergeContext);
		log(IStatus.OK, "EMFResourceMappingMerger.startingModelMerge", emfMappings); //$NON-NLS-1$
		if (emfMappings.length <= 0) {
			return new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
					.getString("EMFResourceMappingMerger.mergeFailedGeneric")); //$NON-NLS-1$
		}
		final IStatus hasInvalidMappings = validateMappings(emfMappings);
		if (hasInvalidMappings.getSeverity() != IStatus.OK) {
			return hasInvalidMappings;
		}

		// Use a sub-monitor with 10 ticks per child
		// For the time being, Cancel is not supported here because reverting changes is problematic
		SubMonitor subMonitor = SubMonitor.convert(monitor, emfMappings.length);
		try {
			final Set<ResourceMapping> failingMappings = new HashSet<ResourceMapping>();
			for (ResourceMapping mapping : emfMappings) {
				mergeMapping(mapping, mergeContext, failingMappings, subMonitor.newChild(1));
			}

			if (!failingMappings.isEmpty()) {
				final ResourceMapping[] failingArray = failingMappings
						.toArray(new ResourceMapping[failingMappings.size()]);
				return new MergeStatus(EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
						.getString("EMFResourceMappingMerger.mergeFailedConflicts"), failingArray); //$NON-NLS-1$
			}
			log(IStatus.OK, "EMFResourceMappingMerger.successfulModelMerge", emfMappings); //$NON-NLS-1$
			return Status.OK_STATUS;
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	/**
	 * Logs the EMFCompareIDEUIPlugin message for the given {@code key} with the given severity
	 * {@code statusCode} and {@code emfMappings}.
	 * 
	 * @param statusCode
	 *            Status code must be one of {@link IStatus}.
	 * @param key
	 *            Message key for EMFCompareIDEUIPlugin.
	 * @param emfMappings
	 *            The resource mappings to log.
	 */
	private void log(int statusCode, String key, ResourceMapping[] emfMappings) {
		final List<IResource> iResources = getInvolvedIResources(emfMappings);
		final String message = EMFCompareIDEUIMessages.getString(key, String.valueOf(iResources.size()));
		log(statusCode, message, iResources);
	}

	/**
	 * Logs the given {@code message} and the given {@code iResources} with the given severity
	 * {@code statusCode}.
	 * <p>
	 * The logged status is a {@link MultiStatus}, having the given {@code message} as a parent status and the
	 * names of the provided {@code iResources} as child statuses.
	 * </p>
	 * 
	 * @param statusCode
	 *            Status code must be one of {@link IStatus}.
	 * @param message
	 *            The message to be logged.
	 * @param iResources
	 *            The resources to be added as child status.
	 */
	private void log(int statusCode, String message, Collection<IResource> iResources) {
		final MultiStatus multiStatus = new MultiStatus(EMFCompareIDEUIPlugin.PLUGIN_ID, 0, message, null);
		for (IResource iResource : iResources) {
			final Status childStatus = new Status(statusCode, EMFCompareIDEUIPlugin.PLUGIN_ID, iResource
					.getFullPath().toOSString());
			multiStatus.add(childStatus);
		}
		log(multiStatus);
	}

	/**
	 * Returns the {@link IResource resources} involved in the given {@code emfMappings}.
	 * 
	 * @param emfMappings
	 *            The resource mappings to get the involved resources from.
	 * @return The resources involved in {@code emfMappings}.
	 */
	private List<IResource> getInvolvedIResources(ResourceMapping[] emfMappings) {
		final List<IResource> iResources = new ArrayList<IResource>();
		for (ResourceMapping mapping : emfMappings) {
			if (mapping instanceof EMFResourceMapping) {
				final SynchronizationModel syncModel = ((EMFResourceMapping)mapping).getLatestModel();
				for (IResource iResource : syncModel.getResources()) {
					iResources.add(iResource);
				}
			}
		}
		return iResources;
	}

	/**
	 * Logs the given {@code status} to the log of {@link EMFCompareIDEUIPlugin}.
	 * 
	 * @param status
	 *            The {@link IStatus} to log.
	 */
	private void log(IStatus status) {
		EMFCompareIDEUIPlugin.getDefault().getLog().log(status);
	}

	/**
	 * Merges one mapping.
	 * 
	 * @param mapping
	 *            The mapping to merge
	 * @param mergeContext
	 *            The merge context
	 * @param failingMappings
	 *            The set of failing mappings
	 * @param subMonitor
	 *            The progress monitor to use, 10 ticks will be consumed
	 */
	private void mergeMapping(ResourceMapping mapping, final IMergeContext mergeContext,
			final Set<ResourceMapping> failingMappings, IProgressMonitor monitor) throws CoreException {
		final SubMonitor subMonitor = SubMonitor.convert(monitor, 10);
		// validateMappings() has made sure we only have EMFResourceMappings
		final SynchronizationModel syncModel = ((EMFResourceMapping)mapping).getLatestModel();

		final IModelMinimizer minimizer = new IdenticalResourceMinimizer();
		minimizer.minimize(syncModel, subMonitor.newChild(1)); // 10%
		final IComparisonScope scope = ComparisonScopeBuilder.create(syncModel, subMonitor.newChild(3)); // 40%

		final Builder builder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(builder);

		final Comparison comparison = builder.build().compare(scope,
				BasicMonitor.toMonitor(SubMonitor.convert(subMonitor.newChild(1), 10))); // 50%
		final IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		if (hasRealConflict(comparison)) {
			// pre-merge what can be
			final Graph<Diff> differencesGraph = MergeDependenciesUtil.mapDifferences(comparison,
					mergerRegistry, true);
			final PruningIterator<Diff> iterator = differencesGraph.breadthFirstIterator();
			final Monitor emfMonitor = BasicMonitor.toMonitor(subMonitor.newChild(5)); // 100%

			while (iterator.hasNext()) {
				final Diff next = iterator.next();
				if (hasConflict(ConflictKind.REAL).apply(next)) {
					iterator.prune();
				} else {
					if (next.getState() != DifferenceState.MERGED) {
						final IMerger merger = mergerRegistry.getHighestRankingMerger(next);
						merger.copyRightToLeft(next, emfMonitor);
					}
				}
			}

			save(scope.getLeft());

			failingMappings.add(mapping);
		} else {
			MarkNewResourceAsMergedListener listener = new MarkNewResourceAsMergedListener(mergeContext);
			TrackAddedAndRemovedResourcesListener resourceTracker = new TrackAddedAndRemovedResourcesListener();
			try {
				scope.getLeft().eAdapters().add(listener);
				scope.getLeft().eAdapters().add(resourceTracker);
				final IBatchMerger merger = new BatchMerger(mergerRegistry, fromSide(DifferenceSource.RIGHT));
				merger.copyAllRightToLeft(comparison.getDifferences(), BasicMonitor.toMonitor(subMonitor
						.newChild(3))); // 50%
				save(scope.getLeft());

				for (IStorage storage : syncModel.getLeftTraversal().getStorages()) {
					final IPath fullPath = ResourceUtil.getFixedPath(storage);
					if (fullPath == null) {
						EMFCompareIDEUIPlugin.getDefault().getLog().log(
								new Status(IStatus.WARNING, EMFCompareIDEUIPlugin.PLUGIN_ID,
										EMFCompareIDEUIMessages
												.getString("EMFResourceMappingMerger.mergeIncomplete"))); //$NON-NLS-1$
					} else {
						final IDiff diff = mergeContext.getDiffTree().getDiff(fullPath);
						if (diff != null) {
							if (IDiff.REMOVE == diff.getKind()
									&& !resourceTracker.containsRemovedResource(fullPath)) {
								mergeContext.merge(diff, false, subMonitor.newChild(1));
							} else {
								mergeContext.markAsMerged(diff, true, subMonitor.newChild(1));
							}
						}
					}
				}

				for (IStorage rightStorage : syncModel.getRightTraversal().getStorages()) {
					final IPath fullPath = ResourceUtil.getFixedPath(rightStorage);
					if (fullPath != null) {
						final IDiff diff = mergeContext.getDiffTree().getDiff(fullPath);
						if (diff != null && IDiff.ADD == diff.getKind()
								&& !resourceTracker.containsAddedResource(fullPath)) {
							mergeContext.merge(diff, false, subMonitor.newChild(1));
						}
					}
				}

			} finally {
				scope.getLeft().eAdapters().remove(listener);
				scope.getLeft().eAdapters().remove(resourceTracker);
			}
		}
		subMonitor.setWorkRemaining(0);
	}

	/**
	 * Validates the given array of mappings. Basically, this merger can only operate if all of its target
	 * mappings are instances of EMFResourceMappings that were properly initialized.
	 * 
	 * @param mappings
	 *            The mappings we are to validate.
	 * @return {@link Status#OK_STATUS} if the given mappings are valid, a status describing the failure
	 *         otherwise.
	 */
	private IStatus validateMappings(ResourceMapping[] mappings) {
		for (ResourceMapping mapping : mappings) {
			if (mapping instanceof EMFResourceMapping
					&& mapping.getModelObject() instanceof SynchronizationModel) {
				final SynchronizationModel model = (SynchronizationModel)mapping.getModelObject();
				if (model.getDiagnostic().getSeverity() >= Diagnostic.WARNING) {
					return BasicDiagnostic.toIStatus(model.getDiagnostic());
				}
			} else {
				return new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
						.getString("EMFResourceMappingMerger.mergeFailedInvalidMapping")); //$NON-NLS-1$
			}
		}
		return Status.OK_STATUS;
	}

	/**
	 * Saves the given notifier to disk after a successful merge.
	 * 
	 * @param notifier
	 *            The notifier.
	 */
	private void save(Notifier notifier) {
		if (notifier instanceof ResourceSet) {
			ResourceUtil
					.saveAllResources((ResourceSet)notifier, ImmutableMap.of(
							Resource.OPTION_SAVE_ONLY_IF_CHANGED,
							Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
		} else if (notifier instanceof Resource) {
			ResourceUtil
					.saveResource((Resource)notifier, ImmutableMap.of(Resource.OPTION_SAVE_ONLY_IF_CHANGED,
							Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
		}
	}

	/**
	 * Checks whether this comparison presents a real conflict.
	 * 
	 * @param comparison
	 *            The comparison to check for conflicts.
	 * @return <code>true</code> if there's at least one {@link ConflictKind#REAL real conflict} within this
	 *         comparison.
	 */
	private boolean hasRealConflict(Comparison comparison) {
		for (Conflict conflict : comparison.getConflicts()) {
			if (conflict.getKind() == ConflictKind.REAL) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public ISchedulingRule getMergeRule(IMergeContext context) {
		final ResourceMapping[] emfMappings = getEMFMappings(context);
		final Set<IProject> impactedProjects = new LinkedHashSet<IProject>();
		for (ResourceMapping mapping : emfMappings) {
			impactedProjects.addAll(Arrays.asList(mapping.getProjects()));
		}
		return MultiRule.combine(impactedProjects.toArray(new ISchedulingRule[impactedProjects.size()]));
	}

	/**
	 * Retrieves all mappings from the given merge context that were created from the EMFModelProvider.
	 * 
	 * @param context
	 *            The context from which to retrieve resource mappings.
	 * @return All resource mappings from this context that were created from the EMFModelProvider.
	 */
	private ResourceMapping[] getEMFMappings(IMergeContext context) {
		return context.getScope().getMappings(EMFModelProvider.PROVIDER_ID);
	}

	/** {@inheritDoc} */
	public IStatus validateMerge(IMergeContext mergeContext, IProgressMonitor monitor) {
		return Status.OK_STATUS;
	}

	private static class MarkNewResourceAsMergedListener extends AdapterImpl {
		private final IMergeContext mergeContext;

		public MarkNewResourceAsMergedListener(IMergeContext mergeContext) {
			this.mergeContext = mergeContext;
		}

		@Override
		public void notifyChanged(Notification msg) {
			if (msg.getEventType() == Notification.ADD && msg.getNewValue() instanceof Resource) {
				Resource newResource = (Resource)msg.getNewValue();
				URI uri = newResource.getURI();
				if (uri.isPlatformResource()) {
					String path = uri.toPlatformString(true);
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
					IDiff diff = mergeContext.getDiffTree().getDiff(file);
					try {
						mergeContext.markAsMerged(diff, true, new NullProgressMonitor());
					} catch (CoreException e) {
						EMFCompareIDEUIPlugin.getDefault().log(e);
					}
				}
			}
		}
	}

	private static class TrackAddedAndRemovedResourcesListener extends AdapterImpl {

		private final Set<String> addedResources = new HashSet<String>();

		private final Set<String> removedResources = new HashSet<String>();

		@Override
		public void notifyChanged(Notification msg) {
			int notificationType = msg.getEventType();
			if (notificationType != Notification.ADD && notificationType != Notification.REMOVE) {
				return;
			}
			if (msg.getNewValue() instanceof Resource) {
				final Resource newResource = (Resource)msg.getNewValue();
				final URI uri = newResource.getURI();
				if (uri.isPlatformResource()) {
					final String path = uri.toPlatformString(true);
					final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
					final String stringRepresentation = getStringRepresentation(file.getFullPath());
					if (msg.getEventType() == Notification.ADD) {
						addedResources.add(stringRepresentation);
					} else if (msg.getEventType() == Notification.REMOVE) {
						removedResources.add(stringRepresentation);
					}
				}
			}
		}

		private String getStringRepresentation(IPath path) {
			return path.toPortableString();
		}

		public boolean containsAddedResource(IPath path) {
			return addedResources.contains(getStringRepresentation(path));
		}

		public boolean containsRemovedResource(IPath path) {
			return removedResources.contains(getStringRepresentation(path));
		}

	}
}
