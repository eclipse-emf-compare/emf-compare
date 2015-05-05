/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.egit.internal.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.egit.core.storage.GitBlobStorage;
import org.eclipse.emf.compare.ide.utils.IStoragePathProvider;

/**
 * Adapts EGit storages into instances of {@link IStoragePathProvider}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
// Suppress warnings : interface signatures are raw, so we can't generify
@SuppressWarnings({"rawtypes", "unchecked", })
public class EGitAdapterFactory implements IAdapterFactory {
	/** {@inheritDoc} */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof GitBlobStorage && adapterType == IStoragePathProvider.class) {
			return new BlobStoragePathProvider();
		}
		return null;
	}

	/** {@inheritDoc} */
	public Class[] getAdapterList() {
		return new Class[] {IStoragePathProvider.class, };
	}

}
