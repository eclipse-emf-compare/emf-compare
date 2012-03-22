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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.ecore.EObject;

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
		Iterable<Diff> diffIterable = Lists.newArrayList();

		final List<Match> mappings = getMatches();
		for (int i = 0; i < mappings.size(); i++) {
			diffIterable = Iterables.concat(diffIterable, getDifferences(mappings.get(i)));
		}

		final EList<Diff> allDifferences = new BasicEList<Diff>();
		for (Diff diff : diffIterable) {
			((AbstractEList<Diff>)allDifferences).addUnique(diff);
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
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#addDiff(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void addDiff(EObject element, Diff newDiff) {
		final Match match = getMatch(element);
		match.getDifferences().add(newDiff);
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

		final List<EObject> path = Lists.newArrayList(element);

		EObject container = element.eContainer();
		while (container != null) {
			path.add(container);
			container = container.eContainer();
		}

		final ListIterator<EObject> pathIterator = path.listIterator(path.size());
		// We have at least one element in the list
		EObject currentEObject = pathIterator.previous();

		List<Match> rootMatches = getMatches();
		Match currentMatch = null;
		for (int i = 0; i < rootMatches.size() && currentMatch == null; i++) {
			final Match root = rootMatches.get(i);
			if (root.getLeft() == currentEObject || root.getRight() == currentEObject
					|| root.getOrigin() == currentEObject) {
				currentMatch = root;
			}
		}

		while (pathIterator.hasPrevious() && currentMatch != null) {
			currentEObject = pathIterator.previous();
			currentMatch = getMatch(currentMatch, currentEObject);
		}

		return currentMatch;
	}

	/**
	 * Find the sub-match of <code>parent</code> that correspond to the given <code>element</code>, be it the
	 * left, right or origin element of that sub-match.
	 * 
	 * @param parent
	 *            The parent for which we need a corresponding sub-match.
	 * @param element
	 *            The element of which we need the match.
	 * @return The sub-match of <code>parent</code> that correspond to the given <code>element</code>,
	 *         <code>null</code> if we could not find a corresponding submatch.
	 */
	private static Match getMatch(Match parent, EObject element) {
		Match submatch = null;
		for (int i = 0; i < parent.getSubmatches().size() && submatch == null; i++) {
			final Match child = parent.getSubmatches().get(i);
			if (child.getLeft() == element || child.getRight() == element || child.getOrigin() == element) {
				submatch = child;
			}
		}
		return submatch;
	}

	/**
	 * Returns all differences related to the given Match and its sub-matches.
	 * 
	 * @param match
	 *            The match for which we need all differences.
	 * @return All differences related to the given match and its sub-matches.
	 */
	private static Iterable<Diff> getDifferences(Match match) {
		Iterable<Diff> differences = Lists.newArrayList(match.getDifferences());

		final List<Match> submappings = match.getSubmatches();
		for (int i = 0; i < submappings.size(); i++) {
			differences = Iterables.concat(differences, getDifferences(submappings.get(i)));
		}

		return differences;
	}
}
