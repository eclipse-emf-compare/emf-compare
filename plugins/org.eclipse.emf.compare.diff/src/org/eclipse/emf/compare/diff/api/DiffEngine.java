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
package org.eclipse.emf.compare.diff.api;

import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.match.MatchModel;

/**
 * A Diff engine has the responsability to provide a diff (or delta) model from
 * a matching model
 * 
 * @author Cedric Brun  <a href="mailto:cedric.brun@obeo.fr ">cedric.brun@obeo.fr</a> 
 * 
 */
public interface DiffEngine {
	/**
	 * Return a diffmodel created using the match model. This implementation is
	 * a generic and simple one.
	 * 
	 * @param match
	 *            the matching model
	 * @return the corresponding diff model
	 * @throws FactoryException
	 */
	public DiffModel doDiff(MatchModel match);
}
