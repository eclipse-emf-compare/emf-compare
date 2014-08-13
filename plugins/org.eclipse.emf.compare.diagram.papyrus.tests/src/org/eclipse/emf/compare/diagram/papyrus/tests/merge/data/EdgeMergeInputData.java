/**
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.papyrus.tests.merge.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;

/**
 * Provides input models to the unit tests of the matching by id.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
@SuppressWarnings("nls")
public class EdgeMergeInputData extends DiagramInputData {
	
	public Resource getA1EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a1/left.notation");
	}

	public Resource getA1EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a1/right.notation");
	}
	
	public Resource getA2EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a2/left.notation");
	}

	public Resource getA2EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a2/right.notation");
	}
	
	public Resource getA3EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a3/left.notation");
	}

	public Resource getA3EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a3/right.notation");
	}
	
	public Resource getA4EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a4/left.notation");
	}

	public Resource getA4EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a4/right.notation");
	}
	
	public Resource getA5EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a5/left.notation");
	}

	public Resource getA5EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a5/right.notation");
	}
	
	public Resource getA6EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a6/left.notation");
	}

	public Resource getA6EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a6/right.notation");
	}
	
	public Resource getA7EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a7/left.notation");
	}

	public Resource getA7EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a7/right.notation");
	}
	
	@Override
	protected Resource loadFromClassLoader(String string) throws IOException {
		final URL fileURL = getClass().getResource(string);
		final InputStream str = fileURL.openStream();
		final URI uri = URI.createURI(fileURL.toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		getSets().add(resourceSet);

		if (!EMFPlugin.IS_RESOURCES_BUNDLE_AVAILABLE) {
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml",
					new UMLResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("notation",
					new GMFResourceFactory());
			EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
			EPackage.Registry.INSTANCE.put(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
		}

		Resource resource = resourceSet.createResource(uri);

		resource.load(str, Collections.emptyMap());
		str.close();
		
		EcoreUtil.resolveAll(resourceSet);

		return resource;
	}
}
