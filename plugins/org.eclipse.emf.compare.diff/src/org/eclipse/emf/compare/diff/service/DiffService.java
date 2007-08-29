/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diff.DiffPlugin;
import org.eclipse.emf.compare.diff.api.DiffEngine;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;

/**
 * TODOCBR comment. and handle dynamic plugin notifications
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class DiffService {
	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_ENGINE = "engine"; //$NON-NLS-1$

	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_DIFF_EXTENSION = "diff_extension"; //$NON-NLS-1$

	/** Keeps track of all the engines we've parsed. */
	private final List<EngineDescriptor> engines = new ArrayList<EngineDescriptor>();

	/** Keeps track of all the diff extensions we've parsed. */
	private final Map<String, ArrayList<DiffExtensionDescriptor>> diffExtensions = new ConcurrentHashMap<String, ArrayList<DiffExtensionDescriptor>>(
			8);

	/**
	 * Default constructor.
	 */
	public DiffService() {
		parseExtensionMetadata();
	}

	/**
	 * This will parse the currently running platform for extensions and store
	 * all the diff engines that can be found.
	 */
	private void parseExtensionMetadata() {
		final IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(DiffPlugin.PLUGIN_ID, TAG_ENGINE)
				.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				final EngineDescriptor desc = parseEngine(configElements[j]);
				engines.add(desc);
			}
		}

		/*
		 * Now parsing the diff extension extension point
		 */
		final IExtension[] d_extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(DiffPlugin.PLUGIN_ID, TAG_DIFF_EXTENSION)
				.getExtensions();
		for (int i = 0; i < d_extensions.length; i++) {
			final IConfigurationElement[] configElements = d_extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				final DiffExtensionDescriptor desc = parseDiffExtension(configElements[j]);
				if (!diffExtensions.containsKey(desc.getFileExtension())) {
					diffExtensions.put(desc.getFileExtension(),
							new ArrayList<DiffExtensionDescriptor>());
				}
				final List<DiffExtensionDescriptor> set = diffExtensions
						.get(desc.getFileExtension());
				set.add(desc);
			}
		}
	}

	/**
	 * Returns the best {@link EngineDescriptor}.
	 * 
	 * @return The best {@link EngineDescriptor}.
	 */
	private EngineDescriptor getBestDescriptor() {
		return getHighestDescriptor(engines);
	}

	/**
	 * Returns the highest {@link EngineDescriptor} from the given {@link List}.
	 * 
	 * @param set
	 *            {@link List} of {@link EngineDescriptor} from which to find
	 *            the highest one.
	 * @return The highest {@link EngineDescriptor} from the given {@link List}.
	 */
	private EngineDescriptor getHighestDescriptor(List<EngineDescriptor> set) {
		Collections.sort(set, Collections.reverseOrder());
		if (set.size() > 0)
			return set.get(0);
		return null;
	}

	/**
	 * This will parse the given
	 * {@link IConfigurationElement configuration element} and return a
	 * descriptor for it if it describes and engine.
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link EngineDescriptor} wrapped around
	 *         <code>configElement</code> if it describes an engine,
	 *         <code>null</code> otherwise.
	 */
	private EngineDescriptor parseEngine(IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_ENGINE))
			return null;
		final EngineDescriptor desc = new EngineDescriptor(configElement);
		return desc;
	}

	/**
	 * This will parse the given
	 * {@link IConfigurationElement configuration element} and return a
	 * descriptor for it if it describes a DiffExtension
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link DiffExtensionDescriptor} wrapped around
	 *         <code>configElement</code> if it describes an diff extension,
	 *         <code>null</code> otherwise.
	 */
	private DiffExtensionDescriptor parseDiffExtension(
			IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_DIFF_EXTENSION))
			return null;
		final DiffExtensionDescriptor desc = new DiffExtensionDescriptor(
				configElement);
		return desc;
	}

	/**
	 * Returns the best {@link DiffEngine} for a file extension.
	 * 
	 * @param extension
	 *            The extension of the file we need a {@link DiffEngine} for.
	 * @return The best {@link DiffEngine} for the given file extension.
	 */
	public DiffEngine getBestDiffEngine(@SuppressWarnings("unused")
	String extension) {
		final EngineDescriptor desc = getBestDescriptor();
		return desc.getEngineInstance();
	}

	/**
	 * TODOCBR comment
	 * 
	 * @param extension
	 * @return
	 */
	public Collection<AbstractDiffExtension> getCorrespondingDiffExtensions(
			String extension) {
		Collection<AbstractDiffExtension> result = new ArrayList<AbstractDiffExtension>();
		if (diffExtensions.containsKey("*")) {
			for (DiffExtensionDescriptor extensionDesc : diffExtensions
					.get("*")) {
				result.add(extensionDesc.getDiffExtensionInstance());
			}
		}
		Collection<DiffExtensionDescriptor> descs = diffExtensions
				.get(extension);
		if (descs != null)
			for (DiffExtensionDescriptor desc : descs) {
				result.add(desc.getDiffExtensionInstance());
			}
		return result;
	}
}
