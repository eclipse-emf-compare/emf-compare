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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.api.IMatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.metamodel.Side;
import org.eclipse.emf.compare.match.metamodel.UnmatchModel;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.compare.util.EMFComparePreferenceKeys;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Service facade for matching models.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
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
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 * @since 0.9.0
	 */
	public static MatchResourceSet doResourceSetMatch(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet, ResourceSet ancestorResourceSet, Map<String, Object> options)
			throws InterruptedException {
		// Resolve all proxies so that all resources get loaded
		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);
		EcoreUtil.resolveAll(ancestorResourceSet);

		final List<Resource> remainingLeftResources = new ArrayList<Resource>(leftResourceSet.getResources());
		final List<Resource> remainingRightResources = new ArrayList<Resource>(rightResourceSet
				.getResources());
		final List<Resource> remainingAncestorResources = new ArrayList<Resource>(ancestorResourceSet
				.getResources());

		final MatchResourceSet match = MatchFactory.eINSTANCE.createMatchResourceSet();
		for (final Resource res : leftResourceSet.getResources()) {
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
	 * @see org.eclipse.emf.compare.match.api.MatchOptions
	 * @since 0.9.0
	 */
	public static MatchResourceSet doResourceSetMatch(ResourceSet leftResourceSet,
			ResourceSet rightResourceSet, Map<String, Object> options) throws InterruptedException {
		// Resolve all proxies so that all resources get loaded
		EcoreUtil.resolveAll(leftResourceSet);
		EcoreUtil.resolveAll(rightResourceSet);

		final List<Resource> remainingLeftResources = new ArrayList<Resource>(leftResourceSet.getResources());
		final List<Resource> remainingRightResources = new ArrayList<Resource>(rightResourceSet
				.getResources());

		final MatchResourceSet match = MatchFactory.eINSTANCE.createMatchResourceSet();
		for (final Resource res : leftResourceSet.getResources()) {
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
	 * This will try and find a resource in <code>candidates</code> similar to <code>resource</code>.
	 * 
	 * @param resource
	 *            The resource we seek a similar to in the given resourceSet.
	 * @param candidates
	 *            candidate resources.
	 * @return The most similar resource to <code>resource</code> we could find in <code>resourceSet</code>.
	 */
	private static Resource findMatchingResource(Resource resource, List<Resource> candidates) {
		final double resourceSimilarityThreshold = 0.7d;
		final URI referenceURI = resource.getURI();
		if (candidates.size() == 1)
			return candidates.get(0);

		Resource mostSimilar = null;
		double highestSimilarity = -1;
		for (final Resource candidate : candidates) {
			final URI candidateURI = candidate.getURI();
			if (referenceURI.fileExtension().equals(candidateURI.fileExtension())) {
				final String[] referenceSegments = referenceURI.trimFileExtension().segments();
				final String[] candidateSegments = candidateURI.trimFileExtension().segments();
				final double similarity = resourceURISimilarity(referenceSegments, candidateSegments);
				if (similarity > highestSimilarity) {
					highestSimilarity = similarity;
					mostSimilar = candidate;
				}
			}
		}

		// Consider dissimilar
		if (highestSimilarity < resourceSimilarityThreshold) {
			mostSimilar = null;
		}
		return mostSimilar;
	}

	/**
	 * This will compute the similarity of two URIs based on their segments (minus file extension).
	 * 
	 * @param reference
	 *            The reference URI.
	 * @param candidate
	 *            Candidate for which the similarity to <code>reference</code> is to be computed.
	 * @return A double comprised between <code>0</code> and <code>1</code> included, <code>1</code> being
	 *         equal and <code>0</code> different.
	 */
	private static double resourceURISimilarity(String[] reference, String[] candidate) {
		final double nameWeight = 0.6;
		final double equalSegmentWeight = 0.4;

		final String referenceName = reference[reference.length - 1];
		final String candidateName = candidate[candidate.length - 1];
		final double nameSimilarity = NameSimilarity.nameSimilarityMetric(referenceName, candidateName);

		double equalSegments = 0d;
		int referenceIndex = reference.length - 2;
		int candidateIndex = candidate.length - 2;
		while (referenceIndex >= 0 && candidateIndex >= 0) {
			if (reference[referenceIndex].equals(candidate[candidateIndex])) {
				equalSegments++;
			}
			referenceIndex--;
			candidateIndex--;
		}
		if (reference.length == 1 || candidate.length == 1)
			return nameSimilarity;
		else
			return nameSimilarity * nameWeight
					+ (equalSegments * 2 / (reference.length + candidate.length - 2)) * equalSegmentWeight;
	}
}
