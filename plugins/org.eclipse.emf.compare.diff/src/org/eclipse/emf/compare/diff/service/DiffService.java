/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diff.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.DiffPlugin;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.api.DiffEngine;
import org.eclipse.emf.compare.match.MatchModel;

/**
 * TODOCBR doc
 * 
 * @author Cedric Brun  cedric.brun@obeo.fr 
 * 
 */
public class DiffService {
	// The shared instance
	private static DiffService service;

	private Collection engines = new ArrayList();

	/**
	 * The constructor
	 */
	public DiffService() {
		service = this;
		parseExtensionMetadata();
	}

	private void parseExtensionMetadata() {
		IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(DiffPlugin.PLUGIN_ID, "engine")
				.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configElements = extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				EngineDescriptor desc = parseEngine(configElements[j]);
				storeEngineDescriptor(desc);
			}
		}

	}

	private void storeEngineDescriptor(EngineDescriptor desc) {
		engines.add(desc);
	}

	private EngineDescriptor getBestDescriptor() {
		return getHighestDescriptor((List) (engines));
	}

	private EngineDescriptor getHighestDescriptor(List set) {
		Collections.sort(set, Collections.reverseOrder());
		if (set.size() > 0)
			return (EngineDescriptor) set.get(0);
		return null;
	}

	private static final String TAG_ENGINE = "engine";

	private EngineDescriptor parseEngine(IConfigurationElement configElements) {
		if (!configElements.getName().equals(TAG_ENGINE))
			return null;
		EngineDescriptor desc = new EngineDescriptor(configElements);
		return desc;
	}

	/**
	 * 
	 * @return the singleton instance
	 */
	public static DiffService getInstance() {
		if (service == null)
			service = new DiffService();
		return service;
	}

	/**
	 * Build diff model from a match model
	 * 
	 * @param match
	 * @return the corresponding diff model
	 */
	public DiffModel doDiff(MatchModel match) {
		DiffModel result = null;
		EngineDescriptor desc = getBestDescriptor();
		DiffEngine currentEngine = desc.getEngineInstance();
		result = currentEngine.doDiff(match);
		return result;
	}

	/**
	 * Return the best diff engine from a file extension
	 * 
	 * @param extension
	 * @return the best diff engine
	 */
	public DiffEngine getBestDiffEngine(String extension) {
		EngineDescriptor desc = getBestDescriptor();
		return desc.getEngineInstance();
	}

}
