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
package org.eclipse.emf.compare.command.impl;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.annotations.Beta;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.internal.domain.IMergeAllNonConflictingRunnable;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

/**
 * A merge command that merges all non-conflicting differences of the given comparison in either direction.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.1
 */
@Beta
public class MergeAllNonConflictingCommand extends AbstractCopyCommand {
	/** The comparison which differences this command will merge. */
	private final Comparison comparison;

	/**
	 * This will be set to the list of diffs that have been merged successfully after {@link #doExecute()
	 * execution} of this command.
	 */
	private List<Diff> affectedDiffs;

	/** The runnable to execute for the actual merge operation. */
	private IMergeAllNonConflictingRunnable runnable;

	/**
	 * Creates a new instance.
	 * 
	 * @param changeRecorder
	 *            The change recorder associated to this command.
	 * @param notifiers
	 *            The collection of notifiers that will be notified of this command's execution.
	 * @param comparison
	 *            The comparison which differences this command will merge.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param runnable
	 *            The runnable to execute for the actual merge operation.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 */
	public MergeAllNonConflictingCommand(ChangeRecorder changeRecorder, Collection<Notifier> notifiers,
			Comparison comparison, boolean leftToRight, IMerger.Registry mergerRegistry,
			IMergeAllNonConflictingRunnable runnable) {
		super(changeRecorder, notifiers, getDifferencesToMerge(comparison, leftToRight), leftToRight,
				mergerRegistry);
		this.comparison = comparison;
		this.runnable = runnable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
	 */
	@Override
	protected void doExecute() {
		runnable.merge(comparison, isLeftToRight(), mergerRegistry);
	}

	/** {@inheritDoc} */
	@Override
	public Collection<?> getAffectedObjects() {
		if (affectedDiffs == null) {
			return Collections.emptyList();
		}
		return affectedDiffs;
	}

	/**
	 * Returns the set of differences this command will operate on.
	 * <p>
	 * <b>Note</b> that this is not the set of affected objects! The command will operate on all of these
	 * differences, but only after execution can we tell whether the diffs have actually been merged.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison we're operating on.
	 * @param leftToRight
	 *            Direction of the merge.
	 * @return The list of differences this command will try and merge
	 */
	private static List<Diff> getDifferencesToMerge(Comparison comparison, boolean leftToRight) {
		Iterable<Diff> diffs;
		if (leftToRight) {
			diffs = Iterables.filter(comparison.getDifferences(), fromSide(DifferenceSource.LEFT));
		} else {
			diffs = Iterables.filter(comparison.getDifferences(), fromSide(DifferenceSource.RIGHT));
		}
		return Lists.newArrayList(diffs);
	}
}
