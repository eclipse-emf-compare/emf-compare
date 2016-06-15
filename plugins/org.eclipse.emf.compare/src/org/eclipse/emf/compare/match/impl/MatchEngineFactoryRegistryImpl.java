/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;

/**
 * The default implementation of the {@link IMatchEngine.Factory.Registry}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MatchEngineFactoryRegistryImpl implements IMatchEngine.Factory.Registry {

	/** A map that associates the class name to theirs {@link IMatchEngine.Factory}. */
	private final Map<String, IMatchEngine.Factory> map;

	/**
	 * Constructs the registry.
	 */
	public MatchEngineFactoryRegistryImpl() {
		map = new ConcurrentHashMap<String, IMatchEngine.Factory>();
	}

	/**
	 * Returns a registry filled with the default match engine factory provided by EMF Compare
	 * {@link MatchEngineFactoryImpl}.
	 * 
	 * @return A registry filled with the default match engine factory provided by EMF Compare.
	 */
	public static IMatchEngine.Factory.Registry createStandaloneInstance() {
		final IMatchEngine.Factory.Registry registry = new MatchEngineFactoryRegistryImpl();

		final MatchEngineFactoryImpl matchEngineFactory = new MatchEngineFactoryImpl(
				UseIdentifiers.WHEN_AVAILABLE);
		matchEngineFactory.setRanking(10);
		matchEngineFactory
				.setWeightProviderRegistry(WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
		registry.add(matchEngineFactory);

		return registry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IMatchEngine.Factory.Registry#getHighestRankingMatchEngine(java.lang.Object)
	 */
	public IMatchEngine.Factory getHighestRankingMatchEngineFactory(IComparisonScope scope) {
		Iterator<IMatchEngine.Factory> matchEngineFactories = getMatchEngineFactories(scope).iterator();

		IMatchEngine.Factory ret = null;

		if (matchEngineFactories.hasNext()) {
			IMatchEngine.Factory highestRanking = matchEngineFactories.next();
			while (matchEngineFactories.hasNext()) {
				IMatchEngine.Factory engineFactory = matchEngineFactories.next();
				if (engineFactory.getRanking() > highestRanking.getRanking()) {
					highestRanking = engineFactory;
				}
			}
			ret = highestRanking;
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IMatchEngine.Factory.Registry#getMatchEngines(org.eclipse.emf.compare.scope.IComparisonScope)
	 */
	public List<IMatchEngine.Factory> getMatchEngineFactories(IComparisonScope scope) {
		Iterable<IMatchEngine.Factory> matchEngineFactories = filter(map.values(),
				isMatchEngineFactoryActivable(scope));
		List<IMatchEngine.Factory> ret = newArrayList();
		for (IMatchEngine.Factory matchEngineFactory : matchEngineFactories) {
			ret.add(matchEngineFactory);
		}
		return ret;
	}

	/**
	 * Returns a predicate that represents the activation condition based on the scope.
	 * 
	 * @param scope
	 *            The scope on which the group provider will be applied.
	 * @return A predicate that represents the activation condition based on the scope.
	 */
	private static Predicate<IMatchEngine.Factory> isMatchEngineFactoryActivable(
			final IComparisonScope scope) {
		return new Predicate<IMatchEngine.Factory>() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see com.google.common.base.Predicate#apply(java.lang.Object)
			 */
			public boolean apply(IMatchEngine.Factory factory) {
				return factory.isMatchEngineFactoryFor(scope);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IMatchEngine.Factory.Registry#add(org.eclipse.emf.compare.match.IMatchEngine)
	 */
	public IMatchEngine.Factory add(IMatchEngine.Factory filter) {
		Preconditions.checkNotNull(filter);
		return map.put(filter.getClass().getName(), filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IMatchEngine.Factory.Registry#remove(java.lang.String)
	 */
	public IMatchEngine.Factory remove(String className) {
		return map.remove(className);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IMatchEngine.Factory.Registry#clear()
	 */
	public void clear() {
		map.clear();
	}

}
