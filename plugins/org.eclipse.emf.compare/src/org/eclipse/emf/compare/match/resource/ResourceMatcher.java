/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.resource;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * A {@link ResourceMatcher} will be used to match two or three {@link Resource}s together; depending on
 * whether we are doing a two or three way comparison.
 * <p>
 * Do take note that the match engine expects ResourceMatchers to return matching resources as well as
 * resources that do not match.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ResourceMatcher {
	/** Left resources we are to match. */
	protected final Iterable<Resource> leftResources;

	/** Right resources we are to match. */
	protected final Iterable<Resource> rightResources;

	/** Origin of the resources we are to match. */
	protected final Iterable<Resource> originResources;

	/**
	 * Instantiates a {@link ResourceMatcher} given its target resources.
	 * 
	 * @param leftResources
	 *            All resources from the left side.
	 * @param rightResources
	 *            All resources from the right side.
	 * @param originResources
	 *            All resources from the origin side.
	 */
	public ResourceMatcher(Iterable<Resource> leftResources, Iterable<Resource> rightResources,
			Iterable<Resource> originResources) {
		this.leftResources = leftResources;
		this.rightResources = rightResources;
		this.originResources = originResources;
	}

	/**
	 * This will be called by the engine in order to retrieve the mappings created by this matcher.
	 * <p>
	 * The returned mappings should include both "matching" resources and "not matching" resources (i.e.
	 * resources that are in either left or right ... but not in any of the two other lists).
	 * </p>
	 * 
	 * @return The created resource mappings.
	 */
	public Iterable<ResourceMapping> createMappings() {
		final List<ResourceMapping> mappings = Lists.newArrayList();
		// If there is only one of each resource, no need to go any further
		if (Iterables.size(leftResources) == 1 && Iterables.size(rightResources) == 1) {
			final Resource left = leftResources.iterator().next();
			final Resource right = rightResources.iterator().next();
			final ResourceMapping mapping;
			if (Iterables.isEmpty(originResources)) {
				mapping = new ResourceMapping(left, right, null);
			} else {
				mapping = new ResourceMapping(left, right, originResources.iterator().next());
			}
			mappings.add(mapping);
		} else if (!Iterables.isEmpty(leftResources) && !Iterables.isEmpty(rightResources)) {
			// We will try two ways of matching resources before considering them "unmatched" : equal name or
			// equals "roots" identifiers.
			final IResourceMatchingStrategy strategy = createResourceMatchingStrategy();

			mappings.addAll(strategy.matchResources(leftResources, rightResources, originResources));

			// Any resource that has not been matched by now is unmatched
			final List<Resource> leftUnmatch = Lists.newArrayList(leftResources);
			final List<Resource> rightUnmatch = Lists.newArrayList(rightResources);
			final List<Resource> originUnmatch = Lists.newArrayList(originResources);

			for (ResourceMapping mapping : mappings) {
				leftUnmatch.remove(mapping.getLeft());
				rightUnmatch.remove(mapping.getRight());
				originUnmatch.remove(mapping.getOrigin());
			}

			for (Resource left : leftUnmatch) {
				mappings.add(new ResourceMapping(left, null, null));
			}
			for (Resource right : rightUnmatch) {
				mappings.add(new ResourceMapping(null, right, null));
			}
			for (Resource origin : originUnmatch) {
				mappings.add(new ResourceMapping(null, null, origin));
			}
		}

		return mappings;
	}

	/**
	 * Creates the resource matching strategy that should be used by this matcher.
	 * 
	 * @return The resource matching strategy that should be used by this matcher.
	 */
	protected IResourceMatchingStrategy createResourceMatchingStrategy() {
		final IResourceMatchingStrategy nameStrategy = new NameMatchingStrategy();
		final IResourceMatchingStrategy idStrategy = new RootIDMatchingStrategy();
		return new ChainMatchingStrategy(nameStrategy, idStrategy);
	}
}
