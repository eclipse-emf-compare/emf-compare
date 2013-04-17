/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.variants.IResourceVariant;

/** Allows access to the underlying IResourceVariant's storage. */
public class ResourceVariantStorageProvider implements IStorageProvider {
	/** Wrapped variant. */
	private final IResourceVariant variant;

	/**
	 * Wraps a resource variant as a storage provider.
	 * 
	 * @param variant
	 *            The wrapped resource variant.
	 */
	public ResourceVariantStorageProvider(IResourceVariant variant) {
		this.variant = variant;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IStorageProvider#getStorage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStorage getStorage(IProgressMonitor monitor) throws CoreException {
		return variant.getStorage(monitor);
	}
}
