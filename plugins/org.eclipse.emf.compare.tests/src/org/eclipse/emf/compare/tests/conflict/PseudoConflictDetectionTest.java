/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.conflict;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
import org.eclipse.emf.compare.tests.framework.AbstractMatcherTest;
import org.eclipse.emf.compare.tests.framework.MatcherKind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class test the detection of pseudo conflicts.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
@RunWith(Parameterized.class)
public class PseudoConflictDetectionTest extends AbstractMatcherTest {

	private final Notifier origin;

	private final Notifier left;

	private final Notifier right;

	private final int numberOfConflicts;

	/**
	 * Loads independent models for one matcher and pseudo-conflict scenario.
	 *
	 * @param matcher
	 *            the selected matcher
	 * @param scenario
	 *            the fixture number
	 * @param numberOfConflicts
	 *            the expected pseudo-conflict count
	 * @throws IOException
	 *             if a fixture cannot be loaded
	 */
	public PseudoConflictDetectionTest(MatcherKind matcher, int scenario, int numberOfConflicts)
			throws IOException {
		super(matcher);
		ConflictInputData data = new ConflictInputData();
		switch (scenario) {
			case 1:
				origin = data.getPseudoConflictCase1Ancestor();
				left = data.getPseudoConflictCase1RightAndLeftModel();
				right = data.getPseudoConflictCase1RightAndLeftModel();
				break;
			case 2:
				origin = data.getPseudoConflictCase2Ancestor();
				left = data.getPseudoConflictCase2RightAndLeftModel();
				right = data.getPseudoConflictCase2RightAndLeftModel();
				break;
			case 3:
				origin = data.getPseudoConflictCase3Ancestor();
				left = data.getPseudoConflictCase3RightAndLeftModel();
				right = data.getPseudoConflictCase3RightAndLeftModel();
				break;
			case 4:
				origin = data.getPseudoConflictCase4Ancestor();
				left = data.getPseudoConflictCase4RightAndLeftModel();
				right = data.getPseudoConflictCase4RightAndLeftModel();
				break;
			default:
				throw new IllegalArgumentException("Unknown pseudo-conflict scenario: " + scenario); //$NON-NLS-1$
		}
		this.numberOfConflicts = numberOfConflicts;
	}

	/**
	 * Lists each scenario for both matchers.
	 *
	 * @return the matcher, scenario and expected conflict count
	 */
	@Parameters(name = "{0}-case{1}")
	public static Collection<Object[]> params() {
		Collection<Object[]> result = new ArrayList<>();
		for (MatcherKind matcher : MatcherKind.values()) {
			result.add(new Object[] {matcher, 1, 5 });
			result.add(new Object[] {matcher, 2, 1 });
			result.add(new Object[] {matcher, 3, 2 });
			result.add(new Object[] {matcher, 4, 2 });
		}
		return result;
	}

	@Test
	public void verifyPseudoConflictDetection() throws IOException {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = matcher.builder().build().compare(scope);

		EList<Conflict> conflicts = comparison.getConflicts();
		assertEquals(numberOfConflicts, conflicts.size());

		for (Conflict conflict : conflicts) {
			assertEquals(ConflictKind.PSEUDO, conflict.getKind());
		}
	}
}
