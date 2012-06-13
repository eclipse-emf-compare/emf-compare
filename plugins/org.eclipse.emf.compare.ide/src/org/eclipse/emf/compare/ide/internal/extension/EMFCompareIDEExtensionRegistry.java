/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.logical.extension.EMFCompareExtensionRegistry;

/**
 * This will contain all of the EMF Compare extensions parsed from its extension points.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class EMFCompareIDEExtensionRegistry extends EMFCompareExtensionRegistry {
	/** List of all the model resolvers contributed through "org.eclipse.emf.compare.modelResolver". */
	private static final List<ModelResolverDescriptor> MODEL_RESOLVERS = new ArrayList<ModelResolverDescriptor>();

	/** This class should not be instantiated. */
	private EMFCompareIDEExtensionRegistry() {
		super();
	}

	/**
	 * Adds a model resolver to the registry.
	 * 
	 * @param modelResolver
	 *            Model resolver that is to be added to this registry.
	 */
	public static void addModelResolver(ModelResolverDescriptor modelResolver) {
		MODEL_RESOLVERS.add(modelResolver);
	}

	/**
	 * Removes all extensions from this registry. This will be called at plugin stopping and should never be
	 * used explicitly.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static void clearRegistry() {
		MODEL_RESOLVERS.clear();
	}

	/**
	 * This will return a copy of the registered model resolvers list.
	 * 
	 * @return A copy of the registered model resolvers list.
	 */
	public static List<ModelResolverDescriptor> getRegisteredModelResolvers() {
		return new ArrayList<ModelResolverDescriptor>(MODEL_RESOLVERS);
	}

	/**
	 * Removes a model resolver from this registry.
	 * 
	 * @param modelResolverClassName
	 *            Qualified class name of the model resolver that is to be removed from the reigstry.
	 */
	public static void removeModelResolver(String modelResolverClassName) {
		for (ModelResolverDescriptor descriptor : getRegisteredModelResolvers()) {
			if (descriptor.getExtensionClassName().equals(modelResolverClassName)) {
				MODEL_RESOLVERS.remove(descriptor);
			}
		}
	}
}
