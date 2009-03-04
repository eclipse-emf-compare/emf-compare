/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.engine.IMatchEngine;
import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.compare.match.filter.ResourceFilterRegistry;
import org.eclipse.emf.compare.match.internal.service.DefaultMatchEngineSelector;
import org.eclipse.emf.compare.match.internal.statistic.ResourceSimilarity;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.metamodel.Side;
import org.eclipse.emf.compare.match.metamodel.UnmatchModel;
import org.eclipse.emf.compare.util.EMFComparePreferenceKeys;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Service facade for matching models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class MatchService {
	/** Default extension for EObjects not attached to a resource. */
	private static final String DEFAULT_EXTENSION = "ecore"; //$NON-NLS-1$

	/** Keeps track of those resources that are loaded as fragments of others. */
	private static final Set<Resource> FRAGMENT_RESOURCES = new HashSet<Resource>();

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
	 * @see org.eclipse.emf.compare.match.MatchOptions
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
	 * @see org.eclipse.emf.compare.match.MatchOptions
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
	 * @see org.eclipse.emf.compare.match.MatchOptions
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
	 * @see org.eclipse.emf.compare.match.MatchOptions
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
	 * @see org.eclipse.emf.compare.match.MatchOptions
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
	 * @see org.eclipse.emf.compare.match.MatchOptions
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
	 * Matches the resources contained by two resourceSets and return all corresponding MatchModels.
	 * 
	 * @param leftResourceSet
	 *            ResourceSet of the left compared Resource.
	 * @param rightResourceSet
	 *            ResourceSet of the right compared Resource.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link java.util.Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return {@link MatchResourceSet} containing all corresponding {@link MatchModel}s.
	 * @throws InterruptedException
	 *             Thrown if the options map specifies a progress monitor, and the comparison gets interrupted
	 *             somehow.
	 * @see org.eclipse.emf.compare.match.MatchOptions
	 * @since 0.9.0
	 */
	@SuppressWarnings("unchecked")
	public static MatchResourceSet doResourceSetMatch(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet, Map<String, Object> options) throws InterruptedException {
		// Resolve all proxies so that all resources get loaded
		resolveAll(leftResourceSet);
		resolveAll(rightResourceSet);

		final List<Resource> remainingLeftResources = new ArrayList<Resource>(leftResourceSet.getResources());
		final List<Resource> remainingRightResources = new ArrayList<Resource>(rightResourceSet
				.getResources());

		// Removes fragments from the resources to match
		removeFragments(remainingLeftResources, remainingRightResources);
		// filters out resources if any client extends the filtering extension point
		filterResources(remainingLeftResources, remainingRightResources);

		final MatchResourceSet match = MatchFactory.eINSTANCE.createMatchResourceSet();
		for (final Resource res : new ArrayList<Resource>(remainingLeftResources)) {
			final Resource matchedResource = findMatchingResource(res, remainingRightResources);
			if (matchedResource != null
					&& findMatchingResource(matchedResource, remainingLeftResources) == res) {
				remainingLeftResources.remove(res);
				remainingRightResources.remove(matchedResource);
				match.getMatchModels().add(doResourceMatch(res, matchedResource, options));
			}
		}
		/*
		 * Tries matching remaining resources with a second pass. All unmatched are considered to have no
		 * counterpart in the second resourceSet.
		 */
		for (final Resource res : new ArrayList<Resource>(remainingLeftResources)) {
			final Resource matchedResource = findMatchingResource(res, remainingRightResources);
			if (matchedResource != null
					&& findMatchingResource(matchedResource, remainingLeftResources) == res) {
				remainingLeftResources.remove(res);
				remainingRightResources.remove(matchedResource);
				match.getMatchModels().add(doResourceMatch(res, matchedResource, options));
			} else {
				final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
				unmatched.setSide(Side.LEFT);
				unmatched.getRoots().addAll(res.getContents());
				remainingLeftResources.remove(res);
				match.getUnmatchedModels().add(unmatched);
			}
		}
		for (final Resource res : new ArrayList<Resource>(remainingRightResources)) {
			final Resource matchedResource = findMatchingResource(res, remainingLeftResources);
			if (matchedResource != null
					&& findMatchingResource(matchedResource, remainingRightResources) == res) {
				remainingLeftResources.remove(matchedResource);
				remainingRightResources.remove(res);
				match.getMatchModels().add(doResourceMatch(matchedResource, res, options));
			} else {
				final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
				unmatched.setSide(Side.RIGHT);
				unmatched.getRoots().addAll(res.getContents());
				remainingLeftResources.remove(res);
				match.getUnmatchedModels().add(unmatched);
			}
		}

		return match;
	}

	/**
	 * Matches the resources contained by three resourceSets and return all corresponding MatchModels.
	 * 
	 * @param leftResourceSet
	 *            ResourceSet of the left compared Resource.
	 * @param rightResourceSet
	 *            ResourceSet of the right compared Resource.
	 * @param ancestorResourceSet
	 *            resourceSet containing the common ancestor of the two compared resources.
	 * @param options
	 *            Options to tweak the matching procedure. <code>null</code> or
	 *            {@link java.util.Collections#EMPTY_MAP} will result in the default options to be used.
	 * @return {@link MatchResourceSet} containing all corresponding {@link MatchModel}s.
	 * @throws InterruptedException
	 *             Thrown if the options map specifies a progress monitor, and the comparison gets interrupted
	 *             somehow.
	 * @see org.eclipse.emf.compare.match.MatchOptions
	 * @since 0.9.0
	 */
	@SuppressWarnings("unchecked")
	public static MatchResourceSet doResourceSetMatch(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet, ResourceSet ancestorResourceSet, Map<String, Object> options)
			throws InterruptedException {
		// Resolve all proxies so that all resources get loaded
		resolveAll(leftResourceSet);
		resolveAll(rightResourceSet);
		resolveAll(ancestorResourceSet);

		final List<Resource> remainingLeftResources = new ArrayList<Resource>(leftResourceSet.getResources());
		final List<Resource> remainingRightResources = new ArrayList<Resource>(rightResourceSet
				.getResources());
		final List<Resource> remainingAncestorResources = new ArrayList<Resource>(ancestorResourceSet
				.getResources());

		// Removes fragments from the resources to match
		removeFragments(remainingLeftResources, remainingRightResources, remainingAncestorResources);
		// filters out resources if any client extends the filtering extension point
		filterResources(remainingLeftResources, remainingRightResources, remainingAncestorResources);

		final MatchResourceSet match = MatchFactory.eINSTANCE.createMatchResourceSet();
		for (final Resource res : new ArrayList<Resource>(remainingLeftResources)) {
			final Resource matchedRight = findMatchingResource(res, remainingRightResources);
			final Resource matchedAncestor = findMatchingResource(res, remainingAncestorResources);
			if (matchedRight != null && findMatchingResource(matchedRight, remainingLeftResources) == res
					&& matchedAncestor != null
					&& findMatchingResource(matchedAncestor, remainingLeftResources) == res) {
				remainingLeftResources.remove(res);
				remainingRightResources.remove(matchedRight);
				remainingAncestorResources.remove(matchedAncestor);
				match.getMatchModels().add(doResourceMatch(res, matchedRight, matchedAncestor, options));
			}
		}
		/*
		 * Tries matching remaining resources with a second pass. All unmatched are considered to have no
		 * counterpart in the second resourceSet.
		 */
		for (final Resource res : new ArrayList<Resource>(remainingLeftResources)) {
			final Resource matchedRight = findMatchingResource(res, remainingRightResources);
			final Resource matchedAncestor = findMatchingResource(res, remainingAncestorResources);
			if (matchedRight != null && findMatchingResource(matchedRight, remainingLeftResources) == res) {
				remainingLeftResources.remove(res);
				remainingRightResources.remove(matchedRight);
				if (matchedAncestor != null
						&& findMatchingResource(matchedAncestor, remainingLeftResources) == res) {
					remainingAncestorResources.remove(matchedAncestor);
					match.getMatchModels().add(doResourceMatch(res, matchedRight, matchedAncestor, options));
				} else {
					match.getMatchModels().add(doResourceMatch(res, matchedRight, options));
				}
			} else {
				remainingLeftResources.remove(res);
				if (matchedAncestor != null
						&& findMatchingResource(matchedAncestor, remainingLeftResources) == res) {
					final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
					unmatched.setSide(Side.LEFT);
					unmatched.getRoots().addAll(res.getContents());
					unmatched.setRemote(true);
					remainingAncestorResources.remove(matchedAncestor);
					match.getUnmatchedModels().add(unmatched);
				} else {
					final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
					unmatched.setSide(Side.LEFT);
					unmatched.getRoots().addAll(res.getContents());
					match.getUnmatchedModels().add(unmatched);
				}
			}
		}
		for (final Resource res : new ArrayList<Resource>(remainingRightResources)) {
			final Resource matchedLeft = findMatchingResource(res, remainingLeftResources);
			final Resource matchedAncestor = findMatchingResource(res, remainingAncestorResources);
			if (matchedLeft != null && findMatchingResource(matchedLeft, remainingRightResources) == res) {
				remainingLeftResources.remove(matchedLeft);
				remainingRightResources.remove(res);
				if (matchedAncestor != null
						&& findMatchingResource(matchedAncestor, remainingLeftResources) == res) {
					remainingAncestorResources.remove(matchedAncestor);
					match.getMatchModels().add(doResourceMatch(matchedLeft, res, matchedAncestor, options));
				} else {
					match.getMatchModels().add(doResourceMatch(matchedLeft, res, options));
				}
			} else {
				remainingRightResources.remove(res);
				if (matchedAncestor != null
						&& findMatchingResource(matchedAncestor, remainingLeftResources) == res) {
					final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
					unmatched.setSide(Side.RIGHT);
					unmatched.getRoots().addAll(res.getContents());
					unmatched.setRemote(true);
					remainingAncestorResources.remove(matchedAncestor);
					match.getUnmatchedModels().add(unmatched);
				} else {
					final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
					unmatched.setSide(Side.RIGHT);
					unmatched.getRoots().addAll(res.getContents());
					match.getUnmatchedModels().add(unmatched);
				}
			}
		}

		return match;
	}

	/**
	 * Returns the best {@link IMatchEngine} for a file given its extension.
	 * 
	 * @param extension
	 *            The extension of the file we need an {@link IMatchEngine} for.
	 * @return The best {@link IMatchEngine} for the given file extension.
	 */
	public static IMatchEngine getBestMatchEngine(String extension) {
		if (EMFPlugin.IS_ECLIPSE_RUNNING
				&& EMFComparePlugin.getDefault().getBoolean(
						EMFComparePreferenceKeys.PREFERENCES_KEY_ENGINE_SELECTION)) {
			final MatchEngineDescriptor desc = getBestDescriptor(extension);
			return desc.getEngineInstance();
		}
		return MatchEngineRegistry.INSTANCE.getHighestEngine(extension);
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
	 * Remove all fragment resources from the given resources lists.
	 * 
	 * @param resources
	 *            Lists that are to be cleared off fragments.
	 */
	private static void filterResources(List<Resource>... resources) {
		for (final IResourceFilter filter : ResourceFilterRegistry.INSTANCE.getRegisteredResourceFilters()) {
			if (resources.length == 2) {
				filter.filter(resources[0], resources[1]);
			} else {
				filter.filter(resources[0], resources[1], resources[2]);
			}
		}
	}

	/**
	 * Returns the best {@link MatchEngineDescriptor} for a given file extension.
	 * 
	 * @param extension
	 *            The file extension we need a match engine for.
	 * @return The best {@link MatchEngineDescriptor}.
	 */
	private static MatchEngineDescriptor getBestDescriptor(String extension) {
		final List<MatchEngineDescriptor> engines = MatchEngineRegistry.INSTANCE.getDescriptors(extension);
		MatchEngineDescriptor engine = null;
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
			if (resources[i] == null) {
				extension = DEFAULT_EXTENSION;
				break;
			} else if (resources[i].getURI() != null) {
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

	/**
	 * Remove all fragment resources from the given resources lists.
	 * 
	 * @param resources
	 *            Lists that are to be cleared off fragments.
	 */
	private static void removeFragments(List<Resource>... resources) {
		for (final Resource resource : FRAGMENT_RESOURCES) {
			for (final List<Resource> res : resources) {
				res.remove(resource);
			}
		}
		FRAGMENT_RESOURCES.clear();
	}

	/**
	 * This will allow us to resolve all references from resources contained within <code>resourceSet</code>,
	 * loading referenced resources along the way as would
	 * {@link org.eclipse.emf.ecore.util.EcoreUtil#resolveAll(ResourceSet)}. The difference lies in the fact
	 * we will populate {@link #FRAGMENT_RESOURCES} so as to keep track of the resources loaded as fragments
	 * of others.
	 * 
	 * @param resourceSet
	 *            The resourceSet we wish all references of resolved.
	 */
	private static void resolveAll(ResourceSet resourceSet) {
		final List<Resource> resources = resourceSet.getResources();
		for (int i = 0; i < resources.size(); ++i) {
			final Iterator<EObject> resourceContent = resources.get(i).getAllContents();
			while (resourceContent.hasNext()) {
				final EObject eObject = resourceContent.next();
				final Resource childResource = eObject.eResource();
				if (childResource != null && childResource != resources.get(i)) {
					FRAGMENT_RESOURCES.add(childResource);
				}
				final Iterator<EObject> objectChildren = eObject.eCrossReferences().iterator();
				while (objectChildren.hasNext()) {
					// Resolves cross references by simply visiting them.
					objectChildren.next();
				}
			}
		}
	}

	/**
	 * This will try and find a resource in <code>candidates</code> similar to <code>resource</code>.
	 * 
	 * @param resource
	 *            The resource we seek a similar to in the given resourceSet.
	 * @param candidates
	 *            candidate resources.
	 * @return The most similar resource to <code>resource</code> we could find in <code>resourceSet</code>.
	 */
	public static Resource findMatchingResource(Resource resource, List<Resource> candidates) {
		return ResourceSimilarity.findMatchingResource(resource, candidates);
	}
}
