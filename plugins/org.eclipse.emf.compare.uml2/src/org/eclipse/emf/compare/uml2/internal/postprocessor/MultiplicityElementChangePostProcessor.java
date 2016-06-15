/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Alexandra Buzila - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.uml2.internal.MultiplicityElementChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Post processor handling conflicts of {@link MultiplicityElementChange MultiplicityElementChanges}.
 * 
 * @author Alexandra Buzila <abuzila@eclipsesource.com>
 */
public class MultiplicityElementChangePostProcessor implements IPostProcessor {

	/**
	 * A predicate that can be used to check whether a {@link Diff} is a {@link MultiplicityElementChange}.
	 */
	private static final Predicate<Diff> IS_MULTIPLICITY_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return diff instanceof MultiplicityElementChange;
		}
	};

	/**
	 * A predicate that can be used to check whether a {@link Diff} is a {@link MultiplicityElementChange} and
	 * its {@link DifferenceSource} is LEFT.
	 */
	private static final Predicate<Diff> IS_LEFT_MULTIPLICITY_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return DifferenceSource.LEFT.equals(diff.getSource()) && IS_MULTIPLICITY_CHANGE.apply(diff);
		}
	};

	/**
	 * A predicate that can be used to check whether a {@link Diff} is a {@link MultiplicityElementChange} and
	 * its {@link DifferenceSource} is RIGHT.
	 */
	private static final Predicate<Diff> IS_RIGHT_MULTIPLICITY_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return DifferenceSource.RIGHT.equals(diff.getSource()) && IS_MULTIPLICITY_CHANGE.apply(diff);
		}
	};

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
				IS_MULTIPLICITY_CHANGE);
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
		final EList<Diff> diffs = comparison.getDifferences();
		final Iterable<Diff> leftChanges = filter(diffs, IS_LEFT_MULTIPLICITY_CHANGE);
		for (Diff leftDiff : leftChanges) {
			final MultiplicityElementChange leftChange = (MultiplicityElementChange)leftDiff;

			final Match match = leftChange.getMatch();
			final Iterable<Diff> rightChanges = filter(diffs, IS_RIGHT_MULTIPLICITY_CHANGE);
			for (Diff rightDiff : rightChanges) {
				MultiplicityElementChange rightChange = (MultiplicityElementChange)rightDiff;
				if (leftChange.getConflict() != null) {
					verifyConflict(match, leftChange, rightChange);
				}
			}
		}
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
		if (!isRefinedByReferenceChange(leftChange) || !isRefinedByReferenceChange(rightChange)) {
			return;
		}
		ReferenceChange leftRefChange = (ReferenceChange)leftChange.getRefinedBy().get(0);
		EReference reference = leftRefChange.getReference();

		if (!affectsReference(reference, rightChange)) {
			return;
		}
		boolean sameValue = sameValue(reference, match.getLeft(), match.getRight());
		updateConflict(leftChange, rightChange, sameValue);

	}

	/**
	 * Updates the conflict kind of the given MultiplicityElementChanges.
	 * 
	 * @param leftChange
	 *            the change from the left side
	 * @param rightChange
	 *            the change from the right side
	 * @param sameValue
	 *            specifies whether the conflicting references have the same value (pseudo conflict) or not
	 *            (real conflict)
	 */
	private void updateConflict(MultiplicityElementChange leftChange, MultiplicityElementChange rightChange,
			boolean sameValue) {
		if (sameValue && leftChange.getConflict().getKind() != ConflictKind.PSEUDO
				&& containsOnlyMultiplicityReferenceChanges(leftChange.getConflict())) {
			leftChange.getConflict().setKind(ConflictKind.PSEUDO);
		} else if (!sameValue && (leftChange.getConflict().getKind() != ConflictKind.REAL
				|| rightChange.getConflict().getKind() != ConflictKind.REAL)) {
			Conflict conflict = leftChange.getConflict();
			conflict.setKind(ConflictKind.REAL);
			// make sure the multiplicity changes' conflict is the real one
			leftChange.setConflict(conflict);
			rightChange.setConflict(conflict);
		}

	}

	/**
	 * Returns true if the prime refining of the multiplicity element change is a reference change.
	 * 
	 * @param change
	 *            the {@link MultiplicityElementChange} to check
	 * @return whether the given change has a {@link ReferenceChange} as its prime refining
	 */
	private boolean isRefinedByReferenceChange(MultiplicityElementChange change) {
		return change.getPrimeRefining() instanceof ReferenceChange;
	}

	/**
	 * Checks if the given conflict contains differences that are not of type
	 * {@link MultiplicityElementChange} or are not {@link ReferenceChange reference changes} of
	 * {@link MultiplicityElement multiplicity elements}.
	 * 
	 * @param conflict
	 *            the conflict to check
	 * @return <code>true</code> if the conflict contains only {@link MultiplicityElementChange} diffs
	 */
	private boolean containsOnlyMultiplicityReferenceChanges(Conflict conflict) {
		for (Diff diff : conflict.getDifferences()) {
			if (diff instanceof MultiplicityElementChange) {
				continue;
			}
			if (!(diff instanceof ReferenceChange)) {
				return false;
			}
			EReference reference = ((ReferenceChange)diff).getReference();
			if (reference != UMLPackage.eINSTANCE.getMultiplicityElement_LowerValue()
					&& reference != UMLPackage.eINSTANCE.getMultiplicityElement_UpperValue()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether the given change affects the specified eReference.
	 * 
	 * @param eReference
	 *            the {@link EReference}
	 * @param change
	 *            the {@link MultiplicityElementChange}
	 * @return true if the given {@link MultiplicityElementChange} contains a refining {@link ReferenceChange}
	 *         affecting the provided {@link EReference}
	 */
	private boolean affectsReference(EReference eReference, MultiplicityElementChange change) {
		for (ReferenceChange diff : filter(change.getRefinedBy(), ReferenceChange.class)) {
			if (diff.getReference() == eReference) {
				return true;
			}
		}
		return false;
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
