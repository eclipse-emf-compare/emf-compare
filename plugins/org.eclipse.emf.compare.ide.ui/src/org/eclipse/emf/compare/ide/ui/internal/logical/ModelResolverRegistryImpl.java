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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;

/**
 * This registry will be used to keep track of all {@link IModelResolver}s that have been registered against
 * the extension point. Clients can query the registry to determine which resolver should be used for a given
 * resource.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelResolverRegistryImpl implements IModelResolverRegistry {
	/** Keeps track of the extensions providing model resolvers. */
	private final Map<String, IModelResolver> resolvers;

	/** Initializes our registry. */
	public ModelResolverRegistryImpl() {
		this.resolvers = new ConcurrentHashMap<String, IModelResolver>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#getBestResolverFor(org.eclipse.core.resources.IStorage)
	 */
	public IModelResolver getBestResolverFor(IStorage sourceStorage) {
		IModelResolver resolver = null;
		for (IModelResolver candidate : resolvers.values()) {
			if (candidate.canResolve(sourceStorage) && ranking(resolver) < ranking(candidate)) {
				resolver = candidate;
			}
		}
		return resolver;
	}

	/**
	 * Returns a ranking for the given resolver, which can be <code>null</code>.
	 * 
	 * @param resolver
	 *            The resolver we need a ranking for.
	 * @return Ranking of the given resolver, <code>-1</code> if that resolver is <code>null</code>.
	 */
	private static final int ranking(IModelResolver resolver) {
		if (resolver == null) {
			return -1;
		}
		return resolver.getRanking();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#addResolver(java.lang.String,
	 *      org.eclipse.emf.compare.ide.ui.logical.IModelResolver)
	 */
	public void addResolver(String key, IModelResolver resolver) {
		resolvers.put(checkNotNull(key), checkNotNull(resolver));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#removeResolver(java.lang.String)
	 */
	public void removeResolver(String key) {
		resolvers.remove(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#clear()
	 */
	public void clear() {
		resolvers.clear();
	}
}
