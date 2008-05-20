/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.statistic.similarity.structuresimilarity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.compare.match.statistic.similarity.StructureSimilarity;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Tests {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} to ensure it
 * behaves as expected.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class RelationsSimilarityBehaviorTest extends TestCase {
	/** Full path to the model containing this test's input. */
	private static final String INPUT_MODEL_PATH = "/data/testInput.ecore";

	/** Message displayed when an unexpected {@link FactoryException} is raised. */
	private static final String MESSAGE_FACTORY_UNEXPECTED = "Unexpected FactoryException has been thrown by relationsSimilarityMetrics.";

	/** Message displayed when an expected {@link NullPointerException} is raised. */
	private static final String MESSAGE_NULLPOINTER_EXPECTED = "Expected NullPointerException hasn't been thrown by relationsSimilarityMetrics.";

	/** Filter that will be used to detect the relevant features of an {@link EObject}. */
	private MetamodelFilter filter;

	/** List of the Objects we'll iterate through for these tests. */
	private List<EObject> inputList = new ArrayList<EObject>();

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with
	 * filtering. Since we pass <code>null</code> as the first or second object, expects a
	 * {@link NullPointerException} to be thrown.
	 */
	public void testFilteredRelationsSimilarityNullObjects() {
		for (int i = 0; i < inputList.size(); i++) {
			try {
				StructureSimilarity.relationsSimilarityMetric(inputList.get(i), null, filter);
				fail(MESSAGE_NULLPOINTER_EXPECTED);
			} catch (NullPointerException e) {
				// This was expected behavior
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
			try {
				StructureSimilarity.relationsSimilarityMetric(null, inputList.get(i), filter);
				fail(MESSAGE_NULLPOINTER_EXPECTED);
			} catch (NullPointerException e) {
				// This was expected behavior
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with filtering
	 * on non-<code>null</code> objects. Expects the result to be comprised between <code>0</code> and
	 * <code>1</code>.
	 */
	public void testFilteredRelationsSimilarityValidObjects() {
		for (int i = 0; i < inputList.size(); i++) {
			try {
				final double similarity = StructureSimilarity.relationsSimilarityMetric(inputList.get(i),
						inputList.get(inputList.size() - i - 1), filter);
				assertTrue("Computed similarity is below 0", similarity >= 0);
				assertTrue("Computed similarity is above 1", similarity <= 1);
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} without
	 * filtering. Since we pass <code>null</code> as the first or second object, expects a
	 * {@link NullPointerException} to be thrown.
	 */
	public void testUnfilteredRelationsSimilarityNullObjects() {
		for (int i = 0; i < inputList.size(); i++) {
			try {
				StructureSimilarity.relationsSimilarityMetric(inputList.get(i), null, null);
				fail(MESSAGE_NULLPOINTER_EXPECTED);
			} catch (NullPointerException e) {
				// This was expected behavior
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
			try {
				StructureSimilarity.relationsSimilarityMetric(null, inputList.get(i), null);
				fail(MESSAGE_NULLPOINTER_EXPECTED);
			} catch (NullPointerException e) {
				// This was expected behavior
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} without
	 * filtering on non-<code>null</code> objects. Expects the result to be comprised between
	 * <code>0</code> and <code>1</code>.
	 */
	public void testUnfilteredRelationsSimilarityValidObjects() {
		for (int i = 0; i < inputList.size(); i++) {
			try {
				final double similarity = StructureSimilarity.relationsSimilarityMetric(inputList.get(i),
						inputList.get(inputList.size() - i - 1), null);
				assertTrue("Computed similarity is below 0", similarity >= 0);
				assertTrue("Computed similarity is above 1", similarity <= 1);
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() {
		try {
			final File modelFile = new File(FileLocator.toFileURL(
					EMFCompareTestPlugin.getDefault().getBundle().getEntry(INPUT_MODEL_PATH)).getFile());
			final EObject model = ModelUtils.load(modelFile, new ResourceSetImpl());
			// index "7" points to the package "structureSimilarityTests" which contains
			// input data for these tests. See model at location INPUT_MODEL_PATH.
			final int packageIndex = 7;
			final EObject inputPackage = model.eContents().get(packageIndex);

			final Iterator<EObject> it = inputPackage.eAllContents();
			while (it.hasNext())
				inputList.add(it.next());

			filter = new MetamodelFilter();
			filter.analyseModel(model);
		} catch (IOException e) {
			fail("Couldn't load input model for StructureSimilarity tests.");
		}
	}
}
