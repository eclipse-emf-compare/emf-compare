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
package org.eclipse.emf.compare.match;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * This class defines the general contract of a Matching engine. We expect subclasses to have a public,
 * no-argument default constructor for instantiation.
 * <p>
 * We generally expect that a call to {@link #match(IComparisonScope)} will return us every single
 * {@link org.eclipse.emf.compare.Match matches} that can be determined from the given
 * {@link IComparisonScope context}. This includes all three of :
 * <ul>
 * <li>Elements that are present on all three sides of the comparison scope,</li>
 * <li>Elements that are present on only two sides,</li>
 * <li>Elements that are only present on a single side.</li>
 * </ul>
 * </p>
 * <p>
 * Clients can subclass the {@link DefaultMatchEngine default implementation} when all that is needed is to
 * change the matching strategy.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see DefaultMatchEngine
 */
public interface IMatchEngine {
	/**
	 * This is the entry point of a Comparison process. It is expected to use the provided scope in order to
	 * determine all objects that need to be matched.
	 * <p>
	 * The returned Comparison should include both matched an unmatched objects. It is not the match engine's
	 * responsibility to determine differences between objects, only to match them together.
	 * </p>
	 * 
	 * @param scope
	 *            The comparison scope that should be used by this engine to determine the objects to match.
	 * @return An initialized {@link Comparison} model with all matches determined.
	 */
	Comparison match(IComparisonScope scope);
}
