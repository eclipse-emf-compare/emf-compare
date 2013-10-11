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

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterChange;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class DifferenceFilterChange implements IDifferenceFilterChange {

	private final Predicate<? super EObject> predicate;

	private final Set<IDifferenceFilter> selectedDifferenceFilters;

	private final Set<IDifferenceFilter> unselectedDifferenceFilters;

	/**
	 * Default constructor.
	 */
	public DifferenceFilterChange(Predicate<? super EObject> predicate,
			Set<IDifferenceFilter> selectedDifferenceFilters,
			Set<IDifferenceFilter> unselectedDifferenceFilters) {
		this.predicate = predicate;
		this.selectedDifferenceFilters = selectedDifferenceFilters;
		this.unselectedDifferenceFilters = unselectedDifferenceFilters;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterChange#getPredicate()
	 */
	public Predicate<? super EObject> getPredicate() {
		return predicate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterChange#getSelectedDifferenceFilters()
	 */
	public Collection<IDifferenceFilter> getSelectedDifferenceFilters() {
		return selectedDifferenceFilters;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilterChange#getUnselectedDifferenceFilters()
	 */
	public Collection<IDifferenceFilter> getUnselectedDifferenceFilters() {
		return unselectedDifferenceFilters;
	}

}
