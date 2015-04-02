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

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;

/**
 * A resource dependency provider is in charge of providing the dependencies of resources (local or remote
 * resources, remote meaning hosted in git for instance).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IResourceDependencyProvider {

	/**
	 * Provides the dependencies of the given file.
	 * 
	 * @param file
	 *            The file
	 * @return The file's dependencies, as a never null Iterable over the dependencies {@link URI}s.
	 */
	Iterable<URI> getDependenciesOf(IFile file);

	/**
	 * Provides the dependencies of the given file.
	 * 
	 * @param file
	 *            The file
	 * @param bounds
	 *            The bounds to exclude from the research, in cas the compared resources are part of the same
	 *            logical model.
	 * @return The file's dependencies, as a never null Iterable over the dependencies {@link URI}s.
	 */
	Iterable<URI> getDependenciesOf(IFile file, Set<URI> bounds);

	/**
	 * Indicates whether the given parent has the given URI as child according to the graph of dependencies.
	 * 
	 * @param parent
	 *            The parent URI
	 * @param candidate
	 *            The candidate child
	 * @return {@code true} if and only if the candidate URI is known as a child of the parent URI in the
	 *         graph of dependencies.
	 */
	boolean hasChild(URI parent, URI candidate);

}
