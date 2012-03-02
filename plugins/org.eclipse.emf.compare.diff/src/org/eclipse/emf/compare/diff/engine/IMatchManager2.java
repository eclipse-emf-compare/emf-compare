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
package org.eclipse.emf.compare.diff.engine;

import org.eclipse.emf.ecore.EObject;

/**
 * Sometimes, relying on the MatchedElement is not enough. This will allow us to check whether a given EObject
 * is a remote or local Unmatched.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 * @since 1.3
 */
public interface IMatchManager2 {
	/**
	 * Returns <code>true</code> if the given element corresponds to a remote UnmatchedElement,
	 * <code>false</code> otherwise.
	 * 
	 * @param element
	 *            The element for which we need to know whether it is a remote unmatched.
	 * @return <code>true</code> if the given element corresponds to a remote UnmatchedElement,
	 *         <code>false</code> otherwise.
	 */
	boolean isRemoteUnmatched(EObject element);
}
