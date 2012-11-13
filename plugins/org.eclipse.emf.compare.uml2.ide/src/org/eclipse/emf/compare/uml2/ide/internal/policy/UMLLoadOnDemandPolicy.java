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
package org.eclipse.emf.compare.uml2.ide.internal.policy;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy;
import org.eclipse.uml2.uml.UMLPlugin;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLLoadOnDemandPolicy implements ILoadOnDemandPolicy {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy#isAuthorizing(org.eclipse.emf.compare.ide.policy.URI)
	 */
	public boolean isAuthorizing(URI uri) {
		Map<String, URI> nsURIToProfileLocationMap = UMLPlugin.getEPackageNsURIToProfileLocationMap();
		Collection<URI> profileLocations = nsURIToProfileLocationMap.values();
		for (URI profileLocation : profileLocations) {
			URI profileResourceLocation = profileLocation.trimFragment();
			if (profileResourceLocation.equals(uri)) {
				return true;
			}
		}
		return false;
	}

}
