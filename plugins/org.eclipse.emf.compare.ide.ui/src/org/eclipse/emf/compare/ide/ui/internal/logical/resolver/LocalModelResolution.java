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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;

/**
 * This will be called by Team in order to determine whether a given file can be compared alone, or if it
 * needs to be compared along with others (and, thus, compared from the synchronize view). Note that only
 * local data is available here.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class LocalModelResolution extends AbstractResolution {
	/** The local resolver. */
	private final IResourceDependencyLocalResolver resolver;

	/**
	 * Constructor.
	 * 
	 * @param dependencyProvider
	 *            The dependency provider
	 * @param scheduler
	 *            multi-thread support to use
	 * @param eventBus
	 *            The event bus
	 * @param monitor
	 *            Progress monitor to use
	 */
	public LocalModelResolution(IResolutionContext context, IProgressMonitor monitor) {
		super(context, monitor);
		this.resolver = context.getLocalResolver();
	}

	/**
	 * Executes this treatment.
	 * 
	 * @param start
	 *            Resource for which we want the traversal
	 * @return The {@link StorageTraversal} for the given resource, never null but possibly empty.
	 */
	public StorageTraversal run(final IResource start) {
		if (logger.isDebugEnabled()) {
			logger.debug("run() - START"); //$NON-NLS-1$
		}
		if (!(start instanceof IFile)) {
			if (logger.isDebugEnabled()) {
				logger.debug("run() - FINISH"); //$NON-NLS-1$
			}
			return new StorageTraversal(new LinkedHashSet<IStorage>());
		}
		return call(new Callable<StorageTraversal>() {
			public StorageTraversal call() throws InterruptedException {
				resolver.updateDependencies(monitor, diagnostic, (IFile)start);

				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}

				final Set<IStorage> traversalSet = resolveTraversal((IFile)start,
						Collections.<URI> emptySet());
				StorageTraversal traversal = new StorageTraversal(traversalSet, diagnostic.getDiagnostic());
				if (logger.isDebugEnabled()) {
					logger.debug("run() - FINISH"); //$NON-NLS-1$
				}
				return traversal;
			}
		});
	}
}
