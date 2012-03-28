/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;

/**
 * This abstract class provides the base functionality to walk over a given
 * {@link Comparison} {@link Match}es. Sub-classes of this will be used to
 * validate the {@link Match} detected by EMF Compare.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractMatchValidator {
	/**
	 * Default constructor.
	 */
	public AbstractMatchValidator() {
		// Empty implementation
	}

	public void validate(Comparison comparison) {
		for (Match rootMatch : comparison.getMatches()) {
			walkAndValidate(rootMatch);
		}
	}

	/**
	 * Walks over the given match and its sub-matches, calling
	 * {@link #validateMatch(Match)} on each.
	 * 
	 * @param match
	 *            The match we are to validate.
	 */
	protected void walkAndValidate(Match match) {
		validateMatch(match);
		for (Match submatch : match.getSubmatches()) {
			validateMatch(submatch);
		}
	}

	/**
	 * Ensures that the given match is indeed matching the expected elements.
	 * 
	 * @param match
	 *            The match we are to validate.
	 */
	protected abstract void validateMatch(Match match);
}
