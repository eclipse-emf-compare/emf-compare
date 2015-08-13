/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.AbstractDifferenceFilter;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

/**
 * A filter hiding all the model elements of Sirius which are considered technicals and should not be
 * displayed by default in the comparison view.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class SiriusTechnicalElementsFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> PREDICATE_WHEN_SELECTED = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			if (input instanceof TreeNode) {
				EObject data = ((TreeNode)input).getData();
				Set<EObject> affectedElements = Sets.newLinkedHashSet();
				if (data instanceof Diff) {
					affectedElements = affectedEObjects((Diff)data);
				}
				return Iterables.any(affectedElements, new Predicate<EObject>() {
					public boolean apply(EObject element) {
						EClass eClass = element.eClass();
						return eClass != null
								&& (eClass.getEPackage() == ViewpointPackage.eINSTANCE || eClass
										.getEPackage() == DiagramPackage.eINSTANCE);
					}
				});
			}
			return false;
		}
	};

	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return PREDICATE_WHEN_SELECTED;
	}

	/**
	 * return a set containing all the compared EObject affected by the diff being from the left, right or
	 * ancestor version.
	 * 
	 * @param diff
	 *            any difference.
	 * @return a set containing all the known EObject affected by the diff being from the left, right or
	 *         ancestor version.
	 */
	private static Set<EObject> affectedEObjects(Diff diff) {
		Match match = diff.getMatch();
		if (match != null) {
			return matchedEObjects(match);
		}
		return Sets.newLinkedHashSet();
	}

	/**
	 * return a set containing all the known EObject affected by the match being from the left, right or
	 * ancestor version.
	 * 
	 * @param match
	 *            any match..
	 * @return a set containing all the known EObject affected by the match being from the left, right or
	 *         ancestor version.
	 */
	private static Set<EObject> matchedEObjects(Match match) {
		Set<EObject> affectedEObjects = Sets.newLinkedHashSet();
		if (match.getLeft() != null) {
			affectedEObjects.add(match.getLeft());
		}
		if (match.getRight() != null) {
			affectedEObjects.add(match.getRight());
		}
		if (match.getOrigin() != null) {
			affectedEObjects.add(match.getOrigin());
		}
		return affectedEObjects;
	}

}
