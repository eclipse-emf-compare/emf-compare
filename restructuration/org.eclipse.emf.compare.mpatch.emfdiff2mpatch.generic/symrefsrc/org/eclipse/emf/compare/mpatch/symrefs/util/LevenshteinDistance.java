/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.util;

/**
 * Copied from http://www.merriampark.com/ld.htm#JAVA (2010-06-01)
 * <p>
 * "Levenshtein distance (LD) is a measure of the similarity between two strings, which we will refer to as
 * the source string (s) and the target string (t). The distance is the number of deletions, insertions, or
 * substitutions required to transform s into t. For example,
 * <ul>
 * <li>If s is "test" and t is "test", then LD(s,t) = 0, because no transformations are needed. The strings
 * are already identical.
 * <li>If s is "test" and t is "tent", then LD(s,t) = 1, because one substitution (change "s" to "n") is
 * sufficient to transform s into t.
 * </ul>
 * The greater the Levenshtein distance, the more different the strings are."
 * </p>
 * [...]
 * <p>
 * "These three implementations are hereby placed in the public domain and are free for anyone to use."
 * </p>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class LevenshteinDistance {

	/**
	 * Get minimum of three values.
	 */
	private static int min(int a, int b, int c) {
		return a < b ? a < c ? a : c : b < c ? b : c;
		// int mi; mi = a; if (b < mi) mi = b; if (c < mi) mi = c; return mi;
	}

	/**
	 * Compute Levenshtein distance.
	 */
	public static int calculateDistance(String s, String t) {
		int d[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost

		// Step 1
		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];

		// Step 2
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}
		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}

		// Step 3
		for (i = 1; i <= n; i++) {
			s_i = s.charAt(i - 1);

			// Step 4
			for (j = 1; j <= m; j++) {
				t_j = t.charAt(j - 1);

				// Step 5
				if (s_i == t_j) {
					cost = 0;
				} else {
					cost = 1;
				}

				// Step 6
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
			}
		}

		// Step 7
		return d[n][m];
	}

	public static void main(String[] args) {
		System.out.println(calculateDistance("person", "season"));
	}
}
