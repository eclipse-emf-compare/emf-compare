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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;

/**
 * This registry implement its own strategy to define the "best" resolver to use.
 * <p>
 * This registry is based on {@link ModelResolverManager}. If the resolving mechanism is disabled then it
 * always returns a resolver that does not resolve anything else than the current resource.
 * </p>
 * <p>
 * This registry will also try to evaluate the {@link ModelResolverManager#getUserSelectedResolver()} before
 * any other. If the user selected resolver can not handle the comparison or if the user did not select a specific resolver then
 * the ranking mechanism is used.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelResolverRegistryImpl implements IModelResolverRegistry {

	/** Keeps track of the extensions providing model resolvers. */
	private final ModelResolverManager resolverManager;

	private final NotResolvingModelResolver noResolvingResolver;

	/** Initializes our registry. */
	public ModelResolverRegistryImpl(ModelResolverManager resolvers) {
		this.resolverManager = resolvers;
		noResolvingResolver = new NotResolvingModelResolver();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#getBestResolverFor(org.eclipse.core.resources.IStorage)
	 */
	public IModelResolver getBestResolverFor(IStorage sourceStorage) {
		if (!resolverManager.isResolvingEnable()) {
			return noResolvingResolver;
		}
		ModelResolverDescriptor resolver = resolverManager.getUserSelectedResolver();

		if (resolver == null || !resolver.getModelResolver().canResolve(sourceStorage)) {
			for (ModelResolverDescriptor candidateDescriptor : resolverManager.getAllResolver()) {
				IModelResolver candidate = candidateDescriptor.getModelResolver();
				if (candidate.canResolve(sourceStorage) && ranking(resolver) < ranking(candidateDescriptor)) {
					resolver = candidateDescriptor;
				}
			}
		}

		if (resolver != null) {
			return resolver.getModelResolver();
		}
		return null;
	}

	/**
	 * Returns a ranking for the given resolver, which can be <code>null</code>.
	 * 
	 * @param resolver
	 *            The resolver we need a ranking for.
	 * @return Ranking of the given resolver, <code>-1</code> if that resolver is <code>null</code>.
	 */
	private static final int ranking(ModelResolverDescriptor resolver) {
		if (resolver == null) {
			return -1;
		}
		return resolver.getModelResolver().getRanking();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#addResolver(java.lang.String,
	 *      org.eclipse.emf.compare.ide.ui.logical.IModelResolver)
	 */
	public void addResolver(String key, IModelResolver resolver) {
		resolverManager.add(resolver, key, null, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#removeResolver(java.lang.String)
	 */
	public IModelResolver removeResolver(String key) {
		ModelResolverDescriptor descriptor = resolverManager.remove(key);
		if (descriptor != null) {
			return descriptor.getModelResolver();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IModelResolverRegistry#clear()
	 */
	public void clear() {
		noResolvingResolver.dispose();
		resolverManager.clear();
	}
}
