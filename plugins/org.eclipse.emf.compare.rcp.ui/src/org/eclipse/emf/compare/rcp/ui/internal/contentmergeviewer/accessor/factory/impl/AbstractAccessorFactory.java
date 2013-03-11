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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.impl;

import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.IAccessorFactory;

public abstract class AbstractAccessorFactory implements IAccessorFactory {

	private int ranking;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.accessor.factory.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory#setRanking(int)
	 */
	public void setRanking(int r) {
		ranking = r;
	}

}