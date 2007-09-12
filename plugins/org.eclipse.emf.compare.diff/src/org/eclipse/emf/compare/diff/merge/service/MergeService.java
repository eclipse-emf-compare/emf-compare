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
package org.eclipse.emf.compare.diff.merge.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.diff.DiffPlugin;
import org.eclipse.emf.compare.diff.merge.api.MergeFactory;

/**
 * Services for model merging.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class MergeService {
	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_FACTORY = "factory"; //$NON-NLS-1$

	/** Keeps track of all the engines parsed. */
	private final List<FactoryDescriptor> engines = new ArrayList<FactoryDescriptor>();

	/**
	 * Default constructor.
	 */
	public MergeService() {
		parseExtensionMetadata();
	}

	/**
	 * Returns the best {@link MergeFactory} from a file extension.
	 * 
	 * @param extension
	 *            The extension of the file we need a {@link MergeFactory} for.
	 * @return The best {@link MergeFactory} for the given file extension.
	 */
	public MergeFactory getBestDiffEngine(@SuppressWarnings("unused")
	String extension) {
		final FactoryDescriptor desc = getBestDescriptor();
		return desc.getEngineInstance();
	}

	/**
	 * Returns the best {@link FactoryDescriptor}.
	 * 
	 * @return The best {@link FactoryDescriptor}.
	 */
	private FactoryDescriptor getBestDescriptor() {
		return getHighestDescriptor(engines);
	}

	/**
	 * Returns the highest {@link FactoryDescriptor} from the given {@link List}.
	 * 
	 * @param set
	 *            {@link List} of {@link FactoryDescriptor} from which to find the highest one.
	 * @return The highest {@link FactoryDescriptor} from the given {@link List}.
	 */
	private FactoryDescriptor getHighestDescriptor(List<FactoryDescriptor> set) {
		Collections.sort(set, Collections.reverseOrder());
		if (set.size() > 0)
			return set.get(0);
		return null;
	}

	/**
	 * This will parse the given {@link IConfigurationElement configuration element} and return a descriptor
	 * for it if it describes and engine.
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link FactoryDescriptor} wrapped around <code>configElement</code> if it describes an
	 *         engine, <code>null</code> otherwise.
	 */
	private FactoryDescriptor parseEngine(IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_FACTORY))
			return null;
		final FactoryDescriptor desc = new FactoryDescriptor(configElement);
		return desc;
	}

	/**
	 * This will parse the currently running platform for extensions and store all the merge engines that can
	 * be found.
	 */
	private void parseExtensionMetadata() {
		final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
				DiffPlugin.PLUGIN_ID, "mergeFactory") //$NON-NLS-1$
				.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				final FactoryDescriptor desc = parseEngine(configElements[j]);
				engines.add(desc);
			}
		}
	}
}
