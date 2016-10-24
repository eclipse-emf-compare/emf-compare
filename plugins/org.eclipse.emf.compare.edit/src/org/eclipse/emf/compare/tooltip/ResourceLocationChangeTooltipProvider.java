/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tooltip;

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * This class handle the tooltips computation for Resource Location changes.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class ResourceLocationChangeTooltipProvider extends AbstractTooltipProvider<ResourceLocationChange> {

	/**
	 * The constructor.
	 * 
	 * @param adapterFactory
	 *            The composed adapter factory
	 */
	public ResourceLocationChangeTooltipProvider(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryItemDelegator(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ITooltipLabelProvider#getTooltip(MergeMode)
	 */
	public String getTooltip(MergeMode mode) throws IllegalArgumentException {
		String tooltip = ""; //$NON-NLS-1$
		ResourceLocationChange diff = (ResourceLocationChange)target;
		if (diff.getKind().equals(DifferenceKind.CHANGE) || diff.getKind().equals(DifferenceKind.MOVE)) {
			tooltip = setResourceLocationChangeTooltip(mode, diff, isFromLeft(diff));
		} else {
			throw new IllegalArgumentException();
		}
		return tooltip;
	}

}
