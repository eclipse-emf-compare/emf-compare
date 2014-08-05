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
package org.eclipse.emf.compare.merge;

import java.util.Set;

import org.eclipse.emf.compare.Diff;

/**
 * This adds the ability for {@link IMerger mergers} to define their own sub-set of "required" differences.
 * <p>
 * This set of differences is what will be used by the UI to highlight related differences, by the batch
 * merger to determine whether the diff can be merged beforehand (one of the related being in conflict will
 * prevent the merge as a whole), ...
 * </p>
 * <p>
 * The {@link AbstractMerger} implements this and can be sub-classed instead when the default set of
 * requirements is enough.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.1
 */
public interface IMerger2 extends IMerger {
	/**
	 * Retrieves the set of all diffs related to the given <code>diff</code> when merging in the given
	 * direction.
	 * <p>
	 * This is expected to return the set of all differences that will be merged along when a user wishes to
	 * merge <code>diff</code>, either because they are related or because they are equivalent one way or
	 * another.
	 * </p>
	 * <p>
	 * Note that as far as the merged is concerned a given diff is considered to be implying itself.
	 * <code>diff</code> will thus be included in the returned set.
	 * </p>
	 * 
	 * @param diff
	 *            The difference for which we seek all related ones.
	 * @param mergeLeftToRight
	 *            The direction in which we're considering a merge.
	 * @param knownImplications
	 *            The set of Diffs already known as being implied by our starting point. Since there may be
	 *            implication cycles, this can be used to break free. Callees are not supposed to add the new
	 *            implications they find within this set.
	 * @return The set of all diffs related to the given <code>diff</code> when merging in the given
	 *         direction.
	 */
	Set<Diff> getResultingMerges(Diff diff, boolean mergeLeftToRight, Set<Diff> knownImplications);

	/**
	 * Retrieves the set of all diffs that will be rejected if the given <code>diff</code> is merged, either
	 * because of unresolveable conflicts or because of unreachable requirements.
	 * 
	 * @param diff
	 *            The difference for which we seek all opposite ones.
	 * @param mergeLeftToRight
	 *            The direction in which we're considering a merge.
	 * @param knownRejections
	 *            The set of Diffs already known as being rejected by our starting point. Since there may be
	 *            rejection cycles, this can be used to break free. Callees are not supposed to add the new
	 *            rejections they find within this set.
	 * @return The set of all diffs that will be rejected if the given <code>diff</code> is merged in the
	 *         given direction.
	 */
	Set<Diff> getResultingRejections(Diff diff, boolean mergeLeftToRight, Set<Diff> knownRejections);
}
