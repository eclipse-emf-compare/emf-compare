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
import org.eclipse.core.resources.IStorage;
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
	 * This will try and find the {@link IFile} containing the given EMF {@link Resource}. Note that the
	 * returned resource might not exist in the workspace if the EMF {@link Resource} has been loaded from a
	 * repository.
	 * 
	 * @param eResource
	 *            The logical resource for which we need a physical resource.
	 * @return The {@link IFile} that contains the given EMF {@link Resource}.
	 */
	public static IFile findIResource(Resource eResource) {
		final URI uri = eResource.getURI();
		IFile iFile = null;
		if (uri != null) {
			if (uri.isPlatformResource()) {
				final IPath path = new Path(uri.trimFragment().toPlatformString(true));
				iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			} else {
				// FIXME URI should be deresolved against the workspace root
				final IPath path = new Path(uri.trimFragment().path());
				iFile = (IFile)ResourcesPlugin.getWorkspace().getRoot().findMember(path);
			}
		}
		return iFile;
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

	// FIXME is this still needed?
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

	/**
	 * This will try and load the given file as an EMF model, and return the corresponding {@link Resource} if
	 * at all possible.
	 * 
	 * @param storage
	 *            The file we need to try and load as a model.
	 * @param resourceSet
	 *            The resource set in which to load this Resource.
	 * @return The loaded EMF Resource if {@code file} was a model, {@code null} otherwise.
	 */
	public static Resource loadResource(IStorage storage, ResourceSet resourceSet) {
		final String resourceName = storage.getName();
		String path = storage.getFullPath().toString();
		if (!path.endsWith(resourceName)) {
			final int endIndex = path.indexOf(resourceName) + resourceName.length();
			path = path.substring(0, endIndex);
		}
		final URI uri = URI.createPlatformResourceURI(path, true);
		final Resource existing = resourceSet.getResource(uri, false);
		if (existing != null) {
			return existing;
		}

		InputStream stream = null;
		Resource resource = null;
		try {
			resource = resourceSet.createResource(uri);
			stream = storage.getContents();
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
