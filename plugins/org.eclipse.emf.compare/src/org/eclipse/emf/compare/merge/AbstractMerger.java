/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import java.util.List;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.utils.EMFCompareCopier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Abstract implementation of an {@link IMerger}. This can be used as a base implementation to avoid
 * re-implementing the whole contract.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public abstract class AbstractMerger implements IMerger {
	/** Ranking of this merger. */
	private int ranking;

	/** Registry from which this merger has been created.. */
	private Registry registry;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#setRanking(int)
	 */
	public void setRanking(int r) {
		ranking = r;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#getRegistry()
	 */
	public Registry getRegistry() {
		return registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IMerger#setRegistry(org.eclipse.emf.compare.merge.IMerger.Registry)
	 */
	public void setRegistry(Registry registry) {
		if (this.registry != null && registry != null) {
			throw new IllegalStateException("The registry has to be set only once."); //$NON-NLS-1$
		}
		this.registry = registry;
	}

	/**
	 * This will merge all {@link Diff#getRequiredBy() differences that require} {@code diff} in the given
	 * direction.
	 * 
	 * @param diff
	 *            We need to merge all differences that require this one (see {@link Diff#getRequiredBy()}.
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft(Diff, Monitor) apply} all differences that require
	 *            {@code diff}. Otherwise, {@link #copyLeftToRight(Diff, Monitor) revert} them.
	 * @param monitor
	 *            The monitor we should use to report progress.
	 */
	protected void mergeRequiredBy(Diff diff, boolean rightToLeft, Monitor monitor) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : diff.getRequiredBy()) {
			// TODO: what to do when state = Discarded but is required?
			mergeDiff(dependency, rightToLeft, monitor);
		}
	}

	/**
	 * This will merge all {@link Diff#getRequires() differences required by} {@code diff} in the given
	 * direction.
	 * 
	 * @param diff
	 *            The difference which requirements we need to merge.
	 * @param rightToLeft
	 *            If {@code true}, {@link #copyRightToLeft(Diff, Monitor) apply} all required differences.
	 *            Otherwise, {@link #copyLeftToRight(Diff, Monitor) revert} them.
	 * @param monitor
	 *            The monitor we should use to report progress.
	 */
	protected void mergeRequires(Diff diff, boolean rightToLeft, Monitor monitor) {
		// TODO log back to the user what we will merge along?
		for (Diff dependency : diff.getRequires()) {
			// TODO: what to do when state = Discarded but is required?
			mergeDiff(dependency, rightToLeft, monitor);
		}
	}

	/**
	 * This can be used by mergers to merge another (required, equivalent...) difference using the right
	 * merger for that diff.
	 * 
	 * @param diff
	 *            The diff we need to merge.
	 * @param rightToLeft
	 *            Direction of that merge.
	 * @param monitor
	 *            The monitor we should use to report progress.
	 */
	protected void mergeDiff(Diff diff, boolean rightToLeft, Monitor monitor) {
		if (rightToLeft) {
			final IMerger delegate = getRegistry().getHighestRankingMerger(diff);
			delegate.copyRightToLeft(diff, monitor);
		} else {
			final IMerger delegate = getRegistry().getHighestRankingMerger(diff);
			delegate.copyLeftToRight(diff, monitor);
		}
	}

	/**
	 * This will create a copy of the given EObject that can be used as the target of an addition (or the
	 * reverting of a deletion).
	 * <p>
	 * The target will be self-contained and will have no reference towards any other EObject set (neither
	 * containment nor "classic" references). All of its attributes' values will match the given
	 * {@code referenceObject}'s.
	 * </p>
	 * 
	 * @param referenceObject
	 *            The EObject for which we'll create a copy.
	 * @return A self-contained copy of {@code referenceObject}.
	 * @see EMFCompareCopier#copy(EObject)
	 */
	protected EObject createCopy(EObject referenceObject) {
		/*
		 * We can't simply use EcoreUtil.copy. References will have their own diffs and will thus be merged
		 * later on.
		 */
		final EcoreUtil.Copier copier = new EMFCompareCopier();
		return copier.copy(referenceObject);
	}

	/**
	 * Adds the given {@code value} into the given {@code list} at the given {@code index}. An {@code index}
	 * under than zero or above the list's size will mean that the value should be appended at the end of the
	 * list.
	 * 
	 * @param list
	 *            The list into which {@code value} should be added.
	 * @param value
	 *            The value we need to add to {@code list}.
	 * @param <E>
	 *            Type of objects contained in the list.
	 * @param insertionIndex
	 *            The index at which {@code value} should be inserted into {@code list}. {@code -1} if it
	 *            should be appended at the end of the list.
	 */
	@SuppressWarnings("unchecked")
	protected <E> void addAt(List<E> list, E value, int insertionIndex) {
		if (list instanceof InternalEList<?>) {
			if (insertionIndex < 0 || insertionIndex > list.size()) {
				((InternalEList<Object>)list).addUnique(value);
			} else {
				((InternalEList<Object>)list).addUnique(insertionIndex, value);
			}
		} else {
			if (insertionIndex < 0 || insertionIndex > list.size()) {
				list.add(value);
			} else {
				list.add(insertionIndex, value);
			}
		}
	}
}
