/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceSource.LEFT;
import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.merge.AbstractMerger.getMergerDelegate;
import static org.eclipse.emf.compare.merge.AbstractMerger.isInTerminalState;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasDirectOrIndirectConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.isAdditiveConflict;

import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
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
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.ide.IAdditiveResourceMappingMerger;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.logical.IModelMinimizer;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.merge.AdditiveMergeCriterion;
import org.eclipse.emf.compare.merge.ComputeDiffsToMerge;
import org.eclipse.emf.compare.merge.DelegatingMerger;
import org.eclipse.emf.compare.merge.MergeBlockedByConflictException;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.mapping.IMergeContext;

public class AdditiveResourceMappingMerger extends EMFResourceMappingMerger implements IAdditiveResourceMappingMerger {

	@Override
	protected void mergeMapping(ResourceMapping mapping, IMergeContext mergeContext,
			Set<ResourceMapping> failingMappings, IProgressMonitor monitor) throws CoreException {
		final SubMonitor subMonitor = SubMonitor.convert(monitor, 10);
		// validateMappings() has made sure we only have EMFResourceMappings
		final SynchronizationModel syncModel = ((EMFResourceMapping)mapping).getLatestModel();
		// we may have non-existing storages in the left traversal, so let's get rid of them
		removeNonExistingStorages(syncModel.getLeftTraversal());
		// get the involved resources before we run the minimizer
		final Set<IResource> resources = Sets.newLinkedHashSet(syncModel.getResources());

		final IModelMinimizer minimizer = new IdenticalResourceMinimizer();
		minimizer.minimize(syncModel, subMonitor.newChild(1)); // 10%
		final IComparisonScope scope = ComparisonScopeBuilder.create(syncModel, subMonitor.newChild(3)); // 40%

		final Builder builder = EMFCompare.builder();
		EMFCompareBuilderConfigurator.createDefault().configure(builder);

		final Comparison comparison = builder.build().compare(scope,
				BasicMonitor.toMonitor(SubMonitor.convert(subMonitor.newChild(1), 10))); // 50%

		final ResourceAdditionAndDeletionTracker resourceTracker = new ResourceAdditionAndDeletionTracker();
		final Set<URI> conflictingURIs = performPreMerge(comparison, subMonitor.newChild(3)); // 80%
		save(scope.getLeft(), syncModel.getLeftTraversal(), syncModel.getRightTraversal(),
				syncModel.getOriginTraversal());
		if (!conflictingURIs.isEmpty()) {
			failingMappings.add(mapping);
			markResourcesAsMerged(mergeContext, resources, conflictingURIs, subMonitor.newChild(2)); // 100%
		} else {
			delegateMergeOfUnmergedResourcesAndMarkDiffsAsMerged(syncModel, mergeContext, resourceTracker,
					subMonitor.newChild(2)); // 100%
		}

		scope.getLeft().eAdapters().remove(resourceTracker);
		subMonitor.setWorkRemaining(0);
	}

	private Set<URI> performPreMerge(Comparison comparison, SubMonitor subMonitor) {
		final Monitor emfMonitor = BasicMonitor.toMonitor(subMonitor);
		final Set<URI> conflictingURIs = new LinkedHashSet<URI>();
		ComputeDiffsToMerge computer = new ComputeDiffsToMerge(true, MERGER_REGISTRY,
				AdditiveMergeCriterion.INSTANCE).failOnRealConflictUnless(isAdditiveConflict());
		for (Diff next : comparison.getDifferences()) {
			doMergeForDiff(next, computer, emfMonitor, conflictingURIs);
		}
		return conflictingURIs;
	}

	private void doMergeForDiff(final Diff diff, final ComputeDiffsToMerge computer, final Monitor emfMonitor,
			final Set<URI> conflictingURIs) {
		if (isInTerminalState(diff)) {
			return;
		}
		try {
			Set<Diff> diffsToMerge = computer.getAllDiffsToMerge(diff);
			for (Diff toMerge : diffsToMerge) {
				atomicMerge(toMerge, emfMonitor);
			}
		} catch (MergeBlockedByConflictException e) {
			conflictingURIs.addAll(collectConflictingResources(e.getConflictingDiffs().iterator()));
		}
	}

	private void atomicMerge(final Diff diff, final Monitor emfMonitor) {
		if (hasDirectOrIndirectConflict(REAL).apply(diff)) {
			if (isOnlyInAdditiveConflicts(diff)) {
				if (diff.getSource() == LEFT) {
					if (isRequiredByDeletion(diff)) {
						// Deletion from left side must be overriden by right changes
						DelegatingMerger delegatingMerger = getMergerDelegate(diff, MERGER_REGISTRY,
								AdditiveMergeCriterion.INSTANCE);
						delegatingMerger.copyRightToLeft(diff, emfMonitor);
					} else {
						// other left changes have to be kept. Mark them as merged
						diff.setState(MERGED);
					}
				} else {
					if (isRequiredByDeletion(diff)) {
						// Deletion from right side must not be merged. Mark them as merged
						diff.setState(DISCARDED);
					} else {
						// Copy all other changes to left side
						getMergerDelegate(diff, MERGER_REGISTRY, AdditiveMergeCriterion.INSTANCE)
								.copyRightToLeft(diff, emfMonitor);
					}
				}
			} else {
				throw new IllegalStateException();
			}

		} else if (isPseudoConflicting(diff)) {
			EList<Diff> conflictingDiffs = diff.getConflict().getDifferences();
			// FIXME This doesn't seem useful
			for (Diff conflictingDiff : conflictingDiffs) {
				conflictingDiff.setState(MERGED);
			}
		} else if (diff.getSource() == LEFT) {
			if (isRequiredByDeletion(diff)) {
				getMergerDelegate(diff, MERGER_REGISTRY, AdditiveMergeCriterion.INSTANCE)
						.copyRightToLeft(diff, emfMonitor);
			} else {
				diff.setState(MERGED);
			}
		} else {
			// Diff from RIGHT
			if (isRequiredByDeletion(diff)) {
				diff.setState(DISCARDED);
			} else {
				getMergerDelegate(diff, MERGER_REGISTRY, AdditiveMergeCriterion.INSTANCE)
						.copyRightToLeft(diff, emfMonitor);
			}
		}
	}

