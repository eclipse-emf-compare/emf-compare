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
public abstract class DelegatingCommandStack implements CommandStack {
	// CHECKSTYLE:ON

	/**
	 * Returns the backing delegate instance that methods are forwarded to.
	 * 
	 * @return the backing delegate instance that methods are forwarded to.
	 */
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
		delegate().addCommandStackListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.command.CommandStack#removeCommandStackListener(org.eclipse.emf.common.command.CommandStackListener)
	 */
	public void removeCommandStackListener(CommandStackListener listener) {
		delegate().removeCommandStackListener(listener);
	}

}
