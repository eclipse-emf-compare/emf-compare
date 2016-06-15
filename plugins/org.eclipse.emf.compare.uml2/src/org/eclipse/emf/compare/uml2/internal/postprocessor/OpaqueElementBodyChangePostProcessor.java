/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.postprocessor;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.conflict.DefaultConflictDetector;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.uml2.internal.postprocessor.util.UMLCompareUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * This post-processor or IConflictDetector adds conflicts specific to opaque element body changes.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class OpaqueElementBodyChangePostProcessor extends DefaultConflictDetector implements IPostProcessor {

	/**
	 * A predicate for {@link Diff} that can be used to check whether the {@link Diff} is a
	 * {@link OpaqueElementBodyChange}.
	 */
	private static final Predicate<Diff> IS_OPAQUE_ELEMENT_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return diff instanceof OpaqueElementBodyChange;
		}
	};

	/**
	 * A predicate for {@link Diff} that can be used to check whether the {@link Diff} is a
	 * {@link OpaqueElementBodyChange} and its {@link DifferenceSource} is LEFT.
	 */
	private static final Predicate<Diff> IS_LEFT_OPAQUE_ELEMENT_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return DifferenceSource.LEFT.equals(diff.getSource()) && IS_OPAQUE_ELEMENT_CHANGE.apply(diff);
		}
	};

	/**
	 * A predicate for {@link Diff} that can be used to check whether the {@link Diff} is a
	 * {@link OpaqueElementBodyChange} and its {@link DifferenceSource} is RIGHT.
	 */
	private static final Predicate<Diff> IS_RIGHT_OPAQUE_ELEMENT_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff diff) {
			return DifferenceSource.RIGHT.equals(diff.getSource()) && IS_OPAQUE_ELEMENT_CHANGE.apply(diff);
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see IPostProcessor#postMatch(Comparison, Monitor)
	 */
	public void postMatch(Comparison comparison, Monitor monitor) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IPostProcessor#postDiff(Comparison, Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IPostProcessor#postRequirements(Comparison, Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IPostProcessor#postEquivalences(Comparison, Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IPostProcessor#postConflicts(Comparison, Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IPostProcessor#postComparison(Comparison, Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {
		detect(comparison, monitor);
	}

	/**
	 * Detects and adds conflicts related to changes of the language and bodies of opaque actions, behaviors,
	 * and expressions.
	 * 
	 * @param comparison
	 *            The comparison to check for conflicts.
	 * @param monitor
	 *            The monitor to use for progress reporting.
	 */
	@Override
	public void detect(Comparison comparison, Monitor monitor) {
		final EList<Diff> diffs = comparison.getDifferences();
		final Iterable<Diff> leftBodyChanges = filter(diffs, IS_LEFT_OPAQUE_ELEMENT_CHANGE);
		for (Diff leftDiff : leftBodyChanges) {
			final OpaqueElementBodyChange leftBodyChange = (OpaqueElementBodyChange)leftDiff;
			final EObject leftOpaqueElement = leftBodyChange.getDiscriminant();
			final Match opaqueElementMatch = comparison.getMatch(leftOpaqueElement);
			final EList<Diff> rightDiffs = comparison.getDifferences(opaqueElementMatch.getRight());
			final Iterable<Diff> rightBodyChanges = filter(rightDiffs, IS_RIGHT_OPAQUE_ELEMENT_CHANGE);
			for (Diff rightDiff : rightBodyChanges) {
				OpaqueElementBodyChange rightBodyChange = (OpaqueElementBodyChange)rightDiff;
				if (isConflicting(leftBodyChange, rightBodyChange)) {
					final Conflict conflict = createRealConflict(leftBodyChange, rightBodyChange);
					comparison.getConflicts().add(conflict);
				}
			}
		}
	}

	/**
	 * Specifies whether the given {@code bodyChange1} is conflicting with the given {@code bodyChange2}. Note
	 * that the conflict relation among differences is symmetric, that means {@code bodyChange1} and
	 * {@code bodyChange2} can be switched and this method returns the same result.
	 * 
	 * @param bodyChange1
	 *            The one {@code OpaqueElementBodyChange} to check.
	 * @param bodyChange2
	 *            The other {@code OpaqueElementBodyChange} to check.
	 * @return <code>true</code> if {@code bodyChange1} and {@code bodyChange2} are conflicting,
	 *         <code>false</code> otherwise.
	 */
	private boolean isConflicting(OpaqueElementBodyChange bodyChange1, OpaqueElementBodyChange bodyChange2) {
		boolean areConflicting = false;
		if (concernSameLanguage(bodyChange1, bodyChange2)) {
			if (areDifferenceKindChange(bodyChange1, bodyChange2)) {
				areConflicting = isThreeWayTextConflict(bodyChange1);
			} else if (areDifferenceKindChangeAndDelete(bodyChange1, bodyChange2)) {
				areConflicting = true;
			} else if (areDifferenceKindAdd(bodyChange1, bodyChange2)) {
				areConflicting = true;
			} else if (areDifferenceKindMove(bodyChange1, bodyChange2)) {
				areConflicting = true;
			}
		}
		return areConflicting;
	}

	/**
	 * Specifies whether the given {@code bodyChange1} and {@code bodyChange2} concern the same language
	 * value.
	 * 
	 * @param bodyChange1
	 *            The one {@code OpaqueElementBodyChange} to check.
	 * @param bodyChange2
	 *            The other {@code OpaqueElementBodyChange} to check.
	 * @return <code>true</code> if {@code bodyChange1} and {@code bodyChange2} concern the same language
	 *         value, <code>false</code> otherwise.
	 */
	private boolean concernSameLanguage(OpaqueElementBodyChange bodyChange1,
			OpaqueElementBodyChange bodyChange2) {
		return bodyChange1.getLanguage().equals(bodyChange2.getLanguage());
	}

	/**
	 * Specifies whether the given {@code bodyChange1} and {@code bodyChange2} both are of
	 * {@link DifferenceKind} CHANGE.
	 * 
	 * @param bodyChange1
	 *            The one {@code OpaqueElementBodyChange} to check.
	 * @param bodyChange2
	 *            The other {@code OpaqueElementBodyChange} to check.
	 * @return <code>true</code> if {@code bodyChange1} and {@code bodyChange2} both are of
	 *         {@link DifferenceKind} CHANGE, <code>false</code> otherwise.
	 */
	private boolean areDifferenceKindChange(OpaqueElementBodyChange bodyChange1,
			OpaqueElementBodyChange bodyChange2) {
		return DifferenceKind.CHANGE.equals(bodyChange1.getKind())
				&& DifferenceKind.CHANGE.equals(bodyChange2.getKind());
	}

	/**
	 * Specifies whether the given {@code bodyChange1} and {@code bodyChange2} both are of
	 * {@link DifferenceKind} ADD.
	 * 
	 * @param bodyChange1
	 *            The one {@code OpaqueElementBodyChange} to check.
	 * @param bodyChange2
	 *            The other {@code OpaqueElementBodyChange} to check.
	 * @return <code>true</code> if {@code bodyChange1} and {@code bodyChange2} both are of
	 *         {@link DifferenceKind} ADD, <code>false</code> otherwise.
	 */
	private boolean areDifferenceKindAdd(OpaqueElementBodyChange bodyChange1,
			OpaqueElementBodyChange bodyChange2) {
		return DifferenceKind.ADD.equals(bodyChange1.getKind())
				&& DifferenceKind.ADD.equals(bodyChange2.getKind());
	}

	/**
	 * Specifies whether the given {@code bodyChange1} is of {@link DifferenceKind} CHANGE and
	 * {@code bodyChange2} of {@link DifferenceKind} DELETE or vice versa.
	 * 
	 * @param bodyChange1
	 *            The one {@code OpaqueElementBodyChange} to check.
	 * @param bodyChange2
	 *            The other {@code OpaqueElementBodyChange} to check.
	 * @return <code>true</code> if {@code bodyChange1} and {@code bodyChange2} are of {@link DifferenceKind}
	 *         CHANGE and {@link DifferenceKind} DELETE or vice versa.
	 */
	private boolean areDifferenceKindChangeAndDelete(OpaqueElementBodyChange bodyChange1,
			OpaqueElementBodyChange bodyChange2) {
		return (DifferenceKind.CHANGE.equals(bodyChange1.getKind())
				&& DifferenceKind.DELETE.equals(bodyChange2.getKind()))
				|| (DifferenceKind.DELETE.equals(bodyChange1.getKind())
						&& DifferenceKind.CHANGE.equals(bodyChange2.getKind()));
	}

	/**
	 * Specifies whether the given {@code bodyChange1} and {@code bodyChange2} both are of
	 * {@link DifferenceKind} MOVE.
	 * 
	 * @param bodyChange1
	 *            The one {@code OpaqueElementBodyChange} to check.
	 * @param bodyChange2
	 *            The other {@code OpaqueElementBodyChange} to check.
	 * @return <code>true</code> if {@code bodyChange1} and {@code bodyChange2} both are of
	 *         {@link DifferenceKind} MOVE, <code>false</code> otherwise.
	 */
	private boolean areDifferenceKindMove(OpaqueElementBodyChange bodyChange1,
			OpaqueElementBodyChange bodyChange2) {
		return DifferenceKind.MOVE.equals(bodyChange1.getKind())
				&& DifferenceKind.MOVE.equals(bodyChange2.getKind());
	}

	/**
	 * Specifies whether the given {@code bodyChange} is a non-mergeable text change.
	 * <p>
	 * Changes are non-mergeable if they cannot be merged with opposite changes using a line-based three-way
	 * merge algorithm.
	 * </p>
	 * 
	 * @param bodyChange
	 *            The {@code OpaqueElementBodyChange} to check.
	 * @return <code>true</code> if the change is conflicting (i.e., non-mergeable), <code>false</code>
	 *         otherwise.
	 */
	private boolean isThreeWayTextConflict(OpaqueElementBodyChange bodyChange) {
		if (bodyChange.getMatch().getOrigin() == null) {
			return false;
		}

		final Match match = bodyChange.getMatch();
		final EObject originContainer = match.getOrigin();
		final EObject leftContainer = match.getLeft();
		final EObject rightContainer = match.getRight();

		final String language = bodyChange.getLanguage();
		final String originBody = UMLCompareUtil.getOpaqueElementBody(originContainer, language);
		final String leftBody = UMLCompareUtil.getOpaqueElementBody(leftContainer, language);
		final String rightBody = UMLCompareUtil.getOpaqueElementBody(rightContainer, language);

		return !isMergeableText(leftBody, rightBody, originBody);
	}

	/**
	 * Creates and returns a {@link Conflict} for the two given {@link OpaqueElementBodyChange changes},
	 * {@code bodyChange1} and {@code bodyChange2}.
	 * 
	 * @param bodyChange1
	 *            The one {@link OpaqueElementBodyChange} to create a conflict for.
	 * @param bodyChange2
	 *            The other {@link OpaqueElementBodyChange} to create a conflict for.
	 * @return The created conflict.
	 */
	private Conflict createRealConflict(final OpaqueElementBodyChange bodyChange1,
			OpaqueElementBodyChange bodyChange2) {
		Conflict conflict = CompareFactory.eINSTANCE.createConflict();
		conflict.setKind(ConflictKind.REAL);
		conflict.getDifferences().add(bodyChange1);
		conflict.getDifferences().add(bodyChange2);
		return conflict;
	}

}
