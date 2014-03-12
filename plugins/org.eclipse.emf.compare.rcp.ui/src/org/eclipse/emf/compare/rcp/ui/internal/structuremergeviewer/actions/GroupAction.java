/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.actions;

import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

/**
 * This action will allow us to group differences by their kind.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
 */
public class GroupAction extends Action {
	/** The viewer grouper that will be affected by this action. */
	private final StructureMergeViewerGrouper structureMergeViewerGrouper;

	/** The actual instance that will provide groups if this action is used. */
	private final IDifferenceGroupProvider provider;

	/**
	 * Instantiates our action given its target grouper.
	 * 
	 * @param structureMergeViewerGrouper
	 *            The grouper to which we'll provide our DifferenceGroupProvider.
	 * @param provider
	 *            The group provider associated with this action.
	 */
	public GroupAction(StructureMergeViewerGrouper structureMergeViewerGrouper,
			IDifferenceGroupProvider provider) {
		super(provider.getLabel(), IAction.AS_RADIO_BUTTON);
		this.structureMergeViewerGrouper = structureMergeViewerGrouper;
		this.provider = provider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		structureMergeViewerGrouper.setProvider(provider);
	}

	@Override
	public void runWithEvent(Event event) {
		Widget widget = event.widget;
		if (widget instanceof MenuItem) {
			MenuItem menuItem = (MenuItem)widget;
			if (menuItem.getSelection()) {
				/*
				 * Only run action if the widget has been selected. In case of the group menu, it is composed
				 * with a combo.A combo send two events for each selection. One for the old selection and one
				 * for the new one. We only want to run this action for the new selection.
				 */
				super.runWithEvent(event);
			}
		}
	}
}
