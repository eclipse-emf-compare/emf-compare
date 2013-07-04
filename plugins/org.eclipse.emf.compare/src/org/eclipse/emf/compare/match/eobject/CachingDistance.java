/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import com.google.common.collect.Maps;

import java.util.Map;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.match.eobject.ProximityEObjectMatcher.DistanceFunction;
import org.eclipse.emf.ecore.EObject;

/**
 * This class wraps a DistanceFunction and cache its result. Any call to distance(a,b) will be cached and the
 * same value will be returned to distance(b,a).
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class CachingDistance implements DistanceFunction {

	/**
	 * The wrapped function.
	 */
	private DistanceFunction wrapped;

	/**
	 * The cache keeping the previous results.
	 */
	private Map<Pair, Double> distanceCache;

	/**
	 * Create a new caching distance.
	 * 
	 * @param wrapped
	 *            actual distance function to cache results from.
	 */
	public CachingDistance(DistanceFunction wrapped) {
		this.wrapped = wrapped;
		distanceCache = Maps.newHashMap();
	}

	/**
	 * {@inheritDoc}
	 */
	public double distance(Comparison inProgress, EObject a, EObject b) {
		Pair key = new Pair(a, b);
		Double previousResult = distanceCache.get(key);
		if (previousResult == null) {
			double dist = wrapped.distance(inProgress, a, b);
			distanceCache.put(key, dist);
			// cache it
			return dist;
		}
		return previousResult.doubleValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean areIdentic(Comparison inProgress, EObject a, EObject b) {
		return wrapped.areIdentic(inProgress, a, b);
	}

	/**
	 * A class used as a key for two EObjects. Pair(a,b) and Pair(b,a) should be equals and have the same
	 * hashcodes
	 */
	class Pair {
		// CHECKSTYLE:OFF
		EObject a;

		EObject b;

		public Pair(EObject a, EObject b) {
			super();
			this.a = a;
			this.b = b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			int first = a.hashCode();
			int second = b.hashCode();
			if (first > second) {
				int tmp = first;
				first = second;
				second = tmp;
			}
			result = prime * result + first;
			result = prime * result + second;
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Pair other = (Pair)obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			return (a == other.a && b == other.b) || (b == other.a && a == other.b);

		}

		private CachingDistance getOuterType() {
			return CachingDistance.this;
		}

	}
	// CHECKSTYLE:ON

}
