/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * Base interface for merger implementation. Clients can implement this interface or extend
 * {@link DefaultMerger} which also provide some utility methods.
 * 
 * @see DefaultMerger
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IMerger {
	/**
	 * Applies the modification in the original (left) model.
	 */
	void applyInOrigin();

	/**
	 * Returns <code>True</code> if the merger is allowed to apply changes in the origin (left) model.
	 * 
	 * @return <code>True</code> if the merger is allowed to apply changes in the origin (left) model,
	 *         <code>False</code> otherwise.
	 */
	boolean canApplyInOrigin();

	/**
	 * Returns <code>True</code> if the merger is allowed to undo changes in the target (right) model.
	 * 
	 * @return <code>True</code> if the merger is allowed to undo changes in the target (right) model,
	 *         <code>False</code> otherwise.
	 */
	boolean canUndoInTarget();

	/**
	 * Sets the {@link DiffElement} to be merged.
	 * 
	 * @param element
	 *            The {@link DiffElement} to be merged.
	 */
	void setDiffElement(DiffElement element);

	/**
	 * Cancels the modification in the target (right) model.
	 */
	void undoInTarget();
}
