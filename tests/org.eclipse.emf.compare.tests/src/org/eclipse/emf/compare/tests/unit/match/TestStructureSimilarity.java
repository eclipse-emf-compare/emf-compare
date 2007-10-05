/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match;

import java.io.File;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.compare.match.statistic.similarity.StructureSimilarity;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.tests.util.EMFCompareTestCase;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Tests the methods used to compute structure and type similarity.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class TestStructureSimilarity extends EMFCompareTestCase {
	/** Full path to the model containing this test's input. */
	private static final String INPUT_MODEL_PATH = "/data/testInput.ecore";

	/** Filter that will be used to detect the relevant features of an {@link EObject}. */
	private MetamodelFilter filter;

	/** Reference to the package named "structureSimilarityTests". See model located at INPUT_MODEL_PATH. */
	private EObject inputPackage;

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		final File modelFile = new File(FileLocator.toFileURL(
				EMFCompareTestPlugin.getDefault().getBundle().getEntry(INPUT_MODEL_PATH)).getFile());
		final EObject model = ModelUtils.load(modelFile, new ResourceSetImpl());
		// index "7" points to the package "structureSimilarityTests" which contains
		// input data for these tests. See model at location INPUT_MODEL_PATH.
		final int packageIndex = 7;
		inputPackage = model.eContents().get(packageIndex);
		filter = new MetamodelFilter();
		filter.analyseModel(model);
	}

	/**
	 * TODO Work In Progress.
	 * 
	 * @throws FactoryException
	 *             Thrown if the similarity couldn't be computed. Considered a failed test.
	 */
	public void testRelationsSimilarityMetricsSimilarPackages() throws FactoryException {
		final EObject defaultPackage = inputPackage.eContents().get(0);
		final EObject similarPackage = inputPackage.eContents().get(1);
		
		System.out.println(StructureSimilarity.relationsSimilarityMetric(
				defaultPackage.eContents().get(0), defaultPackage.eContents().get(0), filter));
		System.out.println(StructureSimilarity.relationsSimilarityMetric(
				defaultPackage.eContents().get(0), defaultPackage.eContents().get(1), filter));
		System.out.println(StructureSimilarity.relationsSimilarityMetric(
				defaultPackage.eContents().get(1), defaultPackage.eContents().get(0), filter));
	}
}
