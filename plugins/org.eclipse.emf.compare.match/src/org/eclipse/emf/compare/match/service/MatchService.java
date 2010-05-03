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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.engine.IMatchEngine;
import org.eclipse.emf.compare.match.engine.IMatchScope;
import org.eclipse.emf.compare.match.engine.IMatchScopeProvider;
import org.eclipse.emf.compare.match.engine.MatchScopeProviderUtil;
import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.compare.match.filter.ResourceFilterRegistry;
import org.eclipse.emf.compare.match.internal.service.DefaultMatchEngineSelector;
import org.eclipse.emf.compare.match.internal.statistic.ResourceSimilarity;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.metamodel.Side;
import org.eclipse.emf.compare.match.metamodel.UnmatchModel;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.emf.compare.util.ModelIdentifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Service facade for matching models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class MatchService {

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
	 * @since 1.0
	 */
	public static MatchModel doContentMatch(EObject leftObject, EObject rightObject, EObject ancestor,
			Map<String, Object> options) throws InterruptedException {
		final IMatchEngine engine = getBestMatchEngine(leftObject.eResource(), rightObject.eResource(),
				ancestor.eResource());
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
	 * @since 1.0
	 */
	public static MatchModel doContentMatch(EObject leftObject, EObject rightObject,
			Map<String, Object> options) throws InterruptedException {
		final IMatchEngine engine = getBestMatchEngine(leftObject.eResource(), rightObject.eResource());
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
		final IMatchEngine engine = getBestMatchEngine(leftRoot.eResource(), rightRoot.eResource(), ancestor
				.eResource());
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
		final IMatchEngine engine = getBestMatchEngine(leftRoot.eResource(), rightRoot.eResource());
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
	 * @since 1.0
	 */
	public static MatchModel doResourceMatch(Resource leftResource, Resource rightResource,
			Map<String, Object> options) throws InterruptedException {
		final IMatchEngine engine = getBestMatchEngine(leftResource, rightResource);
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
		final IMatchEngine engine = getBestMatchEngine(leftResource, rightResource, ancestorResource);
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
	 * @since 1.0
	 */
	public static MatchResourceSet doResourceSetMatch(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet, Map<String, Object> options) throws InterruptedException {

		// Resolve all proxies so that all resources get loaded
		resolveAll(leftResourceSet);
		resolveAll(rightResourceSet);

		// see if scope provider was passed in via option, otherwise create default one
		final IMatchScopeProvider scopeProvider = MatchScopeProviderUtil.getScopeProvider(options,
				leftResourceSet, rightResourceSet);
		final IMatchScope leftScope = scopeProvider.getLeftScope();
		final IMatchScope rightScope = scopeProvider.getRightScope();

		applyExternalFilter(scopeProvider);

		final List<Resource> remainingLeftResources = new ArrayList<Resource>(leftResourceSet.getResources());
		final List<Resource> remainingRightResources = new ArrayList<Resource>(rightResourceSet
				.getResources());

		applyScopeFilter(leftScope, remainingLeftResources);
		applyScopeFilter(rightScope, remainingRightResources);

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
				unmatched.getRoots().addAll(getResourceRoots(res));
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
				unmatched.getRoots().addAll(getResourceRoots(res));
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
	 * @since 1.0
	 */
	public static MatchResourceSet doResourceSetMatch(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet, ResourceSet ancestorResourceSet, Map<String, Object> options)
			throws InterruptedException {
		resolveAll(leftResourceSet);
		resolveAll(rightResourceSet);
		resolveAll(ancestorResourceSet);

		// apply filter to scope provider, then filter resources with provided scopes
		final IMatchScopeProvider scopeProvider = MatchScopeProviderUtil.getScopeProvider(options,
				leftResourceSet, rightResourceSet, ancestorResourceSet);
		final IMatchScope leftScope = scopeProvider.getLeftScope();
		final IMatchScope rightScope = scopeProvider.getRightScope();
		final IMatchScope ancestorScope = scopeProvider.getAncestorScope();
		applyExternalFilter(scopeProvider);
		final List<Resource> remainingLeftResources = new ArrayList<Resource>(leftResourceSet.getResources());
		final List<Resource> remainingRightResources = new ArrayList<Resource>(rightResourceSet
				.getResources());
		final List<Resource> remainingAncestorResources = new ArrayList<Resource>(ancestorResourceSet
				.getResources());
		applyScopeFilter(leftScope, remainingLeftResources);
		applyScopeFilter(rightScope, remainingRightResources);
		applyScopeFilter(ancestorScope, remainingAncestorResources);

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
					unmatched.getRoots().addAll(getResourceRoots(res));
					unmatched.setRemote(true);
					remainingAncestorResources.remove(matchedAncestor);
					match.getUnmatchedModels().add(unmatched);
				} else {
					final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
					unmatched.setSide(Side.LEFT);
					unmatched.getRoots().addAll(getResourceRoots(res));
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
					unmatched.getRoots().addAll(getResourceRoots(res));
					unmatched.setRemote(true);
					remainingAncestorResources.remove(matchedAncestor);
					match.getUnmatchedModels().add(unmatched);
				} else {
					final UnmatchModel unmatched = MatchFactory.eINSTANCE.createUnmatchModel();
					unmatched.setSide(Side.RIGHT);
					unmatched.getRoots().addAll(getResourceRoots(res));
					match.getUnmatchedModels().add(unmatched);
				}
			}
		}
		return match;
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

	/**
	 * Returns the best {@link IMatchEngine} for a given list of {@link Resource} to compare.
	 * 
	 * @param resources
	 *            The list of {@link Resource} to compare.
	 * @return The best {@link IMatchEngine} for the given list of {@link Resource}
	 * @since 1.1
	 */
	public static IMatchEngine getBestMatchEngine(Resource... resources) {
		IMatchEngine engine = null;

		final ModelIdentifier identifier = new ModelIdentifier(resources);

		if (EMFPlugin.IS_ECLIPSE_RUNNING
				&& EMFComparePlugin.getDefault().getBoolean(
						EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION)) {
			final List<MatchEngineDescriptor> engines = MatchEngineRegistry.INSTANCE
					.getDescriptors(identifier);

			if (engines.size() == 1) {
				engine = engines.iterator().next().getEngineInstance();
			} else {
				engine = matchEngineSelector.selectMatchEngine(engines).getEngineInstance();
			}
		} else {
			engine = MatchEngineRegistry.INSTANCE.getHighestEngine(identifier);
		}

		return engine;
	}

	/**
	 * Returns the best {@link IMatchEngine} for a file given its extension.
	 * 
	 * @param engineIdentifier
	 *            An engine identifier to search on the registered {@link IMatchEngine}.<br/>
	 *            An engine identifier is a String that can describe either a file extension, a content-type
	 *            or a namespace.
	 * @return The best {@link IMatchEngine} for the given engine identifier.
	 * @deprecated use {@link MatchService#getBestMatchEngine(Resource...)} instead
	 */
	@Deprecated
	public static IMatchEngine getBestMatchEngine(String engineIdentifier) {
		if (EMFPlugin.IS_ECLIPSE_RUNNING
				&& EMFComparePlugin.getDefault().getBoolean(
						EMFComparePreferenceConstants.PREFERENCES_KEY_ENGINE_SELECTION)) {
			final MatchEngineDescriptor desc = getBestDescriptor(engineIdentifier);
			return desc.getEngineInstance();
		}
		return MatchEngineRegistry.INSTANCE.getHighestEngine(engineIdentifier);
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
	 * Apply external filters to the {@link IMatchScopeProvider}.
	 * 
	 * @param scopeProvider
	 *            The {@link IMatchScopeProvider} the external filters are to be applied to
	 */
	private static void applyExternalFilter(IMatchScopeProvider scopeProvider) {
		for (final IResourceFilter filter : ResourceFilterRegistry.INSTANCE.getRegisteredResourceFilters()) {
			scopeProvider.applyResourceFilter(filter);
		}
	}

	/**
	 * Removes all resources from the given list, which are not covered by the provided scope.
	 * 
	 * @param scope
	 *            the {@link IMatchScope} used to determine with resources to retain in the list
	 * @param resources
	 *            the list of {@link Resource}s that has to be filtered
	 */
	private static void applyScopeFilter(IMatchScope scope, List<Resource> resources) {
		final Iterator<Resource> iterator = resources.iterator();
		while (iterator.hasNext()) {
			if (!scope.isInScope(iterator.next())) {
				iterator.remove();
			}
		}
	}

	/**
	 * Returns the best {@link DiffEngineDescriptor} for a given file extension.
	 * 
	 * @param engineIdentifier
	 *            An engine identifier to search on the registered {@link DiffEngineDescriptor}.<br/>
	 *            An engine identifier is a String that can describe either a file extension, a content-type
	 *            or a namespace.
	 * @return The best {@link DiffEngineDescriptor}.
	 */
	@Deprecated
	private static MatchEngineDescriptor getBestDescriptor(String engineIdentifier) {
		final List<MatchEngineDescriptor> engines = MatchEngineRegistry.INSTANCE
				.getDescriptors(engineIdentifier);
		MatchEngineDescriptor engine = null;
		if (engines.size() == 1) {
			engine = engines.iterator().next();
		} else if (engines.size() > 1) {
			engine = matchEngineSelector.selectMatchEngine(engines);
		}

		return engine;
	}

	/**
	 * Returns the roots of the given resource. This will return a proxy to hold the resource's URI if it has
	 * no roots.
	 * 
	 * @param res
	 *            The resource we need the roots of.
	 * @return The roots of the given resource.
	 */
	private static List<EObject> getResourceRoots(Resource res) {
		final List<EObject> roots = new ArrayList<EObject>(res.getContents());
		if (res.getContents().isEmpty()) {
			// The resource has no root ... create a proxy to hold the resource URI
			final EObject proxy = EcoreFactory.eINSTANCE.createEObject();
			((InternalEObject)proxy).eSetProxyURI(res.getURI().appendFragment("/")); //$NON-NLS-1$
			roots.add(proxy);
		}
		return roots;
	}

	/**
	 * This will allow us to resolve all references from resources contained within <code>resourceSet</code>,
	 * loading referenced resources along the way.
	 * 
	 * @param resourceSet
	 *            The resourceSet we wish all references of resolved.
	 */
	private static void resolveAll(ResourceSet resourceSet) {
		final List<Resource> resources = resourceSet.getResources();
		for (int i = 0; i < resources.size(); ++i) {
			final Resource currentResource = resources.get(i);
			final Iterator<EObject> resourceContent = currentResource.getAllContents();
			while (resourceContent.hasNext()) {
				final EObject eObject = resourceContent.next();
				final Iterator<EObject> objectChildren = eObject.eCrossReferences().iterator();
				while (objectChildren.hasNext()) {
					// Resolves cross references by simply visiting them.
					objectChildren.next();
				}
			}
		}
	}
}
