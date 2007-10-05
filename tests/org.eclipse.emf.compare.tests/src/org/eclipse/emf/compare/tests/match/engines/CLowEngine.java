/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match.engines;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.match.api.MatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;

/**
 * A dummy match engine.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class CLowEngine implements MatchEngine {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.MatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot, EObject ancestor,
			IProgressMonitor monitor) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.MatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public MatchModel modelMatch(final EObject leftRoot, final EObject rightRoot, IProgressMonitor monitor) {
		return null;
	}
}
