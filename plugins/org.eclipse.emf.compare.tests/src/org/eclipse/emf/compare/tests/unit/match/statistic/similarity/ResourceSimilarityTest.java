/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.statistic.similarity;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.match.internal.statistic.ResourceSimilarity;

/**
 * Tests the methods used to compute resource similarity.
 * 
 * @author Gonzague Reydet <a href="mailto:gonzague.reydet@obeo.fr">gonzague.reydet@obeo.fr</a>
 */
@SuppressWarnings("nls")
public class ResourceSimilarityTest extends TestCase {

	/**
	 * Tests
	 * {@link ResourceSimilarity#computeURISimilarity(org.eclipse.emf.common.util.URI, org.eclipse.emf.common.util.URI)}
	 * .
	 */
	public void testURISimilarity() {
		final String[] references = {"platform:/resource/test/model.dsl#/foo/bar",
				"platform:/resource/test/model.dsl#/bar", "platform:/resource/test/model.dsl#/bar",
				"platform:/resource/test/model.dsl", "platform:/resource/folder/model.dsl",
				"platform:/resource/folder1/model.dsl#/foo/bar",
				"ar:/#/arPackageShortName/arSubPackageShortName/arElementName",
				"ar:/#/arPackageShortName/arElementName", "ar:/#/arPackageName/arElementName", };
		final String[] candidates = {"platform:/resource/test/model.dsl#/foo/bar",
				"platform:/resource/test/model.dsl#/package/bar", "platform:/resource/model.dsl#/bar",
				"platform:/resource/test/model.dsl", "platform:/resource/model.dsl",
				"platform:/resource/folder2/model.dsl#/foo2/bar",
				"ar:/#/arPackageShortName/arSubPackageShortName/arElementName",
				"ar:/test#/arPackageShortName/arElementName", "ar:/#/arPackageName/arElementFoo", };
		final double[] similarities = {1d, 0.6571428571428571, 1d, 1d, 1d, 0.88, 1d, 0.6, 0.9886792452830189 };

		for (int i = 0; i < references.length; i++) {
			final URI reference = URI.createURI(references[i]);
			final URI candidate = URI.createURI(candidates[i]);

			final double similarity = ResourceSimilarity.computeURISimilarity(reference, candidate);
			assertEquals("Unexpected result of computeURISimilarity for reference = " + references[i]
					+ " and candidate = " + candidates[i], similarities[i], similarity);
		}
	}
}
