/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import com.google.common.annotations.Beta;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * This will be used by URI Converters in order to retrieve the storages for the files it seeks. The URI
 * Converter usually only knows about local resources, it will thus ask for its storage accessor for the
 * proper remote content.
 */
@Beta
public interface IStorageProviderAccessor {
	/**
	 * This will be called by the URI Converter to get the content associated with the given local resource
	 * (which might not exist locally).
	 * 
	 * @param resource
	 *            The resource we need content for.
	 * @param side
	 *            Side of the content we seek.
	 * @return The content for the given side of the given resource.
	 * @throws CoreException
	 */
	public IStorageProvider getStorageProvider(IResource resource, DiffSide side) throws CoreException;

	/** Used by the resolution process to determine the side of the revision to fetch. */
	public static enum DiffSide {
		/** Source side. Usually denotes "left" or "local" content. */
		SOURCE,

		/** Remote side. Usually denotes the "right" or "reference" content for a comparison. */
		REMOTE,

		/** Origin side. Corresponds to the common ancestor of the local and remote sides. */
		ORIGIN;
	}
}
