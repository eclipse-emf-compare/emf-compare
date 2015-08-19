/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils;

import org.eclipse.emf.common.notify.Adapter;

/**
 * This class is a wrapper used to create different kind of StoragePathAdapter depending on the needs.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.3
 */
public interface IStoragePathAdapterProvider {

	/**
	 * This method is used to create a new StoragePathAdapter.
	 * 
	 * @param fullPath
	 *            The path to use.
	 * @param isLocal
	 *            Whether this storage is local.
	 * @return the new adapter
	 */
	Adapter createStoragePathAdapter(String fullPath, boolean isLocal);
}
