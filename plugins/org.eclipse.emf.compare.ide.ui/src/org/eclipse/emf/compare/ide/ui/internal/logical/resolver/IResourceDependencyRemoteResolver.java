/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - extract this interface from implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;

/**
 * A resource dependency provider is in charge of providing the dependencies of resources (local or remote
 * resources, remote meaning hosted in git for instance).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IResourceDependencyRemoteResolver {

	/**
	 * Allows callers to launch the loading and resolution of the model pointed at by the given URI, without
	 * updating the {@link #dependencyGraph} along the way.
	 * <p>
	 * This will check whether the given storage isn't already being resolved, then submit a job to the
	 * {@link #resolvingPool} to load and resolve the model in a separate thread.
	 * </p>
	 * 
	 * @param resourceSet
	 *            The resource set in which to load the resource.
	 * @param uri
	 *            The uri we are to try and load as a model.
	 * @param diagnostic
	 *            The diagnostic
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 */
	void demandRemoteResolve(SynchronizedResourceSet resourceSet, URI uri, DiagnosticSupport diagnostic,
			ThreadSafeProgressMonitor tspm);

	/**
	 * Provides the computation to run for remote resolutions.
	 * 
	 * @param resourceSet
	 *            The resource set
	 * @param uri
	 *            The URI
	 * @param diagnostic
	 *            The diagnostic
	 * @param tspm
	 *            The progress monitor
	 * @return The computation to run in the scheduler to resolve the remote URIs.
	 */
	RemoteResolveComputation getRemoteResolveComputation(SynchronizedResourceSet resourceSet, URI uri,
			DiagnosticSupport diagnostic, ThreadSafeProgressMonitor tspm);

	/**
	 * Provides the resources set to use for remote resolve computations.
	 * 
	 * @param diagnostic
	 *            The diagnostic
	 * @param tspm
	 *            The progress monitor
	 * @return The resource set to use for remote resolutions.
	 */
	SynchronizedResourceSet getResourceSetForRemoteResolution(DiagnosticSupport diagnostic,
			ThreadSafeProgressMonitor tspm);

}
