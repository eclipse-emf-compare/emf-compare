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
package org.eclipse.emf.compare.match;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EObject;

/**
 * Default implementation of {@link IEqualityHelperFactory}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DefaultEqualityHelperFactory implements IEqualityHelperFactory {
	/** CacheBuilder that will be used to instantiate a cache. */
	private final CacheBuilder<Object, Object> cacheBuilder;

	/**
	 * Default constructor.
	 */
	public DefaultEqualityHelperFactory() {
		this(CacheBuilder.newBuilder().maximumSize(DefaultMatchEngine.DEFAULT_EOBJECT_URI_CACHE_MAX_SIZE));
	}

	/**
	 * Creates a factory with the given CacheBuilder.
	 * 
	 * @param cacheBuilder
	 *            The cache builder to use to instantiate an {@link EqualityHelper}.
	 */
	public DefaultEqualityHelperFactory(CacheBuilder<Object, Object> cacheBuilder) {
		this.cacheBuilder = cacheBuilder;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.IEqualityHelperFactory#createEqualityHelper()
	 */
	public IEqualityHelper createEqualityHelper() {
		Cache<EObject, URI> cache = EqualityHelper.createDefaultCache(getCacheBuilder());
		IEqualityHelper equalityHelper = new EqualityHelper(cache);
		return equalityHelper;
	}

	/**
	 * Returns the cache builder that should be used by this factory to create its equality helpers.
	 * 
	 * @return The cache builder that should be used by this factory to create its equality helpers.
	 */
	protected CacheBuilder<Object, Object> getCacheBuilder() {
		return cacheBuilder;
	}
}
