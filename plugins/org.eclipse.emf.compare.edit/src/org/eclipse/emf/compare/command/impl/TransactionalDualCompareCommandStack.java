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
package org.eclipse.emf.compare.command.impl;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;

import java.util.EventObject;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.transaction.ExceptionHandler;
import org.eclipse.emf.transaction.impl.AbstractTransactionalCommandStack;

/**
 * {@link ICompareCommandStack} implementation that will delegate to two given command stacks; one for each
 * side of the comparison.
 * <p>
 * This implementation is one of the most robust delegating implementation we can do. If an error occurs
 * during execution of a command, only the dirty state will be reset, all models will stay correct.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TransactionalDualCompareCommandStack implements ICompareCommandStack, IDisposable {

	/**
	 * This value forces isSaveNeded to always be true.
	 */
	private static final int IS_SAVE_NEEDED_WILL_BE_TRUE = -2;

	/** The left command stack. */
	private final AbstractTransactionalCommandStack leftCommandStack;

	/** The right command stack. */
	private final AbstractTransactionalCommandStack rightCommandStack;

	/**
	 * The list of command stacks; it will record the stack of which command stack has been used to execute
	 * the commands.
	 */
	private final List<AbstractTransactionalCommandStack> commandStackStack;

	/**
	 * The current position within the list from which the next execute, undo, or redo, will be performed.
	 */
	private int top;

	/**
	 * The command stack on which a command has been most recently executed, undone, or redone.
	 */
	private AbstractTransactionalCommandStack mostRecentCommandStack;

	/**
	 * The value of {@link #top} when {@link #saveIsDone} is called.
	 */
	private int saveIndex = -1;

	/** The listeners of this DualCompareCommandStack. */
	private final List<CommandStackListener> listeners;

	/** The listener of the wrapped command stacks. */
	private final CommandStackListener sideCommandStackListener;

	/**
	 * Creates an instance that delegates to two given {@link AbstractTransactionalCommandStack}.
	 * 
	 * @param leftCommandStack
	 *            the left command stack.
	 * @param rightCommandStack
	 *            the right command stack.
	 */
	public TransactionalDualCompareCommandStack(AbstractTransactionalCommandStack leftCommandStack,
			AbstractTransactionalCommandStack rightCommandStack) {
		this.leftCommandStack = Preconditions.checkNotNull(leftCommandStack);
		this.rightCommandStack = Preconditions.checkNotNull(rightCommandStack);
		this.sideCommandStackListener = new CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				notifyListeners(event.getSource());
			}
		};
		this.leftCommandStack.addCommandStackListener(sideCommandStackListener);
		this.rightCommandStack.addCommandStackListener(sideCommandStackListener);
		this.listeners = newArrayList();
		this.commandStackStack = newArrayList();
		this.top = -1;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IDisposable#dispose()
	 */
	public void dispose() {
		leftCommandStack.removeCommandStackListener(this.sideCommandStackListener);
		rightCommandStack.removeCommandStackListener(this.sideCommandStackListener);
	}

	/**
	 * This is called to ensure that {@link CommandStackListener#commandStackChanged} is called for each
	 * listener.
	 * 
	 * @param source
	 *            the source of the event.
	 */
	protected void notifyListeners(Object source) {
		for (CommandStackListener commandStackListener : listeners) {
			commandStackListener.commandStackChanged(new EventObject(source));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#execute(org.eclipse.emf.common.command.Command)
	 */
	public void execute(Command command) {
		if (command instanceof ICompareCopyCommand) {
			if (command.canExecute()) {
				final ICompareCopyCommand compareCommand = (ICompareCopyCommand)command;
				final AbstractTransactionalCommandStack commandStack;
				if (compareCommand.isLeftToRight()) {
					commandStack = rightCommandStack;
				} else {
					commandStack = leftCommandStack;
				}

				ExceptionHandler oldExceptionHandler = commandStack.getExceptionHandler();
				AbstractCompletionHandler completionHandler = new ExecuteCompletionHandler();
				commandStack.setExceptionHandler(completionHandler);

				commandStack.execute(compareCommand);

				if (!completionHandler.hadException()) {
					completionHandler.handleSuccessfulCompletion(commandStack);
				}

				commandStack.setExceptionHandler(oldExceptionHandler);

				notifyListeners(this);
			}
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
			AbstractTransactionalCommandStack commandStack = commandStackStack.get(top--);

			ExceptionHandler oldExceptionHandler = commandStack.getExceptionHandler();
			AbstractCompletionHandler completionHandler = new UndoCompletionHandler();
			commandStack.setExceptionHandler(completionHandler);

			commandStack.undo();

			if (!completionHandler.hadException()) {
				completionHandler.handleSuccessfulCompletion(commandStack);
			}

			commandStack.setExceptionHandler(oldExceptionHandler);

			notifyListeners(this);
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
			AbstractTransactionalCommandStack commandStack = commandStackStack.get(++top);

			ExceptionHandler oldExceptionHandler = commandStack.getExceptionHandler();
			AbstractCompletionHandler completionHandler = new RedoCompletionHandler();
			commandStack.setExceptionHandler(completionHandler);

			commandStack.redo();

			if (!completionHandler.hadException()) {
				completionHandler.handleSuccessfulCompletion(commandStack);
			}

			commandStack.setExceptionHandler(oldExceptionHandler);

			notifyListeners(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#flush()
	 */
	public void flush() {
		commandStackStack.clear();
		top = -1;
		saveIndex = -1;
		mostRecentCommandStack = null;

		notifyListeners(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#addCommandStackListener(org.eclipse.emf.common.command.CommandStackListener)
	 */
	public void addCommandStackListener(CommandStackListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#removeCommandStackListener(org.eclipse.emf.common.command.CommandStackListener)
	 */
	public void removeCommandStackListener(CommandStackListener listener) {
		this.listeners.remove(listener);
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
	 * An exception handler that will keep in memory if an exception happened during the execution of a
	 * command.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private abstract static class AbstractCompletionHandler implements ExceptionHandler {

		/** Holds the fact that an exception happened during the execution. */
		private boolean hadException;

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.transaction.ExceptionHandler#handleException(java.lang.Exception)
		 */
		public void handleException(Exception e) {
			hadException = true;
		}

		/**
		 * Executes whatever needed on a successful (i.e. without exception) execution.
		 * 
		 * @param commandStack
		 *            the command stack on which the command has been successfully excuted.
		 */
		abstract void handleSuccessfulCompletion(AbstractTransactionalCommandStack commandStack);

		/**
		 * Returns true if an exception occured during the execution of a command, false otherwise.
		 * 
		 * @return true if an exception occured during the execution of a command, false otherwise.
		 */
		boolean hadException() {
			return hadException;
		}

	}

	/**
	 * Completion handler of a {@link org.eclipse.emf.common.command.CommandStack#redo() redo} operation.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class RedoCompletionHandler extends AbstractCompletionHandler {

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.transaction.ExceptionHandler#handleException(java.lang.Exception)
		 */
		@Override
		public void handleException(Exception e) {
			if (!hadException()) { // avoid to handle exception multiple times.
				super.handleException(e);
				mostRecentCommandStack = null;

				// Clear the list past the top.
				commandStackStack.subList(top--, commandStackStack.size()).clear();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.command.impl.TransactionalDualCompareCommandStack.AbstractCompletionHandler#handleSuccessfulCompletion(org.eclipse.emf.transaction.impl.AbstractTransactionalCommandStack)
		 */
		@Override
		public void handleSuccessfulCompletion(AbstractTransactionalCommandStack commandStack) {
			mostRecentCommandStack = commandStack;
		}
	}

	/**
	 * Completion handler of an {@link org.eclipse.emf.common.command.CommandStack#undo() undo} operation.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class UndoCompletionHandler extends AbstractCompletionHandler {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.transaction.ExceptionHandler#handleException(java.lang.Exception)
		 */
		@Override
		public void handleException(Exception e) {
			if (!hadException()) { // avoid to handle exception multiple times.
				super.handleException(e);
				mostRecentCommandStack = null;
				flush();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.command.impl.TransactionalDualCompareCommandStack.AbstractCompletionHandler#handleSuccessfulCompletion(org.eclipse.emf.transaction.impl.AbstractTransactionalCommandStack)
		 */
		@Override
		public void handleSuccessfulCompletion(AbstractTransactionalCommandStack commandStack) {
			mostRecentCommandStack = commandStack;
		}
	}

	/**
	 * Completion handler of an {@link org.eclipse.emf.common.command.CommandStack#execute(Command) execute}
	 * operation.
	 */
	private final class ExecuteCompletionHandler extends AbstractCompletionHandler {

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.command.impl.TransactionalDualCompareCommandStack.AbstractCompletionHandler#handleException(java.lang.Exception)
		 */
		@Override
		public void handleException(Exception e) {
			if (!hadException()) { // avoid to handle exception multiple times.
				super.handleException(e);
				mostRecentCommandStack = null;
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.command.impl.TransactionalDualCompareCommandStack.AbstractCompletionHandler#handleSuccessfulCompletion(org.eclipse.emf.transaction.impl.AbstractTransactionalCommandStack)
		 */
		@Override
		void handleSuccessfulCompletion(AbstractTransactionalCommandStack commandStack) {
			// Clear the list past the top.
			commandStackStack.subList(top + 1, commandStackStack.size()).clear();

			// Record the successfully executed command.
			mostRecentCommandStack = commandStack;
			commandStackStack.add(commandStack);
			++top;

			// This is kind of tricky. If the saveIndex was in the redo part of the command list which has now
			// been wiped out, then we can never reach a point where a save is not necessary, not even if we
			// undo all the way back to the beginning.
			if (saveIndex >= top) {
				saveIndex = IS_SAVE_NEEDED_WILL_BE_TRUE;
			}
		}
	}
}
