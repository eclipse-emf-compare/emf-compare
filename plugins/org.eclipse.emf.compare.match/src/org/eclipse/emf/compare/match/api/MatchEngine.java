/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.match.api;

import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.ecore.EObject;

/**
 * A Match Engine is responsible for returning a match model from a set of
 * models. The resulting match model is then used to create a diff between the two models
 * 
 * @author Cédric Brun <cedric.brun@obeo.fr>
 * 
 */
public interface MatchEngine {

	/**
	 * This method returns a 2 models match. 
	 * 
	 * @param leftRoot left model
	 * @param rightRoot right model
	 * @return the corresponding match
	 */
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot);

}