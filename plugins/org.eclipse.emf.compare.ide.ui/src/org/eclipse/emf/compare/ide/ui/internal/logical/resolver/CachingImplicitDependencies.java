/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * An implementation of the {@link IImplicitDependencies} that delegates the respective dependency calculation
 * to a given object and caches the results to increase performance.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class CachingImplicitDependencies implements IImplicitDependencies {

	/** The delegate to calculate the implicit dependencies. */
	private IImplicitDependencies delegate;

	/** The map holding the cached dependencies. */
	private Map<URIConverter, Map<URI, Set<URI>>> cache;

	/**
	 * Creates a new caching instance that delegates the dependency calculation to the given object and caches
	 * the results to increase performance.
	 * 
	 * @param delegate
	 *            The delegate performing the dependency calculation.
	 */
	public CachingImplicitDependencies(IImplicitDependencies delegate) {
		this.delegate = delegate;
		this.cache = Maps.newHashMap();
	}

	@Override
	public Set<URI> of(URI uri, URIConverter uriConverter) {
		Map<URI, Set<URI>> cachedURIDependencies = cache.get(uriConverter);
		if (cachedURIDependencies == null) {
			cachedURIDependencies = Maps.newHashMap();
			cache.put(uriConverter, cachedURIDependencies);
		}

		Set<URI> dependencies = cachedURIDependencies.get(uri);
		if (dependencies == null) {
			dependencies = delegate.of(uri, uriConverter);
			cachedURIDependencies.put(uri, dependencies);
		}
		return dependencies;
	}
}
