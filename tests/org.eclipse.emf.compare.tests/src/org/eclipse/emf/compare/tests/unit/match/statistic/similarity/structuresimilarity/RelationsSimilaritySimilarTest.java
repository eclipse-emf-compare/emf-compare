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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.compare.match.statistic.similarity.StructureSimilarity;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Tests the behavior of
 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with objects known
 * to be similar.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class RelationsSimilaritySimilarTest extends TestCase {
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
	 * filtering. Since we'll be comparing an object with itself or its clone, expects the result to be
	 * <code>1</code> each time.
	 */
	public void testFilteredRelationsSimilarityCloneObjects() {
		final EObject defaultPackage = inputPackage.eContents().get(0);

		final Iterator<EObject> defaultIterator = defaultPackage.eAllContents();
		while (defaultIterator.hasNext()) {
			final EObject defaultObject = defaultIterator.next();
			// We'll avoid generic types since we cannot copy it
			if (defaultObject instanceof EGenericType)
				continue;

			final EObject copyObject = EcoreUtil.copy(defaultObject);
			// puts copy in same container as needed
			if (defaultObject.eContainer() != null) {
				final EReference containmentReference = defaultObject.eContainmentFeature();
				try {
					EFactory.eAdd(defaultObject.eContainer(), containmentReference.getName(), copyObject);
				} catch (FactoryException e) {
					e.printStackTrace();
					fail("Could not copy Eobject.");
				}
			}

			try {
				assertEquals("Relations similarity between an Object and itself isn't 1.", 1d,
						StructureSimilarity.relationsSimilarityMetric(defaultObject, defaultObject, filter));
				assertEquals("Relations similarity between an Object and its copy isn't 1.", 1d,
						StructureSimilarity.relationsSimilarityMetric(defaultObject, copyObject, filter));
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with
	 * filtering. Since we'll be comparing two newly created Objects, expects the result to be <code>1</code>
	 * each time.
	 */
	public void testFilteredRelationsSimilarityNewObjects() {
		final List<EObject> elements = new ArrayList<EObject>();
		elements.add(DiffFactory.eINSTANCE.createMoveModelElement());
		elements.add(DiffFactory.eINSTANCE.createMoveModelElement());
		elements.add(DiffFactory.eINSTANCE.createDiffGroup());
		elements.add(DiffFactory.eINSTANCE.createDiffGroup());
		elements.add(MatchFactory.eINSTANCE.createMatch2Elements());
		elements.add(MatchFactory.eINSTANCE.createMatch2Elements());
		elements.add(EcoreFactory.eINSTANCE.createEClass());
		elements.add(EcoreFactory.eINSTANCE.createEClass());
		elements.add(EcoreFactory.eINSTANCE.createEPackage());
		elements.add(EcoreFactory.eINSTANCE.createEPackage());

		for (int i = 0; i < elements.size(); i += 2) {
			try {
				assertEquals("The relations similarity between two identical objects isn't 1.", 1d,
						StructureSimilarity.relationsSimilarityMetric(elements.get(i), elements.get(i + 1),
								filter));
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} with
	 * filtering. Input EObjects are the contents of the packages &quot;similar&quot; (first) and
	 * &quot;similar&quot; (second). Since we'll be comparing strictly similar objects, expects the result to
	 * be <code>1</code> each time.
	 */
	public void testFilteredRelationsSimilaritySimilarObjects() {
		final EObject defaultPackage = inputPackage.eContents().get(0);
		final EObject similarPackage = inputPackage.eContents().get(1);

		final Iterator<EObject> defaultIterator = defaultPackage.eAllContents();
		final Iterator<EObject> similarIterator = similarPackage.eAllContents();
		while (defaultIterator.hasNext() && similarIterator.hasNext()) {
			final EObject defaultObject = defaultIterator.next();
			final EObject similarObject = similarIterator.next();

			try {
				final double similarity = StructureSimilarity.relationsSimilarityMetric(defaultObject,
						similarObject, filter);
				assertEquals("Similar EObjects' relations similarity isn't 1.", 1d, similarity);
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} without
	 * filtering. Since we'll be comparing an object with itself or its clone, expects the result to be
	 * <code>1</code> each time.
	 */
	public void testUnfilteredRelationsSimilarityCloneObjects() {
		final EObject defaultPackage = inputPackage.eContents().get(0);

		final Iterator<EObject> defaultIterator = defaultPackage.eAllContents();
		while (defaultIterator.hasNext()) {
			final EObject defaultObject = defaultIterator.next();
			// We'll avoid generic types since we cannot copy it
			if (defaultObject instanceof EGenericType)
				continue;

			final EObject copyObject = EcoreUtil.copy(defaultObject);
			// puts copy in same container as needed
			if (defaultObject.eContainer() != null) {
				final EReference containmentReference = defaultObject.eContainmentFeature();
				try {
					EFactory.eAdd(defaultObject.eContainer(), containmentReference.getName(), copyObject);
				} catch (FactoryException e) {
					e.printStackTrace();
					fail("Could not copy Eobject.");
				}
			}

			try {
				assertEquals("Computed relations similarity between an Object and itself isn't 1.", 1d,
						StructureSimilarity.relationsSimilarityMetric(defaultObject, defaultObject, null));
				assertEquals("Computed relations similarity between an Object and its copy isn't 1.", 1d,
						StructureSimilarity.relationsSimilarityMetric(defaultObject, copyObject, null));
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} without
	 * filtering. Since we'll be comparing two newly created Objects, expects the result to be <code>1</code>
	 * each time.
	 */
	public void testUnfilteredRelationsSimilarityNewObjects() {
		final List<EObject> elements = new ArrayList<EObject>();
		elements.add(DiffFactory.eINSTANCE.createMoveModelElement());
		elements.add(DiffFactory.eINSTANCE.createMoveModelElement());
		elements.add(DiffFactory.eINSTANCE.createDiffGroup());
		elements.add(DiffFactory.eINSTANCE.createDiffGroup());
		elements.add(MatchFactory.eINSTANCE.createMatch2Elements());
		elements.add(MatchFactory.eINSTANCE.createMatch2Elements());
		elements.add(EcoreFactory.eINSTANCE.createEClass());
		elements.add(EcoreFactory.eINSTANCE.createEClass());
		elements.add(EcoreFactory.eINSTANCE.createEPackage());
		elements.add(EcoreFactory.eINSTANCE.createEPackage());

		for (int i = 0; i < elements.size(); i += 2) {
			try {
				assertEquals("The relations similarity between two identical objects isn't 1.", 1d,
						StructureSimilarity.relationsSimilarityMetric(elements.get(i), elements.get(i + 1),
								null));
			} catch (FactoryException e) {
				fail(MESSAGE_FACTORY_UNEXPECTED);
			}
		}
	}

	/**
	 * Tests behavior of
	 * {@link StructureSimilarity#relationsSimilarityMetric(EObject, EObject, MetamodelFilter)} without
	 * filtering. Input EObjects are the contents of the packages &quot;similar&quot; (first) and
	 * &quot;similar&quot; (second). Since we'll be comparing strictly similar objects, expects the result to
	 * be <code>1</code> each time.
	 */
	public void testUnfilteredRelationsSimilaritySimilarObjects() {
		final EObject defaultPackage = inputPackage.eContents().get(0);
		final EObject similarPackage = inputPackage.eContents().get(1);

		final Iterator<EObject> defaultIterator = defaultPackage.eAllContents();
		final Iterator<EObject> similarIterator = similarPackage.eAllContents();
		while (defaultIterator.hasNext() && similarIterator.hasNext()) {
			final EObject defaultObject = defaultIterator.next();
			final EObject similarObject = similarIterator.next();

			try {
				final double similarity = StructureSimilarity.relationsSimilarityMetric(defaultObject,
						similarObject, null);
				assertEquals("Similar EObjects' relations similarity isn't 1.", 1d, similarity);
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
