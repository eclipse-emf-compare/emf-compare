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
package org.eclipse.emf.compare.rcp.ui.internal.configuration;

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class ForwardingEMFCompareConfiguration implements IEMFCompareConfiguration {

	public AdapterFactory getAdapterFactory() {
		return delegate().getAdapterFactory();
	}

	public void comparisonChange(Comparison oldValue, Comparison newValue) {
		delegate().comparisonChange(oldValue, newValue);
	}

	public void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
		delegate().editingDomainChange(oldValue, newValue);
	}

	public void selectedDifferenceGroupProviderChange(IDifferenceGroupProvider oldValue,
			IDifferenceGroupProvider newValue) {
		delegate().selectedDifferenceGroupProviderChange(oldValue, newValue);
	}

	public void selectedDifferenceFiltersChange(Set<IDifferenceFilter> oldValue,
			Set<IDifferenceFilter> newValue) {
		delegate().selectedDifferenceFiltersChange(oldValue, newValue);
	}

	public void aggregatedViewerPredicateChange(Predicate<? super EObject> oldValue,
			Predicate<? super EObject> newValue) {
		delegate().aggregatedViewerPredicateChange(oldValue, newValue);
	}

	public void adapterFactoryChange(AdapterFactory oldValue, AdapterFactory newValue) {
		delegate().adapterFactoryChange(oldValue, newValue);
	}

	public ICompareEditingDomain getEditingDomain() {
		return delegate().getEditingDomain();
	}

	public IDifferenceGroupProvider getSelectedGroup() {
		return delegate().getSelectedGroup();
	}

	public Collection<IDifferenceFilter> getSelectedFilters() {
		return delegate().getSelectedFilters();
	}

	public Predicate<? super EObject> getAggregatedPredicate() {
		return delegate().getAggregatedPredicate();
	}

	public Comparison getComparison() {
		return delegate().getComparison();
	}

	public void dispose() {
		delegate().dispose();
	}

	protected abstract IEMFCompareConfiguration delegate();

}
