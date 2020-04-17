/*******************************************************************************
 * Copyright (c) 2020 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;

/**
 * This utility class provides methods that will be used by test classes.
 * 
 * @author <a href="mailto:glenn.plouhinec@obeo.fr">Glenn Plouhinec</a>
 */
public class EMFCompareTestsUtils {
	/**
	 * Ensure that the two given lists contain the same elements in the same order. The kind of list does not
	 * matter.
	 * 
	 * @param <T>
	 *            The type of objects in each list.
	 * @param comparison
	 *            The comparison used to compare models.
	 * @param list1
	 *            First of the two lists to compare.
	 * @param list2
	 *            Second of the two lists to compare.
	 */
	public static <T extends EObject> void assertEqualContents(Comparison comparison, List<T> list1,
			List<T> list2) {
		final int size = list1.size();
		assertEquals(size, list2.size());

		for (int i = 0; i < size; i++) {
			final EObject eObject1 = list1.get(i);
			final EObject eObject2 = list2.get(i);
			final Match match = comparison.getMatch(eObject1);
			eObject1.equals(eObject2);
			if (match.getLeft() == eObject1) {
				assertEquals(match.getRight(), eObject2);
			} else {
				assertEquals(match.getRight(), eObject1);
				assertEquals(match.getLeft(), eObject2);
			}
		}
	}
}
