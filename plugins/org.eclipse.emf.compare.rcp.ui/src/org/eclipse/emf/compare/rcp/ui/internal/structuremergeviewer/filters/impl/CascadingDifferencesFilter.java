/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterators.any;
import static com.google.common.collect.Iterators.transform;

import com.google.common.base.Predicate;

import java.util.Iterator;

import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.AbstractDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * A filter used by default that filtered out cascading differences (differences located under differences,
 * also known as sub differences).
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 4.0
 */
public class CascadingDifferencesFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> PREDICATE_WHEN_SELECTED = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			boolean ret = false;
			if (input instanceof TreeNode) {
				TreeNode treeNode = (TreeNode)input;
				EObject data = treeNode.getData();
				TreeNode parent = treeNode.getParent();
				final EObject parentData;
				if (parent != null) {
					parentData = parent.getData();
				} else {
					parentData = null;
				}
				if (parentData instanceof Diff && !(parentData instanceof ResourceAttachmentChange)
						&& data instanceof Diff) {
					Iterator<EObject> eAllDataContents = transform(treeNode.eAllContents(),
							IDifferenceGroup.TREE_NODE_DATA);
					return CASCADING_DIFF.apply(data) && !any(eAllDataContents, not(CASCADING_DIFF));
				}
			}
			return ret;
		}
	};

	/**
	 * Predicate to know if the given diff is a conflictual diff.
	 */
	private static final Predicate<EObject> IS_NON_CONFLICTUAL_DIFF = new Predicate<EObject>() {
		public boolean apply(EObject eObject) {
			if (eObject instanceof Diff) {
				Conflict conflict = ((Diff)eObject).getConflict();
				return conflict == null || ConflictKind.PSEUDO == conflict.getKind();
			}
			return false;
		}
	};

	/**
	 * Predicate to know if the given diff respects the requirements of a cascading diff.
	 */
	private static final Predicate<EObject> CASCADING_DIFF = and(IS_NON_CONFLICTUAL_DIFF,
			not(instanceOf(ResourceAttachmentChange.class)));

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenSelected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return PREDICATE_WHEN_SELECTED;
	}
}
