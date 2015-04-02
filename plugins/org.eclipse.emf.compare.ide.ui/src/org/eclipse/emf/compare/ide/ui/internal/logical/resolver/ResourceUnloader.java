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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Implementation of a Runnable that can be used to unload a given resource and make it garbage-collectable.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
/* default */class ResourceUnloader implements Runnable {
	/** The resource set from which to unload a resource. */
	private final SynchronizedResourceSet resourceSet;

	/** The resource to unload. */
	private final Resource resource;

	/** Monitor on which to report progress to the user. */
	private final IProgressMonitor monitor;

	/**
	 * Default constructor.
	 * 
	 * @param resourceSet
	 *            The resource set from which to unload a resource.
	 * @param resource
	 *            The resource to unload.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	public ResourceUnloader(SynchronizedResourceSet resourceSet, Resource resource, IProgressMonitor monitor) {
		this.resourceSet = resourceSet;
		this.resource = resource;
		this.monitor = monitor;
	}

	/** {@inheritDoc} */
	public void run() {
		resourceSet.unload(resource, monitor);
	}
}
