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
package org.eclipse.emf.compare.match.resource;

import java.util.Iterator;

import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * An {@link IResourceMatcher} will be used to match two or three
 * {@link org.eclipse.emf.ecore.resource.Resource}s together; depending on whether we are doing a two or three
 * way comparison.
 * <p>
 * Do take note that the match engine expects IResourceMatchers to return matching resources as well as
 * resources that do not match.
 * </p>
 * <p>
 * A default implementation of the {@link IResourceMatcher}, based on strategies, can also be subclassed by
 * clients, see {@link StrategyResourceMatcher}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see StrategyResourceMatcher
 */
public interface IResourceMatcher {
	/**
	 * This will be called by the engine in order to retrieve the mappings created by this matcher.
	 * <p>
	 * The returned mappings should include both "matching" resources and "not matching" resources (i.e.
	 * resources that are in either left or right ... but not in any of the two other lists).
	 * </p>
	 * 
	 * @param leftResources
	 *            An iterator over the resources we found on the left side.
	 * @param rightResources
	 *            An iterator over the resources we found on the right side.
	 * @param originResources
	 *            An iterator over the resources that may be considered as common ancestors of the couples
	 *            detected on the left and right sides.
	 * @return The created resource mappings. Should include both matched and unmatched resources.
	 */
	Iterable<MatchResource> createMappings(Iterator<? extends Resource> leftResources,
			Iterator<? extends Resource> rightResources, Iterator<? extends Resource> originResources);
}
