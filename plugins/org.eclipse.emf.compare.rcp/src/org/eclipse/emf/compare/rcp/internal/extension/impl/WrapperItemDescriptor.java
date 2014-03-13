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


/**
 * A {@link org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor} that wrap one instance of an item
 * and return the same item each time.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            On factory
 */
public class WrapperItemDescriptor<T> extends AbstractItemDescriptor<T> {

	/** Instance of a factory. */
	private T instance;

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            {@link org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor<T>#getLabel()}
	 * @param description
	 *            {@link org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor<T>#getDescription()}
	 * @param rank
	 *            {@link org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor<T>#getRank()}
	 * @param id
	 *            {@link org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor<T>#getID()}
	 * @param instance
	 *            Instance of the factory
	 */
	public WrapperItemDescriptor(String label, String description, int rank, String id, T instance) {
		super(label, description, rank, id);
		this.instance = instance;
	}

	/**
	 * Create an item.
	 * 
	 * @return a item instance
	 */
	public T getItem() {
		return instance;
	}

}
