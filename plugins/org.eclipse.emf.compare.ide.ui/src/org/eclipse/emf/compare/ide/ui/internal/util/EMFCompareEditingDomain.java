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

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.commands.CopyAllNonConflictingCommand;
import org.eclipse.emf.compare.commands.CopyCommand;
import org.eclipse.emf.compare.commands.ICompareCopyCommand;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareEditingDomain {

	private final ChangeRecorder fChangeRecorder;

	private final ImmutableCollection<Notifier> fNotifiers;

	private final CompareCommandStack fCommandStack;

	public EMFCompareEditingDomain(Notifier left, Notifier right, Notifier ancestor) {
		this(left, right, ancestor, null);
	}

	public EMFCompareEditingDomain(Notifier left, Notifier right, Notifier ancestor, CommandStack commandStack) {
		if (ancestor == null) {
			fNotifiers = ImmutableList.of(left, right);
		} else {
			fNotifiers = ImmutableList.of(left, right, ancestor);
		}

		if (commandStack == null) {
			fCommandStack = new CompareCommandStack(new BasicCommandStack());
		} else {
			fCommandStack = new CompareCommandStack(commandStack);
		}

		fChangeRecorder = new ChangeRecorder();
		fChangeRecorder.setResolveProxies(false);
	}

	public void dispose() {
		fChangeRecorder.dispose();
	}

	public ICompareCommandStack getCommandStack() {
		return fCommandStack;
	}

	public Command createCopyCommand(Diff diff, boolean leftToRight) {
		ImmutableSet<Notifier> notifiers = ImmutableSet.<Notifier> builder().add(
				diff.getMatch().getComparison()).addAll(fNotifiers).build();
		return new CopyCommand(fChangeRecorder, notifiers, Collections.singletonList(diff), leftToRight);
	}

	public Command createCopyAllNonConflictingCommand(List<? extends Diff> differences, boolean leftToRight) {
		ImmutableSet.Builder<Notifier> notifiersBuilder = ImmutableSet.builder();
		for (Diff diff : differences) {
			notifiersBuilder.add(diff.getMatch().getComparison());
		}
		ImmutableSet<Notifier> notifiers = notifiersBuilder.addAll(fNotifiers).build();

		return new CopyAllNonConflictingCommand(fChangeRecorder, notifiers, differences, leftToRight);
	}

	private static class CompareCommandStack implements ICompareCommandStack {

		private final CompareSideCommandStack rightCommandList;

		private final CompareSideCommandStack leftCommandList;

		private final CommandStack delegate;

		public CompareCommandStack(CommandStack delegate) {
			this.delegate = delegate;
			this.rightCommandList = new CompareSideCommandStack();
			this.leftCommandList = new CompareSideCommandStack();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.BasicCommandStack#execute(org.eclipse.emf.common.command.Command)
		 */
		public void execute(Command command) {
			// should do that AFTER delegate.execute, but in this this case, notifiers will not see change in
			// side lists.
			if (command instanceof ICompareCopyCommand) {
				ICompareCopyCommand compareCommand = (ICompareCopyCommand)command;
				if (compareCommand.isLeftToRight()) {
					rightCommandList.executed(compareCommand);
				} else {
					leftCommandList.executed(compareCommand);
				}
			}
			delegate.execute(command);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.BasicCommandStack#undo()
		 */
		public void undo() {
			// should do that AFTER delegate.execute, but in this this case, notifiers will not see change in
			// side lists.
			if (canUndo()) {
				if (getUndoCommand() instanceof ICompareCopyCommand) {
					ICompareCopyCommand compareCommand = (ICompareCopyCommand)getUndoCommand();
					if (compareCommand.isLeftToRight()) {
						rightCommandList.undone();
					} else {
						leftCommandList.undone();
					}
				}
			}
			delegate.undo();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.BasicCommandStack#redo()
		 */
		public void redo() {
			// should do that AFTER delegate.execute, but in this this case, notifiers will not see change in
			// side lists.
			if (canRedo()) {
				if (getRedoCommand() instanceof ICompareCopyCommand) {
					ICompareCopyCommand compareCommand = (ICompareCopyCommand)getRedoCommand();
					if (compareCommand.isLeftToRight()) {
						rightCommandList.redone();
					} else {
						leftCommandList.redone();
					}
				}
			}
			delegate.redo();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#canUndo()
		 */
		public boolean canUndo() {
			return delegate.canUndo();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#canRedo()
		 */
		public boolean canRedo() {
			return delegate.canRedo();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#getUndoCommand()
		 */
		public Command getUndoCommand() {
			return delegate.getUndoCommand();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#getRedoCommand()
		 */
		public Command getRedoCommand() {
			return delegate.getRedoCommand();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#getMostRecentCommand()
		 */
		public Command getMostRecentCommand() {
			return delegate.getMostRecentCommand();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#flush()
		 */
		public void flush() {
			rightCommandList.flushed();
			leftCommandList.flushed();
			delegate.flush();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#addCommandStackListener(org.eclipse.emf.common.command.CommandStackListener)
		 */
		public void addCommandStackListener(CommandStackListener listener) {
			delegate.addCommandStackListener(listener);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.CommandStack#removeCommandStackListener(org.eclipse.emf.common.command.CommandStackListener)
		 */
		public void removeCommandStackListener(CommandStackListener listener) {
			delegate.removeCommandStackListener(listener);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.util.ICompareCommandStack#isLeftSaveNeeded()
		 */
		public boolean isLeftSaveNeeded() {
			return leftCommandList.isSaveNeeded();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.util.ICompareCommandStack#isRightSaveNeeded()
		 */
		public boolean isRightSaveNeeded() {
			return rightCommandList.isSaveNeeded();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.util.ICompareCommandStack#leftSaveIsDone()
		 */
		public void leftSaveIsDone() {
			leftCommandList.saveIsDone();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.ide.ui.internal.util.ICompareCommandStack#rightSaveIsDone()
		 */
		public void rightSaveIsDone() {
			rightCommandList.saveIsDone();
		}
	}

	private static class CompareSideCommandStack {

		private final List<ICompareCopyCommand> commandList;

		private int top;

		private Command mostRecentCommand;

		private int saveIndex = -1;

		public CompareSideCommandStack() {
			commandList = newArrayList();
			top = -1;
		}

		public void executed(ICompareCopyCommand command) {
			// If the command is executable, record and execute it.
			//
			if (command != null) {
				if (command.canExecute()) {
					// Clear the list past the top.
					//
					Iterator<ICompareCopyCommand> commands = commandList.listIterator(top + 1);
					while (commands.hasNext()) {
						commands.next();
						commands.remove();
					}

					// Record the successfully executed command.
					//
					mostRecentCommand = command;
					commandList.add(command);
					++top;

					// This is kind of tricky.
					// If the saveIndex was in the redo part of the command list which has now been wiped
					// out,
					// then we can never reach a point where a save is not necessary, not even if we undo
					// all the way back to the beginning.
					//
					if (saveIndex >= top) {
						// This forces isSaveNeded to always be true.
						//
						saveIndex = -2;
					}
				}
			}
		}

		public void undone() {
			Command command = commandList.get(top--);
			mostRecentCommand = command;
		}

		public void redone() {
			Command command = commandList.get(++top);
			mostRecentCommand = command;
		}

		public void flushed() {
			// Clear the list.
			//
			Iterator<ICompareCopyCommand> commands = commandList.listIterator();
			while (commands.hasNext()) {
				commands.next();
				commands.remove();
			}
			commandList.clear();
			top = -1;
			saveIndex = -1;
			mostRecentCommand = null;
		}

		/**
		 * Called after a save has been successfully performed.
		 */
		public void saveIsDone() {
			// Remember where we are now.
			//
			saveIndex = top;
		}

		/**
		 * Returns whether the model has changes since {@link #saveIsDone} was call the last.
		 * 
		 * @return whether the model has changes since <code>saveIsDone</code> was call the last.
		 */
		public boolean isSaveNeeded() {
			// Only if we are at the remembered index do we NOT need to save.
			//
			// return top != saveIndex;

			if (saveIndex < -1) {
				return true;
			}

			if (top > saveIndex) {
				for (int i = top; i > saveIndex; --i) {
					if (!(commandList.get(i) instanceof AbstractCommand.NonDirtying)) {
						return true;
					}
				}
			} else {
				for (int i = saveIndex; i > top; --i) {
					if (!(commandList.get(i) instanceof AbstractCommand.NonDirtying)) {
						return true;
					}
				}
			}

			return false;
		}

		public Command getUndoCommand() {
			return top == -1 || top == commandList.size() ? null : (Command)commandList.get(top);
		}

		public Command getRedoCommand() {
			return top + 1 >= commandList.size() ? null : (Command)commandList.get(top + 1);
		}

		public Command getMostRecentCommand() {
			return mostRecentCommand;
		}
	}

}
