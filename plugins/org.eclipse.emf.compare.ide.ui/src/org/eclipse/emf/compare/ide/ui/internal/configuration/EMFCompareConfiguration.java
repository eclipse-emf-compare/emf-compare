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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareConfiguration extends ForwardingCompareConfiguration implements IEMFCompareConfiguration {

	private static final String COMPARE_RESULT = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARE_RESULT"; //$NON-NLS-1$

	private static final String COMPARATOR = EMFCompareIDEUIPlugin.PLUGIN_ID + ".COMPARATOR"; //$NON-NLS-1$

	private static final String EDITING_DOMAIN = EMFCompareIDEUIPlugin.PLUGIN_ID + ".EDITING_DOMAIN"; //$NON-NLS-1$

	private static final String ADAPTER_FACTORY = EMFCompareIDEUIPlugin.PLUGIN_ID + ".ADAPTER_FACTORY"; //$NON-NLS-1$

	private static final String SELECTED_DIFFERENCE_FILTERS = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".SELECTED_DIFFERENCE_FILTERS"; //$NON-NLS-1$

	private static final Set<IDifferenceFilter> SELECTED_DIFFERENCE_FILTERS__DEFAULT_VALUE = ImmutableSet
			.of();

	private static final String AGGREGATED_VIEWER_PREDICATE = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".AGGREGATED_VIEWER_PREDICATE"; //$NON-NLS-1$

	private static final Predicate<? super EObject> AGGREGATED_VIEWER_PREDICATE__DEFAULT_VALUE = alwaysFalse();

	private static final String SELECTED_DIFFERENCE_GROUP_PROVIDER = EMFCompareIDEUIPlugin.PLUGIN_ID
			+ ".SELECTED_DIFFERENCE_GROUP_PROVIDER"; //$NON-NLS-1$

	private static final IDifferenceGroupProvider SELECTED_DIFFERENCE_GROUP_PROVIDER__DEFAULT_VALUE = IDifferenceGroupProvider.EMPTY;

	private static final String PREVIEW_MERGE_MODE = EMFCompareIDEUIPlugin.PLUGIN_ID + ".PREVIEW_MERGE_MODE"; //$NON-NLS-1$

	private final List<IEMFCompareConfigurationChangeListener> listeners;

	private final PropertyChangeListener propertyChangeListener;

	private final CompareConfiguration compareConfiguration;

	public EMFCompareConfiguration(CompareConfiguration compareConfiguration) {
		this.compareConfiguration = compareConfiguration;
		setDefaultValues();
		listeners = new CopyOnWriteArrayList<IEMFCompareConfigurationChangeListener>();
		propertyChangeListener = new PropertyChangeListener();
		compareConfiguration.addPropertyChangeListener(propertyChangeListener);
	}

	public void setDefaultValues() {
		if (compareConfiguration.getProperty(AGGREGATED_VIEWER_PREDICATE) == null) {
			compareConfiguration.setProperty(AGGREGATED_VIEWER_PREDICATE,
					AGGREGATED_VIEWER_PREDICATE__DEFAULT_VALUE);
		}

		if (compareConfiguration.getProperty(SELECTED_DIFFERENCE_FILTERS) == null) {
			compareConfiguration.setProperty(SELECTED_DIFFERENCE_FILTERS,
					SELECTED_DIFFERENCE_FILTERS__DEFAULT_VALUE);
		}

		if (compareConfiguration.getProperty(SELECTED_DIFFERENCE_GROUP_PROVIDER) == null) {
			compareConfiguration.setProperty(SELECTED_DIFFERENCE_GROUP_PROVIDER,
					SELECTED_DIFFERENCE_GROUP_PROVIDER__DEFAULT_VALUE);
		}

		if (compareConfiguration.isLeftEditable() && compareConfiguration.isRightEditable()) {
			compareConfiguration.setProperty(PREVIEW_MERGE_MODE, MergeMode.RIGHT_TO_LEFT);
		} else {
			compareConfiguration.setProperty(PREVIEW_MERGE_MODE, MergeMode.ACCEPT);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.configuration.ForwardingCompareConfiguration#delegate()
	 */
	@Override
	protected CompareConfiguration delegate() {
		return compareConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		compareConfiguration.removePropertyChangeListener(propertyChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#addChangeListener(org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener)
	 */
	public void addChangeListener(IEMFCompareConfigurationChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration#removeChangeListener(org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfigurationChangeListener)
	 */
	public void removeChangeListener(IEMFCompareConfigurationChangeListener listener) {
		listeners.remove(listener);
	}

	public Comparison getComparison() {
		return (Comparison)getProperty(COMPARE_RESULT);
	}

	public void setComparison(Comparison comparison) {
		setProperty(COMPARE_RESULT, comparison);
	}

	public EMFCompare getComparator() {
		return (EMFCompare)getProperty(COMPARATOR);
	}

	public void setComparator(EMFCompare comparator) {
		setProperty(COMPARATOR, comparator);
	}

	public ICompareEditingDomain getEditingDomain() {
		return (ICompareEditingDomain)getProperty(EDITING_DOMAIN);
	}

	public void setEditingDomain(ICompareEditingDomain editingDomain) {
		setProperty(EDITING_DOMAIN, editingDomain);
	}

	@SuppressWarnings("unchecked")
	public Set<IDifferenceFilter> getSelectedDifferenceFilters() {
		return (Set<IDifferenceFilter>)getProperty(SELECTED_DIFFERENCE_FILTERS);
	}

	public void setSelectedDifferenceFilters(Set<IDifferenceFilter> differenceFilters) {
		Preconditions.checkNotNull(differenceFilters);
		setProperty(SELECTED_DIFFERENCE_FILTERS, differenceFilters);
	}

	@SuppressWarnings("unchecked")
	public Predicate<? super EObject> getAggregatedViewerPredicate() {
		return (Predicate<? super EObject>)getProperty(AGGREGATED_VIEWER_PREDICATE);
	}

	public void setAggregatedViewerPredicate(Predicate<? super EObject> predicate) {
		Preconditions.checkNotNull(predicate);
		setProperty(AGGREGATED_VIEWER_PREDICATE, predicate);
	}

	public IDifferenceGroupProvider getSelectedDifferenceGroupProvider() {
		return (IDifferenceGroupProvider)getProperty(SELECTED_DIFFERENCE_GROUP_PROVIDER);
	}

	public void setSelectedDifferenceGroupProvider(IDifferenceGroupProvider groupProvider) {
		Preconditions.checkNotNull(groupProvider);
		setProperty(SELECTED_DIFFERENCE_GROUP_PROVIDER, groupProvider);
	}

	public MergeMode getMergePreviewMode() {
		return (MergeMode)getProperty(PREVIEW_MERGE_MODE);
	}

	public void setMergePreviewMode(MergeMode previewMergeMode) {
		Preconditions.checkNotNull(previewMergeMode);
		setProperty(PREVIEW_MERGE_MODE, previewMergeMode);
	}

	public AdapterFactory getAdapterFactory() {
		return (AdapterFactory)getProperty(ADAPTER_FACTORY);
	}

	public void setAdapterFactory(AdapterFactory adapterFactory) {
		setProperty(ADAPTER_FACTORY, adapterFactory);
	}

	public boolean getBooleanProperty(String key, boolean dflt) {
		final boolean ret;
		Object value = getProperty(key);
		if (value instanceof Boolean) {
			ret = ((Boolean)value).booleanValue();
		} else {
			ret = dflt;
		}
		return ret;
	}

	private class PropertyChangeListener implements IPropertyChangeListener {

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			if (EDITING_DOMAIN.equals(property)) {
				handleEditingDomainChange(event);
			} else if (SELECTED_DIFFERENCE_GROUP_PROVIDER.equals(property)) {
				handleSelectedDifferenceGroupProviderChanger(event);
			} else if (SELECTED_DIFFERENCE_FILTERS.equals(property)) {
				handleSelectedDifferenceFiltersChange(event);
			} else if (AGGREGATED_VIEWER_PREDICATE.equals(property)) {
				handleAggregatedViewerPredicateChange(event);
			} else if (COMPARE_RESULT.equals(property)) {
				handleComparisonChange(event);
			} else if (ADAPTER_FACTORY.equals(property)) {
				handleAdapterFactoryChange(event);
			} else if (COMPARATOR.equals(property)) {
				handleComparatorChange(event);
			} else if (PREVIEW_MERGE_MODE.equals(property)) {
				handlePreviewMergeModeChange(event);
			}
		}

		protected void handleAdapterFactoryChange(PropertyChangeEvent event) {
			AdapterFactory oldValue = (AdapterFactory)event.getOldValue();
			AdapterFactory newValue = (AdapterFactory)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.adapterFactoryChange(oldValue, newValue);
			}
		}

		protected void handleComparisonChange(PropertyChangeEvent event) {
			Comparison oldValue = (Comparison)event.getOldValue();
			Comparison newValue = (Comparison)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.comparisonChange(oldValue, newValue);
			}
		}

		@SuppressWarnings("unchecked")
		protected void handleAggregatedViewerPredicateChange(PropertyChangeEvent event) {
			Predicate<? super EObject> oldValue = (Predicate<? super EObject>)event.getOldValue();
			Predicate<? super EObject> newValue = (Predicate<? super EObject>)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.aggregatedViewerPredicateChange(oldValue, newValue);
			}
		}

		@SuppressWarnings("unchecked")
		protected void handleSelectedDifferenceFiltersChange(PropertyChangeEvent event) {
			Set<IDifferenceFilter> oldValue = (Set<IDifferenceFilter>)event.getOldValue();
			Set<IDifferenceFilter> newValue = (Set<IDifferenceFilter>)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.selectedDifferenceFiltersChange(oldValue, newValue);
			}
		}

		protected void handleSelectedDifferenceGroupProviderChanger(PropertyChangeEvent event) {
			IDifferenceGroupProvider oldValue = (IDifferenceGroupProvider)event.getOldValue();
			IDifferenceGroupProvider newValue = (IDifferenceGroupProvider)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.selectedDifferenceGroupProviderChange(oldValue, newValue);
			}
		}

		protected void handleEditingDomainChange(PropertyChangeEvent event) {
			ICompareEditingDomain oldValue = (ICompareEditingDomain)event.getOldValue();
			ICompareEditingDomain newValue = (ICompareEditingDomain)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.editingDomainChange(oldValue, newValue);
			}
		}

		protected void handleComparatorChange(PropertyChangeEvent event) {
			EMFCompare oldValue = (EMFCompare)event.getOldValue();
			EMFCompare newValue = (EMFCompare)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.comparatorChange(oldValue, newValue);
			}
		}

		protected void handlePreviewMergeModeChange(PropertyChangeEvent event) {
			MergeMode oldValue = (MergeMode)event.getOldValue();
			MergeMode newValue = (MergeMode)event.getNewValue();
			for (IEMFCompareConfigurationChangeListener listener : listeners) {
				listener.mergePreviewModeChange(oldValue, newValue);
			}
		}
	}

}
