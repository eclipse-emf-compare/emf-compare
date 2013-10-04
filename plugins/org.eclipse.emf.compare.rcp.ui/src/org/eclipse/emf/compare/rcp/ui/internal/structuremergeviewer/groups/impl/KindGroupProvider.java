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
import com.google.common.collect.Lists;

import java.util.Collection;

import org.eclipse.emf.compare.Comparison;
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
public class KindGroupProvider extends AbstractDifferenceGroupProvider implements IDifferenceGroupProvider {

	/** A human-readable label for this group provider. This will be displayed in the EMF Compare UI. */
	private String label;

	/** The initial activation state of the group provider. */
	private boolean activeByDefault;

	/** The groups provided by this provider. */
	private ImmutableList<IDifferenceGroup> differenceGroups;

	/** The comparison object. */
	private Comparison comp;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Collection<? extends IDifferenceGroup> getGroups(Comparison comparison) {
		if (differenceGroups == null || !comparison.equals(comp)) {
			this.comp = comparison;
			final IDifferenceGroup additions = new BasicDifferenceGroupImpl(comparison, this,
					ofKind(DifferenceKind.ADD), "Additions", getCrossReferenceAdapter());
			final IDifferenceGroup deletions = new BasicDifferenceGroupImpl(comparison, this,
					ofKind(DifferenceKind.DELETE), "Deletions", getCrossReferenceAdapter());
			final IDifferenceGroup changes = new BasicDifferenceGroupImpl(comparison, this,
					ofKind(DifferenceKind.CHANGE), "Changes", getCrossReferenceAdapter());
			final IDifferenceGroup moves = new BasicDifferenceGroupImpl(comparison, this,
					ofKind(DifferenceKind.MOVE), "Moves", getCrossReferenceAdapter());
			Collection<IDifferenceGroup> groups = Lists.newArrayList();
			if (!additions.getChildren().isEmpty()) {
				groups.add(additions);
			}
			if (!deletions.getChildren().isEmpty()) {
				groups.add(deletions);
			}
			if (!changes.getChildren().isEmpty()) {
				groups.add(changes);
			}
			if (!moves.getChildren().isEmpty()) {
				groups.add(moves);
			}
			differenceGroups = ImmutableList.copyOf(groups);
		}
		return differenceGroups;
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
