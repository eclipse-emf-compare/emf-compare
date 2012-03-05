/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.engine.mock;

import java.util.Map;

import org.eclipse.emf.compare.match.engine.IMatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A dummy match engine.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class AEngine implements IMatchEngine {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchEngine#contentMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel contentMatch(EObject leftObject, EObject rightObject, EObject ancestor,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchEngine#contentMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel contentMatch(EObject leftObject, EObject rightRoot, Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot, EObject ancestor,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot, Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchEngine#reset()
	 */
	public void reset() {
		// no reset needed here
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchEngine#resourceMatch(org.eclipse.emf.ecore.resource.Resource,
	 *      org.eclipse.emf.ecore.resource.Resource, java.util.Map)
	 */
	public MatchModel resourceMatch(Resource leftResource, Resource rightResource,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.IMatchEngine#resourceMatch(org.eclipse.emf.ecore.resource.Resource,
	 *      org.eclipse.emf.ecore.resource.Resource, org.eclipse.emf.ecore.resource.Resource, java.util.Map)
	 */
	public MatchModel resourceMatch(Resource leftResource, Resource rightResource, Resource ancestorResource,
			Map<String, Object> optionMap) {
		return null;
	}
}
