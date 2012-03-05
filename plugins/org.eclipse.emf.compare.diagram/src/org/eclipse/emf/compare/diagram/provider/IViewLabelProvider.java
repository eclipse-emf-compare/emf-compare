/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.provider;

import org.eclipse.gmf.runtime.notation.View;

/**
 * Provider for the management of labels.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IViewLabelProvider {

	/**
	 * The default instance of this provider.
	 */
	IViewLabelProvider DEFAULT_INSTANCE = new AbstractLabelProvider() {
		public boolean isManaged(View view) {
			return true;
		}
	};

	/**
	 * Checks if the view represents a label.
	 * 
	 * @param view
	 *            The view.
	 * @return True if the specified view contains a label.
	 */
	boolean isManaged(View view);

	/**
	 * Get the label from the view.
	 * 
	 * @param view
	 *            The view.
	 * @return The label.
	 */
	String elementLabel(View view);
}
