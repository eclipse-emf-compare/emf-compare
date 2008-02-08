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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;

/**
 * Tests the behavior of {@link EFactory#eSet(EObject, String, Object)}.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
@SuppressWarnings({"unchecked", "nls", })
public class TestESet extends TestCase {
	/** Contains invalid feature names whatever the target. */
	private String[] invalidFeatureNames = {null, "", "-1", "invalidFeature", };

	/** Objects used for all these tests. Will be reinitialised for each test via {@link #setUp()}. */
	private EObject[] testEObjects = new EObject[5];

	/**
	 * Tests {@link EFactory#eSet(EObject, String, Object)} on a valid EObject with an invalid feature name.
	 * No matter what the value is, expects a {@link FactoryException} to be thrown.
	 */
	public void testEAddValidObjectInvalidFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (int j = 0; j < invalidFeatureNames.length; j++) {
				try {
					EFactory.eSet(testEObjects[i], invalidFeatureNames[j], null);
					fail("Expected FactoryException hasn't been thrown by eSet().");
				} catch (NullPointerException e) {
					fail("Unexpected NullPointerException has been thrown by eSet().");
				} catch (FactoryException e) {
					// We expected this
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eSet(EObject, String, Object)} on a valid EObject with existing, unsettable
	 * feature names. Expects a {@link FactoryException} to be thrown.
	 */
	public void testERemoveValidObjectUnsettableFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EStructuralFeature feature : testEObjects[i].eClass().getEAllStructuralFeatures()) {
				if (!feature.isChangeable()) {
					try {
						EFactory.eSet(testEObjects[i], feature.getName(), null);
						fail("Expected NullPointerException hasn't been thrown by eSet().");
					} catch (FactoryException e) {
						// This was expected behavior for single-valued features
					}
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eSet(EObject, String, Object)} with <code>null</code> as its first parameter.
	 * No matter what the other arguments are, expects a {@link NullPointerException} to be thrown.
	 */
	public void testESetNullObject() {
		for (int i = 0; i < invalidFeatureNames.length; i++) {
			try {
				EFactory.eSet(null, invalidFeatureNames[i], null);
				fail("Expected NullPointerException hasn't been thrown by eSet().");
			} catch (NullPointerException e) {
				// We expected this
			} catch (FactoryException e) {
				fail("Unexpected FactoryException has been thrown by eSet() operation.");
			}
		}
	}

	/**
	 * Tests {@link EFactory#eSet(EObject, String, Object)} on a valid EObject with valid feature names. If
	 * the feature is an {@link EEnum enumeration} and the value we're trying to set a {@link String},
	 * expects the feature's value to be set to the enumeration value represented by this String. Otherwise,
	 * expects it to behave as would {@link EObject#eSet(EStructuralFeature, Object)}.
	 */
	public void testESetValidEObjectEEnumFeature() {
		try {
			final EPackage packaje = EcoreFactory.eINSTANCE.createEPackage();
			packaje.setName("aPackage");

			final EEnum visibility = createEEnum();
			packaje.getEClassifiers().add(visibility);

			final EClass clazz = EcoreFactory.eINSTANCE.createEClass();
			final EAttribute enumAttribute = EcoreFactory.eINSTANCE.createEAttribute();
			enumAttribute.setName("anEnumAttribute");
			enumAttribute.setEType(visibility);
			enumAttribute.setDefaultValue(visibility.getEEnumLiteral(0));
			clazz.getEStructuralFeatures().add(enumAttribute);
			packaje.getEClassifiers().add(clazz);

			final EObject testObject = packaje.getEFactoryInstance().create(clazz);
			final String newLiteral = "package";

			EFactory.eSet(testObject, enumAttribute.getName(), newLiteral);

			assertEquals("eSet() didn't set correct value for EEnum type attribute.", visibility
					.getEEnumLiteral(newLiteral), testObject.eGet(enumAttribute));
		} catch (FactoryException e) {
			e.printStackTrace();
			fail("Unexpected FactoryException has been thrown by eSet() called on an EEnum.");
		}
	}

	/**
	 * Tests {@link EFactory#eSet(EObject, String, Object)} on a valid EObject with valid feature names. As
	 * none of the features tested here are enumeration, expects the value to be set for the feature as it
	 * would with {@link EObject#eSet(EStructuralFeature, Object)}.
	 */
	public void testESetValidObjectValidFeature() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EStructuralFeature feature : testEObjects[i].eClass().getEAllStructuralFeatures()) {
				final List newValues = new ArrayList();
				if (feature.isMany()) {
					for (Object value : getArbitraryValueFor(feature)) {
						final List actualValue = new ArrayList();
						if (value != null)
							actualValue.add(value);
						newValues.add(actualValue);
					}
				} else {
					newValues.addAll(Arrays.asList(getArbitraryValueFor(feature)));
					final List nullList = new ArrayList();
					nullList.add(null);
					newValues.removeAll(nullList);
				}

				if (feature.isChangeable()) {
					for (Object newValue : newValues) {
						// We don't need to try with null value here
						if (newValue != null) {
							try {
								EFactory.eSet(testEObjects[i], feature.getName(), newValue);
								assertEquals("Feature hasn't been set by eSet().", newValue, testEObjects[i]
										.eGet(feature));
							} catch (FactoryException e) {
								fail("Unexpected FactoryException has been thrown by eSet().");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Tests {@link EFactory#eSet(EObject, String, Object)} on a valid EObject with valid feature names. As we
	 * try to set the feature's value to <code>null</code>, we expect the value to be reset to its default
	 * value.
	 */
	public void testESetValidObjectValidFeatureNullValue() {
		for (int i = 0; i < testEObjects.length; i++) {
			for (EStructuralFeature feature : testEObjects[i].eClass().getEAllStructuralFeatures()) {
				if (feature.isChangeable()) {
					try {
						EFactory.eSet(testEObjects[i], feature.getName(), null);
						final Object expectedValue;
						if (feature.isMany())
							expectedValue = Collections.EMPTY_LIST;
						else
							expectedValue = feature.getDefaultValue();
						assertEquals("Feature hasn't been reset to its default by eSet.", expectedValue,
								testEObjects[i].eGet(feature));
					} catch (FactoryException e) {
						fail("Unexpected FactoryException has been thrown by eSet.");
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
		// These are the objects which features we'll call "eSet" on
		testEObjects[0] = DiffFactory.eINSTANCE.createMoveModelElement();
		testEObjects[1] = DiffFactory.eINSTANCE.createDiffGroup();
		testEObjects[2] = MatchFactory.eINSTANCE.createMatch2Elements();
		testEObjects[3] = EcoreFactory.eINSTANCE.createEClass();
		testEObjects[4] = EcoreFactory.eINSTANCE.createEPackage();
	}

	/**
	 * Creates an enumeration called "visibility" with the following values.
	 * <ul>
	 * <li>private = 0</li>
	 * <li>protected = 1</li>
	 * <li>package = 2</li>
	 * <li>public = 3</li>
	 * </ul>
	 * 
	 * @return The created enumeration.
	 */
	private EEnum createEEnum() {
		final EEnum visibility = EcoreFactory.eINSTANCE.createEEnum();
		visibility.setName("visibility");
		final EEnumLiteral privateLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
		privateLiteral.setName("private");
		privateLiteral.setValue(0);
		final EEnumLiteral protectedLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
		protectedLiteral.setName("protected");
		protectedLiteral.setValue(1);
		final EEnumLiteral packageLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
		packageLiteral.setName("package");
		packageLiteral.setValue(2);
		final EEnumLiteral publicLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
		publicLiteral.setName("public");
		publicLiteral.setValue(3);
		visibility.getELiterals().add(privateLiteral);
		visibility.getELiterals().add(protectedLiteral);
		visibility.getELiterals().add(packageLiteral);
		visibility.getELiterals().add(publicLiteral);
		return visibility;
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
