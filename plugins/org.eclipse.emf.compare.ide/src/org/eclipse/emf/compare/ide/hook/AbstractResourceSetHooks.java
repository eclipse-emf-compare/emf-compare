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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Abstract implementation of {@link IResourceSetHook}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @since 3.2
 */
public abstract class AbstractResourceSetHooks implements IResourceSetHook {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.hook.IResourceSetHook#isHookFor(java.util.Collection)
	 */
	public boolean isHookFor(Collection<? extends URI> uris) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.hook.IResourceSetHook#preLoadingHook(org.eclipse.emf.ecore.resource.ResourceSet,
	 *      java.util.Collection)
	 */
	public void preLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.hook.IResourceSetHook#postLoadingHook(org.eclipse.emf.ecore.resource.ResourceSet,
	 *      java.util.Collection)
	 */
	public void postLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.hook.IResourceSetHook#onDispose(java.lang.Iterable)
	 */
	public void onDispose(Iterable<Resource> resources) {
	}
}
