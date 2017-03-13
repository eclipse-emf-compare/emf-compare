/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.merge;

import java.util.Set;

import org.eclipse.emf.compare.Diff;

/**
 * A computer to retrieve the dependent diffs, resulting merges and resulting rejections for a diff.
 * 
 * @since 3.5
 * @author Martin Fleck <mfleck@eclipsesource.com>
 * @see IMerger2
 */
public interface IDiffRelationshipComputer {
	/**
	 * Retrieves the set of directly required diffs needed in order to merge the given <code>diff</code>. This
	 * may includes the diff's requirements or any other diff that we need to merge before the given one.
	 * 
	 * @param diff
	 *            The difference for which we seek the direct merge dependencies.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return A non-null set of direct merge dependencies.
	 * @see IMerger2#getDirectMergeDependencies(Diff, boolean)
	 */
	Set<Diff> getDirectMergeDependencies(Diff diff, boolean mergeRightToLeft);

	/**
	 * Returns all differences that will be merged because of our merging the given <code>diff</code>. This
	 * may include the diff's implications, the diff's equivalences, the diff's refinements or any other diff
	 * that we need to merge together with the given diff.
	 * 
	 * @param diff
	 *            The difference for which we seek the direct merge dependencies.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return A non-null set of direct resulting merges.
	 * @see IMerger2#getDirectResultingMerges(Diff, boolean)
	 */
	Set<Diff> getDirectResultingMerges(Diff diff, boolean mergeRightToLeft);

	/**
	 * Returns the set of all differences that need to be rejected if <code>diff</code> is merged in the given
	 * direction.
	 * 
	 * @param diff
	 *            The difference for which we seek the direct resulting rejections.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return A non-null set of direct resulting rejections.
	 * @see #getAllResultingRejections(Diff, boolean)
	 * @see IMerger2#getDirectResultingRejections(Diff, boolean)
	 */
	Set<Diff> getDirectResultingRejections(Diff diff, boolean mergeRightToLeft);

	/**
	 * Retrieves the set of all diffs related to the given <code>diff</code> when merging in the given
	 * direction.
	 * <p>
	 * This is expected to return the set of all differences that will be need to merged along when a user
	 * wishes to merge <code>diff</code>, either because they are required by it or because they are implied
	 * by it one way or another.
	 * </p>
	 * <p>
	 * Note that <code>diff</code> will be included in the returned set.
	 * </p>
	 * <p>
	 * Also note that the resulting merges will contain the resulting rejections (diffs from the other side
	 * that will be rejected)
	 * </p>
	 * 
	 * @param diff
	 *            The difference for which we seek all related ones.
	 * @param rightToLeft
	 *            The direction in which we're considering a merge.
	 * @return A non-null set of all diffs related to the given <code>diff</code> when merging in the given
	 *         direction.
	 */
	Set<Diff> getAllResultingMerges(Diff diff, boolean rightToLeft);

	/**
	 * Retrieves the set of all diffs that will be rejected if the given <code>diff</code> is merged, either
	 * because of unresolveable conflicts or because of unreachable requirements.
	 * 
	 * @param diff
	 *            The difference for which we seek all opposite ones.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return A non-null set of all diffs that will be rejected if the given <code>diff</code> is merged in
	 *         the given direction.
	 */
	Set<Diff> getAllResultingRejections(Diff diff, boolean mergeRightToLeft);

	/**
	 * Returns the merger registry used for calculating the diff relationships.
	 * 
	 * @return The merger registry.
	 */
	IMerger.Registry getMergerRegistry();

	/**
	 * Sets the merger registry used for calculating the diff relationships.
	 * 
	 * @param mergerRegistry
	 *            The merger registry.
	 */
	void setMergerRegistry(IMerger.Registry mergerRegistry);

	/**
	 * Returns the merge criterion considered for calculating the diff relationships.
	 * 
	 * @return The merge criterion.
	 */
	IMergeCriterion getMergeCriterion();

	/**
	 * Sets the merge criterion considered for calculating the diff relationships.
	 * 
	 * @param mergeCriterion
	 *            The merger criterion.
	 */
	void setMergeCriterion(IMergeCriterion mergeCriterion);

	/**
	 * Returns the best-fitting merger for the given diff according to the {@link #getMergerRegistry() merger
	 * registry} and the {@link #getMergeCriterion() merge criterion}.
	 * 
	 * @param diff
	 *            The difference for which we seek the merger.
	 * @return The best-fitting merger for the given diff or null if no merger fits.
	 */
	IMerger2 getMerger(Diff diff);

	/**
	 * Indicates whether a best-fitting merger for the given diff is available.
	 * 
	 * @param diff
	 *            The difference for which we seek the merger.
	 * @return Returns true if a best-fitting merger is available, false otherwise.
	 */
	boolean hasMerger(Diff diff);
}
