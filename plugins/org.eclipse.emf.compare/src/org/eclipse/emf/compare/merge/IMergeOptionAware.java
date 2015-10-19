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
package org.eclipse.emf.compare.merge;

import java.util.Map;

/**
 * Handles a list of merge options. All mergers that implement this interface will be able to use and manage
 * merge options.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.3
 */
public interface IMergeOptionAware {

	/**
	 * Get the map of options that the merger will have to take into account.
	 * 
	 * @return the map of options.
	 */
	Map<Object, Object> getMergeOptions();

	/**
	 * Set the map of options that the merger will have to take into account.
	 * 
	 * @param options
	 *            the map of options.
	 */
	void setMergeOptions(Map<Object, Object> options);
}
