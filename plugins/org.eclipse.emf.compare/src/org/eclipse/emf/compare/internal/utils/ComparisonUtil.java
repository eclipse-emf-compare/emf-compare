/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - added #getExpectedSide
 *******************************************************************************/
package org.eclipse.emf.compare.internal.utils;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * This utility class provides common methods for navigation over and querying of the Comparison model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ComparisonUtil {

	/**
	 * Predicate to know if the given diff respects the requirements of a cascading diff.
	 */
	private static final Predicate<Diff> CASCADING_DIFF = and(not(hasConflict(ConflictKind.REAL)),
			not(instanceOf(ResourceAttachmentChange.class)));

	/** Hides default constructor. */
	private ComparisonUtil() {
		// prevents instantiation
	}

	/**
	 * When merging a {@link Diff}, returns the sub diffs of this given diff, and all associated diffs (see
	 * {@link DiffUtil#getAssociatedDiffs(Iterable, boolean, Diff)}) of these sub diffs.
	 * <p>
	 * If the diff is an {@link org.eclipse.emf.compare.AttributeChange}, a
	 * {@link org.eclipse.emf.compare.FeatureMapChange} or a
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
		return getSubDiffs(leftToRight, false, new LinkedHashSet<Diff>());
	}

	/**
	 * When merging a {@link Diff}, returns the first level of sub diffs of this given diff, and all
	 * associated diffs (see {@link DiffUtil#getAssociatedDiffs(Iterable, boolean, Diff)}) of these sub diffs.
	 * <p>
	 * If the diff is an {@link org.eclipse.emf.compare.AttributeChange}, a
	 * {@link org.eclipse.emf.compare.FeatureMapChange} or a
	 * {@link org.eclipse.emf.compare.ResourceAttachmentChange} , this method will return an empty iterable.
	 * </p>
	 * <p>
	 * If the diff is a {@link ReferenceChange} this method will return the first level differences contained
	 * in the match that contains the value of the reference change, and all associated diffs of these
	 * differences.
	 * </p>
	 * 
	 * @param leftToRight
	 *            the direction of merge.
	 * @return an iterable containing the first level of sub diffs of this given diff, and all associated
	 *         diffs of these sub diffs.
	 * @since 3.3
	 */
	public static Function<Diff, Iterable<Diff>> getDirectSubDiffs(final boolean leftToRight) {
		return getSubDiffs(leftToRight, true, new LinkedHashSet<Diff>());
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
			} else if (difference instanceof FeatureMapChange) {
				feature = ((FeatureMapChange)difference).getAttribute();
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
					result = isEqualToDefault(source, feature);
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
	private static boolean isEqualToDefault(EObject object, EStructuralFeature feature) {
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
			} else if (difference instanceof FeatureMapChange) {
				feature = ((FeatureMapChange)difference).getAttribute();
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
					result = isEqualToDefault(expectedContainer, feature);
				}
			}
		}
		return result;
	}

	/**
	 * Checks whether the given diff corresponds to a feature map containment change. This holds true for
	 * differences on feature map containment references' values.
	 * 
	 * @param diff
	 *            The diff to consider.
	 * @return <code>true</code> if the given {@code diff} is to be considered a containment change,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isFeatureMapContainment(final Diff diff) {
		// If the value of the FeatureMap.Entry is contained in the same container than the FeatureMap, it is
		// a containment change.
		if (diff instanceof FeatureMapChange) {
			FeatureMap.Entry entry = (FeatureMap.Entry)((FeatureMapChange)diff).getValue();
			Object entryValue = entry.getValue();
			if (entryValue instanceof EObject) {
				EObject container = ((EObject)entryValue).eContainer();
				Match match = diff.getMatch();
				return container == match.getLeft() || container == match.getRight()
						|| container == match.getOrigin();
			}
		}
		return false;
	}

	/**
	 * Get the expected target container in case of a move.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 * @return The expected target container if found, <code>null</code> otherwise.
	 */
	public static EObject moveElementGetExpectedContainer(final Comparison comparison,
			final FeatureMapChange diff, final boolean rightToLeft) {
		final EObject expectedContainer;
		if (!isFeatureMapContainment(diff)) {
			if (rightToLeft) {
				expectedContainer = diff.getMatch().getLeft();
			} else {
				expectedContainer = diff.getMatch().getRight();
			}
		} else if (diff.getSource() == DifferenceSource.LEFT) {
			if (rightToLeft) {
				expectedContainer = getContainerInEquivalence(comparison, diff, rightToLeft);
			} else {
				expectedContainer = diff.getMatch().getRight();
			}
		} else {
			if (rightToLeft) {
				expectedContainer = diff.getMatch().getLeft();
			} else {
				expectedContainer = getContainerInEquivalence(comparison, diff, rightToLeft);
			}
		}
		return expectedContainer;
	}

	/**
	 * Get the expected target container in the equivalent diffs of the given diff in case of a move.
	 * 
	 * @param comparison
	 *            The comparison object.
	 * @param diff
	 *            The diff we are currently merging.
	 * @param rightToLeft
	 *            Whether we should move the value in the left or right side.
	 * @return The expected target container if found, <code>null</code> otherwise.
	 */
	private static EObject getContainerInEquivalence(final Comparison comparison, final FeatureMapChange diff,
			final boolean rightToLeft) {
		EObject expectedContainer = null;
		Equivalence equ = diff.getEquivalence();
		if (equ != null) {
			for (Diff equivalence : equ.getDifferences()) {
				if (equivalence instanceof ReferenceChange) {
					final Match valueMatch = comparison.getMatch(((ReferenceChange)equivalence).getValue());
					/*
					 * We cannot "trust" the holding match (getMatch) in this case. However, "valueMatch"
					 * cannot be null : we cannot have detected a move if the moved element is not matched on
					 * both sides. Use that information to retrieve the proper "target" container.
					 */
					final Match targetContainerMatch;
					// If it exists, use the source side's container as reference
					if (rightToLeft && valueMatch.getRight() != null) {
						targetContainerMatch = comparison.getMatch(valueMatch.getRight().eContainer());
					} else if (!rightToLeft && valueMatch.getLeft() != null) {
						targetContainerMatch = comparison.getMatch(valueMatch.getLeft().eContainer());
					} else {
						// Otherwise, the value we're moving on one side has been removed from its source
						// side.
						targetContainerMatch = comparison.getMatch(valueMatch.getOrigin().eContainer());
					}
					if (rightToLeft) {
						expectedContainer = targetContainerMatch.getLeft();
					} else {
						expectedContainer = targetContainerMatch.getRight();
					}
					break;
				}
			}
		} else if (rightToLeft) {
			expectedContainer = diff.getMatch().getLeft();
		} else {
			expectedContainer = diff.getMatch().getRight();
		}
		return expectedContainer;
	}

	/**
	 * When merging a {@link Diff}, returns the sub diffs of this given diff, and all associated diffs (see
	 * {@link DiffUtil#getAssociatedDiffs(Iterable, boolean, Diff)}) of these sub diffs.
	 * <p>
	 * If the diff is an {@link org.eclipse.emf.compare.AttributeChange}, a
	 * {@link org.eclipse.emf.compare.FeatureMapChange} or a
	 * {@link org.eclipse.emf.compare.ResourceAttachmentChange}, this method will return an empty iterable.
	 * </p>
	 * <p>
	 * If the diff is a {@link ReferenceChange} this method will return all differences contained in the match
	 * that contains the value of the reference change, and all associated diffs of these differences.
	 * </p>
	 * 
	 * @param leftToRight
	 *            the direction of merge.
	 * @param firstLevelOnly
	 *            to get only the first level of subDiffs.
	 * @param processedDiffs
	 *            a set of diffs which have been already processed.
	 * @return an iterable containing the sub diffs of this given diff, and all associated diffs of these sub
	 *         diffs.
	 * @since 3.0
	 */
	private static Function<Diff, Iterable<Diff>> getSubDiffs(final boolean leftToRight,
			final boolean firstLevelOnly, final LinkedHashSet<Diff> processedDiffs) {
		return new Function<Diff, Iterable<Diff>>() {
			public Iterable<Diff> apply(Diff diff) {
				if (diff instanceof ReferenceChange) {
					Match matchOfValue = diff.getMatch().getComparison()
							.getMatch(((ReferenceChange)diff).getValue());
					if (((ReferenceChange)diff).getReference().isContainment()) {
						final Iterable<Diff> subDiffs;
						// if the diff is a Move diff, we don't want its children.
						if (ofKind(DifferenceKind.MOVE).apply(diff)) {
							subDiffs = ImmutableList.of();
						} else if (matchOfValue != null && !firstLevelOnly) {
							subDiffs = filter(matchOfValue.getAllDifferences(), CASCADING_DIFF);
						} else if (matchOfValue != null && firstLevelOnly) {
							subDiffs = filter(matchOfValue.getDifferences(), CASCADING_DIFF);
						} else {
							subDiffs = ImmutableList.of();
						}
						addAll(processedDiffs, subDiffs);
						final Iterable<Diff> associatedDiffs = getAssociatedDiffs(diff, subDiffs,
								processedDiffs, leftToRight, firstLevelOnly);
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
	 * @param firstLevelOnly
	 *            to get only the first level of subDiffs.
	 * @return an iterable containing the associated diffs of these given sub diffs, and all sub diffs of
	 *         these associated diffs.
	 * @since 3.0
	 */
	private static Iterable<Diff> getAssociatedDiffs(final Diff diffRoot, Iterable<Diff> subDiffs,
			LinkedHashSet<Diff> processedDiffs, boolean leftToRight, boolean firstLevelOnly) {
		Collection<Diff> associatedDiffs = new LinkedHashSet<Diff>();
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
			associatedDiffs.addAll(diff.getRefines());
			for (Diff req : reqs) {
				if (!Iterables.contains(subDiffs, req) && !processedDiffs.contains(req)) {
					processedDiffs.add(req);
					addAll(associatedDiffs,
							getSubDiffs(leftToRight, firstLevelOnly, processedDiffs).apply(req));
				}
			}
		}
		return associatedDiffs;
	}

	/**
	 * Returns the comparison associated with the given object. The given object is expected to be an instance
	 * of one of the Class from the ComparePackage model.
	 * 
	 * @param object
	 *            the object from which the comparison should be retrieved.
	 * @return the comparison.
	 */
	public static Comparison getComparison(EObject object) {
		final Comparison comparison;
		if (object instanceof Match) {
			comparison = ((Match)object).getComparison();
		} else if (object instanceof Diff) {
			comparison = getComparison((Diff)object);
		} else if (object instanceof MatchResource) {
			comparison = ((MatchResource)object).getComparison();
		} else if (object instanceof Equivalence) {
			EObject eContainer = object.eContainer();
			if (eContainer instanceof Comparison) {
				comparison = (Comparison)eContainer;
			} else {
				EList<Diff> differences = ((Equivalence)object).getDifferences();
				Diff first = getFirst(differences, null);
				if (first != null) {
					comparison = first.getMatch().getComparison();
				} else {
					comparison = null;
				}
			}
		} else if (object instanceof Conflict) {
			EObject eContainer = object.eContainer();
			if (eContainer instanceof Comparison) {
				comparison = (Comparison)eContainer;
			} else {
				EList<Diff> differences = ((Conflict)object).getDifferences();
				Diff first = getFirst(differences, null);
				if (first != null) {
					comparison = first.getMatch().getComparison();
				} else {
					comparison = null;
				}
			}
		} else {
			comparison = null;
		}
		return comparison;
	}

	/**
	 * Returns the comparison associated with the given diff.
	 * 
	 * @param diff
	 *            The diff, which must either have a match or a MatchResource container, otherwise a NPE will
	 *            be thrown.
	 * @return The comparison that contains the given diff, or {@code null} if there's none.
	 */
	public static Comparison getComparison(Diff diff) {
		final Comparison comparison;
		if (diff.eContainer() instanceof MatchResource) {
			comparison = ((MatchResource)diff.eContainer()).getComparison();
		} else {
			comparison = diff.getMatch().getComparison();
		}
		return comparison;
	}

	/**
	 * Determines the side of the given {@link Match} which represents the model state the other side will be
	 * changed to.
	 * 
	 * @param match
	 *            The match whose side is returned.
	 * @param source
	 *            The source from which side the differences are determined.
	 * @param mergeRightToLeft
	 *            The direction of the merge.
	 * @return The side of the given {@code match} which represents the desired model state in regards to the
	 *         given {@link DifferenceSource} and {@code MergeDirection}.
	 */
	public static EObject getExpectedSide(Match match, DifferenceSource source, boolean mergeRightToLeft) {
		final EObject result;
		// Bug 458818: prevent NPE if match is null
		if (match != null) {
			final boolean undoingLeft = mergeRightToLeft && source == DifferenceSource.LEFT;
			final boolean undoingRight = !mergeRightToLeft && source == DifferenceSource.RIGHT;

			final Comparison comparison = match.getComparison();

			if (comparison.isThreeWay() && (undoingLeft || undoingRight) && match.getOrigin() != null) {
				result = match.getOrigin();
			} else if (mergeRightToLeft) {
				result = match.getRight();
			} else {
				result = match.getLeft();
			}
		} else {
			result = null;
		}
		return result;
	}

	/**
	 * Determines if the given {@link EObject} is contained directly within a FeatureMap by checking the
	 * {@link EAnnotation}s.
	 *
	 * @param object
	 *            The object to check.
	 * @return {@true} if the {@code object} is directly contained within a FeatureMap.
	 */
	public static boolean isContainedInFeatureMap(EObject object) {
		final EAnnotation annotation = object.eContainingFeature()
				.getEAnnotation(ExtendedMetaData.ANNOTATION_URI);
		if (annotation != null) {
			final String groupKind = ExtendedMetaData.FEATURE_KINDS[ExtendedMetaData.GROUP_FEATURE];
			return annotation.getDetails().containsKey(groupKind);
		}
		return false;
	}

	/**
	 * Checks if both resources are platform resources and only one exists.
	 * 
	 * @param leftResource
	 *            the first resource to check.
	 * @param rightResource
	 *            the second resource to check.
	 * @return true if both resources are platform resources and only one exists, false otherwise.
	 */
	public static boolean bothArePlatformResourcesAndOnlyOneExists(Resource leftResource,
			Resource rightResource) {
		boolean existingPlatformResources = false;
		if (leftResource != null && rightResource != null) {
			final ResourceSet leftResourceSet = leftResource.getResourceSet();
			final ResourceSet rightResourceSet = rightResource.getResourceSet();
			if (leftResourceSet != null && rightResourceSet != null) {
				final URI leftURI = leftResource.getURI();
				final URI rightURI = rightResource.getURI();
				if (leftURI.isPlatformResource() && rightURI.isPlatformResource()) {
					boolean baseExists = leftResourceSet.getURIConverter().exists(leftURI,
							Collections.emptyMap());
					boolean changedExists = rightResourceSet.getURIConverter().exists(rightURI,
							Collections.emptyMap());
					existingPlatformResources = (baseExists && !changedExists)
							|| (!baseExists && changedExists);
				}
			}
		}
		return existingPlatformResources;
	}

	/**
	 * Checks if both resources have resource set.
	 * 
	 * @param leftResource
	 *            the first resource to check.
	 * @param rightResource
	 *            the second resource to check.
	 * @return true if both resources have resource set, false otherwise.
	 */
	public static boolean bothResourceHaveResourceSet(Resource leftResource, Resource rightResource) {
		if (leftResource != null && rightResource != null) {
			final ResourceSet leftResourceSet = leftResource.getResourceSet();
			final ResourceSet rightResourceSet = rightResource.getResourceSet();
			if (leftResourceSet != null && rightResourceSet != null) {
				return true;
			}
		}
		return false;
	}
}
