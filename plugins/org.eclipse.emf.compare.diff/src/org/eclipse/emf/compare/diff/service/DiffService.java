/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.diff.DiffPlugin;
import org.eclipse.emf.compare.diff.api.IDiffEngine;
import org.eclipse.emf.compare.diff.engine.GenericDiffEngine;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.util.EMFCompareMap;

/**
 * Parses extension meta data to fetch the diff engine to use.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class DiffService {
	/** Wild card for file extensions. */
	private static final String ALL_EXTENSIONS = "*"; //$NON-NLS-1$

	/** Name of the extension point to parse for engines. */
	private static final String DIFF_ENGINES_EXTENSION_POINT = "org.eclipse.emf.compare.diff.engine"; //$NON-NLS-1$

	/** Keeps track of all the diff extensions we've parsed. */
	private static final Map<String, ArrayList<DiffExtensionDescriptor>> PARSED_DIFF_EXTENSIONS = new EMFCompareMap<String, ArrayList<DiffExtensionDescriptor>>();

	/** Keeps track of all the engines we've parsed. */
	private static final Map<String, ArrayList<EngineDescriptor>> PARSED_ENGINES = new EMFCompareMap<String, ArrayList<EngineDescriptor>>();

	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_DIFF_EXTENSION = "diff_extension"; //$NON-NLS-1$

	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_ENGINE = "diffengine"; //$NON-NLS-1$

	static {
		parseExtensionMetadata();
	}

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private DiffService() {
		// prevents instantiation
	}

	/**
	 * Return a diffmodel created using the match model. This implementation is a generic and simple one.
	 * 
	 * @param match
	 *            The matching model.
	 * @return The corresponding diff model.
	 */
	public static DiffModel doDiff(MatchModel match) {
		return doDiff(match, false);
	}

	/**
	 * Return a diffmodel created using the match model. This implementation is a generic and simple one.
	 * 
	 * @param match
	 *            the matching model
	 * @param threeWay
	 *            <code>True</code> if we're computing a three way comparison, <code>False</code>
	 *            otherwise.
	 * @return the corresponding diff model
	 */
	public static DiffModel doDiff(MatchModel match, boolean threeWay) {
		final String extension = match.getLeftModel().substring(match.getLeftModel().lastIndexOf(".") + 1); //$NON-NLS-1$
		final IDiffEngine engine = getBestDiffEngine(extension);
		return engine.doDiff(match, threeWay);
	}

	/**
	 * Returns the best {@link IDiffEngine} for a file extension.
	 * 
	 * @param extension
	 *            The extension of the file we need a {@link IDiffEngine} for.
	 * @return The best {@link IDiffEngine} for the given file extension.
	 */
	public static IDiffEngine getBestDiffEngine(String extension) {
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			final EngineDescriptor desc = getBestDescriptor(extension);
			return desc.getEngineInstance();
		}
		return new GenericDiffEngine();
	}

	/**
	 * TODOCBR comment.
	 * 
	 * @param extension
	 *            comment
	 * @return comment
	 */
	public static Collection<AbstractDiffExtension> getCorrespondingDiffExtensions(String extension) {
		final Collection<AbstractDiffExtension> result = new ArrayList<AbstractDiffExtension>();
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			if (PARSED_DIFF_EXTENSIONS.containsKey(ALL_EXTENSIONS)) {
				for (DiffExtensionDescriptor extensionDesc : PARSED_DIFF_EXTENSIONS.get(ALL_EXTENSIONS)) {
					result.add(extensionDesc.getDiffExtensionInstance());
				}
			}
			final Collection<DiffExtensionDescriptor> descs = PARSED_DIFF_EXTENSIONS.get(extension);
			if (descs != null) {
				for (DiffExtensionDescriptor desc : descs) {
					result.add(desc.getDiffExtensionInstance());
				}
			}
		}
		return result;
	}

	/**
	 * Returns the best {@link EngineDescriptor}.
	 * 
	 * @param extension
	 *            The file extension we need a diff engine for.
	 * @return The best {@link EngineDescriptor}.
	 */
	private static EngineDescriptor getBestDescriptor(String extension) {
		EngineDescriptor descriptor = null;
		if (PARSED_ENGINES.containsKey(extension)) {
			descriptor = getHighestDescriptor(PARSED_ENGINES.get(extension));
		} else if (PARSED_ENGINES.containsKey(ALL_EXTENSIONS)) {
			descriptor = getHighestDescriptor(PARSED_ENGINES.get(ALL_EXTENSIONS));
		}
		return descriptor;
	}

	/**
	 * Returns the highest {@link EngineDescriptor} from the given {@link List}.
	 * 
	 * @param set
	 *            {@link List} of {@link EngineDescriptor} from which to find the highest one.
	 * @return The highest {@link EngineDescriptor} from the given {@link List}.
	 */
	private static EngineDescriptor getHighestDescriptor(List<EngineDescriptor> set) {
		Collections.sort(set, Collections.reverseOrder());
		if (set.size() > 0)
			return set.get(0);
		return null;
	}

	/**
	 * This will parse the given {@link IConfigurationElement configuration element} and return a descriptor
	 * for it if it describes a DiffExtension.
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link DiffExtensionDescriptor} wrapped around <code>configElement</code> if it describes an
	 *         diff extension, <code>null</code> otherwise.
	 */
	private static DiffExtensionDescriptor parseDiffExtension(IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_DIFF_EXTENSION))
			return null;
		final DiffExtensionDescriptor desc = new DiffExtensionDescriptor(configElement);
		return desc;
	}

	/**
	 * This will parse the given {@link IConfigurationElement configuration element} and return a descriptor
	 * for it if it describes an engine.
	 * 
	 * @param configElement
	 *            Configuration element to parse.
	 * @return {@link EngineDescriptor} wrapped around <code>configElement</code> if it describes an engine,
	 *         <code>null</code> otherwise.
	 */
	private static EngineDescriptor parseEngine(IConfigurationElement configElement) {
		if (!configElement.getName().equals(TAG_ENGINE))
			return null;
		final EngineDescriptor desc = new EngineDescriptor(configElement);
		return desc;
	}

	/**
	 * This will parse the currently running platform for extensions and store all the diff engines and diff
	 * extensions that can be found.
	 */
	private static void parseExtensionMetadata() {
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
					DIFF_ENGINES_EXTENSION_POINT).getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++) {
					final EngineDescriptor desc = parseEngine(configElements[j]);
					storeEngineDescriptor(desc);
				}
			}

			/*
			 * Now parsing the diff extension extension point
			 */
			extensions = Platform.getExtensionRegistry().getExtensionPoint(DiffPlugin.PLUGIN_ID,
					TAG_DIFF_EXTENSION).getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++) {
					final DiffExtensionDescriptor desc = parseDiffExtension(configElements[j]);
					storeDiffExtensionDescriptor(desc);
				}
			}
		}
	}

	/**
	 * Stores the given descriptor in the {@link List} of known {@link DiffExtensionDescriptor}s.
	 * 
	 * @param desc
	 *            Descriptor to be added to the list of all know descriptors.
	 */
	private static void storeDiffExtensionDescriptor(DiffExtensionDescriptor desc) {
		if (desc.getFileExtension() == null)
			return;

		final String[] extensions = desc.getFileExtension().split(","); //$NON-NLS-1$
		for (String engineExtension : extensions) {
			if (!PARSED_DIFF_EXTENSIONS.containsKey(engineExtension)) {
				PARSED_DIFF_EXTENSIONS.put(engineExtension, new ArrayList<DiffExtensionDescriptor>());
			}
			final List<DiffExtensionDescriptor> set = PARSED_DIFF_EXTENSIONS.get(engineExtension);
			set.add(desc);
		}
	}

	/**
	 * Stores the given descriptor in the {@link List} of known {@link EngineDescriptor}s.
	 * 
	 * @param desc
	 *            Descriptor to be added to the list of all know descriptors.
	 */
	private static void storeEngineDescriptor(EngineDescriptor desc) {
		if (desc.getFileExtension() == null)
			return;

		final String[] extensions = desc.getFileExtension().split(","); //$NON-NLS-1$
		for (String engineExtension : extensions) {
			if (!PARSED_ENGINES.containsKey(engineExtension)) {
				PARSED_ENGINES.put(engineExtension, new ArrayList<EngineDescriptor>());
			}
			final List<EngineDescriptor> set = PARSED_ENGINES.get(engineExtension);
			set.add(desc);
		}
	}
}
