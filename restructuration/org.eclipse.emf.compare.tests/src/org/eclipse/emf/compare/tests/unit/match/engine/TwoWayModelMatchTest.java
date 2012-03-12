/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
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
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Tests the behavior the GenericMatchEngine's implementation of the 2-way model match.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class TwoWayModelMatchTest extends TestCase {
	/** This is the root of the first model we'll use to test the matching process. */
	private EObject testModel1;

	/** This is the root of the second model we'll use to test the matching process. */
	private EObject testModel2;

	/**
	 * Tests the behavior with two distinct EObjects (a model and its deep copy slightly modified).
	 * <p>
	 * The compared models are flat and intended to be a little bigger for this test (150 to 600 elements).
	 * Expects the matchModel to contain a mapping for each and every EObject of the test model, and an
	 * {@link UnMatchElement} for each element added in the copy.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void test2WayModelMatchDifferentBigObjects() throws FactoryException {
		final int writerCount = 150;
		final int bookPerWriterCount = 4;
		final long seed = System.nanoTime();
		testModel1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testModel2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		internalTest2WayDistinctModels();
	}

	/**
	 * Tests the behavior with two distinct EObjects (a model and its deep copy slightly modified).
	 * <p>
	 * The compared models are flat and intended to be small for this test (6 to 15 elements). Expects the
	 * matchModel to contain a mapping for each and every EObject of the test model, and an
	 * {@link UnMatchElement} for each element added in the copy.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void test2WayModelMatchDifferentSmallObjects() throws FactoryException {
		final int writerCount = 3;
		final int bookPerWriterCount = 5;
		final long seed = System.nanoTime();
		testModel1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testModel2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		internalTest2WayDistinctModels();
	}

	/**
	 * Tests the behavior with two equal EObjects (a model and its deep copy).
	 * <p>
	 * The compared models are flat and intended to be a little bigger for this test (150 to 600 elements).
	 * Expects the matchModel to contain a mapping for each and every EObject of the test model.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void test2WayModelMatchEqualBigEObjects() throws FactoryException {
		final int writerCount = 150;
		final int bookPerWriterCount = 4;
		final long seed = System.nanoTime();
		testModel1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testModel2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		internalTest2wayEqualModels();
	}

	/**
	 * Tests the behavior with two equal EObjects (a model and its deep copy).
	 * <p>
	 * The compared models are flat and intended to be small for this test (6 to 15 elements). Expects the
	 * matchModel to contain a mapping for each and every EObject of the test model.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void test2WayModelMatchEqualSmallEObjects() throws FactoryException {
		final int writerCount = 3;
		final int bookPerWriterCount = 5;
		final long seed = System.nanoTime();
		testModel1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testModel2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		internalTest2wayEqualModels();
	}

	/**
	 * Tests the behavior with <code>null</code> as the compared EObjects.
	 * <p>
	 * Expects a {@link NullPointerException} to be thrown.
	 * </p>
	 */
	public void test2WayModelMatchNullEObjects() {
		final String failNPE = "modelMatch() with null objects did not throw the expected NullPointerException.";
		final String failInterrupt = "modelMatch() with null objects threw an unexpected InterruptedException.";
		try {
			MatchService.doMatch(null, EcoreFactory.eINSTANCE.createEObject(), getOptions());
			fail(failNPE);
		} catch (final NullPointerException e) {
			// This was expected behavior
		} catch (final InterruptedException e) {
			fail(failInterrupt);
		}
		try {
			MatchService.doMatch(EcoreFactory.eINSTANCE.createEObject(), null, getOptions());
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
		if (testModel1 != null) {
			EcoreUtil.remove(testModel1);
		}
		if (testModel2 != null) {
			EcoreUtil.remove(testModel2);
		}
		testModel1 = null;
		testModel2 = null;
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

	/**
	 * This handles the modification of the given model.
	 * <p>
	 * We'll retrieve the first Writer we find (see javadoc of {@link EcoreModelUtils#createMetaModel()}).
	 * First we copy this element, modify its name and void its "writtenBooks" reference to have a new element
	 * to add to the model, then we'll modify the original's "name" attribute to see if we can still match it.
	 * </p>
	 * 
	 * @param copyModel
	 *            The model to alter.
	 */
	private void internalModifyModel(EObject copyModel) {
		try {
			EObject originalWriter = null;
			EObject newElement = null;
			for (final EObject element : copyModel.eContents()) {
				if ("Writer".equals(element.eClass().getName())) {
					originalWriter = element;
					newElement = EcoreUtil.copy(element);
					break;
				}
			}
			// Change name
			EFactory.eSet(newElement, "name", "ThisNameShouldntHaveBeenUsedYet");
			// void books
			final List<Object> values = new ArrayList<Object>();
			values.addAll(EFactory.eGetAsList(newElement, "writtenBooks"));
			for (final Object aValue : values) {
				EFactory.eRemove(newElement, "writtenBooks", aValue);
			}
			// add this new element to model
			EFactory.eAdd(copyModel, "authors", newElement);
			// modify existing element
			EFactory.eSet(originalWriter, "name", "ModifiedWriterName");
		} catch (final FactoryException e) {
			/*
			 * Shouldn't have happened if we had found a Writer as expected. Consider it a failure
			 */
			fail("Couldn't modify original model to test matching.");
		}
	}

	/**
	 * Handles the modification and comparing of the test models for distinct objects comparison.
	 * <p>
	 * This will fail if an unexpected exception is thrown, we did not find a mapping for each element of
	 * <code>testModel</code> or there were no UnMatchElements for the added objects. Externalized here to
	 * avoid copy/pasting within the two tests making use of it.
	 * </p>
	 */
	private void internalTest2WayDistinctModels() {
		internalModifyModel(testModel2);

		/*
		 * matching models
		 */
		MatchModel match = null;
		try {
			match = MatchService.doMatch(testModel1, testModel2, getOptions());
		} catch (final InterruptedException e) {
			fail("modelMatch() threw an unexpected InterruptedException.");
		}

		assertNotNull("Failed to match the two models.", match);
		// keeps compiler happy
		assert match != null;

		int elementCount = 0;
		final TreeIterator<EObject> iterator = testModel1.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
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
				fail("modelMatch() did not found a match for every element of the original model.");
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
		 * cannot be less elements since we've found a mapping for each at this point. Note that we need to
		 * add 1 to the model element count since the root hasn't been counted yet.
		 */
		assertEquals("modelMatch() found more matches than there are elements in the original model.",
				elementCount + 1, matchElementCount);

		// We should find one single UnMatchElement corresponding to the added modelElement
		assertTrue("modelMatch() did not found the unmatched element we added to the model.", match
				.getUnmatchedElements() != null
				&& match.getUnmatchedElements().size() == 1);
	}

	/**
	 * Handles the comparing of the test models for equal objects comparison.
	 * <p>
	 * This will fail if an unexpected exception is thrown or we did not find a mapping for each element of
	 * the model. Externalized here to avoid copy/pasting within the two tests making use of it.
	 * </p>
	 */
	private void internalTest2wayEqualModels() {
		try {
			final MatchModel match = MatchService.doMatch(testModel1, testModel2, getOptions());

			int elementCount = 0;
			final TreeIterator<EObject> iterator = testModel1.eAllContents();
			while (iterator.hasNext()) {
				final EObject next = iterator.next();
				boolean found = false;
				final TreeIterator<EObject> matchIterator = match.eAllContents();
				while (matchIterator.hasNext()) {
					final EObject nextMatch = matchIterator.next();
					if (((Match2Elements)nextMatch).getLeftElement().equals(next)) {
						found = true;
						break;
					}
				}
				if (!found) {
					fail("modelMatch() did not found a match for every element of two equal EObjects.");
				}
				elementCount++;
			}

			int matchElementCount = 0;
			final TreeIterator<EObject> matchIterator = match.eAllContents();
			while (matchIterator.hasNext()) {
				matchIterator.next();
				matchElementCount++;
			}

			// cannot be less elements since we've found a mapping for each at this point.
			// Note that we need to add 1 to the model element count since the root hasn't been counted yet.
			assertEquals("modelMatch() found more matches than there are elements in the model.",
					elementCount + 1, matchElementCount);
		} catch (final InterruptedException e) {
			fail("modelMatch() threw an unexpected InterruptedException.");
		}
	}
}
