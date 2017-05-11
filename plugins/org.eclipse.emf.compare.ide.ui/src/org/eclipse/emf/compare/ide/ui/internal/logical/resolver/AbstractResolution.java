/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 512677
 *     Philip Langer - bug 516494
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.eclipse.emf.compare.ide.utils.ResourceUtil.asURI;

import com.google.common.collect.Sets;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * Abstract super-class of resolving computations.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractResolution {

	/** The context. */
	protected final IResolutionContext context;

	/** The monitor. */
	protected final SubMonitor monitor;

	/** The diagnostic. */
	protected DiagnosticSupport diagnostic;

	/** The logger */
	protected final Logger logger = Logger.getLogger(getClass());

	/** The implicit dependencies. */
	protected IImplicitDependencies implicitDependencies;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            The resolution context, must not be {@code null}
	 * @param monitor
	 *            The progress monitor, can be {@code null}
	 */
	public AbstractResolution(IResolutionContext context, IProgressMonitor monitor) {
		this.context = checkNotNull(context);
		this.monitor = SubMonitor.convert(monitor, getTicks());
	}

	/**
	 * Returns the implicit dependencies which can be used to retrieve a set of files that should be part of
	 * the same logical model than a given file.
	 * 
	 * @return The {@link IImplicitDependencies} instance.
	 */
	protected IImplicitDependencies getImplicitDependencies() {
		if (implicitDependencies == null) {
			implicitDependencies = new CachingImplicitDependencies(context.getImplicitDependencies());
		}
		return implicitDependencies;
	}

	/**
	 * Number of ticks to allocate to the progress monitor used for reporting progress.
	 * 
	 * @return The number of ticks to use, 100 by default but can be overridden if necessary.
	 */
	protected int getTicks() {
		return 100;
	}

	/**
	 * Executes the given callable as soon as there is no other computation running, and automatically runs
	 * "finalization" treatment once the computation is over, whatever its outcome (success or failure). A
	 * {@link #diagnostic} is instantiated before the computation and should be used thourghout this whole
	 * computation. It will be set to {@code null} before returning, whatever happens.
	 * 
	 * @param <T>
	 *            The type of the returned value.
	 * @param callable
	 *            Treatment to run
	 * @return The result of the treatment
	 */
	protected <T> T call(Callable<T> callable) {
		this.diagnostic = new DiagnosticSupport();
		return context.getScheduler().call(callable, getFinalizeResolvingRunnable());
	}

	/**
	 * This provides the treatment that is run at the end of the computation, whatever its outcome. It is
	 * guaranteed to run once, in a block "finally", along with other required finalization treatments that
	 * are run systematically. There's no need to acquire a lock, this is guaranteed to have been done before,
	 * and it is released after this treatment ends.
	 * 
	 * @return The {@link Runnable} to run after having resolved resources.
	 */
	protected Runnable getFinalizeResolvingRunnable() {
		return new Runnable() {
			public void run() {
				if (diagnostic.getDiagnostic().getSeverity() >= Diagnostic.ERROR) {
					// something bad (or a cancel request) happened during resolution, so we invalidate the
					// dependency graph to avoid weird behavior next time the resolution is called.
					// TODO Should we really do that?
					// FIXME dependencyGraph.clear();
				}
				diagnostic = null;
			}
		};
	}

	/**
	 * Transforms the given {@link Set} of {@link IStorage}s into a {@link Set} of {@link URI}s.
	 * 
	 * @param storages
	 *            The storages to transform, must not be {@code null}.
	 * @return A mutable set of {@link URI}s, may be empty but never {@code )null}.
	 */
	protected Set<URI> asURISet(Set<IStorage> storages) {
		final Set<URI> uris = new LinkedHashSet<URI>();
		for (IStorage storage : storages) {
			uris.add(asURI().apply(storage));
		}
		return uris;
	}

	/**
	 * Computes the traversal of the given file, excluding the given bounds if needed.
	 * 
	 * @param file
	 *            File for which the traversal is needed
	 * @param bounds
	 *            URI to exclude from the logical model computation in case both compared resources are part
	 *            of the same logical model
	 * @return A {@link Set} of the file's outgoing and incoming dependencies, never null but possibly empty.
	 */
	protected Set<IStorage> resolveTraversal(IFile file, Set<URI> bounds) {
		final Set<URI> effectiveBounds = Sets.newLinkedHashSet(bounds);
		final Set<IStorage> traversalSet = Sets.newLinkedHashSet();
		Set<IFile> filesToAdd = Sets.newLinkedHashSet();
		filesToAdd.add(file);
		Set<URI> knownURIs = Sets.newLinkedHashSet();
		while (!filesToAdd.isEmpty()) {
			Set<IFile> filesToResolve = Sets.newLinkedHashSet();
			for (IFile newFile : filesToAdd) {
				URI baseUri = ResourceUtil.createURIFor(newFile);
				Set<URI> newURIs = getImplicitDependencies().of(baseUri, URIConverter.INSTANCE);
				// Don't visit all these URIs while we're visiting each URI.
				effectiveBounds.addAll(newURIs);
				for (URI uri : newURIs) {
					if (knownURIs.add(uri)) {
						// We must exclude the URI we're visiting now from the bounds.
						effectiveBounds.remove(uri);
						IFile toResolve = ResolutionUtil.getFileAt(uri);
						Iterable<URI> dependencies = context.getDependencyProvider()
								.getDependenciesOf(toResolve, effectiveBounds);
						// But after this dependency computation don't visit it again.
						effectiveBounds.add(uri);
						for (URI dep : dependencies) {
							IFile dependentFile = ResolutionUtil.getFileAt(dep);
							if (dependentFile != null && traversalSet.add(dependentFile)
									&& !knownURIs.contains(dep)) {
								filesToResolve.add(dependentFile);
								// Don't visit this dependency while visiting any other URIs.
								// We'll visit it directly anyway when we visit the files to resolve.
								effectiveBounds.add(ResourceUtil.createURIFor(dependentFile));
							}
							if (monitor.isCanceled()) {
								throw new OperationCanceledException();
							}
						}
					}
				}
			}
			filesToAdd.clear();
			filesToAdd = filesToResolve;
		}
		return traversalSet;
	}
}
