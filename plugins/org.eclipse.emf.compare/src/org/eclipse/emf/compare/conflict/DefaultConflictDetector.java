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
package org.eclipse.emf.compare.conflict;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;

/**
 * The conflict detector is in charge of refining the Comparison model with all detected Conflict between its
 * differences.
 * <p>
 * This default implementation of {@link IConflictDetector} should detect most generic cases, but is not aimed
 * at detecting conflicts at "business" level. For example, adding two enum literals of the same value but
 * distinct IDs might be seen as a conflict... but that is not the "generic" case.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultConflictDetector implements IConflictDetector {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.conflict.IConflictDetector#detect(org.eclipse.emf.compare.Comparison)
	 */
	public void detect(Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();
		final int diffCount = differences.size();

		for (int i = 0; i < diffCount; i++) {
			final Diff diff1 = differences.get(i);
			for (int j = 0; j < diffCount; j++) {
				checkConflict(diff1, differences.get(j));
			}
		}
	}

	protected void checkConflict(Diff diff1, Diff diff2) {

	}

	/**
	 * Checks if the given {@link Diff diff1} has already been detected as being in conflict with the given
	 * {@link Diff diff2}.
	 * 
	 * @param diff1
	 *            First of the two differences we expect to be in conflict with each other.
	 * @param diff2
	 *            Second of the two differences we expect to be in conflict with each other.
	 * @return {@code true} if the two given diffs are in conflict, {@code false} otherwise.
	 */
	protected boolean isInConflictWith(Diff diff1, Diff diff2) {
		final Conflict conflict = diff1.getConflict();

		return conflict != null && conflict.getDifferences().contains(diff2);
	}
}
