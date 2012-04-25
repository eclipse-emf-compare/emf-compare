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
package org.eclipse.emf.compare.utils;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * This utility class will be used to provide similarity implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class SimilarityUtil {
	/** This utility class does not need to be instantiated. */
	private SimilarityUtil() {
		// Hides default constructor
	}

	/**
	 * Computes the dice coefficient between the two given String's bigrams.
	 * <p>
	 * This implementation is case insensitive.
	 * </p>
	 * 
	 * @param first
	 *            First of the two Strings to compare.
	 * @param second
	 *            Second of the two Strings to compare.
	 * @return The dice coefficient of the two given String's bigrams, ranging from 0 to 1.
	 */
	public static double diceCoefficient(String first, String second) {
		final char[] str1 = first.toLowerCase().toCharArray();
		final char[] str2 = second.toLowerCase().toCharArray();

		final double coefficient;

		if (str1.equals(str2)) {
			coefficient = 1d;
		} else if (str1.length <= 2 || str2.length <= 2) {
			int equalChars = 0;

			for (int i = 0; i < Math.min(str1.length, str2.length); i++) {
				if (str1[i] == str2[i]) {
					equalChars++;
				}
			}

			int union = str1.length + str2.length;
			if (str1.length != str2.length) {
				coefficient = (double)equalChars / union;
			} else {
				coefficient = ((double)equalChars * 2) / union;
			}
		} else {
			Set<String> s1Bigrams = Sets.newHashSet();
			Set<String> s2Bigrams = Sets.newHashSet();

			for (int i = 0; i < str1.length - 1; i++) {
				char[] chars = new char[] {str1[i], str1[i + 1], };
				s1Bigrams.add(String.valueOf(chars));
			}
			for (int i = 0; i < str2.length - 1; i++) {
				char[] chars = new char[] {str2[i], str2[i + 1], };
				s2Bigrams.add(String.valueOf(chars));
			}

			Set<String> intersection = Sets.intersection(s1Bigrams, s2Bigrams);
			coefficient = (2 * intersection.size()) / (s1Bigrams.size() + s2Bigrams.size());
		}

		return coefficient;
	}
}
