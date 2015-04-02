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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Event that indicates that a resource that should be part of a dependency graph has been found.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @param <T>
 *            The type of the resource identifier.
 */
public class ResolvedEvent<T> {

	/** The discovered resource identifier. */
	private final T node;

	/**
	 * Constructor.
	 * 
	 * @param node
	 *            The identifier of the discovered resource, must not be {@code null}.
	 */
	public ResolvedEvent(T node) {
		this.node = checkNotNull(node);
	}

	public T getNode() {
		return node;
	}
}
