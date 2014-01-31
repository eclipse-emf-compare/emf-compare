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
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.impl;

import com.google.common.base.Predicate;
import com.google.common.eventbus.Subscribe;

import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.util.SWTUtil;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilterChange;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProviderChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.swt.events.DisposeEvent;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractMergeViewer extends ContentViewer implements IMergeViewer {

	private final MergeViewerSide fSide;

	private final IEMFCompareConfiguration compareConfiguration;

	private Predicate<? super EObject> differenceFilter;

	private IDifferenceGroupProvider differenceGroupProvider;

	/**
	 * 
	 */
	public AbstractMergeViewer(MergeViewerSide side, final IEMFCompareConfiguration compareConfiguration) {
		fSide = side;
		this.compareConfiguration = compareConfiguration;
		getCompareConfiguration().getEventBus().register(this);
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
	 * @return the compareConfiguration
	 */
	protected IEMFCompareConfiguration getCompareConfiguration() {
		return compareConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		getCompareConfiguration().getEventBus().unregister(this);
		super.handleDispose(event);
	}

	@Subscribe
	public void handleDifferenceGroupProviderChange(IDifferenceGroupProviderChange event) {
		differenceGroupProvider = event.getDifferenceGroupProvider();
		SWTUtil.safeRefresh(this, true);
	}

	public IDifferenceGroupProvider getDifferenceGroupProvider() {
		if (differenceGroupProvider == null) {
			return getCompareConfiguration().getStructureMergeViewerGrouper().getProvider();
		} else {
			return differenceGroupProvider;
		}
	}

	@Subscribe
	public void handleDifferenceFilterChange(IDifferenceFilterChange event) {
		differenceFilter = event.getPredicate();
		SWTUtil.safeRefresh(this, true);
	}

	protected final Predicate<? super EObject> getDifferenceFilter() {
		if (differenceFilter == null) {
			return getCompareConfiguration().getStructureMergeViewerFilter().getAggregatedPredicate();
		} else {
			return differenceFilter;
		}
	}
}
