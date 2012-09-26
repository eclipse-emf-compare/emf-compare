/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import com.google.common.annotations.Beta;

import java.util.Set;

import org.eclipse.core.resources.IStorage;

/**
 * A Resource Traversal is no more than a set of resources used by the synchronization model to determine
 * which resources to load as part of a given logical model.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@Beta
public class ResourceTraversal {
	/** The set of storages that are part of this traversal. */
	private Set<? extends IStorage> storages;

	/**
	 * Creates our traversal given its set of resources.
	 * 
	 * @param storages
	 *            The set of resources that are part of this traversal.
	 */
	public ResourceTraversal(Set<? extends IStorage> storages) {
		this.storages = storages;
	}

	/**
	 * Returns the set of resources that are part of this traversal.
	 * <p>
	 * Note that this is the original set, and that any modification on the returned {@link Set} will affect
	 * this traversal.
	 * </p>
	 * 
	 * @return The set of resources that are part of this traversal.
	 */
	public Set<? extends IStorage> getStorages() {
		return storages;
	}
}
