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
package org.eclipse.emf.compare.rcp.ui.mergeviewer;

import com.google.common.base.Objects;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class BasicMergeViewerItemImpl implements IMergeViewerItem {

	private final Object fLeft;

	private final Object fRight;

	private final Object fAncestor;

	private final Diff fDiff;

	public BasicMergeViewerItemImpl(Diff diff, Object left, Object right, Object ancestor) {
		fLeft = left;
		fRight = right;
		fAncestor = ancestor;
		fDiff = diff;
	}

	/**
	 * @return
	 */
	public Diff getDiff() {
		return fDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getAncestor()
	 */
	public Object getAncestor() {
		return fAncestor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getLeft()
	 */
	public Object getLeft() {
		return fLeft;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getRight()
	 */
	public Object getRight() {
		return fRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getSideValue(org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide)
	 */
	public Object getSideValue(MergeViewerSide side) {
		switch (side) {
			case LEFT:
				return fLeft;
			case RIGHT:
				return fRight;
			case ANCESTOR:
				return fAncestor;
			default:
				throw new IllegalStateException(); // happy compiler :)
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BasicMergeViewerItemImpl) {
			BasicMergeViewerItemImpl that = (BasicMergeViewerItemImpl)obj;
			return Objects.equal(fLeft, that.fLeft) && Objects.equal(fRight, that.fRight)
					&& Objects.equal(fRight, that.fRight) && Objects.equal(fDiff, that.fDiff);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(fLeft, fRight, fAncestor, fDiff);
	}
}
