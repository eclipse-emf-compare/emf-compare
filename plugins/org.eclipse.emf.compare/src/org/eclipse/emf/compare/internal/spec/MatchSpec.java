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
import org.eclipse.emf.compare.internal.SubMatchIterable;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.compare.utils.Objects;
import org.eclipse.emf.ecore.EObject;

/**
 * This specialization of the {@link MatchImpl} class allows us to define the derived features and operations
 * implementations.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchSpec extends MatchImpl {
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

		EObject container = eContainer();
		while (!(container instanceof Comparison) && container != null) {
			container = container.eContainer();
		}

		if (container != null) {
			ret = (Comparison)container;
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
		return new SubMatchIterable(this);
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

	/**
	 * Returns whether the given object is the same object as the {@link #left}, {@link #right}, or
	 * {@link #origin}. It is used by {@link EqualityHelper#matchingValues(Object, Object)} and
	 * {@link EqualityHelper#matchingValues(EObject, EObject)}.
	 * 
	 * @param object
	 *            the object in question
	 * @return whether the given object is the same object as the left, right, or origin.
	 */
	public boolean matches(Object object) {
		return object == left || object == right || object == origin;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.impl.BasicEObjectImpl#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(this).add("left", EObjectUtil.getLabel(getLeft()))
				.add("right", EObjectUtil.getLabel(getRight()))
				.add("origin", EObjectUtil.getLabel(getOrigin()))
				.add("#differences", Integer.valueOf(getDifferences().size()))
				.add("#submatches", Integer.valueOf(getSubmatches().size())).toString();
		// @formatter:on
	}
}
