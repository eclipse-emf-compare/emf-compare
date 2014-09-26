/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.hook;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Interface used to hook some behavior into the {@link ResourceSet} used by EMF Compare when using logical
 * model.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public interface IResourceSetHook {

	/**
	 * Returns true if this hook should be used.
	 * 
	 * @param uris
	 *            list of {@link URI}s about to be loaded in the {@link ResourceSet}.
	 * @return <code>true</code> if this hook should be used, <code>false</code> otherwise.
	 */
	boolean isHookFor(Collection<? extends URI> uris);

	/**
	 * This will be called before the final resource set is populated, in unspecified order. Resource set
	 * hooks can load resource in this resource set and thus an individual hook is not guaranteed to be
	 * provided an empty resource set here.
	 * 
	 * @param resourceSet
	 *            about to be filled.
	 * @param uris
	 *            {@link URI}s that the resource set has been requested to load. The {@link Collection} of
	 *            {@link URI} is not modifiable.
	 */
	void preLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris);

	/**
	 * This will be called after the resource set is populated in an unspecified order.
	 * 
	 * @param resourceSet
	 *            that has been filled with {@link org.eclipse.emf.ecore.resource.Resource}s.
	 * @param uris
	 *            {@link URI}s that the resource set has been requested to load.The {@link Collection} of
	 *            {@link URI} is not modifiable.
	 */
	void postLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris);

}
