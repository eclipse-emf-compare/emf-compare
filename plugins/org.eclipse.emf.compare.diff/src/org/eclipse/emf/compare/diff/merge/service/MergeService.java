/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
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

import org.eclipse.emf.compare.diff.merge.api.IMergeListener;
import org.eclipse.emf.compare.diff.merge.api.IMerger;
import org.eclipse.emf.compare.diff.merge.api.MergeEvent;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * Services for model merging.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class MergeService {
	/** Holds a list of all the merge listeners registered for notifications on merge operations. */
	private final List<IMergeListener> listeners = new ArrayList<IMergeListener>();

	/**
	 * Registers a new merge listener for notifications about merge operations. Has no effect if the listener
	 * is already registered.
	 * 
	 * @param listener
	 *            New Listener to register for notifications.
	 */
	public void addMergeListener(IMergeListener listener) {
		listeners.add(listener);
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
	public void merge(DiffElement element, boolean leftToRight) {
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
	public void merge(List<DiffElement> elements, boolean leftToRight) {
		fireMergeOperationStart(elements);
		for (DiffElement element : elements)
			// we might remove the diff from the list before merging it (eOpposite reference)
			if (element.eContainer() != null)
				doMerge(element, leftToRight);
		fireMergeOperationEnd(elements);
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
	protected void doMerge(DiffElement element, boolean leftToRight) {
		fireMergeDiffStart(element);
		final IMerger merger = MergeFactory.createMerger(element);
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
	protected void fireMergeDiffEnd(DiffElement diff) {
		for (IMergeListener listener : listeners)
			listener.mergeDiffEnd(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a DiffElement is about to be merged.
	 * 
	 * @param diff
	 *            {@link DiffElement} which is about to be merged.
	 */
	protected void fireMergeDiffStart(DiffElement diff) {
		for (IMergeListener listener : listeners)
			listener.mergeDiffStart(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a merge operation on a single diff just ended.
	 * 
	 * @param diff
	 *            {@link DiffElement} which has just been merged.
	 */
	protected void fireMergeOperationEnd(DiffElement diff) {
		for (IMergeListener listener : listeners)
			listener.mergeOperationEnd(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a merge operation has ended for a list of differences.
	 * 
	 * @param diffs
	 *            {@link DiffElement}s which have been merged.
	 */
	protected void fireMergeOperationEnd(List<DiffElement> diffs) {
		for (IMergeListener listener : listeners)
			listener.mergeOperationEnd(new MergeEvent(diffs));
	}

	/**
	 * Notifies all registered listeners that a merge operation is about to start for a single diff.
	 * 
	 * @param diff
	 *            {@link DiffElement} which is about to be merged.
	 */
	protected void fireMergeOperationStart(DiffElement diff) {
		for (IMergeListener listener : listeners)
			listener.mergeOperationStart(new MergeEvent(diff));
	}

	/**
	 * Notifies all registered listeners that a merge operation is about to start for a list of differences.
	 * 
	 * @param diffs
	 *            {@link DiffElement}s which are about to be merged.
	 */
	protected void fireMergeOperationStart(List<DiffElement> diffs) {
		for (IMergeListener listener : listeners)
			listener.mergeOperationStart(new MergeEvent(diffs));
	}
}
