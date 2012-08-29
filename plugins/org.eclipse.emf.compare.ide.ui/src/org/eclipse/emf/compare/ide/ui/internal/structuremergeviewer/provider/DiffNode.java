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
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasPseudoConflict;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasRealConflict;

import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode;

/**
 * Specific {@link AbstractEDiffNode} for {@link Diff} objects.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DiffNode extends AbstractEDiffNode {
	/**
	 * Call {@link AbstractEDiffNode super} constructor.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public DiffNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
	 */
	@Override
	public Diff getTarget() {
		return (Diff)super.getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getKind()
	 */
	@Override
	public int getKind() {
		int ret = Differencer.NO_CHANGE;
		final Diff diff = getTarget();
		final DifferenceSource source = diff.getSource();
		final Match match = diff.getMatch();
		final Conflict conflict = diff.getConflict();
		final DifferenceKind diffKind = diff.getKind();
		final Comparison c = match.getComparison();
		if (c.isThreeWay()) {
			switch (source) {
				case LEFT:
					ret |= Differencer.LEFT;
					break;
				case RIGHT:
					ret |= Differencer.RIGHT;
					break;
				default:
					// Cannot happen ... for now.
					break;
			}
			if (conflict != null) {
				ret |= Differencer.CONFLICTING;
				if (conflict.getKind() == ConflictKind.PSEUDO) {
					ret |= Differencer.PSEUDO_CONFLICT;
				}
			} else if (any(diff.getRequiredBy(), hasRealConflict())) {
				ret |= Differencer.CONFLICTING;
			} else if (any(diff.getRequiredBy(), hasPseudoConflict())) {
				// We know there is no real conflict as that would have been handled above
				ret |= Differencer.CONFLICTING | Differencer.PSEUDO_CONFLICT;
			}

			switch (diffKind) {
				case ADD:
					ret |= Differencer.ADDITION;
					break;
				case DELETE:
					ret |= Differencer.DELETION;
					break;
				case CHANGE:
					// fallthrough
				case MOVE:
					ret |= Differencer.CHANGE;
					break;
				default:
					// Cannot happen ... for now
					break;
			}
		} else {
			switch (diffKind) {
				case ADD:
					ret |= Differencer.DELETION;
					break;
				case DELETE:
					ret |= Differencer.ADDITION;
					break;
				case CHANGE:
					// fallthrough
				case MOVE:
					ret |= Differencer.CHANGE;
					break;
				default:
					// Cannot happen ... for now
					break;
			}
		}
		return ret;
	}
}
