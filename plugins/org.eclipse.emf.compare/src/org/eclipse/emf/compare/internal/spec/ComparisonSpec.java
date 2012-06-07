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
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.impl.ComparisonImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * This specialization of the {@link ComparisonImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ComparisonSpec extends ComparisonImpl {
	/** TODO use cross referencer adapters : the Match can evolve through merging. */
	private Map<EObject, Match> eObjectToMatch;

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
	 * @see org.eclipse.emf.compare.impl.ComparisonImpl#getMatch(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public Match getMatch(EObject element) {
		// TODO use cross referencer adapters : the Match can evolve through merging
		if (element == null) {
			return null;
		}

		if (eObjectToMatch == null) {
			eObjectToMatch = Maps.newHashMap();
			for (Match root : getMatches()) {
				recurseMapMatch(root);
			}
		}

		return eObjectToMatch.get(element);
	}

	/**
	 * TODO use cross referencer adapters : the Match can evolve through merging.
	 * 
	 * @param match
	 *            current Match.
	 */
	private void recurseMapMatch(Match match) {
		if (match.getLeft() != null) {
			eObjectToMatch.put(match.getLeft(), match);
		}
		if (match.getRight() != null) {
			eObjectToMatch.put(match.getRight(), match);
		}
		if (match.getOrigin() != null) {
			eObjectToMatch.put(match.getOrigin(), match);
		}
		for (Match child : match.getSubmatches()) {
			recurseMapMatch(child);
		}
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
