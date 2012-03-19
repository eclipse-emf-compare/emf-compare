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
package org.eclipse.emf.compare.utils;

import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * Utility class for model loading, saving and serialization.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelUtils {
	/** Constant for the file encoding system property. */
	private static final String ENCODING_PROPERTY = "file.encoding"; //$NON-NLS-1$

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private ModelUtils() {
		// prevents instantiation
	}

	/**
	 * Loads a model from a {@link java.io.File File} in a given {@link ResourceSet}.
	 * <p>
	 * This will return the first root of the loaded model, other roots can be accessed via the resource's
	 * content.
	 * </p>
	 * 
	 * @param file
	 *            {@link java.io.File File} containing the model to be loaded.
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The model loaded from the file.
	 * @throws IOException
	 *             If the given file does not exist.
	 */
	public static EObject load(File file, ResourceSet resourceSet) throws IOException {
		return load(URI.createFileURI(file.getPath()), resourceSet);
	}

	/**
	 * Load a model from an {@link java.io.InputStream InputStream} in a given {@link ResourceSet}.
	 * <p>
	 * This will return the first root of the loaded model, other roots can be accessed via the resource's
	 * content.
	 * </p>
	 * 
	 * @param stream
	 *            The inputstream to load from
	 * @param fileName
	 *            The original filename
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The loaded model
	 * @throws IOException
	 *             If the given file does not exist.
	 */
	public static EObject load(InputStream stream, String fileName, ResourceSet resourceSet)
			throws IOException {
		if (stream == null) {
			throw new NullPointerException(EMFCompareMessages.getString("ModelUtils.NullInputStream")); //$NON-NLS-1$
		}
		EObject result = null;

		final Resource modelResource = resourceSet.createResource(URI.createURI(fileName));
		modelResource.load(stream, Collections.emptyMap());
		if (modelResource.getContents().size() > 0) {
			result = modelResource.getContents().get(0);
		}

		return result;
	}

	/**
	 * Loads a model from the String representing the location of a model.
	 * <p>
	 * This can be called with paths of the form
	 * <ul>
	 * <li><code>/pluginID/path</code></li>
	 * <li><code>platform:/plugin/pluginID/path</code></li>
	 * <li><code>platform:/resource/pluginID/path</code></li>
	 * </ul>
	 * </p>
	 * <p>
	 * This will return the first root of the loaded model, other roots can be accessed via the resource's
	 * content.
	 * </p>
	 * 
	 * @param path
	 *            Location of the model.
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The model loaded from the path.
	 * @throws IOException
	 *             If the path doesn't resolve to a reachable location.
	 */
	public static EObject load(String path, ResourceSet resourceSet) throws IOException {
		if (path == null || "".equals(path)) { //$NON-NLS-1$
			throw new IllegalArgumentException(EMFCompareMessages.getString("ModelUtils.NullPath")); //$NON-NLS-1$
		}

		final EObject result;
		// path is already defined with a platform scheme
		if (path.startsWith("platform")) { //$NON-NLS-1$
			result = load(URI.createURI(path), resourceSet);
		} else {
			EObject temp = null;
			try {
				// Will first try and load as if the model is in the plugins
				temp = load(URI.createPlatformPluginURI(path, true), resourceSet);
			} catch (IOException e) {
				// Model wasn't in the plugins, try and load it within the workspace
				try {
					temp = load(URI.createPlatformResourceURI(path, true), resourceSet);
				} catch (IOException ee) {
					// Silently discarded, will fail later on
				}
			}
			result = temp;
		}
		if (result == null) {
			throw new IOException(EMFCompareMessages.getString("ModelUtils.LoadFailure", path)); //$NON-NLS-1$
		}

		return result;
	}

	/**
	 * Loads a model from an {@link org.eclipse.emf.common.util.URI URI} in a given {@link ResourceSet}.
	 * <p>
	 * This will return the first root of the loaded model, other roots can be accessed via the resource's
	 * content.
	 * </p>
	 * 
	 * @param modelURI
	 *            {@link org.eclipse.emf.common.util.URI URI} where the model is stored.
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The model loaded from the URI.
	 * @throws IOException
	 *             If the given file does not exist.
	 */
	public static EObject load(URI modelURI, ResourceSet resourceSet) throws IOException {
		EObject result = null;
		final Resource modelResource = resourceSet.getResource(modelURI, true);
		if (modelResource.getContents().size() > 0) {
			result = modelResource.getContents().get(0);
		}

		return result;
	}

	/**
	 * Serializes the given EObjet as a String.
	 * 
	 * @param root
	 *            Root of the objects to be serialized.
	 * @return The given EObjet serialized as a String.
	 * @throws IOException
	 *             Thrown if an I/O operation has failed or been interrupted during the saving process.
	 */
	public static String serialize(EObject root) throws IOException {
		if (root == null) {
			throw new NullPointerException(EMFCompareMessages.getString("ModelUtils.NullSaveRoot")); //$NON-NLS-1$
		}

		// Copies the root to avoid modifying it
		final EObject copyRoot = EcoreUtil.copy(root);
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource newResource = resourceSet.createResource(URI.createFileURI("resource.xml")); //$NON-NLS-1$
		newResource.getContents().add(copyRoot);

		final StringWriter writer = new StringWriter();
		final Map<String, String> options = Maps.newHashMap();
		options.put(XMLResource.OPTION_ENCODING, System.getProperty(ENCODING_PROPERTY));
		// Should not throw ClassCast since uri calls for an xml resource
		((XMLResource)copyRoot.eResource()).save(writer, options);
		final String result = writer.toString();
		writer.flush();
		return result;
	}
}
