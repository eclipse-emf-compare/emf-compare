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

/**
 * Adapter that help to know the way of merge and the editable sides of a difference.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MergeDataImpl extends AdapterImpl implements IMergeData {

	/** Left side is editable. */
	private boolean leftEditable;

	/** Right side is editable. */
	private boolean rightEditable;

	/**
	 * Constructor.
	 * 
	 * @param leftEditable
	 *            Left side editable.
	 * @param rightEditable
	 *            Right side editable.
	 */
	public MergeDataImpl(boolean leftEditable, boolean rightEditable) {
		this.leftEditable = leftEditable;
		this.rightEditable = rightEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IMergeData#isLeftEditable()
	 */
	public boolean isLeftEditable() {
		return leftEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IMergeData#isRightEditable()
	 */
	public boolean isRightEditable() {
		return rightEditable;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == IMergeData.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IMergeData#setLeftEditable(boolean)
	 */
	public void setLeftEditable(boolean leftEditable) {
		this.leftEditable = leftEditable;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.merge.IMergeData#setRightEditable(boolean)
	 */
	public void setRightEditable(boolean rightEditable) {
		this.rightEditable = rightEditable;
	}

}
