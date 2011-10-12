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
package org.eclipse.emf.compare.ui.viewer.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.viewer.AbstractOrderingMenu;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.jface.action.IAction;

/**
 * The menu to select the filters.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public class FiltersMenu extends AbstractOrderingMenu {
	/** The viewer. */
	private ParameterizedStructureMergeViewer mViewer;

	/**
	 * Selected filters.
	 */
	private List<IDifferenceFilter> selectedFilters = new ArrayList<IDifferenceFilter>();

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 *            The viewer.
	 */
	public FiltersMenu(ParameterizedStructureMergeViewer viewer) {
		super(EMFCompareUIMessages.getString("ModelStructureMergeViewer.filtering.tooltip")); //$NON-NLS-1$
		mViewer = viewer;
	}

	/**
	 * Returns the selectedFilters.
	 * 
	 * @since 1.3
	 * @return The selectedFilters.
	 */
	public List<IDifferenceFilter> getSelectedFilters() {
		return selectedFilters;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.AbstractOrderingMenu#doGetMenu()
	 */
	@Override
	protected void doGetMenu() {
		final Iterator<DifferenceFilterDescriptor> descriptors = DifferenceFilterRegistry.INSTANCE
				.getDescriptors().iterator();
		while (descriptors.hasNext()) {
			final DifferenceFilterDescriptor desc = descriptors.next();

			final String preferenceValue = EMFCompareUIPlugin.getDefault().getPreferenceStore()
					.getString(EMFComparePreferenceConstants.PREFERENCES_KEY_DEFAULT_FILTERS);

			final List<DifferenceFilterDescriptor> defaultDescriptors = DifferenceFilterRegistry.INSTANCE
					.getDescriptors(preferenceValue);
			final boolean hasToBeChecked = defaultDescriptors.contains(desc);

			if (hasToBeChecked) {
				selectedFilters.add(desc.getExtension());
			}

			addItemToMenu(desc, hasToBeChecked);
		}
	}

	/**
	 * Add the filtering action in relation to its descriptor.
	 * 
	 * @param desc
	 *            The descriptor.
	 */
	protected void addItemToMenu(DifferenceFilterDescriptor desc) {
		final IAction action = new FilteringAction(desc, mViewer, this);
		addContribution(action);
	}

	/**
	 * Add the filtering action in relation to its descriptor.
	 * 
	 * @param desc
	 *            The descriptor.
	 * @param checked
	 *            To check or not the action.
	 * @since 1.3
	 */
	protected void addItemToMenu(DifferenceFilterDescriptor desc, boolean checked) {
		final IAction action = new FilteringAction(desc, mViewer, checked, this);
		addContribution(action);
	}
}
