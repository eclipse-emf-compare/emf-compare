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


/**
 * IItemDescriptor used for factories. The will be only one instance of each item.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            On factory
 */
public class FactoryDescriptor<T> extends AbstractItemDescriptor<T> {

	/** Instance of a factory. */
	private T instance;

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            {@link EngineDescriptor#label}
	 * @param description
	 *            {@link EngineDescriptor#description}
	 * @param rank
	 *            {@link EngineDescriptor#rank}
	 * @param id
	 *            {@link EngineDescriptor#id}
	 * @param instance
	 *            Instance of the factory
	 */
	public FactoryDescriptor(String label, String description, int rank, String id, T instance) {
		super(label, description, rank, id);
		this.instance = instance;
	}

	/**
	 * Create an engine.
	 * 
	 * @return a engine instance
	 */
	public T getItem() {
		return instance;
	}

}
