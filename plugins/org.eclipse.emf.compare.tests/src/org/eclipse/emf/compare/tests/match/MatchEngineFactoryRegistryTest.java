/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.fullcomparison.data.identifier.IdentifierMatchInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

public class MatchEngineFactoryRegistryTest {

	private IdentifierMatchInputData input = new IdentifierMatchInputData();

	@Test
	public void tesMatchEngineFactoryRegistry() throws IOException {
		final Resource left = input.getExtlibraryLeft();
		final Resource right = input.getExtlibraryRight();

		final IMatchEngine.Factory.Registry registry = MatchEngineFactoryRegistryImpl
				.createStandaloneInstance();

		// Get the appropriate MatchEngineFactory.
		final IComparisonScope scope = new DefaultComparisonScope(left, right, null);
		IMatchEngine.Factory factory = registry.getHighestRankingMatchEngineFactory(scope);

		assertTrue(factory instanceof MatchEngineFactoryImpl);
		assertEquals(1, registry.getMatchEngineFactories(scope).size());

		// Add new MatchEngineFactory with higher ranking, but not appropriate
		IMatchEngine.Factory factoryWithHighestRanking = new MatchEngineFactoryTest();
		factoryWithHighestRanking.setRanking(100);
		registry.add(factoryWithHighestRanking);

		// Get the appropriate MatchEngineFactory.
		factory = registry.getHighestRankingMatchEngineFactory(scope);

		assertFalse(factory instanceof MatchEngineFactoryTest);
		assertEquals(1, registry.getMatchEngineFactories(scope).size());

		// Add new MatchEngineFactory with higher ranking
		IMatchEngine.Factory factoryWithMiddleRanking = new MatchEngineFactoryOtherTest();
		factoryWithMiddleRanking.setRanking(50);
		registry.add(factoryWithMiddleRanking);

		// Get the appropriate MatchEngineFactory.
		factory = registry.getHighestRankingMatchEngineFactory(scope);

		assertTrue(factory instanceof MatchEngineFactoryOtherTest);
		assertEquals(2, registry.getMatchEngineFactories(scope).size());
	}

	private class MatchEngineFactoryTest extends MatchEngineFactoryImpl {

		@Override
		public boolean isMatchEngineFactoryFor(IComparisonScope scope) {
			return false;
		}
	}

	private class MatchEngineFactoryOtherTest extends MatchEngineFactoryImpl {

		@Override
		public boolean isMatchEngineFactoryFor(IComparisonScope scope) {
			return true;
		}
	}
}
