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
package org.eclipse.emf.compare.uml2.rcp.ui.internal.structuremergeviewer.filters;

import static com.google.common.base.Predicates.instanceOf;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.AbstractDifferenceFilter;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.uml2.internal.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * A filter used by default that to filtered out refined UML differences.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class UMLRefinedElementsFilter extends AbstractDifferenceFilter {

	/**
	 * The predicate use by this filter when it is selected.
	 */
	private static final Predicate<? super EObject> predicateWhenSelected = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			if (input instanceof Diff) {
				Diff diff = (Diff)input;
				return Iterables.any(diff.getRefines(), instanceOf(UMLDiff.class));
			}
			return false;
		}
	};

	/**
	 * The predicate use by this filter when it is unselected.
	 */
	private static final Predicate<? super EObject> predicateWhenUnselected = new Predicate<EObject>() {
		public boolean apply(EObject input) {
			EPackage p = input.eClass().getEPackage();
			if (p != null) {
				return p.getNsURI().startsWith("http://www.eclipse.org/emf/compare/uml2"); //$NON-NLS-1$
			}
			return false;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter#isEnabled(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	@Override
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		if (scope != null) {
			for (String nsURI : scope.getNsURIs()) {
				if (nsURI.matches("http://www\\.eclipse\\.org/uml2/.*/UML")) { //$NON-NLS-1$
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenSelected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenSelected() {
		return predicateWhenSelected;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter#getPredicateWhenUnselected()
	 */
	@Override
	public Predicate<? super EObject> getPredicateWhenUnselected() {
		return predicateWhenUnselected;
	}
}
