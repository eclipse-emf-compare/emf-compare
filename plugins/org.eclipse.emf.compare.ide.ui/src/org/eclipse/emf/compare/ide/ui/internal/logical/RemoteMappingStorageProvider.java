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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.RemoteResourceMappingContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProvider;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor.DiffSide;

/**
 * This will use a {@link RemoteResourceMappingContext} in order to fetch the content of the needed file.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
class RemoteMappingStorageProvider implements IStorageProvider {
	/** The context from which to retrieve content. */
	private final RemoteResourceMappingContext context;

	/** Side of the content we need. */
	private final DiffSide side;

	/** Local variant of the content we need. */
	private final IFile file;

	public RemoteMappingStorageProvider(RemoteResourceMappingContext context, DiffSide side, IFile file) {
		this.context = context;
		this.side = side;
		this.file = file;
	}

	/** {@inheritDoc} */
	public IStorage getStorage(IProgressMonitor monitor) throws CoreException {
		final IStorage storage;
		switch (side) {
			case SOURCE:
				storage = file;
				break;
			case REMOTE:
				storage = context.fetchRemoteContents(file, monitor);
				break;
			case ORIGIN:
				storage = context.fetchBaseContents(file, monitor);
				break;
			default:
				storage = null;
				break;
		}
		return storage;
	}
}
