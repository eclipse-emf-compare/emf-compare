/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.logical.common;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This will provide utility methods for going back and forth from {@link Resource}s to {@link IResource}s.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">laurent Goubet</a>
 */
public final class EMFResourceUtil {
	/** Utility classes don't need to (and shouldn't) be instantiated. */
	private EMFResourceUtil() {
		// Hides default constructor
	}

	/**
	 * Returns the EMF {@link Resource} saved within the given {@link IFile}.
	 * 
	 * @param file
	 *            The file from which to load an EMF {@link Resource}.
	 * @param resourceSet
	 *            The resource set in which to load the model.
	 * @return The EMF {@link Resource} saved within the given {@link IFile}.
	 */
	public static final Resource getResource(IFile file, ResourceSet resourceSet) {
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().toString().equals(file.getFullPath().toString())) {
				return resource;
			}
		}
		try {
			return ModelUtils.load(URI.createPlatformResourceURI(file.getFullPath().toString(), true),
					resourceSet).eResource();
		} catch (IOException e) {
			// FIXME handle this gracefully
			throw new RuntimeException(e);
		}
	}
}
