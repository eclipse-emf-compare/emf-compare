/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.extension;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Implementations of this interface can be used in order to tell EMF Compare how to make post treatments at
 * each step of the comparison.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public interface IPostProcessor {

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the match step,
	 * from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the match step.
	 */
	void postMatch(Comparison comparison);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the difference
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the difference step.
	 */
	void postDiff(Comparison comparison);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the requirements
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the requirements step.
	 * @param crossReferencer
	 *            The available cross referencer on the resource set.
	 */
	void postRequirements(Comparison comparison, EcoreUtil.CrossReferencer crossReferencer);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the equivalences
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the equivalences step.
	 * @param crossReferencer
	 *            The available cross referencer on the resource set.
	 */
	void postEquivalences(Comparison comparison, EcoreUtil.CrossReferencer crossReferencer);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the conflicts step,
	 * from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the conflicts step.
	 */
	void postConflicts(Comparison comparison);

}
