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
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.EMFCompareConfiguration;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.compare.internal.DiffCrossReferencer;
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
		checkNotNull(element);

		List<Diff> diffsOfMatch;
		Match match = getMatch(element);
		if (match != null) {
			diffsOfMatch = match.getDifferences();
		} else {
			diffsOfMatch = ImmutableList.of();
		}

		ECrossReferenceAdapter adapter = adapt(this, DiffCrossReferencer.class);
		Iterable<Diff> crossRefs = filter(getInverse(element, adapter), Diff.class);

		return new BasicEList<Diff>(ImmutableList.copyOf(concat(diffsOfMatch, crossRefs)));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getMatch(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Match getMatch(EObject element) {
		checkNotNull(element);

		ECrossReferenceAdapter adapter = adapt(this, MatchCrossReferencer.class);
		Iterable<Match> crossRefs = filter(getInverse(element, adapter), Match.class);

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
	 * Get or create a {@link Adapter} of type {@code clazz} on the given {@code}.
	 * 
	 * @param notifier
	 *            the object to adapt.
	 * @param clazz
	 *            the type of adapter we want to get.
	 * @return the first found adapter of type {@code clazz} or a newly created one.
	 */
	private static <T extends Adapter> T adapt(final Notifier notifier, final Class<T> clazz) {
		Optional<T> adapter = getAdapter(notifier, clazz);
		return adapter.or(new Supplier<T>() {
			public T get() {
				return adaptNew(notifier, clazz);
			}
		});
	}

	/**
	 * Creates and returns a new instance of {@code clazz} and adds it to the {@link Notifier#eAdapters() list
	 * of adapters} of the given {@code notifier}.
	 * 
	 * @param notifier
	 *            the object to be adapted.
	 * @param clazz
	 *            the type of adapter ot create.
	 * @return the newly created adapter.
	 * @throws RuntimeException
	 *             if something goes wrong while creating the instance of {@code clazz} (e.g. there is no
	 *             default constructor available).
	 */
	private static <T extends Adapter> T adaptNew(Notifier notifier, Class<T> clazz) {
		final T adapter;
		try {
			adapter = clazz.newInstance();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
		notifier.eAdapters().add(adapter);
		return adapter;
	}

	/**
	 * Returns an {@link Optional} adapter of the specified {@code clazz} by iterating on all
	 * {@link Notifier#eAdapters() adapters} of the given {@code notifier}.
	 * 
	 * @param notifier
	 *            the object we are looking to adapt to the given type.
	 * @param clazz
	 *            the type of adapter we want to get.
	 * @return the potentially {@link Optional#absent() absent} adapter .
	 */
	private static <T extends Adapter> Optional<T> getAdapter(Notifier notifier, Class<T> clazz) {
		Iterable<T> eAdaptersOfType = filter(notifier.eAdapters(), clazz);
		return Optional.fromNullable(getFirst(eAdaptersOfType, null));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getConfiguration()
	 */
	@Override
	public EMFCompareConfiguration getConfiguration() {
		EMFCompareConfiguration ret = null;
		for (Adapter eAdapter : eAdapters()) {
			if (eAdapter.isAdapterForType(EMFCompareConfiguration.class)) {
				ret = (EMFCompareConfiguration)eAdapter;
				break;
			}
		}
		if (ret == null) {
			ret = EMFCompareConfiguration.builder().build();
		}
		return ret;
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

}
