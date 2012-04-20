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

import java.util.List;

import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A {@link StrategyResourceMatcher} will be used to match two or three {@link Resource}s together; depending
 * on whether we are doing a two or three way comparison.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IResourceMatchingStrategy {
	/**
	 * This will be called by the resource matcher in order to determine the matching between <i>n</i>
	 * resources.
	 * 
	 * @param left
	 *            Resources we are to match in the left.
	 * @param right
	 *            Resources we are to match in the right.
	 * @param origin
	 *            Resources we are to match in the origin.
	 * @return The list of mappings this strategy managed to determine.
	 */
	List<MatchResource> matchResources(Iterable<? extends Resource> left, Iterable<? extends Resource> right,
			Iterable<? extends Resource> origin);
}
