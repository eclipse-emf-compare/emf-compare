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
import static org.mockito.Mockito.verifyNoInteractions;

import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.RemoteMonitoredProxyCreationListener;
import org.junit.Test;

@SuppressWarnings("nls")
public class RemoteMonitoredProxyCreationListenerTest extends AbstractMonitoredProxyCreationListenerTest {

	RemoteMonitoredProxyCreationListener sut;

	@Test
	public void testCorrectProxy() {
		prepareTest("platform:/resource/to", CrossReferenceResolutionScope.WORKSPACE);
		sut = new RemoteMonitoredProxyCreationListener(monitor, remoteResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verify(remoteResolver).demandRemoteResolve(synchronizedResourceSet, to, diagnostic, monitor);
	}

	@Test
	public void testNonPlatformResource() {
		prepareTest("nonplatform:/resource/to", CrossReferenceResolutionScope.WORKSPACE);
		sut = new RemoteMonitoredProxyCreationListener(monitor, remoteResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verifyNoInteractions(localResolver);
	}

	@Test
	public void testNonAbsolutePlatformResource() {
		prepareTest("platform:relative/file", CrossReferenceResolutionScope.WORKSPACE);
		sut = new RemoteMonitoredProxyCreationListener(monitor, remoteResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verifyNoInteractions(localResolver);
	}

	@Test
	public void testWrongResolutionScope() {
		prepareTest("platform:/resource/to", CrossReferenceResolutionScope.SELF);
		sut = new RemoteMonitoredProxyCreationListener(monitor, remoteResolver, diagnostic);
		sut.proxyCreated(source, eObject, feature, proxy, 3);

		verifyNoInteractions(localResolver);
	}
}
