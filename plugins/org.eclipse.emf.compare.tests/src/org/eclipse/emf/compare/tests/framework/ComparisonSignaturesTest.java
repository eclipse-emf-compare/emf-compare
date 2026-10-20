/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

/**
 * Verifies that the signatures used to compare matcher results detect changed partners and difference
 * values. Equal numbers of matches or differences alone could otherwise hide a disagreement between the
 * matchers.
 * <p>
 * Minimal comparisons are constructed directly: one case changes only the right-hand partner of a match;
 * the other changes only an attribute difference's value. In each case the signatures must differ even
 * though the number of entries is unchanged. The objects are detached, so the partner case exercises object
 * identity rather than resource URI encoding.
 * </p>
 * <p>
 * These tests check the diagnostic helper's sensitivity to those two changes. They do not run a matcher or
 * establish which correspondence is correct for a model fixture.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ComparisonSignaturesTest {

	/** Changing only a match partner must change its diagnostic signature. */
	@Test
	public void matchSignatureIncludesThePartner() {
		Comparison first = CompareFactory.eINSTANCE.createComparison();
		Comparison second = CompareFactory.eINSTANCE.createComparison();
		Match original = CompareFactory.eINSTANCE.createMatch();
		original.setLeft(EcoreFactory.eINSTANCE.createEClass());
		original.setRight(EcoreFactory.eINSTANCE.createEClass());
		first.getMatches().add(original);
		Match changed = CompareFactory.eINSTANCE.createMatch();
		changed.setLeft(original.getLeft());
		changed.setRight(original.getRight());
		second.getMatches().add(changed);
		// Distinct Match instances with the same partners must have the same signature.
		assertEquals(ComparisonSignatures.matches(first), ComparisonSignatures.matches(second));

		// Now change only the right partner; match identity must not explain the difference.
		changed.setRight(EcoreFactory.eINSTANCE.createEClass());
		assertNotEquals(ComparisonSignatures.matches(first), ComparisonSignatures.matches(second));
	}

	/** Changing only an attribute value must change its diagnostic difference signature. */
	@Test
	public void differenceSignatureIncludesTheChangedValue() {
		Comparison comparison = CompareFactory.eINSTANCE.createComparison();
		Match match = CompareFactory.eINSTANCE.createMatch();
		comparison.getMatches().add(match);
		AttributeChange diff = CompareFactory.eINSTANCE.createAttributeChange();
		diff.setAttribute(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
		diff.setValue("first"); //$NON-NLS-1$
		match.getDifferences().add(diff);
		Map<List<Object>, Integer> original = ComparisonSignatures.differences(comparison);

		// Keep the same difference, feature and owning match to isolate its stored value.
		diff.setValue("second"); //$NON-NLS-1$
		assertNotEquals(original, ComparisonSignatures.differences(comparison));
	}
}
