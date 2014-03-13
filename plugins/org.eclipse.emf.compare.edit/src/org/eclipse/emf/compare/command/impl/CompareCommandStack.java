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
package org.eclipse.emf.compare.command.impl;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.compare.command.DelegatingCommandStack;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;

/**
 * A simple {@link ICompareCommandStack} that delegate execution to another command stack but keep
 * informations about execution to properly reply to {@link ICompareCommandStack} protocol.
 * <p>
 * This implementation is not robust. If an error occurs during execution of a command, the whole state will
 * be corrupted and the undo/redo may have an unknown behavior.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareCommandStack extends DelegatingCommandStack implements ICompareCommandStack {

	/** The data structure to keep info of command executed on the right side. */
	private final CompareSideCommandStack rightCommandStack;

	/** The data structure to keep info of command executed on the left side. */
	private final CompareSideCommandStack leftCommandStack;

	/** The command to which we delegate to. */
	private final CommandStack delegate;

	/**
	 * Creates a new instance that delegates to the given {@code commandStack}.
	 * 
	 * @param commandStack
	 *            the command stack to which this instance will delegate.
	 */
	public CompareCommandStack(CommandStack commandStack) {
		this.delegate = commandStack;
		this.rightCommandStack = new CompareSideCommandStack();
		this.leftCommandStack = new CompareSideCommandStack();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.DelegatingCommandStack#delegate()
	 */
	@Override
	protected CommandStack delegate() {
		return delegate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.DelegatingCommandStack#execute(org.eclipse.emf.common.command.Command)
	 */
	@Override
	public void execute(Command command) {
		// should do that AFTER delegate.execute, but in this this case, notifiers will not see change in
		// side lists.
		if (command instanceof ICompareCopyCommand) {
			ICompareCopyCommand compareCommand = (ICompareCopyCommand)command;
			if (compareCommand.isLeftToRight()) {
				rightCommandStack.executed(compareCommand);
			} else {
				leftCommandStack.executed(compareCommand);
			}
		}
		super.execute(command);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.BasicCommandStack#undo()
	 */
	@Override
	public void undo() {
		// should do that AFTER delegate.execute, but in this this case, notifiers will not see change in
		// side lists.
		if (canUndo()) {
			if (getUndoCommand() instanceof ICompareCopyCommand) {
				ICompareCopyCommand compareCommand = (ICompareCopyCommand)getUndoCommand();
				if (compareCommand.isLeftToRight()) {
					rightCommandStack.undone();
				} else {
					leftCommandStack.undone();
				}
			}
		}
		super.undo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.BasicCommandStack#redo()
	 */
	@Override
	public void redo() {
		// should do that AFTER delegate.execute, but in this this case, notifiers will not see change in
		// side lists.
		if (canRedo()) {
			if (getRedoCommand() instanceof ICompareCopyCommand) {
				ICompareCopyCommand compareCommand = (ICompareCopyCommand)getRedoCommand();
				if (compareCommand.isLeftToRight()) {
					rightCommandStack.redone();
				} else {
					leftCommandStack.redone();
				}
			}
		}
		super.redo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#flush()
	 */
	@Override
	public void flush() {
		rightCommandStack.flushed();
		leftCommandStack.flushed();
		super.flush();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.ICompareCommandStack#isLeftSaveNeeded()
	 */
	public boolean isLeftSaveNeeded() {
		return leftCommandStack.isSaveNeeded();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.ICompareCommandStack#isRightSaveNeeded()
	 */
	public boolean isRightSaveNeeded() {
		return rightCommandStack.isSaveNeeded();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.ICompareCommandStack#leftSaveIsDone()
	 */
	public void leftSaveIsDone() {
		leftCommandStack.saveIsDone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.ICompareCommandStack#rightSaveIsDone()
	 */
	public void rightSaveIsDone() {
		rightCommandStack.saveIsDone();
	}

	/**
	 * Simple data structure acting like a command stack but without any execution capability. It is used to
	 * record execution of {@link ICompareCopyCommand} on each side.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class CompareSideCommandStack {

		/**
		 * This will force the {@link #isSaveNeeded()} to return <code>true</code>.
		 */
		private static final int IS_SAVE_NEEDED_WILL_BE_TRUE = -2;

		/**
		 * The list of commands.
		 */
		private final List<ICompareCopyCommand> commandList;

		/**
		 * The current position within the list from which the next execute, undo, or redo, will be performed.
		 */
		private int top;

		/**
		 * The command most recently executed, undone, or redone.
		 */
		private Command mostRecentCommand;

		/**
		 * The value of {@link #top} when {@link #saveIsDone} is called.
		 */
		private int saveIndex = -1;

		/**
		 * Creates a new empty instance.
		 */
		public CompareSideCommandStack() {
			commandList = newArrayList();
			top = -1;
		}

		/**
		 * Record the execution of the given command.
		 * 
		 * @param command
		 *            the command to record.
		 */
		public void executed(ICompareCopyCommand command) {
			// If the command is executable, record it.
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
						saveIndex = IS_SAVE_NEEDED_WILL_BE_TRUE;
					}
				}
			}
		}

		/**
		 * Record that the top of the command list has been undone.
		 */
		public void undone() {
			Command command = commandList.get(top--);
			mostRecentCommand = command;
		}

		/**
		 * Record that the top of the command list has been redone.
		 */
		public void redone() {
			Command command = commandList.get(++top);
			mostRecentCommand = command;
		}

		/**
		 * Disposes all the commands in the stack.
		 */
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
			boolean ret = false;

			if (saveIndex < -1) {
				ret = true;
			}

			if (!ret) {
				if (top > saveIndex) {
					for (int i = top; !ret && i > saveIndex; --i) {
						if (!(commandList.get(i) instanceof AbstractCommand.NonDirtying)) {
							ret = true;
						}
					}
				} else {
					for (int i = saveIndex; !ret && i > top; --i) {
						if (!(commandList.get(i) instanceof AbstractCommand.NonDirtying)) {
							ret = true;
						}
					}
				}
			}

			return ret;
		}

		/**
		 * Returns the command that will be undone if {@link #undo} is called.
		 * 
		 * @return the command that will be undone if {@link #undo} is called.
		 */
		public Command getUndoCommand() {
			final Command undoCommand;
			if (top == -1 || top == commandList.size()) {
				undoCommand = null;
			} else {
				undoCommand = commandList.get(top);
			}
			return undoCommand;
		}

		/**
		 * Returns the command that will be redone if {@link #redo} is called.
		 * 
		 * @return the command that will be redone if {@link #redo} is called.
		 */
		public Command getRedoCommand() {
			final Command redoCommand;
			if (top + 1 >= commandList.size()) {
				redoCommand = null;
			} else {
				redoCommand = commandList.get(top + 1);
			}
			return redoCommand;
		}

		/**
		 * Returns the command most recently executed, undone, or redone.
		 * 
		 * @return the command most recently executed, undone, or redone.
		 */
		public Command getMostRecentCommand() {
			return mostRecentCommand;
		}
	}
}
