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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.team.core.mapping.IResourceMappingMerger;

/**
 * Provides adapters for the {@link EMFModelProvider}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
// Suppressing warnings: interface signatures are raw.
@SuppressWarnings("rawtypes")
public class EMFLogicalModelAdapterFactory implements IAdapterFactory {
	/** {@inheritDoc} */
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IResourceMappingMerger.class) {
			return new EMFResourceMappingMerger();
		}
		return null;
	}

	/** {@inheritDoc} */
	public Class[] getAdapterList() {
		return new Class[] {IResourceMappingMerger.class, };
	}
}
