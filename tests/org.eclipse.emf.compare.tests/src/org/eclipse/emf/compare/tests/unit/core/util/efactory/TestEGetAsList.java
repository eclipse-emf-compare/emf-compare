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
package org.eclipse.emf.compare.tests.unit.core.util.efactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;

/**
 * Tests the behavior of {@link EFactory#eGetAsList(EObject, String)}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestEGetAsList extends TestCase {
	/** Contains invalid feature names whatever the target. */
	private String[] invalidFeatureNames = {null, "", "-1", "invalidFeature", };

	/** Objects used for all these tests. Will be reinitialised for each test via {@link #setUp()}. */
	private EObject[] testEObjects = new EObject[5];

	/**
	 * Tests {@link EFactory#eGetAsList(EObject, String)} with <code>null</code> as its first parameter. No
	 * matter what the specified feature name is, expects a {@link NullPointerException} to be thrown.
	 */
	public void testEGetAsListNullObject() {
		for (int i = 0; i < invalidFeatureNames.length; i++) {
			try {
				EFactory.eGetAsList(null, invalidFeatureNames[i]);
				fail("Expected NullPointerException hasn't been thrown by eGetAsList().");
			} catch (NullPointerException e) {
				// We expected this
			} catch (FactoryException e) {
				fail("Unexpected FactoryException has been thrown by eGetAsList() operation.");
			}
		}
	}

	/**
	 * Tests {@link EFactory#eGetAsList(EObject, String)} on a valid EObject with an invalid feature name.
	 * Expects a {@link FactoryException} to be thrown.
	 */
	public void testEGetAsListValidObjectInvalidFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (int j = 0; j < invalidFeatureNames.length; j++) {
				try {
					EFactory.eGetAsList(testEObjects[i], invalidFeatureNames[j]);
					fail("Expected FactoryException hasn't been thrown by eGetAsList().");
				} catch (NullPointerException e) {
					fail("Unexpected NullPointerException has been thrown by eGetAsList().");
				} catch (FactoryException e) {
					// We expected this
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eGetAsList(EObject, String)} on a valid EObject with valid feature names.
	 * <p>
	 * Expected result if
	 * <ul>
	 * <li>feature is a list =&gt; result of {@link EObject#eGet(EStructuralFeature)}.</li>
	 * <li>feature is single valued =&gt; new list with only the feature's value.</li>
	 * </ul>
	 * </p>
	 */
	public void testEGetAsListValidObjectValidFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EStructuralFeature feature : testEObjects[i].eClass().getEAllStructuralFeatures()) {
				try {
					Object expectedValue = testEObjects[i].eGet(feature);
					if (!(expectedValue instanceof List)) {
						final List temp = new ArrayList();
						if (expectedValue != null)
							temp.add(expectedValue);
						expectedValue = temp;
					}
					final Object actualValue = EFactory.eGetAsList(testEObjects[i], feature.getName());
					assertEquals("Unexpected result of eGetAsList() operation.", expectedValue, actualValue);
				} catch (FactoryException e) {
					fail("Unexpected FactoryException has been thrown by eGetAsList().");
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eGetAsList(EObject, String)} on a valid EObject with valid operation names.
	 * Expected result is the result of the given operation's call, added to a new list if it isn't already an
	 * instance of {@link List}.
	 */
	public void testEGetAsListValidObjectValidOperation() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EOperation operation : testEObjects[i].eClass().getEAllOperations()) {
				try {
					if (operation.getEParameters().size() == 0) {
						final Method method = testEObjects[i].getClass().getMethod(operation.getName());
						Object expectedValue = method.invoke(testEObjects[i]);
						if (!(expectedValue instanceof List)) {
							final List temp = new ArrayList();
							if (expectedValue != null)
								temp.add(expectedValue);
							expectedValue = temp;
						}
						final Object actualValue = EFactory.eGetAsList(testEObjects[i], operation.getName());
						assertEquals(
								"Unexpected result of eGetAsList() on a valid EObject for an operation.",
								expectedValue, actualValue);
					}
				} catch (FactoryException e) {
					fail("Unexpected FactoryException has been thrown by eGetAsList().");
				} catch (NoSuchMethodException e) {
					// shouldn't happen
					assert false;
				} catch (InvocationTargetException e) {
					// shouldn't happen
					assert false;
				} catch (IllegalAccessException e) {
					// shouldn't happen
					assert false;
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
	protected void setUp() {
		// These are the objects which features we'll call "eAdd" on
		testEObjects[0] = DiffFactory.eINSTANCE.createMoveModelElement();
		testEObjects[1] = DiffFactory.eINSTANCE.createDiffGroup();
		testEObjects[2] = MatchFactory.eINSTANCE.createMatch2Elements();
		testEObjects[3] = EcoreFactory.eINSTANCE.createEClass();
		testEObjects[4] = EcoreFactory.eINSTANCE.createEPackage();
	}
}
