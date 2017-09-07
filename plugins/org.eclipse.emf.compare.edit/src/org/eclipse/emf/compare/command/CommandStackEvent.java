/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services Gmbh.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.command;

import java.util.EventObject;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;

/**
 * An event that can be passed to {@link CommandStackListener#commandStackChanged(EventObject)}.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class CommandStackEvent extends EventObject {
	/**
	 * Just to keep the compiler happy.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The operation of this event.
	 */
	private Operation operation;

	/**
	 * An event type.
	 */
	public enum Operation {
		/**
		 * The value indicating that {@link CommandStack#execute(Command)} was called.
		 */
		EXECUTE,
		/**
		 * the value indicating that {@link CommandStack#undo()} was called.
		 */
		UNDO,
		/**
		 * The value indicating that {@link CommandStack#redo()} was called.
		 */
		REDO,
		/**
		 * The value indicating that {@link CommandStackt#flush} was called.
		 */
		FLUSH
	}

	/**
	 * Constructor.
	 * 
	 * @param commandStack
	 *            the command stack of the event.
	 * @param type
	 *            the type of event.
	 */
	public CommandStackEvent(CommandStack commandStack, Operation type) {
		super(commandStack);
		this.operation = type;
	}

	@Override
	public CommandStack getSource() {
		return (CommandStack)super.getSource();
	}

	/**
	 * Returns the type of this event.
	 * 
	 * @return the type of this event.
	 */
	public Operation getOperation() {
		return operation;
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return getClass().getName() + "[source=" + source + ", operation=" + operation + "]";
	}
}
