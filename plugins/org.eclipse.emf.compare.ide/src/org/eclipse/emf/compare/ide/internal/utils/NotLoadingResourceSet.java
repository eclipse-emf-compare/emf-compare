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
package org.eclipse.emf.compare.ide.internal.utils;

import static org.eclipse.emf.compare.ide.internal.utils.ResourceUtil.loadResource;

import com.google.common.annotations.Beta;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This implementation of a resource set will be created from a {@link ResourceTraversal}, and only those
 * resources that are part of the traversal will be loaded. This will allow us to resolve the proxies between
 * these "traversed" resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public final class NotLoadingResourceSet extends ResourceSetImpl {
	/** This will be set to <code>true</code> once we are done loading our {@link ResourceTraversal}. */
	private boolean preventLoad;

	/**
	 * Constructs a resource set to contain the resources described by the given traversal.
	 * 
	 * @param traversal
	 *            The traversal containing all resources we are to load.
	 */
	public NotLoadingResourceSet(ResourceTraversal traversal) {
		for (IStorage storage : traversal.getStorages()) {
			loadResource(storage, this, getLoadOptions());
		}
		// Prevent the loading of further resources
		preventLoad = true;
		// Then resolve all proxies between our "loaded" resources
		EcoreUtil.resolveAll(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getResource(org.eclipse.emf.common.util.URI,
	 *      boolean)
	 */
	@Override
	public Resource getResource(URI uri, boolean loadOnDemand) {
		return super.getResource(uri, preventLoad && loadOnDemand);
	}
}
