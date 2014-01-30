/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EClassifierImpl;
import org.junit.Test;

/**
 * Tests the behavior of the {@link CompareFactory generated factory} for package compare.
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class CompareFactoryTest {
	/**
	 * Ensures that creating {@link Comparison} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateComparison() {
		Object result = CompareFactory.eINSTANCE.createComparison();
		assertNotNull(result);
		assertTrue(result instanceof Comparison);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.COMPARISON);
		assertNotNull(result);
		assertTrue(result instanceof Comparison);
	}

	/**
	 * Ensures that creating {@link MatchResource} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateMatchResource() {
		Object result = CompareFactory.eINSTANCE.createMatchResource();
		assertNotNull(result);
		assertTrue(result instanceof MatchResource);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.MATCH_RESOURCE);
		assertNotNull(result);
		assertTrue(result instanceof MatchResource);
	}

	/**
	 * Ensures that creating {@link Match} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateMatch() {
		Object result = CompareFactory.eINSTANCE.createMatch();
		assertNotNull(result);
		assertTrue(result instanceof Match);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.MATCH);
		assertNotNull(result);
		assertTrue(result instanceof Match);
	}

	/**
	 * Ensures that creating {@link Diff} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateDiff() {
		Object result = CompareFactory.eINSTANCE.createDiff();
		assertNotNull(result);
		assertTrue(result instanceof Diff);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.DIFF);
		assertNotNull(result);
		assertTrue(result instanceof Diff);
	}

	/**
	 * Ensures that creating {@link ResourceAttachmentChange} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateResourceAttachmentChange() {
		Object result = CompareFactory.eINSTANCE.createResourceAttachmentChange();
		assertNotNull(result);
		assertTrue(result instanceof ResourceAttachmentChange);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.RESOURCE_ATTACHMENT_CHANGE);
		assertNotNull(result);
		assertTrue(result instanceof ResourceAttachmentChange);
	}

	/**
	 * Ensures that creating {@link ReferenceChange} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateReferenceChange() {
		Object result = CompareFactory.eINSTANCE.createReferenceChange();
		assertNotNull(result);
		assertTrue(result instanceof ReferenceChange);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.REFERENCE_CHANGE);
		assertNotNull(result);
		assertTrue(result instanceof ReferenceChange);
	}

	/**
	 * Ensures that creating {@link AttributeChange} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateAttributeChange() {
		Object result = CompareFactory.eINSTANCE.createAttributeChange();
		assertNotNull(result);
		assertTrue(result instanceof AttributeChange);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.ATTRIBUTE_CHANGE);
		assertNotNull(result);
		assertTrue(result instanceof AttributeChange);
	}

	/**
	 * Ensures that creating {@link Conflict} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateConflict() {
		Object result = CompareFactory.eINSTANCE.createConflict();
		assertNotNull(result);
		assertTrue(result instanceof Conflict);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.CONFLICT);
		assertNotNull(result);
		assertTrue(result instanceof Conflict);
	}

	/**
	 * Ensures that creating {@link Equivalence} can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateEquivalence() {
		Object result = CompareFactory.eINSTANCE.createEquivalence();
		assertNotNull(result);
		assertTrue(result instanceof Equivalence);

		result = CompareFactory.eINSTANCE.create(ComparePackage.Literals.EQUIVALENCE);
		assertNotNull(result);
		assertTrue(result instanceof Equivalence);
	}

	/**
	 * Ensures that trying to create an {@link EClass} from another package yields the expected exception.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateUnknownEClass() {
		try {
			EClass eClass = EcoreFactory.eINSTANCE.createEClass();
			((EClassifierImpl)eClass).setClassifierID(-1);
			CompareFactory.eINSTANCE.create(eClass);
			fail("Expected IllegalArgumentException hasn't been thrown");
		} catch (IllegalArgumentException e) {
			// Expected behavior
		}
	}

	/**
	 * Ensures that converting {@link DifferenceKind} to String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testConvertDifferenceKindToString() {
		for (DifferenceKind value : DifferenceKind.VALUES) {
			Object result = CompareFactory.eINSTANCE.convertToString(ComparePackage.Literals.DIFFERENCE_KIND,
					value);
			assertNotNull(result);
			assertEquals(value.toString(), result);
		}
	}

	/**
	 * Ensures that converting {@link DifferenceSource} to String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testConvertDifferenceSourceToString() {
		for (DifferenceSource value : DifferenceSource.VALUES) {
			Object result = CompareFactory.eINSTANCE.convertToString(
					ComparePackage.Literals.DIFFERENCE_SOURCE, value);
			assertNotNull(result);
			assertEquals(value.toString(), result);
		}
	}

	/**
	 * Ensures that converting {@link DifferenceState} to String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testConvertDifferenceStateToString() {
		for (DifferenceState value : DifferenceState.VALUES) {
			Object result = CompareFactory.eINSTANCE.convertToString(
					ComparePackage.Literals.DIFFERENCE_STATE, value);
			assertNotNull(result);
			assertEquals(value.toString(), result);
		}
	}

	/**
	 * Ensures that converting {@link ConflictKind} to String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testConvertConflictKindToString() {
		for (ConflictKind value : ConflictKind.VALUES) {
			Object result = CompareFactory.eINSTANCE.convertToString(ComparePackage.Literals.CONFLICT_KIND,
					value);
			assertNotNull(result);
			assertEquals(value.toString(), result);
		}
	}

	/**
	 * Ensures that trying to convert an {@link EEnum} from another package to String yields the expected
	 * exception.
	 * 
	 * @generated
	 */
	@Test
	public void testConvertUnknownEEnumToString() {
		try {
			EEnum eEnum = EcoreFactory.eINSTANCE.createEEnum();
			((EClassifierImpl)eEnum).setClassifierID(-1);
			CompareFactory.eINSTANCE.convertToString(eEnum, eEnum);
			fail("Expected IllegalArgumentException hasn't been thrown");
		} catch (IllegalArgumentException e) {
			// Expected behavior
		}
	}

	/**
	 * Ensures that creating {@link DifferenceKind} from String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateDifferenceKindFromString() {
		for (DifferenceKind value : DifferenceKind.VALUES) {
			Object result = CompareFactory.eINSTANCE.createFromString(
					ComparePackage.Literals.DIFFERENCE_KIND, value.getLiteral());
			assertNotNull(result);
			assertSame(value, result);

			try {
				CompareFactory.eINSTANCE.createFromString(ComparePackage.Literals.DIFFERENCE_KIND,
						"ThisShouldntBeAKnownEEnumLiteral");
				fail("Expected IllegalArgumentException hasn't been thrown");
			} catch (IllegalArgumentException e) {
				// Expected behavior
			}
		}
	}

	/**
	 * Ensures that creating {@link DifferenceSource} from String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateDifferenceSourceFromString() {
		for (DifferenceSource value : DifferenceSource.VALUES) {
			Object result = CompareFactory.eINSTANCE.createFromString(
					ComparePackage.Literals.DIFFERENCE_SOURCE, value.getLiteral());
			assertNotNull(result);
			assertSame(value, result);

			try {
				CompareFactory.eINSTANCE.createFromString(ComparePackage.Literals.DIFFERENCE_SOURCE,
						"ThisShouldntBeAKnownEEnumLiteral");
				fail("Expected IllegalArgumentException hasn't been thrown");
			} catch (IllegalArgumentException e) {
				// Expected behavior
			}
		}
	}

	/**
	 * Ensures that creating {@link DifferenceState} from String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateDifferenceStateFromString() {
		for (DifferenceState value : DifferenceState.VALUES) {
			Object result = CompareFactory.eINSTANCE.createFromString(
					ComparePackage.Literals.DIFFERENCE_STATE, value.getLiteral());
			assertNotNull(result);
			assertSame(value, result);

			try {
				CompareFactory.eINSTANCE.createFromString(ComparePackage.Literals.DIFFERENCE_STATE,
						"ThisShouldntBeAKnownEEnumLiteral");
				fail("Expected IllegalArgumentException hasn't been thrown");
			} catch (IllegalArgumentException e) {
				// Expected behavior
			}
		}
	}

	/**
	 * Ensures that creating {@link ConflictKind} from String can be done through the factory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateConflictKindFromString() {
		for (ConflictKind value : ConflictKind.VALUES) {
			Object result = CompareFactory.eINSTANCE.createFromString(ComparePackage.Literals.CONFLICT_KIND,
					value.getLiteral());
			assertNotNull(result);
			assertSame(value, result);

			try {
				CompareFactory.eINSTANCE.createFromString(ComparePackage.Literals.CONFLICT_KIND,
						"ThisShouldntBeAKnownEEnumLiteral");
				fail("Expected IllegalArgumentException hasn't been thrown");
			} catch (IllegalArgumentException e) {
				// Expected behavior
			}
		}
	}

	/**
	 * Ensures that trying to create an {@link EEnum} from another package from String yields the expected
	 * exception.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateUnknownEEnumFromString() {
		try {
			EEnum eEnum = EcoreFactory.eINSTANCE.createEEnum();
			((EClassifierImpl)eEnum).setClassifierID(-1);
			CompareFactory.eINSTANCE.createFromString(eEnum, "ThisShouldntBeAKnownEEnumLiteral");
			fail("Expected IllegalArgumentException hasn't been thrown");
		} catch (IllegalArgumentException e) {
			// Expected behavior
		}
	}
}
