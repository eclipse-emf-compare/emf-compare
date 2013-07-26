/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.utils;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.concat;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This utility class provides common methods for navigation over and querying of the Comparison model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ComparisonUtil {
	/** Hides default constructor. */
	private ComparisonUtil() {
		// prevents instantiation
	}

	/**
	 * When merging a {@link Diff}, returns the sub diffs of this given diff, and all associated diffs (see
	 * {@link DiffUtil#getAssociatedDiffs(Iterable, boolean, Diff)}) of these sub diffs.
	 * <p>
	 * If the diff is an {@link org.eclipse.emf.compare.AttributeChange} or a
	 * {@link org.eclipse.emf.compare.ResourceAttachmentChange} , this method will return an empty iterable.
	 * </p>
	 * <p>
	 * If the diff is a {@link ReferenceChange} this method will return all differences contained in the match
	 * that contains the value of the reference change, and all associated diffs of these differences.
	 * </p>
	 * 
	 * @param leftToRight
	 *            the direction of merge.
	 * @return an iterable containing the sub diffs of this given diff, and all associated diffs of these sub
	 *         diffs.
	 * @since 3.0
	 */
	public static Function<Diff, Iterable<Diff>> getSubDiffs(final boolean leftToRight) {
		return getSubDiffs(leftToRight, new LinkedHashSet<Diff>());
	}

	/**
	 * Checks if the given difference is either an addition or a "set" from the default value to a new
	 * reference.
	 * 
	 * @param difference
	 *            The given difference.
	 * @return <code>true</code> if this is an addition or "set" diff.
	 */
	public static boolean isAddOrSetDiff(Diff difference) {
		boolean result = false;
		if (difference.getKind() == DifferenceKind.ADD) {
			result = true;
		} else if (difference.getKind() == DifferenceKind.CHANGE) {
			final EStructuralFeature feature;
			if (difference instanceof ReferenceChange) {
				feature = ((ReferenceChange)difference).getReference();
			} else if (difference instanceof AttributeChange) {
				feature = ((AttributeChange)difference).getAttribute();
			} else {
				feature = null;
			}

			if (feature != null && !feature.isMany()) {
				final Match match = difference.getMatch();
				final EObject source;
				if (match.getComparison().isThreeWay()) {
					source = match.getOrigin();
				} else {
					source = match.getRight();
				}

				if (source == null) {
					result = true;
				} else {
					result = equalToDefault(source, feature);
				}
			}
		}
		return result;
	}

	/**
	 * Checks whether the given feature of the given EObject is set to its default value or the empty String.
	 * 
	 * @param object
	 *            The object which feature value we need to check.
	 * @param feature
	 *            The feature which value we need to check.
	 * @return <code>true</code> is this object's feature is set to a value equal to its default.
	 */
	private static boolean equalToDefault(EObject object, EStructuralFeature feature) {
		final Object value = ReferenceUtil.safeEGet(object, feature);
		final Object defaultValue = feature.getDefaultValue();
		if (value == null) {
			return defaultValue == null;
		}
		return value.equals(feature.getDefaultValue()) || "".equals(value); //$NON-NLS-1$
	}

	/**
	 * Checks if the given difference is either a deletion or a "unset" to the default value.
	 * 
	 * @param difference
	 *            The given difference.
	 * @return <code>true</code> if this is a deletion or "unset" diff.
	 */
	public static boolean isDeleteOrUnsetDiff(Diff difference) {
		boolean result = false;
		if (difference.getKind() == DifferenceKind.DELETE) {
			result = true;
		} else if (difference.getKind() == DifferenceKind.CHANGE) {
			final EStructuralFeature feature;
			if (difference instanceof ReferenceChange) {
				feature = ((ReferenceChange)difference).getReference();
			} else if (difference instanceof AttributeChange) {
				feature = ((AttributeChange)difference).getAttribute();
			} else {
				feature = null;
			}

			if (feature != null && !feature.isMany()) {
				final Match match = difference.getMatch();
				final EObject expectedContainer;
				if (difference.getSource() == DifferenceSource.LEFT) {
					expectedContainer = match.getLeft();
				} else {
					expectedContainer = match.getRight();
				}

				if (expectedContainer == null) {
					result = true;
				} else {
					result = equalToDefault(expectedContainer, feature);
				}
			}
		}
		return result;
	}

	/**
	 * It checks the comparison contains errors.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @return True if it contains errors, false otherwise.
	 */
	public static boolean containsErrors(Comparison comparison) {
		Diagnostic diagnostic = comparison.getDiagnostic();
		if (diagnostic != null) {
			return diagnostic.getSeverity() > Diagnostic.WARNING;
		}
		return false;
	}

	/**
	 * When merging a {@link Diff}, returns the sub diffs of this given diff, and all associated diffs (see
	 * {@link DiffUtil#getAssociatedDiffs(Iterable, boolean, Diff)}) of these sub diffs.
	 * <p>
	 * If the diff is an {@link org.eclipse.emf.compare.AttributeChange} or a
	 * {@link org.eclipse.emf.compare.ResourceAttachmentChange}, this method will return an empty iterable.
	 * </p>
	 * <p>
	 * If the diff is a {@link ReferenceChange} this method will return all differences contained in the match
	 * that contains the value of the reference change, and all associated diffs of these differences.
	 * </p>
	 * 
	 * @param leftToRight
	 *            the direction of merge.
	 * @param processedDiffs
	 *            a set of diffs which have been already processed.
	 * @return an iterable containing the sub diffs of this given diff, and all associated diffs of these sub
	 *         diffs.
	 * @since 3.0
	 */
	private static Function<Diff, Iterable<Diff>> getSubDiffs(final boolean leftToRight,
			final LinkedHashSet<Diff> processedDiffs) {
		return new Function<Diff, Iterable<Diff>>() {
			public Iterable<Diff> apply(Diff diff) {
				if (diff instanceof ReferenceChange) {
					Match matchOfValue = diff.getMatch().getComparison().getMatch(
							((ReferenceChange)diff).getValue());
					if (((ReferenceChange)diff).getReference().isContainment()) {
						final Iterable<Diff> subDiffs = matchOfValue.getAllDifferences();
						addAll(processedDiffs, subDiffs);
						final Iterable<Diff> associatedDiffs = getAssociatedDiffs(diff, subDiffs,
								processedDiffs, leftToRight);
						return ImmutableSet.copyOf(concat(subDiffs, associatedDiffs));
					}
				}
				return ImmutableSet.of();
			}
		};
	}

	/**
	 * When merging a {@link Diff}, returns the associated diffs of the sub diffs of the diff, and all sub
	 * diffs (see {@link DiffUtil#getSubDiffs(boolean)}) of these associated diffs.
	 * <p>
	 * The associated diffs of a diff are :
	 * <p>
	 * - {@link Diff#getRequiredBy()} if the source of the diff is the left side and the direction of the
	 * merge is right to left.
	 * </p>
	 * <p>
	 * - {@link Diff#getRequiredBy()} if the source of the diff is the right side and the direction of the
	 * merge is left to right.
	 * </p>
	 * <p>
	 * - {@link Diff#getRequires()} if the source of the diff is the left side and the direction of the merge
	 * is left to right.
	 * </p>
	 * <p>
	 * - {@link Diff#getRequires()} if the source of the diff is the right side and the direction of the merge
	 * is right to left.
	 * </p>
	 * </p>
	 * 
	 * @param diffRoot
	 *            the given diff.
	 * @param subDiffs
	 *            the iterable of sub diffs for which we want the associated diffs.
	 * @param processedDiffs
	 *            a set of diffs which have been already processed.
	 * @param leftToRight
	 *            the direction of merge.
	 * @return an iterable containing the associated diffs of these given sub diffs, and all sub diffs of
	 *         these associated diffs.
	 * @since 3.0
	 */
	private static Iterable<Diff> getAssociatedDiffs(final Diff diffRoot, Iterable<Diff> subDiffs,
			LinkedHashSet<Diff> processedDiffs, boolean leftToRight) {
		Collection<Diff> associatedDiffs = new HashSet<Diff>();
		for (Diff diff : subDiffs) {
			final Collection<Diff> reqs = new LinkedHashSet<Diff>();
			if (leftToRight) {
				if (diff.getSource() == DifferenceSource.LEFT) {
					reqs.addAll(diff.getRequires());
				} else {
					reqs.addAll(diff.getRequiredBy());
				}
			} else {
				if (diff.getSource() == DifferenceSource.LEFT) {
					reqs.addAll(diff.getRequiredBy());
				} else {
					reqs.addAll(diff.getRequires());
				}
			}
			reqs.remove(diffRoot);
			associatedDiffs.addAll(reqs);
			for (Diff req : reqs) {
				if (!Iterables.contains(subDiffs, req) && !processedDiffs.contains(req)) {
					processedDiffs.add(req);
					addAll(associatedDiffs, getSubDiffs(leftToRight, processedDiffs).apply(req));
				}
			}
		}
		return associatedDiffs;
	}
}
