/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
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
		Resource modelResource = ModelUtils.createResource(URI.createPlatformResourceURI(file.getFullPath()
				.toOSString(), true), resourceSet);
		try {
			modelResource.load(Collections.emptyMap());
		} catch (IOException e) {
			// If it failed, load the file assuming it is in the plugins
			resourceSet.getResources().remove(modelResource);
			modelResource = ModelUtils.createResource(URI.createPlatformPluginURI(file.getFullPath()
					.toOSString(), true), resourceSet);
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
}
