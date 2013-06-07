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

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
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
	 * @see org.eclipse.emf.compare.req.IReqEngine#computeRequirements(Comparison, Monitor)
	 */
	public void computeRequirements(Comparison comparison, Monitor monitor) {
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

		Set<Diff> requiredDifferences = new HashSet<Diff>();
		Set<Diff> requiredByDifferences = new HashSet<Diff>();

		Match match = difference.getMatch();
		EObject value = getValue(comparison, difference);
		DifferenceKind kind = difference.getKind();

		if (value != null) {
			boolean isAddition = isAddOrSetDiff(difference);
			boolean isDeletion = !isAddition && isDeleteOrUnsetDiff(difference);

			// ADD object
			if (isAddition && isContainment(difference)) {

				// -> requires ADD on the container of the object
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value.eContainer(),
						DifferenceKind.ADD));

				// -> requires DELETE of the origin value on the same containment mono-valued reference
				requiredDifferences.addAll(getDELOriginValueOnContainmentRefSingle(comparison, difference));

				// ADD reference
			} else if (isAddition) {

				// -> requires ADD of the value of the reference (target object)
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value, DifferenceKind.ADD));

				// -> requires ADD of the object containing the reference
				final EObject container = MatchUtil.getContainer(comparison, difference);
				if (container != null) {
					requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, container,
							DifferenceKind.ADD));
				}
				requiredDifferences.addAll(Collections2.filter(match.getDifferences(), and(
						instanceOf(ResourceAttachmentChange.class), ofKind(DifferenceKind.ADD))));

				// DELETE object
			} else if (isDeletion && isContainment(difference)) {

				// -> requires DELETE of the outgoing references and contained objects
				requiredDifferences.addAll(getDELOutgoingReferences(comparison, difference));
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value.eContents(),
						DifferenceKind.DELETE));

				// -> requires MOVE of contained objects
				requiredDifferences.addAll(getMOVEContainedObjects(comparison, difference));

				// The DELETE or CHANGE of incoming references are handled in the DELETE reference and CHANGE
				// reference cases.

				// DELETE reference
			} else if (isDeletion) {

				// -> is required by DELETE of the target object
				requiredByDifferences.addAll(getDifferenceOnGivenObject(comparison, value,
						DifferenceKind.DELETE));

				// MOVE object
			} else if (kind == DifferenceKind.MOVE && isContainment(difference)) {

				EObject container = value.eContainer();

				// -> requires ADD on the container of the object
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, container,
						DifferenceKind.ADD));

				// -> requires MOVE of the container of the object
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, container,
						DifferenceKind.MOVE));

				// CHANGE reference
			} else if (kind == DifferenceKind.CHANGE && !isAddition && !isDeletion) {

				// -> is required by DELETE of the origin target object
				requiredByDifferences.addAll(getDifferenceOnGivenObject(comparison, MatchUtil.getOriginValue(
						comparison, (ReferenceChange)difference), DifferenceKind.DELETE));

				// -> requires ADD of the value of the reference (target object) if required
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value, DifferenceKind.ADD));
			}

			difference.getRequires().addAll(requiredDifferences);
			difference.getRequiredBy().addAll(requiredByDifferences);
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
	private Set<Diff> getDELOriginValueOnContainmentRefSingle(Comparison comparison, Diff sourceDifference) {
		Set<Diff> result = new HashSet<Diff>();
		if (!(sourceDifference instanceof ReferenceChange)) {
			return result;
		}
		EReference reference = ((ReferenceChange)sourceDifference).getReference();
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
	private Set<Diff> getDifferenceOnGivenObject(Comparison comparison, EObject object, DifferenceKind kind) {
		final Set<Diff> result = new LinkedHashSet<Diff>();
		for (Diff diff : filter(comparison.getDifferences(object), isRequiredContainmentChange(object, kind))) {
			result.add(diff);
		}
		return result;
	}

	/**
	 * Will be used to filter out of a list of differences all those that relate to containment changes on the
	 * given object (a containment reference change or a resource attachment change if the given object has no
	 * direct container.
	 * 
	 * @param object
	 *            The object for which we seek containmnent differences.
	 * @param kind
	 *            The kind of difference we seek.
	 * @return The created predicate.
	 */
	private Predicate<? super Diff> isRequiredContainmentChange(final EObject object,
			final DifferenceKind kind) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input.getKind() != kind) {
					return false;
				}

				boolean result = false;
				if (input instanceof ReferenceChange
						&& ((ReferenceChange)input).getReference().isContainment()) {
					result = true;
				} else if (input instanceof ResourceAttachmentChange && object.eContainer() == null) {
					result = true;
				}
				return result;
			}
		};
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
	private Set<Diff> getDifferenceOnGivenObject(Comparison comparison, List<EObject> objects,
			DifferenceKind kind) {
		Set<Diff> result = new HashSet<Diff>();
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
	private Set<ReferenceChange> getDELOutgoingReferences(Comparison comparison, Diff sourceDifference) {
		Set<ReferenceChange> result = new HashSet<ReferenceChange>();

		EObject value = getValue(comparison, sourceDifference);

		if (value != null) {
			final Match valueMatch = comparison.getMatch(value);
			if (valueMatch != null) {
				for (ReferenceChange candidate : filter(valueMatch.getDifferences(), ReferenceChange.class)) {
					if (candidate.getKind() == DifferenceKind.DELETE || isDeleteOrUnsetDiff(candidate)) {
						result.add(candidate);
					}
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
	private Set<ReferenceChange> getMOVEContainedObjects(Comparison comparison, Diff sourceDifference) {
		Set<ReferenceChange> result = new HashSet<ReferenceChange>();
		EObject value = getValue(comparison, sourceDifference);
		if (value != null) {
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
		}
		return result;
	}

	/**
	 * Checks whether the given diff corresponds to a containment change. This holds true for differences on
	 * containment references' values, but also for resource attachment changes.
	 * 
	 * @param diff
	 *            The diff to consider.
	 * @return <code>true</code> if the given {@code diff} is to be considered a containment change,
	 *         <code>false</code> otherwise.
	 */
	private static boolean isContainment(Diff diff) {
		return diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()
				|| diff instanceof ResourceAttachmentChange;
	}

	/**
	 * Retrieves the "value" of the given containment change. This will be either the "value" field of a
	 * ReferenceChange, or the side of the parent match for a resource attachment change.
	 * 
	 * @param comparison
	 *            The comparison during which this {@code diff} was detected.
	 * @param diff
	 *            The diff which value we are to retrieve.
	 * @return The "value" of the given containment change.
	 */
	private static EObject getValue(Comparison comparison, Diff diff) {
		EObject value = null;
		if (diff instanceof ReferenceChange) {
			value = ((ReferenceChange)diff).getValue();
		} else if (diff instanceof ResourceAttachmentChange) {
			value = MatchUtil.getContainer(comparison, diff);
		}
		return value;
	}

}
