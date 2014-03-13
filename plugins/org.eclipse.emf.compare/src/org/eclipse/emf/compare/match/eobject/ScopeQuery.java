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
package org.eclipse.emf.compare.match.eobject;

import org.eclipse.emf.ecore.EObject;

/**
 * A class implementing this contract should have the ability to query the scope and tell, from a given
 * EObject, if it is in the scope or not. Implementation should not trigger any kind of proxy resolution.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public interface ScopeQuery {
	/**
	 * Check whether the object is in the scope or not.
	 * 
	 * @param any
	 *            any EObject.
	 * @return true if the Object is in scope. False otherwise.
	 */
	boolean isInScope(EObject any);
}
