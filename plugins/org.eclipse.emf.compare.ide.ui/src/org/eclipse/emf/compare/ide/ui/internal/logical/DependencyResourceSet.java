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

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This particular resource set will construct a dependency graph between the resources it loads, without
 * actually keeping them in memory. The graph can be later queried in order to determine the subgraph
 * corresponding to the dependencies of one given resource.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DependencyResourceSet extends SyncResourceSet {
	/** Keeps track of the dependency tree discovered through this resource set. */
	private final Graph<URI> graph;

	/**
	 * Constructs a resource set given the dependency graph it will populate.
	 * 
	 * @param graph
	 *            The dependency graph we'll populate as we resolve new references.
	 */
	public DependencyResourceSet(Graph<URI> graph) {
		this.graph = graph;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet#createResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public synchronized Resource createResource(URI uri) {
		final Resource resource = super.createResource(uri);
		graph.add(uri);
		return resource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet#createResource(org.eclipse.emf.common.util.URI,
	 *      java.lang.String)
	 */
	@Override
	public synchronized Resource createResource(URI uri, String contentType) {
		final Resource resource = super.createResource(uri, contentType);
		graph.add(uri);
		return resource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet#resolveCrossReference(org.eclipse.emf.ecore.resource.Resource,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected void resolveCrossReference(Resource resource, EObject proxy) {
		super.resolveCrossReference(resource, proxy);
		URI parent = resource.getURI();
		URI child = ((InternalEObject)proxy).eProxyURI().trimFragment();
		if (parent.isPlatformResource() && child.isPlatformResource()) {
			graph.addChildren(parent, Collections.singleton(child));
		}
	}
}
