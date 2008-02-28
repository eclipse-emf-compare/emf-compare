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
import java.util.Iterator;

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
 * Tests the behavior of
 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with objects known
 * to be distinct.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class RelationsSimilarityDistinctTest extends TestCase {
	/** Similarities higher than this threshold will be considered too high for distinct objects. */
	private static final double HIGH_FILTERED_SIMILARITY_THRESHOLD = 0.4d;

	/**
	 * Defines a higher threshold for relations similarity when computing without filtering unused (thus
	 * identical) features.
	 */
	private static final double HIGH_UNFILTERED_SIMILARITY_THRESHOLD = 0.6d;

	/** Full path to the model containing this test's input. */
	private static final String INPUT_MODEL_PATH = "/data/testInput.ecore";

	/** Message displayed when an unexpected {@link FactoryException} is raised. */
	private static final String MESSAGE_FACTORY_UNEXPECTED = "Unexpected FactoryException has been thrown by relationsSimilarityMetrics.";

	/** Filter that will be used to detect the relevant features of an {@link EObject}. */
	private MetamodelFilter filter;

	/** Reference to the package named "structureSimilarityTests". See model located at INPUT_MODEL_PATH. */
	private EObject inputPackage;

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with
	 * filtering. Input EObjects are the contents of the packages &quot;similar&quot; (first) and
	 * &quot;totallydifferent&quot;.
	 */
	public void testFilteredRelationsSimilarityTotallyDifferentObjects() {
		final EObject defaultPackage = inputPackage.eContents().get(0);
		final EObject totallyDifferentPackage = inputPackage.eContents().get(2);

		final Iterator<EObject> defaultIterator = defaultPackage.eAllContents();
		while (defaultIterator.hasNext()) {
			final EObject defaultObject = defaultIterator.next();
			final Iterator<EObject> differentIterator = totallyDifferentPackage.eAllContents();
			while (differentIterator.hasNext()) {
				final EObject differentObject = differentIterator.next();
				try {
					final double similarity = StructureSimilarity.relationsSimilarityMetric(defaultObject,
							differentObject, filter);
					assertTrue(
							"Relations similarity between two totally different objects shouldn't be this high.",
							similarity < HIGH_FILTERED_SIMILARITY_THRESHOLD);
				} catch (FactoryException e) {
					fail(MESSAGE_FACTORY_UNEXPECTED);
				}
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with no
	 * filtering. Input EObjects are the contents of the packages &quot;similar&quot; (first) and
	 * &quot;totallydifferent&quot;.
	 */
	public void testUnfilteredRelationsSimilarityTotallyDifferentObjects() {
		final EObject defaultPackage = inputPackage.eContents().get(0);
		final EObject totallyDifferentPackage = inputPackage.eContents().get(2);

		final Iterator<EObject> defaultIterator = defaultPackage.eAllContents();
		while (defaultIterator.hasNext()) {
			final EObject defaultObject = defaultIterator.next();
			final Iterator<EObject> differentIterator = totallyDifferentPackage.eAllContents();
			while (differentIterator.hasNext()) {
				final EObject differentObject = differentIterator.next();
				try {
					final double similarity = StructureSimilarity.relationsSimilarityMetric(defaultObject,
							differentObject, null);
					assertTrue(
							"Relations similarity between two totally different objects shouldn't be this high.",
							similarity < HIGH_UNFILTERED_SIMILARITY_THRESHOLD);
				} catch (FactoryException e) {
					fail(MESSAGE_FACTORY_UNEXPECTED);
				}
			}
		}
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
		final EObject model = ModelUtils.load(modelFile, new ResourceSetImpl());
		// index "7" points to the package "structureSimilarityTests" which contains
		// input data for these tests. See model at location INPUT_MODEL_PATH.
		final int packageIndex = 7;
		inputPackage = model.eContents().get(packageIndex);
		filter = new MetamodelFilter();
		filter.analyseModel(model);
	}
}
