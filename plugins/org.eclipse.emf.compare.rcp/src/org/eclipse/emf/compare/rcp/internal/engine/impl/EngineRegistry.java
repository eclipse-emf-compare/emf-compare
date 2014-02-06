/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.engine.impl;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.rcp.internal.engine.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.engine.IItemRegistry;

/**
 * Registry holding {@link IItemDescriptor}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 * @param <T>
 *            one of the engine class
 */
public class EngineRegistry<T> implements IItemRegistry<T> {

	/** Registry of all engine. */
	private final Map<String, IItemDescriptor<T>> registry = new ConcurrentHashMap<String, IItemDescriptor<T>>();

	/**
	 * {@inheritDoc}
	 */
	public IItemDescriptor<T> getHighestRankingDescriptor() {
		List<IItemDescriptor<T>> items = getItemDescriptors();
		if (items.size() > 0) {
			return Collections.max(items);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IItemDescriptor<T>> getItemDescriptors() {
		return Lists.newArrayList(registry.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public IItemDescriptor<T> add(IItemDescriptor<T> engineDescriptor) {
		return registry.put(engineDescriptor.getID(), engineDescriptor);

	}

	/**
	 * {@inheritDoc}
	 */
	public IItemDescriptor<T> remove(String className) {
		return registry.remove(className);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		registry.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return registry.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public IItemDescriptor<T> getItemDescriptor(String qualifiedClassName) {
		return registry.get(qualifiedClassName);
	}

}
