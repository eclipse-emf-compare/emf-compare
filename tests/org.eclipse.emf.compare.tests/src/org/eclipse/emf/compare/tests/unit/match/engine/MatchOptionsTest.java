/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.engine;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.api.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.tests.util.EcoreModelUtils;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This will test the behavior of the generic match engine with each of the available options.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class MatchOptionsTest extends TestCase {
	/**
	 * This will test the behavior of the match engine with different values of the option {@link MatchOptions#OPTION_DISTINCT_METAMODELS}.
	 * <p>
	 * We'll create two models here, each with a metamodel created through
	 * {@link EcoreModelUtils#createMetaModel(boolean)}. The meta-models of each model will be in a different
	 * resource though semantically identical. We then expect no elements to be matched when this option is
	 * set to <code>False</code>.
	 * <p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the comparison fails somehow.
	 * @throws InterruptedException
	 *             Won't be thrown as we're not using progess monitors.
	 */
	public void testDistinctMetamodelOption() throws FactoryException, InterruptedException {
		final int writerCount = 50;
		final int bookPerWriterCount = 4;
		final long seed = System.nanoTime();

		final Resource testResource = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed,
				false, true).eResource();
		final Resource sameMetaModel = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed,
				false, false).eResource();
		final Resource distinctMetaModel = EcoreModelUtils.createModel(writerCount, bookPerWriterCount, seed,
				false, true).eResource();

		final Map<String, Object> options = new HashMap<String, Object>();
		MatchModel match = MatchService.doResourceMatch(testResource, sameMetaModel, options);
		assertEquals(
				"There shouldn't have been a single unmatched element between a model and one using the same metamodel.",
				0, match.getUnMatchedElements().size());

		// With distinct metamodels, EMF Compare will consider the roots as matched.
		match = MatchService.doResourceMatch(testResource, distinctMetaModel, options);
		assertEquals(
				"We shouldn't have been able to match more than the roots when using distinct metamodel.", 0,
				match.getMatchedElements().get(0).getSubMatchElements().size());

		// Now let's set this option to true
		options.put(MatchOptions.OPTION_DISTINCT_METAMODELS, true);

		// As the tests will now be metamodel-independant, we expect to find a matched element for each
		match = MatchService.doResourceMatch(testResource, sameMetaModel, options);
		assertEquals(
				"There shouldn't have been a single unmatched element between a model and one using the same metamodel.",
				0, match.getUnMatchedElements().size());

		match = MatchService.doResourceMatch(testResource, distinctMetaModel, options);
		assertEquals(
				"There shouldn't have been a single unmatched element with OPTION_DISTINCT_METAMODELS set to true.",
				0, match.getUnMatchedElements().size());
	}
}
