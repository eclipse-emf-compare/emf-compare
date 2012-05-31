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
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.MatchItemProvider;
import org.eclipse.emf.ecore.EObject;

/**
 * Specialized {@link MatchItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchItemProviderSpec extends MatchItemProvider {

	/**
	 * Predicate that filters out Match that does not have any difference.
	 */
	private static final Predicate<Match> HAS_DIFFERENCE = new Predicate<Match>() {
		public boolean apply(Match input) {
			return !isEmpty(filter(input.getAllDifferences(), LEFT_CONFLICTUAL_DIFF));
		}
	};

	/**
	 * Predicate that filters out Match that match only on origin (pseudo conflict).
	 */
	private static final Predicate<Match> PSEUDO_MATCH = new Predicate<Match>() {
		public boolean apply(Match input) {
			return input.getOrigin() != null && input.getLeft() == null && input.getRight() == null;
		}
	};

	private static final Predicate<Diff> LEFT_CONFLICTUAL_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input.getConflict() == null
					|| (input.getConflict() != null && input.getSource() == DifferenceSource.LEFT);
		}
	};

	/**
	 * Constructor calling super {@link #MatchItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		Match match = (Match)object;
		Object ret = CompareItemProviderAdapterFactorySpec.getImage(getRootAdapterFactory(), match.getLeft());

		if (ret == null) {
			ret = CompareItemProviderAdapterFactorySpec.getImage(getRootAdapterFactory(), match.getRight());
		}

		if (ret == null) {
			ret = CompareItemProviderAdapterFactorySpec.getImage(getRootAdapterFactory(), match.getOrigin());
		}

		if (ret == null) {
			ret = super.getImage(object);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		Match match = (Match)object;
		String ret = CompareItemProviderAdapterFactorySpec.getText(getRootAdapterFactory(), match.getLeft());

		if (ret == null) {
			ret = CompareItemProviderAdapterFactorySpec.getText(getRootAdapterFactory(), match.getRight());
		}

		if (ret == null) {
			ret = CompareItemProviderAdapterFactorySpec.getText(getRootAdapterFactory(), match.getOrigin());
		}

		if (ret == null) {
			ret = super.getText(object);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		return ImmutableList.copyOf(getChildrenIterable(object));
	}

	/**
	 * Returns an {@link Iterable} with children of the given {@link Match}.
	 * 
	 * @param object
	 *            the match object to retrieve children from.
	 * @return an {@link Iterable} with children of the given {@link Match}.
	 * @see #getChildren(Object)
	 */
	private Iterable<EObject> getChildrenIterable(Object object) {
		Collection<?> children = super.getChildren(object);

		Iterable<Match> matchToDisplay = filter(filter(children, Match.class), and(not(PSEUDO_MATCH),
				HAS_DIFFERENCE));

		Iterable<Diff> leftConflictualDiffs = filter(filter(children, Diff.class), LEFT_CONFLICTUAL_DIFF);

		Iterable<EObject> ret = concat(matchToDisplay, leftConflictualDiffs);
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		return !isEmpty(getChildrenIterable(object));
	}
}
