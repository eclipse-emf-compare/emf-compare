/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.compare.ITypedElement;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.mapping.IMergeContext;
import org.eclipse.team.core.mapping.IResourceMappingMerger;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.core.mapping.provider.MergeStatus;
import org.eclipse.team.core.subscribers.Subscriber;
import org.eclipse.team.core.subscribers.SubscriberMergeContext;

/**
 * A customized merger for the EMFResourceMappings. This will use EMF Compare to recompute the logical model
 * of the mappings it needs to merge, then merge everything to the left model if there are no conflicts,
 * stopping dead if there are any conflicts.
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
		if (emfMappings.length <= 0 || !(mergeContext instanceof SubscriberMergeContext)) {
			return new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
					.getString("EMFResourceMappingMerger.mergeFailedGeneric")); //$NON-NLS-1$
		}
		final IStatus hasInvalidMappings = validateMappings(emfMappings);
		if (hasInvalidMappings.getSeverity() != IStatus.OK) {
			return hasInvalidMappings;
		}
		final Subscriber subscriber = ((SubscriberMergeContext)mergeContext).getSubscriber();
		final IStorageProviderAccessor accessor = new SubscriberStorageAccessor(subscriber);

		/*
		 * The diff tree knows about some differences (mergeContext.getDiffTree()), but it will not know about
		 * resources that do not/no longer exist remotely. We need to recompute both the remote and origin
		 * traversals. Furthermore, since there is a chance that there are no diffs on the files known
		 * locally, the diff tree might be empty. We have to use the resource mappings and subscriber in order
		 * to determine the starting points.
		 */
		ITypedElement left = null;
		ITypedElement right = null;
		ITypedElement origin = null;
		// All we need is one element from each side, even if they are not "the same"
		if (mergeContext.getType() == ISynchronizationContext.THREE_WAY) {
			left = findTypedElement(emfMappings, accessor, monitor, DiffSide.SOURCE);
			right = findTypedElement(emfMappings, accessor, monitor, DiffSide.REMOTE);
			origin = findTypedElement(emfMappings, accessor, monitor, DiffSide.ORIGIN);
		} else {
			left = findTypedElement(emfMappings, accessor, monitor, DiffSide.SOURCE);
			right = findTypedElement(emfMappings, accessor, monitor, DiffSide.REMOTE);
		}

		IStorage leftStorage = null;
		if (left instanceof IAdaptable) {
			leftStorage = (IStorage)((IAdaptable)left).getAdapter(IStorage.class);
		}
		// we don't need to double-check for left != null here if leftStorage is not null.
		if (leftStorage == null || right == null) {
			return new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
					.getString("EMFResourceMappingMerger.mergeFailedGeneric")); //$NON-NLS-1$
		}

		final IModelResolver resolver = EMFCompareIDEUIPlugin.getDefault().getModelResolverRegistry()
				.getBestResolverFor(leftStorage);
		final IModelMinimizer minimizer = new IdenticalResourceMinimizer();

		/*
		 * We Cannot use the scope builder "normally" here since we'll need the traversals later on to mark
		 * the files as merged. ComparisonScopeBuilder#buildSynchronizationModel(...) serves this purpose.
		 */
		final ComparisonScopeBuilder builder = new ComparisonScopeBuilder(resolver, minimizer, accessor);
		final SynchronizationModel syncModel = builder
				.buildSynchronizationModel(left, right, origin, monitor);
		final IComparisonScope scope = builder.build(syncModel, monitor);

		final Comparison comparison = EMFCompare.builder().build().compare(scope,
				BasicMonitor.toMonitor(monitor));
		if (hasRealConflict(comparison)) {
			return new MergeStatus(EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
					.getString("EMFResourceMappingMerger.mergeFailedConflicts"), emfMappings); //$NON-NLS-1$
		}

		final IMerger.Registry mergerRegistry = EMFCompareRCPPlugin.getDefault().getMergerRegistry();
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		final Predicate<Diff> filter = new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input != null && input.getConflict() == null
						&& input.getSource() != DifferenceSource.LEFT;
			}
		};
		merger.copyAllRightToLeft(Iterables.filter(comparison.getDifferences(), filter), BasicMonitor
				.toMonitor(monitor));
		save(scope.getLeft());
		for (IStorage storage : syncModel.getLeftTraversal().getStorages()) {
			if (storage.getFullPath() == null) {
				EMFCompareIDEUIPlugin.getDefault().getLog().log(
						new Status(IStatus.WARNING, EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
								.getString("EMFResourceMappingMerger.mergeIncomplete"))); //$NON-NLS-1$
			} else {
				final IDiff diff = mergeContext.getDiffTree().getDiff(storage.getFullPath());
				if (diff != null) {
					mergeContext.markAsMerged(diff, true, monitor);
				}
			}
		}

		return Status.OK_STATUS;
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
			if (mapping instanceof EMFResourceMapping && mapping.getModelObject() instanceof StorageTraversal) {
				final StorageTraversal traversal = (StorageTraversal)mapping.getModelObject();
				if (traversal.getDiagnostic().getSeverity() >= Diagnostic.WARNING) {
					return BasicDiagnostic.toIStatus(traversal.getDiagnostic());
				}
			} else {
				return new Status(IStatus.ERROR, EMFCompareIDEUIPlugin.PLUGIN_ID, EMFCompareIDEUIMessages
						.getString("EMFResourceMappingMerger.mergeFailedInvalidMapping")); //$NON-NLS-1$
			}
		}
		return Status.OK_STATUS;
	}

	/**
	 * Looks up the given resource mappings in order to create a typed element for the given side of the
	 * comparison.
	 * 
	 * @param emfMappings
	 *            The EMF mappings (must be instances of EMFResourceMapping) to loop through.
	 * @param storageAccessor
	 *            The storage provider accessor in which to retrieve remote information.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 * @param side
	 *            Side of the comparison for which we seek a typed element.
	 * @return The typed element for the given side if we could find one in the given mappings,
	 *         <code>null</code> otherwise.
	 * @throws CoreException
	 *             if we cannot retrieve the content of a resource for some reason.
	 * @throws ClassCastException
	 *             if the given mappings contain something that is not an instance of
	 *             {@link EMFResourceMapping}.
	 */
	private ITypedElement findTypedElement(ResourceMapping[] emfMappings,
			IStorageProviderAccessor storageAccessor, IProgressMonitor monitor, DiffSide side)
			throws CoreException, ClassCastException {
		for (int i = 0; i < emfMappings.length; i++) {
			final EMFResourceMapping mapping = (EMFResourceMapping)emfMappings[i];
			final StorageTraversal traversal = (StorageTraversal)mapping.getModelObject();
			for (IStorage storage : traversal.getStorages()) {
				if (storage instanceof IFile) {
					final IStorageProvider storageProvider = storageAccessor.getStorageProvider(
							(IFile)storage, side);
					if (storageProvider != null) {
						return new StorageTypedElement(storageProvider.getStorage(monitor), storage
								.getFullPath().toString());
					}
				}
			}
		}
		return null;
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

	private boolean hasRealConflict(Comparison comparison) {
		for (Conflict conflict : comparison.getConflicts()) {
			if (conflict.getKind() != ConflictKind.PSEUDO) {
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
}
