/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.tests.util.EcoreModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Tests the behavior of the GenericMatchEngine's implementation of the 3-way content match.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class ThreeWayContentMatchTest extends TestCase {
	/** Name of the metamodel's &quot;Writer&quot; class. */
	private static final String WRITER_CLASS_NAME = "Writer";

	/** This is the EObject that will be used as the left one for the matching process. */
	private EObject testEObject1;

	/** This is the EObject that will be used as the right one for the matching process. */
	private EObject testEObject2;

	/**
	 * Tests the behavior with three distinct EObjects.
	 * <p>
	 * A model and its slightly modified deep copy are taken as roots, then we iterate through their content
	 * and match EObjects one after the other. A deep copy of the original model will be used as common
	 * ancestor.
	 * </p>
	 * <p>
	 * The compared models are flat and intended to be small for this test (6 to 15 elements). Since the
	 * compared EObject are totally distinct, expects the match model to contain an UnmatchedElement for each
	 * object contained within the compared objects.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 * @throws InterruptedException
	 *             Won't be thrown since we've got no progress monitor.
	 */
	public void test3WayContentMatchDifferentObjects() throws FactoryException, InterruptedException {
		final int writerCount = 3;
		final int bookPerWriterCount = 5;
		final long seed = System.nanoTime();
		testEObject1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testEObject2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);

		/*
		 * We'll create the list of the EObjects to be compared : all the writers of the library as "left"
		 * elements, all the books in the library as "right" elements. Writers will also be considered as
		 * common ancestors.
		 */
		final List<EObject> eObjects1 = new ArrayList<EObject>();
		final List<EObject> eObjects2 = new ArrayList<EObject>();
		final TreeIterator<EObject> iteratorObj1 = testEObject1.eAllContents();
		while (iteratorObj1.hasNext()) {
			final EObject next = iteratorObj1.next();
			if (next.eClass().getName().matches(WRITER_CLASS_NAME)) {
				eObjects1.add(next);
			}
		}
		final TreeIterator<EObject> iteratorObj2 = testEObject2.eAllContents();
		while (iteratorObj2.hasNext()) {
			final EObject next = iteratorObj2.next();
			if (next.eClass().getName().matches("Book")) {
				eObjects2.add(next);
			}
		}

		// now tests the matching process
		for (int i = 0; i < eObjects1.size(); i++) {
			final EObject obj1 = eObjects1.get(i);
			for (int j = 0; j < eObjects2.size(); j++) {
				final EObject obj2 = eObjects2.get(j);

				final MatchModel match = MatchService.doContentMatch(obj1, obj2, obj1, getOptions());
				assertNotNull("Failed to match the three objects.", match);

				int elementCount = 0;
				final TreeIterator<EObject> iterator1 = obj1.eAllContents();
				while (iterator1.hasNext()) {
					final EObject next = iterator1.next();
					boolean found = false;
					final TreeIterator<EObject> matchIterator = match.eAllContents();
					while (matchIterator.hasNext()) {
						final EObject nextMatch = matchIterator.next();
						if (nextMatch instanceof UnmatchElement
								&& ((UnmatchElement)nextMatch).getElement().equals(next)) {
							found = true;
							break;
						}
					}
					if (!found) {
						fail("contentMatch() did not found an unmatch for every element of the original object.");
					}
					elementCount++;
				}
				final TreeIterator<EObject> iterator2 = obj2.eAllContents();
				while (iterator2.hasNext()) {
					final EObject next = iterator2.next();
					boolean found = false;
					final TreeIterator<EObject> matchIterator = match.eAllContents();
					while (matchIterator.hasNext()) {
						final EObject nextMatch = matchIterator.next();
						if (nextMatch instanceof UnmatchElement
								&& ((UnmatchElement)nextMatch).getElement().equals(next)) {
							found = true;
							break;
						}
					}
					if (!found) {
						fail("contentMatch() did not found an unmatch for every element of the original object.");
					}
					elementCount++;
				}

				// Note : need to add 3 to the element count this none of the three roots has been counted yet
				assertEquals("contentMatch() shouldn't have found a match element.", elementCount + 3, match
						.getUnmatchedElements().size());

				// We shouldn't find a single MatchElement
				assertTrue("contentMatch() found a matched element in the compared objects",
						match.getMatchedElements() == null || match.getMatchedElements().size() == 0);
			}
		}
	}

	/**
	 * Tests the behavior with three equal EObjects.
	 * <p>
	 * A model and its deep copy are taken as roots, then we iterate through their content and match EObjects
	 * one after the other. The original model's deep copy will be considered common ancestor.
	 * </p>
	 * <p>
	 * The compared models are flat and intended to be small for this test (6 to 15 elements). Expects the
	 * matchModel to contain a mapping for each and every EObject of the test model.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 * @throws InterruptedException
	 *             Won't be thrown: we're not using Progress monitors here.
	 */
	public void test3WayContentMatchEqualEObjects() throws FactoryException, InterruptedException {
		final int writerCount = 3;
		final int bookPerWriterCount = 5;
		final long seed = System.nanoTime();
		testEObject1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testEObject2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);

		/*
		 * We'll create the list of the EObjects to be compared : all the writers and books contained by the
		 * library. We won't compare the library itself (this is handled with modelMatch's tests) or the
		 * objects' structural features.
		 */
		final List<EObject> eObjects1 = new ArrayList<EObject>();
		final List<EObject> eObjects2 = new ArrayList<EObject>();
		final TreeIterator<EObject> iterator1 = testEObject1.eAllContents();
		while (iterator1.hasNext()) {
			final EObject next = iterator1.next();
			if (next.eClass().getName().matches(WRITER_CLASS_NAME + "|Book")) {
				eObjects1.add(next);
			}
		}
		final TreeIterator<EObject> iterator2 = testEObject2.eAllContents();
		while (iterator2.hasNext()) {
			final EObject next = iterator2.next();
			if (next.eClass().getName().matches(WRITER_CLASS_NAME + "|Book")) {
				eObjects2.add(next);
			}
		}

		assertEquals("There isn't the same number of element in both of the objects.", eObjects1.size(),
				eObjects2.size());

		// now tests the matching process
		for (int i = 0; i < eObjects1.size(); i++) {
			final EObject obj1 = eObjects1.get(i);
			final EObject obj2 = eObjects2.get(i);

			final MatchModel match = MatchService.doContentMatch(obj1, obj2, obj1, getOptions());
			assertNotNull("Failed to match the three objects.", match);

			int elementCount = 0;
			final TreeIterator<EObject> iteratorObj1 = obj1.eAllContents();
			while (iteratorObj1.hasNext()) {
				final EObject next = iteratorObj1.next();
				boolean found = false;
				final TreeIterator<EObject> matchIterator = match.eAllContents();
				while (matchIterator.hasNext()) {
					final EObject nextMatch = matchIterator.next();
					if (nextMatch instanceof Match2Elements
							&& ((Match2Elements)nextMatch).getLeftElement().equals(next)
							|| nextMatch instanceof UnmatchElement
							&& ((UnmatchElement)nextMatch).getElement().equals(next)) {
						found = true;
						break;
					}
				}
				if (!found) {
					fail("contentMatch() did not found a match for every element of the original object.");
				}
				elementCount++;
			}

			int matchElementCount = 0;
			final TreeIterator<EObject> matchIterator = match.eAllContents();
			while (matchIterator.hasNext()) {
				if (matchIterator.next() instanceof Match2Elements) {
					matchElementCount++;
				}
			}

			/*
			 * cannot be less elements since we've found a mapping for each at this point. Note that we need
			 * to add 1 to the element count since the object itself hasn't been counted yet.
			 */
			assertEquals("contentMatch() found more matches than there are elements in the original object.",
					elementCount + 1, matchElementCount);

			// We shouldn't find a single UnMatchElement
			assertTrue("contentMatch() found an unmatched element in the compared objects",
					match.getUnmatchedElements() == null || match.getUnmatchedElements().size() == 0);
		}
	}

	/**
	 * Tests the behavior with <code>null</code> as the compared EObjects.
	 * <p>
	 * Expects a {@link NullPointerException} to be thrown.
	 * </p>
	 */
	public void test3WayContentMatchNullEObjects() {
		final String failNPE = "contentMatch() with null objects did not throw the expected NullPointerException.";
		final String failInterrupt = "modelMatch() with null objects threw an unexpected InterruptedException.";
		try {
			MatchService.doContentMatch(null, EcoreFactory.eINSTANCE.createEObject(),
					EcoreFactory.eINSTANCE.createEObject(), getOptions());
			fail(failNPE);
		} catch (final NullPointerException e) {
			// This was expected behavior
		} catch (final InterruptedException e) {
			fail(failInterrupt);
		}
		try {
			MatchService.doContentMatch(EcoreFactory.eINSTANCE.createEObject(), null,
					EcoreFactory.eINSTANCE.createEObject(), getOptions());
			fail(failNPE);
		} catch (final NullPointerException e) {
			// This was expected behavior
		} catch (final InterruptedException e) {
			fail(failInterrupt);
		}
		try {
			MatchService.doContentMatch(EcoreFactory.eINSTANCE.createEObject(),
					EcoreFactory.eINSTANCE.createEObject(), null, getOptions());
			fail(failNPE);
		} catch (final NullPointerException e) {
			// This was expected behavior
		} catch (final InterruptedException e) {
			fail(failInterrupt);
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
		if (testEObject1 != null) {
			EcoreUtil.remove(testEObject1);
		}
		if (testEObject2 != null) {
			EcoreUtil.remove(testEObject2);
		}
		testEObject1 = null;
		testEObject2 = null;
	}

	/**
	 * This will return the map of options to be used for comparisons within this test class.
	 * 
	 * @return Default options for matching.
	 */
	private Map<String, Object> getOptions() {
		final Map<String, Object> options = new HashMap<String, Object>();
		options.put(MatchOptions.OPTION_DISTINCT_METAMODELS, Boolean.TRUE);
		return options;
	}
}
