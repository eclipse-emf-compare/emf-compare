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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This structure will be used to maintain a undirected graph of elements.
 * <p>
 * Take note that the elements of this graph are not necessarily all connected together. This can be used to
 * represent a set of trees, a set of undirected graphs, a set of roots with no children...
 * </p>
 * 
 * @param <E>
 *            Kind of elements used as this graph's nodes.
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class Graph<E> {
	/** Keeps track of this graph's individual nodes. */
	private final Map<E, Node<E>> nodes;

	/** Constructs an empty graph. */
	public Graph() {
		this.nodes = new LinkedHashMap<E, Node<E>>();
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
		synchronized(nodes) {
			return nodes.containsKey(element);
		}
	}

	/**
	 * Adds a new element to this graph, if it does not exists yet. Elements will initially have no connection
	 * to other elements, and can thus be considered "roots" of the graph.
	 * 
	 * @param element
	 *            The element to add as a new root to this graph.
	 * @return <code>true</code> if this element did not previously exist in the graph.
	 */
	public boolean add(E element) {
		synchronized(nodes) {
			Node<E> node = nodes.get(element);
			if (node == null) {
				node = new Node<E>(element);
				nodes.put(element, node);
				return true;
			}
			return false;
		}
	}

	/**
	 * Removes the given element's node from this graph. This will effectively break all connections to that
	 * node.
	 * 
	 * @param element
	 *            The element which is to be removed from this graph.
	 */
	public void remove(E element) {
		synchronized(nodes) {
			final Node<E> node = nodes.remove(element);
			if (node != null) {
				node.breakConnections();
			}
		}
	}

	/**
	 * Connects the given set of elements to another. Note that nodes will be created for all new elements if
	 * they do not exist yet.
	 * 
	 * @param element
	 *            The element that is to be connected with others.
	 * @param connections
	 *            The set of elements to connect to the given parent.
	 */
	public void createConnections(E element, Set<E> connections) {
		synchronized(nodes) {
			Node<E> node = nodes.get(element);
			if (node == null) {
				node = new Node<E>(element);
				nodes.put(element, node);
			}

			for (E newConnection : connections) {
				Node<E> connectedNode = nodes.get(newConnection);
				if (connectedNode == null) {
					connectedNode = new Node<E>(newConnection);
					nodes.put(newConnection, connectedNode);
				}
				node.connectTo(connectedNode);
			}
		}
	}

	/**
	 * Returns an iterable over all elements of the subgraph containing the given element.
	 * 
	 * @param element
	 *            Element we need the subgraph of.
	 * @return An iterable over all elements of the subgraph containing the given element, an empty list if
	 *         that element is not present in this graph.
	 */
	public Iterable<E> getSubgraphContaining(E element) {
		synchronized(nodes) {
			final Node<E> node = nodes.get(element);
			if (node != null) {
				return new SubgraphBuilder<E>(node).build();
			}
			return Collections.emptyList();
		}
	}

	/**
	 * This will be used to represent individual elements in our graph.
	 * 
	 * @param <K>
	 *            Type of the Node's underlying data.
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class Node<K> {
		/** Underlying data of this Node. */
		private final K element;

		/** Nodes that are connected with this one. */
		private final Set<Node<K>> connectedNodes;

		/**
		 * Construct a new Node for the given element.
		 * 
		 * @param element
		 *            The element for which we need a graph Node.
		 */
		public Node(K element) {
			this.element = element;
			this.connectedNodes = new LinkedHashSet<Node<K>>();
		}

		/**
		 * Returns all other Nodes connected to this one.
		 * 
		 * @return All other Nodes connected to this one.
		 */
		public Set<Node<K>> getConnections() {
			return Collections.unmodifiableSet(connectedNodes);
		}

		/**
		 * Registers a link between the given Node and this one.
		 * 
		 * @param other
		 *            The Node that is to be connected to this one.
		 */
		public void connectTo(Node<K> other) {
			this.connectedNodes.add(other);
			other.connectedNodes.add(this);
		}

		/**
		 * Breaks the connection from the given other node to this one.
		 * 
		 * @param other
		 *            The node which connection to this one is to be broken.
		 */
		public void breakConnections() {
			for (Node<K> connected : this.connectedNodes) {
				connected.connectedNodes.remove(this);
			}
			this.connectedNodes.clear();
		}

		/**
		 * Returns the underlying data of this Node.
		 * 
		 * @return The underlying data of this Node.
		 */
		public K getElement() {
			return element;
		}
	}

	/**
	 * This will be used to create a set over an entire subgraph given a starting point in said subgraph.
	 * 
	 * @param <L>
	 *            Kind of elements the created set is meant to contain.
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class SubgraphBuilder<L> {
		/** The Node that will be used as a starting point of the iteration. */
		private final Node<L> start;

		/** Keeps track of the elements we've already iterated over. */
		protected final Set<L> set;

		/**
		 * Constructs a new iterable given the starting point in the target subgraph.
		 * 
		 * @param start
		 *            Starting point of the iteration.
		 */
		public SubgraphBuilder(Node<L> start) {
			this.start = start;
			this.set = new LinkedHashSet<L>();
			this.set.add(start.getElement());
		}

		/**
		 * Constructs the set corresponding to the required subgraph.
		 * 
		 * @return The set of all nodes composing the required subgraph.
		 */
		public Set<L> build() {
			return ImmutableSet.copyOf(new NodeIterator(start));
		}

		/**
		 * A node iterator will allow us to iterate over the data of a Node and the data of all directly
		 * connected Nodes; all the while avoiding data we've already iterated over.
		 * <p>
		 * This kind of iterator does not support {@link #remove() removal}.
		 * </p>
		 * 
		 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
		 */
		private class NodeIterator implements Iterator<L> {
			/** Iterator over the all nodes directly connected with the underlying Node. */
			private final Iterator<Node<L>> nodesIterator;

			/**
			 * Keeps track of the next Node's iterator. This will allow recursion over the connected Nodes'
			 * own connections.
			 */
			private Iterator<L> nextNodeIterator;

			/** Element that should be returned by the next call to {@link #next()}. */
			private L next;

			/**
			 * Construct an iterator over the given Node's connected data.
			 * 
			 * @param node
			 *            The node for which we need an iterator.
			 */
			public NodeIterator(Node<L> node) {
				this.next = node.getElement();
				this.nodesIterator = node.getConnections().iterator();
				prepareNextIterator();
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.util.Iterator#hasNext()
			 */
			public boolean hasNext() {
				return next != null || nextNodeIterator.hasNext() || nodesIterator.hasNext();
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.util.Iterator#next()
			 */
			public L next() {
				if (next == null) {
					throw new NoSuchElementException();
				}
				L result = next;
				prepareNext();
				return result;
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.util.Iterator#remove()
			 */
			public void remove() {
				throw new UnsupportedOperationException();
			}

			/**
			 * This will be called to prepare for a subsequent call to {@link #next()}.
			 */
			private void prepareNext() {
				if (!nextNodeIterator.hasNext()) {
					prepareNextIterator();
				}
				if (nextNodeIterator.hasNext()) {
					next = nextNodeIterator.next();
				} else {
					next = null;
				}
			}

			/**
			 * This will be called whenever we've consumed all nodes connected to the current to "jump over"
			 * to the next one.
			 */
			private void prepareNextIterator() {
				if (nodesIterator.hasNext()) {
					Node<L> nextNode = nodesIterator.next();
					while (SubgraphBuilder.this.set.contains(nextNode.getElement())
							&& nodesIterator.hasNext()) {
						nextNode = nodesIterator.next();
					}
					if (SubgraphBuilder.this.set.add(nextNode.getElement())) {
						nextNodeIterator = new NodeIterator(nextNode);
					} else {
						nextNodeIterator = Iterators.emptyIterator();
					}
				} else {
					nextNodeIterator = Iterators.emptyIterator();
				}
			}
		}
	}
}
