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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.alwaysTrue;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;

/**
 * This implementation of an {@link IBatchMerger} leaves some choice to the client as to what should be
 * merged.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public class BatchMerger implements IBatchMerger {
	/** The registry from which we'll retrieve our mergers. */
	private final IMerger.Registry registry;

	/** Filter the differences that should be merged. */
	private final Predicate<? super Diff> filter;

	/**
	 * Constructs our batch merger provided the registry from which to retrieve the delegate mergers. Using
	 * such a merger will merge every differences passed to its "copy" methods : conflictual or not.
	 * 
	 * @param registry
	 *            The registry from which we'll retrieve delegate mergers.
	 */
	public BatchMerger(IMerger.Registry registry) {
		this(registry, alwaysTrue());
	}

	/**
	 * Constructs our batch merger provided the registry from which to retrieve the delegate mergers, and a
	 * filter if you only wish to merge specific differences.
	 * <p>
	 * <b>Note</b> that the filter indicates differences that will be merged, not those that will be ignored.
	 * </p>
	 * <p>
	 * For example, if you wish to ignore all differences in conflict, you can use :
	 * 
	 * <pre>
	 * IMerger.Registry registry = IMerger.RegistryImpl.createStandaloneInstance();
	 * IBatchMerger bathMerger = new BatchMerger(registry, {@link com.google.common.base.Predicates#not(Predicate) not}({@link org.eclipse.emf.compare.utils.EMFComparePredicates#hasConflict(org.eclipse.emf.compare.ConflictKind...) hasConflict}(ConflictKind.PSEUDO, ConflictKind.REAL)));
	 * bathMerger.copyAll...
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param registry
	 *            The registry from which we'll retrieve delegate mergers.
	 * @param filter
	 *            Additional filter for the differences. This could be set in order to ignore diffs
	 *            originating from a given side. Note that the filter describes the differences that will be
	 *            merged, not those that will be ignored.
	 * @see com.google.common.base.Predicates
	 * @see org.eclipse.emf.compare.utils.EMFComparePredicates
	 */
	public BatchMerger(IMerger.Registry registry, Predicate<? super Diff> filter) {
		this.registry = checkNotNull(registry);
		this.filter = checkNotNull(filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IBatchMerger#copyAllLeftToRight(java.lang.Iterable,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyAllLeftToRight(Iterable<? extends Diff> differences, Monitor monitor) {
		if (filter == alwaysTrue()) {
			for (Diff diff : differences) {
				if (diff.getState() != DifferenceState.MERGED) {
					final IMerger merger = registry.getHighestRankingMerger(diff);
					merger.copyLeftToRight(diff, monitor);
				}
			}
		} else {
			for (Diff diff : Iterables.filter(differences, filter)) {
				if (diff.getState() != DifferenceState.MERGED) {
					final IMerger merger = registry.getHighestRankingMerger(diff);
					merger.copyLeftToRight(diff, monitor);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IBatchMerger#copyAllRightToLeft(java.lang.Iterable,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyAllRightToLeft(Iterable<? extends Diff> differences, Monitor monitor) {
		if (filter == alwaysTrue()) {
			for (Diff diff : differences) {
				if (diff.getState() != DifferenceState.MERGED) {
					final IMerger merger = registry.getHighestRankingMerger(diff);
					merger.copyRightToLeft(diff, monitor);
				}
			}
		} else {
			for (Diff diff : Iterables.filter(differences, filter)) {
				if (diff.getState() != DifferenceState.MERGED) {
					final IMerger merger = registry.getHighestRankingMerger(diff);
					merger.copyRightToLeft(diff, monitor);
				}
			}
		}
	}
}
