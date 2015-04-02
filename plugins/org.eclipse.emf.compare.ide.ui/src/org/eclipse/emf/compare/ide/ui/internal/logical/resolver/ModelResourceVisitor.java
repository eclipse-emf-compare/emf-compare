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

import static org.eclipse.emf.compare.ide.utils.ResourceUtil.hasModelType;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;

/**
 * This implementation of a resource visitor will allow us to browse a given hierarchy and resolve the models
 * files in contains, as determined by {@link ThreadedModelResolver#MODEL_CONTENT_TYPES}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see ThreadedModelResolver#hasModelType(IFile)
 */
public class ModelResourceVisitor implements IResourceVisitor {
	/** Resource set in which to load the model files this visitor will find. */
	private final SynchronizedResourceSet resourceSet;

	/** Monitor on which to report progress to the user. */
	private final ThreadSafeProgressMonitor tspm;

	/** Scheduler to use. */
	private final ResourceComputationScheduler<URI> scheduler;

	/** Local dependency resolver. */
	private final IResourceDependencyLocalResolver resolver;

	/** Diagnostic. */
	private final DiagnosticSupport diagnostic;

	/**
	 * Default constructor.
	 * 
	 * @param scheduler
	 *            The scheduler
	 * @param resourceSet
	 *            The resource set in which this visitor will load the model files it finds.
	 * @param resolver
	 *            the local dependency resolver
	 * @param diagnostic
	 *            The diagnostic
	 * @param monitor
	 *            Monitor on which to report progress to the user.
	 */
	public ModelResourceVisitor(ResourceComputationScheduler<URI> scheduler,
			SynchronizedResourceSet resourceSet, IResourceDependencyLocalResolver resolver,
			DiagnosticSupport diagnostic, ThreadSafeProgressMonitor monitor) {
		this.resourceSet = resourceSet;
		this.scheduler = scheduler;
		this.resolver = resolver;
		this.diagnostic = diagnostic;
		this.tspm = monitor;
	}

	/** {@inheritDoc} */
	public boolean visit(IResource resource) throws CoreException {
		if (ResolutionUtil.isInterruptedOrCanceled(tspm)) {
			scheduler.demandShutdown();
			// cancel the visit
			throw new OperationCanceledException();
		}

		if (resource instanceof IFile) {
			final IFile file = (IFile)resource;
			if (hasModelType(file)) {
				final URI expectedURI = ResourceUtil.createURIFor(file);
				resolver.demandResolve(resourceSet, expectedURI, diagnostic, tspm);
			}
			return false;
		}
		return true;
	}
}
