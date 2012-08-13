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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
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
		return new CopyRightToLeftCommand(diff);
	}

	public Command createCopyLeftToRightCommand(final Diff diff) {
		return new CopyLeftToRightCommand(diff);
	}

	private class CopyLeftToRightCommand extends ChangeCommand {

		private final Diff fDiff;

		/**
		 * 
		 */
		private CopyLeftToRightCommand(Diff diff) {
			super(fChangeRecorder, fNotifiers);
			fDiff = diff;
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

	private class CopyRightToLeftCommand extends ChangeCommand {

		private final Diff fDiff;

		/**
		 * 
		 */
		private CopyRightToLeftCommand(Diff diff) {
			super(fChangeRecorder, fNotifiers);
			fDiff = diff;
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
}
