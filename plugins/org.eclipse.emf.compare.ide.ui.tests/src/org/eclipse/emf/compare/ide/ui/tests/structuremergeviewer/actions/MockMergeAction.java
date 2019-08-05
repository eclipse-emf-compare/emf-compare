/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.structuremergeviewer.actions;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.compare.INavigatable;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.actions.MergeAction;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.merge.IMerger.Registry;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
@SuppressWarnings("restriction")
public class MockMergeAction extends MergeAction {

	public MockMergeAction(IEMFCompareConfiguration compareConfiguration, Registry mergerRegistry,
			MergeMode mode, INavigatable navigatable) {
		super(compareConfiguration, mergerRegistry, mode, navigatable);
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
	protected List<Diff> getSelectedDifferences(IStructuredSelection selection) {
		List<?> selectedObjects = selection.toList();
		return selectedObjects.stream().filter(TreeNode.class::isInstance)
				.map(node -> ((TreeNode)node).getData()).filter(Diff.class::isInstance).map(Diff.class::cast)
				.collect(Collectors.toList());
	}
}
