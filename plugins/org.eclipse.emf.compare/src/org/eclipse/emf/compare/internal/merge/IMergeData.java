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

import org.eclipse.emf.common.notify.Adapter;

/**
 * Interface implemented by {@link org.eclipse.emf.compare.internal.merge.MergeDataImpl}. It helps to know the
 * way of merge and the editable sides of a difference.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public interface IMergeData extends Adapter {

	/**
	 * Returns whether the left side of the comparison is editable.
	 * 
	 * @return true if the left side of the comparison is editable, false otherwise.
	 */
	boolean isLeftEditable();

	/**
	 * Set whether the left side of the comparison is editable.
	 * 
	 * @param leftEditable
	 *            whether the left side of the comparison is editable.
	 */
	void setLeftEditable(boolean leftEditable);

	/**
	 * Returns whether the right side of the comparison is editable.
	 * 
	 * @return true if the right side of the comparison is editable, false otherwise.
	 */
	boolean isRightEditable();

	/**
	 * Set whether the right side of the comparison is editable.
	 * 
	 * @param rightEditable
	 *            whether the right side of the comparison is editable.
	 */
	void setRightEditable(boolean rightEditable);

}
