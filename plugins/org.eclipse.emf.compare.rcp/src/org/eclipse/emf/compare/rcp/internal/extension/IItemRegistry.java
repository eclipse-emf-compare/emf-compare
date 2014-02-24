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
package org.eclipse.emf.compare.rcp.internal.extension;

import java.util.List;

/**
 * Item registry.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            An Item.
 */
public interface IItemRegistry<T> {

	/**
	 * Get a list of all {@link IItemDescriptor} from the registry.
	 * 
	 * @return List of {@link IItemDescriptor}
	 */
	List<IItemDescriptor<T>> getItemDescriptors();

	/**
	 * Get the descriptor with the highest ranking in the registry.
	 * 
	 * @return {@link IItemDescriptor} or null if none
	 */
	IItemDescriptor<T> getHighestRankingDescriptor();

	/**
	 * Return the item descriptor.
	 * 
	 * @param id
	 *            Id of the descriptor.
	 * @return Engine descriptor
	 */
	IItemDescriptor<T> getItemDescriptor(String id);

	/**
	 * Add an item descriptor to the registry.
	 * 
	 * @param itemDescriptor
	 *            The descriptor to add
	 * @return The previous {@link IItemDescriptor} if any.
	 */
	IItemDescriptor<T> add(IItemDescriptor<T> itemDescriptor);

	/**
	 * Remove an {@link IItemDescriptor} using its key.
	 * 
	 * @param id
	 *            Unique identifier of the {@link IItemDescriptor}
	 * @return true if an {@link IItemDescriptor} has been removed
	 */
	IItemDescriptor<T> remove(String id);

	/**
	 * Clear the registry.
	 */
	void clear();

	/**
	 * Get the size of the registry.
	 * 
	 * @return the size of the registry
	 */
	int size();

}
