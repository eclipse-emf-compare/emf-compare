/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - Bug 460902
 *******************************************************************************/
package org.eclipse.emf.compare.req;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.filter;
import static org.eclipse.emf.compare.DifferenceKind.ADD;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.DifferenceKind.DELETE;
import static org.eclipse.emf.compare.DifferenceKind.MOVE;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static org.eclipse.emf.compare.internal.utils.ComparisonUtil.isFeatureMapContainment;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ComparisonCanceledException;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.internal.DiffCrossReferencer;
import org.eclipse.emf.compare.utils.MatchUtil;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;

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

	/** The time before we expire the values in {@link #cachedDifferences} after access, in seconds. */
	private static final long CACHE_EXPIRATION_TIME = 30L;

	/** The initial capacity of our {@link #cachedDifferences cache of differences}. */
	private static final int CACHE_INITIAL_CAPACITY = 200;

	/**
	 * We'll be computing the list of differences related to a given EObject
	 * ({@link #getDifferenceOnGivenObject(Comparison, EObject, DifferenceSource, DifferenceKind)}) a lot of
	 * times in short succession. This short-lived cache will allow us to avoid the cost of creating the Set
	 * anew at every call.
	 */
	private final Cache<CacheKey, Set<Diff>> cachedDifferences = CacheBuilder.newBuilder()
			.expireAfterAccess(CACHE_EXPIRATION_TIME, TimeUnit.SECONDS)
			.initialCapacity(CACHE_INITIAL_CAPACITY).build();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.req.IReqEngine#computeRequirements(Comparison, Monitor)
	 */
	public void computeRequirements(Comparison comparison, Monitor monitor) {
		monitor.subTask(EMFCompareMessages.getString("DefaultReqEngine.monitor.req")); //$NON-NLS-1$
		for (Diff difference : comparison.getDifferences()) {
			if (monitor.isCanceled()) {
				throw new ComparisonCanceledException();
			}
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

		Match match = difference.getMatch();
		EObject value = getValue(comparison, difference);
		DifferenceKind kind = difference.getKind();

		if (value != null) {
			boolean isAddition = isAddOrSetDiff(difference);
			boolean isDeletion = !isAddition && isDeleteOrUnsetDiff(difference);

			if (isAddition && isDeleteOrAddResourceAttachmentChange(comparison, difference)) {
				difference.getRequires()
						.addAll(getDiffsThatShouldDependOn((ResourceAttachmentChange)difference));
				// ADD object
			} else if (isAddition && isReferenceContainment(difference)) {
				// if (isAddition && isReferenceContainment(difference)) {

				// -> requires ADD on the container of the object
				difference.getRequires().addAll(getDifferenceOnGivenObject(comparison, value.eContainer(),
						difference.getSource(), ADD));

				// -> requires DELETE of the origin value on the same containment mono-valued reference
				difference.getRequires()
						.addAll(getDELOriginValueOnContainmentRefSingle(comparison, difference));

				// ADD reference
			} else if (isAddition && !isFeatureMapContainment(difference)) {

				// -> requires ADD of the value of the reference (target object)
				difference.getRequires()
						.addAll(getDifferenceOnGivenObject(comparison, value, difference.getSource(), ADD));

				// -> requires ADD of the object containing the reference
				final EObject container = MatchUtil.getContainer(comparison, difference);
				if (container != null) {
					difference.getRequires().addAll(
							getDifferenceOnGivenObject(comparison, container, difference.getSource(), ADD));
				}
				match.getDifferences().stream().filter(diff -> diff instanceof ResourceAttachmentChange
						&& diff.getKind() == DifferenceKind.ADD && diff.getSource() == difference.getSource())
						.forEach(difference.getRequires()::add);

			} else if (isDeletion && isDeleteOrAddResourceAttachmentChange(comparison, difference)) {
				difference.getRequiredBy()
						.addAll(getDiffsThatShouldDependOn((ResourceAttachmentChange)difference));
				// DELETE object
			} else if (isDeletion && isReferenceContainment(difference)) {

				// -> requires DELETE of the outgoing references and contained objects
				difference.getRequires().addAll(getDELOutgoingReferences(comparison, difference));
				difference.getRequires().addAll(getDifferenceOnGivenObject(comparison, value.eContents(),
						difference.getSource(), DELETE));

				// -> requires MOVE of contained objects
				difference.getRequires().addAll(getMOVEContainedObjects(comparison, difference));

				// The DELETE or CHANGE of incoming references are handled in the DELETE reference and CHANGE
				// reference cases.

				// DELETE reference
			} else if (isDeletion && !isFeatureMapContainment(difference)) {

				// -> is required by DELETE of the target object
				difference.getRequiredBy().addAll(
						getDifferenceOnGivenObject(comparison, value, difference.getSource(), DELETE));

				// MOVE object
			} else if (kind == MOVE && isReferenceContainment(difference)) {

				EObject container = value.eContainer();

				// -> requires ADD on the container of the object
				difference.getRequires().addAll(
						getDifferenceOnGivenObject(comparison, container, difference.getSource(), ADD));

				// -> requires MOVE of the container of the object
				difference.getRequires().addAll(
						getDifferenceOnGivenObject(comparison, container, difference.getSource(), MOVE));

				// CHANGE reference
			} else if (kind == CHANGE && !isAddition && !isDeletion
					&& !(difference instanceof FeatureMapChange)) {

				// -> is required by DELETE of the origin target object
				EObject originValue = MatchUtil.getOriginValue(comparison, (ReferenceChange)difference);
				difference.getRequiredBy().addAll(
						getDifferenceOnGivenObject(comparison, originValue, difference.getSource(), DELETE));

				// -> requires ADD of the value of the reference (target object) if required
				difference.getRequires()
						.addAll(getDifferenceOnGivenObject(comparison, value, difference.getSource(), ADD));
			}
		}

	}

	/**
	 * Checks whether the given diff corresponds to a reference change associated with the addition or the
	 * deletion of an object.
	 * 
	 * @param comparison
	 *            The comparison
	 * @param diff
	 *            The diff to consider
	 * @return <code>true</code> if the given {@code diff} is to be considered a ResourceAttachmentChange with
	 *         ADD or DELETE dependencies, <code>false</code> otherwise.
	 */
	private boolean isDeleteOrAddResourceAttachmentChange(Comparison comparison, Diff diff) {
		if (diff instanceof ResourceAttachmentChange && (diff.getKind() == ADD || diff.getKind() == DELETE)) {
			EObject container = MatchUtil.getContainer(comparison, diff);
			if (container != null) {
				EList<Diff> differences = comparison.getDifferences(container);
				for (Diff containedDiff : differences) {
					if (containedDiff instanceof ReferenceChange
							&& ((ReferenceChange)containedDiff).getReference().isContainment()
							&& containedDiff.getKind() == diff.getKind()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Compute the dependencies specific to ResourceAttachmentChanges DELETE or ADD. (The addition or deletion
	 * of the package controlled/uncontrolled must be a dependency of the RAC)
	 * 
	 * @param diff
	 *            The given difference
	 * @return a list of dependencies
	 */
	private Set<Diff> getDiffsThatShouldDependOn(ResourceAttachmentChange diff) {
		Set<Diff> result = new LinkedHashSet<Diff>();
		Comparison comparison = diff.getMatch().getComparison();
		EObject container = MatchUtil.getContainer(comparison, diff);
		for (ReferenceChange rc : filter(comparison.getDifferences(container), ReferenceChange.class)) {
			if (diff.getSource() == rc.getSource() && diff.getKind() == rc.getKind()) {
				result.add(rc);
			}
		}
		return result;
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
		Set<Diff> result = new LinkedHashSet<Diff>();
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
							sourceDifference.getSource(), DELETE);
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
	 * @param source
	 *            source of the differences. A diff from the left cannot "require" a diff from the right...
	 * @param kind
	 *            The given kind.
	 * @return The found differences.
	 */
	private Set<Diff> getDifferenceOnGivenObject(Comparison comparison, EObject object,
			DifferenceSource source, DifferenceKind kind) {
		return getDifferences(comparison, object, source, kind);
	}

	/**
	 * Will be used to filter out of a list of differences all those that relate to containment changes on the
	 * given object (a containment reference change or a resource attachment change if the given object has no
	 * direct container.
	 * 
	 * @param eObject
	 *            The object for which we seek containment differences.
	 * @param source
	 *            source of the differences. A diff from the left cannot "require" a diff from the right...
	 * @param kind
	 *            The kind of difference we seek.
	 * @return The created predicate.
	 */
	private Predicate<Diff> isRequiredContainmentChange(EObject eObject, DifferenceSource source,
			DifferenceKind kind) {
		return new Predicate<Diff>() {
			public boolean test(Diff diff) {
				boolean result = false;
				if (diff.getKind() == kind && diff.getSource() == source) {
					if (diff instanceof ReferenceChange
							&& ((ReferenceChange)diff).getReference().isContainment()) {
						result = true;
					} else if (diff instanceof ResourceAttachmentChange && eObject.eContainer() == null) {
						result = true;
					}
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
	 * @param source
	 *            source of the differences. A diff from the left cannot "require" a diff from the right...
	 * @param kind
	 *            The kind of requested differences.
	 * @return The found differences.
	 */
	private Set<Diff> getDifferenceOnGivenObject(Comparison comparison, List<EObject> objects,
			DifferenceSource source, DifferenceKind kind) {
		Set<Diff> result = new LinkedHashSet<Diff>();
		for (EObject object : objects) {
			result.addAll(getDifferenceOnGivenObject(comparison, object, source, kind));
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
	private Set<Diff> getDELOutgoingReferences(Comparison comparison, Diff sourceDifference) {
		Set<Diff> result = new LinkedHashSet<Diff>();

		EObject value = getValue(comparison, sourceDifference);

		if (value != null) {
			final Match valueMatch = comparison.getMatch(value);
			if (valueMatch != null) {
				for (Diff candidate : filter(valueMatch.getDifferences(),
						or(instanceOf(ReferenceChange.class), instanceOf(FeatureMapChange.class)))) {
					if (candidate.getSource() == sourceDifference.getSource()
							&& (candidate.getKind() == DELETE || isDeleteOrUnsetDiff(candidate))) {
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
		Set<ReferenceChange> result = new LinkedHashSet<ReferenceChange>();
		EObject value = getValue(comparison, sourceDifference);
		if (value != null) {
			List<EObject> contents = value.eContents();
			for (EObject content : contents) {
				EObject originObject = MatchUtil.getOriginObject(comparison, content);
				if (originObject != null) {
					for (ReferenceChange difference : filter(comparison.getDifferences(originObject),
							ReferenceChange.class)) {
						if (difference.getReference().isContainment()
								&& difference.getSource() == sourceDifference.getSource()
								&& difference.getKind() == MOVE) {
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
	 * containment references' values, but also for feature map or resource attachment changes.
	 * 
	 * @param diff
	 *            The diff to consider.
	 * @return <code>true</code> if the given {@code diff} is to be considered a containment change,
	 *         <code>false</code> otherwise.
	 */
	private static boolean isReferenceContainment(Diff diff) {
		return diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()
				|| diff instanceof ResourceAttachmentChange || diff instanceof FeatureMapChange;
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
		} else if (diff instanceof FeatureMapChange) {
			Object entry = ((FeatureMapChange)diff).getValue();
			if (entry instanceof FeatureMap.Entry) {
				Object entryValue = ((FeatureMap.Entry)entry).getValue();
				if (entryValue instanceof EObject) {
					value = (EObject)entryValue;
				}
			}
		}
		return value;
	}

	/**
	 * This reproduces the same behavior as {@link Comparison#getDifferences(EObject)}, avoiding the overhead
	 * of EList iteration and filtering the differences as deep down as possible to limit the iteration counts
	 * and intermediate collections.
	 * 
	 * @param comparison
	 *            The comparison from which to list differences.
	 * @param element
	 *            The EObject for which we seek all related differences.
	 * @param source
	 *            Only list differences with this source.
	 * @param kind
	 *            Only list differences with this kind.
	 * @return The filtered list of differences related to the given object.
	 */
	private Set<Diff> getDifferences(Comparison comparison, EObject element, DifferenceSource source,
			DifferenceKind kind) {
		if (element == null) {
			return new LinkedHashSet<>();
		}
		DiffCrossReferencer crossReferencer = getCrossReferencer(comparison);
		if (crossReferencer == null) {
			comparison.getDifferences(element);
		}

		CacheKey key = new CacheKey(element, source, kind);
		Set<Diff> cached = cachedDifferences.getIfPresent(key);
		if (cached != null) {
			return cached;
		}

		crossReferencer = getCrossReferencer(comparison);

		final Set<Diff> result;
		final Match match = comparison.getMatch(element);
		if (match != null) {
			result = getDifferences(crossReferencer, match,
					isRequiredContainmentChange(element, source, kind));
		} else {
			final Collection<EStructuralFeature.Setting> settings = crossReferencer
					.getNonNavigableInverseReferences(element, false);
			result = new LinkedHashSet<Diff>(settings.size());
			settings.stream().map(setting -> (Diff)setting.getEObject())
					.filter(isRequiredContainmentChange(element, source, kind)).forEach(result::add);
		}
		cachedDifferences.put(key, result);
		return result;
	}

	/**
	 * Returns the DiffCrossReferencer attached on the given comparison.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @return The DiffCrossReferencer attached on the given comparison.
	 */
	private DiffCrossReferencer getCrossReferencer(Comparison comparison) {
		DiffCrossReferencer crossReferencer = null;
		for (Adapter adapter : comparison.eAdapters()) {
			if (adapter instanceof DiffCrossReferencer) {
				crossReferencer = (DiffCrossReferencer)adapter;
			}
		}
		return crossReferencer;
	}

	/**
	 * Returns the list of differences impacting the given match's sides, filtering as deep down as possible
	 * to reduce the list of candidates as early as possible.
	 * 
	 * @param crossReferencer
	 *            The cross referencer for the current comparison.
	 * @param match
	 *            The match which sides we need the differences of.
	 * @param predicate
	 *            The predicate with which to filter the list of differences.
	 * @return The list of differences impacting the given match's sides.
	 */
	private Set<Diff> getDifferences(DiffCrossReferencer crossReferencer, Match match,
			Predicate<Diff> predicate) {
		final Collection<EStructuralFeature.Setting> leftInverseReference = safeGetInverseReferences(
				crossReferencer, match.getLeft());
		final Collection<EStructuralFeature.Setting> rightInverseReference = safeGetInverseReferences(
				crossReferencer, match.getRight());
		final Collection<EStructuralFeature.Setting> originInverseReference = safeGetInverseReferences(
				crossReferencer, match.getOrigin());

		int maxExpectedSize = leftInverseReference.size() + rightInverseReference.size()
				+ originInverseReference.size();
		Stream<Diff> racs = match.getDifferences().stream()
				.filter(diff -> diff instanceof ResourceAttachmentChange).filter(predicate);

		// Double the max expected size to avoid rehashing as much as possible
		Set<Diff> result = new LinkedHashSet<Diff>(maxExpectedSize * 2);

		leftInverseReference.stream().map(setting -> (Diff)setting.getEObject()).filter(predicate)
				.forEach(result::add);
		rightInverseReference.stream().map(setting -> (Diff)setting.getEObject()).filter(predicate)
				.forEach(result::add);
		originInverseReference.stream().map(setting -> (Diff)setting.getEObject()).filter(predicate)
				.forEach(result::add);
		racs.forEach(result::add);

		return result;
	}

	/**
	 * Returns the list of inverse references known by the given cross-referencer for the given EObject, an
	 * empty set otherwise.
	 * 
	 * @param crossReferencer
	 *            The cross-referencer.
	 * @param object
	 *            The object we need the inverse references for.
	 * @return The list of inverse references known by the given cross-referencer for the given EObject, an
	 *         empty set otherwise.
	 */
	private Collection<EStructuralFeature.Setting> safeGetInverseReferences(
			DiffCrossReferencer crossReferencer, EObject object) {
		Collection<EStructuralFeature.Setting> crossRefs;
		if (object != null) {
			crossRefs = crossReferencer.getNonNavigableInverseReferences(object, false);
		} else {
			crossRefs = Collections.emptySet();
		}
		return crossRefs;
	}

	/**
	 * Represents the key for our {@link #cachedDifferences cache of differences}.
	 * 
	 * @author lgoubet
	 */
	private static class CacheKey {
		/** The element for which we're caching the list of differences. */
		private final EObject element;

		/** The side from which the cached differences originate. */
		private final DifferenceSource source;

		/** The kind of differences we're storing in our list. */
		private final DifferenceKind kind;

		/**
		 * Constructor.
		 * 
		 * @param element
		 *            The element for which we're caching the list of differences.
		 * @param source
		 *            The side from which the cached differences originate.
		 * @param kind
		 *            The kind of differences we're storing in our list.
		 */
		CacheKey(EObject element, DifferenceSource source, DifferenceKind kind) {
			this.element = element;
			this.source = source;
			this.kind = kind;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CacheKey) {
				return this.element == ((CacheKey)obj).element && this.source == ((CacheKey)obj).source
						&& this.kind == ((CacheKey)obj).kind;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(element, source, kind);
		}
	}

}
