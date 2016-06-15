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
package org.eclipse.emf.compare.ide.internal.hook;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.compare.ide.hook.IResourceSetHook;

/**
 * Resgistry of {@link IResourceSetHook}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class ResourceSetHookRegistry {

	/**
	 * Transforms a {@link ResourceSetHookDescriptor} into a {@link IResourceSetHook}.
	 */
	private static final Function<ResourceSetHookDescriptor, IResourceSetHook> TO_HOOK = new Function<ResourceSetHookDescriptor, IResourceSetHook>() {

		public IResourceSetHook apply(ResourceSetHookDescriptor input) {
			return input.getHook();
		}
	};

	/**
	 * Registry of {@link IResourceSetHook}.
	 */
	private final Map<String, ResourceSetHookDescriptor> registry = new ConcurrentHashMap<String, ResourceSetHookDescriptor>();

	/**
	 * Gets the registered {@link IResourceSetHook}s.
	 * 
	 * @return unmodifiable {@link Collection} of {@link IResourceSetHook}.
	 */
	public Collection<IResourceSetHook> getResourceSetHooks() {
		return Collections.unmodifiableCollection(Collections2
				.filter(Collections2.transform(registry.values(), TO_HOOK), Predicates.notNull()));
	}

	/**
	 * Adds a new {@link ResourceSetHookDescriptor}.
	 * 
	 * @param id
	 *            of the descriptor.
	 * @param resourceSetHookDescriptor
	 *            {@link ResourceSetHookDescriptor}.
	 */
	void add(String id, ResourceSetHookDescriptor resourceSetHookDescriptor) {
		registry.put(id, resourceSetHookDescriptor);
	}

	/**
	 * Removes the {@link ResourceSetHookDescriptor} that was registered against this id.
	 * 
	 * @param id
	 *            of the descriptor to remove.
	 * @return <code>true</code> if te descriptor as been removed, <code>false</code> otherwise.
	 */
	boolean remove(String id) {
		return registry.remove(id) != null;
	}

}
