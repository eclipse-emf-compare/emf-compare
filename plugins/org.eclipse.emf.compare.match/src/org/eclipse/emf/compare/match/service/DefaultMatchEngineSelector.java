/*******************************************************************************
 * Copyright (c) 2008 Dimitrios Kolovos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.service;

import java.util.Set;

/**
 * This class will allow the user to select the matching engine he wants to use for comparison when more than
 * one are defined on a single extension.
 * 
 * @author <a href="dkolovos@cs.york.ac.uk">Dimitrios Kolovos</a>
 * @since 0.9
 */
public class DefaultMatchEngineSelector implements IMatchEngineSelector {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.service.IMatchEngineSelector#selectMatchEngine(java.util.ArrayList)
	 */
	public EngineDescriptor selectMatchEngine(Set<EngineDescriptor> engines) {
		EngineDescriptor engine = null;

		if (!engines.isEmpty()) {
			engine = engines.iterator().next();
		}

		return engine;
	}
}
