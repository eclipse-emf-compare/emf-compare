package org.eclipse.emf.compare.match.eobject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * This class is responsible for holding several version of EObjects from sides left, right or origins and
 * provides the ability to return the closest instance (from a difference side) of a given EObject. The
 * implementation expects that the queried EObjects have all been indexed before a query is done. The
 * implementation also expects that when you're done with an EObject and if you don't want to get it back in
 * the result of subsequent queries, the EObject is removed. <br>
 * The scalability of this class will highly depend on the given distance function, especially with calls
 * having a max distance of 0. <br>
 * It is also based on the assumption that it is <b> very likely that the EObject has another version with e
 * distance of value 0</b> in the other versions sets.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ProximityIndex implements EObjectIndex {
	/**
	 * The distance function used to compare the Objects.
	 */
	private ProximityEObjectMatcher.DistanceFunction meter;

	/**
	 * the left objects still present in the index.
	 */
	private List<EObject> lefts;

	/**
	 * the right objects still present in the index.
	 */
	private List<EObject> rights;

	/**
	 * the origin objects still present in the index.
	 */
	private List<EObject> origins;

	/**
	 * Create a new {@link ProximityIndex} using the given distance function.
	 * 
	 * @param meter
	 *            the distance function to use to compare the EObjects.
	 */
	public ProximityIndex(ProximityEObjectMatcher.DistanceFunction meter) {
		this.meter = meter;
		this.lefts = Lists.newArrayList();
		this.rights = Lists.newArrayList();
		this.origins = Lists.newArrayList();

	}

	/**
	 * {@inheritDoc}
	 */

	public Map<Side, EObject> findClosests(EObject eObj, Side passedObjectSide, int maxDistance) {

		Map<Side, EObject> result = new HashMap<EObjectIndex.Side, EObject>(3);

		result.put(passedObjectSide, eObj);
		if (passedObjectSide == Side.LEFT) {
			EObject closestRight = findTheClosest(eObj, Side.LEFT, Side.RIGHT, maxDistance, true);
			EObject closestOrigin = findTheClosest(eObj, Side.LEFT, Side.ORIGIN, maxDistance, true);
			result.put(Side.RIGHT, closestRight);
			result.put(Side.ORIGIN, closestOrigin);
		} else if (passedObjectSide == Side.RIGHT) {
			EObject closestLeft = findTheClosest(eObj, Side.RIGHT, Side.LEFT, maxDistance, true);
			EObject closestOrigin = findTheClosest(eObj, Side.RIGHT, Side.ORIGIN, maxDistance, true);
			result.put(Side.LEFT, closestLeft);
			result.put(Side.ORIGIN, closestOrigin);

		} else if (passedObjectSide == Side.ORIGIN) {
			EObject closestLeft = findTheClosest(eObj, Side.ORIGIN, Side.LEFT, maxDistance, true);
			EObject closestRight = findTheClosest(eObj, Side.ORIGIN, Side.RIGHT, maxDistance, true);
			result.put(Side.LEFT, closestLeft);
			result.put(Side.RIGHT, closestRight);
		}

		return result;

	}

	/**
	 * return the closest EObject of the passed one found in the sideToFind storage.
	 * 
	 * @param eObj
	 *            the base EObject.
	 * @param originalSide
	 *            the side of the base EObject.
	 * @param sideToFind
	 *            the side to search in.
	 * @param maxDistance
	 *            the maximum distance to search.
	 * @param shouldDoubleCheck
	 *            true if we should make sure that the found EObject has the inverse relationship with the
	 *            base one.
	 * @return the closest EObject of the passed one found in the sideToFind storage.
	 */
	private EObject findTheClosest(final EObject eObj, final Side originalSide, final Side sideToFind,
			final int maxDistance, boolean shouldDoubleCheck) {
		List<EObject> storageToSearchFor = lefts;
		switch (sideToFind) {
			case RIGHT:
				storageToSearchFor = rights;
				break;
			case LEFT:
				storageToSearchFor = lefts;
				break;
			case ORIGIN:
				storageToSearchFor = origins;
				break;

			default:
				break;
		}
		/*
		 * We are starting by looking for EObject having a distance of 0. It means we'll iterate two times in
		 * the worst case but it is very likely that the EObject has another version with a distance of 0. It
		 * is also based on the assumption that calling distance() with a 0 max distance triggers shortcuts
		 * and is faster than calling the same distance() method with a max_distance > 0.
		 */
		EObject best = null;
		int bestDist = Integer.MAX_VALUE;
		Iterator<EObject> it = storageToSearchFor.iterator();
		while (it.hasNext() && best == null) {
			EObject fastCheck = it.next();
			int dist = meter.distance(eObj, fastCheck, 0);
			if (dist == 0) {
				best = fastCheck;
				bestDist = 0;
			}
		}

		if (best != null) {
			return best;
		}

		/*
		 * We could not find an EObject which is identical, let's search again and find the closest EObject.
		 */
		it = storageToSearchFor.iterator();
		while (bestDist != 0 && it.hasNext()) {
			EObject potentialClosest = it.next();
			int dist = meter.distance(eObj, potentialClosest, maxDistance);
			if (dist < bestDist) {
				if (shouldDoubleCheck) {
					// We need to double check the currentlyDigging has the same object as the closest !

					EObject doubleCheck = findTheClosest(potentialClosest, sideToFind, originalSide,
							maxDistance, false);
					if (doubleCheck == eObj) {
						bestDist = dist;
						best = potentialClosest;
					}
				} else {
					bestDist = dist;
					best = potentialClosest;
				}
			}
		}

		if (bestDist > maxDistance) {
			best = null;
		}
		return best;
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(EObject obj, Side side) {
		switch (side) {
			case RIGHT:
				rights.remove(obj);
				break;
			case LEFT:
				lefts.remove(obj);
				break;

			case ORIGIN:
				origins.remove(obj);
				break;

			default:
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void index(EObject eObject, Side side) {
		switch (side) {
			case RIGHT:
				rights.add(eObject);
				break;
			case LEFT:
				lefts.add(eObject);
				break;
			case ORIGIN:
				origins.add(eObject);
				break;

			default:
				break;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<EObject> getValuesStillThere(final Side side) {
		Collection<EObject> result = Collections.emptyList();
		switch (side) {
			case RIGHT:
				result = ImmutableList.copyOf(rights);
				break;
			case LEFT:
				result = ImmutableList.copyOf(lefts);
				break;
			case ORIGIN:
				result = ImmutableList.copyOf(origins);
				break;
			default:
				break;
		}
		return result;

	}

}
