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
package org.eclipse.emf.compare.internal.conflict;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.SubMatchIterator;

/**
 * A custom iterator that will walk a Match->submatch tree, and allow iteration over the Diffs of these
 * Matches.
 * <p>
 * Since we're walking over Matches but returning Diffs, this is not a good candidate for guava's filters.
 * We're providing the custom {@link DiffTreeIterator#setFilter(Predicate)} and
 * {@link DiffTreeIterator#setPruningFilter(Predicate)} to allow for filtering or pruning the the iteration.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DiffTreeIterator implements Iterator<Diff> {
	/**
	 * The tree iterator that will walk over our Match tree. Some of the paths can be pruned through the use
	 * of a {@link #pruningFilter}.
	 */
	private final TreeIterator<Match> subMatchIterator;

	/** An iterator over the differences of the current Match. */
	private Iterator<Diff> diffIterator;

	/** Current match. */
	private Match current;

	/** The Diff that will be returned by the next call to {@link #next()}. */
	private Diff nextDiff;

	/** Only Diffs that meet this criterion will be returned by this iterator. */
	private Predicate<? super Diff> filter = Predicates.alwaysTrue();

	/**
	 * This particular filter can be used in order to prune a given Match and all of its differences and
	 * sub-differences.
	 */
	private Predicate<? super Match> pruningFilter = Predicates.alwaysFalse();

	/**
	 * Constructs our iterator given the root of the Match tree to iterate over.
	 * 
	 * @param start
	 *            Starting match of the tree we'll iterate over.
	 */
	public DiffTreeIterator(Match start) {
		this.current = start;
		this.subMatchIterator = new SubMatchIterator(start);
		this.diffIterator = start.getDifferences().iterator();
	}

	/**
	 * Sets the criterion that Diffs must meet to be returned by this iterator.
	 * 
	 * @param filter
	 *            The filter differences must meet.
	 */
	public void setFilter(Predicate<? super Diff> filter) {
		this.filter = filter;
	}

	/**
	 * Sets the pruning filter for this iterator. Any Match that meets this criterion will be pruned along
	 * with all of its differences and sub-differences.
	 * 
	 * @param pruningFilter
	 *            The pruning filter for this iterator.
	 */
	public void setPruningFilter(Predicate<? super Match> pruningFilter) {
		this.pruningFilter = pruningFilter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (nextDiff != null) {
			return true;
		}
		if (!diffIterator.hasNext()) {
			computeNextMatch();
		}
		while (nextDiff == null && diffIterator.hasNext()) {
			final Diff next = diffIterator.next();
			if (filter.apply(next)) {
				nextDiff = next;
			}
		}
		return nextDiff != null;
	}

	/**
	 * Computes the next match within the sub-match tree, pruning those that may meet {@link #pruningFilter}.
	 */
	private void computeNextMatch() {
		final Match old = current;
		while (current == old && subMatchIterator.hasNext()) {
			final Match next = subMatchIterator.next();
			if (pruningFilter.apply(next)) {
				subMatchIterator.prune();
			} else {
				current = next;
				diffIterator = current.getDifferences().iterator();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#next()
	 */
	public Diff next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		final Diff next = nextDiff;
		nextDiff = null;
		return next;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		diffIterator.remove();
	}
}
