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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.api.IMatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.util.EMFComparePreferenceKeys;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Service facade for matching models.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class MatchService {
	/** Default extension for EObjects not attached to a resource. */
	private static final String DEFAULT_EXTENSION = "ecore"; //$NON-NLS-1$

	/** Currently set match engine selector. */
	private static IMatchEngineSelector matchEngineSelector = new DefaultMatchEngineSelector();

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
	 *            Left (local) of the three objects to get compared.
	 * @param rightObject
	 *            Right (latest from repository) of the three objects to compare.
	 * @param ancestor
	 *            Common ancestor of the two others.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or an empty map will result in
	 *            the default options to be used.
	 * @return {@link MatchModel} for these three objects' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 * @see IMatchEngine#contentMatch(EObject, EObject, EObject, Map)
	 * @since 0.9.0
	 */
	public static MatchModel doContentMatch(EObject leftObject, EObject rightObject, EObject ancestor,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftObject.eResource(), rightObject.eResource(), ancestor
				.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		final MatchModel result = engine.contentMatch(leftObject, rightObject, ancestor, options);
		engine.reset();
		return result;
	}

	/**
	 * Matches two objects along with their content, then return the corresponding match model.
	 * 
	 * @param leftObject
	 *            Left (local) of the two objects to get compared.
	 * @param rightObject
	 *            Right (latest from repository) of the two objects to compare.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or an empty map will result in
	 *            the default options to be used.
	 * @return {@link MatchModel} for these two objects' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 * @see IMatchEngine#contentMatch(EObject, EObject, Map)
	 * @since 0.9.0
	 */
	public static MatchModel doContentMatch(EObject leftObject, EObject rightObject,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftObject.eResource(), rightObject.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		final MatchModel result = engine.contentMatch(leftObject, rightObject, options);
		engine.reset();
		return result;
	}

	/**
	 * Matches three models and returns the corresponding matching model.
	 * 
	 * @param leftRoot
	 *            Left (local) model of this comparison.
	 * @param rightRoot
	 *            Right (latest from repository) model of this comparison.
	 * @param ancestor
	 *            Common ancestor of <code>leftRoot</code> and <code>rightRoot</code>.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or an empty map will result in
	 *            the default options to be used.
	 * @return Matching model result of the comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 */
	public static MatchModel doMatch(EObject leftRoot, EObject rightRoot, EObject ancestor,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftRoot.eResource(), rightRoot.eResource(), ancestor
				.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		final MatchModel result = engine.modelMatch(leftRoot, rightRoot, ancestor, options);
		engine.reset();
		return result;
	}

	/**
	 * Matches two models and returns the corresponding matching model.
	 * 
	 * @param leftRoot
	 *            Left (local) model of the comparison.
	 * @param rightRoot
	 *            Right (latest from repository) model of the comparison.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or an empty map will result in
	 *            the default options to be used.
	 * @return Matching model result of these two models' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 */
	public static MatchModel doMatch(EObject leftRoot, EObject rightRoot, Map<String, Object> options)
			throws InterruptedException {
		final String extension = getBestExtension(leftRoot.eResource(), rightRoot.eResource());
		final IMatchEngine engine = getBestMatchEngine(extension);
		final MatchModel result = engine.modelMatch(leftRoot, rightRoot, options);
		engine.reset();
		return result;
	}

	/**
	 * Matches two resources along with their content, then return the corresponding match model.
	 * 
	 * @param leftResource
	 *            Left (local) of the two resources to get compared.
	 * @param rightResource
	 *            Right (latest from repository) of the two resources to compare.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or an empty map will result in
	 *            the default options to be used.
	 * @return {@link MatchModel} for these two resources' comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 * @see IMatchEngine#contentMatch(EObject, EObject, Map)
	 * @since 0.9.0
	 */
	public static MatchModel doResourceMatch(Resource leftResource, Resource rightResource,
			Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftResource, rightResource);
		final IMatchEngine engine = getBestMatchEngine(extension);
		final MatchModel result = engine.resourceMatch(leftResource, rightResource, options);
		engine.reset();
		return result;
	}

	/**
	 * Matches three resources and returns the corresponding matching model.
	 * 
	 * @param leftResource
	 *            Left (local) resource of this comparison.
	 * @param rightResource
	 *            Right (latest from repository) resource of this comparison.
	 * @param ancestorResource
	 *            Common ancestor of <code>leftResource</code> and <code>rightResource</code>.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or an empty map will result in
	 *            the default options to be used.
	 * @return Matching model result of the comparison.
	 * @throws InterruptedException
	 *             Thrown if the matching is interrupted somehow.
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 */
	public static MatchModel doResourceMatch(Resource leftResource, Resource rightResource,
			Resource ancestorResource, Map<String, Object> options) throws InterruptedException {
		final String extension = getBestExtension(leftResource, rightResource, ancestorResource);
		final IMatchEngine engine = getBestMatchEngine(extension);
		final MatchModel result = engine
				.resourceMatch(leftResource, rightResource, ancestorResource, options);
		engine.reset();
		return result;
	}

	/**
	 * Returns the best {@link IMatchEngine} for a file given its extension.
	 * 
	 * @param extension
	 *            The extension of the file we need a {@link IMatchEngine} for.
	 * @return The best {@link IMatchEngine} for the given file extension.
	 */
	public static IMatchEngine getBestMatchEngine(String extension) {
		if (EMFPlugin.IS_ECLIPSE_RUNNING
				&& EMFComparePlugin.getDefault().getBoolean(
						EMFComparePreferenceKeys.PREFERENCES_KEY_ENGINE_SELECTION)) {
			final EngineDescriptor desc = getBestDescriptor(extension);
			return desc.getEngineInstance();
		}
		return EngineRegistry.INSTANCE.getHighestEngine(extension);
	}

	/**
	 * Sets the match engine selector that is to be used.
	 * 
	 * @param selector
	 *            the new engine selector.
	 */
	public static void setMatchEngineSelector(IMatchEngineSelector selector) {
		matchEngineSelector = selector;
	}

	/**
	 * Returns the best {@link EngineDescriptor} for a given file extension.
	 * 
	 * @param extension
	 *            The file extension we need a match engine for.
	 * @return The best {@link EngineDescriptor}.
	 */
	private static EngineDescriptor getBestDescriptor(String extension) {
		final List<EngineDescriptor> engines = EngineRegistry.INSTANCE.getDescriptors(extension);
		EngineDescriptor engine = null;
		if (engines.size() == 1) {
			engine = engines.iterator().next();
		} else if (engines.size() > 1) {
			engine = matchEngineSelector.selectMatchEngine(engines);
		}

		return engine;
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
				if (extension == null) {
					extension = resources[i].getURI().fileExtension();
				} else if (!extension.equals(resources[i].getURI().fileExtension())) {
					extension = DEFAULT_EXTENSION;
					break;
				}
			}
		}
		return extension;
	}
}
