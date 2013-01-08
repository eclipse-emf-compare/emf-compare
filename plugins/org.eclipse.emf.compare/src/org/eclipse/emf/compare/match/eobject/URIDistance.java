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
package org.eclipse.emf.compare.match.eobject;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

/**
 * This class is able to measure similarity between "URI like" strings, basically strings separated by "/".
 * This is mainly intended to be used with EMF's fragments.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class URIDistance implements Function<EObject, Iterable<String>> {
	/**
	 * The upper bound distance we can get using this function.
	 */
	private static final int MAX_DISTANCE = 10;

	/**
	 * A computing cache for the locations.
	 */
	private Cache<EObject, Iterable<String>> locationCache;

	/**
	 * Create a new {@link URIDistance}.
	 */
	public URIDistance() {
		locationCache = CacheBuilder.newBuilder().maximumSize(10000).build(CacheLoader.from(this));
	}

	/**
	 * Return a metric result URI similarities. It compares 2 strings splitting those by "/" and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param a
	 *            First of the two {@link EObject}s to compare.
	 * @param b
	 *            Second of the two {@link EObject}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(EObject a, EObject b) {
		Iterable<String> aPath = locationCache.getUnchecked(a);
		Iterable<String> bPath = locationCache.getUnchecked(b);
		return proximity(aPath, bPath);
	}

	/**
	 * Return a metric result URI similarities. It compares 2 lists of fragments and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param aPath
	 *            First of the two list of {@link String}s to compare.
	 * @param bPath
	 *            Second of the two list of {@link String}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(Iterable<String> aPath, Iterable<String> bPath) {
		int aSize = 0;
		int bSize = 0;
		Iterator<String> itA = aPath.iterator();
		Iterator<String> itB = bPath.iterator();
		boolean areSame = true;
		int commonSegments = 0;
		int remainingASegments = 0;
		int remainingBSegments = 0;
		while (itA.hasNext() && itB.hasNext() && areSame) {
			String a = itA.next();
			String b = itB.next();
			if (a.equals(b)) {
				commonSegments++;
			} else {
				areSame = false;
			}
			aSize++;
			bSize++;

		}
		if (commonSegments == 0) {
			return MAX_DISTANCE;
		}
		remainingASegments = aSize + Iterators.size(itA) - commonSegments;
		remainingBSegments = bSize + Iterators.size(itB) - commonSegments;

		int nbSegmentsToGoFromAToB = remainingASegments + remainingBSegments;
		return (nbSegmentsToGoFromAToB * 10) / (commonSegments * 2 + nbSegmentsToGoFromAToB);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public Iterable<String> apply(EObject input) {
		EObject cur = input;
		String result = ""; //$NON-NLS-1$
		EObject container = input.eContainer();
		if (container != null) {
			EStructuralFeature feat = cur.eContainingFeature();
			if (input instanceof InternalEObject) {
				String frag = ((InternalEObject)container).eURIFragmentSegment(feat, cur);
				result = frag;
			}
		} else {
			result = "0"; //$NON-NLS-1$
		}

		if (input.eContainer() != null) {
			return Iterables.concat(Lists.newArrayList(result), locationCache
					.getUnchecked(input.eContainer()));
		}
		return Lists.newArrayList(result);
	}

	/**
	 * return the maximum value we can get for this distance.
	 * 
	 * @return the maximum value we can get for this distance.
	 */
	public int getUpperBoundDistance() {
		return MAX_DISTANCE;
	}

}
