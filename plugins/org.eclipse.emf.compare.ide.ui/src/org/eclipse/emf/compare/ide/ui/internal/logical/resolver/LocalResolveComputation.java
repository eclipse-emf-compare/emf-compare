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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Implements a runnable that will load the EMF resource pointed at by a given URI, then resolve all of its
 * cross-referenced resources and update the dependency graph accordingly.
 * <p>
 * Once done with the resolution, this thread will spawn an independent job to unload the resource.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
class LocalResolveComputation extends AbstractResourceResolver implements IComputation<URI> {

	/** The event bus. */
	protected final EventBus eventBus;

	/** The post-treatment to run after completion (whether or not successful). */
	private FutureCallback<Object> postTreatment;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            The context of this resolution.
	 * @param diagnostic
	 *            The diagnostic
	 * @param resourceSet
	 *            The resource set
	 * @param uri
	 *            The URI
	 * @param postTreatment
	 *            The post-treatment
	 * @param monitor
	 *            The progress monitor
	 */
	public LocalResolveComputation(IResolutionContext context, DiagnosticSupport diagnostic,
			SynchronizedResourceSet resourceSet, URI uri, FutureCallback<Object> postTreatment,
			ThreadSafeProgressMonitor monitor) {
		super(context, diagnostic, resourceSet, uri, monitor);
		this.eventBus = checkNotNull(context.getEventBus());
		this.postTreatment = postTreatment;
	}

	/** {@inheritDoc} */
	public void run() {
		if (ResolutionUtil.isInterruptedOrCanceled(tspm)) {
			context.getScheduler().demandShutdown();
			return;
		}

		for (URI currentUri : context.getImplicitDependencies().of(uri, resourceSet.getURIConverter())) {
			LocalResolveComputation computation = new LocalResolveComputation(context, diagnostic,
					resourceSet, currentUri, new MonitorCallback(diagnostic, tspm), tspm);
			context.getScheduler().scheduleComputation(computation);
		}

		final Resource resource = resourceSet.loadResource(uri);
		Diagnostic resourceDiagnostic = EcoreUtil.computeDiagnostic(resource, true);
		if (resourceDiagnostic.getSeverity() >= Diagnostic.WARNING) {
			diagnostic.merge(resourceDiagnostic);
		}
		eventBus.post(new ResolvedEvent<URI>(uri));
		demandUnload(resource);
	}

	public URI getKey() {
		return uri;
	}

	public FutureCallback<Object> getPostTreatment() {
		return postTreatment;
	}
}
