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

import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
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
			return !isEmpty(filter(input.getAllDifferences(), DIFF_TO_DISPLAY));
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

	private static final Predicate<Diff> CONFLICTUAL_DIFF_TO_DISPLAY = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			Conflict conflict = input.getConflict();
			if (conflict != null) {
				if (conflict.getKind() == ConflictKind.PSEUDO) {
					return false;
				}
				if (input.getMatch().getLeft() != null && input.getMatch().getRight() != null) {
					return input.getSource() == DifferenceSource.LEFT;
				}
			}
			return true;
		}
	};

	private static final Predicate<Diff> CONTAINMENT_REFERENCE_CHANGE = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			if (input instanceof ReferenceChange) {
				ReferenceChange referenceChange = (ReferenceChange)input;
				return referenceChange.getReference().isContainment();
			}
			return false;
		}
	};

	private static final Predicate<Diff> PSEUDO_DELETE_CONFLICT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input.getConflict() != null && input.getConflict().getKind() == ConflictKind.PSEUDO
					&& input.getKind() == DifferenceKind.DELETE;
		}
	};

	/**
	 * 
	 */
	private static final Predicate<Diff> DIFF_TO_DISPLAY = alwaysTrue();

	// and(CONFLICTUAL_DIFF_TO_DISPLAY, not(CONTAINMENT_REFERENCE_CHANGE));

	/**
	 * 
	 */
	private static final Predicate<Match> MATCH_TO_DISPLAY = and(not(PSEUDO_MATCH), HAS_DIFFERENCE);

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
		Match match = (Match)object;
		List<EObject> sub = containmentDifferences(match);
		Collection<?> children = super.getChildren(object);
		for (Object child : children) {
			if (!(child instanceof ReferenceChange)
					|| !((ReferenceChange)child).getReference().isContainment()) {
				sub.add((EObject)child);
			}
		}

		Iterator<EObject> subIt = sub.iterator();
		while (subIt.hasNext()) {
			EObject eObject = subIt.next();
			if (eObject instanceof Diff) {
				Diff diff = (Diff)eObject;
				boolean removed = false;
				if (diff.getConflict() != null) {
					if (diff.getConflict().getKind() == ConflictKind.PSEUDO) {
						subIt.remove();
						removed = true;
					}
				}
				if (!removed && diff.getState() != DifferenceState.UNRESOLVED) {
					subIt.remove();
				}
			}
		}

		Iterator<EObject> it = sub.iterator();
		List<EObject> newSub = newArrayList();
		while (it.hasNext()) {
			EObject eObject = it.next();
			if (eObject instanceof Match) {
				boolean removed = false;
				if (this.getChildren(eObject).isEmpty()) {
					it.remove();
					removed = true;
				}

				if (!removed && containmentDifferences((Match)eObject).size() == 1) {
					it.remove();
					newSub.add(containmentDifferences((Match)eObject).iterator().next());
				}
			}
		}
		ArrayList<EObject> ret = newArrayList(concat(sub, newSub));

		return ret;
	}

	static List<EObject> containmentDifferences(Match match) {
		EObject eContainer = match.eContainer();
		List<EObject> ret = newArrayList();
		if (eContainer instanceof Match) {
			EList<Diff> differences = ((Match)eContainer).getDifferences();
			Iterable<Diff> containmentReferenceChanges = filter(differences, CONTAINMENT_REFERENCE_CHANGE);

			for (Diff containmentReferenceChange : containmentReferenceChanges) {
				EObject value = ((ReferenceChange)containmentReferenceChange).getValue();
				if (value == match.getLeft() || value == match.getRight() || value == match.getOrigin()) {
					ret.add(containmentReferenceChange);
				}
			}
		}
		return ret;
	}

	/**
	 * Returns an {@link Iterable} with children of the given {@link Match}.
	 * 
	 * @param object
	 *            the match object to retrieve children from.
	 * @return an {@link Iterable} with children of the given {@link Match}.
	 * @see #getChildren(Object)
	 */
	static Iterable<EObject> getChildrenIterable(Match object) {
		Iterable<Match> matchToDisplay = filter(object.getSubmatches(), MATCH_TO_DISPLAY);

		Iterable<Diff> diffToDisplay = filter(object.getDifferences(), DIFF_TO_DISPLAY);

		Iterable<EObject> ret = concat(matchToDisplay, diffToDisplay);
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		return !getChildren(object).isEmpty();
	}
}
