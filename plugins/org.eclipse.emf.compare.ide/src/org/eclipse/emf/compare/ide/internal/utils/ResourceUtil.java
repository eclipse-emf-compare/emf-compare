/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This class will be used to provide various utility method to convert IFile to Resource and vice versa.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ResourceUtil {
	/**
	 * This does not need to be instantiated.
	 */
	private ResourceUtil() {
		// hides default constructor
	}

	/**
	 * This will try and find the {@link IResource} containing the given EMF {@link Resource}. Note that the
	 * returned resource might not exist in the workspace if the EMF {@link Resource} has been loaded from a
	 * repository.
	 * 
	 * @param eResource
	 *            The logical resource for which we need a physical resource.
	 * @return The {@link IResource} that contains the given EMF {@link Resource}.
	 */
	public static IResource findIResource(Resource eResource) {
		final URI uri = eResource.getURI();
		IResource iResource = null;
		if (uri != null) {
			if (uri.isPlatformResource()) {
				final IPath path = new Path(uri.trimFragment().toPlatformString(true));
				iResource = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			} else {
				// FIXME URI should be deresolved against the workspace root
				final IPath path = new Path(uri.trimFragment().path());
				iResource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
			}
		}
		return iResource;
	}

	/**
	 * This will try and load the given file as an EMF model, and return the corresponding {@link Resource} if
	 * at all possible.
	 * 
	 * @param file
	 *            The file we need to try and load as a model.
	 * @return The loaded EMF Resource if {@code file} was a model, {@code null} otherwise.
	 */
	public static Resource loadResource(IFile file) {
		return loadResource(file, new ResourceSetImpl());
	}

	/**
	 * This will try and load the given file as an EMF model, and return the corresponding {@link Resource} if
	 * at all possible.
	 * 
	 * @param file
	 *            The file we need to try and load as a model.
	 * @param resourceSet
	 *            The resource set in which to load this Resource.
	 * @return The loaded EMF Resource if {@code file} was a model, {@code null} otherwise.
	 */
	public static Resource loadResource(IFile file, ResourceSet resourceSet) {
		final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		final Resource existing = resourceSet.getResource(uri, false);
		if (existing != null) {
			return existing;
		}

		InputStream stream = null;
		Resource resource = null;
		try {
			resource = resourceSet.createResource(uri);
			stream = file.getContents();
			resource.load(stream, Collections.emptyMap());
		} catch (IOException e) {
			// return null
		} catch (CoreException e) {
			// return null
		} catch (WrappedException e) {
			// return null
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// Should have been caught by the outer try
				}
			}
		}

		return resource;
	}
}
