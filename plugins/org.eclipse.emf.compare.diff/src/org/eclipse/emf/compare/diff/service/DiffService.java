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
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diff.DiffPlugin;
import org.eclipse.emf.compare.diff.api.DiffEngine;

/**
 * TODOCBR comment.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class DiffService {
	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_ENGINE = "engine"; //$NON-NLS-1$

	/** Keeps track of all the engines we've parsed. */
	private final List<EngineDescriptor> engines = new ArrayList<EngineDescriptor>();

	/**
	 * Default constructor.
	 */
	public DiffService() {
		parseExtensionMetadata();
	}

	private void parseExtensionMetadata() {
		final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(DiffPlugin.PLUGIN_ID, TAG_ENGINE).getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				final EngineDescriptor desc = parseEngine(configElements[j]);
				storeEngineDescriptor(desc);
			}
		}
	}

	private void storeEngineDescriptor(EngineDescriptor desc) {
		engines.add(desc);
	}

	private EngineDescriptor getBestDescriptor() {
		return getHighestDescriptor(engines);
	}

	private EngineDescriptor getHighestDescriptor(List<EngineDescriptor> set) {
		Collections.sort(set, Collections.reverseOrder());
		if (set.size() > 0)
			return set.get(0);
		return null;
	}

	private EngineDescriptor parseEngine(IConfigurationElement configElements) {
		if (!configElements.getName().equals(TAG_ENGINE))
			return null;
		final EngineDescriptor desc = new EngineDescriptor(configElements);
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
}
