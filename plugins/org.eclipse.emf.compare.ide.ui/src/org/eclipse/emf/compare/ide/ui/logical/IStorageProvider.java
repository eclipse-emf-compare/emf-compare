/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Used by the logical model to wrap types that can provide IStorages.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
 */
@Beta
public interface IStorageProvider {
	/**
	 * Retrieves the underlying storage of this provider.
	 * 
	 * @param monitor
	 *            Monitor on which to report progress information.
	 * @return The underlying storage of this provider.
	 * @throws CoreException
	 *             Thrown if the underlying storage cannot be retrieved.
	 */
	IStorage getStorage(IProgressMonitor monitor) throws CoreException;
}
