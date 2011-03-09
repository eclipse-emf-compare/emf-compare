/*******************************************************************************
 * Copyright (c) 2010, 2011  itemis AG (http://www.itemis.de)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexander Nyssen - itemis AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A Match Scope is used by the match engine to limit the range of comparison. Only objects/resources within
 * scope, as well as referenced objects directly outside (but not their contents or any of their external
 * referces) may be compared.
 * 
 * @author <a href="mailto:alexander.nyssen@itemis.de">Alexander Nyssen</a>
 * @since 1.1
 */
public interface IMatchScope {

	/**
	 * Specifies whether the given {@link EObject} should be regarded as part of the match scope or not. If
	 * the passed in {@link EObject} is contained in a {@link Resource}, it may only be regarded to be within
	 * scope, if its {@link Resource} is also specified to be in scope via {@link #isInScope(Resource)}.
	 * 
	 * @param eObject
	 *            the {@link EObject} of interest
	 * @return <code>true</code> if eObject is part of the scope, <code>false</code> otherwise
	 */
	boolean isInScope(EObject eObject);

	/**
	 * Specifies whether the given {@link Resource} should be regarded as part of the match scope or not.
	 * 
	 * @param resource
	 *            the {@link resource} of interest
	 * @return <code>true</code> if resource is part of the scope, <code>false</code> otherwise
	 */
	boolean isInScope(Resource resource);
}
