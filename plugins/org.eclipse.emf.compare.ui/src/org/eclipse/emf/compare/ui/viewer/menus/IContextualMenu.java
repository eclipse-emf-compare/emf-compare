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
package org.eclipse.emf.compare.ui.viewer.menus;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Control;

/**
 * Interface for EMF Compare's contextual menus.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public interface IContextualMenu {

	/**
	 * It builds the contextual menu on the specified viewer.
	 * 
	 * @param configuration
	 *            The compare configuration.
	 * @param viewer
	 *            The viewer on which the menu has to be created.
	 * @param control
	 *            The control of the viewer.
	 */
	void create(final CompareConfiguration configuration, final ISelectionProvider viewer,
			final Control control);

}
