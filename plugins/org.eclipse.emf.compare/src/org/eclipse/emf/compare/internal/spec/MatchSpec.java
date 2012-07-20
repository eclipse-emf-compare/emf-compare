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

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.MatchImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * This specialization of the {@link MatchImpl} class allows us to define the derived features and operations
 * implementations.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchSpec extends MatchImpl {

	/**
	 * Function returning {@link #getSubmatches() all sub matches} of the given match.
	 */
	private static final Function<Match, Iterable<Match>> ALL_SUBMATCHES = new Function<Match, Iterable<Match>>() {
		public Iterable<Match> apply(Match match) {
			if (match == null) {
				return Lists.newArrayList();
			}
			final Iterable<Match> allSubmatches = concat(transform(match.getSubmatches(), ALL_SUBMATCHES));
			return concat(match.getSubmatches(), allSubmatches);
		}
	};

	/**
	 * Function returning {@link #getDifferences() all DIFFERENCES} of the given match.
	 */
	private static final Function<Match, List<Diff>> DIFFERENCES = new Function<Match, List<Diff>>() {
		public List<Diff> apply(Match match) {
			if (match == null) {
				return Lists.newArrayList();
			}
			return match.getDifferences();
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.MatchImpl#getComparison()
	 */
	@Override
	public Comparison getComparison() {
		Comparison ret = null;

		EObject eContainer = eContainer();
		while (!(eContainer instanceof Comparison) && eContainer != null) {
			eContainer = eContainer.eContainer();
		}

		if (eContainer != null) {
			ret = (Comparison)eContainer;
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.MatchImpl#getAllSubmatches()
	 */
	@Override
	public Iterable<Match> getAllSubmatches() {
		return ALL_SUBMATCHES.apply(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.MatchImpl#getAllDifferences()
	 */
	@Override
	public Iterable<Diff> getAllDifferences() {
		final Iterable<Diff> allSubDifferences = concat(transform(getAllSubmatches(), DIFFERENCES));
		return concat(getDifferences(), allSubDifferences);
	}
}
