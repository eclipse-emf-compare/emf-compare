/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;

/**
 * This registry will be populated with all IModelResolver instances registered through the associated
 * extension point.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IModelResolverRegistry {
	/**
	 * This can be called to retrieve the best model resolver for the given IStorage. "Best", here, means the
	 * highest ranking IModelResolver
	 * 
	 * @param sourceStorage
	 *            The storage we're trying to resolve the logical model of.
	 * @return The highest ranking resolver that is capable of resolving the logical model of the given
	 *         storage.
	 */
	IModelResolver getBestResolverFor(IStorage sourceStorage);

	/**
	 * Registers a new resolver within this registry instance.
	 * 
	 * @param key
	 *            Identifier of this resolver.
	 * @param resolver
	 *            The resolver to register.
	 */
	void addResolver(String key, IModelResolver resolver);

	/**
	 * Removes the resolver identified by <code>key</code> from this registry.
	 * 
	 * @param key
	 *            Identifier of the resolver we are to remove from this registry.
	 */
	void removeResolver(String key);

	/** Clears out this registry from all registered resolvers. */
	void clear();
}
