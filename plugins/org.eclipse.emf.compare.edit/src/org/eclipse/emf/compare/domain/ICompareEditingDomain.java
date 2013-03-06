/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.domain;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface ICompareEditingDomain {

	void dispose();

	ICompareCommandStack getCommandStack();

	/**
	 * @since 3.0
	 */
	ChangeRecorder getChangeRecorder();

	/**
	 * @since 3.0
	 */
	Command createCopyCommand(List<? extends Diff> differences, boolean leftToRight,
			IMerger.Registry mergerRegistry);

	/**
	 * @since 3.0
	 */
	Command createCopyAllNonConflictingCommand(List<? extends Diff> differences, boolean leftToRight,
			IMerger.Registry mergerRegistry);

}
