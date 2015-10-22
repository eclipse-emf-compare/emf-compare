/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.mergeresolution;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.ide.ui.internal.mergeresolution.MergeResolutionListenerRegistry;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider.TreeNodeCompareInput;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EObject;

/**
 * This class manages the detection and handling of merge resolution events. Merge resolution happens when all
 * merge conflicts are resolved and the users saves the merge result.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 * @since 4.2
 */
public class MergeResolutionManager {

	/**
	 * The name of the extension point.
	 */
	public static final String EXTENSION_POINT = "org.eclipse.emf.compare.ide.ui.mergeResolutionListener"; //$NON-NLS-1$

	/**
	 * The registry for the extension point.
	 */
	private final MergeResolutionListenerRegistry mergeResolutionListenerRegistry;

	/**
	 * Default constructor.
	 * 
	 * @param registry
	 *            The {@link MergeResolutionListenerRegistry} instance to manage
	 */
	public MergeResolutionManager(MergeResolutionListenerRegistry registry) {
		this.mergeResolutionListenerRegistry = registry;
	}

	/**
	 * Called when a merge result view's flush method is called. This indicates that the user has saved the
	 * contents, but does not necessarily mean that all conflicts are resolved.
	 * 
	 * @param input
	 *            The content of the merge resolution being flushed. This must be a
	 *            {@link TreeNodeCompareInput} object.
	 */
	public void handleFlush(Object input) {
		// We only know how to handle TreeNodeCompareInput
		if (!(input instanceof TreeNodeCompareInput)) {
			return;
		}
		TreeNodeCompareInput treeNodeCompareInput = (TreeNodeCompareInput)input;
		EObject eobject = treeNodeCompareInput.getComparisonObject();

		Comparison comparison = ComparisonUtil.getComparison(eobject);

		if (comparison == null) {
			// what to do? how do we get the Comparison object?
			return;
		}

		if (comparison.getConflicts().size() == 0) {
			return;
		}

		Predicate<Conflict> unresolvedConflict = new Predicate<Conflict>() {
			public boolean apply(Conflict conflict) {
				return conflict != null && conflict.getKind() != ConflictKind.PSEUDO && Iterables.any(
						conflict.getDifferences(), EMFComparePredicates.hasState(DifferenceState.UNRESOLVED));
			}
		};
		if (!Iterables.any(comparison.getConflicts(), unresolvedConflict)) {
			mergeResolutionListenerRegistry.mergeResolutionCompleted(comparison);
		}
	}
}
