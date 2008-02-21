/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.api;

import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.match.metamodel.MatchModel;

/**
 * A Diff engine has the responsability to provide a diff (or delta) model from a matching model.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public interface IDiffEngine {
	/**
	 * Return a diffmodel created using the match model. This implementation is a generic and simple one.
	 * 
	 * @param match
	 *            The matching model.
	 * @return The corresponding diff model.
	 */
	DiffModel doDiff(MatchModel match);

	/**
	 * Return a diffmodel created using the match model. This implementation is a generic and simple one.
	 * 
	 * @param match
	 *            the matching model
	 * @param threeWay
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code>
	 *            otherwise.
	 * @return the corresponding diff model
	 */
	DiffModel doDiff(MatchModel match, boolean threeWay);
	
	/**
     * This will be called with each access from the service to the singleton
     * instance of this engine. Clients should dispose of all caches and
     * recorded information within this method's implementation.
     */
	void reset();
}
