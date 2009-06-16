/*******************************************************************************
 * Copyright (c) 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.internal.service;

import java.util.List;

import org.eclipse.emf.compare.diff.service.DiffEngineDescriptor;
import org.eclipse.emf.compare.diff.service.IDiffEngineSelector;

/**
 * This default implementation of an {@link IDiffEngineSelector} simply selects the first diff engine of the
 * passed list.
 * 
 * @author <a href="laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 1.0
 */
public class DefaultDiffEngineSelector implements IDiffEngineSelector {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.service.IDiffEngineSelector#selectDiffEngine(java.util.List)
	 */
	public DiffEngineDescriptor selectDiffEngine(List<DiffEngineDescriptor> engines) {
		DiffEngineDescriptor engine = null;

		if (!engines.isEmpty()) {
			engine = engines.get(0);
		}

		return engine;
	}
}
