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
package org.eclipse.emf.compare.command;

import org.eclipse.emf.common.command.CommandStack;

/**
 * An extended {@link CommandStack command stack} that knows about each save state of the comparison.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface ICompareCommandStack extends CommandStack {

	/**
	 * Returns whether the left model has changes since {@link #leftSaveIsDone} was call the last.
	 * 
	 * @return whether the left model has changes since {@link #leftSaveIsDone} was call the last.
	 */
	boolean isLeftSaveNeeded();

	/**
	 * Returns whether the right model has changes since {@link #rightSaveIsDone} was call the last.
	 * 
	 * @return whether the right model has changes since {@link #rightSaveIsDone} was call the last.
	 */
	boolean isRightSaveNeeded();

	/**
	 * Called after a save of the left model has been successfully performed.
	 */
	void leftSaveIsDone();

	/**
	 * Called after a save of the right model has been successfully performed.
	 */
	void rightSaveIsDone();

}
