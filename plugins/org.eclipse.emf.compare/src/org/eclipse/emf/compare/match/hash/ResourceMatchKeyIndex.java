/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.hash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Indexes existing resource matches to provide comparable keys for resources and unresolved proxy URIs.
 * Each MatchResource receives a group ID shared by its left, right and ancestor resources. One-sided matches
 * receive distinct IDs too: identical URIs alone must not merge unmatched resources.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ResourceMatchKeyIndex {

	/** Group IDs are non-negative; this value marks a URI shared by different groups. */
	private static final int AMBIGUOUS_RESOURCE_GROUP = -1;

	/** Group IDs indexed by resource identity, including one-sided resource matches. */
	private final Map<Resource, Integer> resourceGroupIds = new IdentityHashMap<>();

	/** Group IDs indexed by URI, or AMBIGUOUS_RESOURCE_GROUP when a URI cannot identify one group. */
	private final Map<URI, Integer> resourceGroupIdsByURI = new HashMap<>();

	/**
	 * Assigns one group ID to each resource match and indexes all of its present sides under that ID.
	 *
	 * @param matches
	 *            the resource correspondences already established by the match engine
	 */
	private ResourceMatchKeyIndex(Iterable<MatchResource> matches) {
		int groupId = 0;
		for (MatchResource match : matches) {
			indexResource(match.getLeft(), groupId);
			indexResource(match.getRight(), groupId);
			indexResource(match.getOrigin(), groupId);
			groupId++;
		}
	}

	/**
	 * Records a resource's group by identity. Its URI identifies the same group only if no other group uses
	 * that URI; otherwise an unresolved proxy cannot tell which resource was intended.
	 *
	 * @param resource
	 *            the resource to index, or null for an absent comparison side
	 * @param groupId
	 *            the non-negative ID shared by the resources in one MatchResource
	 */
	private void indexResource(Resource resource, int groupId) {
		if (resource == null) {
			return;
		}
		resourceGroupIds.put(resource, groupId);

		URI resourceURI = resource.getURI();
		if (resourceURI == null) {
			return;
		}
		Integer previousGroupId = resourceGroupIdsByURI.get(resourceURI);
		if (previousGroupId == null) {
			resourceGroupIdsByURI.put(resourceURI, groupId);
		} else if (previousGroupId != groupId) {
			// An ambiguous URI stays ambiguous even if a later resource repeats one of its groups.
			resourceGroupIdsByURI.put(resourceURI, AMBIGUOUS_RESOURCE_GROUP);
		}
	}

	/**
	 * Builds an index from the comparison's existing resource correspondence.
	 *
	 * @param matches
	 *            the matched resources
	 * @return the comparison-local resource key index
	 */
	public static ResourceMatchKeyIndex of(Iterable<MatchResource> matches) {
		return new ResourceMatchKeyIndex(matches);
	}

	/**
	 * Returns the matched resource's group ID, or its URI when no correspondence was recorded.
	 *
	 * @param resource
	 *            the resource owning the object being grouped, or null for a detached object
	 * @return the Integer group ID, the unmatched resource's URI, or null
	 */
	public Object key(Resource resource) {
		if (resource == null) {
			return null;
		}
		Integer groupId = resourceGroupIds.get(resource);
		if (groupId == null) {
			return resource.getURI();
		}
		return groupId;
	}

	/**
	 * Normalizes an unresolved proxy URI without loading its target. Resource URIs in the same matched group
	 * become comparable while the object fragment is preserved. Unknown or ambiguous resource URIs keep their
	 * original identity.
	 *
	 * @param proxyURI
	 *            the unresolved target's URI, including its fragment
	 * @return a pair of Integer group ID and fragment, or the original URI when no unique group exists
	 */
	public Object key(URI proxyURI) {
		Integer groupId = resourceGroupIdsByURI.get(proxyURI.trimFragment());
		if (groupId == null || groupId == AMBIGUOUS_RESOURCE_GROUP) {
			return proxyURI;
		}
		return Arrays.asList(groupId, proxyURI.fragment());
	}
}
