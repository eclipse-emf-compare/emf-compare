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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.eventbus.EventBus;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.graph.IGraph;

/**
 * Default implementation of {@link IResolutionContext}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DefaultResolutionContext implements IResolutionContext {

	private final EventBus eventBus;

	private final IGraph<URI> graph;

	private final DependencyGraphUpdater<URI> graphUpdater;

	private final ResourceComputationScheduler<URI> scheduler;

	private final ModelResourceListener modelResourceListener;

	private IResourceDependencyProvider dependencyProvider;

	private IResourceDependencyLocalResolver localResolver;

	private IResourceDependencyRemoteResolver remoteResolver;

	private IImplicitDependencies implicitDependencies;

	public DefaultResolutionContext(EventBus eventBus, IGraph<URI> graph,
			DependencyGraphUpdater<URI> graphUpdater, ResourceComputationScheduler<URI> scheduler,
			ModelResourceListener modelResourceListener) {
		this.eventBus = checkNotNull(eventBus);
		this.graph = checkNotNull(graph);
		this.graphUpdater = checkNotNull(graphUpdater);
		this.scheduler = checkNotNull(scheduler);
		this.modelResourceListener = checkNotNull(modelResourceListener);
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public IGraph<URI> getGraph() {
		return graph;
	}

	public DependencyGraphUpdater<URI> getGraphUpdater() {
		return graphUpdater;
	}

	public ResourceComputationScheduler<URI> getScheduler() {
		return scheduler;
	}

	public ModelResourceListener getModelResourceListener() {
		return modelResourceListener;
	}

	public synchronized IResourceDependencyProvider getDependencyProvider() {
		if (dependencyProvider == null) {
			dependencyProvider = new ResourceDependencyProvider(this);
		}
		return dependencyProvider;
	}

	public synchronized IResourceDependencyLocalResolver getLocalResolver() {
		if (localResolver == null) {
			localResolver = new ResourceDependencyLocalResolver(this);
		}
		return localResolver;
	}

	public synchronized IResourceDependencyRemoteResolver getRemoteResolver() {
		if (remoteResolver == null) {
			remoteResolver = new ResourceDependencyRemoteResolver(this);
		}
		return remoteResolver;
	}

	public synchronized IImplicitDependencies getImplicitDependencies() {
		if (implicitDependencies == null) {
			implicitDependencies = new DefaultImplicitDependencies();
		}
		return implicitDependencies;
	}

	/**
	 * {@inheritDoc} When initialized, the context will:
	 * <ol>
	 * <li>install a listener on the workspace to keep track of modified resources</li>
	 * <li>Register its {@link #graphUpdater} to its {@link #eventBus}</li>
	 * <li>initialize its {@link #scheduler}</li>
	 * </ol>
	 */
	public void initialize() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(modelResourceListener);
		eventBus.register(graphUpdater);
		scheduler.initialize();
	}

	/**
	 * Disposes resources allocated during initialization.
	 */
	public void dispose() {
		scheduler.dispose();
		eventBus.unregister(graphUpdater);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(modelResourceListener);
	}
}
