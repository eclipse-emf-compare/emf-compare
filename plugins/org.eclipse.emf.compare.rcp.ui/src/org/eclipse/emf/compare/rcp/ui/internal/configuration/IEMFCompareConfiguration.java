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
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IEMFCompareConfiguration {

	/**
	 * @return the fEditingDomain
	 */
	ICompareEditingDomain getEditingDomain();

	void setEditingDomain(ICompareEditingDomain editingDomain);

	/**
	 * Returns the active group.
	 * 
	 * @return the selected group provider.
	 */
	IDifferenceGroupProvider getSelectedDifferenceGroupProvider();

	/**
	 * @param differenceGroupProvider
	 */
	void setSelectedDifferenceGroupProvider(IDifferenceGroupProvider differenceGroupProvider);

	/**
	 * Returns the active filters.
	 * 
	 * @return the selectedFilters.
	 */
	Collection<IDifferenceFilter> getSelectedDifferenceFilters();

	void setSelectedDifferenceFilters(Set<IDifferenceFilter> filters);

	/**
	 * @return the smvPredicate
	 */
	Predicate<? super EObject> getAggregatedViewerPredicate();

	void setAggregatedViewerPredicate(Predicate<? super EObject> predicate);

	AdapterFactory getAdapterFactory();

	void setAdapterFactory(AdapterFactory adapterFactory);

	/**
	 * @return the fComparison
	 */
	Comparison getComparison();

	void setComparison(Comparison comparison);

	EMFCompare getComparator();

	void setComparator(EMFCompare comparator);

	MergeMode getMergePreviewMode();

	void setMergePreviewMode(MergeMode mergePreviewMode);

	/**
	 * Adds a listener for property changes to this notifier. Has no effect if an identical listener is
	 * already registered.
	 * 
	 * @param listener
	 *            a property change listener
	 */
	void addChangeListener(IEMFCompareConfigurationChangeListener listener);

	/**
	 * Removes the given content change listener from this notifier. Has no effect if the identical listener
	 * is not registered.
	 * 
	 * @param listener
	 *            a property change listener
	 */
	void removeChangeListener(IEMFCompareConfigurationChangeListener listener);

	boolean getBooleanProperty(String key, boolean dflt);
}
