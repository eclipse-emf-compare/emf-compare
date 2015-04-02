/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DependencyFoundEvent;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DependencyGraphUpdater;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResolvedEvent;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.junit.Test;

@SuppressWarnings({"nls", "restriction" })
public class DependencyGraphUpdaterTest {

	@Test(expected = NullPointerException.class)
	public void testInstantiationWithNullGraph() {
		new DependencyGraphUpdater<String>(null, new EventBus());
	}

	@Test(expected = NullPointerException.class)
	public void testInstantiationWithNullEventBus() {
		new DependencyGraphUpdater<String>(new Graph<String>(), null);
	}

	@Test
	public void testInstantiationWithoutException() {
		new DependencyGraphUpdater<String>(new Graph<String>(), new EventBus());
	}

	public void testInstantiationDoesNotModifyGraph() {
		MockStringGraph graph = new MockStringGraph();
		new DependencyGraphUpdater<String>(graph, new EventBus());
		assertTrue(graph.addedElements.isEmpty());
		assertTrue(graph.addedChildRelationships.isEmpty());
		assertTrue(graph.addedParentRelationships.isEmpty());
		assertFalse(graph.didRemove);
	}

	@Test
	public void testRecordNodeAddsNodeToGraph() {
		MockStringGraph graph = new MockStringGraph();
		DependencyGraphUpdater<String> updater = new DependencyGraphUpdater<String>(graph, new EventBus());

		ResolvedEvent<String> resolvedEvent = new ResolvedEvent<String>("1");
		updater.recordNode(resolvedEvent);
		assertEquals(1, graph.addedElements.size());
		assertEquals("1", graph.addedElements.get(0));

		resolvedEvent = new ResolvedEvent<String>("2");
		updater.recordNode(resolvedEvent);
		assertEquals(2, graph.addedElements.size());
		assertEquals("2", graph.addedElements.get(1));
	}

	@Test
	public void testRecordEdgeWithoutParent() {
		MockStringGraph graph = new MockStringGraph();
		DependencyGraphUpdater<String> updater = new DependencyGraphUpdater<String>(graph, new EventBus());

		DependencyFoundEvent<String> event;

		event = new DependencyFoundEvent<String>("from", "to");
		updater.recordEdge(event);
		assertAddedChildrenAtIndex(graph, "from", "to", 0);

		event = new DependencyFoundEvent<String>("from2", "to2");
		updater.recordEdge(event);
		assertAddedChildrenAtIndex(graph, "from2", "to2", 1);
	}

	private void assertAddedChildrenAtIndex(MockStringGraph graph, String expectedFrom, String expectedTo,
			int index) {
		assertEquals(index + 1, graph.addedChildRelationships.size());
		assertEquals(expectedFrom, graph.addedChildRelationships.get(index).element);
		assertEquals(1, graph.addedChildRelationships.get(index).children.size());
		assertEquals(expectedTo, graph.addedChildRelationships.get(index).children.iterator().next());
	}

	@Test
	public void testRecordEdgeWithParent() {
		MockStringGraph graph = new MockStringGraph();
		DependencyGraphUpdater<String> updater = new DependencyGraphUpdater<String>(graph, new EventBus());

		DependencyFoundEvent<String> event;

		event = new DependencyFoundEvent<String>("from", "to", Optional.of("parent"));
		updater.recordEdge(event);
		assertAddedChildrenAtIndex(graph, "from", "to", 0);
		assertAddedParentAtIndex(graph, "to", "parent", 0);

		event = new DependencyFoundEvent<String>("from2", "to2", Optional.of("parent2"));
		updater.recordEdge(event);
		assertAddedChildrenAtIndex(graph, "from2", "to2", 1);
		assertAddedParentAtIndex(graph, "to2", "parent2", 1);
	}

	private void assertAddedParentAtIndex(MockStringGraph graph, String to, String parent, int index) {
		assertEquals(index + 1, graph.addedParentRelationships.size());
		assertEquals(to, graph.addedParentRelationships.get(index).element);
		assertEquals(parent, graph.addedParentRelationships.get(index).parent);
	}

	private class MockStringGraph extends Graph<String> {

		List<String> addedElements = new LinkedList<String>();

		List<ChildRelationShip> addedChildRelationships = new LinkedList<ChildRelationShip>();

		List<ParentRelationShip> addedParentRelationships = new LinkedList<ParentRelationShip>();

		boolean didRemove = false;

		@Override
		public boolean add(String element) {
			addedElements.add(element);
			return super.add(element);
		}

		@Override
		public void addChildren(String element, Set<String> newChildren) {
			addedChildRelationships.add(new ChildRelationShip(element, newChildren));
			super.addChildren(element, newChildren);
		}

		@Override
		public void addParentData(String element, String parentData) {
			addedParentRelationships.add(new ParentRelationShip(element, parentData));
			super.addParentData(element, parentData);
		}

		@Override
		public void remove(String element) {
			didRemove = true;
			super.remove(element);
		}

		@Override
		public void removeAll(Collection<String> elements) {
			didRemove = true;
			super.removeAll(elements);
		}

	}

	private class ChildRelationShip {
		String element;

		Set<String> children;

		protected ChildRelationShip(String element, Set<String> children) {
			this.element = element;
			this.children = children;
		}
	}

	private class ParentRelationShip {
		String element;

		String parent;

		protected ParentRelationShip(String element, String parent) {
			this.element = element;
			this.parent = parent;
		}
	}

}
