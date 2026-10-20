/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.framework;

import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.IdentifierEObjectMatcher;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.match.hash.ContentHashEObjectMatcher;
import org.eclipse.emf.compare.match.hash.ContentHasher;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.utils.UseIdentifiers;

/**
 * Selects the algorithm used when identifier matching is disabled or needs a fallback.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("restriction")
public enum MatchingAlgorithm {
	/** Matches objects by proximity. */
	PROXIMITY,
	/** Matches objects by content hashes and similarity. */
	CONTENT_HASH;

	/**
	 * Creates a comparison builder with an explicit identifier policy.
	 *
	 * @param identifiers
	 *            whether to ignore IDs, prefer IDs with this algorithm as fallback, or use IDs only
	 * @return a fresh comparison builder using the explicit identifier policy
	 */
	public EMFCompare.Builder builder(UseIdentifiers identifiers) {
		IMatchEngine.Factory.Registry registry = new MatchEngineFactoryRegistryImpl();
		registry.add(factory(identifiers));
		return EMFCompare.builder().setMatchEngineFactoryRegistry(registry);
	}

	/**
	 * Creates a factory with the default feature weights.
	 *
	 * @param identifiers
	 *            the identifier policy for either algorithm
	 * @return the match-engine factory
	 */
	public IMatchEngine.Factory factory(UseIdentifiers identifiers) {
		return factory(identifiers, WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
	}

	/**
	 * Creates a factory applying the identifier policy before the selected algorithm.
	 *
	 * @param identifiers
	 *            the identifier policy for either algorithm
	 * @param weights
	 *            the feature-weight registry
	 * @return the match-engine factory
	 */
	@SuppressWarnings("deprecation")
	public IMatchEngine.Factory factory(UseIdentifiers identifiers,
			WeightProvider.Descriptor.Registry weights) {
		IEObjectMatcher matcher;
		if (this == CONTENT_HASH && identifiers != UseIdentifiers.ONLY) {
			matcher = new ContentHashEObjectMatcher(weights, new ContentHasher(weights));
			if (identifiers == UseIdentifiers.WHEN_AVAILABLE) {
				matcher = new IdentifierEObjectMatcher(matcher);
			}
		} else {
			matcher = DefaultMatchEngine.createDefaultEObjectMatcher(identifiers, weights);
		}
		MatchEngineFactoryImpl factory = new MatchEngineFactoryImpl(matcher,
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		factory.setRanking(10);
		return factory;
	}

	/**
	 * Creates a match engine with the default feature weights.
	 *
	 * @param identifiers
	 *            the identifier policy for either algorithm
	 * @return the match engine
	 */
	public IMatchEngine createMatchEngine(UseIdentifiers identifiers) {
		return factory(identifiers).getMatchEngine();
	}

}
