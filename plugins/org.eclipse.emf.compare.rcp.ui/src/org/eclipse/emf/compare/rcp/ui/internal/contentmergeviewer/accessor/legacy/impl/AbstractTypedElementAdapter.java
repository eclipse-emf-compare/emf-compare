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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.impl;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractTypedElementAdapter implements ITypedElement {

	private final AdapterFactory fAdapterFactory;

	/** The item delegator to use to retrieve item */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	public AbstractTypedElementAdapter(AdapterFactory adapterFactory) {
		fAdapterFactory = adapterFactory;
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
	}

	/**
	 * Gets the root factory if this local adapter factory is composed, otherwise just the local one.
	 */
	protected AdapterFactory getRootAdapterFactory() {
		if (fAdapterFactory instanceof ComposeableAdapterFactory) {
			return ((ComposeableAdapterFactory)fAdapterFactory).getRootAdapterFactory();
		}

		return fAdapterFactory;
	}

	/**
	 * @return the fAdapterFactory
	 */
	protected AdapterFactory getAdapterFactory() {
		return fAdapterFactory;
	}

	/**
	 * @return the itemDelegator
	 */
	protected ExtendedAdapterFactoryItemDelegator getItemDelegator() {
		return itemDelegator;
	}
}
