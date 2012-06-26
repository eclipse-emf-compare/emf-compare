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

import static com.google.common.base.Predicates.notNull;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
		return getMatch(element).getDifferences();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getMatch(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Match getMatch(EObject element) {
		if (element == null) {
			return null;
		}

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
		for (MatchResource matchResource : getMatchedResources()) {
			if (matchResource.getOriginURI() != null && matchResource.getOriginURI().length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Converts an inverse reference to its corresponding Match.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class MatchInverseReference implements Function<EStructuralFeature.Setting, Match> {
		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		public Match apply(Setting input) {
			if (input.getEObject() instanceof Match) {
				return (Match)input.getEObject();
			}
			return null;
		}
	}

	/**
	 * This implementation of an {@link ECrossReferenceAdapter} will allow us to only attach ourselves to the
	 * Match elements.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class MatchCrossReferencer extends ECrossReferenceAdapter {
		/**
		 * We're only interested in the cross references that come from the Match.left, Match.right and
		 * Match.origin references.
		 */
		private final Set<EReference> includedReferences;

		/**
		 * Default constructor.
		 */
		public MatchCrossReferencer() {
			final ComparePackage pack = ComparePackage.eINSTANCE;
			includedReferences = Sets.newHashSet(pack.getMatch_Left(), pack.getMatch_Right(), pack
					.getMatch_Origin());
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#getInverseReferences(org.eclipse.emf.ecore.EObject,
		 *      boolean)
		 */
		@Override
		public Collection<Setting> getInverseReferences(EObject eObject, boolean resolve) {
			Collection<EStructuralFeature.Setting> result = Lists.newArrayList();

			Collection<EStructuralFeature.Setting> nonNavigableInverseReferences = inverseCrossReferencer
					.get(eObject);
			if (nonNavigableInverseReferences != null) {
				result.addAll(nonNavigableInverseReferences);
			}

			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#addAdapter(org.eclipse.emf.common.notify.Notifier)
		 */
		@Override
		protected void addAdapter(Notifier notifier) {
			if (notifier instanceof Match) {
				super.addAdapter(notifier);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecore.util.ECrossReferenceAdapter#isIncluded(org.eclipse.emf.ecore.EReference)
		 */
		@Override
		protected boolean isIncluded(EReference eReference) {
			if (super.isIncluded(eReference)) {
				return includedReferences.contains(eReference);
			}
			return false;
		}
	}
}
