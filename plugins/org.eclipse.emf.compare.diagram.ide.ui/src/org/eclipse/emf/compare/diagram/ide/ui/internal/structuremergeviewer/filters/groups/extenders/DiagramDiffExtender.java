/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.structuremergeviewer.filters.groups.extenders;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.IDifferenceGroupExtender;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * Difference group extender for diagram diffs.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class DiagramDiffExtender implements IDifferenceGroupExtender {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.IDifferenceGroupExtender#addChildren(TreeNode)
	 */
	public void addChildren(TreeNode treeNode) {
		Adapter group = EcoreUtil.getAdapter(treeNode.eAdapters(), IDifferenceGroup.class);
		if (group instanceof BasicDifferenceGroupImpl) {
			EObject data = treeNode.getData();
			DiagramDiff diagramDiff = (DiagramDiff)data;
			EObject view = diagramDiff.getView();
			Comparison comparison = diagramDiff.getMatch().getComparison();
			Match match = comparison.getMatch(view);
			if (match != null) {
				for (Match subMatch : match.getSubmatches()) {
					List<TreeNode> buildSubTree = ((BasicDifferenceGroupImpl)group).buildSubTree(match,
							subMatch);
					for (TreeNode subTreeNode : buildSubTree) {
						treeNode.getChildren().addAll(subTreeNode.getChildren());
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.extender.IDifferenceGroupExtender#handle(TreeNode)
	 */
	public boolean handle(TreeNode treeNode) {
		return false;
	}
}
