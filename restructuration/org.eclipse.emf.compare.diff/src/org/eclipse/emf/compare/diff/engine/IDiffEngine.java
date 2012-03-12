/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Victor Roldan Betancort - [352002] introduce IMatchManager
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine;

import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

/**
 * A Diff engine has the responsability to provide a diff (or delta) model from a matching model.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
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
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code> otherwise.
	 * @return the corresponding diff model
	 */
	DiffModel doDiff(MatchModel match, boolean threeWay);

	// FIXME 2.0 : the CrossReferencer is unnecessary as a parameter; we could have initialized it later on.
	/**
	 * Return a diffmodel created using the given match model. <code>crossReferencer</code> has been
	 * initialized on the whole MatchResourceSet.
	 * 
	 * @param match
	 *            the matching model
	 * @param threeWay
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code> otherwise.
	 * @param crossReferencer
	 *            This cross referencer has been initialized with the whole MatchResourceSet and can be used
	 *            to retrieve matched EObjects towards other resources.
	 * @return the corresponding diff model
	 */
	DiffModel doDiffResourceSet(MatchModel match, boolean threeWay, CrossReferencer crossReferencer);

	/**
	 * This will be called with each access from the service to the singleton instance of this engine. Clients
	 * should dispose of all caches and recorded information within this method's implementation.
	 */
	void reset();
}
