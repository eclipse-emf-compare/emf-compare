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

import java.util.Iterator;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.impl.ConflictingDiffElementImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.ITwoWayDiff;
import org.eclipse.team.core.diff.provider.ThreeWayDiff;
import org.eclipse.team.core.diff.provider.TwoWayDiff;

/**
 * This subclass of an {@link EMFDelta} will act as a container for other deltas. It is the counterpart of EMF
 * Compare {@link DiffGroup}s.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public class EMFEObjectDelta extends EMFDelta {
	/** Keeps track of the local EObject for which this instance holds deltas. */
	private EObject localObject;

	/** Keeps track of the remote EObject for which this instance holds deltas. */
	private EObject remoteObject;

	/** Keeps track of the ancestor EObject for which this instance holds deltas. */
	private EObject ancestorObject;

	/** The EMF Compare's {@link DiffGroup} this delta reflects. */
	private DiffGroup diffGroup;

	/** This will only serve in order to decorate the navigator's labels. */
	private IDiff diff;

	/**
	 * Instantiates this delta given the {@link DiffGroup} it should represent.
	 * 
	 * @param parent
	 *            Parent of this delta.
	 * @param diffGroup
	 *            The DiffGroup for which to create this delta.
	 */
	public EMFEObjectDelta(EMFDelta parent, DiffGroup diffGroup) {
		super(parent);
		this.diffGroup = diffGroup;

		// Find back the three objects to which this group correspond
		EObject rightObject = diffGroup.getRightParent();
		String uriFragment = EcoreUtil.getURI(rightObject).fragment();

		EMFDelta resourceDeltaContainer = parent;
		while (resourceDeltaContainer.getParent() != null
				&& !(resourceDeltaContainer instanceof EMFResourceDelta)) {
			resourceDeltaContainer = resourceDeltaContainer.getParent();
		}

		if (resourceDeltaContainer instanceof EMFResourceDelta) {
			Resource localResource = (Resource)((EMFResourceDelta)resourceDeltaContainer).getLocal();
			Resource remoteResource = (Resource)((EMFResourceDelta)resourceDeltaContainer).getRemote();
			Resource ancestorResource = (Resource)((EMFResourceDelta)resourceDeltaContainer).getAncestor();

			localObject = localResource.getEObject(uriFragment);
			remoteObject = remoteResource.getEObject(uriFragment);
			if (ancestorResource != null) {
				ancestorObject = ancestorResource.getEObject(uriFragment);
			}
		}

		createChildren();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#clear()
	 */
	@Override
	public void clear() {
		localObject = null;
		remoteObject = null;
		ancestorObject = null;
		diffGroup = null;
		diff = null;

		super.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getDiff()
	 */
	@Override
	public IDiff getDiff() {
		if (diff == null) {
			int localKind = IDiff.NO_CHANGE;
			int remoteKind = IDiff.NO_CHANGE;
			// Browse the diff group and check wether we have local and/or remote changes
			Iterator<DiffElement> subDiffIterator = getAllSubDifferences(diffGroup).iterator();
			while ((localKind == IDiff.NO_CHANGE || remoteKind == IDiff.NO_CHANGE)
					&& subDiffIterator.hasNext()) {
				DiffElement child = subDiffIterator.next();

				if (child.isRemote()) {
					remoteKind = IDiff.CHANGE;
				} else {
					localKind = IDiff.CHANGE;
				}
			}

			if (ancestorObject != null) {
				ITwoWayDiff localDiff = new TwoWayDiff(getPath(), localKind, 0);
				ITwoWayDiff remoteDiff = new TwoWayDiff(getPath(), remoteKind, 0);
				diff = new ThreeWayDiff(localDiff, remoteDiff);
			} else {
				diff = new TwoWayDiff(getPath(), localKind, 0);
			}
		}
		return diff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getRemote()
	 */
	@Override
	public Object getRemote() {
		return remoteObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getLocal()
	 */
	@Override
	public Object getLocal() {
		return localObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getAncestor()
	 */
	@Override
	public Object getAncestor() {
		return ancestorObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#getPath()
	 */
	@Override
	public IPath getPath() {
		URI uri = EcoreUtil.getURI(diffGroup.getRightParent());
		return new Path(uri.toString());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#createChildren()
	 */
	@Override
	protected void createChildren() {
		for (DiffElement diffElement : diffGroup.getSubDiffElements()) {
			createChildDelta(diffElement);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.logical.synchronization.EMFDelta#isDeltaFor(java.lang.Object)
	 */
	@Override
	public boolean isDeltaFor(Object object) {
		if (!(object instanceof EObject)) {
			// No use going any further
			return false;
		}

		// Local and remote could be null, but all three can not
		final URI objectURI;
		if (localObject != null) {
			objectURI = EcoreUtil.getURI(localObject);
		} else if (remoteObject != null) {
			objectURI = EcoreUtil.getURI(remoteObject);
		} else {
			objectURI = EcoreUtil.getURI(ancestorObject);
		}

		return objectURI.equals(EcoreUtil.getURI((EObject)object));
	}

	/**
	 * Returns the list of all sub-differences of the given DiffElement recursively.
	 * 
	 * @param element
	 *            The element we seek the sub-differences of.
	 * @return The list of all differences under the given {@link DiffElement}, less the sub-DiffGroups
	 *         themselves.
	 */
	private static EList<DiffElement> getAllSubDifferences(DiffElement element) {
		EList<DiffElement> ownedDifferences = new BasicEList<DiffElement>();

		for (DiffElement diff : element.getSubDiffElements()) {
			if (diff instanceof DiffGroup || diff instanceof ConflictingDiffElementImpl) {
				ownedDifferences.addAll(getAllSubDifferences(diff));
			} else {
				ownedDifferences.add(diff);
			}
		}

		return ownedDifferences;
	}
}
