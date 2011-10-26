/**
 *  Copyright (c) 2011 Atos.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  Atos - Initial API and implementation
 * 
 */
package org.eclipse.emf.compare.internal;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * Extend the ComposedAdapterFacotry notion in order to introduce a notion of order among the
 * {@link AdapterFactory}.
 * 
 * @author "Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>"
 */
public class ComposedAdapterFactoryOrdered extends ComposedAdapterFactory {
	/**
	 * Allow the user to insert a {@link AdapterFactory} at a specific index.
	 * 
	 * @param adapterFactory
	 *            {@link AdapterFactory}
	 * @param index
	 *            int
	 */
	public void insertAtAdapterFactory(AdapterFactory adapterFactory, int index) {
		if (!adapterFactories.contains(adapterFactory)) {
			adapterFactories.add(index, adapterFactory);
			if (adapterFactory instanceof ComposeableAdapterFactory) {
				((ComposeableAdapterFactory)adapterFactory).setParentAdapterFactory(this);
			}
		}
	}

}
