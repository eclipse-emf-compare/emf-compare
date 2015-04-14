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
package org.eclipse.emf.compare.ide.utils;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;

/**
 * Some implementations of IStorage provide a plain erroneous {@link IStorage#getFullPath() path}.
 * <p>
 * Implementers can provide an adapter from a given IStorage implementation to a sub-class of this interface
 * to provide EMF Compare with a mean to compute the correct path.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.2
 */
public interface IStoragePathProvider {
	/**
	 * Allows EMF compare to query for the real path of the given IStorage.
	 * <p>
	 * The returned path must be workspace-relative and may not be <code>null</code>. The resource pointed to
	 * by this path is not required to exist locally.
	 * </p>
	 * 
	 * @param storage
	 *            The storage for which we seek a useable path.
	 * @return The fixed path for this IStorage. Cannot be <code>null</code>.
	 */
	IPath computeFixedPath(IStorage storage);
}
