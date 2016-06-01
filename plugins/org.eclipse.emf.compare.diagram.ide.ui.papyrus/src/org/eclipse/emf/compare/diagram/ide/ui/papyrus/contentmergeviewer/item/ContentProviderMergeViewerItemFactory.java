/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.item;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;

/**
 * Factory for {@link ContentProviderMergeViewerItem.Container}.
 * 
 * @author Stefan Dirix
 */
public class ContentProviderMergeViewerItemFactory {

	/**
	 * Creates and returns a new {@link ContentProviderMergeViewerItem.Container} instantiated with the given
	 * parameters.
	 * 
	 * @param comparison
	 *            the {@link Comparison}
	 * @param diff
	 *            the {@link Diff}
	 * @param left
	 *            the left object
	 * @param right
	 *            the right object
	 * @param ancestor
	 *            the ancestor object
	 * @param side
	 *            the {@link MergeViewerSide}
	 * @param adapterFactory
	 *            the {@link AdapterFactory}
	 * @return a new instance of {@link ContentProviderMergeViewerItem.Container}.
	 */
	public IMergeViewerItem.Container createContentProviderMergeViewerItem(Comparison comparison, Diff diff,
			Object left, Object right, Object ancestor, MergeViewerSide side, AdapterFactory adapterFactory) {
		return new ContentProviderMergeViewerItem.Container(comparison, diff, left, right, ancestor, side,
				adapterFactory);
	}

	/**
	 * Creates and returns a new {@link ContentProviderMergeViewerItem.Container} created with the given
	 * parameters.
	 * 
	 * @param comparison
	 *            the {@link Comparison}
	 * @param diff
	 *            the {@link Diff}
	 * @param match
	 *            the {@link Match}
	 * @param side
	 *            the {@link MergeViewerSide}
	 * @param adapterFactory
	 *            the {@link AdapterFactory}
	 * @return a new instance of {@link ContentProviderMergeViewerItem.Container}.
	 */
	public IMergeViewerItem.Container createContentProviderMergeViewerItem(Comparison comparison, Diff diff,
			Match match, MergeViewerSide side, AdapterFactory adapterFactory) {
		return new ContentProviderMergeViewerItem.Container(comparison, diff, match, side, adapterFactory);
	}
}
