/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tooltip;

import org.eclipse.emf.compare.FeatureMapChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This class handle the tooltips computation for Feature Map changes.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class FeatureMapChangeTooltipProvider extends AbstractTooltipProvider<FeatureMapChange> implements ITooltipLabelProvider {

	/**
	 * The constructor.
	 * 
	 * @param adapterFactory
	 *            The composed adapter factory
	 */
	public FeatureMapChangeTooltipProvider(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryItemDelegator(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ITooltipLabelProvider#getTooltip(MergeMode)
	 */
	public String getTooltip(MergeMode mode) throws IllegalArgumentException {
		FeatureMapChange diff = (FeatureMapChange)target;
		boolean isFromLeft = isFromLeft(diff);
		String tooltip;

		switch (diff.getKind()) {
			case DELETE:
				tooltip = setDeleteNonContainmentTooltip(mode, diff, isFromLeft);
				break;
			case ADD:
				tooltip = setAddNonContainmentTooltip(mode, diff, isFromLeft);
				break;
			case MOVE:
				tooltip = setMovePositionTooltip(mode, diff, isFromLeft);
				break;
			default:
				throw new IllegalArgumentException();
		}
		return tooltip;
	}

}
