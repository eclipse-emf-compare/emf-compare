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
package org.eclipse.emf.compare.internal.spec;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.notNull;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.compare.internal.MatchCrossReferencer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * This specialization of the {@link ComparisonImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ComparisonSpec extends ComparisonImpl {
	/**
	 * Caches the value of this boolean. {@link #isThreeWay()} is called a lot of times during the comparison
	 * process. We do not want to compute it again each of these times.
	 */
	private boolean isThreeWay;

	/** This will be used to determine whether we need to invalidate {@link #isThreeWay}. */
	private int resourceCount = -1;

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
		Match match = getMatch(element);
		if (match != null) {
			return match.getDifferences();
		}
		return new BasicEList<Diff>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getMatch(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Match getMatch(EObject element) {
		checkNotNull(element);

		ECrossReferenceAdapter adapter = null;
		final Iterator<Adapter> adapterIterator = eAdapters().iterator();
		while (adapterIterator.hasNext() && adapter == null) {
			final Adapter candidate = adapterIterator.next();
			if (candidate instanceof MatchCrossReferencer) {
				adapter = (MatchCrossReferencer)candidate;
			}
		}
		if (adapter == null) {
			adapter = new MatchCrossReferencer();
			eAdapters().add(adapter);
		}

		final Iterable<EStructuralFeature.Setting> settings = adapter.getInverseReferences(element, false);
		final Iterator<Match> matchIter = Iterables.filter(
				Iterables.transform(settings, new MatchInverseReference()), notNull()).iterator();

		Match result = null;
		if (matchIter.hasNext()) {
			result = matchIter.next();
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#isThreeWay()
	 */
	@Override
	public boolean isThreeWay() {
		/*
		 * No need to go further if we found that we were in three way : adding new Resources to the
		 * comparison will not magically put us in two-way mode.
		 */
		if (isThreeWay) {
			return isThreeWay;
		}

		final List<MatchResource> resources = getMatchedResources();
		final int count = resources.size();
		if (resourceCount != count) {
			isThreeWay = false;
			resourceCount = count;
			for (int i = 0; i < count; i++) {
				final MatchResource matchResource = resources.get(i);
				if (matchResource.getOriginURI() != null && matchResource.getOriginURI().length() > 0) {
					isThreeWay = true;
				}
			}
		}
		return isThreeWay;
	}

	/**
	 * Converts an inverse reference to its corresponding Match.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class MatchInverseReference implements Function<EStructuralFeature.Setting, Match> {
		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		public Match apply(Setting input) {
			if (input != null && input.getEObject() instanceof Match) {
				return (Match)input.getEObject();
			}
			return null;
		}
	}

}
