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

import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * This implementation of a matching strategy will simply delegate to a number of others in order. The
 * resources for which we found a match in a given strategy won't be passed on to the subsequent strategies of
 * the chain.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ChainMatchingStrategy implements IResourceMatchingStrategy {
	/** The set of strategies we are to call through this chain. */
	private final IResourceMatchingStrategy[] strategies;

	/**
	 * Instantiates a chaining strategy given the substrategies that it is to call.
	 * 
	 * @param strategies
	 *            The strategies we are to delegate to, in the order they are to be called.
	 */
	public ChainMatchingStrategy(IResourceMatchingStrategy... strategies) {
		this.strategies = strategies;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.resource.IResourceMatchingStrategy#matchResources(java.lang.Iterable,
	 *      java.lang.Iterable, java.lang.Iterable)
	 */
	public List<ResourceMapping> matchResources(Iterable<Resource> left, Iterable<Resource> right,
			Iterable<Resource> origin) {
		if (strategies.length == 1) {
			return strategies[0].matchResources(left, right, origin);
		}

		final List<ResourceMapping> mappings = Lists.newArrayList();
		final List<Resource> leftCopy = Lists.newArrayList(left);
		final List<Resource> rightCopy = Lists.newArrayList(right);
		final List<Resource> originCopy = Lists.newArrayList(origin);

		// Break this loop if we exhausted all strategies or if two of the lists are empty (no potential
		// match remaining)
		for (int i = 0; i < strategies.length
				&& !atLeastTwo(leftCopy.isEmpty(), rightCopy.isEmpty(), originCopy.isEmpty()); i++) {
			final List<ResourceMapping> newMappings = strategies[i].matchResources(left, right, origin);
			for (ResourceMapping newMapping : newMappings) {
				leftCopy.remove(newMapping.getLeft());
				rightCopy.remove(newMapping.getRight());
				originCopy.remove(newMapping.getOrigin());
			}
			mappings.addAll(newMappings);
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
	private static boolean atLeastTwo(boolean condition1, boolean condition2, boolean condition3) {
		// CHECKSTYLE:OFF This expression is alone in its method, and documented.
		return condition1 && (condition2 || condition3) || (condition2 && condition3);
		// CHECKSTYLE:ON
	}
}
