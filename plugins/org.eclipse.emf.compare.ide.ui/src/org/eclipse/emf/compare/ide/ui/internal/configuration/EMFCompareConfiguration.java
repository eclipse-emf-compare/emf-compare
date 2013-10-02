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
package org.eclipse.emf.compare.ide.ui.internal.configuration;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.collect.Sets.newLinkedHashSet;

import com.google.common.base.Predicate;

import java.util.Collection;
import java.util.Set;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareConstants;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * A wrapping compare configuration.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareConfiguration implements IEMFCompareConfiguration, IPropertyChangeListener {

	/** The compare configuration. */
	private final CompareConfiguration compareConfiguration;

	/** The active group provider. */
	private IDifferenceGroupProvider selectedDifferenceGroupProvider;

	/** The list of active filters. */
	private Set<IDifferenceFilter> selectedDifferenceFilters;

	/** The aggregated viewer predicate. */
	private Predicate<? super EObject> aggregatedViewerPredicate;

	/** The editing domain. */
	private ICompareEditingDomain editingDomain;

	/** The comparison. */
	private Comparison comparison;

	/** The adapter factory. */
	private AdapterFactory adapterFactory;

	/**
	 * Creates a new object wrapping the given {@link CompareConfiguration}.
	 * 
	 * @param cc
	 *            the {@link CompareConfiguration} to wrap.
	 */
	public EMFCompareConfiguration(CompareConfiguration cc) {
		this.compareConfiguration = cc;

		editingDomainChange(null, (ICompareEditingDomain)cc.getProperty(EMFCompareConstants.EDITING_DOMAIN));
		selectedDifferenceGroupProviderChange(null, (IDifferenceGroupProvider)cc
				.getProperty(EMFCompareConstants.SELECTED_GROUP));
		selectedDifferenceFiltersChange(null, (Set<IDifferenceFilter>)cc
				.getProperty(EMFCompareConstants.SELECTED_FILTERS));
		aggregatedViewerPredicateChange(null, (Predicate<? super EObject>)cc
				.getProperty(EMFCompareConstants.AGGREGATED_VIEWER_PREDICATE));
		adapterFactoryChange(null, (AdapterFactory)cc
				.getProperty(EMFCompareConstants.COMPOSED_ADAPTER_FACTORY));
		comparisonChange(null, (Comparison)cc.getProperty(EMFCompareConstants.COMPARE_RESULT));

		compareConfiguration.addPropertyChangeListener(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getEditingDomain()
	 */
	public ICompareEditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getSelectedGroup()
	 */
	public IDifferenceGroupProvider getSelectedGroup() {
		return selectedDifferenceGroupProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getSelectedFilters()
	 */
	public Collection<IDifferenceFilter> getSelectedFilters() {
		return selectedDifferenceFilters;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getAggregatedPredicate()
	 */
	public Predicate<? super EObject> getAggregatedPredicate() {
		return aggregatedViewerPredicate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#getComparison()
	 */
	public Comparison getComparison() {
		return comparison;
	}

	/**
	 * @param property
	 */
	public void adapterFactoryChange(AdapterFactory oldValue, AdapterFactory newValue) {
		adapterFactory = newValue;
	}

	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	/**
	 * @param property
	 */
	public void aggregatedViewerPredicateChange(Predicate<? super EObject> oldValue,
			Predicate<? super EObject> newValue) {
		if (newValue != null) {
			aggregatedViewerPredicate = newValue;
		} else {
			aggregatedViewerPredicate = alwaysFalse();
		}

	}

	/**
	 * @param object
	 * @param property
	 */
	public void selectedDifferenceFiltersChange(Set<IDifferenceFilter> oldValue,
			Set<IDifferenceFilter> newValue) {
		if (newValue != null) {
			this.selectedDifferenceFilters = newValue;
		} else {
			this.selectedDifferenceFilters = newLinkedHashSet();
		}
	}

	/**
	 * @param object
	 * @param property
	 */
	public void selectedDifferenceGroupProviderChange(IDifferenceGroupProvider oldValue,
			IDifferenceGroupProvider newValue) {
		if (newValue != null) {
			selectedDifferenceGroupProvider = newValue;
		} else {
			selectedDifferenceGroupProvider = IDifferenceGroupProvider.EMPTY;
		}
	}

	/**
	 * @param oldValue
	 * @param newValue
	 */
	public void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
		editingDomain = newValue;
	}

	/**
	 * @param object
	 * @param property
	 */
	public void comparisonChange(Comparison oldValue, Comparison newValue) {
		comparison = newValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent event) {
		if (EMFCompareConstants.EDITING_DOMAIN.equals(event.getProperty())) {
			ICompareEditingDomain oldValue = (ICompareEditingDomain)event.getOldValue();
			ICompareEditingDomain newValue = (ICompareEditingDomain)event.getNewValue();
			editingDomainChange(oldValue, newValue);
		} else if (EMFCompareConstants.SELECTED_GROUP.equals(event.getProperty())) {
			IDifferenceGroupProvider oldValue = (IDifferenceGroupProvider)event.getOldValue();
			IDifferenceGroupProvider newValue = (IDifferenceGroupProvider)event.getNewValue();
			selectedDifferenceGroupProviderChange(oldValue, newValue);
		} else if (EMFCompareConstants.SELECTED_FILTERS.equals(event.getProperty())) {
			Set<IDifferenceFilter> oldValue = (Set<IDifferenceFilter>)event.getOldValue();
			Set<IDifferenceFilter> newValue = (Set<IDifferenceFilter>)event.getNewValue();
			selectedDifferenceFiltersChange(oldValue, newValue);
		} else if (EMFCompareConstants.AGGREGATED_VIEWER_PREDICATE.equals(event.getProperty())) {
			Predicate<? super EObject> oldValue = (Predicate<? super EObject>)event.getOldValue();
			Predicate<? super EObject> newValue = (Predicate<? super EObject>)event.getNewValue();
			aggregatedViewerPredicateChange(oldValue, newValue);
		} else if (EMFCompareConstants.COMPARE_RESULT.equals(event.getProperty())) {
			Comparison oldValue = (Comparison)event.getOldValue();
			Comparison newValue = (Comparison)event.getNewValue();
			comparisonChange(oldValue, newValue);
		} else if (EMFCompareConstants.COMPOSED_ADAPTER_FACTORY.equals(event.getProperty())) {
			AdapterFactory oldValue = (AdapterFactory)event.getOldValue();
			AdapterFactory newValue = (AdapterFactory)event.getNewValue();
			adapterFactoryChange(oldValue, newValue);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#dispose()
	 */
	public void dispose() {
		compareConfiguration.removePropertyChangeListener(this);

		editingDomainChange(editingDomain, null);
		selectedDifferenceGroupProviderChange(selectedDifferenceGroupProvider, null);
		selectedDifferenceFiltersChange(selectedDifferenceFilters, null);
		aggregatedViewerPredicateChange(aggregatedViewerPredicate, null);
		adapterFactoryChange(adapterFactory, null);
		comparisonChange(comparison, null);

	}
}
