/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.saveparameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.SaveParameterHook;
import org.eclipse.emf.compare.diagram.papyrus.tests.util.PapyrusSaveParameterUtil;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Test;

/**
 * This class tests the functionality of the SaveParameterHook responsible for integrating the Papyrus Save
 * Parameters into EMF Compare.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class SaveParameterHookTest {

	/**
	 * Tests the {@link SaveParameterHook#isHookFor(java.util.Collection)} to return {@code true} if any
	 * possible Papyrus file is contained in the given collection.
	 */
	@Test
	public void testIsHookFor() {
		final SaveParameterHook saveParameterHook = new SaveParameterHook();

		final Set<URI> uris = new HashSet<URI>();
		assertFalse(saveParameterHook.isHookFor(uris));

		final URI diURI = URI.createURI("model.di");
		uris.add(diURI);
		assertTrue(saveParameterHook.isHookFor(uris));

		uris.remove(diURI);
		uris.add(URI.createURI("noPapyrusFile"));
		assertFalse(saveParameterHook.isHookFor(uris));

		uris.add(URI.createURI("model.notation"));
		assertTrue(saveParameterHook.isHookFor(uris));

		uris.add(diURI);
		assertTrue(saveParameterHook.isHookFor(uris));
	}

	/**
	 * Tests the {@link SaveParameterHook#postLoadingHook(ResourceSet, java.util.Collection)} to correctly
	 * return the Papyrus Save Parameters for present Papyrus files.
	 */
	@Test
	public void testPostLoadingHookPapyrus() {
		final SaveParameterHook saveParameterHook = new SaveParameterHook();

		final ResourceSet resourceSet = new ResourceSetImpl();
		final XMIResourceFactoryImpl resourceFactory = new XMIResourceFactoryImpl();

		final List<URI> uriCollection = new ArrayList<URI>(3);

		final URI diURI = URI.createURI("model.di");
		final URI notationURI = URI.createURI("model.notation");
		final URI umlURI = URI.createURI("model.uml");

		uriCollection.add(diURI);
		uriCollection.add(notationURI);
		uriCollection.add(umlURI);

		final XMIResource diResource = (XMIResource)resourceFactory.createResource(diURI);
		final XMIResource notationResource = (XMIResource)resourceFactory.createResource(notationURI);
		final XMIResource umlResource = (XMIResource)resourceFactory.createResource(umlURI);
		resourceSet.getResources().add(diResource);
		resourceSet.getResources().add(notationResource);
		resourceSet.getResources().add(umlResource);

		saveParameterHook.postLoadingHook(resourceSet, uriCollection);

		final Map<?, ?> diSaveParameters = diResource.getDefaultSaveOptions();
		final Map<?, ?> notationSaveParameters = notationResource.getDefaultSaveOptions();
		final Map<?, ?> umlSaveParameters = umlResource.getDefaultSaveOptions();

		final Map<?, ?> papyrusDiSaveParameters = PapyrusSaveParameterUtil.getDISaveParameter();
		final Map<?, ?> papyrusNotationSaveParameters = PapyrusSaveParameterUtil.getNotationSaveParameter();
		final Map<?, ?> papyrusUmlSaveParameters = PapyrusSaveParameterUtil.getUMLSaveParameter();

		assertTrue(PapyrusSaveParameterUtil.isEqual(diSaveParameters, papyrusDiSaveParameters));
		assertTrue(PapyrusSaveParameterUtil.isEqual(notationSaveParameters, papyrusNotationSaveParameters));
		assertTrue(PapyrusSaveParameterUtil.isEqual(umlSaveParameters, papyrusUmlSaveParameters));
	}

	/**
	 * Tests the {@link SaveParameterHook#postLoadingHook(ResourceSet, java.util.Collection)} to not modify
	 * the given resources if no Papyrus file is present.
	 */
	@Test
	public void testPostLoadingHookNonPapyrus() {
		final SaveParameterHook saveParameterHook = new SaveParameterHook();

		final ResourceSet resourceSet = new ResourceSetImpl();
		final XMIResourceFactoryImpl resourceFactory = new XMIResourceFactoryImpl();

		final List<URI> uriCollection = new ArrayList<URI>(3);

		final URI someURI = URI.createURI("model.some");
		final URI umlURI = URI.createURI("model.uml");

		uriCollection.add(someURI);
		uriCollection.add(umlURI);

		final XMIResource someResource = (XMIResource)resourceFactory.createResource(someURI);
		final XMIResource umlResource = (XMIResource)resourceFactory.createResource(umlURI);
		resourceSet.getResources().add(someResource);
		resourceSet.getResources().add(umlResource);

		saveParameterHook.postLoadingHook(resourceSet, uriCollection);

		final Map<?, ?> someSaveParameters = someResource.getDefaultSaveOptions();
		final Map<?, ?> umlSaveParameters = umlResource.getDefaultSaveOptions();

		assertEquals(someSaveParameters, Collections.EMPTY_MAP);
		assertEquals(umlSaveParameters, Collections.EMPTY_MAP);
	}
}
