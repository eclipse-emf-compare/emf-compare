/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.extension;

import org.osgi.service.prefs.Preferences;

/**
 * Represente an item that can be configured from preferences.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public interface IConfigurableItem {

	/**
	 * Get the configuration of the Item.
	 * 
	 * @return the configuration.
	 */
	Preferences getConfiguration();

	/**
	 * Set the configuration of an Item.
	 * 
	 * @param config
	 *            Configuration to set.
	 */
	void setConfiguration(Preferences config);

}
