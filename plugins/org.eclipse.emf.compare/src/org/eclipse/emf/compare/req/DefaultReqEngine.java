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
package org.eclipse.emf.compare.req;

import static com.google.common.collect.Iterables.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * The requirements engine is in charge of actually computing the requirements between the differences.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific requirements might be necessary.
 * </p>
 * TODO document available extension possibilities. TODO to test on XSD models for FeatureMaps
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DefaultReqEngine implements IReqEngine {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.IDiffEngine#computeRequirements(org.eclipse.emf.compare.Comparison)
	 */
	public void computeRequirements(Comparison comparison) {
		for (Diff difference : comparison.getDifferences()) {
			checkForRequiredDifferences(comparison, difference);
		}
	}

	/**
	 * Checks the potential required differences from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param difference
	 *            The difference that is to be checked
	 */
	protected void checkForRequiredDifferences(Comparison comparison, Diff difference) {
		if (difference instanceof ReferenceChange) {
			ReferenceChange sourceDifference = (ReferenceChange)difference;

			Set<ReferenceChange> requiredDifferences = new HashSet<ReferenceChange>();
			Set<ReferenceChange> requiredByDifferences = new HashSet<ReferenceChange>();

			Match match = difference.getMatch();
			EReference reference = sourceDifference.getReference();
			EObject value = sourceDifference.getValue();
			DifferenceKind kind = sourceDifference.getKind();

			// ADD object
			if (kind == DifferenceKind.ADD && reference.isContainment()) {

				// -> requires ADD on the container of the object
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value.eContainer(),
						DifferenceKind.ADD));

				// -> requires DELETE of the origin value on the same containment mono-valued reference
				requiredDifferences.addAll(getDELOriginValueOnContainmentRefSingle(comparison,
						sourceDifference));

				// ADD reference
			} else if ((kind == DifferenceKind.ADD || isChangeAdd(comparison, sourceDifference))
					&& !reference.isContainment()) {

				// -> requires ADD of the value of the reference (target object)
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value, DifferenceKind.ADD));

				// -> requires ADD of the object containing the reference
				final EObject container;
				if (sourceDifference.getSource() == DifferenceSource.LEFT) {
					container = match.getLeft();
				} else {
					container = match.getRight();
				}
				if (container != null) {
					requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, container,
							DifferenceKind.ADD));
				}

				// DELETE object
			} else if (kind == DifferenceKind.DELETE && reference.isContainment()) {

				// -> requires DELETE of the outgoing references and contained objects
				requiredDifferences.addAll(getDELOutgoingReferences(comparison, sourceDifference));
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value.eContents(),
						DifferenceKind.DELETE));

				// -> requires MOVE of contained objects
				requiredDifferences.addAll(getMOVEContainedObjects(comparison, sourceDifference));

				// The DELETE or CHANGE of incoming references are handled in the DELETE reference and CHANGE
				// reference cases.

				// DELETE reference
			} else if ((kind == DifferenceKind.DELETE || isChangeDelete(sourceDifference))
					&& !reference.isContainment()) {

				// -> is required by DELETE of the target object
				requiredByDifferences.addAll(getDifferenceOnGivenObject(comparison, value,
						DifferenceKind.DELETE));

				// MOVE object
			} else if (kind == DifferenceKind.MOVE && reference.isContainment()) {

				EObject container = value.eContainer();

				// -> requires ADD on the container of the object
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, container,
						DifferenceKind.ADD));

				// -> requires MOVE of the container of the object
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, container,
						DifferenceKind.MOVE));

				// CHANGE reference
			} else if (kind == DifferenceKind.CHANGE && !isChangeAdd(comparison, sourceDifference)
					&& !isChangeDelete(sourceDifference)) {

				// -> is required by DELETE of the origin target object
				requiredByDifferences.addAll(getDifferenceOnGivenObject(comparison, MatchUtil.getOriginValue(
						comparison, sourceDifference), DifferenceKind.DELETE));

				// -> requires ADD of the value of the reference (target object) if required
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value, DifferenceKind.ADD));
			}

			sourceDifference.getRequires().addAll(requiredDifferences);
			sourceDifference.getRequiredBy().addAll(requiredByDifferences);

		}

	}

	/**
	 * From a <code>sourceDifference</code> (ADD) on a containment mono-valued reference, it retrieves a
	 * potential DELETE difference on the origin value.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param sourceDifference
	 *            The given difference.
	 * @return The found differences.
	 */
	private Set<ReferenceChange> getDELOriginValueOnContainmentRefSingle(Comparison comparison,
			ReferenceChange sourceDifference) {
		Set<ReferenceChange> result = new HashSet<ReferenceChange>();
		EReference reference = sourceDifference.getReference();
		if (!reference.isMany()) {
			EObject originContainer = MatchUtil.getOriginContainer(comparison, sourceDifference);
			if (originContainer != null) {
				Object originValue = ReferenceUtil.safeEGet(originContainer, reference);
				if (originValue instanceof EObject) {
					result = getDifferenceOnGivenObject(comparison, (EObject)originValue,
							DifferenceKind.DELETE);
				}
			}
		}
		return result;
	}

	/**
	 * Retrieve candidate reference changes based on the given <code>object</code> (value) which are from the
	 * given <code>kind</code>.
	 * 
	 * @param comparison
	 *            the comparison to search in.
	 * @param object
	 *            The given object.
	 * @param kind
	 *            The given kind.
	 * @return The found differences.
	 */
	private Set<ReferenceChange> getDifferenceOnGivenObject(Comparison comparison, EObject object,
			DifferenceKind kind) {
		final Set<ReferenceChange> result = new HashSet<ReferenceChange>();
		for (ReferenceChange diff : filter(comparison.getDifferences(object), ReferenceChange.class)) {
			if (diff.getKind() == kind && diff.getReference().isContainment()) {
				result.add(diff);
			}
		}
		return result;
	}

	/**
	 * Retrieve candidate reference changes from a list of given <code>objects</code>.
	 * 
	 * @see DefaultReqEngine#getDifferenceOnGivenObject(EObject, DifferenceKind).
	 * @param comparison
	 *            the comparison to search in.
	 * @param objects
	 *            The given objects.
	 * @param kind
	 *            The kind of requested differences.
	 * @return The found differences.
	 */
	private Set<ReferenceChange> getDifferenceOnGivenObject(Comparison comparison, List<EObject> objects,
			DifferenceKind kind) {
		Set<ReferenceChange> result = new HashSet<ReferenceChange>();
		for (EObject object : objects) {
			result.addAll(getDifferenceOnGivenObject(comparison, object, kind));
		}
		return result;
	}

	/**
	 * From a <code>sourceDifference</code> (DELETE) on a containment reference, it retrieves potential DELETE
	 * differences on the outgoing references from the value object of the <code>sourceDifference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param sourceDifference
	 *            The given difference.
	 * @return The found differences.
	 */
	private Set<ReferenceChange> getDELOutgoingReferences(Comparison comparison,
			ReferenceChange sourceDifference) {
		Set<ReferenceChange> result = new HashSet<ReferenceChange>();
		EObject value = sourceDifference.getValue();

		final Match valueMatch = comparison.getMatch(value);
		if (valueMatch != null) {
			for (ReferenceChange candidate : filter(valueMatch.getDifferences(), ReferenceChange.class)) {
				if (candidate.getKind() == DifferenceKind.DELETE || isChangeDelete(candidate)) {
					result.add(candidate);
				}
			}
		}

		return result;
	}

	/**
	 * From a <code>sourceDifference</code> (DELETE) on a containment reference, it retrieves potential MOVE
	 * differences on the objects contained in the value object of the <code>sourceDifference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param sourceDifference
	 *            The given difference.
	 * @return The found differences.
	 */
	private Set<ReferenceChange> getMOVEContainedObjects(Comparison comparison,
			ReferenceChange sourceDifference) {
		Set<ReferenceChange> result = new HashSet<ReferenceChange>();
		EObject value = sourceDifference.getValue();
		List<EObject> contents = value.eContents();
		for (EObject content : contents) {
			EObject originObject = MatchUtil.getOriginObject(comparison, content);
			if (originObject != null) {
				for (ReferenceChange difference : filter(comparison.getDifferences(originObject),
						ReferenceChange.class)) {
					if (difference.getReference().isContainment()
							&& difference.getKind() == DifferenceKind.MOVE) {
						result.add(difference);
					}

				}
			}
		}
		return result;
	}

	/**
	 * Check if the given <code>difference</code> is a CHANGE from a null value on a mono-valued reference.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param difference
	 *            The given difference.
	 * @return True if it is a CHANGE from a null value.
	 */
	private static boolean isChangeAdd(Comparison comparison, ReferenceChange difference) {
		boolean result = false;
		EReference reference = difference.getReference();
		if (!reference.isMany() && !reference.isContainment()) {
			Match match = difference.getMatch();
			if (comparison.isThreeWay()) {
				final EObject origin = match.getOrigin();
				result = origin == null || ReferenceUtil.safeEGet(origin, reference) == null;
			} else {
				// two way can't have "remote" diffs. This is an addition if right is null
				final EObject right = match.getRight();
				result = right == null || ReferenceUtil.safeEGet(right, reference) == null;
			}
		}
		return result;
	}

	/**
	 * Check if the given <code>difference</code> is a CHANGE to a null value on a mono-valued reference.
	 * 
	 * @param difference
	 *            The given difference.
	 * @return True if it is a CHANGE to a null value.
	 */
	private static boolean isChangeDelete(ReferenceChange difference) {
		boolean result = false;
		EReference reference = difference.getReference();
		if (!reference.isMany() && !reference.isContainment()) {
			Match match = difference.getMatch();
			if (difference.getSource() == DifferenceSource.LEFT) {
				final EObject left = match.getLeft();
				result = left == null || ReferenceUtil.safeEGet(left, reference) == null;
			} else {
				final EObject right = match.getRight();
				result = right == null || ReferenceUtil.safeEGet(right, reference) == null;
			}
		}
		return result;
	}

}
