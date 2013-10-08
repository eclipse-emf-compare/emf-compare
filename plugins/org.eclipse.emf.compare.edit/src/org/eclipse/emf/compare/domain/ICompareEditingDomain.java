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
import org.eclipse.emf.compare.command.ICompareCopyCommand;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

/**
 * Something like EMF's {@link org.eclipse.emf.edit.domain.EditingDomain} but dedicated to the handling of
 * comparison merging and undoing / redoing those merge.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface ICompareEditingDomain {

	/**
	 * Returns the associated {@link ICompareCommandStack}.
	 * 
	 * @return the associated {@link ICompareCommandStack}.
	 */
	ICompareCommandStack getCommandStack();

	/**
	 * Returns the associated {@link ChangeRecorder}.
	 * 
	 * @return the associated {@link ChangeRecorder}.
	 * @since 3.0
	 */
	ChangeRecorder getChangeRecorder();

	/**
	 * Creates a new command that will merge the given differences in from right to left or left to right by
	 * using the mergers defined in the given merger registry.
	 * 
	 * @param differences
	 *            the differences to merge.
	 * @param leftToRight
	 *            whether the merge has to be merge from left to right or right to left.
	 * @param mergerRegistry
	 *            the merger registry to query to get the appropriate mergers for each difference to be
	 *            merged.
	 * @return the created command.
	 * @since 3.0
	 */
	Command createCopyCommand(List<? extends Diff> differences, boolean leftToRight,
			IMerger.Registry mergerRegistry);

	/**
	 * Creates a new command that will execute the given {@link IMergeRunnable} that is expected to merge the
	 * given differences in from right to left or left to right by using the mergers defined in the given
	 * merger registry.
	 * 
	 * @param differences
	 *            the differences to merge.
	 * @param leftToRight
	 *            whether the merge has to be merge from left to right or right to left.
	 * @param mergerRegistry
	 *            the merger registry to query to get the appropriate mergers for each difference to be
	 *            merged.
	 * @param runnable
	 *            the runnable that will implement the merge
	 * @return the created command.
	 * @since 4.0
	 */
	ICompareCopyCommand createCopyCommand(List<? extends Diff> differences, boolean leftToRight,
			IMerger.Registry mergerRegistry, IMergeRunnable runnable);

}
