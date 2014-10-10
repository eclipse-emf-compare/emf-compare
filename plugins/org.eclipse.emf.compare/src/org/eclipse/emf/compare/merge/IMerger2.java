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
	 * Retrieves the set of <b>directly</b> required diffs in order to merge the current one. This may
	 * includes the diff's {@link Diff#getRequires() requirements}, its {@link Diff#getImpliedBy() implying
	 * diff}, or any other diff that we need to merge <u>before</u> the given one.
	 * 
	 * @param diff
	 *            The diff which direct requirements we need.
	 * @param mergeLeftToRight
	 *            The direction in which we're considering a merge.
	 * @return The set of <b>directly</b> required diffs in order to merge the current one.
	 */
	Set<Diff> getDirectMergeDependencies(Diff diff, boolean mergeLeftToRight);

	/**
	 * Returns all differences that will be merged because of our merging the given <code>target</code>
	 * difference.
	 * 
	 * @param target
	 *            The difference we're considering merging.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return The Set of all differences that will be merged because we've merged <code>target</code>.
	 */
	Set<Diff> getDirectResultingMerges(Diff target, boolean mergeRightToLeft);

	/**
	 * Returns the set of all differences that need to be rejected if <code>target</code> is merged in the
	 * given direction.
	 * 
	 * @param target
	 *            The difference we're considering merging.
	 * @param mergeRightToLeft
	 *            The direction in which we're considering a merge.
	 * @return The Set of all differences that will be rejected if we are to merge merged <code>target</code>.
	 */
	Set<Diff> getDirectResultingRejections(Diff target, boolean mergeRightToLeft);
}
