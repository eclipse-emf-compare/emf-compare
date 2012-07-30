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
package org.eclipse.emf.compare.ide.ui.internal.actions.group;

import org.eclipse.emf.compare.Comparison;

/**
 * Instances of this class will be used by EMF Compare in order to provide difference grouping facilities to
 * the structural differences view.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface DifferenceGroupProvider {
	/**
	 * This will be called internally by the grouping actions in order to determine how the differences should
	 * be grouped in the structural view.
	 * 
	 * @param comparison
	 *            The comparison which is to be displayed in the structural view. By default, its containment
	 *            tree will be displayed.
	 * @return The collection of difference groups that are to be displayed in the structural viewer. An empty
	 *         group will not be displayed at all. If {@code null}, we'll fall back to the default behavior.
	 */
	Iterable<? extends DifferenceGroup> getGroups(Comparison comparison);
}
