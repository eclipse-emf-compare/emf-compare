/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Alexandra Buzila - Fixes for Bug 462938
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.eventbus.EventBus;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.graph.IGraph;
import org.eclipse.emf.compare.graph.IGraphView;
import org.eclipse.emf.compare.ide.ui.logical.AbstractModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.internal.utils.ReadOnlyGraph;
import org.eclipse.emf.compare.rcp.graph.IGraphConsumer;
import org.eclipse.emf.compare.rcp.ui.internal.util.ResourceUIUtil;

/**
 * This implementation of an {@link IModelResolver} will look up all of the models located in a set container
 * level of the "starting point" (by default, the containing project) to construct the graph of dependencies
 * between these models.
 * <p>
 * Once this graph is created for the "local" resource, the right and origin (if any) resources will be
 * inferred from the same traversal of resources, though this time expanded with a "top-down" approach : load
 * all models of the traversal from the remote side, then resolve their containment tree to check whether
 * there are other remote resources in the logical model that do not (or "that no longer) exist locally and
 * thus couldn't be discovered in the first resolution phase. <b>Note</b> that this will be looped in order to
 * determine whether the resource is really inexistent locally, or if on the contrary, it is a new dependency
 * that's been added remotely; in which case we need to start from the local resolution again : the local
 * resource may have changed locally and depend on other again.
 * </p>
 * <p>
 * All model loading will happen concurrently. At first, a distinct thread will be launched to resolve every
 * model discovered in the container we're browsing. Then, each thread can and will launch separate threads to
 * resolve the set of dependencies discovered "under" the model they are in charge of resolving.
 * </p>
 * <p>
 * No model will be loaded twice, since this will be aware of what models have already been resolved, thus
 * ignoring duplicate resolving demands.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ThreadedModelResolver extends AbstractModelResolver implements IGraphConsumer {

	private IResolutionContext context;

	/**
	 * The URI Graph instance.
	 */
	private IGraph<URI> graph;

	/**
	 * Convert the dependency graph to its read-only version.
	 * 
	 * @return a read-only version of the dependency graph associated to this model resolver.
	 */
	public IGraphView<URI> getGraphView() {
		return ReadOnlyGraph.toReadOnlyGraph(graph);
	}

	/**
	 * {@inheritDoc} When initialized, the ThreadedModelResolver will:
	 * <ol>
	 * <li>install a listener on the workspace to keep track of modified resources</li>
	 * <li>Register its {@link #graphUpdater} to its {@link #eventBus}</li>
	 * <li>initialize its {@link #scheduler}</li>
	 * </ol>
	 */
	@Override
	public void initialize() {
		super.initialize();
		if (graph == null) {
			throw new IllegalStateException();
		}
		EventBus eventBus = new EventBus();
		this.context = createContext(eventBus, graph);
		context.initialize();
	}

	/** {@inheritDoc} */
	@Override
	public void dispose() {
		context.dispose();
		super.dispose();
	}

	/**
	 * For testing purposes, this method is protected.
	 * 
	 * @param eventBus
	 * @param aGraph
	 * @return The resolution context to use.
	 */
	protected DefaultResolutionContext createContext(EventBus eventBus, IGraph<URI> aGraph) {
		return new DefaultResolutionContext(eventBus, aGraph,
				new DependencyGraphUpdater<URI>(aGraph, eventBus), new ResourceComputationScheduler<URI>(),
				new ModelResourceListener());
	}

	/** {@inheritDoc} */
	public boolean canResolve(IStorage sourceStorage) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Note that no two threads will be able to resolve models at once : all three "resolve*" methods will
	 * lock internally to prevent multiple resolutions at once. Though this shouldn't happen unless the user
	 * calls multiple comparisons one after the other in quick succession, we use this locking to prevent
	 * potential unforeseen interactions.
	 * </p>
	 */
	public StorageTraversal resolveLocalModel(final IResource start, final IProgressMonitor monitor)
			throws InterruptedException {
		LocalModelResolution comp = new LocalModelResolution(context, monitor);
		return comp.run(start);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Note that no two threads will be able to resolve models at once : all three "resolve*" methods will
	 * lock internally to prevent multiple resolutions at once. Though this shouldn't happen unless the user
	 * calls multiple comparisons one after the other in quick succession, we use this locking to prevent
	 * potential unforeseen interactions.
	 * </p>
	 */
	public SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor) throws InterruptedException {
		LocalModelsResolution comp = new LocalModelsResolution(context, left, right, origin, monitor);
		return comp.run();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Note that no two threads will be able to resolve models at once : all three "resolve*" methods will
	 * lock internally to prevent multiple resolutions at once. Though this shouldn't happen unless the user
	 * calls multiple comparisons one after the other in quick succession, we use this locking to prevent
	 * potential unforeseen interactions.
	 * </p>
	 */
	public SynchronizationModel resolveModels(final IStorageProviderAccessor storageAccessor,
			final IStorage left, final IStorage right, final IStorage origin, final IProgressMonitor monitor)
			throws InterruptedException {
		ModelsResolution comp = new ModelsResolution(context, monitor, storageAccessor, left, right, origin);
		return comp.run();
	}

	/**
	 * Getter for the ID of the Resource Graph.
	 */
	public String getId() {
		return ResourceUIUtil.RESOURCES_GRAPH_ID;
	}

	/**
	 * Setter for the URI Graph istance.
	 * 
	 * @param graph
	 *            The URI Graph
	 */
	public void setGraph(IGraph<URI> graph) {
		this.graph = graph;
	}
}
