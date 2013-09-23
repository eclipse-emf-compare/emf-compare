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

/**
 * {@link ICompareCommandStack} implementation that will delegates to two given command stacks; one for each
 * side of the comparison.
 * <p>
 * This implementation is not robust. If an error occurs during execution of a command, the whole state will
 * be corrupted and the undo/redo may have an unknown behavior.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DualCompareCommandStack implements ICompareCommandStack {

	/**
	 * This value forces isSaveNeded to always be true.
	 */
	private static final int IS_SAVE_NEEDED_WILL_BE_TRUE = -2;

	/** The left command stack. */
	private final BasicCommandStack leftCommandStack;

	/** The right command stack. */
	private final BasicCommandStack rightCommandStack;

	/**
	 * The list of command stack; it will record the stack of which command stack has been used to execute the
	 * commands.
	 */
	private final List<BasicCommandStack> commandStackStack;

	/**
	 * The current position within the list from which the next execute, undo, or redo, will be performed.
	 */
	private int top;

	/**
	 * The command stack on which a command has been most recently executed, undone, or redone.
	 */
	private BasicCommandStack mostRecentCommandStack;

	/**
	 * The value of {@link #top} when {@link #saveIsDone} is called.
	 */
	private int saveIndex = -1;

	/**
	 * Creates an instance that delegates to two given {@link BasicCommandStack}.
	 * 
	 * @param leftCommandStack
	 *            the left command stack.
	 * @param rightCommandStack
	 *            the right command stack.
	 */
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
				saveIndex = IS_SAVE_NEEDED_WILL_BE_TRUE;
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
		final Command undoCommand;
		if (top == -1 || top == commandStackStack.size()) {
			undoCommand = null;
		} else {
			undoCommand = commandStackStack.get(top).getUndoCommand();
		}
		return undoCommand;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#getRedoCommand()
	 */
	public Command getRedoCommand() {
		final Command redoCommand;
		if (top + 1 >= commandStackStack.size()) {
			redoCommand = null;
		} else {
			redoCommand = commandStackStack.get(top + 1).getRedoCommand();
		}
		return redoCommand;
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
