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

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Simple structure that the {@link StrategyResourceMatcher}s will use to map Resources together.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceMapping {
	/** Holds a reference to the left resource of this mapping. */
	private final Resource left;

	/** Holds a reference to the right resource of this mapping. */
	private final Resource right;

	/** Holds a reference to the common ancestor of {@link #left} and {@link #right}, if any. */
	private final Resource origin;

	/**
	 * Instantiates a ResourceMapping given its mapped resources.
	 * 
	 * @param left
	 *            The left resource.
	 * @param right
	 *            The right resource.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. May be <code>null</code>.
	 */
	public ResourceMapping(Resource left, Resource right, Resource origin) {
		this.left = left;
		this.right = right;
		this.origin = origin;
	}

	/**
	 * Returns the left resource of this mapping.
	 * 
	 * @return The left resource of this mapping.
	 */
	public Resource getLeft() {
		return left;
	}

	/**
	 * Returns the right resource of this mapping.
	 * 
	 * @return The right resource of this mapping.
	 */
	public Resource getRight() {
		return right;
	}

	/**
	 * Returns the origin resource of this mapping, if any.
	 * 
	 * @return The origin resource of this mapping. May be <code>null</code>.
	 */
	public Resource getOrigin() {
		return origin;
	}
}
