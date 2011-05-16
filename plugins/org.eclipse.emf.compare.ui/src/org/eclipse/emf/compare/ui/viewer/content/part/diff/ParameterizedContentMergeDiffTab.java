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
package org.eclipse.emf.compare.ui.viewer.content.part.diff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabItem;
import org.eclipse.emf.compare.ui.viewer.structure.ParameterizedStructureContentProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * Represents the tree view under a {@link ModelContentMergeTabFolder}'s diff tab.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @since 1.2
 */
public class ParameterizedContentMergeDiffTab extends ModelContentMergeDiffTab {
	/**
	 * Enhances visibility of the constructor.
	 * 
	 * @param parentComposite
	 *            Parent composite of this tab.
	 * @param side
	 *            Side of this viewer part.
	 * @param parentFolder
	 *            Parent folder of this tab.
	 */
	public ParameterizedContentMergeDiffTab(Composite parentComposite, int side,
			ModelContentMergeTabFolder parentFolder) {
		super(parentComposite, side, parentFolder);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ui.viewer.content.part.diff.ModelContentMergeDiffTab#getVisibleElements()
	 */
	@Override
	public List<ModelContentMergeTabItem> getVisibleElements() {
		final List<ModelContentMergeTabItem> result = new ArrayList<ModelContentMergeTabItem>();
		final Iterator<ModelContentMergeTabItem> elements = super.getVisibleElements().iterator();
		while (elements.hasNext()) {
			final ModelContentMergeTabItem item = elements.next();
			final DiffElement diff = item.getDiff();
			if (!ParameterizedStructureContentProvider.isHidden(diff)) {
				result.add(item);
			}
		}
		return result;
	}
}
