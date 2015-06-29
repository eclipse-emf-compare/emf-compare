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
package org.eclipse.emf.compare.ide.utils;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;

/**
 * {@inheritDoc}.
 * <p>
 * Implementors can provide an adapter from a given IStorage implementation to a sub-class of this interface
 * to provide EMF Compare with a mean to compute the absolute path.
 * </p>
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.3
 */
public interface IStoragePathProvider2 extends IStoragePathProvider {

	/**
	 * Allows EMF compare to query for the absolute path of the given IStorage.
	 * <p>
	 * The returned path must be absolute and may not be <code>null</code>. The resource pointed to by this
	 * path is not required to exist locally.
	 * </p>
	 * 
	 * @param storage
	 *            storage The storage for which we seek an absolute path.
	 * @return The absolute path for this IStorage. Can return null if absolute path cannot be computed.
	 * @since 3.3
	 */
	IPath computeAbsolutePath(IStorage storage);
}
