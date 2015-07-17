/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.List;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class MockMergeAction extends MergeAction {

	public MockMergeAction(ICompareEditingDomain editingDomain, Registry mergerRegistry, MergeMode mode,
			boolean isLeftEditable, boolean isRightEditable, INavigatable navigatable) {
		super(editingDomain, mergerRegistry, mode, isLeftEditable, isRightEditable, navigatable);
	}

	@Override
	public boolean updateSelection(IStructuredSelection selection) {
		return super.updateSelection(selection);
	}

	@Override
	protected void clearCache() {
		super.clearCache();
	}

	@Override
	protected Iterable<Diff> getSelectedDifferences(IStructuredSelection selection) {
		List<?> selectedObjects = selection.toList();
		Iterable<TreeNode> selectedTreeNode = filter(selectedObjects, TreeNode.class);
		Iterable<EObject> selectedEObjects = transform(selectedTreeNode, IDifferenceGroup.TREE_NODE_DATA);
		return filter(selectedEObjects, Diff.class);
	}
}
