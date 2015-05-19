/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Munich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *     Michael Borkowski - rewrite using Mockito
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import java.util.Collections;

import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DependencyFoundEvent;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DependencyGraphUpdater;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResolvedEvent;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceRemovedEvent;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({"nls", "restriction", "cast", "unchecked" })
public class DependencyGraphUpdaterTest {

	Graph<String> graph;

	DependencyGraphUpdater<String> sut;

	@Before
	public void setUp() {
		graph = (Graph<String>)mock(Graph.class);
		sut = new DependencyGraphUpdater<String>(graph, new EventBus());
	}

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

	@Test
	public void testInstantiationDoesNotModifyGraph() {
		verifyZeroInteractions(graph);
	}

	@Test
	public void testRecordNodeAddsNodeToGraph() {
		ResolvedEvent<String> event1, event2;

		event1 = new ResolvedEvent<String>("1");
		sut.recordNode(event1);

		event2 = new ResolvedEvent<String>("2");
		sut.recordNode(event2);

		verify(graph).add(event1.getNode());
		verify(graph).add(event2.getNode());
		verifyNoMoreInteractions(graph);
	}

	@Test
	public void testRecordEdgeWithoutParent() {
		DependencyFoundEvent<String> event1, event2;

		event1 = new DependencyFoundEvent<String>("from1", "to1");
		sut.recordEdge(event1);

		event2 = new DependencyFoundEvent<String>("from2", "to2");
		sut.recordEdge(event2);

		verify(graph).addChildren(event1.getFrom(), Collections.singleton(event1.getTo()));
		verify(graph).addChildren(event2.getFrom(), Collections.singleton(event2.getTo()));
		verifyNoMoreInteractions(graph);
	}

	@Test
	public void testRecordEdgeWithParent() {
		DependencyFoundEvent<String> event1, event2;

		event1 = new DependencyFoundEvent<String>("from1", "to1", Optional.of("parent"));
		sut.recordEdge(event1);

		verify(graph).addChildren(event1.getFrom(), Collections.singleton(event1.getTo()));
		verify(graph).addParentData(event1.getTo(), event1.getParent().get());

		event2 = new DependencyFoundEvent<String>("from2", "to2", Optional.of("parent2"));
		sut.recordEdge(event2);

		verify(graph).addChildren(event2.getFrom(), Collections.singleton(event2.getTo()));
		verify(graph).addParentData(event2.getTo(), event2.getParent().get());
	}

	@Test
	public void testRemoval() {
		ResourceRemovedEvent<String> event;

		sut.recordNode(new ResolvedEvent<String>("a"));
		sut.recordNode(new ResolvedEvent<String>("b"));
		sut.recordNode(new ResolvedEvent<String>("c"));
		sut.recordNode(new ResolvedEvent<String>("d"));
		event = new ResourceRemovedEvent<String>(Sets.newHashSet("a", "b", "c"));
		sut.recordRemoval(event);

		verify(graph, times(4)).add(anyString());
		verify(graph).removeAll(event.getElements());
		verifyNoMoreInteractions(graph);
	}
}
