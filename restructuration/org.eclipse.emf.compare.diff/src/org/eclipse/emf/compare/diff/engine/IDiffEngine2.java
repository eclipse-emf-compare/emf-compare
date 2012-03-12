/*******************************************************************************
 * Copyright (c) 2011 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Victor Roldan Betancort - [352002] initial API and implementation     
 *     Obeo
 *******************************************************************************/
package org.eclipse.emf.compare.diff.engine;

import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.match.metamodel.MatchModel;

/**
 * Interface added to avoid breaking clients due to the introduction of the <code>IMatchManager</code>
 * concept.
 * 
 * @author Victor Roldan Betancort
 * @see org.eclipse.emf.compare.diff.engine.IMatchManager
 * @since 1.3
 */
public interface IDiffEngine2 extends IDiffEngine {
	/**
	 * Return a diffmodel created using the given match model. <code>IMatchManager</code> instance is provides
	 * the necessary matching info to the underlying IMatchEngine
	 * 
	 * @param match
	 *            the matching model
	 * @param threeWay
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code> otherwise.
	 * @param manager
	 *            the IMatchManager instance offers information on which EObjects have been matched or not
	 * @return the corresponding diff model
	 */
	DiffModel doDiff(MatchModel match, boolean threeWay, IMatchManager manager);

	/**
	 * Return a diffmodel created using the given match model. <code>IMatchManager</code> instance provides
	 * the necessary matching info to the underlying IMatchEngine
	 * 
	 * @param match
	 *            the matching model
	 * @param threeWay
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code> otherwise.
	 * @param manager
	 *            the IMatchManager instance offers information on which EObjects have been matched or not
	 * @return the corresponding diff model
	 */
	DiffModel doDiffResourceSet(MatchModel match, boolean threeWay, IMatchManager manager);
}
