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

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;

/**
 * This command can be used to copy a number of diffs (or a single one) in a given direction.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 3.0
 */
public abstract class AbstractCopyCommand extends ChangeCommand implements ICompareCopyCommand {
	/** The list of differences we are to merge. */
	protected final List<? extends Diff> differences;

	/** Direction of the merge operation. */
	protected final boolean leftToRight;

	/** Merger registry. */
	protected final IMerger.Registry mergerRegistry;

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
	 */
	public AbstractCopyCommand(ChangeRecorder changeRecorder, Collection<Notifier> notifiers,
			List<? extends Diff> differences, boolean leftToRight, IMerger.Registry mergerRegistry) {
		super(changeRecorder, notifiers);
		this.differences = ImmutableList.copyOf(differences);
		this.leftToRight = leftToRight;
		this.mergerRegistry = mergerRegistry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.ICompareCopyCommand#isLeftToRight()
	 */
	public boolean isLeftToRight() {
		return leftToRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.AbstractCommand#getAffectedObjects()
	 */
	@Override
	public Collection<?> getAffectedObjects() {
		return differences;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return super.canExecute() && !differences.isEmpty();
	}
}
