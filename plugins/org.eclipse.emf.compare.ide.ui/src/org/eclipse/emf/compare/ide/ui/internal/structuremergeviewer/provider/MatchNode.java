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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.isEmpty;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.base.Predicate;

import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode;
import org.eclipse.emf.ecore.EObject;

/**
 * Specific AbstractEDiffNode for {@link Match} objects.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchNode extends AbstractEDiffNode {

	/**
	 * 
	 */
	private static final Predicate<Diff> CONFLICTUAL_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getConflict() != null;
		}
	};

	/**
	 * Creates a node with the given factory.
	 * 
	 * @param adapterFactory
	 *            the factory given to the super constructor.
	 */
	public MatchNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public Match getTarget() {
		return (Match)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode#getKind()
	 */
	@Override
	public int getKind() {
		int ret = super.getKind();

		final EObject ancestor = getTarget().getOrigin();
		final EObject left = getTarget().getLeft();
		final EObject right = getTarget().getRight();

		final Iterable<Diff> differences = getTarget().getAllDifferences();

		if (getTarget().getComparison().isThreeWay()) {
			/*
			 * Differencer.CONFLICTING == Differencer.LEFT | Differencer.RIGHT. With that in mind, all we need
			 * to check is whether this is a pseudo conflict, and the kind of diff (deletion, addition).
			 */
			if (any(differences, hasConflict(ConflictKind.REAL))) {
				ret |= Differencer.CONFLICTING;
			} else if (any(differences, hasConflict(ConflictKind.PSEUDO))) {
				// "pseudo" does not include the direction bits, we add them both through "CONFLITING"
				ret |= Differencer.CONFLICTING | Differencer.PSEUDO_CONFLICT;
			} else {
				if (any(differences, fromSide(DifferenceSource.LEFT))) {
					ret |= Differencer.LEFT;
				}
				if (any(differences, fromSide(DifferenceSource.RIGHT))) {
					ret |= Differencer.RIGHT;
				}
			}

			if (ancestor == null) {
				if (left == null || right == null) {
					ret |= Differencer.ADDITION;
				} else {
					// Can't have all three sides null.
				}
			} else if (left == null || right == null) {
				ret |= Differencer.DELETION;
			} else if (!isEmpty(differences)) {
				ret |= Differencer.CHANGE;
			}
		} else {
			// no direction bit in two-way
			if (left == null) {
				ret |= Differencer.DELETION;
			} else if (right == null) {
				ret |= Differencer.ADDITION;
			} else if (!isEmpty(differences)) {
				ret |= Differencer.CHANGE;
			}
		}
		return ret;
	}
}
