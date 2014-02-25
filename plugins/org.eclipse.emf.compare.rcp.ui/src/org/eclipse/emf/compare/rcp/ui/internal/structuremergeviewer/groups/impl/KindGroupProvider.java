/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
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
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.AbstractDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;

/**
 * This implementation of a
 * {@link org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider} will be used to
 * group the differences by their {@link DifferenceKind kind} : additions, deletions, changes and moves.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
 */
public class KindGroupProvider extends AbstractDifferenceGroupProvider {

	/** The groups provided by this provider. */
	private ImmutableList<IDifferenceGroup> differenceGroups;

	/** The comparison object. */
	private Comparison comp;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#getGroups(org.eclipse.emf.compare.Comparison)
	 */
	public Collection<? extends IDifferenceGroup> getGroups(Comparison comparison) {
		if (differenceGroups == null || !comparison.equals(comp)) {
			dispose();
			this.comp = comparison;
			final IDifferenceGroup additions = new BasicDifferenceGroupImpl(comparison,
					ofKind(DifferenceKind.ADD), EMFCompareRCPUIMessages
							.getString("KindGroupProvider.addition.label"), getCrossReferenceAdapter()); //$NON-NLS-1$
			final IDifferenceGroup deletions = new BasicDifferenceGroupImpl(comparison,
					ofKind(DifferenceKind.DELETE), EMFCompareRCPUIMessages
							.getString("KindGroupProvider.deletion.label"), getCrossReferenceAdapter()); //$NON-NLS-1$
			final IDifferenceGroup changes = new BasicDifferenceGroupImpl(comparison,
					ofKind(DifferenceKind.CHANGE), EMFCompareRCPUIMessages
							.getString("KindGroupProvider.change.label"), getCrossReferenceAdapter()); //$NON-NLS-1$
			final IDifferenceGroup moves = new BasicDifferenceGroupImpl(comparison,
					ofKind(DifferenceKind.MOVE), EMFCompareRCPUIMessages
							.getString("KindGroupProvider.move.label"), getCrossReferenceAdapter()); //$NON-NLS-1$
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
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider#dispose()
	 */
	public void dispose() {
		this.comp = null;
		if (differenceGroups != null) {
			for (IDifferenceGroup group : differenceGroups) {
				group.dispose();
			}
			differenceGroups = null;
		}
	}
}
