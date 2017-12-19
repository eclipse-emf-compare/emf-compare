/*******************************************************************************
 * Copyright (c) 2012, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - performance improvements
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.compare.internal.DiffCrossReferencer;
import org.eclipse.emf.compare.internal.MatchCrossReferencer;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This specialization of the {@link ComparisonImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ComparisonSpec extends ComparisonImpl {
	/** Keeps a reference to our match cross referencer. */
	private MatchCrossReferencer matchCrossReferencer;

	/** Keeps a reference to our diff cross referencer. */
	private DiffCrossReferencer diffCrossReferencer;

	/**
	 * The cached value for {@link #getDifferences()}, cleared when the {@link #diffCrossReferencer} detects
	 * structural changes.
	 */
	private EList<Diff> differences;

	@Override
	public EList<Diff> getDifferences() {
		if (differences == null) {
			final Iterator<Diff> diffIterator = Iterators.filter(eAllContents(), Diff.class);

			final BasicEList<Diff> allDifferences = new BasicEList<Diff>() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void didChange() {
					if (differences == this) {
						differences = null;
					}
					super.didChange();
				}
			};

			while (diffIterator.hasNext()) {
				allDifferences.addUnique(diffIterator.next());
			}

			// The diff cross referencer is used to flush the cached value of the differences, so we should
			// only cache the value once the cross referencer has been created.
			if (diffCrossReferencer == null) {
				return allDifferences;
			}

			differences = allDifferences;
		}

		return differences;
	}

	@Override
	public EList<Diff> getDifferences(EObject element) {
		if (element == null) {
			return new BasicEList<Diff>();
		}

		if (diffCrossReferencer == null) {
			diffCrossReferencer = new DiffCrossReferencer() {
				@Override
				protected void addAdapter(Notifier notifier) {
					differences = null;
					super.addAdapter(notifier);
				}

				@Override
				protected void removeAdapter(Notifier notifier) {
					differences = null;
					super.removeAdapter(notifier);
				}
			};
			eAdapters().add(diffCrossReferencer);
		}

		final EList<Diff> result;
		final Match match = getMatch(element);
		if (match != null) {
			result = getInverseReferences(match);
		} else {
			final Collection<EStructuralFeature.Setting> settings = diffCrossReferencer
					.getNonNavigableInverseReferences(element, false);
			result = new BasicEList<Diff>(settings.size());
			for (EStructuralFeature.Setting setting : settings) {
				EObject eObject = setting.getEObject();
				result.add((Diff)eObject);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getMatch(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Match getMatch(EObject element) {
		if (element != null) {
			if (matchCrossReferencer == null) {
				matchCrossReferencer = new MatchCrossReferencer();
				eAdapters().add(matchCrossReferencer);
			}

			final Collection<EStructuralFeature.Setting> settings = matchCrossReferencer
					.getNonNavigableInverseReferences(element, false);

			if (!settings.isEmpty()) {
				// Cast to List and Match without testing.
				// The matchCrossReferencer will only return a List of Settings for Matches
				// #getNonNavigableInverseReferences
				// This method is in the general case called O(n^2) times, so small performance improvements
				// (iterator overhead) here have a big overall impact.
				return (Match)((List<Setting>)settings).get(0).getEObject();
			}
		}

		return null;
	}

	/**
	 * Returns an {@link EList} of Diff being inverse references of sides of the given {@code match} as stored
	 * by {@link #diffCrossReferencer}.
	 * 
	 * @param match
	 *            the target of the search cross references.
	 * @return a possibly empty {@link EList} of inverse references.
	 */
	private EList<Diff> getInverseReferences(Match match) {
		final Collection<EStructuralFeature.Setting> leftInverseReference = safGetNonNavigableInverseReferences(
				match.getLeft(), diffCrossReferencer);
		final Collection<EStructuralFeature.Setting> rightInverseReference = safGetNonNavigableInverseReferences(
				match.getRight(), diffCrossReferencer);
		final Collection<EStructuralFeature.Setting> originInverseReference = safGetNonNavigableInverseReferences(
				match.getOrigin(), diffCrossReferencer);

		int maxExpectedSize = leftInverseReference.size() + rightInverseReference.size()
				+ originInverseReference.size();

		// Because this method, in the most general case, is called O(n^2) times, we process the diffs in
		// this very low-level way to ensure optimal performance, i.e., we avoid all iterator and casting
		// overhead.
		List<Diff> resourceAttachmentChanges = null;
		Diff[] diffs = (Diff[])((BasicEList<Diff>)match.getDifferences()).data();
		if (diffs != null) {
			for (Diff diff : diffs) {
				if (diff instanceof ResourceAttachmentChange) {
					if (resourceAttachmentChanges == null) {
						resourceAttachmentChanges = Lists.newArrayList();
					}
					++maxExpectedSize;
					resourceAttachmentChanges.add(diff);
				}
			}
		}

		EList<Diff> result = new UniqueEList.FastCompare<Diff>(maxExpectedSize);

		addSettingEObjectsTo(result, leftInverseReference);
		addSettingEObjectsTo(result, rightInverseReference);
		addSettingEObjectsTo(result, originInverseReference);

		if (resourceAttachmentChanges != null) {
			result.addAll(resourceAttachmentChanges);
		}

		return result;
	}

	/**
	 * Iterates on the given {@code settings} and retrieve the asociated EObject before adding it to
	 * {@code addTo} List.
	 * <p>
	 * It <b>modifies</b> the {@code addTo} {@link List} and <b>expect</b> the settings to all be from
	 * {@link Diff}. This method only reason to exists is to factorize a loop and <em>should not</em> be used
	 * outside {@link #getInverseReferences(Match)}.
	 * 
	 * @param addTo
	 *            the list to which {@link Setting}'s {@link EObject} will be added.
	 * @param settings
	 *            the list of {@link Setting} to iterate on.
	 */
	private static void addSettingEObjectsTo(List<Diff> addTo,
			final Collection<EStructuralFeature.Setting> settings) {
		// No need to cast the added values to Diff because the diffCrossReferencer will only return Diff from
		// #getNonNavigableInverseReferences
		@SuppressWarnings("unchecked")
		List<Object> uncheckedAddTo = (List<Object>)(List<?>)addTo;
		for (EStructuralFeature.Setting setting : settings) {
			uncheckedAddTo.add(setting.getEObject());
		}
	}

	/**
	 * Returns the inverse reference of the given {@code eObject} regarding the given {@code crossReferencer}.
	 * It is {@code safe} to call it with <code>null</code> {@code eObject}: it will return an empty
	 * collection.
	 * 
	 * @param eObject
	 *            the target of the search cross references.
	 * @param crossReferencer
	 *            the cross referencer adapter to use for searching.
	 * @return a possibly empty {@link EList} of inverse references.
	 */
	private Collection<EStructuralFeature.Setting> safGetNonNavigableInverseReferences(EObject eObject,
			ECrossReferenceAdapter crossReferencer) {
		final Collection<EStructuralFeature.Setting> inverseReference;
		if (eObject != null) {
			inverseReference = crossReferencer.getNonNavigableInverseReferences(eObject, false);
		} else {
			inverseReference = ImmutableList.of();
		}
		return inverseReference;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getEqualityHelper()
	 */
	@Override
	public IEqualityHelper getEqualityHelper() {
		IEqualityHelper ret = (IEqualityHelper)EcoreUtil.getExistingAdapter(this, IEqualityHelper.class);
		if (ret == null) {
			ret = new EqualityHelper(EqualityHelper.createDefaultCache(CacheBuilder.newBuilder()
					.maximumSize(DefaultMatchEngine.DEFAULT_EOBJECT_URI_CACHE_MAX_SIZE)));
			this.eAdapters().add(ret);
			ret.setTarget(this);
		}
		return ret;
	}
}
