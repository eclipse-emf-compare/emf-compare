/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.filter;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;

/**
 * Interface for EMF Compare's difference filters.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.3
 */
public interface IDifferenceFilter {
	/**
	 * Returns true if this filter hides the given {@link DiffElement}.
	 * 
	 * @param element
	 *            the element to test.
	 * @return true if the given element has to be hidden, false otherwise.
	 */
	boolean hides(DiffElement element);
}
