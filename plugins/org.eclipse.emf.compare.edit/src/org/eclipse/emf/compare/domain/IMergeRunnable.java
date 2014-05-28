/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.domain;

import java.util.List;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.merge.IMerger.Registry;

/**
 * Interface for executing merge commands from the {@link ICompareEditingDomain}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 4.0
 */
public interface IMergeRunnable {

	/**
	 * Execute the merge operation.
	 * 
	 * @param differences
	 *            the differences to merge.
	 * @param leftToRight
	 *            whether the differences have to be merge from left to right or right to left.
	 * @param mergerRegistry
	 *            the merger registry to query to get the appropriate mergers for each difference to be
	 *            merged.
	 */
	void merge(List<? extends Diff> differences, boolean leftToRight, Registry mergerRegistry);

}
