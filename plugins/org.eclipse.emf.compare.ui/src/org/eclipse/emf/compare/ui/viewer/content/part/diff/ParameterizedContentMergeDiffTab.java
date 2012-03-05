/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.content.part.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.util.OrderingUtils;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabItem;
import org.eclipse.emf.compare.ui.viewer.filter.IDifferenceFilter;
import org.eclipse.swt.widgets.Composite;

/**
 * Represents the tree view under a {@link ModelContentMergeTabFolder}'s diff tab.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ParameterizedContentMergeDiffTab extends ModelContentMergeDiffTab {

	/**
	 * Selected filters.
	 * 
	 * @since 1.3
	 */
	protected List<IDifferenceFilter> selectedFilters = new ArrayList<IDifferenceFilter>();

	/**
	 * Constructor.
	 * 
	 * @param parentComposite
	 *            The container composite.
	 * @param side
	 *            The side of the viewer.
	 * @param parentFolder
	 *            The parent folder.
	 */
	public ParameterizedContentMergeDiffTab(Composite parentComposite, int side,
			ModelContentMergeTabFolder parentFolder) {
		super(parentComposite, side, parentFolder);
	}

	@Override
	public List<ModelContentMergeTabItem> getVisibleElements() {
		final List<ModelContentMergeTabItem> result = new ArrayList<ModelContentMergeTabItem>();
		final Iterator<ModelContentMergeTabItem> elements = super.getVisibleElements().iterator();
		while (elements.hasNext()) {
			final ModelContentMergeTabItem item = elements.next();
			final DiffElement diff = item.getDiff();
			if (!OrderingUtils.isHidden(diff, selectedFilters)) {
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * Sets the value of selectedFilters to selectedFilters.
	 * 
	 * @param pSelectedFilters
	 *            The selectedFilters to set.
	 * @since 1.3
	 */
	public void setSelectedFilters(List<IDifferenceFilter> pSelectedFilters) {
		this.selectedFilters = pSelectedFilters;
	}

}
