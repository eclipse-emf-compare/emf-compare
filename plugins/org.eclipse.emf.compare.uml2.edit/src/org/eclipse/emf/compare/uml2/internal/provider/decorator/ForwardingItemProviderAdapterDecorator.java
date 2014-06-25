/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.decorator;

import org.eclipse.emf.compare.provider.ExtendedItemProviderDecorator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * Item provider adapter decorator that forwards all requests to the decorated adapter factory.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ForwardingItemProviderAdapterDecorator extends ExtendedItemProviderDecorator implements IEditingDomainItemProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            {@link ComposeableAdapterFactory}
	 */
	public ForwardingItemProviderAdapterDecorator(ComposeableAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

}
