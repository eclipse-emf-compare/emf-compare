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

import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;

public class DualCompareCommandStack implements ICompareCommandStack {

	private final BasicCommandStack leftCommandStack;

	private final BasicCommandStack rightCommandStack;

	private final List<BasicCommandStack> commandStackStack;

	private int top;

	private BasicCommandStack mostRecentCommandStack;

	private int saveIndex = -1;

	public DualCompareCommandStack(BasicCommandStack leftCommandStack, BasicCommandStack rightCommandStack) {
		this.leftCommandStack = Preconditions.checkNotNull(leftCommandStack);
		this.rightCommandStack = Preconditions.checkNotNull(rightCommandStack);
		this.commandStackStack = newArrayList();
		this.top = -1;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#execute(org.eclipse.emf.common.command.Command)
	 */
	public void execute(Command command) {
		// should do that AFTER delegate.execute, but in this this case, notifiers will not see change in
		// side lists.
		if (command instanceof ICompareCopyCommand) {
			final BasicCommandStack commandStack;
			final ICompareCopyCommand compareCommand = (ICompareCopyCommand)command;
			if (compareCommand.isLeftToRight()) {
				commandStack = rightCommandStack;
			} else {
				commandStack = leftCommandStack;
			}

			// Clear the list past the top.
			//
			Iterator<BasicCommandStack> commandStacks = commandStackStack.listIterator(top + 1);
			while (commandStacks.hasNext()) {
				commandStacks.next();
				commandStacks.remove();
			}

			// Record the successfully executed command.
			//
			mostRecentCommandStack = commandStack;
			commandStackStack.add(commandStack);
			++top;

			// This is kind of tricky.
			// If the saveIndex was in the redo part of the command list which has now been wiped out,
			// then we can never reach a point where a save is not necessary, not even if we undo all the
			// way back to the beginning.
			//
			if (saveIndex >= top) {
				// This forces isSaveNeded to always be true.
				//
				saveIndex = -2;
			}

			commandStack.execute(compareCommand);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#canUndo()
	 */
	public boolean canUndo() {
		return top != -1 && commandStackStack.get(top).canUndo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#undo()
	 */
	public void undo() {
		if (canUndo()) {
			BasicCommandStack commandStack = commandStackStack.get(top--);
			commandStack.undo();
			mostRecentCommandStack = commandStack;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#canRedo()
	 */
	public boolean canRedo() {
		return top < commandStackStack.size() - 1;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#getUndoCommand()
	 */
	public Command getUndoCommand() {
		return top == -1 || top == commandStackStack.size() ? null : commandStackStack.get(top)
				.getUndoCommand();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#getRedoCommand()
	 */
	public Command getRedoCommand() {
		return top + 1 >= commandStackStack.size() ? null : commandStackStack.get(top + 1)
				.getRedoCommand();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#getMostRecentCommand()
	 */
	public Command getMostRecentCommand() {
		if (mostRecentCommandStack != null) {
			return mostRecentCommandStack.getMostRecentCommand();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#redo()
	 */
	public void redo() {
		if (canRedo()) {
			BasicCommandStack commandStack = commandStackStack.get(++top);
			commandStack.redo();
			mostRecentCommandStack = commandStack;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#flush()
	 */
	public void flush() {
		Iterator<BasicCommandStack> commands = commandStackStack.listIterator();
		while (commands.hasNext()) {
			commands.next();
			commands.remove();
		}
		commandStackStack.clear();
		top = -1;
		saveIndex = -1;
		mostRecentCommandStack = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#addCommandStackListener(org.eclipse.emf.common.command.CommandStackListener)
	 */
	public void addCommandStackListener(CommandStackListener listener) {
		leftCommandStack.addCommandStackListener(listener);
		rightCommandStack.addCommandStackListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#removeCommandStackListener(org.eclipse.emf.common.command.CommandStackListener)
	 */
	public void removeCommandStackListener(CommandStackListener listener) {
		leftCommandStack.removeCommandStackListener(listener);
		rightCommandStack.removeCommandStackListener(listener);
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

}