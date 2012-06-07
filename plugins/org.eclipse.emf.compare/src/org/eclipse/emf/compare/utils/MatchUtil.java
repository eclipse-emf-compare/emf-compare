package org.eclipse.emf.compare.utils;

import java.util.Iterator;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This utility class holds methods that will be used by the diff and merge processes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class MatchUtil {
	/**
	 * Utility classes don't need a default constructor.
	 */
	private MatchUtil() {
		// Hides default constructor
	}

	/**
	 * Get the object which is the origin value from the given matching <code>object</code>.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param object
	 *            The given object.
	 * @return The origin value.
	 */
	public static EObject getOriginObject(Comparison comparison, EObject object) {
		EObject result = null;
		Match match = comparison.getMatch(object);
		if (match != null) {
			if (comparison.isThreeWay()) {
				result = match.getOrigin();
			} else {
				if (object.equals(match.getLeft())) {
					result = match.getRight();
				} else {
					result = match.getLeft();
				}
			}
		}
		return result;
	}

	/**
	 * From a given mono-valued reference change, get the origin value.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The given reference change.
	 * @return The origin value.
	 */
	public static EObject getOriginValue(Comparison comparison, ReferenceChange difference) {
		if (!difference.getReference().isContainment() && !difference.getReference().isMany()
				&& difference.getKind().equals(DifferenceKind.CHANGE)) {
			EObject originContainer = getOriginContainer(comparison, difference);
			if (originContainer != null) {
				Object originValue = originContainer.eGet(difference.getReference());
				if (originValue instanceof EObject) {
					return (EObject)originValue;
				}
			}
		}
		return null;
	}

	/**
	 * Get the business model object containing the given <code>difference</code> in the origin side.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The difference.
	 * @return The object.
	 */
	public static EObject getOriginContainer(Comparison comparison, ReferenceChange difference) {
		EObject diffContainer = getContainer(comparison, difference);
		if (comparison.isThreeWay()) {
			diffContainer = difference.getMatch().getOrigin();
		} else {
			if (diffContainer.equals(difference.getMatch().getLeft())) {
				diffContainer = difference.getMatch().getRight();
			} else {
				diffContainer = difference.getMatch().getLeft();
			}
		}
		return diffContainer;
	}

	/**
	 * Get the business model object containing the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The difference.
	 * @return The object.
	 */
	public static EObject getContainer(Comparison comparison, ReferenceChange difference) {
		EObject result = null;
		final EObject obj = difference.getValue();
		if (getSide(comparison, obj).equals(Side.LEFT)) {
			result = difference.getMatch().getLeft();
		} else if (getSide(comparison, obj).equals(Side.RIGHT)) {
			result = difference.getMatch().getRight();
		} else if (getSide(comparison, obj).equals(Side.ORIGIN)) {
			result = difference.getMatch().getOrigin();
		}
		return result;
	}

	/**
	 * Enumerated type enabling to know from which side come an object.
	 * 
	 * @author cnotot
	 */
	// CHECKSTYLE:OFF
	public enum Side {
		LEFT, RIGHT, ORIGIN, NOWHERE
	}

	// CHECKSTYLE:ON

	/**
	 * Return the side where is located the given object <code>obj</code>.
	 * 
	 * @param comparison
	 *            The comparison which enable to know from which side come the business model objects owning
	 *            the differences.
	 * @param obj
	 *            The object to request.
	 * @return The side where is located the object.
	 */
	public static Side getSide(Comparison comparison, EObject obj) {
		Side result = Side.NOWHERE;
		final Iterator<MatchResource> matchResources = comparison.getMatchedResources().iterator();
		while (matchResources.hasNext()) {
			MatchResource matchResource = matchResources.next();
			Resource left = matchResource.getLeft();
			Resource right = matchResource.getRight();
			Resource origin = matchResource.getOrigin();
			if (obj.eResource().equals(left)) {
				result = Side.LEFT;
			} else if (obj.eResource().equals(right)) {
				result = Side.RIGHT;
			} else if (obj.eResource().equals(origin)) {
				result = Side.ORIGIN;
			}
		}
		return result;

	}

}
