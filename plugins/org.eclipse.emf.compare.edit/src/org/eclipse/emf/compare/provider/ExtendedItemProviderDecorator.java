/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.compare.provider.spec.OverlayImageProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProviderDecorator;

/**
 * And extended {@link ItemProviderDecorator} that provides an {@link OverlayImageProvider} and and
 * {@link ExtendedAdapterFactoryItemDelegator}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ExtendedItemProviderDecorator extends ItemProviderDecorator implements Adapter.Internal {

	/** The item delegator to reuse root adapter factory (if any). */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/** The image provider used with this item provider. */
	private final OverlayImageProvider overlayProvider;

	/** The list of target notifier we are installed on. */
	private final List<Notifier> targets;

	/**
	 * An instance is created from an adapter factory. The factory is used as a key so that we always know
	 * which factory created this adapter.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to use to adapt.
	 */
	public ExtendedItemProviderDecorator(ComposeableAdapterFactory adapterFactory) {
		super(adapterFactory);
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(adapterFactory.getRootAdapterFactory());
		overlayProvider = new OverlayImageProvider(EMFCompareEditPlugin.INSTANCE.getPluginResourceLocator());
		targets = new BasicEList.FastCompare<Notifier>();
	}

	/**
	 * Returns the overlay provider.
	 * 
	 * @return the overlayProvider
	 */
	protected OverlayImageProvider getOverlayProvider() {
		return overlayProvider;
	}

	/**
	 * Returns the itemDelegator.
	 * 
	 * @return the itemDelegator
	 */
	protected ExtendedAdapterFactoryItemDelegator getItemDelegator() {
		return itemDelegator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		targets.add(newTarget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter.Internal#unsetTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void unsetTarget(Notifier oldTarget) {
		targets.remove(oldTarget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderDecorator#dispose()
	 */
	@Override
	public void dispose() {
		for (Notifier otherTarget : targets) {
			otherTarget.eAdapters().remove(this);
		}
		targets.clear();

		super.dispose();
	}
}
