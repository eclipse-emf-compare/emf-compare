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

import java.util.Set;

import org.eclipse.emf.compare.rcp.ui.internal.configuration.ForwardingEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.IEMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ContentViewer;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractMergeViewer extends ContentViewer implements IMergeViewer {

	/**
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class EMFCompareMergeViewerConfiguration extends ForwardingEMFCompareConfiguration {
		/**
		 * 
		 */
		private final IEMFCompareConfiguration compareConfiguration;

		/**
		 * @param compareConfiguration
		 */
		private EMFCompareMergeViewerConfiguration(IEMFCompareConfiguration compareConfiguration) {
			this.compareConfiguration = compareConfiguration;
		}

		@Override
		protected IEMFCompareConfiguration delegate() {
			return compareConfiguration;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.ForwardingEMFCompareConfiguration#selectedDifferenceFiltersChange(java.util.Set,
		 *      java.util.Set)
		 */
		@Override
		public void selectedDifferenceFiltersChange(Set<IDifferenceFilter> oldValue,
				Set<IDifferenceFilter> newValue) {
			super.selectedDifferenceFiltersChange(oldValue, newValue);
			refresh();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.ForwardingEMFCompareConfiguration#selectedDifferenceGroupProviderChange(org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider,
		 *      org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider)
		 */
		@Override
		public void selectedDifferenceGroupProviderChange(IDifferenceGroupProvider oldValue,
				IDifferenceGroupProvider newValue) {
			super.selectedDifferenceGroupProviderChange(oldValue, newValue);
			refresh();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.internal.configuration.ForwardingEMFCompareConfiguration#aggregatedViewerPredicateChange(com.google.common.base.Predicate,
		 *      com.google.common.base.Predicate)
		 */
		@Override
		public void aggregatedViewerPredicateChange(Predicate<? super EObject> oldValue,
				Predicate<? super EObject> newValue) {
			super.aggregatedViewerPredicateChange(oldValue, newValue);
			refresh();
		}

	}

	private final MergeViewerSide fSide;

	private final IEMFCompareConfiguration compareConfiguration;

	/**
	 * 
	 */
	public AbstractMergeViewer(MergeViewerSide side, final IEMFCompareConfiguration compareConfiguration) {
		fSide = side;
		this.compareConfiguration = new EMFCompareMergeViewerConfiguration(compareConfiguration);
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
}
