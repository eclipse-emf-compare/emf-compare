/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Diff;

/**
 * This interface describes the general contract of what EMF Compare expects in order to "copy all" diffs from
 * a given Comparison or list.
 * <p>
 * Clients can either implement this interface or inherit from the default {@link BatchMerger}.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public interface IBatchMerger {
	/**
	 * This will be called to copy all of the differences from the given list from the left to the right side.
	 * <p>
	 * <b>Note</b> that this may end up merging differences that are not in the given list if one of the diffs
	 * to be merged depends on (or results in) the merging of that 'out of list' difference.
	 * </p>
	 * 
	 * @param differences
	 *            The differences that will be merged.
	 * @param monitor
	 *            Monitor on which to report progress information.
	 */
	void copyAllLeftToRight(Iterable<? extends Diff> differences, Monitor monitor);

	/**
	 * This will be called to copy all of the differences from the given list from the right to the left side.
	 * <p>
	 * <b>Note</b> that this may end up merging differences that are not in the given list if one of the diffs
	 * to be merged depends on (or results in) the merging of that 'out of list' difference.
	 * </p>
	 * 
	 * @param differences
	 *            The differences that will be merged.
	 * @param monitor
	 *            Monitor on which to report progress information.
	 */
	void copyAllRightToLeft(Iterable<? extends Diff> differences, Monitor monitor);
}
