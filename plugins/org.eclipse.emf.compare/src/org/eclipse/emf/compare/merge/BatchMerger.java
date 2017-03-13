/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo.
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

import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;

/**
 * This implementation of an {@link IBatchMerger} leaves some choice to the client as to what should be
 * merged.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public class BatchMerger implements IBatchMerger {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(BatchMerger.class);

	/** The registry from which we'll retrieve our mergers. */
	private final IMerger.Registry2 registry;

	/** Filter the differences that should be merged. */
	private final Predicate<? super Diff> filter;

	/** The relationship computer used to calculate dependencies and requirements of diffs. */
	private IDiffRelationshipComputer relationshipComputer;

	/**
	 * Constructs our batch merger provided the registry from which to retrieve the delegate mergers. Using
	 * such a merger will merge every differences passed to its "copy" methods : conflictual or not.
	 * 
	 * @param registry
	 *            The registry from which we'll retrieve delegate mergers, must be an instance of
	 *            IMerger.Registry2.
	 */
	public BatchMerger(IMerger.Registry registry) {
		this(new DiffRelationshipComputer(registry), alwaysTrue());
	}

	/**
	 * Constructs our batch merger provided the registry from which to retrieve the delegate mergers. Using
	 * such a merger will merge every differences passed to its "copy" methods : conflictual or not.
	 * 
	 * @param relationshipComputer
	 *            The relationship computer used to calculate dependencies and requirements of diffs.
	 * @since 3.5
	 */
	public BatchMerger(IDiffRelationshipComputer relationshipComputer) {
		this(relationshipComputer, alwaysTrue());
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
	 * </p>
	 * 
	 * @param registry
	 *            The registry from which we'll retrieve delegate mergers, must be an instance of
	 *            IMerger.Registry2.
	 * @param filter
	 *            Additional filter for the differences. This could be set in order to ignore diffs
	 *            originating from a given side. Note that the filter describes the differences that will be
	 *            merged, not those that will be ignored.
	 * @see com.google.common.base.Predicates
	 * @see org.eclipse.emf.compare.utils.EMFComparePredicates
	 */
	public BatchMerger(IMerger.Registry registry, Predicate<? super Diff> filter) {
		this(new DiffRelationshipComputer(registry), filter);
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
	 * </p>
	 * 
	 * @param filter
	 *            Additional filter for the differences. This could be set in order to ignore diffs
	 *            originating from a given side. Note that the filter describes the differences that will be
	 *            merged, not those that will be ignored.
	 * @param relationshipComputer
	 *            The relationship computer used to calculate dependencies and requirements of diffs.
	 * @see com.google.common.base.Predicates
	 * @see org.eclipse.emf.compare.utils.EMFComparePredicates
	 * @since 3.5
	 */
	public BatchMerger(IDiffRelationshipComputer relationshipComputer, Predicate<? super Diff> filter) {
		this.relationshipComputer = checkNotNull(relationshipComputer);
		this.registry = (IMerger.Registry2)checkNotNull(relationshipComputer.getMergerRegistry());
		this.filter = checkNotNull(filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IBatchMerger#copyAllLeftToRight(java.lang.Iterable,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyAllLeftToRight(Iterable<? extends Diff> differences, Monitor monitor) {
		long start = 0;
		if (LOGGER.isDebugEnabled()) {
			start = System.currentTimeMillis();
			LOGGER.debug("copyAllLeftToRight(differences, monitor) - Start"); //$NON-NLS-1$
		}
		ComputeDiffsToMerge computer = new ComputeDiffsToMerge(false, relationshipComputer);
		for (Diff diff : Iterables.filter(differences, filter)) {
			if (!AbstractMerger.isInTerminalState(diff)) {
				Set<Diff> diffsToMerge = computer.getAllDiffsToMerge(diff);
				for (Diff toMerge : diffsToMerge) {
					if (!AbstractMerger.isInTerminalState(toMerge)) {
						final IMerger merger = registry.getHighestRankingMerger(toMerge);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("copyAllLeftToRight - Selected merger: " //$NON-NLS-1$
									+ merger.getClass().getSimpleName());
						}
						merger.copyLeftToRight(toMerge, monitor);
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled()) {
			long duration = System.currentTimeMillis() - start;
			LOGGER.debug("copyAllLeftToRight(differences, monitor) - Stop - Time spent: " + duration + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.merge.IBatchMerger#copyAllRightToLeft(java.lang.Iterable,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void copyAllRightToLeft(Iterable<? extends Diff> differences, Monitor monitor) {
		long start = 0;
		if (LOGGER.isDebugEnabled()) {
			start = System.currentTimeMillis();
			LOGGER.debug("copyAllRightToLeft(differences, monitor) - Start"); //$NON-NLS-1$
		}
		ComputeDiffsToMerge computer = new ComputeDiffsToMerge(true, relationshipComputer);
		for (Diff diff : Iterables.filter(differences, filter)) {
			if (!AbstractMerger.isInTerminalState(diff)) {
				Set<Diff> diffsToMerge = computer.getAllDiffsToMerge(diff);
				for (Diff toMerge : diffsToMerge) {
					if (!AbstractMerger.isInTerminalState(toMerge)) {
						final IMerger merger = registry.getHighestRankingMerger(toMerge);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("copyAllLeftToRight - Selected merger: " //$NON-NLS-1$
									+ merger.getClass().getSimpleName());
						}
						merger.copyRightToLeft(toMerge, monitor);
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled()) {
			long duration = System.currentTimeMillis() - start;
			LOGGER.debug("copyAllRightToLeft(differences, monitor) - Stop - Time spent: " + duration + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
