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
package org.eclipse.emf.compare.ide.ui.internal.logical.view.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.ide.ui.internal.logical.view.ILogicalModelViewHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This registry implements its own strategy to define the "best" Logical Model Editors Handler to use.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public final class LogicalModelViewHandlerRegistry {
	/** Keeps track of the extensions providing Logical Model Editor Handlers. */
	private final Map<String, LogicalModelViewHandlerDescriptor> registeredDescriptors;

	/**
	 * Initializes our registry.
	 */
	public LogicalModelViewHandlerRegistry() {
		this.registeredDescriptors = new LinkedHashMap<String, LogicalModelViewHandlerDescriptor>();
	}

	/**
	 * Returns a view of the descriptors registered in this registry.
	 * 
	 * @return A view of the descriptors registered in this registry.
	 */
	public List<LogicalModelViewHandlerDescriptor> getRegisteredDescriptors() {
		return new ArrayList<LogicalModelViewHandlerDescriptor>(registeredDescriptors.values());
	}

	/**
	 * Returns a {@link ILogicalModelViewHandler} that handles the given ISelection.
	 * <p>
	 * This will iterate over all the registered handlers, selecting the highest-ranking handler that can
	 * handle the target selection.
	 * </p>
	 * 
	 * @param part
	 *            the {@link IWorkbenchPart} of the editor on which the selection occurs.
	 * @param selection
	 *            the selection.
	 * @return a {@link ILogicalModelViewHandler} that is able to handle the ISelection.
	 */
	public ILogicalModelViewHandler getBestHandlerFor(IWorkbenchPart part, ISelection selection) {
		LogicalModelViewHandlerDescriptor handler = null;
		for (LogicalModelViewHandlerDescriptor candidate : registeredDescriptors.values()) {
			if (handler == null || handler.getRanking() < candidate.getRanking()) {
				if (candidate.getHandler().canHandle(part, selection)) {
					handler = candidate;
				}
			}
		}

		if (handler != null) {
			return handler.getHandler();
		}

		return null;
	}

	/**
	 * Registers a new handler within this registry instance.
	 * 
	 * @param key
	 *            Identifier of this handler.
	 * @param descriptor
	 *            The handler to register.
	 */
	void addHandler(String key, LogicalModelViewHandlerDescriptor descriptor) {
		registeredDescriptors.put(key, descriptor);
	}

	/**
	 * Removes the handler identified by <code>key</code> from this registry.
	 * 
	 * @param key
	 *            Identifier of the handler we are to remove from this registry.
	 * @return The removed handler, if any.
	 */
	LogicalModelViewHandlerDescriptor removeHandler(String key) {
		return registeredDescriptors.remove(key);
	}

	/** Clears out all registered handlers from this registry. */
	public void clear() {
		final Iterator<LogicalModelViewHandlerDescriptor> descriptors = registeredDescriptors.values()
				.iterator();
		while (descriptors.hasNext()) {
			descriptors.next();
			descriptors.remove();
		}
	}
}
