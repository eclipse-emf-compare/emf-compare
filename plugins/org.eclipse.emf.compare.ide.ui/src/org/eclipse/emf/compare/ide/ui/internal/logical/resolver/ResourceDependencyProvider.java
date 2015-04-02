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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.internal.utils.Graph;

/**
 * The default implementation of the {@link IResourceDependencyProvider}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ResourceDependencyProvider implements IResourceDependencyProvider {

	/** The dependency graph. */
	private final Graph<URI> dependencyGraph;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            The resolution context, must not be null.
	 */
	public ResourceDependencyProvider(IResolutionContext context) {
		checkNotNull(context);
		this.dependencyGraph = checkNotNull(context.getGraph());
	}

	public Iterable<URI> getDependenciesOf(IFile file) {
		return getDependenciesOf(file, Collections.<URI> emptySet());
	}

	public boolean hasChild(URI parent, URI candidate) {
		return dependencyGraph.hasChild(parent, candidate);
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
}
