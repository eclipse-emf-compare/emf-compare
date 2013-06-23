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
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl;

import static com.google.common.collect.Iterators.any;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * A filter used by default that filtered out identical elements.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class IdenticalElementsFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> predicateWhenSelected = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			if (input instanceof TreeNode) {
				TreeNode treeNode = (TreeNode)input;
				EObject data = treeNode.getData();
				if (data instanceof Match) {
					return !any(treeNode.eAllContents(), DATA_IS_DIFF);
				}
			}
			return false;
		}
	};

	/**
	 * Predicate to know if the given TreeNode is a diff.
	 */
	private static final Predicate<EObject> DATA_IS_DIFF = new Predicate<EObject>() {
		public boolean apply(EObject treeNode) {
			return treeNode instanceof TreeNode && ((TreeNode)treeNode).getData() instanceof Diff;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenSelected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return predicateWhenSelected;
	}

}
