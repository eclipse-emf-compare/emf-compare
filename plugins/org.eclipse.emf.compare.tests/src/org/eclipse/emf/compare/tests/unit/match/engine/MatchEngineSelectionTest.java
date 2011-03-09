/*******************************************************************************
 * Copyright (c) 2009, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.engine;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.engine.IMatchEngine;
import org.eclipse.emf.compare.match.service.MatchEngineDescriptor;
import org.eclipse.emf.compare.match.service.MatchEngineRegistry;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.tests.util.EcoreModelUtils;
import org.eclipse.emf.compare.util.ModelIdentifier;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This will test the behavior of the match engine selection.
 * 
 * @author <a href="mailto:gonzague.reydet@obeo.fr">Gonzague Reydet</a>
 */
public class MatchEngineSelectionTest extends TestCase {
	/** Full path to the model containing this test's input. */
	private static final String INPUT_MODEL_PATH = "/inputs/genmodel/attributeChange/v1.genmodel"; //$NON-NLS-1$

	/** Model that contains the test's input. */
	private EObject inputModelFile;

	/** This is the resource holding the first model we'll use to test the match engine selection. */
	private Resource testResource1;

	/** This is the resource holding the second model we'll use to test the match engine selection. */
	private Resource testResource2;

	/**
	 * Test the engine selection for a dynamic resource.
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void testEngineSelectionForDynamicResource() throws FactoryException {
		testResource1 = EcoreModelUtils.createModel(1, 1).eResource();

		final ModelIdentifier identifier = new ModelIdentifier(testResource1);
		assertEquals("ecore", identifier.getExtension()); //$NON-NLS-1$

		final IMatchEngine engine = MatchService.getBestMatchEngine(testResource1);
		assertEquals("GenericMatchEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$
	}

	/**
	 * Test the engine selection for namespace and namespace pattern.
	 */
	public void testEngineSelectionForFileResource() {
		final ModelIdentifier identifier = new ModelIdentifier(inputModelFile.eResource());
		assertEquals("http://www.eclipse.org/emf/2002/GenModel", identifier.getNamespace()); //$NON-NLS-1$
		assertEquals("genmodel", identifier.getExtension()); //$NON-NLS-1$

		final IMatchEngine engine = MatchService.getBestMatchEngine(inputModelFile.eResource());
		assertEquals("DEngine", engine.getClass().getSimpleName()); //$NON-NLS-1$

		final List<MatchEngineDescriptor> descriptors = MatchEngineRegistry.INSTANCE
				.getDescriptors(identifier);
		assertEquals("DEngine", descriptors.get(0).getEngineInstance().getClass().getSimpleName()); //$NON-NLS-1$
		assertEquals("EEngine", descriptors.get(1).getEngineInstance().getClass().getSimpleName()); //$NON-NLS-1$
		assertEquals("GenericMatchEngine", descriptors.get(2).getEngineInstance().getClass().getSimpleName()); //$NON-NLS-1$
		assertEquals(3, descriptors.size());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void tearDown() {
		// voids the testResources (and hopes gc passes by ... should we hint at it here with System.gc?)
		if (testResource1 != null) {
			testResource1.getContents().clear();
		}
		if (testResource2 != null) {
			testResource2.getContents().clear();
		}
		testResource1 = null;
		testResource2 = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		final File modelFile = new File(FileLocator.toFileURL(
				EMFCompareTestPlugin.getDefault().getBundle().getEntry(INPUT_MODEL_PATH)).getFile());
		inputModelFile = ModelUtils.load(modelFile, new ResourceSetImpl());
	}
}
