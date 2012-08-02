/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.actions.filter;

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * These will be the actual actions displayed in the filter menu. Their sole purpose is to provide a
 * DifferenceKind to the structure viewer's filter.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class FilterAction extends Action {
	/**
	 * The Filter type (ADD, MOVE, REMOVE or CHANGE).
	 */
	private DifferenceKind filterType;

	/**
	 * The Filter that will be applyed by the action.
	 */
	private DifferenceFilter differenceFilter;

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            Will be used as the action's tooltip.
	 * @param filterType
	 *            represents the kind of the choosed filter.
	 * @param differenceFilter
	 *            The filter that this action will need to update.
	 */
	public FilterAction(String text, DifferenceKind filterType, DifferenceFilter differenceFilter) {
		super(text, IAction.AS_CHECK_BOX);
		this.filterType = filterType;
		this.differenceFilter = differenceFilter;
	}

	@Override
	public void run() {

		if (isChecked()) {
			differenceFilter.addFilter(filterType);
		} else {
			differenceFilter.removeFilter(filterType);
		}
	}
}
