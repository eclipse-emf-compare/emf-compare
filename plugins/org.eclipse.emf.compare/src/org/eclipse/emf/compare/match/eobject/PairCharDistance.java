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
package org.eclipse.emf.compare.match.eobject;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * a Class to statistically compare two strings.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class PairCharDistance {

	/**
	 * Return a metric result about name similarity. It compares 2 strings and return a double comprised
	 * between 0 and 1. The greater this metric, the more equal the strings are.
	 * 
	 * @param str1
	 *            First of the two {@link String}s to compare.
	 * @param str2
	 *            Second of the two {@link String}s to compare.
	 * @return A metric result about name similarity (0 &lt;= value &lt;= 1).
	 */
	public int distance(String str1, String str2) {
		Set<String> a = pairs(str1);
		Set<String> b = pairs(str2);
		Set<String> pairsInCommon = Sets.intersection(a, b);
		double similarity = pairsInCommon.size() * 2d / (a.size() + b.size());
		double result = 10 * similarity;
		return 10 - (int)result;
	}

	/**
	 * This method returns a {@link Set} of {@link String}s called "pairs". For example,
	 * 
	 * <pre>
	 * pairs(&quot;MyString&quot;)
	 * </pre>
	 * 
	 * returns ["My","yS","St","tr","ri","in","ng"]
	 * 
	 * @param source
	 *            The {@link String} to process.
	 * @return A {@link Set} of {@link String} corresponding to the possibles pairs of the source one.
	 */
	private Set<String> pairs(String source) {
		final Set<String> result = Sets.newLinkedHashSet();
		if (source != null) {
			final int length = source.length();
			for (int i = 0; i < length - 1; i++) {
				result.add(source.substring(i, i + 2));
			}
		}
		return result;
	}
}
