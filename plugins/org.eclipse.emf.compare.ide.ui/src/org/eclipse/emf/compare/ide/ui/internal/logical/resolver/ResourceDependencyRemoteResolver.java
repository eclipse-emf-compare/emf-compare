/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - extract interface
 *     Martin Fleck - bug 512677
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import org.apache.log4j.Logger;
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

	/** The resolution context. */
	private final IResolutionContext context;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            The context, must not be {@code null}
	 */
	public ResourceDependencyRemoteResolver(IResolutionContext context) {
		this.context = context;
	}

	public void demandRemoteResolve(final SynchronizedResourceSet resourceSet, final URI uri,
			final DiagnosticSupport diagnostic, final ThreadSafeProgressMonitor tspm) {
		if (ResolutionUtil.isInterruptedOrCanceled(tspm)) {
			context.getScheduler().demandShutdown();
			return;
		}
		if (context.getScheduler().isScheduled(uri)) {
			return;
		}
		context.getScheduler()
				.scheduleComputation(getRemoteResolveComputation(resourceSet, uri, diagnostic, tspm));
	}

	public RemoteResolveComputation getRemoteResolveComputation(final SynchronizedResourceSet resourceSet,
			final URI uri, final DiagnosticSupport diagnostic, final ThreadSafeProgressMonitor tspm) {
		return new RemoteResolveComputation(context, diagnostic, resourceSet, uri,
				new MonitorCallback(diagnostic, tspm), tspm);
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
