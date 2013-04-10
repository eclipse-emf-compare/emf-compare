/**
 * Copyright (c) 2012 Obeo.
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
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
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
public class IndividualDiffInputData extends DiagramInputData {
	
	public Resource getA1EdgeChangeLeft() throws IOException {
		return loadFromClassLoader("edges/a1/left.notation");
	}

	public Resource getA1EdgeChangeRight() throws IOException {
		return loadFromClassLoader("edges/a1/right.notation");
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
