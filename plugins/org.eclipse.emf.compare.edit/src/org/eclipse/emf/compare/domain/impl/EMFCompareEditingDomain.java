/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
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

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.command.impl.CompareCommandStack;
import org.eclipse.emf.compare.command.impl.DualCompareCommandStack;
import org.eclipse.emf.compare.command.impl.MergeCommand;
import org.eclipse.emf.compare.command.impl.TransactionalDualCompareCommandStack;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.IMergeRunnable;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IBatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.provider.EMFCompareEditPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.AbstractTransactionalCommandStack;
import org.eclipse.emf.transaction.util.TransactionUtil;

/**
 * Default implementation that use a change recorder in the background to record the changes made by executed
 * commands.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareEditingDomain implements ICompareEditingDomain {

	/** The change recorder instance. */
	private final ChangeRecorder fChangeRecorder;

	/** The notifiers on which the change recorder will be installed. */
	private final ImmutableCollection<Notifier> fNotifiers;

	/** The command stack on which the merge commands will be executed. */
	private final ICompareCommandStack fCommandStack;

	/**
	 * Creates a new instance with the given notifiers to be listen to when something will be changed.
	 * 
	 * @param left
	 *            the left root notifier of the comparison (i.e. the
	 *            {@link org.eclipse.emf.compare.scope.IComparisonScope#getLeft()}
	 * @param right
	 *            the right root notifier of the comparison (i.e. the
	 *            {@link org.eclipse.emf.compare.scope.IComparisonScope#getRight()}
	 * @param ancestor
	 *            the ancestor root notifier of the comparison (i.e. the
	 *            {@link org.eclipse.emf.compare.scope.IComparisonScope#getOrigin()}
	 * @param commandStack
	 *            the command stack to be used to track execution of commands.
	 */
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

	/**
	 * Returns the resource set containing the given notifier, the given {@code notifier} if it is a
	 * {@link ResourceSet}, <code>null</code> otherwise.
	 * 
	 * @param notifier
	 *            the notifier from which we have to look for a {@link ResourceSet}.
	 * @return the resource set containing the given notifier, the given {@code notifier} if it is a
	 *         {@link ResourceSet}, <code>null</code> otherwise
	 */
	private static ResourceSet getResourceSet(Notifier notifier) {
		ResourceSet resourceSet = null;
		if (notifier instanceof ResourceSet) {
			resourceSet = (ResourceSet)notifier;
		} else if (notifier instanceof Resource) {
			resourceSet = ((Resource)notifier).getResourceSet();
		} else if (notifier instanceof EObject) {
			Resource eResource = ((EObject)notifier).eResource();
			if (eResource != null) {
				resourceSet = eResource.getResourceSet();
			}
		} else {
			// impossible as of today
		}
		return resourceSet;
	}

	/**
	 * Creates a new compare editing domain on the given notifier with an appropriate
	 * {@link ICompareCommandStack} set up on it.
	 * 
	 * @param left
	 *            the left notifier. Should not be <code>null</code>.
	 * @param right
	 *            the right notifier. Should not be <code>null</code>.
	 * @param ancestor
	 *            the ancestor notifier. May be <code>null</code>.
	 * @return a new compare editing domain on the given notifier.
	 */
	public static ICompareEditingDomain create(Notifier left, Notifier right, Notifier ancestor) {
		EditingDomain leftED = getEditingDomain(left);
		EditingDomain rightED = getEditingDomain(right);

		if (leftED != null && rightED != null) {
			CommandStack leftCommandStack = leftED.getCommandStack();
			CommandStack rightCommandStack = rightED.getCommandStack();
			ICompareCommandStack commandStack;
			if (leftCommandStack instanceof AbstractTransactionalCommandStack
					&& rightCommandStack instanceof AbstractTransactionalCommandStack) {
				commandStack = new TransactionalDualCompareCommandStack(
						(AbstractTransactionalCommandStack)leftCommandStack,
						(AbstractTransactionalCommandStack)rightCommandStack);
			} else if (leftCommandStack instanceof BasicCommandStack
					&& rightCommandStack instanceof BasicCommandStack) {
				commandStack = new DualCompareCommandStack((BasicCommandStack)leftCommandStack,
						(BasicCommandStack)rightCommandStack);
			} else {
				EMFCompareEditPlugin
						.getPlugin()
						.getLog()
						.log(new Status(
								IStatus.WARNING,
								EMFCompareEditPlugin.PLUGIN_ID,
								"Command stacks of the editing domain of " //$NON-NLS-1$
										+ left
										+ " and " //$NON-NLS-1$
										+ right
										+ " are not instances of BasicCommandStack, nor AbstractTransactionalCommandStack, therefore, they will not be used as backing command stacks for the current merge session.")); //$NON-NLS-1$
				commandStack = new CompareCommandStack(new BasicCommandStack());
			}
			return new EMFCompareEditingDomain(left, right, ancestor, commandStack);
		}

		return create(left, right, ancestor, new BasicCommandStack());
	}

	/**
	 * Returns an editing domain associated with the given {@link Notifier}. It will first look for a
	 * {@link TransactionalEditingDomain} then for an {@link AdapterFactoryEditingDomain}. It neither is found
	 * a new {@link TransactionalEditingDomain} is created.
	 * 
	 * @param notifier
	 *            the notifier from which the editing domain has to be linked.
	 * @return an editing domain associated with the given {@link Notifier}
	 */
	private static EditingDomain getEditingDomain(Notifier notifier) {
		EditingDomain editingDomain = TransactionUtil.getEditingDomain(notifier);
		if (editingDomain == null) {
			editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(notifier);
			if (editingDomain == null) {
				ResourceSet resourceSet = getResourceSet(notifier);
				if (resourceSet != null) {
					editingDomain = TransactionalEditingDomain.Factory.INSTANCE
							.createEditingDomain(resourceSet);
				}
			}
		}

		return editingDomain;
	}

	/**
	 * Equivalent to {@code create(left, right, ancestor, commandStack, null)}.
	 * 
	 * @param left
	 *            the left notifier. Should not be <code>null</code>.
	 * @param right
	 *            the right notifier. Should not be <code>null</code>.
	 * @param ancestor
	 *            the ancestor notifier. May be <code>null</code>.
	 * @param commandStack
	 *            a command stack to which merge command will be delegated to.
	 * @return a newly created compare editing domain.
	 */
	public static ICompareEditingDomain create(Notifier left, Notifier right, Notifier ancestor,
			CommandStack commandStack) {
		return create(left, right, ancestor, commandStack, null);
	}

	/**
	 * Creates a new compare editing domain on the given notifier with an appropriate
	 * {@link ICompareCommandStack} set up on it.
	 * 
	 * @param left
	 *            the left notifier. Should not be <code>null</code>.
	 * @param right
	 *            the right notifier. Should not be <code>null</code>.
	 * @param ancestor
	 *            the ancestor notifier. May be <code>null</code>.
	 * @param leftCommandStack
	 *            a command stack to which merge to left command will be delegated to.
	 * @param rightCommandStack
	 *            a command stack to which merge to irght command will be delegated to.
	 * @return a newly created compare editing domain.
	 */
	public static ICompareEditingDomain create(Notifier left, Notifier right, Notifier ancestor,
			CommandStack leftCommandStack, CommandStack rightCommandStack) {

		final ICompareCommandStack commandStack;

		if (leftCommandStack == null && rightCommandStack != null) {
			if (rightCommandStack instanceof ICompareCommandStack) {
				commandStack = (ICompareCommandStack)rightCommandStack;
			} else {
				commandStack = new CompareCommandStack(rightCommandStack);
			}
		} else if (leftCommandStack != null && rightCommandStack == null) {
			if (leftCommandStack instanceof ICompareCommandStack) {
				commandStack = (ICompareCommandStack)leftCommandStack;
			} else {
				commandStack = new CompareCommandStack(leftCommandStack);
			}
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
	 *      boolean, org.eclipse.emf.compare.merge.IMerger.Registry)
	 * @since 3.0
	 */
	public Command createCopyCommand(List<? extends Diff> differences, boolean leftToRight,
			IMerger.Registry mergerRegistry) {
		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		for (Diff diff : differences) {
			notifiersBuilder.add(diff.getMatch().getComparison());
		}
		ImmutableSet<Notifier> notifiers = notifiersBuilder.addAll(fNotifiers).build();

		IMergeRunnable runnable = new IMergeRunnable() {
			public void merge(List<? extends Diff> diffs, boolean lTR, Registry registry) {
				final IBatchMerger merger = new BatchMerger(registry);
				if (lTR) {
					merger.copyAllLeftToRight(diffs, new BasicMonitor());
				} else {
					merger.copyAllRightToLeft(diffs, new BasicMonitor());
				}
			}
		};
		return new MergeCommand(fChangeRecorder, notifiers, differences, leftToRight, mergerRegistry,
				runnable);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.domain.ICompareEditingDomain#createCopyCommand(java.util.List, boolean,
	 *      org.eclipse.emf.compare.merge.IMerger.Registry,
	 *      org.eclipse.emf.compare.command.ICompareCopyCommand.IMergeRunnable)
	 */
	public ICompareCopyCommand createCopyCommand(List<? extends Diff> differences, boolean leftToRight,
			Registry mergerRegistry, IMergeRunnable runnable) {
		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		for (Diff diff : differences) {
			notifiersBuilder.add(diff.getMatch().getComparison());
		}
		ImmutableSet<Notifier> notifiers = notifiersBuilder.addAll(fNotifiers).build();

		return new MergeCommand(fChangeRecorder, notifiers, differences, leftToRight, mergerRegistry,
				runnable);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.domain.ICompareEditingDomain#getChangeRecorder()
	 */
	public ChangeRecorder getChangeRecorder() {
		return fChangeRecorder;
	}

}
