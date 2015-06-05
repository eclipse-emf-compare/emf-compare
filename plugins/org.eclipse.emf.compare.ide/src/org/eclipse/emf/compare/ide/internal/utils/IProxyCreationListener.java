/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Describes the contract for a proxy creation listener as can be notifier from this pool's created parsers.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IProxyCreationListener {
	/**
	 * This will be called when a proxy is created from one of the parser pool's parsers.
	 * 
	 * @param source
	 *            The resource in which a proxy has been created towards another.
	 * @param eObject
	 *            The EObject on which some feature is going to be set with a proxy value.
	 * @param eStructuralFeature
	 *            The structural feature which value will contain a proxy.
	 * @param proxy
	 *            The actual proxy created for this eObject's feature.
	 * @param position
	 *            Position at which the proxy is going to be inserted. This will be set to <code>-1</code>
	 *            when the proxy is added at the end of the <code>eStructuralFeature</code>'s values list (for
	 *            multi-valued features) or if said feature is single-valued.
	 */
	void proxyCreated(Resource source, EObject eObject, EStructuralFeature eStructuralFeature, EObject proxy,
			int position);
}
