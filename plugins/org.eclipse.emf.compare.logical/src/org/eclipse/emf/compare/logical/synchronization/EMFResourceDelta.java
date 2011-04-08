/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.synchronization;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.team.core.diff.IDiff;

/**
 * This subclass of an {@link EMFDelta} will describe changes in EMF Resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFResourceDelta extends EMFDelta {
	/** Keeps track of the local resource for which this instance holds deltas. */
	private final Resource localResource;

	/** Keeps track of the remote resource for which this instance holds deltas. */
	private final Resource remoteResource;

	/** Keeps track of the ancestor resource for which this instance holds deltas. */
	private final Resource ancestorResource;

	/** The actual diff as interpretable by the platform. */
	private final IDiff diff;

	/**
	 * Instantiates our resource delta given all necessary information.
	 * 
	 * @param parent
	 *            Our parent model delta.
	 * @param resourceDiff
	 *            The actual resource diff computed by the platform.
	 * @param localResource
	 *            The local variant of the resource for which we'll hold deltas.
	 * @param remoteResource
	 *            The remote variant of the resource for which we'll hold deltas.
	 * @param ancestorResource
	 *            The ancestor variant of the resource for which we'll hold deltas.
	 */
	public EMFResourceDelta(EMFDelta parent, IDiff resourceDiff, Resource localResource,
			Resource remoteResource, Resource ancestorResource) {
		super(parent);
		diff = resourceDiff;
		this.localResource = localResource;
		this.remoteResource = remoteResource;
		this.ancestorResource = ancestorResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getLocal()
	 */
	@Override
	public Object getLocal() {
		return localResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getAncestor()
	 */
	@Override
	public Object getAncestor() {
		return ancestorResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getRemote()
	 */
	@Override
	public Object getRemote() {
		return remoteResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getDiff()
	 */
	@Override
	public IDiff getDiff() {
		return diff;
	}
}
