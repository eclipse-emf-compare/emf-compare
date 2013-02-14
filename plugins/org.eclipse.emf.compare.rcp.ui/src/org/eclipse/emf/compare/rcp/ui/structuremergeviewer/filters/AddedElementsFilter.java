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

import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;

/**
 * A filter used by default that filtered out added elements.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class AddedElementsFilter implements IDifferenceFilter {

	/** A human-readable label for this filter. This will be displayed in the EMF Compare UI. */
	private String label;

	/** The initial activation state of the filter. */
	private boolean activeByDefault;

	/** The predicate activate through this filter. */
	private Predicate<? super EObject> predicate;

	/**
	 * Constructs the filter with the appropriate predicate.
	 */
	public AddedElementsFilter() {
		super();
		setPredicate();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#defaultSelected()
	 */
	public boolean defaultSelected() {
		return activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#setDefaultSelected(boolean)
	 */
	public void setDefaultSelected(boolean activeByDefault) {
		this.activeByDefault = activeByDefault;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#isEnabled(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter#getPredicate()
	 */
	public Predicate<? super EObject> getPredicate() {
		return predicate;
	}

	/**
	 * Set the predicate that will be activate through this filter.
	 */
	private void setPredicate() {
		final Predicate<? super EObject> actualPredicate = new Predicate<EObject>() {
			public boolean apply(EObject input) {
				return input instanceof Diff && ofKind(DifferenceKind.ADD).apply((Diff)input);
			}
		};
		predicate = actualPredicate;
	}
}
