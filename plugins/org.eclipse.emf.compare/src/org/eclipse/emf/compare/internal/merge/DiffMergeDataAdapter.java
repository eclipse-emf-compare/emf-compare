/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.merge;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.DifferenceSource;

/**
 * Adapter that help to know the way of merge and the editable sides of a difference.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class DiffMergeDataAdapter extends AdapterImpl implements IDiffMergeData {

	/** The merge way. */
	boolean leftToRight;

	/** Left side is editable. */
	boolean leftEditable;

	/** Right side is editable. */
	boolean rightEditable;

	/**
	 * Constructor.
	 * 
	 * @param leftToRight
	 *            The merge way.
	 * @param leftEditable
	 *            Left side editable.
	 * @param rightEditable
	 *            Right side editable.
	 */
	public DiffMergeDataAdapter(boolean leftToRight, boolean leftEditable, boolean rightEditable) {
		this.leftToRight = leftToRight;
		this.leftEditable = leftEditable;
		this.rightEditable = rightEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#hasBeenMergedToLeft()
	 */
	public boolean hasBeenMergedToLeft() {
		return !leftToRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#hasBeenMergedToRight()
	 */
	public boolean hasBeenMergedToRight() {
		return leftToRight;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#setMergedTo(boolean)
	 */
	public void setMergedTo(boolean lToR) {
		this.leftToRight = lToR;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#isLeftEditable()
	 */
	public boolean isLeftEditable() {
		return leftEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#isRightEditable()
	 */
	public boolean isRightEditable() {
		return rightEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#setLeftEditable(boolean)
	 */
	public void setLeftEditable(boolean leftEditable) {
		this.leftEditable = leftEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#setRightEditable(boolean)
	 */
	public void setRightEditable(boolean rightEditable) {
		this.rightEditable = rightEditable;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == IDiffMergeData.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IDiffMergeData#mergedTo()
	 */
	public DifferenceSource mergedTo() {
		if (leftToRight) {
			return DifferenceSource.RIGHT;
		} else {
			return DifferenceSource.LEFT;
		}
	}
}
