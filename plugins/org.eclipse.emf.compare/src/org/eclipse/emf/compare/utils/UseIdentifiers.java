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
package org.eclipse.emf.compare.utils;

/**
 * This can be used to tell EMF Compare which strategy should be used when matching the EObjects as regards
 * the identifier.
 * <p>
 * <ul>
 * <li>ONLY tells EMF Compare to match elements through their ID only. Any element that does not have any
 * identifier will remain unmatched.</li>
 * <li>WHEN_AVAILABLE tells EMF Compare to match elements through their IDs whenever possible, but fall back
 * to a proximity algorithm for other elements.</li>
 * <li>NEVER tells EMF Compare to ignore identifiers altogether.</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public enum UseIdentifiers {
	/**
	 * Only use id, do not fall back on content based matching if no id is found. This means any element not
	 * having an ID will not be matched.
	 */
	ONLY,

	/**
	 * Use ID when available on an element, if not available use the content matching strategy.
	 */
	WHEN_AVAILABLE,

	/**
	 * Never use IDs, always use the content matching strategy. That's useful for instance when you want to
	 * compare two results of a transformation which have arbitrary IDs.
	 */
	NEVER
}
