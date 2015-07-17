/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.tooltips;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.internal.merge.MergeMode;
import org.eclipse.emf.compare.provider.ITooltipLabelProvider;
import org.eclipse.emf.compare.uml2.internal.EMFCompareUML2EditMessages;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * This class defines default tooltips for UML.
 *
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 */
public class DefaultUMLTooltipProvider extends AdapterImpl implements ITooltipLabelProvider {

	/**
	 * The adapter factory.
	 */
	protected ComposedAdapterFactory adapterFactory;

	/**
	 * The label provider.
	 */
	protected IItemLabelProvider labelProvider;

	/**
	 * The constructor.
	 * 
	 * @param adapterFactory
	 *            The composed adapter factory
	 */
	public DefaultUMLTooltipProvider(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
		this.labelProvider = new AdapterFactoryItemDelegator(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ITooltipLabelProvider#getTooltip(org.eclipse.emf.compare.internal.merge.MergeMode)
	 */
	public String getTooltip(MergeMode mode) throws IllegalArgumentException {
		String tooltip;
		switch (mode) {
			case LEFT_TO_RIGHT:
				tooltip = EMFCompareUML2EditMessages.getString("merged.to.right.tooltip"); //$NON-NLS-1$
				break;
			case RIGHT_TO_LEFT:
				tooltip = EMFCompareUML2EditMessages.getString("merged.to.left.tooltip"); //$NON-NLS-1$
				break;
			case ACCEPT:
				tooltip = EMFCompareUML2EditMessages.getString("accept.change.tooltip"); //$NON-NLS-1$
				break;
			case REJECT:
				tooltip = EMFCompareUML2EditMessages.getString("reject.change.tooltip"); //$NON-NLS-1$
				break;
			default:
				throw new IllegalArgumentException();
		}
		return tooltip;
	}

}
