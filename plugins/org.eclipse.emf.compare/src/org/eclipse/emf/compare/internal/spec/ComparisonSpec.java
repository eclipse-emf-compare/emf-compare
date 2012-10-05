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

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.Iterator;

import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.compare.internal.DiffCrossReferencer;
import org.eclipse.emf.compare.internal.MatchCrossReferencer;
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
	/**
	 * Converts an inverse reference to its corresponding EObject.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static final Function<EStructuralFeature.Setting, EObject> INVERSE_REFERENCES = new Function<EStructuralFeature.Setting, EObject>() {
		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		public EObject apply(Setting input) {
			if (input != null) {
				return input.getEObject();
			}
			return null;
		}
	};

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
		Iterable<Diff> crossRefs = filter(getInverse(element, diffCrossReferencer), Diff.class);

		final BasicEList<Diff> diffs = new BasicEList<Diff>();
		for (Diff diff : crossRefs) {
			diffs.add(diff);
		}
		return diffs;
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

		if (matchCrossReferencer == null) {
			matchCrossReferencer = new MatchCrossReferencer();
			eAdapters().add(matchCrossReferencer);
		}
		Iterable<Match> crossRefs = filter(getInverse(element, matchCrossReferencer), Match.class);

		return Iterables.getFirst(crossRefs, null);
	}

	/**
	 * Returns an {@link Iterable} of EObject being inverse references of the given {@code element} stored by
	 * the {@code adapter}.
	 * 
	 * @param element
	 *            the target of the search cross references.
	 * @param adapter
	 *            the {@link ECrossReferenceAdapter} to use to look for inverse references.
	 * @return a possibly empty {@link Iterable} of inverse references.
	 */
	private Iterable<EObject> getInverse(EObject element, ECrossReferenceAdapter adapter) {
		return getInverse(element, adapter, Predicates.<Setting> alwaysTrue());
	}

	/**
	 * Returns an {@link Iterable} of EObject being inverse references of the given {@code element} stored by
	 * the {@code adapter}. It is possible to filter returned EObject by filtering out on {@link Setting cross
	 * references}.
	 * 
	 * @param element
	 *            the target of the search cross references.
	 * @param adapter
	 *            the {@link ECrossReferenceAdapter} to use to look for inverse references.
	 * @param settingsFilter
	 *            a filter of {@link Setting} applied on the output of
	 *            {@link ECrossReferenceAdapter#getInverseReferences(EObject, boolean)}.
	 * @return a possibly empty {@link Iterable} of inverse references.
	 */
	private Iterable<EObject> getInverse(EObject element, ECrossReferenceAdapter adapter,
			Predicate<Setting> settingsFilter) {
		final Iterable<EStructuralFeature.Setting> settings = adapter.getInverseReferences(element, false);
		return transform(filter(settings, settingsFilter), INVERSE_REFERENCES);
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
			ret = new EqualityHelper(EqualityHelper.createDefaultCache(CacheBuilder.newBuilder().weakKeys()));
			this.eAdapters().add(ret);
			ret.setTarget(this);
		}
		return ret;
	}
}
