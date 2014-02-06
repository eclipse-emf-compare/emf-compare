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
package org.eclipse.emf.compare.rcp.internal.engine.impl;

import com.google.common.base.Throwables;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Descriptor for an engine.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            one of the engine
 */
public class EngineDescriptor<T> extends AbstractItemDescriptor<T> {

	/** IConfig element of the engine. */
	private final IConfigurationElement config;

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            {@link EngineDescriptor#label}
	 * @param description
	 *            {@link EngineDescriptor#description}
	 * @param rank
	 *            {@link EngineDescriptor#rank}
	 * @param config
	 *            {@link EngineDescriptor#config}
	 * @param id
	 *            {@link EngineDescriptor#id}
	 */
	public EngineDescriptor(String label, String description, int rank, IConfigurationElement config,
			String id) {
		super(label, description, rank, id);
		this.config = config;
	}

	/**
	 * {@link EngineDescriptor#config}.
	 * 
	 * @return IConfigurationElement.
	 */
	public IConfigurationElement getConfig() {
		return config;
	}

	/**
	 * Create an engine.
	 * 
	 * @return a engine instance
	 */
	public T getItem() {
		T result = null;
		try {
			result = (T)config
					.createExecutableExtension(DescriptorRegistryEventListener.IMPL_CLASS_DESCRIPTPOR_ATTR);
		} catch (CoreException e) {
			Throwables.propagate(e);
		}
		return result;
	}

}
