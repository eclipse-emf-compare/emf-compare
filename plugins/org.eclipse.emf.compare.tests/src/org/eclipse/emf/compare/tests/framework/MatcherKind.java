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
 * Selects the matcher whose result is asserted by one test execution.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
@SuppressWarnings("restriction")
public enum MatcherKind {
	/** Original matching policy, with proximity for objects without identifiers. */
	PROXIMITY,
	/** Content matching without an identifier shortcut. */
	CONTENT_HASH;

	/**
	 * Creates the comparison builder for a parameterized test execution. PROXIMITY retains the default
	 * WHEN_AVAILABLE identifier policy; CONTENT_HASH always exercises content matching.
	 *
	 * @return the comparison builder
	 */
	public EMFCompare.Builder builder() {
		IMatchEngine.Factory.Registry registry = new MatchEngineFactoryRegistryImpl();
		registry.add(factory(UseIdentifiers.WHEN_AVAILABLE));
		return EMFCompare.builder().setMatchEngineFactoryRegistry(registry);
	}

	/**
	 * Creates a content-only comparison, including for fixtures that contain identifiers.
	 *
	 * @return the comparison builder with identifier matching disabled
	 */
	public EMFCompare.Builder contentBuilder() {
		IMatchEngine.Factory.Registry registry = new MatchEngineFactoryRegistryImpl();
		registry.add(factory(UseIdentifiers.NEVER));
		return EMFCompare.builder().setMatchEngineFactoryRegistry(registry);
	}

	/**
	 * Keeps identifier-defined identity explicit in tests of merge and difference mechanics.
	 *
	 * @return a comparison builder with the selected content matcher behind identifier matching
	 */
	@SuppressWarnings("deprecation")
	public EMFCompare.Builder builderWithIdentifiers() {
		WeightProvider.Descriptor.Registry weights = WeightProviderDescriptorRegistryImpl
				.createStandaloneInstance();
		IEObjectMatcher content;
		if (this == CONTENT_HASH) {
			content = new ContentHashEObjectMatcher(weights, new ContentHasher(weights));
		} else {
			content = DefaultMatchEngine.createDefaultEObjectMatcher(UseIdentifiers.NEVER, weights);
		}
		IMatchEngine.Factory.Registry registry = new MatchEngineFactoryRegistryImpl();
		MatchEngineFactoryImpl factory = new MatchEngineFactoryImpl(new IdentifierEObjectMatcher(content),
				new DefaultComparisonFactory(new DefaultEqualityHelperFactory()));
		factory.setRanking(10);
		registry.add(factory);
		return EMFCompare.builder().setMatchEngineFactoryRegistry(registry);
	}

	/**
	 * Creates a factory with the default feature weights.
	 *
	 * @param identifiers
	 *            the original identifier policy
	 * @return the match-engine factory
	 */
	public IMatchEngine.Factory factory(UseIdentifiers identifiers) {
		return factory(identifiers, WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
	}

	/**
	 * Creates a factory for this matcher. Hash runs always read content, including identified objects.
	 *
	 * @param identifiers
	 *            the identifier policy used by the original proximity execution
	 * @param weights
	 *            the feature-weight registry
	 * @return the match-engine factory
	 */
	@SuppressWarnings("deprecation")
	public IMatchEngine.Factory factory(UseIdentifiers identifiers,
			WeightProvider.Descriptor.Registry weights) {
		IEObjectMatcher matcher;
		if (this == CONTENT_HASH) {
			matcher = new ContentHashEObjectMatcher(weights, new ContentHasher(weights));
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
	 *            the original identifier policy
	 * @return the match engine
	 */
	public IMatchEngine createMatchEngine(UseIdentifiers identifiers) {
		return factory(identifiers).getMatchEngine();
	}

}
