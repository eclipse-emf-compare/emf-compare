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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Predicate;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.provider.IMergeViewerItemProviderConfiguration;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;

/**
 * Default implementation of {@link IMergeViewerItemProviderConfiguration}.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class MergeViewerItemProviderConfiguration implements IMergeViewerItemProviderConfiguration {

	private final AdapterFactory adapterFactory;

	private final IDifferenceGroupProvider groupProvider;

	private final Predicate<? super EObject> predicate;

	private final Comparison comparison;

	private final MergeViewerSide side;

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            the {@link AdapterFactory}.
	 * @param groupProvider
	 *            the {@link IDifferenceGroupProvider}.
	 * @param predicate
	 *            the {@link Predicate}.
	 * @param comparison
	 *            the {@link Comparison}.
	 */
	public MergeViewerItemProviderConfiguration(AdapterFactory adapterFactory,
			IDifferenceGroupProvider groupProvider, Predicate<? super EObject> predicate,
			Comparison comparison, MergeViewerSide side) {
		this.adapterFactory = checkNotNull(adapterFactory);
		this.groupProvider = checkNotNull(groupProvider);
		this.predicate = checkNotNull(predicate);
		this.comparison = checkNotNull(comparison);
		this.side = checkNotNull(side);
	}

	/**
	 * {@inheritDoc}
	 */
	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDifferenceGroupProvider getDifferenceGroupProvider() {
		return groupProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	public Predicate<? super EObject> getDifferenceFilterPredicate() {
		return predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Comparison getComparison() {
		return comparison;
	}

	/**
	 * {@inheritDoc}
	 */
	public MergeViewerSide getSide() {
		return side;
	}

}
