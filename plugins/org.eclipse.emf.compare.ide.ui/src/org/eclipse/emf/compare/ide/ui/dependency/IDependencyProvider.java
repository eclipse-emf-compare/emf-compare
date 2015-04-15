/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.dependency;

import com.google.common.annotations.Beta;

import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * Contract for clients of the org.eclipse.emf.ecompare.ide.ui.modelDependencies extension point.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 * @since 4.1
 */
@Beta
public interface IDependencyProvider {

	/**
	 * Specifies whether this {@link IDependencyProvider} can determine dependencies of the given {@code uri}.
	 * 
	 * @param uri
	 *            The {@link URI} for which additional dependencies may be determined.
	 * @return {@code true} if the {@link IDependencyProvider} can provide dependencies for the given
	 *         {@code uri}, {@code false} otherwise.
	 */
	boolean apply(URI uri);

	/**
	 * Determines the dependencies of the given {@code uri}.
	 * 
	 * @param uri
	 *            The {@link URI} for which additional dependencies may be determined.
	 * @param uriConverter
	 *            The {@link URIConverter} to produce an input stream for the {@link URI}.
	 * @return The set of dependencies of the given {@code uri}. If no dependency is determined an empty set
	 *         is returned.
	 */
	Set<URI> getDependencies(URI uri, URIConverter uriConverter);
}
