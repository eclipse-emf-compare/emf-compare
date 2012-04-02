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
package org.eclipse.emf.compare.diff;

import org.eclipse.emf.compare.Comparison;

/**
 * This class defines the general contract of a Differencing engine. We expect subclasses to have a public,
 * no-argument default constructor for instantiation.
 * <p>
 * We generally expect that a call to {@link #diff(Comparison)} will complete every single
 * {@link org.eclipse.emf.compare.Match} it finds with all differences that can be detected on its sides. The
 * diff engine is not expected to detect conflicts, equivalences or dependencies between different diffs,
 * these will be detected later on.
 * </p>
 * <p>
 * Clients can also subclass the {@link DefaultDiffEngine default implementation}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see DefaultDiffEngine
 */
public interface IDiffEngine {
	/**
	 * This is the entry point of the differencing process.
	 * <p>
	 * It will complete the input <code>comparison</code> by iterating over the
	 * {@link org.eclipse.emf.compare.Match matches} it contain, filling in the differences it can detect for
	 * each distinct Match.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 */
	void diff(Comparison comparison);
}
