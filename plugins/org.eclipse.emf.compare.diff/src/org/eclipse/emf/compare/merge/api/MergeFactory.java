/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge.api;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * The merge factory allows the creation of a merger from any kind of {@link DiffElement}.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public abstract class MergeFactory {
	/**
	 * Handles the creation of the merger.
	 * 
	 * @param element
	 * 			{@link DiffElement} for which we need a merger.
	 * @return The merger adapted to <code>element</code>.
	 */
	public abstract AbstractMerger createMerger(DiffElement element);
}
