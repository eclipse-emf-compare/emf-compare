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
package org.eclipse.emf.compare.ui.viewer.group;

import java.util.Iterator;

import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.viewer.AbstractOrderingMenu;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer;
import org.eclipse.jface.action.IAction;

/**
 * The menu to select the groups.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.3
 */
public class GroupsMenu extends AbstractOrderingMenu {
	/** The viewer. */
	private ParameterizedStructureMergeViewer mViewer;

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 *            Viewer on which this menu is displayed.
	 */
	public GroupsMenu(ParameterizedStructureMergeViewer viewer) {
		super(EMFCompareUIMessages.getString("ModelStructureMergeViewer.grouping.tooltip")); //$NON-NLS-1$
		mViewer = viewer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.AbstractOrderingMenu#doGetMenu()
	 */
	@Override
	protected void doGetMenu() {
		addDefaultItemToMenu();

		final Iterator<DifferenceGroupingFacilityDescriptor> descriptors = DifferenceGroupingFacilityRegistry.INSTANCE
				.getDescriptors().iterator();
		while (descriptors.hasNext()) {
			final DifferenceGroupingFacilityDescriptor desc = descriptors.next();
			addItemToMenu(desc);
		}
	}

	/**
	 * Add a new grouping item in the menu in relation to its descriptor.
	 * 
	 * @param desc
	 *            Descriptor for the facility we are to add to the menu.
	 */
	protected void addItemToMenu(DifferenceGroupingFacilityDescriptor desc) {
		final IAction action = new GroupingAction(desc, mViewer);
		addContribution(action);
	}

	/**
	 * Is used to add a default grouping item in the menu when no group is checked.
	 */
	protected void addDefaultItemToMenu() {
		final IAction action = new GroupingAction("Default", mViewer); //$NON-NLS-1$
		addContribution(action);
		action.setChecked(true);
	}
}
