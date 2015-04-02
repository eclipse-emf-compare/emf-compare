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
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DependencyFoundEvent;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResolvedEvent;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceDependencyFoundEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class ResolutionEventsTest {

	@Test
	public void testResolvedEvent() {
		String node = "a";
		assertEquals(node, new ResolvedEvent<String>(node).getNode());
	}

	@Test
	public void testDependencyFoundEvent() {
		String from = "from";
		String to = "to";
		String parent = "parent";
		DependencyFoundEvent<String> evt;

		evt = new DependencyFoundEvent<String>(from, to);
		assertEquals(from, evt.getFrom());
		assertEquals(to, evt.getTo());
		assertFalse(evt.hasParent());
		assertNull(evt.getParent().orNull());

		evt = new DependencyFoundEvent<String>(from, to, Optional.of(parent));
		assertEquals(from, evt.getFrom());
		assertEquals(to, evt.getTo());
		assertTrue(evt.hasParent());
		assertEquals(parent, evt.getParent().orNull());
	}

	@Test
	public void testResourceDependencyFoundEvent() {
		URI from = URI.createURI("from");
		URI to = URI.createURI("to");
		EObject parent = EcorePackage.eINSTANCE.getEClass();
		EStructuralFeature attribute = EcorePackage.eINSTANCE.getENamedElement_Name();
		EStructuralFeature crossReference = EcorePackage.eINSTANCE.getEClass_ESuperTypes();
		EStructuralFeature containmentReference = EcorePackage.eINSTANCE.getEClass_EStructuralFeatures();

		ResourceDependencyFoundEvent evt;

		evt = new ResourceDependencyFoundEvent(from, to, parent, attribute);
		assertEquals(from, evt.getFrom());
		assertEquals(to, evt.getTo());
		assertFalse(evt.hasParent());
		assertNull(evt.getParent().orNull());

		evt = new ResourceDependencyFoundEvent(from, to, parent, crossReference);
		assertEquals(from, evt.getFrom());
		assertEquals(to, evt.getTo());
		assertFalse(evt.hasParent());
		assertNull(evt.getParent().orNull());

		evt = new ResourceDependencyFoundEvent(from, to, parent, containmentReference);
		assertEquals(from, evt.getFrom());
		assertEquals(to, evt.getTo());
		assertTrue(evt.hasParent());
		assertEquals(ResourceDependencyFoundEvent.getUri(parent), evt.getParent().orNull());
	}

}
