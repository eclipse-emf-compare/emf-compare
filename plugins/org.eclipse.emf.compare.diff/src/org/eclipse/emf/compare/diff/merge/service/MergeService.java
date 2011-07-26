/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.merge.EMFCompareEObjectCopier;
import org.eclipse.emf.compare.diff.merge.IMergeListener;
import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.merge.MergeEvent;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.ecore.EObject;

/**
 * Service for use with diff merging operations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class MergeService {
	/** This copier will be used when merging references. */
	private static EMFCompareEObjectCopier copier;

	/**
	 * Holds a list of all the merge listeners registered for notifications on merge operations.
	 */
	private static final List<IMergeListener> MERGE_LISTENERS = new ArrayList<IMergeListener>();

	/**
	 * Default constructor.
	 */
	private MergeService() {
		// hides default constructor
	}

	/**
	 * Registers a new merge listener for notifications about merge operations. Has no effect if the listener
	 * is already registered.
	 * 
	 * @param listener
	 *            New Listener to register for notifications.
	 */
	public static void addMergeListener(IMergeListener listener) {
		MERGE_LISTENERS.add(listener);
	}

	/**
	 * Returns the copier given the diff it should merge.
	 * 
	 * @param diff
	 *            The DiffElement for which a copier is needed.
	 * @return The copier for a given diff.
	 */
	public static EMFCompareEObjectCopier getCopier(DiffElement diff) {
		final DiffModel diffModel = getContainerDiffModel(diff);
		if (diffModel == null)
			throw new IllegalArgumentException("The diff element should be contained in a DiffModel instance"); //$NON-NLS-1$
		if (copier == null) {
			copier = new EMFCompareEObjectCopier(diffModel);
		} else if (copier.getDiffModel() != diffModel) {
			copier.clear();
			copier = new EMFCompareEObjectCopier(diffModel);
		}
		return copier;
	}

	/**
	 * Browse the diff model from the leaf to the top to find the containing {@link DiffModel} instance.
	 * 
	 * @param diff
	 *            any {@link DiffElement}.
	 * @return the containing {@link DiffModel} instance, null if not found.
	 */
	private static DiffModel getContainerDiffModel(DiffElement diff) {
		EObject container = diff.eContainer();
		while (container != null) {
			if (container instanceof DiffModel)
				return (DiffModel)container;
			container = container.eContainer();
		}
		return null;
	}

	/**
	 * Merges a single DiffElement in the direction specified by <code>leftToRight</code>.
	 * <p>
	 * Will notify the list of its merge listeners before, and after the operation.
	 * </p>
	 * 
	 * @param element
	 *            {@link DiffElement} containing the information to merge.
	 * @param leftToRight
	 *            <code>True</code> if the changes must be applied from the left to the right model,
	 *            <code>False</code> when they have to be applied the other way around.
	 */
	public static void merge(DiffElement element, boolean leftToRight) {
		fireMergeOperationStart(element);
		doMerge(element, leftToRight);
		fireMergeOperationEnd(element);
	}

	/**
	 * Merges a list of DiffElements in the direction specified by <code>leftToRight</code>.
	 * <p>
	 * Will notify the list of its merge listeners before, and after the operation.
	 * </p>
	 * 
	 * @param elements
	 *            {@link DiffElement}s containing the information to merge.
	 * @param leftToRight
	 *            <code>True</code> if the changes must be applied from the left to the right model,
	 *            <code>False</code> when they have to be applied the other way around.
	 */
	public static void merge(List<DiffElement> elements, boolean leftToRight) {
		fireMergeOperationStart(elements);
		for (DiffElement element : new ArrayList<DiffElement>(elements))
			// we might remove the diff from the list before merging it
			// (eOpposite reference)
			if (element.eContainer() != null && element.getIsHiddenBy().isEmpty())
				doMerge(element, leftToRight);
		fireMergeOperationEnd(elements);
	}

	/**
	 * removes a merge listener from the list of registered listeners. This will have no effect if the given
	 * listener is not registered for notifications on this service.
	 * 
	 * @param listener
	 *            New Listener to register for notifications.
	 */
	public static void removeMergeListener(IMergeListener listener) {
		MERGE_LISTENERS.remove(listener);
	}

	/**
	 * Applies the changes implied by a given {@link DiffElement} in the direction specified by
	 * <code>leftToRight</code>.
	 * <p>
	 * Will notify the list of its merge listeners before, and after the merge.
	 * </p>
	 * 
	 * @param element
	 *            {@link DiffElement} containing the information to merge.
	 * @param leftToRight
	 *            <code>True</code> if the changes must be applied from the left to the right model,
	 *            <code>False</code> when they have to be applied the other way around.
	 */
	protected static void doMerge(DiffElement element, boolean leftToRight) {
		fireMergeDiffStart(element);
		final IMerger merger;
		if (element instanceof ConflictingDiffElement)
			merger = MergeFactory.createMerger(element.getSubDiffElements().get(0));
		else
			merger = MergeFactory.createMerger(element);
		if (leftToRight && merger.canUndoInTarget()) {
			merger.undoInTarget();
		} else if (!leftToRight && merger.canApplyInOrigin()) {
			merger.applyInOrigin();
		}
		fireMergeDiffEnd(element);
	}

	/**
	 * Notifies all registered listeners that a {@link DiffElement} has just been merged.
	 * 
	 * @param diff
	 *            {@link DiffElement} which has just been merged.
	 */
	protected static void fireMergeDiffEnd(DiffElement diff) {
		for (IMergeListener listener : MERGE_LISTENERS)
			listener.mergeDiffEnd(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a DiffElement is about to be merged.
	 * 
	 * @param diff
	 *            {@link DiffElement} which is about to be merged.
	 */
	protected static void fireMergeDiffStart(DiffElement diff) {
		for (IMergeListener listener : MERGE_LISTENERS)
			listener.mergeDiffStart(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a merge operation on a single diff just ended.
	 * 
	 * @param diff
	 *            {@link DiffElement} which has just been merged.
	 */
	protected static void fireMergeOperationEnd(DiffElement diff) {
		for (IMergeListener listener : MERGE_LISTENERS)
			listener.mergeOperationEnd(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a merge operation has ended for a list of differences.
	 * 
	 * @param diffs
	 *            {@link DiffElement}s which have been merged.
	 */
	protected static void fireMergeOperationEnd(List<DiffElement> diffs) {
		for (IMergeListener listener : MERGE_LISTENERS)
			listener.mergeOperationEnd(new MergeEvent(diffs));
	}

	/**
	 * Notifies all registered listeners that a merge operation is about to start for a single diff.
	 * 
	 * @param diff
	 *            {@link DiffElement} which is about to be merged.
	 */
	protected static void fireMergeOperationStart(DiffElement diff) {
		for (IMergeListener listener : MERGE_LISTENERS)
			listener.mergeOperationStart(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a merge operation is about to start for a list of differences.
	 * 
	 * @param diffs
	 *            {@link DiffElement}s which are about to be merged.
	 */
	protected static void fireMergeOperationStart(List<DiffElement> diffs) {
		for (IMergeListener listener : MERGE_LISTENERS)
			listener.mergeOperationStart(new MergeEvent(diffs));
	}
}
