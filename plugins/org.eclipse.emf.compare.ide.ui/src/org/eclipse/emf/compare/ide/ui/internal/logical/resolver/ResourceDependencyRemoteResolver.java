/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - extract interface
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.util.concurrent.FutureCallback;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;

/**
 * The default implementation of the {@link IResourceDependencyRemoteResolver}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ResourceDependencyRemoteResolver implements IResourceDependencyRemoteResolver {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(ResourceDependencyLocalResolver.class);

	/** The scheduler. */
	protected final ResourceComputationScheduler<URI> scheduler;

	/** The implicit dependencies */
	private final IImplicitDependencies implicitDependencies;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            The context, must not be {@code null}
	 */
	public ResourceDependencyRemoteResolver(IResolutionContext context) {
		this.implicitDependencies = context.getImplicitDependencies();
		this.scheduler = context.getScheduler();
	}

	public void demandRemoteResolve(final SynchronizedResourceSet resourceSet, final URI uri,
			final DiagnosticSupport diagnostic, final ThreadSafeProgressMonitor tspm) {
		if (ResolutionUtil.isInterruptedOrCanceled(tspm)) {
			scheduler.demandShutdown();
			return;
		}
		for (URI currentUri : implicitDependencies.of(uri, resourceSet.getURIConverter())) {
			scheduler.scheduleComputation(getRemoteResolveComputation(resourceSet, currentUri, diagnostic,
					tspm));
		}
	}

	public RemoteResolveComputation getRemoteResolveComputation(final SynchronizedResourceSet resourceSet,
			final URI uri, final DiagnosticSupport diagnostic, final ThreadSafeProgressMonitor tspm) {
		return new RemoteResolveComputation(scheduler, diagnostic, resourceSet, uri,
				new FutureCallback<Object>() {
					public void onSuccess(Object o) {
						if (!ResolutionUtil.isInterruptedOrCanceled(tspm)) {
							// do not report progress anymore when the task has been interrupted of canceled.
							// It speeds up the cancellation.
							tspm.worked(1);
						}
					}

					public void onFailure(Throwable t) {
						if (!ResolutionUtil.isInterruptedOrCanceled(tspm)) {
							// do not report progress or errors anymore when the task has been interrupted of
							// canceled. It speeds up the cancellation.
							tspm.worked(1);
							diagnostic.merge(BasicDiagnostic.toDiagnostic(t));
						}
					}
				}, tspm);
	}

	public SynchronizedResourceSet getResourceSetForRemoteResolution(DiagnosticSupport diagnostic,
			ThreadSafeProgressMonitor tspm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("INSTANTIATING SynchronizedResourceSet for remote resolution."); //$NON-NLS-1$
		}
		final SynchronizedResourceSet resourceSet = new SynchronizedResourceSet(
				new RemoteMonitoredProxyCreationListener(tspm, this, diagnostic));
		return resourceSet;
	}
}
