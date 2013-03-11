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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * This implementation of a {@link IDifferenceGroupProvider} will be used to group the differences by their
 * {@link DifferenceKind kind} : additions, deletions, changes and moves.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public class KindGroupProvider implements IDifferenceGroupProvider {

	/** A human-readable label for this group provider. This will be displayed in the EMF Compare UI. */
	private String label;

	/** The initial activation state of the group provider. */
	private boolean activeByDefault;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Iterable<? extends IDifferenceGroup> getGroups(Comparison comparison) {
		final List<Diff> diffs = comparison.getDifferences();

		final IDifferenceGroup additions = new BasicDifferenceGroupImpl(comparison, diffs,
				ofKind(DifferenceKind.ADD), "Additions");
		final IDifferenceGroup deletions = new BasicDifferenceGroupImpl(comparison, diffs,
				ofKind(DifferenceKind.DELETE), "Deletions");
		final IDifferenceGroup changes = new BasicDifferenceGroupImpl(comparison, diffs,
				ofKind(DifferenceKind.CHANGE), "Changes");
		final IDifferenceGroup moves = new BasicDifferenceGroupImpl(comparison, diffs,
				ofKind(DifferenceKind.MOVE), "Moves");

		return ImmutableList.of(additions, deletions, changes, moves);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#defaultSelected()
	 */
	public boolean defaultSelected() {
		return activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#setDefaultSelected(boolean)
	 */
	public void setDefaultSelected(boolean activeByDefault) {
		this.activeByDefault = activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#isEnabled(org
	 *      .eclipse.emf.compare.scope.IComparisonScope, org.eclipse.emf.compare.Comparison)
	 */
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		return true;
	}

}
