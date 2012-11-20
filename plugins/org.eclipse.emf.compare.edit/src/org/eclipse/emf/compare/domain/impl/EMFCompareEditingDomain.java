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
package org.eclipse.emf.compare.domain.impl;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.impl.CompareCommandStack;
import org.eclipse.emf.compare.command.impl.CopyAllNonConflictingCommand;
import org.eclipse.emf.compare.command.impl.CopyCommand;
import org.eclipse.emf.compare.command.impl.DualCompareCommandStack;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareEditingDomain implements ICompareEditingDomain {

	private final ChangeRecorder fChangeRecorder;

	private final ImmutableCollection<Notifier> fNotifiers;

	private final ICompareCommandStack fCommandStack;

	public EMFCompareEditingDomain(Notifier left, Notifier right, Notifier ancestor,
			ICompareCommandStack commandStack) {
		if (ancestor == null) {
			fNotifiers = ImmutableList.of(left, right);
		} else {
			fNotifiers = ImmutableList.of(left, right, ancestor);
		}

		fCommandStack = commandStack;

		fChangeRecorder = new ChangeRecorder();
		fChangeRecorder.setResolveProxies(false);
	}

	public static ICompareEditingDomain create(Notifier left, Notifier right, Notifier ancestor) {
		return create(left, right, ancestor, new BasicCommandStack());
	}

	public static ICompareEditingDomain create(Notifier left, Notifier right, Notifier ancestor,
			CommandStack commandStack) {
		return create(left, right, ancestor, commandStack, null);
	}

	public static ICompareEditingDomain create(Notifier left, Notifier right, Notifier ancestor,
			CommandStack leftCommandStack, CommandStack rightCommandStack) {

		final ICompareCommandStack commandStack;

		if (leftCommandStack == null && rightCommandStack != null) {
			commandStack = new CompareCommandStack(rightCommandStack);
		} else if (leftCommandStack != null && rightCommandStack == null) {
			commandStack = new CompareCommandStack(leftCommandStack);
		} else if (leftCommandStack instanceof BasicCommandStack
				&& rightCommandStack instanceof BasicCommandStack) {
			commandStack = new DualCompareCommandStack((BasicCommandStack)leftCommandStack,
					(BasicCommandStack)rightCommandStack);
		} else {
			commandStack = new CompareCommandStack(new BasicCommandStack());
		}

		return new EMFCompareEditingDomain(left, right, ancestor, commandStack);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.domain.ICompareEditingDomain#dispose()
	 */
	public void dispose() {
		fChangeRecorder.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.domain.ICompareEditingDomain#getCommandStack()
	 */
	public ICompareCommandStack getCommandStack() {
		return fCommandStack;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.domain.ICompareEditingDomain#createCopyCommand(org.eclipse.emf.compare.Diff,
	 *      boolean)
	 */
	public Command createCopyCommand(Diff diff, boolean leftToRight) {
		ImmutableSet<Notifier> notifiers = ImmutableSet.<Notifier> builder().add(
				diff.getMatch().getComparison()).addAll(fNotifiers).build();
		return new CopyCommand(fChangeRecorder, notifiers, Collections.singletonList(diff), leftToRight);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.domain.ICompareEditingDomain#createCopyAllNonConflictingCommand(java.util.List,
	 *      boolean)
	 */
	public Command createCopyAllNonConflictingCommand(List<? extends Diff> differences, boolean leftToRight) {
		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		for (Diff diff : differences) {
			notifiersBuilder.add(diff.getMatch().getComparison());
		}
		ImmutableSet<Notifier> notifiers = notifiersBuilder.addAll(fNotifiers).build();

		return new CopyAllNonConflictingCommand(fChangeRecorder, notifiers, differences, leftToRight);
	}

}
