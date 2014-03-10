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
package org.eclipse.emf.compare.rcp.internal.extension.impl;

import com.google.common.base.Throwables;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Descriptor for an item.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            an item type
 */
public class ItemDescriptor<T> extends AbstractItemDescriptor<T> {

	/** IConfig element of the item. */
	private final IConfigurationElement config;

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            {@link ItemDescriptor#label}
	 * @param description
	 *            {@link ItemDescriptor#description}
	 * @param rank
	 *            {@link ItemDescriptor#rank}
	 * @param config
	 *            {@link ItemDescriptor#config}
	 * @param id
	 *            {@link ItemDescriptor#id}
	 */
	public ItemDescriptor(String label, String description, int rank, IConfigurationElement config, String id) {
		super(label, description, rank, id);
		this.config = config;
	}

	/**
	 * {@link ItemDescriptor#config}.
	 * 
	 * @return IConfigurationElement.
	 */
	public IConfigurationElement getConfig() {
		return config;
	}

	/**
	 * Create an item.
	 * 
	 * @return a item instance
	 */
	@SuppressWarnings("unchecked")
	public T getItem() {
		T result = null;
		try {
			result = (T)config
					.createExecutableExtension(DescriptorRegistryEventListener.IMPL_CLASS_DESCRIPTOR_ATTR);
		} catch (CoreException e) {
			Throwables.propagate(e);
		}
		return result;
	}

}
