/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * This action will reveal in the StructureMergeViewer all other differences conflicting with the currently
 * selected one.
 * 
 * @author Laurent Goubet <laurent.goubet@obeo.fr>
 */
public class RevealConflictingDiffsAction extends Action {
	private final StructuredViewer viewer;

	private final Diff selectedDiff;

	private final IEMFCompareConfiguration compareConfiguration;

	public RevealConflictingDiffsAction(StructuredViewer viewer, Diff selectedDiff,
			IEMFCompareConfiguration compareConfiguration) {
		super(EMFCompareIDEUIMessages.getString("show.conflicting.diffs.action.text")); //$NON-NLS-1$
		this.viewer = viewer;
		this.selectedDiff = selectedDiff;
		this.compareConfiguration = compareConfiguration;
	}

	@Override
	public void run() {
		Conflict conflict = selectedDiff.getConflict();
		if (conflict == null) {
			return;
		}

		List<Diff> diffsToReveal = conflict.getDifferences();
		for (Diff diff : diffsToReveal) {
			List<TreeNode> nodes = compareConfiguration.getStructureMergeViewerGrouper().getProvider()
					.getTreeNodes(diff);
			for (TreeNode node : nodes) {
				getCompareInputAdapter(node).ifPresent(adapter -> viewer.reveal(adapter));
			}
		}
	}

	private Optional<Adapter> getCompareInputAdapter(TreeNode node) {
		return node.eAdapters().stream().filter(adapter -> adapter instanceof CompareInputAdapter).findAny();
	}
}
