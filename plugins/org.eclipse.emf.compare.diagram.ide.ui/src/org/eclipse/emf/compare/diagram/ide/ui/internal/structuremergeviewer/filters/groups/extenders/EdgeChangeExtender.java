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

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.diagram.internal.extensions.EdgeChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * Difference group extender for edge changes.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class EdgeChangeExtender extends DiagramDiffExtender {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.extender.IDifferenceGroupExtender#handle(TreeNode)
	 */
	@Override
	public boolean handle(TreeNode treeNode) {
		if (treeNode != null) {
			EObject data = treeNode.getData();
			return data instanceof EdgeChange
					&& (((EdgeChange)data).getKind() == DifferenceKind.ADD || ((EdgeChange)data).getKind() == DifferenceKind.DELETE);

		}
		return false;
	}
}
