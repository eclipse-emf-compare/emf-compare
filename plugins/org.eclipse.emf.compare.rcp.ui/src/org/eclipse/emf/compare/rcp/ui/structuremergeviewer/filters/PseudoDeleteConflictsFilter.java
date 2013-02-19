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
package org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * A filter used by default that filtered out pseudo delete conflicts differences.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class PseudoDeleteConflictsFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> predicateWhenSelected = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			boolean ret = false;
			if (input instanceof ReferenceChange) {
				ReferenceChange referenceChange = (ReferenceChange)input;
				Conflict conflict = referenceChange.getConflict();
				if (conflict != null) {
					EReference eReference = referenceChange.getReference();
					EObject value = referenceChange.getValue();
					Iterable<ReferenceChange> conflictualReferenceChanges = filter(conflict.getDifferences(),
							ReferenceChange.class);
					for (ReferenceChange conflictualReferenceChange : conflictualReferenceChanges) {
						if (conflictualReferenceChange != referenceChange
								&& conflictualReferenceChange.getReference() == eReference
								&& conflictualReferenceChange.getValue() == value) {
							ret = true;
						}
					}
				}
			}
			return ret;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#isEnabled(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	@Override
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		if (comparison != null && comparison.isThreeWay()) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenSelected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return predicateWhenSelected;
	}

}
