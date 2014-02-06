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
package org.eclipse.emf.compare.rcp.internal.engine;

/**
 * An engine descriptor.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            one of the engine
 */
public interface IItemDescriptor<T> extends Comparable<IItemDescriptor<T>> {

	/**
	 * The label of the engine.
	 * 
	 * @return label of the engine
	 */
	String getLabel();

	/**
	 * The description of the engine.
	 * 
	 * @return description of the engine
	 */
	String getDescription();

	/**
	 * Rank of the engine.
	 * 
	 * @return rank
	 */
	int getRank();

	/**
	 * Get a unique key identifying this engine.
	 * 
	 * @return a key
	 */
	String getID();

	/**
	 * Get an instance of an item.
	 * 
	 * @return an instance of an engine
	 */
	T getItem();
}
