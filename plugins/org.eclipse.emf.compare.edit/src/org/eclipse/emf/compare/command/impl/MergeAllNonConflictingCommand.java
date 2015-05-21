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

import com.google.common.annotations.Beta;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
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
		// We don't use differences in this case.
		// The IMergeAllNonConflictingRunnable computes all differences he needs.
		// So just take the differences to let the command executable.
		// (A command with no differences isn't executable; see
		// org.eclipse.emf.compare.command.impl.AbstractCopyCommand.canExecute())
		super(changeRecorder, notifiers, comparison.getDifferences(), leftToRight, mergerRegistry);
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
}
