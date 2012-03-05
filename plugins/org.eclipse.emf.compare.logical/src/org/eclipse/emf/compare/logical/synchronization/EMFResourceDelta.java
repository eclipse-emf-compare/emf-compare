/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.synchronization;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.team.core.diff.IDiff;

/**
 * This subclass of an {@link EMFDelta} will describe changes in EMF Resources.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFResourceDelta extends EMFDelta {
	/** Keeps track of the local resource for which this instance holds deltas. */
	private Resource localResource;

	/** Keeps track of the remote resource for which this instance holds deltas. */
	private Resource remoteResource;

	/** Keeps track of the ancestor resource for which this instance holds deltas. */
	private Resource ancestorResource;

	/** The actual diff as interpretable by the platform. */
	private IDiff diff;

	/** The EMF Compare's {@link DiffModel} this delta reflects. */
	private DiffModel diffModel;

	/**
	 * Instantiates our resource delta given all necessary information.
	 * 
	 * @param parent
	 *            Our parent model delta.
	 * @param diffModel
	 *            The diff model we represent.
	 * @param resourceDiff
	 *            The actual resource diff computed by the platform.
	 * @param localResource
	 *            The local variant of the resource for which we'll hold deltas.
	 * @param remoteResource
	 *            The remote variant of the resource for which we'll hold deltas.
	 * @param ancestorResource
	 *            The ancestor variant of the resource for which we'll hold deltas.
	 */
	public EMFResourceDelta(EMFDelta parent, DiffModel diffModel, IDiff resourceDiff, Resource localResource,
			Resource remoteResource, Resource ancestorResource) {
		super(parent);
		this.diff = resourceDiff;
		this.diffModel = diffModel;
		this.localResource = localResource;
		this.remoteResource = remoteResource;
		this.ancestorResource = ancestorResource;

		createChildren();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#clear()
	 */
	@Override
	public void clear() {
		localResource = null;
		remoteResource = null;
		ancestorResource = null;
		diff = null;
		diffModel = null;

		super.clear();
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getPath()
	 */
	@Override
	public IPath getPath() {
		IResource localIResource = EclipseModelUtils.findIResource(localResource);
		if (localIResource != null) {
			return localIResource.getFullPath();
		}
		return Path.EMPTY;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#isDeltaFor(java.lang.Object)
	 */
	@Override
	public boolean isDeltaFor(Object object) {
		if (!(object instanceof Resource)) {
			// No use going any further
			return false;
		}

		boolean isDelta = false;
		IResource localIResource = EclipseModelUtils.findIResource(localResource);
		IResource compareTo = EclipseModelUtils.findIResource((Resource)object);
		if (localIResource != null && compareTo != null) {
			isDelta = localIResource.getFullPath().equals(compareTo.getFullPath());
		}
		return isDelta;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#createChildren()
	 */
	@Override
	protected void createChildren() {
		// diff models contain a "parentless" diff group as root. Ignore it and switch to its children.
		if (diffModel.getOwnedElements().size() == 1) {
			DiffGroup group = (DiffGroup)diffModel.getOwnedElements().get(0);
			for (DiffElement childDiff : group.getSubDiffElements()) {
				createChildDelta(childDiff);
			}
		}
	}
}
