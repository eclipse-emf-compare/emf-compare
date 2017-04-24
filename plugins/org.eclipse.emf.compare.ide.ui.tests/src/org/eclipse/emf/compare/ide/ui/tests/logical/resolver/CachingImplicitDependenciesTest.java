/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CachingImplicitDependencies;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.IImplicitDependencies;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests the {@link CachingImplicitDependencies} class.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class CachingImplicitDependenciesTest {

	/**
	 * Tests that the caching is done correctly and the underlying delegate is only called once for each
	 * dependency calculation.
	 */
	@SuppressWarnings("nls")
	@Test
	public void testCaching() {
		// create URIs and dependencies
		final URI uri1 = URI.createURI("NoProject/File1.file");
		final Set<URI> uri1dependencies = Sets.newHashSet(URI.createURI("NoProject/File1_Dependency1.file"),
				URI.createURI("NoProject/File1_Dependency2.file"),
				URI.createURI("NoProject/File1_Dependency3.file"));

		final URI uri2 = URI.createURI("NoProject/File2.file");
		final Set<URI> uri2dependencies = Sets.newHashSet(URI.createURI("NoProject/File2_Dependency1.file"),
				URI.createURI("NoProject/File2_Dependency2.file"));

		// mock calculation
		IImplicitDependencies delegate = mock(IImplicitDependencies.class);
		when(delegate.of(any(URI.class), any(URIConverter.class))).then(new Answer<Set<URI>>() {
			public Set<URI> answer(InvocationOnMock invocation) throws Throwable {
				Object uriArgument = invocation.getArguments()[0];
				if (uriArgument.equals(uri1)) {
					return Sets.newHashSet(uri1dependencies);
				} else if (uriArgument.equals(uri2)) {
					return Sets.newHashSet(uri2dependencies);
				}
				throw new IllegalArgumentException("This cannot happen.");
			}
		});

		CachingImplicitDependencies cachingDependencies = new CachingImplicitDependencies(delegate);

		// query dependencies
		assertEquals(uri1dependencies, cachingDependencies.of(uri1, null));
		assertEquals(uri2dependencies, cachingDependencies.of(uri2, null));

		// delegate was only called once for each uri
		verify(delegate, times(1)).of(eq(uri1), any(URIConverter.class));
		verify(delegate, times(1)).of(eq(uri2), any(URIConverter.class));

		// query dependencies multiple times
		assertEquals(uri1dependencies, cachingDependencies.of(uri1, null));
		assertEquals(uri2dependencies, cachingDependencies.of(uri2, null));
		assertEquals(uri1dependencies, cachingDependencies.of(uri1, null));
		assertEquals(uri2dependencies, cachingDependencies.of(uri2, null));
		assertEquals(uri1dependencies, cachingDependencies.of(uri1, null));
		assertEquals(uri2dependencies, cachingDependencies.of(uri2, null));
		assertEquals(uri1dependencies, cachingDependencies.of(uri1, null));
		assertEquals(uri2dependencies, cachingDependencies.of(uri2, null));

		// delegate was only called once for each uri, cache used
		verify(delegate, times(1)).of(eq(uri1), any(URIConverter.class));
		verify(delegate, times(1)).of(eq(uri2), any(URIConverter.class));
	}
}