	@Override
	protected void delegateMergeOfUnmergedResourcesAndMarkDiffsAsMerged(SynchronizationModel syncModel,
			IMergeContext mergeContext, ResourceAdditionAndDeletionTracker resourceTracker,
			SubMonitor subMonitor) throws CoreException {
		// mark already deleted files as merged
		for (IFile deletedFile : resourceTracker.getDeletedIFiles()) {
			final IDiff diff = mergeContext.getDiffTree().getDiff(deletedFile);
			markAsMerged(diff, mergeContext, subMonitor);
		}

		// for all left storages, delegate the merge of a deletion that has not been performed yet and mark
		// all already performed diffs as merged
		for (IStorage storage : syncModel.getLeftTraversal().getStorages()) {
			final IPath fullPath = ResourceUtil.getFixedPath(storage);
			if (fullPath == null) {
				EMFCompareIDEUIPlugin.getDefault().getLog().log(new Status(IStatus.WARNING,
						EMFCompareIDEUIPlugin.PLUGIN_ID,
						EMFCompareIDEUIMessages.getString("EMFResourceMappingMerger.mergeIncomplete"))); //$NON-NLS-1$
			} else {
				final IDiff diff = mergeContext.getDiffTree().getDiff(fullPath);
				if (diff != null) {
					markAsMerged(diff, mergeContext, subMonitor.newChild(1));
				}
			}
		}

		// delegate all additions from the right storages that have not been performed yet
		// or, if they have been merged, mark the diff as merged
		for (IStorage rightStorage : syncModel.getRightTraversal().getStorages()) {
			final IPath fullPath = ResourceUtil.getFixedPath(rightStorage);
			if (fullPath != null) {
				final IDiff diff = mergeContext.getDiffTree().getDiff(fullPath);
				if (diff != null && IDiff.ADD == diff.getKind()) {
					if (!resourceTracker.containsAddedResource(fullPath)) {
						IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(fullPath);
						IProject project = file.getProject();
						if (project.isAccessible()) {
							merge(diff, mergeContext, subMonitor.newChild(1));
						} else {
							// The project that will contain the resource is not accessible.
							// We have to copy the file "manually" from the right side to the left side.
							try {
								InputStream inputStream = rightStorage.getContents();
								FileOutputStream outputStream = new FileOutputStream(
										ResourceUtil.getAbsolutePath(rightStorage).toFile());
								ByteStreams.copy(inputStream, outputStream);
								inputStream.close();
								outputStream.close();
							} catch (FileNotFoundException e) {
								EMFCompareIDEUIPlugin.getDefault().log(e);
								// TODO Should we throw the exception here to interrupt the merge ?
							} catch (IOException e) {
								EMFCompareIDEUIPlugin.getDefault().log(e);
								// TODO Should we throw the exception here to interrupt the merge ?
							}
						}
					} else {
						markAsMerged(diff, mergeContext, subMonitor.newChild(1));
					}
				}
			}
		}
	}

	/**
	 * Test if a diff or one of the diffs that require it are delete diffs.
	 * 
	 * @param diff
	 *            The given diff
	 * @return <code>true</code> if the diff or one of the diff that require it is a deletion
	 */
	private boolean isRequiredByDeletion(Diff diff) {
		if (diff.getKind() == DELETE) {
			return true;
		} else {
			EList<Diff> requiredBy = diff.getRequiredBy();
			for (Diff requiredDiff : requiredBy) {
				if (isRequiredByDeletion(requiredDiff)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Test if a conflicting diff is part of a pseudo-conflict.
	 * 
	 * @param diff
	 *            The given diff
	 * @return <code>true</code> if the diff is part of a psudo conflict
	 */
	private boolean isPseudoConflicting(Diff diff) {
		Conflict conflict = diff.getConflict();
		if (conflict != null && conflict.getKind() == PSEUDO) {
			return true;
		}
		return false;
	}

	/**
	 * Test a conflicting diff to determine if it is contained in a conflict that can be considered as an
	 * additive conflict.
	 * 
	 * @param diff
	 *            The given diff
	 * @return <code>true</code> if the diff is part of an additive conflict
	 */
	private boolean isOnlyInAdditiveConflicts(Diff diff) {
		Set<Conflict> checkedConflicts = new LinkedHashSet<Conflict>();
		Conflict conflict = diff.getConflict();
		boolean result = false;
		if (conflict != null && conflict.getKind() == REAL && checkedConflicts.add(conflict)) {
			if (isAdditiveConflict().apply(conflict)) {
				result = true;
			} else {
				// Short-circuit as soon as non-additive conflict is found
				return false;
			}
		}
		Set<Diff> allRefiningDiffs = DiffUtil.getAllRefiningDiffs(diff);
		for (Diff refiningDiff : allRefiningDiffs) {
			conflict = refiningDiff.getConflict();
			if (conflict != null && conflict.getKind() == REAL && checkedConflicts.add(conflict)) {
				if (isAdditiveConflict().apply(conflict)) {
					result = true;
				} else {
					// Short-circuit as soon as non-additive conflict is found
					return false;
				}
			}
		}
		return result;
	}
}
