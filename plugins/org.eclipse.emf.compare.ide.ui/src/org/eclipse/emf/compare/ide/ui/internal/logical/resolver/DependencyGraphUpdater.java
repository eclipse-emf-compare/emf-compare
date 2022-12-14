/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - refactorings
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Collections;

import org.eclipse.emf.compare.graph.IGraph;
import org.eclipse.emf.compare.rcp.EMFCompareLogger;

/**
 * This class's responsibility is to maintain the state of its graph when notified that a new model resource
 * or a new dependency have been found.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DependencyGraphUpdater<T> {

	/** The graph of dependencies between the resources. */
	private final IGraph<T> dependencyGraph;

	/** The logger. */
	private static final EMFCompareLogger LOGGER = new EMFCompareLogger(DependencyGraphUpdater.class);

	/**
	 * Constructor.
	 * 
	 * @param graph
	 *            The graph, must not be null.
	 * @param eventBus
	 *            The event bus that will fire events to record.
	 */
	public DependencyGraphUpdater(IGraph<T> graph, EventBus eventBus) {
		this.dependencyGraph = checkNotNull(graph);
		eventBus.register(this);
	}

	/**
	 * Register a discovered resource in the graph.
	 * 
	 * @param event
	 *            Event that describes the discovered resource.
	 */
	@Subscribe
	public synchronized void recordNode(ResolvedEvent<T> event) {
		dependencyGraph.add(event.getNode());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Added node " + event.getNode()); //$NON-NLS-1$
		}
	}

	/**
	 * Register a dependency in the graph.
	 * 
	 * @param event
	 *            Event that describes the dependency.
	 */
	@Subscribe
	public synchronized void recordEdge(DependencyFoundEvent<T> event) {
		dependencyGraph.addChildren(event.getFrom(), Collections.singleton(event.getTo()));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Added edge " + event.getFrom() + " -> " + event.getTo()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (event.hasParent()) {
			dependencyGraph.addParentData(event.getTo(), event.getParent().get());
		}
	}

	/**
	 * Register removal of nodes.
	 * 
	 * @param event
	 *            The event indicating the removed nodes.
	 */
	@Subscribe
	public synchronized void recordRemoval(ResourceRemovedEvent<T> event) {
		dependencyGraph.removeAll(event.getElements());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Cleared " + event.getElements().size() + " nodes."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
