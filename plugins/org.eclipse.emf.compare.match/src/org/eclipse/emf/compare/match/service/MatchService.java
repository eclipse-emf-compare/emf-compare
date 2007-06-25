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
package org.eclipse.emf.compare.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.MatchPlugin;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;

/**
 * Service facade for matching models.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class MatchService {
	// The shared instance
	private static MatchService service;

	private static final String TAG_ENGINE = "engine"; //$NON-NLS-1$

	private static final String ALL_EXTENSIONS = "*"; //$NON-NLS-1$

	private Map<String, ArrayList<EngineDescriptor>> engines = new HashMap<String, ArrayList<EngineDescriptor>>();

	/**
	 * Default constructor.
	 */
	public MatchService() {
		service = this;
		parseExtensionMetadata();
	}

	private void parseExtensionMetadata() {
		final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
				MatchPlugin.PLUGIN_ID, "engine") //$NON-NLS-1$
				.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				final EngineDescriptor desc = parseEngine(configElements[j]);
				storeEngineDescriptor(desc);
			}
		}

	}

	private void storeEngineDescriptor(EngineDescriptor desc) {
		if (!engines.containsKey(desc.getFileExtension())) {
			engines.put(desc.getFileExtension(), new ArrayList<EngineDescriptor>());
		}
		final List<EngineDescriptor> set = engines.get(desc.getFileExtension());
		set.add(desc);
	}

	private EngineDescriptor getBestDescriptor(String extension) {
		EngineDescriptor descriptor = null;
		if (engines.containsKey(extension)) {
			descriptor = getHighestDescriptor(engines.get(extension));
		} else if (engines.containsKey(ALL_EXTENSIONS)) {
			descriptor = getHighestDescriptor(engines.get(ALL_EXTENSIONS));
		}
		return descriptor;
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
	 * Returns the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static MatchService getInstance() {
		if (service == null)
			service = new MatchService();
		return service;
	}

	/**
	 * Matches two models and returns the corresponding matching model.
	 * 
	 * @param leftRoot
	 *            Left model of the comparison.
	 * @param rightRoot
	 *            Right model of the comparison.
	 * @param monitor
	 *            Progress monitor to display for long operations.
	 * @return Matching model result of these two models' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 */
	public MatchModel doMatch(EObject leftRoot, EObject rightRoot, IProgressMonitor monitor)
			throws InterruptedException {
		MatchModel result = null;
		String extension = "ecore"; //$NON-NLS-1$
		if (leftRoot.eResource().getURI() != null)
			extension = leftRoot.eResource().getURI().fileExtension();
		if (extension == null && rightRoot.eResource() != null)
			extension = rightRoot.eResource().getURI().fileExtension();
		final EngineDescriptor desc = getBestDescriptor(extension);
		final MatchEngine currentEngine = desc.getEngineInstance();
		result = currentEngine.modelMatch(leftRoot, rightRoot, monitor);
		return result;
	}

	/**
	 * Matches three models and returns the corresponding matching model.
	 * 
	 * @param leftRoot
	 *            Left model of this comparison.
	 * @param rightRoot
	 *            Right model of this comparison.
	 * @param ancestor
	 *            Common ancestor of <code>leftRoot</code> and <code>rightRoot</code>.
	 * @return Matching model result of the comparison.
	 */
	public MatchModel doMatch(EObject leftRoot, EObject rightRoot, EObject ancestor) {
		final MatchModel result = null;
		// TODOCBR code 3 Way match
		return result;
	}

	/**
	 * Returns the best {@link MatchEngine} for a file given its extension.
	 * 
	 * @param extension
	 *            The extension of the file we need a {@link MatchEngine} for.
	 * @return The best {@link MatchEngine} for the given file extension.
	 */
	public MatchEngine getBestMatchEngine(String extension) {
		final EngineDescriptor desc = getBestDescriptor(extension);
		return desc.getEngineInstance();
	}
}
