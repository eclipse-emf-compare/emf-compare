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
package org.eclipse.emf.compare.uml2.tests.stereotypes.data.dangling;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.ResourcesPlugin;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class DanglingStereotypeApplicationInputData extends AbstractUMLInputData {

	public Resource getCase1Left() throws IOException {
		return loadFromClassLoader("case1/left.uml"); //$NON-NLS-1$
	}

	public Resource getCase1Right() throws IOException {
		return loadFromClassLoader("case1/right.uml"); //$NON-NLS-1$
	}

	public Resource getCase2Left() throws IOException {
		return loadFromClassLoader("case2/left.uml"); //$NON-NLS-1$
	}

	public Resource getCase2Right() throws IOException {
		return loadFromClassLoader("case2/right.uml"); //$NON-NLS-1$
	}

	public Resource getCase3Left() throws IOException {
		return loadFromClassLoader("case3/left.uml"); //$NON-NLS-1$
	}

	public Resource getCase3Right() throws IOException {
		return loadFromClassLoader("case3/right.uml"); //$NON-NLS-1$
	}

	@Override
	protected Resource loadFromClassLoader(String string) throws IOException {

		final URL fileURL = getClass().getResource(string);
		final InputStream str = fileURL.openStream();
		final URI uri = URI.createURI(fileURL.toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		getSets().add(resourceSet);

		// Standalone
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			UMLResourcesUtil.init(resourceSet);

			// override wrong pathmap mapping in UMLResourcesUtil
			final URL UMLJarredFileLocation = ResourcesPlugin.class.getResource("ResourcesPlugin.class");
			String UMLJarPath = UMLJarredFileLocation.toString();
			UMLJarPath = UMLJarPath.substring(0, UMLJarPath.indexOf('!'));

			final Map<URI, URI> uriMap = URIConverter.URI_MAP;
			uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), URI.createURI(UMLJarPath
					+ "!/libraries/"));
			uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), URI.createURI(UMLJarPath
					+ "!/metamodels/"));
			uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), URI.createURI(UMLJarPath + "!/profiles/"));
		}

		Resource resource = resourceSet.createResource(uri);
		try {
			resource.load(str, Collections.emptyMap());
		} catch (IOException e) {
			// Do nothing. Let the models with errors to be load.
			// See DanglingStereotypApplicationTest.
		}
		str.close();

		return resource;
	}

}
