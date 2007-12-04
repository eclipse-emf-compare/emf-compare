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
package org.eclipse.emf.compare.tests.unit.match.statistic.differencesservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.UnMatchElement;
import org.eclipse.emf.compare.match.statistic.DifferencesServices;
import org.eclipse.emf.compare.tests.util.EcoreModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

// TODO testing : are all pathes explored for two way content matching?
/**
 * Tests the behavior of
 * {@link DifferencesServices#contentMatch(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class TestTwoWayContentMatch extends TestCase {
	/** Name of the metamodel's &quot;Writer&quot; class. */
	private static final String WRITER_CLASS_NAME = "Writer";

	/** This is the EObject that will be used as the left one for the matching process. */
	private EObject testEObject1;

	/** This is the EObject that will be used as the right one for the matching process. */
	private EObject testEObject2;

	/**
	 * Tests the behavior of {@link DifferencesServices#contentMatch(EObject, EObject, java.util.Map)} with
	 * two distinct EObjects.
	 * <p>
	 * A model and its slightly modified deep copy are taken as roots, then we iterate through their content
	 * and match EObjects one after the other.
	 * </p>
	 * <p>
	 * The compared models are flat and intended to be small for this test (6 to 15 elements). Since the
	 * compared EObject are totally distinct, expects the match model to contain an UnmatchedElement for each
	 * object contained within the compared objects.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void test2WayContentMatchDifferentObjects() throws FactoryException {
		final int writerCount = 3;
		final int bookPerWriterCount = 5;
		final long seed = System.nanoTime();
		testEObject1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testEObject2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);

		/*
		 * We'll create the list of the EObjects to be compared : all the writers of the library as "left"
		 * elements, all the books in the library as "right" elements.
		 */
		final List<EObject> eObjects1 = new ArrayList<EObject>();
		final List<EObject> eObjects2 = new ArrayList<EObject>();
		for (final TreeIterator<EObject> iterator = testEObject1.eAllContents(); iterator.hasNext(); ) {
			final EObject next = iterator.next();
			if (next.eClass().getName().matches(WRITER_CLASS_NAME))
				eObjects1.add(next);
		}
		for (final TreeIterator<EObject> iterator = testEObject2.eAllContents(); iterator.hasNext(); ) {
			final EObject next = iterator.next();
			if (next.eClass().getName().matches("Book"))
				eObjects2.add(next);
		}

		assertFalse("There shouldn't be the same number of element in both of the objects.",
				eObjects1.size() == eObjects2.size());

		// now tests the matching process
		for (int i = 0; i < eObjects1.size(); i++) {
			final EObject obj1 = eObjects1.get(i);
			for (int j = 0; j < eObjects2.size(); j++) {
				final EObject obj2 = eObjects2.get(j);

				final MatchModel match = new DifferencesServices().contentMatch(obj1, obj2, Collections
						.<String, Object> emptyMap());
				assertNotNull("Failed to match the two objects.", match);

				int elementCount = 0;
				for (final TreeIterator<EObject> iterator = obj1.eAllContents(); iterator.hasNext(); ) {
					final EObject next = iterator.next();
					boolean found = false;
					for (final TreeIterator<EObject> matchIterator = match.eAllContents(); matchIterator
							.hasNext(); ) {
						final EObject nextMatch = matchIterator.next();
						if (nextMatch instanceof UnMatchElement
								&& ((UnMatchElement)nextMatch).getElement().equals(next)) {
							found = true;
							break;
						}
					}
					if (!found)
						fail("contentMatch() did not found an unmatch for every element of the original object.");
					elementCount++;
				}
				for (final TreeIterator<EObject> iterator = obj2.eAllContents(); iterator.hasNext(); ) {
					final EObject next = iterator.next();
					boolean found = false;
					for (final TreeIterator<EObject> matchIterator = match.eAllContents(); matchIterator
							.hasNext(); ) {
						final EObject nextMatch = matchIterator.next();
						if (nextMatch instanceof UnMatchElement
								&& ((UnMatchElement)nextMatch).getElement().equals(next)) {
							found = true;
							break;
						}
					}
					if (!found)
						fail("contentMatch() did not found an unmatch for every element of the original object.");
					elementCount++;
				}

				int unmatchElementCount = 0;
				for (final TreeIterator<EObject> matchIterator = match.eAllContents(); matchIterator
						.hasNext(); ) {
					if (matchIterator.next() instanceof UnMatchElement)
						unmatchElementCount++;
				}

				// Note : need to add 2 to the element count this none of the two roots has been counted yet
				assertEquals("contentMatch() found shouldn't have found a match element.", elementCount + 2,
						unmatchElementCount);

				// We shouldn't find a single MatchElement
				assertTrue("contentMatch() found a matched element in the compared objects", match
						.getMatchedElements() == null
						|| match.getMatchedElements().size() == 0);
			}
		}
	}

	/**
	 * Tests the behavior of {@link DifferencesServices#contentMatch(EObject, EObject, java.util.Map)} with
	 * two equal EObjects.
	 * <p>
	 * A model and its deep copy are taken as roots, then we iterate through their content and match EObjects
	 * one after the other.
	 * </p>
	 * <p>
	 * The compared models are flat and intended to be small for this test (6 to 15 elements). Expects the
	 * matchModel to contain a mapping for each and every EObject of the test model.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void test2WayContentMatchEqualEObjects() throws FactoryException {
		final int writerCount = 3;
		final int bookPerWriterCount = 5;
		final long seed = System.nanoTime();
		testEObject1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testEObject2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);

		/*
		 * We'll create the list of the EObjects to be compared : all the writers and books contained by the
		 * library. We won't compare the library itself (this test is handled with modelMatch's tests) or the
		 * objects' structural features.
		 */
		final List<EObject> eObjects1 = new ArrayList<EObject>();
		final List<EObject> eObjects2 = new ArrayList<EObject>();
		eObjects1.add(testEObject1);
		eObjects2.add(testEObject2);
		for (final TreeIterator<EObject> iterator = testEObject1.eAllContents(); iterator.hasNext(); ) {
			final EObject next = iterator.next();
			if (next.eClass().getName().matches(WRITER_CLASS_NAME + "|Book"))
				eObjects1.add(next);
		}
		for (final TreeIterator<EObject> iterator = testEObject2.eAllContents(); iterator.hasNext(); ) {
			final EObject next = iterator.next();
			if (next.eClass().getName().matches(WRITER_CLASS_NAME + "|Book"))
				eObjects2.add(next);
		}

		assertEquals("There isn't the same number of element in both of the objects.", eObjects1.size(),
				eObjects2.size());

		// now tests the matching process
		for (int i = 0; i < eObjects1.size(); i++) {
			final EObject obj1 = eObjects1.get(i);
			final EObject obj2 = eObjects2.get(i);

			final MatchModel match = new DifferencesServices().contentMatch(obj1, obj2, Collections
					.<String, Object> emptyMap());
			assertNotNull("Failed to match the two objects.", match);

			int elementCount = 0;
			for (final TreeIterator<EObject> iterator = obj1.eAllContents(); iterator.hasNext(); ) {
				final EObject next = iterator.next();
				boolean found = false;
				for (final TreeIterator<EObject> matchIterator = match.eAllContents(); matchIterator
						.hasNext(); ) {
					final EObject nextMatch = matchIterator.next();
					if (nextMatch instanceof Match2Elements
							&& ((Match2Elements)nextMatch).getLeftElement().equals(next)
							|| (nextMatch instanceof UnMatchElement && ((UnMatchElement)nextMatch)
									.getElement().equals(next))) {
						found = true;
						break;
					}
				}
				if (!found)
					fail("contentMatch() did not found a match for every element of the original object.");
				elementCount++;
			}

			int matchElementCount = 0;
			for (final TreeIterator<EObject> matchIterator = match.eAllContents(); matchIterator.hasNext(); ) {
				if (matchIterator.next() instanceof Match2Elements)
					matchElementCount++;
			}

			/*
			 * cannot be less elements since we've found a mapping for each at this point. Note that we need
			 * to add 1 to the element count since the object itself hasn't been counted yet.
			 */
			assertEquals("contentMatch() found more matches than there are elements in the original object.",
					elementCount + 1, matchElementCount);

			// We shouldn't find a single UnMatchElement
			assertTrue("contentMatch() found an unmatched element in the compared objects", match
					.getUnMatchedElements() == null
					|| match.getUnMatchedElements().size() == 0);
		}
	}

	/**
	 * Tests the behavior of {@link DifferencesServices#contentMatch(EObject, EObject, java.util.Map)} with
	 * <code>null</code> as the compared EObjects.
	 * <p>
	 * Expects a {@link NullPointerException} to be thrown.
	 * </p>
	 */
	public void test2WayContentMatchNullEObjects() {
		final MatchEngine service = new DifferencesServices();
		final String failNPE = "contentMatch() with null objects did not throw the expected NullPointerException.";
		try {
			service.contentMatch(null, EcoreFactory.eINSTANCE.createEObject(), Collections
					.<String, Object> emptyMap());
			fail(failNPE);
		} catch (NullPointerException e) {
			// This was expected behavior
		}
		try {
			service.contentMatch(EcoreFactory.eINSTANCE.createEObject(), null, Collections
					.<String, Object> emptyMap());
			fail(failNPE);
		} catch (NullPointerException e) {
			// This was expected behavior
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void tearDown() {
		// voids the testModels (and hopes gc passes by ... should we hint at it here with System.gc?)
		if (testEObject1 != null)
			EcoreUtil.remove(testEObject1);
		if (testEObject2 != null)
			EcoreUtil.remove(testEObject2);
		testEObject1 = null;
		testEObject2 = null;
	}
}
