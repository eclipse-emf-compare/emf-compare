/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.List;
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
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.resource.IResourceMatchingStrategy#matchResources(java.lang.Iterable,
	 *      java.lang.Iterable, java.lang.Iterable)
	 */
	public List<MatchResource> matchResources(Iterable<? extends Resource> left,
			Iterable<? extends Resource> right, Iterable<? extends Resource> origin) {
		final List<MatchResource> mappings = Lists.newArrayList();

		final List<Resource> rightCopy = Lists.newArrayList(right);
		final List<Resource> originCopy = Lists.newArrayList(origin);

		// Can we find matches for the left resource in either left or origin?
		for (Resource leftResource : left) {
			final Resource matchingRight = findMatch(leftResource, rightCopy);
			final Resource matchingOrigin = findMatch(leftResource, originCopy);

			if (matchingRight != null || matchingOrigin != null) {
				rightCopy.remove(matchingRight);
				originCopy.remove(matchingOrigin);
				mappings.add(createMatchResource(leftResource, matchingRight, matchingOrigin));
			}
		}

		// We no longer have to check in the left, but we may have matches of the right resources in the
		// origin list
		for (Resource rightResource : rightCopy) {
			final Resource matchingOrigin = findMatch(rightResource, originCopy);
			originCopy.remove(matchingOrigin);

			if (matchingOrigin != null) {
				mappings.add(createMatchResource(null, rightResource, matchingOrigin));
			}
		}

		return mappings;
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
	 */
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
