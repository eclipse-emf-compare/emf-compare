/*******************************************************************************
 * Copyright (c) 2015 EclipseSource GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Borkowski - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.mergeresolution;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.ide.ui.dependency.IDependencyProvider;

/**
 * The registry managing the merge resolution extension point information.
 * 
 * @author Michael Borkowski <mborkowski@eclipsesource.com>
 */
public class MergeResolutionListenerRegistry {

	/** Keeps track of the extensions providing merge resolution listeners. */
	private final Map<String, MergeResolutionListenerDescriptor> registeredDescriptors;

	/**
	 * Constructs and initialized this registry.
	 */
	public MergeResolutionListenerRegistry() {
		registeredDescriptors = new LinkedHashMap<String, MergeResolutionListenerDescriptor>();
	}

	/**
	 * Adds the given {@link MergeResolutionListenerDescriptor} to this registry, using the given
	 * {@code className} as the identifier.
	 * 
	 * @param className
	 *            The identifier for the given {@link MergeResolutionListenerDescriptor}.
	 * @param descriptor
	 *            The {@link MergeResolutionListenerDescriptor} which is to be added to this registry.
	 */
	public void addProvider(String className, MergeResolutionListenerDescriptor descriptor) {
		registeredDescriptors.put(className, descriptor);
	}

	/**
	 * Removes the {@link MergeResolutionListenerDescriptor} and its managed {@link IDependencyProvider}
	 * identified by the given {@code className} from this registry.
	 * 
	 * @param className
	 *            Identifier of the provider we are to remove from this registry.
	 * @return The removed {@link MergeResolutionListenerDescriptor}, if any.
	 */
	public MergeResolutionListenerDescriptor removeProvider(String className) {
		return registeredDescriptors.remove(className);
	}

	/** Clears out all registered listeners from this registry. */
	public void clear() {
		registeredDescriptors.clear();
	}

	/**
	 * Notifies all registered extension point clients of the completed merge resolution.
	 * 
	 * @param comparison
	 *            the comparison now resolved
	 */
	public void mergeResolutionCompleted(Comparison comparison) {
		for (MergeResolutionListenerDescriptor descriptor : registeredDescriptors.values()) {
			descriptor.getMergeResolutionListener().mergeResolutionCompleted(comparison);
		}
	}
}
