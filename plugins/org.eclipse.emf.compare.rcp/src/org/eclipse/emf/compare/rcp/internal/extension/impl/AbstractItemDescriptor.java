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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;

/**
 * Abstract class for an {@link IItemDescriptor}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            item type
 */
public abstract class AbstractItemDescriptor<T> implements IItemDescriptor<T> {

	/** EMPTY_STRING. */
	protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/** Label of the item. */
	private final String label;

	/** Description of the item. */
	private final String description;

	/** Rank of the item. */
	private final int rank;

	/** ID of the item (Use as key for the registry). */
	private final String id;

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            {@link AbstractItemDescriptor#label}
	 * @param description
	 *            {@link AbstractItemDescriptor#description}
	 * @param rank
	 *            {@link AbstractItemDescriptor#rank}
	 * @param id
	 *            {@link AbstractItemDescriptor#id}
	 */
	public AbstractItemDescriptor(String label, String description, int rank, String id) {
		super();
		Preconditions.checkNotNull(id);
		if (label != null) {
			this.label = label;
		} else {
			this.label = id;
		}
		if (description != null) {
			this.description = description;
		} else {
			this.description = EMPTY_STRING;
		}
		this.rank = rank;
		this.id = id;

	}

	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getID() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(IItemDescriptor<T> o) {
		Preconditions.checkNotNull(o);
		int comp = getRank() - o.getRank();
		if (comp == 0) {
			comp = getID().compareTo(o.getID());
		}
		return comp;
	}

	/**
	 * get a {@link Function} to transform a descriptor into a item.
	 * 
	 * @param <T>
	 *            A item type
	 * @return A item
	 */
	public static <T> Function<IItemDescriptor<T>, T> getItemFunction() {
		return new Function<IItemDescriptor<T>, T>() {

			public T apply(IItemDescriptor<T> input) {
				if (input != null) {
					return input.getItem();
				}
				return null;
			}
		};
	}

}
