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
package org.eclipse.emf.compare.diagram.ide.ecoretools.tests.nodechanges.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diagram.ecoretools.tests.DiagramInputData;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;

public class NodeChangesInputData extends DiagramInputData {

	public Resource getA1Left() throws IOException {
		return loadFromClassLoader("a1/TC01.ecorediag"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassLoader("a1/TC02.ecorediag"); //$NON-NLS-1$
	}

	public Resource getA2Left() throws IOException {
		return loadFromClassLoader("a2/TC01.ecorediag"); //$NON-NLS-1$
	}

	public Resource getA2Right() throws IOException {
		return loadFromClassLoader("a2/TC02.ecorediag"); //$NON-NLS-1$
	}

	@Override
	protected Resource loadFromClassLoader(String string) throws IOException {
		final URL fileURL = getClass().getResource(string);
		final InputStream str = fileURL.openStream();
		final URI uri = URI.createURI(fileURL.toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		// sets.add(resourceSet);

		if (!EMFPlugin.IS_RESOURCES_BUNDLE_AVAILABLE) {
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
					new EcoreResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecorediag",
					new GMFResourceFactory());
			resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
			resourceSet.getPackageRegistry().put(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
		}

		Resource resource = resourceSet.createResource(uri);

		resource.load(str, Collections.emptyMap());
		str.close();

		EcoreUtil.resolveAll(resource.getResourceSet());

		return resource;
	}

}
