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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.team.core.diff.IDiff;

/**
 * This class will wraps an {@link IDiff} and serve as the basis of all of our EMF model element deltas.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public abstract class EMFDelta {
	/** Children of this delta. */
	private final List<EMFDelta> children = new ArrayList<EMFDelta>();

	/** Parent of this delta. */
	private EMFDelta parent;

	/**
	 * Create a delta for the given element
	 */
	public EMFDelta(EMFDelta parent) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public void clear() {
		children.clear();
	}

	/**
	 * Return all of the children of this delta.
	 * 
	 * @return All of the children of this delta.
	 */
	public List<EMFDelta> getChildren() {
		return new ArrayList<EMFDelta>(children);
	}

	/**
	 * Adds a new child to this delta, making sure that the new child's parent reference point to
	 * <code>this</code> aftewards.
	 * 
	 * @param child
	 *            The child that is to be added to this delta.
	 */
	public void addChild(EMFDelta child) {
		children.add(child);
		child.parent = this;
	}

	/**
	 * Returns the parent of this delta.
	 * 
	 * @return The parent of this delta.
	 */
	public EMFDelta getParent() {
		return parent;
	}

	/**
	 * Returns the root of this delta's containing tree.
	 * 
	 * @return The root of this delta's containing tree.
	 */
	public EMFModelDelta getRoot() {
		EMFDelta parentDelta = getParent();
		while (parentDelta != null && !(parentDelta instanceof EMFModelDelta)) {
			parentDelta = parentDelta.getParent();
		}
		return (EMFModelDelta)parentDelta;
	}

	/**
	 * Returns the actual {@link IDiff} behind this delta.
	 * 
	 * @return The actual {@link IDiff} behind this delta.
	 */
	public abstract IDiff getDiff();

	/**
	 * Returns the kind of this difference. This is equivalent to calling {@link #getDiff()}.
	 * {@link IDiff#getKind()}.
	 * 
	 * @return Kind of this difference.
	 * @see IDiff#getKind()
	 */
	public int getKind() {
		if (getDiff() == null) {
			return IDiff.NO_CHANGE;
		}
		return getDiff().getKind();
	}

	/**
	 * Returns the remote variant of this delta's model object.
	 * 
	 * @return The remote variant of this delta's model object.
	 */
	public abstract Object getRemote();

	/**
	 * Returns the local variant of this delta's model object.
	 * 
	 * @return The local variant of this delta's model object.
	 */
	public abstract Object getLocal();

	/**
	 * Returns the ancestor variant of this delta's model object.
	 * 
	 * @return The ancestor variant of this delta's model object.
	 */
	public abstract Object getAncestor();
}
