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

import org.eclipse.emf.compare.rcp.ui.internal.configuration.EMFCompareConfigurationChangeListener;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.swt.events.DisposeEvent;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractMergeViewer extends ContentViewer implements IMergeViewer {

	private final MergeViewerSide fSide;

	private final IEMFCompareConfiguration compareConfiguration;

	private EMFCompareMergeViewerConfigurationListener configurationListener;

	/**
	 * 
	 */
	public AbstractMergeViewer(MergeViewerSide side, final IEMFCompareConfiguration compareConfiguration) {
		fSide = side;
		this.compareConfiguration = compareConfiguration;
		configurationListener = new EMFCompareMergeViewerConfigurationListener();
		compareConfiguration.addChangeListener(configurationListener);
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
		compareConfiguration.removeChangeListener(configurationListener);
		super.handleDispose(event);
	}

	protected void handleSelectedDifferenceGroupProviderChange() {
		refresh();
	}

	protected void handleAggregatedViewerPredicateChange() {
		refresh();
	}

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class EMFCompareMergeViewerConfigurationListener extends EMFCompareConfigurationChangeListener {
		@Override
		public void selectedDifferenceGroupProviderChange(IDifferenceGroupProvider oldValue,
				IDifferenceGroupProvider newValue) {
			handleSelectedDifferenceGroupProviderChange();
		}
	
		@Override
		public void aggregatedViewerPredicateChange(Predicate<? super EObject> oldValue,
				Predicate<? super EObject> newValue) {
			handleAggregatedViewerPredicateChange();
		}
	}
}
