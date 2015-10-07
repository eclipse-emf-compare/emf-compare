/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.utils;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import org.eclipse.emf.compare.graph.IGraph;
import org.eclipse.emf.compare.graph.IGraphView;
import org.eclipse.emf.compare.graph.PruningIterator;

/**
 * Read-only version of an already existing {@link org.eclipse.emf.compare.internal.utils.Graph}.
 * 
 * @param <E>
 *            Kind of elements used as this graph's nodes.
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public final class ReadOnlyGraph<E> implements IGraphView<E> {

	/** The writable graph. */
	private final IGraph<E> graph;

	/**
	 * Constructor.
	 * 
	 * @param graph
	 *            the graph to convert.
	 */
	private ReadOnlyGraph(IGraph<E> graph) {
		this.graph = graph;
	}

	/**
	 * Convert a graph to a read-only graph.
	 * 
	 * @param <E>
	 *            Kind of elements used as this graph's nodes.
	 * @param graph
	 *            The writable graph to convert.
	 * @return a read-only graph version of the given graph.
	 */
	public static <E> ReadOnlyGraph<E> toReadOnlyGraph(IGraph<E> graph) {
		return new ReadOnlyGraph<E>(graph);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#contains(Object)
	 */
	public boolean contains(E element) {
		return graph.contains(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#hasChild(Object, Object)
	 */
	public boolean hasChild(E parent, E potentialChild) {
		return graph.hasChild(parent, potentialChild);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#getDirectParents(Object)
	 */
	public ImmutableSet<E> getDirectParents(E element) {
		return ImmutableSet.copyOf(graph.getDirectParents(element));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#getParentData(Object)
	 */
	public E getParentData(E element) {
		return graph.getParentData(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#getSubgraphContaining(Object)
	 */
	public ImmutableSet<E> getSubgraphContaining(E element) {
		return getSubgraphContaining(element, ImmutableSet.<E> of());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#getSubgraphContaining(Object, ImmutableSet)
	 */
	public ImmutableSet<E> getSubgraphContaining(E element, ImmutableSet<E> endPoints) {
		return ImmutableSet.copyOf(graph.getSubgraphContaining(element, endPoints));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#getTreeFrom(Object)
	 */
	public ImmutableSet<E> getTreeFrom(E root) {
		return getTreeFrom(root, ImmutableSet.<E> of());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#getTreeFrom(Object, Set)
	 */
	public ImmutableSet<E> getTreeFrom(E root, Set<E> endPoints) {
		return ImmutableSet.copyOf(graph.getTreeFrom(root, endPoints));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.graph.IGraphView#breadthFirstIterator()
	 */
	public PruningIterator<E> breadthFirstIterator() {
		return graph.breadthFirstIterator();
	}
}
