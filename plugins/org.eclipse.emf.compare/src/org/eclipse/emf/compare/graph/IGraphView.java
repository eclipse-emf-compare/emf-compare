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
package org.eclipse.emf.compare.graph;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Read-only view of a {@link org.eclipse.emf.compare.internal.utils.Graph}.
 * 
 * @param <E>
 *            Kind of elements used as this graph's nodes.
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.3
 */
public interface IGraphView<E> {

	/**
	 * Checks whether this graph already contains the given element.
	 * 
	 * @param element
	 *            Element we need to check.
	 * @return <code>true</code> if this graph already contains the given elment, <code>false</code>
	 *         otherwise.
	 */
	boolean contains(E element);

	/**
	 * Checks if the given element is a parent of the given potential child, directly or not.
	 * 
	 * @param parent
	 *            Element that could be a parent of <code>potentialChild</code>.
	 * @param potentialChild
	 *            The potential child of <code>parent</code>.
	 * @return <code>true</code> if <code>parent</code> is an ancestor of <code>potentialChild</code>.
	 */
	boolean hasChild(E parent, E potentialChild);

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
	ImmutableSet<E> getDirectParents(E element);

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
	E getParentData(E element);

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
	ImmutableSet<E> getSubgraphContaining(E element);

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
	ImmutableSet<E> getSubgraphContaining(E element, ImmutableSet<E> endPoints);

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
	ImmutableSet<E> getTreeFrom(E root);

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
	ImmutableSet<E> getTreeFrom(E root, Set<E> endPoints);

	/**
	 * Returns a breadth-first iterator over this whole graph. This will begin iteration on this graph's roots
	 * (whether they are linked together (directly or indirectly) or not), then carry on over each depth
	 * level. This will never visit the same element twice, nor will it ever visit an element which parents
	 * haven't all been iterated over yet.
	 * <p>
	 * The returned iterator does not support removal, and will fail or returned undefined results if this
	 * graph is modified after the iterator's creation.
	 * </p>
	 * 
	 * @return A breadth-first iterator over this whole graph.
	 */
	PruningIterator<E> breadthFirstIterator();

}
