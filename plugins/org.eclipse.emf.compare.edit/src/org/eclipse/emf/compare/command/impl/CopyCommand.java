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
package org.eclipse.emf.compare.command.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

/**
 * This command can be used to copy a number of diffs (or a single one) in a given direction.
 * <p>
 * <b>Note</b> that this will merge <i>all</i> differences that are passed to it, whether they're conflicting
 * or not.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @deprecated Use {@link MergeCommand} instead.
 */
@Deprecated
public class CopyCommand extends AbstractCopyCommand {

	/**
	 * Constructs an instance of this command given the list of differences that it needs to merge.
	 * 
	 * @param changeRecorder
	 *            The change recorder associated to this command.
	 * @param notifiers
	 *            The collection of notifiers that will be notified of this command's execution.
	 * @param differences
	 *            The list of differences that this command should merge.
	 * @param leftToRight
	 *            The direction in which {@code differences} should be merged.
	 * @param mergerRegistry
	 *            The registry of mergers.
	 * @since 3.0
	 */
	public CopyCommand(ChangeRecorder changeRecorder, Collection<Notifier> notifiers,
			List<? extends Diff> differences, boolean leftToRight, IMerger.Registry mergerRegistry) {
		super(changeRecorder, notifiers, differences, leftToRight, mergerRegistry);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
	 */
	@Override
	protected void doExecute() {
		final IBatchMerger merger = new BatchMerger(mergerRegistry);
		if (leftToRight) {
			merger.copyAllLeftToRight(differences, new BasicMonitor());
		} else {
			merger.copyAllRightToLeft(differences, new BasicMonitor());
		}
	}
}
