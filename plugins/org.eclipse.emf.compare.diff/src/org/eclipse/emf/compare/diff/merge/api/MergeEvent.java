/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * An event characterizing a merge operation. Holds information about the DiffElement(s) that is(are) about to
 * be merged (or which has just been merged) and the merger that handled the operation.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public class MergeEvent {
	/** Target {@link DiffElement}s of the underlying merge operation. */
	private final List<DiffElement> elements = new ArrayList<DiffElement>();

	/**
	 * Constructs a new merge event given the {@link IMerger} in charge and a single target
	 * {@link DiffElement}.
	 * 
	 * @param diff
	 *            {@link DiffElement} which holds the information for this merge operation.
	 */
	public MergeEvent(DiffElement diff) {
		elements.add(diff);
	}

	/**
	 * Constructs a new merge event given the {@link IMerger} in charge and a list of targeted
	 * {@link DiffElement}s.
	 * 
	 * @param diffs
	 *            {@link DiffElement}s which holds the information for this merge operation.
	 */
	public MergeEvent(List<DiffElement> diffs) {
		elements.addAll(diffs);
	}

	/**
	 * Returns the DiffElement(s) targeted by the underlying operation.
	 * 
	 * @return The DiffElement(s) targeted by the underlying operation.
	 */
	public List<DiffElement> getDifferences() {
		return elements;
	}
}
