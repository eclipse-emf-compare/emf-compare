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
package org.eclipse.emf.compare.ide.ui.internal.actions.group;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;

/**
 * This implementation of a {@link DifferenceGroupProvider} will be used to group the differences by their
 * {@link DifferenceKind kind} : additions, deletions, changes and moves.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class KindGroupProvider implements DifferenceGroupProvider {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.actions.group.DifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Iterable<? extends DifferenceGroup> getGroups(Comparison comparison) {
		final List<Diff> diffs = comparison.getDifferences();

		final DifferenceGroup additions = new DefaultDifferenceGroup(comparison, diffs,
				ofKind(DifferenceKind.ADD), "Additions");
		final DifferenceGroup deletions = new DefaultDifferenceGroup(comparison, diffs,
				ofKind(DifferenceKind.DELETE), "Deletions");
		final DifferenceGroup changes = new DefaultDifferenceGroup(comparison, diffs,
				ofKind(DifferenceKind.CHANGE), "Changes");
		final DifferenceGroup moves = new DefaultDifferenceGroup(comparison, diffs,
				ofKind(DifferenceKind.MOVE), "Moves");

		return ImmutableList.of(additions, deletions, changes, moves);
	}
}
