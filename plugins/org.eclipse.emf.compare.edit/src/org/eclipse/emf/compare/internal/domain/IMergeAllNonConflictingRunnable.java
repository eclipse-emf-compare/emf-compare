/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.domain;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.IMerger.Registry;

/**
 * The basic contract for a "merge all non-conflicting" differences.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public interface IMergeAllNonConflictingRunnable {
	/**
	 * Merges all non-conflicting differences from the given comparison in either direction.
	 * 
	 * @param comparison
	 *            The comparison which differences we are to merge.
	 * @param leftToRight
	 *            Direction of the merge operation.
	 * @param mergerRegistry
	 *            The registry to query for the appropriate merger for each difference.
	 * @return an iterable over the differences that have actually been merged by this operation.
	 */
	Iterable<Diff> merge(Comparison comparison, boolean leftToRight, Registry mergerRegistry);
}
