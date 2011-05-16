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

import java.util.Iterator;

import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.viewer.AbstractOrderingMenu;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureMergeViewer;
import org.eclipse.jface.action.IAction;

/**
 * The menu to select the filters.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class FiltersMenu extends AbstractOrderingMenu {
	/** The viewer. */
	private ParameterizedStructureMergeViewer mViewer;

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
			addItemToMenu(desc);
		}
	}

	/**
	 * Add the filtering action in relation to its descriptor.
	 * 
	 * @param desc
	 *            The descriptor.
	 */
	protected void addItemToMenu(DifferenceFilterDescriptor desc) {
		final IAction action = new FilteringAction(desc, mViewer);
		addContribution(action);
	}
}
