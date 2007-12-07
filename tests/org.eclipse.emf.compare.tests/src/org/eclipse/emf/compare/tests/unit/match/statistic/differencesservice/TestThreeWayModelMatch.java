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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.UnMatchElement;
import org.eclipse.emf.compare.match.statistic.DifferencesServices;
import org.eclipse.emf.compare.tests.util.EcoreModelUtils;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

// TODO testing : finish implementation of these tests
/**
 * Tests the behavior of
 * {@link DifferencesServices#modelMatch(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)}
 * and
 * {@link DifferencesServices#modelMatch(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, org.eclipse.core.runtime.IProgressMonitor, java.util.Map)}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class TestThreeWayModelMatch extends TestCase {
	/** This is the root of the first model we'll use to test the matching process. */
	private EObject testModel1;

	/** This is the root of the second model we'll use to test the matching process. */
	private EObject testModel2;

	/** This is the root of the ancestor model we'll use to test the matching process. */
	private EObject testModel3;

	/**
	 * Tests the behavior of
	 * {@link DifferencesServices#modelMatch(EObject, EObject, EObject, org.eclipse.core.runtime.IProgressMonitor, java.util.Map)}
	 * and {@link DifferencesServices#modelMatch(EObject, EObject, EObject, java.util.Map)} with three
	 * distinct EObjects (a model and its deep copy slightly modified).
	 * <p>
	 * The compared models are flat and intended to be a little bigger for this test (150 to 600 elements).
	 * Expects the matchModel to contain a mapping for each and every EObject of the test model, and an
	 * {@link UnMatchElement} for each element added in the copy. There will be no conflictual change for this
	 * test.
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 */
	public void test3WayModelMatchDifferentBigObjects() throws FactoryException {
		final int writerCount = 150;
		final int bookPerWriterCount = 4;
		final long seed = System.nanoTime();
		testModel1 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testModel2 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		testModel3 = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed);
		internalTest3WayDistinctModels();
	}

	/**
	 * Tests the behavior of
	 * {@link DifferencesServices#modelMatch(EObject, EObject, EObject, org.eclipse.core.runtime.IProgressMonitor, java.util.Map)}
	 * and {@link DifferencesServices#modelMatch(EObject, EObject, EObject, java.util.Map)} with
	 * <code>null</code> as the compared EObjects.
	 * <p>
	 * Expects a {@link NullPointerException} to be thrown.
	 * </p>
	 */
	public void test3WayModelMatchNullEObjects() {
		final MatchEngine service = new DifferencesServices();
		final String failNPE = "modelMatch() with null objects did not throw the expected NullPointerException.";
		final String failInterrupt = "modelMatch() with null objects threw an unexpected InterruptedException.";
		try {
			service.modelMatch(null, EcoreFactory.eINSTANCE.createEObject(), null, new NullProgressMonitor(),
					Collections.<String, Object> emptyMap());
			fail(failNPE);
		} catch (NullPointerException e) {
			// This was expected behavior
		} catch (InterruptedException e) {
			fail(failInterrupt);
		}
		try {
			service.modelMatch(null, null, EcoreFactory.eINSTANCE.createEObject(), new NullProgressMonitor(),
					Collections.<String, Object> emptyMap());
			fail(failNPE);
		} catch (NullPointerException e) {
			// This was expected behavior
		} catch (InterruptedException e) {
			fail(failInterrupt);
		}
		try {
			service.modelMatch(null, EcoreFactory.eINSTANCE.createEObject(), (EObject)null, Collections
					.<String, Object> emptyMap());
			fail(failNPE);
		} catch (NullPointerException e) {
			// This was expected behavior
		}
		try {
			service.modelMatch(null, null, EcoreFactory.eINSTANCE.createEObject(), Collections
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
		if (testModel1 != null)
			EcoreUtil.remove(testModel1);
		if (testModel2 != null)
			EcoreUtil.remove(testModel2);
		if (testModel3 != null)
			EcoreUtil.remove(testModel3);
		testModel1 = null;
		testModel2 = null;
		testModel3 = null;
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
			for (EObject element : copyModel.eContents()) {
				if (element.eClass().getName().equals("Writer")) {
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
			for (Object aValue : values)
				EFactory.eRemove(newElement, "writtenBooks", aValue);
			// add this new element to model
			EFactory.eAdd(copyModel, "authors", newElement);
			// modify existing element
			EFactory.eSet(originalWriter, "name", "ModifiedAuthorName");
		} catch (FactoryException e) {
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
	 * avoid copy/pasting within the tests making use of it.
	 * </p>
	 */
	private void internalTest3WayDistinctModels() {
		internalModifyModel(testModel2);

		/*
		 * matching models
		 */
		MatchModel match1 = null;
		MatchModel match2 = null;
		try {
			final MatchEngine service = new DifferencesServices();
			match1 = service.modelMatch(testModel1, testModel2, testModel3, Collections
					.<String, Object> emptyMap());
			match2 = service.modelMatch(testModel1, testModel2, testModel3, new NullProgressMonitor(),
					Collections.<String, Object> emptyMap());
		} catch (InterruptedException e) {
			fail("modelMatch() threw an unexpected InterruptedException while comparing three models.");
		}

		assertNotNull("Failed to match the three models.", match1);
		assertNotNull("Failed to match the three models.", match2);
		// keeps compiler happy
		assert match1 != null;
		assert match2 != null;

		int elementCount = 0;
		for (final TreeIterator<EObject> iterator = testModel1.eAllContents(); iterator.hasNext(); ) {
			final EObject next = iterator.next();
			boolean found1 = false;
			for (final TreeIterator<EObject> matchIterator = match1.eAllContents(); matchIterator.hasNext(); ) {
				final EObject nextMatch = matchIterator.next();
				if (nextMatch instanceof Match2Elements
						&& ((Match2Elements)nextMatch).getLeftElement().equals(next)
						|| (nextMatch instanceof UnMatchElement && ((UnMatchElement)nextMatch).getElement()
								.equals(next))) {
					found1 = true;
					break;
				}
			}
			boolean found2 = false;
			for (final TreeIterator<EObject> matchIterator = match2.eAllContents(); matchIterator.hasNext(); ) {
				final EObject nextMatch = matchIterator.next();
				if ((nextMatch instanceof Match2Elements && ((Match2Elements)nextMatch).getLeftElement()
						.equals(next))
						|| (nextMatch instanceof UnMatchElement && ((UnMatchElement)nextMatch).getElement()
								.equals(next))) {
					found2 = true;
					break;
				}
			}
			if (!found1 || !found2)
				fail("modelMatch() did not found a match for every element of the original model with three way comparison.");
			elementCount++;
		}

		int matchElementCount1 = 0;
		for (final TreeIterator<EObject> matchIterator = match1.eAllContents(); matchIterator.hasNext(); ) {
			if (matchIterator.next() instanceof Match2Elements)
				matchElementCount1++;
		}
		int matchElementCount2 = 0;
		for (final TreeIterator<EObject> matchIterator = match2.eAllContents(); matchIterator.hasNext(); ) {
			if (matchIterator.next() instanceof Match2Elements)
				matchElementCount2++;
		}

		/*
		 * cannot be less elements since we've found a mapping for each at this point. Note that we need to
		 * add 1 to the model element count since the root hasn't been counted yet.
		 */
		assertEquals("modelMatch() found more matches than there are elements in the left model.",
				elementCount + 1, matchElementCount1);
		assertEquals("modelMatch() found more matches than there are elements in the left model.",
				elementCount + 1, matchElementCount2);

		// We should find one single UnMatchElement corresponding to the added modelElement
		assertTrue("modelMatch() did not found the unmatched element we added in the right model.", match1
				.getUnMatchedElements() != null
				&& match1.getUnMatchedElements().size() == 1);
		assertTrue("modelMatch() did not found the unmatched element we added in the right model.", match2
				.getUnMatchedElements() != null
				&& match2.getUnMatchedElements().size() == 1);
	}
}
