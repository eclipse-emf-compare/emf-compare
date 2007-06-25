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
package org.eclipse.emf.compare.merge.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.DiffPlugin;
import org.eclipse.emf.compare.merge.api.MergeFactory;

/**
 * Services for model merging.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class MergeService {
	private static final String TAG_ENGINE = "factory"; //$NON-NLS-1$

	// The shared instance
	private static MergeService service;

	private List<FactoryDescriptor> engines = new ArrayList<FactoryDescriptor>();

	/**
	 * Default constructor.
	 */
	public MergeService() {
		service = this;
		parseExtensionMetadata();
	}

	private void parseExtensionMetadata() {
		final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
				DiffPlugin.PLUGIN_ID, "mergeFactory") //$NON-NLS-1$
				.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				final FactoryDescriptor desc = parseEngine(configElements[j]);
				storeEngineDescriptor(desc);
			}
		}
	}

	private void storeEngineDescriptor(FactoryDescriptor desc) {
		engines.add(desc);
	}

	private FactoryDescriptor getBestDescriptor() {
		return getHighestDescriptor(engines);
	}

	private FactoryDescriptor getHighestDescriptor(List<FactoryDescriptor> set) {
		Collections.sort(set, Collections.reverseOrder());
		if (set.size() > 0)
			return set.get(0);
		return null;
	}

	private FactoryDescriptor parseEngine(IConfigurationElement configElements) {
		if (!configElements.getName().equals(TAG_ENGINE))
			return null;
		final FactoryDescriptor desc = new FactoryDescriptor(configElements);
		return desc;
	}

	/**
	 * Returns the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static MergeService getInstance() {
		if (service == null)
			service = new MergeService();
		return service;
	}

	/**
	 * Returns the best {@link MergeFactory} found.
	 * 
	 * @return The best {@link MergeFactory} found.
	 */
	public MergeFactory getBestFactory() {
		final FactoryDescriptor desc = getBestDescriptor();
		final MergeFactory currentEngine = desc.getEngineInstance();
		return currentEngine;
	}

	/**
	 * Returns the best {@link MergeFactory} from a file extension.
	 * 
	 * @param extension
	 *            The extension of the file we need a {@link MergeFactory} for.
	 * @return The best {@link MergeFactory} for the given file extension.
	 */
	public MergeFactory getBestDiffEngine(String extension) {
		final FactoryDescriptor desc = getBestDescriptor();
		return desc.getEngineInstance();
	}
}
