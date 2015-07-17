/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider;

import org.eclipse.emf.compare.internal.merge.MergeMode;

/**
 * Provider of tooltips.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 4.2
 */
public interface ITooltipLabelProvider {

	/**
	 * Return a tooltip for the given MergeMode.
	 * 
	 * @param mode
	 *            The merge mode
	 * @return the tooltip
	 * @throws IllegalArgumentException
	 *             For an undefined or invalid {@link MergeMode}
	 */
	String getTooltip(MergeMode mode) throws IllegalArgumentException;

}
