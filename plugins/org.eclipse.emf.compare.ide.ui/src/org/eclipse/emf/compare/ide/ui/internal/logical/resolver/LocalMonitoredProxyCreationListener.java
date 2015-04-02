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
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.eventbus.EventBus;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Implementation of {@link IProxyCreationListener} for local resolutions.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
class LocalMonitoredProxyCreationListener extends AbstractMonitoredProxyCreationListener {

	/** The event bus to use to notify interesting events. */
	protected final EventBus eventBus;

	/** The local resolver. */
	protected final IResourceDependencyLocalResolver localResolver;

	/**
	 * Constructor.
	 * 
	 * @param monitor
	 *            The progress monitor to use
	 * @param eventBus
	 *            The event bus
	 * @param dependencyProvider
	 *            The dependency provider
	 * @param diagnostic
	 *            The diagnostic
	 */
	public LocalMonitoredProxyCreationListener(ThreadSafeProgressMonitor monitor, EventBus eventBus,
			IResourceDependencyLocalResolver localResolver, DiagnosticSupport diagnostic) {
		super(monitor, diagnostic);
		this.localResolver = localResolver;
		this.eventBus = eventBus;
	}

	/**
	 * {@inheritDoc}
	 */
	public void proxyCreated(Resource source, EObject eObject, EStructuralFeature feature, EObject proxy,
			int position) {
		final URI from = source.getURI();
		final URI to = ((InternalEObject)proxy).eProxyURI().trimFragment();
		// TODO Does this work with relative URIs? (isPlatformResource())
		if (ResolutionUtil.getResolutionScope() != CrossReferenceResolutionScope.SELF
				&& to.isPlatformResource()) {
			SynchronizedResourceSet resourceSet = (SynchronizedResourceSet)source.getResourceSet();
			eventBus.post(new ResourceDependencyFoundEvent(from, to, eObject, feature));
			localResolver.demandResolve(resourceSet, to, diagnostic, tspm);
		}
	}
}
