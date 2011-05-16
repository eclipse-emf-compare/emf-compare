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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.viewer.OrderingListener;
import org.eclipse.emf.compare.ui.viewer.content.UpdateCenterCanvasListener;
import org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterRegistry;
import org.eclipse.emf.compare.ui.viewer.filter.FiltersMenu;
import org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupingFacilityRegistry;
import org.eclipse.emf.compare.ui.viewer.group.GroupsMenu;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;

/**
 * A {@link ModelStructureMergeViewer} which manages the group and filter functionalities.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedStructureMergeViewer extends ModelStructureMergeViewer {
	/** The parameterized content provider. */
	private ParameterizedStructureContentProvider mProvider;

	/**
	 * The list of {@link OrderingListener} listeners.
	 */
	private List<OrderingListener> listeners = new ArrayList<OrderingListener>();

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
		listeners.add(new UpdateStructureListener(this));
		listeners.add(new UpdateCenterCanvasListener());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer#createContentProvider(org.eclipse.compare.CompareConfiguration)
	 */
	@Override
	protected ModelStructureContentProvider createContentProvider(CompareConfiguration compareConfiguration) {
		mProvider = new ParameterizedStructureContentProvider(compareConfiguration);
		return mProvider;
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
						.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry("icons/full/filter.png"))); //$NON-NLS-1$
				filtersMenu.setImageDescriptor(imgDesc);
			} catch (IOException e) {
				// No management
			}
			tbm.appendToGroup(orderingGroupName, filtersMenu);
		}

		if (DifferenceGroupingFacilityRegistry.INSTANCE.getDescriptors().size() > 0) {
			final GroupsMenu groupsMenu = new GroupsMenu(this);
			try {
				final ImageDescriptor imgDesc = ImageDescriptor.createFromURL(FileLocator.toFileURL(Platform
						.getBundle(EMFCompareUIPlugin.PLUGIN_ID).getEntry("icons/full/category.png"))); //$NON-NLS-1$
				groupsMenu.setImageDescriptor(imgDesc);
			} catch (IOException e) {
				// No management
			}
			tbm.appendToGroup(orderingGroupName, groupsMenu);
		}
	}

	/**
	 * Notifies the registered ordering listeners.
	 * 
	 * @param event
	 *            An {@link OrderingListener} event.
	 * @param descriptor
	 *            The descriptor linked to the ordering event (
	 *            {@link org.eclipse.emf.compare.ui.viewer.filter.DifferenceFilterDescriptor} or
	 *            {@link org.eclipse.emf.compare.ui.viewer.group.DifferenceGroupingFacilityDescriptor}).
	 */
	public void fireOrderingChanged(int event, Object descriptor) {
		final Iterator<OrderingListener> it = listeners.iterator();
		while (it.hasNext()) {
			final OrderingListener listener = it.next();
			listener.notifyChanged(event, descriptor);
		}
	}

}
