/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - sort resources before matching
 *******************************************************************************/
package org.eclipse.emf.compare.match.resource;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A {@link StrategyResourceMatcher} will be used to match two or three {@link Resource}s together; depending
 * on whether we are doing a two or three way comparison.
 * <p>
 * Do take note that the match engine expects ResourceMatchers to return matching resources as well as
 * resources that do not match.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class StrategyResourceMatcher implements IResourceMatcher {

	/**
	 * Compares resources according to the string representation of their {@link URI}s.
	 */
	protected Comparator<Resource> resourceURIComparator = new Comparator<Resource>() {
		public int compare(Resource arg0, Resource arg1) {
			if (arg0.getURI() == null && arg1.getURI() == null) {
				return 0;
			}
			if (arg0.getURI() == null) {
				return -1;
			}
			if (arg1.getURI() == null) {
				return 1;
			}
			return arg0.getURI().toString().compareTo(arg1.getURI().toString());
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.resource.IResourceMatcher#createMappings(java.util.Iterator,
	 *      java.util.Iterator, java.util.Iterator)
	 */
	public Iterable<MatchResource> createMappings(Iterator<? extends Resource> leftResources,
			Iterator<? extends Resource> rightResources, Iterator<? extends Resource> originResources) {
		final List<MatchResource> mappings;

		// Copy the input Resource lists : we'll exhaust them as we go
		final List<? extends Resource> leftCopy = Lists.newArrayList(leftResources);
		final List<? extends Resource> rightCopy = Lists.newArrayList(rightResources);
		final List<? extends Resource> originCopy = Lists.newArrayList(originResources);

		Collections.sort(leftCopy, resourceURIComparator);
		Collections.sort(rightCopy, resourceURIComparator);
		Collections.sort(originCopy, resourceURIComparator);

		// Detect matching resources
		final IResourceMatchingStrategy[] strategies = getResourceMatchingStrategies();
		if (strategies.length == 1) {
			mappings = strategies[0].matchResources(leftCopy, rightCopy, originCopy);

			// Remove all matched from the copies to leave only unmatched
			for (MatchResource newMapping : mappings) {
				leftCopy.remove(newMapping.getLeft());
				rightCopy.remove(newMapping.getRight());
				originCopy.remove(newMapping.getOrigin());
			}
		} else {
			mappings = Lists.newArrayList();

			// Break this loop if we exhausted all strategies or if two of the lists are empty (no potential
			// match remaining)
			for (int i = 0; i < strategies.length
					&& !atLeastTwo(leftCopy.isEmpty(), rightCopy.isEmpty(), originCopy.isEmpty()); i++) {
				final List<MatchResource> newMappings = strategies[i].matchResources(leftCopy, rightCopy,
						originCopy);
				for (MatchResource newMapping : newMappings) {
					leftCopy.remove(newMapping.getLeft());
					rightCopy.remove(newMapping.getRight());
					originCopy.remove(newMapping.getOrigin());
				}
				mappings.addAll(newMappings);
			}
		}

		// Any resource that has not been matched by now is an unmatch. The "copies" list have been updated
		// each time we found a
		// match, they only contain the remaining unmatch resources now.
		for (Resource left : leftCopy) {
			mappings.add(createMatchResource(left, null, null));
		}
		for (Resource right : rightCopy) {
			mappings.add(createMatchResource(null, right, null));
		}
		for (Resource origin : originCopy) {
			mappings.add(createMatchResource(null, null, origin));
		}

		return mappings;
	}

	/**
	 * This will check that at least two of the three given booleans are <code>true</code>.
	 * 
	 * @param condition1
	 *            First of the three booleans.
	 * @param condition2
	 *            Second of the three booleans.
	 * @param condition3
	 *            Third of the three booleans.
	 * @return <code>true</code> if at least two of the three given booleans are <code>true</code>,
	 *         <code>false</code> otherwise.
	 */
	protected static boolean atLeastTwo(boolean condition1, boolean condition2, boolean condition3) {
		// CHECKSTYLE:OFF This expression is alone in its method, and documented.
		return condition1 && (condition2 || condition3) || (condition2 && condition3);
		// CHECKSTYLE:ON
	}

	/**
	 * Returns the matching strategies that are to be used by this resource matcher.
	 * <p>
	 * This default implementation will try two ways of matching the resources before giving up : resources
	 * have equal name, then resources have roots with equal identifiers.
	 * </p>
	 * <p>
	 * Resource Matching Strategies are expected to map resources together, but not to detect resources that
	 * do not match.
	 * </p>
	 * 
	 * @return The resource matching strategies that should be used by this matcher.
	 */
	protected IResourceMatchingStrategy[] getResourceMatchingStrategies() {
		final IResourceMatchingStrategy locationStrategy = new LocationMatchingStrategy();
		return new IResourceMatchingStrategy[] {locationStrategy, };
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
	protected static MatchResource createMatchResource(Resource left, Resource right, Resource origin) {
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
