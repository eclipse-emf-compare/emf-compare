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
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import com.google.common.collect.ImmutableList;

import java.util.Collection;

import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.jface.viewers.ContentViewer;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractMergeViewer extends ContentViewer implements IMergeViewer {

	private final MergeViewerSide fSide;

	/** The list of active filters. */
	private Collection<IDifferenceFilter> selectedFilters;

	/**
	 * 
	 */
	public AbstractMergeViewer(MergeViewerSide side) {
		fSide = side;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer#getSide()
	 */
	public MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer#getSelectedFilters()
	 */
	public Collection<IDifferenceFilter> getSelectedFilters() {
		if (selectedFilters != null) {
			return selectedFilters;
		}
		return ImmutableList.of();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer#setSelectedFilters(Collection)
	 */
	public void setSelectedFilters(Collection<IDifferenceFilter> filters) {
		selectedFilters = filters;
	}

}
