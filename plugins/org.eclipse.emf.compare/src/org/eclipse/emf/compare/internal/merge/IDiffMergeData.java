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

import org.eclipse.emf.compare.DifferenceSource;

/**
 * Interface implemented by {@link org.eclipse.emf.compare.internal.merge.DiffMergeDataAdapter}. It helps to
 * know the way of merge and the editable sides of a difference.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public interface IDiffMergeData {

	/**
	 * Check if the difference has been merged from right to left.
	 * 
	 * @return true if the difference has been merged from right to left, false otherwise.
	 */
	boolean hasBeenMergedToLeft();

	/**
	 * Check if the difference has been merged from left to right.
	 * 
	 * @return true if the difference has been merged from left to right, false otherwise.
	 */
	boolean hasBeenMergedToRight();

	/**
	 * Returns on which side the difference has been merged to.
	 * 
	 * @return on which side the difference has been merged to
	 */
	DifferenceSource mergedTo();

	/**
	 * Set the way of merge.
	 * 
	 * @param leftToRight
	 *            true if the difference has been merge from left to right, false otehrwise.
	 */
	void setMergedTo(boolean leftToRight);

	/**
	 * Check if the left side of the difference is editable.
	 * 
	 * @return true if the left side of the difference is editable, false otherwise.
	 */
	boolean isLeftEditable();

	/**
	 * Check if the right side of the difference is editable.
	 * 
	 * @return true if the right side of the difference is editable, false otherwise.
	 */
	boolean isRightEditable();

	/**
	 * Set that the left side of the difference is editable or not.
	 * 
	 * @param leftEditable
	 *            true if the left side of the difference is editable, false otherwise.
	 */
	void setLeftEditable(boolean leftEditable);

	/**
	 * Set that the right side of the difference is editable or not.
	 * 
	 * @param rightEditable
	 *            true if the right side of the difference is editable, false otherwise.
	 */
	void setRightEditable(boolean rightEditable);

}
