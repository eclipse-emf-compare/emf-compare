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
package org.eclipse.emf.compare.internal.spec;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterators.addAll;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEList;
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
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getDifferences()
	 */
	@Override
	public EList<Diff> getDifferences() {
		final Iterator<Diff> diffIterator = Iterators.filter(eAllContents(), Diff.class);

		final EList<Diff> allDifferences = new BasicEList<Diff>();
		while (diffIterator.hasNext()) {
			((AbstractEList<Diff>)allDifferences).addUnique(diffIterator.next());
		}

		return allDifferences;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getDifferences(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public EList<Diff> getDifferences(EObject element) {
		if (element == null) {
			return new BasicEList<Diff>();
		}

		if (diffCrossReferencer == null) {
			diffCrossReferencer = new DiffCrossReferencer();
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

			Iterator<Setting> settingsIterator = settings.iterator();
			if (settingsIterator.hasNext()) {
				// cast to Match without testing
				// the matchCrossReferencer will only return Match from #getNonNavigableInverseReferences
				return (Match)settingsIterator.next().getEObject();
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

		// creates a list eagerly as we have to know the size beforehand and we will iterate on it later.
		List<ResourceAttachmentChange> resourceAttachmentChanges = newArrayList(
				filter(match.getDifferences(), ResourceAttachmentChange.class));

		final int maxExpectedSize = leftInverseReference.size() + rightInverseReference.size()
				+ originInverseReference.size() + resourceAttachmentChanges.size();
		EList<Diff> result = new UniqueEList.FastCompare<Diff>(maxExpectedSize);

		addSettingEObjectsTo(result, leftInverseReference);
		addSettingEObjectsTo(result, rightInverseReference);
		addSettingEObjectsTo(result, originInverseReference);

		addAll(result, resourceAttachmentChanges.iterator());

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
		for (EStructuralFeature.Setting setting : settings) {
			// cast to Diff without testing
			// the diffCrossReferencer will only return Diff from #getNonNavigableInverseReferences
			addTo.add((Diff)setting.getEObject());
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
