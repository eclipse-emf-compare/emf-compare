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
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.compare.ide.IAdditiveResourceMappingMerger;
import org.eclipse.team.core.mapping.IResourceMappingMerger;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;

/**
 * Provides adapters for the {@link EMFModelProvider}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
// Suppressing warnings: interface signatures are raw.
@SuppressWarnings({"rawtypes", "unchecked" })
public class EMFLogicalModelAdapterFactory implements IAdapterFactory {
	/** {@inheritDoc} */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		Object adapter = null;
		if (adapterType == IResourceMappingMerger.class) {
			adapter = new EMFResourceMappingMerger();
		} else if (adapterType == IAdditiveResourceMappingMerger.class) {
			adapter = new AdditiveResourceMappingMerger();
		} else if (adapterType == ISynchronizationCompareAdapter.class) {
			adapter = new EMFSynchronizationCompareAdapter();
		}
		return adapter;
	}

	/** {@inheritDoc} */
	public Class[] getAdapterList() {
		return new Class[] {IResourceMappingMerger.class, IAdditiveResourceMappingMerger.class,
				ISynchronizationCompareAdapter.class, };
	}
}
