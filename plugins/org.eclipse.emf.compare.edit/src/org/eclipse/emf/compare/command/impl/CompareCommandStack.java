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
import org.eclipse.emf.compare.command.DelagatingCommandStack;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;

public class CompareCommandStack extends DelagatingCommandStack implements ICompareCommandStack {

	private final CompareSideCommandStack rightCommandStack;

	private final CompareSideCommandStack leftCommandStack;

	private final CommandStack delegate;

	public CompareCommandStack(CommandStack delegate) {
		this.delegate = delegate;
		this.rightCommandStack = new CompareSideCommandStack();
		this.leftCommandStack = new CompareSideCommandStack();
	}

	/**
	 * @return the delegate
	 */
	@Override
	protected CommandStack delegate() {
		return delegate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.command.DelagatingCommandStack#execute(org.eclipse.emf.common.command.Command)
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

	public static class CompareSideCommandStack {

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
