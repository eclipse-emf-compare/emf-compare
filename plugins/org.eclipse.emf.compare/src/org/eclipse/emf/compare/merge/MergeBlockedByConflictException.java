/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import java.util.Collection;

import org.eclipse.emf.compare.Diff;

/**
 * Exception that indicates that a merge cannot be performed because it would lead to involuntarily resolving
 * a conflict.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 3.5
 */
public class MergeBlockedByConflictException extends RuntimeException {

	/** Serial version UID. */
	private static final long serialVersionUID = -8395528878410874955L;

	/** The conflicting diffs. */
	private final Collection<Diff> conflictingDiffs;

	/**
	 * Constructor.
	 * 
	 * @param conflictingDiffs
	 *            The diffs that depend on a conflict
	 */
	public MergeBlockedByConflictException(Collection<Diff> conflictingDiffs) {
		this.conflictingDiffs = conflictingDiffs;
	}

	/**
	 * Get the conflicting diffs.
	 * 
	 * @return The conflicting diffs.
	 */
	public Collection<Diff> getConflictingDiffs() {
		return conflictingDiffs;
	}

}
