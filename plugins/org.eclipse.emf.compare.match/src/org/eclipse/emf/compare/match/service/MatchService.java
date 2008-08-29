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
package org.eclipse.emf.compare.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.match.api.IMatchEngine;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Service facade for matching models.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class MatchService {
	/** Wild card for file extensions. */
	private static final String ALL_EXTENSIONS = "*"; //$NON-NLS-1$

	/** Default extension for EObjects not attached to a resource. */
	private static final String DEFAULT_EXTENSION = "ecore"; //$NON-NLS-1$

	/** Name of the extension point to parse for engines. */
	private static final String MATCH_ENGINES_EXTENSION_POINT = "org.eclipse.emf.compare.match.engine"; //$NON-NLS-1$

	/** Keeps track of all the engines parsed. */
	private static final Map<String, ArrayList<EngineDescriptor>> PARSED_ENGINES = new EMFCompareMap<String, ArrayList<EngineDescriptor>>();

	/** Externalized here to avoid too many distinct usages. */
	private static final String TAG_ENGINE = "matchengine"; //$NON-NLS-1$

	static {
		parseExtensionMetadata();
	}

	/**
	 * Utility classes don't need to (and shouldn't) be instantiated.
	 */
	private MatchService() {
		// prevents instantiation
	}

	/**
	 * Matches three objects along with their content, then return the corresponding match model.
	 * 
	 * @param leftObject
	 *            Left of the three objects to get compared.
	 * @param rightObject
	 *            Right of the three objects to compare.
	 * @param ancestor
	 *            Common ancestor of the two others.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return {@link MatchModel} for these three objects' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see MatchOptions
	 * @see IMatchEngine#contentMatch(EObject, EObject, EObject, Map)
	 * @since 0.9.0
	 */
	public static MatchModel doContentMatch(EObject leftObject, EObject rightObject, EObject ancestor,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftObject.eResource(), rightObject.eResource(), ancestor
				.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		return engine.contentMatch(leftObject, rightObject, ancestor, options);
	}

	/**
	 * Matches two objects along with their content, then return the corresponding match model.
	 * 
	 * @param leftObject
	 *            Left of the two objects to get compared.
	 * @param rightObject
	 *            Right of the two objects to compare.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return {@link MatchModel} for these two objects' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see MatchOptions
	 * @see IMatchEngine#contentMatch(EObject, EObject, Map)
	 * @since 0.9.0
	 */
	public static MatchModel doContentMatch(EObject leftObject, EObject rightObject,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftObject.eResource(), rightObject.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		return engine.contentMatch(leftObject, rightObject, options);
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
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return Matching model result of the comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see MatchOptions
	 */
	public static MatchModel doMatch(EObject leftRoot, EObject rightRoot, EObject ancestor,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftRoot.eResource(), rightRoot.eResource(), ancestor
				.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		return engine.modelMatch(leftRoot, rightRoot, ancestor, options);
	}

	/**
	 * Matches two models and returns the corresponding matching model.
	 * 
	 * @param leftRoot
	 *            Left model of the comparison.
	 * @param rightRoot
	 *            Right model of the comparison.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return Matching model result of these two models' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see MatchOptions
	 */
	public static MatchModel doMatch(EObject leftRoot, EObject rightRoot, Map<String, Object> options)
			throws InterruptedException {
		final String extension = getBestExtension(leftRoot.eResource(), rightRoot.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		return engine.modelMatch(leftRoot, rightRoot, options);
	}

	/**
	 * Matches two resources along with their content, then return the corresponding match model.
	 * 
	 * @param leftResource
	 *            Left of the two resources to get compared.
	 * @param rightResource
	 *            Right of the two resources to compare.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return {@link MatchModel} for these two resources' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see MatchOptions
	 * @see IMatchEngine#contentMatch(EObject, EObject, Map)
	 * @since 0.9.0
	 */
	public static MatchModel doResourceMatch(Resource leftResource, Resource rightResource,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftResource, rightResource);
		final IMatchEngine engine = getBestMatchEngine(extension);
		return engine.resourceMatch(leftResource, rightResource, options);
	}

	/**
	 * Matches three resources and returns the corresponding matching model.
	 * 
	 * @param leftResource
	 *            Left resource of this comparison.
	 * @param rightResource
	 *            Right resource of this comparison.
	 * @param ancestorResource
	 *            Common ancestor of <code>leftResource</code> and <code>rightResource</code>.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return Matching model result of the comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see MatchOptions
	 */
	public static MatchModel doResourceMatch(Resource leftResource, Resource rightResource,
			Resource ancestorResource, Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftResource, rightResource, ancestorResource);
		final IMatchEngine engine = getBestMatchEngine(extension);
		return engine.resourceMatch(leftResource, rightResource, ancestorResource, options);
	}

	/**
	 * Returns the best {@link IMatchEngine} for a file given its extension.
	 * 
	 * @param extension
	 *            The extension of the file we need a {@link IMatchEngine} for.
	 * @return The best {@link IMatchEngine} for the given file extension.
	 */
	public static IMatchEngine getBestMatchEngine(String extension) {
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			final EngineDescriptor desc = getBestDescriptor(extension);
			return desc.getEngineInstance();
		}
		return new GenericMatchEngine();
	}

	/**
	 * Returns the best {@link EngineDescriptor} for a given file extension.
	 * 
	 * @param extension
	 *            The file extension we need a match engine for.
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
	 * This will try and find the file extension of the compared models.
	 * <p>
	 * When the two extensions are distinct or empty, {@link #DEFAULT_EXTENSION} will be returned.
	 * </p>
	 * 
	 * @param resources
	 *            The Resources that will be compared.
	 * @return The file extension to consider when searching for a match engine.
	 */
	private static String getBestExtension(Resource... resources) {
		String extension = null;
		for (int i = 0; i < resources.length; i++) {
			if (resources[i].getURI() != null) {
				if (extension == null)
					extension = resources[i].getURI().fileExtension();
				else if (!extension.equals(resources[i].getURI().fileExtension())) {
					extension = DEFAULT_EXTENSION;
					break;
				}
			}
		}
		return extension;
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
	 * for it if it describes and engine.
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
	 * This will parse the currently running platform for extensions and store all the match engines that can
	 * be found.
	 */
	private static void parseExtensionMetadata() {
		if (EMFPlugin.IS_ECLIPSE_RUNNING) {
			final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(
					MATCH_ENGINES_EXTENSION_POINT).getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				final IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++) {
					final EngineDescriptor desc = parseEngine(configElements[j]);
					storeEngineDescriptor(desc);
				}
			}
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
