/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.util;

import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences.ValidationResult;

/**
 * Helper class for accumulating the validation result.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
class ResultAccumulator {

	/**
	 * Clients should set it to <code>true</code>, if there is a case in which the state before the change is found in
	 * the resolved model elements.
	 */
	boolean before = false;
	/**
	 * Clients should set it to <code>true</code>, if there is a case in which the state after the change is found in the
	 * resolved model elements.
	 */
	boolean after = false;
	/**
	 * Clients should set it to <code>true</code>, if there is a case in which neither state could be found in the
	 * resolved model elements.
	 */
	boolean invalid = false;

	/**
	 * We got the problem that we need to accumulate all status to one single status!
	 * 
	 * @param strict
	 *            If strict is <code>true</code>, then the property is checked for all resolved corresponding elements.
	 *            E.g. in case of an attribute change, the value of the state before must exist for all corresponding
	 *            elements. If <code>strict = false</code>, then just one elements must at least fulfill the
	 *            precondition.
	 * @param before
	 *            Indicates whether elements are found that have the status before the change.
	 * @param after
	 *            Indicates whether elements are found that have the status after the change.
	 * @param invalid
	 *            Indicates whether elements are found that do not have a valid status.
	 * @return An accumulated result.
	 */
	ValidationResult accumulate(boolean strict) {
		if (strict) {
			if (invalid) // strict doesn't allow any invalid state
				return ValidationResult.STATE_INVALID;
			if (before && !after) // perfect, all are before!
				return ValidationResult.STATE_BEFORE;
			if (!before && after) // perfect, all are after!
				return ValidationResult.STATE_AFTER;
			return ValidationResult.STATE_INVALID; // mixtures arn't that good...
		} else {
			if (before)
				return ValidationResult.STATE_BEFORE;
			if (after) // && !before
				return ValidationResult.STATE_AFTER;
			if (invalid) // && !before && !after
				return ValidationResult.STATE_INVALID;
			return ValidationResult.STATE_INVALID; // no state isn't good either!
		}
	}
}