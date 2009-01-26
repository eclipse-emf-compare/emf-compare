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

import java.util.Map;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * This allows the association of mergers to specific {@link DiffElement}.
 * <p>
 * Mergers should implement the interface {@link IMerger} and provide a default, no-arg constructor. This
 * interface is intended to be implemented for the use of the extension point
 * <code>org.eclipse.emf.compare.diff.mergerprovider</code>.
 * </p>
 * 
 * @see IMerger
 * @see MergeFactory
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IMergerProvider {
	/**
	 * This will be called by the merge factory to get a list of all the mergers associated to a given
	 * {@link DiffElement}.
	 * 
	 * @return The map allowing us to know which merger should be used for which DiffElement.
	 */
	Map<Class<? extends DiffElement>, Class<? extends IMerger>> getMergers();
}
