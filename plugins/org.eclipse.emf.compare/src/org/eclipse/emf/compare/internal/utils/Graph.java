/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.utils;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterators.concat;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.emf.common.util.AbstractTreeIterator;

/**
 * This structure will be used to maintain a undirected graph of elements.
 * <p>
 * This boils down to maintaining a list of children and parents of each node, updating them in sync with each
 * other.
 * </p>
 * <p>
 * Take note that the elements of this graph are not necessarily all connected together. This can be used to
 * represent a set of trees, a set of undirected graphs, a set of roots with no children...
 * </p>
 * <p>
 * This class is not intended to be sub-classed.
 * </p>
 * 
 * @param <E>
 *            Kind of elements used as this graph's nodes.
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class Graph<E> {
	/** Keeps track of this graph's individual nodes. */
	private final Map<E, Node<E>> nodes;

	/**
	 * keeps track of this graph's roots. This will reference all nodes with no parents themselves, whether
	 * they are part of the same subgraph or totally unrelated to each other.
	 */
	private final Set<Node<E>> roots;

	/**
	 * All operations affecting the graph will be synchronized so that changes to {@link #nodes} and
	 * {@link #roots} are consistent with each other.
	 */
	private final ReentrantLock lock;

	/**
	 * This will be incremented each time this graph is structurally modified by an operation, ensuring
	 * fail-fast iterations from our returned iterators.
	 */
	private transient volatile int modcount;

	/** Constructs an empty graph. */
	public Graph() {
		this.nodes = new LinkedHashMap<E, Node<E>>();
		this.roots = new LinkedHashSet<Node<E>>();
		this.lock = new ReentrantLock();
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
		lock.lock();
		try {
			return nodes.containsKey(element);
		} finally {
			lock.unlock();
		}
	}

	/** Clears this graph and goes back to a pristine state. */
	public void clear() {
		lock.lock();
		try {
			nodes.clear();
			roots.clear();
			modcount++;
		} finally {
			lock.unlock();
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
		lock.lock();
		try {
			Node<E> node = nodes.get(element);
			if (node == null) {
				node = new Node<E>(element);
				nodes.put(element, node);
				roots.add(node);
				modcount++;
				return true;
			}
			return false;
		} finally {
			lock.unlock();
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
		lock.lock();
		try {
			final Node<E> node = nodes.remove(element);
			if (node != null) {
				node.breakConnections();
				roots.remove(node);
				modcount++;
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Removes the given elements' nodes from this graph. This will effectively break all connections to these
	 * nodes.
	 * 
	 * @param elements
	 *            The elements which are to be removed from this graph.
	 */
	public void removeAll(Collection<E> elements) {
		lock.lock();
		try {
			for (E e : elements) {
				remove(e);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Connects the given set of elements to a given parent. Note that nodes will be created for all new
	 * elements if they do not exist yet.
	 * 
	 * @param element
	 *            The element that is to be connected with new children.
	 * @param newChildren
	 *            The set of elements to connect to the given parent.
	 */
	public void addChildren(E element, Set<E> newChildren) {
		lock.lock();
		try {
			Node<E> node = nodes.get(element);
			if (node == null) {
				node = new Node<E>(element);
				nodes.put(element, node);
				roots.add(node);
				modcount++;
			}

			for (E newChild : newChildren) {
				Node<E> childNode = nodes.get(newChild);
				if (childNode == null) {
					childNode = new Node<E>(newChild);
					nodes.put(newChild, childNode);
				} else {
					roots.remove(childNode);
				}
				modcount++;
				node.connectChild(childNode);
			}
		} finally {
			lock.unlock();
		}
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
		lock.lock();
		try {
			final Node<E> node = nodes.get(potentialChild);
			if (node != null) {
				return Iterables.any(node.getAllParents(), is(parent));
			}
			return false;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * This predicate will be used to check if a Node's element is the given one.
	 * 
	 * @param element
	 *            the element we expect a node for.
	 * @return The constructed predicate.
	 */
	private Predicate<? super Node<E>> is(final E element) {
		return new Predicate<Node<E>>() {
			public boolean apply(Node<E> input) {
				return input != null && input.getElement().equals(element);
			}
		};
	}

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
	public Set<E> getDirectParents(E element) {
		final Set<E> parents = new LinkedHashSet<E>();
		lock.lock();
		try {
			final Node<E> node = nodes.get(element);
			if (node != null) {
				for (Node<E> parent : node.getParents()) {
					parents.add(parent.getElement());
				}
			}
		} finally {
			lock.unlock();
		}
		return parents;
	}

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
	public Set<E> getTreeFrom(E root) {
		return getTreeFrom(root, Collections.<E> emptySet());
	}

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
	public Set<E> getTreeFrom(E root, Set<E> endPoints) {
		lock.lock();
		try {
			final Node<E> node = nodes.get(root);
			if (node != null) {
				Set<E> boundaries = endPoints;
				if (boundaries == null) {
					boundaries = Collections.emptySet();
				}
				return new SubgraphBuilder<E>(node, boundaries).buildTree();
			}
		} finally {
			lock.unlock();
		}
		return Collections.emptySet();
	}

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
	public Set<E> getSubgraphContaining(E element) {
		return getSubgraphContaining(element, Collections.<E> emptySet());
	}

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
	public Set<E> getSubgraphContaining(E element, Set<E> endPoints) {
		lock.lock();
		try {
			final Node<E> node = nodes.get(element);
			if (node != null) {
				Set<E> boundaries = endPoints;
				if (boundaries == null) {
					boundaries = Collections.emptySet();
				}
				return new SubgraphBuilder<E>(node, boundaries).buildSubgraph();
			}
			return Collections.emptySet();
		} finally {
			lock.unlock();
		}
	}

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
	public PruningIterator<E> breadthFirstIterator() {
		return new BreadthFirstIterator();
	}

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
	public E getParentData(E element) {
		lock.lock();
		try {
			Node<E> node = nodes.get(element);
			if (node != null) {
				return node.getParentData();
			}
		} finally {
			lock.unlock();
		}
		return null;
	}

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
	public void addParentData(E element, E parentData) {
		lock.lock();
		try {
			Node<E> node = nodes.get(element);
			if (node != null) {
				E tmp = this.getParentData(element);
				if (tmp == null) {
					node.setParentData(parentData);
				} else {
					node.setParentData(null);
				}
			}
		} finally {
			lock.unlock();
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

		/** The object that contains underlying data of this Node. */
		private K parentData;

		/** Nodes that are connected with this one as a parent. */
		private final Set<Node<K>> children;

		/** Nodes that are connected with this one as a child. */
		private final Set<Node<K>> parents;

		/**
		 * Construct a new Node for the given element.
		 * 
		 * @param element
		 *            The element for which we need a graph Node. This element must not be null.
		 */
		public Node(K element) {
			Preconditions.checkNotNull(element);
			this.element = element;
			this.parentData = null;
			this.parents = new LinkedHashSet<Node<K>>();
			this.children = new LinkedHashSet<Node<K>>();
		}

		/**
		 * Returns all children nodes of this one.
		 * 
		 * @return All children nodes of this one.
		 */
		public Set<Node<K>> getChildren() {
			return Collections.unmodifiableSet(children);
		}

		/**
		 * Returns all parent nodes of this one.
		 * 
		 * @return All parent nodes of this one.
		 */
		public Set<Node<K>> getParents() {
			return Collections.unmodifiableSet(parents);
		}

		/**
		 * Returns all direct and indirect parents of this node.
		 * 
		 * @return All direct and indirect parents of this node.
		 */
		public Iterable<Node<K>> getAllParents() {
			return new ParentsIterable<K>(this);
		}

		/**
		 * Registers the given element as a child of this one.
		 * 
		 * @param child
		 *            The Node that is to be connected as a child of this one.
		 */
		public void connectChild(Node<K> child) {
			this.children.add(child);
			child.parents.add(this);
		}

		/**
		 * Breaks all connections of this node.
		 */
		public void breakConnections() {
			for (Node<K> parent : this.parents) {
				parent.children.remove(this);
			}
			for (Node<K> child : this.children) {
				child.parents.remove(this);
			}
			this.parents.clear();
			this.children.clear();
			this.parentData = null;
		}

		/**
		 * Returns the underlying data of this Node.
		 * 
		 * @return The underlying data of this Node.
		 */
		public K getElement() {
			return element;
		}

		/**
		 * Returns the object that contains underlying data of this Node.
		 * 
		 * @return The object that contains underlying data of this Node.
		 */
		public K getParentData() {
			return parentData;
		}

		/**
		 * Sets the object that contains underlying data of this Node.
		 * 
		 * @param parentData
		 *            the object that contains underlying data of this Node.
		 */
		public void setParentData(K parentData) {
			this.parentData = parentData;
		}
	}

	/**
	 * This can be used to create a set over an entire subgraph given a starting point within said subgraph.
	 * 
	 * @param <L>
	 *            Kind of elements the created set is meant to contain.
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class SubgraphBuilder<L> {
		/** Keeps track of the elements we've already iterated over. */
		protected final Set<L> set;

		/** Nodes that will be considered as boundaries for this subgraph. */
		protected final Set<L> endPoints;

		/** The Node that will be used as a starting point of the iteration. */
		private final Node<L> start;

		/**
		 * Constructs a new iterable given the starting point the target subgraph and the elements that will
		 * be considered as (excluded) boundaries of that subgraph.
		 * 
		 * @param start
		 *            Starting point of the iteration.
		 * @param endPoints
		 *            Excluded boundaries of the target subgraph. Iteration will be pruned on these, along
		 *            with their own subgraphs.
		 */
		public SubgraphBuilder(Node<L> start, Set<L> endPoints) {
			this.start = start;
			this.set = new LinkedHashSet<L>();
			this.set.add(start.getElement());
			this.endPoints = checkNotNull(endPoints);
		}

		/**
		 * Constructs a set over the entire subgraph containing the specified starting point.
		 * 
		 * @return The set of all nodes composing the required subgraph.
		 */
		public Set<L> buildSubgraph() {
			return ImmutableSet.copyOf(new NodeIterator(start));
		}

		/**
		 * Constructs a set over the subtree starting from the specified starting point. This will only
		 * (recursively) iterate over the children of the nodes.
		 * 
		 * @return The tree containined the specified starting point.
		 */
		public Set<L> buildTree() {
			final Iterator<L> iterator = new NodeIterator(start) {
				@Override
				protected Iterator<Node<L>> createConnectedNodesIterator(Node<L> node) {
					return node.getChildren().iterator();
				}
			};
			return ImmutableSet.copyOf(iterator);
		}

		/**
		 * A node iterator will allow us to iterate over the data of a Node and the data of all directly
		 * connected Nodes; all the while avoiding data we've already iterated over.
		 * <p>
		 * This kind of iterator does not support {@link #remove() removal}.
		 * </p>
		 * <p>
		 * Note : this is not a static class as we're building the external SubgraphBuilder's set through this
		 * (and checking for already iterated over elements).
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
				this.nodesIterator = createConnectedNodesIterator(node);
				prepareNextIterator();
			}

			/**
			 * Creates the iterator over the nodes connected to the current. By default, this will iterate
			 * over all parents, then all children.
			 * 
			 * @param node
			 *            the starting node for this iteration.
			 * @return Iterator over the connected nodes we are to iterate over.
			 */
			protected Iterator<Node<L>> createConnectedNodesIterator(Node<L> node) {
				return concat(node.getParents().iterator(), node.getChildren().iterator());
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see java.util.Iterator#hasNext()
			 */
			public boolean hasNext() {
				return next != null || nextNodeIterator.hasNext();
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
							&& !SubgraphBuilder.this.endPoints.contains(nextNode.getElement())
							&& nodesIterator.hasNext()) {
						nextNode = nodesIterator.next();
					}
					if (!SubgraphBuilder.this.endPoints.contains(nextNode.getElement())
							&& SubgraphBuilder.this.set.add(nextNode.getElement())) {
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

	/**
	 * A custom Iterable that will iterate over the Node->parent Node tree of a given Node.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class ParentsIterable<O> implements Iterable<Node<O>> {
		/** The leaf of the Node->parent tree for which this iterable has been constructed. */
		private final Node<O> start;

		/**
		 * Constructs an iterable given the leaf of its tree.
		 * 
		 * @param start
		 *            Leaf node of the tree we'll iterate over.
		 */
		public ParentsIterable(Node<O> start) {
			this.start = start;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Iterable#iterator()
		 */
		public Iterator<Node<O>> iterator() {
			return new ParentsIterator<O>(start);
		}
	}

	/**
	 * A custom TreeIterator that will iterate over the Node->parent Node tree.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class ParentsIterator<P> extends AbstractTreeIterator<Node<P>> {
		/** Generated SUID. */
		private static final long serialVersionUID = -4476850344598138970L;

		/**
		 * Construct an iterator given the root (well, "leaf") of its tree.
		 * 
		 * @param start
		 *            Start node of the tree we'll iterate over.
		 */
		public ParentsIterator(Node<P> start) {
			super(start, false);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.util.AbstractTreeIterator#getChildren(java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected Iterator<? extends Node<P>> getChildren(Object obj) {
			return ((Node<P>)obj).getParents().iterator();
		}
	}

	/**
	 * A custom iterator implementing a breadth-first iteration algorithm over the underlying graph. This will
	 * start from the graph's {@link Graph#roots roots} and carry the iteration downward to each individual
	 * node, never iterating twice on the same node, and never iterating over a node which parents have yet to
	 * be iterated over.
	 * <p>
	 * <b>Note</b> that this iterator doesn't allow clients to structurally change the graph after its
	 * creation. Any attempt at doing so will make the next call to {@link #next()} fail in
	 * ConcurrentModificationException.
	 * </p>
	 * <p>
	 * This iterator does not support removal of values from its underlying graph.
	 * </p>
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class BreadthFirstIterator implements PruningIterator<E> {
		/**
		 * The current depth-level iterator (first, an iterator over all roots, then, an iterator over all of
		 * these roots' direct children, and so on). See also {@link #nextIterable}.
		 */
		private Iterator<Node<E>> currentIterator;

		/**
		 * The set of all children found for the current depth level. This will be used in conjunction with
		 * {@link #consumedNodes} in order to determine the iterator we'll use when {@link #currentIterator}
		 * is exhausted.
		 */
		private Set<Node<E>> nextIterable;

		/**
		 * The set of all nodes from the underlying graph we've already iterated over. This will prevent us
		 * from cycling over nodes if there are cycles in our graph, and will also prevent us from iterating
		 * twice over the same node (when a node has multiple parents).
		 */
		private Set<Node<E>> consumedNodes;

		/**
		 * The node we've last returned from a call to {@link #next()}. This will be <code>null</code>ed out
		 * by a call to {@link #prune()}.
		 */
		private Node<E> lastReturned;

		/**
		 * The node we'll next return from a call to {@link #next()}. <code>null</code> when we've reached the
		 * end of our iteration.
		 */
		private Node<E> next;

		/**
		 * The expected {@link Graph#modcount} for this iterator. Any attempt at calling {@link #next()} after
		 * the underlying graph has been modified will fail in {@link ConcurrentModificationException}.
		 */
		private int expectedModCount;

		/** Default constructor. */
		public BreadthFirstIterator() {
			this.currentIterator = Graph.this.roots.iterator();
			this.nextIterable = new LinkedHashSet<Node<E>>();
			this.consumedNodes = new LinkedHashSet<Node<E>>();
			this.expectedModCount = Graph.this.modcount;
			prepareNext();
		}

		/** {@inheritDoc} */
		public boolean hasNext() {
			return next != null || currentIterator.hasNext() || !consumedNodes.containsAll(nextIterable);
		}

		/** {@inheritDoc} */
		public E next() {
			if (Graph.this.modcount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (next == null) {
				throw new NoSuchElementException();
			}
			final E result = next.getElement();
			consumedNodes.add(next);
			lastReturned = next;
			prepareNext();
			return result;
		}

		/** {@inheritDoc} */
		public void prune() {
			// do not accept successive pruning calls, and don't fail when trying to prune before even
			// starting the iteration.
			if (lastReturned == null) {
				return;
			}
			// the current has already been added to the "consumed" set.
			// All direct and indirect children of this path must be pruned as well.
			// TODO any effective way of achieving this without iterating?
			Set<Node<E>> children = lastReturned.getChildren();
			lastReturned = null;
			while (!children.isEmpty()) {
				final Set<Node<E>> copy = children;
				children = new LinkedHashSet<Node<E>>();
				for (Node<E> child : copy) {
					if (consumedNodes.add(child)) {
						children.addAll(child.getChildren());
					}
				}
			}
		}

		/**
		 * Unsupported.
		 * 
		 * @throws UnsupportedOperationException
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/** This will prepare this iterator for a subsequent call to {@link #next()}. */
		private void prepareNext() {
			if (!currentIterator.hasNext()) {
				prepareNextIterator();
			}
			if (currentIterator.hasNext()) {
				next = currentIterator.next();
				nextIterable.addAll(next.getChildren());
			} else {
				next = null;
			}
		}

		/**
		 * Whenever we've consumed all of the current depth's nodes, this will create a new iterator for the
		 * next depth level out of {@link #nextIterable} and {@link #consumedNodes}.
		 */
		private void prepareNextIterator() {
			final Set<Node<E>> difference = new LinkedHashSet<Node<E>>();
			for (Node<E> node : nextIterable) {
				if (!consumedNodes.contains(node)) {
					difference.add(node);
				}
			}
			currentIterator = Iterators.filter(difference.iterator(), new Predicate<Node<E>>() {
				public boolean apply(Node<E> input) {
					return consumedNodes.containsAll(input.getParents());
				}
			});
			nextIterable.clear();
		}
	}
}
