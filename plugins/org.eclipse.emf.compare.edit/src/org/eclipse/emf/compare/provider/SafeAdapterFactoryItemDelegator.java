/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Service GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider;

import java.util.MissingResourceException;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * A safe {@link AdapterFactoryItemDelegator}.
 * <p>
 * This adapter factory item delegator is safe with respect to {@link MissingResourceException missing image
 * resources} that may occur in the item providers in the adapter factory this delegator delegates to.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class SafeAdapterFactoryItemDelegator extends AdapterFactoryItemDelegator {

	/**
	 * Creates a new safe adapter factory item delegator with the given <code>adapterFactory</code>.
	 * 
	 * @param adapterFactory
	 *            The adapter factory to delegate to.
	 */
	public SafeAdapterFactoryItemDelegator(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc} However, this method catches {@link MissingResourceException} potentially thrown in item
	 * providers of the adapter factories and returns <code>null</code> in case a
	 * {@link MissingResourceException} was caught.
	 */
	@Override
	public Object getImage(Object object) {
		try {
			return super.getImage(object);
		} catch (MissingResourceException ex) {
			// be resilient in regard to missing resources
			EMFCompareEditPlugin.getPlugin().log(ex);
			return null;
		}
	}
}
