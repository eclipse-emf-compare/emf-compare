/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.util;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Utility class for model loading/saving and serialization within Eclipse.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 0.8
 */
public final class EclipseModelUtils {
	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private EclipseModelUtils() {
		// prevents instantiation
	}

	/**
	 * This will create an EMF {@link Monitor progress monitor} that can be used standalone to display
	 * comparison progress to the user. If <code>delegate</code> isn't <code>null</code>, the created monitor
	 * will delegate all calls to it.
	 * 
	 * @param delegate
	 *            The delegate progress monitor. Can be <code>null</code> or Eclipse specific monitors.
	 * @return The created progress monitor.
	 * @since 1.0
	 */
	public static Monitor createProgressMonitor(Object delegate) {
		final Monitor monitor;
		if (delegate instanceof IProgressMonitorWithBlocking) {
			monitor = BasicMonitor.toMonitor((IProgressMonitorWithBlocking)delegate);
		} else if (delegate instanceof IProgressMonitor) {
			monitor = BasicMonitor.toMonitor((IProgressMonitor)delegate);
		} else {
			monitor = new BasicMonitor();
		}
		return monitor;
	}

	/**
	 * This will create a {@link Resource} given the model extension it is intended for and a ResourceSet.
	 * 
	 * @param modelURI
	 *            {@link org.eclipse.emf.common.util.URI URI} where the model is stored.
	 * @param contentType
	 *            Content type of this file.
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The {@link Resource} given the model extension it is intended for.
	 * @since 1.1
	 */
	public static Resource createResource(URI modelURI, String contentType, ResourceSet resourceSet) {
		// First search the resource set for our resource factory
		Resource.Factory.Registry registry = resourceSet.getResourceFactoryRegistry();
		Object resourceFactory = registry.getContentTypeToFactoryMap().get(contentType);
		if (resourceFactory == null) {
			// Then the global registry
			registry = Resource.Factory.Registry.INSTANCE;
			resourceFactory = registry.getContentTypeToFactoryMap().get(contentType);
			if (resourceFactory != null) {
				resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap()
						.put(contentType, resourceFactory);
			}
		}

		return resourceSet.createResource(modelURI, contentType);
	}

	/**
	 * This will try and find the common content-type of the given resources.
	 * 
	 * @param uris
	 *            The resource URIs that will be compared.
	 * @return The content-type to consider when searching for a match engine or <code>null</code> if
	 *         content-types are distinct.
	 * @since 1.1
	 */
	public static String getCommonContentType(URI... uris) {
		String contentType = null;
		for (int i = 0; i < uris.length; i++) {
			if (uris[i] != null) {
				if (uris[i].isPlatformResource()) {
					final IPath modelPath = new Path(uris[i].toPlatformString(true));
					String newContentType = null;
					try {
						final IContentDescription contentDescription = ResourcesPlugin.getWorkspace()
								.getRoot().getFile(modelPath).getContentDescription();
						if (contentDescription != null && contentDescription.getContentType() != null) {
							newContentType = contentDescription.getContentType().getId();
						}

					} catch (CoreException e) {
						// Do nothing
					}

					if (contentType == null) {
						contentType = newContentType;
					} else if (newContentType != null && !contentType.equals(newContentType)) {
						return null;
					}
				}
			}
		}
		return contentType;
	}

	/**
	 * Loads a model from an {@link org.eclipse.core.resources.IFile IFile} in a given {@link ResourceSet}.
	 * <p>
	 * This will return the first root of the loaded model, other roots can be accessed via the resource's
	 * content.
	 * </p>
	 * 
	 * @param file
	 *            {@link org.eclipse.core.resources.IFile IFile} containing the model to be loaded.
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The model loaded from the file.
	 * @throws IOException
	 *             If the given file does not exist.
	 */
	public static EObject load(IFile file, ResourceSet resourceSet) throws IOException {
		EObject result = null;

		// First tries to load the IFile assuming it is in the workspace
		final URI resourceURI = URI.createPlatformResourceURI(file.getFullPath().toOSString(), true);
		Resource modelResource;
		IContentType contentType = null;
		try {
			if (file.getContentDescription() != null) {
				contentType = file.getContentDescription().getContentType();
			}
		} catch (CoreException e) {
			// discard
		}
		if (contentType != null) {
			modelResource = createResource(resourceURI, contentType.getId(), resourceSet);
		} else {
			modelResource = ModelUtils.createResource(resourceURI, resourceSet);
		}
		try {
			modelResource.load(Collections.emptyMap());
		} catch (IOException e) {
			// If it failed, load the file assuming it is in the plugins
			resourceSet.getResources().remove(modelResource);
			modelResource = ModelUtils.createResource(
					URI.createPlatformPluginURI(file.getFullPath().toOSString(), true), resourceSet);
			try {
				modelResource.load(Collections.emptyMap());
			} catch (IOException ee) {
				// If it fails anew, throws the first IOException
				throw e;
			}
		}
		// Returns the first root of the loaded model
		if (modelResource.getContents().size() > 0)
			result = modelResource.getContents().get(0);
		return result;
	}

	/**
	 * Loads a model from an {@link IPath} in a given {@link ResourceSet}.
	 * <p>
	 * This will return the first root of the loaded model, other roots can be accessed via the resource's
	 * content.
	 * </p>
	 * 
	 * @param path
	 *            {@link IPath} where the model lies.
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The model loaded from the path.
	 * @throws IOException
	 *             If the given file does not exist.
	 */
	public static EObject load(IPath path, ResourceSet resourceSet) throws IOException {
		return load(ResourcesPlugin.getWorkspace().getRoot().getFile(path), resourceSet);
	}

	/**
	 * Returns the EMF {@link Resource} saved within the given {@link IFile}.
	 * 
	 * @param file
	 *            The file from which to load an EMF {@link Resource}.
	 * @param resourceSet
	 *            The resource set in which to load the model.
	 * @return The EMF {@link Resource} saved within the given {@link IFile}.
	 * @throws IOException
	 *             If the given file cannot be loaded.
	 * @since 1.3
	 */
	public static Resource getResource(IFile file, ResourceSet resourceSet) throws IOException {
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().toString().equals(file.getFullPath().toString())) {
				return resource;
			}
		}
		return ModelUtils.load(URI.createPlatformResourceURI(file.getFullPath().toString(), true),
				resourceSet).eResource();
	}

	/**
	 * This will try and find the {@link IResource} containing the given EMF {@link Resource}. Note that the
	 * returned resource might not exist in the workspace if the EMF {@link Resource} has been loaded from a
	 * repository.
	 * 
	 * @param eResource
	 *            The logical resource for which we need a physical resource.
	 * @return The {@link IResource} that contains the given EMF {@link Resource}.
	 * @since 1.3
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
}
