/*******************************************************************************
 * Copyright (c) 2011, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.URIStorage;
import org.eclipse.emf.compare.utils.DelegatingURIConverter;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;

/**
 * This implementation of an URI converter will keep track of the storages from which it created input
 * streams.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class StorageURIConverter extends DelegatingURIConverter {
	/** Keeps references towards the revisions that we've loaded through this URI converter. */
	private Set<IStorage> loadedRevisions;

	/**
	 * Constructs our uri converter given its delegate.
	 * 
	 * @param delegate
	 *            The delegate URI Converter.
	 */
	public StorageURIConverter(URIConverter delegate) {
		super(delegate);
		this.loadedRevisions = Sets.newSetFromMap(new ConcurrentHashMap<IStorage, Boolean>());
	}

	/**
	 * Allows clients of this API to retrieve the set of revisions that were loaded while resolving the
	 * resource set on which this converter is installed.
	 * 
	 * @return The set of revisions loaded through this converter.
	 */
	public Set<IStorage> getLoadedRevisions() {
		return loadedRevisions;
	}

	@Override
	public InputStream createInputStream(URI uri) throws IOException {
		return createInputStream(uri, Maps.newHashMap());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.utils.DelegatingURIConverter#createInputStream(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		final URI normalizedURI = normalize(uri);
		final URIHandler handler = getURIHandler(normalizedURI);
		// Only keep track of the loaded resource if it's a platform:/resource.
		// Resources loaded from plugins don't need to be part of our final logical model.
		if (uri.isPlatformResource()) {
			getLoadedRevisions().add(createStorage(uri, handler, this));
		}

		final Map<Object, Object> actualOptions = Maps.newLinkedHashMap();
		actualOptions.put(URIConverter.OPTION_URI_CONVERTER, this);
		if (options != null) {
			actualOptions.putAll(options);
		}
		return handler.createInputStream(normalizedURI, actualOptions);
	}

	/**
	 * Creates a new IStorage for the given URI.
	 * 
	 * @param uri
	 *            The uri for which we need a storage.
	 * @param handler
	 *            The URI handler that can be used to retrieve this URI's contents.
	 * @param converter
	 *            The URI converter from which this storage was created.
	 * @return The newly created IStorage.
	 */
	protected IStorage createStorage(URI uri, URIHandler handler, URIConverter converter) {
		return new URIStorage(uri, handler, converter);
	}
}
