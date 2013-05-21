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
 * @since 3.0
 */
public class MergeDataAdapter extends AdapterImpl implements IMergeData {

	boolean leftToRight;

	public MergeDataAdapter(boolean leftToRight) {
		this.leftToRight = leftToRight;
	}

	public boolean hasBeenMergedToLeft() {
		return leftToRight == false;
	}

	public boolean hasBeenMergedToRight() {
		return leftToRight == true;
	}

	public void setMergedToLeft() {
		leftToRight = false;
	}

	public void setMergedToRight() {
		leftToRight = true;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == IMergeData.class;
	}
}
