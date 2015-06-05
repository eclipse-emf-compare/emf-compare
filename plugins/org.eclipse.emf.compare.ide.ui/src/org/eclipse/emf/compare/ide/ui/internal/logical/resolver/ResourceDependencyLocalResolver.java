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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.util.ThreadSafeProgressMonitor;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.internal.utils.Graph;

/**
 * The default implementation of the {@link IResourceDependencyProvider}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ResourceDependencyLocalResolver implements IResourceDependencyLocalResolver {
	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(ResourceDependencyLocalResolver.class);

	/** The scheduler. */
	private final ResourceComputationScheduler<URI> scheduler;

	/** The event bus */
	private final EventBus eventBus;

	/** The dependency graph. */
	private final Graph<URI> dependencyGraph;

	/** The resource listener. */
	private final ModelResourceListener resourceListener;

	/** The implicit dependencies */
	private final IImplicitDependencies implicitDependencies;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            The resolution context, must not be {@code null}
	 */
	public ResourceDependencyLocalResolver(IResolutionContext context) {
		this.implicitDependencies = context.getImplicitDependencies();
		this.scheduler = context.getScheduler();
		this.eventBus = context.getEventBus();
		this.dependencyGraph = context.getGraph();
		this.resourceListener = context.getModelResourceListener();
	}

	public Iterable<URI> getDependenciesOf(IFile file) {
		return getDependenciesOf(file, Collections.<URI> emptySet());
	}

	public Iterable<URI> getDependenciesOf(IFile file, Set<URI> bounds) {
		final URI expectedURI = ResourceUtil.createURIFor(file);

		final Iterable<URI> dependencies;
		switch (ResolutionUtil.getResolutionScope()) {
			case WORKSPACE:
				dependencies = dependencyGraph.getSubgraphContaining(expectedURI, bounds);
				break;
			case PROJECT:
				final Set<URI> allDependencies = dependencyGraph.getSubgraphContaining(expectedURI, bounds);
				final IResource project = file.getProject();
				dependencies = Iterables.filter(allDependencies, isInContainer(project));
				break;
			case CONTAINER:
				final Set<URI> allDependencies1 = dependencyGraph.getSubgraphContaining(expectedURI, bounds);
				final IResource container = file.getParent();
				dependencies = Iterables.filter(allDependencies1, isInContainer(container));
				break;
			case OUTGOING:
				dependencies = dependencyGraph.getTreeFrom(expectedURI, bounds);
				break;
			case SELF:
				// fall through
			default:
				dependencies = Collections.singleton(expectedURI);
				break;
		}
		return dependencies;
	}

	/**
	 * Checks the current state of our {@link #resourceListener} and updates the dependency graph for all
	 * resources that have been changed since we last checked.
	 * 
	 * @param resourceSet
	 *            The resource set in which to load our temporary resources.
	 * @param diagnostic
	 *            The diagnostic.
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 */
	protected void updateChangedResources(SynchronizedResourceSet resourceSet, DiagnosticSupport diagnostic,
			ThreadSafeProgressMonitor tspm) {
		// this.diagnostic = createDiagnostic();
		final Set<URI> removedURIs = Sets.difference(resourceListener.popRemovedURIs(), scheduler
				.getComputedElements());
		final Set<URI> changedURIs = Sets.difference(resourceListener.popChangedURIs(), scheduler
				.getComputedElements());

		eventBus.post(new ResourceRemovedEvent<URI>(removedURIs));

		// We need to re-resolve the changed resources, along with their direct parents
		final Set<URI> recompute = new LinkedHashSet<URI>(changedURIs);
		final Multimap<URI, URI> parentToGrandParents = ArrayListMultimap.create();
		for (URI changed : changedURIs) {
			if (dependencyGraph.contains(changed)) {
				Set<URI> directParents = dependencyGraph.getDirectParents(changed);
				recompute.addAll(directParents);
				for (URI uri : directParents) {
					Set<URI> grandParents = dependencyGraph.getDirectParents(uri);
					parentToGrandParents.putAll(uri, grandParents);
				}
			}
		}

		eventBus.post(new ResourceRemovedEvent<URI>(recompute));

		demandResolveAll(recompute, diagnostic, resourceSet, tspm);

		// Re-connect changed resources parents' with their parents
		demandResolveAll(Iterables.filter(parentToGrandParents.keySet(), new Predicate<URI>() {
			public boolean apply(URI uri) {
				return dependencyGraph.contains(uri);
			}
		}), diagnostic, resourceSet, tspm);
	}

	public void demandResolve(final SynchronizedResourceSet resourceSet, final URI uri,
			final DiagnosticSupport diagnostic, final ThreadSafeProgressMonitor tspm) {
		if (ResolutionUtil.isInterruptedOrCanceled(tspm)) {
			scheduler.demandShutdown();
			return;
		}
		for (URI currentUri : implicitDependencies.of(uri, resourceSet.getURIConverter())) {
			scheduler.scheduleComputation(new LocalResolveComputation(scheduler, eventBus, diagnostic,
					resourceSet, currentUri, new MonitorCallback(diagnostic, tspm), tspm));
		}
	}

	/**
	 * Allows callers to launch the loading and resolution of the model pointed at by the given URI.
	 * <p>
	 * This will check whether the given storage isn't already being resolved, then submit a job to the
	 * {@link #resolvingPool} to load and resolve the model in a separate thread.
	 * </p>
	 * 
	 * @param resourceSet
	 *            The resource set in which to load the resource.
	 * @param uris
	 *            The uris we are to try and load as models.
	 * @param diagnostic
	 *            The diagnostic
	 * @param tspm
	 *            Monitor on which to report progress to the user.
	 * @see LocalResolveComputation
	 */
	private void demandResolveAll(Iterable<URI> uris, final DiagnosticSupport diagnostic,
			final SynchronizedResourceSet resourceSet, final ThreadSafeProgressMonitor tspm) {
		scheduler.computeAll(Iterables.transform(uris, new Function<URI, IComputation<URI>>() {
			public IComputation<URI> apply(final URI uri) {
				// In this case, we don't want to call the implicit dependencies extension point
				return new LocalResolveComputation(scheduler, eventBus, diagnostic, resourceSet, uri,
						new MonitorCallback(diagnostic, tspm), tspm);
			}
		}));
	}

	/**
	 * This predicate can be used to check wether a given URI points to a workspace resource contained in the
	 * given container.
	 * 
	 * @param container
	 *            The container in which we need the resources to be contained.
	 * @return A ready to use predicate.
	 */
	protected Predicate<URI> isInContainer(final IResource container) {
		return new Predicate<URI>() {
			public boolean apply(URI input) {
				if (input != null) {
					final IFile pointedFile = ResolutionUtil.getFileAt(input);
					if (pointedFile != null) {
						return container.getLocation().isPrefixOf(pointedFile.getLocation());
					}
				}
				return false;
			}
		};
	}

	/**
	 * Update the dependency graph to make sure that it contains the given file.
	 * <p>
	 * If the graph does not yet contain this file, we'll try and find cross-references outgoing from and/or
	 * incoming to the given file, depending on the current {@link #getResolutionScope() resolution scope}.
	 * </p>
	 * 
	 * @param monitor
	 *            The progress monitor.
	 * @param diagnostic
	 *            The diagnostic
	 * @param files
	 *            The files which we need to be present in the dependency graph.
	 * @throws InterruptedException
	 *             if the computation of dependencies is interrupted.
	 */
	public void updateDependencies(IProgressMonitor monitor, final DiagnosticSupport diagnostic,
			IFile... files) throws InterruptedException {
		final ThreadSafeProgressMonitor tspm = new ThreadSafeProgressMonitor(monitor);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("INSTANTIATING SynchronizedResourceSet to update dependencies with " //$NON-NLS-1$
					+ files.length + " files"); //$NON-NLS-1$
		}
		final SynchronizedResourceSet resourceSet = new SynchronizedResourceSet(
				new LocalMonitoredProxyCreationListener(tspm, eventBus, this, diagnostic));
		Iterable<IFile> filesToResolve = Iterables.filter(Arrays.asList(files), new Predicate<IFile>() {
			public boolean apply(IFile file) {
				return !dependencyGraph.contains(ResourceUtil.asURI().apply(file));
			}
		});
		scheduler.runAll(Iterables.transform(filesToResolve, new Function<IFile, Runnable>() {
			public Runnable apply(final IFile file) {
				return new Runnable() {
					public void run() {
						final IResource startingPoint = getResolutionStartingPoint(file);
						final ModelResourceVisitor modelVisitor = new ModelResourceVisitor(scheduler,
								resourceSet, ResourceDependencyLocalResolver.this, diagnostic, tspm);
						try {
							startingPoint.accept(modelVisitor);
						} catch (CoreException e) {
							diagnostic.merge(BasicDiagnostic.toDiagnostic(e));
						}
					}
				};
			}
		}));
		updateChangedResources(resourceSet, diagnostic, tspm);

		resourceSet.dispose();
	}

	/**
	 * Returns the starting point for the resolution of the given file's logical model according to
	 * {@link #getResolutionScope()}.
	 * 
	 * @param file
	 *            The file which logical model we need to add to the current {@link #dependencyGraph}.
	 * @return Starting point for this file's logical model resolution.
	 * @see CrossReferenceResolutionScope
	 */
	protected IResource getResolutionStartingPoint(IFile file) {
		final IResource startingPoint;
		switch (ResolutionUtil.getResolutionScope()) {
			case WORKSPACE:
				startingPoint = ResourcesPlugin.getWorkspace().getRoot();
				break;
			case PROJECT:
				startingPoint = file.getProject();
				break;
			case CONTAINER:
				startingPoint = file.getParent();
				break;
			case OUTGOING:
				// fall through, the difference between SELF and OUTGOING will only come later on
			case SELF:
				// fall through
			default:
				startingPoint = file;
				break;
		}
		return startingPoint;
	}

	public boolean hasChild(URI parent, URI candidate) {
		return dependencyGraph.hasChild(parent, candidate);
	}
}
