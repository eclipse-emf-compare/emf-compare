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

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;

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
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postMatch(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the difference
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the difference step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postDiff(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the requirements
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the requirements step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postRequirements(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the equivalences
	 * step, from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the equivalences step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postEquivalences(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after the conflicts step,
	 * from a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the conflicts step.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postConflicts(Comparison comparison, Monitor monitor);

	/**
	 * This will be called by EMF Compare in order to execute the specified behavior after a comparison, from
	 * a <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison after the all steps.
	 * @param monitor
	 *            The monitor to report progress or to check for cancellation
	 * @since 3.0
	 */
	void postComparison(Comparison comparison, Monitor monitor);

}
