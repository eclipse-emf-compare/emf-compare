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
package org.eclipse.emf.compare.tests.diff;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.junit.Test;

/**
 * We will use this to test the utility methods exposed by the {@link DefaultDiffEngine}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("all")
public class DiffEngineTest {
	@Test
	public void lcsTest1() {
		final List<Character> left = Lists.charactersOf("abcde");
		final List<Character> right = Lists.charactersOf("czdab");

		final Comparison emptyComparison = CompareFactory.eINSTANCE.createComparison();
		final List<Character> lcs = DefaultDiffEngine.longestCommonSubsequence(emptyComparison, left, right);

		/*
		 * This is documented in {@link DefaultDiffEngine#longestCommonSubsequence(Comparison, List, List)}.
		 * Ensure the documentation stays in sync.
		 */
		assertEqualContents(Lists.charactersOf("cd"), lcs);
	}

	@Test
	public void lcsTest2() {
		final List<Character> left = Lists.charactersOf("abcde");
		final List<Character> right = Lists.charactersOf("ycdeb");

		final Comparison emptyComparison = CompareFactory.eINSTANCE.createComparison();
		final List<Character> lcs = DefaultDiffEngine.longestCommonSubsequence(emptyComparison, left, right);

		/*
		 * This is documented in {@link DefaultDiffEngine#longestCommonSubsequence(Comparison, List, List)}.
		 * Ensure the documentation stays in sync.
		 */
		assertEqualContents(Lists.charactersOf("cde"), lcs);
	}

	@Test
	public void lcsTest3() {
		final List<Integer> left = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final List<Integer> right = Lists.newArrayList(8, 9, 2, 3, 4, 1, 0);

		final Comparison emptyComparison = CompareFactory.eINSTANCE.createComparison();
		final List<Integer> lcs = DefaultDiffEngine.longestCommonSubsequence(emptyComparison, left, right);

		// These are the origin and left sides of the "complex" conflict test case.
		assertEqualContents(Lists.newArrayList(2, 3, 4), lcs);
	}

	@Test
	public void lcsTest4() {
		final List<Integer> left = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
		final List<Integer> right = Lists.newArrayList(6, 2, 9, 3, 0, 4, 1, 7);

		final Comparison emptyComparison = CompareFactory.eINSTANCE.createComparison();
		final List<Integer> lcs = DefaultDiffEngine.longestCommonSubsequence(emptyComparison, left, right);

		// These are the origin and right sides of the "complex" conflict test case.
		assertEqualContents(Lists.newArrayList(2, 3, 4, 7), lcs);
	}

	/**
	 * Ensures that the two given lists contain the same elements in the same order. The kind of list does not
	 * matter.
	 * 
	 * @param list1
	 *            First of the two lists to compare.
	 * @param list2
	 *            Second of the two lists to compare.
	 */
	private static <T> void assertEqualContents(List<T> list1, List<T> list2) {
		final int size = list1.size();
		assertSame(Integer.valueOf(size), Integer.valueOf(list2.size()));

		for (int i = 0; i < size; i++) {
			assertEquals(list1.get(i), list2.get(i));
		}
	}
}
