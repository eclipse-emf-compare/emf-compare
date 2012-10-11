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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.EObjectIndex.Side;
import org.eclipse.emf.ecore.EObject;

/**
 * This matcher is using a distance function to match EObject. It guarantees that elements are matched with
 * the other EObject having the lowest distance. If two elements have the same distance regarding the other
 * EObject it will arbitrary pick one. (You should probably not rely on this and make sure your distance only
 * return 0 if both EObject have the very same content). The matcher will try to use the fact that it is a
 * distance to achieve a suitable scalability. It is also build on the following assumptions :
 * <ul>
 * <li>Most EObjects have no difference and have their corresponding EObject on the other sides of the model
 * (right and origins)</li>
 * <li>Two consecutive calls on the distance function with the same parameters will give the same distance.</li>
 * </ul>
 * The scalability you'll get will highly depend on the complexity of the distance function. The
 * implementation is not caching any distance result from two EObjects.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ProximityEObjectMatcher implements IEObjectMatcher {

	/**
	 * The index which keep the EObjects.
	 */
	private EObjectIndex index;

	/**
	 * The list of matches found.
	 */
	private List<Match> matches = Lists.newArrayList();

	/**
	 * A map cross referencing the eObject to their match.
	 */
	private Map<EObject, Match> eObjectsToMatch = Maps.newHashMap();

	/**
	 * The function we are using to measure the distance between two eObjects.
	 */
	private DistanceFunction meter;

	/**
	 * Create the matcher using the given distance function.
	 * 
	 * @param meter
	 *            a function to measure the distance between two {@link EObject}s.
	 */
	public ProximityEObjectMatcher(DistanceFunction meter) {
		this.meter = meter;
		this.index = new ByTypeIndex(meter);
	}

	/**
	 * {@inheritDoc}
	 */

	public Iterable<Match> createMatches(Iterator<? extends EObject> leftEObjects,
			Iterator<? extends EObject> rightEObjects, Iterator<? extends EObject> originEObjects) {

		/*
		 * We are iterating through the three sides of the scope at the same time so that index might apply
		 * pre-matching strategies elements if they wish.
		 */
		while (leftEObjects.hasNext() || rightEObjects.hasNext() || leftEObjects.hasNext()) {
			if (leftEObjects.hasNext()) {
				index.index(leftEObjects.next(), Side.LEFT);
			}
			if (rightEObjects.hasNext()) {
				index.index(rightEObjects.next(), Side.RIGHT);
			}
			if (originEObjects.hasNext()) {
				index.index(originEObjects.next(), Side.ORIGIN);
			}
		}

		for (EObject left : index.getValuesStillThere(Side.LEFT)) {
			Map<Side, EObject> closests = index.findClosests(left, Side.LEFT);
			EObject right = closests.get(Side.RIGHT);
			EObject ancestor = closests.get(Side.ORIGIN);
			areMatching(left, right, ancestor);
			if (right != null) {
				index.remove(right, Side.RIGHT);
			}
			index.remove(left, Side.LEFT);
			if (ancestor != null) {
				index.remove(ancestor, Side.ORIGIN);
			}

		}

		/*
		 * now we have to process the remaining objects starting from the right index and trying to match an l
		 * object.
		 */
		for (EObject rObj : index.getValuesStillThere(Side.RIGHT)) {
			Map<Side, EObject> closests = index.findClosests(rObj, Side.RIGHT);
			EObject lObj = closests.get(Side.LEFT);
			EObject aObj = closests.get(Side.ORIGIN);
			areMatching(lObj, rObj, aObj);
			index.remove(rObj, Side.RIGHT);
			if (lObj != null) {
				index.remove(lObj, Side.LEFT);
			}
			if (aObj != null) {
				index.remove(aObj, Side.ORIGIN);
			}
		}

		for (EObject notFound : index.getValuesStillThere(Side.RIGHT)) {
			areMatching(null, notFound, null);
			index.remove(notFound, Side.RIGHT);
		}
		for (EObject notFound : index.getValuesStillThere(Side.LEFT)) {
			areMatching(notFound, null, null);
			index.remove(notFound, Side.LEFT);
		}
		for (EObject notFound : index.getValuesStillThere(Side.ORIGIN)) {
			areMatching(null, null, notFound);
			index.remove(notFound, Side.ORIGIN);
		}

		restructureMatchModel();

		return matches;
	}

	/**
	 * Process all the matches and re-attache them to their parent if one is found.
	 */
	private void restructureMatchModel() {
		Iterator<Match> it = matches.iterator();

		while (it.hasNext()) {
			Match cur = it.next();
			EObject possibleContainer = null;
			if (cur.getLeft() != null) {
				possibleContainer = cur.getLeft().eContainer();
			}
			if (possibleContainer != null && cur.getRight() != null) {
				possibleContainer = cur.getRight().eContainer();
			}
			if (possibleContainer != null && cur.getOrigin() != null) {
				possibleContainer = cur.getOrigin().eContainer();
			}
			Match possibleContainerMatch = eObjectsToMatch.get(possibleContainer);
			if (possibleContainerMatch != null) {
				((BasicEList<Match>)possibleContainerMatch.getSubmatches()).addUnique(cur);
				it.remove();
			}
		}

		Iterator<Match> it2 = matches.iterator();
		if (it2.hasNext()) {
			Match root = it2.next();
			while (it2.hasNext()) {
				((BasicEList<Match>)root.getSubmatches()).addUnique(it2.next());
				it2.remove();
			}

		}
	}

	/**
	 * Register the given object as a match.
	 * 
	 * @param left
	 *            left element.
	 * @param right
	 *            right element
	 * @param origin
	 *            origin element.
	 * @return the created match.
	 */
	private Match areMatching(EObject left, EObject right, EObject origin) {
		Match result = CompareFactory.eINSTANCE.createMatch();
		result.setLeft(left);
		result.setRight(right);
		result.setOrigin(origin);
		matches.add(result);
		if (left != null) {
			eObjectsToMatch.put(left, result);
			index.remove(left, Side.LEFT);
		}
		if (right != null) {
			eObjectsToMatch.put(right, result);
			index.remove(right, Side.RIGHT);
		}
		if (origin != null) {
			eObjectsToMatch.put(origin, result);
			index.remove(origin, Side.ORIGIN);
		}
		return result;
	}

	/**
	 * This represent a distance function used by the {@link ProximityEObjectMatcher} to compare EObjects and
	 * retrieve the closest EObject from one side to another. Axioms of the distance are supposed to be
	 * respected more especially :
	 * <ul>
	 * <li>symetry : dist(a,b) == dist(b,a)</li>
	 * <li>separation :dist(a,a) == 0</li>
	 * </ul>
	 * Triangular inequality is not leveraged with the current implementation but might be at some point to
	 * speed up the indexing. <br/>
	 * computing the distance between two EObjects should be a <b> fast operation</b> or the scalability of
	 * the whole matching phase will be poor.
	 * 
	 * @author cedric brun <cedric.brun@obeo.fr>
	 */
	public interface DistanceFunction {
		/**
		 * Return the distance between two EObjects. When the two objects should considered as completely
		 * different the implementation is expected to return Integer.MAX_VALUE.
		 * 
		 * @param a
		 *            first object.
		 * @param b
		 *            second object.
		 * @return the distance between the two EObjects or Integer.MAX_VALUE when the objects are considered
		 *         too different to be the same.
		 */
		int distance(EObject a, EObject b);

		/**
		 * Check that two objects are equals from the distance function point of view (distance should be 0)
		 * You should prefer this method when you just want to check objects are not equals enabling the
		 * distance to stop sooner.
		 * 
		 * @param a
		 *            first object.
		 * @param b
		 *            second object.
		 * @return true of the two objects are equals, false otherwise.
		 */
		boolean areIdentic(EObject a, EObject b);
	}

	/**
	 * static method returning a builder to easily specify the configuration options of an
	 * {@link ProximityEObjectMatcher}.
	 * 
	 * @param function
	 *            the distance function to use.
	 * @return a builder to easily specify the configuration options of an {@link ProximityEObjectMatcher}.
	 */
	public static Builder builder(DistanceFunction function) {
		return new Builder(function);
	}

	/**
	 * An builder class to instanciate and configure the proximity Matcher.
	 */
	public static class Builder {
		/**
		 * The matcher being configured by the builder.
		 */
		private ProximityEObjectMatcher beingConfigured;

		/**
		 * Create a new builder to configure the given matcher.
		 * 
		 * @param function
		 *            : the distance function to use.
		 */
		public Builder(DistanceFunction function) {
			this.beingConfigured = new ProximityEObjectMatcher(function);
		}

		/**
		 * Specify the distance function to use for the comparison.
		 * 
		 * @param function
		 *            the function to use for measuring the delta between two EObjects.
		 * @return the current builder so that one can chain calls.
		 */
		public Builder distanceFunction(DistanceFunction function) {
			this.beingConfigured.index = new ByTypeIndex(function);
			return this;
		}

		/**
		 * return the {@link ProximityEObjectMatcher} being configured.
		 * 
		 * @return the {@link ProximityEObjectMatcher} being configured.
		 */
		public ProximityEObjectMatcher build() {
			return this.beingConfigured;
		}

	}

}
