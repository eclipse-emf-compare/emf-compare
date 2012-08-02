/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit;

import static junit.framework.Assert.assertNull;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.util.CompareSwitch;
import org.junit.Test;

/*
 * TODO This is but a skeleton for the tests of CompareSwitch.
 * Set as "generated NOT" and override each test if you overrode the default generated
 * behavior.
 */
/**
 * Tests the behavior of the {@link CompareSwitch generated switch} for package compare.
 * 
 * @generated
 */
public class CompareSwitchTest {
	/**
	 * Ensures that the generated switch knows {@link Comparison}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseComparison() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseComparison(CompareFactory.eINSTANCE.createComparison()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createComparison()));
	}

	/**
	 * Ensures that the generated switch knows {@link MatchResource}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseMatchResource() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseMatchResource(CompareFactory.eINSTANCE.createMatchResource()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createMatchResource()));
	}

	/**
	 * Ensures that the generated switch knows {@link Match}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseMatch() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseMatch(CompareFactory.eINSTANCE.createMatch()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createMatch()));
	}

	/**
	 * Ensures that the generated switch knows {@link Diff}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseDiff() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseDiff(CompareFactory.eINSTANCE.createDiff()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createDiff()));
	}

	/**
	 * Ensures that the generated switch knows {@link ResourceAttachmentChange}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseResourceAttachmentChange() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseResourceAttachmentChange(CompareFactory.eINSTANCE
				.createResourceAttachmentChange()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createResourceAttachmentChange()));
	}

	/**
	 * Ensures that the generated switch knows {@link ReferenceChange}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseReferenceChange() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseReferenceChange(CompareFactory.eINSTANCE.createReferenceChange()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createReferenceChange()));
	}

	/**
	 * Ensures that the generated switch knows {@link AttributeChange}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseAttributeChange() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseAttributeChange(CompareFactory.eINSTANCE.createAttributeChange()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createAttributeChange()));
	}

	/**
	 * Ensures that the generated switch knows {@link Conflict}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseConflict() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseConflict(CompareFactory.eINSTANCE.createConflict()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createConflict()));
	}

	/**
	 * Ensures that the generated switch knows {@link Equivalence}.
	 * 
	 * @generated
	 */
	@Test
	public void testCaseEquivalence() {
		CompareSwitch<?> compareswitch = new CompareSwitch<Object>();
		assertNull(compareswitch.caseEquivalence(CompareFactory.eINSTANCE.createEquivalence()));
		assertNull(compareswitch.doSwitch(CompareFactory.eINSTANCE.createEquivalence()));
	}
}
