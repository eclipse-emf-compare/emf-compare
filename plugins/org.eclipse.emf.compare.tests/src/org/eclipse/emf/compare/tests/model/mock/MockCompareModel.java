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
package org.eclipse.emf.compare.tests.model.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * This class will be used to create a testing instance of a comparison model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class MockCompareModel {
	public Resource getLeftModel() throws IOException {
		return loadFromClassloader("extlibraryLeft.ecore");
	}

	public Resource getRightModel() throws IOException {
		return loadFromClassloader("extlibraryRight.ecore");
	}

	public Resource getOriginModel() throws IOException {
		return loadFromClassloader("extlibraryOrigin.ecore");
	}

	public Comparison createComparisonModel() throws IOException {
		return EMFCompare.compare(getLeftModel(), getRightModel(),
				getOriginModel());
	}

	/**
	 * Tries and locate a model in the current class' classpath.
	 * 
	 * @param string
	 *            Relative path to the model we seek (relative to this class).
	 * @return The loaded resource.
	 * @throws IOException
	 *             Thrown if we could not access either this class' resource, or
	 *             the file towards which <code>string</code> points.
	 */
	private Resource loadFromClassloader(String string) throws IOException {
		final URL fileURL = getClass().getResource(string);
		final InputStream str = fileURL.openStream();
		final URI uri = URI.createURI(fileURL.toString());

		Resource.Factory resourceFactory = Resource.Factory.Registry.INSTANCE
				.getFactory(uri);
		if (resourceFactory == null) {
			// Most likely a standalone run. Try with a plain XMI resource
			resourceFactory = new XMIResourceFactoryImpl();
		}

		// resourceFactory cannot be null
		Resource res = resourceFactory.createResource(uri);
		res.load(str, Collections.emptyMap());
		str.close();
		return res;
	}
}
