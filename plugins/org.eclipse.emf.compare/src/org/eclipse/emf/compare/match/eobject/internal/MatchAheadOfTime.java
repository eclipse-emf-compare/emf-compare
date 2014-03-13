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
package org.eclipse.emf.compare.match.eobject.internal;

import org.eclipse.emf.compare.match.eobject.EObjectIndex.Side;
import org.eclipse.emf.ecore.EObject;

/**
 * Implementators of this interface might provide elements to be matched "ahead of time". "Ahead of Time"
 * means : before the whole scope got resolved.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface MatchAheadOfTime {
	/**
	 * Return EObjects to match ahead of time if there are some.
	 * 
	 * @param side
	 *            the side to look for.
	 * @return the EObjects to match ahead of time.
	 */
	Iterable<EObject> getValuesToMatchAhead(Side side);
}
