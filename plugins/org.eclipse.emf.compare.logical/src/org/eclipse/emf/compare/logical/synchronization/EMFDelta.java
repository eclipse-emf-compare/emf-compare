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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.team.core.diff.IDiff;
import org.eclipse.team.core.diff.IThreeWayDiff;
import org.eclipse.team.core.diff.ITwoWayDiff;

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
			parent.getChildren().add(this);
		}
	}

	/**
	 * Clears out the content of this delta.
	 */
	public void clear() {
		for (EMFDelta child : getChildren()) {
			child.clear();
		}
		children.clear();
	}

	/**
	 * Searches through the children of this delta to check whether it contains a delta for the given
	 * <em>object</em>. If <em>this</em> is itself a delta for <em>object</em>, then simply return
	 * <em>this</em>.
	 * 
	 * @param object
	 *            The object we seek a child delta for.
	 * @return <em>this</em> if it is a delta for <em>object</em>. Otherwise, the child delta if any,
	 *         <code>null</code> if none.
	 */
	public EMFDelta getChildDeltaFor(Object object) {
		EMFDelta childDelta = null;
		if (this.isDeltaFor(object)) {
			return this;
		}

		// Search the direct children
		Iterator<EMFDelta> childIterator = getChildren().iterator();
		while (childDelta == null && childIterator.hasNext()) {
			EMFDelta child = childIterator.next();
			if (child.isDeltaFor(object)) {
				childDelta = child;
			}
		}

		// It wasn't a direct children, perform a deep search
		if (childDelta == null) {
			childIterator = getChildren().iterator();
			while (childDelta == null && childIterator.hasNext()) {
				EMFDelta child = childIterator.next();
				childDelta = child.getChildDeltaFor(object);
			}
		}

		return childDelta;
	}

	/**
	 * Checks whether this delta represents a change in the given <em>object</em>.
	 * 
	 * @param object
	 *            The object to consider.
	 * @return <code>true</code> if this delta represents a change in the given <em>object</em>,
	 *         <code>false</code> otherwise.
	 */
	public boolean isDeltaFor(Object object) {
		Object local = getLocal();
		Object remote = getRemote();
		Object ancestor = getAncestor();

		boolean isDelta = false;
		if (local != null && (local == object || local.equals(object))) {
			isDelta = true;
		}
		if (!isDelta && remote != null && (remote == object || remote.equals(object))) {
			isDelta = true;
		}
		if (!isDelta && ancestor != null && (ancestor == object || ancestor.equals(object))) {
			isDelta = true;
		}

		return isDelta;
	}

	/**
	 * Return all of the children of this delta.
	 * 
	 * @return All of the children of this delta.
	 */
	public List<EMFDelta> getChildren() {
		return children;
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
	 * Returns the direction of this difference.
	 * 
	 * @return Direction of this difference.
	 * @see IThreeWayDiff#getDirection()
	 */
	public int getDirection() {
		if (getDiff() == null || getDiff() instanceof ITwoWayDiff) {
			return IDiff.NO_CHANGE;
		}
		return ((IThreeWayDiff)getDiff()).getDirection();
	}

	/**
	 * This will return the {@link IPath} to the object this is a delta for.
	 * 
	 * @return The path to the object this is a delta for.
	 */
	public abstract IPath getPath();

	/**
	 * Creates a child to this delta that will reflect the given {@link DiffElement}.
	 * 
	 * @param diffElement
	 *            The diff element the newly created child should represent.
	 */
	protected void createChildDelta(DiffElement diffElement) {
		if (diffElement instanceof DiffGroup && ((DiffGroup)diffElement).getSubchanges() > 0) {
			new EMFEObjectDelta(this, (DiffGroup)diffElement);
		}
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

	/**
	 * Creates the children delta for this Resource delta.
	 * 
	 * @param diffModel
	 *            The diff model from which to retrieve children diffs.
	 */
	protected abstract void createChildren();
}
