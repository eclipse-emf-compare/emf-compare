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

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.ReferenceChangeImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * This specialization of the {@link referenceChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ReferenceChangeSpec extends ReferenceChangeImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#apply()
	 */
	@Override
	public void apply() {
		// "apply" is merging from right (reference) to left (working copy)
		if (getSource() == DifferenceSource.LEFT) {
			// Any diff that is from the left side can simply be left alone : it is already "applied"
		}

		// Merge all of our dependencies if any
		for (Diff dependency : getRequires()) {
			// FIXME handle dependency circles
			dependency.apply();
		}

		if (getKind() == DifferenceKind.ADD) {
			if (!getReference().isContainment()) {
				applyAddToContainment();
			} else {
				applyAdd();
			}
		}
	}

	protected void applyAdd() {
		// this is an addition which has been done on the right side.
		// Applying is simply doing the same addition on the left
		final EObject expectedContainer = getMatch().getLeft();
		final Match valueMatch = getMatch().getComparison().getMatch(getValue());

		if (expectedContainer == null || valueMatch == null || valueMatch.getLeft() == null) {
			// FIXME throw exception? log? re-try to match the requires?
			// one of the "required" diffs should have created our container.
			// another should have created our left value and added it to its Match
		} else {
			final EObject expectedValue = valueMatch.getLeft();

			// We have the container reference and value. We need to know the insertion index
		}
	}

	protected void applyAddToContainment() {
		// this is an addition which has been done on the right side.
		// Applying is simply doing the same addition on the left
		final EObject expectedContainer = getMatch().getLeft();

		if (expectedContainer == null) {
			// This should never happen : one of the "required" diffs should have created our container.
		}
	}
}
