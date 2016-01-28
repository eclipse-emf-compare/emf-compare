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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.conflict.data.ConflictInputData;
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
public class PseudoConflictDetectionTest {

	private final Notifier origin;

	private final Notifier left;

	private final Notifier right;

	private final int numberOfConflicts;

	@Parameters
	public static Collection<Object[]> params() throws IOException {
		ConflictInputData inputData = new ConflictInputData();
		/**
		 * Ancestor model is fragmented into 2 files, and the controlled file is deleted on both sides.
		 */
		Object[] test1 = new Object[] {inputData.getPseudoConflictCase1Ancestor(),
				inputData.getPseudoConflictCase1RightAndLeftModel(),
				inputData.getPseudoConflictCase1RightAndLeftModel(), new Integer(4) };
		/**
		 * Right and left models add the same univaluated containment EReference.
		 */
		Object[] test2 = new Object[] {inputData.getPseudoConflictCase2Ancestor(),
				inputData.getPseudoConflictCase2RightAndLeftModel(),
				inputData.getPseudoConflictCase2RightAndLeftModel(), new Integer(1) };
		/**
		 * Right and left models add an element in an univaluated containment EReference and this element
		 * referenced another element by a non-containment reference.
		 */
		Object[] test3 = new Object[] {inputData.getPseudoConflictCase3Ancestor(),
				inputData.getPseudoConflictCase3RightAndLeftModel(),
				inputData.getPseudoConflictCase3RightAndLeftModel(), new Integer(2) };
		/**
		 * Test the same behavior that test3 but with a more complex model.
		 */
		Object[] test4 = new Object[] {inputData.getPseudoConflictCase4Ancestor(),
				inputData.getPseudoConflictCase4RightAndLeftModel(),
				inputData.getPseudoConflictCase4RightAndLeftModel(), new Integer(2) };
		return Arrays.asList(test1, test2, test3, test4);
	}

	public PseudoConflictDetectionTest(final Notifier origin, final Notifier left, final Notifier right,
			final Integer numberOfConflicts) {
		this.origin = checkNotNull(origin);
		this.left = checkNotNull(left);
		this.right = checkNotNull(right);
		this.numberOfConflicts = (checkNotNull(numberOfConflicts)).intValue();
	}

	@Test
	public void verifyPseudoConflictDetection() throws IOException {
		final IComparisonScope scope = new DefaultComparisonScope(left, right, origin);
		final Comparison comparison = EMFCompare.builder().build().compare(scope);

		EList<Conflict> conflicts = comparison.getConflicts();
		assertEquals(numberOfConflicts, conflicts.size());

		for (Conflict conflict : conflicts) {
			assertEquals(ConflictKind.PSEUDO, conflict.getKind());
		}
	}
}
