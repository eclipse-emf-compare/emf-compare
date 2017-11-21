/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

/**
 * This implementation of an {@link URIConverter URI Converter} will delegate all calls to another.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 1.3
 */
public class DelegatingURIConverter extends ExtensibleURIConverterImpl {
	/** Our delegate {@link URIConverter}. */
	private URIConverter delegate;

	/**
	 * Instantiates our {@link URIConverter} given its delegate.
	 * 
	 * @param delegateURIConverter
	 *            Our delegate {@link URIConverter}.
	 */
	public DelegatingURIConverter(URIConverter delegateURIConverter) {
		this.delegate = delegateURIConverter;

		// Reset what has been done by the super constructor
		uriHandlers.clear();
		contentHandlers.clear();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#contentDescription(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException {
		return delegate.contentDescription(uri, options);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#createInputStream(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		return delegate.createInputStream(uri, options);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#createOutputStream(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		return delegate.createOutputStream(uri, options);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#delete(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public void delete(URI uri, Map<?, ?> options) throws IOException {
		delegate.delete(uri, options);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#exists(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		return delegate.exists(uri, options);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#getAttributes(org.eclipse.emf.common.util.URI,
	 *      java.util.Map)
	 */
	@Override
	public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
		return delegate.getAttributes(uri, options);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#getContentHandlers()
	 */
	@Override
	public EList<ContentHandler> getContentHandlers() {
		// This will be called during init, before the delegate is set
		if (delegate == null) {
			return super.getContentHandlers();
		}
		return delegate.getContentHandlers();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#getURIHandler(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public URIHandler getURIHandler(URI uri) {
		return delegate.getURIHandler(uri);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#getURIHandlers()
	 */
	@Override
	public EList<URIHandler> getURIHandlers() {
		// This will be called during init, before the delegate is set
		if (delegate == null) {
			return super.getURIHandlers();
		}
		return delegate.getURIHandlers();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#getURIMap()
	 */
	@Override
	public Map<URI, URI> getURIMap() {
		return delegate.getURIMap();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#normalize(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public URI normalize(URI uri) {
		return delegate.normalize(uri);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.resource.URIConverter#setAttributes(org.eclipse.emf.common.util.URI,
	 *      java.util.Map, java.util.Map)
	 */
	@Override
	public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException {
		delegate.setAttributes(uri, attributes, options);
	}
}
