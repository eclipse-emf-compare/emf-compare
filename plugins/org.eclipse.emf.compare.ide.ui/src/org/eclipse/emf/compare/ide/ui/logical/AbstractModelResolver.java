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
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;

/**
 * This can be subclassed instead of {@link IModelResolver} to avoid reimplementing common extension-realted
 * code.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public abstract class AbstractModelResolver implements IModelResolver {
	/** Ranking of this resolver. */
	protected int ranking;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelResolver#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.logical.IModelResolver#setRanking(int)
	 */
	public void setRanking(int newRanking) {
		this.ranking = newRanking;
	}
}
