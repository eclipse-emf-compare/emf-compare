/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - bug 460780
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
 * In one particular case, we want to force the loading of any UML profile model. In order to do it, this
 * policy will compare the input URI with the URIs registered as profile in the platform. However it does not
 * handle the case of non registered dynamic profile (profile that are not registered against the UML profile
 * extension). In order to take into account such profile an approximation has been made. Any URI that as
 * thier extension file equal to ".profile.uml" will be considered as referencing a profile model and so will
 * be automatically loaded.
 * </p>
 * <p>
 * We also want to force the loading of the UML metamodel to allow resolving sufficient meta information for
 * the loaded models to properly work. To do that we check if the last segment of the normalized URI is
 * "URL.metamodel.uml".
 * </p>
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLLoadOnDemandPolicy implements ILoadOnDemandPolicy {
	/** The {@link URI} of the UML metamodel offered by the UML2 Eclipse plugins */
	private static final String PLATFORM_UML_METAMODEL_URI = "platform:/plugin/org.eclipse.uml2.uml.resources/metamodels/UML.metamodel.uml"; //$NON-NLS-1$

	/**
	 * The UML file extension. We'll react to resource sets containing at least one resource with this
	 * extension.
	 */
	protected static final String UML_EXTENSION = "uml"; //$NON-NLS-1$

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
		return isConventionalURIForUMLProfile(normalizedURI) || isUMLMetaModel(normalizedURI)
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
	protected boolean isRegisteredUMLProfile(URI normalizedURI, final URIConverter uriConverter) {
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
	protected boolean isConventionalURIForUMLProfile(URI normalizedURI) {
		URI noFragmentURI = normalizedURI.trimFragment();
		String firstFileExtension = noFragmentURI.fileExtension();
		if (UML_EXTENSION.equals(firstFileExtension)) {
			URI withoutFirstFileExtension = noFragmentURI.trimFileExtension();
			String secondFileExtension = withoutFirstFileExtension.fileExtension();
			if ("profile".equals(secondFileExtension)) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the given {@link URI} corresponds to the UML metamodel.
	 * 
	 * @param normalizedURI
	 *            input URI to test.
	 * @return {@code true} if the given {@link URI} corresponds to the UML metamodel.
	 */
	protected boolean isUMLMetaModel(URI normalizedURI) {
		URI noFragmentURI = normalizedURI.trimFragment();
		return PLATFORM_UML_METAMODEL_URI.equals(noFragmentURI.toString());
	}
}
