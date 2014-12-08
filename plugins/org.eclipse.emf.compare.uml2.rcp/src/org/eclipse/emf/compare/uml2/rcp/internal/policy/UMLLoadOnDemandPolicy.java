/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.internal.policy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.rcp.policy.ILoadOnDemandPolicy;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.uml2.uml.UMLPlugin;

/**
 * This policy is used to force the loading required resources by a UML model.
 * <p>
 * In this particular case, we want to force the loading of any UML profile model. In order to do it, this
 * policy will compare the input URI with the URIs registered as profile in the platform. However it does not
 * handle the case of non registered dynamic profile (profile that are not registered against the UML profile
 * extension). In order to take into account such profile an approximation has been made. Any URI that as
 * thier extension file equal to ".profile.uml" will be considered as referencing a profile model and so will
 * be automatically loaded.
 * </p>
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLLoadOnDemandPolicy implements ILoadOnDemandPolicy {
	/** Keep track of the normalizations we've already made. */
	private final BiMap<String, URI> profileNsURIToNormalized = HashBiMap.create();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.policy.ILoadOnDemandPolicy#isAuthorizing(org.eclipse.emf.compare.ide.policy.URI)
	 */
	public boolean isAuthorizing(URI uri) {
		URIConverter uriConverter = new ExtensibleURIConverterImpl();
		// Needs to normalize the URI in order to resolve URI using path map
		URI normalizedURI = uriConverter.normalize(uri);
		return isConventionalURIForUMLProfile(normalizedURI)
				|| isRegisteredUMLProfile(normalizedURI, uriConverter);
	}

	/**
	 * Returns <code>true</code> if the URI is registered in the UML profile registry.
	 * 
	 * @param normalizedURI
	 *            normalized URI to compare.
	 * @param uriConverter
	 *            {@link URIConverter} to use for the registered profile URIs.
	 * @return
	 */
	private boolean isRegisteredUMLProfile(URI normalizedURI, final URIConverter uriConverter) {
		for (Map.Entry<String, URI> entry : UMLPlugin.getEPackageNsURIToProfileLocationMap().entrySet()) {
			if (!profileNsURIToNormalized.containsKey(entry.getKey())) {
				profileNsURIToNormalized.put(entry.getKey(), uriConverter.normalize(entry.getValue()));
			}
		}
		// This should be a short-hand to inverse().containsKey(...), but HashBiMap seems to optimize
		// containsValue further
		return profileNsURIToNormalized.containsValue(normalizedURI);
	}

	/**
	 * Tries to guess if the input URI is pointing a profile URI. Any URI which as a file extension equals to
	 * ".profile.uml" will be considered as a profile URI.
	 * 
	 * @param normalizedURI
	 *            input URI to test.
	 * @return <code>true</code> if the input URI is considered as a profile URI, <code>false</code>
	 *         otherwise.
	 */
	private boolean isConventionalURIForUMLProfile(URI normalizedURI) {
		URI noFragmentURI = normalizedURI.trimFragment();
		String firstFileExtension = noFragmentURI.fileExtension();
		if ("uml".equals(firstFileExtension)) { //$NON-NLS-1$
			URI withoutFirstFileExtension = noFragmentURI.trimFileExtension();
			String secondFileExtension = withoutFirstFileExtension.fileExtension();
			if ("profile".equals(secondFileExtension)) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}
}
