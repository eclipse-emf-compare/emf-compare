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

import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareConfigurationChangeListener implements IEMFCompareConfigurationChangeListener {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#comparisonChange(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	public void comparisonChange(Comparison oldValue, Comparison newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#editingDomainChange(org.eclipse.emf.compare.domain.ICompareEditingDomain,
	 *      org.eclipse.emf.compare.domain.ICompareEditingDomain)
	 */
	public void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#selectedDifferenceGroupProviderChange(org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider,
	 *      org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider)
	 */
	public void selectedDifferenceGroupProviderChange(IDifferenceGroupProvider oldValue,
			IDifferenceGroupProvider newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#selectedDifferenceFiltersChange(java.util.Set,
	 *      java.util.Set)
	 */
	public void selectedDifferenceFiltersChange(Set<IDifferenceFilter> oldValue,
			Set<IDifferenceFilter> newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#aggregatedViewerPredicateChange(com.google.common.base.Predicate,
	 *      com.google.common.base.Predicate)
	 */
	public void aggregatedViewerPredicateChange(Predicate<? super EObject> oldValue,
			Predicate<? super EObject> newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#adapterFactoryChange(org.eclipse.emf.common.notify.AdapterFactory,
	 *      org.eclipse.emf.common.notify.AdapterFactory)
	 */
	public void adapterFactoryChange(AdapterFactory oldValue, AdapterFactory newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#comparatorChange(org.eclipse.emf.compare.EMFCompare,
	 *      org.eclipse.emf.compare.EMFCompare)
	 */
	public void comparatorChange(EMFCompare oldValue, EMFCompare newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#mergePreviewModeChange(org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration.PreviewMergeMode,
	 *      org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration.PreviewMergeMode)
	 */
	public void mergePreviewModeChange(MergeMode oldValue, MergeMode newValue) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener#comparisonScopeChange(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.scope.IComparisonScope)
	 */
	public void comparisonScopeChange(IComparisonScope oldValue, IComparisonScope newValue) {

	}

}
