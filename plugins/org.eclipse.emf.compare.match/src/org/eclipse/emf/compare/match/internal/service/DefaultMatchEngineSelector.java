/*******************************************************************************
 * Copyright (c) 2008, 2009 Dimitrios Kolovos and other.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Obeo
 *******************************************************************************/
package org.eclipse.emf.compare.match.internal.service;

import java.util.List;

import org.eclipse.emf.compare.match.service.IMatchEngineSelector;
import org.eclipse.emf.compare.match.service.MatchEngineDescriptor;

/**
 * This class will allow the user to select the matching engine he wants to use for comparison when more than
 * one are defined on a single extension.
 * 
 * @author <a href="dkolovos@cs.york.ac.uk">Dimitrios Kolovos</a>
 * @since 1.0
 */
public class DefaultMatchEngineSelector implements IMatchEngineSelector {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.service.IMatchEngineSelector#selectMatchEngine(java.util.List)
	 */
	public MatchEngineDescriptor selectMatchEngine(List<MatchEngineDescriptor> engines) {
		MatchEngineDescriptor engine = null;

		if (!engines.isEmpty()) {
			engine = engines.get(0);
		}

		return engine;
	}
}
