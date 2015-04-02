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

import com.google.common.eventbus.EventBus;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.internal.utils.Graph;

/**
 * A resolution context provides the different elements that can be involved in a comparison taking logical
 * models into account. One instance of this interface is supposed to provide elements that are consistent
 * with one another, that is, which can be used together in the same computation.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IResolutionContext {
	/** The {@link EventBus} to use to broadcast and receive events. */
	EventBus getEventBus();

	/** The graph of dependencies. */
	Graph<URI> getGraph();

	/** The graph updater, in charge of making changes to the dependency graph. */
	DependencyGraphUpdater<URI> getGraphUpdater();

	/** The scheduler used to support multi-threading. */
	ResourceComputationScheduler<URI> getScheduler();

	/** The provider of dependencies between resources / URIs */
	IResourceDependencyProvider getDependencyProvider();

	/** The resolver for local resources. */
	IResourceDependencyLocalResolver getLocalResolver();

	/** The resolver for "remote" (git) resources. */
	IResourceDependencyRemoteResolver getRemoteResolver();

	/** The listener of model resource changes. */
	ModelResourceListener getModelResourceListener();

	/** The implicit dependencies provider. */
	IImplicitDependencies getImplicitDependencies();

	/** Initializes the context. */
	void initialize();

	/** Disposes the context. */
	void dispose();
}
