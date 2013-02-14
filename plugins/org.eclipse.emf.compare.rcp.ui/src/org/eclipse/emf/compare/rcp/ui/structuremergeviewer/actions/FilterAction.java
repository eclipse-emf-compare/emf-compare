/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.structuremergeviewer.actions;

import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * These will be the actual actions displayed in the filter menu. Their sole purpose is to provide a Predicate
 * to the structure viewer's filter.
 * <p>
 * Do note that each distinct {@link FilterAction} in the {@link FilterActionMenu filter menu} is considered
 * as an "exclude" filter, and that they are OR'ed together (thus, any element must <b>not</b> meet the
 * selected filters' criteria in order to be displayed).
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public class FilterAction extends Action {

	/** The filter associated with this action. */
	private IDifferenceFilter filter;

	/** The Filter that will be modified by the action. */
	private StructureMergeViewerFilter structureMergeViewerFilter;

	/**
	 * The "default" constructor for this action.
	 * 
	 * @param text
	 *            Will be used as the action's tooltip.
	 * @param structureMergeViewerFilter
	 *            The viewer filter that this action will need to update.
	 * @param filter
	 *            The filter associated with this action.
	 */
	public FilterAction(String text, StructureMergeViewerFilter structureMergeViewerFilter, IDifferenceFilter filter) {
		super(text, IAction.AS_CHECK_BOX);
		this.structureMergeViewerFilter = structureMergeViewerFilter;
		this.filter = filter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (isChecked()) {
			structureMergeViewerFilter.addFilter(filter);
		} else {
			structureMergeViewerFilter.removeFilter(filter);
		}
	}
}
