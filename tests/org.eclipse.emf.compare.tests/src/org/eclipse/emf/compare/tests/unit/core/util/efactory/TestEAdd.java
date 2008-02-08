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
package org.eclipse.emf.compare.tests.unit.core.util.efactory;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;

/**
 * Tests the behavior of {@link EFactory#eAdd(EObject, String, Object)}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestEAdd extends TestCase {
	/** Contains invalid feature names whatever the target. */
	private String[] invalidFeatureNames = {null, "", "-1", "invalidFeature", };

	/** Objects used for all these tests. Will be reinitialised for each test via {@link #setUp()}. */
	private EObject[] testEObjects = new EObject[5];

	/**
	 * Tests {@link EFactory#eAdd(EObject, String, Object)} with <code>null</code> as its first parameter.
	 * No matter what the other arguments are, expects a {@link NullPointerException} to be thrown.
	 */
	public void testEAddNullObject() {
		for (int i = 0; i < invalidFeatureNames.length; i++) {
			try {
				EFactory.eAdd(null, invalidFeatureNames[i], null);
				fail("Expected NullPointerException hasn't been thrown by eAdd().");
			} catch (NullPointerException e) {
				// We expected this
			} catch (FactoryException e) {
				fail("Unexpected FactoryException has been thrown by eAdd() operation.");
			}
		}
	}

	/**
	 * Tests {@link EFactory#eAdd(EObject, String, Object)} on a valid EObject with an invalid feature name.
	 * No matter what the value is, expects a {@link FactoryException} to be thrown.
	 */
	public void testEAddValidObjectInvalidFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (int j = 0; j < invalidFeatureNames.length; j++) {
				try {
					EFactory.eAdd(testEObjects[i], invalidFeatureNames[j], null);
					fail("Expected FactoryException hasn't been thrown by eAdd().");
				} catch (NullPointerException e) {
					fail("Unexpected NullPointerException has been thrown by eAdd().");
				} catch (FactoryException e) {
					// We expected this
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eAdd(EObject, String, Object)} on a valid EObject with valid feature names. If
	 * the feature is a list, expects the value to be appended to it, otherwise expects the value to be set as
	 * the feature value.
	 */
	public void testEAddValidObjectValidFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EStructuralFeature feature : testEObjects[i].eClass().getEAllStructuralFeatures()) {
				if (feature.isChangeable()) {
					for (Object newValue : getArbitraryValueFor(feature)) {
						// We don't need to try with null values here
						if (newValue != null) {
							try {
								EFactory.eAdd(testEObjects[i], feature.getName(), newValue);
								if (testEObjects[i].eGet(feature) instanceof List) {
									assertTrue(
											"New value hasn't been appended to the feature's values list.",
											((List)testEObjects[i].eGet(feature)).contains(newValue));
								} else {
									assertEquals("Feature hasn't been set by eAdd.", newValue,
											testEObjects[i].eGet(feature));
								}
							} catch (FactoryException e) {
								fail("Unexpected FactoryException has been thrown by eAdd().");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eAdd(EObject, String, Object)} on a valid EObject with valid feature names. As we
	 * try to add <code>null</code> from the feature's value, we expect no change to the value if it is a
	 * {@link List}. Otherwise, we expect the value to be reset to its default value.
	 */
	public void testEAddValidObjectValidFeatureNullValue() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EStructuralFeature feature : testEObjects[i].eClass().getEAllStructuralFeatures()) {
				if (feature.isChangeable()) {
					final Object oldValue = testEObjects[i].eGet(feature);
					try {
						EFactory.eAdd(testEObjects[i], feature.getName(), null);
						if (oldValue instanceof List) {
							assertSame("eAdd changed the feature's values list.", oldValue, testEObjects[i]
									.eGet(feature));
						} else {
							assertEquals("Feature hasn't been reset to its default by eAdd.", feature
									.getDefaultValue(), testEObjects[i].eGet(feature));
						}
					} catch (FactoryException e) {
						fail("Unexpected FactoryException has been thrown by eAdd().");
					}
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eAdd(EObject, String, Object)} on a valid EObject with existing, unsettable
	 * feature names. Expects a {@link FactoryException} to be thrown if the feature is single-valued.
	 * Multi-valued features won't be affected.
	 */
	public void testERemoveValidObjectUnsettableFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EStructuralFeature feature : testEObjects[i].eClass().getEAllStructuralFeatures()) {
				if (!feature.isChangeable()) {
					final Object oldValue = testEObjects[i].eGet(feature);
					try {
						EFactory.eAdd(testEObjects[i],            feature.getName(), null);
						if (oldValue instanceof List)
							assertEquals("eAdd changed the feature's values list.", oldValue, testEObjects[i]
									.eGet(feature));
						else
							fail("Expected NullPointerException hasn't been thrown by eAdd();");
					} catch (FactoryException e) {
						if (oldValue instanceof List)
							fail("Unexpected NullPointerException thrown by eAdd.");
						// This was expected behavior for single-valued features
					}
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

	/**
	 * This will return an arbitrary value to be set for the given feature.
	 * 
	 * @param feature
	 *            Feature which value is to be set.
	 * @return Possible new values for the feature.
	 */
	private Object[] getArbitraryValueFor(EStructuralFeature feature) {
		Object[] newValues = new Object[1];
		if (feature.getEType().getInstanceClass() != null) {
			try {
				newValues[0] = feature.getEType().getInstanceClass().newInstance();
			} catch (IllegalAccessException e) {
				// couldn't create an instance of the class this feature accepts as value
			} catch (InstantiationException e) {
				final String desiredClass = feature.getEType().getInstanceClassName();
				if (desiredClass.matches("double")) {
					newValues = new Object[] {(double)feature.getLowerBound(), Double.NaN, 0d,
							Double.POSITIVE_INFINITY, (double)feature.getUpperBound(), };
				} else if (desiredClass.equals("org.eclipse.emf.ecore.EObject")) {
					newValues[0] = EcoreFactory.eINSTANCE.createEObject();
				} else if (desiredClass.equals("org.eclipse.emf.compare.diff.metamodel.DiffElement")) {
					newValues[0] = DiffFactory.eINSTANCE.createDiffGroup();
				} else if (desiredClass.equals("org.eclipse.emf.ecore.EPackage")) {
					newValues[0] = EcoreFactory.eINSTANCE.createEPackage();
				}
				// many elements aren't given a test value here. Add as needed
			}
		}

		return newValues;
	}
}
