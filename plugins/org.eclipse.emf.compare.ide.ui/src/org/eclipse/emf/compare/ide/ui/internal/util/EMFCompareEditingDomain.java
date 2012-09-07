/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.util;

import static com.google.common.collect.Iterables.any;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasState;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareEditingDomain {

	private final ChangeRecorder fChangeRecorder;

	private final ImmutableCollection<Notifier> fNotifiers;

	private final BasicCommandStack fCommandStack;

	public EMFCompareEditingDomain(Comparison comparison, Notifier left, Notifier right, Notifier ancestor) {
		if (ancestor == null) {
			fNotifiers = ImmutableList.of(comparison, left, right);
		} else {
			fNotifiers = ImmutableList.of(comparison, left, right, ancestor);
		}
		fCommandStack = new BasicCommandStack();

		fChangeRecorder = new ChangeRecorder();
		fChangeRecorder.setResolveProxies(false);

		for (Notifier notifier : fNotifiers) {
			notifier.eAdapters().add(fChangeRecorder);
		}
	}

	public void dispose() {
		fChangeRecorder.dispose();
	}

	public BasicCommandStack getCommandStack() {
		return fCommandStack;
	}

	public Command createCopyRightToLeftCommand(Diff diff) {
		return new CopyRightToLeftCommand(fChangeRecorder, fNotifiers, diff);
	}

	public Command createCopyLeftToRightCommand(final Diff diff) {
		return new CopyLeftToRightCommand(fChangeRecorder, fNotifiers, diff);
	}

	public Command createCopyAllNonConflictingRightToLeftCommand(Collection<? extends Diff> diff) {
		return new CopyAllNonConflictingRightToLeftCommand(fChangeRecorder, fNotifiers, diff);
	}

	public Command createCopyAllNonConflictingLeftToRightCommand(final Collection<? extends Diff> diff) {
		return new CopyAllNonConflictingLeftToRightCommand(fChangeRecorder, fNotifiers, diff);
	}

	private static class CopyLeftToRightCommand extends ChangeCommand {

		private final Diff fDiff;

		/**
		 * 
		 */
		private CopyLeftToRightCommand(ChangeRecorder changeRecorder, Collection<Notifier> notifiers,
				Diff diff) {
			super(changeRecorder, notifiers);
			fDiff = diff;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
		 */
		@Override
		public boolean canExecute() {
			return fDiff.getState() == DifferenceState.UNRESOLVED && super.canExecute();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			fDiff.copyLeftToRight();
		}

	}

	private static class CopyRightToLeftCommand extends ChangeCommand {

		private final Diff fDiff;

		/**
		 * 
		 */
		private CopyRightToLeftCommand(ChangeRecorder changeRecorder, Collection<Notifier> notifiers,
				Diff diff) {
			super(changeRecorder, notifiers);
			fDiff = diff;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
		 */
		@Override
		public boolean canExecute() {
			return fDiff.getState() == DifferenceState.UNRESOLVED && super.canExecute();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			fDiff.copyRightToLeft();
		}

	}

	private static class CopyAllNonConflictingRightToLeftCommand extends ChangeCommand {

		private final Collection<? extends Diff> fDiff;

		/**	
		 * 
		 */
		public CopyAllNonConflictingRightToLeftCommand(ChangeRecorder changeRecorder,
				Collection<Notifier> notifiers, Collection<? extends Diff> diff) {
			super(changeRecorder, notifiers);
			fDiff = diff;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
		 */
		@Override
		public boolean canExecute() {
			return any(fDiff, hasState(DifferenceState.UNRESOLVED)) && super.canExecute();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			for (Diff diff : fDiff) {
				if (diff.getSource() == DifferenceSource.RIGHT) {
					if (diff.getConflict() == null) {
						diff.copyRightToLeft();
					}
				}
			}

		}

	}

	private static class CopyAllNonConflictingLeftToRightCommand extends ChangeCommand {

		private final Collection<? extends Diff> fDiff;

		/**	
		 * 
		 */
		public CopyAllNonConflictingLeftToRightCommand(ChangeRecorder changeRecorder,
				Collection<Notifier> notifiers, Collection<? extends Diff> diff) {
			super(changeRecorder, notifiers);
			fDiff = diff;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
		 */
		@Override
		public boolean canExecute() {
			return any(fDiff, hasState(DifferenceState.UNRESOLVED)) && super.canExecute();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
		 */
		@Override
		protected void doExecute() {
			for (Diff diff : fDiff) {
				if (diff.getSource() == DifferenceSource.LEFT) {
					if (diff.getConflict() == null) {
						diff.copyLeftToRight();
					}
				}
			}
		}

	}
}
