/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *     Philip Langer - bug 501864
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.anyRefined;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Post processor handling conflicts of {@link MultiplicityElementChange MultiplicityElementChanges}.
 * 
 * @author Alexandra Buzila <abuzila@eclipsesource.com>
 */
public class MultiplicityElementChangePostProcessor implements IPostProcessor {

	/** {@inheritDoc} */
	public void postMatch(Comparison comparison, Monitor monitor) {
		// do nothing
	}

	/** {@inheritDoc} */
	public void postDiff(Comparison comparison, Monitor monitor) {
		// do nothing
	}

	/** {@inheritDoc} */
	public void postRequirements(Comparison comparison, Monitor monitor) {
		// do nothing
	}

	/** {@inheritDoc} */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
		// do nothing
	}

	/** {@inheritDoc} */
	public void postConflicts(Comparison comparison, Monitor monitor) {
		// do nothing
	}

	/** {@inheritDoc} */
	public void postComparison(Comparison comparison, Monitor monitor) {
		updateRequiresAndRefines(comparison);
		verifyConflicts(comparison);
	}

	/**
	 * Update the "refinedBy" and "requiredBy" relationships for the MultiplicityElementChanges.
	 * <p>
	 * If a MultiplicityElementChange is refined by a difference, <b>refiningDiff</b>, which in turn refines
	 * another diff, <b>refinedDiff</b>, then the refinement relationships will be updated such that the
	 * MultiplicityElementChange will refine the <b>refinedDiff</b>, instead of the <b>refiningDiff</b>.
	 * <p>
	 * This is done to avoid having diffs that refine multiple elements. Moreover, since any refined diffs of
	 * a refining diff are marked as required by the {@link UMLPostProcessor}, the requirement relationship
	 * needs to be updated accordingly.
	 * 
	 * @param comparison
	 *            the current comparison
	 */
	private void updateRequiresAndRefines(Comparison comparison) {
		Iterator<Diff> multiplicityChanges = Iterators.filter(comparison.getDifferences().iterator(),
				instanceOf(MultiplicityElementChange.class));
		while (multiplicityChanges.hasNext()) {
			MultiplicityElementChange refChange = (MultiplicityElementChange)multiplicityChanges.next();
			for (Diff refiningDiff : refChange.getRefinedBy()) {
				ArrayList<Diff> refinedChangesToUpdate = new ArrayList<Diff>();
				for (Diff refinedDiff : refiningDiff.getRefines()) {
					if (refinedDiff != refChange) {
						refinedChangesToUpdate.add(refinedDiff);
					}
				}
				for (Diff refined : refinedChangesToUpdate) {
					refined.getRefinedBy().remove(refiningDiff);
					refined.getRefinedBy().add(refChange);
					refined.getRequires().remove(refChange);
				}
			}
		}
	}

	/**
	 * Verifies whether the {@link ConflictKind} of conflicts between MultiplicityChanges is correct.
	 * 
	 * @param comparison
	 *            the comparison containing the conflicts
	 */
	private void verifyConflicts(Comparison comparison) {
		for (Conflict conflict : comparison.getConflicts()) {
			if (all(conflict.getDifferences(), anyRefined(instanceOf(MultiplicityElementChange.class)))) {
				final Iterable<Diff> leftDiffs = collectRefinedDiffs(conflict.getLeftDifferences(),
						instanceOf(MultiplicityElementChange.class));
				for (Diff leftDiff : leftDiffs) {
					final MultiplicityElementChange leftMultiplicityChange = (MultiplicityElementChange)leftDiff;
					final Match match = leftMultiplicityChange.getMatch();
					final Iterable<Diff> rightDiffs = collectRefinedDiffs(conflict.getRightDifferences(),
							instanceOf(MultiplicityElementChange.class));
					for (Diff rightDiff : rightDiffs) {
						verifyConflict(match, leftMultiplicityChange, (MultiplicityElementChange)rightDiff);
					}
				}
			}
		}
	}

	/**
	 * Collects the refined differences that fulfill the given predicate from the given diffs.
	 * 
	 * @param diffs
	 *            The diffs to collect its refined differences from.
	 * @param predicate
	 *            The predicate to be fulfilled.
	 * @return The list of refined differences fulfilling the predicate.
	 */
	private Iterable<Diff> collectRefinedDiffs(List<Diff> diffs, Predicate<Object> predicate) {
		Builder<Diff> builder = ImmutableList.builder();
		for (Diff diff : diffs) {
			builder.addAll(filter(diff.getRefines(), predicate));
		}
		return builder.build();
	}

	/**
	 * Verifies whether the {@link ConflictKind} of conflicts between MultiplicityChanges is correct, for a
	 * given match. Specifically, it checks whether the type of all the diffs refining the multiplicity
	 * reference changes is correct and makes sure that if a change contains both PSEUDO and REAL conflicts,
	 * the conflict of the multiplicity change is marked as REAL.
	 * 
	 * @param match
	 *            the {@link Match} for the object the multiplicity changes refer to
	 * @param leftChange
	 *            the left side change
	 * @param rightChange
	 *            the right side change
	 */
	private void verifyConflict(Match match, MultiplicityElementChange leftChange,
			MultiplicityElementChange rightChange) {
		final Optional<ReferenceChange> leftReferenceChange = tryGetReferenceChange(leftChange);
		final Optional<ReferenceChange> rightReferenceChange = tryGetReferenceChange(rightChange);
		if (!leftReferenceChange.isPresent() || !rightReferenceChange.isPresent()) {
			return;
		}

		final EReference leftReference = leftReferenceChange.get().getReference();
		final EReference rightReference = rightReferenceChange.get().getReference();
		if (!leftReference.equals(rightReference)) {
			return;
		}

		final boolean sameValue = sameValue(leftReference, match.getLeft(), match.getRight());
		updateConflict(leftReferenceChange.get(), rightReferenceChange.get(), sameValue);
	}

	/**
	 * Updates the conflict kind of the given references that refine MultiplicityElementChanges.
	 * 
	 * @param diff
	 *            the change from the left side
	 * @param diff2
	 *            the change from the right side
	 * @param sameValue
	 *            specifies whether the conflicting references have the same value (pseudo conflict) or not
	 *            (real conflict)
	 */
	private void updateConflict(Diff diff, Diff diff2, boolean sameValue) {
		if (sameValue && diff.getConflict().getKind() != ConflictKind.PSEUDO) {
			diff.getConflict().setKind(ConflictKind.PSEUDO);
		} else if (!sameValue && (diff.getConflict().getKind() != ConflictKind.REAL
				|| diff2.getConflict().getKind() != ConflictKind.REAL)) {
			Conflict conflict = diff.getConflict();
			conflict.setKind(ConflictKind.REAL);
			// make sure the multiplicity changes' conflict is the real one
			diff.setConflict(conflict);
			diff2.setConflict(conflict);
		}
	}

	/**
	 * Returns the reference change that refines the given multiplicity element change. A multiplicity change
	 * should only have one refining diff, which is a reference change.
	 * 
	 * @param change
	 *            The {@link MultiplicityElementChange} to get its refining reference change.
	 * @return The refining reference change.
	 */
	private Optional<ReferenceChange> tryGetReferenceChange(MultiplicityElementChange change) {
		final Iterable<ReferenceChange> refChanges = filter(change.getRefinedBy(), ReferenceChange.class);
		return Optional.fromNullable(Iterables.getFirst(refChanges, null));
	}

	/**
	 * Checks whether the given EObjects have the same value for the specified {@link EStructuralFeature}.
	 * 
	 * @param feature
	 *            the {@link EStructuralFeature} to check
	 * @param object1
	 *            the first object
	 * @param object2
	 *            the second object
	 * @return true if the two given {@link EObject}s have the same value for the given
	 *         {@link EStructuralFeature}.
	 */
	private boolean sameValue(EStructuralFeature feature, EObject object1, EObject object2) {
		if (object1 == null || object2 == null) {
			return object1 == object2;
		}
		Object value1 = object1.eGet(feature);
		Object value2 = object2.eGet(feature);
		if (value1 == null || value2 == null) {
			return value1 == value2;
		}
		if (value1 instanceof EObject && value2 instanceof EObject) {
			return EcoreUtil.equals((EObject)value1, (EObject)value2);
		}
		return value1.equals(value2);
	}

}
