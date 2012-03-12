/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexander Nyssen  - Itemis AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.engine;

import java.util.Map;

import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Utility class to support work with {@link IMatchScope}s.
 * 
 * @author <a href="mailto:alexander.nyssen@itemis.de">Alexander Nyssen</a>
 * @since 1.1
 */
public final class MatchScopeProviderUtil {

	/**
	 * Private constructor (to prevent instantiation).
	 */
	private MatchScopeProviderUtil() {
		// private constructor to prevent instantiation.
	}

	/**
	 * Used to obtain the {@link IMatchScopeProvider} to be used for matching. If one is specified within the
	 * provided options via {@link MatchOptions#OPTION_MATCH_SCOPE_PROVIDER}, this one is used, otherwise a
	 * new {@link GenericMatchScopeProvider} will be instantiated and returned.
	 * 
	 * @param options
	 *            the options to search for any preset {@link IMatchScopeProvider}.
	 * @param resourceSets
	 *            the list of resources sets to be used for constructing a new
	 *            {@link GenericMatchScopeProvider}, in case none is specified via the options. Has to be
	 *            either of size 2 (left and right) or 3 (left, right, and ancestor).
	 * @return the {@link IMatchScopeProvider} specified via the given options or a new
	 *         {@link GenericMatchScopeProvider} instance if none is specified in the options.
	 */
	public static IMatchScopeProvider getScopeProvider(Map<String, Object> options,
			ResourceSet... resourceSets) {
		IMatchScopeProvider scopeProvider = getScopeProvider(options);
		if (scopeProvider == null) {
			if (resourceSets.length == 2) {
				scopeProvider = new DefaultMatchScopeProvider(resourceSets[0], resourceSets[1]);
			} else {
				scopeProvider = new DefaultMatchScopeProvider(resourceSets[0], resourceSets[1],
						resourceSets[2]);
			}
			setScopeProvider(options, scopeProvider);
		}
		return scopeProvider;
	}

	/**
	 * Used to obtain the {@link IMatchScopeProvider} to be used for matching. If one is specified within the
	 * provided options via {@link MatchOptions#OPTION_MATCH_SCOPE_PROVIDER}, this one is used, otherwise a
	 * new {@link GenericMatchScopeProvider} will be instantiated and returned.
	 * 
	 * @param options
	 *            the options to search for any preset {@link IMatchScopeProvider}.
	 * @param resources
	 *            the list of resources to be used for constructing a new {@link GenericMatchScopeProvider},
	 *            in case none is specified via the options. Has to be either of size 2 (left and right) or 3
	 *            (left, right, and ancestor).
	 * @return the {@link IMatchScopeProvider} specified via the given options or a new
	 *         {@link GenericMatchScopeProvider} instance if none is specified in the options.
	 */
	public static IMatchScopeProvider getScopeProvider(Map<String, Object> options, Resource... resources) {
		IMatchScopeProvider scopeProvider = getScopeProvider(options);
		if (scopeProvider == null) {
			if (resources.length == 2) {
				scopeProvider = new DefaultMatchScopeProvider(resources[0], resources[1]);
			} else {
				scopeProvider = new DefaultMatchScopeProvider(resources[0], resources[1], resources[2]);
			}
			setScopeProvider(options, scopeProvider);
		}
		return scopeProvider;
	}

	/**
	 * Used to obtain the {@link IMatchScopeProvider} to be used for matching. If one is specified within the
	 * provided options via {@link MatchOptions#OPTION_MATCH_SCOPE_PROVIDER}, this one is used, otherwise a
	 * new {@link GenericMatchScopeProvider} will be instantiated and returned.
	 * 
	 * @param options
	 *            the options to search for any preset {@link IMatchScopeProvider}.
	 * @param eObjects
	 *            the list of objects to be used for constructing a new {@link GenericMatchScopeProvider}, in
	 *            case none is specified via the options. Has to be either of size 2 (left and right) or 3
	 *            (left, right, and ancestor).
	 * @return the {@link IMatchScopeProvider} specified via the given options or a new
	 *         {@link GenericMatchScopeProvider} instance if none is specified in the options.
	 */
	public static IMatchScopeProvider getScopeProvider(Map<String, Object> options, EObject... eObjects) {
		IMatchScopeProvider scopeProvider = getScopeProvider(options);
		if (scopeProvider == null) {
			if (eObjects.length == 2) {
				scopeProvider = new DefaultMatchScopeProvider(eObjects[0], eObjects[1]);
			} else {
				scopeProvider = new DefaultMatchScopeProvider(eObjects[0], eObjects[1], eObjects[2]);
			}
			setScopeProvider(options, scopeProvider);
		}
		return scopeProvider;
	}

	/**
	 * Checks whether an {@link IMatchScopeProvider} instance is specified within the options via
	 * {@link MatchOptions#OPTION_MATCH_SCOPE_PROVIDER} and returns it.
	 * 
	 * @param options
	 *            the options to process.
	 * @return the {@link IMatchScopeProvider} specified within the given options, <code>null</code> if no
	 *         provider is specified.
	 */
	private static IMatchScopeProvider getScopeProvider(Map<String, Object> options) {
		IMatchScopeProvider scopeProvider = null;
		if (options != null) {
			scopeProvider = (IMatchScopeProvider)options.get(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER);
		}
		return scopeProvider;
	}

	/**
	 * Sets the scope provider to be used for this comparison.
	 * 
	 * @param options
	 *            the options to process.
	 * @param provider
	 *            the {@link IMatchScopeProvider} that is to be put in the given option map.
	 */
	private static void setScopeProvider(Map<String, Object> options, IMatchScopeProvider provider) {
		if (options != null) {
			try {
				options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, provider);
			} catch (UnsupportedOperationException e) {
				// Don't store the scope provider
			}
		}
	}
}
