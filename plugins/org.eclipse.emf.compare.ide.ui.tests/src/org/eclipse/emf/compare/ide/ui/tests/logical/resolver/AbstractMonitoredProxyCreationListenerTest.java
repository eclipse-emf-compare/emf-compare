/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial test implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.DiagnosticSupport;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.IResourceDependencyLocalResolver;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.IResourceDependencyRemoteResolver;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.SynchronizedResourceSet;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings({"restriction", "nls" })
public class AbstractMonitoredProxyCreationListenerTest {

	EventBus eventBus;

	SynchronizedResourceSet synchronizedResourceSet;

	IResourceDependencyLocalResolver localResolver;

	IResourceDependencyRemoteResolver remoteResolver;

	DiagnosticSupport diagnostic;

	ThreadSafeProgressMonitor monitor;

	URI from, to;

	EObject eObject;

	EStructuralFeature feature;

	Resource source;

	InternalEObject proxy;

	public void preSetUp() {
		monitor = mock(ThreadSafeProgressMonitor.class);
		eventBus = mock(EventBus.class);
		localResolver = mock(IResourceDependencyLocalResolver.class);
		remoteResolver = mock(IResourceDependencyRemoteResolver.class);
		diagnostic = mock(DiagnosticSupport.class);

		synchronizedResourceSet = mock(SynchronizedResourceSet.class);
	}

	public void prepareTest(String toUri, CrossReferenceResolutionScope scope) {
		source = mock(Resource.class);
		eObject = mock(EObject.class);
		feature = mock(EStructuralFeature.class);
		proxy = mock(InternalEObject.class);
		from = URI.createURI("platform:/resource/from");
		when(source.getURI()).thenReturn(from);

		when(source.getResourceSet()).thenReturn(synchronizedResourceSet);

		to = URI.createURI(toUri);
		when(proxy.eProxyURI()).thenReturn(to);

		setResolutionScopeTo(scope);
	}

	private void setResolutionScopeTo(CrossReferenceResolutionScope scope) {
		EMFCompareIDEUIPlugin.getDefault().getPreferenceStore().setValue(
				EMFCompareUIPreferences.DISABLE_RESOLVERS_PREFERENCE, false);
		EMFCompareIDEUIPlugin.getDefault().getPreferenceStore().setValue(
				EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE, scope.name());
	}
}
