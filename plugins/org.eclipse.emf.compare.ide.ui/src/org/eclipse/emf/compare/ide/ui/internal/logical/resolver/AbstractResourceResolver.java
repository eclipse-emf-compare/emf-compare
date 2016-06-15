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

import com.google.common.util.concurrent.FutureCallback;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Implements a runnable that will load the EMF resource pointed at by a given URI, then resolve all of its
 * cross-referenced resources and update the dependency graph accordingly.
 * <p>
 * Once done with the resolution, this thread will spawn an independent job to unload the resource.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
abstract class AbstractResourceResolver implements Runnable {
	/** The scheduler. */
	protected final ResourceComputationScheduler<URI> scheduler;

	/** The resource set in which to load the resource. */
	protected final SynchronizedResourceSet resourceSet;

	/** URI that needs to be loaded as an EMF model. */
	protected final URI uri;

	/** Monitor on which to report progress to the user. */
	protected final ThreadSafeProgressMonitor tspm;

	/** The diagnostic. */
	protected final DiagnosticSupport diagnostic;

	/**
	 * Default constructor.
	 * 
	 * @param scheduler
	 *            The scheduler to use.
	 * @param diagnostic
	 *            The diagnostic to use.
	 * @param resourceSet
	 *            The resource set in which to load the resource.
	 * @param uri
	 *            URI that needs to be loaded as an EMF model.
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	public AbstractResourceResolver(ResourceComputationScheduler<URI> scheduler, DiagnosticSupport diagnostic,
			SynchronizedResourceSet resourceSet, URI uri, ThreadSafeProgressMonitor monitor) {
		this.scheduler = scheduler;
		this.diagnostic = diagnostic;
		this.resourceSet = resourceSet;
		this.uri = uri;
		this.tspm = monitor;
	}

	/**
	 * Allows callers to launch the unloading of the given resource.
	 * <p>
	 * Do note that even though this is called "unload", we won't actually call {@link Resource#unload()} on
	 * the given resource unless we deem it necessary (we only call if for UML because of the CacheAdapter)
	 * for now. This will only remove the resource from its containing resource set so as to allow it to be
	 * garbage collected.
	 * </p>
	 * 
	 * @param resource
	 *            The resource to unload.
	 * @see ResourceUnloader
	 */
	protected void demandUnload(Resource resource) {
		// Regardless of the amount of progress reported so far, use 0.1% of the space remaining in the
		// monitor to process the next node.
		tspm.setWorkRemaining(1000);
		scheduler.scheduleUnload(new ResourceUnloader(resourceSet, resource, tspm),
				new FutureCallback<Object>() {
					public void onSuccess(Object result) {
						if (!ResolutionUtil.isInterruptedOrCanceled(tspm)) {
							tspm.worked(1);
						}
					}

					public void onFailure(Throwable t) {
						if (!ResolutionUtil.isInterruptedOrCanceled(tspm)) {
							tspm.worked(1);
							diagnostic.merge(BasicDiagnostic.toDiagnostic(t));
						}
					}
				});
	}
}
