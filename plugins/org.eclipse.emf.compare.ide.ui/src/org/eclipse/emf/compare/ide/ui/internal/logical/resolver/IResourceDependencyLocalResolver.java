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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;

/**
 * A resource dependency provider is in charge of providing the dependencies of resources (local or remote
 * resources, remote meaning hosted in git for instance).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IResourceDependencyLocalResolver {

	/**
	 * Allows callers to launch the loading and resolution of the model pointed at by the given URI.
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
	 * @see LocalResolveComputation
	 */
	void demandResolve(SynchronizedResourceSet resourceSet, URI uri, DiagnosticSupport diagnostic,
			ThreadSafeProgressMonitor tspm);

	/**
	 * Make sure that dependencies for the given files are up to date.
	 * 
	 * @param monitor
	 *            Progress monitor to use
	 * @param diagnostic
	 *            Diagnostic to report issues
	 * @param start
	 *            files that are the starting points for the update
	 * @throws InterruptedException
	 */
	void updateDependencies(IProgressMonitor monitor, DiagnosticSupport diagnostic, IFile... start)
			throws InterruptedException;

}
