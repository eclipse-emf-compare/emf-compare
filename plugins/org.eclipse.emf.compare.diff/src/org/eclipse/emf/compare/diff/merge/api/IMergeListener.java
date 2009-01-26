/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.api;

import java.util.EventListener;

/**
 * Base interface for the listeners that can be notified when a merge operation is about to occur or to end.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IMergeListener extends EventListener {
	/**
	 * This is called whenever a difference has been merge.
	 * 
	 * @param event
	 *            Holds information about the merged {@link DiffElement}.
	 */
	void mergeDiffEnd(MergeEvent event);

	/**
	 * This is called whenever a DiffElement is about to be merged.
	 * 
	 * @param event
	 *            Holds information about the merged {@link DiffElement}.
	 */
	void mergeDiffStart(MergeEvent event);

	/**
	 * This is called whenever a merge operation has ended, either on a single {@link DiffElement} or a list
	 * of differences.
	 * 
	 * @param event
	 *            Holds information about the merged {@link DiffElement}(s).
	 */
	void mergeOperationEnd(MergeEvent event);

	/**
	 * This is called whenever a merge operation is about to be launched, either on a single
	 * {@link DiffElement} or a list of differences.
	 * 
	 * @param event
	 *            Holds information about the merged {@link DiffElement}(s).
	 */
	void mergeOperationStart(MergeEvent event);
}
