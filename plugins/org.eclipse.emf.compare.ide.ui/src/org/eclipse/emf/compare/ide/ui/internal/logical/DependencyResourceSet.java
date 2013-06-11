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
	private final Graph<URI> graph = new Graph<URI>();

	/**
	 * Returns an iterable over all the URIs that compose the logical model of the given one, whether they are
	 * parents of children of the resource at that location. Note that only resources located in the workspace
	 * are considered.
	 * 
	 * @param uri
	 *            Uri we need the dependency graph for.
	 * @return An iterable over the logical model of <code>uri</code>.
	 */
	public Iterable<URI> getDependencyGraph(URI uri) {
		return graph.getSubgraphContaining(uri);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.internal.utils.SyncResourceSet#loadResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected Resource loadResource(URI uri) {
		Resource loaded = super.loadResource(uri);
		graph.add(loaded.getURI());
		return loaded;
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
			graph.createConnections(parent, Collections.singleton(child));
		}
	}
}
