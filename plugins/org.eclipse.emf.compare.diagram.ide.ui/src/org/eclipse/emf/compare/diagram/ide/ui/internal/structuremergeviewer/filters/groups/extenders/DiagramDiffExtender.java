/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.internal.structuremergeviewer.filters.groups.extenders;

import static org.eclipse.emf.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.BasicDifferenceGroupImpl;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.extender.IDifferenceGroupExtender;
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
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.extender.IDifferenceGroupExtender#addChildren(TreeNode)
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
				Diff primeRefining = diagramDiff.getPrimeRefining();
				if (CONTAINMENT_REFERENCE_CHANGE.apply(primeRefining)) {
					List<TreeNode> buildSubTree = ((BasicDifferenceGroupImpl)group)
							.buildContainmentSubTree(match);
					treeNode.getChildren().addAll(buildSubTree);
				} else {
					for (Match subMatch : match.getSubmatches()) {
						List<TreeNode> buildSubTree = ((BasicDifferenceGroupImpl)group)
								.buildContainmentSubTree(subMatch);
						treeNode.getChildren().addAll(buildSubTree);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.extender.IDifferenceGroupExtender#handle(TreeNode)
	 */
	public boolean handle(TreeNode treeNode) {
		return false;
	}
}
