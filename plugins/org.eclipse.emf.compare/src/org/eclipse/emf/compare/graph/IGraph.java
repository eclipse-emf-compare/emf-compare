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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Interface of a directed graph.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @param <E>
 *            The type of the graph nodes
 * @since 3.3
 */
public interface IGraph<E> {

	/**
	 * Checks whether this graph already contains the given element.
	 * 
	 * @param element
	 *            Element we need to check.
	 * @return <code>true</code> if this graph already contains the given elment, <code>false</code>
	 *         otherwise.
	 */
	boolean contains(E element);

	/** Clears this graph and goes back to a pristine state. */
	void clear();

	/**
	 * Adds a new element to this graph, if it does not exists yet. Elements will initially have no connection
	 * to other elements, and can thus be considered "roots" of the graph.
	 * 
	 * @param element
	 *            The element to add as a new root to this graph.
	 * @return <code>true</code> if this element did not previously exist in the graph.
	 */
	boolean add(E element);

	/**
	 * Removes the given element's node from this graph. This will effectively break all connections to that
	 * node.
	 * 
	 * @param element
	 *            The element which is to be removed from this graph.
	 */
	void remove(E element);

	/**
	 * Removes the given elements' nodes from this graph. This will effectively break all connections to these
	 * nodes.
	 * 
	 * @param elements
	 *            The elements which are to be removed from this graph.
	 */
	void removeAll(Collection<E> elements);

	/**
	 * Connects the given set of elements to a given parent. Note that nodes will be created for all new
	 * elements if they do not exist yet.
	 * 
	 * @param element
	 *            The element that is to be connected with new children.
	 * @param newChildren
	 *            The set of elements to connect to the given parent.
	 */
	void addChildren(E element, Set<E> newChildren);

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
	 * <b>Note</b> that the returned set is a view over a sub-graph of this graph, and that changes to it will
	 * not be reflected within the graph itself.
	 * </p>
	 * 
	 * @param element
	 *            The element which parents we seek.
	 * @return The set of <u>direct</u> parents for the given <code>element</code>.
	 */
	Set<E> getDirectParents(E element);

	/**
	 * Returns the tree starting from the given root element if it is contained in the graph.
	 * <p>
	 * Contrarily to {@link #getSubgraphContaining(Object)}, this will only iterate over the children (and
	 * recursively) of the given node, without ever "going up" to parents of these children.
	 * </p>
	 * <p>
	 * <b>Note</b> that the returned set is a view over a sub-graph of this graph, and that changes to it will
	 * not be reflected within the graph itself.
	 * </p>
	 * 
	 * @param root
	 *            The element we are to consider as the root of a tree.
	 * @return The tree starting from the given root element if it is contained in this graph, and empty set
	 *         otherwise.
	 */
	Set<E> getTreeFrom(E root);

	/**
	 * Returns the tree starting from the given root element and ending at the given boundaries..
	 * <p>
	 * Contrarily to {@link #getSubgraphContaining(Object, Set)}, this will only iterate over the children
	 * (and recursively) of the given node, without ever "going up" to parents of these children.
	 * </p>
	 * <p>
	 * <b>Note</b> that the returned set is a view over a sub-graph of this graph, and that changes to it will
	 * not be reflected within the graph itself.
	 * </p>
	 * 
	 * @param root
	 *            The element we are to consider as the root of a tree.
	 * @param endPoints
	 *            Boundaries of the tree.
	 * @return The tree starting from the given root element if it is contained in this graph, and empty set
	 *         otherwise.
	 */
	Set<E> getTreeFrom(E root, Set<E> endPoints);

	/**
	 * Returns the set of all elements of the subgraph containing the given element.
	 * <p>
	 * <b>Note</b> that the returned set is a view over a sub-graph of this graph, and that changes to it will
	 * not be reflected within the graph itself.
	 * </p>
	 * 
	 * @param element
	 *            Element we need the subgraph of.
	 * @return The set of all elements of the subgraph containing the given element, an empty set if that
	 *         element is not present in this graph.
	 */
	Set<E> getSubgraphContaining(E element);

	/**
	 * Returns the set of all elements of the subgraph containing the given element and ending at the given
	 * boundaries.
	 * <p>
	 * <b>Note</b> that the returned set is a view over a sub-graph of this graph, and that changes to it will
	 * not be reflected within the graph itself.
	 * </p>
	 * 
	 * @param element
	 *            Element we need the subgraph of.
	 * @param endPoints
	 *            Boundaries of the needed subgraph.
	 * @return An iterable over all elements of the subgraph containing the given element, an empty set if
	 *         that element is not present in this graph.
	 */
	Set<E> getSubgraphContaining(E element, Set<E> endPoints);

	/**
	 * Returns a breadth-first iterator over this whole graph. This will begin iteration on this graph's roots
	 * (whether they are linked together (directly or indirectly) or not), then carry on over each depth
	 * level. This will never visit the same element twice, nor will it ever visit an element which parents
	 * haven't all been iterated over yet.
	 * <p>
	 * The returned iterator does not support removal, and will fail or return undefined results if this graph
	 * is modified after the iterator's creation.
	 * </p>
	 * 
	 * @return A breadth-first iterator over this whole graph.
	 */
	PruningIterator<E> breadthFirstIterator();

	/**
	 * Returns a depth first iterator created with the given element as root. If the graph contains cycles,
	 * the same node won't be returned twice.
	 * <p>
	 * The root will be returned first, then the left-most child of that root, then the left-most child of
	 * that child if any, or the closest sibling to the right of the current element. For example, with the
	 * following tree:
	 * 
	 * <pre>
	 *     A
	 *    / \
	 *   B   C
	 *  /   / \
	 * D   E   F
	 * </pre>
	 * 
	 * The iteration order will be : A, B, D, C, E, F.
	 * </p>
	 * <p>
	 * The returned iterator does not support removal, and will fail or return undefined results if this graph
	 * is modified after the iterator's creation.
	 * </p>
	 * 
	 * @param root
	 *            The root of the tree over which we need to iterate.
	 * @return An iterator over the tree starting from the given root.
	 */
	Iterator<E> depthFirstIterator(E root);

	/**
	 * Get the parent data of the given element.
	 * <p>
	 * The parent data, is the URI of the parent resource's object in case of a containment relationship
	 * between the given element and its parent.
	 * <p>
	 * 
	 * @param element
	 *            Element we need the parent data of.
	 * @return A parent data of type <code>E</code> if this element has a parent data, <code>null</code>
	 *         otherwise.
	 */
	E getParentData(E element);

	/**
	 * Set the parent data for the given element.
	 * <p>
	 * The parent data, is the URI of the parent resource's object in case of a containment relationship
	 * between the given element and its parent.
	 * </p>
	 * If the given element has several parents, then the addition of a new parent data results in delete the
	 * parent data. If the given element has no parents, then the addition of a new parent data results in
	 * delete the parent data.
	 * 
	 * @param element
	 *            Element for which we need to set the parent data.
	 * @param parentData
	 *            The parent data to set.
	 */
	void addParentData(E element, E parentData);

}
