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
package org.eclipse.emf.compare.diff;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.scope.AbstractComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The diff engine is in charge of actually computing the differences between the objects mapped by a
 * {@link Match} object.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific differences, refinements of differences or even higher level differences might be
 * necessary.
 * </p>
 * TODO document available extension possibilities.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultDiffEngine {
	/**
	 * The comparison for which we are detecting differences. Should only be accessed through
	 * {@link #getComparison()}.
	 */
	private Comparison currentComparison;

	/**
	 * The diff processor that will be used by this engine. Should only be instantiated through
	 * {@link #createDiffProcessor()} and accessed by {@link #getDiffProcessor()}.
	 */
	private IDiffProcessor diffProcessor;

	/**
	 * This will complete the input <code>comparison</code> by iterating over the {@link Match matches} it
	 * contain, filling in the differences it can detect for each distinct Match. This default implementation
	 * does not make use of the given {@link AbstractComparisonScope comparison scope} since we expect the
	 * match engine to have mapped all objects from all three sides.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param scope
	 *            The input scope of this comparison.
	 */
	public void diff(Comparison comparison, AbstractComparisonScope scope) {
		this.currentComparison = comparison;
		diffProcessor = createDiffProcessor();

		for (Match rootMatch : comparison.getMatches()) {
			checkForDifferences(rootMatch);
		}
	}

	/**
	 * Checks the given {@link Match}'s sides for potential differences. Will recursively check for
	 * differences on submatches.
	 * 
	 * @param match
	 *            The match that is to be checked.
	 */
	protected void checkForDifferences(Match match) {
		final FeatureFilter featureFilter = createFeatureFilter();
		final Iterator<EReference> references = featureFilter.getReferencesToCheck(match);
		while (references.hasNext()) {
			final EReference reference = references.next();
			final boolean considerOrdering = featureFilter.checkForOrderingChanges(reference);
			if (reference.isContainment()) {
				computeContainmentDifference(match, reference, considerOrdering);
			} else {
				computeDifferences(match, reference, considerOrdering);
			}
		}
		for (Match submatch : match.getSubmatches()) {
			checkForDifferences(submatch);
		}
	}

	/**
	 * Computes the differences between the sides of the given match for the given <code>reference</code>.
	 * <p>
	 * Note that once here, we know that <code>reference</code> is not a containment reference.
	 * </p>
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param reference
	 *            The reference which values are to be checked.
	 * @param checkOrdering
	 *            <code>true</code> if we should consider ordering changes on this reference,
	 *            <code>false</code> otherwise.
	 */
	protected void computeDifferences(Match match, EReference reference, boolean checkOrdering) {
		final EObject left = match.getLeft();
		final EObject right = match.getRight();
		final EObject origin = match.getOrigin();

		// This will only be used for iteration, once. use the original list
		final Iterable<EObject> leftValues = Iterables.filter(getValue(left, reference), EObject.class);
		/*
		 * These two will be used mainly for lookup, we'll transform them to Sets here. Linked set for the
		 * rightValues since we'll iterate over its values once, a plain hash set for the origin as it will
		 * only be used for lookup purposes
		 */
		final Set<EObject> rightValues = Sets.newLinkedHashSet(Iterables.filter(getValue(right, reference),
				EObject.class));
		final Set<EObject> originValues = Sets.newHashSet(Iterables.filter(getValue(origin, reference),
				EObject.class));

		for (EObject value : leftValues) {
			final Match valueMatch = getComparison().getMatch(value);

			if (valueMatch != null) {
				// Is this value present in the right side?
				final EObject rightMatch = valueMatch.getRight();

				if (rightMatch == null || rightValues.contains(rightMatch)) {
					// No need to go any further
					final DifferenceKind kind;
					final DifferenceSource source;
					if (originValues.contains(valueMatch.getOrigin())) {
						source = DifferenceSource.RIGHT;
						kind = DifferenceKind.DELETE;
					} else {
						source = DifferenceSource.LEFT;
						kind = DifferenceKind.ADD;
					}
					getDiffProcessor().referenceChange(match, reference, value, kind, source);
				} else if (checkOrdering) {
					// Value is present in both left and right lists. We can only have a diff on ordering.
					rightValues.remove(rightMatch);
					// FIXME CODEME
				}
			} else {
				// this value is out of the comparison scope
			}
		}

		for (EObject value : rightValues) {
			final Match valueMatch = getComparison().getMatch(value);

			if (valueMatch != null) {
				// This value was not in the left list, so it can only be a diff.
				final DifferenceKind kind;
				final DifferenceSource source;
				if (originValues.contains(valueMatch.getOrigin())) {
					source = DifferenceSource.LEFT;
					kind = DifferenceKind.DELETE;
				} else {
					source = DifferenceSource.RIGHT;
					kind = DifferenceKind.ADD;
				}
				getDiffProcessor().referenceChange(match, reference, value, kind, source);
			} else {
				// this value is out of the comparison scope
			}
		}
	}

	/**
	 * Containment differences will be treated differently than plain references since the corresponding Match
	 * elements themselves contain all information we might need for difference detection on these, ordering
	 * excepted.
	 * 
	 * @param match
	 *            The match which sides we need to check for potential differences.
	 * @param reference
	 *            The reference which values are to be checked.
	 * @param checkOrdering
	 *            <code>true</code> if we should consider ordering changes on this reference,
	 *            <code>false</code> otherwise.
	 */
	protected void computeContainmentDifference(Match match, EReference reference, boolean checkOrdering) {
		final EObject left = match.getLeft();

		// This will only be used for iteration, once. use the original list
		final Iterable<EObject> leftValues = Iterables.filter(getValue(left, reference), EObject.class);

		for (EObject leftValue : leftValues) {
			final Match valueMatch = getComparison().getMatch(leftValue);

			if (valueMatch != null) {
				computContainmentDiffForLeftValue(match, reference, valueMatch, checkOrdering);
			} else {
				// this value is out of the comparison scope
			}
		}

		final EObject right = match.getRight();
		// This will only be used for iteration, once. use the original list
		final Iterable<EObject> rightValues = Iterables.filter(getValue(right, reference), EObject.class);

		for (EObject rightValue : rightValues) {
			final Match valueMatch = getComparison().getMatch(rightValue);

			if (valueMatch == null || valueMatch.getLeft() != null
					&& match.getLeft() == valueMatch.getLeft().eContainer()
					&& reference == valueMatch.getLeft().eContainmentFeature()) {
				// Either out of scope or handled by the iteration on the left side
			} else {
				computContainmentDiffForRightValue(match, reference, valueMatch, checkOrdering);
			}
		}
	}

	/**
	 * This will be called by the diff engine to compute containment differences on the given
	 * <code>value</code>, which {@link Match#getLeft() left side} is known to be in the
	 * <code>parent.reference</code>'s content list.
	 * <p>
	 * The necessary sanity checks have been made to know that <code>value.getLeft()</code> is not
	 * <code>null</code> and part of <code>parent.eGet(reference)</code>. We also know that reference is a
	 * containment reference.
	 * </p>
	 * 
	 * @param parent
	 *            The Match which containment references we are currently checking for differences.
	 * @param reference
	 *            The reference of which content <code>value</code> is known to be a part.
	 * @param value
	 *            The value of the given containment reference which is to be checked here.
	 * @param checkOrdering
	 *            <code>true</code> if we should consider ordering changes on this reference,
	 *            <code>false</code> otherwise.
	 */
	protected void computContainmentDiffForLeftValue(Match parent, EReference reference, Match value,
			boolean checkOrdering) {
		final EObject leftValue = value.getLeft();
		final EObject rightValue = value.getRight();
		final EObject originValue = value.getOrigin();

		if (rightValue == null) {
			if (originValue == null) {
				// This value has been added in the left side
				getDiffProcessor().referenceChange(parent, reference, leftValue, DifferenceKind.ADD,
						DifferenceSource.LEFT);
			} else if (parent.getOrigin() == originValue.eContainer()
					&& reference == originValue.eContainmentFeature()) {
				// This value is also in the origin, in the same container. It has thus been removed
				// from the right side
				getDiffProcessor().referenceChange(parent, reference, originValue, DifferenceKind.DELETE,
						DifferenceSource.RIGHT);
				// FIXME check ordering if needed
			} else {
				/*
				 * This value is also in the origin, it has thus been deleted from the right side. But it was
				 * not in the same container; so there is also a MOVE difference in the left side. The delete
				 * of the right side, however, is in another match element. It will be detected later on.
				 */
				getDiffProcessor().referenceChange(parent, reference, leftValue, DifferenceKind.MOVE,
						DifferenceSource.LEFT);
			}
		} else if (originValue == null) {
			// This value is in both left and right sides, but not in the origin
			if (parent.getRight() == rightValue.eContainer() && reference == rightValue.eContainmentFeature()) {
				// they are in the same container; so there's actually no diff here : the same
				// modification has been made in both sides
				// FIXME there could be an ordering diff though
			} else {
				/*
				 * This is actually a conflict : same object added in two distinct containers. The addition in
				 * the right side, however, is in another match element and will be detected later on.
				 */
				getDiffProcessor().referenceChange(parent, reference, leftValue, DifferenceKind.ADD,
						DifferenceSource.LEFT);
			}
		} else {
			// This value is on all three sides
			if (parent.getRight() == rightValue.eContainer() && reference == rightValue.eContainmentFeature()) {
				/*
				 * Even if the value is in another container in in the origin, it is in the same container on
				 * both left and right sides. No diff here as the same modification has been made on both
				 * sides.
				 */
				// FIXME there could be an ordering diff though
			} else if (parent.getOrigin() != originValue.eContainer()
					|| reference != originValue.eContainmentFeature()) {
				// value present on all sides, but its container has changed in the left side as
				// compared to the origin
				getDiffProcessor().referenceChange(parent, reference, leftValue, DifferenceKind.MOVE,
						DifferenceSource.LEFT);
			} else {
				// The value has been moved in the right side ... but that's in another Match element
				// FIXME there could also be a diff on the ordering
			}
		}
	}

	/**
	 * This will be called by the diff engine to compute containment differences on the given
	 * <code>value</code>, which {@link Match#getRight() right side} is known to be in the
	 * <code>parent.reference</code>'s content list.
	 * <p>
	 * The necessary sanity checks have been made to know that <code>value.getRight()</code> is not
	 * <code>null</code> and part of <code>parent.getRight().eGet(reference)</code>. We also know that
	 * <code>value.getLeft()</code> is <b>not</b> a part of <code>parent.getLeft().eGet(reference)</code>
	 * since these have already been taken care of by
	 * {@link #computContainmentDiffForLeftValue(Match, EReference, Match, boolean)}.
	 * </p>
	 * 
	 * @param parent
	 *            The Match which containment references we are currently checking for differences.
	 * @param reference
	 *            The reference of which content <code>value</code> is known to be a part.
	 * @param value
	 *            The value of the given containment reference which is to be checked here.
	 * @param checkOrdering
	 *            <code>true</code> if we should consider ordering changes on this reference,
	 *            <code>false</code> otherwise.
	 */
	protected void computContainmentDiffForRightValue(Match parent, EReference reference, Match value,
			boolean checkOrdering) {
		final EObject leftValue = value.getLeft();
		final EObject rightValue = value.getRight();
		final EObject originValue = value.getOrigin();

		if (leftValue == null) {
			if (originValue == null) {
				// This value has been added in the right side
				getDiffProcessor().referenceChange(parent, reference, rightValue, DifferenceKind.ADD,
						DifferenceSource.RIGHT);
			} else if (parent.getOrigin() == originValue.eContainer()
					&& reference == originValue.eContainmentFeature()) {
				// This value is also in the origin, in the same container. It has thus been removed
				// from the left side
				getDiffProcessor().referenceChange(parent, reference, originValue, DifferenceKind.DELETE,
						DifferenceSource.LEFT);
				// FIXME check ordering if needed
			} else {
				/*
				 * This value is also in the origin, it has thus been deleted from the left side. But it was
				 * not in the same container; so there is also a MOVE difference in the right side. The delete
				 * of the left side, however, is in another match element. It will be detected later on.
				 */
				getDiffProcessor().referenceChange(parent, reference, rightValue, DifferenceKind.MOVE,
						DifferenceSource.RIGHT);
			}
		} else if (originValue == null) {
			// This value is in both left and right sides, but not in the origin
			// We also know that they are not in the same container (handled by the loop on the left side)
			/*
			 * This is actually a conflict : same object added in two distinct containers. The addition in the
			 * left side, however, is in another match element and will be detected later on.
			 */
			getDiffProcessor().referenceChange(parent, reference, rightValue, DifferenceKind.ADD,
					DifferenceSource.RIGHT);
		} else {
			// This value is on all three sides
			// We also know that they are not in the same container (handled by the loop on the left side)
			if (parent.getOrigin() != originValue.eContainer()
					|| reference != originValue.eContainmentFeature()) {
				// value present on all sides, but its container has changed in the right side as
				// compared to the origin
				getDiffProcessor().referenceChange(parent, reference, rightValue, DifferenceKind.MOVE,
						DifferenceSource.RIGHT);
			} else {
				// The value has been moved in the left side ... but that's in another Match element
				// FIXME there could also be a diff on the ordering
			}
		}
	}

	/**
	 * Checks whether the given <code>value</code> is contained within the given <code>container</code>,
	 * through the given <code>containmentReference</code>.
	 * 
	 * @param container
	 *            The expected container of <code>value</code>.
	 * @param containmentReference
	 *            The reference of <code>container</code> within which we expect to find <code>value</code>.
	 * @param value
	 *            The value which container we are checking.
	 * @return <code>true</code> if the given value is contained within the expected reference of the expected
	 *         container, <code>false</code> otherwise.
	 */
	protected static boolean isContainedBy(EObject container, EReference containmentReference, EObject value) {
		return container == value.eContainer() && containmentReference == value.eContainmentFeature();
	}

	/**
	 * This default implementation considers all values as lists. This utility simply allows us to retrieve
	 * the value of a given value as an iterable.
	 * 
	 * @param object
	 *            The object for which feature we need a value.
	 * @param feature
	 *            The actual feature of which we need the value.
	 * @return The value of the given <code>feature</code> for the given <code>object</code> as a list. An
	 *         empty list if this object has no value for that feature or if the object is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	protected static Iterable<Object> getValue(EObject object, EStructuralFeature feature) {
		if (object != null) {
			Object value = object.eGet(feature, false);
			final Iterable<Object> asList;
			if (value instanceof Iterable) {
				asList = (Iterable<Object>)value;
			} else {
				asList = Collections.singleton(value);
			}
			return asList;
		}
		return Collections.emptyList();
	}

	/**
	 * This will be used in order to create the diff processor that is to be used by this diff engine.
	 * 
	 * @return The diff processor of this diff engine. Will only be called once per call to
	 *         {@link #diff(Comparison, AbstractComparisonScope)}.
	 */
	protected IDiffProcessor createDiffProcessor() {
		return new DiffBuilder();
	}

	/**
	 * This will return the diff processor that has been created through {@link #createDiffProcessor()} for
	 * this differencing process.
	 * 
	 * @return The diff processor to notify of difference detections.
	 */
	protected final IDiffProcessor getDiffProcessor() {
		return diffProcessor;
	}

	/**
	 * This will be used in order to create the {@link FeatureFilter} that should be used by this engine to
	 * determine the structural features on which it is to try and detect differences.
	 * 
	 * @return The newly created feature filter.
	 */
	protected FeatureFilter createFeatureFilter() {
		return new FeatureFilter();
	}

	/**
	 * Returns the comparison for which we are currently detecting differences.
	 * 
	 * @return The comparison for which we are currently detecting differences.
	 */
	protected Comparison getComparison() {
		return currentComparison;
	}
}
