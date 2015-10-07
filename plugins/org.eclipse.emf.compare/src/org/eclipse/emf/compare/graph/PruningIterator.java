/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.graph;

import java.util.Iterator;

/**
 * A specialized tree iterator allowing clients to prune all children below a selected one.
 * <p>
 * <b>Note</b> that this only makes sense if the underlying iteration operates on a tree-like structure.
 * </p>
 * 
 * @param <E>
 *            Kind of elements this iterator is for.
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.3
 */
public interface PruningIterator<E> extends Iterator<E> {
	/**
	 * Prunes the iterator so that it skips over all the elements below the most recent result of calling
	 * {@link #next next()}.
	 * <p>
	 * This will have no effect if called before any call to {@link #next()}.
	 * </p>
	 */
	void prune();
}
