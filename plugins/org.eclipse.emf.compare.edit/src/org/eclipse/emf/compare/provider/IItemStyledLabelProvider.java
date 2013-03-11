/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider;

import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;

/**
 * This is the interface implemented to provide a styled label for an item.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public interface IItemStyledLabelProvider {

	/**
	 * Returns the styled text label for the given object.
	 * 
	 * @param object
	 *            the object to evaluate the styled string for.
	 * @return the styled string.
	 */
	IComposedStyledString getStyledText(Object object);
}
