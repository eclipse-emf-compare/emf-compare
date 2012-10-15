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

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;

/**
 * This class defines the general contract of a Conflict detector. We expect subclasses to have a public,
 * no-argument default constructor for instantiation.
 * <p>
 * This will be called by EMF Compare after the differencing engine. We generally expect that a call to
 * {@link #detect(Comparison)} will complete the given comparison with all conflicts it can detect between all
 * differences of this {@link Comparison} instance.
 * </p>
 * <p>
 * Clients can also subclass the {@link DefaultConflictDetector default implementation}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @see DefaultConflictDetector
 */
public interface IConflictDetector {
	/**
	 * This is the entry point of the conflict detection process.
	 * <p>
	 * It is expected to complete the input <code>comparison</code> by iterating over the
	 * {@link org.eclipse.emf.compare.Diff differences} it contain, filling in all conflicts it can detect
	 * between those Diffs.
	 * </p>
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 */
	void detect(Comparison comparison, Monitor monitor);
}
