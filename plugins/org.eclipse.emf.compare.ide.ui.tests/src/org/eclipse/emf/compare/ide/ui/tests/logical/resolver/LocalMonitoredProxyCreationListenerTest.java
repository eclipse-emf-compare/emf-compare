/*******************************************************************************
 * Copyright (c) 2015, 2017 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial test implementation
 *     Philip Langer - bug 516500
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.LocalMonitoredProxyCreationListener;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceDependencyFoundEvent;
import org.junit.Test;

@SuppressWarnings("nls")
public class LocalMonitoredProxyCreationListenerTest extends AbstractMonitoredProxyCreationListenerTest {

	LocalMonitoredProxyCreationListener sut;

	@Test
	public void testCorrectProxy() {
		prepareTest("platform:/resource/to", CrossReferenceResolutionScope.WORKSPACE);
		sut = new LocalMonitoredProxyCreationListener(monitor, eventBus, localResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verify(eventBus).post(new ResourceDependencyFoundEvent(from, to, eObject, feature));
		verify(localResolver).demandResolve(synchronizedResourceSet, to, diagnostic, monitor);
	}

	@Test
	public void testNonPlatformResource() {
		prepareTest("nonplatform:/resource/to", CrossReferenceResolutionScope.WORKSPACE);
		sut = new LocalMonitoredProxyCreationListener(monitor, eventBus, localResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verifyZeroInteractions(eventBus, localResolver);
	}

	@Test
	public void testNonAbsolutePlatformResource() {
		prepareTest("platform:relative/file", CrossReferenceResolutionScope.WORKSPACE);
		sut = new LocalMonitoredProxyCreationListener(monitor, eventBus, localResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verifyZeroInteractions(eventBus, localResolver);
	}

	@Test
	public void testWrongResolutionScope() {
		prepareTest("platform:/resource/to", CrossReferenceResolutionScope.SELF);
		sut = new LocalMonitoredProxyCreationListener(monitor, eventBus, localResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verifyZeroInteractions(eventBus, localResolver);
	}
}
