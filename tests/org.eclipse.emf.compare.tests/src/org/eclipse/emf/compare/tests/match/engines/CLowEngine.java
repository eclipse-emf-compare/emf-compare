/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match.engines;

import java.util.Map;

import org.eclipse.emf.compare.match.api.IMatchEngine;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * A dummy match engine.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class CLowEngine implements IMatchEngine {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#contentMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel contentMatch(EObject leftObject, EObject rightObject, EObject ancestor,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#contentMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel contentMatch(EObject leftObject, EObject rightRoot, Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot, EObject ancestor,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot, Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#resourceMatch(org.eclipse.emf.ecore.resource.Resource,
	 *      org.eclipse.emf.ecore.resource.Resource, java.util.Map)
	 */
	public MatchModel resourceMatch(Resource leftResource, Resource rightResource,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#resourceMatch(org.eclipse.emf.ecore.resource.Resource,
	 *      org.eclipse.emf.ecore.resource.Resource, org.eclipse.emf.ecore.resource.Resource, java.util.Map)
	 */
	public MatchModel resourceMatch(Resource leftResource, Resource rightResource, Resource ancestorResource,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#resourceSetMatch(org.eclipse.emf.ecore.resource.ResourceSet,
	 *      org.eclipse.emf.ecore.resource.ResourceSet, java.util.Map)
	 */
	public MatchModel resourceSetMatch(ResourceSet leftResourceSet, ResourceSet rightResourceSet,
			Map<String, Object> optionMap) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.api.IMatchEngine#resourceSetMatch(org.eclipse.emf.ecore.resource.ResourceSet,
	 *      org.eclipse.emf.ecore.resource.ResourceSet, org.eclipse.emf.ecore.resource.ResourceSet,
	 *      java.util.Map)
	 */
	public MatchModel resourceSetMatch(ResourceSet leftResourceSet, ResourceSet rightResourceSet,
			ResourceSet ancestorResourceSet, Map<String, Object> optionMap) {
		return null;
	}
}
