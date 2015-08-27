/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - only return unique matches
 *******************************************************************************/
package org.eclipse.emf.compare.match.resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * This implementation of a matching strategy checks for the IDs of the resources' roots, and consider that
 * resources match if the identifiers of their roots do.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class RootIDMatchingStrategy implements IResourceMatchingStrategy {

	/**
	 * Matches the given resources according to the IDs found in their roots.
	 * <p>
	 * When the root IDs of two resources intersect, they are considered as matching. This strategy will only
	 * return unique matches between all resources.
	 * </p>
	 * 
	 * @param left
	 *            Resources we are to match in the left.
	 * @param right
	 *            Resources we are to match in the right.
	 * @param origin
	 *            Resources we are to match in the origin.
	 * @return The list of unique mappings this strategy managed to determine.
	 */
	public List<MatchResource> matchResources(Iterable<? extends Resource> left,
			Iterable<? extends Resource> right, Iterable<? extends Resource> origin) {
		final List<MatchResource> mappings = Lists.newArrayList();

		final List<Resource> leftCopy = Lists.newArrayList(left);
		final List<Resource> rightCopy = Lists.newArrayList(right);
		final List<Resource> originCopy = Lists.newArrayList(origin);

		final Map<Resource, List<Resource>> leftRightMap = new LinkedHashMap<Resource, List<Resource>>();
		final Map<Resource, List<Resource>> leftOriginMap = new LinkedHashMap<Resource, List<Resource>>();
		final Map<Resource, List<Resource>> rightOriginMap = new LinkedHashMap<Resource, List<Resource>>();

		for (Resource leftResource : leftCopy) {
			final List<Resource> matchingRights = findMatches(leftResource, rightCopy);
			leftRightMap.put(leftResource, matchingRights);

			final List<Resource> matchingOrigins = findMatches(leftResource, originCopy);
			leftOriginMap.put(leftResource, matchingOrigins);
		}
		for (Resource rightResource : rightCopy) {
			final List<Resource> matchingLefts = findMatches(rightResource, leftCopy);
			leftRightMap.put(rightResource, matchingLefts);

			final List<Resource> matchingOrigins = findMatches(rightResource, originCopy);
			rightOriginMap.put(rightResource, matchingOrigins);
		}
		for (Resource originResource : originCopy) {
			final List<Resource> matchingLefts = findMatches(originResource, leftCopy);
			leftOriginMap.put(originResource, matchingLefts);

			final List<Resource> matchingRights = findMatches(originResource, rightCopy);
			rightOriginMap.put(originResource, matchingRights);
		}

		for (Resource leftResource : leftCopy) {
			List<Resource> rightObjects = leftRightMap.get(leftResource);

			Resource rightResource = null;
			Resource originResource = null;

			if (rightObjects.size() > 1) {
				rightCopy.removeAll(rightObjects);
				continue;
			}

			if (rightObjects.size() == 1) {
				final Resource rightCandidate = rightObjects.get(0);
				// check left
				if (leftRightMap.get(rightCandidate).size() == 1) {
					rightResource = rightCandidate;
				}
			}

			// check origins
			if (!originCopy.isEmpty()) {
				Set<Resource> originObjects = Sets.newHashSet();
				originObjects.addAll(leftOriginMap.get(leftResource));
				if (rightResource != null) {
					originObjects.addAll(rightOriginMap.get(rightResource));
				}
				if (originObjects.size() > 1) {
					rightCopy.remove(rightResource);
					continue;
				}
				if (originObjects.size() == 1) {
					final Resource originCandidate = originObjects.iterator().next();
					// check origin does not map to more
					Set<Resource> mappedResources = Sets.newHashSet();
					mappedResources.addAll(leftOriginMap.get(originCandidate));
					mappedResources.addAll(rightOriginMap.get(originCandidate));
					mappedResources.add(leftResource);
					mappedResources.add(rightResource);
					if (mappedResources.size() > 2) {
						rightCopy.remove(rightResource);
						continue;
					} else {
						originResource = originCandidate;
					}
				}
			}
			if (rightResource != null || originResource != null) {
				mappings.add(createMatchResource(leftResource, rightResource, originResource));
				rightCopy.remove(rightResource);
			}
		}

		if (!originCopy.isEmpty()) {
			for (Resource rightResource : rightCopy) {

				Resource leftResource = null;
				Resource originResource = null;

				List<Resource> originObjects = rightOriginMap.get(rightResource);
				if (originObjects.size() == 1) {
					originResource = originObjects.get(0);
					// check right side
					if (rightOriginMap.get(originResource).size() > 1) {
						continue;
					}
					// check left side
					List<Resource> leftCandidates = leftOriginMap.get(originResource);
					if (leftCandidates.size() > 1) {
						continue;
					} else if (leftCandidates.size() == 1) {
						Resource leftCandidate = leftCandidates.get(0);

						// check right and origin
						if (leftOriginMap.get(leftCandidate).size() > 1) {
							continue;
						}
						if (!leftRightMap.get(leftCandidate).isEmpty()) {
							continue;
						}
						leftResource = leftCandidate;
					}
					mappings.add(createMatchResource(leftResource, rightResource, originResource));
				}
			}
		}
		return mappings;
	}

	/**
	 * Returns the first two matches of <code>reference</code> in <code>candidates</code>. This implementation
	 * will consider two Resources to be "matches" if their roots have IDs, and these IDs intersect.
	 * <p>
	 * Subclasses may return more than two elements if considered useful.
	 * </p>
	 * 
	 * @param reference
	 *            The reference resource.
	 * @param candidates
	 *            The list of potential candidates that may match <code>reference</code>.
	 * @return The first two matches of <code>reference</code> in <code>candidates</code>. Empty list if none.
	 * @since 3.3
	 */
	protected List<Resource> findMatches(Resource reference, Iterable<Resource> candidates) {
		final Set<String> referenceIDs = getResourceIdentifiers(reference);
		if (referenceIDs.isEmpty()) {
			return Lists.newArrayList();
		}

		final List<Resource> matches = new ArrayList<Resource>(2);
		final Iterator<Resource> candidateIterator = candidates.iterator();

		// optimize for size 2 since we do not need more at the moment
		while (candidateIterator.hasNext() && matches.size() < 2) {
			final Resource candidate = candidateIterator.next();
			final Set<String> candidateIDs = getResourceIdentifiers(candidate);
			if (!candidateIDs.isEmpty() && !Sets.intersection(candidateIDs, referenceIDs).isEmpty()) {
				matches.add(candidate);
			}
		}

		return matches;
	}

	/**
	 * Returns the first match of <code>reference</code> in <code>candidates</code>. This implementation will
	 * consider two Resources to be "matches" if their roots have IDs, and these IDs are the same.
	 * 
	 * @param reference
	 *            The reference resource.
	 * @param candidates
	 *            The list of potential candidates that may match <code>reference</code>.
	 * @return The first match of <code>reference</code> in <code>candidates</code>. <code>null</code> if
	 *         none.
	 * @deprecated use {@link RootIDMatchingStrategy#findMatches(Resource, Iterable)} instead.
	 */
	@Deprecated
	protected Resource findMatch(Resource reference, Iterable<Resource> candidates) {
		final Set<String> referenceIDs = getResourceIdentifiers(reference);
		if (referenceIDs.isEmpty()) {
			return null;
		}

		Resource match = null;
		final Iterator<Resource> candidateIterator = candidates.iterator();
		while (candidateIterator.hasNext() && match == null) {
			final Resource candidate = candidateIterator.next();
			final Set<String> candidateIDs = getResourceIdentifiers(candidate);
			if (!candidateIDs.isEmpty() && !Sets.intersection(candidateIDs, referenceIDs).isEmpty()) {
				match = candidate;
			}
		}
		return match;
	}

	/**
	 * Retrieves the set of identifiers for the given resource's root.
	 * 
	 * @param resource
	 *            The resource for which we need the identifiers.
	 * @return The identifiers (both XMI:ID and eAttribute ID) of the resource's roots, if any. May be empty
	 *         if the resource has no roots or if they have no ID.
	 */
	protected Set<String> getResourceIdentifiers(Resource resource) {
		final Set<String> identifiers = Sets.newHashSet();

		for (EObject root : resource.getContents()) {
			if (resource instanceof XMIResource) {
				String resourceId = ((XMIResource)resource).getID(root);
				if (resourceId != null) {
					identifiers.add(resourceId);
				}
			}
			String rootID = EcoreUtil.getID(root);
			if (rootID != null) {
				identifiers.add(rootID);
			}
		}

		return identifiers;
	}

	/**
	 * Creates a {@link MatchResource} instance and sets all three resources of the mapping on it.
	 * 
	 * @param left
	 *            The left resource of this mapping.
	 * @param right
	 *            The right resource of this mapping.
	 * @param origin
	 *            The origin resource of this mapping.
	 * @return The create mapping.
	 */
	protected MatchResource createMatchResource(Resource left, Resource right, Resource origin) {
		final MatchResource match = CompareFactory.eINSTANCE.createMatchResource();

		match.setLeft(left);
		match.setRight(right);
		match.setOrigin(origin);

		if (left != null && left.getURI() != null) {
			match.setLeftURI(left.getURI().toString());
		}
		if (right != null && right.getURI() != null) {
			match.setRightURI(right.getURI().toString());
		}
		if (origin != null && origin.getURI() != null) {
			match.setOriginURI(origin.getURI().toString());
		}

		return match;
	}
}
