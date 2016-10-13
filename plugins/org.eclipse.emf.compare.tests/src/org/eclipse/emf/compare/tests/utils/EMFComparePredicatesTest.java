/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.utils;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.instanceOf;
import static java.util.Arrays.asList;
import static org.eclipse.emf.compare.ConflictKind.PSEUDO;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.allAtomicRefining;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.anyRefining;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasDirectOrIndirectConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasNoDirectOrIndirectConflict;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.junit.Test;

public class EMFComparePredicatesTest {

	private CompareFactory factory = CompareFactory.eINSTANCE;

	@Test
	public void testAnyRefiningWithoutRecursion() {
		Comparison comp = factory.createComparison();
		Match rootMatch = factory.createMatch();
		comp.getMatches().add(rootMatch);

		AttributeChange ac = factory.createAttributeChange();
		ReferenceChange rc1 = factory.createReferenceChange();
		ReferenceChange rc2 = factory.createReferenceChange();
		ReferenceChange rc3 = factory.createReferenceChange();
		EList<Diff> diffs = rootMatch.getDifferences();
		diffs.add(ac);
		diffs.add(rc1);
		diffs.add(rc2);
		diffs.add(rc3);
		ac.getRefinedBy().addAll(asList(rc1, rc2, rc3));

		assertFalse(anyRefining(instanceOf(AttributeChange.class)).apply(ac));
		assertTrue(anyRefining(instanceOf(ReferenceChange.class)).apply(ac));
		assertTrue(anyRefining(equalTo((Diff)rc3)).apply(ac));
	}

	@Test
	public void testAnyRefiningWithRecursion() {
		Comparison comp = factory.createComparison();
		Match rootMatch = factory.createMatch();
		comp.getMatches().add(rootMatch);

		AttributeChange ac = factory.createAttributeChange();
		AttributeChange ac1 = factory.createAttributeChange();
		ReferenceChange rc11 = factory.createReferenceChange();
		ReferenceChange rc12 = factory.createReferenceChange();
		ReferenceChange rc2 = factory.createReferenceChange();
		ReferenceChange rc3 = factory.createReferenceChange();
		EList<Diff> diffs = rootMatch.getDifferences();
		diffs.add(ac);
		diffs.add(ac1);
		diffs.add(rc11);
		diffs.add(rc12);
		diffs.add(rc2);
		diffs.add(rc3);
		ac.getRefinedBy().addAll(asList(ac1, rc2, rc3));
		ac1.getRefinedBy().addAll(asList(rc11, rc12));

		assertTrue(anyRefining(instanceOf(AttributeChange.class)).apply(ac));
		assertTrue(anyRefining(instanceOf(ReferenceChange.class)).apply(ac));
		assertTrue(anyRefining(equalTo((Diff)rc12)).apply(ac));
		assertFalse(anyRefining(equalTo((Diff)ac)).apply(ac));
	}

	@Test
	public void testAllAtomicRefiningWithoutRecursion() {
		Comparison comp = factory.createComparison();
		Match rootMatch = factory.createMatch();
		comp.getMatches().add(rootMatch);

		AttributeChange ac = factory.createAttributeChange();
		ReferenceChange rc1 = factory.createReferenceChange();
		ReferenceChange rc2 = factory.createReferenceChange();
		ReferenceChange rc3 = factory.createReferenceChange();
		EList<Diff> diffs = rootMatch.getDifferences();
		diffs.add(ac);
		diffs.add(rc1);
		diffs.add(rc2);
		diffs.add(rc3);
		ac.getRefinedBy().addAll(asList(rc1, rc2, rc3));

		assertFalse(allAtomicRefining(instanceOf(AttributeChange.class)).apply(ac));
		assertTrue(allAtomicRefining(instanceOf(ReferenceChange.class)).apply(ac));
		assertFalse(allAtomicRefining(equalTo((Diff)rc3)).apply(ac));
	}

	@Test
	public void testAllAtomicRefiningWithRecursion() {
		Comparison comp = factory.createComparison();
		Match rootMatch = factory.createMatch();
		comp.getMatches().add(rootMatch);

		AttributeChange ac = factory.createAttributeChange();
		AttributeChange ac1 = factory.createAttributeChange();
		ReferenceChange rc11 = factory.createReferenceChange();
		ReferenceChange rc12 = factory.createReferenceChange();
		ReferenceChange rc2 = factory.createReferenceChange();
		ReferenceChange rc3 = factory.createReferenceChange();
		EList<Diff> diffs = rootMatch.getDifferences();
		diffs.add(ac);
		diffs.add(ac1);
		diffs.add(rc11);
		diffs.add(rc12);
		diffs.add(rc2);
		diffs.add(rc3);
		ac.getRefinedBy().addAll(asList(ac1, rc2, rc3));
		ac1.getRefinedBy().addAll(asList(rc11, rc12));

		assertFalse(allAtomicRefining(instanceOf(AttributeChange.class)).apply(ac));
		assertTrue(allAtomicRefining(instanceOf(ReferenceChange.class)).apply(ac));
		assertFalse(allAtomicRefining(equalTo((Diff)rc12)).apply(ac));
	}

	@Test
	public void testHasDirectOrIndirectConflictForDirectConflict() {
		Comparison comp = factory.createComparison();
		Match rootMatch = factory.createMatch();
		comp.getMatches().add(rootMatch);
		AttributeChange acl = factory.createAttributeChange();
		AttributeChange acr = factory.createAttributeChange();
		rootMatch.getDifferences().addAll(asList(acl, acr));
		Conflict conflict = factory.createConflict();
		conflict.setKind(REAL);
		conflict.getDifferences().addAll(asList(acl, acr));
		comp.getConflicts().add(conflict);

		assertTrue(hasDirectOrIndirectConflict(REAL).apply(acl));
		assertTrue(hasDirectOrIndirectConflict(REAL).apply(acr));
		assertFalse(hasDirectOrIndirectConflict(PSEUDO).apply(acl));
		assertFalse(hasDirectOrIndirectConflict(PSEUDO).apply(acr));

		assertFalse(hasNoDirectOrIndirectConflict(REAL).apply(acl));
		assertFalse(hasNoDirectOrIndirectConflict(REAL).apply(acr));
		assertTrue(hasNoDirectOrIndirectConflict(PSEUDO).apply(acl));
		assertTrue(hasNoDirectOrIndirectConflict(PSEUDO).apply(acr));
	}

	@Test
	public void testHasDirectOrIndirectConflictForIndirectConflict() {
		Comparison comp = factory.createComparison();
		Match rootMatch = factory.createMatch();
		comp.getMatches().add(rootMatch);
		ReferenceChange rc = factory.createReferenceChange();
		AttributeChange acl = factory.createAttributeChange();
		AttributeChange acr = factory.createAttributeChange();
		rootMatch.getDifferences().addAll(asList(rc, acl, acr));
		rc.getRefinedBy().addAll(asList(acl));
		Conflict conflict = factory.createConflict();
		conflict.setKind(REAL);
		conflict.getDifferences().addAll(asList(acl, acr));
		comp.getConflicts().add(conflict);

		assertTrue(hasDirectOrIndirectConflict(REAL).apply(rc));
		assertFalse(hasDirectOrIndirectConflict(PSEUDO).apply(rc));
		assertFalse(hasNoDirectOrIndirectConflict(REAL).apply(rc));
		assertTrue(hasNoDirectOrIndirectConflict(PSEUDO).apply(rc));
	}
}
