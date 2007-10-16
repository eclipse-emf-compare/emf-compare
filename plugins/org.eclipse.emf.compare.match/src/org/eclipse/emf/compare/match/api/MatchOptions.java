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
package org.eclipse.emf.compare.match.api;

/**
 * Defines constants for the different options available to tweak the matching process.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public interface MatchOptions {
	/**
	 * Default value for the search window.
	 */
	int DEFAULT_SEARCH_WINDOW = 100;

	/** Key for the option specifying whether we should ignore XMI ID when comparing. */
	String OPTION_IGNORE_XMI_ID = "match.ignore.xmi.id"; //$NON-NLS-1$

	/** Key for the option defining the search window. */
	String OPTION_SEARCH_WINDOW = "match.search.window"; //$NON-NLS-1$
}
