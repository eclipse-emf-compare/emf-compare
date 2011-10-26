/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.structure;

import java.io.IOException;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.ui.viewer.OrderingListener;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterDescriptor;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterRegistry;
import org.eclipse.emf.compare.ui.viewer.filter.FiltersMenu;
import org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter;
import org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupingFacilityDescriptor;
import org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupingFacilityRegistry;
import org.eclipse.emf.compare.ui.viewer.group.GroupsMenu;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * A {@link ModelStructureMergeViewer} which manages the group and filter functionalities.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedStructureMergeViewer extends ModelStructureMergeViewer {

	/**
	 * The parameterized content provider.
	 * 
	 * @deprecated
	 */
	@Deprecated
	private ParameterizedStructureContentProvider mProvider;

	/**
	 * Listener to react on ordering changes.
	 */
	private IPropertyChangeListener orderingSelectionListener;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            Parent composite of this viewer.
	 * @param compareConfiguration
	 *            Configuration of the underlying comparison.
	 */
	public ParameterizedStructureMergeViewer(Composite parent, CompareConfiguration compareConfiguration) {
		super(parent, compareConfiguration);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer#createContentProvider(org.eclipse.compare.CompareConfiguration)
	 */
	@Override
	protected ModelStructureContentProvider createContentProvider(CompareConfiguration compareConfiguration) {
		final String preferenceValue = EMFCompareUIPlugin.getDefault().getPreferenceStore()
				.getString(EMFComparePreferenceConstants.PREFERENCES_KEY_DEFAULT_FILTERS);
		final ParameterizedStructureContentProvider contentProvider = buildContentProvider(compareConfiguration);
		orderingSelectionListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(EMFCompareConstants.PROPERTY_STRUCTURE_FILTERS)) {
					contentProvider.setSelectedFilters((List<IDifferenceFilter>)event.getNewValue());
				} else if (event.getProperty().equals(EMFCompareConstants.PROPERTY_STRUCTURE_GROUP)) {
					contentProvider.setSelectedGroup((IDifferenceGroupingFacility)event.getNewValue());
				}
			}
		};
		configuration.addPropertyChangeListener(orderingSelectionListener);
		// deprecated:
		mProvider = contentProvider;
		return contentProvider;
	}

	/**
	 * Build the content provider in relation to the compare configuration and the preference values on
	 * filters to apply.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration.
	 * @return The content provider.
	 * @since 1.3
	 */
	protected ParameterizedStructureContentProvider buildContentProvider(
			CompareConfiguration compareConfiguration) {
		final ParameterizedStructureContentProvider contentProvider = new ParameterizedStructureContentProvider(
				compareConfiguration, getDefaultOrdering(), getDefaultFilters());
		return contentProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer#createToolItems()
	 */
	@Override
	protected void createToolItems() {
		super.createToolItems();
		final String orderingGroupName = "ordering"; //$NON-NLS-1$

		final ToolBarManager tbm = CompareViewerPane.getToolBarManager(getControl().getParent());
		tbm.add(new Separator(orderingGroupName));

		if (DifferenceFilterRegistry.INSTANCE.getDescriptors().size() > 0) {
			final FiltersMenu filtersMenu = new FiltersMenu(this);
			try {
				final ImageDescriptor imgDesc = ImageDescriptor.createFromURL(FileLocator.toFileURL(Platform
						.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry("icons/full/filter.gif"))); //$NON-NLS-1$
				filtersMenu.setImageDescriptor(imgDesc);
			} catch (IOException e) {
				// No management
			}
			tbm.appendToGroup(orderingGroupName, filtersMenu);
		}

		if (DifferenceGroupingFacilityRegistry.INSTANCE.getDescriptors().size() > 0) {
			final GroupsMenu groupsMenu = new GroupsMenu(this);
			final ImageDescriptor imgDesc = PlatformUI.getWorkbench().getSharedImages()
					.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
			groupsMenu.setImageDescriptor(imgDesc);
			tbm.appendToGroup(orderingGroupName, groupsMenu);
		}
		tbm.update(true);
	}

	/**
	 * Notifies the registered ordering listeners.
	 * 
	 * @deprecated Use setProperty(String, Object) on org.eclipse.compare.CompareConfiguration instead of it.
	 * @param event
	 *            An {@link OrderingListener} event.
	 * @param descriptor
	 *            The descriptor linked to the ordering event (
	 *            {@link org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterDescriptor} or
	 *            {@link org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupingFacilityDescriptor}).
	 */
	@Deprecated
	public void fireOrderingChanged(int event, Object descriptor) {
		if (mProvider != null) {
			if (event == OrderingListener.ADD_FILTER_EVENT) {
				mProvider.addSelectedFilter(((DifferenceFilterDescriptor)descriptor).getExtension());
			} else if (event == OrderingListener.REMOVE_FILTER_EVENT) {
				mProvider.removeSelectedFilter(((DifferenceFilterDescriptor)descriptor).getExtension());
			} else if (event == OrderingListener.CHANGE_GROUP_EVENT) {
				mProvider.setSelectedGroup(((DifferenceGroupingFacilityDescriptor)descriptor).getExtension());
			}
		}
	}

	/**
	 * Returns the orderingSelectionListener.
	 * 
	 * @return The orderingSelectionListener.
	 * @since 1.3
	 */
	public IPropertyChangeListener getOrderingSelectionListener() {
		return orderingSelectionListener;
	}

	/**
	 * Get the default filters which are applied during opening of the viewer.
	 * 
	 * @return The list of filters to apply.
	 * @since 1.3
	 */
	protected List<IDifferenceFilter> getDefaultFilters() {
		final String preferenceValue = EMFCompareUIPlugin.getDefault().getPreferenceStore()
				.getString(EMFComparePreferenceConstants.PREFERENCES_KEY_DEFAULT_FILTERS);
		return DifferenceFilterRegistry.INSTANCE.getFilters(preferenceValue);
	}

	/**
	 * Get the default ordering to apply during opening of the viewer.
	 * 
	 * @return The kind of ordering.
	 * @since 1.3
	 */
	protected IDifferenceGroupingFacility getDefaultOrdering() {
		return null;
	}

}
