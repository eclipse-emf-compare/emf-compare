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

/**
 * Read-only version of an already existing {@link org.eclipse.emf.compare.internal.utils.Graph}.
 * 
 * @param <E>
 *            Kind of elements used as this graph's nodes.
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public final class ReadOnlyGraph<E> {

	/** The writable graph. */
	private final Graph<E> graph;

	/**
	 * Constructor.
	 * 
	 * @param graph
	 *            the graph to convert.
	 */
	private ReadOnlyGraph(Graph<E> graph) {
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
	public static <E> ReadOnlyGraph<E> toReadOnlyGraph(Graph<E> graph) {
		return new ReadOnlyGraph<E>(graph);
	}

	/**
	 * Checks whether this graph already contains the given element.
	 * 
	 * @param element
	 *            Element we need to check.
	 * @return <code>true</code> if this graph already contains the given elment, <code>false</code>
	 *         otherwise.
	 */
	public boolean contains(E element) {
		return graph.contains(element);
	}

	/**
	 * Checks if the given element is a parent of the given potential child, directly or not.
	 * 
	 * @param parent
	 *            Element that could be a parent of <code>potentialChild</code>.
	 * @param potentialChild
	 *            The potential child of <code>parent</code>.
	 * @return <code>true</code> if <code>parent</code> is an ancestor of <code>potentialChild</code>.
	 */
	public boolean hasChild(E parent, E potentialChild) {
		return graph.hasChild(parent, potentialChild);
	}

	/**
	 * Returns the <u>direct</u> parents of the given <code>element</code>.
	 * <p>
	 * <b>Note</b> that the returned set is an immutable view over a sub-graph of this graph.
	 * </p>
	 * 
	 * @param element
	 *            The element which parents we seek.
	 * @return An immutable set of <u>direct</u> parents for the given <code>element</code>.
	 */
	public ImmutableSet<E> getDirectParents(E element) {
		return ImmutableSet.copyOf(graph.getDirectParents(element));
	}

	/**
	 * Get the parent data of the given element. If the given element has several parents, then this method
	 * will return <code>null</code>. If the given element has no parents, then then this method will return
	 * <code>null</code>
	 * 
	 * @param element
	 *            Element we need the parent data of.
	 * @return A parent data of type <code>E</code> if this element has a parent data, <code>null</code>
	 *         otherwise.
	 */
	public E getParentData(E element) {
		return graph.getParentData(element);
	}

	/**
	 * Returns the set of all elements of the subgraph containing the given element.
	 * <p>
	 * <b>Note</b> that the returned set is an immutable view over a sub-graph of this graph.
	 * </p>
	 * 
	 * @param element
	 *            Element we need the subgraph of.
	 * @return An immutable set of all elements of the subgraph containing the given element, an empty
	 *         immutable set if that element is not present in this graph.
	 */
	public ImmutableSet<E> getSubgraphContaining(E element) {
		return getSubgraphContaining(element, ImmutableSet.<E> of());
	}

	/**
	 * Returns the set of all elements of the subgraph containing the given element and ending at the given
	 * boundaries.
	 * <p>
	 * <b>Note</b> that the returned set is an immutable view over a sub-graph of this graph.
	 * </p>
	 * 
	 * @param element
	 *            Element we need the subgraph of.
	 * @param endPoints
	 *            Boundaries of the needed subgraph.
	 * @return An immutable set over all elements of the subgraph containing the given element, an immutable
	 *         empty set if that element is not present in this graph.
	 */
	public ImmutableSet<E> getSubgraphContaining(E element, ImmutableSet<E> endPoints) {
		return ImmutableSet.copyOf(graph.getSubgraphContaining(element, endPoints));
	}

	/**
	 * Returns the tree starting from the given root element if it is contained in the graph.
	 * <p>
	 * Contrarily to {@link #getSubgraphContaining(Object)}, this will only iterate over the children (and
	 * recursively) of the given node, without ever "going up" to parents of these children.
	 * </p>
	 * <p>
	 * <b>Note</b> that the returned set is an immutable view over a sub-graph of this graph.
	 * </p>
	 * 
	 * @param root
	 *            The element we are to consider as the root of a tree.
	 * @return The immutable tree starting from the given root element if it is contained in this graph, and
	 *         immutable empty set otherwise.
	 */
	public ImmutableSet<E> getTreeFrom(E root) {
		return getTreeFrom(root, ImmutableSet.<E> of());
	}

	/**
	 * Returns the tree starting from the given root element and ending at the given boundaries..
	 * <p>
	 * Contrarily to {@link #getSubgraphContaining(Object, Set)}, this will only iterate over the children
	 * (and recursively) of the given node, without ever "going up" to parents of these children.
	 * </p>
	 * <p>
	 * <b>Note</b> that the returned set is an immutable view over a sub-graph of this graph.
	 * </p>
	 * 
	 * @param root
	 *            The element we are to consider as the root of a tree.
	 * @param endPoints
	 *            Boundaries of the tree.
	 * @return The immutable tree starting from the given root element if it is contained in this graph, and
	 *         immutable empty set otherwise.
	 */
	public ImmutableSet<E> getTreeFrom(E root, Set<E> endPoints) {
		return ImmutableSet.copyOf(graph.getTreeFrom(root, endPoints));
	}
}
