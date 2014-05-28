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
package org.eclipse.emf.compare.command;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.ForwardingObject;

import java.util.EventObject;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;

/**
 * Abstract implementation that forward method calls to a {@link #delegate() delegatating}
 * {@link CommandStack}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
// CHECKSTYLE:OFF
public abstract class DelegatingCommandStack extends ForwardingObject implements CommandStack {
	// CHECKSTYLE:ON

	/** Holds the list of listeners. */
	private final List<CommandStackListener> listeners;

	/**
	 * Default constructor.
	 */
	public DelegatingCommandStack() {
		this.listeners = newArrayList();
	}

	/**
	 * Returns the backing delegate instance that methods are forwarded to.
	 * 
	 * @return the backing delegate instance that methods are forwarded to.
	 */
	@Override
	protected abstract CommandStack delegate();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#execute(org.eclipse.emf.common.command.Command)
	 */
	public void execute(Command command) {
		delegate().execute(command);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#canUndo()
	 */
	public boolean canUndo() {
		return delegate().canUndo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#undo()
	 */
	public void undo() {
		delegate().undo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#canRedo()
	 */
	public boolean canRedo() {
		return delegate().canRedo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#getUndoCommand()
	 */
	public Command getUndoCommand() {
		return delegate().getUndoCommand();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#getRedoCommand()
	 */
	public Command getRedoCommand() {
		return delegate().getRedoCommand();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#getMostRecentCommand()
	 */
	public Command getMostRecentCommand() {
		return delegate().getMostRecentCommand();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#redo()
	 */
	public void redo() {
		delegate().redo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#flush()
	 */
	public void flush() {
		delegate().flush();
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
	 * This is called to ensure that {@link CommandStackListener#commandStackChanged} is called for each
	 * listener.
	 */
	protected void notifyListeners() {
		for (CommandStackListener commandStackListener : listeners) {
			commandStackListener.commandStackChanged(new EventObject(this));
		}
	}

}
